package org.firstinspires.ftc.robotcore.external.navigation;

public class Temperature {
  public long acquisitionTime;
  
  public double temperature;
  
  public TempUnit unit;
  
  public Temperature() {
    this(TempUnit.CELSIUS, 0.0D, 0L);
  }
  
  public Temperature(TempUnit paramTempUnit, double paramDouble, long paramLong) {
    this.unit = paramTempUnit;
    this.temperature = paramDouble;
    this.acquisitionTime = paramLong;
  }
  
  public Temperature toUnit(TempUnit paramTempUnit) {
    return (paramTempUnit != this.unit) ? new Temperature(paramTempUnit, paramTempUnit.fromUnit(this.unit, this.temperature), this.acquisitionTime) : this;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\Temperature.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */