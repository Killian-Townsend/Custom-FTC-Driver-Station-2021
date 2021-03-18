package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.tfod.TfodBase;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCustomModel;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

final class TfodCustomModelAccess extends TfodBaseAccess<TfodCustomModel> {
  private String assetName;
  
  private String[] labels;
  
  private String tfliteModelFilename;
  
  TfodCustomModelAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, paramHardwareMap, "TensorFlowObjectDetectionCustomModel");
  }
  
  protected TfodCustomModel createTfod() {
    TfodCustomModel tfodCustomModel = new TfodCustomModel();
    String str = this.assetName;
    if (str != null) {
      tfodCustomModel.setModelFromAsset(str, this.labels);
      return tfodCustomModel;
    } 
    tfodCustomModel.setModelFromFile(this.tfliteModelFilename, this.labels);
    return tfodCustomModel;
  }
  
  @JavascriptInterface
  public void setModelFromAsset(String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".setModelFromAsset");
    String[] arrayOfString = (String[])SimpleGson.getInstance().fromJson(paramString2, String[].class);
    this.assetName = paramString1;
    this.tfliteModelFilename = null;
    this.labels = arrayOfString;
  }
  
  @JavascriptInterface
  public void setModelFromFile(String paramString1, String paramString2) {
    startBlockExecution(BlockType.FUNCTION, ".setModelFromFile");
    String[] arrayOfString = (String[])SimpleGson.getInstance().fromJson(paramString2, String[].class);
    this.assetName = null;
    this.tfliteModelFilename = paramString1;
    this.labels = arrayOfString;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\TfodCustomModelAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */