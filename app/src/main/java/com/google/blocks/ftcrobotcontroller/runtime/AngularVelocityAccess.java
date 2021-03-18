package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;

class AngularVelocityAccess extends Access {
  AngularVelocityAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AngularVelocity");
  }
  
  private AngularVelocity checkAngularVelocity(Object paramObject) {
    return checkArg(paramObject, AngularVelocity.class, "angularVelocity");
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, constructor = true)
  public AngularVelocity create() {
    startBlockExecution(BlockType.CREATE, "");
    return new AngularVelocity();
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, constructor = true)
  public AngularVelocity create_withArgs(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (angleUnit != null) ? new AngularVelocity(angleUnit, paramFloat1, paramFloat2, paramFloat3, paramLong) : null;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"acquisitionTime"})
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkAngularVelocity(paramObject);
    return (paramObject != null) ? ((AngularVelocity)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"unit"})
  public String getAngleUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AngleUnit");
    paramObject = checkAngularVelocity(paramObject);
    if (paramObject != null) {
      paramObject = ((AngularVelocity)paramObject).unit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"xRotationRate", "yRotationRate", "zRotationRate"})
  public float getRotationRate(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getRotationRate");
    paramObject = checkAngularVelocity(paramObject);
    Axis axis = checkArg(paramString, Axis.class, "axis");
    if (paramObject != null && axis != null) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$Axis[axis.ordinal()];
      if (i != 1) {
        if (i != 2) {
          if (i != 3) {
            if (i == 4)
              reportInvalidArg("axis", "Axis.X, Axis.Y, or Axis.Z"); 
          } else {
            return ((AngularVelocity)paramObject).zRotationRate;
          } 
        } else {
          return ((AngularVelocity)paramObject).yRotationRate;
        } 
      } else {
        return ((AngularVelocity)paramObject).xRotationRate;
      } 
    } 
    return 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"xRotationRate"})
  public float getXRotationRate(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".XRotationRate");
    paramObject = checkAngularVelocity(paramObject);
    return (paramObject != null) ? ((AngularVelocity)paramObject).xRotationRate : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"yRotationRate"})
  public float getYRotationRate(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".YRotationRate");
    paramObject = checkAngularVelocity(paramObject);
    return (paramObject != null) ? ((AngularVelocity)paramObject).yRotationRate : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, fieldName = {"zRotationRate"})
  public float getZRotationRate(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".ZRotationRate");
    paramObject = checkAngularVelocity(paramObject);
    return (paramObject != null) ? ((AngularVelocity)paramObject).zRotationRate : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {AngularVelocity.class}, methodName = {"toAngleUnit"})
  public AngularVelocity toAngleUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toAngleUnit");
    paramObject = checkAngularVelocity(paramObject);
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (paramObject != null && angleUnit != null) ? paramObject.toAngleUnit(angleUnit) : null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AngularVelocityAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */