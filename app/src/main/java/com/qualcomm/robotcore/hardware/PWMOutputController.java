package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface PWMOutputController extends HardwareDevice {
  int getPulseWidthOutputTime(int paramInt);
  
  int getPulseWidthPeriod(int paramInt);
  
  SerialNumber getSerialNumber();
  
  void setPulseWidthOutputTime(int paramInt1, int paramInt2);
  
  void setPulseWidthPeriod(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PWMOutputController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */