package com.vuforia;

public class EyewearUserCalibrator {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected EyewearUserCalibrator(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(EyewearUserCalibrator paramEyewearUserCalibrator) {
    return (paramEyewearUserCalibrator == null) ? 0L : paramEyewearUserCalibrator.swigCPtr;
  }
  
  private int getProjectionMatrices(EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading1, int paramInt1, EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading2, int paramInt2, Matrix34F paramMatrix34F1, Matrix34F paramMatrix34F2, Matrix34F paramMatrix34F3, Matrix34F paramMatrix34F4) {
    return VuforiaJNI.EyewearUserCalibrator_getProjectionMatrices(this.swigCPtr, this, EyewearCalibrationReading.cArrayUnwrap(paramArrayOfEyewearCalibrationReading1), paramInt1, EyewearCalibrationReading.cArrayUnwrap(paramArrayOfEyewearCalibrationReading2), paramInt2, Matrix34F.getCPtr(paramMatrix34F1), paramMatrix34F1, Matrix34F.getCPtr(paramMatrix34F2), paramMatrix34F2, Matrix34F.getCPtr(paramMatrix34F3), paramMatrix34F3, Matrix34F.getCPtr(paramMatrix34F4), paramMatrix34F4);
  }
  
  private boolean getProjectionMatrix(EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading, int paramInt, Matrix34F paramMatrix34F1, Matrix34F paramMatrix34F2) {
    return VuforiaJNI.EyewearUserCalibrator_getProjectionMatrix(this.swigCPtr, this, EyewearCalibrationReading.cArrayUnwrap(paramArrayOfEyewearCalibrationReading), paramInt, Matrix34F.getCPtr(paramMatrix34F1), paramMatrix34F1, Matrix34F.getCPtr(paramMatrix34F2), paramMatrix34F2);
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
    //   27: invokestatic delete_EyewearUserCalibrator : (J)V
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
  
  public float getDrawingAspectRatio(long paramLong1, long paramLong2) {
    return VuforiaJNI.EyewearUserCalibrator_getDrawingAspectRatio(this.swigCPtr, this, paramLong1, paramLong2);
  }
  
  public float getMaxScaleHint() {
    return VuforiaJNI.EyewearUserCalibrator_getMaxScaleHint(this.swigCPtr, this);
  }
  
  public float getMinScaleHint() {
    return VuforiaJNI.EyewearUserCalibrator_getMinScaleHint(this.swigCPtr, this);
  }
  
  public int getProjectionMatrices(EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading1, EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading2, Matrix34F paramMatrix34F1, Matrix34F paramMatrix34F2, Matrix34F paramMatrix34F3, Matrix34F paramMatrix34F4) {
    if (paramMatrix34F1 != null && paramMatrix34F2 != null && paramMatrix34F3 != null && paramMatrix34F4 != null)
      return getProjectionMatrices(paramArrayOfEyewearCalibrationReading1, paramArrayOfEyewearCalibrationReading1.length, paramArrayOfEyewearCalibrationReading2, paramArrayOfEyewearCalibrationReading2.length, paramMatrix34F1, paramMatrix34F2, paramMatrix34F3, paramMatrix34F4); 
    throw new NullPointerException("Matrix34F argument is null");
  }
  
  public boolean getProjectionMatrix(EyewearCalibrationReading[] paramArrayOfEyewearCalibrationReading, Matrix34F paramMatrix34F1, Matrix34F paramMatrix34F2) {
    if (paramMatrix34F1 != null && paramMatrix34F2 != null)
      return getProjectionMatrix(paramArrayOfEyewearCalibrationReading, paramArrayOfEyewearCalibrationReading.length, paramMatrix34F1, paramMatrix34F2); 
    throw new NullPointerException("Matrix34F argument is null");
  }
  
  public boolean init(long paramLong1, long paramLong2, float paramFloat1, float paramFloat2) {
    return VuforiaJNI.EyewearUserCalibrator_init(this.swigCPtr, this, paramLong1, paramLong2, paramFloat1, paramFloat2);
  }
  
  public boolean isStereoStretched() {
    return VuforiaJNI.EyewearUserCalibrator_isStereoStretched(this.swigCPtr, this);
  }
  
  public static final class CONSISTENCY {
    public static final int BAD = 2;
    
    public static final int CONSISTENCY_LEN = 5;
    
    public static final int GOOD = 4;
    
    public static final int NONE = 0;
    
    public static final int OK = 3;
    
    public static final int VERY_BAD = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\EyewearUserCalibrator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */