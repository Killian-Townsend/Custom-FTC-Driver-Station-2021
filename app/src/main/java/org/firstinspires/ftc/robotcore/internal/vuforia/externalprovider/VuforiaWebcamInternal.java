package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;

public interface VuforiaWebcamInternal {
  CameraCalibration getCalibrationInUse();
  
  Camera getCamera();
  
  CameraName getCameraName();
  
  void getFrameOnce(Continuation<? extends Consumer<CameraFrame>> paramContinuation);
  
  void postVuforiaDeinit();
  
  void postVuforiaInit();
  
  void preVuforiaDeinit();
  
  boolean preVuforiaInit();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\VuforiaWebcamInternal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */