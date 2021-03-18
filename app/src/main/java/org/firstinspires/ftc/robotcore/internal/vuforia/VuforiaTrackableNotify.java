package org.firstinspires.ftc.robotcore.internal.vuforia;

import com.vuforia.TrackableResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;

public interface VuforiaTrackableNotify {
  void noteNotTracked();
  
  void noteTracked(TrackableResult paramTrackableResult, CameraName paramCameraName, Camera paramCamera);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaTrackableNotify.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */