package org.firstinspires.ftc.robotcore.internal.vuforia.externalprovider;

import android.content.res.XmlResourceParser;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.android.util.Size;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.function.ThrowingSupplier;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureRequest;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSequenceId;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCaptureSession;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraCharacteristics;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraException;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraFrame;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.FocusControl;
import org.firstinspires.ftc.robotcore.internal.camera.CameraFrameInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraInternal;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.camera.ImageFormatMapper;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationIdentity;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibrationManager;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiCameraFrame;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiExposureControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.api.UvcApiFocusControl;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.constants.UvcFrameFormat;
import org.firstinspires.ftc.robotcore.internal.camera.libuvc.nativeobject.VuforiaExternalProviderCameraFrame;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.ContinuationSynchronizer;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.system.NativeObject;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;
import org.firstinspires.ftc.robotcore.internal.vuforia.VuforiaException;
import org.xmlpull.v1.XmlPullParser;

public class VuforiaWebcam implements VuforiaWebcamInternal, VuforiaWebcamNativeCallbacks {
  public static final String TAG = "VuforiaWebcam";
  
  public static boolean TRACE = true;
  
  public static boolean TRACE_VERBOSE = false;
  
  protected CameraCalibration calibrationInUse = null;
  
  protected final CameraCalibrationManager calibrationManager;
  
  protected Camera camera = null;
  
  protected CameraCalibration cameraCalibrationCache = null;
  
  protected CameraCaptureSession cameraCaptureSession = null;
  
  protected CameraCharacteristics cameraCharacteristics = null;
  
  protected final CameraManagerInternal cameraManager;
  
  protected List<CameraMode> cameraModesForVuforia = new ArrayList<CameraMode>();
  
  protected final CameraName cameraName;
  
  protected Camera cameraTemplate;
  
  protected final String externalCameraLib = "libRobotCore.so";
  
  protected Continuation<? extends Consumer<CameraFrame>> getFrameOnce = null;
  
  protected final Object getFrameOnceLock = new Object();
  
  protected final Object lock = new Object();
  
  protected final double maxAspectRatio;
  
  protected final double minAspectRatio;
  
  protected long msFrameExposureCacheRefresh = 750L;
  
  protected NativeVuforiaWebcam nativeVuforiaWebcam = null;
  
  protected long nsFrameExposureCache = 0L;
  
  protected final int secondsPermissionTimeout;
  
  protected final Executor serialThreadPool;
  
  protected Tracer tracer = Tracer.create("VuforiaWebcam", TRACE);
  
  protected Tracer verboseTracer = Tracer.create("VuforiaWebcam", TRACE_VERBOSE);
  
  public VuforiaWebcam(int[] paramArrayOfint, File[] paramArrayOfFile, double paramDouble1, double paramDouble2, int paramInt, Camera paramCamera) {
    this(paramArrayOfint, paramArrayOfFile, paramDouble1, paramDouble2, paramInt, paramCamera, null);
  }
  
  protected VuforiaWebcam(int[] paramArrayOfint, File[] paramArrayOfFile, double paramDouble1, double paramDouble2, int paramInt, Camera paramCamera, CameraName paramCameraName) {
    this.secondsPermissionTimeout = paramInt;
    CameraManagerInternal cameraManagerInternal = (CameraManagerInternal)ClassFactory.getInstance().getCameraManager();
    this.cameraManager = cameraManagerInternal;
    this.serialThreadPool = cameraManagerInternal.getSerialThreadPool();
    if (paramCamera == null) {
      paramCamera = null;
    } else {
      paramCamera = paramCamera.dup();
    } 
    this.cameraTemplate = paramCamera;
    this.minAspectRatio = paramDouble1;
    this.maxAspectRatio = paramDouble2;
    this.cameraName = paramCameraName;
    ArrayList<XmlResourceParser> arrayList1 = new ArrayList();
    ArrayList<FileInputStream> arrayList = new ArrayList();
    try {
    
    } finally {
      paramArrayOfint = null;
      for (XmlPullParser xmlPullParser : arrayList1) {
        if (xmlPullParser instanceof XmlResourceParser)
          ((XmlResourceParser)xmlPullParser).close(); 
      } 
      Iterator<FileInputStream> iterator = arrayList.iterator();
    } 
    paramInt++;
    continue;
  }
  
  public VuforiaWebcam(int[] paramArrayOfint, File[] paramArrayOfFile, double paramDouble1, double paramDouble2, int paramInt, CameraName paramCameraName) {
    this(paramArrayOfint, paramArrayOfFile, paramDouble1, paramDouble2, paramInt, null, paramCameraName);
  }
  
  protected static void throwFailure(String paramString, Object... paramVarArgs) {
    throw new VuforiaException(paramString, paramVarArgs);
  }
  
  public boolean closeCamera() {
    return ((Boolean)this.tracer.trace("closeCamera()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              VuforiaWebcam.this.stopCamera();
              Camera camera = VuforiaWebcam.this.camera;
              VuforiaWebcam.this.camera = null;
              if (camera != null)
                camera.close(); 
              return Boolean.valueOf(true);
            } 
          }
        })).booleanValue();
  }
  
  protected void computeCameraModesForVuforia() {
    Assert.assertNotNull(this.camera);
    List<CameraMode> list3 = filterSupportedFormats();
    ArrayList arrayList = new ArrayList();
    List<CameraMode> list1 = arrayList;
    if (arrayList.isEmpty())
      list1 = filterAspectRatios(filterHasCalibration(list3)); 
    List<CameraMode> list2 = list1;
    if (list1.isEmpty())
      list2 = filterAspectRatios(list3); 
    list1 = list2;
    if (list2.isEmpty())
      list1 = filterHasCalibration(list3); 
    if (list1.isEmpty())
      list1 = list3; 
    this.cameraModesForVuforia = list1;
  }
  
  public boolean createNativeVuforiaWebcam() {
    String str;
    if (isExternalCamera()) {
      str = "libRobotCore.so";
    } else {
      str = null;
    } 
    long l = nativePreVuforiaInit(str);
    if (l != 0L) {
      this.nativeVuforiaWebcam = new NativeVuforiaWebcam(l);
      return true;
    } 
    RobotLog.ee("VuforiaWebcam", "nativeCreateNativeVuforiaWebcam() failed");
    return false;
  }
  
  protected List<CameraMode> filterAspectRatios(List<CameraMode> paramList) {
    ArrayList<CameraMode> arrayList = new ArrayList();
    for (CameraMode cameraMode : paramList) {
      if (isLegalAspectRatio(cameraMode.getSize()))
        arrayList.add(cameraMode); 
    } 
    return arrayList;
  }
  
  protected List<CameraMode> filterHasCalibration(List<CameraMode> paramList) {
    ArrayList<CameraMode> arrayList = new ArrayList();
    for (CameraMode cameraMode : paramList) {
      if (((CameraInternal)this.camera).hasCalibration(this.calibrationManager, cameraMode.getSize()))
        arrayList.add(cameraMode); 
    } 
    return arrayList;
  }
  
  protected List<CameraMode> filterSupportedFormats() {
    ArrayList<CameraMode> arrayList = new ArrayList();
    for (CameraCharacteristics.CameraMode cameraMode : this.cameraCharacteristics.getAllCameraModes()) {
      this.verboseTracer.trace("supported cameraMode: %s", new Object[] { cameraMode });
      if (cameraMode.androidFormat != 20)
        continue; 
      arrayList.add(new CameraMode(cameraMode.size.getWidth(), cameraMode.size.getHeight(), cameraMode.fps, ImageFormatMapper.vuforiaWebcamFromAndroid(cameraMode.androidFormat)));
    } 
    return arrayList;
  }
  
  public CameraCalibration getCalibrationInUse() {
    return this.calibrationInUse;
  }
  
  protected CameraCalibration getCalibrationOfCurrentCamera(CameraMode paramCameraMode) {
    CameraCalibrationIdentity cameraCalibrationIdentity;
    Camera camera = this.camera;
    if (camera == null) {
      camera = null;
    } else {
      cameraCalibrationIdentity = ((CameraInternal)camera).getCalibrationIdentity();
    } 
    if (this.camera != null)
      if (cameraCalibrationIdentity != null) {
        CameraCalibration cameraCalibration = this.cameraCalibrationCache;
        if (cameraCalibration == null || !cameraCalibrationIdentity.equals(cameraCalibration.getIdentity()) || !paramCameraMode.getSize().equals(this.cameraCalibrationCache.getSize()))
          this.cameraCalibrationCache = ((CameraInternal)this.camera).getCalibration(this.calibrationManager, paramCameraMode.getSize()); 
      } else {
        this.cameraCalibrationCache = null;
      }  
    if (this.cameraCalibrationCache == null)
      this.cameraCalibrationCache = CameraCalibration.forUnavailable(cameraCalibrationIdentity, paramCameraMode.getSize()); 
    return this.cameraCalibrationCache;
  }
  
  public Camera getCamera() {
    synchronized (this.lock) {
      return this.camera;
    } 
  }
  
  public CameraName getCameraName() {
    synchronized (this.lock) {
      if (this.camera != null)
        return this.camera.getCameraName(); 
      if (this.cameraTemplate != null)
        return this.cameraTemplate.getCameraName(); 
      return this.cameraName;
    } 
  }
  
  public void getFrameOnce(Continuation<? extends Consumer<CameraFrame>> paramContinuation) {
    synchronized (this.getFrameOnceLock) {
      this.getFrameOnce = paramContinuation;
      return;
    } 
  }
  
  protected boolean isExternalCamera() {
    return (this.cameraName != null || this.cameraTemplate != null);
  }
  
  protected boolean isLegalAspectRatio(Size paramSize) {
    double d = paramSize.getWidth() / paramSize.getHeight();
    return (this.minAspectRatio <= d && d <= this.maxAspectRatio);
  }
  
  public boolean nativeCallbackClose() {
    return ((Boolean)this.tracer.trace("nativeCallbackClose()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              boolean bool = VuforiaWebcam.this.closeCamera();
              return Boolean.valueOf(bool);
            } 
          }
        })).booleanValue();
  }
  
  public long nativeCallbackGetExposure() {
    return ((Long)this.verboseTracer.trace("nativeCallbackGetMinExposure()", new Supplier<Long>() {
          public Long get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                long l = exposureControl.getExposure(TimeUnit.NANOSECONDS);
                return Long.valueOf(l);
              } 
              return Long.valueOf(0L);
            } 
          }
        })).longValue();
  }
  
  public int nativeCallbackGetExposureMode() {
    return ((Integer)this.verboseTracer.traceResult("nativeCallbackGetExposureMode()", new Supplier<Integer>() {
          public Integer get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                ExposureControl.Mode mode2 = exposureControl.getMode();
                ExposureControl.Mode mode1 = mode2;
                if (mode2 == ExposureControl.Mode.AperturePriority)
                  mode1 = ExposureControl.Mode.Auto; 
                int j = UvcApiExposureControl.toVuforia(mode1).ordinal();
                return Integer.valueOf(j);
              } 
              int i = ExtendedExposureMode.UNKNOWN.ordinal();
              return Integer.valueOf(i);
            } 
          }
        })).intValue();
  }
  
  public double nativeCallbackGetFocusLength() {
    return ((Double)this.verboseTracer.trace("nativeCallbackGetFocusLength()", new Supplier<Double>() {
          public Double get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                double d = focusControl.getFocusLength();
                return Double.valueOf(d);
              } 
              return Double.valueOf(-1.0D);
            } 
          }
        })).doubleValue();
  }
  
  public int nativeCallbackGetFocusMode() {
    return ((Integer)this.verboseTracer.trace("nativeCallbackGetFocusMode()", new Supplier<Integer>() {
          public Integer get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              FocusMode focusMode = FocusMode.UNKNOWN;
              if (focusControl != null)
                focusMode = UvcApiFocusControl.toVuforia(focusControl.getMode()); 
              int i = focusMode.ordinal();
              return Integer.valueOf(i);
            } 
          }
        })).intValue();
  }
  
  public long nativeCallbackGetMaxExposure() {
    return ((Long)this.verboseTracer.trace("nativeCallbackGetMinExposure()", new Supplier<Long>() {
          public Long get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                long l = exposureControl.getMaxExposure(TimeUnit.NANOSECONDS);
                return Long.valueOf(l);
              } 
              return Long.valueOf(0L);
            } 
          }
        })).longValue();
  }
  
  public double nativeCallbackGetMaxFocusLength() {
    return ((Double)this.verboseTracer.trace("nativeCallbackGetMaxFocusLength()", new Supplier<Double>() {
          public Double get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                double d = focusControl.getMaxFocusLength();
                return Double.valueOf(d);
              } 
              return Double.valueOf(-1.0D);
            } 
          }
        })).doubleValue();
  }
  
  public long nativeCallbackGetMinExposure() {
    return ((Long)this.verboseTracer.trace("nativeCallbackGetMinExposure()", new Supplier<Long>() {
          public Long get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                long l = exposureControl.getMinExposure(TimeUnit.NANOSECONDS);
                return Long.valueOf(l);
              } 
              return Long.valueOf(0L);
            } 
          }
        })).longValue();
  }
  
  public double nativeCallbackGetMinFocusLength() {
    return ((Double)this.verboseTracer.trace("nativeCallbackGetMinFocusLength()", new Supplier<Double>() {
          public Double get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                double d = focusControl.getMinFocusLength();
                return Double.valueOf(d);
              } 
              return Double.valueOf(-1.0D);
            } 
          }
        })).doubleValue();
  }
  
  public int nativeCallbackGetNumSupportedCameraModes() {
    return ((Integer)this.verboseTracer.trace("nativeCallbackGetNumSupportedCameraModes", new Supplier<Integer>() {
          public Integer get() {
            synchronized (VuforiaWebcam.this.lock) {
              int i = VuforiaWebcam.this.cameraModesForVuforia.size();
              return Integer.valueOf(i);
            } 
          }
        })).intValue();
  }
  
  public int[] nativeCallbackGetSupportedCameraMode(int paramInt) {
    synchronized (this.lock) {
      return ((CameraMode)this.cameraModesForVuforia.get(paramInt)).toArray();
    } 
  }
  
  public boolean nativeCallbackIsExposureModeSupported(final int vuforiaMode) {
    return ((Boolean)this.verboseTracer.traceResult("nativeCallbackIsExposureModeSupported()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                ExposureControl.Mode mode = UvcApiExposureControl.fromVuforia(vuforiaMode);
                boolean bool2 = exposureControl.isModeSupported(mode);
                boolean bool1 = bool2;
                if (!bool2) {
                  bool1 = bool2;
                  if (mode == ExposureControl.Mode.Auto)
                    bool1 = exposureControl.isModeSupported(ExposureControl.Mode.AperturePriority); 
                } 
                return Boolean.valueOf(bool1);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackIsExposureSupported() {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackIsExposureSupported()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                boolean bool = exposureControl.isExposureSupported();
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackIsFocusLengthSupported() {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackIsFocusLengthSupported()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                boolean bool = focusControl.isFocusLengthSupported();
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackIsFocusModeSupported(final int vuforiaFocusMode) {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackIsFocusModeSupported()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                boolean bool = focusControl.isModeSupported(UvcApiFocusControl.fromVuforia(vuforiaFocusMode));
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackOpen() {
    return ((Boolean)this.tracer.trace("nativeCallbackOpen()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              boolean bool = VuforiaWebcam.this.openCamera();
              return Boolean.valueOf(bool);
            } 
            VuforiaWebcam.this.tracer.traceError((Throwable)SYNTHETIC_LOCAL_VARIABLE_2, "exception in nativeCallbackOpen(); ignored", new Object[0]);
            /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_3} */
            return Boolean.valueOf(false);
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackSetExposure(final long nsExposure) {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackSetExposure()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                boolean bool = exposureControl.setExposure(nsExposure, TimeUnit.NANOSECONDS);
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackSetExposureMode(final int vuforiaMode) {
    return ((Boolean)this.verboseTracer.traceResult("nativeCallbackSetExposureMode", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
              if (exposureControl != null) {
                ExposureControl.Mode mode = UvcApiExposureControl.fromVuforia(vuforiaMode);
                boolean bool2 = exposureControl.setMode(mode);
                boolean bool1 = bool2;
                if (!bool2) {
                  bool1 = bool2;
                  if (mode == ExposureControl.Mode.Auto)
                    bool1 = exposureControl.setMode(ExposureControl.Mode.AperturePriority); 
                } 
                return Boolean.valueOf(bool1);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackSetFocusLength(final double focusLength) {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackSetFocusLength()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                boolean bool = focusControl.setFocusLength(focusLength);
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackSetFocusMode(final int vuforiaFocusMode) {
    return ((Boolean)this.verboseTracer.trace("nativeCallbackSetFocusMode", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              FocusControl focusControl = (FocusControl)VuforiaWebcam.this.camera.getControl(FocusControl.class);
              if (focusControl != null) {
                boolean bool = focusControl.setMode(UvcApiFocusControl.fromVuforia(vuforiaFocusMode));
                return Boolean.valueOf(bool);
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackStart(final int[] cameraModeData, final long pointerVuforiaEngineFrameCallback) {
    return ((Boolean)this.tracer.trace("nativeCallbackStart()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              if (VuforiaWebcam.this.camera != null) {
                null = new CameraMode(cameraModeData);
                CameraCallback cameraCallback = new CameraCallback(pointerVuforiaEngineFrameCallback);
                try {
                  return VuforiaWebcam.this.startCamera(null, cameraCallback);
                } finally {
                  cameraCallback.releaseRef();
                } 
              } 
              return Boolean.valueOf(false);
            } 
          }
        })).booleanValue();
  }
  
  public boolean nativeCallbackStop() {
    return ((Boolean)this.tracer.trace("nativeCallbackStop()", new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              return VuforiaWebcam.this.stopCamera();
            } 
          }
        })).booleanValue();
  }
  
  protected native void nativeDeliverFrameToVuforiaUvc(long paramLong1, long paramLong2, long paramLong3, long paramLong4, float[] paramArrayOffloat);
  
  protected native void nativeDeliverFrameToVuforiaVuforia(long paramLong1, long paramLong2, long paramLong3, float[] paramArrayOffloat);
  
  protected native void nativeNoteAndroidVuforiaExternalFormatMapping(long paramLong, int paramInt1, int paramInt2);
  
  protected native void nativeNoteCameraIntrinsics(long paramLong, float[] paramArrayOffloat);
  
  protected native void nativePostVuforiaDeinit();
  
  protected native boolean nativePostVuforiaInit(long paramLong, VuforiaWebcamNativeCallbacks paramVuforiaWebcamNativeCallbacks);
  
  protected native boolean nativePreVuforiaDeinit(long paramLong);
  
  protected native long nativePreVuforiaInit(String paramString);
  
  protected native void nativeReleaseVuforiaWebcam(long paramLong);
  
  protected boolean openCamera() throws CameraException {
    return ((Boolean)this.tracer.trace("openCamera()", new ThrowingSupplier<Boolean, CameraException>() {
          public Boolean get() throws CameraException {
            synchronized (VuforiaWebcam.this.lock) {
              boolean bool;
              if (VuforiaWebcam.this.cameraTemplate != null) {
                VuforiaWebcam.this.camera = VuforiaWebcam.this.cameraTemplate.dup();
              } else {
                VuforiaWebcam.this.camera = VuforiaWebcam.this.cameraManager.requestPermissionAndOpenCamera(new Deadline(VuforiaWebcam.this.secondsPermissionTimeout, TimeUnit.SECONDS), VuforiaWebcam.this.cameraName, null);
              } 
              if (VuforiaWebcam.this.camera != null) {
                VuforiaWebcam.this.cameraCharacteristics = VuforiaWebcam.this.camera.getCameraName().getCameraCharacteristics();
                VuforiaWebcam.this.computeCameraModesForVuforia();
                bool = true;
              } else {
                VuforiaWebcam.this.cameraCharacteristics = VuforiaWebcam.this.cameraName.getCameraCharacteristics();
                VuforiaWebcam.this.cameraModesForVuforia = new ArrayList<CameraMode>();
                bool = false;
              } 
              return Boolean.valueOf(bool);
            } 
          }
        })).booleanValue();
  }
  
  public void postVuforiaDeinit() {
    synchronized (this.lock) {
      nativePostVuforiaDeinit();
      if (this.nativeVuforiaWebcam != null) {
        this.nativeVuforiaWebcam.releaseRef();
        this.nativeVuforiaWebcam = null;
      } 
      if (this.cameraTemplate != null) {
        this.cameraTemplate.close();
        this.cameraTemplate = null;
      } 
      return;
    } 
  }
  
  public void postVuforiaInit() {
    synchronized (this.lock) {
      if (this.nativeVuforiaWebcam != null)
        this.nativeVuforiaWebcam.postVuforiaInit(); 
      return;
    } 
  }
  
  public void preVuforiaDeinit() {
    synchronized (this.lock) {
      if (this.nativeVuforiaWebcam != null)
        this.nativeVuforiaWebcam.preVuforiaDeinit(); 
      return;
    } 
  }
  
  public boolean preVuforiaInit() {
    boolean bool2 = createNativeVuforiaWebcam();
    boolean bool1 = bool2;
    if (bool2) {
      CameraName cameraName = this.cameraName;
      bool1 = bool2;
      if (cameraName != null)
        bool1 = cameraName.requestCameraPermission(new Deadline(this.secondsPermissionTimeout, TimeUnit.SECONDS)); 
    } 
    return bool1;
  }
  
  protected Boolean startCamera(final CameraMode cameraMode, final CameraCallback vuforiaCameraCallbackParam) {
    Tracer tracer = this.tracer;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("start(");
    stringBuilder.append(cameraMode);
    stringBuilder.append(")");
    return (Boolean)tracer.trace(stringBuilder.toString(), new Supplier<Boolean>() {
          public Boolean get() {
            synchronized (VuforiaWebcam.this.lock) {
              VuforiaWebcam.this.stopCamera();
              final ContinuationSynchronizer synchronizer = new ContinuationSynchronizer(VuforiaWebcam.this.secondsPermissionTimeout, TimeUnit.SECONDS, VuforiaWebcam.TRACE);
              boolean bool = false;
              try {
                VuforiaWebcam.this.camera.createCaptureSession(Continuation.create(VuforiaWebcam.this.serialThreadPool, new CameraCaptureSession.StateCallback() {
                        CameraCallback vuforiaCameraCallback = null;
                        
                        public void onClosed(CameraCaptureSession param2CameraCaptureSession) {
                          VuforiaWebcam.this.tracer.trace("capture session reports closed: %s", new Object[] { param2CameraCaptureSession });
                          CameraCallback cameraCallback = this.vuforiaCameraCallback;
                          if (cameraCallback != null) {
                            cameraCallback.releaseRef();
                            this.vuforiaCameraCallback = null;
                          } 
                        }
                        
                        public void onConfigured(CameraCaptureSession param2CameraCaptureSession) {
                          VuforiaWebcam.this.tracer.trace("capture session %s reports configured", new Object[] { param2CameraCaptureSession });
                          CameraCallback cameraCallback = vuforiaCameraCallbackParam;
                          this.vuforiaCameraCallback = cameraCallback;
                          cameraCallback.addRef();
                          try {
                            CameraCaptureRequest cameraCaptureRequest = VuforiaWebcam.this.camera.createCaptureRequest(cameraMode.getAndroidFormat(), cameraMode.getSize(), cameraMode.getFramesPerSecond());
                            VuforiaWebcam.this.calibrationInUse = VuforiaWebcam.this.getCalibrationOfCurrentCamera(cameraMode);
                            param2CameraCaptureSession.startCapture(cameraCaptureRequest, new CameraCaptureSession.CaptureCallback() {
                                  public void onNewFrame(CameraCaptureSession param3CameraCaptureSession, CameraCaptureRequest param3CameraCaptureRequest, CameraFrame param3CameraFrame) {
                                    if (VuforiaWebcam.this.nativeVuforiaWebcam != null)
                                      VuforiaWebcam.this.nativeVuforiaWebcam.deliverFrameToVuforia(VuforiaWebcam.null.null.this.vuforiaCameraCallback, param3CameraFrame, VuforiaWebcam.this.calibrationInUse.toArray()); 
                                    synchronized (VuforiaWebcam.this.getFrameOnceLock) {
                                      Continuation<? extends Consumer<CameraFrame>> continuation = VuforiaWebcam.this.getFrameOnce;
                                      VuforiaWebcam.this.getFrameOnce = null;
                                      if (continuation != null)
                                        continuation.dispatch(new ContinuationResult<Consumer<CameraFrame>>() {
                                              public void handle(Consumer<CameraFrame> param4Consumer) {
                                                param4Consumer.accept(copiedFrame);
                                                copiedFrame.releaseRef();
                                              }
                                            }); 
                                      return;
                                    } 
                                  }
                                }Continuation.create(VuforiaWebcam.this.serialThreadPool, new CameraCaptureSession.StatusCallback() {
                                    public void onCaptureSequenceCompleted(CameraCaptureSession param3CameraCaptureSession, CameraCaptureSequenceId param3CameraCaptureSequenceId, long param3Long) {
                                      VuforiaWebcam.this.tracer.trace("capture sequence %s reports completed: lastFrame=%d", new Object[] { param3CameraCaptureSequenceId, Long.valueOf(param3Long) });
                                    }
                                  }));
                          } catch (CameraException cameraException) {
                            RobotLog.ee("VuforiaWebcam", (Throwable)cameraException, "exception setting repeat capture request: closing session: %s", new Object[] { param2CameraCaptureSession });
                            param2CameraCaptureSession.close();
                            param2CameraCaptureSession = null;
                          } catch (RuntimeException runtimeException) {}
                          synchronizer.finish("onConfigured", param2CameraCaptureSession);
                        }
                      }));
              } catch (CameraException cameraException) {
                VuforiaWebcam.this.tracer.traceError((Throwable)cameraException, "exception starting capture: %s", new Object[] { this.this$0.camera });
                continuationSynchronizer.finish("exception starting capture", null);
              } catch (RuntimeException runtimeException) {}
              try {
                continuationSynchronizer.await("camera start");
              } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
              } 
              VuforiaWebcam.this.cameraCaptureSession = (CameraCaptureSession)continuationSynchronizer.getValue();
              if (VuforiaWebcam.this.cameraCaptureSession != null)
                bool = true; 
              return Boolean.valueOf(bool);
            } 
          }
        });
  }
  
  public Boolean stopCamera() {
    synchronized (this.lock) {
      final CameraCaptureSession cameraCaptureSession = this.cameraCaptureSession;
      this.cameraCaptureSession = null;
      boolean bool = false;
      if (cameraCaptureSession != null) {
        this.tracer.trace("stop()", new Runnable() {
              public void run() {
                cameraCaptureSession.stopCapture();
                cameraCaptureSession.close();
              }
            });
        bool = true;
      } 
      return Boolean.valueOf(bool);
    } 
  }
  
  class NativeVuforiaWebcam extends NativeObject {
    protected final Map<UvcFrameFormat, FrameFormat> formatMap = new HashMap<UvcFrameFormat, FrameFormat>();
    
    public NativeVuforiaWebcam(long param1Long) {
      super(param1Long, NativeObject.MemoryAllocator.EXTERNAL);
      for (UvcFrameFormat uvcFrameFormat : UvcFrameFormat.values()) {
        if (uvcFrameFormat != UvcFrameFormat.UNKNOWN) {
          FrameFormat frameFormat = ImageFormatMapper.vuforiaWebcamFromUvc(uvcFrameFormat);
          this.formatMap.put(uvcFrameFormat, frameFormat);
        } 
      } 
    }
    
    public void deliverFrameToVuforia(CameraCallback param1CameraCallback, CameraFrame param1CameraFrame, float[] param1ArrayOffloat) {
      if (param1CameraFrame instanceof CameraFrameInternal) {
        UvcApiCameraFrame uvcApiCameraFrame = ((CameraFrameInternal)param1CameraFrame).getUvcApiCameraFrame();
        if (uvcApiCameraFrame != null) {
          VuforiaWebcam.this.nativeDeliverFrameToVuforiaUvc(this.pointer, param1CameraCallback.getPointer(), uvcApiCameraFrame.getPointer(), getFrameExposureTime(param1CameraFrame), param1ArrayOffloat);
          return;
        } 
        VuforiaWebcam.this.tracer.traceError("getUvcApiCameraFrame() failed", new Object[0]);
        return;
      } 
      VuforiaExternalProviderCameraFrame vuforiaExternalProviderCameraFrame = new VuforiaExternalProviderCameraFrame();
      try {
        Size size = param1CameraFrame.getSize();
        vuforiaExternalProviderCameraFrame.setFrameIndex((int)param1CameraFrame.getFrameNumber());
        vuforiaExternalProviderCameraFrame.setWidth(size.getWidth());
        vuforiaExternalProviderCameraFrame.setHeight(size.getHeight());
        vuforiaExternalProviderCameraFrame.setFormat(((FrameFormat)this.formatMap.get(param1CameraFrame.getUvcFrameFormat())).ordinal());
        vuforiaExternalProviderCameraFrame.setStride(param1CameraFrame.getStride());
        vuforiaExternalProviderCameraFrame.setBuffer(param1CameraFrame.getImageBuffer());
        vuforiaExternalProviderCameraFrame.setBufferSize(param1CameraFrame.getImageSize());
        vuforiaExternalProviderCameraFrame.setTimestamp(param1CameraFrame.getCaptureTime());
        vuforiaExternalProviderCameraFrame.setExposureTime(getFrameExposureTime(param1CameraFrame));
        VuforiaWebcam.this.nativeDeliverFrameToVuforiaVuforia(this.pointer, param1CameraCallback.getPointer(), vuforiaExternalProviderCameraFrame.getPointer(), param1ArrayOffloat);
        return;
      } finally {
        vuforiaExternalProviderCameraFrame.releaseRef();
      } 
    }
    
    protected void destructor() {
      if (this.pointer != 0L) {
        VuforiaWebcam.this.nativeReleaseVuforiaWebcam(this.pointer);
        clearPointer();
      } 
      super.destructor();
    }
    
    protected long getFrameExposureTime(CameraFrame param1CameraFrame) {
      ExposureControl exposureControl = (ExposureControl)VuforiaWebcam.this.camera.getControl(ExposureControl.class);
      if (exposureControl != null)
        VuforiaWebcam.this.nsFrameExposureCache = exposureControl.getCachedExposure(TimeUnit.NANOSECONDS, new MutableReference(), VuforiaWebcam.this.msFrameExposureCacheRefresh, TimeUnit.MILLISECONDS); 
      return VuforiaWebcam.this.nsFrameExposureCache;
    }
    
    public void postVuforiaInit() {
      boolean bool = VuforiaWebcam.this.nativePostVuforiaInit(this.pointer, VuforiaWebcam.this);
      int i = 0;
      if (bool) {
        ImageFormatMapper.Format[] arrayOfFormat = ImageFormatMapper.all();
        int j = arrayOfFormat.length;
        while (i < j) {
          ImageFormatMapper.Format format = arrayOfFormat[i];
          VuforiaWebcam.this.nativeNoteAndroidVuforiaExternalFormatMapping(this.pointer, format.uvc.value, format.vuforiaWebcam.ordinal());
          i++;
        } 
      } else {
        VuforiaWebcam.throwFailure("nativePostVuforiaInit() failed", new Object[0]);
      } 
    }
    
    public void preVuforiaDeinit() {
      if (this.pointer != 0L)
        VuforiaWebcam.this.nativePreVuforiaDeinit(this.pointer); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\vuforia\externalprovider\VuforiaWebcam.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */