package com.vuforia;

public class MultiTarget extends ObjectTarget {
  private long swigCPtr;
  
  protected MultiTarget(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.MultiTarget_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(MultiTarget paramMultiTarget) {
    return (paramMultiTarget == null) ? 0L : paramMultiTarget.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.MultiTarget_getClassType(), true);
  }
  
  public int addPart(Trackable paramTrackable) {
    return VuforiaJNI.MultiTarget_addPart(this.swigCPtr, this, Trackable.getCPtr(paramTrackable), paramTrackable);
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
    //   27: invokestatic delete_MultiTarget : (J)V
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
    boolean bool = paramObject instanceof MultiTarget;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((MultiTarget)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getNumParts() {
    return VuforiaJNI.MultiTarget_getNumParts(this.swigCPtr, this);
  }
  
  public Trackable getPart(int paramInt) {
    long l = VuforiaJNI.MultiTarget_getPart__SWIG_0(this.swigCPtr, this, paramInt);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
  }
  
  public Trackable getPart(String paramString) {
    long l = VuforiaJNI.MultiTarget_getPart__SWIG_1(this.swigCPtr, this, paramString);
    if (l == 0L)
      return null; 
    Trackable trackable = new Trackable(l, false);
    return (Trackable)(trackable.isOfType(ImageTarget.getClassType()) ? new ImageTarget(l, false) : (trackable.isOfType(CylinderTarget.getClassType()) ? new CylinderTarget(l, false) : (trackable.isOfType(getClassType()) ? new MultiTarget(l, false) : (trackable.isOfType(VuMarkTarget.getClassType()) ? new VuMarkTarget(l, false) : (trackable.isOfType(VuMarkTemplate.getClassType()) ? new VuMarkTemplate(l, false) : (trackable.isOfType(ModelTarget.getClassType()) ? new ModelTarget(l, false) : (trackable.isOfType(ObjectTarget.getClassType()) ? new ObjectTarget(l, false) : (trackable.isOfType(Anchor.getClassType()) ? new Anchor(l, false) : (trackable.isOfType(DeviceTrackable.getClassType()) ? new DeviceTrackable(l, false) : null)))))))));
  }
  
  public boolean getPartOffset(int paramInt, Matrix34F paramMatrix34F) {
    return VuforiaJNI.MultiTarget_getPartOffset(this.swigCPtr, this, paramInt, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
  }
  
  public boolean removePart(int paramInt) {
    return VuforiaJNI.MultiTarget_removePart(this.swigCPtr, this, paramInt);
  }
  
  public boolean setPartOffset(int paramInt, Matrix34F paramMatrix34F) {
    return VuforiaJNI.MultiTarget_setPartOffset(this.swigCPtr, this, paramInt, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\MultiTarget.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */