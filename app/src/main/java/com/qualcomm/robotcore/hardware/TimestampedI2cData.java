package com.qualcomm.robotcore.hardware;

import java.util.concurrent.atomic.AtomicInteger;

public class TimestampedI2cData extends TimestampedData {
  protected static AtomicInteger healthStatusSuppressionCount = new AtomicInteger(0);
  
  public I2cAddr i2cAddr;
  
  public int register;
  
  public static TimestampedI2cData makeFakeData(Object paramObject, I2cAddr paramI2cAddr, int paramInt1, int paramInt2) {
    if (healthStatusSuppressionCount.get() == 0 && paramObject != null && paramObject instanceof HardwareDeviceHealth)
      ((HardwareDeviceHealth)paramObject).setHealthStatus(HardwareDeviceHealth.HealthStatus.UNHEALTHY); 
    paramObject = new TimestampedI2cData();
    ((TimestampedI2cData)paramObject).data = new byte[paramInt2];
    ((TimestampedI2cData)paramObject).nanoTime = System.nanoTime();
    ((TimestampedI2cData)paramObject).i2cAddr = paramI2cAddr;
    ((TimestampedI2cData)paramObject).register = paramInt1;
    return (TimestampedI2cData)paramObject;
  }
  
  public static void suppressNewHealthWarnings(boolean paramBoolean) {
    if (paramBoolean) {
      healthStatusSuppressionCount.getAndIncrement();
      return;
    } 
    healthStatusSuppressionCount.getAndDecrement();
  }
  
  public static void suppressNewHealthWarningsWhile(Runnable paramRunnable) {
    healthStatusSuppressionCount.getAndIncrement();
    try {
      paramRunnable.run();
      return;
    } finally {
      healthStatusSuppressionCount.getAndDecrement();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\TimestampedI2cData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */