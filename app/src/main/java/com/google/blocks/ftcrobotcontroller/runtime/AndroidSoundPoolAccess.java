package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.ftccommon.SoundPlayer;
import org.firstinspires.ftc.robotcore.external.android.AndroidSoundPool;
import org.firstinspires.ftc.robotcore.internal.android.SoundPoolIntf;

class AndroidSoundPoolAccess extends Access {
  private final AndroidSoundPool androidSoundPool = new AndroidSoundPool();
  
  AndroidSoundPoolAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AndroidSoundPool");
  }
  
  void close() {
    this.androidSoundPool.close();
  }
  
  @JavascriptInterface
  public int getLoop() {
    startBlockExecution(BlockType.GETTER, ".Loop");
    return this.androidSoundPool.getLoop();
  }
  
  @JavascriptInterface
  public float getRate() {
    startBlockExecution(BlockType.GETTER, ".Rate");
    return this.androidSoundPool.getRate();
  }
  
  @JavascriptInterface
  public float getVolume() {
    startBlockExecution(BlockType.GETTER, ".Volume");
    return this.androidSoundPool.getVolume();
  }
  
  @JavascriptInterface
  public void initialize() {
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    this.androidSoundPool.initialize((SoundPoolIntf)SoundPlayer.getInstance());
  }
  
  @JavascriptInterface
  public void play(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".play");
    try {
      if (!this.androidSoundPool.play(paramString)) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to load ");
        stringBuilder.append(paramString);
        reportWarning(stringBuilder.toString());
        return;
      } 
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
    } 
  }
  
  @JavascriptInterface
  public boolean preloadSound(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".preloadSound");
    try {
      if (this.androidSoundPool.preloadSound(paramString))
        return true; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Failed to preload ");
      stringBuilder.append(paramString);
      reportWarning(stringBuilder.toString());
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
    } 
    return false;
  }
  
  @JavascriptInterface
  public void setLoop(int paramInt) {
    startBlockExecution(BlockType.SETTER, ".Loop");
    if (paramInt >= -1) {
      this.androidSoundPool.setLoop(paramInt);
      return;
    } 
    reportInvalidArg("", "a number greater than or equal to -1");
  }
  
  @JavascriptInterface
  public void setRate(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".Rate");
    if (paramFloat >= 0.5F && paramFloat <= 2.0F) {
      this.androidSoundPool.setRate(paramFloat);
      return;
    } 
    reportInvalidArg("", "a number between 0.5 and 2.0");
  }
  
  @JavascriptInterface
  public void setVolume(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".Volume");
    if (paramFloat >= 0.0F && paramFloat <= 1.0F) {
      this.androidSoundPool.setVolume(paramFloat);
      return;
    } 
    reportInvalidArg("", "a number between 0.0 and 1.0");
  }
  
  @JavascriptInterface
  public void stop() {
    startBlockExecution(BlockType.FUNCTION, ".stop");
    this.androidSoundPool.stop();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AndroidSoundPoolAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */