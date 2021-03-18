package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

class QuaternionAccess extends Access {
  QuaternionAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Quaternion");
  }
  
  private Quaternion checkQuaternion(Object paramObject) {
    return checkArg(paramObject, Quaternion.class, "quaternion");
  }
  
  @JavascriptInterface
  public Quaternion congugate(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".congugate");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? paramObject.congugate() : null;
  }
  
  @JavascriptInterface
  public Quaternion create() {
    startBlockExecution(BlockType.CREATE, "");
    return new Quaternion();
  }
  
  @JavascriptInterface
  public Quaternion create_withArgs(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    return new Quaternion(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramLong);
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? ((Quaternion)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public float getMagnitude(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Magnitude");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? paramObject.magnitude() : 0.0F;
  }
  
  @JavascriptInterface
  public float getW(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".W");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? ((Quaternion)paramObject).w : 0.0F;
  }
  
  @JavascriptInterface
  public float getX(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".X");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? ((Quaternion)paramObject).x : 0.0F;
  }
  
  @JavascriptInterface
  public float getY(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Y");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? ((Quaternion)paramObject).y : 0.0F;
  }
  
  @JavascriptInterface
  public float getZ(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Z");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? ((Quaternion)paramObject).z : 0.0F;
  }
  
  @JavascriptInterface
  public Quaternion normalized(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".normalized");
    paramObject = checkQuaternion(paramObject);
    return (paramObject != null) ? paramObject.normalized() : null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\QuaternionAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */