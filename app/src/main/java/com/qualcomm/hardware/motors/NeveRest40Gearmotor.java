package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "AndyMark", model = "am-2964a", url = "http://www.andymark.com/NeveRest-40-Gearmotor-p/am-2964a.htm")
@ModernRoboticsMotorControllerParams(D = 112, I = 32, P = 160, ratio = 25)
@DeviceProperties(builtIn = true, name = "NeveRest 40 Gearmotor", xmlTag = "NeveRest40Gearmotor")
@MotorType(gearing = 40.0D, maxRPM = 160.0D, orientation = Rotation.CCW, ticksPerRev = 1120.0D)
public interface NeveRest40Gearmotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\NeveRest40Gearmotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */