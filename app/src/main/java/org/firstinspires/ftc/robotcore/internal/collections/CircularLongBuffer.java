package org.firstinspires.ftc.robotcore.internal.collections;

import java.nio.LongBuffer;

public class CircularLongBuffer {
  protected long[] buffer;
  
  protected final int capacity;
  
  protected int readIndex;
  
  protected int size;
  
  public CircularLongBuffer(int paramInt) {
    this(paramInt, 2147483647);
  }
  
  public CircularLongBuffer(int paramInt1, int paramInt2) {
    this.buffer = new long[paramInt1];
    this.capacity = paramInt2;
    clear();
  }
  
  public void addLast(long paramLong) {
    int i = this.size + 1;
    if (i <= this.capacity) {
      if (i > allocated())
        grow(i); 
      i = mod(this.readIndex + this.size);
      this.buffer[i] = paramLong;
      this.size++;
      return;
    } 
    throw new IllegalStateException("full");
  }
  
  protected int allocated() {
    return this.buffer.length;
  }
  
  public int capacity() {
    return this.capacity;
  }
  
  public void clear() {
    this.readIndex = 0;
    this.size = 0;
  }
  
  public long get(int paramInt) {
    if (paramInt < this.size && paramInt >= 0)
      return this.buffer[mod(this.readIndex + paramInt)]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("get(");
    stringBuilder.append(paramInt);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public long getFirst() {
    return get(0);
  }
  
  public long getLast() {
    return get(this.size - 1);
  }
  
  protected void grow(int paramInt) {
    long[] arrayOfLong = new long[paramInt];
    paramInt = read(arrayOfLong, 0, this.size);
    this.buffer = arrayOfLong;
    this.readIndex = 0;
    this.size = paramInt;
  }
  
  public boolean isEmpty() {
    return (this.size == 0);
  }
  
  protected int min(int paramInt1, int paramInt2) {
    return Math.min(paramInt1, paramInt2);
  }
  
  protected int min(int paramInt1, int paramInt2, int paramInt3) {
    return Math.min(paramInt1, Math.min(paramInt2, paramInt3));
  }
  
  protected int mod(int paramInt) {
    return (paramInt < allocated()) ? paramInt : (paramInt - allocated());
  }
  
  public void put(int paramInt, long paramLong) {
    if (paramInt < this.size && paramInt >= 0) {
      this.buffer[mod(this.readIndex + paramInt)] = paramLong;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("put(");
    stringBuilder.append(paramInt);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int read(long[] paramArrayOflong) {
    return read(paramArrayOflong, 0, paramArrayOflong.length);
  }
  
  public int read(long[] paramArrayOflong, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      int i = min(paramInt2, this.size, allocated() - this.readIndex);
      readTo(paramArrayOflong, paramInt1, i);
      paramInt2 = min(paramInt2 - i, this.size);
      readTo(paramArrayOflong, paramInt1 + i, paramInt2);
      return i + paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void readTo(long[] paramArrayOflong, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      System.arraycopy(this.buffer, this.readIndex, paramArrayOflong, paramInt1, paramInt2);
      this.readIndex = mod(this.readIndex + paramInt2);
      this.size -= paramInt2;
    } 
  }
  
  public int remainingCapacity() {
    return this.capacity - this.size;
  }
  
  public long removeFirst() {
    if (!isEmpty()) {
      long[] arrayOfLong = this.buffer;
      int i = this.readIndex;
      long l = arrayOfLong[i];
      this.readIndex = mod(i + 1);
      this.size--;
      return l;
    } 
    throw new IndexOutOfBoundsException("removeFirst");
  }
  
  public int size() {
    return this.size;
  }
  
  public int skip(int paramInt) {
    if (paramInt >= 0) {
      int i = min(paramInt, this.size, allocated() - this.readIndex);
      skipBy(i);
      paramInt = min(paramInt - i, this.size);
      skipBy(paramInt);
      return i + paramInt;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void skipBy(int paramInt) {
    if (paramInt > 0) {
      this.readIndex = mod(this.readIndex + paramInt);
      this.size -= paramInt;
    } 
  }
  
  public int write(LongBuffer paramLongBuffer) {
    return write(paramLongBuffer.array(), paramLongBuffer.arrayOffset() + paramLongBuffer.position(), paramLongBuffer.remaining());
  }
  
  public int write(long[] paramArrayOflong) {
    return write(paramArrayOflong, 0, paramArrayOflong.length);
  }
  
  public int write(long[] paramArrayOflong, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      int m = this.size;
      int k = m + paramInt2;
      int j = this.capacity;
      int i = k;
      if (k > j) {
        paramInt2 = Math.max(0, j - m);
        i = j;
      } 
      if (i > allocated())
        grow(i); 
      if (this.size == 0)
        this.readIndex = 0; 
      i = this.readIndex + this.size;
      if (i < allocated()) {
        i = min(paramInt2, allocated() - i);
        writeFrom(paramArrayOflong, paramInt1, i);
        paramInt2 = min(paramInt2 - i, this.readIndex);
        writeFrom(paramArrayOflong, paramInt1 + i, paramInt2);
        return i + paramInt2;
      } 
      paramInt2 = min(paramInt2, allocated() - this.size);
      writeFrom(paramArrayOflong, paramInt1, paramInt2);
      return paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void writeFrom(long[] paramArrayOflong, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      int i = mod(this.readIndex + this.size);
      System.arraycopy(paramArrayOflong, paramInt1, this.buffer, i, paramInt2);
      this.size += paramInt2;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\CircularLongBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */