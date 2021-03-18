package com.vuforia;

public class VirtualButton {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected VirtualButton(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(VirtualButton paramVirtualButton) {
    return (paramVirtualButton == null) ? 0L : paramVirtualButton.swigCPtr;
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
    boolean bool = paramObject instanceof VirtualButton;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((VirtualButton)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public Area getArea() {
    long l = VuforiaJNI.VirtualButton_getArea(this.swigCPtr, this);
    if (l == 0L)
      return null; 
    int i = (new Area(l, false)).getType();
    return (Area)((i != 0) ? ((i != 1) ? null : new RectangleInt(l, false)) : new Rectangle(l, false));
  }
  
  public int getID() {
    return VuforiaJNI.VirtualButton_getID(this.swigCPtr, this);
  }
  
  public String getName() {
    return VuforiaJNI.VirtualButton_getName(this.swigCPtr, this);
  }
  
  public boolean isEnabled() {
    return VuforiaJNI.VirtualButton_isEnabled(this.swigCPtr, this);
  }
  
  public boolean setArea(Area paramArea) {
    return VuforiaJNI.VirtualButton_setArea(this.swigCPtr, this, Area.getCPtr(paramArea), paramArea);
  }
  
  public boolean setEnabled(boolean paramBoolean) {
    return VuforiaJNI.VirtualButton_setEnabled(this.swigCPtr, this, paramBoolean);
  }
  
  public boolean setSensitivity(int paramInt) {
    return VuforiaJNI.VirtualButton_setSensitivity(this.swigCPtr, this, paramInt);
  }
  
  public static final class SENSITIVITY {
    public static final int HIGH = 0;
    
    public static final int LOW = 2;
    
    public static final int MEDIUM = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\VirtualButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */