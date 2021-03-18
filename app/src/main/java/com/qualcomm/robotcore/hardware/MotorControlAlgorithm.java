package com.qualcomm.robotcore.hardware;

public enum MotorControlAlgorithm {
  LegacyPID, PIDF, Unknown;
  
  static {
    LegacyPID = new MotorControlAlgorithm("LegacyPID", 1);
    MotorControlAlgorithm motorControlAlgorithm = new MotorControlAlgorithm("PIDF", 2);
    PIDF = motorControlAlgorithm;
    $VALUES = new MotorControlAlgorithm[] { Unknown, LegacyPID, motorControlAlgorithm };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\MotorControlAlgorithm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */