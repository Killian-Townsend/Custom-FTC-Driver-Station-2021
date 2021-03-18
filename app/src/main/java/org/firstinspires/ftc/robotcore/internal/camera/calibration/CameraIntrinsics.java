package org.firstinspires.ftc.robotcore.internal.camera.calibration;

import java.util.Arrays;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class CameraIntrinsics {
  public final float[] distortionCoefficients;
  
  public float focalLengthX;
  
  public float focalLengthY;
  
  public float principalPointX;
  
  public float principalPointY;
  
  public CameraIntrinsics(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOffloat) throws RuntimeException {
    if (paramArrayOffloat != null && paramArrayOffloat.length == 8) {
      this.focalLengthX = paramFloat1;
      this.focalLengthY = paramFloat2;
      this.principalPointX = paramFloat3;
      this.principalPointY = paramFloat4;
      this.distortionCoefficients = Arrays.copyOf(paramArrayOffloat, paramArrayOffloat.length);
      return;
    } 
    throw Misc.illegalArgumentException("distortionCoefficients must have length 8");
  }
  
  public boolean isDegenerate() {
    if (this.focalLengthX == 0.0F && this.focalLengthY == 0.0F && this.principalPointX == 0.0F && this.principalPointY == 0.0F) {
      float[] arrayOfFloat = this.distortionCoefficients;
      if (arrayOfFloat[0] == 0.0F && arrayOfFloat[1] == 0.0F && arrayOfFloat[2] == 0.0F && arrayOfFloat[3] == 0.0F && arrayOfFloat[4] == 0.0F && arrayOfFloat[5] == 0.0F && arrayOfFloat[6] == 0.0F && arrayOfFloat[7] == 0.0F)
        return true; 
    } 
    return false;
  }
  
  public float[] toArray() {
    float[] arrayOfFloat = new float[12];
    arrayOfFloat[0] = this.focalLengthX;
    arrayOfFloat[1] = this.focalLengthY;
    arrayOfFloat[2] = this.principalPointX;
    arrayOfFloat[3] = this.principalPointY;
    System.arraycopy(this.distortionCoefficients, 0, arrayOfFloat, 4, 8);
    return arrayOfFloat;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\calibration\CameraIntrinsics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */