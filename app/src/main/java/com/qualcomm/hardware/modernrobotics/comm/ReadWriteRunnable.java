package com.qualcomm.hardware.modernrobotics.comm;

import com.qualcomm.robotcore.eventloop.SyncdDevice;
import java.util.concurrent.ExecutorService;

public interface ReadWriteRunnable extends Runnable, SyncdDevice {
  public static final int MAX_BUFFER_SIZE = 256;
  
  void close();
  
  ReadWriteRunnableSegment createSegment(int paramInt1, int paramInt2, int paramInt3);
  
  void destroySegment(int paramInt);
  
  void drainPendingWrites();
  
  void executeUsing(ExecutorService paramExecutorService);
  
  boolean getAcceptingWrites();
  
  ReadWriteRunnableSegment getSegment(int paramInt);
  
  void queueSegmentRead(int paramInt);
  
  void queueSegmentWrite(int paramInt);
  
  byte[] read(int paramInt1, int paramInt2);
  
  byte[] readFromWriteCache(int paramInt1, int paramInt2);
  
  void resetWriteNeeded();
  
  void run();
  
  void setAcceptingWrites(boolean paramBoolean);
  
  void setCallback(Callback paramCallback);
  
  void suppressReads(boolean paramBoolean);
  
  void write(int paramInt, byte[] paramArrayOfbyte);
  
  boolean writeNeeded();
  
  public enum BlockingState {
    BLOCKING, WAITING;
    
    static {
      BlockingState blockingState = new BlockingState("WAITING", 1);
      WAITING = blockingState;
      $VALUES = new BlockingState[] { BLOCKING, blockingState };
    }
  }
  
  public static interface Callback {
    void readComplete() throws InterruptedException;
    
    void shutdownComplete() throws InterruptedException;
    
    void startupComplete() throws InterruptedException;
    
    void writeComplete() throws InterruptedException;
  }
  
  public static class EmptyCallback implements Callback {
    public void readComplete() throws InterruptedException {}
    
    public void shutdownComplete() throws InterruptedException {}
    
    public void startupComplete() throws InterruptedException {}
    
    public void writeComplete() throws InterruptedException {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ReadWriteRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */