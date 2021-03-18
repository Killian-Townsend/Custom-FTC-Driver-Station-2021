package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

class NavigationAccess extends Access {
  NavigationAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "");
  }
  
  @JavascriptInterface
  public double angleUnit_convert(double paramDouble, String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, "AngleUnit", ".convert");
    AngleUnit angleUnit1 = checkArg(paramString1, AngleUnit.class, "from");
    AngleUnit angleUnit2 = checkArg(paramString2, AngleUnit.class, "to");
    return (angleUnit1 != null && angleUnit2 != null) ? angleUnit2.fromUnit(angleUnit1, paramDouble) : 0.0D;
  }
  
  @JavascriptInterface
  public double angleUnit_normalize(double paramDouble, String paramString) {
    startBlockExecution(BlockType.FUNCTION, "AngleUnit", ".normalize");
    AngleUnit angleUnit = checkAngleUnit(paramString);
    return (angleUnit != null) ? angleUnit.normalize(paramDouble) : 0.0D;
  }
  
  @JavascriptInterface
  public double unnormalizedAngleUnit_convert(double paramDouble, String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, "UnnormalizedAngleUnit", ".convert");
    AngleUnit angleUnit1 = checkArg(paramString1, AngleUnit.class, "from");
    AngleUnit angleUnit2 = checkArg(paramString2, AngleUnit.class, "to");
    return (angleUnit1 != null && angleUnit2 != null) ? angleUnit2.getUnnormalized().fromUnit(angleUnit1.getUnnormalized(), paramDouble) : 0.0D;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\NavigationAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */