package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

class VuforiaTrackableAccess extends Access {
  VuforiaTrackableAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "VuforiaTrackable");
  }
  
  @JavascriptInterface
  public VuforiaTrackable.Listener getListener(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Listener");
    paramObject = checkVuforiaTrackable(paramObject);
    return (paramObject != null) ? paramObject.getListener() : null;
  }
  
  @JavascriptInterface
  public OpenGLMatrix getLocation(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Location");
    paramObject = checkVuforiaTrackable(paramObject);
    return (paramObject != null) ? paramObject.getLocation() : null;
  }
  
  @JavascriptInterface
  public String getName(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Name");
    paramObject = checkVuforiaTrackable(paramObject);
    if (paramObject != null) {
      paramObject = paramObject.getName();
      if (paramObject != null)
        return (String)paramObject; 
    } 
    return "";
  }
  
  @JavascriptInterface
  public VuforiaTrackables getTrackables(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Trackables");
    paramObject = checkVuforiaTrackable(paramObject);
    return (paramObject != null) ? paramObject.getTrackables() : null;
  }
  
  @JavascriptInterface
  public Object getUserData(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".UserData");
    paramObject = checkVuforiaTrackable(paramObject);
    return (paramObject != null) ? paramObject.getUserData() : null;
  }
  
  @JavascriptInterface
  public void setLocation(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".setLocation");
    paramObject1 = checkVuforiaTrackable(paramObject1);
    paramObject2 = checkOpenGLMatrix(paramObject2);
    if (paramObject1 != null && paramObject2 != null)
      paramObject1.setLocation((OpenGLMatrix)paramObject2); 
  }
  
  @JavascriptInterface
  public void setName(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setName");
    paramObject = checkVuforiaTrackable(paramObject);
    if (paramObject != null)
      paramObject.setName(paramString); 
  }
  
  @JavascriptInterface
  public void setUserData(Object paramObject1, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".setUserData");
    paramObject1 = checkVuforiaTrackable(paramObject1);
    if (paramObject1 != null)
      paramObject1.setUserData(paramObject2); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaTrackableAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */