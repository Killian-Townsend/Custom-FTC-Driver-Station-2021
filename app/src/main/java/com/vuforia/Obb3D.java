package com.vuforia;

public class Obb3D {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public Obb3D() {
    this(VuforiaJNI.new_Obb3D__SWIG_0(), true);
  }
  
  protected Obb3D(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public Obb3D(Obb3D paramObb3D) {
    this(VuforiaJNI.new_Obb3D__SWIG_1(getCPtr(paramObb3D), paramObb3D), true);
  }
  
  public Obb3D(Vec3F paramVec3F1, Vec3F paramVec3F2, float paramFloat) {
    this(VuforiaJNI.new_Obb3D__SWIG_2(Vec3F.getCPtr(paramVec3F1), paramVec3F1, Vec3F.getCPtr(paramVec3F2), paramVec3F2, paramFloat), true);
  }
  
  protected static long getCPtr(Obb3D paramObb3D) {
    return (paramObb3D == null) ? 0L : paramObb3D.swigCPtr;
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
    //   27: invokestatic delete_Obb3D : (J)V
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
    boolean bool = paramObject instanceof Obb3D;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Obb3D)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public Vec3F getCenter() {
    return new Vec3F(VuforiaJNI.Obb3D_getCenter(this.swigCPtr, this), false);
  }
  
  public Vec3F getHalfExtents() {
    return new Vec3F(VuforiaJNI.Obb3D_getHalfExtents(this.swigCPtr, this), false);
  }
  
  public float getRotationZ() {
    return VuforiaJNI.Obb3D_getRotationZ(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Obb3D.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */