package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

class RevBlinkinLedDriverAccess extends HardwareAccess<RevBlinkinLedDriver> {
  private final RevBlinkinLedDriver revBlinkinLedDriver = this.hardwareDevice;
  
  RevBlinkinLedDriverAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, RevBlinkinLedDriver.class);
  }
  
  @JavascriptInterface
  public void setPattern(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Pattern");
    RevBlinkinLedDriver.BlinkinPattern blinkinPattern = checkBlinkinPattern(paramString);
    if (blinkinPattern != null)
      this.revBlinkinLedDriver.setPattern(blinkinPattern); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\RevBlinkinLedDriverAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */