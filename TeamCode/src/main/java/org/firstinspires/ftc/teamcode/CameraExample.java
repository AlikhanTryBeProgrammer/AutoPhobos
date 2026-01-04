package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.mechanics.AprilTagCamera;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@Autonomous
public class CameraExample extends OpMode {
    AprilTagCamera cam = new AprilTagCamera();

    @Override
    public void init() {
        cam.init(hardwareMap, telemetry);

    }

    @Override
    public void loop() {


        cam.update();
        AprilTagDetection id20 = cam.getTagBySpecifiedId(20);
//        telemetry.addData("id20 string", id20.toString());


    }
}