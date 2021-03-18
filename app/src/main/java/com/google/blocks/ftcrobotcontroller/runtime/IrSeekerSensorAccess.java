package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtIrSeekerSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cIrSeekerSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;

class IrSeekerSensorAccess extends HardwareAccess<IrSeekerSensor> {
  private final IrSeekerSensor irSeekerSensor = this.hardwareDevice;
  
  IrSeekerSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, IrSeekerSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getAngle"})
  public double getAngle() {
    startBlockExecution(BlockType.GETTER, ".Angle");
    return this.irSeekerSensor.getAngle();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.irSeekerSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.irSeekerSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"signalDetected"})
  public boolean getIsSignalDetected() {
    startBlockExecution(BlockType.GETTER, ".IsSignalDetected");
    return this.irSeekerSensor.signalDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getMode"})
  public String getMode() {
    startBlockExecution(BlockType.GETTER, ".Mode");
    IrSeekerSensor.Mode mode = this.irSeekerSensor.getMode();
    return (mode != null) ? mode.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getSignalDetectedThreshold"})
  public double getSignalDetectedThreshold() {
    startBlockExecution(BlockType.GETTER, ".SignalDetectedThreshold");
    return this.irSeekerSensor.getSignalDetectedThreshold();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"getStrength"})
  public double getStrength() {
    startBlockExecution(BlockType.GETTER, ".Strength");
    return this.irSeekerSensor.getStrength();
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.irSeekerSensor.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.irSeekerSensor.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"setMode"})
  public void setMode(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Mode");
    IrSeekerSensor.Mode mode = (IrSeekerSensor.Mode)checkArg(paramString, IrSeekerSensor.Mode.class, "");
    if (mode != null)
      this.irSeekerSensor.setMode(mode); 
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtIrSeekerSensor.class, ModernRoboticsI2cIrSeekerSensorV3.class}, methodName = {"setSignalDetectedThreshold"})
  public void setSignalDetectedThreshold(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".SignalDetectedThreshold");
    this.irSeekerSensor.setSignalDetectedThreshold(paramDouble);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\IrSeekerSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */