package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

class VelocityAccess extends Access {
  VelocityAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Velocity");
  }
  
  private Velocity checkVelocity(Object paramObject) {
    return checkArg(paramObject, Velocity.class, "velocity");
  }
  
  @JavascriptInterface
  public Velocity create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Velocity();
  }
  
  @JavascriptInterface
  public Velocity create_withArgs(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    DistanceUnit distanceUnit = checkDistanceUnit(paramString);
    return (distanceUnit != null) ? new Velocity(distanceUnit, paramDouble1, paramDouble2, paramDouble3, paramLong) : null;
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkVelocity(paramObject);
    return (paramObject != null) ? ((Velocity)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public String getDistanceUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".DistanceUnit");
    paramObject = checkVelocity(paramObject);
    if (paramObject != null) {
      paramObject = ((Velocity)paramObject).unit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public double getXVeloc(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".XVeloc");
    paramObject = checkVelocity(paramObject);
    return (paramObject != null) ? ((Velocity)paramObject).xVeloc : 0.0D;
  }
  
  @JavascriptInterface
  public double getYVeloc(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".YVeloc");
    paramObject = checkVelocity(paramObject);
    return (paramObject != null) ? ((Velocity)paramObject).yVeloc : 0.0D;
  }
  
  @JavascriptInterface
  public double getZVeloc(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".ZVeloc");
    paramObject = checkVelocity(paramObject);
    return (paramObject != null) ? ((Velocity)paramObject).zVeloc : 0.0D;
  }
  
  @JavascriptInterface
  public Velocity toDistanceUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toDistanceUnit");
    paramObject = checkVelocity(paramObject);
    DistanceUnit distanceUnit = checkDistanceUnit(paramString);
    return (paramObject != null && distanceUnit != null) ? paramObject.toUnit(distanceUnit) : null;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkVelocity(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VelocityAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */