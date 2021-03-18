package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import org.firstinspires.ftc.robotcore.external.Telemetry;

class TelemetryAccess extends Access {
  private final Telemetry telemetry;
  
  TelemetryAccess(BlocksOpMode paramBlocksOpMode, String paramString, Telemetry paramTelemetry) {
    super(paramBlocksOpMode, paramString, "Telemetry");
    this.telemetry = paramTelemetry;
  }
  
  @JavascriptInterface
  public void addNumericData(String paramString, double paramDouble) {
    startBlockExecution(BlockType.FUNCTION, ".addData");
    this.telemetry.addData(paramString, Double.valueOf(paramDouble));
  }
  
  @JavascriptInterface
  public void addObjectData(String paramString, Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".addData");
    Telemetry telemetry = this.telemetry;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramObject);
    telemetry.addData(paramString, stringBuilder.toString());
  }
  
  @JavascriptInterface
  public void addTextData(String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".addData");
    if (paramString2 != null)
      this.telemetry.addData(paramString1, paramString2); 
  }
  
  @JavascriptInterface
  public void setDisplayFormat(String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setDisplayFormat");
    Telemetry.DisplayFormat displayFormat = checkArg(paramString, Telemetry.DisplayFormat.class, "displayFormat");
    if (displayFormat != null)
      this.telemetry.setDisplayFormat(displayFormat); 
  }
  
  @JavascriptInterface
  public void speakObjectData(Object paramObject, String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".speak");
    Telemetry telemetry = this.telemetry;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("");
    stringBuilder.append(paramObject);
    telemetry.speak(stringBuilder.toString(), paramString1, paramString2);
  }
  
  @JavascriptInterface
  public void speakTextData(String paramString1, String paramString2, String paramString3) {
    startBlockExecution(BlockType.FUNCTION, ".speak");
    if (paramString1 != null)
      this.telemetry.speak(paramString1, paramString2, paramString3); 
  }
  
  @JavascriptInterface
  public void update() {
    startBlockExecution(BlockType.FUNCTION, ".update");
    this.telemetry.update();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TelemetryAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */