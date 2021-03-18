package com.vuforia;

public class Trackable {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected Trackable(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Trackable paramTrackable) {
    return (paramTrackable == null) ? 0L : paramTrackable.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.Trackable_getClassType(), true);
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
    //   27: invokestatic delete_Trackable : (J)V
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
    boolean bool = paramObject instanceof Trackable;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Trackable)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getId() {
    return VuforiaJNI.Trackable_getId(this.swigCPtr, this);
  }
  
  public String getName() {
    return VuforiaJNI.Trackable_getName(this.swigCPtr, this);
  }
  
  public Type getType() {
    return new Type(VuforiaJNI.Trackable_getType(this.swigCPtr, this), true);
  }
  
  public Object getUserData() {
    return Vuforia.retreiveFromUserDataMap(Integer.valueOf(getId()));
  }
  
  public boolean isExtendedTrackingStarted() {
    return VuforiaJNI.Trackable_isExtendedTrackingStarted(this.swigCPtr, this);
  }
  
  public boolean isOfType(Type paramType) {
    return VuforiaJNI.Trackable_isOfType(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
  }
  
  public boolean setUserData(Object paramObject) {
    return Vuforia.updateUserDataMap(Integer.valueOf(getId()), paramObject);
  }
  
  public boolean startExtendedTracking() {
    return VuforiaJNI.Trackable_startExtendedTracking(this.swigCPtr, this);
  }
  
  public boolean stopExtendedTracking() {
    return VuforiaJNI.Trackable_stopExtendedTracking(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Trackable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */