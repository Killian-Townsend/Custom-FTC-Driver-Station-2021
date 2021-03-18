package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.android.AndroidOrientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

class AndroidOrientationAccess extends Access {
  private final AndroidOrientation androidOrientation = new AndroidOrientation();
  
  AndroidOrientationAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AndroidOrientation");
  }
  
  void close() {
    this.androidOrientation.stopListening();
  }
  
  @JavascriptInterface
  public double getAngle() {
    startBlockExecution(BlockType.GETTER, ".Angle");
    return this.androidOrientation.getAngle();
  }
  
  @JavascriptInterface
  public String getAngleUnit() {
    startBlockExecution(BlockType.GETTER, ".AngleUnit");
    return this.androidOrientation.getAngleUnit().toString();
  }
  
  @JavascriptInterface
  public double getAzimuth() {
    startBlockExecution(BlockType.GETTER, ".Azimuth");
    return this.androidOrientation.getAzimuth();
  }
  
  @JavascriptInterface
  public double getMagnitude() {
    startBlockExecution(BlockType.GETTER, ".Magnitude");
    return this.androidOrientation.getMagnitude();
  }
  
  @JavascriptInterface
  public double getPitch() {
    startBlockExecution(BlockType.GETTER, ".Pitch");
    return this.androidOrientation.getPitch();
  }
  
  @JavascriptInterface
  public double getRoll() {
    startBlockExecution(BlockType.GETTER, ".Roll");
    return this.androidOrientation.getRoll();
  }
  
  @JavascriptInterface
  public boolean isAvailable() {
    startBlockExecution(BlockType.FUNCTION, ".isAvailable");
    return this.androidOrientation.isAvailable();
  }
  
  @JavascriptInterface
  public void setAngleUnit(String paramString) {
    startBlockExecution(BlockType.SETTER, ".AngleUnit");
    AngleUnit angleUnit = checkArg(paramString, AngleUnit.class, "");
    if (angleUnit != null)
      this.androidOrientation.setAngleUnit(angleUnit); 
  }
  
  @JavascriptInterface
  public void startListening() {
    startBlockExecution(BlockType.FUNCTION, ".startListening");
    this.androidOrientation.startListening();
  }
  
  @JavascriptInterface
  public void stopListening() {
    startBlockExecution(BlockType.FUNCTION, ".stopListening");
    this.androidOrientation.stopListening();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AndroidOrientationAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */