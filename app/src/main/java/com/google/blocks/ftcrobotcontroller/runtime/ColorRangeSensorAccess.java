package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

class ColorRangeSensorAccess extends HardwareAccess<ColorRangeSensor> {
  private final ColorRangeSensor colorRangeSensor = this.hardwareDevice;
  
  ColorRangeSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, ColorRangeSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"alpha"})
  public int getAlpha() {
    startBlockExecution(BlockType.GETTER, ".Alpha");
    return this.colorRangeSensor.alpha();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"argb"})
  public int getArgb() {
    startBlockExecution(BlockType.GETTER, ".Argb");
    return this.colorRangeSensor.argb();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"blue"})
  public int getBlue() {
    startBlockExecution(BlockType.GETTER, ".Blue");
    return this.colorRangeSensor.blue();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getDistance"})
  public double getDistance(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".getDistance");
    DistanceUnit distanceUnit = (DistanceUnit)checkArg(paramString, DistanceUnit.class, "unit");
    return (distanceUnit != null) ? this.colorRangeSensor.getDistance(distanceUnit) : 0.0D;
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getGain"})
  public float getGain() {
    startBlockExecution(BlockType.GETTER, ".Gain");
    return this.colorRangeSensor.getGain();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"green"})
  public int getGreen() {
    startBlockExecution(BlockType.GETTER, ".Green");
    return this.colorRangeSensor.green();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.colorRangeSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.colorRangeSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getLightDetected"})
  public double getLightDetected() {
    startBlockExecution(BlockType.GETTER, ".LightDetected");
    return this.colorRangeSensor.getLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getNormalizedColors"})
  public String getNormalizedColors() {
    startBlockExecution(BlockType.FUNCTION, ".getNormalizedColors");
    NormalizedRGBA normalizedRGBA = this.colorRangeSensor.getNormalizedColors();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ \"Red\":");
    stringBuilder.append(normalizedRGBA.red);
    stringBuilder.append(", \"Green\":");
    stringBuilder.append(normalizedRGBA.green);
    stringBuilder.append(", \"Blue\":");
    stringBuilder.append(normalizedRGBA.blue);
    stringBuilder.append(", \"Alpha\":");
    stringBuilder.append(normalizedRGBA.alpha);
    stringBuilder.append(", \"Color\":");
    stringBuilder.append(normalizedRGBA.toColor());
    stringBuilder.append(" }");
    return stringBuilder.toString();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getRawLightDetected"})
  public double getRawLightDetected() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetected");
    return this.colorRangeSensor.getRawLightDetected();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"getRawLightDetectedMax"})
  public double getRawLightDetectedMax() {
    startBlockExecution(BlockType.GETTER, ".RawLightDetectedMax");
    return this.colorRangeSensor.getRawLightDetectedMax();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"red"})
  public int getRed() {
    startBlockExecution(BlockType.GETTER, ".Red");
    return this.colorRangeSensor.red();
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"setGain"})
  public void setGain(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".Gain");
    this.colorRangeSensor.setGain(paramFloat);
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.colorRangeSensor.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {LynxI2cColorRangeSensor.class, RevColorSensorV3.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.colorRangeSensor.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ColorRangeSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */