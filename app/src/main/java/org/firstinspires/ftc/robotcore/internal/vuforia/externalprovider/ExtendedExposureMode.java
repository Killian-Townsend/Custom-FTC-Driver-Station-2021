package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

public enum ExtendedExposureMode {
  APERTURE_PRIORITY, AUTO, CONTINUOUS_AUTO, MANUAL, SHUTTER_PRIORITY, UNKNOWN;
  
  static {
    AUTO = new ExtendedExposureMode("AUTO", 1);
    CONTINUOUS_AUTO = new ExtendedExposureMode("CONTINUOUS_AUTO", 2);
    MANUAL = new ExtendedExposureMode("MANUAL", 3);
    SHUTTER_PRIORITY = new ExtendedExposureMode("SHUTTER_PRIORITY", 4);
    ExtendedExposureMode extendedExposureMode = new ExtendedExposureMode("APERTURE_PRIORITY", 5);
    APERTURE_PRIORITY = extendedExposureMode;
    $VALUES = new ExtendedExposureMode[] { UNKNOWN, AUTO, CONTINUOUS_AUTO, MANUAL, SHUTTER_PRIORITY, extendedExposureMode };
  }
  
  public static ExtendedExposureMode from(int paramInt) {
    return (paramInt >= 0 && paramInt < (values()).length) ? values()[paramInt] : UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\ExtendedExposureMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */