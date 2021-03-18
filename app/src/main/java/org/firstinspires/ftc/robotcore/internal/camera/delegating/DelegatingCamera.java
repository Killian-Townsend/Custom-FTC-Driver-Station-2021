package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import android.content.Context;
import android.hardware.usb.UsbManager;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.ThrowingSupplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl;
import org.firstinspires.ftc.robotcore.internal.camera.CameraImpl;
import org.firstinspires.ftc.robotcore.internal.camera.CameraInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraState;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCameraHelper;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public abstract class DelegatingCamera extends RefCounted implements RefCountedCamera {
  public static final String TAG = "DelegatingCamera";
  
  public static boolean TRACE = true;
  
  protected final CameraManagerInternal cameraManager;
  
  protected final Context context;
  
  protected Camera delegatedCamera = null;
  
  protected CameraState delegatedCameraState = CameraState.Nascent;
  
  protected List<CameraControl> delegatingCameraControls = new ArrayList<CameraControl>();
  
  protected DelegatingCaptureSession delegatingCaptureSession = null;
  
  protected final DispatchingCallback dispatchingCallback;
  
  protected int nextCaptureSessionId = 1;
  
  protected final Executor openClosePool = ThreadPool.newSingleThreadExecutor("DelegatingCamera");
  
  protected final Object outerLock = this.lock;
  
  protected final RefCountedCameraHelper refCountedCameraHelper;
  
  protected Camera selfCamera = null;
  
  protected final CameraName selfCameraName;
  
  protected CameraState selfState = CameraState.Nascent;
  
  protected final Executor serialThreadPool;
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  protected final UsbManager usbManager;
  
  protected DelegatingCamera(CameraManagerInternal paramCameraManagerInternal, CameraName paramCameraName, Continuation<? extends Camera.StateCallback> paramContinuation) {
    Context context = AppUtil.getDefContext();
    this.context = context;
    this.usbManager = (UsbManager)context.getSystemService("usb");
    this.cameraManager = paramCameraManagerInternal;
    this.serialThreadPool = paramCameraManagerInternal.getSerialThreadPool();
    this.selfCameraName = paramCameraName;
    this.refCountedCameraHelper = new RefCountedCameraHelper(this, this.outerLock, this.tracer, new Runnable() {
          public void run() {
            DelegatingCamera.this.shutdown();
          }
        });
    this.dispatchingCallback = new DispatchingCallback(paramContinuation);
    constructControls();
  }
  
  public void addRefExternal() {
    synchronized (this.outerLock) {
      this.refCountedCameraHelper.addRefExternal();
      return;
    } 
  }
  
  protected final void changeDelegatedCamera(final Camera newCamera) {
    synchronized (this.outerLock) {
      if (this.delegatedCamera != newCamera)
        this.tracer.trace(this.tracer.format("changeDelegatedCamera(%s->%s)", new Object[] { this.delegatedCamera, newCamera }), new Runnable() {
              public void run() {
                DelegatingCamera.this.delegatedCamera = newCamera;
                DelegatingCamera.this.updateCameraHolders();
              }
            }); 
      return;
    } 
  }
  
  protected void closeCaptureSession() {
    synchronized (this.outerLock) {
      DelegatingCaptureSession delegatingCaptureSession2 = this.delegatingCaptureSession;
      DelegatingCaptureSession delegatingCaptureSession1 = null;
      if (delegatingCaptureSession2 != null) {
        delegatingCaptureSession1 = this.delegatingCaptureSession;
        this.delegatingCaptureSession = null;
      } 
      if (delegatingCaptureSession1 != null)
        delegatingCaptureSession1.close(); 
      return;
    } 
  }
  
  protected void closeDelegatedCameras() {
    synchronized (this.outerLock) {
      if (this.delegatedCamera != null) {
        this.delegatedCamera.close();
        changeDelegatedCamera((Camera)null);
      } 
      return;
    } 
  }
  
  protected abstract void constructControls();
  
  public CameraCaptureRequest createCaptureRequest(int paramInt1, Size paramSize, int paramInt2) throws CameraException {
    return (CameraCaptureRequest)new DelegatingCaptureRequest(this, paramInt1, paramSize, paramInt2);
  }
  
  public CameraCaptureSession createCaptureSession(final Continuation<? extends CameraCaptureSession.StateCallback> userContinuation) throws CameraException {
    return (CameraCaptureSession)this.tracer.trace("createCaptureSession()", new ThrowingSupplier<CameraCaptureSession, CameraException>() {
          public CameraCaptureSession get() throws CameraException {
            synchronized (DelegatingCamera.this.outerLock) {
              DelegatingCamera.this.closeCaptureSession();
              DelegatingCamera delegatingCamera1 = DelegatingCamera.this;
              DelegatingCamera delegatingCamera2 = DelegatingCamera.this;
              Continuation<? extends CameraCaptureSession.StateCallback> continuation = userContinuation;
              DelegatingCamera delegatingCamera3 = DelegatingCamera.this;
              int i = delegatingCamera3.nextCaptureSessionId;
              delegatingCamera3.nextCaptureSessionId = i + 1;
              delegatingCamera1.delegatingCaptureSession = new DelegatingCaptureSession(delegatingCamera2, continuation, i);
              DelegatingCamera.this.updateCameraHolders();
              return DelegatingCamera.this.delegatingCaptureSession;
            } 
          }
        });
  }
  
  protected void createSelfCamera() {
    this.selfCamera = (Camera)new CameraImpl(this);
  }
  
  protected void destructor() {
    shutdown();
    super.destructor();
  }
  
  public CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.lock) {
      if (this.delegatedCamera != null && this.delegatedCamera instanceof CameraInternal)
        return ((CameraInternal)this.delegatedCamera).getCalibration(paramCameraCalibrationManager, paramSize); 
      return null;
    } 
  }
  
  public CameraCalibrationIdentity getCalibrationIdentity() {
    synchronized (this.lock) {
      if (this.delegatedCamera != null && this.delegatedCamera instanceof CameraInternal)
        return ((CameraInternal)this.delegatedCamera).getCalibrationIdentity(); 
      return null;
    } 
  }
  
  public CameraName getCameraName() {
    return this.selfCameraName;
  }
  
  public <T extends CameraControl> T getControl(Class<T> paramClass) {
    synchronized (this.outerLock) {
      for (CameraControl cameraControl : this.delegatingCameraControls) {
        if (paramClass.isInstance(cameraControl))
          return paramClass.cast(cameraControl); 
      } 
      return null;
    } 
  }
  
  public String getExternalTraceIdentifier() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getTag());
    stringBuilder.append(getTraceIdentifier());
    return stringBuilder.toString();
  }
  
  public String getTag() {
    return "DelegatingCamera";
  }
  
  public Tracer getTracer() {
    return this.tracer;
  }
  
  public boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    synchronized (this.lock) {
      if (this.delegatedCamera != null && this.delegatedCamera instanceof CameraInternal)
        return ((CameraInternal)this.delegatedCamera).hasCalibration(paramCameraCalibrationManager, paramSize); 
      return false;
    } 
  }
  
  public void onClosed(DelegatingCaptureSession paramDelegatingCaptureSession) {}
  
  public final void openSelfAndReport() {
    this.tracer.trace("openSelfAndReport()", new Runnable() {
          public void run() {
            synchronized (DelegatingCamera.this.outerLock) {
              int i = DelegatingCamera.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[DelegatingCamera.this.selfState.ordinal()];
              if (i != 1) {
                if (i != 2)
                  throw new InternalError("openSelfAndReport"); 
              } else {
                DelegatingCamera.this.selfState = CameraState.OpenNotStarted;
                DelegatingCamera.this.createSelfCamera();
                DelegatingCamera.this.dispatchingCallback.onOpened(DelegatingCamera.this.selfCamera);
              } 
              return;
            } 
          }
        });
  }
  
  public int releaseRefExternal() {
    synchronized (this.outerLock) {
      return this.refCountedCameraHelper.releaseRefExternal();
    } 
  }
  
  public final void reportError(Camera.Error paramError) {
    Assert.assertNotNull(this.selfCamera);
    this.dispatchingCallback.onError(this.selfCamera, paramError);
  }
  
  public final void reportOpenFailed(Camera.OpenFailure paramOpenFailure) {
    synchronized (this.outerLock) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[this.selfState.ordinal()];
      if (i != 1) {
        if (i != 3)
          throw new InternalError("reportOpenFailed"); 
      } else {
        this.selfState = CameraState.FailedOpen;
        this.dispatchingCallback.onOpenFailed(this.selfCameraName, paramOpenFailure);
      } 
      return;
    } 
  }
  
  public final void reportSelfClosed() {
    synchronized (this.outerLock) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[this.selfState.ordinal()];
      if (i != 3 && i != 4) {
        this.selfState = CameraState.Closed;
        this.dispatchingCallback.onClosed(this.selfCamera);
      } 
      return;
    } 
  }
  
  protected void shutdown() {
    closeCaptureSession();
    reportSelfClosed();
    closeDelegatedCameras();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getTag());
    stringBuilder.append("(");
    stringBuilder.append(this.selfCameraName);
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  protected final void updateCameraHolders() {
    synchronized (this.outerLock) {
      if (this.delegatingCaptureSession != null)
        this.delegatingCaptureSession.onCameraChanged(this.delegatedCamera); 
      for (CameraControl cameraControl : this.delegatingCameraControls) {
        if (cameraControl instanceof DelegatingCameraControl)
          ((DelegatingCameraControl)cameraControl).onCameraChanged(this.delegatedCamera); 
      } 
      return;
    } 
  }
  
  protected class DispatchingCallback implements Camera.StateCallback {
    public final Continuation<? extends Camera.StateCallback> userContinuation;
    
    public DispatchingCallback(Continuation<? extends Camera.StateCallback> param1Continuation) {
      this.userContinuation = param1Continuation;
    }
    
    public void onClosed(final Camera camera) {
      CameraImpl.addRefCamera(camera);
      this.userContinuation.dispatch(new ContinuationResult<Camera.StateCallback>() {
            public void handle(Camera.StateCallback param2StateCallback) {
              param2StateCallback.onClosed(camera);
              CameraImpl.releaseRefCamera(camera);
            }
          });
    }
    
    public void onError(final Camera camera, final Camera.Error error) {
      CameraImpl.addRefCamera(camera);
      this.userContinuation.dispatch(new ContinuationResult<Camera.StateCallback>() {
            public void handle(Camera.StateCallback param2StateCallback) {
              param2StateCallback.onError(camera, error);
              CameraImpl.releaseRefCamera(camera);
            }
          });
    }
    
    public void onOpenFailed(final CameraName cameraName, final Camera.OpenFailure failureReason) {
      this.userContinuation.dispatch(new ContinuationResult<Camera.StateCallback>() {
            public void handle(Camera.StateCallback param2StateCallback) {
              param2StateCallback.onOpenFailed(cameraName, failureReason);
            }
          });
    }
    
    public void onOpened(final Camera camera) {
      CameraImpl.addRefCamera(camera);
      this.userContinuation.dispatch(new ContinuationResult<Camera.StateCallback>() {
            public void handle(Camera.StateCallback param2StateCallback) {
              param2StateCallback.onOpened(camera);
              CameraImpl.releaseRefCamera(camera);
            }
          });
    }
  }
  
  class null implements ContinuationResult<Camera.StateCallback> {
    public void handle(Camera.StateCallback param1StateCallback) {
      param1StateCallback.onOpened(camera);
      CameraImpl.releaseRefCamera(camera);
    }
  }
  
  class null implements ContinuationResult<Camera.StateCallback> {
    public void handle(Camera.StateCallback param1StateCallback) {
      param1StateCallback.onOpenFailed(cameraName, failureReason);
    }
  }
  
  class null implements ContinuationResult<Camera.StateCallback> {
    public void handle(Camera.StateCallback param1StateCallback) {
      param1StateCallback.onClosed(camera);
      CameraImpl.releaseRefCamera(camera);
    }
  }
  
  class null implements ContinuationResult<Camera.StateCallback> {
    public void handle(Camera.StateCallback param1StateCallback) {
      param1StateCallback.onError(camera, error);
      CameraImpl.releaseRefCamera(camera);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\DelegatingCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */