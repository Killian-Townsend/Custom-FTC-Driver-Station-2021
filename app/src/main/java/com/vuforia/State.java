package com.vuforia;

public class State implements Cloneable {
  private Frame mFrame = null;
  
  private Object mFrameMutex = new Object();
  
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public State() {
    this(VuforiaJNI.new_State__SWIG_0(), true);
  }
  
  protected State(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public State(State paramState) {
    this(VuforiaJNI.new_State__SWIG_1(getCPtr(paramState), paramState), true);
  }
  
  protected static long getCPtr(State paramState) {
    return (paramState == null) ? 0L : paramState.swigCPtr;
  }
  
  public State clone() {
    return new State(this);
  }
  
  protected void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 71
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifeq -> 30
    //   18: aload_0
    //   19: iconst_0
    //   20: putfield swigCMemOwn : Z
    //   23: aload_0
    //   24: getfield swigCPtr : J
    //   27: invokestatic delete_State : (J)V
    //   30: aload_0
    //   31: getfield mFrameMutex : Ljava/lang/Object;
    //   34: astore_1
    //   35: aload_1
    //   36: monitorenter
    //   37: aload_0
    //   38: getfield mFrame : Lcom/vuforia/Frame;
    //   41: ifnull -> 56
    //   44: aload_0
    //   45: getfield mFrame : Lcom/vuforia/Frame;
    //   48: invokevirtual delete : ()V
    //   51: aload_0
    //   52: aconst_null
    //   53: putfield mFrame : Lcom/vuforia/Frame;
    //   56: aload_1
    //   57: monitorexit
    //   58: aload_0
    //   59: lconst_0
    //   60: putfield swigCPtr : J
    //   63: goto -> 71
    //   66: astore_2
    //   67: aload_1
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //   74: astore_1
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_1
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	74	finally
    //   30	37	74	finally
    //   37	56	66	finally
    //   56	58	66	finally
    //   58	63	74	finally
    //   67	69	66	finally
    //   69	71	74	finally
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof State;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((State)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public CameraCalibration getCameraCalibration() {
    long l = VuforiaJNI.State_getCameraCalibration(this.swigCPtr, this);
    return (l == 0L) ? null : new CameraCalibration(l, false);
  }
  
  public DeviceTrackableResult getDeviceTrackableResult() {
    long l = VuforiaJNI.State_getDeviceTrackableResult(this.swigCPtr, this);
    return (l == 0L) ? null : new DeviceTrackableResult(l, false);
  }
  
  public Frame getFrame() {
    synchronized (this.mFrameMutex) {
      if (this.mFrame == null)
        this.mFrame = new Frame(VuforiaJNI.State_getFrame(this.swigCPtr, this), true); 
      return this.mFrame;
    } 
  }
  
  public Illumination getIllumination() {
    long l = VuforiaJNI.State_getIllumination(this.swigCPtr, this);
    return (l == 0L) ? null : new Illumination(l, false);
  }
  
  public int getNumTrackableResults() {
    return VuforiaJNI.State_getNumTrackableResults(this.swigCPtr, this);
  }
  
  public int getNumTrackables() {
    return VuforiaJNI.State_getNumTrackables(this.swigCPtr, this);
  }
  
  public Trackable getTrackable(int paramInt) {
    long l = VuforiaJNI.State_getTrackable(this.swigCPtr, this, paramInt);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(MultiTarget.getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
  }
  
  public TrackableResult getTrackableResult(int paramInt) {
    long l = VuforiaJNI.State_getTrackableResult(this.swigCPtr, this, paramInt);
    if (l == 0L)
      return null; 
    TrackableResult trackableResult = new TrackableResult(l, false);
    return (TrackableResult)(trackableResult.isOfType(ImageTargetResult.getClassType()) ? new ImageTargetResult(l, false) : (trackableResult.isOfType(CylinderTargetResult.getClassType()) ? new CylinderTargetResult(l, false) : (trackableResult.isOfType(MultiTargetResult.getClassType()) ? new MultiTargetResult(l, false) : (trackableResult.isOfType(VuMarkTargetResult.getClassType()) ? new VuMarkTargetResult(l, false) : (trackableResult.isOfType(ModelTargetResult.getClassType()) ? new ModelTargetResult(l, false) : (trackableResult.isOfType(ObjectTargetResult.getClassType()) ? new ObjectTargetResult(l, false) : (trackableResult.isOfType(AnchorResult.getClassType()) ? new AnchorResult(l, false) : (trackableResult.isOfType(DeviceTrackableResult.getClassType()) ? new DeviceTrackableResult(l, false) : null))))))));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\State.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */