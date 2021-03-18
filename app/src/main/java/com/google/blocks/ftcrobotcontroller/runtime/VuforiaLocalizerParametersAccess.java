package com.google.blocks.ftcrobotcontroller.runtime;

import android.content.Context;
import android.util.Pair;
import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

class VuforiaLocalizerParametersAccess extends Access {
  private final Context context;
  
  private final HardwareMap hardwareMap;
  
  VuforiaLocalizerParametersAccess(BlocksOpMode paramBlocksOpMode, String paramString, Context paramContext, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, "VuforiaLocalizer.Parameters");
    this.context = paramContext;
    this.hardwareMap = paramHardwareMap;
  }
  
  @JavascriptInterface
  public void addWebcamCalibrationFile(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".addWebcamCalibrationFile");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null)
      paramObject.addWebcamCalibrationFile(paramString); 
  }
  
  @JavascriptInterface
  public VuforiaLocalizer.Parameters create() {
    startBlockExecution(BlockType.CREATE, "");
    return VuforiaBase.createParameters();
  }
  
  @JavascriptInterface
  public String getCameraDirection(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".CameraDirection");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((VuforiaLocalizer.Parameters)paramObject).cameraDirection;
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public String getCameraMonitorFeedback(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".CameraMonitorFeedback");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null) {
      paramObject = ((VuforiaLocalizer.Parameters)paramObject).cameraMonitorFeedback;
      return (paramObject == null) ? "DEFAULT" : paramObject.toString();
    } 
    return "";
  }
  
  @JavascriptInterface
  public String getCameraName(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".CameraName");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    return (paramObject != null) ? cameraNameToString(this.hardwareMap, ((VuforiaLocalizer.Parameters)paramObject).cameraName) : null;
  }
  
  @JavascriptInterface
  public boolean getEnableCameraMonitoring(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".EnableCameraMonitoring");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramObject != null) {
      bool1 = bool2;
      if (((VuforiaLocalizer.Parameters)paramObject).cameraMonitorViewIdParent != 0)
        bool1 = true; 
    } 
    return bool1;
  }
  
  @JavascriptInterface
  public boolean getFillCameraMonitorViewParent(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".FillCameraMonitorViewParent");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    return (paramObject != null) ? ((VuforiaLocalizer.Parameters)paramObject).fillCameraMonitorViewParent : false;
  }
  
  @JavascriptInterface
  public boolean getUseExtendedTracking(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".UseExtendedTracking");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    return (paramObject != null) ? ((VuforiaLocalizer.Parameters)paramObject).useExtendedTracking : false;
  }
  
  @JavascriptInterface
  public String getVuforiaLicenseKey(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".VuforiaLicenseKey");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    return (paramObject != null) ? ((VuforiaLocalizer.Parameters)paramObject).vuforiaLicenseKey : null;
  }
  
  @JavascriptInterface
  public void setCameraDirection(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setCameraDirection");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    VuforiaLocalizer.CameraDirection cameraDirection = checkVuforiaLocalizerCameraDirection(paramString);
    if (paramObject != null || cameraDirection != null)
      ((VuforiaLocalizer.Parameters)paramObject).cameraDirection = cameraDirection; 
  }
  
  @JavascriptInterface
  public void setCameraMonitorFeedback(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setCameraMonitorFeedback");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    Pair<Boolean, VuforiaLocalizer.Parameters.CameraMonitorFeedback> pair = checkCameraMonitorFeedback(paramString);
    if (paramObject != null && ((Boolean)pair.first).booleanValue())
      ((VuforiaLocalizer.Parameters)paramObject).cameraMonitorFeedback = (VuforiaLocalizer.Parameters.CameraMonitorFeedback)pair.second; 
  }
  
  @JavascriptInterface
  public void setCameraName(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setCameraName");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null)
      ((VuforiaLocalizer.Parameters)paramObject).cameraName = cameraNameFromString(this.hardwareMap, paramString); 
  }
  
  @JavascriptInterface
  public void setEnableCameraMonitoring(Object paramObject, boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".setEnableCameraMonitoring");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null) {
      if (paramBoolean) {
        ((VuforiaLocalizer.Parameters)paramObject).cameraMonitorViewIdParent = this.context.getResources().getIdentifier("cameraMonitorViewId", "id", this.context.getPackageName());
        return;
      } 
      ((VuforiaLocalizer.Parameters)paramObject).cameraMonitorViewIdParent = 0;
    } 
  }
  
  @JavascriptInterface
  public void setFillCameraMonitorViewParent(Object paramObject, boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".setFillCameraMonitorViewParent");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null)
      ((VuforiaLocalizer.Parameters)paramObject).fillCameraMonitorViewParent = paramBoolean; 
  }
  
  @JavascriptInterface
  public void setUseExtendedTracking(Object paramObject, boolean paramBoolean) {
    startBlockExecution(BlockType.FUNCTION, ".setUseExtendedTracking");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null)
      ((VuforiaLocalizer.Parameters)paramObject).useExtendedTracking = paramBoolean; 
  }
  
  @JavascriptInterface
  public void setVuforiaLicenseKey(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setVuforiaLicenseKey");
    paramObject = checkVuforiaLocalizerParameters(paramObject);
    if (paramObject != null)
      ((VuforiaLocalizer.Parameters)paramObject).vuforiaLicenseKey = paramString; 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaLocalizerParametersAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */