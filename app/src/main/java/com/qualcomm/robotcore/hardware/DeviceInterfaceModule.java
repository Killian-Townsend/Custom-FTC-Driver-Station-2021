package com.qualcomm.robotcore.hardware;

public interface DeviceInterfaceModule extends DigitalChannelController, AnalogInputController, PWMOutputController, I2cController, AnalogOutputController {
  byte getDigitalIOControlByte();
  
  int getDigitalInputStateByte();
  
  byte getDigitalOutputStateByte();
  
  boolean getLEDState(int paramInt);
  
  void setDigitalIOControlByte(byte paramByte);
  
  void setDigitalOutputByte(byte paramByte);
  
  void setLED(int paramInt, boolean paramBoolean);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DeviceInterfaceModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */