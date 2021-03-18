package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

public enum FocusMode {
  AUTO, CONTINUOUS_AUTO, FIXED, INFINITY_FOCUS, MACRO, UNKNOWN;
  
  static {
    AUTO = new FocusMode("AUTO", 1);
    CONTINUOUS_AUTO = new FocusMode("CONTINUOUS_AUTO", 2);
    MACRO = new FocusMode("MACRO", 3);
    INFINITY_FOCUS = new FocusMode("INFINITY_FOCUS", 4);
    FocusMode focusMode = new FocusMode("FIXED", 5);
    FIXED = focusMode;
    $VALUES = new FocusMode[] { UNKNOWN, AUTO, CONTINUOUS_AUTO, MACRO, INFINITY_FOCUS, focusMode };
  }
  
  public static FocusMode from(int paramInt) {
    return (paramInt >= 0 && paramInt < (values()).length) ? values()[paramInt] : UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\FocusMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */