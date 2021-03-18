package com.google.blocks.ftcrobotcontroller.runtime;

import android.webkit.JavascriptInterface;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

class VuforiaTrackableDefaultListenerAccess extends Access {
  private final HardwareMap hardwareMap;
  
  VuforiaTrackableDefaultListenerAccess(BlocksOpMode paramBlocksOpMode, String paramString, HardwareMap paramHardwareMap) {
    super(paramBlocksOpMode, paramString, "VuforiaTrackableDefaultListener");
    this.hardwareMap = paramHardwareMap;
  }
  
  private VuforiaTrackableDefaultListener checkVuforiaTrackableDefaultListener(Object paramObject) {
    return checkArg(paramObject, VuforiaTrackableDefaultListener.class, "vuforiaTrackableDefaultListener");
  }
  
  @JavascriptInterface
  public OpenGLMatrix getPose(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".getPose");
    paramObject = checkVuforiaTrackableDefaultListener(paramObject);
    return (paramObject != null) ? paramObject.getPose() : null;
  }
  
  @JavascriptInterface
  public String getRelicRecoveryVuMark(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".getRelicRecoveryVuMark");
    paramObject = checkVuforiaTrackableDefaultListener(paramObject);
    return (paramObject != null) ? RelicRecoveryVuMark.from((VuforiaTrackable.Listener)paramObject).toString() : RelicRecoveryVuMark.UNKNOWN.toString();
  }
  
  @JavascriptInterface
  public OpenGLMatrix getUpdatedRobotLocation(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".getUpdatedRobotLocation");
    paramObject = checkVuforiaTrackableDefaultListener(paramObject);
    return (paramObject != null) ? paramObject.getUpdatedRobotLocation() : null;
  }
  
  @JavascriptInterface
  public boolean isVisible(Object paramObject) {
    startBlockExecution(BlockType.FUNCTION, ".isVisible");
    paramObject = checkVuforiaTrackableDefaultListener(paramObject);
    return (paramObject != null) ? paramObject.isVisible() : false;
  }
  
  @JavascriptInterface
  public void setCameraLocationOnRobot(Object paramObject1, String paramString, Object paramObject2) {
    startBlockExecution(BlockType.FUNCTION, ".setCameraLocationOnRobot");
    paramObject1 = checkVuforiaTrackableDefaultListener(paramObject1);
    CameraName cameraName = checkCameraNameFromString(this.hardwareMap, paramString);
    paramObject2 = checkOpenGLMatrix(paramObject2);
    if (paramObject1 != null && cameraName != null && paramObject2 != null)
      paramObject1.setCameraLocationOnRobot(cameraName, (OpenGLMatrix)paramObject2); 
  }
  
  @JavascriptInterface
  public void setPhoneInformation(Object paramObject1, Object paramObject2, String paramString) {
    startBlockExecution(BlockType.FUNCTION, ".setPhoneInformation");
    paramObject1 = checkVuforiaTrackableDefaultListener(paramObject1);
    paramObject2 = checkOpenGLMatrix(paramObject2);
    VuforiaLocalizer.CameraDirection cameraDirection = checkVuforiaLocalizerCameraDirection(paramString);
    if (paramObject1 != null && paramObject2 != null && cameraDirection != null)
      paramObject1.setPhoneInformation((OpenGLMatrix)paramObject2, cameraDirection); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\VuforiaTrackableDefaultListenerAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */