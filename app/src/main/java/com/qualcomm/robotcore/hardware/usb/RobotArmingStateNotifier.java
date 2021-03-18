package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.util.SerialNumber;

public interface RobotArmingStateNotifier {
  ARMINGSTATE getArmingState();
  
  SerialNumber getSerialNumber();
  
  void registerCallback(Callback paramCallback, boolean paramBoolean);
  
  void unregisterCallback(Callback paramCallback);
  
  public enum ARMINGSTATE {
    ARMED, CLOSED, DISARMED, PRETENDING, TO_ARMED, TO_DISARMED, TO_PRETENDING;
    
    static {
      CLOSED = new ARMINGSTATE("CLOSED", 3);
      TO_ARMED = new ARMINGSTATE("TO_ARMED", 4);
      TO_PRETENDING = new ARMINGSTATE("TO_PRETENDING", 5);
      ARMINGSTATE aRMINGSTATE = new ARMINGSTATE("TO_DISARMED", 6);
      TO_DISARMED = aRMINGSTATE;
      $VALUES = new ARMINGSTATE[] { ARMED, PRETENDING, DISARMED, CLOSED, TO_ARMED, TO_PRETENDING, aRMINGSTATE };
    }
  }
  
  public static interface Callback {
    void onModuleStateChange(RobotArmingStateNotifier param1RobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE param1ARMINGSTATE);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotArmingStateNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */