package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.Locale;

public enum DistanceUnit {
  CM,
  INCH,
  METER(0),
  MM(0);
  
  public static final double infinity = 1.7976931348623157E308D;
  
  public static final double mPerInch = 0.0254D;
  
  public static final double mmPerInch = 25.4D;
  
  public final byte bVal;
  
  static {
    CM = new DistanceUnit("CM", 1, 1);
    MM = new DistanceUnit("MM", 2, 2);
    DistanceUnit distanceUnit = new DistanceUnit("INCH", 3, 3);
    INCH = distanceUnit;
    $VALUES = new DistanceUnit[] { METER, CM, MM, distanceUnit };
  }
  
  DistanceUnit(int paramInt1) {
    this.bVal = (byte)paramInt1;
  }
  
  public double fromCm(double paramDouble) {
    if (paramDouble == Double.MAX_VALUE)
      return Double.MAX_VALUE; 
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    double d = paramDouble;
    if (i != 2) {
      if (i != 3)
        return (i != 4) ? (paramDouble / 100.0D) : fromMeters(METER.fromCm(paramDouble)); 
      d = paramDouble * 10.0D;
    } 
    return d;
  }
  
  public double fromInches(double paramDouble) {
    if (paramDouble == Double.MAX_VALUE)
      return Double.MAX_VALUE; 
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    if (i != 2) {
      if (i != 3) {
        double d2 = paramDouble;
        if (i != 4)
          d2 = paramDouble * 0.0254D; 
        return d2;
      } 
      double d1 = paramDouble * 0.0254D;
      paramDouble = 1000.0D;
      return d1 * paramDouble;
    } 
    double d = paramDouble * 0.0254D;
    paramDouble = 100.0D;
    return d * paramDouble;
  }
  
  public double fromMeters(double paramDouble) {
    if (paramDouble == Double.MAX_VALUE)
      return Double.MAX_VALUE; 
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? paramDouble : (paramDouble / 0.0254D)) : (paramDouble * 1000.0D)) : (paramDouble * 100.0D);
  }
  
  public double fromMm(double paramDouble) {
    if (paramDouble == Double.MAX_VALUE)
      return Double.MAX_VALUE; 
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    if (i != 2) {
      double d = paramDouble;
      if (i != 3) {
        if (i != 4)
          return paramDouble / 1000.0D; 
        d = fromMeters(METER.fromMm(paramDouble));
      } 
      return d;
    } 
    return paramDouble / 10.0D;
  }
  
  public double fromUnit(DistanceUnit paramDistanceUnit, double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[paramDistanceUnit.ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? fromMeters(paramDouble) : fromInches(paramDouble)) : fromMm(paramDouble)) : fromCm(paramDouble);
  }
  
  public double toCm(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? CM.fromMeters(paramDouble) : CM.fromInches(paramDouble)) : CM.fromMm(paramDouble)) : CM.fromCm(paramDouble);
  }
  
  public double toInches(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? INCH.fromMeters(paramDouble) : INCH.fromInches(paramDouble)) : INCH.fromMm(paramDouble)) : INCH.fromCm(paramDouble);
  }
  
  public double toMeters(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? METER.fromMeters(paramDouble) : METER.fromInches(paramDouble)) : METER.fromMm(paramDouble)) : METER.fromCm(paramDouble);
  }
  
  public double toMm(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? MM.fromMeters(paramDouble) : MM.fromInches(paramDouble)) : MM.fromMm(paramDouble)) : MM.fromCm(paramDouble);
  }
  
  public String toString() {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? "m" : "in") : "mm") : "cm";
  }
  
  public String toString(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$DistanceUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? String.format(Locale.getDefault(), "%.3fm", new Object[] { Double.valueOf(paramDouble) }) : String.format(Locale.getDefault(), "%.2fin", new Object[] { Double.valueOf(paramDouble) })) : String.format(Locale.getDefault(), "%.0fmm", new Object[] { Double.valueOf(paramDouble) })) : String.format(Locale.getDefault(), "%.1fcm", new Object[] { Double.valueOf(paramDouble) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\DistanceUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */