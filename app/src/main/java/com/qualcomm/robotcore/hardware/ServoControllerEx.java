package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

public interface ServoControllerEx extends ServoController {
  PwmControl.PwmRange getServoPwmRange(int paramInt);
  
  boolean isServoPwmEnabled(int paramInt);
  
  void setServoPwmDisable(int paramInt);
  
  void setServoPwmEnable(int paramInt);
  
  void setServoPwmRange(int paramInt, PwmControl.PwmRange paramPwmRange);
  
  void setServoType(int paramInt, ServoConfigurationType paramServoConfigurationType);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ServoControllerEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */