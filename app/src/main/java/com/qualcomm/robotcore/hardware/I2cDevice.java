package com.qualcomm.robotcore.hardware;

import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public interface I2cDevice extends I2cControllerPortDevice, HardwareDevice {
  void clearI2cPortActionFlag();
  
  void copyBufferIntoWriteBuffer(byte[] paramArrayOfbyte);
  
  void deregisterForPortReadyBeginEndCallback();
  
  void deregisterForPortReadyCallback();
  
  void enableI2cReadMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2);
  
  void enableI2cWriteMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2);
  
  int getCallbackCount();
  
  @Deprecated
  I2cController getController();
  
  byte[] getCopyOfReadBuffer();
  
  byte[] getCopyOfWriteBuffer();
  
  I2cController.I2cPortReadyCallback getI2cPortReadyCallback();
  
  byte[] getI2cReadCache();
  
  Lock getI2cReadCacheLock();
  
  TimeWindow getI2cReadCacheTimeWindow();
  
  byte[] getI2cWriteCache();
  
  Lock getI2cWriteCacheLock();
  
  int getMaxI2cWriteLatency();
  
  I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback();
  
  boolean isArmed();
  
  @Deprecated
  boolean isI2cPortActionFlagSet();
  
  boolean isI2cPortInReadMode();
  
  boolean isI2cPortInWriteMode();
  
  boolean isI2cPortReady();
  
  void readI2cCacheFromController();
  
  @Deprecated
  void readI2cCacheFromModule();
  
  void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback paramI2cPortReadyCallback);
  
  void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications);
  
  void setI2cPortActionFlag();
  
  void writeI2cCacheToController();
  
  @Deprecated
  void writeI2cCacheToModule();
  
  void writeI2cPortFlagOnlyToController();
  
  @Deprecated
  void writeI2cPortFlagOnlyToModule();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */