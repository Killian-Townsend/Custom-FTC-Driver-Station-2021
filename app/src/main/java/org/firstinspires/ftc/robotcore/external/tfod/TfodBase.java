package org.firstinspires.ftc.robotcore.external.tfod;

import android.app.Activity;
import java.io.File;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class TfodBase {
  private String assetName;
  
  private String[] labels;
  
  private String tfliteModelFilename;
  
  private TFObjectDetector tfod;
  
  protected TfodBase() {}
  
  protected TfodBase(String paramString, String[] paramArrayOfString) {
    setModelFromAsset(paramString, paramArrayOfString);
  }
  
  public void activate() {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null) {
      tFObjectDetector.activate();
      return;
    } 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
  
  public void close() {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null) {
      tFObjectDetector.deactivate();
      this.tfod.shutdown();
      this.tfod = null;
    } 
  }
  
  public void deactivate() {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null) {
      tFObjectDetector.deactivate();
      return;
    } 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
  
  public List<Recognition> getRecognitions() {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null)
      return tFObjectDetector.getRecognitions(); 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
  
  public List<Recognition> getUpdatedRecognitions() {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null)
      return tFObjectDetector.getUpdatedRecognitions(); 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
  
  public void initialize(VuforiaBase paramVuforiaBase, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    initialize(paramVuforiaBase.getVuforiaLocalizer(), paramFloat, paramBoolean1, paramBoolean2);
  }
  
  public void initialize(VuforiaLocalizer paramVuforiaLocalizer, float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
    if (this.assetName == null || this.tfliteModelFilename == null) {
      String str1;
      TFObjectDetector.Parameters parameters = new TFObjectDetector.Parameters();
      parameters.minimumConfidence = paramFloat;
      parameters.minResultConfidence = paramFloat;
      parameters.useObjectTracker = paramBoolean1;
      if (paramBoolean2) {
        Activity activity = AppUtil.getInstance().getRootActivity();
        parameters.tfodMonitorViewIdParent = activity.getResources().getIdentifier("tfodMonitorViewId", "id", activity.getPackageName());
      } 
      TFObjectDetector tFObjectDetector = ClassFactory.getInstance().createTFObjectDetector(parameters, paramVuforiaLocalizer);
      this.tfod = tFObjectDetector;
      String str2 = this.assetName;
      if (str2 != null) {
        tFObjectDetector.loadModelFromAsset(str2, this.labels);
        return;
      } 
      File file = new File(AppUtil.TFLITE_MODELS_DIR, this.tfliteModelFilename);
      if (file.exists()) {
        str1 = file.getAbsolutePath();
      } else {
        str1 = this.tfliteModelFilename;
      } 
      this.tfod.loadModelFromFile(str1, this.labels);
      return;
    } 
    throw new IllegalStateException("assetName and tfliteModelFilename are both non-null!");
  }
  
  public void setClippingMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null) {
      tFObjectDetector.setClippingMargins(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
  
  public void setModelFromAsset(String paramString, List<String> paramList) {
    setModelFromAsset(paramString, paramList.<String>toArray(new String[paramList.size()]));
  }
  
  public void setModelFromAsset(String paramString, String[] paramArrayOfString) {
    if (this.tfod == null) {
      this.assetName = paramString;
      this.tfliteModelFilename = null;
      this.labels = paramArrayOfString;
      return;
    } 
    throw new IllegalStateException("You may not call setModelFromAsset after Tfod.initialize!");
  }
  
  public void setModelFromFile(String paramString, List<String> paramList) {
    setModelFromFile(paramString, paramList.<String>toArray(new String[paramList.size()]));
  }
  
  public void setModelFromFile(String paramString, String[] paramArrayOfString) {
    if (this.tfod == null) {
      this.assetName = null;
      this.tfliteModelFilename = paramString;
      this.labels = paramArrayOfString;
      return;
    } 
    throw new IllegalStateException("You may not call setModelFromFile after Tfod.initialize!");
  }
  
  public void setZoom(double paramDouble1, double paramDouble2) {
    TFObjectDetector tFObjectDetector = this.tfod;
    if (tFObjectDetector != null) {
      tFObjectDetector.setZoom(paramDouble1, paramDouble2);
      return;
    } 
    throw new IllegalStateException("You forgot to call Tfod.initialize!");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\tfod\TfodBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */