package com.qualcomm.robotcore.hardware;

public enum I2cWaitControl {
  ATOMIC, NONE, WRITTEN;
  
  static {
    ATOMIC = new I2cWaitControl("ATOMIC", 1);
    I2cWaitControl i2cWaitControl = new I2cWaitControl("WRITTEN", 2);
    WRITTEN = i2cWaitControl;
    $VALUES = new I2cWaitControl[] { NONE, ATOMIC, i2cWaitControl };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cWaitControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */