package org.firstinspires.ftc.robotcore.internal.tfod;

import com.google.ftcresearch.tfod.util.Size;
import java.util.Objects;

class CameraInformation {
  final float horizontalFocalLength;
  
  final int rotation;
  
  final Size size;
  
  final float verticalFocalLength;
  
  CameraInformation(int paramInt1, float paramFloat1, float paramFloat2, int paramInt2, int paramInt3) {
    this.rotation = paramInt1;
    this.horizontalFocalLength = paramFloat1;
    this.verticalFocalLength = paramFloat2;
    this.size = new Size(paramInt2, paramInt3);
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject != null) {
      if (getClass() != paramObject.getClass())
        return false; 
      paramObject = paramObject;
      return (this.rotation == ((CameraInformation)paramObject).rotation && this.horizontalFocalLength == ((CameraInformation)paramObject).horizontalFocalLength && this.verticalFocalLength == ((CameraInformation)paramObject).verticalFocalLength && this.size.equals(((CameraInformation)paramObject).size));
    } 
    return false;
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { Integer.valueOf(this.rotation), Float.valueOf(this.horizontalFocalLength), Float.valueOf(this.verticalFocalLength), this.size });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\CameraInformation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */