package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Demo Drivetrain")
public class Drivetrain extends LinearOpMode {

    Hardware robot = Hardware.getInstance();
    private Follower follower;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addLine("Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            follower.update();

            double forward = -gamepad1.left_stick_y;
            double strafe  =  gamepad1.left_stick_x * 1.1;
            double turn    =  gamepad1.right_stick_x;

            drive(strafe, forward, turn);

            // testing data to import into an arraylist into csv into pandas df
            telemetry.addData("Right front",    robot.rf.getPower());
            telemetry.addData("Left front",    robot.lf.getPower());
            telemetry.addData("Right back",    robot.rb.getPower());
            telemetry.addData("Left back",    robot.lb.getPower());
            telemetry.update();
        }
    }

    private void drive(double x, double y, double turn) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(turn), 1);
        robot.setPower(
                (y + x + turn) / denominator,
                (y - x - turn) / denominator,
                (y - x + turn) / denominator,
                (y + x - turn) / denominator
        );
    }
}