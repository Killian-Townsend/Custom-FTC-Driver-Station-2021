package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcSimpleFocusMode {
  FULL_RANGE, MACRO, PEOPLE, SCENE;
  
  static {
    UvcSimpleFocusMode uvcSimpleFocusMode = new UvcSimpleFocusMode("SCENE", 3);
    SCENE = uvcSimpleFocusMode;
    $VALUES = new UvcSimpleFocusMode[] { FULL_RANGE, MACRO, PEOPLE, uvcSimpleFocusMode };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcSimpleFocusMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */