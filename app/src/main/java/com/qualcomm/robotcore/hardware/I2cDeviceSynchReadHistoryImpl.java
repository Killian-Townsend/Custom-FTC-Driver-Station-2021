package com.qualcomm.robotcore.hardware;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;

public class I2cDeviceSynchReadHistoryImpl implements I2cDeviceSynchReadHistory {
  protected BlockingQueue<TimestampedI2cData> historyQueue;
  
  protected int historyQueueCapacity;
  
  protected final Object historyQueueLock = new Object();
  
  public I2cDeviceSynchReadHistoryImpl() {
    setHistoryQueueCapacity(0);
  }
  
  public void addToHistoryQueue(TimestampedI2cData paramTimestampedI2cData) {
    synchronized (this.historyQueueLock) {
      if (this.historyQueueCapacity > 0)
        this.historyQueue.add(paramTimestampedI2cData); 
      return;
    } 
  }
  
  public BlockingQueue<TimestampedI2cData> getHistoryQueue() {
    synchronized (this.historyQueueLock) {
      return this.historyQueue;
    } 
  }
  
  public int getHistoryQueueCapacity() {
    synchronized (this.historyQueueLock) {
      return this.historyQueueCapacity;
    } 
  }
  
  public void setHistoryQueueCapacity(int paramInt) {
    synchronized (this.historyQueueLock) {
      this.historyQueueCapacity = Math.max(0, paramInt);
      if (paramInt <= 0) {
        this.historyQueue = new ArrayBlockingQueue<TimestampedI2cData>(1);
      } else {
        this.historyQueue = (BlockingQueue<TimestampedI2cData>)new EvictingBlockingQueue(new ArrayBlockingQueue(paramInt));
      } 
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchReadHistoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */