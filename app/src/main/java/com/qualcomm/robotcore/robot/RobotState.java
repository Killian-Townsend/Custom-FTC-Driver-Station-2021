package com.qualcomm.robotcore.robot;

import android.content.Context;
import com.qualcomm.robotcore.R;

public enum RobotState {
  EMERGENCY_STOP,
  INIT,
  NOT_STARTED,
  RUNNING,
  STOPPED,
  UNKNOWN(-1);
  
  private int robotState;
  
  static {
    NOT_STARTED = new RobotState("NOT_STARTED", 1, 0);
    INIT = new RobotState("INIT", 2, 1);
    RUNNING = new RobotState("RUNNING", 3, 2);
    STOPPED = new RobotState("STOPPED", 4, 3);
    RobotState robotState = new RobotState("EMERGENCY_STOP", 5, 4);
    EMERGENCY_STOP = robotState;
    $VALUES = new RobotState[] { UNKNOWN, NOT_STARTED, INIT, RUNNING, STOPPED, robotState };
  }
  
  RobotState(int paramInt1) {
    this.robotState = (byte)paramInt1;
  }
  
  public static RobotState fromByte(int paramInt) {
    for (RobotState robotState : values()) {
      if (robotState.robotState == paramInt)
        return robotState; 
    } 
    return UNKNOWN;
  }
  
  public byte asByte() {
    return (byte)this.robotState;
  }
  
  public String toString(Context paramContext) {
    switch (this) {
      default:
        return paramContext.getString(R.string.robotStateInternalError);
      case null:
        return paramContext.getString(R.string.robotStateEmergencyStop);
      case null:
        return paramContext.getString(R.string.robotStateStopped);
      case null:
        return paramContext.getString(R.string.robotStateRunning);
      case null:
        return paramContext.getString(R.string.robotStateInit);
      case null:
        return paramContext.getString(R.string.robotStateNotStarted);
      case null:
        break;
    } 
    return paramContext.getString(R.string.robotStateUnknown);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robot\RobotState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */