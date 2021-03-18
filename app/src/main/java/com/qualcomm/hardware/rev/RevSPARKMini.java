package com.qualcomm.hardware.rev;

import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/rev_spark_mini_description", name = "@string/rev_spark_mini_name", xmlTag = "RevSPARKMini")
@ServoType(flavor = ServoFlavor.CONTINUOUS, usPulseLower = 500.0D, usPulseUpper = 2500.0D)
public interface RevSPARKMini {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\rev\RevSPARKMini.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */