package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;

class TemperatureAccess extends Access {
  TemperatureAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Temperature");
  }
  
  private TempUnit checkTempUnit(String paramString) {
    return checkArg(paramString, TempUnit.class, "tempUnit");
  }
  
  private Temperature checkTemperature(Object paramObject) {
    return checkArg(paramObject, Temperature.class, "temperature");
  }
  
  @JavascriptInterface
  public Temperature create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Temperature();
  }
  
  @JavascriptInterface
  public Temperature create_withArgs(String paramString, double paramDouble, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    TempUnit tempUnit = checkTempUnit(paramString);
    return (tempUnit != null) ? new Temperature(tempUnit, paramDouble, paramLong) : null;
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkTemperature(paramObject);
    return (paramObject != null) ? ((Temperature)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public String getTempUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".TempUnit");
    paramObject = checkTemperature(paramObject);
    if (paramObject != null) {
      paramObject = ((Temperature)paramObject).unit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public double getTemperature(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Temperature");
    paramObject = checkTemperature(paramObject);
    return (paramObject != null) ? ((Temperature)paramObject).temperature : 0.0D;
  }
  
  @JavascriptInterface
  public Temperature toTempUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toTempUnit");
    paramObject = checkTemperature(paramObject);
    TempUnit tempUnit = checkTempUnit(paramString);
    return (paramObject != null && tempUnit != null) ? paramObject.toUnit(tempUnit) : null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TemperatureAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */