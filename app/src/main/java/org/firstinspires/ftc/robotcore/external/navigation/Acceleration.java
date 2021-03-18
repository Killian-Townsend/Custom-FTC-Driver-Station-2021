package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.Locale;

public class Acceleration {
  public static final double earthGravity = 9.80665D;
  
  public long acquisitionTime;
  
  public DistanceUnit unit;
  
  public double xAccel;
  
  public double yAccel;
  
  public double zAccel;
  
  public Acceleration() {
    this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
  }
  
  public Acceleration(DistanceUnit paramDistanceUnit, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    this.unit = paramDistanceUnit;
    this.xAccel = paramDouble1;
    this.yAccel = paramDouble2;
    this.zAccel = paramDouble3;
    this.acquisitionTime = paramLong;
  }
  
  public static Acceleration fromGravity(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    return new Acceleration(DistanceUnit.METER, paramDouble1 * 9.80665D, paramDouble2 * 9.80665D, paramDouble3 * 9.80665D, paramLong);
  }
  
  public String toString() {
    return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s/s^2", new Object[] { Double.valueOf(this.xAccel), Double.valueOf(this.yAccel), Double.valueOf(this.zAccel), this.unit.toString() });
  }
  
  public Acceleration toUnit(DistanceUnit paramDistanceUnit) {
    return (paramDistanceUnit != this.unit) ? new Acceleration(paramDistanceUnit, paramDistanceUnit.fromUnit(this.unit, this.xAccel), paramDistanceUnit.fromUnit(this.unit, this.yAccel), paramDistanceUnit.fromUnit(this.unit, this.zAccel), this.acquisitionTime) : this;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Acceleration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */