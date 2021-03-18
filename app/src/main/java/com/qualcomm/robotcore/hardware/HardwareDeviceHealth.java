package com.qualcomm.robotcore.hardware;

public interface HardwareDeviceHealth {
  HealthStatus getHealthStatus();
  
  void setHealthStatus(HealthStatus paramHealthStatus);
  
  public enum HealthStatus {
    CLOSED, HEALTHY, UNHEALTHY, UNKNOWN;
    
    static {
      HealthStatus healthStatus = new HealthStatus("CLOSED", 3);
      CLOSED = healthStatus;
      $VALUES = new HealthStatus[] { UNKNOWN, HEALTHY, UNHEALTHY, healthStatus };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\HardwareDeviceHealth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */