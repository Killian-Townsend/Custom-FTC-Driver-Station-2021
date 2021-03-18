package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.camera.ImageFormatMapper;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class CameraMode {
  public final FrameFormat format;
  
  public final int fps;
  
  public final int height;
  
  public final int width;
  
  public CameraMode(int paramInt1, int paramInt2, int paramInt3, FrameFormat paramFrameFormat) {
    this.width = paramInt1;
    this.height = paramInt2;
    this.fps = paramInt3;
    this.format = paramFrameFormat;
  }
  
  public CameraMode(int[] paramArrayOfint) {
    this.width = paramArrayOfint[0];
    this.height = paramArrayOfint[1];
    this.fps = paramArrayOfint[2];
    this.format = FrameFormat.from(paramArrayOfint[3]);
  }
  
  public int getAndroidFormat() {
    return ImageFormatMapper.androidFromVuforiaWebcam(this.format);
  }
  
  public int getFramesPerSecond() {
    return this.fps;
  }
  
  public long getNsFrameDuration() {
    return Math.round(1.0E9D / this.fps);
  }
  
  public Size getSize() {
    return new Size(this.width, this.height);
  }
  
  public int[] toArray() {
    return new int[] { this.width, this.height, this.fps, this.format.ordinal() };
  }
  
  public String toString() {
    FrameFormat frameFormat = this.format;
    return Misc.formatInvariant("CameraMode(format=%s:%d w=%d h=%d fps=%d)", new Object[] { frameFormat, Integer.valueOf(frameFormat.ordinal()), Integer.valueOf(this.width), Integer.valueOf(this.height), Integer.valueOf(this.fps) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\CameraMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */