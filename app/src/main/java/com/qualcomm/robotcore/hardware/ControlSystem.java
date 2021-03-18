package com.qualcomm.robotcore.hardware;

public enum ControlSystem {
  MODERN_ROBOTICS, REV_HUB;
  
  static {
    ControlSystem controlSystem = new ControlSystem("REV_HUB", 1);
    REV_HUB = controlSystem;
    $VALUES = new ControlSystem[] { MODERN_ROBOTICS, controlSystem };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\ControlSystem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */