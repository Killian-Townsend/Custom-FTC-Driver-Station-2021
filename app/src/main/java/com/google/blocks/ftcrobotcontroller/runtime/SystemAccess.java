package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;

class SystemAccess extends Access {
  SystemAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "System");
  }
  
  @JavascriptInterface
  public long nanoTime() {
    startBlockExecution(BlockType.FUNCTION, ".nanoTime");
    return System.nanoTime();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\SystemAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */