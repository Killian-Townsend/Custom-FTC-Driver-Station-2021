package org.firstinspires.ftc.robotcore.internal.collections;

import java.nio.IntBuffer;

public class CircularIntBuffer {
  protected int[] buffer;
  
  protected final int capacity;
  
  protected int readIndex;
  
  protected int size;
  
  public CircularIntBuffer(int paramInt) {
    this(paramInt, 2147483647);
  }
  
  public CircularIntBuffer(int paramInt1, int paramInt2) {
    this.buffer = new int[paramInt1];
    this.capacity = paramInt2;
    clear();
  }
  
  public void addLast(int paramInt) {
    int i = this.size + 1;
    if (i <= this.capacity) {
      if (i > allocated())
        grow(i); 
      i = mod(this.readIndex + this.size);
      this.buffer[i] = paramInt;
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
  
  public int get(int paramInt) {
    if (paramInt < this.size && paramInt >= 0)
      return this.buffer[mod(this.readIndex + paramInt)]; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("get(");
    stringBuilder.append(paramInt);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int getFirst() {
    return get(0);
  }
  
  public int getLast() {
    return get(this.size - 1);
  }
  
  protected void grow(int paramInt) {
    int[] arrayOfInt = new int[paramInt];
    paramInt = read(arrayOfInt, 0, this.size);
    this.buffer = arrayOfInt;
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
  
  public void put(int paramInt1, int paramInt2) {
    if (paramInt1 < this.size && paramInt1 >= 0) {
      this.buffer[mod(this.readIndex + paramInt1)] = paramInt2;
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("put(");
    stringBuilder.append(paramInt1);
    stringBuilder.append(")");
    throw new IndexOutOfBoundsException(stringBuilder.toString());
  }
  
  public int read(int[] paramArrayOfint) {
    return read(paramArrayOfint, 0, paramArrayOfint.length);
  }
  
  public int read(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    if (paramInt2 >= 0) {
      int i = min(paramInt2, this.size, allocated() - this.readIndex);
      readTo(paramArrayOfint, paramInt1, i);
      paramInt2 = min(paramInt2 - i, this.size);
      readTo(paramArrayOfint, paramInt1 + i, paramInt2);
      return i + paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void readTo(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      System.arraycopy(this.buffer, this.readIndex, paramArrayOfint, paramInt1, paramInt2);
      this.readIndex = mod(this.readIndex + paramInt2);
      this.size -= paramInt2;
    } 
  }
  
  public int remainingCapacity() {
    return this.capacity - this.size;
  }
  
  public int removeFirst() {
    if (!isEmpty()) {
      int[] arrayOfInt = this.buffer;
      int i = this.readIndex;
      int j = arrayOfInt[i];
      this.readIndex = mod(i + 1);
      this.size--;
      return j;
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
  
  public int write(IntBuffer paramIntBuffer) {
    return write(paramIntBuffer.array(), paramIntBuffer.arrayOffset() + paramIntBuffer.position(), paramIntBuffer.remaining());
  }
  
  public int write(int[] paramArrayOfint) {
    return write(paramArrayOfint, 0, paramArrayOfint.length);
  }
  
  public int write(int[] paramArrayOfint, int paramInt1, int paramInt2) {
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
        writeFrom(paramArrayOfint, paramInt1, i);
        paramInt2 = min(paramInt2 - i, this.readIndex);
        writeFrom(paramArrayOfint, paramInt1 + i, paramInt2);
        return i + paramInt2;
      } 
      paramInt2 = min(paramInt2, allocated() - this.size);
      writeFrom(paramArrayOfint, paramInt1, paramInt2);
      return paramInt2;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("count must be non-negative: ");
    stringBuilder.append(paramInt2);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  protected void writeFrom(int[] paramArrayOfint, int paramInt1, int paramInt2) {
    if (paramInt2 > 0) {
      int i = mod(this.readIndex + this.size);
      System.arraycopy(paramArrayOfint, paramInt1, this.buffer, i, paramInt2);
      this.size += paramInt2;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\CircularIntBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */