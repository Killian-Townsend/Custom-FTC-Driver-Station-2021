package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class PIDCoefficients {
  public double d;
  
  public double i;
  
  public double p;
  
  public PIDCoefficients() {
    this.d = 0.0D;
    this.i = 0.0D;
    this.p = 0.0D;
  }
  
  public PIDCoefficients(double paramDouble1, double paramDouble2, double paramDouble3) {
    this.p = paramDouble1;
    this.i = paramDouble2;
    this.d = paramDouble3;
  }
  
  public String toString() {
    return Misc.formatForUser("%s(p=%f i=%f d=%f)", new Object[] { getClass().getSimpleName(), Double.valueOf(this.p), Double.valueOf(this.i), Double.valueOf(this.d) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PIDCoefficients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */