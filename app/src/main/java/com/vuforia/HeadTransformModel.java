package com.vuforia;

public class HeadTransformModel extends TransformModel {
  private long swigCPtr;
  
  public HeadTransformModel() {
    this(VuforiaJNI.new_HeadTransformModel__SWIG_0(), true);
  }
  
  protected HeadTransformModel(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.HeadTransformModel_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  public HeadTransformModel(HeadTransformModel paramHeadTransformModel) {
    this(VuforiaJNI.new_HeadTransformModel__SWIG_1(getCPtr(paramHeadTransformModel), paramHeadTransformModel), true);
  }
  
  public HeadTransformModel(Vec3F paramVec3F) {
    this(VuforiaJNI.new_HeadTransformModel__SWIG_2(Vec3F.getCPtr(paramVec3F), paramVec3F), true);
  }
  
  protected static long getCPtr(HeadTransformModel paramHeadTransformModel) {
    return (paramHeadTransformModel == null) ? 0L : paramHeadTransformModel.swigCPtr;
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
    //   27: invokestatic delete_HeadTransformModel : (J)V
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
    boolean bool = paramObject instanceof HeadTransformModel;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((HeadTransformModel)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public Vec3F getPivotPoint() {
    return new Vec3F(VuforiaJNI.HeadTransformModel_getPivotPoint(this.swigCPtr, this), false);
  }
  
  public int getType() {
    return VuforiaJNI.HeadTransformModel_getType(this.swigCPtr, this);
  }
  
  public boolean setPivotPoint(Vec3F paramVec3F) {
    return VuforiaJNI.HeadTransformModel_setPivotPoint(this.swigCPtr, this, Vec3F.getCPtr(paramVec3F), paramVec3F);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\HeadTransformModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */