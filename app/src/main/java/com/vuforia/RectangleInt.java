package com.vuforia;

public class RectangleInt extends Area {
  private long swigCPtr;
  
  public RectangleInt() {
    this(VuforiaJNI.new_RectangleInt__SWIG_0(), true);
  }
  
  public RectangleInt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this(VuforiaJNI.new_RectangleInt__SWIG_2(paramInt1, paramInt2, paramInt3, paramInt4), true);
  }
  
  protected RectangleInt(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.RectangleInt_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  public RectangleInt(RectangleInt paramRectangleInt) {
    this(VuforiaJNI.new_RectangleInt__SWIG_1(getCPtr(paramRectangleInt), paramRectangleInt), true);
  }
  
  protected static long getCPtr(RectangleInt paramRectangleInt) {
    return (paramRectangleInt == null) ? 0L : paramRectangleInt.swigCPtr;
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
    //   27: invokestatic delete_RectangleInt : (J)V
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
    boolean bool = paramObject instanceof RectangleInt;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((RectangleInt)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getAreaSize() {
    return VuforiaJNI.RectangleInt_getAreaSize(this.swigCPtr, this);
  }
  
  public int getHeight() {
    return VuforiaJNI.RectangleInt_getHeight(this.swigCPtr, this);
  }
  
  public int getLeftTopX() {
    return VuforiaJNI.RectangleInt_getLeftTopX(this.swigCPtr, this);
  }
  
  public int getLeftTopY() {
    return VuforiaJNI.RectangleInt_getLeftTopY(this.swigCPtr, this);
  }
  
  public int getRightBottomX() {
    return VuforiaJNI.RectangleInt_getRightBottomX(this.swigCPtr, this);
  }
  
  public int getRightBottomY() {
    return VuforiaJNI.RectangleInt_getRightBottomY(this.swigCPtr, this);
  }
  
  public int getType() {
    return VuforiaJNI.RectangleInt_getType(this.swigCPtr, this);
  }
  
  public int getWidth() {
    return VuforiaJNI.RectangleInt_getWidth(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\RectangleInt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */