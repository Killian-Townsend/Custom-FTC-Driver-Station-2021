package org.firstinspires.ftc.robotcore.internal.tfod;

import android.graphics.Rect;

class Zoom {
  double aspectRatio;
  
  double magnification;
  
  Zoom(double paramDouble1, double paramDouble2) {
    validateArguments(paramDouble1, paramDouble2);
    this.magnification = paramDouble1;
    this.aspectRatio = paramDouble2;
  }
  
  static boolean areEqual(double paramDouble1, double paramDouble2) {
    return (Math.abs(paramDouble1 - paramDouble2) <= 1.0E-4D);
  }
  
  static Rect getZoomArea(double paramDouble1, double paramDouble2, int paramInt1, int paramInt2) {
    double d1 = paramInt1;
    paramDouble1 = d1 / paramDouble1;
    paramDouble2 = paramDouble1 / paramDouble2;
    d1 = (d1 - paramDouble1) / 2.0D;
    double d2 = (paramInt2 - paramDouble2) / 2.0D;
    return new Rect((int)Math.round(d1), (int)Math.round(d2), (int)Math.round(d1 + paramDouble1), (int)Math.round(d2 + paramDouble2));
  }
  
  static boolean isZoomed(double paramDouble) {
    return areEqual(paramDouble, 1.0D) ^ true;
  }
  
  static void validateArguments(double paramDouble1, double paramDouble2) {
    if (paramDouble1 >= 0.9999D) {
      if (paramDouble2 > 1.0E-4D)
        return; 
      throw new IllegalArgumentException("aspectRatio must be greater than 0");
    } 
    throw new IllegalArgumentException("magnification must be greater than or equal to 1.0");
  }
  
  Rect getZoomArea(int paramInt1, int paramInt2) {
    return getZoomArea(this.magnification, this.aspectRatio, paramInt1, paramInt2);
  }
  
  boolean isZoomed() {
    return isZoomed(this.magnification);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\tfod\Zoom.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */