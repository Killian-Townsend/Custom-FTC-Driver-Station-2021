package com.vuforia;

public class CameraDevice {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected CameraDevice(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(CameraDevice paramCameraDevice) {
    return (paramCameraDevice == null) ? 0L : paramCameraDevice.swigCPtr;
  }
  
  public static CameraDevice getInstance() {
    if (Vuforia.wasInitializedJava())
      return new CameraDevice(VuforiaJNI.CameraDevice_getInstance(), false); 
    throw new RuntimeException("Use of the Java Vuforia APIs requires initalization via the com.vuforia.Vuforia class");
  }
  
  public boolean deinit() {
    return VuforiaJNI.CameraDevice_deinit(this.swigCPtr, this);
  }
  
  protected void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 35
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifeq -> 30
    //   18: aload_0
    //   19: iconst_0
    //   20: putfield swigCMemOwn : Z
    //   23: aload_0
    //   24: getfield swigCPtr : J
    //   27: invokestatic delete_CameraDevice : (J)V
    //   30: aload_0
    //   31: lconst_0
    //   32: putfield swigCPtr : J
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	38	finally
    //   30	35	38	finally
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof CameraDevice;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((CameraDevice)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public CameraCalibration getCameraCalibration() {
    return new CameraCalibration(VuforiaJNI.CameraDevice_getCameraCalibration(this.swigCPtr, this), false);
  }
  
  public int getCameraDirection() {
    return VuforiaJNI.CameraDevice_getCameraDirection(this.swigCPtr, this);
  }
  
  public boolean getCameraField(int paramInt, CameraField paramCameraField) {
    return VuforiaJNI.CameraDevice_getCameraField(this.swigCPtr, this, paramInt, CameraField.getCPtr(paramCameraField), paramCameraField);
  }
  
  public boolean getFieldBool(String paramString, boolean[] paramArrayOfboolean) {
    return VuforiaJNI.CameraDevice_getFieldBool(this.swigCPtr, this, paramString, paramArrayOfboolean);
  }
  
  public boolean getFieldFloat(String paramString, float[] paramArrayOffloat) {
    return VuforiaJNI.CameraDevice_getFieldFloat(this.swigCPtr, this, paramString, paramArrayOffloat);
  }
  
  public boolean getFieldInt64(String paramString, long[] paramArrayOflong) {
    return VuforiaJNI.CameraDevice_getFieldInt64(this.swigCPtr, this, paramString, paramArrayOflong);
  }
  
  public boolean getFieldInt64Range(String paramString, long[] paramArrayOflong) {
    return VuforiaJNI.CameraDevice_getFieldInt64Range(this.swigCPtr, this, paramString, paramArrayOflong);
  }
  
  public String getFieldString(String paramString) {
    return VuforiaJNI.CameraDevice_getFieldString(this.swigCPtr, this, paramString);
  }
  
  public int getNumFields() {
    return VuforiaJNI.CameraDevice_getNumFields(this.swigCPtr, this);
  }
  
  public int getNumVideoModes() {
    return VuforiaJNI.CameraDevice_getNumVideoModes(this.swigCPtr, this);
  }
  
  public VideoMode getVideoMode(int paramInt) {
    return new VideoMode(VuforiaJNI.CameraDevice_getVideoMode(this.swigCPtr, this, paramInt), true);
  }
  
  public boolean init() {
    return VuforiaJNI.CameraDevice_init__SWIG_1(this.swigCPtr, this);
  }
  
  public boolean init(int paramInt) {
    return VuforiaJNI.CameraDevice_init__SWIG_0(this.swigCPtr, this, paramInt);
  }
  
  public boolean selectVideoMode(int paramInt) {
    return VuforiaJNI.CameraDevice_selectVideoMode(this.swigCPtr, this, paramInt);
  }
  
  public boolean setField(String paramString, float paramFloat) {
    return VuforiaJNI.CameraDevice_setField__SWIG_2(this.swigCPtr, this, paramString, paramFloat);
  }
  
  public boolean setField(String paramString, long paramLong) {
    return VuforiaJNI.CameraDevice_setField__SWIG_1(this.swigCPtr, this, paramString, paramLong);
  }
  
  public boolean setField(String paramString1, String paramString2) {
    return VuforiaJNI.CameraDevice_setField__SWIG_0(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public boolean setField(String paramString, boolean paramBoolean) {
    return VuforiaJNI.CameraDevice_setField__SWIG_3(this.swigCPtr, this, paramString, paramBoolean);
  }
  
  public boolean setField(String paramString, long[] paramArrayOflong) {
    return VuforiaJNI.CameraDevice_setField__SWIG_4(this.swigCPtr, this, paramString, paramArrayOflong);
  }
  
  public boolean setFlashTorchMode(boolean paramBoolean) {
    return VuforiaJNI.CameraDevice_setFlashTorchMode(this.swigCPtr, this, paramBoolean);
  }
  
  public boolean setFocusMode(int paramInt) {
    return VuforiaJNI.CameraDevice_setFocusMode(this.swigCPtr, this, paramInt);
  }
  
  public boolean start() {
    return VuforiaJNI.CameraDevice_start(this.swigCPtr, this);
  }
  
  public boolean stop() {
    return VuforiaJNI.CameraDevice_stop(this.swigCPtr, this);
  }
  
  public static final class CAMERA_DIRECTION {
    public static final int CAMERA_DIRECTION_BACK = 1;
    
    public static final int CAMERA_DIRECTION_DEFAULT = 0;
    
    public static final int CAMERA_DIRECTION_FRONT = 2;
  }
  
  public static final class FOCUS_MODE {
    public static final int FOCUS_MODE_CONTINUOUSAUTO = 2;
    
    public static final int FOCUS_MODE_INFINITY = 3;
    
    public static final int FOCUS_MODE_MACRO = 4;
    
    public static final int FOCUS_MODE_NORMAL = 0;
    
    public static final int FOCUS_MODE_TRIGGERAUTO = 1;
  }
  
  public static final class MODE {
    public static final int MODE_DEFAULT = -1;
    
    public static final int MODE_OPTIMIZE_QUALITY = -3;
    
    public static final int MODE_OPTIMIZE_SPEED = -2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\CameraDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */