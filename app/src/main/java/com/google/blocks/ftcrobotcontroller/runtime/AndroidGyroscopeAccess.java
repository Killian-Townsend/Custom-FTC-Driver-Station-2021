package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.android.AndroidGyroscope;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;

class AndroidGyroscopeAccess extends Access {
  private final AndroidGyroscope androidGyroscope = new AndroidGyroscope();
  
  AndroidGyroscopeAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AndroidGyroscope");
  }
  
  void close() {
    this.androidGyroscope.stopListening();
  }
  
  @JavascriptInterface
  public String getAngleUnit() {
    startBlockExecution(BlockType.GETTER, ".AngleUnit");
    return this.androidGyroscope.getAngleUnit().toString();
  }
  
  @JavascriptInterface
  public AngularVelocity getAngularVelocity() {
    startBlockExecution(BlockType.GETTER, ".AngularVelocity");
    return this.androidGyroscope.getAngularVelocity();
  }
  
  @JavascriptInterface
  public float getX() {
    startBlockExecution(BlockType.GETTER, ".X");
    return this.androidGyroscope.getX();
  }
  
  @JavascriptInterface
  public float getY() {
    startBlockExecution(BlockType.GETTER, ".Y");
    return this.androidGyroscope.getY();
  }
  
  @JavascriptInterface
  public float getZ() {
    startBlockExecution(BlockType.GETTER, ".Z");
    return this.androidGyroscope.getZ();
  }
  
  @JavascriptInterface
  public boolean isAvailable() {
    startBlockExecution(BlockType.FUNCTION, ".isAvailable");
    return this.androidGyroscope.isAvailable();
  }
  
  @JavascriptInterface
  public void setAngleUnit(String paramString) {
    startBlockExecution(BlockType.SETTER, ".AngleUnit");
    AngleUnit angleUnit = checkArg(paramString, AngleUnit.class, "");
    if (angleUnit != null)
      this.androidGyroscope.setAngleUnit(angleUnit); 
  }
  
  @JavascriptInterface
  public void startListening() {
    startBlockExecution(BlockType.FUNCTION, ".startListening");
    this.androidGyroscope.startListening();
  }
  
  @JavascriptInterface
  public void stopListening() {
    startBlockExecution(BlockType.FUNCTION, ".stopListening");
    this.androidGyroscope.stopListening();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AndroidGyroscopeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */