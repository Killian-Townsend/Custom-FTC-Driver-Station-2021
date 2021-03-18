package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

class OpenGLMatrixAccess extends Access {
  OpenGLMatrixAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "OpenGLMatrix");
  }
  
  @JavascriptInterface
  public OpenGLMatrix create() {
    startBlockExecution(BlockType.CREATE, "");
    return new OpenGLMatrix();
  }
  
  @JavascriptInterface
  public OpenGLMatrix create_withMatrixF(Object paramObject) {
    startBlockExecution(BlockType.CREATE, "");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? new OpenGLMatrix((MatrixF)paramObject) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix identityMatrix() {
    startBlockExecution(BlockType.FUNCTION, ".identityMatrix");
    return OpenGLMatrix.identityMatrix();
  }
  
  @JavascriptInterface
  public OpenGLMatrix multiplied(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject1 = checkArg(paramObject1, OpenGLMatrix.class, "matrix1");
    paramObject2 = checkArg(paramObject2, OpenGLMatrix.class, "matrix2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.multiplied((OpenGLMatrix)paramObject2) : null;
  }
  
  @JavascriptInterface
  public void multiply(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiply");
    paramObject1 = checkArg(paramObject1, OpenGLMatrix.class, "matrix1");
    paramObject2 = checkArg(paramObject2, OpenGLMatrix.class, "matrix2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.multiply((OpenGLMatrix)paramObject2); 
  }
  
  @JavascriptInterface
  public void rotate(Object paramObject, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    startBlockExecution(BlockType.FUNCTION, ".rotate");
    paramObject = checkOpenGLMatrix(paramObject);
    AngleUnit angleUnit = checkAngleUnit(paramString);
    if (paramObject != null && angleUnit != null)
      paramObject.rotate(angleUnit, paramFloat1, paramFloat2, paramFloat3, paramFloat4); 
  }
  
  @JavascriptInterface
  public void rotate_withAxesArgs(Object paramObject, String paramString1, String paramString2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".rotate");
    paramObject = checkOpenGLMatrix(paramObject);
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    if (paramObject != null && axesReference != null && axesOrder != null && angleUnit != null)
      paramObject.rotate(axesReference, axesOrder, angleUnit, paramFloat1, paramFloat2, paramFloat3); 
  }
  
  @JavascriptInterface
  public OpenGLMatrix rotated(Object paramObject, String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    startBlockExecution(BlockType.FUNCTION, ".rotated");
    paramObject = checkOpenGLMatrix(paramObject);
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (paramObject != null && angleUnit != null) ? paramObject.rotated(angleUnit, paramFloat1, paramFloat2, paramFloat3, paramFloat4) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix rotated_withAxesArgs(Object paramObject, String paramString1, String paramString2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".rotated");
    paramObject = checkOpenGLMatrix(paramObject);
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (paramObject != null && axesReference != null && axesOrder != null && angleUnit != null) ? paramObject.rotated(axesReference, axesOrder, angleUnit, paramFloat1, paramFloat2, paramFloat3) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix rotation(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    startBlockExecution(BlockType.FUNCTION, ".rotation");
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (angleUnit != null) ? OpenGLMatrix.rotation(angleUnit, paramFloat1, paramFloat2, paramFloat3, paramFloat4) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix rotation_withAxesArgs(String paramString1, String paramString2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".rotation");
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (axesReference != null && axesOrder != null && angleUnit != null) ? OpenGLMatrix.rotation(axesReference, axesOrder, angleUnit, paramFloat1, paramFloat2, paramFloat3) : null;
  }
  
  @JavascriptInterface
  public void scale_with1(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".scale");
    paramObject = checkOpenGLMatrix(paramObject);
    if (paramObject != null)
      paramObject.scale(paramFloat); 
  }
  
  @JavascriptInterface
  public void scale_with3(Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".scale");
    paramObject = checkOpenGLMatrix(paramObject);
    if (paramObject != null)
      paramObject.scale(paramFloat1, paramFloat2, paramFloat3); 
  }
  
  @JavascriptInterface
  public OpenGLMatrix scaled_with1(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".scaled");
    paramObject = checkOpenGLMatrix(paramObject);
    return (paramObject != null) ? paramObject.scaled(paramFloat) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix scaled_with3(Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".scaled");
    paramObject = checkOpenGLMatrix(paramObject);
    return (paramObject != null) ? paramObject.scaled(paramFloat1, paramFloat2, paramFloat3) : null;
  }
  
  @JavascriptInterface
  public void translate(Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".translate");
    paramObject = checkOpenGLMatrix(paramObject);
    if (paramObject != null)
      paramObject.translate(paramFloat1, paramFloat2, paramFloat3); 
  }
  
  @JavascriptInterface
  public OpenGLMatrix translated(Object paramObject, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".translated");
    paramObject = checkOpenGLMatrix(paramObject);
    return (paramObject != null) ? paramObject.translated(paramFloat1, paramFloat2, paramFloat3) : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix translation(float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".translation");
    return OpenGLMatrix.translation(paramFloat1, paramFloat2, paramFloat3);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\OpenGLMatrixAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */