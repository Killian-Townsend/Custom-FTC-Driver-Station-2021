package org.firstinspires.ftc.robotcore.internal.camera.names;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class BuiltinCameraNameImpl extends CameraNameImplBase implements BuiltinCameraName {
  protected final VuforiaLocalizer.CameraDirection cameraDirection;
  
  private BuiltinCameraNameImpl(VuforiaLocalizer.CameraDirection paramCameraDirection) {
    this.cameraDirection = paramCameraDirection;
  }
  
  public static BuiltinCameraName forCameraDirection(VuforiaLocalizer.CameraDirection paramCameraDirection) {
    return new BuiltinCameraNameImpl(paramCameraDirection);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof BuiltinCameraNameImpl) {
      paramObject = paramObject;
      return this.cameraDirection.equals(((BuiltinCameraNameImpl)paramObject).cameraDirection);
    } 
    return super.equals(paramObject);
  }
  
  public VuforiaLocalizer.CameraDirection getCameraDirection() {
    return this.cameraDirection;
  }
  
  public int hashCode() {
    return this.cameraDirection.hashCode();
  }
  
  public boolean isCameraDirection() {
    return true;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("BuiltinCamera:");
    stringBuilder.append(this.cameraDirection);
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\names\BuiltinCameraNameImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */