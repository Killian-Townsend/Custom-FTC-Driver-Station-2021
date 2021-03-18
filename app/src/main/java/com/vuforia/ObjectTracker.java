package com.vuforia;

public class ObjectTracker extends Tracker {
  private long swigCPtr;
  
  protected ObjectTracker(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.ObjectTracker_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(ObjectTracker paramObjectTracker) {
    return (paramObjectTracker == null) ? 0L : paramObjectTracker.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.ObjectTracker_getClassType(), true);
  }
  
  public boolean activateDataSet(DataSet paramDataSet) {
    return VuforiaJNI.ObjectTracker_activateDataSet(this.swigCPtr, this, DataSet.getCPtr(paramDataSet), paramDataSet);
  }
  
  public DataSet createDataSet() {
    long l = VuforiaJNI.ObjectTracker_createDataSet(this.swigCPtr, this);
    return (l == 0L) ? null : new DataSet(l, false);
  }
  
  public boolean deactivateDataSet(DataSet paramDataSet) {
    return VuforiaJNI.ObjectTracker_deactivateDataSet(this.swigCPtr, this, DataSet.getCPtr(paramDataSet), paramDataSet);
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
    //   27: invokestatic delete_ObjectTracker : (J)V
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
  
  public boolean destroyDataSet(DataSet paramDataSet) {
    return VuforiaJNI.ObjectTracker_destroyDataSet(this.swigCPtr, this, DataSet.getCPtr(paramDataSet), paramDataSet);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof ObjectTracker;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((ObjectTracker)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public DataSet getActiveDataSet(int paramInt) {
    long l = VuforiaJNI.ObjectTracker_getActiveDataSet(this.swigCPtr, this, paramInt);
    return (l == 0L) ? null : new DataSet(l, false);
  }
  
  public int getActiveDataSetCount() {
    return VuforiaJNI.ObjectTracker_getActiveDataSetCount(this.swigCPtr, this);
  }
  
  public ImageTargetBuilder getImageTargetBuilder() {
    long l = VuforiaJNI.ObjectTracker_getImageTargetBuilder(this.swigCPtr, this);
    return (l == 0L) ? null : new ImageTargetBuilder(l, false);
  }
  
  public TargetFinder getTargetFinder() {
    long l = VuforiaJNI.ObjectTracker_getTargetFinder(this.swigCPtr, this);
    return (l == 0L) ? null : new TargetFinder(l, false);
  }
  
  public boolean persistExtendedTracking(boolean paramBoolean) {
    return VuforiaJNI.ObjectTracker_persistExtendedTracking(this.swigCPtr, this, paramBoolean);
  }
  
  public boolean resetExtendedTracking() {
    return VuforiaJNI.ObjectTracker_resetExtendedTracking(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ObjectTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */