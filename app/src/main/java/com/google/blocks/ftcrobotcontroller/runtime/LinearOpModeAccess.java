package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;

class LinearOpModeAccess extends Access {
  private final BlocksOpMode blocksOpMode;
  
  LinearOpModeAccess(BlocksOpMode paramBlocksOpMode, String paramString1, String paramString2) {
    super(paramBlocksOpMode, paramString1, paramString2);
    this.blocksOpMode = paramBlocksOpMode;
  }
  
  @JavascriptInterface
  public double getRuntime() {
    startBlockExecution(BlockType.FUNCTION, ".getRuntime");
    return this.blocksOpMode.getRuntime();
  }
  
  @JavascriptInterface
  public void idle() {
    startBlockExecution(BlockType.FUNCTION, ".idle");
    this.blocksOpMode.idle();
  }
  
  @JavascriptInterface
  public boolean isStarted() {
    startBlockExecution(BlockType.FUNCTION, ".isStarted");
    return this.blocksOpMode.isStartedForBlocks();
  }
  
  @JavascriptInterface
  public boolean isStopRequested() {
    startBlockExecution(BlockType.FUNCTION, ".isStopRequested");
    return this.blocksOpMode.isStopRequestedForBlocks();
  }
  
  @JavascriptInterface
  public boolean opModeIsActive() {
    startBlockExecution(BlockType.FUNCTION, ".opModeIsActive");
    return this.blocksOpMode.opModeIsActive();
  }
  
  @JavascriptInterface
  public void sleep(double paramDouble) {
    startBlockExecution(BlockType.FUNCTION, ".sleep");
    this.blocksOpMode.sleepForBlocks((long)paramDouble);
  }
  
  @JavascriptInterface
  public void waitForStart() {
    startBlockExecution(BlockType.FUNCTION, ".waitForStart");
    this.blocksOpMode.waitForStartForBlocks();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\LinearOpModeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */