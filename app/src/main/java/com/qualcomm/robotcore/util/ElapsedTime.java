package com.qualcomm.robotcore.util;

import java.util.concurrent.TimeUnit;

public class ElapsedTime {
  public static final long MILLIS_IN_NANO = 1000000L;
  
  public static final long SECOND_IN_NANO = 1000000000L;
  
  protected volatile long nsStartTime;
  
  protected final double resolution;
  
  public ElapsedTime() {
    reset();
    this.resolution = 1.0E9D;
  }
  
  public ElapsedTime(long paramLong) {
    this.nsStartTime = paramLong;
    this.resolution = 1.0E9D;
  }
  
  public ElapsedTime(Resolution paramResolution) {
    reset();
    if (null.$SwitchMap$com$qualcomm$robotcore$util$ElapsedTime$Resolution[paramResolution.ordinal()] != 2) {
      this.resolution = 1.0E9D;
      return;
    } 
    this.resolution = 1000000.0D;
  }
  
  private String resolutionStr() {
    double d = this.resolution;
    return (d == 1.0E9D) ? "seconds" : ((d == 1000000.0D) ? "milliseconds" : "unknown units");
  }
  
  public Resolution getResolution() {
    return (this.resolution == 1000000.0D) ? Resolution.MILLISECONDS : Resolution.SECONDS;
  }
  
  public void log(String paramString) {
    RobotLog.v(String.format("TIMER: %20s - %1.3f %s", new Object[] { paramString, Double.valueOf(time()), resolutionStr() }));
  }
  
  public double milliseconds() {
    return seconds() * 1000.0D;
  }
  
  public long nanoseconds() {
    return nsNow() - this.nsStartTime;
  }
  
  public long now(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(nsNow(), TimeUnit.NANOSECONDS);
  }
  
  protected long nsNow() {
    return System.nanoTime();
  }
  
  public void reset() {
    this.nsStartTime = nsNow();
  }
  
  public double seconds() {
    return nanoseconds() / 1.0E9D;
  }
  
  public double startTime() {
    return this.nsStartTime / this.resolution;
  }
  
  public long startTimeNanoseconds() {
    return this.nsStartTime;
  }
  
  public double time() {
    return (nsNow() - this.nsStartTime) / this.resolution;
  }
  
  public long time(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(nanoseconds(), TimeUnit.NANOSECONDS);
  }
  
  public String toString() {
    return String.format("%1.4f %s", new Object[] { Double.valueOf(time()), resolutionStr() });
  }
  
  public enum Resolution {
    MILLISECONDS, SECONDS;
    
    static {
      Resolution resolution = new Resolution("MILLISECONDS", 1);
      MILLISECONDS = resolution;
      $VALUES = new Resolution[] { SECONDS, resolution };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ElapsedTime.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */