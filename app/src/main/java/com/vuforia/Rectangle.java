package com.vuforia;

public class Rectangle extends Area {
  private long swigCPtr;
  
  public Rectangle() {
    this(VuforiaJNI.new_Rectangle__SWIG_0(), true);
  }
  
  public Rectangle(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    this(VuforiaJNI.new_Rectangle__SWIG_2(paramFloat1, paramFloat2, paramFloat3, paramFloat4), true);
  }
  
  protected Rectangle(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.Rectangle_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  public Rectangle(Rectangle paramRectangle) {
    this(VuforiaJNI.new_Rectangle__SWIG_1(getCPtr(paramRectangle), paramRectangle), true);
  }
  
  protected static long getCPtr(Rectangle paramRectangle) {
    return (paramRectangle == null) ? 0L : paramRectangle.swigCPtr;
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
    //   27: invokestatic delete_Rectangle : (J)V
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
    boolean bool = paramObject instanceof Rectangle;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Rectangle)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public float getAreaSize() {
    return VuforiaJNI.Rectangle_getAreaSize(this.swigCPtr, this);
  }
  
  public float getHeight() {
    return VuforiaJNI.Rectangle_getHeight(this.swigCPtr, this);
  }
  
  public float getLeftTopX() {
    return VuforiaJNI.Rectangle_getLeftTopX(this.swigCPtr, this);
  }
  
  public float getLeftTopY() {
    return VuforiaJNI.Rectangle_getLeftTopY(this.swigCPtr, this);
  }
  
  public float getRightBottomX() {
    return VuforiaJNI.Rectangle_getRightBottomX(this.swigCPtr, this);
  }
  
  public float getRightBottomY() {
    return VuforiaJNI.Rectangle_getRightBottomY(this.swigCPtr, this);
  }
  
  public int getType() {
    return VuforiaJNI.Rectangle_getType(this.swigCPtr, this);
  }
  
  public float getWidth() {
    return VuforiaJNI.Rectangle_getWidth(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Rectangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */