package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;

class AndroidTextToSpeechAccess extends Access {
  private final AndroidTextToSpeech androidTextToSpeech = new AndroidTextToSpeech();
  
  AndroidTextToSpeechAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "AndroidTextToSpeech");
  }
  
  void close() {
    this.androidTextToSpeech.close();
  }
  
  @JavascriptInterface
  public String getCountryCode() {
    startBlockExecution(BlockType.GETTER, ".CountryCode");
    try {
      return this.androidTextToSpeech.getCountryCode();
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return "";
    } 
  }
  
  @JavascriptInterface
  public boolean getIsSpeaking() {
    startBlockExecution(BlockType.GETTER, ".IsSpeaking");
    try {
      return this.androidTextToSpeech.isSpeaking();
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return false;
    } 
  }
  
  @JavascriptInterface
  public String getLanguageCode() {
    startBlockExecution(BlockType.GETTER, ".LanguageCode");
    try {
      return this.androidTextToSpeech.getLanguageCode();
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return "";
    } 
  }
  
  @JavascriptInterface
  public String getStatus() {
    startBlockExecution(BlockType.GETTER, ".Status");
    return this.androidTextToSpeech.getStatus();
  }
  
  @JavascriptInterface
  public void initialize() {
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    this.androidTextToSpeech.initialize();
  }
  
  @JavascriptInterface
  public boolean isLanguageAndCountryAvailable(String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".isLanguageAndCountryAvailable");
    try {
      return this.androidTextToSpeech.isLanguageAndCountryAvailable(paramString1, paramString2);
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return false;
    } 
  }
  
  @JavascriptInterface
  public boolean isLanguageAvailable(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".isLanguageAvailable");
    try {
      return this.androidTextToSpeech.isLanguageAvailable(paramString);
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return false;
    } 
  }
  
  @JavascriptInterface
  public void setLanguage(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setLanguage");
    try {
      this.androidTextToSpeech.setLanguage(paramString);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return;
    } 
  }
  
  @JavascriptInterface
  public void setLanguageAndCountry(String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".setLanguageAndCountry");
    try {
      this.androidTextToSpeech.setLanguageAndCountry(paramString1, paramString2);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return;
    } 
  }
  
  @JavascriptInterface
  public void setPitch(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".Pitch");
    try {
      this.androidTextToSpeech.setPitch(paramFloat);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return;
    } 
  }
  
  @JavascriptInterface
  public void setSpeechRate(float paramFloat) {
    startBlockExecution(BlockType.SETTER, ".SpeechRate");
    try {
      this.androidTextToSpeech.setSpeechRate(paramFloat);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return;
    } 
  }
  
  @JavascriptInterface
  public void speak(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".speak");
    try {
      this.androidTextToSpeech.speak(paramString);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning("You forgot to call AndroidTextToSpeech.initialize!");
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\AndroidTextToSpeechAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */