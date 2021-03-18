package com.vuforia;

public class PositionalDeviceTracker extends DeviceTracker {
  private long swigCPtr;
  
  protected PositionalDeviceTracker(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.PositionalDeviceTracker_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(PositionalDeviceTracker paramPositionalDeviceTracker) {
    return (paramPositionalDeviceTracker == null) ? 0L : paramPositionalDeviceTracker.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.PositionalDeviceTracker_getClassType(), true);
  }
  
  public Anchor createAnchor(String paramString, HitTestResult paramHitTestResult) {
    long l = VuforiaJNI.PositionalDeviceTracker_createAnchor__SWIG_1(this.swigCPtr, this, paramString, HitTestResult.getCPtr(paramHitTestResult), paramHitTestResult);
    return (l == 0L) ? null : new Anchor(l, false);
  }
  
  public Anchor createAnchor(String paramString, Matrix34F paramMatrix34F) {
    long l = VuforiaJNI.PositionalDeviceTracker_createAnchor__SWIG_0(this.swigCPtr, this, paramString, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
    return (l == 0L) ? null : new Anchor(l, false);
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
    //   27: invokestatic delete_PositionalDeviceTracker : (J)V
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
  
  public boolean destroyAnchor(Anchor paramAnchor) {
    return VuforiaJNI.PositionalDeviceTracker_destroyAnchor(this.swigCPtr, this, Anchor.getCPtr(paramAnchor), paramAnchor);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof PositionalDeviceTracker;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((PositionalDeviceTracker)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public Anchor getAnchor(int paramInt) {
    long l = VuforiaJNI.PositionalDeviceTracker_getAnchor(this.swigCPtr, this, paramInt);
    return (l == 0L) ? null : new Anchor(l, false);
  }
  
  public int getNumAnchors() {
    return VuforiaJNI.PositionalDeviceTracker_getNumAnchors(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\PositionalDeviceTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */