package com.qualcomm.robotcore.hardware;

public interface CompassSensor extends HardwareDevice {
  boolean calibrationFailed();
  
  double getDirection();
  
  void setMode(CompassMode paramCompassMode);
  
  String status();
  
  public enum CompassMode {
    CALIBRATION_MODE, MEASUREMENT_MODE;
    
    static {
      CompassMode compassMode = new CompassMode("CALIBRATION_MODE", 1);
      CALIBRATION_MODE = compassMode;
      $VALUES = new CompassMode[] { MEASUREMENT_MODE, compassMode };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\CompassSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */