package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

class PIDFCoefficientsAccess extends Access {
  PIDFCoefficientsAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "PIDFCoefficients");
  }
  
  private PIDFCoefficients checkPIDFCoefficients(Object paramObject) {
    return checkArg(paramObject, PIDFCoefficients.class, "pidfCoefficients");
  }
  
  @JavascriptInterface
  public PIDFCoefficients create() {
    startBlockExecution(BlockType.CREATE, "");
    return new PIDFCoefficients();
  }
  
  @JavascriptInterface
  public PIDFCoefficients create_withPIDF(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4) {
    startBlockExecution(BlockType.CREATE, "");
    return new PIDFCoefficients(paramDouble1, paramDouble2, paramDouble3, paramDouble4);
  }
  
  @JavascriptInterface
  public PIDFCoefficients create_withPIDFAlgorithm(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, String paramString) {
    startBlockExecution(BlockType.CREATE, "");
    MotorControlAlgorithm motorControlAlgorithm = checkArg(paramString, MotorControlAlgorithm.class, "algorithm");
    return (motorControlAlgorithm != null) ? new PIDFCoefficients(paramDouble1, paramDouble2, paramDouble3, paramDouble4, motorControlAlgorithm) : null;
  }
  
  @JavascriptInterface
  public PIDFCoefficients create_withPIDFCoefficients(Object paramObject) {
    startBlockExecution(BlockType.CREATE, "");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? new PIDFCoefficients((PIDFCoefficients)paramObject) : null;
  }
  
  @JavascriptInterface
  public String getAlgorithm(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Algorithm");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null && ((PIDFCoefficients)paramObject).algorithm != null) ? ((PIDFCoefficients)paramObject).algorithm.toString() : "";
  }
  
  @JavascriptInterface
  public double getD(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".D");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? ((PIDFCoefficients)paramObject).d : 0.0D;
  }
  
  @JavascriptInterface
  public double getF(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".F");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? ((PIDFCoefficients)paramObject).f : 0.0D;
  }
  
  @JavascriptInterface
  public double getI(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".I");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? ((PIDFCoefficients)paramObject).i : 0.0D;
  }
  
  @JavascriptInterface
  public double getP(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".P");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? ((PIDFCoefficients)paramObject).p : 0.0D;
  }
  
  @JavascriptInterface
  public void setAlgorithm(Object paramObject, String paramString) {
    startBlockExecution(BlockType.SETTER, ".Algorithm");
    paramObject = checkPIDFCoefficients(paramObject);
    MotorControlAlgorithm motorControlAlgorithm = checkArg(paramString, MotorControlAlgorithm.class, "");
    if (paramObject != null && motorControlAlgorithm != null)
      ((PIDFCoefficients)paramObject).algorithm = motorControlAlgorithm; 
  }
  
  @JavascriptInterface
  public void setD(Object paramObject, double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".D");
    paramObject = checkPIDFCoefficients(paramObject);
    if (paramObject != null)
      ((PIDFCoefficients)paramObject).d = paramDouble; 
  }
  
  @JavascriptInterface
  public void setF(Object paramObject, double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".F");
    paramObject = checkPIDFCoefficients(paramObject);
    if (paramObject != null)
      ((PIDFCoefficients)paramObject).f = paramDouble; 
  }
  
  @JavascriptInterface
  public void setI(Object paramObject, double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".I");
    paramObject = checkPIDFCoefficients(paramObject);
    if (paramObject != null)
      ((PIDFCoefficients)paramObject).i = paramDouble; 
  }
  
  @JavascriptInterface
  public void setP(Object paramObject, double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".P");
    paramObject = checkPIDFCoefficients(paramObject);
    if (paramObject != null)
      ((PIDFCoefficients)paramObject).p = paramDouble; 
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkPIDFCoefficients(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\PIDFCoefficientsAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */