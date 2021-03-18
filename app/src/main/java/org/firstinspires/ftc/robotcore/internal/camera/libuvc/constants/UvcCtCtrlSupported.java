package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcCtCtrlSupported {
  AE_MODE, AE_PRIORITY, DIGITAL_WINDOW, EXPOSURE_TIME_ABSOLUTE, EXPOSURE_TIME_RELATIVE, FOCUS_ABSOLUTE, FOCUS_AUTO, FOCUS_RELATIVE, FOCUS_SIMPLE, IRIS_ABSOLUTE, IRIS_RELATIVE, PANTILT_ABSOLUTE, PANTILT_RELATIVE, PRIVACY, REGION_OF_INTEREST, RESERVED_0, RESERVED_1, RESERVED_2, RESERVED_3, ROLL_ABSOLUTE, ROLL_RELATIVE, SCANNING_MODE, ZOOM_ABSOLUTE, ZOOM_RELATIVE;
  
  static {
    AE_MODE = new UvcCtCtrlSupported("AE_MODE", 1);
    AE_PRIORITY = new UvcCtCtrlSupported("AE_PRIORITY", 2);
    EXPOSURE_TIME_ABSOLUTE = new UvcCtCtrlSupported("EXPOSURE_TIME_ABSOLUTE", 3);
    EXPOSURE_TIME_RELATIVE = new UvcCtCtrlSupported("EXPOSURE_TIME_RELATIVE", 4);
    FOCUS_ABSOLUTE = new UvcCtCtrlSupported("FOCUS_ABSOLUTE", 5);
    FOCUS_RELATIVE = new UvcCtCtrlSupported("FOCUS_RELATIVE", 6);
    IRIS_ABSOLUTE = new UvcCtCtrlSupported("IRIS_ABSOLUTE", 7);
    IRIS_RELATIVE = new UvcCtCtrlSupported("IRIS_RELATIVE", 8);
    ZOOM_ABSOLUTE = new UvcCtCtrlSupported("ZOOM_ABSOLUTE", 9);
    ZOOM_RELATIVE = new UvcCtCtrlSupported("ZOOM_RELATIVE", 10);
    PANTILT_ABSOLUTE = new UvcCtCtrlSupported("PANTILT_ABSOLUTE", 11);
    PANTILT_RELATIVE = new UvcCtCtrlSupported("PANTILT_RELATIVE", 12);
    ROLL_ABSOLUTE = new UvcCtCtrlSupported("ROLL_ABSOLUTE", 13);
    ROLL_RELATIVE = new UvcCtCtrlSupported("ROLL_RELATIVE", 14);
    RESERVED_0 = new UvcCtCtrlSupported("RESERVED_0", 15);
    RESERVED_1 = new UvcCtCtrlSupported("RESERVED_1", 16);
    FOCUS_AUTO = new UvcCtCtrlSupported("FOCUS_AUTO", 17);
    PRIVACY = new UvcCtCtrlSupported("PRIVACY", 18);
    FOCUS_SIMPLE = new UvcCtCtrlSupported("FOCUS_SIMPLE", 19);
    DIGITAL_WINDOW = new UvcCtCtrlSupported("DIGITAL_WINDOW", 20);
    REGION_OF_INTEREST = new UvcCtCtrlSupported("REGION_OF_INTEREST", 21);
    RESERVED_2 = new UvcCtCtrlSupported("RESERVED_2", 22);
    UvcCtCtrlSupported uvcCtCtrlSupported = new UvcCtCtrlSupported("RESERVED_3", 23);
    RESERVED_3 = uvcCtCtrlSupported;
    $VALUES = new UvcCtCtrlSupported[] { 
        SCANNING_MODE, AE_MODE, AE_PRIORITY, EXPOSURE_TIME_ABSOLUTE, EXPOSURE_TIME_RELATIVE, FOCUS_ABSOLUTE, FOCUS_RELATIVE, IRIS_ABSOLUTE, IRIS_RELATIVE, ZOOM_ABSOLUTE, 
        ZOOM_RELATIVE, PANTILT_ABSOLUTE, PANTILT_RELATIVE, ROLL_ABSOLUTE, ROLL_RELATIVE, RESERVED_0, RESERVED_1, FOCUS_AUTO, PRIVACY, FOCUS_SIMPLE, 
        DIGITAL_WINDOW, REGION_OF_INTEREST, RESERVED_2, uvcCtCtrlSupported };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcCtCtrlSupported.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */