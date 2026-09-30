package org.firstinspires.ftc.teamcode;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class TelemetryData {

    // Data storage
    private ArrayList<Double> timeLog = new ArrayList<>();
    private ArrayList<Double> rfPowerLog = new ArrayList<>();
    private ArrayList<Double> lfPowerLog = new ArrayList<>();
    private ArrayList<Double> rbPowerLog = new ArrayList<>();
    private ArrayList<Double> lbPowerLog = new ArrayList<>();
    private ArrayList<Double> batteryLog = new ArrayList<>();

    // Add one telemetry sample
    public void addSample(
            double time,
            double rfPower,
            double lfPower,
            double rbPower,
            double lbPower,
            double batteryVoltage
    ) {
        timeLog.add(time);
        rfPowerLog.add(rfPower);
        lfPowerLog.add(lfPower);
        rbPowerLog.add(rbPower);
        lbPowerLog.add(lbPower);
        batteryLog.add(batteryVoltage);
    }

    // Number of samples currently stored
    public int getSampleCount() {
        return timeLog.size();
    }

    // Export all stored data to CSV
    public void exportToCsv() {

        File directory = new File("/sdcard/FIRST/data");
        String dataPurpose = "drivetrain_telemetry_";

        if (!directory.exists()) {
            directory.mkdirs();
        }

        Date currentDate = new Date();
        SimpleDateFormat formatter =
                new SimpleDateFormat("yyyyMMdd_HHmmss");

        String timestamp = formatter.format(currentDate);

        File file = new File(
                directory,
                "data_" + dataPurpose + timestamp + ".csv"
        );

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {

            writer.println(
                    "time_seconds,rf_power,lf_power,rb_power,lb_power,battery_voltage"
            );

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

            com.qualcomm.robotcore.util.RobotLog.ii(
                    "CSV_EXPORT",
                    "Exported: " + file.getAbsolutePath()
            );

        } catch (IOException e) {

            com.qualcomm.robotcore.util.RobotLog.ee(
                    "CSV_EXPORT",
                    "Write Error: " + e.getMessage()
            );
        }
    }
}