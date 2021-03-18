package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public interface DistanceSensor extends HardwareDevice {
  public static final double distanceOutOfRange = 1.7976931348623157E308D;
  
  double getDistance(DistanceUnit paramDistanceUnit);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DistanceSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */