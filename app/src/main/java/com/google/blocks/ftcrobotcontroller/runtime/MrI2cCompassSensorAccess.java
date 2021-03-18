package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;

class MrI2cCompassSensorAccess extends HardwareAccess<ModernRoboticsI2cCompassSensor> {
  private final ModernRoboticsI2cCompassSensor mrI2cCompassSensor = this.hardwareDevice;
  
  MrI2cCompassSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, ModernRoboticsI2cCompassSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"calibrationFailed"})
  public boolean calibrationFailed() {
    startBlockExecution(BlockType.FUNCTION, ".calibrationFailed");
    return this.mrI2cCompassSensor.calibrationFailed();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getDirection"})
  public double getDirection() {
    startBlockExecution(BlockType.GETTER, ".Direction");
    return this.mrI2cCompassSensor.getDirection();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.mrI2cCompassSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.mrI2cCompassSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getAcceleration"})
  public double getXAccel() {
    startBlockExecution(BlockType.GETTER, ".XAccel");
    Acceleration acceleration = this.mrI2cCompassSensor.getAcceleration();
    return (acceleration != null) ? acceleration.xAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getMagneticFlux"})
  public double getXMagneticFlux() {
    startBlockExecution(BlockType.GETTER, ".XMagneticFlux");
    MagneticFlux magneticFlux = this.mrI2cCompassSensor.getMagneticFlux();
    return (magneticFlux != null) ? magneticFlux.x : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getAcceleration"})
  public double getYAccel() {
    startBlockExecution(BlockType.GETTER, ".YAccel");
    Acceleration acceleration = this.mrI2cCompassSensor.getAcceleration();
    return (acceleration != null) ? acceleration.yAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getMagneticFlux"})
  public double getYMagneticFlux() {
    startBlockExecution(BlockType.GETTER, ".YMagneticFlux");
    MagneticFlux magneticFlux = this.mrI2cCompassSensor.getMagneticFlux();
    return (magneticFlux != null) ? magneticFlux.y : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getAcceleration"})
  public double getZAccel() {
    startBlockExecution(BlockType.GETTER, ".ZAccel");
    Acceleration acceleration = this.mrI2cCompassSensor.getAcceleration();
    return (acceleration != null) ? acceleration.zAccel : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"getMagneticFlux"})
  public double getZMagneticFlux() {
    startBlockExecution(BlockType.GETTER, ".ZMagneticFlux");
    MagneticFlux magneticFlux = this.mrI2cCompassSensor.getMagneticFlux();
    return (magneticFlux != null) ? magneticFlux.z : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"isCalibrating"})
  public boolean isCalibrating() {
    startBlockExecution(BlockType.FUNCTION, ".isCalibrating");
    return this.mrI2cCompassSensor.isCalibrating();
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.mrI2cCompassSensor.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.mrI2cCompassSensor.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {ModernRoboticsI2cCompassSensor.class}, methodName = {"setMode"})
  public void setMode(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setMode");
    CompassSensor.CompassMode compassMode = (CompassSensor.CompassMode)checkArg(paramString, CompassSensor.CompassMode.class, "compassMode");
    if (compassMode != null)
      this.mrI2cCompassSensor.setMode(compassMode); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\MrI2cCompassSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */