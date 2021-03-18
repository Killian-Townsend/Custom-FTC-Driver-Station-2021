package com.vuforia;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class InstanceId {
  protected boolean swigCMemOwn;
  
  private long swigCPtr;
  
  protected InstanceId(long paramLong, boolean paramBoolean) {
    this.swigCMemOwn = paramBoolean;
    this.swigCPtr = paramLong;
  }
  
  protected static long getCPtr(InstanceId paramInstanceId) {
    return (paramInstanceId == null) ? 0L : paramInstanceId.swigCPtr;
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
    //   27: invokestatic delete_InstanceId : (J)V
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
    boolean bool = paramObject instanceof InstanceId;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (bool) {
      bool1 = bool2;
      if (((InstanceId)paramObject).swigCPtr == this.swigCPtr)
        bool1 = true; 
    } 
    return bool1;
  }
  
  protected void finalize() {
    delete();
  }
  
  public ByteBuffer getBuffer() {
    return VuforiaJNI.InstanceId_getBuffer(this.swigCPtr, this);
  }
  
  public int getDataType() {
    return VuforiaJNI.InstanceId_getDataType(this.swigCPtr, this);
  }
  
  public long getLength() {
    return VuforiaJNI.InstanceId_getLength(this.swigCPtr, this);
  }
  
  public BigInteger getNumericValue() {
    return VuforiaJNI.InstanceId_getNumericValue(this.swigCPtr, this);
  }
  
  public static final class ID_DATA_TYPE {
    public static final int BYTES = 0;
    
    public static final int NUMERIC = 2;
    
    public static final int STRING = 1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\vuforia\InstanceId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */