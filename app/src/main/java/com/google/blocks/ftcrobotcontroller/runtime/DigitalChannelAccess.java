package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

class DigitalChannelAccess extends HardwareAccess<DigitalChannel> {
  private final DigitalChannel digitalChannel = this.hardwareDevice;
  
  DigitalChannelAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, DigitalChannel.class);
  }
  
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = {"getMode"})
  public String getMode() {
    startBlockExecution(BlockType.GETTER, ".Mode");
    DigitalChannel.Mode mode = this.digitalChannel.getMode();
    return (mode != null) ? mode.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = {"getState"})
  public boolean getState() {
    startBlockExecution(BlockType.GETTER, ".State");
    return this.digitalChannel.getState();
  }
  
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = {"setMode"})
  public void setMode(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Mode");
    DigitalChannel.Mode mode = (DigitalChannel.Mode)checkArg(paramString, DigitalChannel.Mode.class, "");
    if (mode != null)
      this.digitalChannel.setMode(mode); 
  }
  
  @JavascriptInterface
  @Block(classes = {DigitalChannel.class}, methodName = {"setState"})
  public void setState(boolean paramBoolean) {
    startBlockExecution(BlockType.SETTER, ".State");
    this.digitalChannel.setState(paramBoolean);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\DigitalChannelAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */