package org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.CameraControl;
import org.firstinspires.ftc.robotcore.internal.camera.CameraImpl;
import org.firstinspires.ftc.robotcore.internal.camera.CameraState;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCameraHelper;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.VendorProductCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraCaptureRequest;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCaptureSession;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiExposureControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiFocusControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcAutoExposureMode;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.ExtendedExposureMode;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.FocusMode;

public class UvcDeviceHandle extends NativeObject<UvcDevice> implements RefCountedCamera {
  public static final String TAG = UvcDeviceHandle.class.getSimpleName();
  
  public static boolean TRACE = true;
  
  protected List<CameraControl> cameraControls = new ArrayList<CameraControl>();
  
  protected CameraCaptureSession currentCaptureSession = null;
  
  protected int nextCaptureSessionId = 1;
  
  protected RefCountedCameraHelper refCountedCameraHelper;
  
  protected boolean reportClosedCalled = false;
  
  protected Camera selfCamera;
  
  protected CameraState selfState = CameraState.Nascent;
  
  protected Camera.StateCallback stateCallback = null;
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  public UvcDeviceHandle(long paramLong, UvcDevice paramUvcDevice, Camera.StateCallback paramStateCallback) {
    super(paramLong);
    setParent((RefCounted)paramUvcDevice);
    this.stateCallback = paramStateCallback;
    constructControls();
    this.refCountedCameraHelper = new RefCountedCameraHelper((RefCounted)this, this.lock, this.tracer, new Runnable() {
          public void run() {
            UvcDeviceHandle.this.shutdown();
          }
        });
  }
  
  protected static native void nativeAddRefDeviceHandle(long paramLong);
  
  protected static native String nativeGetDiagnostics(long paramLong);
  
  protected static native long nativeGetExposure(long paramLong);
  
  protected static native double nativeGetFocusLength(long paramLong);
  
  protected static native long nativeGetMaxExposure(long paramLong);
  
  protected static native double nativeGetMaxFocusLength(long paramLong);
  
  protected static native long nativeGetMinExposure(long paramLong);
  
  protected static native double nativeGetMinFocusLength(long paramLong);
  
  protected static native int nativeGetStreamControlFormatSize(long paramLong1, long paramLong2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  protected static native int nativeGetVuforiaExposureMode(long paramLong);
  
  protected static native int nativeGetVuforiaFocusMode(long paramLong);
  
  protected static native boolean nativeIsExposureSupported(long paramLong);
  
  protected static native boolean nativeIsFocusLengthSupported(long paramLong);
  
  protected static native boolean nativeIsVuforiaExposureModeSupported(long paramLong, int paramInt);
  
  protected static native boolean nativeIsVuforiaFocusModeSupported(long paramLong, int paramInt);
  
  protected static native void nativeReleaseRefDeviceHandle(long paramLong);
  
  protected static native void nativeSetAutoExposure(long paramLong, byte paramByte);
  
  protected static native boolean nativeSetExposure(long paramLong1, long paramLong2);
  
  protected static native boolean nativeSetFocusLength(long paramLong, double paramDouble);
  
  protected static native boolean nativeSetVuforiaExposureMode(long paramLong, int paramInt);
  
  protected static native boolean nativeSetVuforiaFocusMode(long paramLong, int paramInt);
  
  protected static native int nativeStopAllStreaming(long paramLong);
  
  public void addRefExternal() {
    synchronized (this.lock) {
      this.refCountedCameraHelper.addRefExternal();
      return;
    } 
  }
  
  protected void closeCaptureSession() {
    synchronized (this.lock) {
      CameraCaptureSession cameraCaptureSession2 = this.currentCaptureSession;
      CameraCaptureSession cameraCaptureSession1 = null;
      if (cameraCaptureSession2 != null) {
        cameraCaptureSession1 = this.currentCaptureSession;
        this.currentCaptureSession = null;
      } 
      if (cameraCaptureSession1 != null)
        cameraCaptureSession1.close(); 
      return;
    } 
  }
  
  void constructControls() {
    this.cameraControls.add(new UvcApiFocusControl(this));
    this.cameraControls.add(new UvcApiExposureControl(this));
  }
  
  public CameraCaptureRequest createCaptureRequest(int paramInt1, Size paramSize, int paramInt2) throws CameraException {
    return (CameraCaptureRequest)new UvcApiCameraCaptureRequest(this, paramInt1, paramSize, paramInt2);
  }
  
  public CameraCaptureSession createCaptureSession(Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation) throws CameraException {
    synchronized (this.lock) {
      closeCaptureSession();
      int i = this.nextCaptureSessionId;
      this.nextCaptureSessionId = i + 1;
      UvcApiCaptureSession uvcApiCaptureSession = new UvcApiCaptureSession(this, paramContinuation, i);
      this.currentCaptureSession = (CameraCaptureSession)uvcApiCaptureSession;
      return (CameraCaptureSession)uvcApiCaptureSession;
    } 
  }
  
  protected void createSelfCamera() {
    this.selfCamera = (Camera)new CameraImpl(this);
  }
  
  protected void destructor() {
    this.tracer.trace("destructor", new Runnable() {
          public void run() {
            UvcDeviceHandle.this.shutdown();
            if (UvcDeviceHandle.this.pointer != 0L) {
              UvcDeviceHandle.nativeReleaseRefDeviceHandle(UvcDeviceHandle.this.pointer);
              UvcDeviceHandle.this.clearPointer();
            } 
            UvcDeviceHandle.this.destructor();
          }
        });
  }
  
  public long getAddRefdPointer() {
    synchronized (this.lock) {
      if (this.pointer != 0L)
        nativeAddRefDeviceHandle(this.pointer); 
      return this.pointer;
    } 
  }
  
  public CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    return paramCameraCalibrationManager.getCalibration(getCalibrationIdentity(), paramSize);
  }
  
  public CameraCalibrationIdentity getCalibrationIdentity() {
    return (CameraCalibrationIdentity)new VendorProductCalibrationIdentity(getVendorId(), getProductId());
  }
  
  public WebcamName getCameraName() {
    return ((UvcDevice)getParent()).getWebcamName();
  }
  
  public <T extends CameraControl> T getControl(Class<T> paramClass) {
    synchronized (this.lock) {
      for (CameraControl cameraControl : this.cameraControls) {
        if (paramClass.isInstance(cameraControl))
          return paramClass.cast(cameraControl); 
      } 
      return null;
    } 
  }
  
  public long getExposure() {
    synchronized (this.lock) {
      return nativeGetExposure(this.pointer);
    } 
  }
  
  public String getExternalTraceIdentifier() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("UvcDeviceHandle");
    stringBuilder.append(getTraceIdentifier());
    return stringBuilder.toString();
  }
  
  public double getFocusLength() {
    synchronized (this.lock) {
      return nativeGetFocusLength(this.pointer);
    } 
  }
  
  public long getMaxExposure() {
    synchronized (this.lock) {
      return nativeGetMaxExposure(this.pointer);
    } 
  }
  
  public double getMaxFocusLength() {
    synchronized (this.lock) {
      return nativeGetMaxFocusLength(this.pointer);
    } 
  }
  
  public long getMinExposure() {
    synchronized (this.lock) {
      return nativeGetMinExposure(this.pointer);
    } 
  }
  
  public double getMinFocusLength() {
    synchronized (this.lock) {
      return nativeGetMinFocusLength(this.pointer);
    } 
  }
  
  public long getPointer() {
    return this.pointer;
  }
  
  public int getProductId() {
    return ((UvcDevice)getParent()).getProductId();
  }
  
  public Camera getSelfCamera() {
    return this.selfCamera;
  }
  
  public UvcStreamCtrl getStreamControl(final UvcFrameFormat uvcFrameFormat, final int width, final int height, final int fps) {
    synchronized (this.lock) {
      return (UvcStreamCtrl)this.tracer.trace(this.tracer.format("getStreamControl(%dx%d %d)", new Object[] { Integer.valueOf(width), Integer.valueOf(height), Integer.valueOf(fps) }), new Supplier<UvcStreamCtrl>() {
            public UvcStreamCtrl get() {
              if (uvcFrameFormat != null) {
                UvcStreamCtrl uvcStreamCtrl = new UvcStreamCtrl(UvcDeviceHandle.this);
                if (UvcDeviceHandle.nativeGetStreamControlFormatSize(UvcDeviceHandle.this.pointer, uvcStreamCtrl.getPointer(), uvcFrameFormat.getValue(), width, height, fps) == 0)
                  return uvcStreamCtrl; 
                uvcStreamCtrl.releaseRef();
              } 
              return null;
            }
          });
    } 
  }
  
  public String getTag() {
    return TAG;
  }
  
  public Tracer getTracer() {
    return this.tracer;
  }
  
  public UvcContext getUvcContext() {
    return ((UvcDevice)getParent()).getUvcContext();
  }
  
  public int getVendorId() {
    return ((UvcDevice)getParent()).getVendorId();
  }
  
  public int getVuforiaExposureMode() {
    synchronized (this.lock) {
      return nativeGetVuforiaExposureMode(this.pointer);
    } 
  }
  
  public FocusMode getVuforiaFocusMode() {
    synchronized (this.lock) {
      return FocusMode.from(nativeGetVuforiaFocusMode(this.pointer));
    } 
  }
  
  public boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize) {
    return paramCameraCalibrationManager.hasCalibration(getCalibrationIdentity(), paramSize);
  }
  
  public boolean isExposureSupported() {
    synchronized (this.lock) {
      return nativeIsExposureSupported(this.pointer);
    } 
  }
  
  public boolean isFocusLengthSupported() {
    synchronized (this.lock) {
      return nativeIsFocusLengthSupported(this.pointer);
    } 
  }
  
  public boolean isVuforiaExposureModeSupported(ExtendedExposureMode paramExtendedExposureMode) {
    synchronized (this.lock) {
      return nativeIsVuforiaExposureModeSupported(this.pointer, paramExtendedExposureMode.ordinal());
    } 
  }
  
  public boolean isVuforiaFocusModeSupported(FocusMode paramFocusMode) {
    synchronized (this.lock) {
      return nativeIsVuforiaFocusModeSupported(this.pointer, paramFocusMode.ordinal());
    } 
  }
  
  public void onClosed(UvcApiCaptureSession paramUvcApiCaptureSession) {}
  
  public final void openSelfAndReport() {
    synchronized (this.lock) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[this.selfState.ordinal()];
      if (i != 1) {
        if (i != 2)
          throw Misc.illegalStateException("openSelfCameraAndReport(): %s", new Object[] { this.selfState }); 
      } else {
        this.selfState = CameraState.OpenNotStarted;
        createSelfCamera();
        this.stateCallback.onOpened(this.selfCamera);
      } 
      return;
    } 
  }
  
  public int releaseRefExternal() {
    synchronized (this.lock) {
      return this.refCountedCameraHelper.releaseRefExternal();
    } 
  }
  
  protected void reportError(Camera.Error paramError) {
    synchronized (this.lock) {
      if (this.stateCallback != null)
        this.stateCallback.onError(this.selfCamera, paramError); 
      return;
    } 
  }
  
  protected final void reportSelfClosed() {
    synchronized (this.lock) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$camera$CameraState[this.selfState.ordinal()];
      if (i != 3 && i != 4) {
        this.selfState = CameraState.Closed;
        this.stateCallback.onClosed(this.selfCamera);
      } 
      return;
    } 
  }
  
  public void setAutoExposure(UvcAutoExposureMode paramUvcAutoExposureMode) {
    nativeSetAutoExposure(this.pointer, paramUvcAutoExposureMode.getValue());
  }
  
  public boolean setExposure(long paramLong) {
    synchronized (this.lock) {
      return nativeSetExposure(this.pointer, paramLong);
    } 
  }
  
  public boolean setFocusLength(double paramDouble) {
    synchronized (this.lock) {
      return nativeSetFocusLength(this.pointer, paramDouble);
    } 
  }
  
  public boolean setVuforiaExposureMode(ExtendedExposureMode paramExtendedExposureMode) {
    synchronized (this.lock) {
      return nativeSetVuforiaExposureMode(this.pointer, paramExtendedExposureMode.ordinal());
    } 
  }
  
  public boolean setVuforiaFocusMode(FocusMode paramFocusMode) {
    synchronized (this.lock) {
      return nativeSetVuforiaFocusMode(this.pointer, paramFocusMode.ordinal());
    } 
  }
  
  protected void shutdown() {
    closeCaptureSession();
    reportSelfClosed();
  }
  
  public void stopAllStreaming() {
    synchronized (this.lock) {
      nativeStopAllStreaming(this.pointer);
      return;
    } 
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%s|%s)", new Object[] { getClass().getSimpleName(), getTraceIdentifier(), ((UvcDevice)getParent()).toString() });
  }
  
  public String toStringVerbose() {
    return nativeGetDiagnostics(this.pointer);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\nativeobject\UvcDeviceHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */