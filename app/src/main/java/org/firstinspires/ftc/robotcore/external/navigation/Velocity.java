package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.Locale;

public class Velocity {
  public long acquisitionTime;
  
  public DistanceUnit unit;
  
  public double xVeloc;
  
  public double yVeloc;
  
  public double zVeloc;
  
  public Velocity() {
    this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
  }
  
  public Velocity(DistanceUnit paramDistanceUnit, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    this.unit = paramDistanceUnit;
    this.xVeloc = paramDouble1;
    this.yVeloc = paramDouble2;
    this.zVeloc = paramDouble3;
    this.acquisitionTime = paramLong;
  }
  
  public String toString() {
    return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s/s", new Object[] { Double.valueOf(this.xVeloc), Double.valueOf(this.yVeloc), Double.valueOf(this.zVeloc), this.unit.toString() });
  }
  
  public Velocity toUnit(DistanceUnit paramDistanceUnit) {
    return (paramDistanceUnit != this.unit) ? new Velocity(paramDistanceUnit, paramDistanceUnit.fromUnit(this.unit, this.xVeloc), paramDistanceUnit.fromUnit(this.unit, this.yVeloc), paramDistanceUnit.fromUnit(this.unit, this.zVeloc), this.acquisitionTime) : this;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Velocity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */