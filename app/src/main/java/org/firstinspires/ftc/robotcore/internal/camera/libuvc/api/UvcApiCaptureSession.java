package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.internal.camera.RefCountedCamera;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDeviceHandle;
import org.firstinspires.ftc.robotcore.internal.system.CloseableDestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class UvcApiCaptureSession extends CloseableDestructOnFinalize<UvcDeviceHandle> implements CameraCaptureSession {
  public static boolean TRACE = true;
  
  protected final int captureSessionId;
  
  protected boolean closeReported = false;
  
  protected int nextCaptureSequenceId = 1;
  
  protected final Tracer tracer = Tracer.create(getTag(), TRACE);
  
  protected final Continuation<? extends CameraCaptureSession.StateCallback> userContinuation;
  
  protected UvcApiCameraCaptureSequence uvcCaptureSequence = null;
  
  public UvcApiCaptureSession(UvcDeviceHandle paramUvcDeviceHandle, Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation, int paramInt) {
    setParent((RefCounted)paramUvcDeviceHandle);
    this.captureSessionId = paramInt;
    this.userContinuation = paramContinuation;
    enableOnlyClose();
    reportConfigured();
  }
  
  protected void destructor() {
    this.tracer.trace("destructor", new Runnable() {
          public void run() {
            UvcApiCaptureSession.this.shutdown();
            ((UvcDeviceHandle)UvcApiCaptureSession.this.getParent()).onClosed(UvcApiCaptureSession.this);
            UvcApiCaptureSession.this.destructor();
          }
        });
  }
  
  public void doClose() {
    this.tracer.trace("doClose()", new Runnable() {
          public void run() {
            UvcApiCaptureSession.this.shutdown();
            UvcApiCaptureSession.this.doClose();
          }
        });
  }
  
  public Camera getCamera() {
    return getDeviceHandle().getSelfCamera();
  }
  
  public UvcDeviceHandle getDeviceHandle() {
    return (UvcDeviceHandle)getParent();
  }
  
  public String getTag() {
    return UvcApiCaptureSession.class.getSimpleName();
  }
  
  protected void reportClosedIfNeeded() {
    synchronized (this.lock) {
      if (!this.closeReported) {
        this.closeReported = true;
        addRef();
        this.userContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StateCallback>() {
              public void handle(CameraCaptureSession.StateCallback param1StateCallback) {
                param1StateCallback.onClosed(UvcApiCaptureSession.this);
                UvcApiCaptureSession.this.releaseRef();
              }
            });
      } 
      return;
    } 
  }
  
  protected void reportConfigured() {
    synchronized (this.lock) {
      addRef();
      this.userContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StateCallback>() {
            public void handle(CameraCaptureSession.StateCallback param1StateCallback) {
              param1StateCallback.onConfigured(UvcApiCaptureSession.this);
              UvcApiCaptureSession.this.releaseRef();
            }
          });
      return;
    } 
  }
  
  protected void shutdown() {
    stopCapture();
    reportClosedIfNeeded();
  }
  
  public CameraCaptureSequenceId startCapture(CameraCaptureRequest paramCameraCaptureRequest, Continuation<? extends CameraCaptureSession.CaptureCallback> paramContinuation, Continuation<? extends CameraCaptureSession.StatusCallback> paramContinuation1) throws CameraException {
    synchronized (this.lock) {
      if (UvcApiCameraCaptureRequest.isForDeviceHandle(getDeviceHandle(), paramCameraCaptureRequest)) {
        UvcApiCameraCaptureRequest uvcApiCameraCaptureRequest = (UvcApiCameraCaptureRequest)paramCameraCaptureRequest;
        stopCapture();
        try {
          RefCountedCamera refCountedCamera = (RefCountedCamera)getParent();
          int i = this.nextCaptureSequenceId;
          this.nextCaptureSequenceId = i + 1;
          UvcApiCameraCaptureSequence uvcApiCameraCaptureSequence = new UvcApiCameraCaptureSequence(this, new UvcApiCameraCaptureSequenceId(refCountedCamera, i), uvcApiCameraCaptureRequest, paramContinuation, paramContinuation1);
          this.uvcCaptureSequence = uvcApiCameraCaptureSequence;
          uvcApiCameraCaptureSequence.startStreaming();
          return (CameraCaptureSequenceId)this.uvcCaptureSequence.uvcCaptureSequenceId;
        } catch (CameraException cameraException) {
        
        } catch (RuntimeException runtimeException) {}
        stopCapture();
        throw runtimeException;
      } 
      throw Misc.illegalArgumentException("capture request is not from this camera");
    } 
  }
  
  public CameraCaptureSequenceId startCapture(CameraCaptureRequest paramCameraCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Continuation<? extends CameraCaptureSession.StatusCallback> paramContinuation) throws CameraException {
    return startCapture(paramCameraCaptureRequest, Continuation.createTrivial(paramCaptureCallback), paramContinuation);
  }
  
  public void stopCapture() {
    try {
      synchronized (this.lock) {
        if (this.uvcCaptureSequence != null)
          this.tracer.trace("stopCapture()", new Runnable() {
                public void run() {
                  UvcApiCaptureSession.this.uvcCaptureSequence.stopStreamingAndReportClosedIfNeeded();
                  UvcApiCaptureSession.this.uvcCaptureSequence.releaseRef();
                  UvcApiCaptureSession.this.uvcCaptureSequence = null;
                }
              }); 
        return;
      } 
    } catch (RuntimeException runtimeException) {
      RobotLog.ee(getTag(), runtimeException, "unexpected exception in stopCapture(); ignoring");
      return;
    } 
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%s)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.captureSessionId) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCaptureSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */