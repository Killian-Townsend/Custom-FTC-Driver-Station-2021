package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

class VuforiaLocalizerAccess extends Access {
  VuforiaLocalizerAccess(BlocksOpMode paramBlocksOpMode, String paramString) {
    super(paramBlocksOpMode, paramString, "VuforiaLocalizer");
  }
  
  private VuforiaLocalizer checkVuforiaLocalizer(Object paramObject) {
    return checkArg(paramObject, VuforiaLocalizer.class, "vuforiaLocalizer");
  }
  
  @JavascriptInterface
  public VuforiaLocalizer create(final Object vuforiaLocalizerHolder) {
    startBlockExecution(BlockType.CREATE, "");
    final VuforiaLocalizer.Parameters parameters = checkVuforiaLocalizerParameters(vuforiaLocalizerHolder);
    if (parameters != null) {
      vuforiaLocalizerHolder = new AtomicReference();
      Thread thread = new Thread(new Runnable() {
            public void run() {
              vuforiaLocalizerHolder.set(ClassFactory.getInstance().createVuforia(parameters));
            }
          });
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
      return vuforiaLocalizerHolder.get();
    } 
    return null;
  }
  
  @JavascriptInterface
  public VuforiaTrackables loadTrackablesFromAsset(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".loadTrackablesFromAsset");
    paramObject = checkVuforiaLocalizer(paramObject);
    return (paramObject != null) ? paramObject.loadTrackablesFromAsset(paramString) : null;
  }
  
  @JavascriptInterface
  public VuforiaTrackables loadTrackablesFromFile(Object paramObject, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".loadTrackablesFromFile");
    paramObject = checkVuforiaLocalizer(paramObject);
    return (paramObject != null) ? paramObject.loadTrackablesFromFile(paramString) : null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaLocalizerAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */