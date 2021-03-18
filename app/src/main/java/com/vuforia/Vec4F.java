package com.vuforia;

public class Vec4F {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public Vec4F() {
    this(VuforiaJNI.new_Vec4F__SWIG_0(), true);
  }
  
  public Vec4F(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this(VuforiaJNI.new_Vec4F__SWIG_2(paramFloat1, paramFloat2, paramFloat3, paramFloat4), true);
  }
  
  protected Vec4F(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Vec4F(Vec4F paramVec4F) {
    this(VuforiaJNI.new_Vec4F__SWIG_3(getCPtr(paramVec4F), paramVec4F), true);
  }
  
  public Vec4F(float[] paramArrayOffloat) {
    this(VuforiaJNI.new_Vec4F__SWIG_1(paramArrayOffloat), true);
  }
  
  protected static long getCPtr(Vec4F paramVec4F) {
    return (paramVec4F == null) ? 0L : paramVec4F.swigCPtr;
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
    //   27: invokestatic delete_Vec4F : (J)V
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
    boolean bool = paramObject instanceof Vec4F;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Vec4F)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public float[] getData() {
    return VuforiaJNI.Vec4F_data_get(this.swigCPtr, this);
  }
  
  public void setData(float[] paramArrayOffloat) {
    VuforiaJNI.Vec4F_data_set(this.swigCPtr, this, paramArrayOffloat);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Vec4F.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */