package com.qualcomm.robotcore.util;

public class DifferentialControlLoopCoefficients {
  public double d = 0.0D;
  
  public double i = 0.0D;
  
  public double p = 0.0D;
  
  public DifferentialControlLoopCoefficients() {}
  
  public DifferentialControlLoopCoefficients(double paramDouble1, double paramDouble2, double paramDouble3) {
    this.p = paramDouble1;
    this.i = paramDouble2;
    this.d = paramDouble3;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\DifferentialControlLoopCoefficients.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */