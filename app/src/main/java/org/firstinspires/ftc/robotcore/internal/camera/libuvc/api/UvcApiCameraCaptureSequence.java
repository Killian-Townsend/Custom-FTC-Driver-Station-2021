package org.firstinspires.ftc.robotcore.internal.camera.libuvc.api;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.internal.camera.ImageFormatMapper;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcDeviceHandle;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcFrame;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcStreamCtrl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.UvcStreamHandle;
import org.firstinspires.ftc.robotcore.internal.system.DestructOnFinalize;
import org.firstinspires.ftc.robotcore.internal.system.RefCounted;

class UvcApiCameraCaptureSequence extends DestructOnFinalize<UvcApiCaptureSession> {
  protected boolean closeReported = false;
  
  protected long lastFrameNumber = -1L;
  
  protected boolean reportOnClose = false;
  
  protected final Continuation<? extends CameraCaptureSession.CaptureCallback> userCaptureContinuation;
  
  protected final Continuation<? extends CameraCaptureSession.StatusCallback> userStatusContinuation;
  
  protected final UvcApiCameraCaptureRequest uvcCameraCaptureRequest;
  
  protected final UvcApiCameraCaptureSequenceId uvcCaptureSequenceId;
  
  protected UvcStreamHandle uvcStreamHandle = null;
  
  public UvcApiCameraCaptureSequence(UvcApiCaptureSession paramUvcApiCaptureSession, UvcApiCameraCaptureSequenceId paramUvcApiCameraCaptureSequenceId, UvcApiCameraCaptureRequest paramUvcApiCameraCaptureRequest, Continuation<? extends CameraCaptureSession.CaptureCallback> paramContinuation, Continuation<? extends CameraCaptureSession.StatusCallback> paramContinuation1) {
    setParent((RefCounted)paramUvcApiCaptureSession);
    this.uvcCaptureSequenceId = paramUvcApiCameraCaptureSequenceId;
    this.uvcCameraCaptureRequest = paramUvcApiCameraCaptureRequest;
    this.userCaptureContinuation = paramContinuation;
    this.userStatusContinuation = paramContinuation1;
  }
  
  protected void destructor() {
    stopStreamingAndReportClosedIfNeeded();
    super.destructor();
  }
  
  protected UvcApiCaptureSession getCaptureSession() {
    return (UvcApiCaptureSession)getParent();
  }
  
  protected UvcDeviceHandle getDeviceHandle() {
    return getCaptureSession().getDeviceHandle();
  }
  
  protected void reportClosedIfNeeded() {
    synchronized (this.lock) {
      if (this.reportOnClose && !this.closeReported) {
        this.closeReported = true;
        final UvcApiCaptureSession uvcCaptureSession = getCaptureSession();
        uvcApiCaptureSession.addRef();
        this.userStatusContinuation.dispatch(new ContinuationResult<CameraCaptureSession.StatusCallback>() {
              public void handle(CameraCaptureSession.StatusCallback param1StatusCallback) {
                param1StatusCallback.onCaptureSequenceCompleted(uvcCaptureSession, (CameraCaptureSequenceId)UvcApiCameraCaptureSequence.this.uvcCaptureSequenceId, UvcApiCameraCaptureSequence.this.lastFrameNumber);
                uvcCaptureSession.releaseRef();
              }
            });
      } 
      return;
    } 
  }
  
  protected void startStreaming() throws CameraException {
    synchronized (this.lock) {
      this.reportOnClose = true;
      try {
        UvcFrameFormat uvcFrameFormat = ImageFormatMapper.uvcFromAndroid(this.uvcCameraCaptureRequest.getAndroidFormat());
        Size size = this.uvcCameraCaptureRequest.getSize();
        UvcStreamCtrl uvcStreamCtrl = getDeviceHandle().getStreamControl(uvcFrameFormat, size.getWidth(), size.getHeight(), this.uvcCameraCaptureRequest.getFramesPerSecond());
        if (uvcStreamCtrl != null)
          try {
            UvcStreamHandle uvcStreamHandle = uvcStreamCtrl.open();
            this.uvcStreamHandle = uvcStreamHandle;
            if (uvcStreamHandle != null) {
              try {
                uvcStreamHandle.startStreaming(new Consumer<UvcFrame>() {
                      public void accept(UvcFrame param1UvcFrame) {
                        boolean bool;
                        if (UvcApiCameraCaptureSequence.this.userCaptureContinuation.isDispatchSynchronous() || UvcApiCameraCaptureSequence.this.userCaptureContinuation.canBorrowThread(Thread.currentThread())) {
                          bool = true;
                        } else {
                          bool = false;
                        } 
                        ContinuationResult<CameraCaptureSession.CaptureCallback> continuationResult = new ContinuationResult<CameraCaptureSession.CaptureCallback>() {
                            public void handle(CameraCaptureSession.CaptureCallback param2CaptureCallback) {
                              UvcApiCameraCaptureSequence.this.lastFrameNumber = capturedFrame.getFrameNumber();
                              param2CaptureCallback.onNewFrame(UvcApiCameraCaptureSequence.this.getCaptureSession(), (CameraCaptureRequest)UvcApiCameraCaptureSequence.this.uvcCameraCaptureRequest, capturedFrame);
                              capturedFrame.releaseRef();
                            }
                          };
                        if (bool) {
                          UvcApiCameraCaptureSequence.this.userCaptureContinuation.dispatchHere(continuationResult);
                          return;
                        } 
                        UvcApiCameraCaptureSequence.this.userCaptureContinuation.dispatch(continuationResult);
                      }
                    });
                return;
              } catch (IOException iOException) {
              
              } catch (RuntimeException runtimeException) {}
              RobotLog.ee(UvcDeviceHandle.TAG, runtimeException, "uvcStreamHandle.startStreaming() failed");
              this.uvcStreamHandle.releaseRef();
              this.uvcStreamHandle = null;
              throw new CameraException(Camera.Error.OtherError, runtimeException);
            } 
            RobotLog.ee(UvcDeviceHandle.TAG, "uvcStreamCtrl.open() failed");
            throw new CameraException(Camera.Error.OtherError);
          } finally {
            uvcStreamCtrl.releaseRef();
          }  
        RobotLog.ee(UvcDeviceHandle.TAG, "getStreamControl() failed");
        throw new CameraException(Camera.Error.StreamingRequestNotSupported);
      } catch (RuntimeException runtimeException) {
        this.reportOnClose = false;
        throw new CameraException(Camera.Error.InternalError, runtimeException);
      } catch (CameraException cameraException) {
        this.reportOnClose = false;
        throw cameraException;
      } 
    } 
  }
  
  protected void stopStreamingAndReportClosedIfNeeded() {
    synchronized (this.lock) {
      if (this.uvcStreamHandle != null) {
        this.uvcStreamHandle.stopStreaming();
        this.uvcStreamHandle.releaseRef();
        this.uvcStreamHandle = null;
      } 
      reportClosedIfNeeded();
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\libuvc\api\UvcApiCameraCaptureSequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */