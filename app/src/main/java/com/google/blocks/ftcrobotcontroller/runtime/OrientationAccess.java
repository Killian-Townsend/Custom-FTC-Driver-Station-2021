package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

class OrientationAccess extends Access {
  OrientationAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Orientation");
  }
  
  private Orientation checkOrientation(Object paramObject) {
    return checkArg(paramObject, Orientation.class, "orientation");
  }
  
  @JavascriptInterface
  public Orientation create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Orientation();
  }
  
  @JavascriptInterface
  public Orientation create_withArgs(String paramString1, String paramString2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (axesReference != null && axesOrder != null && angleUnit != null) ? new Orientation(axesReference, axesOrder, angleUnit, paramFloat1, paramFloat2, paramFloat3, paramLong) : null;
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? ((Orientation)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public String getAngleUnit(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AngleUnit");
    paramObject = checkOrientation(paramObject);
    if (paramObject != null) {
      paramObject = ((Orientation)paramObject).angleUnit;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public String getAxesOrder(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AxesOrder");
    paramObject = checkOrientation(paramObject);
    if (paramObject != null) {
      paramObject = ((Orientation)paramObject).axesOrder;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public String getAxesReference(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AxesReference");
    paramObject = checkOrientation(paramObject);
    if (paramObject != null) {
      paramObject = ((Orientation)paramObject).axesReference;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public float getFirstAngle(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".FirstAngle");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? ((Orientation)paramObject).firstAngle : 0.0F;
  }
  
  @JavascriptInterface
  public Orientation getOrientation(Object paramObject, String paramString1, String paramString2, String paramString3) {
    startBlockExecution(BlockType.FUNCTION, ".getOrientation");
    paramObject = checkMatrixF(paramObject);
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (paramObject != null && axesReference != null && axesOrder != null && angleUnit != null) ? Orientation.getOrientation((MatrixF)paramObject, axesReference, axesOrder, angleUnit) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix getRotationMatrix(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".getRotationMatrix");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? paramObject.getRotationMatrix() : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix getRotationMatrix_withArgs(String paramString1, String paramString2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".getRotationMatrix");
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (axesReference != null && axesOrder != null && angleUnit != null) ? Orientation.getRotationMatrix(axesReference, axesOrder, angleUnit, paramFloat1, paramFloat2, paramFloat3) : null;
  }
  
  @JavascriptInterface
  public float getSecondAngle(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".SecondAngle");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? ((Orientation)paramObject).secondAngle : 0.0F;
  }
  
  @JavascriptInterface
  public float getThirdAngle(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".ThirdAngle");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? ((Orientation)paramObject).thirdAngle : 0.0F;
  }
  
  @JavascriptInterface
  public Orientation toAngleUnit(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toAngleUnit");
    paramObject = checkOrientation(paramObject);
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (paramObject != null && angleUnit != null) ? paramObject.toAngleUnit(angleUnit) : null;
  }
  
  @JavascriptInterface
  public Orientation toAxesOrder(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toAxesOrder");
    paramObject = checkOrientation(paramObject);
    AxesOrder axesOrder = checkAxesOrder(paramString);
    return (paramObject != null && axesOrder != null) ? paramObject.toAxesOrder(axesOrder) : null;
  }
  
  @JavascriptInterface
  public Orientation toAxesReference(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toAxesReference");
    paramObject = checkOrientation(paramObject);
    AxesReference axesReference = checkAxesReference(paramString);
    return (paramObject != null && axesReference != null) ? paramObject.toAxesReference(axesReference) : null;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkOrientation(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\OrientationAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */