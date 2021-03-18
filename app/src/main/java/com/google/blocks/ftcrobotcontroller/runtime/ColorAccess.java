package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

class ColorAccess extends Access {
  private final Activity activity;
  
  ColorAccess(BlocksOpMode paramBlocksOpMode, String paramString, Activity paramActivity) {
    super(paramBlocksOpMode, paramString, "Color");
    this.activity = paramActivity;
  }
  
  @JavascriptInterface
  public int ahsvToColor(int paramInt, float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".ahsvToColor");
    return Color.HSVToColor(paramInt, new float[] { paramFloat1, paramFloat2, paramFloat3 });
  }
  
  @JavascriptInterface
  public int argbToColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    startBlockExecution(BlockType.FUNCTION, ".argbToColor");
    return Color.argb(paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  @JavascriptInterface
  public double getAlpha(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Alpha");
    return Color.alpha(paramInt);
  }
  
  @JavascriptInterface
  public double getBlue(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Blue");
    return Color.blue(paramInt);
  }
  
  @JavascriptInterface
  public double getGreen(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Green");
    return Color.green(paramInt);
  }
  
  @JavascriptInterface
  public float getHue(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Hue");
    return JavaUtil.colorToHue(paramInt);
  }
  
  @JavascriptInterface
  public double getRed(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Red");
    return Color.red(paramInt);
  }
  
  @JavascriptInterface
  public float getSaturation(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Saturation");
    return JavaUtil.colorToSaturation(paramInt);
  }
  
  @JavascriptInterface
  public float getValue(int paramInt) {
    startBlockExecution(BlockType.GETTER, ".Value");
    return JavaUtil.colorToValue(paramInt);
  }
  
  @JavascriptInterface
  public int hsvToColor(float paramFloat1, float paramFloat2, float paramFloat3) {
    startBlockExecution(BlockType.FUNCTION, ".hsvToColor");
    return Color.HSVToColor(new float[] { paramFloat1, paramFloat2, paramFloat3 });
  }
  
  @JavascriptInterface
  public int rgbToColor(int paramInt1, int paramInt2, int paramInt3) {
    startBlockExecution(BlockType.FUNCTION, ".rgbToColor");
    return Color.rgb(paramInt1, paramInt2, paramInt3);
  }
  
  @JavascriptInterface
  public float rgbToHue(int paramInt1, int paramInt2, int paramInt3) {
    startBlockExecution(BlockType.FUNCTION, ".rgbToHue");
    return JavaUtil.rgbToHue(paramInt1, paramInt2, paramInt3);
  }
  
  @JavascriptInterface
  public float rgbToSaturation(int paramInt1, int paramInt2, int paramInt3) {
    startBlockExecution(BlockType.FUNCTION, ".rgbToSaturation");
    return JavaUtil.rgbToSaturation(paramInt1, paramInt2, paramInt3);
  }
  
  @JavascriptInterface
  public float rgbToValue(int paramInt1, int paramInt2, int paramInt3) {
    startBlockExecution(BlockType.FUNCTION, ".rgbToValue");
    return JavaUtil.rgbToValue(paramInt1, paramInt2, paramInt3);
  }
  
  @JavascriptInterface
  public void showColor(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".showColor");
    JavaUtil.showColor((Context)this.activity, paramInt);
  }
  
  @JavascriptInterface
  public int textToColor(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".textToColor");
    return Color.parseColor(paramString);
  }
  
  @JavascriptInterface
  public String toText(int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    return JavaUtil.colorToText(paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ColorAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */