package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.Range;

class RangeAccess extends Access {
  RangeAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "Range");
  }
  
  @JavascriptInterface
  public double clip(double paramDouble1, double paramDouble2, double paramDouble3) {
    startBlockExecution(BlockType.FUNCTION, ".clip");
    return Range.clip(paramDouble1, paramDouble2, paramDouble3);
  }
  
  @JavascriptInterface
  public double scale(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5) {
    startBlockExecution(BlockType.FUNCTION, ".scale");
    return Range.scale(paramDouble1, paramDouble2, paramDouble3, paramDouble4, paramDouble5);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\RangeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */