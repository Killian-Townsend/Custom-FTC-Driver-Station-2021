package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TfodBase;

abstract class TfodBaseAccess<T extends TfodBase> extends Access {
  private final HardwareMap hardwareMap;
  
  private T tfodBase;
  
  TfodBaseAccess(BlocksOpMode paramBlocksOpMode, String paramString1, HardwareMap paramHardwareMap, String paramString2) {
    super(paramBlocksOpMode, paramString1, paramString2);
    this.hardwareMap = paramHardwareMap;
  }
  
  private boolean checkAndSetTfodBase() {
    if (this.tfodBase != null) {
      reportWarning("Tfod.initialize has already been called!");
      return false;
    } 
    this.tfodBase = createTfod();
    return true;
  }
  
  private static String toJson(List<Recognition> paramList) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    Iterator<Recognition> iterator = paramList.iterator();
    for (String str = ""; iterator.hasNext(); str = ",") {
      Recognition recognition = iterator.next();
      stringBuilder.append(str);
      stringBuilder.append(toJson(recognition));
    } 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  private static String toJson(Recognition paramRecognition) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{ \"Label\":\"");
    stringBuilder.append(paramRecognition.getLabel());
    stringBuilder.append("\", \"Confidence\":");
    stringBuilder.append(paramRecognition.getConfidence());
    stringBuilder.append(", \"Left\":");
    stringBuilder.append(paramRecognition.getLeft());
    stringBuilder.append(", \"Right\":");
    stringBuilder.append(paramRecognition.getRight());
    stringBuilder.append(", \"Top\":");
    stringBuilder.append(paramRecognition.getTop());
    stringBuilder.append(", \"Bottom\":");
    stringBuilder.append(paramRecognition.getBottom());
    stringBuilder.append(", \"Width\":");
    stringBuilder.append(paramRecognition.getWidth());
    stringBuilder.append(", \"Height\":");
    stringBuilder.append(paramRecognition.getHeight());
    stringBuilder.append(", \"ImageWidth\":");
    stringBuilder.append(paramRecognition.getImageWidth());
    stringBuilder.append(", \"ImageHeight\":");
    stringBuilder.append(paramRecognition.getImageHeight());
    stringBuilder.append(", \"estimateAngleToObject\":");
    stringBuilder.append(paramRecognition.estimateAngleToObject(AngleUnit.RADIANS));
    stringBuilder.append(" }");
    return stringBuilder.toString();
  }
  
  @JavascriptInterface
  public void activate() {
    StringBuilder stringBuilder;
    startBlockExecution(BlockType.FUNCTION, ".activate");
    T t = this.tfodBase;
    if (t == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      return;
    } 
    try {
      stringBuilder.activate();
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
  
  void close() {
    T t = this.tfodBase;
    if (t != null) {
      t.close();
      this.tfodBase = null;
    } 
  }
  
  protected abstract T createTfod();
  
  @JavascriptInterface
  public void deactivate() {
    StringBuilder stringBuilder;
    startBlockExecution(BlockType.FUNCTION, ".deactivate");
    T t = this.tfodBase;
    if (t == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      return;
    } 
    try {
      stringBuilder.deactivate();
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
  
  @JavascriptInterface
  public String getRecognitions() {
    StringBuilder stringBuilder;
    startBlockExecution(BlockType.FUNCTION, ".getRecognitions");
    T t = this.tfodBase;
    if (t == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      return "[]";
    } 
    try {
      return toJson(stringBuilder.getRecognitions());
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return "[]";
    } 
  }
  
  @JavascriptInterface
  public void initialize(VuforiaBaseAccess paramVuforiaBaseAccess, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    VuforiaLocalizer vuforiaLocalizer = paramVuforiaBaseAccess.getVuforiaBase().getVuforiaLocalizer();
    if (checkAndSetTfodBase() && vuforiaLocalizer != null)
      try {
        this.tfodBase.initialize(vuforiaLocalizer, paramFloat, paramBoolean1, paramBoolean2);
        return;
      } catch (IllegalStateException illegalStateException) {
        reportWarning(illegalStateException.getMessage());
      }  
  }
  
  @JavascriptInterface
  public void setClippingMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    StringBuilder stringBuilder;
    startBlockExecution(BlockType.FUNCTION, ".setClippingMargins");
    T t = this.tfodBase;
    if (t == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      return;
    } 
    try {
      stringBuilder.setClippingMargins(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
  
  @JavascriptInterface
  public void setZoom(double paramDouble1, double paramDouble2) {
    StringBuilder stringBuilder;
    startBlockExecution(BlockType.FUNCTION, ".setZoom");
    T t = this.tfodBase;
    if (t == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      return;
    } 
    try {
      stringBuilder.setZoom(paramDouble1, paramDouble2);
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TfodBaseAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */