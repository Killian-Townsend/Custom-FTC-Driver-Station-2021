package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerPositionParams;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerVelocityParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "@string/rev_distributor", model = "REV-41-1300", url = "http://www.revrobotics.com/rev-41-1300")
@ExpansionHubMotorControllerPositionParams(D = 0.0D, I = 0.05D, P = 10.0D)
@ExpansionHubMotorControllerVelocityParams(D = 0.0D, I = 3.0D, P = 10.0D)
@DeviceProperties(builtIn = true, name = "@string/rev_core_hex_name", xmlTag = "RevRoboticsCoreHexMotor")
@MotorType(gearing = 36.25D, maxRPM = 137.0D, orientation = Rotation.CCW, ticksPerRev = 288.0D)
public interface RevRoboticsCoreHexMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\RevRoboticsCoreHexMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */