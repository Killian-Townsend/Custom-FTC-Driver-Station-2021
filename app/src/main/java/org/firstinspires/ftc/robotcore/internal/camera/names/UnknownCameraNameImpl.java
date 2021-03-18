package org.firstinspires.ftc.robotcore.internal.camera.names;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;

public class UnknownCameraNameImpl extends CameraNameImplBase {
  public static CameraName forUnknown() {
    return new UnknownCameraNameImpl();
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject instanceof UnknownCameraNameImpl) ? true : super.equals(paramObject);
  }
  
  public int hashCode() {
    return UnknownCameraNameImpl.class.hashCode();
  }
  
  public boolean isUnknown() {
    return true;
  }
  
  public String toString() {
    return "UnknownCamera";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\names\UnknownCameraNameImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */