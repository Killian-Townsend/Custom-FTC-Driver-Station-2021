package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public interface I2cController extends HardwareDevice {
  public static final byte I2C_BUFFER_START_ADDRESS = 4;
  
  void clearI2cPortActionFlag(int paramInt);
  
  void copyBufferIntoWriteBuffer(int paramInt, byte[] paramArrayOfbyte);
  
  void deregisterForPortReadyBeginEndCallback(int paramInt);
  
  void deregisterForPortReadyCallback(int paramInt);
  
  void enableI2cReadMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3);
  
  void enableI2cWriteMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3);
  
  byte[] getCopyOfReadBuffer(int paramInt);
  
  byte[] getCopyOfWriteBuffer(int paramInt);
  
  I2cPortReadyCallback getI2cPortReadyCallback(int paramInt);
  
  byte[] getI2cReadCache(int paramInt);
  
  Lock getI2cReadCacheLock(int paramInt);
  
  TimeWindow getI2cReadCacheTimeWindow(int paramInt);
  
  byte[] getI2cWriteCache(int paramInt);
  
  Lock getI2cWriteCacheLock(int paramInt);
  
  int getMaxI2cWriteLatency(int paramInt);
  
  I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int paramInt);
  
  SerialNumber getSerialNumber();
  
  boolean isArmed();
  
  boolean isI2cPortActionFlagSet(int paramInt);
  
  boolean isI2cPortInReadMode(int paramInt);
  
  boolean isI2cPortInWriteMode(int paramInt);
  
  boolean isI2cPortReady(int paramInt);
  
  void readI2cCacheFromController(int paramInt);
  
  @Deprecated
  void readI2cCacheFromModule(int paramInt);
  
  void registerForI2cPortReadyCallback(I2cPortReadyCallback paramI2cPortReadyCallback, int paramInt);
  
  void registerForPortReadyBeginEndCallback(I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications, int paramInt);
  
  void setI2cPortActionFlag(int paramInt);
  
  void writeI2cCacheToController(int paramInt);
  
  @Deprecated
  void writeI2cCacheToModule(int paramInt);
  
  void writeI2cPortFlagOnlyToController(int paramInt);
  
  @Deprecated
  void writeI2cPortFlagOnlyToModule(int paramInt);
  
  public static interface I2cPortReadyBeginEndNotifications {
    void onPortIsReadyCallbacksBegin(int param1Int) throws InterruptedException;
    
    void onPortIsReadyCallbacksEnd(int param1Int) throws InterruptedException;
  }
  
  public static interface I2cPortReadyCallback {
    void portIsReady(int param1Int);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */