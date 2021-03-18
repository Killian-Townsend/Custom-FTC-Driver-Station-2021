package org.firstinspires.ftc.robotcore.internal.camera;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;

public interface CameraInternal {
  CameraCalibration getCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize);
  
  CameraCalibrationIdentity getCalibrationIdentity();
  
  boolean hasCalibration(CameraCalibrationManager paramCameraCalibrationManager, Size paramSize);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraInternal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */