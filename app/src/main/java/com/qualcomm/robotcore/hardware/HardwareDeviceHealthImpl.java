package com.qualcomm.robotcore.hardware;

import java.util.concurrent.Callable;

public class HardwareDeviceHealthImpl implements HardwareDeviceHealth {
  protected HardwareDeviceHealth.HealthStatus healthStatus;
  
  protected Callable<HardwareDeviceHealth.HealthStatus> override;
  
  protected String tag;
  
  public HardwareDeviceHealthImpl(String paramString) {
    this(paramString, null);
  }
  
  public HardwareDeviceHealthImpl(String paramString, Callable<HardwareDeviceHealth.HealthStatus> paramCallable) {
    this.tag = paramString;
    this.healthStatus = HardwareDeviceHealth.HealthStatus.UNKNOWN;
    this.override = paramCallable;
  }
  
  public void close() {
    setHealthStatus(HardwareDeviceHealth.HealthStatus.CLOSED);
  }
  
  public HardwareDeviceHealth.HealthStatus getHealthStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield override : Ljava/util/concurrent/Callable;
    //   6: astore_1
    //   7: aload_1
    //   8: ifnull -> 37
    //   11: aload_0
    //   12: getfield override : Ljava/util/concurrent/Callable;
    //   15: invokeinterface call : ()Ljava/lang/Object;
    //   20: checkcast com/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus
    //   23: astore_1
    //   24: getstatic com/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus.UNKNOWN : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   27: astore_2
    //   28: aload_1
    //   29: aload_2
    //   30: if_acmpeq -> 37
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: areturn
    //   37: aload_0
    //   38: getfield healthStatus : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   41: astore_1
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_1
    //   45: areturn
    //   46: astore_1
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_1
    //   50: athrow
    //   51: astore_1
    //   52: goto -> 37
    // Exception table:
    //   from	to	target	type
    //   2	7	46	finally
    //   11	28	51	java/lang/Exception
    //   11	28	46	finally
    //   33	35	46	finally
    //   37	44	46	finally
    //   47	49	46	finally
  }
  
  public void setHealthStatus(HardwareDeviceHealth.HealthStatus paramHealthStatus) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield healthStatus : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   6: getstatic com/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus.CLOSED : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   9: if_acmpeq -> 17
    //   12: aload_0
    //   13: aload_1
    //   14: putfield healthStatus : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_1
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_1
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
    //   17	19	20	finally
    //   21	23	20	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\HardwareDeviceHealthImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */