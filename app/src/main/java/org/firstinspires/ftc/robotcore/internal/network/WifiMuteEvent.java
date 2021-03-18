package org.firstinspires.ftc.robotcore.internal.network;

import org.firstinspires.ftc.robotcore.external.Event;

public enum WifiMuteEvent implements Event {
  ACTIVITY_OTHER, ACTIVITY_START, ACTIVITY_STOP, RUNNING_OPMODE, STOPPED_OPMODE, USER_ACTIVITY, WATCHDOG_TIMEOUT, WATCHDOG_WARNING;
  
  static {
    WATCHDOG_TIMEOUT = new WifiMuteEvent("WATCHDOG_TIMEOUT", 2);
    RUNNING_OPMODE = new WifiMuteEvent("RUNNING_OPMODE", 3);
    STOPPED_OPMODE = new WifiMuteEvent("STOPPED_OPMODE", 4);
    ACTIVITY_START = new WifiMuteEvent("ACTIVITY_START", 5);
    ACTIVITY_STOP = new WifiMuteEvent("ACTIVITY_STOP", 6);
    WifiMuteEvent wifiMuteEvent = new WifiMuteEvent("ACTIVITY_OTHER", 7);
    ACTIVITY_OTHER = wifiMuteEvent;
    $VALUES = new WifiMuteEvent[] { USER_ACTIVITY, WATCHDOG_WARNING, WATCHDOG_TIMEOUT, RUNNING_OPMODE, STOPPED_OPMODE, ACTIVITY_START, ACTIVITY_STOP, wifiMuteEvent };
  }
  
  public String getName() {
    return toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiMuteEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */