package com.vuforia;

public class CustomViewerParameters extends ViewerParameters {
  private long swigCPtr;
  
  public CustomViewerParameters(float paramFloat, String paramString1, String paramString2) {
    this(VuforiaJNI.new_CustomViewerParameters__SWIG_0(paramFloat, paramString1, paramString2), true);
  }
  
  protected CustomViewerParameters(long paramLong, boolean paramBoolean) {
    super(VuforiaJNI.CustomViewerParameters_SWIGUpcast(paramLong), paramBoolean);
    this.swigCPtr = paramLong;
  }
  
  public CustomViewerParameters(CustomViewerParameters paramCustomViewerParameters) {
    this(VuforiaJNI.new_CustomViewerParameters__SWIG_1(getCPtr(paramCustomViewerParameters), paramCustomViewerParameters), true);
  }
  
  protected static long getCPtr(CustomViewerParameters paramCustomViewerParameters) {
    return (paramCustomViewerParameters == null) ? 0L : paramCustomViewerParameters.swigCPtr;
  }
  
  public void addDistortionCoefficient(float paramFloat) {
    VuforiaJNI.CustomViewerParameters_addDistortionCoefficient(this.swigCPtr, this, paramFloat);
  }
  
  public void clearDistortionCoefficients() {
    VuforiaJNI.CustomViewerParameters_clearDistortionCoefficients(this.swigCPtr, this);
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
    //   27: invokestatic delete_CustomViewerParameters : (J)V
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
    boolean bool = paramObject instanceof CustomViewerParameters;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((CustomViewerParameters)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public void setButtonType(int paramInt) {
    VuforiaJNI.CustomViewerParameters_setButtonType(this.swigCPtr, this, paramInt);
  }
  
  public void setContainsMagnet(boolean paramBoolean) {
    VuforiaJNI.CustomViewerParameters_setContainsMagnet(this.swigCPtr, this, paramBoolean);
  }
  
  public void setFieldOfView(Vec4F paramVec4F) {
    VuforiaJNI.CustomViewerParameters_setFieldOfView(this.swigCPtr, this, Vec4F.getCPtr(paramVec4F), paramVec4F);
  }
  
  public void setInterLensDistance(float paramFloat) {
    VuforiaJNI.CustomViewerParameters_setInterLensDistance(this.swigCPtr, this, paramFloat);
  }
  
  public void setLensCentreToTrayDistance(float paramFloat) {
    VuforiaJNI.CustomViewerParameters_setLensCentreToTrayDistance(this.swigCPtr, this, paramFloat);
  }
  
  public void setScreenToLensDistance(float paramFloat) {
    VuforiaJNI.CustomViewerParameters_setScreenToLensDistance(this.swigCPtr, this, paramFloat);
  }
  
  public void setTrayAlignment(int paramInt) {
    VuforiaJNI.CustomViewerParameters_setTrayAlignment(this.swigCPtr, this, paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\CustomViewerParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */