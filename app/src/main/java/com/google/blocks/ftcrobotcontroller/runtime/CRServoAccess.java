package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

class CRServoAccess extends HardwareAccess<CRServo> {
  private final CRServo crServo = this.hardwareDevice;
  
  CRServoAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramHardwareItem, paramHardwareMap, CRServo.class);
  }
  
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = {"getDirection"})
  public String getDirection() {
    startBlockExecution(BlockType.GETTER, ".Direction");
    DcMotorSimple.Direction direction = this.crServo.getDirection();
    return (direction != null) ? direction.toString() : "";
  }
  
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = {"getPower"})
  public double getPower() {
    startBlockExecution(BlockType.GETTER, ".Power");
    return this.crServo.getPower();
  }
  
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = {"setDirection"})
  public void setDirection(String paramString) {
    startBlockExecution(BlockType.SETTER, ".Direction");
    DcMotorSimple.Direction direction = (DcMotorSimple.Direction)checkArg(paramString, DcMotorSimple.Direction.class, "");
    if (direction != null)
      this.crServo.setDirection(direction); 
  }
  
  @JavascriptInterface
  @Block(classes = {CRServoImpl.class}, methodName = {"setPower"})
  public void setPower(double paramDouble) {
    startBlockExecution(BlockType.SETTER, ".Power");
    this.crServo.setPower(paramDouble);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\CRServoAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */