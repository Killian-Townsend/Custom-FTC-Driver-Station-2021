package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.internal.camera.CameraImpl;

public class SwitchableCameraImpl extends CameraImpl implements SwitchableCamera {
  protected RefCountedSwitchableCamera refCountedSwitchableCamera;
  
  public SwitchableCameraImpl(RefCountedSwitchableCamera paramRefCountedSwitchableCamera) {
    super(paramRefCountedSwitchableCamera);
    this.refCountedSwitchableCamera = paramRefCountedSwitchableCamera;
  }
  
  public Camera dup() {
    return (Camera)new SwitchableCameraImpl(this.refCountedSwitchableCamera);
  }
  
  public CameraName getActiveCamera() {
    return this.refCountedSwitchableCamera.getActiveCamera();
  }
  
  public CameraName[] getMembers() {
    return this.refCountedSwitchableCamera.getMembers();
  }
  
  public void setActiveCamera(CameraName paramCameraName) {
    this.refCountedSwitchableCamera.setActiveCamera(paramCameraName);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\SwitchableCameraImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */