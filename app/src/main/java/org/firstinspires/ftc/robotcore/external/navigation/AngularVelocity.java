package org.firstinspires.ftc.robotcore.external.navigation;

public class AngularVelocity {
  public long acquisitionTime;
  
  public AngleUnit unit;
  
  public float xRotationRate;
  
  public float yRotationRate;
  
  public float zRotationRate;
  
  public AngularVelocity() {
    this(AngleUnit.DEGREES, 0.0F, 0.0F, 0.0F, 0L);
  }
  
  public AngularVelocity(AngleUnit paramAngleUnit, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong) {
    this.unit = paramAngleUnit;
    this.xRotationRate = paramFloat1;
    this.yRotationRate = paramFloat2;
    this.zRotationRate = paramFloat3;
    this.acquisitionTime = paramLong;
  }
  
  public AngularVelocity toAngleUnit(AngleUnit paramAngleUnit) {
    return (paramAngleUnit != this.unit) ? new AngularVelocity(paramAngleUnit, paramAngleUnit.fromUnit(this.unit, this.xRotationRate), paramAngleUnit.fromUnit(this.unit, this.yRotationRate), paramAngleUnit.fromUnit(this.unit, this.zRotationRate), this.acquisitionTime) : this;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\AngularVelocity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */