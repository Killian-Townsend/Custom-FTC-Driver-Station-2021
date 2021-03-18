package com.vuforia;

public class ViewerParameters {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected ViewerParameters(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public ViewerParameters(ViewerParameters paramViewerParameters) {
    this(VuforiaJNI.new_ViewerParameters(getCPtr(paramViewerParameters), paramViewerParameters), true);
  }
  
  protected static long getCPtr(ViewerParameters paramViewerParameters) {
    return (paramViewerParameters == null) ? 0L : paramViewerParameters.swigCPtr;
  }
  
  public boolean containsMagnet() {
    return VuforiaJNI.ViewerParameters_containsMagnet(this.swigCPtr, this);
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
    //   27: invokestatic delete_ViewerParameters : (J)V
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
    boolean bool = paramObject instanceof ViewerParameters;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((ViewerParameters)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getButtonType() {
    return VuforiaJNI.ViewerParameters_getButtonType(this.swigCPtr, this);
  }
  
  public float getDistortionCoefficient(int paramInt) {
    return VuforiaJNI.ViewerParameters_getDistortionCoefficient(this.swigCPtr, this, paramInt);
  }
  
  public Vec4F getFieldOfView() {
    return new Vec4F(VuforiaJNI.ViewerParameters_getFieldOfView(this.swigCPtr, this), true);
  }
  
  public float getInterLensDistance() {
    return VuforiaJNI.ViewerParameters_getInterLensDistance(this.swigCPtr, this);
  }
  
  public float getLensCentreToTrayDistance() {
    return VuforiaJNI.ViewerParameters_getLensCentreToTrayDistance(this.swigCPtr, this);
  }
  
  public String getManufacturer() {
    return VuforiaJNI.ViewerParameters_getManufacturer(this.swigCPtr, this);
  }
  
  public String getName() {
    return VuforiaJNI.ViewerParameters_getName(this.swigCPtr, this);
  }
  
  public long getNumDistortionCoefficients() {
    return VuforiaJNI.ViewerParameters_getNumDistortionCoefficients(this.swigCPtr, this);
  }
  
  public float getScreenToLensDistance() {
    return VuforiaJNI.ViewerParameters_getScreenToLensDistance(this.swigCPtr, this);
  }
  
  public int getTrayAlignment() {
    return VuforiaJNI.ViewerParameters_getTrayAlignment(this.swigCPtr, this);
  }
  
  public float getVersion() {
    return VuforiaJNI.ViewerParameters_getVersion(this.swigCPtr, this);
  }
  
  public static final class BUTTON_TYPE {
    public static final int BUTTON_TYPE_BUTTON_TOUCH = 3;
    
    public static final int BUTTON_TYPE_FINGER_TOUCH = 2;
    
    public static final int BUTTON_TYPE_MAGNET = 1;
    
    public static final int BUTTON_TYPE_NONE = 0;
  }
  
  public static final class TRAY_ALIGNMENT {
    public static final int TRAY_ALIGN_BOTTOM = 0;
    
    public static final int TRAY_ALIGN_CENTRE = 1;
    
    public static final int TRAY_ALIGN_TOP = 2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\ViewerParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */