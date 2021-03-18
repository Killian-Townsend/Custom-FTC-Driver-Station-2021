package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

class UltrasonicSensorAccess extends HardwareAccess<UltrasonicSensor> {
  private final UltrasonicSensor ultrasonicSensor = this.hardwareDevice;
  
  UltrasonicSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, UltrasonicSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtUltrasonicSensor.class}, methodName = {"getUltrasonicLevel"})
  public double getUltrasonicLevel() {
    startBlockExecution(BlockType.GETTER, ".UltrasonicLevel");
    return this.ultrasonicSensor.getUltrasonicLevel();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\UltrasonicSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */