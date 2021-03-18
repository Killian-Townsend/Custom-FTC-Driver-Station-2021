package com.vuforia;

public class Obb2D {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public Obb2D() {
    this(VuforiaJNI.new_Obb2D__SWIG_0(), true);
  }
  
  protected Obb2D(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Obb2D(Obb2D paramObb2D) {
    this(VuforiaJNI.new_Obb2D__SWIG_1(getCPtr(paramObb2D), paramObb2D), true);
  }
  
  public Obb2D(Vec2F paramVec2F1, Vec2F paramVec2F2, float paramFloat) {
    this(VuforiaJNI.new_Obb2D__SWIG_2(Vec2F.getCPtr(paramVec2F1), paramVec2F1, Vec2F.getCPtr(paramVec2F2), paramVec2F2, paramFloat), true);
  }
  
  protected static long getCPtr(Obb2D paramObb2D) {
    return (paramObb2D == null) ? 0L : paramObb2D.swigCPtr;
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
    //   27: invokestatic delete_Obb2D : (J)V
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
    boolean bool = paramObject instanceof Obb2D;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Obb2D)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public Vec2F getCenter() {
    return new Vec2F(VuforiaJNI.Obb2D_getCenter(this.swigCPtr, this), false);
  }
  
  public Vec2F getHalfExtents() {
    return new Vec2F(VuforiaJNI.Obb2D_getHalfExtents(this.swigCPtr, this), false);
  }
  
  public float getRotation() {
    return VuforiaJNI.Obb2D_getRotation(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Obb2D.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */