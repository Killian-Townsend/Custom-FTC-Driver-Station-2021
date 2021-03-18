package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

class MatrixFAccess extends Access {
  MatrixFAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "MatrixF");
  }
  
  @JavascriptInterface
  public void add_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".add");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.add((MatrixF)paramObject2); 
  }
  
  @JavascriptInterface
  public void add_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".add");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.add((VectorF)paramObject2); 
  }
  
  @JavascriptInterface
  public MatrixF added_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".added");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.added((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public MatrixF added_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".added");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.added((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public MatrixF diagonalMatrix(int paramInt1, int paramInt2) {
    startBlockExecution(BlockType.FUNCTION, ".diagonalMatrix");
    return MatrixF.diagonalMatrix(paramInt1, paramInt2);
  }
  
  @JavascriptInterface
  public MatrixF diagonalMatrix_withVector(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".diagonalMatrix");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? MatrixF.diagonalMatrix((VectorF)paramObject) : null;
  }
  
  @JavascriptInterface
  public String formatAsTransform(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".formatAsTransform");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.formatAsTransform() : "";
  }
  
  @JavascriptInterface
  public String formatAsTransform_withArgs(Object paramObject, String paramString1, String paramString2, String paramString3) {
    startBlockExecution(BlockType.FUNCTION, ".formatAsTransform");
    paramObject = checkMatrixF(paramObject);
    AxesReference axesReference = checkAxesReference(paramString1);
    AxesOrder axesOrder = checkAxesOrder(paramString2);
    AngleUnit angleUnit = checkAngleUnit(paramString3);
    return (paramObject != null && axesReference != null && axesOrder != null && angleUnit != null) ? paramObject.formatAsTransform(axesReference, axesOrder, angleUnit) : "";
  }
  
  @JavascriptInterface
  public float get(Object paramObject, int paramInt1, int paramInt2) {
    startBlockExecution(BlockType.FUNCTION, ".get");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.get(paramInt1, paramInt2) : 0.0F;
  }
  
  @JavascriptInterface
  public VectorF getColumn(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".getColumn");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.getColumn(paramInt) : null;
  }
  
  @JavascriptInterface
  public int getNumCols(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".NumCols");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.numCols() : 0;
  }
  
  @JavascriptInterface
  public int getNumRows(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".NumRows");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.numRows() : 0;
  }
  
  @JavascriptInterface
  public VectorF getRow(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".getRow");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.getRow(paramInt) : null;
  }
  
  @JavascriptInterface
  public VectorF getTranslation(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".getTranslation");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.getTranslation() : null;
  }
  
  @JavascriptInterface
  public MatrixF identityMatrix(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".identityMatrix");
    return MatrixF.identityMatrix(paramInt);
  }
  
  @JavascriptInterface
  public MatrixF inverted(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".inverted");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.inverted() : null;
  }
  
  @JavascriptInterface
  public MatrixF multiplied_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.multiplied((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public MatrixF multiplied_withScale(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.multiplied(paramFloat) : null;
  }
  
  @JavascriptInterface
  public VectorF multiplied_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.multiplied((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public void multiply_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiply");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.multiply((MatrixF)paramObject2); 
  }
  
  @JavascriptInterface
  public void multiply_withScale(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".multiply");
    paramObject = checkMatrixF(paramObject);
    if (paramObject != null)
      paramObject.multiply(paramFloat); 
  }
  
  @JavascriptInterface
  public void multiply_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiply");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.multiply((VectorF)paramObject2); 
  }
  
  @JavascriptInterface
  public void put(Object paramObject, int paramInt1, int paramInt2, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".put");
    paramObject = checkMatrixF(paramObject);
    if (paramObject != null)
      paramObject.put(paramInt1, paramInt2, paramFloat); 
  }
  
  @JavascriptInterface
  public MatrixF slice(Object paramObject, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    startBlockExecution(BlockType.FUNCTION, ".slice");
    paramObject = checkMatrixF(paramObject);
    return (MatrixF)((paramObject != null) ? paramObject.slice(paramInt1, paramInt2, paramInt3, paramInt4) : null);
  }
  
  @JavascriptInterface
  public void subtract_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtract");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.subtract((MatrixF)paramObject2); 
  }
  
  @JavascriptInterface
  public void subtract_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtract");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.subtract((VectorF)paramObject2); 
  }
  
  @JavascriptInterface
  public MatrixF subtracted_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtracted");
    paramObject1 = checkArg(paramObject1, MatrixF.class, "matrix1");
    paramObject2 = checkArg(paramObject2, MatrixF.class, "matrix2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.subtracted((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public MatrixF subtracted_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtracted");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.subtracted((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
  
  @JavascriptInterface
  public VectorF toVector(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toVector");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.toVector() : null;
  }
  
  @JavascriptInterface
  public VectorF transform(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".transform");
    paramObject1 = checkMatrixF(paramObject1);
    paramObject2 = checkVectorF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.transform((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public MatrixF transposed(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".transposed");
    paramObject = checkMatrixF(paramObject);
    return (paramObject != null) ? paramObject.transposed() : null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\MatrixFAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */