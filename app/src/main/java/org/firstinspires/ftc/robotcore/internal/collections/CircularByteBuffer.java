package org.firstinspires.ftc.robotcore.internal.collections;

import java.nio.ByteBuffer;

public class CircularByteBuffer {
  protected byte[] buffer;
  
  protected final int capacity;
  
  protected int readIndex;
  
  protected int size;
  
  public CircularByteBuffer(int paramInt) {
    this(paramInt, 2147483647);
  }
  
  public CircularByteBuffer(int paramInt1, int paramInt2) {
    this.buffer = new byte[paramInt1];
    this.capacity = paramInt2;
    clear();
  }
  
  public void addLast(byte paramByte) {
    int i = this.size + 1;
    if (i <= this.capacity) {
      if (i > allocated())
        grow(i); 
      i = mod(this.readIndex + this.size);
      this.buffer[i] = paramByte;
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
  
  public byte get(int paramInt) {
    if (paramInt < this.size && paramInt >= 0)
      return this.buffer[mod(this.readIndex + paramInt)]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("get(");
    stringBuilder.append(paramInt);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public byte getFirst() {
    return get(0);
  }
  
  public byte getLast() {
    return get(this.size - 1);
  }
  
  protected void grow(int paramInt) {
    byte[] arrayOfByte = new byte[paramInt];
    paramInt = read(arrayOfByte, 0, this.size);
    this.buffer = arrayOfByte;
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
  
  public void put(int paramInt, byte paramByte) {
    if (paramInt < this.size && paramInt >= 0) {
      this.buffer[mod(this.readIndex + paramInt)] = paramByte;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("put(");
    stringBuilder.append(paramInt);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int read(byte[] paramArrayOfbyte) {
    return read(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      int i = min(paramInt2, this.size, allocated() - this.readIndex);
      readTo(paramArrayOfbyte, paramInt1, i);
      paramInt2 = min(paramInt2 - i, this.size);
      readTo(paramArrayOfbyte, paramInt1 + i, paramInt2);
      return i + paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public byte[] readAll() {
    int i = size();
    byte[] arrayOfByte = new byte[i];
    readTo(arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  protected void readTo(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      System.arraycopy(this.buffer, this.readIndex, paramArrayOfbyte, paramInt1, paramInt2);
      this.readIndex = mod(this.readIndex + paramInt2);
      this.size -= paramInt2;
    } 
  }
  
  public int remainingCapacity() {
    return this.capacity - this.size;
  }
  
  public byte removeFirst() {
    if (!isEmpty()) {
      byte[] arrayOfByte = this.buffer;
      int i = this.readIndex;
      byte b = arrayOfByte[i];
      this.readIndex = mod(i + 1);
      this.size--;
      return b;
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
  
  public int write(ByteBuffer paramByteBuffer) {
    return write(paramByteBuffer.array(), paramByteBuffer.arrayOffset() + paramByteBuffer.position(), paramByteBuffer.remaining());
  }
  
  public int write(byte[] paramArrayOfbyte) {
    return write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
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
        writeFrom(paramArrayOfbyte, paramInt1, i);
        paramInt2 = min(paramInt2 - i, this.readIndex);
        writeFrom(paramArrayOfbyte, paramInt1 + i, paramInt2);
        return i + paramInt2;
      } 
      paramInt2 = min(paramInt2, allocated() - this.size);
      writeFrom(paramArrayOfbyte, paramInt1, paramInt2);
      return paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void writeFrom(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      int i = mod(this.readIndex + this.size);
      System.arraycopy(paramArrayOfbyte, paramInt1, this.buffer, i, paramInt2);
      this.size += paramInt2;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\CircularByteBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */