package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcAutoExposureMode {
  APERTURE_PRIORITY,
  AUTO,
  MANUAL(1),
  SHUTTER_PRIORITY(1);
  
  private byte value;
  
  static {
    AUTO = new UvcAutoExposureMode("AUTO", 1, 2);
    SHUTTER_PRIORITY = new UvcAutoExposureMode("SHUTTER_PRIORITY", 2, 4);
    UvcAutoExposureMode uvcAutoExposureMode = new UvcAutoExposureMode("APERTURE_PRIORITY", 3, 8);
    APERTURE_PRIORITY = uvcAutoExposureMode;
    $VALUES = new UvcAutoExposureMode[] { MANUAL, AUTO, SHUTTER_PRIORITY, uvcAutoExposureMode };
  }
  
  UvcAutoExposureMode(int paramInt1) {
    this.value = (byte)paramInt1;
  }
  
  public byte getValue() {
    return this.value;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcAutoExposureMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */