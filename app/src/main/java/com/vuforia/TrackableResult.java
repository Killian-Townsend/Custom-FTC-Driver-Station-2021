package com.vuforia;

public class TrackableResult {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected TrackableResult(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(TrackableResult paramTrackableResult) {
    return (paramTrackableResult == null) ? 0L : paramTrackableResult.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.TrackableResult_getClassType(), true);
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
    //   27: invokestatic delete_TrackableResult : (J)V
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
    boolean bool = paramObject instanceof TrackableResult;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((TrackableResult)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getCoordinateSystem() {
    return VuforiaJNI.TrackableResult_getCoordinateSystem(this.swigCPtr, this);
  }
  
  public Matrix34F getPose() {
    return new Matrix34F(VuforiaJNI.TrackableResult_getPose(this.swigCPtr, this), false);
  }
  
  public int getStatus() {
    return VuforiaJNI.TrackableResult_getStatus(this.swigCPtr, this);
  }
  
  public int getStatusInfo() {
    return VuforiaJNI.TrackableResult_getStatusInfo(this.swigCPtr, this);
  }
  
  public double getTimeStamp() {
    return VuforiaJNI.TrackableResult_getTimeStamp(this.swigCPtr, this);
  }
  
  public Trackable getTrackable() {
    long l = VuforiaJNI.TrackableResult_getTrackable(this.swigCPtr, this);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(MultiTarget.getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
  }
  
  public Type getType() {
    return new Type(VuforiaJNI.TrackableResult_getType(this.swigCPtr, this), true);
  }
  
  public boolean isOfType(Type paramType) {
    return VuforiaJNI.TrackableResult_isOfType(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
  }
  
  public static final class STATUS {
    public static final int DETECTED = 2;
    
    public static final int EXTENDED_TRACKED = 4;
    
    public static final int LIMITED = 1;
    
    public static final int NO_POSE = 0;
    
    public static final int TRACKED = 3;
  }
  
  public static final class STATUS_INFO {
    public static final int EXCESSIVE_MOTION = 3;
    
    public static final int INITIALIZING = 2;
    
    public static final int INSUFFICIENT_FEATURES = 4;
    
    public static final int NORMAL = 0;
    
    public static final int UNKNOWN = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\TrackableResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */