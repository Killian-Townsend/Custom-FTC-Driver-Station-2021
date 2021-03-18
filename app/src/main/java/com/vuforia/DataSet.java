package com.vuforia;

public class DataSet {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected DataSet(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public static boolean exists(String paramString, int paramInt) {
    return VuforiaJNI.DataSet_exists(paramString, paramInt);
  }
  
  protected static long getCPtr(DataSet paramDataSet) {
    return (paramDataSet == null) ? 0L : paramDataSet.swigCPtr;
  }
  
  public MultiTarget createMultiTarget(String paramString) {
    long l = VuforiaJNI.DataSet_createMultiTarget(this.swigCPtr, this, paramString);
    return (l == 0L) ? null : new MultiTarget(l, false);
  }
  
  public Trackable createTrackable(TrackableSource paramTrackableSource) {
    long l = VuforiaJNI.DataSet_createTrackable(this.swigCPtr, this, TrackableSource.getCPtr(paramTrackableSource), paramTrackableSource);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(MultiTarget.getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
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
    //   27: invokestatic delete_DataSet : (J)V
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
  
  public boolean destroy(Trackable paramTrackable) {
    return VuforiaJNI.DataSet_destroy(this.swigCPtr, this, Trackable.getCPtr(paramTrackable), paramTrackable);
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof DataSet;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((DataSet)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getNumTrackables() {
    return VuforiaJNI.DataSet_getNumTrackables(this.swigCPtr, this);
  }
  
  public Trackable getTrackable(int paramInt) {
    long l = VuforiaJNI.DataSet_getTrackable(this.swigCPtr, this, paramInt);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(MultiTarget.getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
  }
  
  public boolean hasReachedTrackableLimit() {
    return VuforiaJNI.DataSet_hasReachedTrackableLimit(this.swigCPtr, this);
  }
  
  public boolean isActive() {
    return VuforiaJNI.DataSet_isActive(this.swigCPtr, this);
  }
  
  public boolean load(String paramString, int paramInt) {
    return VuforiaJNI.DataSet_load(this.swigCPtr, this, paramString, paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\DataSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */