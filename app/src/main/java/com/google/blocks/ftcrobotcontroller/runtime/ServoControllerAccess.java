package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

class ServoControllerAccess extends HardwareAccess<ServoController> {
  private final ServoController servoController = this.hardwareDevice;
  
  ServoControllerAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, ServoController.class);
  }
  
  @JavascriptInterface
  public String getPwmStatus() {
    startBlockExecution(BlockType.GETTER, ".PwmStatus");
    ServoController.PwmStatus pwmStatus = this.servoController.getPwmStatus();
    return (pwmStatus != null) ? pwmStatus.toString() : "";
  }
  
  @JavascriptInterface
  public void pwmDisable() {
    startBlockExecution(BlockType.FUNCTION, ".pwmDisable");
    this.servoController.pwmDisable();
  }
  
  @JavascriptInterface
  public void pwmEnable() {
    startBlockExecution(BlockType.FUNCTION, ".pwmEnable");
    this.servoController.pwmEnable();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ServoControllerAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */