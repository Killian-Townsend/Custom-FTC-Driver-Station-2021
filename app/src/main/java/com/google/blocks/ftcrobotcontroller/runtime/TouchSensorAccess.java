package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

class TouchSensorAccess extends HardwareAccess<TouchSensor> {
  private final TouchSensor touchSensor = this.hardwareDevice;
  
  TouchSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, TouchSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtTouchSensor.class, ModernRoboticsTouchSensor.class, RevTouchSensor.class}, methodName = {"isPressed"})
  public boolean getIsPressed() {
    startBlockExecution(BlockType.GETTER, ".IsPressed");
    return this.touchSensor.isPressed();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TouchSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */