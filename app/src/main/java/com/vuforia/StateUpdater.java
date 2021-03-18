package com.vuforia;

public class StateUpdater {
  private static State sState;
  
  private static final Object sStateMutex = new Object();
  
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected StateUpdater(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(StateUpdater paramStateUpdater) {
    return (paramStateUpdater == null) ? 0L : paramStateUpdater.swigCPtr;
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
    //   27: invokestatic delete_StateUpdater : (J)V
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
    boolean bool = paramObject instanceof StateUpdater;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((StateUpdater)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public double getCurrentTimeStamp() {
    return VuforiaJNI.StateUpdater_getCurrentTimeStamp(this.swigCPtr, this);
  }
  
  public State getLatestState() {
    synchronized (sStateMutex) {
      return sState;
    } 
  }
  
  public State updateState() {
    synchronized (sStateMutex) {
      if (sState != null)
        sState.delete(); 
      State state = new State(VuforiaJNI.StateUpdater_updateState(this.swigCPtr, this), true);
      sState = state;
      return state;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\StateUpdater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */