package com.vuforia;

public class Vec2F {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public Vec2F() {
    this(VuforiaJNI.new_Vec2F__SWIG_0(), true);
  }
  
  public Vec2F(float paramFloat1, float paramFloat2) {
    this(VuforiaJNI.new_Vec2F__SWIG_2(paramFloat1, paramFloat2), true);
  }
  
  protected Vec2F(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Vec2F(Vec2F paramVec2F) {
    this(VuforiaJNI.new_Vec2F__SWIG_3(getCPtr(paramVec2F), paramVec2F), true);
  }
  
  public Vec2F(float[] paramArrayOffloat) {
    this(VuforiaJNI.new_Vec2F__SWIG_1(paramArrayOffloat), true);
  }
  
  protected static long getCPtr(Vec2F paramVec2F) {
    return (paramVec2F == null) ? 0L : paramVec2F.swigCPtr;
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
    //   27: invokestatic delete_Vec2F : (J)V
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
    boolean bool = paramObject instanceof Vec2F;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Vec2F)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public float[] getData() {
    return VuforiaJNI.Vec2F_data_get(this.swigCPtr, this);
  }
  
  public void setData(float[] paramArrayOffloat) {
    VuforiaJNI.Vec2F_data_set(this.swigCPtr, this, paramArrayOffloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Vec2F.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */