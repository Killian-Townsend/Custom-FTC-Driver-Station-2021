package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

class ServoAccess extends HardwareAccess<Servo> {
  private final Servo servo = this.hardwareDevice;
  
  ServoAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, Servo.class);
  }
  
  @JavascriptInterface
  public String getDirection() {
    startBlockExecution(BlockType.GETTER, ".Direction");
    Servo.Direction direction = this.servo.getDirection();
    return (direction != null) ? direction.toString() : "";
  }
  
  @JavascriptInterface
  public double getPosition() {
    startBlockExecution(BlockType.GETTER, ".Position");
    return this.servo.getPosition();
  }
  
  @JavascriptInterface
  public void scaleRange(double paramDouble1, double paramDouble2) {
    startBlockExecution(BlockType.FUNCTION, ".scaleRange");
    this.servo.scaleRange(paramDouble1, paramDouble2);
  }
  
  @JavascriptInterface
  public void setDirection(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Direction");
    Servo.Direction direction = (Servo.Direction)checkArg(paramString, Servo.Direction.class, "");
    if (direction != null)
      this.servo.setDirection(direction); 
  }
  
  @JavascriptInterface
  public void setPosition(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".Position");
    this.servo.setPosition(paramDouble);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ServoAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */