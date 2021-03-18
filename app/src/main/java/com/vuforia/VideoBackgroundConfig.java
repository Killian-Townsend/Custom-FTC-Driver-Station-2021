package com.vuforia;

public class VideoBackgroundConfig {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public VideoBackgroundConfig() {
    this(VuforiaJNI.new_VideoBackgroundConfig(), true);
  }
  
  protected VideoBackgroundConfig(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(VideoBackgroundConfig paramVideoBackgroundConfig) {
    return (paramVideoBackgroundConfig == null) ? 0L : paramVideoBackgroundConfig.swigCPtr;
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
    //   27: invokestatic delete_VideoBackgroundConfig : (J)V
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
    boolean bool = paramObject instanceof VideoBackgroundConfig;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((VideoBackgroundConfig)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public boolean getEnabled() {
    return VuforiaJNI.VideoBackgroundConfig_Enabled_get(this.swigCPtr, this);
  }
  
  public Vec2I getPosition() {
    long l = VuforiaJNI.VideoBackgroundConfig_Position_get(this.swigCPtr, this);
    return (l == 0L) ? null : new Vec2I(l, false);
  }
  
  public int getReflection() {
    return VuforiaJNI.VideoBackgroundConfig_Reflection_get(this.swigCPtr, this);
  }
  
  public Vec2I getSize() {
    long l = VuforiaJNI.VideoBackgroundConfig_Size_get(this.swigCPtr, this);
    return (l == 0L) ? null : new Vec2I(l, false);
  }
  
  public void setEnabled(boolean paramBoolean) {
    VuforiaJNI.VideoBackgroundConfig_Enabled_set(this.swigCPtr, this, paramBoolean);
  }
  
  public void setPosition(Vec2I paramVec2I) {
    VuforiaJNI.VideoBackgroundConfig_Position_set(this.swigCPtr, this, Vec2I.getCPtr(paramVec2I), paramVec2I);
  }
  
  public void setReflection(int paramInt) {
    VuforiaJNI.VideoBackgroundConfig_Reflection_set(this.swigCPtr, this, paramInt);
  }
  
  public void setSize(Vec2I paramVec2I) {
    VuforiaJNI.VideoBackgroundConfig_Size_set(this.swigCPtr, this, Vec2I.getCPtr(paramVec2I), paramVec2I);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\VideoBackgroundConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */