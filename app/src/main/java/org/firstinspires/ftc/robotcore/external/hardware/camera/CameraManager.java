package org.firstinspires.ftc.robotcore.external.hardware.camera;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraName;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public interface CameraManager {
  void asyncOpenCameraAssumingPermission(CameraName paramCameraName, Continuation<? extends Camera.StateCallback> paramContinuation, long paramLong, TimeUnit paramTimeUnit);
  
  List<WebcamName> getAllWebcams();
  
  SwitchableCameraName nameForSwitchableCamera(CameraName... paramVarArgs);
  
  CameraName nameForUnknownCamera();
  
  CameraName nameFromCameraDirection(VuforiaLocalizer.CameraDirection paramCameraDirection);
  
  Camera requestPermissionAndOpenCamera(Deadline paramDeadline, CameraName paramCameraName, Continuation<? extends Camera.StateCallback> paramContinuation);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */