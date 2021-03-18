package org.firstinspires.ftc.robotcore.internal.vuforia;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.vuforia.CameraCalibration;
import com.vuforia.CameraDevice;
import com.vuforia.DataSet;
import com.vuforia.Device;
import com.vuforia.Frame;
import com.vuforia.GLTextureUnit;
import com.vuforia.Image;
import com.vuforia.Matrix44F;
import com.vuforia.Mesh;
import com.vuforia.ObjectTargetResult;
import com.vuforia.ObjectTracker;
import com.vuforia.Renderer;
import com.vuforia.RenderingPrimitives;
import com.vuforia.RotationalDeviceTracker;
import com.vuforia.State;
import com.vuforia.TextureUnit;
import com.vuforia.Tool;
import com.vuforia.Trackable;
import com.vuforia.TrackableResult;
import com.vuforia.TrackerManager;
import com.vuforia.Vec2I;
import com.vuforia.VideoBackgroundConfig;
import com.vuforia.VideoMode;
import com.vuforia.Vuforia;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamSource;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.delegating.SwitchableCameraName;
import org.firstinspires.ftc.robotcore.internal.collections.EvictingBlockingQueue;
import org.firstinspires.ftc.robotcore.internal.opengl.AutoConfigGLSurfaceView;
import org.firstinspires.ftc.robotcore.internal.opengl.Texture;
import org.firstinspires.ftc.robotcore.internal.opengl.models.MeshObject;
import org.firstinspires.ftc.robotcore.internal.opengl.models.SavedMeshObject;
import org.firstinspires.ftc.robotcore.internal.opengl.models.SolidCylinder;
import org.firstinspires.ftc.robotcore.internal.opengl.models.Teapot;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.CubeMeshProgram;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.PositionAttributeShader;
import org.firstinspires.ftc.robotcore.internal.opengl.shaders.SimpleColorProgram;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcam;
import org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider.VuforiaWebcamInternal;

public class VuforiaLocalizerImpl implements VuforiaLocalizer {
  protected static final float PROJ_MAT_FAR_PLANE = 5000.0F;
  
  protected static final float PROJ_MAT_NEAR_PLANE = 10.0F;
  
  public static final String TAG = "Vuforia";
  
  public static boolean TRACE = true;
  
  protected final Activity activity;
  
  protected AppUtil appUtil;
  
  protected Continuation<? extends Consumer<Bitmap>> bitmapContinuation;
  
  protected final Object bitmapFrameLock;
  
  protected SavedMeshObject buildingsModel;
  
  protected float buildingsScale;
  
  protected Texture buildingsTexture;
  
  protected int callbackCount;
  
  protected CameraCalibration camCal;
  
  protected VuforiaLocalizer.Parameters.CameraMonitorFeedback cameraCameraMonitorFeedback;
  
  protected CoordinateAxes coordinateAxes;
  
  protected CubeMeshProgram cubeMeshProgram;
  
  protected boolean fillSurfaceParent;
  
  protected BlockingQueue<VuforiaLocalizer.CloseableFrame> frameQueue;
  
  protected int frameQueueCapacity;
  
  protected final Object frameQueueLock;
  
  protected Continuation<? extends Consumer<Frame>> getFrameOnce;
  
  protected AutoConfigGLSurfaceView glSurface;
  
  protected ViewGroup glSurfaceParent;
  
  protected int glSurfaceParentPreviousVisibility;
  
  protected GLSurfaceViewRenderer glSurfaceViewRenderer;
  
  protected boolean invertAspectRatio;
  
  protected boolean isCameraInited;
  
  protected boolean isCameraRunning;
  
  protected boolean isCameraStarted;
  
  protected boolean isExtendedTrackingActive;
  
  protected LifeCycleCallbacks lifeCycleCallbacks;
  
  protected final List<VuforiaTrackablesImpl> loadedTrackableSets;
  
  protected View loadingIndicator;
  
  protected RelativeLayout loadingIndicatorOverlay;
  
  protected OpModeManagerImpl opModeManager;
  
  protected OpModeNotifications opModeNotifications;
  
  protected VuforiaLocalizer.Parameters parameters;
  
  protected Matrix44F projectionMatrix;
  
  protected int renderCount;
  
  protected Renderer renderer;
  
  protected boolean rendererIsActive;
  
  protected CloseableRenderingPrimitives renderingPrimitives;
  
  protected final Object renderingPrimitivesMutex;
  
  protected SimpleColorProgram simpleColorProgram;
  
  protected final Object startStopLock;
  
  protected int surfaceRotation;
  
  protected Teapot teapot;
  
  protected float teapotScale;
  
  protected Texture teapotTexture;
  
  protected List<Texture> textures;
  
  protected Tracer tracer;
  
  protected final Object updateCallbackLock;
  
  protected volatile ViewPort viewport;
  
  protected VuforiaCallback vuforiaCallback;
  
  protected int vuforiaFlags;
  
  protected VuforiaInitPhase vuforiaInitPhase;
  
  protected final VuforiaWebcamInternal vuforiaWebcam;
  
  protected boolean wantCamera;
  
  public VuforiaLocalizerImpl(VuforiaLocalizer.Parameters paramParameters) {
    Activity activity;
    this.tracer = Tracer.create("Vuforia", TRACE);
    this.lifeCycleCallbacks = new LifeCycleCallbacks();
    this.opModeManager = null;
    this.opModeNotifications = new OpModeNotifications();
    this.vuforiaCallback = new VuforiaCallback();
    this.glSurfaceViewRenderer = new GLSurfaceViewRenderer();
    this.appUtil = AppUtil.getInstance();
    this.parameters = null;
    this.vuforiaFlags = 0;
    this.wantCamera = false;
    this.isCameraInited = false;
    this.isCameraStarted = false;
    this.isCameraRunning = false;
    this.camCal = null;
    this.vuforiaInitPhase = VuforiaInitPhase.Nascent;
    this.glSurfaceParent = null;
    this.glSurfaceParentPreviousVisibility = 0;
    this.glSurface = null;
    this.fillSurfaceParent = false;
    this.cameraCameraMonitorFeedback = null;
    this.loadingIndicatorOverlay = null;
    this.loadingIndicator = null;
    this.renderer = null;
    this.rendererIsActive = false;
    this.renderingPrimitivesMutex = new Object();
    this.renderingPrimitives = null;
    this.startStopLock = new Object();
    this.viewport = null;
    this.projectionMatrix = null;
    this.cubeMeshProgram = null;
    this.simpleColorProgram = null;
    this.textures = null;
    this.teapotTexture = null;
    this.teapot = null;
    this.teapotScale = 3.0F;
    this.coordinateAxes = new CoordinateAxes();
    this.buildingsTexture = null;
    this.buildingsModel = null;
    this.buildingsScale = 12.0F;
    this.updateCallbackLock = new Object();
    this.loadedTrackableSets = new LinkedList<VuforiaTrackablesImpl>();
    this.isExtendedTrackingActive = false;
    this.frameQueueLock = new Object();
    this.getFrameOnce = null;
    this.bitmapFrameLock = new Object();
    this.renderCount = 0;
    this.callbackCount = 0;
    this.invertAspectRatio = false;
    this.parameters = paramParameters;
    if (paramParameters.activity == null) {
      activity = this.appUtil.getActivity();
    } else {
      activity = paramParameters.activity;
    } 
    this.activity = activity;
    this.isExtendedTrackingActive = paramParameters.useExtendedTracking;
    VuforiaLocalizer.Parameters.CameraMonitorFeedback cameraMonitorFeedback = paramParameters.cameraMonitorFeedback;
    this.cameraCameraMonitorFeedback = cameraMonitorFeedback;
    if (cameraMonitorFeedback == null) {
      if (this.isExtendedTrackingActive) {
        cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.BUILDINGS;
      } else {
        cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
      } 
      this.cameraCameraMonitorFeedback = cameraMonitorFeedback;
    } 
    this.vuforiaWebcam = createVuforiaWebcam();
    this.fillSurfaceParent = paramParameters.fillCameraMonitorViewParent;
    setFrameQueueCapacity(0);
    registerLifeCycleCallbacks();
    if (paramParameters.cameraMonitorViewParent != null) {
      setMonitorViewParent(paramParameters.cameraMonitorViewParent);
    } else {
      setMonitorViewParent(paramParameters.cameraMonitorViewIdParent);
    } 
    makeLoadingIndicator();
    loadTextures();
    startAR();
    CameraStreamServer.getInstance().setSource((CameraStreamSource)this);
  }
  
  protected static ObjectTracker getObjectTracker() {
    return (ObjectTracker)TrackerManager.getInstance().getTracker(ObjectTracker.getClassType());
  }
  
  protected static RotationalDeviceTracker getRotationalDeviceTracker() {
    return (RotationalDeviceTracker)TrackerManager.getInstance().getTracker(RotationalDeviceTracker.getClassType());
  }
  
  protected static void throwFailure() {
    throwFailure("Vuforia operation failed", new Object[0]);
  }
  
  protected static void throwFailure(Exception paramException) {
    throw new VuforiaException(paramException, "Vuforia operation failed", new Object[0]);
  }
  
  protected static void throwFailure(String paramString, Object... paramVarArgs) {
    throw new VuforiaException(paramString, paramVarArgs);
  }
  
  protected static void throwIfFail(boolean paramBoolean) {
    if (!paramBoolean)
      throwFailure(); 
  }
  
  protected static void throwIfFail(boolean paramBoolean, String paramString, Object... paramVarArgs) {
    if (!paramBoolean)
      throwFailure(paramString, paramVarArgs); 
  }
  
  protected void adjustExtendedTracking() {
    synchronized (this.loadedTrackableSets) {
      Iterator<VuforiaTrackablesImpl> iterator = this.loadedTrackableSets.iterator();
      while (iterator.hasNext())
        ((VuforiaTrackablesImpl)iterator.next()).adjustExtendedTracking(this.isExtendedTrackingActive); 
      return;
    } 
  }
  
  protected boolean advanceInitPhase(Supplier<Boolean> paramSupplier) {
    try {
      boolean bool = ((Boolean)paramSupplier.get()).booleanValue();
      if (bool)
        this.vuforiaInitPhase = this.vuforiaInitPhase.next(); 
      return bool;
    } catch (VuforiaException vuforiaException) {
      this.tracer.traceError(vuforiaException, "exception in vuforia initialization: phase:%s", new Object[] { this.vuforiaInitPhase });
      throw vuforiaException;
    } catch (Exception exception) {
      this.tracer.traceError(exception, "exception in vuforia initialization: phase:%s", new Object[] { this.vuforiaInitPhase });
      throw exception;
    } finally {}
    throw paramSupplier;
  }
  
  protected boolean buildingsRequired() {
    return (this.parameters.cameraMonitorFeedback == VuforiaLocalizer.Parameters.CameraMonitorFeedback.BUILDINGS);
  }
  
  protected void close() {
    stopAR();
    removeLoadingIndicator();
    unregisterLifeCycleCallbacks();
  }
  
  protected void configureVideoBackground(boolean paramBoolean) {
    AutoConfigGLSurfaceView autoConfigGLSurfaceView = this.glSurface;
    if (autoConfigGLSurfaceView == null)
      return; 
    int i = autoConfigGLSurfaceView.getWidth();
    int j = this.glSurface.getHeight();
    VideoMode videoMode = CameraDevice.getInstance().getVideoMode(-1);
    double d2 = videoMode.getWidth() / videoMode.getHeight();
    ViewPort viewPort = new ViewPort();
    double d1 = d2;
    if (this.invertAspectRatio)
      d1 = 1.0D / d2; 
    d2 = j * d1;
    double d3 = i;
    if (d2 < d3) {
      viewPort.extent.y = j;
      viewPort.extent.x = (int)Math.round(d2);
    } else {
      viewPort.extent.x = i;
      viewPort.extent.y = (int)Math.round(d3 / d1);
    } 
    VideoBackgroundConfig videoBackgroundConfig = new VideoBackgroundConfig();
    videoBackgroundConfig.setEnabled(true);
    videoBackgroundConfig.setPosition(new Vec2I(3, 3));
    videoBackgroundConfig.setSize(new Vec2I(viewPort.extent.x, viewPort.extent.y));
    viewPort.lowerLeft.x = (i - viewPort.extent.x) / 2 + videoBackgroundConfig.getPosition().getData()[0];
    viewPort.lowerLeft.y = (j - viewPort.extent.y) / 2 + videoBackgroundConfig.getPosition().getData()[1];
    Renderer.getInstance().setVideoBackgroundConfig(videoBackgroundConfig);
    this.viewport = viewPort;
    if (paramBoolean)
      this.appUtil.runOnUiThread(new Runnable() {
            public void run() {
              if (VuforiaLocalizerImpl.this.glSurface != null)
                VuforiaLocalizerImpl.this.glSurface.setVisibility(0); 
            }
          }); 
  }
  
  public Bitmap convertFrameToBitmap(Frame paramFrame) {
    for (int i = 0; i < 2; i++) {
      (new int[2])[0] = 1;
      (new int[2])[1] = 16;
      int k = (new int[2])[i];
      for (int j = 0; j < paramFrame.getNumImages(); j++) {
        Image image = paramFrame.getImage(j);
        if (image.getFormat() == k) {
          Bitmap.Config config;
          if (k != 1) {
            if (k != 16)
              continue; 
            config = Bitmap.Config.ARGB_8888;
          } else {
            config = Bitmap.Config.RGB_565;
          } 
          Bitmap bitmap = Bitmap.createBitmap(image.getWidth(), image.getHeight(), config);
          bitmap.copyPixelsFromBuffer(image.getPixels());
          return bitmap;
        } 
        continue;
      } 
    } 
    return null;
  }
  
  protected VuforiaWebcamInternal createVuforiaWebcam() {
    if (this.parameters.camera != null) {
      this.tracer.trace("using camera by instance: %s", new Object[] { this.parameters.camera.getCameraName() });
      return (VuforiaWebcamInternal)new VuforiaWebcam(this.parameters.webcamCalibrationResources, this.parameters.webcamCalibrationFiles, this.parameters.minWebcamAspectRatio, this.parameters.maxWebcamAspectRatio, this.parameters.secondsUsbPermissionTimeout, this.parameters.camera);
    } 
    if (this.parameters.cameraName != null) {
      if (this.parameters.cameraName.isWebcam() || (this.parameters.cameraName.isSwitchable() && ((SwitchableCameraName)this.parameters.cameraName).allMembersAreWebcams())) {
        this.tracer.trace("using camera by name: %s", new Object[] { this.parameters.cameraName });
        return (VuforiaWebcamInternal)new VuforiaWebcam(this.parameters.webcamCalibrationResources, this.parameters.webcamCalibrationFiles, this.parameters.minWebcamAspectRatio, this.parameters.maxWebcamAspectRatio, this.parameters.secondsUsbPermissionTimeout, this.parameters.cameraName);
      } 
      if (this.parameters.cameraName.isUnknown()) {
        this.tracer.trace("using camera by direction (classic): %s", new Object[] { this.parameters.cameraDirection });
      } else {
        if (this.parameters.cameraName.isCameraDirection()) {
          VuforiaLocalizer.Parameters parameters = this.parameters;
          parameters.cameraDirection = ((BuiltinCameraName)parameters.cameraName).getCameraDirection();
          this.tracer.trace("using camera by direction (wrapped): %s", new Object[] { this.parameters.cameraDirection });
          return null;
        } 
        throw Misc.illegalArgumentException("parameters.cameraName=%s is not supported", new Object[] { this.parameters.cameraName });
      } 
      return null;
    } 
    throw Misc.illegalArgumentException("parameters.cameraName is null; is your camera attached to the robot controller?");
  }
  
  protected void deinitTracker() {
    TrackerManager.getInstance().deinitTracker(ObjectTracker.getClassType());
  }
  
  protected void destroyTrackables() {
    synchronized (this.loadedTrackableSets) {
      Iterator<VuforiaTrackablesImpl> iterator = this.loadedTrackableSets.iterator();
      while (iterator.hasNext())
        ((VuforiaTrackablesImpl)iterator.next()).destroy(); 
      this.loadedTrackableSets.clear();
      return;
    } 
  }
  
  protected void drawAxes(float[] paramArrayOffloat) {
    this.coordinateAxes.draw(paramArrayOffloat);
  }
  
  protected void drawBuildings(float[] paramArrayOffloat) {
    Assert.assertTrue(buildingsRequired());
    Matrix.rotateM(paramArrayOffloat, 0, 90.0F, 1.0F, 0.0F, 0.0F);
    float f = this.buildingsScale;
    Matrix.scaleM(paramArrayOffloat, 0, f, f, f);
    float[] arrayOfFloat = new float[16];
    Matrix.multiplyMM(arrayOfFloat, 0, this.projectionMatrix.getData(), 0, paramArrayOffloat, 0);
    this.cubeMeshProgram.useProgram();
    GLES20.glDisable(2884);
    this.cubeMeshProgram.vertex.setCoordinates((MeshObject)this.buildingsModel);
    this.cubeMeshProgram.fragment.setTexture(this.buildingsTexture);
    this.cubeMeshProgram.vertex.setModelViewProjectionMatrix(arrayOfFloat);
    GLES20.glDrawArrays(4, 0, this.buildingsModel.getNumObjectVertex());
    this.cubeMeshProgram.vertex.disableAttributes();
  }
  
  protected void drawTeapot(float[] paramArrayOffloat) {
    Assert.assertTrue(teapotRequired());
    float f = this.teapotScale;
    Matrix.scaleM(paramArrayOffloat, 0, f, f, f);
    float[] arrayOfFloat = new float[16];
    Matrix.multiplyMM(arrayOfFloat, 0, this.projectionMatrix.getData(), 0, paramArrayOffloat, 0);
    this.cubeMeshProgram.useProgram();
    this.cubeMeshProgram.vertex.setCoordinates((MeshObject)this.teapot);
    this.cubeMeshProgram.fragment.setTexture(this.teapotTexture);
    this.cubeMeshProgram.vertex.setModelViewProjectionMatrix(arrayOfFloat);
    GLES20.glDrawElements(4, this.teapot.getNumObjectIndex(), 5123, this.teapot.getIndices());
    this.cubeMeshProgram.vertex.disableAttributes();
  }
  
  public void enableConvertFrameToBitmap() {
    enableConvertFrameToFormat(new int[] { 1 });
  }
  
  public boolean[] enableConvertFrameToFormat(int... paramVarArgs) {
    boolean[] arrayOfBoolean = new boolean[paramVarArgs.length];
    int k = paramVarArgs.length;
    int i = 0;
    int j = i;
    while (i < k) {
      int m = paramVarArgs[i];
      if (Vuforia.setFrameFormat(m, true)) {
        arrayOfBoolean[j] = true;
      } else {
        arrayOfBoolean[j] = false;
        this.tracer.traceError("enableConvertFrameToBitmap(): internal error: setFrameFormat(%d) failed", new Object[] { Integer.valueOf(m) });
      } 
      j++;
      i++;
    } 
    return arrayOfBoolean;
  }
  
  protected void expectPhase(VuforiaInitPhase paramVuforiaInitPhase) {
    boolean bool;
    if (this.vuforiaInitPhase == paramVuforiaInitPhase) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
  }
  
  protected void generateProjectionMatrix() {
    if (this.vuforiaWebcam != null) {
      generateProjectionMatrixForWebcam();
      return;
    } 
    generateProjectionMatrixForInternalCam();
  }
  
  protected void generateProjectionMatrixForInternalCam() {
    CameraCalibration cameraCalibration = CameraDevice.getInstance().getCameraCalibration();
    this.projectionMatrix = Tool.getProjectionGL(cameraCalibration, 10.0F, 5000.0F);
    float[] arrayOfFloat1 = cameraCalibration.getSize().getData();
    float[] arrayOfFloat2 = cameraCalibration.getFocalLength().getData();
    float[] arrayOfFloat3 = cameraCalibration.getPrincipalPoint().getData();
    float[] arrayOfFloat4 = cameraCalibration.getDistortionParameters().getData();
    float[] arrayOfFloat5 = new float[8];
    System.arraycopy(arrayOfFloat4, 0, arrayOfFloat5, 0, arrayOfFloat4.length);
    this.camCal = new CameraCalibration(null, new int[] { Math.round(arrayOfFloat1[0]), Math.round(arrayOfFloat1[1]) }, arrayOfFloat2, arrayOfFloat3, arrayOfFloat5, false, false);
  }
  
  protected void generateProjectionMatrixForWebcam() {
    int j;
    Matrix44F matrix44F = new Matrix44F();
    CameraCalibration cameraCalibration = this.vuforiaWebcam.getCalibrationInUse();
    this.camCal = cameraCalibration;
    if (cameraCalibration.isFake()) {
      generateProjectionMatrixForInternalCam();
      return;
    } 
    if (this.invertAspectRatio) {
      i = cameraCalibration.getSize().getHeight();
      j = cameraCalibration.getSize().getWidth();
      f1 = cameraCalibration.principalPointX;
      f2 = cameraCalibration.principalPointY;
      f3 = cameraCalibration.focalLengthX;
      f4 = cameraCalibration.focalLengthY;
    } else {
      j = cameraCalibration.getSize().getHeight();
      i = cameraCalibration.getSize().getWidth();
      f1 = cameraCalibration.principalPointY;
      f2 = cameraCalibration.principalPointX;
      f3 = cameraCalibration.focalLengthY;
      f4 = cameraCalibration.focalLengthX;
    } 
    float f6 = (i / 2);
    float f5 = (j / 2);
    float f7 = i;
    float f4 = f4 * 2.0F / f7;
    float f8 = j;
    float f3 = f3 * -2.0F / f8;
    float f2 = (f2 - f6) * 2.0F / f7;
    float f1 = (f1 - f5 + 1.0F) * -2.0F / f8;
    float[] arrayOfFloat = new float[16];
    arrayOfFloat[0] = f4;
    arrayOfFloat[1] = 0.0F;
    arrayOfFloat[2] = 0.0F;
    arrayOfFloat[3] = 0.0F;
    arrayOfFloat[4] = 0.0F;
    arrayOfFloat[5] = f3;
    arrayOfFloat[6] = 0.0F;
    arrayOfFloat[7] = 0.0F;
    arrayOfFloat[8] = f2;
    arrayOfFloat[9] = f1;
    arrayOfFloat[10] = 1.004008F;
    arrayOfFloat[11] = 1.0F;
    arrayOfFloat[12] = 0.0F;
    arrayOfFloat[13] = 0.0F;
    arrayOfFloat[14] = -20.040081F;
    arrayOfFloat[15] = 0.0F;
    int i = this.surfaceRotation;
    i = i * 90 - getNaturalOrientationOffset(i);
    if (i != 0) {
      matrix44F.setData(rotateProjectionMatrixAroundZAxis(arrayOfFloat, i));
    } else {
      matrix44F.setData(arrayOfFloat);
    } 
    this.projectionMatrix = matrix44F;
  }
  
  public int getCallbackCount() {
    return this.callbackCount;
  }
  
  public Camera getCamera() {
    VuforiaWebcamInternal vuforiaWebcamInternal = this.vuforiaWebcam;
    return (vuforiaWebcamInternal != null) ? vuforiaWebcamInternal.getCamera() : null;
  }
  
  public CameraCalibration getCameraCalibration() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield camCal : Lorg/firstinspires/ftc/robotcore/internal/camera/calibration/CameraCalibration;
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
  
  public CameraName getCameraName() {
    VuforiaWebcamInternal vuforiaWebcamInternal = this.vuforiaWebcam;
    return (vuforiaWebcamInternal != null) ? vuforiaWebcamInternal.getCameraName() : ClassFactory.getInstance().getCameraManager().nameFromCameraDirection(this.parameters.cameraDirection);
  }
  
  public void getFrameBitmap(Continuation<? extends Consumer<Bitmap>> paramContinuation) {
    synchronized (this.bitmapFrameLock) {
      this.bitmapContinuation = paramContinuation;
      return;
    } 
  }
  
  public void getFrameOnce(Continuation<? extends Consumer<Frame>> paramContinuation) {
    synchronized (this.frameQueueLock) {
      this.getFrameOnce = paramContinuation;
      return;
    } 
  }
  
  public BlockingQueue<VuforiaLocalizer.CloseableFrame> getFrameQueue() {
    synchronized (this.frameQueueLock) {
      return this.frameQueue;
    } 
  }
  
  public int getFrameQueueCapacity() {
    synchronized (this.frameQueueLock) {
      return this.frameQueueCapacity;
    } 
  }
  
  protected String getInitializationErrorString(int paramInt) {
    switch (paramInt) {
      default:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_UNKNOWN_ERROR);
      case -2:
        return this.activity.getString(R.string.VUFORIA_INIT_ERROR_DEVICE_NOT_SUPPORTED);
      case -3:
        return this.activity.getString(R.string.VUFORIA_INIT_ERROR_NO_CAMERA_ACCESS);
      case -4:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_MISSING_KEY);
      case -5:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_INVALID_KEY);
      case -6:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_PERMANENT);
      case -7:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_NO_NETWORK_TRANSIENT);
      case -8:
        return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_CANCELED_KEY);
      case -9:
        break;
    } 
    return this.activity.getString(R.string.VUFORIA_INIT_LICENSE_ERROR_PRODUCT_TYPE_MISMATCH);
  }
  
  protected int getNaturalOrientationOffset(int paramInt) {
    return isNaturalOrientationLandscape(paramInt) ? 0 : 90;
  }
  
  public int getRenderCount() {
    return this.renderCount;
  }
  
  protected float getSceneScaleFactor() {
    return (float)(Math.tan((CameraDevice.getInstance().getCameraCalibration().getFieldOfViewRads().getData()[1] / 2.0F)) / Math.tan(0.7417649320975901D));
  }
  
  protected Bitmap glSurfaceToBitmap() {
    // Byte code:
    //   0: aload_0
    //   1: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   4: getfield extent : Landroid/graphics/Point;
    //   7: getfield x : I
    //   10: istore_3
    //   11: aload_0
    //   12: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   15: getfield extent : Landroid/graphics/Point;
    //   18: getfield y : I
    //   21: istore #4
    //   23: aload_0
    //   24: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   27: getfield lowerLeft : Landroid/graphics/Point;
    //   30: getfield x : I
    //   33: istore_1
    //   34: aload_0
    //   35: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   38: getfield lowerLeft : Landroid/graphics/Point;
    //   41: getfield y : I
    //   44: istore_2
    //   45: iload_3
    //   46: iload #4
    //   48: imul
    //   49: istore #5
    //   51: iload #5
    //   53: newarray int
    //   55: astore #6
    //   57: iload #5
    //   59: newarray int
    //   61: astore #7
    //   63: aload #6
    //   65: invokestatic wrap : ([I)Ljava/nio/IntBuffer;
    //   68: astore #8
    //   70: aload #8
    //   72: iconst_0
    //   73: invokevirtual position : (I)Ljava/nio/Buffer;
    //   76: pop
    //   77: iload_1
    //   78: iload_2
    //   79: iload_3
    //   80: iload #4
    //   82: sipush #6408
    //   85: sipush #5121
    //   88: aload #8
    //   90: invokestatic glReadPixels : (IIIIIILjava/nio/Buffer;)V
    //   93: iconst_0
    //   94: istore_1
    //   95: goto -> 185
    //   98: iload_2
    //   99: iload_3
    //   100: if_icmpge -> 159
    //   103: aload #6
    //   105: iload_1
    //   106: iload_3
    //   107: imul
    //   108: iload_2
    //   109: iadd
    //   110: iaload
    //   111: istore #5
    //   113: aload #7
    //   115: iload #4
    //   117: iload_1
    //   118: isub
    //   119: iconst_1
    //   120: isub
    //   121: iload_3
    //   122: imul
    //   123: iload_2
    //   124: iadd
    //   125: iload #5
    //   127: ldc_w -16711936
    //   130: iand
    //   131: iload #5
    //   133: bipush #16
    //   135: ishl
    //   136: ldc_w 16711680
    //   139: iand
    //   140: ior
    //   141: iload #5
    //   143: bipush #16
    //   145: ishr
    //   146: sipush #255
    //   149: iand
    //   150: ior
    //   151: iastore
    //   152: iload_2
    //   153: iconst_1
    //   154: iadd
    //   155: istore_2
    //   156: goto -> 98
    //   159: iload_1
    //   160: iconst_1
    //   161: iadd
    //   162: istore_1
    //   163: goto -> 185
    //   166: aload #7
    //   168: iload_3
    //   169: iload #4
    //   171: getstatic android/graphics/Bitmap$Config.ARGB_8888 : Landroid/graphics/Bitmap$Config;
    //   174: invokestatic createBitmap : ([IIILandroid/graphics/Bitmap$Config;)Landroid/graphics/Bitmap;
    //   177: areturn
    //   178: aconst_null
    //   179: areturn
    //   180: astore #6
    //   182: goto -> 178
    //   185: iload_1
    //   186: iload #4
    //   188: if_icmpge -> 166
    //   191: iconst_0
    //   192: istore_2
    //   193: goto -> 98
    // Exception table:
    //   from	to	target	type
    //   77	93	180	android/opengl/GLException
  }
  
  protected void initRendering() {
    float f;
    this.renderer = Renderer.getInstance();
    if (Vuforia.requiresAlpha()) {
      f = 0.0F;
    } else {
      f = 1.0F;
    } 
    GLES20.glClearColor(0.0F, 0.0F, 0.0F, f);
    for (Texture texture : this.textures) {
      GLES20.glGenTextures(1, texture.mTextureID, 0);
      GLES20.glBindTexture(3553, texture.mTextureID[0]);
      GLES20.glTexParameterf(3553, 10241, 9729.0F);
      GLES20.glTexParameterf(3553, 10240, 9729.0F);
      GLES20.glTexImage2D(3553, 0, 6408, texture.mWidth, texture.mHeight, 0, 6408, 5121, texture.mData);
    } 
    this.cubeMeshProgram = new CubeMeshProgram((Context)this.activity);
    this.simpleColorProgram = new SimpleColorProgram((Context)this.activity);
    if (teapotRequired())
      this.teapot = new Teapot(); 
    if (buildingsRequired()) {
      SavedMeshObject savedMeshObject = new SavedMeshObject();
      this.buildingsModel = savedMeshObject;
      try {
        savedMeshObject.loadModel(this.activity.getResources().getAssets(), "Buildings.txt");
        return;
      } catch (IOException iOException) {
        throwFailure(iOException);
      } 
    } 
  }
  
  protected void initTracker() {
    TrackerManager.getInstance().initTracker(ObjectTracker.getClassType());
  }
  
  protected boolean isNaturalOrientationLandscape(int paramInt) {
    int i = (this.appUtil.getActivity().getResources().getConfiguration()).orientation;
    boolean bool2 = false;
    boolean bool1 = false;
    if (paramInt != 0 && paramInt != 2) {
      if (i == 1)
        bool1 = true; 
      return bool1;
    } 
    bool1 = bool2;
    if (i == 2)
      bool1 = true; 
    return bool1;
  }
  
  protected boolean isObjectTargetTrackableResult(TrackableResult paramTrackableResult) {
    return paramTrackableResult.isOfType(ObjectTargetResult.getClassType());
  }
  
  protected VuforiaTrackables loadDataSet(String paramString, int paramInt, Class<? extends VuforiaTrackable.Listener> paramClass) {
    showLoadingIndicator(0);
    try {
      DataSet dataSet = getObjectTracker().createDataSet();
      this.tracer.trace("loading data set '%s'...", new Object[] { paramString });
    } finally {
      showLoadingIndicator(4);
    } 
  }
  
  protected void loadTextures() {
    this.buildingsTexture = Texture.loadTextureFromApk("Buildings.jpeg", this.activity.getAssets());
    this.teapotTexture = Texture.loadTextureFromApk("TextureTeapotBrass.png", this.activity.getAssets());
    LinkedList<Texture> linkedList = new LinkedList();
    this.textures = linkedList;
    linkedList.add(this.buildingsTexture);
    this.textures.add(this.teapotTexture);
  }
  
  public VuforiaTrackables loadTrackablesFromAsset(String paramString) {
    return loadTrackablesFromAsset(paramString, (Class)VuforiaTrackableDefaultListener.class);
  }
  
  public VuforiaTrackables loadTrackablesFromAsset(String paramString, Class<? extends VuforiaTrackable.Listener> paramClass) {
    return loadDataSet(paramString, 1, paramClass);
  }
  
  public VuforiaTrackables loadTrackablesFromFile(String paramString) {
    return loadTrackablesFromFile(paramString, (Class)VuforiaTrackableDefaultListener.class);
  }
  
  public VuforiaTrackables loadTrackablesFromFile(String paramString, Class<? extends VuforiaTrackable.Listener> paramClass) {
    return loadDataSet(paramString, 2, paramClass);
  }
  
  protected void makeGlSurface() {
    final boolean translucent = Vuforia.requiresAlpha();
    if (this.glSurfaceParent != null)
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              ViewGroup viewGroup = VuforiaLocalizerImpl.this.glSurfaceParent;
              if (viewGroup != null) {
                VuforiaLocalizerImpl vuforiaLocalizerImpl = VuforiaLocalizerImpl.this;
                AutoConfigGLSurfaceView autoConfigGLSurfaceView = new AutoConfigGLSurfaceView((Context)VuforiaLocalizerImpl.this.activity);
                vuforiaLocalizerImpl.glSurface = autoConfigGLSurfaceView;
                autoConfigGLSurfaceView.init(translucent, 16, 0);
                autoConfigGLSurfaceView.setRenderer(VuforiaLocalizerImpl.this.glSurfaceViewRenderer);
                autoConfigGLSurfaceView.setVisibility(4);
                viewGroup.addView((View)autoConfigGLSurfaceView);
                viewGroup.setVisibility(0);
              } 
            }
          }); 
  }
  
  protected void makeLoadingIndicator() {
    removeLoadingIndicator();
    this.appUtil.synchronousRunOnUiThread(new Runnable() {
          public void run() {
            VuforiaLocalizerImpl vuforiaLocalizerImpl = VuforiaLocalizerImpl.this;
            vuforiaLocalizerImpl.loadingIndicatorOverlay = (RelativeLayout)View.inflate((Context)vuforiaLocalizerImpl.activity, R.layout.loading_indicator_overlay, null);
            vuforiaLocalizerImpl = VuforiaLocalizerImpl.this;
            vuforiaLocalizerImpl.loadingIndicator = vuforiaLocalizerImpl.loadingIndicatorOverlay.findViewById(R.id.loadingIndicator);
            VuforiaLocalizerImpl.this.loadingIndicator.setVisibility(4);
            VuforiaLocalizerImpl.this.activity.addContentView((View)VuforiaLocalizerImpl.this.loadingIndicatorOverlay, new ViewGroup.LayoutParams(-1, -1));
          }
        });
  }
  
  public void onRenderFrame() {}
  
  protected void pauseAR() {
    stopCamera();
    Vuforia.onPause();
  }
  
  protected void registerLifeCycleCallbacks() {
    this.appUtil.getApplication().registerActivityLifecycleCallbacks(this.lifeCycleCallbacks);
    OpModeManagerImpl opModeManagerImpl = OpModeManagerImpl.getOpModeManagerOfActivity(this.activity);
    this.opModeManager = opModeManagerImpl;
    if (opModeManagerImpl != null)
      opModeManagerImpl.registerListener(this.opModeNotifications); 
  }
  
  protected void removeGlSurface() {
    if (this.glSurfaceParent != null)
      this.appUtil.runOnUiThread(new Runnable() {
            public void run() {
              AutoConfigGLSurfaceView autoConfigGLSurfaceView = VuforiaLocalizerImpl.this.glSurface;
              if (autoConfigGLSurfaceView != null)
                autoConfigGLSurfaceView.setVisibility(4); 
              ViewGroup viewGroup = VuforiaLocalizerImpl.this.glSurfaceParent;
              if (viewGroup != null) {
                viewGroup.removeAllViews();
                viewGroup.getOverlay().clear();
                VuforiaLocalizerImpl.this.glSurface = null;
                viewGroup.setVisibility(VuforiaLocalizerImpl.this.glSurfaceParentPreviousVisibility);
              } 
            }
          }); 
  }
  
  protected void removeLoadingIndicator() {
    if (this.loadingIndicatorOverlay != null)
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              if (VuforiaLocalizerImpl.this.loadingIndicatorOverlay != null) {
                ((ViewGroup)VuforiaLocalizerImpl.this.loadingIndicatorOverlay.getParent()).removeView((View)VuforiaLocalizerImpl.this.loadingIndicatorOverlay);
                VuforiaLocalizerImpl.this.loadingIndicatorOverlay = null;
              } 
            }
          }); 
  }
  
  protected void renderFrame() {
    // Byte code:
    //   0: aload_0
    //   1: getfield glSurface : Lorg/firstinspires/ftc/robotcore/internal/opengl/AutoConfigGLSurfaceView;
    //   4: ifnonnull -> 8
    //   7: return
    //   8: aload_0
    //   9: getfield renderingPrimitivesMutex : Ljava/lang/Object;
    //   12: astore_3
    //   13: aload_3
    //   14: monitorenter
    //   15: aload_0
    //   16: aload_0
    //   17: getfield renderCount : I
    //   20: iconst_1
    //   21: iadd
    //   22: putfield renderCount : I
    //   25: sipush #16640
    //   28: invokestatic glClear : (I)V
    //   31: aload_0
    //   32: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   35: ifnull -> 319
    //   38: aload_0
    //   39: getfield renderer : Lcom/vuforia/Renderer;
    //   42: invokevirtual begin : ()Lcom/vuforia/State;
    //   45: astore #4
    //   47: aload_0
    //   48: invokevirtual renderVideoBackground : ()V
    //   51: sipush #2929
    //   54: invokestatic glEnable : (I)V
    //   57: aload_0
    //   58: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   61: getfield lowerLeft : Landroid/graphics/Point;
    //   64: getfield x : I
    //   67: aload_0
    //   68: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   71: getfield lowerLeft : Landroid/graphics/Point;
    //   74: getfield y : I
    //   77: aload_0
    //   78: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   81: getfield extent : Landroid/graphics/Point;
    //   84: getfield x : I
    //   87: aload_0
    //   88: getfield viewport : Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$ViewPort;
    //   91: getfield extent : Landroid/graphics/Point;
    //   94: getfield y : I
    //   97: invokestatic glViewport : (IIII)V
    //   100: sipush #2884
    //   103: invokestatic glEnable : (I)V
    //   106: sipush #1029
    //   109: invokestatic glCullFace : (I)V
    //   112: invokestatic getInstance : ()Lcom/vuforia/Renderer;
    //   115: invokevirtual getVideoBackgroundConfig : ()Lcom/vuforia/VideoBackgroundConfig;
    //   118: invokevirtual getReflection : ()I
    //   121: iconst_1
    //   122: if_icmpne -> 134
    //   125: sipush #2304
    //   128: invokestatic glFrontFace : (I)V
    //   131: goto -> 329
    //   134: sipush #2305
    //   137: invokestatic glFrontFace : (I)V
    //   140: goto -> 329
    //   143: iload_1
    //   144: aload #4
    //   146: invokevirtual getNumTrackableResults : ()I
    //   149: if_icmpge -> 239
    //   152: aload #4
    //   154: iload_1
    //   155: invokevirtual getTrackableResult : (I)Lcom/vuforia/TrackableResult;
    //   158: astore #5
    //   160: aload_0
    //   161: aload #5
    //   163: invokevirtual isObjectTargetTrackableResult : (Lcom/vuforia/TrackableResult;)Z
    //   166: ifeq -> 334
    //   169: aload #5
    //   171: invokevirtual getPose : ()Lcom/vuforia/Matrix34F;
    //   174: invokestatic convertPose2GLMatrix : (Lcom/vuforia/Matrix34F;)Lcom/vuforia/Matrix44F;
    //   177: invokevirtual getData : ()[F
    //   180: astore #5
    //   182: getstatic org/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$15.$SwitchMap$org$firstinspires$ftc$robotcore$external$navigation$VuforiaLocalizer$Parameters$CameraMonitorFeedback : [I
    //   185: aload_0
    //   186: getfield cameraCameraMonitorFeedback : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$Parameters$CameraMonitorFeedback;
    //   189: invokevirtual ordinal : ()I
    //   192: iaload
    //   193: istore_2
    //   194: iload_2
    //   195: iconst_2
    //   196: if_icmpeq -> 230
    //   199: iload_2
    //   200: iconst_3
    //   201: if_icmpeq -> 221
    //   204: iload_2
    //   205: iconst_4
    //   206: if_icmpeq -> 212
    //   209: goto -> 334
    //   212: aload_0
    //   213: aload #5
    //   215: invokevirtual drawAxes : ([F)V
    //   218: goto -> 334
    //   221: aload_0
    //   222: aload #5
    //   224: invokevirtual drawTeapot : ([F)V
    //   227: goto -> 334
    //   230: aload_0
    //   231: aload #5
    //   233: invokevirtual drawBuildings : ([F)V
    //   236: goto -> 334
    //   239: aload_0
    //   240: invokevirtual onRenderFrame : ()V
    //   243: aload_0
    //   244: getfield bitmapFrameLock : Ljava/lang/Object;
    //   247: astore #4
    //   249: aload #4
    //   251: monitorenter
    //   252: aload_0
    //   253: getfield bitmapContinuation : Lorg/firstinspires/ftc/robotcore/external/function/Continuation;
    //   256: ifnull -> 292
    //   259: aload_0
    //   260: invokevirtual glSurfaceToBitmap : ()Landroid/graphics/Bitmap;
    //   263: astore #5
    //   265: aload #5
    //   267: ifnull -> 292
    //   270: aload_0
    //   271: getfield bitmapContinuation : Lorg/firstinspires/ftc/robotcore/external/function/Continuation;
    //   274: new org/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl$13
    //   277: dup
    //   278: aload_0
    //   279: aload #5
    //   281: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/vuforia/VuforiaLocalizerImpl;Landroid/graphics/Bitmap;)V
    //   284: invokevirtual dispatch : (Lorg/firstinspires/ftc/robotcore/external/function/ContinuationResult;)V
    //   287: aload_0
    //   288: aconst_null
    //   289: putfield bitmapContinuation : Lorg/firstinspires/ftc/robotcore/external/function/Continuation;
    //   292: aload #4
    //   294: monitorexit
    //   295: sipush #2929
    //   298: invokestatic glDisable : (I)V
    //   301: aload_0
    //   302: getfield renderer : Lcom/vuforia/Renderer;
    //   305: invokevirtual end : ()V
    //   308: goto -> 319
    //   311: astore #5
    //   313: aload #4
    //   315: monitorexit
    //   316: aload #5
    //   318: athrow
    //   319: aload_3
    //   320: monitorexit
    //   321: return
    //   322: astore #4
    //   324: aload_3
    //   325: monitorexit
    //   326: aload #4
    //   328: athrow
    //   329: iconst_0
    //   330: istore_1
    //   331: goto -> 143
    //   334: iload_1
    //   335: iconst_1
    //   336: iadd
    //   337: istore_1
    //   338: goto -> 143
    // Exception table:
    //   from	to	target	type
    //   15	131	322	finally
    //   134	140	322	finally
    //   143	194	322	finally
    //   212	218	322	finally
    //   221	227	322	finally
    //   230	236	322	finally
    //   239	252	322	finally
    //   252	265	311	finally
    //   270	292	311	finally
    //   292	295	311	finally
    //   295	308	322	finally
    //   313	316	311	finally
    //   316	319	322	finally
    //   319	321	322	finally
    //   324	326	322	finally
  }
  
  protected void renderVideoBackground() {
    GLTextureUnit gLTextureUnit = new GLTextureUnit(0);
    GLES20.glClearColor(0.9372549F, 0.9372549F, 0.9372549F, 1.0F);
    GLES20.glClear(16384);
    if (this.renderer.updateVideoBackgroundTexture((TextureUnit)gLTextureUnit)) {
      Matrix44F matrix44F = Tool.convert2GLMatrix(this.renderingPrimitives.getVideoBackgroundProjectionMatrix(0, 1));
      if (Device.getInstance().isViewerActive()) {
        float f = getSceneScaleFactor();
        scalePoseMatrix(f, f, 1.0F, matrix44F);
      } 
      boolean[] arrayOfBoolean = new boolean[3];
      GLES20.glGetBooleanv(2929, arrayOfBoolean, 0);
      GLES20.glGetBooleanv(2884, arrayOfBoolean, 1);
      GLES20.glGetBooleanv(3089, arrayOfBoolean, 2);
      GLES20.glDisable(2929);
      GLES20.glDisable(2884);
      GLES20.glDisable(3089);
      Mesh mesh = this.renderingPrimitives.getVideoBackgroundMesh(0);
      this.cubeMeshProgram.useProgram();
      this.cubeMeshProgram.vertex.setCoordinates(mesh);
      this.cubeMeshProgram.fragment.setTextureUnit(gLTextureUnit);
      this.cubeMeshProgram.vertex.setModelViewProjectionMatrix(matrix44F.getData());
      GLES20.glDrawElements(4, mesh.getNumTriangles() * 3, 5123, mesh.getTriangles());
      this.cubeMeshProgram.vertex.disableAttributes();
      if (arrayOfBoolean[0])
        GLES20.glEnable(2929); 
      if (arrayOfBoolean[1])
        GLES20.glEnable(2884); 
      if (arrayOfBoolean[2])
        GLES20.glEnable(3089); 
    } 
  }
  
  protected void resumeAR() {
    Vuforia.onResume();
    if (this.wantCamera)
      startCamera(); 
  }
  
  protected void retreatInitPhase(Runnable paramRunnable) {
    try {
      paramRunnable.run();
      this.vuforiaInitPhase = this.vuforiaInitPhase.prev();
      return;
    } catch (Exception exception) {
      this.tracer.traceError(exception, "exception in vuforia deinitialization: phase:%s", new Object[] { this.vuforiaInitPhase });
      throw exception;
    } finally {}
    this.vuforiaInitPhase = this.vuforiaInitPhase.prev();
    throw paramRunnable;
  }
  
  protected float[] rotateProjectionMatrixAroundZAxis(float[] paramArrayOffloat, double paramDouble) {
    paramDouble = Math.toRadians(paramDouble);
    float f1 = (float)Math.cos(paramDouble);
    float f2 = (float)-Math.sin(paramDouble);
    float f3 = (float)Math.sin(paramDouble);
    float f4 = (float)Math.cos(paramDouble);
    float[] arrayOfFloat = new float[16];
    Matrix.multiplyMM(arrayOfFloat, 0, paramArrayOffloat, 0, new float[] { 
          f1, f2, 0.0F, 0.0F, f3, f4, 0.0F, 0.0F, 0.0F, 0.0F, 
          1.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F }, 0);
    return arrayOfFloat;
  }
  
  protected void scalePoseMatrix(float paramFloat1, float paramFloat2, float paramFloat3, Matrix44F paramMatrix44F) {
    float[] arrayOfFloat = paramMatrix44F.getData();
    arrayOfFloat[0] = arrayOfFloat[0] * paramFloat1;
    arrayOfFloat[1] = arrayOfFloat[1] * paramFloat1;
    arrayOfFloat[2] = arrayOfFloat[2] * paramFloat1;
    arrayOfFloat[3] = arrayOfFloat[3] * paramFloat1;
    arrayOfFloat[4] = arrayOfFloat[4] * paramFloat2;
    arrayOfFloat[5] = arrayOfFloat[5] * paramFloat2;
    arrayOfFloat[6] = arrayOfFloat[6] * paramFloat2;
    arrayOfFloat[7] = arrayOfFloat[7] * paramFloat2;
    arrayOfFloat[8] = arrayOfFloat[8] * paramFloat3;
    arrayOfFloat[9] = arrayOfFloat[9] * paramFloat3;
    arrayOfFloat[10] = arrayOfFloat[10] * paramFloat3;
    arrayOfFloat[11] = arrayOfFloat[11] * paramFloat3;
    paramMatrix44F.setData(arrayOfFloat);
  }
  
  public void setFrameQueueCapacity(int paramInt) {
    synchronized (this.frameQueueLock) {
      this.frameQueueCapacity = Math.max(0, paramInt);
      if (paramInt <= 0) {
        this.frameQueue = new ArrayBlockingQueue<VuforiaLocalizer.CloseableFrame>(1);
      } else {
        EvictingBlockingQueue evictingBlockingQueue = new EvictingBlockingQueue(new ArrayBlockingQueue(paramInt));
        evictingBlockingQueue.setEvictAction(new Consumer<VuforiaLocalizer.CloseableFrame>() {
              public void accept(VuforiaLocalizer.CloseableFrame param1CloseableFrame) {
                param1CloseableFrame.close();
              }
            });
        this.frameQueue = (BlockingQueue<VuforiaLocalizer.CloseableFrame>)evictingBlockingQueue;
      } 
      return;
    } 
  }
  
  protected void setMonitorViewParent(int paramInt) {
    setMonitorViewParent((ViewGroup)this.activity.findViewById(paramInt));
  }
  
  protected void setMonitorViewParent(ViewGroup paramViewGroup) {
    this.glSurfaceParent = paramViewGroup;
    if (paramViewGroup != null)
      this.glSurfaceParentPreviousVisibility = paramViewGroup.getVisibility(); 
  }
  
  protected void showLoadingIndicator(final int visibility) {
    this.appUtil.runOnUiThread(new Runnable() {
          public void run() {
            if (VuforiaLocalizerImpl.this.loadingIndicator != null)
              VuforiaLocalizerImpl.this.loadingIndicator.setVisibility(visibility); 
          }
        });
  }
  
  protected boolean startAR() {
    synchronized (this.startStopLock) {
      this.vuforiaInitPhase = VuforiaInitPhase.Nascent;
      showLoadingIndicator(0);
    } 
  }
  
  protected boolean startCamera() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCameraRunning : Z
    //   6: istore_2
    //   7: iconst_0
    //   8: istore_3
    //   9: iload_2
    //   10: ifne -> 255
    //   13: iconst_1
    //   14: istore_2
    //   15: goto -> 18
    //   18: iload_2
    //   19: ldc_w 'camera already running'
    //   22: iconst_0
    //   23: anewarray java/lang/Object
    //   26: invokestatic throwIfFail : (ZLjava/lang/String;[Ljava/lang/Object;)V
    //   29: aload_0
    //   30: getfield vuforiaWebcam : Lorg/firstinspires/ftc/robotcore/internal/vuforia/externalprovider/VuforiaWebcamInternal;
    //   33: ifnonnull -> 50
    //   36: aload_0
    //   37: getfield parameters : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$Parameters;
    //   40: getfield cameraDirection : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection;
    //   43: invokevirtual getDirection : ()I
    //   46: istore_1
    //   47: goto -> 57
    //   50: getstatic org/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection.BACK : Lorg/firstinspires/ftc/robotcore/external/navigation/VuforiaLocalizer$CameraDirection;
    //   53: invokevirtual getDirection : ()I
    //   56: istore_1
    //   57: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   60: iload_1
    //   61: invokevirtual init : (I)Z
    //   64: ifeq -> 228
    //   67: aload_0
    //   68: iconst_1
    //   69: putfield isCameraInited : Z
    //   72: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   75: iconst_m1
    //   76: invokevirtual selectVideoMode : (I)Z
    //   79: ifeq -> 202
    //   82: aload_0
    //   83: iconst_1
    //   84: invokevirtual configureVideoBackground : (Z)V
    //   87: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   90: invokevirtual start : ()Z
    //   93: ifeq -> 176
    //   96: aload_0
    //   97: iconst_1
    //   98: putfield isCameraStarted : Z
    //   101: aload_0
    //   102: invokevirtual generateProjectionMatrix : ()V
    //   105: aload_0
    //   106: invokevirtual startTracker : ()Z
    //   109: ifeq -> 150
    //   112: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   115: iconst_2
    //   116: invokevirtual setFocusMode : (I)Z
    //   119: ifne -> 140
    //   122: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   125: iconst_1
    //   126: invokevirtual setFocusMode : (I)Z
    //   129: ifne -> 140
    //   132: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   135: iconst_0
    //   136: invokevirtual setFocusMode : (I)Z
    //   139: pop
    //   140: aload_0
    //   141: iconst_1
    //   142: putfield isCameraRunning : Z
    //   145: iconst_1
    //   146: istore_2
    //   147: goto -> 244
    //   150: aload_0
    //   151: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   154: ldc_w 'camera tracker failed to start on camera %s'
    //   157: iconst_1
    //   158: anewarray java/lang/Object
    //   161: dup
    //   162: iconst_0
    //   163: aload_0
    //   164: invokevirtual getCameraName : ()Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;
    //   167: aastore
    //   168: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   171: iload_3
    //   172: istore_2
    //   173: goto -> 244
    //   176: aload_0
    //   177: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   180: ldc_w 'unable to select video mode on camera %s'
    //   183: iconst_1
    //   184: anewarray java/lang/Object
    //   187: dup
    //   188: iconst_0
    //   189: aload_0
    //   190: invokevirtual getCameraName : ()Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;
    //   193: aastore
    //   194: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   197: iload_3
    //   198: istore_2
    //   199: goto -> 244
    //   202: aload_0
    //   203: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   206: ldc_w 'camera %s failed to start '
    //   209: iconst_1
    //   210: anewarray java/lang/Object
    //   213: dup
    //   214: iconst_0
    //   215: aload_0
    //   216: invokevirtual getCameraName : ()Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraName;
    //   219: aastore
    //   220: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   223: iload_3
    //   224: istore_2
    //   225: goto -> 244
    //   228: aload_0
    //   229: getfield tracer : Lorg/firstinspires/ftc/robotcore/internal/system/Tracer;
    //   232: ldc_w 'CameraDevice.getInstance.init() failed'
    //   235: iconst_0
    //   236: anewarray java/lang/Object
    //   239: invokevirtual traceError : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   242: iload_3
    //   243: istore_2
    //   244: aload_0
    //   245: monitorexit
    //   246: iload_2
    //   247: ireturn
    //   248: astore #4
    //   250: aload_0
    //   251: monitorexit
    //   252: aload #4
    //   254: athrow
    //   255: iconst_0
    //   256: istore_2
    //   257: goto -> 18
    // Exception table:
    //   from	to	target	type
    //   2	7	248	finally
    //   18	47	248	finally
    //   50	57	248	finally
    //   57	140	248	finally
    //   140	145	248	finally
    //   150	171	248	finally
    //   176	197	248	finally
    //   202	223	248	finally
    //   228	242	248	finally
  }
  
  protected boolean startTracker() {
    return getObjectTracker().start();
  }
  
  protected void stopAR() {
    synchronized (this.startStopLock) {
      this.wantCamera = false;
      this.rendererIsActive = false;
      stopCamera();
      destroyTrackables();
      deinitTracker();
      removeGlSurface();
      if (this.vuforiaInitPhase.value >= VuforiaInitPhase.PostInit.value)
        retreatInitPhase(new Runnable() {
              public void run() {
                if (VuforiaLocalizerImpl.this.vuforiaWebcam != null)
                  VuforiaLocalizerImpl.this.vuforiaWebcam.preVuforiaDeinit(); 
              }
            }); 
      if (this.vuforiaInitPhase.value >= VuforiaInitPhase.Init.value)
        retreatInitPhase(new Runnable() {
              public void run() {
                VuforiaLocalizerImpl.this.tracer.trace("Vuforia.deinit()", new Runnable() {
                      public void run() {
                        Vuforia.deinit();
                      }
                    });
              }
            }); 
      if (this.vuforiaInitPhase.value >= VuforiaInitPhase.PreInit.value)
        retreatInitPhase(new Runnable() {
              public void run() {
                if (VuforiaLocalizerImpl.this.vuforiaWebcam != null)
                  VuforiaLocalizerImpl.this.vuforiaWebcam.postVuforiaDeinit(); 
              }
            }); 
      expectPhase(VuforiaInitPhase.Nascent);
      return;
    } 
  }
  
  protected void stopCamera() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isCameraRunning : Z
    //   6: ifeq -> 56
    //   9: aload_0
    //   10: invokevirtual stopTracker : ()V
    //   13: aload_0
    //   14: getfield isCameraStarted : Z
    //   17: ifeq -> 27
    //   20: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   23: invokevirtual stop : ()Z
    //   26: pop
    //   27: aload_0
    //   28: getfield isCameraInited : Z
    //   31: ifeq -> 41
    //   34: invokestatic getInstance : ()Lcom/vuforia/CameraDevice;
    //   37: invokevirtual deinit : ()Z
    //   40: pop
    //   41: aload_0
    //   42: iconst_0
    //   43: putfield isCameraInited : Z
    //   46: aload_0
    //   47: iconst_0
    //   48: putfield isCameraStarted : Z
    //   51: aload_0
    //   52: iconst_0
    //   53: putfield isCameraRunning : Z
    //   56: aload_0
    //   57: monitorexit
    //   58: return
    //   59: astore_1
    //   60: aload_0
    //   61: monitorexit
    //   62: aload_1
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	59	finally
    //   27	41	59	finally
    //   41	56	59	finally
  }
  
  protected void stopTracker() {
    getObjectTracker().stop();
  }
  
  protected boolean teapotRequired() {
    return (this.parameters.cameraMonitorFeedback == VuforiaLocalizer.Parameters.CameraMonitorFeedback.TEAPOT);
  }
  
  protected void unregisterLifeCycleCallbacks() {
    OpModeManagerImpl opModeManagerImpl = this.opModeManager;
    if (opModeManagerImpl != null)
      opModeManagerImpl.unregisterListener(this.opModeNotifications); 
    this.appUtil.getApplication().unregisterActivityLifecycleCallbacks(this.lifeCycleCallbacks);
  }
  
  protected void updateRendering(int paramInt1, int paramInt2) {
    configureVideoBackground(false);
  }
  
  protected void updateRenderingPrimitives() {
    synchronized (this.renderingPrimitivesMutex) {
      if (this.renderingPrimitives != null) {
        this.renderingPrimitives.close();
        this.renderingPrimitives = null;
      } 
      this.renderingPrimitives = new CloseableRenderingPrimitives(Device.getInstance().getRenderingPrimitives());
      return;
    } 
  }
  
  protected static class CloseableRenderingPrimitives extends RenderingPrimitives {
    public CloseableRenderingPrimitives(RenderingPrimitives param1RenderingPrimitives) {
      super(param1RenderingPrimitives);
    }
    
    public void close() {
      delete();
    }
  }
  
  class CoordinateAxes {
    private SolidCylinder axis = new SolidCylinder(0.05F, 1.0F, 32);
    
    private void drawAxis(float[] param1ArrayOffloat, SolidCylinder param1SolidCylinder, int param1Int, float param1Float1, float param1Float2, float param1Float3, float param1Float4, float param1Float5, float param1Float6) {
      param1ArrayOffloat = Arrays.copyOf(param1ArrayOffloat, param1ArrayOffloat.length);
      if (param1Float1 != 0.0F || param1Float2 != 0.0F || param1Float3 != 0.0F)
        Matrix.rotateM(param1ArrayOffloat, 0, 90.0F, param1Float1, param1Float2, param1Float3); 
      Matrix.translateM(param1ArrayOffloat, 0, param1Float4 * 100.0F, param1Float5 * 100.0F, param1Float6 * 100.0F);
      Matrix.scaleM(param1ArrayOffloat, 0, 100.0F, 100.0F, 100.0F);
      float[] arrayOfFloat = new float[16];
      Matrix.multiplyMM(arrayOfFloat, 0, VuforiaLocalizerImpl.this.projectionMatrix.getData(), 0, param1ArrayOffloat, 0);
      VuforiaLocalizerImpl.this.simpleColorProgram.useProgram();
      VuforiaLocalizerImpl.this.simpleColorProgram.fragment.setColor(param1Int);
      param1SolidCylinder.bindData((PositionAttributeShader)VuforiaLocalizerImpl.this.simpleColorProgram.vertex);
      VuforiaLocalizerImpl.this.simpleColorProgram.vertex.setModelViewProjectionMatrix(arrayOfFloat);
      param1SolidCylinder.draw();
      VuforiaLocalizerImpl.this.simpleColorProgram.vertex.disableAttributes();
    }
    
    public void draw(float[] param1ArrayOffloat) {
      SolidCylinder solidCylinder = this.axis;
      drawAxis(param1ArrayOffloat, solidCylinder, -65536, 0.0F, 0.0F, -1.0F, 0.0F, solidCylinder.height / 2.0F, 0.0F);
      solidCylinder = this.axis;
      drawAxis(param1ArrayOffloat, solidCylinder, -16776961, -1.0F, 0.0F, 0.0F, 0.0F, -solidCylinder.height / 2.0F, 0.0F);
      solidCylinder = this.axis;
      drawAxis(param1ArrayOffloat, solidCylinder, -16711936, 0.0F, 0.0F, 0.0F, 0.0F, solidCylinder.height / 2.0F, 0.0F);
    }
  }
  
  protected class GLSurfaceViewRenderer implements GLSurfaceView.Renderer {
    public void onDrawFrame(GL10 param1GL10) {
      if (VuforiaLocalizerImpl.this.rendererIsActive)
        VuforiaLocalizerImpl.this.renderFrame(); 
    }
    
    public void onSurfaceChanged(GL10 param1GL10, int param1Int1, int param1Int2) {
      synchronized (VuforiaLocalizerImpl.this.renderingPrimitivesMutex) {
        VuforiaLocalizerImpl.this.surfaceRotation = VuforiaLocalizerImpl.this.activity.getWindowManager().getDefaultDisplay().getRotation();
        VuforiaLocalizerImpl vuforiaLocalizerImpl = VuforiaLocalizerImpl.this;
        int i = (VuforiaLocalizerImpl.this.activity.getResources().getConfiguration()).orientation;
        boolean bool = true;
        if (i != 1)
          bool = false; 
        vuforiaLocalizerImpl.invertAspectRatio = bool;
        VuforiaLocalizerImpl.this.generateProjectionMatrix();
        VuforiaLocalizerImpl.this.updateRendering(param1Int1, param1Int2);
        Vuforia.onSurfaceChanged(param1Int1, param1Int2);
        VuforiaLocalizerImpl.this.updateRenderingPrimitives();
        return;
      } 
    }
    
    public void onSurfaceCreated(GL10 param1GL10, EGLConfig param1EGLConfig) {
      VuforiaLocalizerImpl.this.initRendering();
      Vuforia.onSurfaceCreated();
    }
  }
  
  protected class LifeCycleCallbacks implements Application.ActivityLifecycleCallbacks {
    public void onActivityCreated(Activity param1Activity, Bundle param1Bundle) {}
    
    public void onActivityDestroyed(Activity param1Activity) {
      if (param1Activity == VuforiaLocalizerImpl.this.activity)
        VuforiaLocalizerImpl.this.close(); 
    }
    
    public void onActivityPaused(Activity param1Activity) {
      if (param1Activity == VuforiaLocalizerImpl.this.activity) {
        if (VuforiaLocalizerImpl.this.glSurface != null) {
          VuforiaLocalizerImpl.this.glSurface.setVisibility(4);
          VuforiaLocalizerImpl.this.glSurface.onPause();
        } 
        VuforiaLocalizerImpl.this.pauseAR();
      } 
    }
    
    public void onActivityResumed(Activity param1Activity) {
      if (param1Activity == VuforiaLocalizerImpl.this.activity) {
        VuforiaLocalizerImpl.this.resumeAR();
        if (VuforiaLocalizerImpl.this.glSurface != null) {
          VuforiaLocalizerImpl.this.glSurface.setVisibility(0);
          VuforiaLocalizerImpl.this.glSurface.onResume();
        } 
      } 
    }
    
    public void onActivitySaveInstanceState(Activity param1Activity, Bundle param1Bundle) {}
    
    public void onActivityStarted(Activity param1Activity) {}
    
    public void onActivityStopped(Activity param1Activity) {}
  }
  
  protected class OpModeNotifications implements OpModeManagerNotifier.Notifications {
    public void onOpModePostStop(OpMode param1OpMode) {
      VuforiaLocalizerImpl.this.close();
    }
    
    public void onOpModePreInit(OpMode param1OpMode) {}
    
    public void onOpModePreStart(OpMode param1OpMode) {}
  }
  
  public static class ViewPort {
    public Point extent = new Point();
    
    public Point lowerLeft = new Point();
    
    public String toString() {
      return Misc.formatForUser("[(%d,%d)-(%d,%d)]", new Object[] { Integer.valueOf(this.lowerLeft.x), Integer.valueOf(this.lowerLeft.y), Integer.valueOf(this.extent.x), Integer.valueOf(this.extent.y) });
    }
  }
  
  protected class VuforiaCallback implements Vuforia.UpdateCallbackInterface {
    public void Vuforia_onUpdate(State param1State) {
      synchronized (VuforiaLocalizerImpl.this.updateCallbackLock) {
        VuforiaLocalizerImpl vuforiaLocalizerImpl = VuforiaLocalizerImpl.this;
        vuforiaLocalizerImpl.callbackCount++;
        synchronized (VuforiaLocalizerImpl.this.frameQueueLock) {
          List<VuforiaTrackablesImpl> list;
          TrackableResult trackableResult;
          if (VuforiaLocalizerImpl.this.frameQueueCapacity > 0) {
            VuforiaLocalizer.CloseableFrame closeableFrame = new VuforiaLocalizer.CloseableFrame(param1State.getFrame());
            VuforiaLocalizerImpl.this.frameQueue.add(closeableFrame);
          } 
          Continuation<? extends Consumer<Frame>> continuation = VuforiaLocalizerImpl.this.getFrameOnce;
          VuforiaLocalizerImpl.this.getFrameOnce = null;
          if (continuation != null)
            continuation.dispatch(new ContinuationResult<Consumer<Frame>>() {
                  public void handle(Consumer<Frame> param2Consumer) {
                    param2Consumer.accept(closeableFrame);
                    closeableFrame.close();
                  }
                }); 
          null = new HashSet();
          synchronized (VuforiaLocalizerImpl.this.loadedTrackableSets) {
            Iterator<VuforiaTrackablesImpl> iterator = VuforiaLocalizerImpl.this.loadedTrackableSets.iterator();
            while (iterator.hasNext()) {
              for (VuforiaTrackable vuforiaTrackable : iterator.next()) {
                null.add(vuforiaTrackable);
                if (vuforiaTrackable instanceof VuforiaTrackableContainer)
                  null.addAll(((VuforiaTrackableContainer)vuforiaTrackable).children()); 
                vuforiaTrackable = vuforiaTrackable.getParent();
                if (vuforiaTrackable != null)
                  null.add(vuforiaTrackable); 
              } 
            } 
            int j = param1State.getNumTrackableResults();
            for (int i = 0;; i++) {
              if (i < j) {
                trackableResult = param1State.getTrackableResult(i);
                if (VuforiaLocalizerImpl.this.isObjectTargetTrackableResult(trackableResult)) {
                  Trackable trackable = trackableResult.getTrackable();
                  if (trackable != null) {
                    VuforiaTrackable vuforiaTrackable = VuforiaTrackableImpl.from(trackable);
                    if (vuforiaTrackable != null) {
                      null.remove(vuforiaTrackable);
                      VuforiaTrackable vuforiaTrackable1 = vuforiaTrackable.getParent();
                      if (vuforiaTrackable1 != null)
                        null.remove(vuforiaTrackable1); 
                      if (vuforiaTrackable instanceof VuforiaTrackableNotify)
                        ((VuforiaTrackableNotify)vuforiaTrackable).noteTracked(trackableResult, VuforiaLocalizerImpl.this.getCameraName(), VuforiaLocalizerImpl.this.getCamera()); 
                    } else {
                      VuforiaLocalizerImpl.this.tracer.trace("vuforiaTrackable unexpectedly null: %s", new Object[] { trackableResult.getClass().getSimpleName() });
                    } 
                  } else {
                    VuforiaLocalizerImpl.this.tracer.trace("trackable unexpectedly null: %s", new Object[] { trackableResult.getClass().getSimpleName() });
                  } 
                } else {
                  VuforiaLocalizerImpl.this.tracer.trace("unexpected TrackableResult: %s", new Object[] { trackableResult.getClass().getSimpleName() });
                } 
              } else {
                for (Object null : null) {
                  if (null instanceof VuforiaTrackableNotify)
                    ((VuforiaTrackableNotify)null).noteNotTracked(); 
                } 
                return;
              } 
            } 
          } 
        } 
      } 
    }
  }
  
  class null implements ContinuationResult<Consumer<Frame>> {
    public void handle(Consumer<Frame> param1Consumer) {
      param1Consumer.accept(closeableFrame);
      closeableFrame.close();
    }
  }
  
  protected enum VuforiaInitPhase {
    Init,
    Nascent(0),
    PostInit(0),
    PreInit(1);
    
    public int value = 0;
    
    static {
      VuforiaInitPhase vuforiaInitPhase = new VuforiaInitPhase("PostInit", 3, 3);
      PostInit = vuforiaInitPhase;
      $VALUES = new VuforiaInitPhase[] { Nascent, PreInit, Init, vuforiaInitPhase };
    }
    
    VuforiaInitPhase(int param1Int1) {
      this.value = param1Int1;
    }
    
    public VuforiaInitPhase from(int param1Int) {
      for (VuforiaInitPhase vuforiaInitPhase : values()) {
        if (vuforiaInitPhase.value == param1Int)
          return vuforiaInitPhase; 
      } 
      return null;
    }
    
    public VuforiaInitPhase next() {
      return from(this.value + 1);
    }
    
    public VuforiaInitPhase prev() {
      return from(this.value - 1);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\VuforiaLocalizerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */