package org.firstinspires.ftc.robotcore.internal.camera;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraControls;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public interface RefCountedCamera extends CameraInternal, CameraControls {
  void addRefExternal();
  
  CameraCaptureRequest createCaptureRequest(int paramInt1, Size paramSize, int paramInt2) throws CameraException;
  
  CameraCaptureSession createCaptureSession(Continuation<? extends CameraCaptureSession.StateCallback> paramContinuation) throws CameraException;
  
  CameraName getCameraName();
  
  String getExternalTraceIdentifier();
  
  Tracer getTracer();
  
  int releaseRefExternal();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\RefCountedCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */