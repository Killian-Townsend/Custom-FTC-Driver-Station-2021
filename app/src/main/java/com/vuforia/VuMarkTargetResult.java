package com.vuforia;

public class VuMarkTargetResult extends ObjectTargetResult {
  private long swigCPtr;
  
  protected VuMarkTargetResult(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.VuMarkTargetResult_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(VuMarkTargetResult paramVuMarkTargetResult) {
    return (paramVuMarkTargetResult == null) ? 0L : paramVuMarkTargetResult.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.VuMarkTargetResult_getClassType(), true);
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
    //   27: invokestatic delete_VuMarkTargetResult : (J)V
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
    boolean bool = paramObject instanceof VuMarkTargetResult;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((VuMarkTargetResult)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getId() {
    return VuforiaJNI.VuMarkTargetResult_getId(this.swigCPtr, this);
  }
  
  public Trackable getTrackable() {
    return new VuMarkTarget(VuforiaJNI.VuMarkTargetResult_getTrackable(this.swigCPtr, this), false);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\VuMarkTargetResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */