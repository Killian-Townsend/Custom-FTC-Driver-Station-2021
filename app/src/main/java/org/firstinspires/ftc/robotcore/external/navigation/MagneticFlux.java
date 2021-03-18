package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.Locale;

public class MagneticFlux {
  public long acquisitionTime;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public MagneticFlux() {
    this(0.0D, 0.0D, 0.0D, 0L);
  }
  
  public MagneticFlux(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    this.x = paramDouble1;
    this.y = paramDouble2;
    this.z = paramDouble3;
    this.acquisitionTime = paramLong;
  }
  
  public String toString() {
    return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)mT", new Object[] { Double.valueOf(this.x * 1000.0D), Double.valueOf(this.y * 1000.0D), Double.valueOf(this.z * 1000.0D) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\MagneticFlux.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */