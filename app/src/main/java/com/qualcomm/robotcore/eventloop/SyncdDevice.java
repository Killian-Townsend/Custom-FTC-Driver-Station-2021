package com.qualcomm.robotcore.eventloop;

import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;

public interface SyncdDevice {
  public static final int msAbnormalReopenInterval = 250;
  
  RobotUsbModule getOwner();
  
  ShutdownReason getShutdownReason();
  
  void setOwner(RobotUsbModule paramRobotUsbModule);
  
  public static interface Manager {
    void registerSyncdDevice(SyncdDevice param1SyncdDevice);
    
    void unregisterSyncdDevice(SyncdDevice param1SyncdDevice);
  }
  
  public enum ShutdownReason {
    ABNORMAL, ABNORMAL_ATTEMPT_REOPEN, NORMAL;
    
    static {
      ShutdownReason shutdownReason = new ShutdownReason("ABNORMAL_ATTEMPT_REOPEN", 2);
      ABNORMAL_ATTEMPT_REOPEN = shutdownReason;
      $VALUES = new ShutdownReason[] { NORMAL, ABNORMAL, shutdownReason };
    }
  }
  
  public static interface Syncable {
    void setSyncDeviceManager(SyncdDevice.Manager param1Manager);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\SyncdDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */