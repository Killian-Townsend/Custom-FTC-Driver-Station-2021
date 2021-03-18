package org.firstinspires.ftc.robotcore.internal.ui;

public class ProgressParameters {
  public int cur = 0;
  
  public int max = 100;
  
  public ProgressParameters() {}
  
  public ProgressParameters(int paramInt1, int paramInt2) {
    this.cur = paramInt1;
    this.max = paramInt2;
  }
  
  public static ProgressParameters fromFraction(double paramDouble) {
    return fromFraction(paramDouble, 100);
  }
  
  public static ProgressParameters fromFraction(double paramDouble, int paramInt) {
    return new ProgressParameters((int)Math.round(paramDouble * paramInt), paramInt);
  }
  
  public double fractionComplete() {
    return this.cur / this.max;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\ui\ProgressParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */