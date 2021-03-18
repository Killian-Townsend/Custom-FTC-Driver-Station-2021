package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "@string/rev_distributor", model = "REV-41-1301", url = "http://www.revrobotics.com/rev-41-1301")
@DeviceProperties(builtIn = true, name = "@string/rev_40_hd_hex_name", xmlTag = "RevRobotics40HDHexMotor", xmlTagAliases = {"RevRoboticsHDHexMotor"})
@MotorType(gearing = 40.0D, maxRPM = 150.0D, orientation = Rotation.CCW, ticksPerRev = 1120.0D)
public interface RevRobotics40HdHexMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\RevRobotics40HdHexMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */