package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

public class Hardware {

    // the following hardware is meant for Mr. Olson's demo robot
    public DcMotor rf; // cm0
    public DcMotor lf; // cm1
    public DcMotor rb; // cm2
    public DcMotor lb; // cm3

    public static double maxSpeed = 1;
    // slightly lowered speed to prevent feeling of being overly reactive
    private static Hardware myInstance = null;
    public static Hardware getInstance(){
        if(myInstance == null) {
            myInstance = new Hardware();
        }
        return myInstance;
    }
    public void init(HardwareMap hwMap){

        // motors for wheels
        // in this order, left front, right front, right back, left back

        lf = hwMap.get(DcMotor.class, "cm1");
        lf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lf.setPower(0);

        rf = hwMap.get(DcMotor.class, "cm0");
        rf.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setPower(0);

        rb = hwMap.get(DcMotor.class, "cm2");
        rb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setPower(0);

        lb = hwMap.get(DcMotor.class, "cm3");
        lb.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setPower(0);
    }
    public void setPower(double fl, double fr, double bl, double br){
        lf.setPower(Range.clip(fl, -maxSpeed, maxSpeed));
        rf.setPower(Range.clip(fr, -maxSpeed, maxSpeed));
        lb.setPower(Range.clip(bl, -maxSpeed, maxSpeed));
        rb.setPower(Range.clip(br, -maxSpeed, maxSpeed));
    }
}