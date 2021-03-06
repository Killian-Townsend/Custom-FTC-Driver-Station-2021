package com.qualcomm.hardware.motors;

import com.qualcomm.robotcore.hardware.configuration.DistributorInfo;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import org.firstinspires.ftc.robotcore.external.navigation.Rotation;

@DistributorInfo(distributor = "goBILDA_distributor", model = "goBILDA-5202", url = "https://www.gobilda.com/5202-series-yellow-jacket-planetary-gear-motors/")
@DeviceProperties(builtIn = true, name = "GoBILDA 5202 series", xmlTag = "goBILDA5202SeriesMotor")
@MotorType(gearing = 99.5D, maxRPM = 60.0D, orientation = Rotation.CCW, ticksPerRev = 2786.0D)
public interface GoBILDA5202Series {}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\motors\GoBILDA5202Series.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */