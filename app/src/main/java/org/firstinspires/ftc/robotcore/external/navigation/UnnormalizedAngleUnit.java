package org.firstinspires.ftc.robotcore.external.navigation;

public enum UnnormalizedAngleUnit {
  DEGREES(0),
  RADIANS(0);
  
  public final byte bVal;
  
  static {
    UnnormalizedAngleUnit unnormalizedAngleUnit = new UnnormalizedAngleUnit("RADIANS", 1, 1);
    RADIANS = unnormalizedAngleUnit;
    $VALUES = new UnnormalizedAngleUnit[] { DEGREES, unnormalizedAngleUnit };
  }
  
  UnnormalizedAngleUnit(int paramInt1) {
    this.bVal = (byte)paramInt1;
  }
  
  public double fromDegrees(double paramDouble) {
    double d = paramDouble;
    if (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2)
      d = paramDouble / 180.0D * Math.PI; 
    return d;
  }
  
  public float fromDegrees(float paramFloat) {
    float f = paramFloat;
    if (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2)
      f = paramFloat / 180.0F * 3.1415927F; 
    return f;
  }
  
  public double fromRadians(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? paramDouble : (paramDouble / Math.PI * 180.0D);
  }
  
  public float fromRadians(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? paramFloat : (paramFloat / 3.1415927F * 180.0F);
  }
  
  public double fromUnit(UnnormalizedAngleUnit paramUnnormalizedAngleUnit, double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[paramUnnormalizedAngleUnit.ordinal()] != 2) ? fromRadians(paramDouble) : fromDegrees(paramDouble);
  }
  
  public float fromUnit(UnnormalizedAngleUnit paramUnnormalizedAngleUnit, float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[paramUnnormalizedAngleUnit.ordinal()] != 2) ? fromRadians(paramFloat) : fromDegrees(paramFloat);
  }
  
  public AngleUnit getNormalized() {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? AngleUnit.RADIANS : AngleUnit.DEGREES;
  }
  
  public double toDegrees(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? DEGREES.fromRadians(paramDouble) : DEGREES.fromDegrees(paramDouble);
  }
  
  public float toDegrees(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? DEGREES.fromRadians(paramFloat) : DEGREES.fromDegrees(paramFloat);
  }
  
  public double toRadians(double paramDouble) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? RADIANS.fromRadians(paramDouble) : RADIANS.fromDegrees(paramDouble);
  }
  
  public float toRadians(float paramFloat) {
    return (null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$UnnormalizedAngleUnit[ordinal()] != 2) ? RADIANS.fromRadians(paramFloat) : RADIANS.fromDegrees(paramFloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\UnnormalizedAngleUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */