package org.firstinspires.ftc.robotcore.external.navigation;

public enum Rotation {
  CCW, CW;
  
  static {
    Rotation rotation = new Rotation("CCW", 1);
    CCW = rotation;
    $VALUES = new Rotation[] { CW, rotation };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Rotation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */