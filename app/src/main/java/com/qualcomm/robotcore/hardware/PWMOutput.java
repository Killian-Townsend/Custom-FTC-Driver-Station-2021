package com.qualcomm.robotcore.hardware;

public interface PWMOutput extends HardwareDevice {
  int getPulseWidthOutputTime();
  
  int getPulseWidthPeriod();
  
  void setPulseWidthOutputTime(int paramInt);
  
  void setPulseWidthPeriod(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PWMOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */