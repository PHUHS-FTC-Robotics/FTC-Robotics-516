package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    // this is hardware updated with driver hub
    public DcMotor rf; // cm0
    public DcMotor lf; // cm1
    public DcMotor rb; // cm2
    public DcMotor lb; // cm3
    public DcMotorEx shoot; // em0
    public DcMotorEx stopper; // em1
    public DcMotor it; // em2
    // this will be used for any pandas dataframes
    public VoltageSensor voltageSensor;

    public static double maxSpeed = 1;
    private static Hardware myInstance = null;
    public static Hardware getInstance(){
        if(myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }
    public void init(HardwareMap hwMap){

        // motors for wheels

        rf = hwMap.get(DcMotor.class, "rf");
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setPower(0);

        lf = hwMap.get(DcMotor.class, "lf");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lf.setPower(0);

        rb = hwMap.get(DcMotor.class, "rb");
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setPower(0);

        lb = hwMap.get(DcMotor.class, "lb");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setPower(0);

        shoot = hwMap.get(DcMotorEx.class, "shoot");
        shoot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shoot.setDirection(DcMotorSimple.Direction.REVERSE);
        shoot.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoot.setPower(0);

        stopper = hwMap.get(DcMotorEx.class, "stopper");
        stopper.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        stopper.setDirection(DcMotorSimple.Direction.REVERSE);
        stopper.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        stopper.setPower(0);

        it = hwMap.get(DcMotor.class, "it");
        it.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        it.setDirection(DcMotorSimple.Direction.REVERSE);
        it.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        it.setPower(0);

        // grabs first available voltage sensor
        if (hwMap.voltageSensor.iterator().hasNext()) {
            voltageSensor = hwMap.voltageSensor.iterator().next();
        }
    }
    public void setPower(double fl, double fr, double bl, double br){
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
    }
    // helper function to get battery voltage without crashing everything
    public double getBatteryVoltage() {
        if (voltageSensor != null) {
            return voltageSensor.getVoltage();
        }
        return 0.0; // return zero if hardware link fails
    }
}