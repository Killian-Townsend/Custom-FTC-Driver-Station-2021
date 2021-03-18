package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.android.AndroidAccelerometer;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

class AndroidAccelerometerAccess extends Access {
  private final AndroidAccelerometer androidAccelerometer = new AndroidAccelerometer();
  
  AndroidAccelerometerAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AndroidAccelerometer");
  }
  
  void close() {
    this.androidAccelerometer.stopListening();
  }
  
  @JavascriptInterface
  public Acceleration getAcceleration() {
    startBlockExecution(BlockType.GETTER, ".Acceleration");
    return this.androidAccelerometer.getAcceleration();
  }
  
  @JavascriptInterface
  public String getDistanceUnit() {
    startBlockExecution(BlockType.GETTER, ".DistanceUnit");
    return this.androidAccelerometer.getDistanceUnit().toString();
  }
  
  @JavascriptInterface
  public double getX() {
    startBlockExecution(BlockType.GETTER, ".X");
    return this.androidAccelerometer.getX();
  }
  
  @JavascriptInterface
  public double getY() {
    startBlockExecution(BlockType.GETTER, ".Y");
    return this.androidAccelerometer.getY();
  }
  
  @JavascriptInterface
  public double getZ() {
    startBlockExecution(BlockType.GETTER, ".Z");
    return this.androidAccelerometer.getZ();
  }
  
  @JavascriptInterface
  public boolean isAvailable() {
    return this.androidAccelerometer.isAvailable();
  }
  
  @JavascriptInterface
  public void setDistanceUnit(String paramString) {
    startBlockExecution(BlockType.SETTER, ".DistanceUnit");
    DistanceUnit distanceUnit = checkArg(paramString, DistanceUnit.class, "");
    if (distanceUnit != null)
      this.androidAccelerometer.setDistanceUnit(distanceUnit); 
  }
  
  @JavascriptInterface
  public void startListening() {
    startBlockExecution(BlockType.FUNCTION, ".startListening");
    this.androidAccelerometer.startListening();
  }
  
  @JavascriptInterface
  public void stopListening() {
    startBlockExecution(BlockType.FUNCTION, ".stopListening");
    this.androidAccelerometer.stopListening();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AndroidAccelerometerAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */