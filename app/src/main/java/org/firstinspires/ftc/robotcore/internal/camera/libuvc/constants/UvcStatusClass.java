package org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants;

public enum UvcStatusClass {
  FAILURE_CHANGE,
  INFO_CHANGE,
  UNKNOWN,
  VALUE_CHANGE(0);
  
  int value;
  
  static {
    INFO_CHANGE = new UvcStatusClass("INFO_CHANGE", 1, 1);
    FAILURE_CHANGE = new UvcStatusClass("FAILURE_CHANGE", 2, 2);
    UvcStatusClass uvcStatusClass = new UvcStatusClass("UNKNOWN", 3, 255);
    UNKNOWN = uvcStatusClass;
    $VALUES = new UvcStatusClass[] { VALUE_CHANGE, INFO_CHANGE, FAILURE_CHANGE, uvcStatusClass };
  }
  
  UvcStatusClass(int paramInt1) {
    this.value = paramInt1;
  }
  
  static UvcStatusClass from(int paramInt) {
    for (UvcStatusClass uvcStatusClass : values()) {
      if (uvcStatusClass.value == paramInt)
        return uvcStatusClass; 
    } 
    return UNKNOWN;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\constants\UvcStatusClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */