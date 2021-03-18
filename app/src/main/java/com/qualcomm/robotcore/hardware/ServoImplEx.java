package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;

public class ServoImplEx extends ServoImpl implements PwmControl {
  protected ServoControllerEx controllerEx;
  
  public ServoImplEx(ServoControllerEx paramServoControllerEx, int paramInt, Servo.Direction paramDirection, ServoConfigurationType paramServoConfigurationType) {
    super(paramServoControllerEx, paramInt, paramDirection);
    this.controllerEx = paramServoControllerEx;
    paramServoControllerEx.setServoType(paramInt, paramServoConfigurationType);
  }
  
  public ServoImplEx(ServoControllerEx paramServoControllerEx, int paramInt, ServoConfigurationType paramServoConfigurationType) {
    this(paramServoControllerEx, paramInt, Servo.Direction.FORWARD, paramServoConfigurationType);
  }
  
  public PwmControl.PwmRange getPwmRange() {
    return this.controllerEx.getServoPwmRange(getPortNumber());
  }
  
  public boolean isPwmEnabled() {
    return this.controllerEx.isServoPwmEnabled(getPortNumber());
  }
  
  public void setPwmDisable() {
    this.controllerEx.setServoPwmDisable(getPortNumber());
  }
  
  public void setPwmEnable() {
    this.controllerEx.setServoPwmEnable(getPortNumber());
  }
  
  public void setPwmRange(PwmControl.PwmRange paramPwmRange) {
    this.controllerEx.setServoPwmRange(getPortNumber(), paramPwmRange);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ServoImplEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */