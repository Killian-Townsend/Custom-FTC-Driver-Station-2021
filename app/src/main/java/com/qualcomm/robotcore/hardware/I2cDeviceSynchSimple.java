package com.qualcomm.robotcore.hardware;

public interface I2cDeviceSynchSimple extends HardwareDevice, HardwareDeviceHealth, I2cAddrConfig, RobotConfigNameable {
  void enableWriteCoalescing(boolean paramBoolean);
  
  @Deprecated
  I2cAddr getI2cAddr();
  
  boolean getLogging();
  
  String getLoggingTag();
  
  boolean isArmed();
  
  boolean isWriteCoalescingEnabled();
  
  byte[] read(int paramInt1, int paramInt2);
  
  byte read8(int paramInt);
  
  TimestampedData readTimeStamped(int paramInt1, int paramInt2);
  
  @Deprecated
  void setI2cAddr(I2cAddr paramI2cAddr);
  
  void setLogging(boolean paramBoolean);
  
  void setLoggingTag(String paramString);
  
  void waitForWriteCompletions(I2cWaitControl paramI2cWaitControl);
  
  void write(int paramInt, byte[] paramArrayOfbyte);
  
  void write(int paramInt, byte[] paramArrayOfbyte, I2cWaitControl paramI2cWaitControl);
  
  void write8(int paramInt1, int paramInt2);
  
  void write8(int paramInt1, int paramInt2, I2cWaitControl paramI2cWaitControl);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */