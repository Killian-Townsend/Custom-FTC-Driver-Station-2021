package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "goBILDA_distributor", model = "goBILDA-5201", url = "https://www.gobilda.com/5201-series-spur-gear-motors/")
@DeviceProperties(builtIn = true, name = "GoBILDA 5201 series", xmlTag = "goBILDA5201SeriesMotor")
@MotorType(gearing = 54.0D, maxRPM = 104.0D, orientation = Rotation.CCW, ticksPerRev = 1500.0D)
public interface GoBILDA5201Series {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\GoBILDA5201Series.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */