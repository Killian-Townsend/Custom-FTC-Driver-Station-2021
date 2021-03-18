package com.vuforia;

public class RenderingPrimitives {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected RenderingPrimitives(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public RenderingPrimitives(RenderingPrimitives paramRenderingPrimitives) {
    this(VuforiaJNI.new_RenderingPrimitives(getCPtr(paramRenderingPrimitives), paramRenderingPrimitives), true);
  }
  
  protected static long getCPtr(RenderingPrimitives paramRenderingPrimitives) {
    return (paramRenderingPrimitives == null) ? 0L : paramRenderingPrimitives.swigCPtr;
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
    //   27: invokestatic delete_RenderingPrimitives : (J)V
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
    boolean bool = paramObject instanceof RenderingPrimitives;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((RenderingPrimitives)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public Mesh getDistortionTextureMesh(int paramInt) {
    return new Mesh(VuforiaJNI.RenderingPrimitives_getDistortionTextureMesh(this.swigCPtr, this, paramInt), false);
  }
  
  public Vec2I getDistortionTextureSize(int paramInt) {
    return new Vec2I(VuforiaJNI.RenderingPrimitives_getDistortionTextureSize(this.swigCPtr, this, paramInt), true);
  }
  
  public Vec4I getDistortionTextureViewport(int paramInt) {
    return new Vec4I(VuforiaJNI.RenderingPrimitives_getDistortionTextureViewport(this.swigCPtr, this, paramInt), true);
  }
  
  public Vec4F getEffectiveFov(int paramInt) {
    return new Vec4F(VuforiaJNI.RenderingPrimitives_getEffectiveFov(this.swigCPtr, this, paramInt), true);
  }
  
  public Matrix34F getEyeDisplayAdjustmentMatrix(int paramInt) {
    return new Matrix34F(VuforiaJNI.RenderingPrimitives_getEyeDisplayAdjustmentMatrix(this.swigCPtr, this, paramInt), true);
  }
  
  public Vec4F getNormalizedViewport(int paramInt) {
    return new Vec4F(VuforiaJNI.RenderingPrimitives_getNormalizedViewport(this.swigCPtr, this, paramInt), true);
  }
  
  public Matrix34F getProjectionMatrix(int paramInt1, int paramInt2, CameraCalibration paramCameraCalibration) {
    return new Matrix34F(VuforiaJNI.RenderingPrimitives_getProjectionMatrix__SWIG_1(this.swigCPtr, this, paramInt1, paramInt2, CameraCalibration.getCPtr(paramCameraCalibration), paramCameraCalibration), true);
  }
  
  public Matrix34F getProjectionMatrix(int paramInt1, int paramInt2, CameraCalibration paramCameraCalibration, boolean paramBoolean) {
    return new Matrix34F(VuforiaJNI.RenderingPrimitives_getProjectionMatrix__SWIG_0(this.swigCPtr, this, paramInt1, paramInt2, CameraCalibration.getCPtr(paramCameraCalibration), paramCameraCalibration, paramBoolean), true);
  }
  
  public ViewList getRenderingViews() {
    return new ViewList(VuforiaJNI.RenderingPrimitives_getRenderingViews(this.swigCPtr, this), false);
  }
  
  public Mesh getVideoBackgroundMesh(int paramInt) {
    return new Mesh(VuforiaJNI.RenderingPrimitives_getVideoBackgroundMesh(this.swigCPtr, this, paramInt), false);
  }
  
  public Matrix34F getVideoBackgroundProjectionMatrix(int paramInt1, int paramInt2) {
    return new Matrix34F(VuforiaJNI.RenderingPrimitives_getVideoBackgroundProjectionMatrix__SWIG_1(this.swigCPtr, this, paramInt1, paramInt2), true);
  }
  
  public Matrix34F getVideoBackgroundProjectionMatrix(int paramInt1, int paramInt2, boolean paramBoolean) {
    return new Matrix34F(VuforiaJNI.RenderingPrimitives_getVideoBackgroundProjectionMatrix__SWIG_0(this.swigCPtr, this, paramInt1, paramInt2, paramBoolean), true);
  }
  
  public Vec2I getVideoBackgroundTextureSize() {
    return new Vec2I(VuforiaJNI.RenderingPrimitives_getVideoBackgroundTextureSize(this.swigCPtr, this), true);
  }
  
  public Vec4I getViewport(int paramInt) {
    return new Vec4I(VuforiaJNI.RenderingPrimitives_getViewport(this.swigCPtr, this, paramInt), true);
  }
  
  public Vec2F getViewportCentreToEyeAxis(int paramInt1, int paramInt2) {
    return new Vec2F(VuforiaJNI.RenderingPrimitives_getViewportCentreToEyeAxis(this.swigCPtr, this, paramInt1, paramInt2), true);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\RenderingPrimitives.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */