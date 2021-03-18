package org.firstinspires.ftc.robotcore.external.navigation;

import java.util.Locale;

public class Position {
  public long acquisitionTime;
  
  public DistanceUnit unit;
  
  public double x;
  
  public double y;
  
  public double z;
  
  public Position() {
    this(DistanceUnit.MM, 0.0D, 0.0D, 0.0D, 0L);
  }
  
  public Position(DistanceUnit paramDistanceUnit, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    this.unit = paramDistanceUnit;
    this.x = paramDouble1;
    this.y = paramDouble2;
    this.z = paramDouble3;
    this.acquisitionTime = paramLong;
  }
  
  public String toString() {
    return String.format(Locale.getDefault(), "(%.3f %.3f %.3f)%s", new Object[] { Double.valueOf(this.x), Double.valueOf(this.y), Double.valueOf(this.z), this.unit.toString() });
  }
  
  public Position toUnit(DistanceUnit paramDistanceUnit) {
    return (paramDistanceUnit != this.unit) ? new Position(paramDistanceUnit, paramDistanceUnit.fromUnit(this.unit, this.x), paramDistanceUnit.fromUnit(this.unit, this.y), paramDistanceUnit.fromUnit(this.unit, this.z), this.acquisitionTime) : this;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Position.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */