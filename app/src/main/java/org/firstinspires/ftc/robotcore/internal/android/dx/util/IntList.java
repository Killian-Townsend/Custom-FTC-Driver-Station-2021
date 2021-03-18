package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.util.Arrays;

public final class IntList extends MutabilityControl {
  public static final IntList EMPTY;
  
  private int size;
  
  private boolean sorted;
  
  private int[] values;
  
  static {
    IntList intList = new IntList(0);
    EMPTY = intList;
    intList.setImmutable();
  }
  
  public IntList() {
    this(4);
  }
  
  public IntList(int paramInt) {
    super(true);
    try {
      this.values = new int[paramInt];
      this.size = 0;
      this.sorted = true;
      return;
    } catch (NegativeArraySizeException negativeArraySizeException) {
      throw new IllegalArgumentException("size < 0");
    } 
  }
  
  private void growIfNeeded() {
    int i = this.size;
    int[] arrayOfInt = this.values;
    if (i == arrayOfInt.length) {
      int[] arrayOfInt1 = new int[i * 3 / 2 + 10];
      System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, i);
      this.values = arrayOfInt1;
    } 
  }
  
  public static IntList makeImmutable(int paramInt) {
    IntList intList = new IntList(1);
    intList.add(paramInt);
    intList.setImmutable();
    return intList;
  }
  
  public static IntList makeImmutable(int paramInt1, int paramInt2) {
    IntList intList = new IntList(2);
    intList.add(paramInt1);
    intList.add(paramInt2);
    intList.setImmutable();
    return intList;
  }
  
  public void add(int paramInt) {
    throwIfImmutable();
    growIfNeeded();
    int[] arrayOfInt = this.values;
    int i = this.size;
    int j = i + 1;
    this.size = j;
    arrayOfInt[i] = paramInt;
    if (this.sorted) {
      boolean bool = true;
      if (j > 1) {
        if (paramInt < arrayOfInt[j - 2])
          bool = false; 
        this.sorted = bool;
      } 
    } 
  }
  
  public int binarysearch(int paramInt) {
    int k = this.size;
    if (!this.sorted) {
      for (int m = 0; m < k; m++) {
        if (this.values[m] == paramInt)
          return m; 
      } 
      return -k;
    } 
    int j = -1;
    int i = k;
    while (i > j + 1) {
      int m = (i - j >> 1) + j;
      if (paramInt <= this.values[m]) {
        i = m;
        continue;
      } 
      j = m;
    } 
    return (i != k) ? ((paramInt == this.values[i]) ? i : (-i - 1)) : (-k - 1);
  }
  
  public boolean contains(int paramInt) {
    return (indexOf(paramInt) >= 0);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (!(paramObject instanceof IntList))
      return false; 
    paramObject = paramObject;
    if (this.sorted != ((IntList)paramObject).sorted)
      return false; 
    if (this.size != ((IntList)paramObject).size)
      return false; 
    for (int i = 0; i < this.size; i++) {
      if (this.values[i] != ((IntList)paramObject).values[i])
        return false; 
    } 
    return true;
  }
  
  public int get(int paramInt) {
    if (paramInt < this.size)
      try {
        return this.values[paramInt];
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        throw new IndexOutOfBoundsException("n < 0");
      }  
    throw new IndexOutOfBoundsException("n >= size()");
  }
  
  public int hashCode() {
    int i = 0;
    int j = 0;
    while (i < this.size) {
      j = j * 31 + this.values[i];
      i++;
    } 
    return j;
  }
  
  public int indexOf(int paramInt) {
    paramInt = binarysearch(paramInt);
    return (paramInt >= 0) ? paramInt : -1;
  }
  
  public void insert(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: iload_1
    //   1: aload_0
    //   2: getfield size : I
    //   5: if_icmpgt -> 127
    //   8: aload_0
    //   9: invokespecial growIfNeeded : ()V
    //   12: aload_0
    //   13: getfield values : [I
    //   16: astore #7
    //   18: iload_1
    //   19: iconst_1
    //   20: iadd
    //   21: istore_3
    //   22: aload #7
    //   24: iload_1
    //   25: aload #7
    //   27: iload_3
    //   28: aload_0
    //   29: getfield size : I
    //   32: iload_1
    //   33: isub
    //   34: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   37: aload_0
    //   38: getfield values : [I
    //   41: astore #7
    //   43: aload #7
    //   45: iload_1
    //   46: iload_2
    //   47: iastore
    //   48: aload_0
    //   49: getfield size : I
    //   52: istore #4
    //   54: iconst_1
    //   55: istore #6
    //   57: aload_0
    //   58: iload #4
    //   60: iconst_1
    //   61: iadd
    //   62: putfield size : I
    //   65: aload_0
    //   66: getfield sorted : Z
    //   69: ifeq -> 117
    //   72: iload_1
    //   73: ifeq -> 86
    //   76: iload_2
    //   77: aload #7
    //   79: iload_1
    //   80: iconst_1
    //   81: isub
    //   82: iaload
    //   83: if_icmple -> 117
    //   86: iload #6
    //   88: istore #5
    //   90: iload_1
    //   91: aload_0
    //   92: getfield size : I
    //   95: iconst_1
    //   96: isub
    //   97: if_icmpeq -> 120
    //   100: iload_2
    //   101: aload_0
    //   102: getfield values : [I
    //   105: iload_3
    //   106: iaload
    //   107: if_icmpge -> 117
    //   110: iload #6
    //   112: istore #5
    //   114: goto -> 120
    //   117: iconst_0
    //   118: istore #5
    //   120: aload_0
    //   121: iload #5
    //   123: putfield sorted : Z
    //   126: return
    //   127: new java/lang/IndexOutOfBoundsException
    //   130: dup
    //   131: ldc 'n > size()'
    //   133: invokespecial <init> : (Ljava/lang/String;)V
    //   136: athrow
  }
  
  public IntList mutableCopy() {
    int j = this.size;
    IntList intList = new IntList(j);
    for (int i = 0; i < j; i++)
      intList.add(this.values[i]); 
    return intList;
  }
  
  public int pop() {
    throwIfImmutable();
    int i = get(this.size - 1);
    this.size--;
    return i;
  }
  
  public void pop(int paramInt) {
    throwIfImmutable();
    this.size -= paramInt;
  }
  
  public void removeIndex(int paramInt) {
    int i = this.size;
    if (paramInt < i) {
      int[] arrayOfInt = this.values;
      System.arraycopy(arrayOfInt, paramInt + 1, arrayOfInt, paramInt, i - paramInt - 1);
      this.size--;
      return;
    } 
    throw new IndexOutOfBoundsException("n >= size()");
  }
  
  public void set(int paramInt1, int paramInt2) {
    throwIfImmutable();
    if (paramInt1 < this.size)
      try {
        this.values[paramInt1] = paramInt2;
        this.sorted = false;
        return;
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        if (paramInt1 >= 0)
          return; 
        throw new IllegalArgumentException("n < 0");
      }  
    throw new IndexOutOfBoundsException("n >= size()");
  }
  
  public void shrink(int paramInt) {
    if (paramInt >= 0) {
      if (paramInt <= this.size) {
        throwIfImmutable();
        this.size = paramInt;
        return;
      } 
      throw new IllegalArgumentException("newSize > size");
    } 
    throw new IllegalArgumentException("newSize < 0");
  }
  
  public int size() {
    return this.size;
  }
  
  public void sort() {
    throwIfImmutable();
    if (!this.sorted) {
      Arrays.sort(this.values, 0, this.size);
      this.sorted = true;
    } 
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(this.size * 5 + 10);
    stringBuffer.append('{');
    for (int i = 0; i < this.size; i++) {
      if (i != 0)
        stringBuffer.append(", "); 
      stringBuffer.append(this.values[i]);
    } 
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
  
  public int top() {
    return get(this.size - 1);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\IntList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */