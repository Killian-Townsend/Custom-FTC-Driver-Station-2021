package com.qualcomm.robotcore.hardware;

public interface ServoController extends HardwareDevice {
  PwmStatus getPwmStatus();
  
  double getServoPosition(int paramInt);
  
  void pwmDisable();
  
  void pwmEnable();
  
  void setServoPosition(int paramInt, double paramDouble);
  
  public enum PwmStatus {
    DISABLED, ENABLED, MIXED;
    
    static {
      PwmStatus pwmStatus = new PwmStatus("MIXED", 2);
      MIXED = pwmStatus;
      $VALUES = new PwmStatus[] { ENABLED, DISABLED, pwmStatus };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ServoController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */