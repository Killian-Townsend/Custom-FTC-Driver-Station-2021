package org.firstinspires.ftc.robotcore.external.hardware.camera;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;

public interface CameraFrame {
  public static final long UnknownFrameNumber = -1L;
  
  void addRef();
  
  CameraFrame copy();
  
  void copyToBitmap(Bitmap paramBitmap);
  
  CameraCaptureSequenceId getCaptureSequenceId();
  
  long getCaptureTime();
  
  long getFrameNumber();
  
  long getImageBuffer();
  
  int getImageSize();
  
  CameraCaptureRequest getRequest();
  
  Size getSize();
  
  int getStride();
  
  UvcFrameFormat getUvcFrameFormat();
  
  int releaseRef();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\hardware\camera\CameraFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */