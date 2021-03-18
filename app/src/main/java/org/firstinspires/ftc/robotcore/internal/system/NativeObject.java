package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;

public class NativeObject<ParentType extends RefCounted> extends DestructOnFinalize<ParentType> {
  protected ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
  
  protected MemoryAllocator memoryAllocator;
  
  protected long pointer;
  
  static {
    System.loadLibrary("RobotCore");
  }
  
  protected NativeObject() {
    this(defaultTraceLevel);
  }
  
  protected NativeObject(long paramLong) {
    this(paramLong, MemoryAllocator.UNKNOWN);
  }
  
  protected NativeObject(long paramLong, MemoryAllocator paramMemoryAllocator) {
    this(paramLong, paramMemoryAllocator, defaultTraceLevel);
  }
  
  protected NativeObject(long paramLong, MemoryAllocator paramMemoryAllocator, RefCounted.TraceLevel paramTraceLevel) {
    super(RefCounted.TraceLevel.None);
    this.traceLevel = paramTraceLevel;
    if (paramLong != 0L) {
      this.pointer = paramLong;
      this.memoryAllocator = paramMemoryAllocator;
      if (traceCtor())
        RobotLog.vv(getTag(), "construct(%s)", new Object[] { getTraceIdentifier() }); 
      return;
    } 
    throw new IllegalArgumentException("pointer must not be null");
  }
  
  protected NativeObject(long paramLong, RefCounted.TraceLevel paramTraceLevel) {
    this(paramLong, MemoryAllocator.UNKNOWN, paramTraceLevel);
  }
  
  protected NativeObject(RefCounted.TraceLevel paramTraceLevel) {
    super(paramTraceLevel);
    this.pointer = 0L;
    this.memoryAllocator = MemoryAllocator.UNKNOWN;
  }
  
  protected static long checkAlloc(long paramLong) {
    if (paramLong != 0L)
      return paramLong; 
    throw new OutOfMemoryError();
  }
  
  protected static native long nativeAllocMemory(long paramLong);
  
  protected static native void nativeFreeMemory(long paramLong);
  
  protected static native byte[] nativeGetBytes(long paramLong, int paramInt1, int paramInt2);
  
  protected static native long[] nativeGetLinkedList(long paramLong, int paramInt);
  
  protected static native long[] nativeGetNullTerminatedList(long paramLong, int paramInt1, int paramInt2);
  
  protected static native long nativeGetPointer(long paramLong, int paramInt);
  
  protected static native String nativeGetString(long paramLong, int paramInt);
  
  protected static native void nativeSetBytes(long paramLong, int paramInt, byte[] paramArrayOfbyte);
  
  protected static native void nativeSetPointer(long paramLong1, int paramInt, long paramLong2);
  
  public void allocateMemory(long paramLong) {
    synchronized (this.lock) {
      freeMemory();
      int i = paramLong cmp 0L;
      if (i > 0) {
        this.pointer = checkAlloc(nativeAllocMemory(paramLong));
        setMemoryAllocator(MemoryAllocator.MALLOC);
      } else if (i < 0) {
        throw new IllegalArgumentException("cbAlloc must be >= 0");
      } 
      return;
    } 
  }
  
  protected void clearPointer() {
    synchronized (this.lock) {
      this.pointer = 0L;
      return;
    } 
  }
  
  protected void destructor() {
    freeMemory();
    super.destructor();
  }
  
  public void freeMemory() {
    synchronized (this.lock) {
      if (this.memoryAllocator == MemoryAllocator.MALLOC) {
        nativeFreeMemory(this.pointer);
        clearPointer();
      } 
      return;
    } 
  }
  
  protected byte getByte(int paramInt) {
    return nativeGetBytes(this.pointer, paramInt, 1)[0];
  }
  
  protected byte[] getBytes(int paramInt1, int paramInt2) {
    return nativeGetBytes(this.pointer, paramInt1, paramInt2);
  }
  
  protected int getInt(int paramInt) {
    return TypeConversion.byteArrayToInt(nativeGetBytes(this.pointer, paramInt, 4), this.byteOrder);
  }
  
  protected long getLong(int paramInt) {
    return TypeConversion.byteArrayToLong(nativeGetBytes(this.pointer, paramInt, 8), this.byteOrder);
  }
  
  protected long getPointer(int paramInt) {
    return nativeGetPointer(this.pointer, paramInt);
  }
  
  protected short getShort(int paramInt) {
    return TypeConversion.byteArrayToShort(nativeGetBytes(this.pointer, paramInt, 2), this.byteOrder);
  }
  
  protected int getSizet(int paramInt) {
    return getInt(paramInt);
  }
  
  protected String getString(int paramInt) {
    return nativeGetString(this.pointer, paramInt);
  }
  
  public String getTraceIdentifier() {
    return Misc.formatInvariant("pointer=0x%08x", new Object[] { Long.valueOf(this.pointer) });
  }
  
  protected int getUByte(int paramInt) {
    return TypeConversion.unsignedByteToInt(getByte(paramInt));
  }
  
  protected long getUInt(int paramInt) {
    return TypeConversion.unsignedIntToLong(getInt(paramInt));
  }
  
  protected int getUShort(int paramInt) {
    return TypeConversion.unsignedShortToInt(getShort(paramInt));
  }
  
  protected void setBytes(int paramInt, byte[] paramArrayOfbyte) {
    nativeSetBytes(this.pointer, paramInt, paramArrayOfbyte);
  }
  
  protected void setInt(int paramInt1, int paramInt2) {
    byte[] arrayOfByte = TypeConversion.intToByteArray(paramInt2, this.byteOrder);
    nativeSetBytes(this.pointer, paramInt1, arrayOfByte);
  }
  
  protected void setLong(int paramInt, long paramLong) {
    byte[] arrayOfByte = TypeConversion.longToByteArray(paramLong, this.byteOrder);
    nativeSetBytes(this.pointer, paramInt, arrayOfByte);
  }
  
  protected void setMemoryAllocator(MemoryAllocator paramMemoryAllocator) {
    synchronized (this.lock) {
      this.memoryAllocator = paramMemoryAllocator;
      return;
    } 
  }
  
  protected void setPointer(int paramInt, long paramLong) {
    nativeSetPointer(this.pointer, paramInt, paramLong);
  }
  
  protected void setUInt(int paramInt, long paramLong) {
    setInt(paramInt, (int)paramLong);
  }
  
  public enum MemoryAllocator {
    EXTERNAL, MALLOC, UNKNOWN;
    
    static {
      MemoryAllocator memoryAllocator = new MemoryAllocator("EXTERNAL", 2);
      EXTERNAL = memoryAllocator;
      $VALUES = new MemoryAllocator[] { UNKNOWN, MALLOC, memoryAllocator };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\NativeObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */