package com.google.blocks.ftcrobotcontroller.runtime;

import android.util.Pair;
import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

abstract class VuforiaBaseAccess<T extends VuforiaBase> extends Access {
  private final HardwareMap hardwareMap;
  
  private T vuforiaBase;
  
  VuforiaBaseAccess(BlocksOpMode paramBlocksOpMode, String paramString1, HardwareMap paramHardwareMap, String paramString2) {
    super(paramBlocksOpMode, paramString1, paramString2);
    this.hardwareMap = paramHardwareMap;
  }
  
  private boolean checkAndSetVuforiaBase() {
    if (this.vuforiaBase != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize has already been called!");
      reportWarning(stringBuilder.toString());
      return false;
    } 
    this.vuforiaBase = createVuforia();
    return true;
  }
  
  @JavascriptInterface
  public void activate() {
    startBlockExecution(BlockType.FUNCTION, ".activate");
    try {
      this.vuforiaBase.activate();
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
  
  void close() {
    T t = this.vuforiaBase;
    if (t != null) {
      t.close();
      this.vuforiaBase = null;
    } 
  }
  
  protected abstract T createVuforia();
  
  @JavascriptInterface
  public void deactivate() {
    startBlockExecution(BlockType.FUNCTION, ".deactivate");
    try {
      this.vuforiaBase.deactivate();
      return;
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
      return;
    } 
  }
  
  T getVuforiaBase() {
    StringBuilder stringBuilder;
    T t2 = this.vuforiaBase;
    T t1 = t2;
    if (t2 == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("You forgot to call ");
      stringBuilder.append(this.blockFirstName);
      stringBuilder.append(".initialize!");
      reportWarning(stringBuilder.toString());
      stringBuilder = null;
    } 
    return (T)stringBuilder;
  }
  
  @JavascriptInterface
  public void initialize_withCameraDirection(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean3) {
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    VuforiaLocalizer.CameraDirection cameraDirection = checkVuforiaLocalizerCameraDirection(paramString2);
    Pair<Boolean, VuforiaLocalizer.Parameters.CameraMonitorFeedback> pair = checkCameraMonitorFeedback(paramString3);
    if (cameraDirection != null && ((Boolean)pair.first).booleanValue() && checkAndSetVuforiaBase())
      try {
        this.vuforiaBase.initialize(paramString1, cameraDirection, paramBoolean1, paramBoolean2, (VuforiaLocalizer.Parameters.CameraMonitorFeedback)pair.second, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean3);
        return;
      } catch (Exception exception) {
        this.blocksOpMode.throwException(exception);
      }  
  }
  
  @JavascriptInterface
  public void initialize_withWebcam(String paramString1, String paramString2, boolean paramBoolean1, boolean paramBoolean2, String paramString3, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean3) {
    CameraName cameraName;
    startBlockExecution(BlockType.FUNCTION, ".initialize");
    if (paramString1.equals("Switchable Camera")) {
      cameraName = this.blocksOpMode.getSwitchableCamera();
    } else {
      cameraName = checkCameraNameFromString(this.hardwareMap, (String)cameraName);
    } 
    Pair<Boolean, VuforiaLocalizer.Parameters.CameraMonitorFeedback> pair = checkCameraMonitorFeedback(paramString3);
    if (cameraName != null && ((Boolean)pair.first).booleanValue() && checkAndSetVuforiaBase())
      try {
        this.vuforiaBase.initialize("", cameraName, paramString2, paramBoolean1, paramBoolean2, (VuforiaLocalizer.Parameters.CameraMonitorFeedback)pair.second, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean3);
        return;
      } catch (Exception exception) {
        this.blocksOpMode.throwException(exception);
      }  
  }
  
  @JavascriptInterface
  public void setActiveCamera(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".deactivate");
    CameraName cameraName = checkCameraNameFromString(this.hardwareMap, paramString);
    if (cameraName != null)
      try {
        this.vuforiaBase.setActiveCamera(cameraName);
        return;
      } catch (IllegalStateException illegalStateException) {
        reportWarning(illegalStateException.getMessage());
      }  
  }
  
  @JavascriptInterface
  public String track(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".track");
    try {
      return this.vuforiaBase.track(paramString).toJson();
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
    } catch (IllegalArgumentException illegalArgumentException) {
      reportInvalidArg("name", this.vuforiaBase.printTrackableNames());
    } 
    return this.vuforiaBase.emptyTrackingResults(paramString).toJson();
  }
  
  @JavascriptInterface
  public String trackPose(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".trackPose");
    try {
      return this.vuforiaBase.trackPose(paramString).toJson();
    } catch (IllegalStateException illegalStateException) {
      reportWarning(illegalStateException.getMessage());
    } catch (IllegalArgumentException illegalArgumentException) {
      reportInvalidArg("name", this.vuforiaBase.printTrackableNames());
    } 
    return this.vuforiaBase.emptyTrackingResults(paramString).toJson();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaBaseAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */