package com.qualcomm.robotcore.robot;

import android.content.Context;
import com.qualcomm.robotcore.R;

public enum RobotStatus {
  ABORT_DUE_TO_INTERRUPT, NETWORK_TIMED_OUT, NONE, SCANNING_USB, STARTING_ROBOT, UNABLE_TO_START_ROBOT, UNKNOWN, WAITING_ON_NETWORK_CONNECTION, WAITING_ON_WIFI, WAITING_ON_WIFI_DIRECT;
  
  static {
    NONE = new RobotStatus("NONE", 1);
    SCANNING_USB = new RobotStatus("SCANNING_USB", 2);
    ABORT_DUE_TO_INTERRUPT = new RobotStatus("ABORT_DUE_TO_INTERRUPT", 3);
    WAITING_ON_WIFI = new RobotStatus("WAITING_ON_WIFI", 4);
    WAITING_ON_WIFI_DIRECT = new RobotStatus("WAITING_ON_WIFI_DIRECT", 5);
    WAITING_ON_NETWORK_CONNECTION = new RobotStatus("WAITING_ON_NETWORK_CONNECTION", 6);
    NETWORK_TIMED_OUT = new RobotStatus("NETWORK_TIMED_OUT", 7);
    STARTING_ROBOT = new RobotStatus("STARTING_ROBOT", 8);
    RobotStatus robotStatus = new RobotStatus("UNABLE_TO_START_ROBOT", 9);
    UNABLE_TO_START_ROBOT = robotStatus;
    $VALUES = new RobotStatus[] { UNKNOWN, NONE, SCANNING_USB, ABORT_DUE_TO_INTERRUPT, WAITING_ON_WIFI, WAITING_ON_WIFI_DIRECT, WAITING_ON_NETWORK_CONNECTION, NETWORK_TIMED_OUT, STARTING_ROBOT, robotStatus };
  }
  
  public String toString(Context paramContext) {
    switch (this) {
      default:
        return paramContext.getString(R.string.robotStatusInternalError);
      case null:
        return paramContext.getString(R.string.robotStatusUnableToStartRobot);
      case null:
        return paramContext.getString(R.string.robotStatusStartingRobot);
      case null:
        return paramContext.getString(R.string.robotStatusNetworkTimedOut);
      case null:
        return paramContext.getString(R.string.robotStatusWaitingOnNetworkConnection);
      case null:
        return paramContext.getString(R.string.robotStatusWaitingOnWifiDirect);
      case null:
        return paramContext.getString(R.string.robotStatusWaitingOnWifi);
      case null:
        return paramContext.getString(R.string.robotStatusScanningUSB);
      case null:
        return "";
      case null:
        break;
    } 
    return paramContext.getString(R.string.robotStatusUnknown);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robot\RobotStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */