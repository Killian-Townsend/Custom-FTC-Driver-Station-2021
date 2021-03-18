package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;

class PositionAccess extends Access {
  PositionAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Position");
  }
  
  private Position checkPosition(Object paramObject) {
    return checkArg(paramObject, Position.class, "position");
  }
  
  @JavascriptInterface
  public Position create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Position();
  }
  
  @JavascriptInterface
  public Position create_withArgs(String paramString, double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    DistanceUnit distanceUnit = checkDistanceUnit(paramString);
    return (distanceUnit != null) ? new Position(distanceUnit, paramDouble1, paramDouble2, paramDouble3, paramLong) : null;
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkPosition(paramObject);
    return (paramObject != null) ? ((Position)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public String getDistanceUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".DistanceUnit");
    paramObject = checkPosition(paramObject);
    if (paramObject != null) {
      paramObject = ((Position)paramObject).unit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public double getX(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".X");
    paramObject = checkPosition(paramObject);
    return (paramObject != null) ? ((Position)paramObject).x : 0.0D;
  }
  
  @JavascriptInterface
  public double getY(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Y");
    paramObject = checkPosition(paramObject);
    return (paramObject != null) ? ((Position)paramObject).y : 0.0D;
  }
  
  @JavascriptInterface
  public double getZ(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Z");
    paramObject = checkPosition(paramObject);
    return (paramObject != null) ? ((Position)paramObject).z : 0.0D;
  }
  
  @JavascriptInterface
  public Position toDistanceUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toDistanceUnit");
    paramObject = checkPosition(paramObject);
    DistanceUnit distanceUnit = checkDistanceUnit(paramString);
    return (paramObject != null && distanceUnit != null) ? paramObject.toUnit(distanceUnit) : null;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkPosition(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\PositionAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */