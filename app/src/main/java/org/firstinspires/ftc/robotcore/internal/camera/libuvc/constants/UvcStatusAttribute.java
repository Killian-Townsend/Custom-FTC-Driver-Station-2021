package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcStatusAttribute {
  CONTROL,
  CONTROL_CAMERA,
  CONTROL_PROCESSING,
  UNKNOWN(0);
  
  int value;
  
  static {
    CONTROL = new UvcStatusAttribute("CONTROL", 1, 16);
    CONTROL_CAMERA = new UvcStatusAttribute("CONTROL_CAMERA", 2, 17);
    UvcStatusAttribute uvcStatusAttribute = new UvcStatusAttribute("CONTROL_PROCESSING", 3, 18);
    CONTROL_PROCESSING = uvcStatusAttribute;
    $VALUES = new UvcStatusAttribute[] { UNKNOWN, CONTROL, CONTROL_CAMERA, uvcStatusAttribute };
  }
  
  UvcStatusAttribute(int paramInt1) {
    this.value = paramInt1;
  }
  
  static UvcStatusAttribute from(int paramInt) {
    for (UvcStatusAttribute uvcStatusAttribute : values()) {
      if (uvcStatusAttribute.value == paramInt)
        return uvcStatusAttribute; 
    } 
    return UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcStatusAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */