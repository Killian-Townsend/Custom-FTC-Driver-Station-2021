package com.qualcomm.robotcore.hardware.configuration;

public enum ServoFlavor {
  CONTINUOUS, CUSTOM, STANDARD;
  
  static {
    CONTINUOUS = new ServoFlavor("CONTINUOUS", 1);
    ServoFlavor servoFlavor = new ServoFlavor("CUSTOM", 2);
    CUSTOM = servoFlavor;
    $VALUES = new ServoFlavor[] { STANDARD, CONTINUOUS, servoFlavor };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ServoFlavor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */