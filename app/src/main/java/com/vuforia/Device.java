package com.vuforia;

public class Device {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected Device(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(Device paramDevice) {
    return (paramDevice == null) ? 0L : paramDevice.swigCPtr;
  }
  
  public static Type getClassType() {
    return new Type(VuforiaJNI.Device_getClassType(), true);
  }
  
  public static Device getInstance() {
    if (Vuforia.wasInitializedJava()) {
      long l = VuforiaJNI.Device_getInstance();
      return VuforiaJNI.Device_isOfType(l, null, Type.getCPtr(EyewearDevice.getClassType()), EyewearDevice.getClassType()) ? new EyewearDevice(l, false) : new Device(l, false);
    } 
    throw new RuntimeException("Use of the Java Vuforia APIs requires initalization via the com.vuforia.Vuforia class");
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
    //   27: invokestatic delete_Device : (J)V
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
    boolean bool = paramObject instanceof Device;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Device)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public int getMode() {
    return VuforiaJNI.Device_getMode(this.swigCPtr, this);
  }
  
  public RenderingPrimitives getRenderingPrimitives() {
    return new RenderingPrimitives(VuforiaJNI.Device_getRenderingPrimitives(this.swigCPtr, this), true);
  }
  
  public ViewerParameters getSelectedViewer() {
    return new ViewerParameters(VuforiaJNI.Device_getSelectedViewer(this.swigCPtr, this), true);
  }
  
  public Type getType() {
    return new Type(VuforiaJNI.Device_getType(this.swigCPtr, this), true);
  }
  
  public ViewerParametersList getViewerList() {
    return new ViewerParametersList(VuforiaJNI.Device_getViewerList(this.swigCPtr, this), false);
  }
  
  public boolean isOfType(Type paramType) {
    return VuforiaJNI.Device_isOfType(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
  }
  
  public boolean isViewerActive() {
    return VuforiaJNI.Device_isViewerActive(this.swigCPtr, this);
  }
  
  public boolean selectViewer(ViewerParameters paramViewerParameters) {
    return VuforiaJNI.Device_selectViewer(this.swigCPtr, this, ViewerParameters.getCPtr(paramViewerParameters), paramViewerParameters);
  }
  
  public void setConfigurationChanged() {
    VuforiaJNI.Device_setConfigurationChanged(this.swigCPtr, this);
  }
  
  public boolean setMode(int paramInt) {
    return VuforiaJNI.Device_setMode(this.swigCPtr, this, paramInt);
  }
  
  public void setViewerActive(boolean paramBoolean) {
    VuforiaJNI.Device_setViewerActive(this.swigCPtr, this, paramBoolean);
  }
  
  public static final class MODE {
    public static final int MODE_AR = 0;
    
    public static final int MODE_VR = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Device.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */