package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcCtCtrlSelector {
  AE_MODE, AE_PRIORITY, CONTROL_UNDEFINED, DIGITAL_WINDOW, EXPOSURE_TIME_ABSOLUTE, EXPOSURE_TIME_RELATIVE, FOCUS_ABSOLUTE, FOCUS_AUTO, FOCUS_RELATIVE, FOCUS_SIMPLE, IRIS_ABSOLUTE, IRIS_RELATIVE, PANTILT_ABSOLUTE, PANTILT_RELATIVE, PRIVACY, REGION_OF_INTEREST, ROLL_ABSOLUTE, ROLL_RELATIVE, SCANNING_MODE, ZOOM_ABSOLUTE, ZOOM_RELATIVE;
  
  static {
    AE_MODE = new UvcCtCtrlSelector("AE_MODE", 2);
    AE_PRIORITY = new UvcCtCtrlSelector("AE_PRIORITY", 3);
    EXPOSURE_TIME_ABSOLUTE = new UvcCtCtrlSelector("EXPOSURE_TIME_ABSOLUTE", 4);
    EXPOSURE_TIME_RELATIVE = new UvcCtCtrlSelector("EXPOSURE_TIME_RELATIVE", 5);
    FOCUS_ABSOLUTE = new UvcCtCtrlSelector("FOCUS_ABSOLUTE", 6);
    FOCUS_RELATIVE = new UvcCtCtrlSelector("FOCUS_RELATIVE", 7);
    FOCUS_AUTO = new UvcCtCtrlSelector("FOCUS_AUTO", 8);
    IRIS_ABSOLUTE = new UvcCtCtrlSelector("IRIS_ABSOLUTE", 9);
    IRIS_RELATIVE = new UvcCtCtrlSelector("IRIS_RELATIVE", 10);
    ZOOM_ABSOLUTE = new UvcCtCtrlSelector("ZOOM_ABSOLUTE", 11);
    ZOOM_RELATIVE = new UvcCtCtrlSelector("ZOOM_RELATIVE", 12);
    PANTILT_ABSOLUTE = new UvcCtCtrlSelector("PANTILT_ABSOLUTE", 13);
    PANTILT_RELATIVE = new UvcCtCtrlSelector("PANTILT_RELATIVE", 14);
    ROLL_ABSOLUTE = new UvcCtCtrlSelector("ROLL_ABSOLUTE", 15);
    ROLL_RELATIVE = new UvcCtCtrlSelector("ROLL_RELATIVE", 16);
    PRIVACY = new UvcCtCtrlSelector("PRIVACY", 17);
    FOCUS_SIMPLE = new UvcCtCtrlSelector("FOCUS_SIMPLE", 18);
    DIGITAL_WINDOW = new UvcCtCtrlSelector("DIGITAL_WINDOW", 19);
    UvcCtCtrlSelector uvcCtCtrlSelector = new UvcCtCtrlSelector("REGION_OF_INTEREST", 20);
    REGION_OF_INTEREST = uvcCtCtrlSelector;
    $VALUES = new UvcCtCtrlSelector[] { 
        CONTROL_UNDEFINED, SCANNING_MODE, AE_MODE, AE_PRIORITY, EXPOSURE_TIME_ABSOLUTE, EXPOSURE_TIME_RELATIVE, FOCUS_ABSOLUTE, FOCUS_RELATIVE, FOCUS_AUTO, IRIS_ABSOLUTE, 
        IRIS_RELATIVE, ZOOM_ABSOLUTE, ZOOM_RELATIVE, PANTILT_ABSOLUTE, PANTILT_RELATIVE, ROLL_ABSOLUTE, ROLL_RELATIVE, PRIVACY, FOCUS_SIMPLE, DIGITAL_WINDOW, 
        uvcCtCtrlSelector };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcCtCtrlSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */