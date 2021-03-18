package org.firstinspires.ftc.robotcore.external.navigation;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Looper;
import android.text.TextUtils;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.SwitchableCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.MatrixF;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class VuforiaBase {
  private static final String LICENSE_KEY_FILE = "CzechWolf";
  
  public static final float MM_FTC_FIELD_WIDTH = 3606.8F;
  
  public static final float MM_PER_INCH = 25.4F;
  
  private final String assetName;
  
  private final Map<String, VuforiaTrackableDefaultListener> listenerMap = new HashMap<String, VuforiaTrackableDefaultListener>();
  
  private final Map<String, OpenGLMatrix> locationMap = new HashMap<String, OpenGLMatrix>();
  
  private final Map<String, OpenGLMatrix> locationsOnField = new HashMap<String, OpenGLMatrix>();
  
  private final Map<String, OpenGLMatrix> poseMap = new HashMap<String, OpenGLMatrix>();
  
  private final String[] trackableNames;
  
  private volatile VuforiaLocalizer vuforiaLocalizer;
  
  private volatile VuforiaTrackables vuforiaTrackables;
  
  protected VuforiaBase(String paramString, String[] paramArrayOfString, Map<String, OpenGLMatrix> paramMap) {
    this.assetName = paramString;
    this.trackableNames = paramArrayOfString;
    this.locationsOnField.putAll(paramMap);
  }
  
  protected VuforiaBase(String paramString, String[] paramArrayOfString, OpenGLMatrix[] paramArrayOfOpenGLMatrix) {
    this.assetName = paramString;
    this.trackableNames = paramArrayOfString;
    int i;
    for (i = 0; i < paramArrayOfString.length; i++) {
      paramString = paramArrayOfString[i];
      this.locationsOnField.put(paramString, paramArrayOfOpenGLMatrix[i]);
    } 
  }
  
  public static VuforiaLocalizer.Parameters createParameters() {
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
    AssetManager assetManager = AppUtil.getInstance().getRootActivity().getAssets();
    try {
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open("CzechWolf")));
      try {
        parameters.vuforiaLicenseKey = bufferedReader.readLine();
        return parameters;
      } finally {
        parameters = null;
      } 
    } catch (IOException iOException) {
      throw new RuntimeException("Failed to read vuforia license key from asset.", iOException);
    } 
  }
  
  private VuforiaLocalizer.Parameters createParametersWithoutCamera(String paramString, boolean paramBoolean1, boolean paramBoolean2, VuforiaLocalizer.Parameters.CameraMonitorFeedback paramCameraMonitorFeedback) {
    VuforiaLocalizer.Parameters parameters = createParameters();
    if (paramString.length() >= 217)
      parameters.vuforiaLicenseKey = paramString; 
    parameters.useExtendedTracking = paramBoolean1;
    parameters.cameraMonitorFeedback = paramCameraMonitorFeedback;
    if (paramBoolean2) {
      Activity activity = AppUtil.getInstance().getRootActivity();
      parameters.cameraMonitorViewIdParent = activity.getResources().getIdentifier("cameraMonitorViewId", "id", activity.getPackageName());
    } 
    return parameters;
  }
  
  public static CameraName getSwitchableCamera(HardwareMap paramHardwareMap) {
    List<CameraName> list = paramHardwareMap.getAll(WebcamName.class);
    CameraName[] arrayOfCameraName = new CameraName[list.size()];
    for (int i = 0; i < list.size(); i++)
      arrayOfCameraName[i] = list.get(i); 
    return (CameraName)ClassFactory.getInstance().getCameraManager().nameForSwitchableCamera(arrayOfCameraName);
  }
  
  private void initTrackable(VuforiaTrackable paramVuforiaTrackable, String paramString, OpenGLMatrix paramOpenGLMatrix1, OpenGLMatrix paramOpenGLMatrix2, VuforiaLocalizer.Parameters paramParameters) {
    paramVuforiaTrackable.setName(paramString);
    if (paramOpenGLMatrix1 != null)
      paramVuforiaTrackable.setLocation(paramOpenGLMatrix1); 
    VuforiaTrackableDefaultListener vuforiaTrackableDefaultListener = (VuforiaTrackableDefaultListener)paramVuforiaTrackable.getListener();
    if (paramParameters.cameraName != null && !paramParameters.cameraName.isUnknown()) {
      vuforiaTrackableDefaultListener.setCameraLocationOnRobot(paramParameters.cameraName, paramOpenGLMatrix2);
    } else {
      vuforiaTrackableDefaultListener.setPhoneInformation(paramOpenGLMatrix2, paramParameters.cameraDirection);
    } 
    this.listenerMap.put(paramString.toUpperCase(Locale.ENGLISH), vuforiaTrackableDefaultListener);
  }
  
  private void initialize(final VuforiaLocalizer.Parameters parameters, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean) {
    OpenGLMatrix openGLMatrix = OpenGLMatrix.translation(paramFloat1, paramFloat2, paramFloat3).multiplied(Orientation.getRotationMatrix(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, paramFloat4, paramFloat5, paramFloat6));
    Looper looper = Looper.myLooper();
    if (looper != null && !looper.equals(Looper.getMainLooper())) {
      final AtomicReference<VuforiaLocalizer> vuforiaLocalizerReference = new AtomicReference();
      final AtomicReference<RuntimeException> exceptionReference = new AtomicReference();
      Thread thread = new Thread(new Runnable() {
            public void run() {
              try {
                vuforiaLocalizerReference.set(ClassFactory.getInstance().createVuforia(parameters));
                return;
              } catch (RuntimeException runtimeException) {
                exceptionReference.set(runtimeException);
                return;
              } 
            }
          });
      thread.start();
      try {
        thread.join();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
      RuntimeException runtimeException = atomicReference1.get();
      if (runtimeException == null) {
        this.vuforiaLocalizer = atomicReference.get();
      } else {
        runtimeException.fillInStackTrace();
        throw runtimeException;
      } 
    } else {
      this.vuforiaLocalizer = ClassFactory.getInstance().createVuforia(parameters);
    } 
    this.vuforiaTrackables = this.vuforiaLocalizer.loadTrackablesFromAsset(this.assetName);
    int i;
    for (i = 0; i < this.trackableNames.length; i++) {
      OpenGLMatrix openGLMatrix1;
      VuforiaTrackable vuforiaTrackable = this.vuforiaTrackables.get(i);
      String str = this.trackableNames[i];
      if (paramBoolean) {
        openGLMatrix1 = this.locationsOnField.get(str);
      } else {
        openGLMatrix1 = OpenGLMatrix.rotation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES, 90.0F, 0.0F, -90.0F);
      } 
      initTrackable(vuforiaTrackable, str, openGLMatrix1, openGLMatrix, parameters);
    } 
  }
  
  public void activate() {
    if (this.vuforiaTrackables != null) {
      this.vuforiaTrackables.activate();
      return;
    } 
    throw new IllegalStateException("You forgot to call Vuforia.initialize!");
  }
  
  public void close() {
    if (this.vuforiaTrackables != null) {
      this.vuforiaTrackables.deactivate();
      this.vuforiaTrackables = null;
    } 
    this.listenerMap.clear();
    this.locationMap.clear();
    this.poseMap.clear();
  }
  
  public void deactivate() {
    if (this.vuforiaTrackables != null) {
      this.vuforiaTrackables.deactivate();
      return;
    } 
    throw new IllegalStateException("You forgot to call Vuforia.initialize!");
  }
  
  public TrackingResults emptyTrackingResults(String paramString) {
    return new TrackingResults(paramString);
  }
  
  protected VuforiaTrackableDefaultListener getListener(String paramString) {
    return this.listenerMap.get(paramString.toUpperCase(Locale.ENGLISH));
  }
  
  public VuforiaLocalizer getVuforiaLocalizer() {
    if (this.vuforiaLocalizer != null)
      return this.vuforiaLocalizer; 
    throw new IllegalStateException("You forgot to call Vuforia.initialize!");
  }
  
  public void initialize(String paramString1, CameraName paramCameraName, String paramString2, boolean paramBoolean1, boolean paramBoolean2, VuforiaLocalizer.Parameters.CameraMonitorFeedback paramCameraMonitorFeedback, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean3) {
    VuforiaLocalizer.Parameters parameters = createParametersWithoutCamera(paramString1, paramBoolean1, paramBoolean2, paramCameraMonitorFeedback);
    parameters.cameraName = paramCameraName;
    if (!TextUtils.isEmpty(paramString2))
      parameters.addWebcamCalibrationFile(paramString2); 
    initialize(parameters, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean3);
  }
  
  public void initialize(String paramString, VuforiaLocalizer.CameraDirection paramCameraDirection, boolean paramBoolean1, boolean paramBoolean2, VuforiaLocalizer.Parameters.CameraMonitorFeedback paramCameraMonitorFeedback, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, boolean paramBoolean3) {
    VuforiaLocalizer.Parameters parameters = createParametersWithoutCamera(paramString, paramBoolean1, paramBoolean2, paramCameraMonitorFeedback);
    parameters.cameraDirection = paramCameraDirection;
    initialize(parameters, paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramBoolean3);
  }
  
  public String printTrackableNames() {
    StringBuilder stringBuilder = new StringBuilder();
    String[] arrayOfString = this.trackableNames;
    int j = arrayOfString.length;
    String str = "";
    int i = 0;
    while (i < j) {
      String str1 = arrayOfString[i];
      stringBuilder.append(str);
      stringBuilder.append(str1);
      i++;
      str = ", ";
    } 
    return stringBuilder.toString();
  }
  
  public void setActiveCamera(CameraName paramCameraName) {
    Camera camera = getVuforiaLocalizer().getCamera();
    if (camera instanceof SwitchableCamera)
      ((SwitchableCamera)camera).setActiveCamera(paramCameraName); 
  }
  
  public TrackingResults track(String paramString) {
    if (this.vuforiaTrackables != null) {
      VuforiaTrackableDefaultListener vuforiaTrackableDefaultListener = getListener(paramString);
      if (vuforiaTrackableDefaultListener != null) {
        boolean bool;
        OpenGLMatrix openGLMatrix = vuforiaTrackableDefaultListener.getUpdatedRobotLocation();
        if (openGLMatrix != null) {
          bool = true;
          this.locationMap.put(paramString, openGLMatrix);
        } else {
          bool = false;
          openGLMatrix = this.locationMap.get(paramString);
        } 
        return new TrackingResults(paramString, vuforiaTrackableDefaultListener.isVisible(), bool, openGLMatrix);
      } 
      throw new IllegalArgumentException("name");
    } 
    throw new IllegalStateException("You forgot to call Vuforia.initialize!");
  }
  
  public TrackingResults trackPose(String paramString) {
    if (this.vuforiaTrackables != null) {
      VuforiaTrackableDefaultListener vuforiaTrackableDefaultListener = getListener(paramString);
      if (vuforiaTrackableDefaultListener != null) {
        OpenGLMatrix openGLMatrix = vuforiaTrackableDefaultListener.getPose();
        if (openGLMatrix != null) {
          this.poseMap.put(paramString, openGLMatrix);
        } else {
          openGLMatrix = this.poseMap.get(paramString);
        } 
        return new TrackingResults(paramString, vuforiaTrackableDefaultListener.isVisible(), false, openGLMatrix);
      } 
      throw new IllegalArgumentException("name");
    } 
    throw new IllegalStateException("You forgot to call Vuforia.initialize!");
  }
  
  public static class TrackingResults {
    public boolean isUpdatedRobotLocation;
    
    public boolean isVisible;
    
    public OpenGLMatrix matrix;
    
    public String name;
    
    public float x;
    
    public float xAngle;
    
    public float y;
    
    public float yAngle;
    
    public float z;
    
    public float zAngle;
    
    TrackingResults(String param1String) {
      this.name = param1String;
    }
    
    TrackingResults(String param1String, boolean param1Boolean1, boolean param1Boolean2, OpenGLMatrix param1OpenGLMatrix) {
      this.name = param1String;
      this.isVisible = param1Boolean1;
      this.isUpdatedRobotLocation = param1Boolean2;
      this.matrix = param1OpenGLMatrix;
      if (param1OpenGLMatrix != null) {
        VectorF vectorF = param1OpenGLMatrix.getTranslation();
        Orientation orientation = Orientation.getOrientation((MatrixF)param1OpenGLMatrix, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);
        this.x = vectorF.get(0);
        this.y = vectorF.get(1);
        this.z = vectorF.get(2);
        this.xAngle = orientation.firstAngle;
        this.yAngle = orientation.secondAngle;
        this.zAngle = orientation.thirdAngle;
      } 
    }
    
    protected TrackingResults(TrackingResults param1TrackingResults) {
      this(param1TrackingResults.name, param1TrackingResults.isVisible, param1TrackingResults.isUpdatedRobotLocation, param1TrackingResults.matrix);
    }
    
    public String formatAsTransform() {
      OpenGLMatrix openGLMatrix = this.matrix;
      return (openGLMatrix != null) ? openGLMatrix.formatAsTransform() : "";
    }
    
    public String toJson() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("{ \"Name\":\"");
      stringBuilder.append(this.name);
      stringBuilder.append("\", \"IsVisible\":");
      stringBuilder.append(this.isVisible);
      stringBuilder.append(", \"IsUpdatedRobotLocation\":");
      stringBuilder.append(this.isUpdatedRobotLocation);
      stringBuilder.append(", \"X\":");
      stringBuilder.append(this.x);
      stringBuilder.append(", \"Y\":");
      stringBuilder.append(this.y);
      stringBuilder.append(", \"Z\":");
      stringBuilder.append(this.z);
      stringBuilder.append(", \"XAngle\":");
      stringBuilder.append(this.xAngle);
      stringBuilder.append(", \"YAngle\":");
      stringBuilder.append(this.yAngle);
      stringBuilder.append(", \"ZAngle\":");
      stringBuilder.append(this.zAngle);
      stringBuilder.append(" }");
      return stringBuilder.toString();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\navigation\VuforiaBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */