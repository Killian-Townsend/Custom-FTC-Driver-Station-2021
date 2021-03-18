package com.qualcomm.robotcore.hardware;

public interface LegacyModule extends HardwareDevice, I2cController {
  void enable9v(int paramInt, boolean paramBoolean);
  
  void enableAnalogReadMode(int paramInt);
  
  double getMaxAnalogInputVoltage();
  
  byte[] readAnalogRaw(int paramInt);
  
  double readAnalogVoltage(int paramInt);
  
  void setDigitalLine(int paramInt1, int paramInt2, boolean paramBoolean);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LegacyModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */