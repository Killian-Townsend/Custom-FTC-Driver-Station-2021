package org.firstinspires.ftc.robotcore.internal.camera.delegating;

import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.CountDownLatch;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.ThrowingSupplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.internal.system.CloseableRefCounted;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public class DelegatingCaptureSession extends CloseableRefCounted implements CameraCaptureSession {
  public static final String TAG = "DelCaptureSession";
  
  protected Camera camera = null;
  
  protected final int captureSessionId;
  
  protected boolean closeReported = false;
  
  protected final DelegatingCamera delegatingCamera;
  
  protected DelegatingCaptureSequence delegatingCaptureSequence = null;
  
  protected int nextCaptureSequenceId = 1;
  
  protected final Tracer tracer;
  
  protected final Continuation<? extends CameraCaptureSession.StateCallback> userStateContinuation;
  
  public DelegatingCaptureSession(DelegatingCamera paramDelegatingCamera, Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation, int paramInt) {
    this.delegatingCamera = paramDelegatingCamera;
    this.captureSessionId = paramInt;
    this.userStateContinuation = paramContinuation;
    this.tracer = Tracer.create(getTag(), DelegatingCamera.TRACE);
    enableOnlyClose();
    reportConfigured();
  }
  
  protected void destructor() {
    this.tracer.trace("destructor()", new Runnable() {
          public void run() {
            DelegatingCaptureSession.this.shutdown();
            DelegatingCaptureSession.this.delegatingCamera.onClosed(DelegatingCaptureSession.this);
            DelegatingCaptureSession.this.destructor();
          }
        });
  }
  
  protected void doClose() {
    this.tracer.trace("doClose()", new Runnable() {
          public void run() {
            DelegatingCaptureSession.this.shutdown();
            DelegatingCaptureSession.this.doClose();
          }
        });
  }
  
  public Camera getCamera() {
    return this.delegatingCamera.selfCamera;
  }
  
  public String getTag() {
    DelegatingCamera delegatingCamera = this.delegatingCamera;
    String str = "DelCaptureSession";
    if (delegatingCamera != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.delegatingCamera.getTag());
      stringBuilder.append("|");
      stringBuilder.append("DelCaptureSession");
      str = stringBuilder.toString();
    } 
    return str;
  }
  
  public Continuation<InterveningStateCallback> newInterveningStateCallback() {
    return Continuation.create(ThreadPool.getDefault(), new InterveningStateCallback());
  }
  
  public void onCameraChanged(final Camera newCamera) {
    synchronized (this.lock) {
      if (this.camera != newCamera)
        this.tracer.trace(this.tracer.format("onCameraChange(%s->%s)", new Object[] { this.camera, newCamera }), new Runnable() {
              public void run() {
                DelegatingCaptureSession.this.camera = newCamera;
                DelegatingCaptureSession.this.updateCameraInSequence();
              }
            }); 
      return;
    } 
  }
  
  protected void reportClosedIfNeeded() {
    synchronized (this.lock) {
      if (!this.closeReported) {
        this.closeReported = true;
        addRef();
        this.userStateContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StateCallback>() {
              public void handle(CameraCaptureSession.StateCallback param1StateCallback) {
                param1StateCallback.onClosed(DelegatingCaptureSession.this);
                DelegatingCaptureSession.this.releaseRef();
              }
            });
      } 
      return;
    } 
  }
  
  protected void reportConfigured() {
    synchronized (this.lock) {
      addRef();
      this.userStateContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StateCallback>() {
            public void handle(CameraCaptureSession.StateCallback param1StateCallback) {
              param1StateCallback.onConfigured(DelegatingCaptureSession.this);
              DelegatingCaptureSession.this.releaseRef();
            }
          });
      return;
    } 
  }
  
  protected void shutdown() {
    stopCapture();
    reportClosedIfNeeded();
  }
  
  public CameraCaptureSequenceId startCapture(final CameraCaptureRequest captureRequest, final Continuation<? extends CameraCaptureSession.CaptureCallback> userCaptureContinuation, final Continuation<? extends CameraCaptureSession.StatusCallback> userStatusContinuation) throws CameraException {
    return (CameraCaptureSequenceId)this.tracer.trace("startCapture()", new ThrowingSupplier<CameraCaptureSequenceId, CameraException>() {
          public CameraCaptureSequenceId get() throws CameraException {
            synchronized (DelegatingCaptureSession.this.lock) {
              if (DelegatingCaptureRequest.isForCamera(DelegatingCaptureSession.this.delegatingCamera, captureRequest)) {
                DelegatingCaptureRequest delegatingCaptureRequest = (DelegatingCaptureRequest)captureRequest;
                DelegatingCaptureSession.this.stopCapture();
                try {
                  DelegatingCaptureSession delegatingCaptureSession1 = DelegatingCaptureSession.this;
                  DelegatingCamera delegatingCamera1 = DelegatingCaptureSession.this.delegatingCamera;
                  DelegatingCaptureSession delegatingCaptureSession2 = DelegatingCaptureSession.this;
                  DelegatingCamera delegatingCamera2 = DelegatingCaptureSession.this.delegatingCamera;
                  DelegatingCaptureSession delegatingCaptureSession3 = DelegatingCaptureSession.this;
                  int i = delegatingCaptureSession3.nextCaptureSequenceId;
                  delegatingCaptureSession3.nextCaptureSequenceId = i + 1;
                  delegatingCaptureSession1.delegatingCaptureSequence = new DelegatingCaptureSequence(delegatingCamera1, delegatingCaptureSession2, new DelegatingCaptureSequenceId(delegatingCamera2, i), delegatingCaptureRequest, userCaptureContinuation, userStatusContinuation);
                  DelegatingCaptureSession.this.updateCameraInSequence();
                  DelegatingCaptureSession.this.delegatingCaptureSequence.startStreaming();
                  return (CameraCaptureSequenceId)DelegatingCaptureSession.this.delegatingCaptureSequence.delegatingSequenceId;
                } catch (CameraException cameraException) {
                
                } catch (RuntimeException runtimeException) {}
                DelegatingCaptureSession.this.stopCapture();
                throw runtimeException;
              } 
              throw Misc.illegalArgumentException("capture request is not from this camera");
            } 
          }
        });
  }
  
  public CameraCaptureSequenceId startCapture(CameraCaptureRequest paramCameraCaptureRequest, CameraCaptureSession.CaptureCallback paramCaptureCallback, Continuation<? extends CameraCaptureSession.StatusCallback> paramContinuation) throws CameraException {
    return startCapture(paramCameraCaptureRequest, Continuation.createTrivial(paramCaptureCallback), paramContinuation);
  }
  
  public void stopCapture() {
    try {
      synchronized (this.lock) {
        if (this.delegatingCaptureSequence != null)
          this.tracer.trace("stopCapture()", new Runnable() {
                public void run() {
                  DelegatingCaptureSession.this.delegatingCaptureSequence.stopStreamingAndReportClosedIfNeeded();
                  DelegatingCaptureSession.this.delegatingCaptureSequence.releaseRef();
                  DelegatingCaptureSession.this.delegatingCaptureSequence = null;
                }
              }); 
        return;
      } 
    } catch (RuntimeException runtimeException) {
      this.tracer.traceError(runtimeException, "unexpected exception in stopCapture(); ignoring", new Object[0]);
      return;
    } 
  }
  
  public String toString() {
    return Misc.formatInvariant("%s(%s)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.captureSessionId) });
  }
  
  protected void updateCameraInSequence() {
    synchronized (this.lock) {
      if (this.delegatingCaptureSequence != null)
        this.delegatingCaptureSequence.onCameraChanged(this.camera); 
      return;
    } 
  }
  
  protected class InterveningStateCallback implements CameraCaptureSession.StateCallback {
    protected boolean configured = false;
    
    protected CountDownLatch latch = new CountDownLatch(1);
    
    public boolean awaitConfiguredOrClosed() {
      try {
        this.latch.await();
        return this.configured;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return false;
      } 
    }
    
    public void onClosed(CameraCaptureSession param1CameraCaptureSession) {
      DelegatingCaptureSession.this.tracer.trace("camera session is closed: %s", new Object[] { param1CameraCaptureSession.getCamera().getCameraName() });
      this.configured = false;
      this.latch.countDown();
    }
    
    public void onConfigured(CameraCaptureSession param1CameraCaptureSession) {
      DelegatingCaptureSession.this.tracer.trace("camera session is configured: %s", new Object[] { param1CameraCaptureSession.getCamera().getCameraName() });
      this.configured = true;
      this.latch.countDown();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\delegating\DelegatingCaptureSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */