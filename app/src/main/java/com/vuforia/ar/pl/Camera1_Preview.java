package com.vuforia.ar.pl;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

public class Camera1_Preview implements Camera.PreviewCallback {
  private static final int AR_CAMERA_DIRECTION_BACK = 268443665;
  
  private static final int AR_CAMERA_DIRECTION_FRONT = 268443666;
  
  private static final int AR_CAMERA_DIRECTION_UNKNOWN = 268443664;
  
  private static final int AR_CAMERA_EXPOSUREMODE_AUTO = 805314560;
  
  private static final int AR_CAMERA_EXPOSUREMODE_CONTINUOUSAUTO = 805322752;
  
  private static final int AR_CAMERA_EXPOSUREMODE_LOCKED = 805310464;
  
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
  
  private static final int[] CAMERA_IMAGE_FORMAT_CONVERSIONTABLE = new int[] { 16, 268439816, 17, 268439817, 4, 268439810, 842094169, 268439818 };
  
  private static boolean CONVERT_FORMAT_TO_ANDROID = false;
  
  private static boolean CONVERT_FORMAT_TO_PL = true;
  
  private static final String FOCUS_MODE_NORMAL = "normal";
  
  private static final String MODULENAME = "Camera1_Preview";
  
  private static final int NUM_CAPTURE_BUFFERS = 2;
  
  private static final int NUM_CAPTURE_BUFFERS_TO_ADD = 2;
  
  private static final int NUM_MAX_CAMERAOPEN_RETRY = 10;
  
  private static final String SAMSUNG_PARAM_FAST_FPS_MODE = "fast-fps-mode";
  
  private static final String SAMSUNG_PARAM_VRMODE = "vrmode";
  
  private static final String SAMSUNG_PARAM_VRMODE_SUPPORTED = "vrmode-supported";
  
  private static final int TIME_CAMERAOPEN_RETRY_DELAY_MS = 250;
  
  private static final int _NUM_CAMERA_CAPSINFO_VALUE_ = 6;
  
  private static final int _NUM_CAMERA_CAPTUREINFO_VALUE_ = 5;
  
  private Vector<CameraCacheInfo> cameraCacheInfo = null;
  
  private HashMap<Camera, Integer> cameraCacheInfoIndexCache = null;
  
  private SurfaceManager surfaceManager = null;
  
  static {
    CONVERT_FORMAT_TO_ANDROID = false;
  }
  
  private boolean checkPermission() {
    boolean bool = false;
    try {
      Activity activity = SystemTools.getActivityFromNative();
      if (activity == null)
        return false; 
      int i = activity.getPackageManager().checkPermission("android.permission.CAMERA", activity.getPackageName());
      if (i == 0)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  private boolean checkSamsungHighFPS(CameraCacheInfo paramCameraCacheInfo) {
    Camera.Parameters parameters = getCameraParameters(paramCameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    if ("true".equalsIgnoreCase(parameters.get("vrmode-supported")) && paramCameraCacheInfo.requestWidth > 0 && paramCameraCacheInfo.requestHeight > 0 && parameters.get("fast-fps-mode") != null && parameters.getInt("fast-fps-mode") != 0 && (paramCameraCacheInfo.requestWidth != (parameters.getPreviewSize()).width || paramCameraCacheInfo.requestHeight != (parameters.getPreviewSize()).height)) {
      DebugLog.LOGW("Camera1_Preview", "Detected Samsung high fps camera driver bug.");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Preview size doesn't match request; width ");
      stringBuilder.append(paramCameraCacheInfo.requestWidth);
      stringBuilder.append("!=");
      stringBuilder.append((parameters.getPreviewSize()).width);
      stringBuilder.append(" or height ");
      stringBuilder.append(paramCameraCacheInfo.requestHeight);
      stringBuilder.append("!=");
      stringBuilder.append((parameters.getPreviewSize()).height);
      DebugLog.LOGW("Camera1_Preview", stringBuilder.toString());
      setCameraPreviewFps(30, parameters);
      parameters.setPreviewSize(paramCameraCacheInfo.requestWidth, paramCameraCacheInfo.requestHeight);
      try {
        paramCameraCacheInfo.camera.setParameters(parameters);
        parameters = getCameraParameters(paramCameraCacheInfo.camera);
        if (paramCameraCacheInfo.requestWidth != (parameters.getPreviewSize()).width || paramCameraCacheInfo.requestHeight != (parameters.getPreviewSize()).height) {
          DebugLog.LOGE("Camera1_Preview", "Unable to workaround Samsung high fps camera driver bug.");
          stringBuilder = new StringBuilder();
          stringBuilder.append("Preview size doesn't match request; width ");
          stringBuilder.append(paramCameraCacheInfo.requestWidth);
          stringBuilder.append("!=");
          stringBuilder.append((parameters.getPreviewSize()).width);
          stringBuilder.append(" or height ");
          stringBuilder.append(paramCameraCacheInfo.requestHeight);
          stringBuilder.append("!=");
          stringBuilder.append((parameters.getPreviewSize()).height);
          DebugLog.LOGE("Camera1_Preview", stringBuilder.toString());
          return false;
        } 
        return true;
      } catch (Exception exception) {
        SystemTools.setSystemErrorCode(6);
        return false;
      } 
    } 
    return true;
  }
  
  private CameraCacheInfo getCameraCacheInfo(int paramInt) {
    return (paramInt < 0 || paramInt >= this.cameraCacheInfo.size()) ? null : this.cameraCacheInfo.get(paramInt);
  }
  
  private int getCameraDeviceIndex(int paramInt1, int paramInt2, int paramInt3) {
    boolean bool1 = SystemTools.checkMinimumApiLevel(9);
    boolean bool = false;
    paramInt2 = 1;
    if (bool1) {
      switch (paramInt3) {
        default:
          SystemTools.setSystemErrorCode(2);
          return -1;
        case 268443665:
          paramInt2 = 0;
          break;
        case 268443664:
          paramInt2 = -1;
          break;
        case 268443666:
          break;
      } 
      int i = Camera.getNumberOfCameras();
      paramInt3 = bool;
      while (true) {
        if (paramInt3 < i) {
          Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
          try {
            Camera.getCameraInfo(paramInt3, cameraInfo);
            if ((paramInt2 < 0 || paramInt2 == cameraInfo.facing) && (paramInt1 < 0 || paramInt1 == paramInt3))
              return paramInt3; 
          } catch (Exception exception) {}
          paramInt3++;
          continue;
        } 
        SystemTools.setSystemErrorCode(6);
        return -1;
      } 
    } 
    if (paramInt3 == 268443666) {
      SystemTools.setSystemErrorCode(2);
      return -1;
    } 
    if (paramInt1 >= 1) {
      SystemTools.setSystemErrorCode(2);
      return -1;
    } 
    return 0;
  }
  
  private Camera.Parameters getCameraParameters(Camera paramCamera) {
    try {
      return paramCamera.getParameters();
    } catch (Exception exception) {
      return null;
    } 
  }
  
  private native void newFrameAvailable(long paramLong1, int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte, long paramLong2);
  
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
  
  private boolean setCameraCaptureParams(CameraCacheInfo paramCameraCacheInfo, Camera.Parameters paramParameters, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    if (paramArrayOfint1 != null || paramArrayOfint2 != null) {
      int j;
      if (paramArrayOfint2 != null) {
        j = paramArrayOfint2[0];
      } else {
        j = paramArrayOfint1[0];
      } 
      paramCameraCacheInfo.overrideWidth = j;
      if (paramArrayOfint2 != null) {
        j = paramArrayOfint2[1];
      } else {
        j = paramArrayOfint1[1];
      } 
      paramCameraCacheInfo.overrideHeight = j;
      if (paramArrayOfint2 != null) {
        j = paramArrayOfint2[2];
      } else {
        j = paramArrayOfint1[2];
      } 
      paramCameraCacheInfo.overrideFormatAndroid = translateImageFormat(j, CONVERT_FORMAT_TO_ANDROID);
    } 
    if (paramArrayOfint1 == null)
      return true; 
    paramCameraCacheInfo.requestWidth = paramArrayOfint1[0];
    paramCameraCacheInfo.requestHeight = paramArrayOfint1[1];
    paramCameraCacheInfo.requestFormatAndroid = translateImageFormat(paramArrayOfint1[2], CONVERT_FORMAT_TO_ANDROID);
    int i = paramArrayOfint1[3];
    try {
      if (paramCameraCacheInfo.requestWidth > 0 && paramCameraCacheInfo.requestHeight > 0)
        paramParameters.setPreviewSize(paramCameraCacheInfo.requestWidth, paramCameraCacheInfo.requestHeight); 
      if (i > 0)
        if (SystemTools.checkMinimumApiLevel(8)) {
          if (!setCameraPreviewFps(i, paramParameters))
            paramParameters.setPreviewFrameRate(i); 
        } else {
          paramParameters.setPreviewFrameRate(i);
        }  
      if (paramCameraCacheInfo.requestFormatAndroid != 0)
        paramParameters.setPreviewFormat(paramCameraCacheInfo.requestFormatAndroid); 
      if (paramArrayOfint1[4] > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      if (i != 0) {
        if (SystemTools.checkMinimumApiLevel(11))
          try {
            paramCameraCacheInfo.surfaceTexture = new SurfaceTexture(-1);
            try {
              paramCameraCacheInfo.camera.setPreviewTexture(paramCameraCacheInfo.surfaceTexture);
              return true;
            } catch (Exception null) {
              return true;
            } 
          } catch (Exception exception) {
            return false;
          }  
        SurfaceManager surfaceManager = this.surfaceManager;
        if (surfaceManager == null || !surfaceManager.addCameraSurface((CameraCacheInfo)exception))
          return false; 
      } 
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  private boolean setCameraPreviewFps(int paramInt, Camera.Parameters paramParameters) {
    int[] arrayOfInt;
    List list = paramParameters.getSupportedPreviewFpsRange();
    if ((paramInt == 60 || paramInt == 120) && "true".equalsIgnoreCase(paramParameters.get("vrmode-supported"))) {
      int[] arrayOfInt1 = new int[2];
      paramParameters.set("vrmode", 1);
      paramParameters.setRecordingHint(true);
      paramParameters.set("focus-mode", "continuous-video");
      if (paramInt == 60) {
        paramParameters.set("fast-fps-mode", 1);
        arrayOfInt1[0] = 60000;
        arrayOfInt1[1] = 60000;
      } 
      arrayOfInt = arrayOfInt1;
      if (paramInt == 120) {
        paramParameters.set("fast-fps-mode", 2);
        arrayOfInt1[0] = 120000;
        arrayOfInt1[1] = 120000;
        arrayOfInt = arrayOfInt1;
      } 
    } else {
      if ("true".equalsIgnoreCase(paramParameters.get("vrmode-supported")) && paramParameters.get("fast-fps-mode") != null && paramParameters.getInt("fast-fps-mode") != 0) {
        paramParameters.set("vrmode", 0);
        paramParameters.set("fast-fps-mode", 0);
      } 
      Iterator<int[]> iterator = arrayOfInt.iterator();
      arrayOfInt = null;
      while (iterator.hasNext()) {
        int[] arrayOfInt1 = iterator.next();
        if (arrayOfInt1[0] == paramInt * 1000 && arrayOfInt1[1] - arrayOfInt1[0] < Integer.MAX_VALUE)
          arrayOfInt = arrayOfInt1; 
      } 
    } 
    if (arrayOfInt != null) {
      paramParameters.setPreviewFpsRange(arrayOfInt[0], arrayOfInt[1]);
      return true;
    } 
    return false;
  }
  
  private boolean setCustomCameraParams(Camera.Parameters paramParameters, String paramString) {
    try {
      JSONObject jSONObject = new JSONObject(paramString);
      Iterator<String> iterator = jSONObject.keys();
      while (true) {
        if (iterator.hasNext()) {
          String str = iterator.next();
          try {
            Object object = jSONObject.get(str);
            if (object.getClass() == String.class) {
              paramParameters.set(str, (String)object);
              continue;
            } 
            if (object.getClass() == Integer.class) {
              paramParameters.set(str, ((Integer)object).intValue());
              continue;
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
  
  private boolean setupPreviewBuffer(CameraCacheInfo paramCameraCacheInfo) {
    Camera.Parameters parameters = getCameraParameters(paramCameraCacheInfo.camera);
    boolean bool = false;
    if (parameters == null)
      return false; 
    try {
      if (paramCameraCacheInfo.requestWidth == paramCameraCacheInfo.overrideWidth) {
        i = (parameters.getPreviewSize()).width;
      } else {
        i = paramCameraCacheInfo.overrideWidth;
      } 
      paramCameraCacheInfo.bufferWidth = i;
      if (paramCameraCacheInfo.requestHeight == paramCameraCacheInfo.overrideHeight) {
        i = (parameters.getPreviewSize()).height;
      } else {
        i = paramCameraCacheInfo.overrideHeight;
      } 
      paramCameraCacheInfo.bufferHeight = i;
      if (paramCameraCacheInfo.requestFormatAndroid == paramCameraCacheInfo.overrideFormatAndroid) {
        i = parameters.getPreviewFormat();
      } else {
        i = paramCameraCacheInfo.overrideFormatAndroid;
      } 
      paramCameraCacheInfo.bufferFormatPL = translateImageFormat(i, CONVERT_FORMAT_TO_PL);
      try {
        PixelFormat pixelFormat = new PixelFormat();
        PixelFormat.getPixelFormatInfo(i, pixelFormat);
        int k = pixelFormat.bitsPerPixel;
        i = k;
      } catch (Exception exception) {
        int k = getBitsPerPixel(i);
        i = k;
        if (k == 0)
          return false; 
      } 
      int j = paramCameraCacheInfo.bufferWidth * paramCameraCacheInfo.bufferHeight * i / 8 + 4096;
      if (j <= paramCameraCacheInfo.bufferSize) {
        paramCameraCacheInfo.camera.setPreviewCallbackWithBuffer(this);
        return true;
      } 
      paramCameraCacheInfo.buffer = new byte[2][];
      for (int i = bool; i < 2; i++) {
        paramCameraCacheInfo.buffer[i] = new byte[j];
        if (i < 2)
          paramCameraCacheInfo.camera.addCallbackBuffer(paramCameraCacheInfo.buffer[i]); 
      } 
      paramCameraCacheInfo.bufferSize = j;
      paramCameraCacheInfo.camera.setPreviewCallbackWithBuffer(this);
      System.gc();
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  private int translateImageFormat(int paramInt, boolean paramBoolean) {
    boolean bool = false;
    int i = 0;
    while (true) {
      int[] arrayOfInt = CAMERA_IMAGE_FORMAT_CONVERSIONTABLE;
      if (i < arrayOfInt.length / 2) {
        int j;
        if (paramBoolean == CONVERT_FORMAT_TO_PL) {
          j = arrayOfInt[i * 2];
        } else {
          j = arrayOfInt[i * 2 + 1];
        } 
        if (paramInt == j)
          return (paramBoolean == CONVERT_FORMAT_TO_PL) ? CAMERA_IMAGE_FORMAT_CONVERSIONTABLE[i * 2 + 1] : CAMERA_IMAGE_FORMAT_CONVERSIONTABLE[i * 2]; 
        i++;
        continue;
      } 
      paramInt = bool;
      if (paramBoolean == CONVERT_FORMAT_TO_PL)
        paramInt = 268439808; 
      return paramInt;
    } 
  }
  
  public boolean close(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    boolean bool = false;
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    if (cameraCacheInfo.isHalDriven) {
      DebugLog.LOGW("Camera1_Preview", "We shouldn't be here for HAL driven camera!");
      return true;
    } 
    this.cameraCacheInfoIndexCache.remove(cameraCacheInfo.camera);
    try {
      cameraCacheInfo.camera.release();
      bool = true;
    } catch (Exception exception) {}
    cameraCacheInfo.camera = null;
    cameraCacheInfo.buffer = (byte[][])null;
    cameraCacheInfo.status = 268443649;
    System.gc();
    return bool;
  }
  
  int getBitsPerPixel(int paramInt) {
    return (paramInt != 4) ? ((paramInt != 842094169) ? ((paramInt != 16) ? ((paramInt != 17) ? 0 : 12) : 16) : 12) : 16;
  }
  
  public int[] getCameraCapabilities(int paramInt) {
    byte b1;
    byte b2;
    boolean bool;
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    if (cameraCacheInfo.caps != null)
      return cameraCacheInfo.caps; 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    List list3 = parameters.getSupportedPreviewSizes();
    List list2 = parameters.getSupportedPreviewFrameRates();
    List list1 = parameters.getSupportedPreviewFormats();
    List list4 = parameters.getSupportedFlashModes();
    List list5 = parameters.getSupportedFocusModes();
    if (list3 != null) {
      paramInt = list3.size();
    } else {
      paramInt = 0;
    } 
    if (list2 != null) {
      b2 = list2.size();
    } else {
      b2 = 0;
    } 
    if (list1 != null) {
      b1 = list1.size();
    } else {
      b1 = 0;
    } 
    cameraCacheInfo.caps = new int[paramInt * 2 + 6 + b2 + b1];
    cameraCacheInfo.caps[0] = 536870912;
    if (list4 != null && (list4.contains("torch") || list4.contains("on"))) {
      bool = true;
    } else {
      bool = false;
    } 
    setCameraCapsBit(cameraCacheInfo, 0, 536870913, bool);
    setCameraCapsBit(cameraCacheInfo, 0, 536870914, true);
    setCameraCapsBit(cameraCacheInfo, 0, 536870916, SystemTools.checkMinimumApiLevel(8));
    setCameraCapsBit(cameraCacheInfo, 0, 536870928, SystemTools.checkMinimumApiLevel(14));
    setCameraCapsBit(cameraCacheInfo, 0, 536871936, SystemTools.checkMinimumApiLevel(8));
    setCameraCapsBit(cameraCacheInfo, 0, 536872960, SystemTools.checkMinimumApiLevel(8));
    if (SystemTools.checkMinimumApiLevel(8) && parameters.isZoomSupported()) {
      bool = true;
    } else {
      bool = false;
    } 
    setCameraCapsBit(cameraCacheInfo, 0, 536903680, bool);
    if (SystemTools.checkMinimumApiLevel(8) && parameters.isZoomSupported()) {
      bool = true;
    } else {
      bool = false;
    } 
    setCameraCapsBit(cameraCacheInfo, 0, 536936448, bool);
    setCameraCapsBit(cameraCacheInfo, 0, 553648128, SystemTools.checkMinimumApiLevel(15));
    cameraCacheInfo.caps[1] = 536870912;
    if (list4 != null && (list4.contains("torch") || list4.contains("on"))) {
      bool = true;
    } else {
      bool = false;
    } 
    setCameraCapsBit(cameraCacheInfo, 1, 536870913, bool);
    setCameraCapsBit(cameraCacheInfo, 1, 536870914, true);
    setCameraCapsBit(cameraCacheInfo, 1, 536870928, SystemTools.checkMinimumApiLevel(14));
    setCameraCapsBit(cameraCacheInfo, 1, 536871936, SystemTools.checkMinimumApiLevel(8));
    if (SystemTools.checkMinimumApiLevel(8) && parameters.isZoomSupported()) {
      bool = true;
    } else {
      bool = false;
    } 
    setCameraCapsBit(cameraCacheInfo, 1, 536903680, bool);
    setCameraCapsBit(cameraCacheInfo, 1, 553648128, SystemTools.checkMinimumApiLevel(15));
    cameraCacheInfo.caps[2] = 805306368;
    if (list4 != null && (list4.contains("torch") || list4.contains("on"))) {
      setCameraCapsBit(cameraCacheInfo, 2, 805306369, true);
      setCameraCapsBit(cameraCacheInfo, 2, 805306370, true);
    } 
    if (list5 != null) {
      setCameraCapsBit(cameraCacheInfo, 2, 805306384, true);
      setCameraCapsBit(cameraCacheInfo, 2, 805306400, list5.contains("auto"));
      setCameraCapsBit(cameraCacheInfo, 2, 805306432, list5.contains("continuous-video"));
      setCameraCapsBit(cameraCacheInfo, 2, 805306496, list5.contains("macro"));
      setCameraCapsBit(cameraCacheInfo, 2, 805306624, list5.contains("infinity"));
      setCameraCapsBit(cameraCacheInfo, 2, 805306880, list5.contains("fixed"));
    } 
    cameraCacheInfo.caps[3] = paramInt;
    cameraCacheInfo.caps[4] = b2;
    cameraCacheInfo.caps[5] = b1;
    if (paramInt > 0) {
      ListIterator<Camera.Size> listIterator = list3.listIterator();
      int j = 6;
      while (true) {
        paramInt = j;
        if (listIterator.hasNext()) {
          Camera.Size size = listIterator.next();
          cameraCacheInfo.caps[j] = size.width;
          cameraCacheInfo.caps[j + 1] = size.height;
          j += 2;
          continue;
        } 
        break;
      } 
    } else {
      paramInt = 6;
    } 
    int i = paramInt;
    if (b2 > 0) {
      ListIterator<Integer> listIterator = list2.listIterator();
      while (true) {
        i = paramInt;
        if (listIterator.hasNext()) {
          i = ((Integer)listIterator.next()).intValue();
          cameraCacheInfo.caps[paramInt] = i;
          paramInt++;
          continue;
        } 
        break;
      } 
    } 
    if (b1 > 0) {
      ListIterator<Integer> listIterator = list1.listIterator();
      while (listIterator.hasNext()) {
        paramInt = ((Integer)listIterator.next()).intValue();
        cameraCacheInfo.caps[i] = translateImageFormat(paramInt, true);
        i++;
      } 
    } 
    return cameraCacheInfo.caps;
  }
  
  public int[] getCaptureInfo(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    try {
      int i = (parameters.getPreviewSize()).width;
      paramInt = 0;
      int j = (parameters.getPreviewSize()).height;
      int k = translateImageFormat(parameters.getPreviewFormat(), CONVERT_FORMAT_TO_PL);
      int m = parameters.getPreviewFrameRate();
      if (cameraCacheInfo.surface != null || cameraCacheInfo.surfaceTexture != null)
        paramInt = 1; 
      return new int[] { i, j, k, m, paramInt };
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
    if (SystemTools.checkMinimumApiLevel(9)) {
      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
      try {
        Camera.getCameraInfo(paramInt, cameraInfo);
        paramInt = cameraInfo.facing;
        return (paramInt != 0) ? ((paramInt != 1) ? 268443664 : 268443666) : 268443665;
      } catch (Exception exception) {
        SystemTools.setSystemErrorCode(6);
        return -1;
      } 
    } 
    return 268443665;
  }
  
  String getFlattenedParameters(int paramInt) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.camera == null) {
      SystemTools.setSystemErrorCode(4);
      return "";
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return "";
    } 
    return parameters.flatten();
  }
  
  public int getNumberOfCameras() {
    throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
  }
  
  public int getOrientation(int paramInt) {
    if (!checkPermission()) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    if (SystemTools.checkMinimumApiLevel(9)) {
      Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
      try {
        Camera.getCameraInfo(paramInt, cameraInfo);
        return cameraInfo.orientation;
      } catch (Exception exception) {
        SystemTools.setSystemErrorCode(6);
        return -1;
      } 
    } 
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
    float[] arrayOfFloat;
    String str1;
    String str2;
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt1);
    if (cameraCacheInfo == null || cameraCacheInfo.camera == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    switch (paramInt2) {
      default:
        return null;
      case 553648128:
        try {
          return parameters.getVideoStabilization() ? Boolean.valueOf(true) : Boolean.valueOf(false);
        } catch (Exception exception) {
          SystemTools.setSystemErrorCode(6);
          return null;
        } 
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
      case 537001984:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536936448:
        if (SystemTools.checkMinimumApiLevel(8) && parameters.isZoomSupported())
          return new int[] { 0, parameters.getMaxZoom() }; 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536903680:
        if (SystemTools.checkMinimumApiLevel(8) && parameters.isZoomSupported())
          return Integer.valueOf(parameters.getZoom()); 
        SystemTools.setSystemErrorCode(6);
        return null;
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
        if (SystemTools.checkMinimumApiLevel(8))
          return new float[] { parameters.getExposureCompensationStep() * parameters.getMinExposureCompensation(), parameters.getExposureCompensationStep() * parameters.getMaxExposureCompensation() }; 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536871936:
        if (SystemTools.checkMinimumApiLevel(8))
          return Float.valueOf(parameters.getExposureCompensationStep() * parameters.getExposureCompensation()); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870944:
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870928:
        if (SystemTools.checkMinimumApiLevel(14) && parameters.getMaxNumFocusAreas() > 0) {
          List<Camera.Area> list = parameters.getFocusAreas();
          if (list.size() > 0) {
            Camera.Area area = list.get(0);
            return new float[] { area.rect.left, area.rect.top, area.rect.right, area.rect.bottom, area.weight };
          } 
        } 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870920:
        if (SystemTools.checkMinimumApiLevel(9)) {
          arrayOfFloat = new float[3];
          parameters.getFocusDistances(arrayOfFloat);
          return new float[] { arrayOfFloat[0], arrayOfFloat[2] };
        } 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870916:
        if (SystemTools.checkMinimumApiLevel(8))
          return Float.valueOf(parameters.getFocalLength()); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870914:
        str2 = parameters.getFocusMode();
        if (str2.equals("auto")) {
          if (((CameraCacheInfo)arrayOfFloat).isAutoFocusing) {
            paramInt1 = 805306400;
            return Integer.valueOf(paramInt1);
          } 
          break;
        } 
        if (str2.equals("continuous-video"))
          return Integer.valueOf(805306432); 
        if (str2.equals("infinity"))
          return Integer.valueOf(805306624); 
        if (str2.equals("macro"))
          return Integer.valueOf(805306496); 
        if (str2.equals("fixed"))
          return Integer.valueOf(805306880); 
        SystemTools.setSystemErrorCode(6);
        return null;
      case 536870913:
        str1 = str2.getFlashMode();
        if (str1.equals("torch") || str1.equals("on"))
          return Integer.valueOf(805306370); 
        if (str1.equals("off"))
          return Integer.valueOf(805306369); 
        SystemTools.setSystemErrorCode(6);
        return null;
    } 
    paramInt1 = 805306384;
    return Integer.valueOf(paramInt1);
  }
  
  String getUntypedCameraParameter(int paramInt, String paramString) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.camera == null) {
      SystemTools.setSystemErrorCode(4);
      return null;
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return null;
    } 
    paramString = parameters.get(paramString);
    if (paramString == null)
      SystemTools.setSystemErrorCode(6); 
    return paramString;
  }
  
  public boolean init() {
    this.cameraCacheInfo = new Vector<CameraCacheInfo>();
    this.cameraCacheInfoIndexCache = new HashMap<Camera, Integer>();
    return true;
  }
  
  public void onPreviewFrame(byte[] paramArrayOfbyte, Camera paramCamera) {
    long l = System.nanoTime();
    SystemTools.checkMinimumApiLevel(18);
    Integer integer = (Integer)this.cameraCacheInfoIndexCache.get(paramCamera);
    if (integer == null) {
      SystemTools.checkMinimumApiLevel(18);
      return;
    } 
    int i = ((Integer)integer).intValue();
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(i);
    if (cameraCacheInfo == null) {
      SystemTools.checkMinimumApiLevel(18);
      return;
    } 
    newFrameAvailable(cameraCacheInfo.deviceHandle, i, cameraCacheInfo.bufferWidth, cameraCacheInfo.bufferHeight, cameraCacheInfo.bufferFormatPL, paramArrayOfbyte, l);
    paramCamera.addCallbackBuffer(paramArrayOfbyte);
    SystemTools.checkMinimumApiLevel(18);
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
    //   15: iload_3
    //   16: iload #4
    //   18: iload #5
    //   20: invokespecial getCameraDeviceIndex : (III)I
    //   23: istore #4
    //   25: iload #4
    //   27: ifge -> 32
    //   30: iconst_m1
    //   31: ireturn
    //   32: aload_0
    //   33: getfield cameraCacheInfo : Ljava/util/Vector;
    //   36: invokevirtual size : ()I
    //   39: istore #5
    //   41: iconst_0
    //   42: istore #9
    //   44: aconst_null
    //   45: astore #10
    //   47: iconst_0
    //   48: istore_3
    //   49: iload_3
    //   50: iload #5
    //   52: if_icmpge -> 88
    //   55: aload_0
    //   56: getfield cameraCacheInfo : Ljava/util/Vector;
    //   59: iload_3
    //   60: invokevirtual get : (I)Ljava/lang/Object;
    //   63: checkcast com/vuforia/ar/pl/Camera1_Preview$CameraCacheInfo
    //   66: astore #10
    //   68: aload #10
    //   70: getfield deviceID : I
    //   73: iload #4
    //   75: if_icmpne -> 81
    //   78: goto -> 90
    //   81: iload_3
    //   82: iconst_1
    //   83: iadd
    //   84: istore_3
    //   85: goto -> 49
    //   88: iconst_m1
    //   89: istore_3
    //   90: iload_3
    //   91: ifge -> 218
    //   94: new com/vuforia/ar/pl/Camera1_Preview$CameraCacheInfo
    //   97: dup
    //   98: aload_0
    //   99: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera1_Preview;)V
    //   102: astore #10
    //   104: aload #10
    //   106: iload #4
    //   108: putfield deviceID : I
    //   111: aload #10
    //   113: lload_1
    //   114: putfield deviceHandle : J
    //   117: aload #10
    //   119: aconst_null
    //   120: putfield camera : Landroid/hardware/Camera;
    //   123: aload #10
    //   125: aconst_null
    //   126: putfield surface : Lcom/vuforia/ar/pl/CameraSurface;
    //   129: aload #10
    //   131: aconst_null
    //   132: checkcast [[B
    //   135: putfield buffer : [[B
    //   138: aload #10
    //   140: iconst_0
    //   141: putfield overrideWidth : I
    //   144: aload #10
    //   146: iconst_0
    //   147: putfield requestWidth : I
    //   150: aload #10
    //   152: iconst_0
    //   153: putfield bufferWidth : I
    //   156: aload #10
    //   158: iconst_0
    //   159: putfield overrideHeight : I
    //   162: aload #10
    //   164: iconst_0
    //   165: putfield requestHeight : I
    //   168: aload #10
    //   170: iconst_0
    //   171: putfield bufferHeight : I
    //   174: aload #10
    //   176: ldc 268439808
    //   178: putfield bufferFormatPL : I
    //   181: aload #10
    //   183: iconst_0
    //   184: putfield overrideFormatAndroid : I
    //   187: aload #10
    //   189: iconst_0
    //   190: putfield requestFormatAndroid : I
    //   193: aload #10
    //   195: aconst_null
    //   196: putfield caps : [I
    //   199: aload #10
    //   201: ldc 268443649
    //   203: putfield status : I
    //   206: aload #10
    //   208: iconst_0
    //   209: putfield isAutoFocusing : Z
    //   212: aload #10
    //   214: iconst_0
    //   215: putfield isHalDriven : Z
    //   218: aload #10
    //   220: iconst_0
    //   221: putfield bufferSize : I
    //   224: bipush #10
    //   226: istore #5
    //   228: iconst_0
    //   229: istore #4
    //   231: bipush #9
    //   233: invokestatic checkMinimumApiLevel : (I)Z
    //   236: ifeq -> 255
    //   239: aload #10
    //   241: aload #10
    //   243: getfield deviceID : I
    //   246: invokestatic open : (I)Landroid/hardware/Camera;
    //   249: putfield camera : Landroid/hardware/Camera;
    //   252: goto -> 271
    //   255: aload #10
    //   257: getfield deviceID : I
    //   260: ifne -> 271
    //   263: aload #10
    //   265: invokestatic open : ()Landroid/hardware/Camera;
    //   268: putfield camera : Landroid/hardware/Camera;
    //   271: aload #10
    //   273: getfield camera : Landroid/hardware/Camera;
    //   276: astore #11
    //   278: aload #11
    //   280: ifnull -> 289
    //   283: iconst_1
    //   284: istore #4
    //   286: goto -> 292
    //   289: iconst_0
    //   290: istore #4
    //   292: iload #4
    //   294: ifne -> 323
    //   297: iload #5
    //   299: ifle -> 323
    //   302: aload_0
    //   303: monitorenter
    //   304: aload_0
    //   305: ldc2_w 250
    //   308: invokevirtual wait : (J)V
    //   311: aload_0
    //   312: monitorexit
    //   313: goto -> 323
    //   316: astore #11
    //   318: aload_0
    //   319: monitorexit
    //   320: aload #11
    //   322: athrow
    //   323: iload #4
    //   325: ifne -> 345
    //   328: iload #5
    //   330: ifgt -> 336
    //   333: goto -> 345
    //   336: iload #5
    //   338: iconst_1
    //   339: isub
    //   340: istore #5
    //   342: goto -> 231
    //   345: aload #10
    //   347: getfield camera : Landroid/hardware/Camera;
    //   350: ifnonnull -> 360
    //   353: bipush #6
    //   355: invokestatic setSystemErrorCode : (I)V
    //   358: iconst_m1
    //   359: ireturn
    //   360: aload #7
    //   362: ifnull -> 371
    //   365: aload #7
    //   367: arraylength
    //   368: ifgt -> 382
    //   371: aload #8
    //   373: ifnull -> 388
    //   376: aload #8
    //   378: arraylength
    //   379: ifle -> 388
    //   382: iconst_1
    //   383: istore #4
    //   385: goto -> 391
    //   388: iconst_0
    //   389: istore #4
    //   391: iload #9
    //   393: istore #5
    //   395: aload #6
    //   397: ifnull -> 415
    //   400: iload #9
    //   402: istore #5
    //   404: aload #6
    //   406: invokevirtual length : ()I
    //   409: ifle -> 415
    //   412: iconst_1
    //   413: istore #5
    //   415: iload #4
    //   417: ifne -> 425
    //   420: iload #5
    //   422: ifeq -> 536
    //   425: aload_0
    //   426: aload #10
    //   428: getfield camera : Landroid/hardware/Camera;
    //   431: invokespecial getCameraParameters : (Landroid/hardware/Camera;)Landroid/hardware/Camera$Parameters;
    //   434: astore #11
    //   436: aload #11
    //   438: ifnonnull -> 448
    //   441: bipush #6
    //   443: invokestatic setSystemErrorCode : (I)V
    //   446: iconst_m1
    //   447: ireturn
    //   448: iload #4
    //   450: ifeq -> 493
    //   453: aload #7
    //   455: ifnull -> 471
    //   458: aload #7
    //   460: arraylength
    //   461: iconst_5
    //   462: if_icmpeq -> 471
    //   465: iconst_2
    //   466: invokestatic setSystemErrorCode : (I)V
    //   469: iconst_m1
    //   470: ireturn
    //   471: aload_0
    //   472: aload #10
    //   474: aload #11
    //   476: aload #7
    //   478: aload #8
    //   480: invokespecial setCameraCaptureParams : (Lcom/vuforia/ar/pl/Camera1_Preview$CameraCacheInfo;Landroid/hardware/Camera$Parameters;[I[I)Z
    //   483: ifne -> 493
    //   486: bipush #6
    //   488: invokestatic setSystemErrorCode : (I)V
    //   491: iconst_m1
    //   492: ireturn
    //   493: iload #5
    //   495: ifeq -> 515
    //   498: aload_0
    //   499: aload #11
    //   501: aload #6
    //   503: invokespecial setCustomCameraParams : (Landroid/hardware/Camera$Parameters;Ljava/lang/String;)Z
    //   506: ifne -> 515
    //   509: iconst_2
    //   510: invokestatic setSystemErrorCode : (I)V
    //   513: iconst_m1
    //   514: ireturn
    //   515: aload #10
    //   517: getfield camera : Landroid/hardware/Camera;
    //   520: aload #11
    //   522: invokevirtual setParameters : (Landroid/hardware/Camera$Parameters;)V
    //   525: aload_0
    //   526: aload #10
    //   528: invokespecial checkSamsungHighFPS : (Lcom/vuforia/ar/pl/Camera1_Preview$CameraCacheInfo;)Z
    //   531: ifne -> 536
    //   534: iconst_m1
    //   535: ireturn
    //   536: aload #10
    //   538: ldc 268443650
    //   540: putfield status : I
    //   543: iload_3
    //   544: istore #4
    //   546: iload_3
    //   547: ifge -> 571
    //   550: aload_0
    //   551: getfield cameraCacheInfo : Ljava/util/Vector;
    //   554: aload #10
    //   556: invokevirtual add : (Ljava/lang/Object;)Z
    //   559: pop
    //   560: aload_0
    //   561: getfield cameraCacheInfo : Ljava/util/Vector;
    //   564: invokevirtual size : ()I
    //   567: iconst_1
    //   568: isub
    //   569: istore #4
    //   571: aload_0
    //   572: getfield cameraCacheInfoIndexCache : Ljava/util/HashMap;
    //   575: aload #10
    //   577: getfield camera : Landroid/hardware/Camera;
    //   580: iload #4
    //   582: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   585: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   588: pop
    //   589: iload #4
    //   591: ireturn
    //   592: bipush #6
    //   594: invokestatic setSystemErrorCode : (I)V
    //   597: iconst_m1
    //   598: ireturn
    //   599: astore #11
    //   601: goto -> 292
    //   604: astore #11
    //   606: goto -> 323
    //   609: astore #6
    //   611: goto -> 592
    // Exception table:
    //   from	to	target	type
    //   231	252	599	java/lang/Exception
    //   255	271	599	java/lang/Exception
    //   271	278	599	java/lang/Exception
    //   302	304	604	java/lang/Exception
    //   304	313	316	finally
    //   318	320	316	finally
    //   320	323	604	java/lang/Exception
    //   515	525	609	java/lang/Exception
  }
  
  public int registerHalCamera(int paramInt1, int paramInt2, int paramInt3, String paramString, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    CameraCacheInfo cameraCacheInfo;
    boolean bool = checkPermission();
    byte b = -1;
    if (!bool) {
      SystemTools.setSystemErrorCode(6);
      return -1;
    } 
    paramInt3 = getCameraDeviceIndex(paramInt1, paramInt2, paramInt3);
    if (paramInt3 < 0)
      return -1; 
    int i = this.cameraCacheInfo.size();
    paramString = null;
    paramInt2 = 0;
    while (true) {
      paramInt1 = b;
      if (paramInt2 < i) {
        cameraCacheInfo = this.cameraCacheInfo.get(paramInt2);
        if (cameraCacheInfo.deviceID == paramInt3 && cameraCacheInfo.isHalDriven) {
          paramInt1 = paramInt2;
          break;
        } 
        paramInt2++;
        continue;
      } 
      break;
    } 
    if (paramInt1 < 0) {
      cameraCacheInfo = new CameraCacheInfo();
      cameraCacheInfo.deviceID = paramInt3;
      cameraCacheInfo.camera = null;
      cameraCacheInfo.surface = null;
      cameraCacheInfo.buffer = (byte[][])null;
      cameraCacheInfo.overrideWidth = 0;
      cameraCacheInfo.requestWidth = 0;
      cameraCacheInfo.bufferWidth = 0;
      cameraCacheInfo.overrideHeight = 0;
      cameraCacheInfo.requestHeight = 0;
      cameraCacheInfo.bufferHeight = 0;
      cameraCacheInfo.bufferFormatPL = 268439808;
      cameraCacheInfo.overrideFormatAndroid = 0;
      cameraCacheInfo.requestFormatAndroid = 0;
      cameraCacheInfo.caps = null;
      cameraCacheInfo.status = 268443649;
      cameraCacheInfo.isAutoFocusing = false;
      cameraCacheInfo.isHalDriven = true;
    } 
    cameraCacheInfo.bufferSize = 0;
    paramInt2 = paramInt1;
    if (paramInt1 < 0) {
      this.cameraCacheInfo.add(cameraCacheInfo);
      paramInt2 = this.cameraCacheInfo.size() - 1;
      DebugLog.LOGD("Camera1_Preview", "New HAL camera cache info added to cache");
    } 
    return paramInt2;
  }
  
  public boolean setBatchParameters(int paramInt, String paramString) {
    if (paramString == null)
      return false; 
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.camera == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    if (!setCustomCameraParams(parameters, paramString))
      return false; 
    cameraCacheInfo.camera.setParameters(parameters);
    return true;
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
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    if (!setCameraCaptureParams(cameraCacheInfo, parameters, paramArrayOfint1, paramArrayOfint2)) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    try {
      cameraCacheInfo.camera.setParameters(parameters);
      return !!checkSamsungHighFPS(cameraCacheInfo);
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
  }
  
  public void setSurfaceManager(SurfaceManager paramSurfaceManager) {
    this.surfaceManager = paramSurfaceManager;
  }
  
  boolean setTypedCameraParameter(int paramInt1, int paramInt2, Object paramObject) {
    // Byte code:
    //   0: aload_0
    //   1: iload_1
    //   2: invokespecial getCameraCacheInfo : (I)Lcom/vuforia/ar/pl/Camera1_Preview$CameraCacheInfo;
    //   5: astore #10
    //   7: aload #10
    //   9: ifnull -> 1257
    //   12: aload #10
    //   14: getfield camera : Landroid/hardware/Camera;
    //   17: ifnonnull -> 23
    //   20: goto -> 1257
    //   23: aload_0
    //   24: aload #10
    //   26: getfield camera : Landroid/hardware/Camera;
    //   29: invokespecial getCameraParameters : (Landroid/hardware/Camera;)Landroid/hardware/Camera$Parameters;
    //   32: astore #11
    //   34: aload #11
    //   36: ifnonnull -> 46
    //   39: bipush #6
    //   41: invokestatic setSystemErrorCode : (I)V
    //   44: iconst_0
    //   45: ireturn
    //   46: iload_2
    //   47: lookupswitch default -> 224, 536870913 -> 1088, 536870914 -> 894, 536870916 -> 887, 536870920 -> 880, 536870928 -> 711, 536870944 -> 651, 536870976 -> 561, 536871936 -> 500, 536872960 -> 493, 536875008 -> 396, 536879104 -> 389, 536887296 -> 382, 536903680 -> 344, 536936448 -> 337, 537001984 -> 330, 537133056 -> 323, 537395200 -> 316, 537919488 -> 309, 538968064 -> 302, 541065216 -> 254, 553648128 -> 226
    //   224: iconst_0
    //   225: ireturn
    //   226: aload_3
    //   227: checkcast java/lang/Boolean
    //   230: invokevirtual booleanValue : ()Z
    //   233: ifeq -> 245
    //   236: aload #11
    //   238: iconst_1
    //   239: invokevirtual setVideoStabilization : (Z)V
    //   242: goto -> 1186
    //   245: aload #11
    //   247: iconst_0
    //   248: invokevirtual setVideoStabilization : (Z)V
    //   251: goto -> 1186
    //   254: aload_3
    //   255: checkcast java/lang/Number
    //   258: invokevirtual intValue : ()I
    //   261: istore_1
    //   262: bipush #14
    //   264: invokestatic checkMinimumApiLevel : (I)Z
    //   267: ifeq -> 1289
    //   270: iload_1
    //   271: ifeq -> 1283
    //   274: iconst_1
    //   275: istore #8
    //   277: goto -> 280
    //   280: aload #11
    //   282: iload #8
    //   284: invokevirtual setRecordingHint : (Z)V
    //   287: goto -> 1186
    //   290: aload #11
    //   292: ldc_w 'recording-hint'
    //   295: aload_3
    //   296: invokevirtual set : (Ljava/lang/String;Ljava/lang/String;)V
    //   299: goto -> 1186
    //   302: bipush #6
    //   304: invokestatic setSystemErrorCode : (I)V
    //   307: iconst_0
    //   308: ireturn
    //   309: bipush #6
    //   311: invokestatic setSystemErrorCode : (I)V
    //   314: iconst_0
    //   315: ireturn
    //   316: bipush #6
    //   318: invokestatic setSystemErrorCode : (I)V
    //   321: iconst_0
    //   322: ireturn
    //   323: bipush #6
    //   325: invokestatic setSystemErrorCode : (I)V
    //   328: iconst_0
    //   329: ireturn
    //   330: bipush #6
    //   332: invokestatic setSystemErrorCode : (I)V
    //   335: iconst_0
    //   336: ireturn
    //   337: bipush #6
    //   339: invokestatic setSystemErrorCode : (I)V
    //   342: iconst_0
    //   343: ireturn
    //   344: bipush #8
    //   346: invokestatic checkMinimumApiLevel : (I)Z
    //   349: ifeq -> 375
    //   352: aload #11
    //   354: invokevirtual isZoomSupported : ()Z
    //   357: ifeq -> 375
    //   360: aload #11
    //   362: aload_3
    //   363: checkcast java/lang/Number
    //   366: invokevirtual intValue : ()I
    //   369: invokevirtual setZoom : (I)V
    //   372: goto -> 1186
    //   375: bipush #6
    //   377: invokestatic setSystemErrorCode : (I)V
    //   380: iconst_0
    //   381: ireturn
    //   382: bipush #6
    //   384: invokestatic setSystemErrorCode : (I)V
    //   387: iconst_0
    //   388: ireturn
    //   389: bipush #6
    //   391: invokestatic setSystemErrorCode : (I)V
    //   394: iconst_0
    //   395: ireturn
    //   396: aload_3
    //   397: checkcast java/lang/Number
    //   400: invokevirtual intValue : ()I
    //   403: istore_1
    //   404: iload_1
    //   405: ldc 806354944
    //   407: if_icmpeq -> 476
    //   410: iload_1
    //   411: ldc 809500672
    //   413: if_icmpeq -> 459
    //   416: iconst_3
    //   417: invokestatic setSystemErrorCode : (I)V
    //   420: new java/lang/StringBuilder
    //   423: dup
    //   424: invokespecial <init> : ()V
    //   427: astore_3
    //   428: aload_3
    //   429: ldc_w 'Cannot set unknown white balance mode ('
    //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: pop
    //   436: aload_3
    //   437: iload_1
    //   438: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   441: pop
    //   442: aload_3
    //   443: ldc_w ')'
    //   446: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   449: pop
    //   450: aload_3
    //   451: invokevirtual toString : ()Ljava/lang/String;
    //   454: invokestatic logSystemError : (Ljava/lang/String;)V
    //   457: iconst_0
    //   458: ireturn
    //   459: aload #11
    //   461: invokevirtual isAutoWhiteBalanceLockSupported : ()Z
    //   464: ifeq -> 1186
    //   467: aload #11
    //   469: iconst_0
    //   470: invokevirtual setAutoWhiteBalanceLock : (Z)V
    //   473: goto -> 1186
    //   476: aload #11
    //   478: invokevirtual isAutoWhiteBalanceLockSupported : ()Z
    //   481: ifeq -> 1186
    //   484: aload #11
    //   486: iconst_1
    //   487: invokevirtual setAutoWhiteBalanceLock : (Z)V
    //   490: goto -> 1186
    //   493: bipush #6
    //   495: invokestatic setSystemErrorCode : (I)V
    //   498: iconst_0
    //   499: ireturn
    //   500: bipush #8
    //   502: invokestatic checkMinimumApiLevel : (I)Z
    //   505: ifeq -> 554
    //   508: aload_3
    //   509: checkcast java/lang/Number
    //   512: invokevirtual floatValue : ()F
    //   515: fstore #4
    //   517: aload #11
    //   519: invokevirtual getExposureCompensationStep : ()F
    //   522: fstore #5
    //   524: fload #5
    //   526: fconst_0
    //   527: fcmpl
    //   528: ifne -> 538
    //   531: bipush #6
    //   533: invokestatic setSystemErrorCode : (I)V
    //   536: iconst_0
    //   537: ireturn
    //   538: aload #11
    //   540: fload #4
    //   542: fload #5
    //   544: fdiv
    //   545: invokestatic round : (F)I
    //   548: invokevirtual setExposureCompensation : (I)V
    //   551: goto -> 1186
    //   554: bipush #6
    //   556: invokestatic setSystemErrorCode : (I)V
    //   559: iconst_0
    //   560: ireturn
    //   561: aload_3
    //   562: checkcast java/lang/Number
    //   565: invokevirtual intValue : ()I
    //   568: invokestatic toString : (I)Ljava/lang/String;
    //   571: astore #9
    //   573: aload #11
    //   575: ldc_w 'iso-values'
    //   578: invokevirtual get : (Ljava/lang/String;)Ljava/lang/String;
    //   581: astore #12
    //   583: aload #9
    //   585: astore_3
    //   586: aload #12
    //   588: ifnull -> 639
    //   591: aload #12
    //   593: ldc_w ','
    //   596: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   599: astore #12
    //   601: iconst_0
    //   602: istore_1
    //   603: aload #9
    //   605: astore_3
    //   606: iload_1
    //   607: aload #12
    //   609: arraylength
    //   610: if_icmpge -> 639
    //   613: aload #12
    //   615: iload_1
    //   616: aaload
    //   617: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   620: aload #9
    //   622: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   625: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   628: ifeq -> 1307
    //   631: aload #12
    //   633: iload_1
    //   634: aaload
    //   635: astore_3
    //   636: goto -> 639
    //   639: aload #11
    //   641: ldc_w 'iso'
    //   644: aload_3
    //   645: invokevirtual set : (Ljava/lang/String;Ljava/lang/String;)V
    //   648: goto -> 1186
    //   651: aload_3
    //   652: checkcast java/lang/Number
    //   655: invokevirtual intValue : ()I
    //   658: istore_1
    //   659: iload_1
    //   660: ldc 805310464
    //   662: if_icmpeq -> 694
    //   665: iload_1
    //   666: ldc 805322752
    //   668: if_icmpeq -> 677
    //   671: iconst_3
    //   672: invokestatic setSystemErrorCode : (I)V
    //   675: iconst_0
    //   676: ireturn
    //   677: aload #11
    //   679: invokevirtual isAutoExposureLockSupported : ()Z
    //   682: ifeq -> 1186
    //   685: aload #11
    //   687: iconst_0
    //   688: invokevirtual setAutoExposureLock : (Z)V
    //   691: goto -> 1186
    //   694: aload #11
    //   696: invokevirtual isAutoExposureLockSupported : ()Z
    //   699: ifeq -> 1186
    //   702: aload #11
    //   704: iconst_1
    //   705: invokevirtual setAutoExposureLock : (Z)V
    //   708: goto -> 1186
    //   711: bipush #14
    //   713: invokestatic checkMinimumApiLevel : (I)Z
    //   716: ifeq -> 873
    //   719: aload_3
    //   720: checkcast [F
    //   723: checkcast [F
    //   726: astore_3
    //   727: aload_3
    //   728: arraylength
    //   729: iconst_5
    //   730: if_icmpeq -> 1314
    //   733: iconst_2
    //   734: invokestatic setSystemErrorCode : (I)V
    //   737: iconst_0
    //   738: ireturn
    //   739: aload_3
    //   740: iconst_0
    //   741: faload
    //   742: f2d
    //   743: ldc2_w 2000.0
    //   746: dmul
    //   747: d2i
    //   748: istore_1
    //   749: aload_3
    //   750: iconst_1
    //   751: faload
    //   752: f2d
    //   753: ldc2_w 2000.0
    //   756: dmul
    //   757: d2i
    //   758: istore #6
    //   760: aload_3
    //   761: iconst_2
    //   762: faload
    //   763: fstore #4
    //   765: fload #4
    //   767: f2d
    //   768: ldc2_w 2000.0
    //   771: dmul
    //   772: d2i
    //   773: istore #7
    //   775: new android/graphics/Rect
    //   778: dup
    //   779: iload_1
    //   780: sipush #1000
    //   783: isub
    //   784: iload #6
    //   786: sipush #1000
    //   789: isub
    //   790: iload #7
    //   792: sipush #1000
    //   795: isub
    //   796: aload_3
    //   797: iconst_3
    //   798: faload
    //   799: f2d
    //   800: ldc2_w 2000.0
    //   803: dmul
    //   804: d2i
    //   805: sipush #1000
    //   808: isub
    //   809: invokespecial <init> : (IIII)V
    //   812: astore #9
    //   814: new java/util/ArrayList
    //   817: dup
    //   818: invokespecial <init> : ()V
    //   821: astore #12
    //   823: aload #12
    //   825: new android/hardware/Camera$Area
    //   828: dup
    //   829: aload #9
    //   831: aload_3
    //   832: iconst_4
    //   833: faload
    //   834: f2d
    //   835: ldc2_w 1000.0
    //   838: dmul
    //   839: d2i
    //   840: invokespecial <init> : (Landroid/graphics/Rect;I)V
    //   843: invokeinterface add : (Ljava/lang/Object;)Z
    //   848: pop
    //   849: aload #11
    //   851: invokevirtual getMaxNumFocusAreas : ()I
    //   854: ifle -> 1186
    //   857: aload #11
    //   859: aload #12
    //   861: invokevirtual setFocusAreas : (Ljava/util/List;)V
    //   864: goto -> 1186
    //   867: iconst_2
    //   868: invokestatic setSystemErrorCode : (I)V
    //   871: iconst_0
    //   872: ireturn
    //   873: bipush #6
    //   875: invokestatic setSystemErrorCode : (I)V
    //   878: iconst_0
    //   879: ireturn
    //   880: bipush #6
    //   882: invokestatic setSystemErrorCode : (I)V
    //   885: iconst_0
    //   886: ireturn
    //   887: bipush #6
    //   889: invokestatic setSystemErrorCode : (I)V
    //   892: iconst_0
    //   893: ireturn
    //   894: aload #10
    //   896: getfield camera : Landroid/hardware/Camera;
    //   899: invokevirtual cancelAutoFocus : ()V
    //   902: aload_3
    //   903: checkcast java/lang/Number
    //   906: invokevirtual intValue : ()I
    //   909: istore_1
    //   910: iload_1
    //   911: lookupswitch default -> 968, 805306384 -> 1052, 805306400 -> 1041, 805306432 -> 1007, 805306496 -> 996, 805306624 -> 985, 805306880 -> 974
    //   968: iconst_3
    //   969: invokestatic setSystemErrorCode : (I)V
    //   972: iconst_0
    //   973: ireturn
    //   974: aload #11
    //   976: ldc_w 'fixed'
    //   979: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   982: goto -> 1186
    //   985: aload #11
    //   987: ldc_w 'infinity'
    //   990: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   993: goto -> 1186
    //   996: aload #11
    //   998: ldc_w 'macro'
    //   1001: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   1004: goto -> 1186
    //   1007: aload #11
    //   1009: invokevirtual getSupportedFocusModes : ()Ljava/util/List;
    //   1012: ldc_w 'continuous-video'
    //   1015: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1020: ifeq -> 1034
    //   1023: aload #11
    //   1025: ldc_w 'continuous-video'
    //   1028: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   1031: goto -> 1186
    //   1034: bipush #6
    //   1036: invokestatic setSystemErrorCode : (I)V
    //   1039: iconst_0
    //   1040: ireturn
    //   1041: aload #11
    //   1043: ldc_w 'auto'
    //   1046: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   1049: goto -> 1400
    //   1052: aload #11
    //   1054: invokevirtual getSupportedFocusModes : ()Ljava/util/List;
    //   1057: ldc 'normal'
    //   1059: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1064: ifeq -> 1077
    //   1067: aload #11
    //   1069: ldc 'normal'
    //   1071: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   1074: goto -> 1186
    //   1077: aload #11
    //   1079: ldc_w 'auto'
    //   1082: invokevirtual setFocusMode : (Ljava/lang/String;)V
    //   1085: goto -> 1400
    //   1088: aload_3
    //   1089: checkcast java/lang/Number
    //   1092: invokevirtual intValue : ()I
    //   1095: istore_1
    //   1096: iload_1
    //   1097: tableswitch default -> 1128, 805306369 -> 1178, 805306370 -> 1140, 805306371 -> 1128, 805306372 -> 1134
    //   1128: iconst_3
    //   1129: invokestatic setSystemErrorCode : (I)V
    //   1132: iconst_0
    //   1133: ireturn
    //   1134: iconst_3
    //   1135: invokestatic setSystemErrorCode : (I)V
    //   1138: iconst_0
    //   1139: ireturn
    //   1140: aload #11
    //   1142: invokevirtual getSupportedFlashModes : ()Ljava/util/List;
    //   1145: ldc_w 'torch'
    //   1148: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1153: ifeq -> 1167
    //   1156: aload #11
    //   1158: ldc_w 'torch'
    //   1161: invokevirtual setFlashMode : (Ljava/lang/String;)V
    //   1164: goto -> 1186
    //   1167: aload #11
    //   1169: ldc_w 'on'
    //   1172: invokevirtual setFlashMode : (Ljava/lang/String;)V
    //   1175: goto -> 1186
    //   1178: aload #11
    //   1180: ldc_w 'off'
    //   1183: invokevirtual setFlashMode : (Ljava/lang/String;)V
    //   1186: iconst_0
    //   1187: istore_1
    //   1188: aload #10
    //   1190: getfield camera : Landroid/hardware/Camera;
    //   1193: aload #11
    //   1195: invokevirtual setParameters : (Landroid/hardware/Camera$Parameters;)V
    //   1198: iload_1
    //   1199: ifeq -> 1241
    //   1202: iload_2
    //   1203: ldc 536870914
    //   1205: if_icmpeq -> 1210
    //   1208: iconst_1
    //   1209: ireturn
    //   1210: aload #10
    //   1212: iconst_1
    //   1213: putfield isAutoFocusing : Z
    //   1216: aload #10
    //   1218: getfield camera : Landroid/hardware/Camera;
    //   1221: new com/vuforia/ar/pl/Camera1_Preview$1
    //   1224: dup
    //   1225: aload_0
    //   1226: invokespecial <init> : (Lcom/vuforia/ar/pl/Camera1_Preview;)V
    //   1229: invokevirtual autoFocus : (Landroid/hardware/Camera$AutoFocusCallback;)V
    //   1232: iconst_1
    //   1233: ireturn
    //   1234: bipush #6
    //   1236: invokestatic setSystemErrorCode : (I)V
    //   1239: iconst_0
    //   1240: ireturn
    //   1241: iconst_1
    //   1242: ireturn
    //   1243: bipush #6
    //   1245: invokestatic setSystemErrorCode : (I)V
    //   1248: iconst_0
    //   1249: ireturn
    //   1250: bipush #6
    //   1252: invokestatic setSystemErrorCode : (I)V
    //   1255: iconst_0
    //   1256: ireturn
    //   1257: iconst_4
    //   1258: invokestatic setSystemErrorCode : (I)V
    //   1261: iconst_0
    //   1262: ireturn
    //   1263: astore_3
    //   1264: goto -> 1397
    //   1267: astore_3
    //   1268: goto -> 1250
    //   1271: astore_3
    //   1272: goto -> 1250
    //   1275: astore_3
    //   1276: goto -> 1243
    //   1279: astore_3
    //   1280: goto -> 1234
    //   1283: iconst_0
    //   1284: istore #8
    //   1286: goto -> 280
    //   1289: iload_1
    //   1290: ifeq -> 1300
    //   1293: ldc_w 'true'
    //   1296: astore_3
    //   1297: goto -> 290
    //   1300: ldc_w 'false'
    //   1303: astore_3
    //   1304: goto -> 290
    //   1307: iload_1
    //   1308: iconst_1
    //   1309: iadd
    //   1310: istore_1
    //   1311: goto -> 603
    //   1314: aload_3
    //   1315: iconst_0
    //   1316: faload
    //   1317: fconst_0
    //   1318: fcmpg
    //   1319: iflt -> 867
    //   1322: aload_3
    //   1323: iconst_0
    //   1324: faload
    //   1325: fconst_1
    //   1326: fcmpl
    //   1327: ifgt -> 867
    //   1330: aload_3
    //   1331: iconst_1
    //   1332: faload
    //   1333: fconst_0
    //   1334: fcmpg
    //   1335: iflt -> 867
    //   1338: aload_3
    //   1339: iconst_1
    //   1340: faload
    //   1341: fconst_1
    //   1342: fcmpl
    //   1343: ifgt -> 867
    //   1346: aload_3
    //   1347: iconst_2
    //   1348: faload
    //   1349: fconst_0
    //   1350: fcmpg
    //   1351: iflt -> 867
    //   1354: aload_3
    //   1355: iconst_2
    //   1356: faload
    //   1357: fconst_1
    //   1358: fcmpl
    //   1359: ifgt -> 867
    //   1362: aload_3
    //   1363: iconst_3
    //   1364: faload
    //   1365: fconst_0
    //   1366: fcmpg
    //   1367: iflt -> 867
    //   1370: aload_3
    //   1371: iconst_3
    //   1372: faload
    //   1373: fconst_1
    //   1374: fcmpl
    //   1375: ifgt -> 867
    //   1378: aload_3
    //   1379: iconst_4
    //   1380: faload
    //   1381: fconst_0
    //   1382: fcmpg
    //   1383: iflt -> 867
    //   1386: aload_3
    //   1387: iconst_4
    //   1388: faload
    //   1389: fconst_1
    //   1390: fcmpl
    //   1391: ifle -> 739
    //   1394: goto -> 867
    //   1397: goto -> 1250
    //   1400: iconst_1
    //   1401: istore_1
    //   1402: goto -> 1188
    // Exception table:
    //   from	to	target	type
    //   226	242	1263	java/lang/Exception
    //   245	251	1263	java/lang/Exception
    //   254	270	1263	java/lang/Exception
    //   280	287	1263	java/lang/Exception
    //   290	299	1263	java/lang/Exception
    //   302	307	1263	java/lang/Exception
    //   309	314	1263	java/lang/Exception
    //   316	321	1263	java/lang/Exception
    //   323	328	1263	java/lang/Exception
    //   330	335	1263	java/lang/Exception
    //   337	342	1263	java/lang/Exception
    //   344	372	1263	java/lang/Exception
    //   375	380	1263	java/lang/Exception
    //   382	387	1263	java/lang/Exception
    //   389	394	1263	java/lang/Exception
    //   396	404	1263	java/lang/Exception
    //   416	457	1263	java/lang/Exception
    //   459	473	1263	java/lang/Exception
    //   476	490	1263	java/lang/Exception
    //   493	498	1263	java/lang/Exception
    //   500	524	1263	java/lang/Exception
    //   531	536	1263	java/lang/Exception
    //   538	551	1263	java/lang/Exception
    //   554	559	1263	java/lang/Exception
    //   561	583	1263	java/lang/Exception
    //   591	601	1263	java/lang/Exception
    //   606	631	1263	java/lang/Exception
    //   639	648	1263	java/lang/Exception
    //   651	659	1263	java/lang/Exception
    //   671	675	1263	java/lang/Exception
    //   677	691	1263	java/lang/Exception
    //   694	708	1263	java/lang/Exception
    //   711	737	1263	java/lang/Exception
    //   775	864	1267	java/lang/Exception
    //   867	871	1267	java/lang/Exception
    //   873	878	1271	java/lang/Exception
    //   880	885	1271	java/lang/Exception
    //   887	892	1271	java/lang/Exception
    //   894	910	1267	java/lang/Exception
    //   968	972	1267	java/lang/Exception
    //   974	982	1267	java/lang/Exception
    //   985	993	1267	java/lang/Exception
    //   996	1004	1267	java/lang/Exception
    //   1007	1031	1267	java/lang/Exception
    //   1034	1039	1271	java/lang/Exception
    //   1041	1049	1267	java/lang/Exception
    //   1052	1074	1267	java/lang/Exception
    //   1077	1085	1267	java/lang/Exception
    //   1088	1096	1267	java/lang/Exception
    //   1128	1132	1271	java/lang/Exception
    //   1134	1138	1267	java/lang/Exception
    //   1140	1164	1267	java/lang/Exception
    //   1167	1175	1267	java/lang/Exception
    //   1178	1186	1267	java/lang/Exception
    //   1188	1198	1275	java/lang/Exception
    //   1210	1232	1279	java/lang/Exception
  }
  
  boolean setUntypedCameraParameter(int paramInt, String paramString1, String paramString2) {
    CameraCacheInfo cameraCacheInfo = getCameraCacheInfo(paramInt);
    if (cameraCacheInfo == null || cameraCacheInfo.camera == null) {
      SystemTools.setSystemErrorCode(4);
      return false;
    } 
    Camera.Parameters parameters = getCameraParameters(cameraCacheInfo.camera);
    if (parameters == null) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
    try {
      parameters.set(paramString1, paramString2);
      cameraCacheInfo.camera.setParameters(parameters);
      return true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
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
      cameraCacheInfo.camera.startPreview();
      cameraCacheInfo.status = 268443651;
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
      cameraCacheInfo.camera.stopPreview();
      cameraCacheInfo.status = 268443650;
      return true;
    } catch (Exception exception) {
      SystemTools.setSystemErrorCode(6);
      return false;
    } 
  }
  
  public class CameraCacheInfo {
    byte[][] buffer;
    
    int bufferFormatPL;
    
    int bufferHeight;
    
    int bufferSize;
    
    int bufferWidth;
    
    Camera camera;
    
    int[] caps;
    
    long deviceHandle;
    
    int deviceID;
    
    boolean isAutoFocusing;
    
    boolean isHalDriven;
    
    int overrideFormatAndroid;
    
    int overrideHeight;
    
    int overrideWidth;
    
    int requestFormatAndroid;
    
    int requestHeight;
    
    int requestWidth;
    
    int status;
    
    CameraSurface surface;
    
    SurfaceTexture surfaceTexture;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ar\pl\Camera1_Preview.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */