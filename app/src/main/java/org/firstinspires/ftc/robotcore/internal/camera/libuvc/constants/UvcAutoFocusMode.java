package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcAutoFocusMode {
  AUTO_FOCUS_MODE_AUTO, AUTO_FOCUS_MODE_FIXED;
  
  static {
    UvcAutoFocusMode uvcAutoFocusMode = new UvcAutoFocusMode("AUTO_FOCUS_MODE_AUTO", 1);
    AUTO_FOCUS_MODE_AUTO = uvcAutoFocusMode;
    $VALUES = new UvcAutoFocusMode[] { AUTO_FOCUS_MODE_FIXED, uvcAutoFocusMode };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcAutoFocusMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */