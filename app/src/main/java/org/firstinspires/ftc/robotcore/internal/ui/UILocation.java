package org.firstinspires.ftc.robotcore.internal.ui;

public enum UILocation {
  BOTH, ONLY_LOCAL;
  
  static {
    UILocation uILocation = new UILocation("BOTH", 1);
    BOTH = uILocation;
    $VALUES = new UILocation[] { ONLY_LOCAL, uILocation };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\UILocation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */