package com.qualcomm.robotcore.hardware;

public interface GyroSensor extends HardwareDevice {
  void calibrate();
  
  int getHeading();
  
  double getRotationFraction();
  
  boolean isCalibrating();
  
  int rawX();
  
  int rawY();
  
  int rawZ();
  
  void resetZAxisIntegrator();
  
  String status();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\GyroSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */