package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

class VuforiaTrackablesAccess extends Access {
  VuforiaTrackablesAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "VuforiaTrackables");
  }
  
  private VuforiaTrackables checkVuforiaTrackables(Object paramObject) {
    return checkArg(paramObject, VuforiaTrackables.class, "vuforiaTrackables");
  }
  
  @JavascriptInterface
  public void activate(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".activate");
    paramObject = checkVuforiaTrackables(paramObject);
    if (paramObject != null)
      paramObject.activate(); 
  }
  
  @JavascriptInterface
  public void deactivate(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".deactivate");
    paramObject = checkVuforiaTrackables(paramObject);
    if (paramObject != null)
      paramObject.deactivate(); 
  }
  
  @JavascriptInterface
  public VuforiaTrackable get(Object paramObject, int paramInt) {
    startBlockExecution(BlockType.FUNCTION, ".get");
    paramObject = checkVuforiaTrackables(paramObject);
    return (paramObject != null) ? (VuforiaTrackable)paramObject.get(paramInt) : null;
  }
  
  @JavascriptInterface
  public VuforiaLocalizer getLocalizer(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Localizer");
    paramObject = checkVuforiaTrackables(paramObject);
    return (paramObject != null) ? paramObject.getLocalizer() : null;
  }
  
  @JavascriptInterface
  public String getName(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Name");
    paramObject = checkVuforiaTrackables(paramObject);
    if (paramObject != null) {
      paramObject = paramObject.getName();
      if (paramObject != null)
        return (String)paramObject; 
    } 
    return "";
  }
  
  @JavascriptInterface
  public int getSize(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Size");
    paramObject = checkVuforiaTrackables(paramObject);
    return (paramObject != null) ? paramObject.size() : 0;
  }
  
  @JavascriptInterface
  public void setName(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setName");
    paramObject = checkVuforiaTrackables(paramObject);
    if (paramObject != null)
      paramObject.setName(paramString); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaTrackablesAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */