package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;

public interface AnalogOutputController extends HardwareDevice {
  SerialNumber getSerialNumber();
  
  void setAnalogOutputFrequency(int paramInt1, int paramInt2);
  
  void setAnalogOutputMode(int paramInt, byte paramByte);
  
  void setAnalogOutputVoltage(int paramInt1, int paramInt2);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\AnalogOutputController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */