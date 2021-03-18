package com.google.blocks.ftcrobotcontroller.runtime;

import android.util.Pair;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Locale;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;

abstract class Access {
  protected static final String DEFAULT_CAMERA_MONTIOR_FEEDBACK_STRING = "DEFAULT";
  
  protected final String blockFirstName;
  
  protected final BlocksOpMode blocksOpMode;
  
  private final String identifier;
  
  protected Access(BlocksOpMode paramBlocksOpMode, String paramString1, String paramString2) {
    this.blocksOpMode = paramBlocksOpMode;
    this.identifier = paramString1;
    this.blockFirstName = paramString2;
  }
  
  private CameraName checkCameraName(Object paramObject) {
    return checkArg(paramObject, CameraName.class, "cameraName");
  }
  
  private final String getTypeFromClass(Class<?> paramClass) {
    return BNO055IMU.Parameters.class.isAssignableFrom(paramClass) ? "IMU-BNO055.Parameters" : (VuforiaLocalizer.Parameters.class.isAssignableFrom(paramClass) ? "VuforiaLocalizer.Parameters" : paramClass.getSimpleName());
  }
  
  private final void reportWarning(String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    RobotLog.ww("Blocks", paramString);
    RobotLog.addGlobalWarningMessage(paramString);
  }
  
  protected CameraName cameraNameFromString(HardwareMap paramHardwareMap, String paramString) {
    return (CameraName)paramHardwareMap.tryGet(WebcamName.class, paramString);
  }
  
  protected String cameraNameToString(HardwareMap paramHardwareMap, CameraName paramCameraName) {
    if (paramCameraName instanceof HardwareDevice) {
      Set<String> set = paramHardwareMap.getNamesOf((HardwareDevice)paramCameraName);
      if (!set.isEmpty())
        return set.iterator().next(); 
    } 
    return "";
  }
  
  protected AngleUnit checkAngleUnit(String paramString) {
    return checkArg(paramString, AngleUnit.class, "angleUnit");
  }
  
  protected final <T extends Enum<T>> T checkArg(String paramString1, Class<T> paramClass, String paramString2) {
    if (paramString1 == null) {
      reportInvalidArg(paramString2, paramClass.getSimpleName());
      return null;
    } 
    try {
      return (T)Enum.valueOf((Class)paramClass, paramString1);
    } catch (IllegalArgumentException illegalArgumentException) {
      try {
        return (T)Enum.valueOf((Class)paramClass, paramString1.toUpperCase(Locale.ENGLISH));
      } catch (IllegalArgumentException illegalArgumentException1) {
        reportInvalidArg(paramString2, paramClass.getSimpleName());
        return null;
      } 
    } 
  }
  
  protected final <T> T checkArg(Object paramObject, Class<T> paramClass, String paramString) {
    if (!paramClass.isInstance(paramObject)) {
      reportInvalidArg(paramString, getTypeFromClass(paramClass));
      return null;
    } 
    return paramClass.cast(paramObject);
  }
  
  protected AxesOrder checkAxesOrder(String paramString) {
    return checkArg(paramString, AxesOrder.class, "axesOrder");
  }
  
  protected AxesReference checkAxesReference(String paramString) {
    return checkArg(paramString, AxesReference.class, "axesReference");
  }
  
  protected BNO055IMU.Parameters checkBNO055IMUParameters(Object paramObject) {
    return checkArg(paramObject, BNO055IMU.Parameters.class, "parameters");
  }
  
  protected RevBlinkinLedDriver.BlinkinPattern checkBlinkinPattern(String paramString) {
    return checkArg(paramString, RevBlinkinLedDriver.BlinkinPattern.class, "blinkinPattern");
  }
  
  protected Pair<Boolean, VuforiaLocalizer.Parameters.CameraMonitorFeedback> checkCameraMonitorFeedback(String paramString) {
    VuforiaLocalizer.Parameters.CameraMonitorFeedback cameraMonitorFeedback;
    boolean bool1 = paramString.equalsIgnoreCase("DEFAULT");
    boolean bool = true;
    if (bool1) {
      paramString = null;
    } else {
      cameraMonitorFeedback = checkArg(paramString, VuforiaLocalizer.Parameters.CameraMonitorFeedback.class, "cameraMonitorFeedback");
      if (cameraMonitorFeedback == null)
        bool = false; 
    } 
    return Pair.create(Boolean.valueOf(bool), cameraMonitorFeedback);
  }
  
  protected CameraName checkCameraNameFromString(HardwareMap paramHardwareMap, String paramString) {
    return checkCameraName(cameraNameFromString(paramHardwareMap, paramString));
  }
  
  protected DistanceUnit checkDistanceUnit(String paramString) {
    return checkArg(paramString, DistanceUnit.class, "distanceUnit");
  }
  
  protected MatrixF checkMatrixF(Object paramObject) {
    return checkArg(paramObject, MatrixF.class, "matrix");
  }
  
  protected OpenGLMatrix checkOpenGLMatrix(Object paramObject) {
    return checkArg(paramObject, OpenGLMatrix.class, "matrix");
  }
  
  protected VectorF checkVectorF(Object paramObject) {
    return checkArg(paramObject, VectorF.class, "vector");
  }
  
  protected VuforiaLocalizer.CameraDirection checkVuforiaLocalizerCameraDirection(String paramString) {
    return checkArg(paramString, VuforiaLocalizer.CameraDirection.class, "cameraDirection");
  }
  
  protected VuforiaLocalizer.Parameters checkVuforiaLocalizerParameters(Object paramObject) {
    return checkArg(paramObject, VuforiaLocalizer.Parameters.class, "vuforiaLocalizerParameters");
  }
  
  protected VuforiaTrackable checkVuforiaTrackable(Object paramObject) {
    return checkArg(paramObject, VuforiaTrackable.class, "vuforiaTrackable");
  }
  
  void close() {}
  
  protected final void reportHardwareError(String paramString) {
    reportWarning("Error while initializing hardware items. %s", new Object[] { this.blocksOpMode.getFullBlockLabel(), paramString });
  }
  
  protected final void reportInvalidArg(String paramString1, String paramString2) {
    if (paramString1 != null && !paramString1.isEmpty()) {
      reportWarning("Incorrect block plugged into the %s socket of the block labeled \"%s\". Expected %s.", new Object[] { paramString1, this.blocksOpMode.getFullBlockLabel(), paramString2 });
      return;
    } 
    reportWarning("Incorrect block plugged into a socket of the block labeled \"%s\". Expected %s.", new Object[] { this.blocksOpMode.getFullBlockLabel(), paramString2 });
  }
  
  protected final void reportWarning(String paramString) {
    reportWarning("Warning while executing the block labeled \"%s\". %s", new Object[] { this.blocksOpMode.getFullBlockLabel(), paramString });
  }
  
  protected final void startBlockExecution(BlockType paramBlockType, String paramString) {
    this.blocksOpMode.startBlockExecution(paramBlockType, this.blockFirstName, paramString);
  }
  
  protected final void startBlockExecution(BlockType paramBlockType, String paramString1, String paramString2) {
    this.blocksOpMode.startBlockExecution(paramBlockType, paramString1, paramString2);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\Access.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */