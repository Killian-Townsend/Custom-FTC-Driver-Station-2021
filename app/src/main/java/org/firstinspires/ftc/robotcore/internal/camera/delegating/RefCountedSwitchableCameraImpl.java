package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraState;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class RefCountedSwitchableCameraImpl extends DelegatingCamera implements RefCountedSwitchableCamera {
  public static final String TAG = "SwitchableCamImpl";
  
  protected CameraName activeCameraName;
  
  protected CountDownLatch awaitAllCamerasOpenOrOpenFailed;
  
  protected final Map<CameraName, SwitchableMemberInfo> cameraInfos = new HashMap<CameraName, SwitchableMemberInfo>();
  
  protected final SwitchableCameraName selfSwitchableCameraName;
  
  public RefCountedSwitchableCameraImpl(CameraManagerInternal paramCameraManagerInternal, SwitchableCameraName paramSwitchableCameraName, CameraName[] paramArrayOfCameraName, Continuation<? extends Camera.StateCallback> paramContinuation) {
    super(paramCameraManagerInternal, paramSwitchableCameraName, paramContinuation);
    this.selfSwitchableCameraName = paramSwitchableCameraName;
    int j = paramArrayOfCameraName.length;
    int i;
    for (i = 0; i < j; i++) {
      CameraName cameraName = paramArrayOfCameraName[i];
      this.cameraInfos.put(cameraName, new SwitchableMemberInfo(this, cameraName));
    } 
    this.activeCameraName = paramArrayOfCameraName[0];
  }
  
  protected boolean allCamerasOpen() {
    synchronized (this.outerLock) {
      Iterator<SwitchableMemberInfo> iterator = this.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        if (!((SwitchableMemberInfo)iterator.next()).isOpen())
          return false; 
      } 
      return true;
    } 
  }
  
  protected boolean anyCamerasOpen() {
    synchronized (this.outerLock) {
      Iterator<SwitchableMemberInfo> iterator = this.cameraInfos.values().iterator();
      while (iterator.hasNext()) {
        if (((SwitchableMemberInfo)iterator.next()).isOpen())
          return true; 
      } 
      return false;
    } 
  }
  
  protected void closeDelegatedCameras() {
    synchronized (this.outerLock) {
      Iterator<SwitchableMemberInfo> iterator = this.cameraInfos.values().iterator();
      while (iterator.hasNext())
        ((SwitchableMemberInfo)iterator.next()).closeCamera(); 
      return;
    } 
  }
  
  protected void constructControls() {
    this.delegatingCameraControls.add(new SwitchableFocusControl(this));
    this.delegatingCameraControls.add(new SwitchableExposureControl(this));
  }
  
  protected void createSelfCamera() {
    this.selfCamera = (Camera)new SwitchableCameraImpl(this);
  }
  
  public CameraName getActiveCamera() {
    synchronized (this.outerLock) {
      return this.activeCameraName;
    } 
  }
  
  public CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.outerLock) {
      return getCurrentInfo().getCalibration(paramCameraCalibrationManager, paramSize);
    } 
  }
  
  public CameraCalibrationIdentity getCalibrationIdentity() {
    synchronized (this.outerLock) {
      return getCurrentInfo().getCalibrationIdentity();
    } 
  }
  
  protected SwitchableMemberInfo getCurrentInfo() {
    synchronized (this.outerLock) {
      return this.cameraInfos.get(this.activeCameraName);
    } 
  }
  
  public CameraName[] getMembers() {
    synchronized (this.outerLock) {
      return this.selfSwitchableCameraName.getMembers();
    } 
  }
  
  public String getTag() {
    return "SwitchableCamImpl";
  }
  
  public boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.outerLock) {
      return getCurrentInfo().hasCalibration(paramCameraCalibrationManager, paramSize);
    } 
  }
  
  public void memberClosed(SwitchableMemberInfo paramSwitchableMemberInfo) {
    synchronized (this.outerLock) {
      closeDelegatedCameras();
      if (!anyCamerasOpen())
        reportSelfClosed(); 
      return;
    } 
  }
  
  public void memberError(SwitchableMemberInfo paramSwitchableMemberInfo, Camera.Error paramError) {
    reportError(paramError);
  }
  
  public void memberOpenedOrFailedOpen(SwitchableMemberInfo paramSwitchableMemberInfo) {
    this.awaitAllCamerasOpenOrOpenFailed.countDown();
  }
  
  public void openAssumingPermission(long paramLong, TimeUnit paramTimeUnit) {
    synchronized (this.outerLock) {
      Camera.OpenFailure openFailure1;
      Camera.OpenFailure openFailure2;
      switch (this.selfState) {
        case null:
          Assert.assertNull(this.delegatedCamera);
          openFailure2 = Camera.OpenFailure.None;
          this.awaitAllCamerasOpenOrOpenFailed = new CountDownLatch(this.cameraInfos.size());
          openFailure1 = openFailure2;
          try {
            Iterator<SwitchableMemberInfo> iterator = this.cameraInfos.values().iterator();
            while (true) {
              openFailure1 = openFailure2;
              if (iterator.hasNext()) {
                openFailure1 = openFailure2;
                SwitchableMemberInfo switchableMemberInfo = iterator.next();
                openFailure1 = openFailure2;
                this.tracer.trace("async opening %s", new Object[] { switchableMemberInfo.getCameraName() });
                openFailure1 = openFailure2;
                this.cameraManager.asyncOpenCameraAssumingPermission(switchableMemberInfo.getCameraName(), Continuation.create(this.serialThreadPool, switchableMemberInfo), paramLong, paramTimeUnit);
                continue;
              } 
              openFailure1 = openFailure2;
              this.awaitAllCamerasOpenOrOpenFailed.await();
              openFailure1 = openFailure2;
              if (allCamerasOpen()) {
                openFailure1 = openFailure2;
                changeDelegatedCamera(getCurrentInfo().getCamera());
                openFailure1 = openFailure2;
                openSelfAndReport();
              } 
              if (this.selfState != CameraState.OpenNotStarted) {
                reportOpenFailed(openFailure2);
              } else {
                break;
              } 
              closeDelegatedCameras();
              break;
            } 
            break;
          } catch (InterruptedException interruptedException) {
            openFailure1 = openFailure2;
          } catch (RuntimeException runtimeException) {
            openFailure1 = openFailure2;
            Camera.OpenFailure openFailure = Camera.OpenFailure.InternalError;
            openFailure1 = openFailure;
            this.tracer.traceError(runtimeException, "failure opening camera: %s", new Object[] { this.selfCameraName });
            if (this.selfState != CameraState.OpenNotStarted) {
              reportOpenFailed(openFailure);
            } else {
              break;
            } 
          } finally {}
          closeDelegatedCameras();
          break;
        case null:
        case null:
        case null:
        case null:
        case null:
          throw Misc.illegalStateException("attempt to open camera %s in state %s", new Object[] { this.selfCameraName, this.selfState });
      } 
      return;
    } 
  }
  
  public void setActiveCamera(CameraName paramCameraName) {
    synchronized (this.outerLock) {
      if (this.cameraInfos.containsKey(paramCameraName)) {
        this.activeCameraName = paramCameraName;
        changeDelegatedCamera(getCurrentInfo().getCamera());
        return;
      } 
      throw Misc.illegalArgumentException("%s is not one of the cameras in this switcher", new Object[] { paramCameraName });
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\RefCountedSwitchableCameraImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */