package org.firstinspires.ftc.robotcore.external.hardware.camera;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.android.util.Size;

public interface CameraCaptureRequest {
  Bitmap createEmptyBitmap();
  
  int getAndroidFormat();
  
  int getFramesPerSecond();
  
  long getNsFrameDuration();
  
  Size getSize();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraCaptureRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */