package com.vuforia.ar.pl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Trace;
import android.util.Pair;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.view.Surface;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import org.json.JSONException;
import org.json.JSONObject;

public class Camera2_Preview {
  private static final int AR_CAMERA_DIRECTION_BACK = 268443665;
  
  private static final int AR_CAMERA_DIRECTION_FRONT = 268443666;
  
  private static final int AR_CAMERA_DIRECTION_UNKNOWN = 268443664;
  
  private static final int AR_CAMERA_EXPOSUREMODE_AUTO = 805314560;
  
  private static final int AR_CAMERA_EXPOSUREMODE_CONTINUOUSAUTO = 805322752;
  
  private static final int AR_CAMERA_EXPOSUREMODE_LOCKED = 805310464;
  
  private static final int AR_CAMERA_EXPOSUREMODE_MANUAL = 805339136;
  
  private static final int AR_CAMERA_EXPOSUREMODE_SHUTTER_PRIORITY = 805371904;
  
  private static final int AR_CAMERA_FOCUSMODE_AUTO = 805306400;
  
  private static final int AR_CAMERA_FOCUSMODE_CONTINUOUSAUTO = 805306432;
  
  private static final int AR_CAMERA_FOCUSMODE_FIXED = 805306880;
  
  private static final int AR_CAMERA_FOCUSMODE_INFINITY = 805306624;
  
  private static final int AR_CAMERA_FOCUSMODE_MACRO = 805306496;
  
  private static final int AR_CAMERA_FOCUSMODE_NORMAL = 805306384;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_ARGB32 = 268439813;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_ARGB8888 = 268439813;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_BGR24 = 268439822;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_BGR888 = 268439822;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_BGRA32 = 268439814;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_BGRA8888 = 268439814;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_LUM = 268439809;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_NV12 = 268439815;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_NV16 = 268439816;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_NV21 = 268439817;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGB24 = 268439811;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGB565 = 268439810;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGB888 = 268439811;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGBA32 = 268439812;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGBA4444 = 268439821;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGBA5551 = 268439820;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_RGBA8888 = 268439812;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_UNKNOWN = 268439808;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_YUV420P = 268439828;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_YV12 = 268439818;
  
  private static final int AR_CAMERA_IMAGE_FORMAT_YV16 = 268439819;
  
  private static final int AR_CAMERA_PARAMTYPE_BASE = 536870912;
  
  private static final int AR_CAMERA_PARAMTYPE_BRIGHTNESSRANGE = 537133056;
  
  private static final int AR_CAMERA_PARAMTYPE_BRIGHTNESSVALUE = 537001984;
  
  private static final int AR_CAMERA_PARAMTYPE_CONTRASTRANGE = 537919488;
  
  private static final int AR_CAMERA_PARAMTYPE_CONTRASTVALUE = 537395200;
  
  private static final int AR_CAMERA_PARAMTYPE_EXPOSUREMODE = 536870944;
  
  private static final int AR_CAMERA_PARAMTYPE_EXPOSURETIME = 536871168;
  
  private static final int AR_CAMERA_PARAMTYPE_EXPOSURETIMERANGE = 536871424;
  
  private static final int AR_CAMERA_PARAMTYPE_EXPOSUREVALUE = 536871936;
  
  private static final int AR_CAMERA_PARAMTYPE_EXPOSUREVALUERANGE = 536872960;
  
  private static final int AR_CAMERA_PARAMTYPE_FOCUSMODE = 536870914;
  
  private static final int AR_CAMERA_PARAMTYPE_FOCUSRANGE = 536870920;
  
  private static final int AR_CAMERA_PARAMTYPE_FOCUSREGION = 536870928;
  
  private static final int AR_CAMERA_PARAMTYPE_FOCUSVALUE = 536870916;
  
  private static final int AR_CAMERA_PARAMTYPE_ISO = 536870976;
  
  private static final int AR_CAMERA_PARAMTYPE_ISORANGE = 536871040;
  
  private static final int AR_CAMERA_PARAMTYPE_LENS_IS_ADJUSTING = 545259520;
  
  private static final int AR_CAMERA_PARAMTYPE_RECORDING_HINT = 541065216;
  
  private static final int AR_CAMERA_PARAMTYPE_ROTATION = 538968064;
  
  private static final int AR_CAMERA_PARAMTYPE_TORCHMODE = 536870913;
  
  private static final int AR_CAMERA_PARAMTYPE_VIDEO_STABILIZATION = 553648128;
  
  private static final int AR_CAMERA_PARAMTYPE_WHITEBALANCEMODE = 536875008;
  
  private static final int AR_CAMERA_PARAMTYPE_WHITEBALANCERANGE = 536887296;
  
  private static final int AR_CAMERA_PARAMTYPE_WHITEBALANCEVALUE = 536879104;
  
  private static final int AR_CAMERA_PARAMTYPE_ZOOMRANGE = 536936448;
  
  private static final int AR_CAMERA_PARAMTYPE_ZOOMVALUE = 536903680;
  
  private static final int AR_CAMERA_PARAMVALUE_BASE = 805306368;
  
  private static final int AR_CAMERA_STATUS_CAPTURE_RUNNING = 268443651;
  
  private static final int AR_CAMERA_STATUS_OPENED = 268443650;
  
  private static final int AR_CAMERA_STATUS_UNINITIALIZED = 268443649;
  
  private static final int AR_CAMERA_STATUS_UNKNOWN = 268443648;
  
  private static final int AR_CAMERA_TORCHMODE_AUTO = 805306372;
  
  private static final int AR_CAMERA_TORCHMODE_CONTINUOUSAUTO = 805306376;
  
  private static final int AR_CAMERA_TORCHMODE_OFF = 805306369;
  
  private static final int AR_CAMERA_TORCHMODE_ON = 805306370;
  
  private static final int AR_CAMERA_TYPE_MONO = 268447761;
  
  private static final int AR_CAMERA_TYPE_STEREO = 268447762;
  
  private static final int AR_CAMERA_TYPE_UNKNOWN = 268447760;
  
  private static final int AR_CAMERA_WHITEBALANCEMODE_AUTO = 807403520;
  
  private static final int AR_CAMERA_WHITEBALANCEMODE_CONTINUOUSAUTO = 809500672;
  
  private static final int AR_CAMERA_WHITEBALANCEMODE_LOCKED = 806354944;
  
  private static final int CAMERA_CAPSINFO_VALUE_NUM_SUPPORTED_FRAMERATES = 4;
  
  private static final int CAMERA_CAPSINFO_VALUE_NUM_SUPPORTED_IMAGEFORMATS = 5;
  
  private static final int CAMERA_CAPSINFO_VALUE_NUM_SUPPORTED_IMAGESIZES = 3;
  
  private static final int CAMERA_CAPSINFO_VALUE_SUPPORTED_PARAMVALUES = 2;
  
  private static final int CAMERA_CAPSINFO_VALUE_SUPPORTED_QUERYABLE_PARAMS = 0;
  
  private static final int CAMERA_CAPSINFO_VALUE_SUPPORTED_SETTABLE_PARAMS = 1;
  
  private static final int CAMERA_CAPTUREINFO_VALUE_FORMAT = 2;
  
  private static final int CAMERA_CAPTUREINFO_VALUE_FRAMERATE = 3;
  
  private static final int CAMERA_CAPTUREINFO_VALUE_HEIGHT = 1;
  
  private static final int CAMERA_CAPTUREINFO_VALUE_PREVIEWSURFACEENABLED = 4;
  
  private static final int CAMERA_CAPTUREINFO_VALUE_WIDTH = 0;
  
  private static final int[] CAMERA_VALID_IMAGE_FORMAT_PL = new int[] { 268439817, 268439815, 268439818, 268439828 };
  
  private static final Range<Integer> EMPTY_RANGE;
  
  private static final String FOCUS_MODE_NORMAL = "normal";
  
  private static final int MAX_HIGHEST_FPS_ALLOWED = 300;
  
  private static final int MAX_LOWEST_FPS_ALLOWED = 150;
  
  private static final String MODULENAME = "Camera2_Preview";
  
  private static final int NUM_CAPTURE_BUFFERS = 2;
  
  private static final int NUM_MAX_CAMERAOPEN_RETRY = 10;
  
  private static final int TIME_CAMERAOPEN_RETRY_DELAY_MS = 250;
  
  private static final int _NUM_CAMERA_CAPSINFO_VALUE_ = 6;
  
  private static final int _NUM_CAMERA_CAPTUREINFO_VALUE_ = 5;
  
  private HashMap<ImageReader, Integer> mCameraCacheInfoIndexCache = null;
  
  private Vector<CameraCacheInfo> mCameraCacheInfos = null;
  
  private Vector<CameraCacheInfo> mCameraCacheInfosInProgress = null;
  
  private CameraManager mCameraManager;
  
  private Context mContext;
  
  private int mIsPermissionGranted = -1;
  
  private Semaphore mOpenCloseSemaphore = new Semaphore(1);
  
  static {
    Integer integer = Integer.valueOf(0);
    EMPTY_RANGE = new Range(integer, integer);
  }
  
  private boolean checkCameraManager() {
    if (this.mCameraManager != null)
      return true; 
    Activity activity = SystemTools.getActivityFromNative();
    if (activity == null)
      return false; 
    Application application = activity.getApplication();
    if (application == null)
      return false; 
    CameraManager cameraManager = (CameraManager)application.getSystemService("camera");
    this.mCameraManager = cameraManager;
    return !(cameraManager == null);
  }
  
  public static boolean checkMinimumHardwareSupportLevel(int paramInt1, int paramInt2) {
    if (paramInt1 == 268443665) {
      paramInt1 = 1;
    } else if (paramInt1 == 268443666) {
      paramInt1 = 0;
    } else {
      return false;
    } 
    try {
      CameraManager cameraManager = (CameraManager)SystemTools.getActivityFromNative().getSystemService("camera");
      String[] arrayOfString = cameraManager.getCameraIdList();
      for (int i = 0; i < arrayOfString.length; i++) {
        CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(arrayOfString[i]);
        if (((Integer)cameraCharacteristics.get(CameraCharacteristics.LENS_FACING)).intValue() == paramInt1) {
          paramInt1 = compareHardwareSupportLevel(((Integer)cameraCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue(), paramInt2);
          return (paramInt1 >= 0);
        } 
      } 
      return false;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
    } 
    return false;
  }
  
  private boolean checkPermission() {
    if (this.mIsPermissionGranted == 0)
      return true; 
    try {
      Activity activity = SystemTools.getActivityFromNative();
      int i = activity.getPackageManager().checkPermission("android.permission.CAMERA", activity.getPackageName());
      this.mIsPermissionGranted = i;
      if (i == 0)
        return true; 
    } catch (Exception exception) {}
    return false;
  }
  
  private void cleanupHandlerThread(CameraCacheInfo paramCameraCacheInfo) {
    paramCameraCacheInfo.handler = null;
    paramCameraCacheInfo.thread.quitSafely();
    paramCameraCacheInfo.thread = null;
  }
  
  private static int compareHardwareSupportLevel(int paramInt1, int paramInt2) {
    if (paramInt1 == paramInt2)
      return 0; 
    byte b = -1;
    if (paramInt1 == 2)
      return (paramInt2 >= 0) ? -1 : 1; 
    if (paramInt2 == 2) {
      paramInt2 = b;
      if (paramInt1 >= 0)
        paramInt2 = 1; 
      return paramInt2;
    } 
    return paramInt1 - paramInt2;
  }
  
  private native long getBufferAddress(ByteBuffer paramByteBuffer);
  
  private CameraCacheInfo getCameraCacheInfo(int paramInt) {
    return (paramInt < 0 || paramInt >= this.mCameraCacheInfos.size()) ? null : this.mCameraCacheInfos.get(paramInt);
  }
  
  private int getCameraDeviceIndex(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: iconst_0
    //   1: istore #4
    //   3: iload_3
    //   4: tableswitch default -> 32, 268443664 -> 48, 268443665 -> 43, 268443666 -> 38
    //   32: iconst_2
    //   33: invokestatic setSystemErrorCode : (I)V
    //   36: iconst_m1
    //   37: ireturn
    //   38: iconst_0
    //   39: istore_2
    //   40: goto -> 50
    //   43: iconst_1
    //   44: istore_2
    //   45: goto -> 50
    //   48: iconst_m1
    //   49: istore_2
    //   50: aload_0
    //   51: getfield mCameraManager : Landroid/hardware/camera2/CameraManager;
    //   54: invokevirtual getCameraIdList : ()[Ljava/lang/String;
    //   57: astore #5
    //   59: iload #4
    //   61: istore_3
    //   62: iload_3
    //   63: aload #5
    //   65: arraylength
    //   66: if_icmpge -> 128
    //   69: aload_0
    //   70: getfield mCameraManager : Landroid/hardware/camera2/CameraManager;
    //   73: aload #5
    //   75: iload_3
    //   76: aaload
    //   77: invokevirtual getCameraCharacteristics : (Ljava/lang/String;)Landroid/hardware/camera2/CameraCharacteristics;
    //   80: astore #6
    //   82: iload_2
    //   83: iflt -> 108
    //   86: aload #6
    //   88: getstatic android/hardware/camera2/CameraCharacteristics.LENS_FACING : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   91: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   94: checkcast java/lang/Integer
    //   97: invokevirtual intValue : ()I
    //   100: istore #4
    //   102: iload_2
    //   103: iload #4
    //   105: if_icmpne -> 119
    //   108: iload_1
    //   109: iflt -> 126
    //   112: iload_1
    //   113: iload_3
    //   114: if_icmpne -> 119
    //   117: iload_3
    //   118: ireturn
    //   119: iload_3
    //   120: iconst_1
    //   121: iadd
    //   122: istore_3
    //   123: goto -> 62
    //   126: iload_3
    //   127: ireturn
    //   128: bipush #6
    //   130: invokestatic setSystemErrorCode : (I)V
    //   133: iconst_m1
    //   134: ireturn
    //   135: astore #5
    //   137: goto -> 128
    // Exception table:
    //   from	to	target	type
    //   50	59	135	android/hardware/camera2/CameraAccessException
    //   62	82	135	android/hardware/camera2/CameraAccessException
    //   86	102	135	android/hardware/camera2/CameraAccessException
  }
  
  private List<Integer> getSupportedPreviewFrameRates(CameraCharacteristics paramCameraCharacteristics) {
    Range[] arrayOfRange = (Range[])paramCameraCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
    int m = arrayOfRange.length;
    int i = Integer.MAX_VALUE;
    int j = Integer.MIN_VALUE;
    int k;
    for (k = 0; k < m; k++) {
      Range range = arrayOfRange[k];
      i = Math.min(i, ((Integer)range.getLower()).intValue());
      j = Math.max(j, ((Integer)range.getUpper()).intValue());
    } 
    LinkedList<Integer> linkedList = new LinkedList();
    if (i >= 0 && i < 150 && j >= 0) {
      k = i;
      if (j >= 300) {
        DebugLog.LOGW("Camera2_Preview", String.format("Detected odd fps values from Camera2 API: low=%d, high=%d.  Using saner defaults instead.", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
        linkedList.add(Integer.valueOf(30));
        return linkedList;
      } 
      while (k <= j) {
        m = arrayOfRange.length;
        for (i = 0; i < m; i++) {
          if (arrayOfRange[i].contains(Integer.valueOf(k))) {
            linkedList.add(Integer.valueOf(k));
            break;
          } 
        } 
        k++;
      } 
      return linkedList;
    } 
    DebugLog.LOGW("Camera2_Preview", String.format("Detected odd fps values from Camera2 API: low=%d, high=%d.  Using saner defaults instead.", new Object[] { Integer.valueOf(i), Integer.valueOf(j) }));
    linkedList.add(Integer.valueOf(30));
    return linkedList;
  }
  
  private <T> CaptureRequest.Key<T> mapStringToKey(String paramString, CameraCharacteristics paramCameraCharacteristics, T paramT) {
    for (CaptureRequest.Key<T> key : (Iterable<CaptureRequest.Key<T>>)paramCameraCharacteristics.getAvailableCaptureRequestKeys()) {
      if (key.getName().equals(paramString))
        return key; 
    } 
    return null;
  }
  
  private native void newFrameAvailable(long paramLong, int paramInt1, int paramInt2, int paramInt3, int[] paramArrayOfint, int paramInt4, ByteBuffer paramByteBuffer, Object paramObject);
  
  private void setCameraCapsBit(CameraCacheInfo paramCameraCacheInfo, int paramInt1, int paramInt2, boolean paramBoolean) {
    int i;
    if (paramInt1 != 0 && paramInt1 != 1) {
      if (paramInt1 != 2)
        return; 
      i = 805306368;
    } else {
      i = 536870912;
    } 
    paramInt2 = (int)(Math.log((paramInt2 & i)) / Math.log(2.0D));
    if (paramBoolean) {
      arrayOfInt = paramCameraCacheInfo.caps;
      arrayOfInt[paramInt1] = 1 << paramInt2 | arrayOfInt[paramInt1];
      return;
    } 
    int[] arrayOfInt = ((CameraCacheInfo)arrayOfInt).caps;
    arrayOfInt[paramInt1] = 1 << paramInt2 & arrayOfInt[paramInt1];
  }
  
  private boolean setCameraCaptureParams(CameraCacheInfo paramCameraCacheInfo, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge Z and I\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  private boolean setCustomCameraParams(CameraCacheInfo paramCameraCacheInfo, String paramString) {
    if (paramCameraCacheInfo != null && paramCameraCacheInfo.builder != null) {
      if (paramCameraCacheInfo.characteristics == null)
        return false; 
      try {
        JSONObject jSONObject = new JSONObject(paramString);
        Iterator<String> iterator = jSONObject.keys();
        while (true) {
          if (iterator.hasNext()) {
            String str = iterator.next();
            try {
              Object object = jSONObject.get(str);
              Class<?> clazz = object.getClass();
              if (clazz == String.class || clazz == Integer.class) {
                if (mapStringToKey(str, paramCameraCacheInfo.characteristics, object) != null) {
                  paramCameraCacheInfo.builder.set(mapStringToKey(str, paramCameraCacheInfo.characteristics, object), object);
                  continue;
                } 
                return false;
              } 
              return false;
            } catch (JSONException jSONException) {
              return false;
            } 
          } 
          return true;
        } 
      } catch (JSONException jSONException) {
        return false;
      } 
    } 
    return false;
  }
  
  private boolean setupPreviewBuffer(CameraCacheInfo paramCameraCacheInfo) {
    int i;
    paramCameraCacheInfo.reader = ImageReader.newInstance(paramCameraCacheInfo.requestWidth, paramCameraCacheInfo.requestHeight, paramCameraCacheInfo.requestFormatAndroid, 2);
    paramCameraCacheInfo.imageSemaphore = new Semaphore(2);
    paramCameraCacheInfo.images = new Image[2];
    if (paramCameraCacheInfo.requestWidth == paramCameraCacheInfo.overrideWidth) {
      i = paramCameraCacheInfo.reader.getWidth();
    } else {
      i = paramCameraCacheInfo.overrideWidth;
    } 
    paramCameraCacheInfo.bufferWidth = i;
    if (paramCameraCacheInfo.requestHeight == paramCameraCacheInfo.overrideHeight) {
      i = paramCameraCacheInfo.reader.getHeight();
    } else {
      i = paramCameraCacheInfo.overrideHeight;
    } 
    paramCameraCacheInfo.bufferHeight = i;
    if (paramCameraCacheInfo.requestFormatAndroid == paramCameraCacheInfo.overrideFormatAndroid) {
      paramCameraCacheInfo.reader.getImageFormat();
    } else {
      i = paramCameraCacheInfo.overrideFormatAndroid;
    } 
    if (paramCameraCacheInfo.requestFormatPL == paramCameraCacheInfo.overrideFormatPL) {
      i = paramCameraCacheInfo.requestFormatPL;
    } else {
      i = paramCameraCacheInfo.overrideFormatPL;
    } 
    paramCameraCacheInfo.bufferFormatPL = i;
    paramCameraCacheInfo.reader.setOnImageAvailableListener(new OnCameraDataAvailable(), paramCameraCacheInfo.handler);
    if (paramCameraCacheInfo.surfaces == null)
      paramCameraCacheInfo.surfaces = new LinkedList<Surface>(); 
    paramCameraCacheInfo.surfaces.clear();
    paramCameraCacheInfo.surfaces.add(paramCameraCacheInfo.reader.getSurface());
    return true;
  }
  
  private int translateImageFormatPLToAndroid(int paramInt) {
    int i = 0;
    while (true) {
      int[] arrayOfInt = CAMERA_VALID_IMAGE_FORMAT_PL;
      if (i < arrayOfInt.length) {
        if (paramInt == arrayOfInt[i])
          return 35; 
        i++;
        continue;
      } 
      return 0;
    } 
  }
  
  public boolean close(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    boolean bool = false;
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    this.mCameraCacheInfoIndexCache.remove(cameraCacheInfo.reader);
    try {
      if (cameraCacheInfo.session != null)
        cameraCacheInfo.session.close(); 
      if (cameraCacheInfo.device != null)
        cameraCacheInfo.device.close(); 
      if (cameraCacheInfo.reader != null)
        cameraCacheInfo.reader.close(); 
      bool = true;
    } catch (Exception exception) {}
    cameraCacheInfo.session = null;
    cameraCacheInfo.reader = null;
    cameraCacheInfo.images = null;
    cameraCacheInfo.status = 268443649;
    cleanupHandlerThread(cameraCacheInfo);
    System.gc();
    return bool;
  }
  
  int getBitsPerPixel(int paramInt) {
    return (paramInt != 4) ? ((paramInt != 842094169) ? ((paramInt != 16) ? ((paramInt != 17) ? 0 : 12) : 16) : 12) : 16;
  }
  
  public int[] getCameraCapabilities(int paramInt) {
    if (!checkCameraManager()) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    if (cameraCacheInfo.caps != null)
      return cameraCacheInfo.caps; 
    try {
      byte b;
      boolean bool1;
      boolean bool2;
      boolean bool3;
      boolean bool4;
      boolean bool5;
      boolean bool6;
      boolean bool7;
      String str = this.mCameraManager.getCameraIdList()[cameraCacheInfo.deviceID];
      StreamConfigurationMap streamConfigurationMap = (StreamConfigurationMap)this.mCameraManager.getCameraCharacteristics(str).get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      Size[] arrayOfSize = streamConfigurationMap.getOutputSizes(35);
      List<Integer> list = getSupportedPreviewFrameRates(cameraCacheInfo.characteristics);
      int[] arrayOfInt2 = (int[])cameraCacheInfo.characteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
      Arrays.sort(arrayOfInt2);
      int[] arrayOfInt3 = (int[])cameraCacheInfo.characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
      Arrays.sort(arrayOfInt3);
      LinkedList<Integer> linkedList = new LinkedList();
      linkedList.add(Integer.valueOf(35));
      if (arrayOfSize != null) {
        paramInt = arrayOfSize.length;
      } else {
        paramInt = 0;
      } 
      if (list != null) {
        b = list.size();
      } else {
        b = 0;
      } 
      int j = linkedList.size();
      float[] arrayOfFloat = (float[])cameraCacheInfo.characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
      if (arrayOfFloat != null && arrayOfFloat.length > 0) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1) {
        int m = arrayOfFloat.length;
        for (int k = 0; k < m; k++)
          float f = arrayOfFloat[k]; 
      } 
      Boolean bool9 = (Boolean)cameraCacheInfo.characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
      Boolean bool8 = bool9;
      if (bool9 == null)
        bool8 = Boolean.valueOf(false); 
      Integer integer = (Integer)cameraCacheInfo.characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
      if (integer != null && integer.intValue() > 0) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      Range range1 = (Range)cameraCacheInfo.characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
      if (range1 != null && !EMPTY_RANGE.equals(range1)) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      range1 = (Range)cameraCacheInfo.characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
      if (range1 != null && !EMPTY_RANGE.equals(range1)) {
        bool4 = true;
      } else {
        bool4 = false;
      } 
      Range range2 = (Range)cameraCacheInfo.characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
      if (range1 != null && !EMPTY_RANGE.equals(range2)) {
        bool5 = true;
      } else {
        bool5 = false;
      } 
      int[] arrayOfInt1 = (int[])cameraCacheInfo.characteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
      int[] arrayOfInt4 = (int[])cameraCacheInfo.characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION);
      if ((arrayOfInt1 != null && arrayOfInt1.length > 1) || (arrayOfInt4 != null && arrayOfInt4.length > 1)) {
        bool6 = true;
      } else {
        bool6 = false;
      } 
      cameraCacheInfo.caps = new int[paramInt * 2 + 6 + b + j];
      cameraCacheInfo.caps[0] = 536870912;
      setCameraCapsBit(cameraCacheInfo, 0, 536870913, bool8.booleanValue());
      if (arrayOfInt2.length > 0) {
        bool7 = true;
      } else {
        bool7 = false;
      } 
      setCameraCapsBit(cameraCacheInfo, 0, 536870914, bool7);
      setCameraCapsBit(cameraCacheInfo, 0, 536870916, bool1);
      setCameraCapsBit(cameraCacheInfo, 0, 536870928, bool3);
      if (arrayOfInt3.length > 0) {
        bool7 = true;
      } else {
        bool7 = false;
      } 
      setCameraCapsBit(cameraCacheInfo, 0, 536870944, bool7);
      setCameraCapsBit(cameraCacheInfo, 0, 536871936, bool2);
      setCameraCapsBit(cameraCacheInfo, 0, 536872960, bool2);
      setCameraCapsBit(cameraCacheInfo, 0, 536870976, bool4);
      setCameraCapsBit(cameraCacheInfo, 0, 536871040, bool4);
      setCameraCapsBit(cameraCacheInfo, 0, 536871168, bool5);
      setCameraCapsBit(cameraCacheInfo, 0, 536871424, bool5);
      setCameraCapsBit(cameraCacheInfo, 0, 536903680, bool1);
      setCameraCapsBit(cameraCacheInfo, 0, 536936448, bool1);
      setCameraCapsBit(cameraCacheInfo, 0, 553648128, bool6);
      cameraCacheInfo.caps[1] = 536870912;
      setCameraCapsBit(cameraCacheInfo, 1, 536870913, bool8.booleanValue());
      if (arrayOfInt2.length > 0) {
        bool7 = true;
      } else {
        bool7 = false;
      } 
      setCameraCapsBit(cameraCacheInfo, 1, 536870914, bool7);
      setCameraCapsBit(cameraCacheInfo, 1, 536870928, bool3);
      if (arrayOfInt3.length > 0) {
        bool3 = true;
      } else {
        bool3 = false;
      } 
      setCameraCapsBit(cameraCacheInfo, 1, 536870944, bool3);
      setCameraCapsBit(cameraCacheInfo, 1, 536871936, bool2);
      setCameraCapsBit(cameraCacheInfo, 1, 536870976, bool4);
      setCameraCapsBit(cameraCacheInfo, 1, 536871168, bool5);
      setCameraCapsBit(cameraCacheInfo, 1, 536903680, bool1);
      setCameraCapsBit(cameraCacheInfo, 1, 553648128, bool6);
      cameraCacheInfo.caps[2] = 805306368;
      if (bool8.booleanValue()) {
        setCameraCapsBit(cameraCacheInfo, 2, 805306369, true);
        setCameraCapsBit(cameraCacheInfo, 2, 805306370, true);
      } 
      if (arrayOfInt2 != null) {
        if (Arrays.binarySearch(arrayOfInt2, 1) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306384, bool1);
        if (Arrays.binarySearch(arrayOfInt2, 1) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306400, bool1);
        if (Arrays.binarySearch(arrayOfInt2, 3) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306432, bool1);
        if (Arrays.binarySearch(arrayOfInt2, 2) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306496, bool1);
        if (Arrays.binarySearch(arrayOfInt2, 0) != -1 && CaptureRequest.LENS_FOCUS_DISTANCE != null) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306624, bool1);
        if (Arrays.binarySearch(arrayOfInt2, 0) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805306880, bool1);
      } 
      int i = 0;
      if (arrayOfInt3 != null) {
        if (Arrays.binarySearch(arrayOfInt3, 0) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805310464, bool1);
        if (Arrays.binarySearch(arrayOfInt3, 0) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805339136, bool1);
        if (Arrays.binarySearch(arrayOfInt3, 1) != -1) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        setCameraCapsBit(cameraCacheInfo, 2, 805322752, bool1);
      } 
      cameraCacheInfo.caps[3] = paramInt;
      cameraCacheInfo.caps[4] = b;
      cameraCacheInfo.caps[5] = j;
      if (paramInt > 0) {
        int k = arrayOfSize.length;
        paramInt = 6;
        while (i < k) {
          Size size = arrayOfSize[i];
          cameraCacheInfo.caps[paramInt] = size.getWidth();
          cameraCacheInfo.caps[paramInt + 1] = size.getHeight();
          paramInt += 2;
          i++;
        } 
      } else {
        paramInt = 6;
      } 
      i = paramInt;
      if (b > 0) {
        Iterator<Integer> iterator = list.iterator();
        while (true) {
          i = paramInt;
          if (iterator.hasNext()) {
            Integer integer1 = iterator.next();
            cameraCacheInfo.caps[paramInt] = integer1.intValue();
            paramInt++;
            continue;
          } 
          break;
        } 
      } 
      if (j > 0)
        for (Integer integer1 : linkedList) {
          cameraCacheInfo.caps[i] = cameraCacheInfo.requestFormatPL;
          i++;
        }  
      return cameraCacheInfo.caps;
    } catch (CameraAccessException cameraAccessException) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
  }
  
  public int[] getCaptureInfo(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    try {
      int[] arrayOfInt = new int[5];
      if (cameraCacheInfo.reader != null) {
        arrayOfInt[0] = cameraCacheInfo.reader.getWidth();
        arrayOfInt[1] = cameraCacheInfo.reader.getHeight();
      } else {
        arrayOfInt[0] = cameraCacheInfo.requestWidth;
        arrayOfInt[1] = cameraCacheInfo.requestHeight;
      } 
      arrayOfInt[2] = cameraCacheInfo.requestFormatPL;
      if (cameraCacheInfo.builder != null) {
        arrayOfInt[3] = ((Integer)((Range)cameraCacheInfo.builder.get(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE)).getUpper()).intValue();
      } else {
        arrayOfInt[3] = cameraCacheInfo.requestFramerate;
      } 
      arrayOfInt[4] = 1;
      return arrayOfInt;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
  }
  
  public int getDeviceID(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return -1;
    } 
    return cameraCacheInfo.deviceID;
  }
  
  public int getDirection(int paramInt) {
    if (!checkPermission()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (!checkCameraManager()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (SystemTools.checkMinimumApiLevel(21))
      try {
        String[] arrayOfString = this.mCameraManager.getCameraIdList();
        if (paramInt < arrayOfString.length) {
          paramInt = ((Integer)this.mCameraManager.getCameraCharacteristics(arrayOfString[paramInt]).get(CameraCharacteristics.LENS_FACING)).intValue();
          return (paramInt != 0) ? ((paramInt != 1) ? 268443664 : 268443665) : 268443666;
        } 
        return 268443665;
      } catch (Exception exception) {
        SystemTools.setSystemErrorCode(6);
        return -1;
      }  
    return 268443665;
  }
  
  String getNamedParameter(int paramInt1, int paramInt2) {
    CaptureRequest.Key key;
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt1);
    if (cameraCacheInfo == null || cameraCacheInfo.builder == null || cameraCacheInfo.characteristics == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    List<CaptureRequest.Key> list = cameraCacheInfo.characteristics.getAvailableCaptureRequestKeys();
    List<CameraCharacteristics.Key> list1 = cameraCacheInfo.characteristics.getKeys();
    if (paramInt2 < list.size()) {
      key = list.get(paramInt2);
      return (key == null) ? null : key.getName();
    } 
    if (paramInt2 - key.size() < list1.size()) {
      CameraCharacteristics.Key key1 = list1.get(paramInt2 - key.size());
      return (key1 == null) ? null : key1.getName();
    } 
    SystemTools.setSystemErrorCode(6);
    return null;
  }
  
  int getNamedParameterCount(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.builder == null || cameraCacheInfo.characteristics == null) {
      SystemTools.setSystemErrorCode(4);
      return -1;
    } 
    List list1 = cameraCacheInfo.characteristics.getAvailableCaptureRequestKeys();
    List list2 = cameraCacheInfo.characteristics.getKeys();
    return list1.size() + list2.size();
  }
  
  public int getNumberOfCameras() {
    if (!checkPermission()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (!checkCameraManager()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (SystemTools.checkMinimumApiLevel(21))
      try {
        return (this.mCameraManager.getCameraIdList()).length;
      } catch (Exception exception) {} 
    SystemTools.setSystemErrorCode(6);
    return -1;
  }
  
  public int getOrientation(int paramInt) {
    if (!checkPermission()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (!checkCameraManager()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (SystemTools.checkMinimumApiLevel(21))
      try {
        String[] arrayOfString = this.mCameraManager.getCameraIdList();
        if (paramInt < arrayOfString.length)
          return ((Integer)this.mCameraManager.getCameraCharacteristics(arrayOfString[paramInt]).get(CameraCharacteristics.SENSOR_ORIENTATION)).intValue(); 
      } catch (Exception exception) {} 
    SystemTools.setSystemErrorCode(6);
    return -1;
  }
  
  int getStatus(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return 268443648;
    } 
    return cameraCacheInfo.status;
  }
  
  Object getTypedCameraParameter(int paramInt1, int paramInt2) {
    Integer integer3;
    Float float_2;
    Rational rational;
    Range range2;
    Long long_;
    Range range1;
    Integer integer2;
    Pair pair;
    Float float_1;
    Integer integer1;
    Range range3;
    Integer integer4;
    MeteringRectangle[] arrayOfMeteringRectangle;
    Integer integer5;
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt1);
    if (cameraCacheInfo == null || cameraCacheInfo.characteristics == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    CaptureResult captureResult = cameraCacheInfo.lastResult;
    boolean bool = true;
    switch (paramInt2) {
      default:
        return null;
      case 553648128:
        if (captureResult == null)
          try {
            SystemTools.setSystemErrorCode(6);
            return null;
          } catch (Exception exception) {
            SystemTools.setSystemErrorCode(6);
            return null;
          }  
        integer3 = (Integer)captureResult.get(CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE);
        if (integer3 != null && CaptureResult.CONTROL_VIDEO_STABILIZATION_MODE != null) {
          if (integer3.equals(Integer.valueOf(1)))
            return Boolean.valueOf(true); 
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        } 
        integer3 = (Integer)captureResult.get(CaptureResult.LENS_OPTICAL_STABILIZATION_MODE);
        if (integer3 != null && CaptureResult.LENS_OPTICAL_STABILIZATION_MODE != null) {
          paramInt1 = bool;
          if (integer3.equals(Integer.valueOf(1)))
            return Boolean.valueOf(true); 
        } 
        if (paramInt1 == 0) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return Boolean.valueOf(false);
      case 538968064:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 537919488:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 537395200:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 537133056:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536936448:
        SystemTools.setSystemErrorCode(6);
      case 537001984:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536903680:
        if (captureResult == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        float_2 = (Float)captureResult.get(CaptureResult.LENS_FOCAL_LENGTH);
        if (float_2 == null || CaptureResult.LENS_FOCAL_LENGTH == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return float_2;
      case 536887296:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536879104:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536875008:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536872960:
        range3 = (Range)((CameraCacheInfo)float_2).characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
        rational = (Rational)((CameraCacheInfo)float_2).characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
        if (range3 == null || CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE == null || rational == null || CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return new float[] { rational.floatValue() * ((Integer)range3.getLower()).intValue(), rational.floatValue() * ((Integer)range3.getUpper()).intValue() };
      case 536871936:
        if (range3 == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer4 = (Integer)range3.get(CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION);
        rational = (Rational)((CameraCacheInfo)rational).characteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
        if (integer4 != null && CaptureResult.CONTROL_AE_EXPOSURE_COMPENSATION != null && rational != null && CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP != null)
          return Float.valueOf(rational.floatValue() * integer4.intValue()); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536871424:
        range2 = (Range)((CameraCacheInfo)rational).characteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        if (range2 == null || CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return new float[] { (float)(((Long)range2.getLower()).doubleValue() / 1.0E9D), (float)(((Long)range2.getUpper()).doubleValue() / 1.0E9D) };
      case 536871168:
        if (integer4 == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        long_ = (Long)integer4.get(CaptureResult.SENSOR_EXPOSURE_TIME);
        if (long_ != null && CaptureResult.SENSOR_EXPOSURE_TIME != null)
          return Float.valueOf((float)(long_.doubleValue() / 1.0E9D)); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536871040:
        range1 = (Range)((CameraCacheInfo)long_).characteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        if (range1 == null || CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return new float[] { ((Integer)range1.getLower()).floatValue(), ((Integer)range1.getUpper()).floatValue() };
      case 536870976:
        if (integer4 == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer2 = (Integer)integer4.get(CaptureResult.SENSOR_SENSITIVITY);
        if (integer2 != null && CaptureResult.SENSOR_SENSITIVITY != null)
          return Float.valueOf(integer2.floatValue()); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870944:
        if (integer4 == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer2 = (Integer)integer4.get(CaptureResult.CONTROL_AE_MODE);
        if (integer2 == null || CaptureResult.CONTROL_AE_MODE == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        if (integer2.equals(Integer.valueOf(0)))
          return Integer.valueOf(805339136); 
        if (integer2.equals(Integer.valueOf(1)))
          return Integer.valueOf(805322752); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870928:
        if (integer4 == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer5 = (Integer)((CameraCacheInfo)integer2).characteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AF);
        if (integer5 != null && CameraCharacteristics.CONTROL_MAX_REGIONS_AF != null && integer5.intValue() > 0 && CaptureResult.CONTROL_AF_REGIONS != null) {
          arrayOfMeteringRectangle = (MeteringRectangle[])integer4.get(CaptureResult.CONTROL_AF_REGIONS);
          if (arrayOfMeteringRectangle != null) {
            if (arrayOfMeteringRectangle.length == 0)
              return null; 
            Rect rect1 = (Rect)((CameraCacheInfo)integer2).characteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
            if (rect1 == null) {
              SystemTools.setSystemErrorCode(6);
              return Boolean.valueOf(false);
            } 
            Rect rect2 = arrayOfMeteringRectangle[0].getRect();
            return new float[] { (rect2.left / (rect1.width() - 1)), (rect2.top / (rect1.height() - 1)), (rect2.right / (rect1.width() - 1)), (rect2.bottom / (rect1.height() - 1)), ((arrayOfMeteringRectangle[0].getMeteringWeight() - 0) / 1000) };
          } 
        } else {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return null;
      case 536870920:
        if (arrayOfMeteringRectangle == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        pair = (Pair)arrayOfMeteringRectangle.get(CaptureResult.LENS_FOCUS_RANGE);
        if (pair != null && CaptureResult.LENS_FOCUS_RANGE != null)
          return new float[] { ((Float)pair.first).floatValue(), ((Float)pair.second).floatValue() }; 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870916:
        if (arrayOfMeteringRectangle == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        float_1 = (Float)arrayOfMeteringRectangle.get(CaptureResult.LENS_FOCAL_LENGTH);
        if (float_1 == null || CaptureResult.LENS_FOCAL_LENGTH == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        return float_1;
      case 536870914:
        if (arrayOfMeteringRectangle == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer5 = (Integer)arrayOfMeteringRectangle.get(CaptureResult.CONTROL_AF_MODE);
        if (integer5 == null || CaptureResult.CONTROL_AF_MODE == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        if (integer5.equals(Integer.valueOf(1))) {
          if (((CameraCacheInfo)float_1).isAutoFocusing) {
            paramInt1 = 805306400;
            return Integer.valueOf(paramInt1);
          } 
          break;
        } 
        if (integer5.equals(Integer.valueOf(3)))
          return Integer.valueOf(805306432); 
        if (integer5.equals(Integer.valueOf(0))) {
          float_1 = (Float)arrayOfMeteringRectangle.get(CaptureResult.LENS_FOCUS_DISTANCE);
          return (float_1 == null || CaptureResult.LENS_FOCUS_DISTANCE == null || !float_1.equals(Float.valueOf(0.0F))) ? Integer.valueOf(805306880) : Integer.valueOf(805306624);
        } 
        if (integer5.equals(Integer.valueOf(2)))
          return Integer.valueOf(805306496); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870913:
        if (arrayOfMeteringRectangle == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        integer1 = (Integer)arrayOfMeteringRectangle.get(CaptureResult.FLASH_MODE);
        if (integer1 == null || CaptureResult.FLASH_MODE == null) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
        if (integer1.equals(Integer.valueOf(2)))
          return Integer.valueOf(805306370); 
        if (integer1.equals(Integer.valueOf(0)))
          return Integer.valueOf(805306369); 
        SystemTools.setSystemErrorCode(6);
        return null;
    } 
    paramInt1 = 805306384;
    return Integer.valueOf(paramInt1);
  }
  
  Object getUntypedCameraParameter(int paramInt, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getCameraCacheInfo : (I)Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;
    //   5: astore #5
    //   7: aload #5
    //   9: ifnull -> 371
    //   12: aload #5
    //   14: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   17: ifnull -> 371
    //   20: aload #5
    //   22: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   25: ifnull -> 371
    //   28: aload_2
    //   29: ifnonnull -> 35
    //   32: goto -> 371
    //   35: aload #5
    //   37: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   40: invokevirtual getAvailableCaptureRequestKeys : ()Ljava/util/List;
    //   43: astore_3
    //   44: iconst_0
    //   45: istore_1
    //   46: iload_1
    //   47: aload_3
    //   48: invokeinterface size : ()I
    //   53: if_icmpge -> 101
    //   56: aload_3
    //   57: iload_1
    //   58: invokeinterface get : (I)Ljava/lang/Object;
    //   63: checkcast android/hardware/camera2/CaptureRequest$Key
    //   66: astore #4
    //   68: aload #4
    //   70: invokevirtual getName : ()Ljava/lang/String;
    //   73: aload_2
    //   74: invokevirtual equals : (Ljava/lang/Object;)Z
    //   77: ifne -> 87
    //   80: iload_1
    //   81: iconst_1
    //   82: iadd
    //   83: istore_1
    //   84: goto -> 46
    //   87: aload #5
    //   89: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   92: aload #4
    //   94: invokevirtual get : (Landroid/hardware/camera2/CaptureRequest$Key;)Ljava/lang/Object;
    //   97: astore_3
    //   98: goto -> 103
    //   101: aconst_null
    //   102: astore_3
    //   103: aload #5
    //   105: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   108: invokevirtual getKeys : ()Ljava/util/List;
    //   111: astore #6
    //   113: iconst_0
    //   114: istore_1
    //   115: aload_3
    //   116: astore #4
    //   118: iload_1
    //   119: aload #6
    //   121: invokeinterface size : ()I
    //   126: if_icmpge -> 173
    //   129: aload #6
    //   131: iload_1
    //   132: invokeinterface get : (I)Ljava/lang/Object;
    //   137: checkcast android/hardware/camera2/CameraCharacteristics$Key
    //   140: astore #4
    //   142: aload #4
    //   144: invokevirtual getName : ()Ljava/lang/String;
    //   147: aload_2
    //   148: invokevirtual equals : (Ljava/lang/Object;)Z
    //   151: ifne -> 161
    //   154: iload_1
    //   155: iconst_1
    //   156: iadd
    //   157: istore_1
    //   158: goto -> 115
    //   161: aload #5
    //   163: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   166: aload #4
    //   168: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   171: astore #4
    //   173: aload #4
    //   175: ifnonnull -> 185
    //   178: bipush #6
    //   180: invokestatic setSystemErrorCode : (I)V
    //   183: aconst_null
    //   184: areturn
    //   185: aload #4
    //   187: instanceof java/lang/Long
    //   190: ifne -> 368
    //   193: aload #4
    //   195: instanceof java/lang/Float
    //   198: ifne -> 368
    //   201: aload #4
    //   203: instanceof java/lang/Boolean
    //   206: ifne -> 368
    //   209: aload #4
    //   211: instanceof java/lang/String
    //   214: ifeq -> 220
    //   217: aload #4
    //   219: areturn
    //   220: aload #4
    //   222: instanceof java/lang/Integer
    //   225: ifeq -> 244
    //   228: new java/lang/Long
    //   231: dup
    //   232: aload #4
    //   234: checkcast java/lang/Integer
    //   237: invokevirtual longValue : ()J
    //   240: invokespecial <init> : (J)V
    //   243: areturn
    //   244: aload #4
    //   246: instanceof java/lang/Byte
    //   249: ifeq -> 268
    //   252: new java/lang/Long
    //   255: dup
    //   256: aload #4
    //   258: checkcast java/lang/Byte
    //   261: invokevirtual longValue : ()J
    //   264: invokespecial <init> : (J)V
    //   267: areturn
    //   268: aload #4
    //   270: instanceof android/util/Range
    //   273: ifeq -> 361
    //   276: aload #4
    //   278: checkcast android/util/Range
    //   281: astore_3
    //   282: aload_3
    //   283: invokevirtual getLower : ()Ljava/lang/Comparable;
    //   286: astore_2
    //   287: aload_3
    //   288: invokevirtual getUpper : ()Ljava/lang/Comparable;
    //   291: astore_3
    //   292: aload_2
    //   293: instanceof java/lang/Integer
    //   296: ifeq -> 323
    //   299: iconst_2
    //   300: newarray long
    //   302: dup
    //   303: iconst_0
    //   304: aload_2
    //   305: checkcast java/lang/Integer
    //   308: invokevirtual longValue : ()J
    //   311: lastore
    //   312: dup
    //   313: iconst_1
    //   314: aload_3
    //   315: checkcast java/lang/Integer
    //   318: invokevirtual longValue : ()J
    //   321: lastore
    //   322: areturn
    //   323: aload_2
    //   324: instanceof java/lang/Long
    //   327: ifeq -> 354
    //   330: iconst_2
    //   331: newarray long
    //   333: dup
    //   334: iconst_0
    //   335: aload_2
    //   336: checkcast java/lang/Long
    //   339: invokevirtual longValue : ()J
    //   342: lastore
    //   343: dup
    //   344: iconst_1
    //   345: aload_3
    //   346: checkcast java/lang/Long
    //   349: invokevirtual longValue : ()J
    //   352: lastore
    //   353: areturn
    //   354: bipush #6
    //   356: invokestatic setSystemErrorCode : (I)V
    //   359: aconst_null
    //   360: areturn
    //   361: bipush #6
    //   363: invokestatic setSystemErrorCode : (I)V
    //   366: aconst_null
    //   367: areturn
    //   368: aload #4
    //   370: areturn
    //   371: iconst_4
    //   372: invokestatic setSystemErrorCode : (I)V
    //   375: aconst_null
    //   376: areturn
  }
  
  int getUntypedCameraParameterType(int paramInt, String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getCameraCacheInfo : (I)Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;
    //   5: astore #7
    //   7: aload #7
    //   9: ifnull -> 305
    //   12: aload #7
    //   14: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   17: ifnull -> 305
    //   20: aload #7
    //   22: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   25: ifnull -> 305
    //   28: aload_2
    //   29: ifnonnull -> 35
    //   32: goto -> 305
    //   35: aconst_null
    //   36: astore #5
    //   38: aload #7
    //   40: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   43: invokevirtual getAvailableCaptureRequestKeys : ()Ljava/util/List;
    //   46: astore #6
    //   48: iconst_0
    //   49: istore_1
    //   50: iload_1
    //   51: aload #6
    //   53: invokeinterface size : ()I
    //   58: if_icmpge -> 110
    //   61: aload #6
    //   63: iload_1
    //   64: invokeinterface get : (I)Ljava/lang/Object;
    //   69: checkcast android/hardware/camera2/CaptureRequest$Key
    //   72: astore #8
    //   74: aload #8
    //   76: invokevirtual getName : ()Ljava/lang/String;
    //   79: aload_2
    //   80: invokevirtual equals : (Ljava/lang/Object;)Z
    //   83: ifne -> 93
    //   86: iload_1
    //   87: iconst_1
    //   88: iadd
    //   89: istore_1
    //   90: goto -> 50
    //   93: aload #7
    //   95: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   98: aload #8
    //   100: invokevirtual get : (Landroid/hardware/camera2/CaptureRequest$Key;)Ljava/lang/Object;
    //   103: astore #5
    //   105: iconst_1
    //   106: istore_1
    //   107: goto -> 112
    //   110: iconst_0
    //   111: istore_1
    //   112: aload #7
    //   114: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   117: invokevirtual getKeys : ()Ljava/util/List;
    //   120: astore #8
    //   122: iconst_0
    //   123: istore_3
    //   124: aload #5
    //   126: astore #6
    //   128: iload_1
    //   129: istore #4
    //   131: iload_3
    //   132: aload #8
    //   134: invokeinterface size : ()I
    //   139: if_icmpge -> 189
    //   142: aload #8
    //   144: iload_3
    //   145: invokeinterface get : (I)Ljava/lang/Object;
    //   150: checkcast android/hardware/camera2/CameraCharacteristics$Key
    //   153: astore #6
    //   155: aload #6
    //   157: invokevirtual getName : ()Ljava/lang/String;
    //   160: aload_2
    //   161: invokevirtual equals : (Ljava/lang/Object;)Z
    //   164: ifne -> 174
    //   167: iload_3
    //   168: iconst_1
    //   169: iadd
    //   170: istore_3
    //   171: goto -> 124
    //   174: aload #7
    //   176: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   179: aload #6
    //   181: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   184: astore #6
    //   186: iconst_1
    //   187: istore #4
    //   189: iload #4
    //   191: ifne -> 201
    //   194: bipush #6
    //   196: invokestatic setSystemErrorCode : (I)V
    //   199: iconst_m1
    //   200: ireturn
    //   201: aload #6
    //   203: ifnonnull -> 208
    //   206: iconst_m1
    //   207: ireturn
    //   208: aload #6
    //   210: instanceof java/lang/Integer
    //   213: ifeq -> 218
    //   216: iconst_1
    //   217: ireturn
    //   218: aload #6
    //   220: instanceof java/lang/Byte
    //   223: ifeq -> 228
    //   226: iconst_1
    //   227: ireturn
    //   228: aload #6
    //   230: instanceof java/lang/Long
    //   233: ifeq -> 238
    //   236: iconst_1
    //   237: ireturn
    //   238: aload #6
    //   240: instanceof java/lang/Float
    //   243: ifeq -> 248
    //   246: iconst_2
    //   247: ireturn
    //   248: aload #6
    //   250: instanceof java/lang/Boolean
    //   253: ifeq -> 258
    //   256: iconst_3
    //   257: ireturn
    //   258: aload #6
    //   260: instanceof java/lang/String
    //   263: ifeq -> 268
    //   266: iconst_0
    //   267: ireturn
    //   268: aload #6
    //   270: instanceof android/util/Range
    //   273: ifeq -> 303
    //   276: aload #6
    //   278: checkcast android/util/Range
    //   281: invokevirtual getLower : ()Ljava/lang/Comparable;
    //   284: astore_2
    //   285: aload_2
    //   286: instanceof java/lang/Integer
    //   289: ifeq -> 294
    //   292: iconst_4
    //   293: ireturn
    //   294: aload_2
    //   295: instanceof java/lang/Long
    //   298: ifeq -> 303
    //   301: iconst_4
    //   302: ireturn
    //   303: iconst_m1
    //   304: ireturn
    //   305: iconst_4
    //   306: invokestatic setSystemErrorCode : (I)V
    //   309: iconst_m1
    //   310: ireturn
  }
  
  public boolean init() {
    this.mCameraCacheInfos = new Vector<CameraCacheInfo>();
    this.mCameraCacheInfosInProgress = new Vector<CameraCacheInfo>();
    this.mCameraCacheInfoIndexCache = new HashMap<ImageReader, Integer>();
    return true;
  }
  
  public int open(long paramLong, int paramInt1, int paramInt2, int paramInt3, String paramString, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial checkPermission : ()Z
    //   4: ifne -> 14
    //   7: bipush #6
    //   9: invokestatic setSystemErrorCode : (I)V
    //   12: iconst_m1
    //   13: ireturn
    //   14: aload_0
    //   15: invokespecial checkCameraManager : ()Z
    //   18: ifne -> 28
    //   21: bipush #6
    //   23: invokestatic setSystemErrorCode : (I)V
    //   26: iconst_m1
    //   27: ireturn
    //   28: aload_0
    //   29: iload_3
    //   30: iload #4
    //   32: iload #5
    //   34: invokespecial getCameraDeviceIndex : (III)I
    //   37: istore #4
    //   39: iload #4
    //   41: ifge -> 46
    //   44: iconst_m1
    //   45: ireturn
    //   46: aload_0
    //   47: getfield mCameraCacheInfos : Ljava/util/Vector;
    //   50: invokevirtual size : ()I
    //   53: istore #5
    //   55: iconst_0
    //   56: istore #9
    //   58: aconst_null
    //   59: astore #10
    //   61: iconst_0
    //   62: istore_3
    //   63: iload_3
    //   64: iload #5
    //   66: if_icmpge -> 102
    //   69: aload_0
    //   70: getfield mCameraCacheInfos : Ljava/util/Vector;
    //   73: iload_3
    //   74: invokevirtual get : (I)Ljava/lang/Object;
    //   77: checkcast com/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo
    //   80: astore #10
    //   82: aload #10
    //   84: getfield deviceID : I
    //   87: iload #4
    //   89: if_icmpne -> 95
    //   92: goto -> 104
    //   95: iload_3
    //   96: iconst_1
    //   97: iadd
    //   98: istore_3
    //   99: goto -> 63
    //   102: iconst_m1
    //   103: istore_3
    //   104: iload_3
    //   105: ifge -> 369
    //   108: new com/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo
    //   111: dup
    //   112: aload_0
    //   113: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera2_Preview;)V
    //   116: astore #10
    //   118: aload #10
    //   120: iload #4
    //   122: putfield deviceID : I
    //   125: aload #10
    //   127: lload_1
    //   128: putfield deviceHandle : J
    //   131: aload #10
    //   133: aload_0
    //   134: getfield mCameraManager : Landroid/hardware/camera2/CameraManager;
    //   137: invokevirtual getCameraIdList : ()[Ljava/lang/String;
    //   140: aload #10
    //   142: getfield deviceID : I
    //   145: aaload
    //   146: putfield deviceIDString : Ljava/lang/String;
    //   149: aload #10
    //   151: aload_0
    //   152: getfield mCameraManager : Landroid/hardware/camera2/CameraManager;
    //   155: aload #10
    //   157: getfield deviceIDString : Ljava/lang/String;
    //   160: invokevirtual getCameraCharacteristics : (Ljava/lang/String;)Landroid/hardware/camera2/CameraCharacteristics;
    //   163: putfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   166: aload #10
    //   168: aconst_null
    //   169: putfield device : Landroid/hardware/camera2/CameraDevice;
    //   172: aload #10
    //   174: aconst_null
    //   175: putfield session : Landroid/hardware/camera2/CameraCaptureSession;
    //   178: aload #10
    //   180: aconst_null
    //   181: putfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   184: aload #10
    //   186: aconst_null
    //   187: putfield surfaces : Ljava/util/List;
    //   190: aload #10
    //   192: aconst_null
    //   193: putfield reader : Landroid/media/ImageReader;
    //   196: aload #10
    //   198: aconst_null
    //   199: putfield images : [Landroid/media/Image;
    //   202: aload #10
    //   204: aconst_null
    //   205: putfield imageSemaphore : Ljava/util/concurrent/Semaphore;
    //   208: aload #10
    //   210: iconst_0
    //   211: putfield overrideWidth : I
    //   214: aload #10
    //   216: iconst_0
    //   217: putfield bufferWidth : I
    //   220: aload #10
    //   222: iconst_0
    //   223: putfield overrideHeight : I
    //   226: aload #10
    //   228: iconst_0
    //   229: putfield bufferHeight : I
    //   232: aload #10
    //   234: ldc 268439808
    //   236: putfield bufferFormatPL : I
    //   239: aload #10
    //   241: ldc 268439808
    //   243: putfield overrideFormatPL : I
    //   246: aload #10
    //   248: iconst_0
    //   249: putfield overrideFormatAndroid : I
    //   252: aload #10
    //   254: aconst_null
    //   255: putfield caps : [I
    //   258: aload #10
    //   260: ldc 268443649
    //   262: putfield status : I
    //   265: aload #10
    //   267: iconst_0
    //   268: putfield isAutoFocusing : Z
    //   271: aload #10
    //   273: ldc 268439817
    //   275: putfield requestFormatPL : I
    //   278: aload #10
    //   280: bipush #35
    //   282: putfield requestFormatAndroid : I
    //   285: aload #10
    //   287: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   290: getstatic android/hardware/camera2/CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   293: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   296: checkcast android/hardware/camera2/params/StreamConfigurationMap
    //   299: aload #10
    //   301: getfield requestFormatAndroid : I
    //   304: invokevirtual getOutputSizes : (I)[Landroid/util/Size;
    //   307: astore #11
    //   309: aload #11
    //   311: arraylength
    //   312: ifle -> 831
    //   315: aload #11
    //   317: iconst_0
    //   318: aaload
    //   319: invokevirtual getWidth : ()I
    //   322: istore #4
    //   324: goto -> 327
    //   327: aload #10
    //   329: iload #4
    //   331: putfield requestWidth : I
    //   334: aload #11
    //   336: arraylength
    //   337: ifle -> 837
    //   340: aload #11
    //   342: iconst_0
    //   343: aaload
    //   344: invokevirtual getHeight : ()I
    //   347: istore #4
    //   349: goto -> 352
    //   352: aload #10
    //   354: iload #4
    //   356: putfield requestHeight : I
    //   359: goto -> 369
    //   362: bipush #6
    //   364: invokestatic setSystemErrorCode : (I)V
    //   367: iconst_m1
    //   368: ireturn
    //   369: bipush #10
    //   371: istore #5
    //   373: new java/lang/StringBuilder
    //   376: dup
    //   377: invokespecial <init> : ()V
    //   380: astore #11
    //   382: aload #11
    //   384: aload #10
    //   386: getfield deviceIDString : Ljava/lang/String;
    //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: pop
    //   393: aload #11
    //   395: ldc_w '_camera_thread'
    //   398: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   401: pop
    //   402: aload #10
    //   404: new android/os/HandlerThread
    //   407: dup
    //   408: aload #11
    //   410: invokevirtual toString : ()Ljava/lang/String;
    //   413: invokespecial <init> : (Ljava/lang/String;)V
    //   416: putfield thread : Landroid/os/HandlerThread;
    //   419: aload #10
    //   421: getfield thread : Landroid/os/HandlerThread;
    //   424: invokevirtual start : ()V
    //   427: aload #10
    //   429: new android/os/Handler
    //   432: dup
    //   433: aload #10
    //   435: getfield thread : Landroid/os/HandlerThread;
    //   438: invokevirtual getLooper : ()Landroid/os/Looper;
    //   441: invokespecial <init> : (Landroid/os/Looper;)V
    //   444: putfield handler : Landroid/os/Handler;
    //   447: iconst_0
    //   448: istore #4
    //   450: aload_0
    //   451: getfield mOpenCloseSemaphore : Ljava/util/concurrent/Semaphore;
    //   454: invokevirtual acquire : ()V
    //   457: aload_0
    //   458: getfield mCameraCacheInfosInProgress : Ljava/util/Vector;
    //   461: aload #10
    //   463: invokevirtual add : (Ljava/lang/Object;)Z
    //   466: pop
    //   467: aload_0
    //   468: getfield mCameraManager : Landroid/hardware/camera2/CameraManager;
    //   471: aload #10
    //   473: getfield deviceIDString : Ljava/lang/String;
    //   476: new com/vuforia/ar/pl/Camera2_Preview$1
    //   479: dup
    //   480: aload_0
    //   481: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera2_Preview;)V
    //   484: aload #10
    //   486: getfield handler : Landroid/os/Handler;
    //   489: invokevirtual openCamera : (Ljava/lang/String;Landroid/hardware/camera2/CameraDevice$StateCallback;Landroid/os/Handler;)V
    //   492: aload_0
    //   493: getfield mOpenCloseSemaphore : Ljava/util/concurrent/Semaphore;
    //   496: invokevirtual acquire : ()V
    //   499: aload_0
    //   500: getfield mCameraCacheInfosInProgress : Ljava/util/Vector;
    //   503: aload #10
    //   505: invokevirtual remove : (Ljava/lang/Object;)Z
    //   508: pop
    //   509: aload_0
    //   510: getfield mOpenCloseSemaphore : Ljava/util/concurrent/Semaphore;
    //   513: invokevirtual release : ()V
    //   516: aload #10
    //   518: getfield device : Landroid/hardware/camera2/CameraDevice;
    //   521: ifnull -> 542
    //   524: aload #10
    //   526: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   529: astore #11
    //   531: aload #11
    //   533: ifnull -> 542
    //   536: iconst_1
    //   537: istore #4
    //   539: goto -> 545
    //   542: iconst_0
    //   543: istore #4
    //   545: iload #4
    //   547: ifne -> 576
    //   550: iload #5
    //   552: ifle -> 576
    //   555: aload_0
    //   556: monitorenter
    //   557: aload_0
    //   558: ldc2_w 250
    //   561: invokevirtual wait : (J)V
    //   564: aload_0
    //   565: monitorexit
    //   566: goto -> 576
    //   569: astore #11
    //   571: aload_0
    //   572: monitorexit
    //   573: aload #11
    //   575: athrow
    //   576: iload #4
    //   578: ifne -> 598
    //   581: iload #5
    //   583: ifgt -> 589
    //   586: goto -> 598
    //   589: iload #5
    //   591: iconst_1
    //   592: isub
    //   593: istore #5
    //   595: goto -> 450
    //   598: aload #10
    //   600: getfield device : Landroid/hardware/camera2/CameraDevice;
    //   603: ifnull -> 803
    //   606: aload #10
    //   608: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   611: ifnonnull -> 617
    //   614: goto -> 803
    //   617: aload #7
    //   619: ifnull -> 628
    //   622: aload #7
    //   624: arraylength
    //   625: ifgt -> 639
    //   628: aload #8
    //   630: ifnull -> 645
    //   633: aload #8
    //   635: arraylength
    //   636: ifle -> 645
    //   639: iconst_1
    //   640: istore #4
    //   642: goto -> 648
    //   645: iconst_0
    //   646: istore #4
    //   648: iload #9
    //   650: istore #5
    //   652: aload #6
    //   654: ifnull -> 672
    //   657: iload #9
    //   659: istore #5
    //   661: aload #6
    //   663: invokevirtual length : ()I
    //   666: ifle -> 672
    //   669: iconst_1
    //   670: istore #5
    //   672: iload #4
    //   674: ifne -> 682
    //   677: iload #5
    //   679: ifeq -> 765
    //   682: iload #4
    //   684: ifeq -> 737
    //   687: aload #7
    //   689: ifnull -> 711
    //   692: aload #7
    //   694: arraylength
    //   695: iconst_5
    //   696: if_icmpeq -> 711
    //   699: iconst_2
    //   700: invokestatic setSystemErrorCode : (I)V
    //   703: aload_0
    //   704: aload #10
    //   706: invokespecial cleanupHandlerThread : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   709: iconst_m1
    //   710: ireturn
    //   711: aload_0
    //   712: aload #10
    //   714: aload #7
    //   716: aload #8
    //   718: invokespecial setCameraCaptureParams : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;[I[I)Z
    //   721: ifne -> 737
    //   724: bipush #6
    //   726: invokestatic setSystemErrorCode : (I)V
    //   729: aload_0
    //   730: aload #10
    //   732: invokespecial cleanupHandlerThread : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   735: iconst_m1
    //   736: ireturn
    //   737: iload #5
    //   739: ifeq -> 765
    //   742: aload_0
    //   743: aload #10
    //   745: aload #6
    //   747: invokespecial setCustomCameraParams : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;Ljava/lang/String;)Z
    //   750: ifne -> 765
    //   753: iconst_2
    //   754: invokestatic setSystemErrorCode : (I)V
    //   757: aload_0
    //   758: aload #10
    //   760: invokespecial cleanupHandlerThread : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   763: iconst_m1
    //   764: ireturn
    //   765: aload #10
    //   767: ldc 268443650
    //   769: putfield status : I
    //   772: iload_3
    //   773: istore #4
    //   775: iload_3
    //   776: ifge -> 800
    //   779: aload_0
    //   780: getfield mCameraCacheInfos : Ljava/util/Vector;
    //   783: aload #10
    //   785: invokevirtual add : (Ljava/lang/Object;)Z
    //   788: pop
    //   789: aload_0
    //   790: getfield mCameraCacheInfos : Ljava/util/Vector;
    //   793: invokevirtual size : ()I
    //   796: iconst_1
    //   797: isub
    //   798: istore #4
    //   800: iload #4
    //   802: ireturn
    //   803: bipush #6
    //   805: invokestatic setSystemErrorCode : (I)V
    //   808: aload_0
    //   809: aload #10
    //   811: invokespecial cleanupHandlerThread : (Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   814: iconst_m1
    //   815: ireturn
    //   816: astore #6
    //   818: goto -> 362
    //   821: astore #11
    //   823: goto -> 545
    //   826: astore #11
    //   828: goto -> 576
    //   831: iconst_0
    //   832: istore #4
    //   834: goto -> 327
    //   837: iconst_0
    //   838: istore #4
    //   840: goto -> 352
    // Exception table:
    //   from	to	target	type
    //   108	324	816	android/hardware/camera2/CameraAccessException
    //   327	349	816	android/hardware/camera2/CameraAccessException
    //   352	359	816	android/hardware/camera2/CameraAccessException
    //   450	531	821	java/lang/Exception
    //   555	557	826	java/lang/Exception
    //   557	566	569	finally
    //   571	573	569	finally
    //   573	576	826	java/lang/Exception
  }
  
  public boolean setBatchParameters(int paramInt, String paramString) {
    if (paramString == null)
      return false; 
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.builder == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    return !!setCustomCameraParams(cameraCacheInfo, paramString);
  }
  
  public boolean setCaptureInfo(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    if (paramArrayOfint1.length != 5) {
      SystemTools.setSystemErrorCode(2);
      return false;
    } 
    if (!setCameraCaptureParams(cameraCacheInfo, paramArrayOfint1, paramArrayOfint2)) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    return true;
  }
  
  boolean setTypedCameraParameter(int paramInt1, int paramInt2, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getCameraCacheInfo : (I)Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;
    //   5: astore #8
    //   7: iconst_0
    //   8: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   11: astore #9
    //   13: aload #8
    //   15: ifnull -> 2239
    //   18: aload #8
    //   20: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   23: ifnull -> 2239
    //   26: aload #8
    //   28: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   31: ifnonnull -> 37
    //   34: goto -> 2239
    //   37: iconst_3
    //   38: istore_1
    //   39: iload_2
    //   40: lookupswitch default -> 244, 536870913 -> 1881, 536870914 -> 1538, 536870916 -> 1497, 536870920 -> 1490, 536870928 -> 1239, 536870944 -> 1090, 536870976 -> 1008, 536871040 -> 1001, 536871168 -> 914, 536871424 -> 907, 536871936 -> 782, 536872960 -> 775, 536875008 -> 653, 536879104 -> 646, 536887296 -> 639, 536903680 -> 531, 536936448 -> 524, 537001984 -> 517, 537133056 -> 510, 537395200 -> 503, 537919488 -> 496, 538968064 -> 489, 541065216 -> 435, 553648128 -> 246
    //   244: iconst_0
    //   245: ireturn
    //   246: aload #8
    //   248: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   251: getstatic android/hardware/camera2/CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   254: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   257: checkcast [I
    //   260: astore #10
    //   262: aload #10
    //   264: ifnull -> 2305
    //   267: aload #10
    //   269: arraylength
    //   270: iconst_1
    //   271: if_icmple -> 2305
    //   274: getstatic android/hardware/camera2/CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   277: ifnull -> 2305
    //   280: iconst_1
    //   281: istore_1
    //   282: goto -> 285
    //   285: aload #8
    //   287: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   290: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   293: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   296: checkcast [I
    //   299: astore #10
    //   301: aload #10
    //   303: ifnull -> 2310
    //   306: aload #10
    //   308: arraylength
    //   309: iconst_1
    //   310: if_icmple -> 2310
    //   313: getstatic android/hardware/camera2/CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   316: ifnull -> 2310
    //   319: iconst_1
    //   320: istore #5
    //   322: goto -> 325
    //   325: iload_1
    //   326: ifne -> 341
    //   329: iload #5
    //   331: ifne -> 341
    //   334: bipush #6
    //   336: invokestatic setSystemErrorCode : (I)V
    //   339: iconst_0
    //   340: ireturn
    //   341: aload_3
    //   342: checkcast java/lang/Boolean
    //   345: invokevirtual booleanValue : ()Z
    //   348: istore #7
    //   350: iload_1
    //   351: ifeq -> 367
    //   354: aload #8
    //   356: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   359: getstatic android/hardware/camera2/CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   362: aload #9
    //   364: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   367: iload #5
    //   369: ifeq -> 385
    //   372: aload #8
    //   374: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   377: getstatic android/hardware/camera2/CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   380: aload #9
    //   382: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   385: iload #7
    //   387: ifeq -> 2518
    //   390: iload_1
    //   391: ifeq -> 412
    //   394: aload #8
    //   396: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   399: getstatic android/hardware/camera2/CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   402: iconst_1
    //   403: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   406: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   409: goto -> 2518
    //   412: iload #5
    //   414: ifeq -> 2518
    //   417: aload #8
    //   419: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   422: getstatic android/hardware/camera2/CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   425: iconst_1
    //   426: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   429: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   432: goto -> 2518
    //   435: getstatic android/hardware/camera2/CaptureRequest.CONTROL_CAPTURE_INTENT : Landroid/hardware/camera2/CaptureRequest$Key;
    //   438: ifnonnull -> 448
    //   441: bipush #6
    //   443: invokestatic setSystemErrorCode : (I)V
    //   446: iconst_0
    //   447: ireturn
    //   448: aload_3
    //   449: checkcast java/lang/Number
    //   452: invokevirtual intValue : ()I
    //   455: istore #5
    //   457: aload #8
    //   459: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   462: astore_3
    //   463: getstatic android/hardware/camera2/CaptureRequest.CONTROL_CAPTURE_INTENT : Landroid/hardware/camera2/CaptureRequest$Key;
    //   466: astore #9
    //   468: iload #5
    //   470: ifeq -> 2316
    //   473: goto -> 476
    //   476: aload_3
    //   477: aload #9
    //   479: iload_1
    //   480: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   483: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   486: goto -> 2518
    //   489: bipush #6
    //   491: invokestatic setSystemErrorCode : (I)V
    //   494: iconst_0
    //   495: ireturn
    //   496: bipush #6
    //   498: invokestatic setSystemErrorCode : (I)V
    //   501: iconst_0
    //   502: ireturn
    //   503: bipush #6
    //   505: invokestatic setSystemErrorCode : (I)V
    //   508: iconst_0
    //   509: ireturn
    //   510: bipush #6
    //   512: invokestatic setSystemErrorCode : (I)V
    //   515: iconst_0
    //   516: ireturn
    //   517: bipush #6
    //   519: invokestatic setSystemErrorCode : (I)V
    //   522: iconst_0
    //   523: ireturn
    //   524: bipush #6
    //   526: invokestatic setSystemErrorCode : (I)V
    //   529: iconst_0
    //   530: ireturn
    //   531: aload_3
    //   532: checkcast java/lang/Number
    //   535: invokevirtual intValue : ()I
    //   538: istore #5
    //   540: aload #8
    //   542: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   545: getstatic android/hardware/camera2/CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   548: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   551: checkcast [F
    //   554: astore_3
    //   555: aload_3
    //   556: ifnull -> 632
    //   559: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCAL_LENGTH : Landroid/hardware/camera2/CaptureRequest$Key;
    //   562: ifnonnull -> 568
    //   565: goto -> 632
    //   568: aload_3
    //   569: arraylength
    //   570: istore #6
    //   572: iconst_0
    //   573: istore_1
    //   574: iload_1
    //   575: iload #6
    //   577: if_icmpge -> 2328
    //   580: aload_3
    //   581: iload_1
    //   582: faload
    //   583: fstore #4
    //   585: fload #4
    //   587: iload #5
    //   589: i2f
    //   590: fsub
    //   591: invokestatic abs : (F)F
    //   594: ldc_w 0.01
    //   597: fcmpg
    //   598: ifge -> 2321
    //   601: aload #8
    //   603: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   606: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCAL_LENGTH : Landroid/hardware/camera2/CaptureRequest$Key;
    //   609: fload #4
    //   611: invokestatic valueOf : (F)Ljava/lang/Float;
    //   614: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   617: iconst_1
    //   618: istore_1
    //   619: goto -> 622
    //   622: iload_1
    //   623: ifne -> 2518
    //   626: iconst_2
    //   627: invokestatic setSystemErrorCode : (I)V
    //   630: iconst_0
    //   631: ireturn
    //   632: bipush #6
    //   634: invokestatic setSystemErrorCode : (I)V
    //   637: iconst_0
    //   638: ireturn
    //   639: bipush #6
    //   641: invokestatic setSystemErrorCode : (I)V
    //   644: iconst_0
    //   645: ireturn
    //   646: bipush #6
    //   648: invokestatic setSystemErrorCode : (I)V
    //   651: iconst_0
    //   652: ireturn
    //   653: aload_3
    //   654: checkcast java/lang/Number
    //   657: invokevirtual intValue : ()I
    //   660: istore_1
    //   661: iload_1
    //   662: ldc 806354944
    //   664: if_icmpeq -> 744
    //   667: iload_1
    //   668: ldc 809500672
    //   670: if_icmpeq -> 679
    //   673: iconst_3
    //   674: invokestatic setSystemErrorCode : (I)V
    //   677: iconst_0
    //   678: ireturn
    //   679: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_LOCK : Landroid/hardware/camera2/CaptureRequest$Key;
    //   682: ifnull -> 720
    //   685: aload #8
    //   687: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   690: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_LOCK : Landroid/hardware/camera2/CaptureRequest$Key;
    //   693: invokevirtual get : (Landroid/hardware/camera2/CaptureRequest$Key;)Ljava/lang/Object;
    //   696: checkcast java/lang/Boolean
    //   699: invokevirtual booleanValue : ()Z
    //   702: ifeq -> 720
    //   705: aload #8
    //   707: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   710: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_LOCK : Landroid/hardware/camera2/CaptureRequest$Key;
    //   713: iconst_0
    //   714: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   717: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   720: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   723: ifnull -> 2518
    //   726: aload #8
    //   728: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   731: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   734: iconst_1
    //   735: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   738: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   741: goto -> 2518
    //   744: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_LOCK : Landroid/hardware/camera2/CaptureRequest$Key;
    //   747: ifnonnull -> 757
    //   750: bipush #6
    //   752: invokestatic setSystemErrorCode : (I)V
    //   755: iconst_0
    //   756: ireturn
    //   757: aload #8
    //   759: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   762: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AWB_LOCK : Landroid/hardware/camera2/CaptureRequest$Key;
    //   765: iconst_1
    //   766: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   769: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   772: goto -> 2518
    //   775: bipush #6
    //   777: invokestatic setSystemErrorCode : (I)V
    //   780: iconst_0
    //   781: ireturn
    //   782: aload_3
    //   783: checkcast java/lang/Number
    //   786: invokevirtual floatValue : ()F
    //   789: fstore #4
    //   791: aload #8
    //   793: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   796: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   799: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   802: checkcast android/util/Range
    //   805: astore_3
    //   806: aload #8
    //   808: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   811: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   814: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   817: checkcast android/util/Rational
    //   820: astore #9
    //   822: getstatic com/vuforia/ar/pl/Camera2_Preview.EMPTY_RANGE : Landroid/util/Range;
    //   825: aload_3
    //   826: invokevirtual equals : (Ljava/lang/Object;)Z
    //   829: ifne -> 900
    //   832: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION : Landroid/hardware/camera2/CaptureRequest$Key;
    //   835: ifnull -> 900
    //   838: aload #9
    //   840: ifnull -> 900
    //   843: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   846: ifnonnull -> 852
    //   849: goto -> 900
    //   852: fload #4
    //   854: aload #9
    //   856: invokevirtual floatValue : ()F
    //   859: fdiv
    //   860: invokestatic round : (F)I
    //   863: istore_1
    //   864: aload_3
    //   865: iload_1
    //   866: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   869: invokevirtual contains : (Ljava/lang/Comparable;)Z
    //   872: ifeq -> 893
    //   875: aload #8
    //   877: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   880: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION : Landroid/hardware/camera2/CaptureRequest$Key;
    //   883: iload_1
    //   884: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   887: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   890: goto -> 2518
    //   893: bipush #6
    //   895: invokestatic setSystemErrorCode : (I)V
    //   898: iconst_0
    //   899: ireturn
    //   900: bipush #6
    //   902: invokestatic setSystemErrorCode : (I)V
    //   905: iconst_0
    //   906: ireturn
    //   907: bipush #6
    //   909: invokestatic setSystemErrorCode : (I)V
    //   912: iconst_0
    //   913: ireturn
    //   914: aload #8
    //   916: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   919: getstatic android/hardware/camera2/CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   922: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   925: checkcast android/util/Range
    //   928: astore #9
    //   930: aload #9
    //   932: ifnull -> 994
    //   935: getstatic android/hardware/camera2/CaptureRequest.SENSOR_EXPOSURE_TIME : Landroid/hardware/camera2/CaptureRequest$Key;
    //   938: ifnonnull -> 944
    //   941: goto -> 994
    //   944: aload_3
    //   945: checkcast java/lang/Number
    //   948: invokevirtual floatValue : ()F
    //   951: f2d
    //   952: ldc2_w 1.0E9
    //   955: dmul
    //   956: invokestatic round : (D)J
    //   959: invokestatic valueOf : (J)Ljava/lang/Long;
    //   962: astore_3
    //   963: aload #9
    //   965: aload_3
    //   966: invokevirtual contains : (Ljava/lang/Comparable;)Z
    //   969: ifeq -> 987
    //   972: aload #8
    //   974: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   977: getstatic android/hardware/camera2/CaptureRequest.SENSOR_EXPOSURE_TIME : Landroid/hardware/camera2/CaptureRequest$Key;
    //   980: aload_3
    //   981: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   984: goto -> 2518
    //   987: bipush #6
    //   989: invokestatic setSystemErrorCode : (I)V
    //   992: iconst_0
    //   993: ireturn
    //   994: bipush #6
    //   996: invokestatic setSystemErrorCode : (I)V
    //   999: iconst_0
    //   1000: ireturn
    //   1001: bipush #6
    //   1003: invokestatic setSystemErrorCode : (I)V
    //   1006: iconst_0
    //   1007: ireturn
    //   1008: aload #8
    //   1010: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   1013: getstatic android/hardware/camera2/CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   1016: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   1019: checkcast android/util/Range
    //   1022: astore #9
    //   1024: aload #9
    //   1026: ifnull -> 1083
    //   1029: getstatic android/hardware/camera2/CaptureRequest.SENSOR_SENSITIVITY : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1032: ifnonnull -> 1038
    //   1035: goto -> 1083
    //   1038: aload_3
    //   1039: checkcast java/lang/Number
    //   1042: invokevirtual intValue : ()I
    //   1045: istore_1
    //   1046: aload #9
    //   1048: iload_1
    //   1049: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1052: invokevirtual contains : (Ljava/lang/Comparable;)Z
    //   1055: ifeq -> 1076
    //   1058: aload #8
    //   1060: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1063: getstatic android/hardware/camera2/CaptureRequest.SENSOR_SENSITIVITY : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1066: iload_1
    //   1067: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1070: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1073: goto -> 2518
    //   1076: bipush #6
    //   1078: invokestatic setSystemErrorCode : (I)V
    //   1081: iconst_0
    //   1082: ireturn
    //   1083: bipush #6
    //   1085: invokestatic setSystemErrorCode : (I)V
    //   1088: iconst_0
    //   1089: ireturn
    //   1090: aload_3
    //   1091: checkcast java/lang/Number
    //   1094: invokevirtual intValue : ()I
    //   1097: istore_1
    //   1098: aload #8
    //   1100: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   1103: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   1106: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   1109: checkcast [I
    //   1112: astore_3
    //   1113: aload_3
    //   1114: ifnull -> 1232
    //   1117: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AE_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1120: ifnonnull -> 1126
    //   1123: goto -> 1232
    //   1126: iload_1
    //   1127: ldc 805310464
    //   1129: if_icmpeq -> 1192
    //   1132: iload_1
    //   1133: ldc 805322752
    //   1135: if_icmpeq -> 1150
    //   1138: iload_1
    //   1139: ldc 805339136
    //   1141: if_icmpeq -> 1192
    //   1144: iconst_3
    //   1145: invokestatic setSystemErrorCode : (I)V
    //   1148: iconst_0
    //   1149: ireturn
    //   1150: aload_3
    //   1151: arraylength
    //   1152: istore #6
    //   1154: iconst_0
    //   1155: istore #5
    //   1157: iload #5
    //   1159: istore_1
    //   1160: goto -> 2333
    //   1163: iload_1
    //   1164: ifne -> 1174
    //   1167: bipush #6
    //   1169: invokestatic setSystemErrorCode : (I)V
    //   1172: iconst_0
    //   1173: ireturn
    //   1174: aload #8
    //   1176: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1179: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AE_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1182: iconst_1
    //   1183: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1186: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1189: goto -> 2518
    //   1192: aload_3
    //   1193: arraylength
    //   1194: istore #6
    //   1196: iconst_0
    //   1197: istore #5
    //   1199: iload #5
    //   1201: istore_1
    //   1202: goto -> 2371
    //   1205: iload_1
    //   1206: ifne -> 1216
    //   1209: bipush #6
    //   1211: invokestatic setSystemErrorCode : (I)V
    //   1214: iconst_0
    //   1215: ireturn
    //   1216: aload #8
    //   1218: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1221: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AE_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1224: aload #9
    //   1226: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1229: goto -> 2518
    //   1232: bipush #6
    //   1234: invokestatic setSystemErrorCode : (I)V
    //   1237: iconst_0
    //   1238: ireturn
    //   1239: aload_3
    //   1240: checkcast [F
    //   1243: checkcast [F
    //   1246: astore_3
    //   1247: aload_3
    //   1248: arraylength
    //   1249: iconst_5
    //   1250: if_icmpeq -> 2408
    //   1253: iconst_2
    //   1254: invokestatic setSystemErrorCode : (I)V
    //   1257: iconst_0
    //   1258: ireturn
    //   1259: aload #8
    //   1261: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   1264: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_MAX_REGIONS_AF : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   1267: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   1270: checkcast java/lang/Integer
    //   1273: astore #9
    //   1275: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_REGIONS : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1278: ifnull -> 1466
    //   1281: aload #9
    //   1283: ifnull -> 1466
    //   1286: aload #9
    //   1288: invokevirtual intValue : ()I
    //   1291: ifne -> 1297
    //   1294: goto -> 1466
    //   1297: aload #8
    //   1299: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   1302: getstatic android/hardware/camera2/CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   1305: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   1308: checkcast android/graphics/Rect
    //   1311: astore #9
    //   1313: aload #9
    //   1315: ifnonnull -> 1325
    //   1318: bipush #6
    //   1320: invokestatic setSystemErrorCode : (I)V
    //   1323: iconst_0
    //   1324: ireturn
    //   1325: aload_3
    //   1326: iconst_0
    //   1327: faload
    //   1328: fstore #4
    //   1330: fload #4
    //   1332: aload #9
    //   1334: invokevirtual width : ()I
    //   1337: i2f
    //   1338: fmul
    //   1339: f2i
    //   1340: aload #9
    //   1342: invokevirtual width : ()I
    //   1345: iconst_1
    //   1346: isub
    //   1347: invokestatic min : (II)I
    //   1350: istore_1
    //   1351: aload_3
    //   1352: iconst_1
    //   1353: faload
    //   1354: fstore #4
    //   1356: new android/hardware/camera2/params/MeteringRectangle
    //   1359: dup
    //   1360: new android/graphics/Rect
    //   1363: dup
    //   1364: iload_1
    //   1365: fload #4
    //   1367: aload #9
    //   1369: invokevirtual height : ()I
    //   1372: i2f
    //   1373: fmul
    //   1374: f2i
    //   1375: aload #9
    //   1377: invokevirtual height : ()I
    //   1380: iconst_1
    //   1381: isub
    //   1382: invokestatic min : (II)I
    //   1385: aload_3
    //   1386: iconst_2
    //   1387: faload
    //   1388: aload #9
    //   1390: invokevirtual width : ()I
    //   1393: i2f
    //   1394: fmul
    //   1395: f2i
    //   1396: aload #9
    //   1398: invokevirtual width : ()I
    //   1401: iconst_1
    //   1402: isub
    //   1403: invokestatic min : (II)I
    //   1406: aload_3
    //   1407: iconst_3
    //   1408: faload
    //   1409: aload #9
    //   1411: invokevirtual height : ()I
    //   1414: i2f
    //   1415: fmul
    //   1416: f2i
    //   1417: aload #9
    //   1419: invokevirtual height : ()I
    //   1422: iconst_1
    //   1423: isub
    //   1424: invokestatic min : (II)I
    //   1427: invokespecial <init> : (IIII)V
    //   1430: aload_3
    //   1431: iconst_4
    //   1432: faload
    //   1433: ldc_w 1000.0
    //   1436: fmul
    //   1437: fconst_0
    //   1438: fadd
    //   1439: f2i
    //   1440: invokespecial <init> : (Landroid/graphics/Rect;I)V
    //   1443: astore_3
    //   1444: aload #8
    //   1446: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1449: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_REGIONS : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1452: iconst_1
    //   1453: anewarray android/hardware/camera2/params/MeteringRectangle
    //   1456: dup
    //   1457: iconst_0
    //   1458: aload_3
    //   1459: aastore
    //   1460: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1463: goto -> 1867
    //   1466: bipush #6
    //   1468: istore_1
    //   1469: iload_1
    //   1470: invokestatic setSystemErrorCode : (I)V
    //   1473: iconst_0
    //   1474: ireturn
    //   1475: iconst_2
    //   1476: invokestatic setSystemErrorCode : (I)V
    //   1479: iconst_0
    //   1480: ireturn
    //   1481: iconst_0
    //   1482: istore #7
    //   1484: bipush #6
    //   1486: istore_1
    //   1487: goto -> 2232
    //   1490: bipush #6
    //   1492: invokestatic setSystemErrorCode : (I)V
    //   1495: iconst_0
    //   1496: ireturn
    //   1497: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCUS_DISTANCE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1500: ifnonnull -> 1510
    //   1503: bipush #6
    //   1505: invokestatic setSystemErrorCode : (I)V
    //   1508: iconst_0
    //   1509: ireturn
    //   1510: aload_3
    //   1511: checkcast java/lang/Number
    //   1514: invokevirtual floatValue : ()F
    //   1517: fstore #4
    //   1519: aload #8
    //   1521: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1524: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCUS_DISTANCE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1527: fload #4
    //   1529: invokestatic valueOf : (F)Ljava/lang/Float;
    //   1532: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1535: goto -> 2518
    //   1538: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1541: astore #10
    //   1543: aload #10
    //   1545: ifnonnull -> 1555
    //   1548: bipush #6
    //   1550: invokestatic setSystemErrorCode : (I)V
    //   1553: iconst_0
    //   1554: ireturn
    //   1555: aload #8
    //   1557: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   1560: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   1563: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   1566: checkcast [I
    //   1569: astore #10
    //   1571: aload #10
    //   1573: invokestatic sort : ([I)V
    //   1576: aload_3
    //   1577: checkcast java/lang/Number
    //   1580: invokevirtual intValue : ()I
    //   1583: lookupswitch default -> 2500, 805306384 -> 1833, 805306400 -> 1833, 805306432 -> 1796, 805306496 -> 1759, 805306624 -> 1690, 805306880 -> 1646
    //   1640: iconst_3
    //   1641: invokestatic setSystemErrorCode : (I)V
    //   1644: iconst_0
    //   1645: ireturn
    //   1646: aload #10
    //   1648: iconst_0
    //   1649: invokestatic binarySearch : ([II)I
    //   1652: istore_1
    //   1653: iload_1
    //   1654: iconst_m1
    //   1655: if_icmpne -> 1674
    //   1658: bipush #6
    //   1660: invokestatic setSystemErrorCode : (I)V
    //   1663: iconst_0
    //   1664: ireturn
    //   1665: bipush #6
    //   1667: istore_1
    //   1668: iconst_0
    //   1669: istore #7
    //   1671: goto -> 2232
    //   1674: aload #8
    //   1676: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1679: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1682: aload #9
    //   1684: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1687: goto -> 2518
    //   1690: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCUS_DISTANCE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1693: astore_3
    //   1694: aload_3
    //   1695: ifnonnull -> 1711
    //   1698: bipush #6
    //   1700: invokestatic setSystemErrorCode : (I)V
    //   1703: iconst_0
    //   1704: ireturn
    //   1705: bipush #6
    //   1707: istore_1
    //   1708: goto -> 1875
    //   1711: aload #10
    //   1713: iconst_0
    //   1714: invokestatic binarySearch : ([II)I
    //   1717: iconst_m1
    //   1718: if_icmpne -> 1728
    //   1721: bipush #6
    //   1723: invokestatic setSystemErrorCode : (I)V
    //   1726: iconst_0
    //   1727: ireturn
    //   1728: aload #8
    //   1730: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1733: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1736: aload #9
    //   1738: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1741: aload #8
    //   1743: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1746: getstatic android/hardware/camera2/CaptureRequest.LENS_FOCUS_DISTANCE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1749: fconst_0
    //   1750: invokestatic valueOf : (F)Ljava/lang/Float;
    //   1753: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1756: goto -> 2518
    //   1759: aload #10
    //   1761: iconst_2
    //   1762: invokestatic binarySearch : ([II)I
    //   1765: istore_1
    //   1766: iload_1
    //   1767: iconst_m1
    //   1768: if_icmpne -> 1778
    //   1771: bipush #6
    //   1773: invokestatic setSystemErrorCode : (I)V
    //   1776: iconst_0
    //   1777: ireturn
    //   1778: aload #8
    //   1780: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1783: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1786: iconst_2
    //   1787: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1790: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1793: goto -> 1867
    //   1796: aload #10
    //   1798: iconst_3
    //   1799: invokestatic binarySearch : ([II)I
    //   1802: istore_1
    //   1803: iload_1
    //   1804: iconst_m1
    //   1805: if_icmpne -> 1815
    //   1808: bipush #6
    //   1810: invokestatic setSystemErrorCode : (I)V
    //   1813: iconst_0
    //   1814: ireturn
    //   1815: aload #8
    //   1817: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1820: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1823: iconst_3
    //   1824: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1827: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1830: goto -> 2518
    //   1833: aload #10
    //   1835: iconst_1
    //   1836: invokestatic binarySearch : ([II)I
    //   1839: istore_1
    //   1840: iload_1
    //   1841: iconst_m1
    //   1842: if_icmpne -> 1852
    //   1845: bipush #6
    //   1847: invokestatic setSystemErrorCode : (I)V
    //   1850: iconst_0
    //   1851: ireturn
    //   1852: aload #8
    //   1854: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1857: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1860: iconst_1
    //   1861: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1864: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1867: iconst_1
    //   1868: istore_1
    //   1869: goto -> 1994
    //   1872: bipush #6
    //   1874: istore_1
    //   1875: iconst_0
    //   1876: istore #7
    //   1878: goto -> 2232
    //   1881: getstatic android/hardware/camera2/CaptureRequest.FLASH_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1884: astore #10
    //   1886: aload #10
    //   1888: ifnonnull -> 1904
    //   1891: bipush #6
    //   1893: invokestatic setSystemErrorCode : (I)V
    //   1896: iconst_0
    //   1897: ireturn
    //   1898: bipush #6
    //   1900: istore_1
    //   1901: goto -> 1875
    //   1904: aload_3
    //   1905: checkcast java/lang/Number
    //   1908: invokevirtual intValue : ()I
    //   1911: istore_1
    //   1912: iload_1
    //   1913: tableswitch default -> 1944, 805306369 -> 1978, 805306370 -> 1960, 805306371 -> 1944, 805306372 -> 1954
    //   1944: iconst_0
    //   1945: istore #7
    //   1947: iconst_3
    //   1948: invokestatic setSystemErrorCode : (I)V
    //   1951: iload #7
    //   1953: ireturn
    //   1954: iconst_3
    //   1955: invokestatic setSystemErrorCode : (I)V
    //   1958: iconst_0
    //   1959: ireturn
    //   1960: aload #8
    //   1962: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1965: getstatic android/hardware/camera2/CaptureRequest.FLASH_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1968: iconst_2
    //   1969: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   1972: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1975: goto -> 2518
    //   1978: aload #8
    //   1980: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   1983: getstatic android/hardware/camera2/CaptureRequest.FLASH_MODE : Landroid/hardware/camera2/CaptureRequest$Key;
    //   1986: aload #9
    //   1988: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   1991: goto -> 2518
    //   1994: aload #8
    //   1996: getfield session : Landroid/hardware/camera2/CameraCaptureSession;
    //   1999: ifnull -> 2218
    //   2002: aload #8
    //   2004: getfield session : Landroid/hardware/camera2/CameraCaptureSession;
    //   2007: aload #8
    //   2009: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   2012: invokevirtual build : ()Landroid/hardware/camera2/CaptureRequest;
    //   2015: new com/vuforia/ar/pl/Camera2_Preview$OnFrameCapturedCallback
    //   2018: dup
    //   2019: aload_0
    //   2020: aload #8
    //   2022: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera2_Preview;Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   2025: aload #8
    //   2027: getfield handler : Landroid/os/Handler;
    //   2030: invokevirtual setRepeatingRequest : (Landroid/hardware/camera2/CaptureRequest;Landroid/hardware/camera2/CameraCaptureSession$CaptureCallback;Landroid/os/Handler;)I
    //   2033: pop
    //   2034: iload_1
    //   2035: ifeq -> 2218
    //   2038: iload_2
    //   2039: ldc 536870914
    //   2041: if_icmpeq -> 2052
    //   2044: iload_2
    //   2045: ldc 536870928
    //   2047: if_icmpeq -> 2177
    //   2050: iconst_1
    //   2051: ireturn
    //   2052: aload #8
    //   2054: getfield characteristics : Landroid/hardware/camera2/CameraCharacteristics;
    //   2057: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_MAX_REGIONS_AF : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   2060: invokevirtual get : (Landroid/hardware/camera2/CameraCharacteristics$Key;)Ljava/lang/Object;
    //   2063: checkcast java/lang/Integer
    //   2066: astore_3
    //   2067: aload_3
    //   2068: ifnull -> 2177
    //   2071: getstatic android/hardware/camera2/CameraCharacteristics.CONTROL_MAX_REGIONS_AF : Landroid/hardware/camera2/CameraCharacteristics$Key;
    //   2074: ifnull -> 2177
    //   2077: aload_3
    //   2078: invokevirtual intValue : ()I
    //   2081: ifle -> 2177
    //   2084: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_REGIONS : Landroid/hardware/camera2/CaptureRequest$Key;
    //   2087: ifnull -> 2177
    //   2090: aload #8
    //   2092: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   2095: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_REGIONS : Landroid/hardware/camera2/CaptureRequest$Key;
    //   2098: invokevirtual get : (Landroid/hardware/camera2/CaptureRequest$Key;)Ljava/lang/Object;
    //   2101: checkcast [Landroid/hardware/camera2/params/MeteringRectangle;
    //   2104: astore_3
    //   2105: aload_3
    //   2106: ifnull -> 2177
    //   2109: aload_3
    //   2110: arraylength
    //   2111: ifle -> 2177
    //   2114: aload_3
    //   2115: arraylength
    //   2116: anewarray android/hardware/camera2/params/MeteringRectangle
    //   2119: astore #9
    //   2121: aload_3
    //   2122: arraylength
    //   2123: istore #5
    //   2125: iconst_0
    //   2126: istore_2
    //   2127: iconst_0
    //   2128: istore_1
    //   2129: iload_2
    //   2130: iload #5
    //   2132: if_icmpge -> 2164
    //   2135: aload #9
    //   2137: iload_1
    //   2138: new android/hardware/camera2/params/MeteringRectangle
    //   2141: dup
    //   2142: aload_3
    //   2143: iload_2
    //   2144: aaload
    //   2145: invokevirtual getRect : ()Landroid/graphics/Rect;
    //   2148: iconst_0
    //   2149: invokespecial <init> : (Landroid/graphics/Rect;I)V
    //   2152: aastore
    //   2153: iload_2
    //   2154: iconst_1
    //   2155: iadd
    //   2156: istore_2
    //   2157: iload_1
    //   2158: iconst_1
    //   2159: iadd
    //   2160: istore_1
    //   2161: goto -> 2129
    //   2164: aload #8
    //   2166: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
    //   2169: getstatic android/hardware/camera2/CaptureRequest.CONTROL_AF_REGIONS : Landroid/hardware/camera2/CaptureRequest$Key;
    //   2172: aload #9
    //   2174: invokevirtual set : (Landroid/hardware/camera2/CaptureRequest$Key;Ljava/lang/Object;)V
    //   2177: new com/vuforia/ar/pl/Camera2_Preview$AutofocusRunner
    //   2180: dup
    //   2181: aload_0
    //   2182: aload #8
    //   2184: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera2_Preview;Lcom/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo;)V
    //   2187: invokevirtual triggerAutofocus : ()Z
    //   2190: istore #7
    //   2192: iload #7
    //   2194: ifne -> 2218
    //   2197: bipush #6
    //   2199: invokestatic setSystemErrorCode : (I)V
    //   2202: iconst_0
    //   2203: ireturn
    //   2204: bipush #6
    //   2206: invokestatic setSystemErrorCode : (I)V
    //   2209: iconst_0
    //   2210: ireturn
    //   2211: bipush #6
    //   2213: invokestatic setSystemErrorCode : (I)V
    //   2216: iconst_0
    //   2217: ireturn
    //   2218: iconst_1
    //   2219: ireturn
    //   2220: iconst_0
    //   2221: istore #7
    //   2223: goto -> 2229
    //   2226: iconst_0
    //   2227: istore #7
    //   2229: bipush #6
    //   2231: istore_1
    //   2232: iload_1
    //   2233: invokestatic setSystemErrorCode : (I)V
    //   2236: iload #7
    //   2238: ireturn
    //   2239: iconst_4
    //   2240: invokestatic setSystemErrorCode : (I)V
    //   2243: iconst_0
    //   2244: ireturn
    //   2245: astore_3
    //   2246: goto -> 1481
    //   2249: astore_3
    //   2250: goto -> 2503
    //   2253: astore_3
    //   2254: goto -> 1872
    //   2257: astore_3
    //   2258: goto -> 2220
    //   2261: astore_3
    //   2262: goto -> 1875
    //   2265: astore_3
    //   2266: goto -> 2491
    //   2269: astore_3
    //   2270: goto -> 1898
    //   2273: astore_3
    //   2274: goto -> 1665
    //   2277: astore_3
    //   2278: goto -> 1705
    //   2281: astore_3
    //   2282: goto -> 2509
    //   2285: astore_3
    //   2286: goto -> 2226
    //   2289: astore_3
    //   2290: goto -> 2229
    //   2293: astore_3
    //   2294: goto -> 2211
    //   2297: astore_3
    //   2298: goto -> 2204
    //   2301: astore_3
    //   2302: goto -> 2204
    //   2305: iconst_0
    //   2306: istore_1
    //   2307: goto -> 285
    //   2310: iconst_0
    //   2311: istore #5
    //   2313: goto -> 325
    //   2316: iconst_1
    //   2317: istore_1
    //   2318: goto -> 476
    //   2321: iload_1
    //   2322: iconst_1
    //   2323: iadd
    //   2324: istore_1
    //   2325: goto -> 574
    //   2328: iconst_0
    //   2329: istore_1
    //   2330: goto -> 622
    //   2333: iload #5
    //   2335: iload #6
    //   2337: if_icmpge -> 1163
    //   2340: aload_3
    //   2341: iload #5
    //   2343: iaload
    //   2344: iconst_1
    //   2345: if_icmpne -> 2353
    //   2348: iconst_1
    //   2349: istore_1
    //   2350: goto -> 2355
    //   2353: iconst_0
    //   2354: istore_1
    //   2355: iload_1
    //   2356: ifeq -> 2362
    //   2359: goto -> 1163
    //   2362: iload #5
    //   2364: iconst_1
    //   2365: iadd
    //   2366: istore #5
    //   2368: goto -> 2333
    //   2371: iload #5
    //   2373: iload #6
    //   2375: if_icmpge -> 1205
    //   2378: aload_3
    //   2379: iload #5
    //   2381: iaload
    //   2382: ifne -> 2390
    //   2385: iconst_1
    //   2386: istore_1
    //   2387: goto -> 2392
    //   2390: iconst_0
    //   2391: istore_1
    //   2392: iload_1
    //   2393: ifeq -> 2399
    //   2396: goto -> 1205
    //   2399: iload #5
    //   2401: iconst_1
    //   2402: iadd
    //   2403: istore #5
    //   2405: goto -> 2371
    //   2408: aload_3
    //   2409: iconst_0
    //   2410: faload
    //   2411: fconst_0
    //   2412: fcmpg
    //   2413: iflt -> 1475
    //   2416: aload_3
    //   2417: iconst_0
    //   2418: faload
    //   2419: fconst_1
    //   2420: fcmpl
    //   2421: ifgt -> 1475
    //   2424: aload_3
    //   2425: iconst_1
    //   2426: faload
    //   2427: fconst_0
    //   2428: fcmpg
    //   2429: iflt -> 1475
    //   2432: aload_3
    //   2433: iconst_1
    //   2434: faload
    //   2435: fconst_1
    //   2436: fcmpl
    //   2437: ifgt -> 1475
    //   2440: aload_3
    //   2441: iconst_2
    //   2442: faload
    //   2443: fconst_0
    //   2444: fcmpg
    //   2445: iflt -> 1475
    //   2448: aload_3
    //   2449: iconst_2
    //   2450: faload
    //   2451: fconst_1
    //   2452: fcmpl
    //   2453: ifgt -> 1475
    //   2456: aload_3
    //   2457: iconst_3
    //   2458: faload
    //   2459: fconst_0
    //   2460: fcmpg
    //   2461: iflt -> 1475
    //   2464: aload_3
    //   2465: iconst_3
    //   2466: faload
    //   2467: fconst_1
    //   2468: fcmpl
    //   2469: ifgt -> 1475
    //   2472: aload_3
    //   2473: iconst_4
    //   2474: faload
    //   2475: fconst_0
    //   2476: fcmpg
    //   2477: iflt -> 1475
    //   2480: aload_3
    //   2481: iconst_4
    //   2482: faload
    //   2483: fconst_1
    //   2484: fcmpl
    //   2485: ifle -> 1259
    //   2488: goto -> 1475
    //   2491: iconst_0
    //   2492: istore #7
    //   2494: bipush #6
    //   2496: istore_1
    //   2497: goto -> 2232
    //   2500: goto -> 1640
    //   2503: iconst_0
    //   2504: istore #7
    //   2506: goto -> 2229
    //   2509: bipush #6
    //   2511: istore_1
    //   2512: iconst_0
    //   2513: istore #7
    //   2515: goto -> 2232
    //   2518: iconst_0
    //   2519: istore_1
    //   2520: goto -> 1994
    // Exception table:
    //   from	to	target	type
    //   246	262	2245	java/lang/Exception
    //   267	280	2245	java/lang/Exception
    //   285	301	2245	java/lang/Exception
    //   306	319	2245	java/lang/Exception
    //   334	339	2245	java/lang/Exception
    //   341	350	2245	java/lang/Exception
    //   354	367	2245	java/lang/Exception
    //   372	385	2245	java/lang/Exception
    //   394	409	2245	java/lang/Exception
    //   417	432	2245	java/lang/Exception
    //   435	446	2245	java/lang/Exception
    //   448	468	2245	java/lang/Exception
    //   476	486	2245	java/lang/Exception
    //   489	494	2245	java/lang/Exception
    //   496	501	2245	java/lang/Exception
    //   503	508	2245	java/lang/Exception
    //   510	515	2245	java/lang/Exception
    //   517	522	2245	java/lang/Exception
    //   524	529	2245	java/lang/Exception
    //   531	555	2245	java/lang/Exception
    //   559	565	2245	java/lang/Exception
    //   568	572	2245	java/lang/Exception
    //   585	617	2245	java/lang/Exception
    //   626	630	2245	java/lang/Exception
    //   632	637	2245	java/lang/Exception
    //   639	644	2245	java/lang/Exception
    //   646	651	2245	java/lang/Exception
    //   653	661	2245	java/lang/Exception
    //   673	677	2245	java/lang/Exception
    //   679	720	2245	java/lang/Exception
    //   720	741	2245	java/lang/Exception
    //   744	755	2245	java/lang/Exception
    //   757	772	2245	java/lang/Exception
    //   775	780	2245	java/lang/Exception
    //   782	838	2245	java/lang/Exception
    //   843	849	2245	java/lang/Exception
    //   852	890	2245	java/lang/Exception
    //   893	898	2245	java/lang/Exception
    //   900	905	2245	java/lang/Exception
    //   907	912	2245	java/lang/Exception
    //   914	930	2245	java/lang/Exception
    //   935	941	2245	java/lang/Exception
    //   944	984	2245	java/lang/Exception
    //   987	992	2245	java/lang/Exception
    //   994	999	2245	java/lang/Exception
    //   1001	1006	2245	java/lang/Exception
    //   1008	1024	2245	java/lang/Exception
    //   1029	1035	2245	java/lang/Exception
    //   1038	1073	2245	java/lang/Exception
    //   1076	1081	2245	java/lang/Exception
    //   1083	1088	2245	java/lang/Exception
    //   1090	1113	2245	java/lang/Exception
    //   1117	1123	2245	java/lang/Exception
    //   1144	1148	2245	java/lang/Exception
    //   1150	1154	2245	java/lang/Exception
    //   1167	1172	2245	java/lang/Exception
    //   1174	1189	2245	java/lang/Exception
    //   1192	1196	2245	java/lang/Exception
    //   1209	1214	2245	java/lang/Exception
    //   1216	1229	2245	java/lang/Exception
    //   1232	1237	2245	java/lang/Exception
    //   1239	1257	2245	java/lang/Exception
    //   1259	1281	2245	java/lang/Exception
    //   1286	1294	2245	java/lang/Exception
    //   1297	1313	2245	java/lang/Exception
    //   1318	1323	2245	java/lang/Exception
    //   1330	1351	2249	java/lang/Exception
    //   1356	1444	2253	java/lang/Exception
    //   1444	1463	2253	java/lang/Exception
    //   1469	1473	2261	java/lang/Exception
    //   1475	1479	2253	java/lang/Exception
    //   1490	1495	2265	java/lang/Exception
    //   1497	1508	2265	java/lang/Exception
    //   1510	1535	2253	java/lang/Exception
    //   1538	1543	2253	java/lang/Exception
    //   1548	1553	2269	java/lang/Exception
    //   1555	1640	2253	java/lang/Exception
    //   1640	1644	2253	java/lang/Exception
    //   1646	1653	2249	java/lang/Exception
    //   1658	1663	2273	java/lang/Exception
    //   1674	1687	2253	java/lang/Exception
    //   1690	1694	2253	java/lang/Exception
    //   1698	1703	2277	java/lang/Exception
    //   1711	1726	2281	java/lang/Exception
    //   1728	1756	2253	java/lang/Exception
    //   1759	1766	2253	java/lang/Exception
    //   1771	1776	2269	java/lang/Exception
    //   1778	1793	2253	java/lang/Exception
    //   1796	1803	2253	java/lang/Exception
    //   1808	1813	2269	java/lang/Exception
    //   1815	1830	2253	java/lang/Exception
    //   1833	1840	2253	java/lang/Exception
    //   1845	1850	2269	java/lang/Exception
    //   1852	1867	2253	java/lang/Exception
    //   1881	1886	2285	java/lang/Exception
    //   1891	1896	2269	java/lang/Exception
    //   1904	1912	2257	java/lang/Exception
    //   1947	1951	2289	java/lang/Exception
    //   1954	1958	2257	java/lang/Exception
    //   1960	1975	2253	java/lang/Exception
    //   1978	1991	2285	java/lang/Exception
    //   2002	2034	2293	android/hardware/camera2/CameraAccessException
    //   2052	2067	2297	java/lang/Exception
    //   2071	2105	2297	java/lang/Exception
    //   2109	2125	2297	java/lang/Exception
    //   2135	2153	2297	java/lang/Exception
    //   2164	2177	2297	java/lang/Exception
    //   2177	2192	2297	java/lang/Exception
    //   2197	2202	2301	java/lang/Exception
  }
  
  boolean setUntypedCameraParameter(int paramInt, String paramString, Object paramObject) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.builder == null || cameraCacheInfo.characteristics == null || paramString == null || paramObject == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    List<CaptureRequest.Key> list = cameraCacheInfo.characteristics.getAvailableCaptureRequestKeys();
    paramInt = 0;
    while (paramInt < list.size()) {
      CaptureRequest.Key key = list.get(paramInt);
      if (!key.getName().equals(paramString)) {
        paramInt++;
        continue;
      } 
      Object object2 = cameraCacheInfo.builder.get(key);
      boolean bool = object2 instanceof Integer;
      if (!bool && !(object2 instanceof Float) && !(object2 instanceof Boolean) && !(object2 instanceof Byte) && !(object2 instanceof Long))
        return false; 
      Object object1 = paramObject;
      if (object2 instanceof Byte) {
        object1 = paramObject;
        if (paramObject instanceof Long)
          object1 = new Byte(((Long)paramObject).byteValue()); 
      } 
      paramObject = object1;
      if (bool) {
        paramObject = object1;
        if (object1 instanceof Long)
          paramObject = new Integer(((Long)object1).intValue()); 
      } 
      if (!object2.getClass().equals(paramObject.getClass()))
        return false; 
      try {
        cameraCacheInfo.builder.set(key, paramObject);
        if (cameraCacheInfo.session != null)
          cameraCacheInfo.session.setRepeatingRequest(cameraCacheInfo.builder.build(), new OnFrameCapturedCallback(cameraCacheInfo), cameraCacheInfo.handler); 
        return true;
      } catch (Exception exception) {
        SystemTools.setSystemErrorCode(6);
        return false;
      } 
    } 
    SystemTools.setSystemErrorCode(6);
    return false;
  }
  
  public boolean start(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    if (!setupPreviewBuffer(cameraCacheInfo)) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    try {
      if (cameraCacheInfo.session == null) {
        this.mOpenCloseSemaphore.acquire();
        cameraCacheInfo.device.createCaptureSession(cameraCacheInfo.surfaces, new CameraCaptureSession.StateCallback() {
              public void onConfigureFailed(CameraCaptureSession param1CameraCaptureSession) {
                param1CameraCaptureSession.close();
                Camera2_Preview.this.mOpenCloseSemaphore.release();
              }
              
              public void onConfigured(CameraCaptureSession param1CameraCaptureSession) {
                // Byte code:
                //   0: aload_0
                //   1: getfield this$0 : Lcom/vuforia/ar/pl/Camera2_Preview;
                //   4: invokestatic access$100 : (Lcom/vuforia/ar/pl/Camera2_Preview;)Ljava/util/Vector;
                //   7: invokevirtual iterator : ()Ljava/util/Iterator;
                //   10: astore_3
                //   11: aload_3
                //   12: invokeinterface hasNext : ()Z
                //   17: ifeq -> 50
                //   20: aload_3
                //   21: invokeinterface next : ()Ljava/lang/Object;
                //   26: checkcast com/vuforia/ar/pl/Camera2_Preview$CameraCacheInfo
                //   29: astore_2
                //   30: aload_2
                //   31: getfield deviceIDString : Ljava/lang/String;
                //   34: aload_1
                //   35: invokevirtual getDevice : ()Landroid/hardware/camera2/CameraDevice;
                //   38: invokevirtual getId : ()Ljava/lang/String;
                //   41: invokevirtual equals : (Ljava/lang/Object;)Z
                //   44: ifeq -> 11
                //   47: goto -> 52
                //   50: aconst_null
                //   51: astore_2
                //   52: aload_2
                //   53: aload_1
                //   54: putfield session : Landroid/hardware/camera2/CameraCaptureSession;
                //   57: aload_2
                //   58: getfield surfaces : Ljava/util/List;
                //   61: invokeinterface iterator : ()Ljava/util/Iterator;
                //   66: astore_1
                //   67: aload_1
                //   68: invokeinterface hasNext : ()Z
                //   73: ifeq -> 97
                //   76: aload_1
                //   77: invokeinterface next : ()Ljava/lang/Object;
                //   82: checkcast android/view/Surface
                //   85: astore_3
                //   86: aload_2
                //   87: getfield builder : Landroid/hardware/camera2/CaptureRequest$Builder;
                //   90: aload_3
                //   91: invokevirtual addTarget : (Landroid/view/Surface;)V
                //   94: goto -> 67
                //   97: aload_0
                //   98: getfield this$0 : Lcom/vuforia/ar/pl/Camera2_Preview;
                //   101: invokestatic access$600 : (Lcom/vuforia/ar/pl/Camera2_Preview;)Ljava/util/concurrent/Semaphore;
                //   104: invokevirtual release : ()V
                //   107: return
              }
            },  cameraCacheInfo.handler);
        this.mOpenCloseSemaphore.acquire();
        this.mOpenCloseSemaphore.release();
        if (cameraCacheInfo.session == null) {
          SystemTools.setSystemErrorCode(6);
          return false;
        } 
      } 
      cameraCacheInfo.session.setRepeatingRequest(cameraCacheInfo.builder.build(), new OnFrameCapturedCallback(cameraCacheInfo), cameraCacheInfo.handler);
      cameraCacheInfo.status = 268443651;
      this.mCameraCacheInfoIndexCache.put(cameraCacheInfo.reader, Integer.valueOf(paramInt));
      return true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
  }
  
  public boolean stop(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    try {
      cameraCacheInfo.session.abortCaptures();
      cameraCacheInfo.status = 268443650;
      return true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
  }
  
  private class AutofocusRunner extends CameraCaptureSession.CaptureCallback {
    private Camera2_Preview.CameraCacheInfo mCCI;
    
    private CaptureRequest mCancelRequest;
    
    private CaptureRequest mFocusRequest;
    
    public AutofocusRunner(Camera2_Preview.CameraCacheInfo param1CameraCacheInfo) {
      this.mCCI = param1CameraCacheInfo;
      this.mCancelRequest = null;
      this.mFocusRequest = null;
    }
    
    public void onCaptureCompleted(CameraCaptureSession param1CameraCaptureSession, CaptureRequest param1CaptureRequest, TotalCaptureResult param1TotalCaptureResult) {
      super.onCaptureCompleted(param1CameraCaptureSession, param1CaptureRequest, param1TotalCaptureResult);
      Integer integer = (Integer)param1TotalCaptureResult.get(CaptureResult.CONTROL_AF_STATE);
      if (param1CaptureRequest.equals(this.mCancelRequest) && integer.intValue() == 0)
        try {
          param1CameraCaptureSession.capture(this.mFocusRequest, this, this.mCCI.handler);
          return;
        } catch (CameraAccessException cameraAccessException) {
          return;
        }  
      if (param1CaptureRequest.equals(this.mFocusRequest) && (integer.intValue() == 4 || integer.intValue() == 5))
        this.mCCI.isAutoFocusing = false; 
    }
    
    public boolean triggerAutofocus() throws CameraAccessException {
      Camera2_Preview.CameraCacheInfo cameraCacheInfo = this.mCCI;
      Integer integer = Integer.valueOf(0);
      if (cameraCacheInfo != null && cameraCacheInfo.builder != null) {
        if (this.mCCI.session == null)
          return false; 
        Integer integer1 = (Integer)this.mCCI.builder.get(CaptureRequest.CONTROL_AF_MODE);
        if (CaptureRequest.CONTROL_AF_MODE != null) {
          if (integer1 == null)
            return false; 
          if (integer1.intValue() != 1 && integer1.intValue() != 2)
            return false; 
          this.mCCI.isAutoFocusing = true;
          this.mCCI.builder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(2));
          this.mCancelRequest = this.mCCI.builder.build();
          this.mCCI.builder.set(CaptureRequest.CONTROL_AF_TRIGGER, integer);
          this.mCCI.builder.set(CaptureRequest.CONTROL_AF_TRIGGER, Integer.valueOf(1));
          this.mFocusRequest = this.mCCI.builder.build();
          this.mCCI.builder.set(CaptureRequest.CONTROL_AF_TRIGGER, integer);
          this.mCCI.session.capture(this.mCancelRequest, this, this.mCCI.handler);
          return true;
        } 
      } 
      return false;
    }
  }
  
  public class CameraCacheInfo {
    int bufferFormatPL;
    
    int bufferHeight;
    
    int bufferWidth;
    
    CaptureRequest.Builder builder;
    
    int[] caps;
    
    CameraCharacteristics characteristics;
    
    CameraDevice device;
    
    long deviceHandle;
    
    int deviceID;
    
    String deviceIDString;
    
    Handler handler;
    
    Semaphore imageSemaphore;
    
    Image[] images;
    
    boolean isAutoFocusing;
    
    CaptureResult lastResult;
    
    int overrideFormatAndroid;
    
    int overrideFormatPL;
    
    int overrideHeight;
    
    int overrideWidth;
    
    ImageReader reader;
    
    int requestFormatAndroid;
    
    int requestFormatPL;
    
    int requestFramerate;
    
    int requestHeight;
    
    int requestWidth;
    
    CameraCaptureSession session;
    
    int status;
    
    List<Surface> surfaces;
    
    HandlerThread thread;
  }
  
  public class FrameInfo {
    long exposureTime;
    
    int iso;
    
    long timestamp;
  }
  
  private class OnCameraDataAvailable implements ImageReader.OnImageAvailableListener {
    private int[] actualBufferSize = null;
    
    private int actualCaptureFormat = 268439808;
    
    private OnCameraDataAvailable() {}
    
    private int[] calculateActualBufferSize(long param1Long1, long param1Long2, long param1Long3, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      int[] arrayOfInt;
      if (((param1Int5 == 268439815 || param1Int5 == 268439817) && param1Long1 + (param1Int4 * param1Int3) != param1Long2) || ((param1Int5 == 268439818 || param1Int5 == 268439828) && (param1Long1 + (param1Int4 * param1Int3) != param1Long2 || param1Long2 + (param1Int4 / 2 * param1Int3 / 2) != param1Long3))) {
        param1Int3 = 1;
      } else {
        param1Int3 = 0;
      } 
      if (param1Int3 != 0) {
        int[] arrayOfInt1 = new int[4];
        arrayOfInt1[0] = param1Int1;
        arrayOfInt1[1] = (int)((param1Long2 - param1Long1) / param1Int1);
        arrayOfInt1[2] = param1Int2;
        if (param1Int5 == 268439815 || param1Int5 == 268439817) {
          arrayOfInt1[3] = param1Int4 / 2;
          return arrayOfInt1;
        } 
        if (param1Int5 != 268439818) {
          arrayOfInt = arrayOfInt1;
          if (param1Int5 == 268439828) {
            arrayOfInt1[3] = (int)((param1Long3 - param1Long2) / param1Int2);
            return arrayOfInt1;
          } 
        } else {
          arrayOfInt1[3] = (int)((param1Long3 - param1Long2) / param1Int2);
          return arrayOfInt1;
        } 
      } else {
        arrayOfInt = null;
      } 
      return arrayOfInt;
    }
    
    private int getImageFormat(Image param1Image) {
      if (param1Image != null && (param1Image.getPlanes()).length == 3) {
        if (param1Image.getFormat() != 35)
          return 268439808; 
        Image.Plane plane1 = param1Image.getPlanes()[0];
        Image.Plane plane2 = param1Image.getPlanes()[1];
        Image.Plane plane3 = param1Image.getPlanes()[2];
        if (plane1.getPixelStride() != 1)
          return 268439808; 
        if (plane2.getPixelStride() == plane3.getPixelStride()) {
          if (plane2.getRowStride() != plane3.getRowStride())
            return 268439808; 
          long[] arrayOfLong = new long[3];
          arrayOfLong[0] = Camera2_Preview.this.getBufferAddress(plane1.getBuffer());
          arrayOfLong[1] = Camera2_Preview.this.getBufferAddress(plane2.getBuffer());
          arrayOfLong[2] = Camera2_Preview.this.getBufferAddress(plane3.getBuffer());
          if (arrayOfLong[0] != 0L && arrayOfLong[1] != 0L) {
            if (arrayOfLong[2] == 0L)
              return 268439808; 
            if (plane2.getPixelStride() == 2) {
              if (arrayOfLong[1] + 1L == arrayOfLong[2]) {
                this.actualBufferSize = calculateActualBufferSize(arrayOfLong[0], arrayOfLong[1], arrayOfLong[2], plane1.getRowStride(), plane2.getRowStride(), param1Image.getWidth(), param1Image.getHeight(), 268439815);
                return 268439815;
              } 
              if (arrayOfLong[2] + 1L == arrayOfLong[1]) {
                this.actualBufferSize = calculateActualBufferSize(arrayOfLong[0], arrayOfLong[2], arrayOfLong[1], plane1.getRowStride(), plane2.getRowStride(), param1Image.getWidth(), param1Image.getHeight(), 268439817);
                return 268439817;
              } 
            } 
            if (plane2.getPixelStride() == 1) {
              if (arrayOfLong[1] < arrayOfLong[2]) {
                this.actualBufferSize = calculateActualBufferSize(arrayOfLong[0], arrayOfLong[1], arrayOfLong[2], plane1.getRowStride(), plane2.getRowStride(), param1Image.getWidth(), param1Image.getHeight(), 268439818);
                return 268439818;
              } 
              this.actualBufferSize = calculateActualBufferSize(arrayOfLong[0], arrayOfLong[2], arrayOfLong[1], plane1.getRowStride(), plane2.getRowStride(), param1Image.getWidth(), param1Image.getHeight(), 268439828);
              return 268439828;
            } 
            DebugLog.LOGE("Camera2_Preview", "Unable to detect a supported image format, Unknown Image Format");
          } 
        } 
      } 
      return 268439808;
    }
    
    public void onImageAvailable(ImageReader param1ImageReader) {
      Trace.beginSection("onImageAvailable (java)");
      Integer integer = (Integer)Camera2_Preview.this.mCameraCacheInfoIndexCache.get(param1ImageReader);
      if (integer == null) {
        DebugLog.LOGE("Camera2_Preview", "Unable to find reader in the index cache!");
        Trace.endSection();
        return;
      } 
      Camera2_Preview.CameraCacheInfo cameraCacheInfo = Camera2_Preview.this.mCameraCacheInfos.get(integer.intValue());
      if (cameraCacheInfo == null) {
        DebugLog.LOGE("Camera2_Preview", "Unable to find CCI in list!");
        Trace.endSection();
        return;
      } 
      if (!cameraCacheInfo.imageSemaphore.tryAcquire()) {
        DebugLog.LOGE("Camera2_Preview", "Unable to aquire image semaphore, need to free some buffers!!");
        Trace.endSection();
        return;
      } 
      if (param1ImageReader.getMaxImages() > 0) {
        Image image = param1ImageReader.acquireLatestImage();
        if (image != null) {
          Camera2_Preview.FrameInfo frameInfo = new Camera2_Preview.FrameInfo();
          frameInfo.timestamp = image.getTimestamp();
          CaptureResult captureResult = cameraCacheInfo.lastResult;
          if (captureResult != null) {
            Long long_ = (Long)captureResult.get(CaptureResult.SENSOR_EXPOSURE_TIME);
            if (long_ != null) {
              frameInfo.exposureTime = long_.longValue();
              frameInfo.timestamp += long_.longValue();
            } 
            Integer integer1 = (Integer)captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
            if (integer1 != null)
              frameInfo.iso = integer1.intValue(); 
          } 
          if (this.actualCaptureFormat == 268439808)
            this.actualCaptureFormat = getImageFormat(image); 
          Camera2_Preview.this.newFrameAvailable(cameraCacheInfo.deviceHandle, integer.intValue(), cameraCacheInfo.bufferWidth, cameraCacheInfo.bufferHeight, this.actualBufferSize, this.actualCaptureFormat, image.getPlanes()[0].getBuffer(), frameInfo);
          image.close();
        } 
      } 
      cameraCacheInfo.imageSemaphore.release();
      Trace.endSection();
    }
  }
  
  private class OnFrameCapturedCallback extends CameraCaptureSession.CaptureCallback {
    Camera2_Preview.CameraCacheInfo mCCI;
    
    public OnFrameCapturedCallback(Camera2_Preview.CameraCacheInfo param1CameraCacheInfo) {
      this.mCCI = param1CameraCacheInfo;
    }
    
    public void onCaptureCompleted(CameraCaptureSession param1CameraCaptureSession, CaptureRequest param1CaptureRequest, TotalCaptureResult param1TotalCaptureResult) {
      Camera2_Preview.CameraCacheInfo cameraCacheInfo = this.mCCI;
      if (cameraCacheInfo != null)
        cameraCacheInfo.lastResult = (CaptureResult)param1TotalCaptureResult; 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\Camera2_Preview.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */