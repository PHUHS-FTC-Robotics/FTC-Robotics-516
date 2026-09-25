package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;

@TeleOp(name = "Demo Drivetrain")
public class Drivetrain extends LinearOpMode {

    Hardware robot = Hardware.getInstance();

    // all constants needed for the code
    int MS_TRANSMISSION_INTERVAL = 100; // be default 250, set to 500ms for collecting live telemetry data
    // now, given this interval, we will find the following:
    // battery voltage, all wheel powers, and a time log
    // these will all be in the form of ArrayLists, which will then be converted into CSV files
    private ArrayList<Double> timeLog = new ArrayList<>();
    private ArrayList<Double> rfPowerLog = new ArrayList<>();
    private ArrayList<Double> lfPowerLog = new ArrayList<>();
    private ArrayList<Double> rbPowerLog = new ArrayList<>();
    private ArrayList<Double> lbPowerLog = new ArrayList<>();
    private ArrayList<Double> batteryLog = new ArrayList<>();
    // for the time log, the following variables will need to be set:
    private ElapsedTime matchTimer = new ElapsedTime();
    private ElapsedTime sampleTimer = new ElapsedTime();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.setMsTransmissionInterval(MS_TRANSMISSION_INTERVAL);

        telemetry.addLine("Ready...");
        telemetry.update();
        waitForStart();
        // reset the timers from above as soon as play is hit:
        matchTimer.reset();
        sampleTimer.reset();

        while (opModeIsActive()) {

            double forward = -gamepad1.left_stick_y;
            double strafe  =  gamepad1.left_stick_x * 1.1;
            double turn    =  gamepad1.right_stick_x;

            drive(strafe, forward, turn);

            // get all variables needed for dataframe safely
            double currentRf = robot.rf.getPower();
            double currentLf = robot.lf.getPower();
            double currentRb = robot.rb.getPower();
            double currentLb = robot.lb.getPower();
            double currentVoltage = robot.getBatteryVoltage();
            // log the data to the dataframe (currently an arraylist)
            if (sampleTimer.milliseconds() >= MS_TRANSMISSION_INTERVAL) {
                timeLog.add(matchTimer.seconds());
                rfPowerLog.add(currentRf);
                lfPowerLog.add(currentLf);
                rbPowerLog.add(currentRb);
                lbPowerLog.add(currentLb);
                batteryLog.add(currentVoltage);

                sampleTimer.reset(); // Reset sample window clock
            }

            // print live data to the driver station
            telemetry.addData("Right front",    robot.rf.getPower());
            telemetry.addData("Left front",    robot.lf.getPower());
            telemetry.addData("Right back",    robot.rb.getPower());
            telemetry.addData("Left back",    robot.lb.getPower());
            telemetry.addData("Battery Voltage", "%.2fV", currentVoltage);
            telemetry.update();
        }
        // when done, all data is automatically exported as a CSV
        exportLogsToCsv();
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
    // exports java arraylist to CSV properly in data dir
    private void exportLogsToCsv() {
        File directory = new File("/sdcard/FIRST/data");
        String dataPurpose = "drivetrain_telemetry_";

        if (!directory.exists()) {
            directory.mkdirs();
        }

        java.util.Date currentDate = new java.util.Date();
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = formatter.format(currentDate);

        File file = new File(directory, "data_" + dataPurpose + timestamp + ".csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            writer.println("time_seconds,rf_power,lf_power,rb_power,lb_power,battery_voltage");

            for (int i = 0; i < timeLog.size(); i++) {
                writer.println(
                        String.format("%.3f", timeLog.get(i)) + "," +
                                rfPowerLog.get(i) + "," +
                                lfPowerLog.get(i) + "," +
                                rbPowerLog.get(i) + "," +
                                lbPowerLog.get(i) + "," +
                                batteryLog.get(i)
                );
            }
            com.qualcomm.robotcore.util.RobotLog.ii("CSV_EXPORT", "Exported directly to project: " + file.getAbsolutePath());
        } catch (IOException e) {
            com.qualcomm.robotcore.util.RobotLog.ee("CSV_EXPORT", "Write Error: " + e.getMessage());
        }
    }
}