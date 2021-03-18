package com.qualcomm.robotcore.exception;

public class TargetPositionNotSetException extends RuntimeException {
  public TargetPositionNotSetException() {
    super("Failed to enable motor. You must set a target position before switching to RUN_TO_POSITION mode");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\exception\TargetPositionNotSetException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */