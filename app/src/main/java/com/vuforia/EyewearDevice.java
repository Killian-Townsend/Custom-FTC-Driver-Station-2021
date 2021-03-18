package com.vuforia;

public class EyewearDevice extends Device {
  private long swigCPtr;
  
  protected EyewearDevice(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.EyewearDevice_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(EyewearDevice paramEyewearDevice) {
    return (paramEyewearDevice == null) ? 0L : paramEyewearDevice.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.EyewearDevice_getClassType(), true);
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
    //   27: invokestatic delete_EyewearDevice : (J)V
    //   30: aload_0
    //   31: lconst_0
    //   32: putfield swigCPtr : J
    //   35: aload_0
    //   36: invokespecial delete : ()V
    //   39: aload_0
    //   40: monitorexit
    //   41: return
    //   42: astore_1
    //   43: aload_0
    //   44: monitorexit
    //   45: aload_1
    //   46: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	42	finally
    //   30	35	42	finally
    //   35	39	42	finally
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof EyewearDevice;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((EyewearDevice)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public EyewearCalibrationProfileManager getCalibrationProfileManager() {
    return new EyewearCalibrationProfileManager(VuforiaJNI.EyewearDevice_getCalibrationProfileManager(this.swigCPtr, this), false);
  }
  
  public int getScreenOrientation() {
    return VuforiaJNI.EyewearDevice_getScreenOrientation(this.swigCPtr, this);
  }
  
  public EyewearUserCalibrator getUserCalibrator() {
    return new EyewearUserCalibrator(VuforiaJNI.EyewearDevice_getUserCalibrator(this.swigCPtr, this), false);
  }
  
  public boolean isDisplayExtended() {
    return VuforiaJNI.EyewearDevice_isDisplayExtended(this.swigCPtr, this);
  }
  
  public boolean isDisplayExtendedGLOnly() {
    return VuforiaJNI.EyewearDevice_isDisplayExtendedGLOnly(this.swigCPtr, this);
  }
  
  public boolean isDualDisplay() {
    return VuforiaJNI.EyewearDevice_isDualDisplay(this.swigCPtr, this);
  }
  
  public boolean isPredictiveTrackingEnabled() {
    return VuforiaJNI.EyewearDevice_isPredictiveTrackingEnabled(this.swigCPtr, this);
  }
  
  public boolean isSeeThru() {
    return VuforiaJNI.EyewearDevice_isSeeThru(this.swigCPtr, this);
  }
  
  public boolean setDisplayExtended(boolean paramBoolean) {
    return VuforiaJNI.EyewearDevice_setDisplayExtended(this.swigCPtr, this, paramBoolean);
  }
  
  public boolean setPredictiveTracking(boolean paramBoolean) {
    return VuforiaJNI.EyewearDevice_setPredictiveTracking(this.swigCPtr, this, paramBoolean);
  }
  
  public static final class ORIENTATION {
    public static final int ORIENTATION_LANDSCAPE_LEFT = 2;
    
    public static final int ORIENTATION_LANDSCAPE_RIGHT = 3;
    
    public static final int ORIENTATION_PORTRAIT = 1;
    
    public static final int ORIENTATION_UNDEFINED = 0;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\EyewearDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */