package org.firstinspires.ftc.teamcode;

// these imports are needed in order to write the csv files for python to read
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CsvTest {

    public static void main(String[] args) {

        ArrayList<Integer> timeSeconds = new ArrayList<>();
        ArrayList<Double> batteryVoltages = new ArrayList<>();
        ArrayList<Double> motorPower = new ArrayList<>();

        timeSeconds.add(0);
        batteryVoltages.add(12.6);
        motorPower.add(0.20);

        timeSeconds.add(1);
        batteryVoltages.add(12.5);
        motorPower.add(0.45);

        timeSeconds.add(2);
        batteryVoltages.add(12.4);
        motorPower.add(0.70);

        timeSeconds.add(3);
        batteryVoltages.add(12.3);
        motorPower.add(0.85);

        timeSeconds.add(4);
        batteryVoltages.add(12.2);
        motorPower.add(0.60);

        // makes the csv file, otherwise creates error message
        try (PrintWriter writer = new PrintWriter(new FileWriter("data/dummy_telemetry.csv"))) {

            writer.println("time_seconds,battery_voltage,motor_power");

            for (int i = 0; i < timeSeconds.size(); i++) {
                writer.println(
                        timeSeconds.get(i) + "," +
                                batteryVoltages.get(i) + "," +
                                motorPower.get(i)
                );
            }

            System.out.println("CSV file created successfully.");

        } catch (IOException e) {
            System.out.println("Error writing CSV:");
            e.printStackTrace();
        }
    }
}