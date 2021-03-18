package com.vuforia;

public class ImageTargetBuilder {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected ImageTargetBuilder(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(ImageTargetBuilder paramImageTargetBuilder) {
    return (paramImageTargetBuilder == null) ? 0L : paramImageTargetBuilder.swigCPtr;
  }
  
  public boolean build(String paramString, float paramFloat) {
    return VuforiaJNI.ImageTargetBuilder_build(this.swigCPtr, this, paramString, paramFloat);
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
    boolean bool = paramObject instanceof ImageTargetBuilder;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((ImageTargetBuilder)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public int getFrameQuality() {
    return VuforiaJNI.ImageTargetBuilder_getFrameQuality(this.swigCPtr, this);
  }
  
  public TrackableSource getTrackableSource() {
    long l = VuforiaJNI.ImageTargetBuilder_getTrackableSource(this.swigCPtr, this);
    return (l == 0L) ? null : new TrackableSource(l, false);
  }
  
  public void startScan() {
    VuforiaJNI.ImageTargetBuilder_startScan(this.swigCPtr, this);
  }
  
  public void stopScan() {
    VuforiaJNI.ImageTargetBuilder_stopScan(this.swigCPtr, this);
  }
  
  public static final class FRAME_QUALITY {
    public static final int FRAME_QUALITY_HIGH = 2;
    
    public static final int FRAME_QUALITY_LOW = 0;
    
    public static final int FRAME_QUALITY_MEDIUM = 1;
    
    public static final int FRAME_QUALITY_NONE = -1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ImageTargetBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */