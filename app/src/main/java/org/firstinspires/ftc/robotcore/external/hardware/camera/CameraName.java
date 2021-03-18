package org.firstinspires.ftc.robotcore.external.hardware.camera;

import android.content.Context;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public interface CameraName {
  void asyncRequestCameraPermission(Context paramContext, Deadline paramDeadline, Continuation<? extends Consumer<Boolean>> paramContinuation);
  
  CameraCharacteristics getCameraCharacteristics();
  
  boolean isCameraDirection();
  
  boolean isSwitchable();
  
  boolean isUnknown();
  
  boolean isWebcam();
  
  boolean requestCameraPermission(Deadline paramDeadline);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */