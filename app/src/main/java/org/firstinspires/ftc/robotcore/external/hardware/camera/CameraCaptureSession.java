package org.firstinspires.ftc.robotcore.external.hardware.camera;

import org.firstinspires.ftc.robotcore.external.function.Continuation;

public interface CameraCaptureSession {
  void close();
  
  Camera getCamera();
  
  CameraCaptureSequenceId startCapture(CameraCaptureRequest paramCameraCaptureRequest, Continuation<? extends CaptureCallback> paramContinuation, Continuation<? extends StatusCallback> paramContinuation1) throws CameraException;
  
  CameraCaptureSequenceId startCapture(CameraCaptureRequest paramCameraCaptureRequest, CaptureCallback paramCaptureCallback, Continuation<? extends StatusCallback> paramContinuation) throws CameraException;
  
  void stopCapture();
  
  public static interface CaptureCallback {
    void onNewFrame(CameraCaptureSession param1CameraCaptureSession, CameraCaptureRequest param1CameraCaptureRequest, CameraFrame param1CameraFrame);
  }
  
  public static interface StateCallback {
    void onClosed(CameraCaptureSession param1CameraCaptureSession);
    
    void onConfigured(CameraCaptureSession param1CameraCaptureSession);
  }
  
  public static abstract class StateCallbackDefault implements StateCallback {
    public void onClosed(CameraCaptureSession param1CameraCaptureSession) {}
    
    public void onConfigured(CameraCaptureSession param1CameraCaptureSession) {}
  }
  
  public static interface StatusCallback {
    void onCaptureSequenceCompleted(CameraCaptureSession param1CameraCaptureSession, CameraCaptureSequenceId param1CameraCaptureSequenceId, long param1Long);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraCaptureSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */