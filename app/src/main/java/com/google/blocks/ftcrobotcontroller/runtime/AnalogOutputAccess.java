package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.HardwareMap;

class AnalogOutputAccess extends HardwareAccess<AnalogOutput> {
  private final AnalogOutput analogOutput = this.hardwareDevice;
  
  AnalogOutputAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, AnalogOutput.class);
  }
  
  @JavascriptInterface
  @Block(classes = {AnalogOutput.class}, methodName = {"setAnalogOutputFrequency"})
  public void setAnalogOutputFrequency(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".setAnalogOutputFrequency");
    this.analogOutput.setAnalogOutputFrequency(paramInt);
  }
  
  @JavascriptInterface
  @Block(classes = {AnalogOutput.class}, methodName = {"setAnalogOutputMode"})
  public void setAnalogOutputMode(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".setAnalogOutputMode");
    this.analogOutput.setAnalogOutputMode((byte)paramInt);
  }
  
  @JavascriptInterface
  @Block(classes = {AnalogOutput.class}, methodName = {"setAnalogOutputVoltage"})
  public void setAnalogOutputVoltage(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".setAnalogOutputVoltage");
    this.analogOutput.setAnalogOutputVoltage(paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AnalogOutputAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */