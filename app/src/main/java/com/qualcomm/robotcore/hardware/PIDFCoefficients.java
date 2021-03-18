package com.qualcomm.robotcore.hardware;

import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class PIDFCoefficients {
  public MotorControlAlgorithm algorithm;
  
  public double d;
  
  public double f;
  
  public double i;
  
  public double p;
  
  public PIDFCoefficients() {
    this.f = 0.0D;
    this.d = 0.0D;
    this.i = 0.0D;
    this.p = 0.0D;
    this.algorithm = MotorControlAlgorithm.PIDF;
  }
  
  public PIDFCoefficients(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    this(paramDouble1, paramDouble2, paramDouble3, paramDouble4, MotorControlAlgorithm.PIDF);
  }
  
  public PIDFCoefficients(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, MotorControlAlgorithm paramMotorControlAlgorithm) {
    this.p = paramDouble1;
    this.i = paramDouble2;
    this.d = paramDouble3;
    this.f = paramDouble4;
    this.algorithm = paramMotorControlAlgorithm;
  }
  
  public PIDFCoefficients(PIDCoefficients paramPIDCoefficients) {
    this.p = paramPIDCoefficients.p;
    this.i = paramPIDCoefficients.i;
    this.d = paramPIDCoefficients.d;
    this.f = 0.0D;
    this.algorithm = MotorControlAlgorithm.LegacyPID;
  }
  
  public PIDFCoefficients(PIDFCoefficients paramPIDFCoefficients) {
    this.p = paramPIDFCoefficients.p;
    this.i = paramPIDFCoefficients.i;
    this.d = paramPIDFCoefficients.d;
    this.f = paramPIDFCoefficients.f;
    this.algorithm = paramPIDFCoefficients.algorithm;
  }
  
  public String toString() {
    return Misc.formatForUser("%s(p=%f i=%f d=%f f=%f alg=%s)", new Object[] { getClass().getSimpleName(), Double.valueOf(this.p), Double.valueOf(this.i), Double.valueOf(this.d), Double.valueOf(this.f), this.algorithm });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\PIDFCoefficients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */