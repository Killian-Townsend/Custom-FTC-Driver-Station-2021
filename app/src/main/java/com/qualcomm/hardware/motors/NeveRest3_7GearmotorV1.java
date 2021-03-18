package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "AndyMark", model = "am-3461")
@ModernRoboticsMotorControllerParams(D = 192, I = 40, P = 128, ratio = 57)
@DeviceProperties(builtIn = true, name = "NeveRest 3.7 v1 Gearmotor", xmlTag = "NeveRest3.7v1Gearmotor")
@MotorType(gearing = 3.7D, maxRPM = 1784.0D, orientation = Rotation.CCW, ticksPerRev = 44.4D)
public interface NeveRest3_7GearmotorV1 {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\NeveRest3_7GearmotorV1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */