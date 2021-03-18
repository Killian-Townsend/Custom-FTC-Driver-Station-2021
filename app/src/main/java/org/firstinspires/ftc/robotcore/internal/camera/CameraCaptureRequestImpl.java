package org.firstinspires.ftc.robotcore.internal.camera;

import android.graphics.Bitmap;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;

public abstract class CameraCaptureRequestImpl implements CameraCaptureRequest {
  public final int androidFormat;
  
  public final int fps;
  
  public final Size size;
  
  public CameraCaptureRequestImpl(int paramInt1, Size paramSize, int paramInt2) {
    this.androidFormat = paramInt1;
    this.size = paramSize;
    this.fps = paramInt2;
  }
  
  public Bitmap createEmptyBitmap() {
    return Bitmap.createBitmap(this.size.getWidth(), this.size.getHeight(), Bitmap.Config.ARGB_8888);
  }
  
  public int getAndroidFormat() {
    return this.androidFormat;
  }
  
  public int getFramesPerSecond() {
    return this.fps;
  }
  
  public long getNsFrameDuration() {
    return Math.round(1.0E9D / this.fps);
  }
  
  public Size getSize() {
    return this.size;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\CameraCaptureRequestImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */