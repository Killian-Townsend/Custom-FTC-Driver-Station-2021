package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

class AnalogInputAccess extends HardwareAccess<AnalogInput> {
  private final AnalogInput analogInput = this.hardwareDevice;
  
  AnalogInputAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, AnalogInput.class);
  }
  
  @JavascriptInterface
  @Block(classes = {AnalogInput.class}, methodName = {"getMaxVoltage"})
  public double getMaxVoltage() {
    startBlockExecution(BlockType.GETTER, ".MaxVoltage");
    return this.analogInput.getMaxVoltage();
  }
  
  @JavascriptInterface
  @Block(classes = {AnalogInput.class}, methodName = {"getVoltage"})
  public double getVoltage() {
    startBlockExecution(BlockType.GETTER, ".Voltage");
    return this.analogInput.getVoltage();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AnalogInputAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */