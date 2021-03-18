package org.firstinspires.ftc.robotcore.external.navigation;

import com.vuforia.TrackableResult;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;

public interface VuforiaTrackable {
  OpenGLMatrix getFtcFieldFromTarget();
  
  Listener getListener();
  
  OpenGLMatrix getLocation();
  
  String getName();
  
  VuforiaTrackable getParent();
  
  VuforiaTrackables getTrackables();
  
  Object getUserData();
  
  void setListener(Listener paramListener);
  
  void setLocation(OpenGLMatrix paramOpenGLMatrix);
  
  void setLocationFtcFieldFromTarget(OpenGLMatrix paramOpenGLMatrix);
  
  void setName(String paramString);
  
  void setUserData(Object paramObject);
  
  public static interface Listener {
    void addTrackable(VuforiaTrackable param1VuforiaTrackable);
    
    void onNotTracked();
    
    void onTracked(TrackableResult param1TrackableResult, CameraName param1CameraName, Camera param1Camera, VuforiaTrackable param1VuforiaTrackable);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */