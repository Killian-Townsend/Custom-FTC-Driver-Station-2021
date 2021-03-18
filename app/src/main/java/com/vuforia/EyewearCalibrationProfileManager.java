package com.vuforia;

public class EyewearCalibrationProfileManager {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected EyewearCalibrationProfileManager(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(EyewearCalibrationProfileManager paramEyewearCalibrationProfileManager) {
    return (paramEyewearCalibrationProfileManager == null) ? 0L : paramEyewearCalibrationProfileManager.swigCPtr;
  }
  
  public boolean clearProfile(int paramInt) {
    return VuforiaJNI.EyewearCalibrationProfileManager_clearProfile(this.swigCPtr, this, paramInt);
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
    //   27: invokestatic delete_EyewearCalibrationProfileManager : (J)V
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
    boolean bool = paramObject instanceof EyewearCalibrationProfileManager;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((EyewearCalibrationProfileManager)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getActiveProfile() {
    return VuforiaJNI.EyewearCalibrationProfileManager_getActiveProfile(this.swigCPtr, this);
  }
  
  public Matrix34F getCameraToEyePose(int paramInt1, int paramInt2) {
    return new Matrix34F(VuforiaJNI.EyewearCalibrationProfileManager_getCameraToEyePose(this.swigCPtr, this, paramInt1, paramInt2), true);
  }
  
  public Matrix34F getEyeProjection(int paramInt1, int paramInt2) {
    return new Matrix34F(VuforiaJNI.EyewearCalibrationProfileManager_getEyeProjection(this.swigCPtr, this, paramInt1, paramInt2), true);
  }
  
  public long getMaxCount() {
    return VuforiaJNI.EyewearCalibrationProfileManager_getMaxCount(this.swigCPtr, this);
  }
  
  public String getProfileName(int paramInt) {
    short[] arrayOfShort = VuforiaJNI.EyewearCalibrationProfileManager_getProfileName(this.swigCPtr, this, paramInt);
    if (arrayOfShort == null)
      return null; 
    StringBuilder stringBuilder = new StringBuilder(arrayOfShort.length);
    int i = arrayOfShort.length;
    for (paramInt = 0; paramInt < i; paramInt++)
      stringBuilder.appendCodePoint(arrayOfShort[paramInt]); 
    return stringBuilder.toString();
  }
  
  public long getUsedCount() {
    return VuforiaJNI.EyewearCalibrationProfileManager_getUsedCount(this.swigCPtr, this);
  }
  
  public boolean isProfileUsed(int paramInt) {
    return VuforiaJNI.EyewearCalibrationProfileManager_isProfileUsed(this.swigCPtr, this, paramInt);
  }
  
  public boolean setActiveProfile(int paramInt) {
    return VuforiaJNI.EyewearCalibrationProfileManager_setActiveProfile(this.swigCPtr, this, paramInt);
  }
  
  public boolean setCameraToEyePose(int paramInt1, int paramInt2, Matrix34F paramMatrix34F) {
    return VuforiaJNI.EyewearCalibrationProfileManager_setCameraToEyePose(this.swigCPtr, this, paramInt1, paramInt2, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
  }
  
  public boolean setEyeProjection(int paramInt1, int paramInt2, Matrix34F paramMatrix34F) {
    return VuforiaJNI.EyewearCalibrationProfileManager_setEyeProjection(this.swigCPtr, this, paramInt1, paramInt2, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
  }
  
  public boolean setProfileName(int paramInt, String paramString) {
    return VuforiaJNI.EyewearCalibrationProfileManager_setProfileName(this.swigCPtr, this, paramInt, Vuforia.convertStringToShortArray(paramString));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\EyewearCalibrationProfileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */