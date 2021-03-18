package org.firstinspires.ftc.robotcore.internal.camera.calibration;

import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class CameraCalibration extends CameraIntrinsics implements Cloneable {
  protected CameraCalibrationIdentity identity;
  
  protected final boolean isFake;
  
  protected boolean remove;
  
  protected Size size;
  
  public CameraCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float[] paramArrayOffloat, boolean paramBoolean1, boolean paramBoolean2) throws RuntimeException {
    super(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramArrayOffloat);
    this.identity = paramCameraCalibrationIdentity;
    this.size = paramSize;
    this.remove = paramBoolean1;
    this.isFake = paramBoolean2;
  }
  
  public CameraCalibration(CameraCalibrationIdentity paramCameraCalibrationIdentity, int[] paramArrayOfint, float[] paramArrayOffloat1, float[] paramArrayOffloat2, float[] paramArrayOffloat3, boolean paramBoolean1, boolean paramBoolean2) throws RuntimeException {
    this(paramCameraCalibrationIdentity, new Size(paramArrayOfint[0], paramArrayOfint[1]), paramArrayOffloat1[0], paramArrayOffloat1[1], paramArrayOffloat2[0], paramArrayOffloat2[1], paramArrayOffloat3, paramBoolean1, paramBoolean2);
    if (paramArrayOfint.length == 2) {
      if (paramArrayOffloat2.length == 2) {
        if (paramArrayOffloat1.length == 2) {
          if (paramArrayOffloat3.length == 8)
            return; 
          throw Misc.illegalArgumentException("distortion coefficients size must be 8");
        } 
        throw Misc.illegalArgumentException("focal length size must be 2");
      } 
      throw Misc.illegalArgumentException("principal point size must be 2");
    } 
    throw Misc.illegalArgumentException("frame size must be 2");
  }
  
  public static CameraCalibration forUnavailable(CameraCalibrationIdentity paramCameraCalibrationIdentity, Size paramSize) {
    CameraCalibrationIdentity cameraCalibrationIdentity = paramCameraCalibrationIdentity;
    if (paramCameraCalibrationIdentity == null)
      cameraCalibrationIdentity = new VendorProductCalibrationIdentity(0, 0); 
    return new CameraCalibration(cameraCalibrationIdentity, paramSize, 0.0F, 0.0F, 0.0F, 0.0F, new float[8], false, true);
  }
  
  protected static double getAspectRatio(Size paramSize) {
    return paramSize.getWidth() / paramSize.getHeight();
  }
  
  public static double getDiagonal(Size paramSize) {
    return Math.sqrt((paramSize.getWidth() * paramSize.getWidth() + paramSize.getHeight() * paramSize.getHeight()));
  }
  
  public double getAspectRatio() {
    return getAspectRatio(this.size);
  }
  
  public double getDiagonal() {
    return getDiagonal(this.size);
  }
  
  public CameraCalibrationIdentity getIdentity() {
    return this.identity;
  }
  
  public boolean getRemove() {
    return this.remove;
  }
  
  public Size getSize() {
    return this.size;
  }
  
  public boolean isFake() {
    return this.isFake;
  }
  
  protected CameraCalibration memberwiseClone() {
    try {
      return (CameraCalibration)clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw AppUtil.getInstance().unreachable();
    } 
  }
  
  public CameraCalibration scaledTo(Size paramSize) {
    Assert.assertTrue(Misc.approximatelyEquals(getAspectRatio(paramSize), getAspectRatio(this.size)));
    double d = paramSize.getWidth() / this.size.getWidth();
    CameraCalibration cameraCalibration = memberwiseClone();
    cameraCalibration.size = paramSize;
    cameraCalibration.focalLengthX = (float)(cameraCalibration.focalLengthX * d);
    cameraCalibration.focalLengthY = (float)(cameraCalibration.focalLengthY * d);
    cameraCalibration.principalPointX = (float)(cameraCalibration.principalPointX * d);
    cameraCalibration.principalPointY = (float)(cameraCalibration.principalPointY * d);
    return cameraCalibration;
  }
  
  public String toString() {
    return Misc.formatInvariant("CameraCalibration(%s %dx%d f=%.3f,%.3f)", new Object[] { this.identity, Integer.valueOf(this.size.getWidth()), Integer.valueOf(this.size.getHeight()), Float.valueOf(this.focalLengthX), Float.valueOf(this.focalLengthY) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\camera\calibration\CameraCalibration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */