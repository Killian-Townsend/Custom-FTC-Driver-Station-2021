package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "@string/rev_distributor", model = "REV-41-1600", url = "https://www.revrobotics.com/rev-41-1600/")
@DeviceProperties(builtIn = true, name = "@string/rev_ultraplanetary_hd_hex_name", xmlTag = "RevRoboticsUltraplanetaryHDHexMotor")
@MotorType(gearing = 20.0D, maxRPM = 300.0D, orientation = Rotation.CW, ticksPerRev = 560.0D)
public interface RevRoboticsUltraPlanetaryHdHexMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\RevRoboticsUltraPlanetaryHdHexMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */