package com.qualcomm.robotcore.hardware;

public class PWMOutputImplEx extends PWMOutputImpl implements PWMOutputEx {
  PWMOutputControllerEx controllerEx;
  
  public PWMOutputImplEx(PWMOutputController paramPWMOutputController, int paramInt) {
    super(paramPWMOutputController, paramInt);
    this.controllerEx = (PWMOutputControllerEx)paramPWMOutputController;
  }
  
  public boolean isPwmEnabled() {
    return this.controllerEx.isPwmEnabled(this.port);
  }
  
  public void setPwmDisable() {
    this.controllerEx.setPwmDisable(this.port);
  }
  
  public void setPwmEnable() {
    this.controllerEx.setPwmEnable(this.port);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PWMOutputImplEx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */