package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Demo Drivetrain")
public class Drivetrain extends LinearOpMode {

    Hardware robot = Hardware.getInstance();

    // Telemetry sampling interval
    int MS_TRANSMISSION_INTERVAL = 100;

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

        // Start timers when PLAY is pressed
        matchTimer.reset();
        sampleTimer.reset();

        while (opModeIsActive()) {
            // Read driver controls
            double forward = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x * 1.1;
            double turn = gamepad1.right_stick_x;
            // Drive robot
            drive(strafe, forward, turn);
            // Get current robot data
            double currentRf = robot.rf.getPower();
            double currentLf = robot.lf.getPower();
            double currentRb = robot.rb.getPower();
            double currentLb = robot.lb.getPower();
            double currentVoltage = robot.getBatteryVoltage();
            // Record telemetry sample
            if (sampleTimer.milliseconds() >= MS_TRANSMISSION_INTERVAL) {
                telemetryData.addSample(
                        matchTimer.seconds(),
                        currentRf,
                        currentLf,
                        currentRb,
                        currentLb,
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
}