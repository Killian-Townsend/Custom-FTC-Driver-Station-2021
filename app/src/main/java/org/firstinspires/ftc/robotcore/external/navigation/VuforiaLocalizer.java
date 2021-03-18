package org.firstinspires.ftc.robotcore.external.navigation;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import com.qualcomm.robotcore.R;
import com.vuforia.Frame;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public interface VuforiaLocalizer extends CameraStreamSource {
  Bitmap convertFrameToBitmap(Frame paramFrame);
  
  void enableConvertFrameToBitmap();
  
  boolean[] enableConvertFrameToFormat(int... paramVarArgs);
  
  Camera getCamera();
  
  CameraCalibration getCameraCalibration();
  
  CameraName getCameraName();
  
  void getFrameOnce(Continuation<? extends Consumer<Frame>> paramContinuation);
  
  BlockingQueue<CloseableFrame> getFrameQueue();
  
  int getFrameQueueCapacity();
  
  VuforiaTrackables loadTrackablesFromAsset(String paramString);
  
  VuforiaTrackables loadTrackablesFromFile(String paramString);
  
  void setFrameQueueCapacity(int paramInt);
  
  public enum CameraDirection {
    BACK(1),
    DEFAULT(1),
    FRONT(2),
    UNKNOWN(2);
    
    protected final int direction;
    
    static {
      CameraDirection cameraDirection = new CameraDirection("UNKNOWN", 3, -1);
      UNKNOWN = cameraDirection;
      $VALUES = new CameraDirection[] { BACK, FRONT, DEFAULT, cameraDirection };
    }
    
    CameraDirection(int param1Int1) {
      this.direction = param1Int1;
    }
    
    public static CameraDirection from(String param1String) {
      for (CameraDirection cameraDirection : values()) {
        if (cameraDirection.toString().equals(param1String))
          return cameraDirection; 
      } 
      return UNKNOWN;
    }
    
    public int getDirection() {
      if (this != UNKNOWN)
        return this.direction; 
      throw new IllegalArgumentException("%s has no actual 'direction' value");
    }
  }
  
  public static class CloseableFrame extends Frame {
    public CloseableFrame(Frame param1Frame) {
      super(param1Frame);
    }
    
    public void close() {
      delete();
    }
  }
  
  public static class Parameters {
    public Activity activity = null;
    
    public Camera camera = null;
    
    public VuforiaLocalizer.CameraDirection cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    
    public CameraMonitorFeedback cameraMonitorFeedback = CameraMonitorFeedback.AXES;
    
    public int cameraMonitorViewIdParent = 0;
    
    public ViewGroup cameraMonitorViewParent = null;
    
    public CameraName cameraName = ClassFactory.getInstance().getCameraManager().nameForUnknownCamera();
    
    public boolean fillCameraMonitorViewParent = false;
    
    public double maxWebcamAspectRatio = Double.MAX_VALUE;
    
    public double minWebcamAspectRatio = 0.0D;
    
    public int secondsUsbPermissionTimeout = 30;
    
    public boolean useExtendedTracking = true;
    
    public String vuforiaLicenseKey = "<visit https://developer.vuforia.com/license-manager to obtain a license key>";
    
    public File[] webcamCalibrationFiles = new File[0];
    
    public int[] webcamCalibrationResources = new int[] { R.xml.teamwebcamcalibrations };
    
    public Parameters() {}
    
    public Parameters(int param1Int) {
      this.cameraMonitorViewIdParent = param1Int;
    }
    
    public void addWebcamCalibrationFile(File param1File) {
      File[] arrayOfFile = this.webcamCalibrationFiles;
      arrayOfFile = Arrays.<File>copyOf(arrayOfFile, arrayOfFile.length + 1);
      this.webcamCalibrationFiles = arrayOfFile;
      arrayOfFile[arrayOfFile.length - 1] = param1File;
    }
    
    public void addWebcamCalibrationFile(String param1String) {
      addWebcamCalibrationFile(new File(AppUtil.WEBCAM_CALIBRATIONS_DIR, param1String));
    }
    
    public enum CameraMonitorFeedback {
      NONE,
      TEAPOT,
      AXES(2),
      BUILDINGS(2);
      
      static {
        CameraMonitorFeedback cameraMonitorFeedback = new CameraMonitorFeedback("BUILDINGS", 3);
        BUILDINGS = cameraMonitorFeedback;
        $VALUES = new CameraMonitorFeedback[] { NONE, AXES, TEAPOT, cameraMonitorFeedback };
      }
    }
  }
  
  public enum CameraMonitorFeedback {
    AXES, BUILDINGS, NONE, TEAPOT;
    
    static {
      CameraMonitorFeedback cameraMonitorFeedback = new CameraMonitorFeedback("BUILDINGS", 3);
      BUILDINGS = cameraMonitorFeedback;
      $VALUES = new CameraMonitorFeedback[] { NONE, AXES, TEAPOT, cameraMonitorFeedback };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaLocalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */