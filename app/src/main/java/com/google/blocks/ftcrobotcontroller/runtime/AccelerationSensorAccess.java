package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtAccelerationSensor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;

class AccelerationSensorAccess extends HardwareAccess<AccelerationSensor> {
  private final AccelerationSensor accelerationSensor = this.hardwareDevice;
  
  AccelerationSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, AccelerationSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtAccelerationSensor.class}, methodName = {"getAcceleration"})
  public Acceleration getAcceleration() {
    startBlockExecution(BlockType.GETTER, ".Acceleration");
    return this.accelerationSensor.getAcceleration();
  }
  
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getXAccel() {
    startBlockExecution(BlockType.GETTER, ".XAccel");
    Acceleration acceleration = this.accelerationSensor.getAcceleration();
    return (acceleration != null) ? acceleration.xAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getYAccel() {
    startBlockExecution(BlockType.GETTER, ".YAccel");
    Acceleration acceleration = this.accelerationSensor.getAcceleration();
    return (acceleration != null) ? acceleration.yAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(exclusiveToBlocks = true)
  public double getZAccel() {
    startBlockExecution(BlockType.GETTER, ".ZAccel");
    Acceleration acceleration = this.accelerationSensor.getAcceleration();
    return (acceleration != null) ? acceleration.zAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtAccelerationSensor.class}, methodName = {"toString"})
  public String toText() {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    return this.accelerationSensor.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AccelerationSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */