package org.firstinspires.ftc.teamcode.auto;

import static com.pedropathing.api.Paths.line;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous(name = "Blue Goal Auto")
public class BlueGoalAuto extends OpMode {

    private final Hardware robot = Hardware.getInstance();

    // shooting constants which are the same as TeleOp
    private static final double TARGET_VELOCITY  = 1900;
    private static final double STOPPER_VELOCITY = 250;
    private static final double READY_THRESHOLD  = 0.95;
    private static final double READY_TIME_SEC   = 0.15;
    private static final double FEED_TIME_SEC    = 10.0;   // how long to feed balls once ready
    private static final double INTAKE_FEED_PWR  = 0.5;

    // poses
    private final PoseFactory p = PoseFactory.degrees();

    private final Pose startPose = p.of(85, 8,   90);
    private final Pose shootPose = p.of(85,   15,  90); // aim the shooter at the goal
    private final Pose rowPose   = p.of(124,   55,  90);
    private final Pose endPose   = p.of(132,   100, 90);

    private Follower follower;
    private final ElapsedTime stateTimer = new ElapsedTime();

    private boolean pathStarted   = false;
    private boolean flywheelReady = false;
    private double  readyStart    = -1;

    public enum AutoState { DRIVE_TO_SHOOT, SHOOT, DRIVE_TO_ROW, DRIVE_TO_END, IDLE }
    private AutoState state;

    // all paths here
    private Path toShoot() { return line(startPose, shootPose).linear(startPose, shootPose); }
    private Path toRow()   { return line(shootPose, rowPose).linear(shootPose, rowPose); }
    private Path toEnd()   { return line(rowPose, endPose).linear(rowPose, endPose); }

    private boolean shoot() {
        robot.shoot.setVelocity(TARGET_VELOCITY);

        boolean atSpeed = Math.abs(robot.shoot.getVelocity()) > TARGET_VELOCITY * READY_THRESHOLD;

        if (!flywheelReady) {
            if (atSpeed) {
                if (readyStart < 0) readyStart = stateTimer.seconds();
                if (stateTimer.seconds() - readyStart > READY_TIME_SEC) {
                    flywheelReady = true;
                    stateTimer.reset();   // start feed timer
                }
            } else {
                readyStart = -1;
            }
            return false;
        }

        robot.it.setPower(INTAKE_FEED_PWR);
        robot.stopper.setVelocity(-STOPPER_VELOCITY);

        if (stateTimer.seconds() > FEED_TIME_SEC) {
            robot.shoot.setVelocity(0);
            robot.it.setPower(0);
            robot.stopper.setVelocity(0);
            return true;
        }
        return false;
    }

    private void setState(AutoState newState) {
        state = newState;
        stateTimer.reset();
        pathStarted = false;
    }

    private void stateUpdate() {
        switch (state) {

            case DRIVE_TO_SHOOT:
                if (!pathStarted) {
                    follower.follow(toShoot());
                    pathStarted = true;
                } else if (!follower.isBusy()) {
                    flywheelReady = false;
                    readyStart = -1;
                    setState(AutoState.SHOOT);
                }
                break;

            case SHOOT:
                if (shoot()) setState(AutoState.DRIVE_TO_ROW);
                break;

            case DRIVE_TO_ROW:
                if (!pathStarted) {
                    follower.follow(toRow());
                    pathStarted = true;
                } else if (!follower.isBusy()) {
                    setState(AutoState.DRIVE_TO_END);
                }
                break;

            case DRIVE_TO_END:
                if (!pathStarted) {
                    follower.follow(toEnd());
                    pathStarted = true;
                } else if (!follower.isBusy()) {
                    setState(AutoState.IDLE);
                }
                break;

            case IDLE:
                robot.shoot.setVelocity(0);
                robot.it.setPower(0);
                robot.stopper.setVelocity(0);
                break;
        }
    }

    @Override
    public void init() {
        robot.init(hardwareMap);
        follower = Constants.create(hardwareMap);
        follower.setPose(startPose);
        follower.update();
        state = AutoState.DRIVE_TO_SHOOT;
    }

    @Override
    public void start() {
        stateTimer.reset();
    }

    @Override
    public void loop() {
        follower.update();
        stateUpdate();
        telemetry.addData("State", state);
        telemetry.addData("Follower mode", follower.mode());
        telemetry.addData("Shooter vel", String.format("%.0f", robot.shoot.getVelocity()));
        telemetry.addData("X", String.format("%.1f", follower.pose().x()));
        telemetry.addData("Y", String.format("%.1f", follower.pose().y()));
        telemetry.addData("Heading", String.format("%.1f", Math.toDegrees(follower.pose().heading())));
        telemetry.update();
    }
}