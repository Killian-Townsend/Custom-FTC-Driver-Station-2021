package com.vuforia;

public class Renderer {
  public static final int TARGET_FPS_CONTINUOUS = -1;
  
  private static State sState;
  
  private static final Object sStateMutex = new Object();
  
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected Renderer(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Renderer paramRenderer) {
    return (paramRenderer == null) ? 0L : paramRenderer.swigCPtr;
  }
  
  public static Renderer getInstance() {
    if (Vuforia.wasInitializedJava())
      return new Renderer(VuforiaJNI.Renderer_getInstance(), false); 
    throw new RuntimeException("Use of the Java Vuforia APIs requires initalization via the com.vuforia.Vuforia class");
  }
  
  public State begin() {
    synchronized (sStateMutex) {
      if (sState != null)
        sState.delete(); 
      State state = new State(VuforiaJNI.Renderer_begin__SWIG_0(this.swigCPtr, this), true);
      sState = state;
      return state;
    } 
  }
  
  public void begin(State paramState) {
    VuforiaJNI.Renderer_begin__SWIG_1(this.swigCPtr, this, State.getCPtr(paramState), paramState);
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
    //   27: invokestatic delete_Renderer : (J)V
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
  
  public void end() {
    VuforiaJNI.Renderer_end(this.swigCPtr, this);
    synchronized (sStateMutex) {
      if (sState != null) {
        sState.delete();
        sState = null;
      } 
      return;
    } 
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof Renderer;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Renderer)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getRecommendedFps() {
    return VuforiaJNI.Renderer_getRecommendedFps__SWIG_1(this.swigCPtr, this);
  }
  
  public int getRecommendedFps(int paramInt) {
    return VuforiaJNI.Renderer_getRecommendedFps__SWIG_0(this.swigCPtr, this, paramInt);
  }
  
  public VideoBackgroundConfig getVideoBackgroundConfig() {
    return new VideoBackgroundConfig(VuforiaJNI.Renderer_getVideoBackgroundConfig(this.swigCPtr, this), false);
  }
  
  public VideoBackgroundTextureInfo getVideoBackgroundTextureInfo() {
    return new VideoBackgroundTextureInfo(VuforiaJNI.Renderer_getVideoBackgroundTextureInfo(this.swigCPtr, this), false);
  }
  
  public boolean setTargetFps(int paramInt) {
    return VuforiaJNI.Renderer_setTargetFps(this.swigCPtr, this, paramInt);
  }
  
  public void setVideoBackgroundConfig(VideoBackgroundConfig paramVideoBackgroundConfig) {
    VuforiaJNI.Renderer_setVideoBackgroundConfig(this.swigCPtr, this, VideoBackgroundConfig.getCPtr(paramVideoBackgroundConfig), paramVideoBackgroundConfig);
  }
  
  public boolean setVideoBackgroundTexture(TextureData paramTextureData) {
    return VuforiaJNI.Renderer_setVideoBackgroundTexture(this.swigCPtr, this, TextureData.getCPtr(paramTextureData), paramTextureData);
  }
  
  public boolean updateVideoBackgroundTexture() {
    return VuforiaJNI.Renderer_updateVideoBackgroundTexture__SWIG_1(this.swigCPtr, this);
  }
  
  public boolean updateVideoBackgroundTexture(TextureUnit paramTextureUnit) {
    return VuforiaJNI.Renderer_updateVideoBackgroundTexture__SWIG_0(this.swigCPtr, this, TextureUnit.getCPtr(paramTextureUnit), paramTextureUnit);
  }
  
  public static final class FPSHINT_FLAGS {
    public static final int FPSHINT_DEFAULT_FLAGS = 0;
    
    public static final int FPSHINT_FAST = 4;
    
    public static final int FPSHINT_NONE = 0;
    
    public static final int FPSHINT_NO_VIDEOBACKGROUND = 1;
    
    public static final int FPSHINT_POWER_EFFICIENCY = 2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Renderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */