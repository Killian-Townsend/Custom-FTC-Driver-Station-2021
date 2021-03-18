package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

class MrI2cRangeSensorAccess extends HardwareAccess<ModernRoboticsI2cRangeSensor> {
  private final ModernRoboticsI2cRangeSensor mrI2cRangeSensor = this.hardwareDevice;
  
  MrI2cRangeSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, ModernRoboticsI2cRangeSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"cmOptical"})
  public double getCmOptical() {
    startBlockExecution(BlockType.GETTER, ".CmOptical");
    return this.mrI2cRangeSensor.cmOptical();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"cmUltrasonic"})
  public double getCmUltrasonic() {
    startBlockExecution(BlockType.GETTER, ".CmUltrasonic");
    return this.mrI2cRangeSensor.cmUltrasonic();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getDistance"})
  public double getDistance(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getDistance");
    DistanceUnit distanceUnit = (DistanceUnit)checkArg(paramString, DistanceUnit.class, "unit");
    return (distanceUnit != null) ? this.mrI2cRangeSensor.getDistance(distanceUnit) : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.mrI2cRangeSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.mrI2cRangeSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getLightDetected"})
  public double getLightDetected() {
    startBlockExecution(BlockType.GETTER, ".LightDetected");
    return this.mrI2cRangeSensor.getLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getRawLightDetected"})
  public double getRawLightDetected() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetected");
    return this.mrI2cRangeSensor.getRawLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"getRawLightDetectedMax"})
  public double getRawLightDetectedMax() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetectedMax");
    return this.mrI2cRangeSensor.getRawLightDetectedMax();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"rawOptical"})
  public double getRawOptical() {
    startBlockExecution(BlockType.GETTER, ".RawOptical");
    return this.mrI2cRangeSensor.rawOptical();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"rawUltrasonic"})
  public double getRawUltrasonic() {
    startBlockExecution(BlockType.GETTER, ".RawUltrasonic");
    return this.mrI2cRangeSensor.rawUltrasonic();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.mrI2cRangeSensor.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cRangeSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.mrI2cRangeSensor.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\MrI2cRangeSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */