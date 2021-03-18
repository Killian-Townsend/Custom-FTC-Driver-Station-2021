package com.vuforia;

public class Tool {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  public Tool() {
    this(VuforiaJNI.new_Tool(), true);
  }
  
  protected Tool(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  public static Matrix44F convert2GLMatrix(Matrix34F paramMatrix34F) {
    return new Matrix44F(VuforiaJNI.Tool_convert2GLMatrix(Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F), true);
  }
  
  public static Matrix44F convertPerspectiveProjection2GLMatrix(Matrix34F paramMatrix34F, float paramFloat1, float paramFloat2) {
    return new Matrix44F(VuforiaJNI.Tool_convertPerspectiveProjection2GLMatrix(Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F, paramFloat1, paramFloat2), true);
  }
  
  public static Matrix44F convertPose2GLMatrix(Matrix34F paramMatrix34F) {
    return new Matrix44F(VuforiaJNI.Tool_convertPose2GLMatrix(Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F), true);
  }
  
  protected static long getCPtr(Tool paramTool) {
    return (paramTool == null) ? 0L : paramTool.swigCPtr;
  }
  
  public static Matrix44F getProjectionGL(CameraCalibration paramCameraCalibration, float paramFloat1, float paramFloat2) {
    return new Matrix44F(VuforiaJNI.Tool_getProjectionGL(CameraCalibration.getCPtr(paramCameraCalibration), paramCameraCalibration, paramFloat1, paramFloat2), true);
  }
  
  public static Matrix34F multiply(Matrix34F paramMatrix34F1, Matrix34F paramMatrix34F2) {
    return new Matrix34F(VuforiaJNI.Tool_multiply__SWIG_0(Matrix34F.getCPtr(paramMatrix34F1), paramMatrix34F1, Matrix34F.getCPtr(paramMatrix34F2), paramMatrix34F2), true);
  }
  
  public static Matrix44F multiply(Matrix44F paramMatrix44F1, Matrix44F paramMatrix44F2) {
    return new Matrix44F(VuforiaJNI.Tool_multiply__SWIG_1(Matrix44F.getCPtr(paramMatrix44F1), paramMatrix44F1, Matrix44F.getCPtr(paramMatrix44F2), paramMatrix44F2), true);
  }
  
  public static Vec4F multiply(Vec4F paramVec4F, Matrix44F paramMatrix44F) {
    return new Vec4F(VuforiaJNI.Tool_multiply__SWIG_2(Vec4F.getCPtr(paramVec4F), paramVec4F, Matrix44F.getCPtr(paramMatrix44F), paramMatrix44F), true);
  }
  
  public static Matrix44F multiplyGL(Matrix44F paramMatrix44F1, Matrix44F paramMatrix44F2) {
    return new Matrix44F(VuforiaJNI.Tool_multiplyGL(Matrix44F.getCPtr(paramMatrix44F1), paramMatrix44F1, Matrix44F.getCPtr(paramMatrix44F2), paramMatrix44F2), true);
  }
  
  public static Vec2F projectPoint(CameraCalibration paramCameraCalibration, Matrix34F paramMatrix34F, Vec3F paramVec3F) {
    return new Vec2F(VuforiaJNI.Tool_projectPoint(CameraCalibration.getCPtr(paramCameraCalibration), paramCameraCalibration, Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F, Vec3F.getCPtr(paramVec3F), paramVec3F), true);
  }
  
  public static void setRotation(Matrix34F paramMatrix34F, Vec3F paramVec3F, float paramFloat) {
    VuforiaJNI.Tool_setRotation(Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F, Vec3F.getCPtr(paramVec3F), paramVec3F, paramFloat);
  }
  
  public static void setTranslation(Matrix34F paramMatrix34F, Vec3F paramVec3F) {
    VuforiaJNI.Tool_setTranslation(Matrix34F.getCPtr(paramMatrix34F), paramMatrix34F, Vec3F.getCPtr(paramVec3F), paramVec3F);
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
    //   27: invokestatic delete_Tool : (J)V
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
    boolean bool = paramObject instanceof Tool;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((Tool)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\Tool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */