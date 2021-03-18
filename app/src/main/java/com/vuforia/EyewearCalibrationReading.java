package com.vuforia;

public class EyewearCalibrationReading {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public EyewearCalibrationReading() {
    this(VuforiaJNI.new_EyewearCalibrationReading(), true);
  }
  
  protected EyewearCalibrationReading(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long[] cArrayUnwrap(EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading) {
    long[] arrayOfLong = new long[paramArrayOfEyewearCalibrationReading.length];
    for (int i = 0; i < paramArrayOfEyewearCalibrationReading.length; i++)
      arrayOfLong[i] = getCPtr(paramArrayOfEyewearCalibrationReading[i]); 
    return arrayOfLong;
  }
  
  protected static EyewearCalibrationReading[] cArrayWrap(long[] paramArrayOflong, boolean paramBoolean) {
    EyewearCalibrationReading[] arrayOfEyewearCalibrationReading = new EyewearCalibrationReading[paramArrayOflong.length];
    for (int i = 0; i < paramArrayOflong.length; i++)
      arrayOfEyewearCalibrationReading[i] = new EyewearCalibrationReading(paramArrayOflong[i], paramBoolean); 
    return arrayOfEyewearCalibrationReading;
  }
  
  protected static long getCPtr(EyewearCalibrationReading paramEyewearCalibrationReading) {
    return (paramEyewearCalibrationReading == null) ? 0L : paramEyewearCalibrationReading.swigCPtr;
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
    //   27: invokestatic delete_EyewearCalibrationReading : (J)V
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
  
  protected void finalize() {
    delete();
  }
  
  public float getCenterX() {
    return VuforiaJNI.EyewearCalibrationReading_CenterX_get(this.swigCPtr, this);
  }
  
  public float getCenterY() {
    return VuforiaJNI.EyewearCalibrationReading_CenterY_get(this.swigCPtr, this);
  }
  
  public Matrix34F getPose() {
    long l = VuforiaJNI.EyewearCalibrationReading_Pose_get(this.swigCPtr, this);
    return (l == 0L) ? null : new Matrix34F(l, false);
  }
  
  public float getScale() {
    return VuforiaJNI.EyewearCalibrationReading_Scale_get(this.swigCPtr, this);
  }
  
  public int getType() {
    return VuforiaJNI.EyewearCalibrationReading_Type_get(this.swigCPtr, this);
  }
  
  public void setCenterX(float paramFloat) {
    VuforiaJNI.EyewearCalibrationReading_CenterX_set(this.swigCPtr, this, paramFloat);
  }
  
  public void setCenterY(float paramFloat) {
    VuforiaJNI.EyewearCalibrationReading_CenterY_set(this.swigCPtr, this, paramFloat);
  }
  
  public void setPose(Matrix34F paramMatrix34F) {
    VuforiaJNI.EyewearCalibrationReading_Pose_set(this.swigCPtr, this, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F);
  }
  
  public void setScale(float paramFloat) {
    VuforiaJNI.EyewearCalibrationReading_Scale_set(this.swigCPtr, this, paramFloat);
  }
  
  public void setType(int paramInt) {
    VuforiaJNI.EyewearCalibrationReading_Type_set(this.swigCPtr, this, paramInt);
  }
  
  public static final class AlignmentType {
    public static final int HORIZONTAL_LINE = 1;
    
    public static final int RECTANGLE = 0;
    
    public static final int VERTICAL_LINE = 2;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\EyewearCalibrationReading.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */