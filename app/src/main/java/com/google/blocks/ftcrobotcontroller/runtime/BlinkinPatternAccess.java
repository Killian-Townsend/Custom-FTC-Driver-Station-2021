package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;

class BlinkinPatternAccess extends Access {
  BlinkinPatternAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "BlinkinPattern");
  }
  
  @JavascriptInterface
  public String fromNumber(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".fromNumber");
    return RevBlinkinLedDriver.BlinkinPattern.fromNumber(paramInt).toString();
  }
  
  @JavascriptInterface
  public String fromText(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".fromText");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    return (blinkinPattern != null) ? blinkinPattern.toString() : "";
  }
  
  @JavascriptInterface
  public String next(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".next");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    return (blinkinPattern != null) ? blinkinPattern.next().toString() : "";
  }
  
  @JavascriptInterface
  public String previous(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".previous");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    return (blinkinPattern != null) ? blinkinPattern.previous().toString() : "";
  }
  
  @JavascriptInterface
  public int toNumber(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toNumber");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    return (blinkinPattern != null) ? blinkinPattern.ordinal() : 0;
  }
  
  @JavascriptInterface
  public String toText(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    return (blinkinPattern != null) ? blinkinPattern.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\BlinkinPatternAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */