package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraControls;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraState;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;

class SwitchableMemberInfo implements Camera.StateCallback, CameraControls {
  private Camera camera = null;
  
  private final CameraName cameraName;
  
  private CameraState cameraState = CameraState.Nascent;
  
  private final Object localLock = new Object();
  
  private RefCountedSwitchableCameraImpl switchableCamera;
  
  public SwitchableMemberInfo(RefCountedSwitchableCameraImpl paramRefCountedSwitchableCameraImpl, CameraName paramCameraName) {
    this.switchableCamera = paramRefCountedSwitchableCameraImpl;
    this.cameraName = paramCameraName;
  }
  
  public void closeCamera() {
    synchronized (this.localLock) {
      if (this.camera != null) {
        this.camera.close();
        this.camera = null;
      } 
      return;
    } 
  }
  
  public CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.localLock) {
      if (this.camera != null && this.camera instanceof CameraInternal)
        return ((CameraInternal)this.camera).getCalibration(paramCameraCalibrationManager, paramSize); 
      return null;
    } 
  }
  
  public CameraCalibrationIdentity getCalibrationIdentity() {
    synchronized (this.localLock) {
      if (this.camera != null && this.camera instanceof CameraInternal)
        return ((CameraInternal)this.camera).getCalibrationIdentity(); 
      return null;
    } 
  }
  
  public Camera getCamera() {
    synchronized (this.localLock) {
      return this.camera;
    } 
  }
  
  public CameraName getCameraName() {
    return this.cameraName;
  }
  
  public <T extends org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl> T getControl(Class<T> paramClass) {
    synchronized (this.localLock) {
      if (this.camera != null)
        return (T)this.camera.getControl(paramClass); 
      return null;
    } 
  }
  
  public boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.localLock) {
      if (this.camera != null && this.camera instanceof CameraInternal)
        return ((CameraInternal)this.camera).hasCalibration(paramCameraCalibrationManager, paramSize); 
      return false;
    } 
  }
  
  public boolean isOpen() {
    synchronized (this.localLock) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[this.cameraState.ordinal()];
      if (i != 1 && i != 2)
        return false; 
      return true;
    } 
  }
  
  public void onClosed(Camera paramCamera) {
    synchronized (this.localLock) {
      this.cameraState = CameraState.Closed;
      this.switchableCamera.memberClosed(this);
      return;
    } 
  }
  
  public void onError(Camera paramCamera, Camera.Error paramError) {
    this.switchableCamera.memberError(this, paramError);
  }
  
  public void onOpenFailed(CameraName paramCameraName, Camera.OpenFailure paramOpenFailure) {
    synchronized (this.localLock) {
      this.camera = null;
      this.cameraState = CameraState.FailedOpen;
      this.switchableCamera.memberOpenedOrFailedOpen(this);
      return;
    } 
  }
  
  public void onOpened(Camera paramCamera) {
    synchronized (this.localLock) {
      this.camera = paramCamera;
      this.cameraState = CameraState.OpenNotStarted;
      this.switchableCamera.memberOpenedOrFailedOpen(this);
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\SwitchableMemberInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */