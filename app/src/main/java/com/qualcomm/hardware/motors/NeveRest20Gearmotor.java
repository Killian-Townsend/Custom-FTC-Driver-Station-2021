package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "AndyMark", model = "am-3102", url = "http://www.andymark.com/NeveRest-20-12V-Gearmotor-p/am-3102.htm")
@ModernRoboticsMotorControllerParams(D = 112, I = 32, P = 160, ratio = 25)
@DeviceProperties(builtIn = true, name = "NeveRest 20 Gearmotor", xmlTag = "NeveRest20Gearmotor")
@MotorType(gearing = 20.0D, maxRPM = 315.0D, orientation = Rotation.CW, ticksPerRev = 560.0D)
public interface NeveRest20Gearmotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\NeveRest20Gearmotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */