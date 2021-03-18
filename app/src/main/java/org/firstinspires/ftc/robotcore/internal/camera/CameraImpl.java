package org.firstinspires.ftc.robotcore.internal.camera;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.CloseableDestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

public class CameraImpl extends CloseableDestructOnFinalize implements Camera, CameraInternal {
  protected boolean ownExternalRef;
  
  protected RefCountedCamera refCountedCamera;
  
  public CameraImpl(RefCountedCamera paramRefCountedCamera) {
    this.refCountedCamera = paramRefCountedCamera;
    paramRefCountedCamera.addRefExternal();
    this.ownExternalRef = true;
    enableOnlyClose();
  }
  
  public static void addRefCamera(Camera paramCamera) {
    if (paramCamera instanceof RefCounted)
      ((RefCounted)paramCamera).addRef(); 
  }
  
  public static void closeCamera(String paramString, Camera paramCamera) {
    RobotLog.vv("CameraManager", "%s closing camera: %s", new Object[] { paramString, paramCamera });
    paramCamera.close();
  }
  
  public static void releaseRefCamera(Camera paramCamera) {
    if (paramCamera instanceof RefCounted)
      ((RefCounted)paramCamera).releaseRef(); 
  }
  
  public CameraCaptureRequest createCaptureRequest(int paramInt1, Size paramSize, int paramInt2) throws CameraException {
    synchronized (this.lock) {
      return this.refCountedCamera.createCaptureRequest(paramInt1, paramSize, paramInt2);
    } 
  }
  
  public CameraCaptureSession createCaptureSession(Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation) throws CameraException {
    synchronized (this.lock) {
      return this.refCountedCamera.createCaptureSession(paramContinuation);
    } 
  }
  
  protected void destructor() {
    this.refCountedCamera.getTracer().trace("CameraImpl.destructor()", new Runnable() {
          public void run() {
            Assert.assertTrue(CameraImpl.this.ownExternalRef);
            if (CameraImpl.this.ownExternalRef) {
              CameraImpl.this.ownExternalRef = false;
              CameraImpl.this.refCountedCamera.releaseRefExternal();
            } 
            CameraImpl.this.destructor();
          }
        });
  }
  
  public Camera dup() {
    RobotLog.vv(getTag(), "dup()");
    return new CameraImpl(this.refCountedCamera);
  }
  
  public CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    return this.refCountedCamera.getCalibration(paramCameraCalibrationManager, paramSize);
  }
  
  public CameraCalibrationIdentity getCalibrationIdentity() {
    return this.refCountedCamera.getCalibrationIdentity();
  }
  
  public CameraName getCameraName() {
    synchronized (this.lock) {
      return this.refCountedCamera.getCameraName();
    } 
  }
  
  public <T extends org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl> T getControl(Class<T> paramClass) {
    synchronized (this.lock) {
      return (T)this.refCountedCamera.getControl(paramClass);
    } 
  }
  
  public boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    return this.refCountedCamera.hasCalibration(paramCameraCalibrationManager, paramSize);
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%s)", new Object[] { getClass().getSimpleName(), this.refCountedCamera });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */