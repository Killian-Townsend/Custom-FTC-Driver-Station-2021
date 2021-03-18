package org.firstinspires.ftc.robotcore.external.navigation;

public enum AngleUnit {
  DEGREES(0),
  RADIANS(0);
  
  public static final float Pif = 3.1415927F;
  
  protected static final double TwoPi = 6.283185307179586D;
  
  public final byte bVal;
  
  static {
    AngleUnit angleUnit = new AngleUnit("RADIANS", 1, 1);
    RADIANS = angleUnit;
    $VALUES = new AngleUnit[] { DEGREES, angleUnit };
  }
  
  AngleUnit(int paramInt1) {
    this.bVal = (byte)paramInt1;
  }
  
  public static double normalizeDegrees(double paramDouble) {
    double d;
    while (true) {
      d = paramDouble;
      if (paramDouble >= 180.0D) {
        paramDouble -= 360.0D;
        continue;
      } 
      break;
    } 
    while (d < -180.0D)
      d += 360.0D; 
    return d;
  }
  
  public static float normalizeDegrees(float paramFloat) {
    return (float)normalizeDegrees(paramFloat);
  }
  
  public static double normalizeRadians(double paramDouble) {
    double d;
    while (true) {
      d = paramDouble;
      if (paramDouble >= Math.PI) {
        paramDouble -= 6.283185307179586D;
        continue;
      } 
      break;
    } 
    while (d < -3.141592653589793D)
      d += 6.283185307179586D; 
    return d;
  }
  
  public static float normalizeRadians(float paramFloat) {
    return (float)normalizeRadians(paramFloat);
  }
  
  public double fromDegrees(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalize(paramDouble / 180.0D * Math.PI) : normalize(paramDouble);
  }
  
  public float fromDegrees(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalize(paramFloat / 180.0F * 3.1415927F) : normalize(paramFloat);
  }
  
  public double fromRadians(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalize(paramDouble) : normalize(paramDouble / Math.PI * 180.0D);
  }
  
  public float fromRadians(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalize(paramFloat) : normalize(paramFloat / 3.1415927F * 180.0F);
  }
  
  public double fromUnit(AngleUnit paramAngleUnit, double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[paramAngleUnit.ordinal()] != 2) ? fromRadians(paramDouble) : fromDegrees(paramDouble);
  }
  
  public float fromUnit(AngleUnit paramAngleUnit, float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[paramAngleUnit.ordinal()] != 2) ? fromRadians(paramFloat) : fromDegrees(paramFloat);
  }
  
  public UnnormalizedAngleUnit getUnnormalized() {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? UnnormalizedAngleUnit.RADIANS : UnnormalizedAngleUnit.DEGREES;
  }
  
  public double normalize(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalizeRadians(paramDouble) : normalizeDegrees(paramDouble);
  }
  
  public float normalize(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? normalizeRadians(paramFloat) : normalizeDegrees(paramFloat);
  }
  
  public double toDegrees(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? DEGREES.fromRadians(paramDouble) : DEGREES.fromDegrees(paramDouble);
  }
  
  public float toDegrees(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? DEGREES.fromRadians(paramFloat) : DEGREES.fromDegrees(paramFloat);
  }
  
  public double toRadians(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? RADIANS.fromRadians(paramDouble) : RADIANS.fromDegrees(paramDouble);
  }
  
  public float toRadians(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$AngleUnit[ordinal()] != 2) ? RADIANS.fromRadians(paramFloat) : RADIANS.fromDegrees(paramFloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\AngleUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */