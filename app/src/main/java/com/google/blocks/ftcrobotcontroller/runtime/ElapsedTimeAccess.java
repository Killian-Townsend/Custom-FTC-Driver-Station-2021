package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.util.ElapsedTime;

class ElapsedTimeAccess extends Access {
  ElapsedTimeAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "ElapsedTime");
  }
  
  private ElapsedTime checkElapsedTime(Object paramObject) {
    return checkArg(paramObject, ElapsedTime.class, "timer");
  }
  
  @JavascriptInterface
  public ElapsedTime create() {
    startBlockExecution(BlockType.CREATE, "");
    return new ElapsedTime();
  }
  
  @JavascriptInterface
  public ElapsedTime create_withResolution(String paramString) {
    startBlockExecution(BlockType.CREATE, "");
    ElapsedTime.Resolution resolution = checkArg(paramString, ElapsedTime.Resolution.class, "resolution");
    return (resolution != null) ? new ElapsedTime(resolution) : null;
  }
  
  @JavascriptInterface
  public ElapsedTime create_withStartTime(long paramLong) {
    startBlockExecution(BlockType.CREATE, "");
    return new ElapsedTime(paramLong);
  }
  
  @JavascriptInterface
  public String getAsText(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".AsText");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
  
  @JavascriptInterface
  public double getMilliseconds(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Milliseconds");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.milliseconds() : 0.0D;
  }
  
  @JavascriptInterface
  public String getResolution(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Resolution");
    paramObject = checkElapsedTime(paramObject);
    if (paramObject != null) {
      paramObject = paramObject.getResolution();
      if (paramObject != null)
        return paramObject.toString(); 
    } 
    return "";
  }
  
  @JavascriptInterface
  public double getSeconds(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Seconds");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.seconds() : 0.0D;
  }
  
  @JavascriptInterface
  public double getStartTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".StartTime");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.startTime() : 0.0D;
  }
  
  @JavascriptInterface
  public double getTime(Object paramObject) {
    startBlockExecution(BlockType.GETTER, ".Time");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.time() : 0.0D;
  }
  
  @JavascriptInterface
  public void log(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".log");
    paramObject = checkElapsedTime(paramObject);
    if (paramObject != null)
      paramObject.log(paramString); 
  }
  
  @JavascriptInterface
  public void reset(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".reset");
    paramObject = checkElapsedTime(paramObject);
    if (paramObject != null)
      paramObject.reset(); 
  }
  
  @JavascriptInterface
  public String toText(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".toText");
    paramObject = checkElapsedTime(paramObject);
    return (paramObject != null) ? paramObject.toString() : "";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\ElapsedTimeAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */