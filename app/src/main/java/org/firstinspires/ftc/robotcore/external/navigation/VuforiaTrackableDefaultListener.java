package org.firstinspires.ftc.robotcore.external.navigation;

import com.vuforia.TrackableResult;
import com.vuforia.VuMarkTarget;
import com.vuforia.VuMarkTargetResult;
import java.util.HashMap;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraManager;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaPoseMatrix;

public class VuforiaTrackableDefaultListener implements VuforiaTrackable.Listener {
  public static final String TAG = "Vuforia";
  
  protected final CameraName cameraNameBack;
  
  protected final CameraName cameraNameFront;
  
  protected PoseAndCamera currentPoseAndCamera;
  
  protected final OpenGLMatrix ftcCameraBackFromPhone;
  
  protected final Map<CameraName, OpenGLMatrix> ftcCameraFromRobotCoords = new HashMap<CameraName, OpenGLMatrix>();
  
  protected final OpenGLMatrix ftcCameraFromVuforiaCamera;
  
  protected final OpenGLMatrix ftcCameraFrontFromPhone;
  
  protected PoseAndCamera lastTrackedPoseAndCamera;
  
  protected final Object lock = new Object();
  
  protected boolean newLocationAvailable;
  
  protected boolean newPoseAvailable;
  
  protected final OpenGLMatrix phoneFromFtcCameraBack;
  
  protected final OpenGLMatrix phoneFromFtcCameraFront;
  
  protected final OpenGLMatrix phoneFromVuforiaCameraBack;
  
  protected final OpenGLMatrix phoneFromVuforiaCameraFront;
  
  protected final Map<CameraName, OpenGLMatrix> robotFromFtcCameraCoords = new HashMap<CameraName, OpenGLMatrix>();
  
  protected VuforiaTrackable trackable;
  
  protected VuMarkInstanceId vuMarkInstanceId = null;
  
  protected final OpenGLMatrix vuforiaCameraFromFtcCamera;
  
  protected final OpenGLMatrix vuforiaCameraFrontFromVuforiaCameraBack;
  
  public VuforiaTrackableDefaultListener() {
    this(null);
  }
  
  public VuforiaTrackableDefaultListener(VuforiaTrackable paramVuforiaTrackable) {
    this.trackable = paramVuforiaTrackable;
    this.newPoseAvailable = false;
    this.newLocationAvailable = false;
    this.lastTrackedPoseAndCamera = null;
    this.currentPoseAndCamera = null;
    this.vuMarkInstanceId = null;
    this.phoneFromVuforiaCameraFront = new OpenGLMatrix(new float[] { 
          0.0F, 1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
          1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });
    this.phoneFromVuforiaCameraBack = new OpenGLMatrix(new float[] { 
          0.0F, -1.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
          -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });
    this.ftcCameraFromVuforiaCamera = new OpenGLMatrix(new float[] { 
          -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, -1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 
          1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F });
    this.vuforiaCameraFrontFromVuforiaCameraBack = this.phoneFromVuforiaCameraFront.inverted().multiplied(this.phoneFromVuforiaCameraBack);
    OpenGLMatrix openGLMatrix = this.ftcCameraFromVuforiaCamera.inverted();
    this.vuforiaCameraFromFtcCamera = openGLMatrix;
    this.phoneFromFtcCameraFront = this.phoneFromVuforiaCameraFront.multiplied(openGLMatrix);
    this.phoneFromFtcCameraBack = this.phoneFromVuforiaCameraBack.multiplied(this.vuforiaCameraFromFtcCamera);
    this.ftcCameraFrontFromPhone = this.phoneFromFtcCameraFront.inverted();
    this.ftcCameraBackFromPhone = this.phoneFromFtcCameraBack.inverted();
    CameraManager cameraManager = ClassFactory.getInstance().getCameraManager();
    this.cameraNameFront = cameraManager.nameFromCameraDirection(VuforiaLocalizer.CameraDirection.FRONT);
    this.cameraNameBack = cameraManager.nameFromCameraDirection(VuforiaLocalizer.CameraDirection.BACK);
  }
  
  public void addTrackable(VuforiaTrackable paramVuforiaTrackable) {
    synchronized (this.lock) {
      this.trackable = paramVuforiaTrackable;
      return;
    } 
  }
  
  @Deprecated
  public VuforiaLocalizer.CameraDirection getCameraDirection() {
    synchronized (this.lock) {
      if (this.currentPoseAndCamera != null)
        return this.currentPoseAndCamera.getCameraDirection(); 
      return null;
    } 
  }
  
  public OpenGLMatrix getCameraLocationOnRobot(CameraName paramCameraName) {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix2 = this.robotFromFtcCameraCoords.get(paramCameraName);
      OpenGLMatrix openGLMatrix1 = openGLMatrix2;
      if (openGLMatrix2 == null)
        openGLMatrix1 = OpenGLMatrix.identityMatrix(); 
      return openGLMatrix1;
    } 
  }
  
  public CameraName getCameraName() {
    synchronized (this.lock) {
      if (this.lastTrackedPoseAndCamera != null)
        return this.lastTrackedPoseAndCamera.cameraName; 
      return null;
    } 
  }
  
  protected OpenGLMatrix getFtcCameraFromRobot(CameraName paramCameraName) {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix2 = this.ftcCameraFromRobotCoords.get(this.currentPoseAndCamera.cameraName);
      OpenGLMatrix openGLMatrix1 = openGLMatrix2;
      if (openGLMatrix2 == null)
        openGLMatrix1 = OpenGLMatrix.identityMatrix(); 
      return openGLMatrix1;
    } 
  }
  
  public OpenGLMatrix getFtcCameraFromTarget() {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix = getVuforiaCameraFromTarget();
      if (openGLMatrix != null) {
        openGLMatrix = this.ftcCameraFromVuforiaCamera.multiplied(openGLMatrix);
        return openGLMatrix;
      } 
      return null;
    } 
  }
  
  public OpenGLMatrix getFtcFieldFromRobot() {
    synchronized (this.lock) {
      if (this.currentPoseAndCamera != null) {
        OpenGLMatrix openGLMatrix1 = getVuforiaCameraFromTarget();
        if (openGLMatrix1 != null) {
          OpenGLMatrix openGLMatrix2 = this.trackable.getFtcFieldFromTarget();
          openGLMatrix1 = openGLMatrix1.inverted();
          OpenGLMatrix openGLMatrix3 = getFtcCameraFromRobot(this.currentPoseAndCamera.cameraName);
          openGLMatrix2 = openGLMatrix2.multiplied(openGLMatrix1).multiplied(this.vuforiaCameraFromFtcCamera).multiplied(openGLMatrix3);
          return openGLMatrix2;
        } 
      } 
    } 
    OpenGLMatrix openGLMatrix = null;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return openGLMatrix;
  }
  
  public OpenGLMatrix getLastTrackedPoseVuforiaCamera() {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix;
      if (this.lastTrackedPoseAndCamera == null) {
        openGLMatrix = null;
      } else {
        openGLMatrix = this.lastTrackedPoseAndCamera.pose.toOpenGL();
      } 
      return openGLMatrix;
    } 
  }
  
  @Deprecated
  public OpenGLMatrix getLastTrackedRawPose() {
    return getLastTrackedPoseVuforiaCamera();
  }
  
  protected OpenGLMatrix getPhoneFromVuforiaCamera(VuforiaLocalizer.CameraDirection paramCameraDirection) {
    return getPoseCorrectionMatrix(paramCameraDirection);
  }
  
  @Deprecated
  public OpenGLMatrix getPhoneLocationOnRobot() {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix = this.robotFromFtcCameraCoords.get(this.cameraNameFront);
      if (openGLMatrix != null) {
        openGLMatrix = openGLMatrix.multiplied(this.ftcCameraFrontFromPhone);
        return openGLMatrix;
      } 
      openGLMatrix = this.robotFromFtcCameraCoords.get(this.cameraNameBack);
      if (openGLMatrix != null) {
        openGLMatrix = openGLMatrix.multiplied(this.ftcCameraBackFromPhone);
        return openGLMatrix;
      } 
      return null;
    } 
  }
  
  public OpenGLMatrix getPose() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getPosePhone : ()Lorg/firstinspires/ftc/robotcore/external/matrices/OpenGLMatrix;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  @Deprecated
  public OpenGLMatrix getPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection paramCameraDirection) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuforiaLocalizer$CameraDirection[paramCameraDirection.ordinal()];
    return (i != 1) ? ((i != 2) ? null : this.phoneFromFtcCameraBack) : this.phoneFromVuforiaCameraFront;
  }
  
  public OpenGLMatrix getPosePhone() {
    synchronized (this.lock) {
      OpenGLMatrix openGLMatrix = getVuforiaCameraFromTarget();
      if (openGLMatrix == null) {
        openGLMatrix = null;
      } else {
        openGLMatrix = getPhoneFromVuforiaCamera(this.currentPoseAndCamera.getCameraDirection()).multiplied(openGLMatrix);
      } 
      return openGLMatrix;
    } 
  }
  
  @Deprecated
  public OpenGLMatrix getRawPose() {
    return getVuforiaCameraFromTarget();
  }
  
  @Deprecated
  public OpenGLMatrix getRawUpdatedPose() {
    return getUpdatedVuforiaCameraFromTarget();
  }
  
  public OpenGLMatrix getRobotLocation() {
    return getFtcFieldFromRobot();
  }
  
  public OpenGLMatrix getUpdatedRobotLocation() {
    synchronized (this.lock) {
      if (this.newLocationAvailable) {
        this.newLocationAvailable = false;
        return getRobotLocation();
      } 
      return null;
    } 
  }
  
  public OpenGLMatrix getUpdatedVuforiaCameraFromTarget() {
    synchronized (this.lock) {
      if (this.newPoseAvailable) {
        this.newPoseAvailable = false;
        return getVuforiaCameraFromTarget();
      } 
      return null;
    } 
  }
  
  public VuMarkInstanceId getVuMarkInstanceId() {
    synchronized (this.lock) {
      return this.vuMarkInstanceId;
    } 
  }
  
  public OpenGLMatrix getVuforiaCameraFromTarget() {
    synchronized (this.lock) {
      if (this.currentPoseAndCamera != null)
        return this.currentPoseAndCamera.pose.toOpenGL(); 
      return null;
    } 
  }
  
  public boolean isVisible() {
    synchronized (this.lock) {
      if (this.currentPoseAndCamera != null)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return bool;
  }
  
  public void onNotTracked() {
    synchronized (this.lock) {
      this.currentPoseAndCamera = null;
      this.newPoseAvailable = true;
      this.newLocationAvailable = true;
      this.vuMarkInstanceId = null;
      return;
    } 
  }
  
  public void onTracked(TrackableResult paramTrackableResult, CameraName paramCameraName, Camera paramCamera, VuforiaTrackable paramVuforiaTrackable) {
    synchronized (this.lock) {
      PoseAndCamera poseAndCamera = new PoseAndCamera(new VuforiaPoseMatrix(paramTrackableResult.getPose()), paramCameraName);
      this.currentPoseAndCamera = poseAndCamera;
      this.newPoseAvailable = true;
      this.newLocationAvailable = true;
      this.lastTrackedPoseAndCamera = poseAndCamera;
      if (paramTrackableResult.isOfType(VuMarkTargetResult.getClassType()))
        this.vuMarkInstanceId = new VuMarkInstanceId(((VuMarkTarget)((VuMarkTargetResult)paramTrackableResult).getTrackable()).getInstanceId()); 
      return;
    } 
  }
  
  public void setCameraLocationOnRobot(CameraName paramCameraName, OpenGLMatrix paramOpenGLMatrix) {
    OpenGLMatrix openGLMatrix = paramOpenGLMatrix.inverted();
    synchronized (this.lock) {
      this.robotFromFtcCameraCoords.put(paramCameraName, paramOpenGLMatrix);
      this.ftcCameraFromRobotCoords.put(paramCameraName, openGLMatrix);
      return;
    } 
  }
  
  public void setPhoneInformation(OpenGLMatrix paramOpenGLMatrix, VuforiaLocalizer.CameraDirection paramCameraDirection) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuforiaLocalizer$CameraDirection[paramCameraDirection.ordinal()];
    if (i != 1) {
      if (i == 2) {
        setCameraLocationOnRobot(this.cameraNameBack, paramOpenGLMatrix.multiplied(this.phoneFromFtcCameraBack));
        return;
      } 
      throw Misc.illegalArgumentException("cameraDirection:%s", new Object[] { paramCameraDirection });
    } 
    setCameraLocationOnRobot(this.cameraNameFront, paramOpenGLMatrix.multiplied(this.phoneFromFtcCameraFront));
  }
  
  @Deprecated
  public void setPoseCorrectionMatrix(VuforiaLocalizer.CameraDirection paramCameraDirection, OpenGLMatrix paramOpenGLMatrix) {
    throw new UnsupportedOperationException("this method has no longer has any effect");
  }
  
  protected static class PoseAndCamera {
    public final CameraName cameraName;
    
    public final VuforiaPoseMatrix pose;
    
    public PoseAndCamera(VuforiaPoseMatrix param1VuforiaPoseMatrix, CameraName param1CameraName) {
      this.pose = param1VuforiaPoseMatrix;
      this.cameraName = param1CameraName;
    }
    
    public VuforiaLocalizer.CameraDirection getCameraDirection() {
      CameraName cameraName = this.cameraName;
      return (cameraName instanceof BuiltinCameraName) ? ((BuiltinCameraName)cameraName).getCameraDirection() : VuforiaLocalizer.CameraDirection.UNKNOWN;
    }
    
    public String toString() {
      return Misc.formatForUser("PoseAndCamera(%s|%s)", new Object[] { this.pose, this.cameraName });
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaTrackableDefaultListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */