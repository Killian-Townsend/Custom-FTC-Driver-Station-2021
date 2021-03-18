package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

public interface AccelerationSensor extends HardwareDevice {
  Acceleration getAcceleration();
  
  String status();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\AccelerationSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */