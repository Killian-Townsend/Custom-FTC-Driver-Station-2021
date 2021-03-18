package com.vuforia;

public class TargetFinder {
  public static final int FILTER_CURRENTLY_TRACKED = 1;
  
  public static final int FILTER_NONE = 0;
  
  public static final int INIT_DEFAULT = 0;
  
  public static final int INIT_ERROR_NO_NETWORK_CONNECTION = -1;
  
  public static final int INIT_ERROR_SERVICE_NOT_AVAILABLE = -2;
  
  public static final int INIT_RUNNING = 1;
  
  public static final int INIT_SUCCESS = 2;
  
  public static final int UPDATE_ERROR_AUTHORIZATION_FAILED = -1;
  
  public static final int UPDATE_ERROR_BAD_FRAME_QUALITY = -5;
  
  public static final int UPDATE_ERROR_NO_NETWORK_CONNECTION = -3;
  
  public static final int UPDATE_ERROR_PROJECT_SUSPENDED = -2;
  
  public static final int UPDATE_ERROR_REQUEST_TIMEOUT = -8;
  
  public static final int UPDATE_ERROR_SERVICE_NOT_AVAILABLE = -4;
  
  public static final int UPDATE_ERROR_TIMESTAMP_OUT_OF_RANGE = -7;
  
  public static final int UPDATE_ERROR_UPDATE_SDK = -6;
  
  public static final int UPDATE_NO_MATCH = 0;
  
  public static final int UPDATE_NO_REQUEST = 1;
  
  public static final int UPDATE_RESULTS_AVAILABLE = 2;
  
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected TargetFinder(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(TargetFinder paramTargetFinder) {
    return (paramTargetFinder == null) ? 0L : paramTargetFinder.swigCPtr;
  }
  
  public void clearTrackables() {
    VuforiaJNI.TargetFinder_clearTrackables(this.swigCPtr, this);
  }
  
  public boolean deinit() {
    return VuforiaJNI.TargetFinder_deinit(this.swigCPtr, this);
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
    //   27: invokestatic delete_TargetFinder : (J)V
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
  
  public ImageTarget enableTracking(TargetSearchResult paramTargetSearchResult) {
    long l = VuforiaJNI.TargetFinder_enableTracking(this.swigCPtr, this, TargetSearchResult.getCPtr(paramTargetSearchResult), paramTargetSearchResult);
    return (l == 0L) ? null : new ImageTarget(l, false);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof TargetFinder;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((TargetFinder)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public ImageTarget getImageTarget(int paramInt) {
    long l = VuforiaJNI.TargetFinder_getImageTarget(this.swigCPtr, this, paramInt);
    return (l == 0L) ? null : new ImageTarget(l, false);
  }
  
  public int getInitState() {
    return VuforiaJNI.TargetFinder_getInitState(this.swigCPtr, this);
  }
  
  public int getNumImageTargets() {
    return VuforiaJNI.TargetFinder_getNumImageTargets(this.swigCPtr, this);
  }
  
  public TargetSearchResult getResult(int paramInt) {
    long l = VuforiaJNI.TargetFinder_getResult(this.swigCPtr, this, paramInt);
    return (l == 0L) ? null : new TargetSearchResult(l, false);
  }
  
  public int getResultCount() {
    return VuforiaJNI.TargetFinder_getResultCount(this.swigCPtr, this);
  }
  
  public boolean isRequesting() {
    return VuforiaJNI.TargetFinder_isRequesting(this.swigCPtr, this);
  }
  
  public boolean startInit(String paramString1, String paramString2) {
    return VuforiaJNI.TargetFinder_startInit(this.swigCPtr, this, paramString1, paramString2);
  }
  
  public boolean startRecognition() {
    return VuforiaJNI.TargetFinder_startRecognition(this.swigCPtr, this);
  }
  
  public boolean stop() {
    return VuforiaJNI.TargetFinder_stop(this.swigCPtr, this);
  }
  
  public int updateSearchResults() {
    return VuforiaJNI.TargetFinder_updateSearchResults__SWIG_1(this.swigCPtr, this);
  }
  
  public int updateSearchResults(int paramInt) {
    return VuforiaJNI.TargetFinder_updateSearchResults__SWIG_0(this.swigCPtr, this, paramInt);
  }
  
  public void waitUntilInitFinished() {
    VuforiaJNI.TargetFinder_waitUntilInitFinished(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\TargetFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */