package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public class MessageKeyedLock {
  private final Lock acquisitionsLock = new ReentrantLock(true);
  
  private final Condition condition;
  
  private final Lock lock;
  
  private long lockAquisitionTime;
  
  private int lockCount;
  
  private volatile LynxMessage lockOwner;
  
  private final String name;
  
  private long nanoLockAquisitionTimeMax;
  
  private volatile boolean throwOnAcquisitionAttempt;
  
  private volatile boolean tryingToHangAcquisitions = false;
  
  public MessageKeyedLock(String paramString) {
    this(paramString, 500);
  }
  
  public MessageKeyedLock(String paramString, int paramInt) {
    ReentrantLock reentrantLock = new ReentrantLock();
    this.lock = reentrantLock;
    this.name = paramString;
    this.condition = reentrantLock.newCondition();
    this.lockOwner = null;
    this.lockCount = 0;
    this.lockAquisitionTime = 0L;
    this.nanoLockAquisitionTimeMax = paramInt * 1000000L;
  }
  
  private void loge(String paramString, Object... paramVarArgs) {
    RobotLog.e("%s: %s", new Object[] { this.name, String.format(paramString, paramVarArgs) });
  }
  
  private void logv(String paramString, Object... paramVarArgs) {
    RobotLog.v("%s: %s", new Object[] { this.name, String.format(paramString, paramVarArgs) });
  }
  
  public void acquire(LynxMessage paramLynxMessage) throws InterruptedException {
    if (paramLynxMessage != null) {
      if (!this.throwOnAcquisitionAttempt || paramLynxMessage instanceof com.qualcomm.hardware.lynx.commands.standard.LynxKeepAliveCommand) {
        if (this.tryingToHangAcquisitions) {
          this.acquisitionsLock.lock();
        } else {
          this.acquisitionsLock.lockInterruptibly();
        } 
        try {
          this.lock.lockInterruptibly();
          try {
            if (this.lockOwner != paramLynxMessage) {
              while (this.lockOwner != null) {
                if (System.nanoTime() - this.lockAquisitionTime > this.nanoLockAquisitionTimeMax) {
                  loge("#### abandoning lock: old=%s(%d)", new Object[] { this.lockOwner.getClass().getSimpleName(), Integer.valueOf(this.lockOwner.getMessageNumber()) });
                  loge("                      new=%s(%d)", new Object[] { paramLynxMessage.getClass().getSimpleName(), Integer.valueOf(paramLynxMessage.getMessageNumber()) });
                  break;
                } 
                this.condition.await(this.nanoLockAquisitionTimeMax / 4L, TimeUnit.NANOSECONDS);
              } 
              this.lockCount = 0;
              this.lockAquisitionTime = System.nanoTime();
              this.lockOwner = paramLynxMessage;
              if (LynxUsbDeviceImpl.DEBUG_LOG_DATAGRAMS_LOCK)
                logv("lock %s msg#=%d", new Object[] { this.lockOwner.getClass().getSimpleName(), Integer.valueOf(this.lockOwner.getMessageNumber()) }); 
            } else {
              logv("lock recursively acquired", new Object[0]);
            } 
            this.lockCount++;
            return;
          } finally {
            this.lock.unlock();
            this.acquisitionsLock.unlock();
          } 
        } catch (Exception exception) {
          this.acquisitionsLock.unlock();
          throw exception;
        } 
      } 
      throw new OpModeManagerImpl.ForceStopException();
    } 
    throw new IllegalArgumentException("MessageKeyedLock.acquire: null message");
  }
  
  public void lockAcquisitions() {
    if (LynxUsbDeviceImpl.DEBUG_LOG_DATAGRAMS_LOCK)
      logv("***ALL FUTURE ACQUISITION ATTEMPTS FROM THREADS OTHER THAN %s WILL NOW HANG!***", new Object[] { Thread.currentThread().getName() }); 
    this.acquisitionsLock.lock();
    this.tryingToHangAcquisitions = true;
  }
  
  public void release(LynxMessage paramLynxMessage) throws InterruptedException {
    if (paramLynxMessage != null) {
      this.lock.lockInterruptibly();
      try {
        if (this.lockOwner == paramLynxMessage) {
          int i = this.lockCount - 1;
          this.lockCount = i;
          if (i == 0) {
            if (LynxUsbDeviceImpl.DEBUG_LOG_DATAGRAMS_LOCK)
              logv("unlock %s msg#=%d", new Object[] { this.lockOwner.getClass().getSimpleName(), Integer.valueOf(this.lockOwner.getMessageNumber()) }); 
            this.lockOwner = null;
            this.condition.signalAll();
          } else {
            logv("lock recursively released", new Object[0]);
          } 
        } else if (this.lockOwner != null) {
          loge("#### incorrect owner releasing message keyed lock: ignored: old=%s(%d:%d)", new Object[] { this.lockOwner.getClass().getSimpleName(), Integer.valueOf(this.lockOwner.getModuleAddress()), Integer.valueOf(this.lockOwner.getMessageNumber()) });
          loge("                                                            new=%s(%d:%d)", new Object[] { paramLynxMessage.getClass().getSimpleName(), Integer.valueOf(paramLynxMessage.getModuleAddress()), Integer.valueOf(paramLynxMessage.getMessageNumber()) });
        } else {
          loge("#### releasing ownerless message keyed lock: ignored: %s", new Object[] { paramLynxMessage.getClass().getSimpleName() });
        } 
        return;
      } finally {
        this.lock.unlock();
      } 
    } 
    throw new IllegalArgumentException("MessageKeyedLock.release: null message");
  }
  
  public void reset() throws InterruptedException {
    this.lock.lockInterruptibly();
    try {
      this.lockOwner = null;
      this.lockCount = 0;
      this.lockAquisitionTime = 0L;
      this.condition.signalAll();
      return;
    } finally {
      this.lock.unlock();
    } 
  }
  
  public void throwOnLockAcquisitions(boolean paramBoolean) {
    this.throwOnAcquisitionAttempt = paramBoolean;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\MessageKeyedLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */