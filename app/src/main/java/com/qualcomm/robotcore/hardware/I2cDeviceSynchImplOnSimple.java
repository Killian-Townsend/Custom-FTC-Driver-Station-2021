package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.util.ThreadPool;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class I2cDeviceSynchImplOnSimple extends I2cDeviceSynchReadHistoryImpl implements I2cDeviceSynch {
  protected final Object concurrentClientLock = new Object();
  
  protected int cregReadLast;
  
  protected final Object engagementLock = new Object();
  
  protected I2cDeviceSynch.HeartbeatAction heartbeatAction;
  
  protected ScheduledExecutorService heartbeatExecutor;
  
  protected I2cDeviceSynchSimple i2cDeviceSynchSimple;
  
  protected I2cDeviceSynchReadHistory i2cDeviceSynchSimpleHistory;
  
  protected int iregReadLast;
  
  protected int iregWriteLast;
  
  protected boolean isClosing;
  
  protected boolean isEngaged;
  
  protected boolean isHooked;
  
  protected boolean isSimpleOwned;
  
  protected int msHeartbeatInterval;
  
  protected byte[] rgbWriteLast;
  
  public I2cDeviceSynchImplOnSimple(I2cDeviceSynchSimple paramI2cDeviceSynchSimple, boolean paramBoolean) {
    this.i2cDeviceSynchSimple = paramI2cDeviceSynchSimple;
    if (paramI2cDeviceSynchSimple instanceof I2cDeviceSynchReadHistory) {
      I2cDeviceSynchReadHistory i2cDeviceSynchReadHistory = (I2cDeviceSynchReadHistory)paramI2cDeviceSynchSimple;
    } else {
      paramI2cDeviceSynchSimple = null;
    } 
    this.i2cDeviceSynchSimpleHistory = (I2cDeviceSynchReadHistory)paramI2cDeviceSynchSimple;
    this.isSimpleOwned = paramBoolean;
    this.msHeartbeatInterval = 0;
    this.heartbeatAction = null;
    this.heartbeatExecutor = null;
    this.cregReadLast = 0;
    this.rgbWriteLast = null;
    this.isEngaged = false;
    this.isHooked = false;
    this.isClosing = false;
  }
  
  public void addToHistoryQueue(TimestampedI2cData paramTimestampedI2cData) {
    if (this.i2cDeviceSynchSimpleHistory == null)
      super.addToHistoryQueue(paramTimestampedI2cData); 
  }
  
  protected void adjustHooking() {
    synchronized (this.engagementLock) {
      if (!this.isHooked && this.isEngaged) {
        hook();
      } else if (this.isHooked && !this.isEngaged) {
        unhook();
      } 
      return;
    } 
  }
  
  public void close() {
    this.isClosing = true;
    disengage();
    if (this.isSimpleOwned)
      this.i2cDeviceSynchSimple.close(); 
  }
  
  public void disengage() {
    synchronized (this.engagementLock) {
      this.isEngaged = false;
      adjustHooking();
      return;
    } 
  }
  
  public void enableWriteCoalescing(boolean paramBoolean) {
    this.i2cDeviceSynchSimple.enableWriteCoalescing(paramBoolean);
  }
  
  public void engage() {
    synchronized (this.engagementLock) {
      this.isEngaged = true;
      adjustHooking();
      return;
    } 
  }
  
  public void ensureReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow1, I2cDeviceSynch.ReadWindow paramReadWindow2) {}
  
  public String getConnectionInfo() {
    return this.i2cDeviceSynchSimple.getConnectionInfo();
  }
  
  public String getDeviceName() {
    return this.i2cDeviceSynchSimple.getDeviceName();
  }
  
  public HardwareDeviceHealth.HealthStatus getHealthStatus() {
    return this.i2cDeviceSynchSimple.getHealthStatus();
  }
  
  public I2cDeviceSynch.HeartbeatAction getHeartbeatAction() {
    return this.heartbeatAction;
  }
  
  public int getHeartbeatInterval() {
    return this.msHeartbeatInterval;
  }
  
  public BlockingQueue<TimestampedI2cData> getHistoryQueue() {
    I2cDeviceSynchReadHistory i2cDeviceSynchReadHistory = this.i2cDeviceSynchSimpleHistory;
    return (i2cDeviceSynchReadHistory == null) ? super.getHistoryQueue() : i2cDeviceSynchReadHistory.getHistoryQueue();
  }
  
  public int getHistoryQueueCapacity() {
    I2cDeviceSynchReadHistory i2cDeviceSynchReadHistory = this.i2cDeviceSynchSimpleHistory;
    return (i2cDeviceSynchReadHistory == null) ? super.getHistoryQueueCapacity() : i2cDeviceSynchReadHistory.getHistoryQueueCapacity();
  }
  
  public I2cAddr getI2cAddr() {
    return this.i2cDeviceSynchSimple.getI2cAddress();
  }
  
  public I2cAddr getI2cAddress() {
    return getI2cAddr();
  }
  
  public boolean getLogging() {
    return this.i2cDeviceSynchSimple.getLogging();
  }
  
  public String getLoggingTag() {
    return this.i2cDeviceSynchSimple.getLoggingTag();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.i2cDeviceSynchSimple.getManufacturer();
  }
  
  public I2cDeviceSynch.ReadWindow getReadWindow() {
    return null;
  }
  
  public String getUserConfiguredName() {
    return this.i2cDeviceSynchSimple.getUserConfiguredName();
  }
  
  public int getVersion() {
    return this.i2cDeviceSynchSimple.getVersion();
  }
  
  protected void hook() {
    synchronized (this.engagementLock) {
      if (!this.isHooked) {
        startHeartBeat();
        this.isHooked = true;
      } 
      return;
    } 
  }
  
  public boolean isArmed() {
    synchronized (this.engagementLock) {
      if (this.isHooked)
        return this.i2cDeviceSynchSimple.isArmed(); 
      return false;
    } 
  }
  
  public boolean isEngaged() {
    return this.isEngaged;
  }
  
  protected boolean isOpenForReading() {
    return (this.isHooked && newReadsAndWritesAllowed());
  }
  
  protected boolean isOpenForWriting() {
    return (this.isHooked && newReadsAndWritesAllowed());
  }
  
  public boolean isWriteCoalescingEnabled() {
    return this.i2cDeviceSynchSimple.isWriteCoalescingEnabled();
  }
  
  protected boolean newReadsAndWritesAllowed() {
    return this.isClosing ^ true;
  }
  
  public byte[] read(int paramInt1, int paramInt2) {
    return (readTimeStamped(paramInt1, paramInt2)).data;
  }
  
  public byte read8(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: iconst_1
    //   5: invokevirtual read : (II)[B
    //   8: iconst_0
    //   9: baload
    //   10: istore_2
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_2
    //   14: ireturn
    //   15: astore_3
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_3
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2) {
    synchronized (this.concurrentClientLock) {
      if (!isOpenForReading())
        return TimestampedI2cData.makeFakeData((Object)null, getI2cAddress(), paramInt1, paramInt2); 
      this.iregReadLast = paramInt1;
      this.cregReadLast = paramInt2;
      TimestampedI2cData timestampedI2cData = new TimestampedI2cData();
      timestampedI2cData.i2cAddr = getI2cAddress();
      timestampedI2cData.register = paramInt1;
      TimestampedData timestampedData = this.i2cDeviceSynchSimple.readTimeStamped(paramInt1, paramInt2);
      timestampedI2cData.data = timestampedData.data;
      timestampedI2cData.nanoTime = timestampedData.nanoTime;
      addToHistoryQueue(timestampedI2cData);
      return timestampedI2cData;
    } 
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2, I2cDeviceSynch.ReadWindow paramReadWindow1, I2cDeviceSynch.ReadWindow paramReadWindow2) {
    return readTimeStamped(paramInt1, paramInt2);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    this.i2cDeviceSynchSimple.resetDeviceConfigurationForOpMode();
  }
  
  public void setHealthStatus(HardwareDeviceHealth.HealthStatus paramHealthStatus) {
    this.i2cDeviceSynchSimple.setHealthStatus(paramHealthStatus);
  }
  
  public void setHeartbeatAction(I2cDeviceSynch.HeartbeatAction paramHeartbeatAction) {
    this.heartbeatAction = paramHeartbeatAction;
  }
  
  public void setHeartbeatInterval(int paramInt) {
    synchronized (this.concurrentClientLock) {
      this.msHeartbeatInterval = Math.max(0, this.msHeartbeatInterval);
      stopHeartBeat();
      startHeartBeat();
      return;
    } 
  }
  
  public void setHistoryQueueCapacity(int paramInt) {
    I2cDeviceSynchReadHistory i2cDeviceSynchReadHistory = this.i2cDeviceSynchSimpleHistory;
    if (i2cDeviceSynchReadHistory == null) {
      super.setHistoryQueueCapacity(paramInt);
      return;
    } 
    i2cDeviceSynchReadHistory.setHistoryQueueCapacity(paramInt);
  }
  
  public void setI2cAddr(I2cAddr paramI2cAddr) {
    this.i2cDeviceSynchSimple.setI2cAddress(paramI2cAddr);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    setI2cAddr(paramI2cAddr);
  }
  
  public void setLogging(boolean paramBoolean) {
    this.i2cDeviceSynchSimple.setLogging(paramBoolean);
  }
  
  public void setLoggingTag(String paramString) {
    this.i2cDeviceSynchSimple.setLoggingTag(paramString);
  }
  
  public void setReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow) {}
  
  public void setUserConfiguredName(String paramString) {
    this.i2cDeviceSynchSimple.setUserConfiguredName(paramString);
  }
  
  void startHeartBeat() {
    if (this.msHeartbeatInterval > 0) {
      ThreadPool.RecordingScheduledExecutor recordingScheduledExecutor = ThreadPool.newScheduledExecutor(1, "I2cDeviceSyncImplOnSimple heartbeat");
      this.heartbeatExecutor = (ScheduledExecutorService)recordingScheduledExecutor;
      recordingScheduledExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
              I2cDeviceSynch.HeartbeatAction heartbeatAction = I2cDeviceSynchImplOnSimple.this.getHeartbeatAction();
              if (heartbeatAction != null)
                synchronized (I2cDeviceSynchImplOnSimple.this.concurrentClientLock) {
                  if (heartbeatAction.rereadLastRead && I2cDeviceSynchImplOnSimple.this.cregReadLast != 0) {
                    I2cDeviceSynchImplOnSimple.this.read(I2cDeviceSynchImplOnSimple.this.iregReadLast, I2cDeviceSynchImplOnSimple.this.cregReadLast);
                    return;
                  } 
                  if (heartbeatAction.rewriteLastWritten && I2cDeviceSynchImplOnSimple.this.rgbWriteLast != null) {
                    I2cDeviceSynchImplOnSimple.this.write(I2cDeviceSynchImplOnSimple.this.iregWriteLast, I2cDeviceSynchImplOnSimple.this.rgbWriteLast);
                    return;
                  } 
                  if (heartbeatAction.heartbeatReadWindow != null)
                    I2cDeviceSynchImplOnSimple.this.read(heartbeatAction.heartbeatReadWindow.getRegisterFirst(), heartbeatAction.heartbeatReadWindow.getRegisterCount()); 
                  return;
                }  
            }
          }0L, this.msHeartbeatInterval, TimeUnit.MILLISECONDS);
    } 
  }
  
  void stopHeartBeat() {
    ScheduledExecutorService scheduledExecutorService = this.heartbeatExecutor;
    if (scheduledExecutorService != null) {
      scheduledExecutorService.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.heartbeatExecutor, 2L, TimeUnit.SECONDS, "heartbeat executor", "internal error");
      this.heartbeatExecutor = null;
    } 
  }
  
  protected void unhook() {
    synchronized (this.engagementLock) {
      if (this.isHooked) {
        stopHeartBeat();
        synchronized (this.concurrentClientLock) {
          waitForWriteCompletions(I2cWaitControl.ATOMIC);
          this.isHooked = false;
        } 
      } 
      return;
    } 
  }
  
  public void waitForWriteCompletions(I2cWaitControl paramI2cWaitControl) {
    this.i2cDeviceSynchSimple.waitForWriteCompletions(paramI2cWaitControl);
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    write(paramInt, paramArrayOfbyte, I2cWaitControl.ATOMIC);
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte, I2cWaitControl paramI2cWaitControl) {
    synchronized (this.concurrentClientLock) {
      if (!isOpenForWriting())
        return; 
      this.iregWriteLast = paramInt;
      this.rgbWriteLast = Arrays.copyOf(paramArrayOfbyte, paramArrayOfbyte.length);
      this.i2cDeviceSynchSimple.write(paramInt, paramArrayOfbyte, paramI2cWaitControl);
      return;
    } 
  }
  
  public void write8(int paramInt1, int paramInt2) {
    write8(paramInt1, paramInt2, I2cWaitControl.ATOMIC);
  }
  
  public void write8(int paramInt1, int paramInt2, I2cWaitControl paramI2cWaitControl) {
    write(paramInt1, new byte[] { (byte)paramInt2 }, paramI2cWaitControl);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchImplOnSimple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */