package org.firstinspires.ftc.robotcore.external.hardware.camera;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.internal.camera.CameraImpl;

public interface Camera extends CameraControls {
  void close();
  
  CameraCaptureRequest createCaptureRequest(int paramInt1, Size paramSize, int paramInt2) throws CameraException;
  
  CameraCaptureSession createCaptureSession(Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation) throws CameraException;
  
  Camera dup();
  
  CameraName getCameraName();
  
  public enum Error {
    Connected, Disconnected, InternalError, None, OtherError, StreamingRequestNotSupported, Timeout;
    
    static {
      Connected = new Error("Connected", 3);
      StreamingRequestNotSupported = new Error("StreamingRequestNotSupported", 4);
      Timeout = new Error("Timeout", 5);
      Error error = new Error("InternalError", 6);
      InternalError = error;
      $VALUES = new Error[] { None, OtherError, Disconnected, Connected, StreamingRequestNotSupported, Timeout, error };
    }
  }
  
  public enum OpenFailure {
    None, CameraTypeNotSupported, Disconnected, InUseOrAccessDenied, InternalError, OtherFailure;
    
    static {
      Disconnected = new OpenFailure("Disconnected", 4);
      OpenFailure openFailure = new OpenFailure("InternalError", 5);
      InternalError = openFailure;
      $VALUES = new OpenFailure[] { None, OtherFailure, CameraTypeNotSupported, InUseOrAccessDenied, Disconnected, openFailure };
    }
  }
  
  public static interface StateCallback {
    void onClosed(Camera param1Camera);
    
    void onError(Camera param1Camera, Camera.Error param1Error);
    
    void onOpenFailed(CameraName param1CameraName, Camera.OpenFailure param1OpenFailure);
    
    void onOpened(Camera param1Camera);
  }
  
  public static class StateCallbackDefault implements StateCallback {
    public void onClosed(Camera param1Camera) {}
    
    public void onError(Camera param1Camera, Camera.Error param1Error) {}
    
    public void onOpenFailed(CameraName param1CameraName, Camera.OpenFailure param1OpenFailure) {}
    
    public void onOpened(Camera param1Camera) {
      CameraImpl.closeCamera("StateCallbackDefault", param1Camera);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\Camera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */