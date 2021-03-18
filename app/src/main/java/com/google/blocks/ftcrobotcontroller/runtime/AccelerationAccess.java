package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

class AccelerationAccess extends Access {
  AccelerationAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Acceleration");
  }
  
  private Acceleration checkAcceleration(Object paramObject) {
    return checkArg(paramObject, Acceleration.class, "acceleration");
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, constructor = true)
  public Acceleration create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Acceleration();
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, constructor = true)
  public Acceleration create_withArgs(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    DistanceUnit distanceUnit = checkArg(paramString, DistanceUnit.class, "distanceUnit");
    return (distanceUnit != null) ? new Acceleration(distanceUnit, paramDouble1, paramDouble2, paramDouble3, paramLong) : null;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, methodName = {"fromGravity"})
  public Acceleration fromGravity(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    startBlockExecution(BlockType.FUNCTION, ".fromGravity");
    return Acceleration.fromGravity(paramDouble1, paramDouble2, paramDouble3, paramLong);
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, fieldName = {"acquisitionTime"})
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkAcceleration(paramObject);
    return (paramObject != null) ? ((Acceleration)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, fieldName = {"unit"})
  public String getDistanceUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".DistanceUnit");
    paramObject = checkAcceleration(paramObject);
    if (paramObject != null) {
      paramObject = ((Acceleration)paramObject).unit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, fieldName = {"xAccel"})
  public double getXAccel(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".XAccel");
    paramObject = checkAcceleration(paramObject);
    return (paramObject != null) ? ((Acceleration)paramObject).xAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, fieldName = {"yAccel"})
  public double getYAccel(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".YAccel");
    paramObject = checkAcceleration(paramObject);
    return (paramObject != null) ? ((Acceleration)paramObject).yAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, fieldName = {"zAccel"})
  public double getZAccel(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".ZAccel");
    paramObject = checkAcceleration(paramObject);
    return (paramObject != null) ? ((Acceleration)paramObject).zAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, methodName = {"toUnit"})
  public Acceleration toDistanceUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toDistanceUnit");
    paramObject = checkAcceleration(paramObject);
    DistanceUnit distanceUnit = checkArg(paramString, DistanceUnit.class, "distanceUnit");
    return (paramObject != null && distanceUnit != null) ? paramObject.toUnit(distanceUnit) : null;
  }
  
  @JavascriptInterface
  @Block(classes = {Acceleration.class}, methodName = {"toString"})
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkAcceleration(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AccelerationAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */