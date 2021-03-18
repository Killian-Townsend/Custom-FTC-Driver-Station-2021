package com.qualcomm.robotcore.hardware;

public interface ColorSensor extends HardwareDevice {
  int alpha();
  
  int argb();
  
  int blue();
  
  void enableLed(boolean paramBoolean);
  
  I2cAddr getI2cAddress();
  
  int green();
  
  int red();
  
  void setI2cAddress(I2cAddr paramI2cAddr);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */