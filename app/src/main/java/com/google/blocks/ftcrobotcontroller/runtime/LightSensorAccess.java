package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

class LightSensorAccess extends HardwareAccess<LightSensor> {
  private final LightSensor lightSensor = this.hardwareDevice;
  
  LightSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, LightSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = {"enableLed"})
  public void enableLed(boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".enableLed");
    this.lightSensor.enableLed(paramBoolean);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = {"getLightDetected"})
  public double getLightDetected() {
    startBlockExecution(BlockType.GETTER, ".LightDetected");
    return this.lightSensor.getLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = {"getRawLightDetected"})
  public double getRawLightDetected() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetected");
    return this.lightSensor.getRawLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtLightSensor.class}, methodName = {"getRawLightDetectedMax"})
  public double getRawLightDetectedMax() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetectedMax");
    return this.lightSensor.getRawLightDetectedMax();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\LightSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */