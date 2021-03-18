package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

class OpticalDistanceSensorAccess extends HardwareAccess<OpticalDistanceSensor> {
  private final OpticalDistanceSensor opticalDistanceSensor = this.hardwareDevice;
  
  OpticalDistanceSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, OpticalDistanceSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = {"enableLed"})
  public void enableLed(boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".enableLed");
    this.opticalDistanceSensor.enableLed(paramBoolean);
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = {"getLightDetected"})
  public double getLightDetected() {
    startBlockExecution(BlockType.GETTER, ".LightDetected");
    return this.opticalDistanceSensor.getLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = {"getRawLightDetected"})
  public double getRawLightDetected() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetected");
    return this.opticalDistanceSensor.getRawLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsAnalogOpticalDistanceSensor.class}, methodName = {"getRawLightDetectedMax"})
  public double getRawLightDetectedMax() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetectedMax");
    return this.opticalDistanceSensor.getRawLightDetectedMax();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\OpticalDistanceSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */