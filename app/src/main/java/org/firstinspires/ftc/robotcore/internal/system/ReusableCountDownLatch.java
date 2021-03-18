package org.firstinspires.ftc.robotcore.internal.system;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class ReusableCountDownLatch {
  protected final Sync sync;
  
  public ReusableCountDownLatch(int paramInt) {
    if (paramInt >= 0) {
      this.sync = new Sync(paramInt);
      return;
    } 
    throw new IllegalArgumentException("count < 0");
  }
  
  public void await() throws InterruptedException {
    this.sync.acquireSharedInterruptibly(1);
  }
  
  public boolean await(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return this.sync.tryAcquireSharedNanos(1, paramTimeUnit.toNanos(paramLong));
  }
  
  public boolean countDown() {
    return this.sync.releaseShared(1);
  }
  
  public long getCount() {
    return this.sync.getCount();
  }
  
  public void reset(int paramInt) {
    this.sync.setCount(paramInt);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(super.toString());
    stringBuilder.append("[Count = ");
    stringBuilder.append(this.sync.getCount());
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  protected static final class Sync extends AbstractQueuedSynchronizer {
    protected Sync(int param1Int) {
      setState(param1Int);
    }
    
    protected int getCount() {
      return getState();
    }
    
    protected void setCount(int param1Int) {
      setState(param1Int);
    }
    
    protected int tryAcquireShared(int param1Int) {
      return (getState() == 0) ? 1 : -1;
    }
    
    protected boolean tryReleaseShared(int param1Int) {
      while (true) {
        param1Int = getState();
        boolean bool = false;
        if (param1Int == 0)
          return false; 
        int i = param1Int - 1;
        if (compareAndSetState(param1Int, i)) {
          if (i == 0)
            bool = true; 
          return bool;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\ReusableCountDownLatch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */