package com.vuforia;

public class RotationalDeviceTracker extends DeviceTracker {
  private long swigCPtr;
  
  protected RotationalDeviceTracker(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.RotationalDeviceTracker_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(RotationalDeviceTracker paramRotationalDeviceTracker) {
    return (paramRotationalDeviceTracker == null) ? 0L : paramRotationalDeviceTracker.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.RotationalDeviceTracker_getClassType(), true);
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
    //   27: invokestatic delete_RotationalDeviceTracker : (J)V
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
    boolean bool = paramObject instanceof RotationalDeviceTracker;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((RotationalDeviceTracker)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public HandheldTransformModel getDefaultHandheldModel() {
    long l = VuforiaJNI.RotationalDeviceTracker_getDefaultHandheldModel(this.swigCPtr, this);
    return (l == 0L) ? null : new HandheldTransformModel(l, false);
  }
  
  public HeadTransformModel getDefaultHeadModel() {
    long l = VuforiaJNI.RotationalDeviceTracker_getDefaultHeadModel(this.swigCPtr, this);
    return (l == 0L) ? null : new HeadTransformModel(l, false);
  }
  
  public TransformModel getModelCorrection() {
    long l = VuforiaJNI.RotationalDeviceTracker_getModelCorrection(this.swigCPtr, this);
    if (l == 0L)
      return null; 
    int i = (new TransformModel(l, false)).getType();
    return (TransformModel)((i != 0) ? ((i != 1) ? null : new HandheldTransformModel(l, false)) : new HeadTransformModel(l, false));
  }
  
  public boolean getPosePrediction() {
    return VuforiaJNI.RotationalDeviceTracker_getPosePrediction(this.swigCPtr, this);
  }
  
  public boolean recenter() {
    return VuforiaJNI.RotationalDeviceTracker_recenter(this.swigCPtr, this);
  }
  
  public boolean setModelCorrection(TransformModel paramTransformModel) {
    return VuforiaJNI.RotationalDeviceTracker_setModelCorrection(this.swigCPtr, this, TransformModel.getCPtr(paramTransformModel), paramTransformModel);
  }
  
  public boolean setPosePrediction(boolean paramBoolean) {
    return VuforiaJNI.RotationalDeviceTracker_setPosePrediction(this.swigCPtr, this, paramBoolean);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\RotationalDeviceTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */