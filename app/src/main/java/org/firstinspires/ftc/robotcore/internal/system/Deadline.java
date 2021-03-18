package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class Deadline extends ElapsedTime {
  protected TimeUnit awaitUnit = TimeUnit.NANOSECONDS;
  
  protected long msPollInterval = 125L;
  
  protected long nsDeadline;
  
  protected final long nsDuration;
  
  public Deadline(long paramLong, TimeUnit paramTimeUnit) {
    this.nsDuration = paramTimeUnit.toNanos(paramLong);
    this.nsDeadline = Misc.saturatingAdd(this.nsStartTime, this.nsDuration);
  }
  
  public boolean await(CountDownLatch paramCountDownLatch) throws InterruptedException {
    long l = this.awaitUnit.convert(this.msPollInterval, TimeUnit.MILLISECONDS);
    while (true) {
      long l1 = Math.min(l, timeRemaining(this.awaitUnit));
      if (l1 <= 0L)
        return false; 
      if (paramCountDownLatch.await(l1, this.awaitUnit))
        return true; 
    } 
  }
  
  public void cancel() {
    expire();
  }
  
  public void expire() {
    this.nsDeadline = this.nsStartTime;
  }
  
  public long getDeadline(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.nsDeadline, TimeUnit.NANOSECONDS);
  }
  
  public long getDuration(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.nsDuration, TimeUnit.NANOSECONDS);
  }
  
  public boolean hasExpired() {
    return (timeRemaining(TimeUnit.NANOSECONDS) <= 0L);
  }
  
  public void reset() {
    super.reset();
    this.nsDeadline = Misc.saturatingAdd(this.nsStartTime, this.nsDuration);
  }
  
  public long timeRemaining(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(Math.max(0L, this.nsDeadline - nsNow()), TimeUnit.NANOSECONDS);
  }
  
  public boolean tryAcquire(Semaphore paramSemaphore) throws InterruptedException {
    long l = this.awaitUnit.convert(this.msPollInterval, TimeUnit.MILLISECONDS);
    while (true) {
      long l1 = Math.min(l, timeRemaining(this.awaitUnit));
      if (l1 <= 0L)
        return false; 
      if (paramSemaphore.tryAcquire(l1, this.awaitUnit))
        return true; 
    } 
  }
  
  public boolean tryLock(Lock paramLock) throws InterruptedException {
    long l = this.awaitUnit.convert(this.msPollInterval, TimeUnit.MILLISECONDS);
    while (true) {
      long l1 = Math.min(l, timeRemaining(this.awaitUnit));
      if (l1 <= 0L)
        return false; 
      if (paramLock.tryLock(l1, this.awaitUnit))
        return true; 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Deadline.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */