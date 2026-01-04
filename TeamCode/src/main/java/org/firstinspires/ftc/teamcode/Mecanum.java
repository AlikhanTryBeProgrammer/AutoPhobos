package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;



@TeleOp
public class Mecanum extends OpMode {


    DcMotor frontLeft, frontRight, backLeft, backRight, intake, shooterLeft, shooterRight, transfer;

    Servo ax1, ax2;

    boolean shooterOn = false;
    boolean lastX = false;

    boolean lastDown = false;


    @Override
    public void init() {
        frontLeft  = hardwareMap.get(DcMotor.class, "fl");
        frontRight = hardwareMap.get(DcMotor.class, "fr");
        backLeft   = hardwareMap.get(DcMotor.class, "bl");
        backRight  = hardwareMap.get(DcMotor.class, "br");
        shooterLeft  = hardwareMap.get(DcMotor.class, "shooterL");
        shooterRight = hardwareMap.get(DcMotor.class, "shooterR");

        ax1 = hardwareMap.get(Servo.class, "ax1");
        ax2 = hardwareMap.get(Servo.class, "ax2");



        ax1.setDirection(Servo.Direction.FORWARD);
        ax2.setDirection(Servo.Direction.FORWARD);


        shooterLeft.setDirection(DcMotor.Direction.REVERSE);
        shooterRight.setDirection(DcMotor.Direction.REVERSE);
        shooterLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        shooterRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        // Optional but recommended
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        intake = hardwareMap.get(DcMotor.class, "intake");
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transfer = hardwareMap.get(DcMotor.class, "transfer");
        transfer.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    @Override
    public void loop() {
        // Left stick: move & strafe, right stick X: rotate
        double y  = gamepad1.left_stick_y;          // forward/back
        double x  = -gamepad1.left_stick_x * 1.1;     // strafe
        double rx = -gamepad1.right_stick_x;          // rotation
        boolean xPressed = gamepad1.x;
        boolean downPressed = gamepad1.a;
        boolean bPressed = gamepad1.b;

        double frontLeftPower  = y + x + rx;
        double backLeftPower   = y - x + rx;
        double frontRightPower = y - x - rx;
        double backRightPower  = y + x - rx;

        // Normalize, so nothing exceeds |1|
        double max = Math.max(1.0,
                Math.max(Math.abs(frontLeftPower),
                        Math.max(Math.abs(backLeftPower),
                                Math.max(Math.abs(frontRightPower),
                                        Math.abs(backRightPower)))));

        frontLeft.setPower((frontLeftPower * frontLeftPower * frontLeftPower) / max);
        backLeft.setPower((backLeftPower * backLeftPower * backLeftPower) / max);
        frontRight.setPower((frontRightPower * frontRightPower * frontRightPower) / max);
        backRight.setPower(Math.pow(backRightPower, 3) / max);

        telemetry.addData("FL", frontLeft.getPower());
        telemetry.addData("FR", frontRight.getPower());
        telemetry.addData("BL", backLeft.getPower());
        telemetry.addData("BR", backRight.getPower());


        if (gamepad1.right_bumper) {
            intake.setPower(-1.0);      // R1 → вперёд
        } else if (gamepad1.right_trigger > 0.3) {
            intake.setPower(-1.0);
            transfer.setPower(-1.0);
        }else if (gamepad1.left_trigger > 0.3){
            intake.setPower(1.0);
        } else {
            intake.setPower(0);
            transfer.setPower(0);// ничего не нажато → стоп
        }




        if (xPressed && !lastX) {
            shooterOn = !shooterOn;
        }

        lastX = xPressed;

        if (shooterOn) {
            shooterLeft.setPower(0.80);
            shooterRight.setPower(0.80);
        } else {
            shooterLeft.setPower(0.0);
            shooterRight.setPower(0.0);
        }


        double ax1Pos = ax1.getPosition();
        if (gamepad1.dpad_up) {
            ax1Pos += 0.01; // увеличиваем позицию плавно
        }
        if (gamepad1.dpad_down) {
            ax1Pos -= 0.01; // уменьшаем позицию плавно
        }
        ax1Pos = Math.max(0, Math.min(1, ax1Pos)); // ограничиваем от 0 до 1
        ax1.setPosition(ax1Pos);


        if (gamepad1.dpad_right) {
            ax2.setPosition(1.0);
        }
        if (gamepad1.dpad_down) {
            ax2.setPosition(0.0);
        }

        lastDown = downPressed;


        telemetry.addData("Right Trigger", gamepad1.right_trigger);
        telemetry.addData("Right Bumper", gamepad1.right_bumper);
        telemetry.addData("Intake Power", intake.getPower());

        telemetry.addData("ax1", gamepad1.dpad_up);
        telemetry.addData("ax1 artka", gamepad1.dpad_left);
        telemetry.addData("ax2", gamepad1.dpad_right);
        telemetry.addData("ax2 artka", gamepad1.dpad_down);

        telemetry.update();

    }
}