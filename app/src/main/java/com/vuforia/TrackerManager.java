package com.vuforia;

public class TrackerManager {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected TrackerManager(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(TrackerManager paramTrackerManager) {
    return (paramTrackerManager == null) ? 0L : paramTrackerManager.swigCPtr;
  }
  
  public static TrackerManager getInstance() {
    if (Vuforia.wasInitializedJava())
      return new TrackerManager(VuforiaJNI.TrackerManager_getInstance(), false); 
    throw new RuntimeException("Use of the Java Vuforia APIs requires initalization via the com.vuforia.Vuforia class");
  }
  
  public boolean deinitTracker(Type paramType) {
    return VuforiaJNI.TrackerManager_deinitTracker(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
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
    //   27: invokestatic delete_TrackerManager : (J)V
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
    boolean bool = paramObject instanceof TrackerManager;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((TrackerManager)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public StateUpdater getStateUpdater() {
    return new StateUpdater(VuforiaJNI.TrackerManager_getStateUpdater(this.swigCPtr, this), false);
  }
  
  public Tracker getTracker(Type paramType) {
    long l = VuforiaJNI.TrackerManager_getTracker(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
    if (l == 0L)
      return null; 
    Tracker tracker = new Tracker(l, false);
    return (Tracker)(tracker.isOfType(ObjectTracker.getClassType()) ? new ObjectTracker(l, false) : (tracker.isOfType(SmartTerrain.getClassType()) ? new SmartTerrain(l, false) : (tracker.isOfType(RotationalDeviceTracker.getClassType()) ? new RotationalDeviceTracker(l, false) : (tracker.isOfType(PositionalDeviceTracker.getClassType()) ? new PositionalDeviceTracker(l, false) : null))));
  }
  
  public Tracker initTracker(Type paramType) {
    long l = VuforiaJNI.TrackerManager_initTracker(this.swigCPtr, this, Type.getCPtr(paramType), paramType);
    if (l == 0L)
      return null; 
    Tracker tracker = new Tracker(l, false);
    return (Tracker)(tracker.isOfType(ObjectTracker.getClassType()) ? new ObjectTracker(l, false) : (tracker.isOfType(SmartTerrain.getClassType()) ? new SmartTerrain(l, false) : (tracker.isOfType(RotationalDeviceTracker.getClassType()) ? new RotationalDeviceTracker(l, false) : (tracker.isOfType(PositionalDeviceTracker.getClassType()) ? new PositionalDeviceTracker(l, false) : null))));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\TrackerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */