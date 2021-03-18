package com.vuforia;

import java.nio.ByteBuffer;

public class Image {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected Image(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Image paramImage) {
    return (paramImage == null) ? 0L : paramImage.swigCPtr;
  }
  
  protected void delete() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield swigCPtr : J
    //   6: lconst_0
    //   7: lcmp
    //   8: ifeq -> 41
    //   11: aload_0
    //   12: getfield swigCMemOwn : Z
    //   15: ifne -> 26
    //   18: aload_0
    //   19: lconst_0
    //   20: putfield swigCPtr : J
    //   23: goto -> 41
    //   26: aload_0
    //   27: iconst_0
    //   28: putfield swigCMemOwn : Z
    //   31: new java/lang/UnsupportedOperationException
    //   34: dup
    //   35: ldc 'C++ destructor does not have public access'
    //   37: invokespecial <init> : (Ljava/lang/String;)V
    //   40: athrow
    //   41: aload_0
    //   42: monitorexit
    //   43: return
    //   44: astore_1
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_1
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	44	finally
    //   26	41	44	finally
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Image;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Image)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public int getBufferHeight() {
    return VuforiaJNI.Image_getBufferHeight(this.swigCPtr, this);
  }
  
  public int getBufferWidth() {
    return VuforiaJNI.Image_getBufferWidth(this.swigCPtr, this);
  }
  
  public int getFormat() {
    return VuforiaJNI.Image_getFormat(this.swigCPtr, this);
  }
  
  public int getHeight() {
    return VuforiaJNI.Image_getHeight(this.swigCPtr, this);
  }
  
  public ByteBuffer getPixels() {
    return VuforiaJNI.Image_getPixels(this.swigCPtr, this);
  }
  
  public int getStride() {
    return VuforiaJNI.Image_getStride(this.swigCPtr, this);
  }
  
  public int getWidth() {
    return VuforiaJNI.Image_getWidth(this.swigCPtr, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Image.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */