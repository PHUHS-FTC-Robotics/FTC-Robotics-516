package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Main TeleOp")
public class MainTeleOp extends LinearOpMode {

    Hardware robot = Hardware.getInstance();

    // Telemetry sampling interval
    int MS_TRANSMISSION_INTERVAL = 100;
    // other constants for shooting and intake
    // currently even numbered balls shoot consistently only, continue tuning
    private static final double TARGET_VELOCITY = 1850;
    private static final double STOPPER_VELOCITY = 400;
    private static final double READY_THRESHOLD = 0.95;
    private static final long READY_TIME_MS = 150;
    private long readyStartTime = 0;
    private boolean flywheelReady = false;

    // Telemetry data manager
    private TelemetryData telemetryData = new TelemetryData();

    // Timers
    private ElapsedTime matchTimer = new ElapsedTime();
    private ElapsedTime sampleTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        telemetry.setMsTransmissionInterval(
                MS_TRANSMISSION_INTERVAL
        );
        telemetry.addLine("Ready...");
        telemetry.update();

        waitForStart();

        // Start timers when play is pressed
        matchTimer.reset();
        sampleTimer.reset();

        while (opModeIsActive()) {
            // Read driver controls
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double turn = gamepad1.right_stick_x;
            // methods for robot
            drive(strafe, forward, turn);
            handleIntake();
            handleShooter(gamepad1.y);
            // Get current robot data
            double currentRf = robot.rf.getPower();
            double currentLf = robot.lf.getPower();
            double currentRb = robot.rb.getPower();
            double currentLb = robot.lb.getPower();
            // the following will be in velocity due to being DcMotorEx
            double currentShoot = robot.shoot.getVelocity();
            double currentStopper = robot.stopper.getVelocity();
            // use simple setPower for intake motor, no need for encoders
            double currentIt = robot.it.getPower();
            double currentVoltage = robot.getBatteryVoltage();
            // Record telemetry sample
            if (sampleTimer.milliseconds() >= MS_TRANSMISSION_INTERVAL) {
                telemetryData.addSample(
                        matchTimer.seconds(),
                        currentRf,
                        currentLf,
                        currentRb,
                        currentLb,
                        currentShoot,
                        currentStopper,
                        currentIt,
                        currentVoltage
                );
                sampleTimer.reset();
            }
            // Display live data
            telemetry.addData(
                    "Right front",
                    currentRf
            );
            telemetry.addData(
                    "Left front",
                    currentLf
            );
            telemetry.addData(
                    "Right back",
                    currentRb
            );
            telemetry.addData(
                    "Left back",
                    currentLb
            );
            telemetry.addData(
                    "Shooter motor",
                    currentShoot
            );
            telemetry.addData(
                    "Stopper motor",
                    currentStopper
            );
            telemetry.addData(
                    "Intake motor",
                    currentIt
            );
            telemetry.addData(
                    "Battery Voltage",
                    "%.2fV",
                    currentVoltage
            );
            telemetry.addData(
                    "Samples",
                    telemetryData.getSampleCount()
            );
            telemetry.update();
        }
        // Export data when OpMode ends
        telemetryData.exportToCsv();
    }
    // drive method
    private void drive(double x, double y, double turn) {
        double denominator = Math.max(
                Math.abs(y) + Math.abs(x) + Math.abs(turn),
                1
        );
        robot.setPower(
                (y + x + turn) / denominator,
                (y - x - turn) / denominator,
                (y - x + turn) / denominator,
                (y + x - turn) / denominator
        );
    }
    // intake method
    private void handleIntake() {
        if (gamepad1.left_bumper) {
            robot.it.setPower(-1);
            robot.stopper.setPower(1);
        } else if (gamepad1.right_bumper) {
            robot.it.setPower(0.8);
        } // emergency buttons if top statements do not work
        else if (gamepad1.dpad_down) {
            robot.stopper.setPower(1);
            robot.stopper.setPower(1);
        } else if (gamepad1.dpad_up){
            robot.stopper.setPower(-1);
        }
    }
    // shooting method
    private void handleShooter(boolean shooting) {
        boolean intakeActive = gamepad1.left_bumper || gamepad1.right_bumper || gamepad1.dpad_up || gamepad1.dpad_down;

        if (!shooting) {
            robot.shoot.setVelocity(0);
            if (!intakeActive) {
                robot.it.setPower(0);
                robot.stopper.setPower(0);
            }
            readyStartTime = 0;
            flywheelReady = false;
            return;
        }
        robot.shoot.setVelocity(TARGET_VELOCITY);
        boolean atSpeed = Math.abs(robot.shoot.getVelocity()) > TARGET_VELOCITY * READY_THRESHOLD;
        if (!flywheelReady) {
            if (atSpeed) {
                if (readyStartTime == 0) readyStartTime = System.currentTimeMillis();
                if (System.currentTimeMillis() - readyStartTime > READY_TIME_MS) {
                    flywheelReady = true;
                }
            } else {
                readyStartTime = 0;
            }
        }

        if (!intakeActive) { // only control intake motor if driver isn't using it
            if (flywheelReady) {
                robot.it.setPower(0.5);
                robot.stopper.setVelocity(-STOPPER_VELOCITY);
            } else {
                robot.it.setPower(0);
                robot.stopper.setVelocity(0);
            }
        }
    }
}