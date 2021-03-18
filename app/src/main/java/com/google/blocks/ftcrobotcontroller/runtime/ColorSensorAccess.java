package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.Light;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

class ColorSensorAccess extends HardwareAccess<ColorSensor> {
  private final ColorSensor colorSensor = this.hardwareDevice;
  
  ColorSensorAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, ColorSensor.class);
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"enableLed"})
  public void enableLed(boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".enableLed");
    this.colorSensor.enableLed(paramBoolean);
  }
  
  @JavascriptInterface
  @Block(classes = {HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"enableLight"})
  public void enableLight(boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".enableLight");
    ColorSensor colorSensor = this.colorSensor;
    if (colorSensor instanceof SwitchableLight) {
      ((SwitchableLight)colorSensor).enableLight(paramBoolean);
      return;
    } 
    reportWarning("This ColorSensor is not a SwitchableLight.");
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"alpha"})
  public int getAlpha() {
    startBlockExecution(BlockType.GETTER, ".Alpha");
    return this.colorSensor.alpha();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"argb"})
  public int getArgb() {
    startBlockExecution(BlockType.GETTER, ".Argb");
    return this.colorSensor.argb();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"blue"})
  public int getBlue() {
    startBlockExecution(BlockType.GETTER, ".Blue");
    return this.colorSensor.blue();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"getGain"})
  public float getGain() {
    startBlockExecution(BlockType.GETTER, ".Gain");
    ColorSensor colorSensor = this.colorSensor;
    return (colorSensor instanceof NormalizedColorSensor) ? ((NormalizedColorSensor)colorSensor).getGain() : 0.0F;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"green"})
  public int getGreen() {
    startBlockExecution(BlockType.GETTER, ".Green");
    return this.colorSensor.green();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress7Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress7Bit");
    I2cAddr i2cAddr = this.colorSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get7Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"getI2cAddress"})
  public int getI2cAddress8Bit() {
    startBlockExecution(BlockType.GETTER, ".I2cAddress8Bit");
    I2cAddr i2cAddr = this.colorSensor.getI2cAddress();
    return (i2cAddr != null) ? i2cAddr.get8Bit() : 0;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"getNormalizedColors"})
  public String getNormalizedColors() {
    startBlockExecution(BlockType.FUNCTION, ".getNormalizedColors");
    ColorSensor colorSensor = this.colorSensor;
    if (colorSensor instanceof NormalizedColorSensor) {
      NormalizedRGBA normalizedRGBA = ((NormalizedColorSensor)colorSensor).getNormalizedColors();
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
    return "{ \"Red\":0, \"Green\":0, \"Blue\":0, \"Alpha\":0, \"Color\":0 }";
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"red"})
  public int getRed() {
    startBlockExecution(BlockType.GETTER, ".Red");
    return this.colorSensor.red();
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"isLightOn"})
  public boolean isLightOn() {
    startBlockExecution(BlockType.FUNCTION, ".isLightOn");
    ColorSensor colorSensor = this.colorSensor;
    if (colorSensor instanceof Light)
      return ((Light)colorSensor).isLightOn(); 
    reportWarning("This ColorSensor is not a Light.");
    return false;
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"setGain"})
  public void setGain(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".Gain");
    ColorSensor colorSensor = this.colorSensor;
    if (colorSensor instanceof NormalizedColorSensor)
      ((NormalizedColorSensor)colorSensor).setGain(paramFloat); 
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress7Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress7Bit");
    this.colorSensor.setI2cAddress(I2cAddr.create7bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"setI2cAddress"})
  public void setI2cAddress8Bit(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".I2cAddress8Bit");
    this.colorSensor.setI2cAddress(I2cAddr.create8bit(paramInt));
  }
  
  @JavascriptInterface
  @Block(classes = {AdafruitI2cColorSensor.class, HiTechnicNxtColorSensor.class, ModernRoboticsI2cColorSensor.class}, methodName = {"toString"})
  public String toText() {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    return this.colorSensor.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ColorSensorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */