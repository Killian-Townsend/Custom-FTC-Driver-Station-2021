package org.firstinspires.ftc.robotcore.internal.tfod;

import java.util.concurrent.TimeUnit;

public class Rate {
  private static final String TAG = "Rate";
  
  private long lastExecutionTimeNanos;
  
  private final long minSleepTimeNanos;
  
  public Rate(double paramDouble) {
    this.minSleepTimeNanos = (long)(1.0D / paramDouble * 1.0E9D);
  }
  
  private long getNextSleepTimeMs() {
    long l1 = System.nanoTime();
    long l2 = this.lastExecutionTimeNanos;
    long l3 = this.minSleepTimeNanos;
    return TimeUnit.MILLISECONDS.convert(l3 - l1 - l2, TimeUnit.NANOSECONDS);
  }
  
  public void checkedSleep() throws InterruptedException {
    long l = getNextSleepTimeMs();
    if (l > 0L)
      Thread.sleep(l); 
    this.lastExecutionTimeNanos = System.nanoTime();
  }
  
  public void sleep() {
    try {
      checkedSleep();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      this.lastExecutionTimeNanos = System.nanoTime();
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\Rate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */