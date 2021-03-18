package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtCompassSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

class CompassSensorAccess extends HardwareAccess<CompassSensor> {
  private final CompassSensor compassSensor = this.hardwareDevice;
  
  CompassSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, CompassSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = {"calibrationFailed"})
  public boolean getCalibrationFailed() {
    startBlockExecution(BlockType.GETTER, ".CalibrationFailed");
    return this.compassSensor.calibrationFailed();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = {"getDirection"})
  public double getDirection() {
    startBlockExecution(BlockType.GETTER, ".Direction");
    return this.compassSensor.getDirection();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtCompassSensor.class}, methodName = {"setMode"})
  public void setMode(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".Mode");
    CompassSensor.CompassMode compassMode = (CompassSensor.CompassMode)checkArg(paramString, CompassSensor.CompassMode.class, "compassMode");
    if (compassMode != null)
      this.compassSensor.setMode(compassMode); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\CompassSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */