package androidx.renderscript;

public class BaseObj {
  private boolean mDestroyed;
  
  private long mID;
  
  RenderScript mRS;
  
  BaseObj(long paramLong, RenderScript paramRenderScript) {
    paramRenderScript.validate();
    this.mRS = paramRenderScript;
    this.mID = paramLong;
    this.mDestroyed = false;
  }
  
  private void helpDestroy() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mDestroyed : Z
    //   6: istore_2
    //   7: iconst_1
    //   8: istore_1
    //   9: iload_2
    //   10: ifne -> 83
    //   13: aload_0
    //   14: iconst_1
    //   15: putfield mDestroyed : Z
    //   18: goto -> 21
    //   21: aload_0
    //   22: monitorexit
    //   23: iload_1
    //   24: ifeq -> 77
    //   27: aload_0
    //   28: getfield mRS : Landroidx/renderscript/RenderScript;
    //   31: getfield mRWLock : Ljava/util/concurrent/locks/ReentrantReadWriteLock;
    //   34: invokevirtual readLock : ()Ljava/util/concurrent/locks/ReentrantReadWriteLock$ReadLock;
    //   37: astore_3
    //   38: aload_3
    //   39: invokevirtual lock : ()V
    //   42: aload_0
    //   43: getfield mRS : Landroidx/renderscript/RenderScript;
    //   46: invokevirtual isAlive : ()Z
    //   49: ifeq -> 63
    //   52: aload_0
    //   53: getfield mRS : Landroidx/renderscript/RenderScript;
    //   56: aload_0
    //   57: getfield mID : J
    //   60: invokevirtual nObjDestroy : (J)V
    //   63: aload_3
    //   64: invokevirtual unlock : ()V
    //   67: aload_0
    //   68: aconst_null
    //   69: putfield mRS : Landroidx/renderscript/RenderScript;
    //   72: aload_0
    //   73: lconst_0
    //   74: putfield mID : J
    //   77: return
    //   78: astore_3
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_3
    //   82: athrow
    //   83: iconst_0
    //   84: istore_1
    //   85: goto -> 21
    // Exception table:
    //   from	to	target	type
    //   2	7	78	finally
    //   13	18	78	finally
    //   21	23	78	finally
    //   79	81	78	finally
  }
  
  void checkValid() {
    if (this.mID == 0L) {
      if (getNObj() != null)
        return; 
      throw new RSIllegalArgumentException("Invalid object.");
    } 
  }
  
  public void destroy() {
    if (!this.mDestroyed) {
      helpDestroy();
      return;
    } 
    throw new RSInvalidStateException("Object already destroyed.");
  }
  
  public boolean equals(Object paramObject) {
    if (this == paramObject)
      return true; 
    if (paramObject == null)
      return false; 
    if (getClass() != paramObject.getClass())
      return false; 
    paramObject = paramObject;
    return (this.mID == ((BaseObj)paramObject).mID);
  }
  
  protected void finalize() throws Throwable {
    helpDestroy();
    super.finalize();
  }
  
  long getID(RenderScript paramRenderScript) {
    this.mRS.validate();
    if (!this.mDestroyed) {
      if (this.mID != 0L) {
        if (paramRenderScript == null || paramRenderScript == this.mRS)
          return this.mID; 
        throw new RSInvalidStateException("using object with mismatched context.");
      } 
      throw new RSRuntimeException("Internal error: Object id 0.");
    } 
    throw new RSInvalidStateException("using a destroyed object.");
  }
  
  android.renderscript.BaseObj getNObj() {
    return null;
  }
  
  public int hashCode() {
    long l = this.mID;
    return (int)(l >> 32L ^ 0xFFFFFFFL & l);
  }
  
  void setID(long paramLong) {
    if (this.mID == 0L) {
      this.mID = paramLong;
      return;
    } 
    throw new RSRuntimeException("Internal Error, reset of object ID.");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\renderscript\BaseObj.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */