package org.firstinspires.ftc.robotcore.external.navigation;

public enum TempUnit {
  CELSIUS(0),
  FARENHEIT(1),
  KELVIN(1);
  
  public static final double CperF = 0.5555555555555556D;
  
  public static final double zeroCelsiusF = 32.0D;
  
  public static final double zeroCelsiusK = 273.15D;
  
  public final byte bVal;
  
  static {
    TempUnit tempUnit = new TempUnit("KELVIN", 2, 2);
    KELVIN = tempUnit;
    $VALUES = new TempUnit[] { CELSIUS, FARENHEIT, tempUnit };
  }
  
  TempUnit(int paramInt1) {
    this.bVal = (byte)paramInt1;
  }
  
  public double fromCelsius(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    if (i != 2) {
      if (i != 3)
        return paramDouble; 
      paramDouble /= 0.5555555555555556D;
      double d1 = 32.0D;
      return paramDouble + d1;
    } 
    double d = 273.15D;
    return paramDouble + d;
  }
  
  public double fromFarenheit(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    if (i != 2) {
      double d = paramDouble;
      if (i != 3)
        d = (paramDouble - 32.0D) * 0.5555555555555556D; 
      return d;
    } 
    return fromCelsius(CELSIUS.fromFarenheit(paramDouble));
  }
  
  public double fromKelvin(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    double d = paramDouble;
    if (i != 2) {
      if (i != 3)
        return paramDouble - 273.15D; 
      d = fromCelsius(CELSIUS.fromKelvin(paramDouble));
    } 
    return d;
  }
  
  public double fromUnit(TempUnit paramTempUnit, double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[paramTempUnit.ordinal()];
    return (i != 2) ? ((i != 3) ? fromCelsius(paramDouble) : fromFarenheit(paramDouble)) : fromKelvin(paramDouble);
  }
  
  double toCelsius(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? CELSIUS.fromCelsius(paramDouble) : CELSIUS.fromFarenheit(paramDouble)) : CELSIUS.fromKelvin(paramDouble);
  }
  
  double toFarenheit(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? FARENHEIT.fromCelsius(paramDouble) : FARENHEIT.fromFarenheit(paramDouble)) : FARENHEIT.fromKelvin(paramDouble);
  }
  
  double toKelvin(double paramDouble) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$TempUnit[ordinal()];
    return (i != 2) ? ((i != 3) ? KELVIN.fromCelsius(paramDouble) : KELVIN.fromFarenheit(paramDouble)) : KELVIN.fromKelvin(paramDouble);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\TempUnit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */