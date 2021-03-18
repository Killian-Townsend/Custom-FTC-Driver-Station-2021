package org.firstinspires.ftc.robotcore.external.tfod;

import android.app.Activity;
import android.view.ViewGroup;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;

public interface TFObjectDetector extends CameraStreamSource {
  void activate();
  
  void deactivate();
  
  List<Recognition> getRecognitions();
  
  List<Recognition> getUpdatedRecognitions();
  
  void loadModelFromAsset(String paramString, String... paramVarArgs);
  
  void loadModelFromFile(String paramString, String... paramVarArgs);
  
  void setClippingMargins(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void setZoom(double paramDouble1, double paramDouble2);
  
  void shutdown();
  
  public static class Parameters {
    public Activity activity = null;
    
    public int inputSize = 300;
    
    public boolean isModelQuantized = true;
    
    public double maxFrameRate = 30.0D;
    
    public int maxNumDetections = 10;
    
    public float minResultConfidence = 0.4F;
    
    @Deprecated
    public double minimumConfidence = 0.4D;
    
    public int numExecutorThreads = 2;
    
    public int numInterpreterThreads = 1;
    
    public int tfodMonitorViewIdParent = 0;
    
    public ViewGroup tfodMonitorViewParent = null;
    
    public int timingBufferSize = 10;
    
    public float trackerMarginalCorrelation = 0.75F;
    
    public float trackerMaxOverlap = 0.2F;
    
    public float trackerMinCorrelation = 0.3F;
    
    public float trackerMinSize = 16.0F;
    
    public boolean useObjectTracker = true;
    
    public Parameters() {}
    
    public Parameters(int param1Int) {
      this.tfodMonitorViewIdParent = param1Int;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\tfod\TFObjectDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */