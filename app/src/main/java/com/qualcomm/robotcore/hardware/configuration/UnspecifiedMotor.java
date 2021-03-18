package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;

@DeviceProperties(builtIn = true, name = "Unspecified Motor", xmlTag = "Motor")
@MotorType(gearing = 52.0D, maxRPM = 165.0D, ticksPerRev = 1440.0D)
public interface UnspecifiedMotor {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\UnspecifiedMotor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */