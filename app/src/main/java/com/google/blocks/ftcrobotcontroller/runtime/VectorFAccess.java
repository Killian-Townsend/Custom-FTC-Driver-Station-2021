package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;

class VectorFAccess extends Access {
  VectorFAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "VectorF");
  }
  
  @JavascriptInterface
  public void add_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".add");
    paramObject1 = checkArg(paramObject1, VectorF.class, "vector1");
    paramObject2 = checkArg(paramObject2, VectorF.class, "vector2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.add((VectorF)paramObject2); 
  }
  
  @JavascriptInterface
  public MatrixF added_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".added");
    paramObject1 = checkVectorF(paramObject1);
    paramObject2 = checkMatrixF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.added((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public VectorF added_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".added");
    paramObject1 = checkArg(paramObject1, VectorF.class, "vector1");
    paramObject2 = checkArg(paramObject2, VectorF.class, "vector2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.added((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public VectorF create(int paramInt) {
    startBlockExecution(BlockType.CREATE, "");
    return VectorF.length(paramInt);
  }
  
  @JavascriptInterface
  public float dotProduct(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".dotProduct");
    paramObject1 = checkArg(paramObject1, VectorF.class, "vector1");
    paramObject2 = checkArg(paramObject2, VectorF.class, "vector2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.dotProduct((VectorF)paramObject2) : 0.0F;
  }
  
  @JavascriptInterface
  public float get(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".get");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.get(paramInt) : 0.0F;
  }
  
  @JavascriptInterface
  public int getLength(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Length");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.length() : 0;
  }
  
  @JavascriptInterface
  public float getMagnitude(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Magnitude");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.magnitude() : 0.0F;
  }
  
  @JavascriptInterface
  public MatrixF multiplied(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject1 = checkVectorF(paramObject1);
    paramObject2 = checkMatrixF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.multiplied((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public VectorF multiplied_withScale(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".multiplied");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.multiplied(paramFloat) : null;
  }
  
  @JavascriptInterface
  public void multiply_withScale(Object paramObject, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".multiply");
    paramObject = checkVectorF(paramObject);
    if (paramObject != null)
      paramObject.multiply(paramFloat); 
  }
  
  @JavascriptInterface
  public VectorF normalized3D(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".normalized3D");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.normalized3D() : null;
  }
  
  @JavascriptInterface
  public void put(Object paramObject, int paramInt, float paramFloat) {
    startBlockExecution(BlockType.FUNCTION, ".put");
    paramObject = checkVectorF(paramObject);
    if (paramObject != null)
      paramObject.put(paramInt, paramFloat); 
  }
  
  @JavascriptInterface
  public void subtract_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtract");
    paramObject1 = checkArg(paramObject1, VectorF.class, "vector1");
    paramObject2 = checkArg(paramObject2, VectorF.class, "vector2");
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.subtract((VectorF)paramObject2); 
  }
  
  @JavascriptInterface
  public MatrixF subtracted_withMatrix(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtracted");
    paramObject1 = checkVectorF(paramObject1);
    paramObject2 = checkMatrixF(paramObject2);
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.subtracted((MatrixF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public VectorF subtracted_withVector(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".subtracted");
    paramObject1 = checkArg(paramObject1, VectorF.class, "vector1");
    paramObject2 = checkArg(paramObject2, VectorF.class, "vector2");
    return (paramObject1 != null && paramObject2 != null) ? paramObject1.subtracted((VectorF)paramObject2) : null;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkVectorF(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VectorFAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */