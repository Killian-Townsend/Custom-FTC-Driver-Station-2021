package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;

class MagneticFluxAccess extends Access {
  MagneticFluxAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "MagneticFlux");
  }
  
  private MagneticFlux checkMagneticFlux(Object paramObject) {
    return checkArg(paramObject, MagneticFlux.class, "magneticFlux");
  }
  
  @JavascriptInterface
  public MagneticFlux create() {
    startBlockExecution(BlockType.CREATE, "");
    return new MagneticFlux();
  }
  
  @JavascriptInterface
  public MagneticFlux create_withArgs(double paramDouble1, double paramDouble2, double paramDouble3, long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    return new MagneticFlux(paramDouble1, paramDouble2, paramDouble3, paramLong);
  }
  
  @JavascriptInterface
  public long getAcquisitionTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AcquisitionTime");
    paramObject = checkMagneticFlux(paramObject);
    return (paramObject != null) ? ((MagneticFlux)paramObject).acquisitionTime : 0L;
  }
  
  @JavascriptInterface
  public double getX(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".X");
    paramObject = checkMagneticFlux(paramObject);
    return (paramObject != null) ? ((MagneticFlux)paramObject).x : 0.0D;
  }
  
  @JavascriptInterface
  public double getY(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Y");
    paramObject = checkMagneticFlux(paramObject);
    return (paramObject != null) ? ((MagneticFlux)paramObject).y : 0.0D;
  }
  
  @JavascriptInterface
  public double getZ(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Z");
    paramObject = checkMagneticFlux(paramObject);
    return (paramObject != null) ? ((MagneticFlux)paramObject).z : 0.0D;
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkMagneticFlux(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\MagneticFluxAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */