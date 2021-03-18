package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LED;

class LedAccess extends HardwareAccess<LED> {
  private final LED led = this.hardwareDevice;
  
  LedAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, LED.class);
  }
  
  @JavascriptInterface
  @Block(classes = {LED.class}, methodName = {"enable"})
  public void enableLed(boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".enableLed");
    this.led.enable(paramBoolean);
  }
  
  @JavascriptInterface
  @Block(classes = {LED.class}, methodName = {"isLightOn"})
  public boolean isLightOn() {
    startBlockExecution(BlockType.FUNCTION, ".isLightOn");
    return this.led.isLightOn();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\LedAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */