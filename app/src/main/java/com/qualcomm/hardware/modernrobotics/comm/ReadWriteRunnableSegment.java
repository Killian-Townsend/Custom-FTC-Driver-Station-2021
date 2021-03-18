package com.qualcomm.hardware.modernrobotics.comm;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public class ReadWriteRunnableSegment {
  private int address;
  
  private final byte[] bufferRead;
  
  private final byte[] bufferWrite;
  
  private int key;
  
  final Lock lockRead;
  
  final Lock lockWrite;
  
  private boolean retryOnReadFailure;
  
  private TimeWindow timeWindow;
  
  public ReadWriteRunnableSegment(int paramInt1, int paramInt2, int paramInt3) {
    this.key = paramInt1;
    this.address = paramInt2;
    this.lockRead = new ReentrantLock();
    this.bufferRead = new byte[paramInt3];
    this.lockWrite = new ReentrantLock();
    this.bufferWrite = new byte[paramInt3];
    this.retryOnReadFailure = true;
    this.timeWindow = new TimeWindow();
  }
  
  public int getAddress() {
    return this.address;
  }
  
  public int getKey() {
    return this.key;
  }
  
  public byte[] getReadBuffer() {
    return this.bufferRead;
  }
  
  public Lock getReadLock() {
    return this.lockRead;
  }
  
  public boolean getRetryOnReadFailure() {
    return this.retryOnReadFailure;
  }
  
  public TimeWindow getTimeWindow() {
    return this.timeWindow;
  }
  
  public byte[] getWriteBuffer() {
    return this.bufferWrite;
  }
  
  public Lock getWriteLock() {
    return this.lockWrite;
  }
  
  public void setAddress(int paramInt) {
    this.address = paramInt;
  }
  
  public void setRetryOnReadFailure(boolean paramBoolean) {
    this.retryOnReadFailure = paramBoolean;
  }
  
  public String toString() {
    return String.format("Segment - address:%d read:%d write:%d", new Object[] { Integer.valueOf(this.address), Integer.valueOf(this.bufferRead.length), Integer.valueOf(this.bufferWrite.length) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ReadWriteRunnableSegment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */