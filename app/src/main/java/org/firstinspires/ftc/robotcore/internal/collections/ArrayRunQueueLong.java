package org.firstinspires.ftc.robotcore.internal.collections;

import java.util.NoSuchElementException;

public class ArrayRunQueueLong {
  protected CircularIntBuffer counts;
  
  protected CircularLongBuffer elements;
  
  protected int size;
  
  public ArrayRunQueueLong() {
    this(8);
  }
  
  public ArrayRunQueueLong(int paramInt) {
    this.elements = new CircularLongBuffer(paramInt);
    this.counts = new CircularIntBuffer(paramInt);
    clear();
  }
  
  public void addLast(long paramLong) {
    addLast(paramLong, 1);
  }
  
  public void addLast(long paramLong, int paramInt) {
    if (paramInt >= 0) {
      if (paramInt > 0)
        try {
          int i = size();
          if (!isEmpty() && this.elements.getLast() == paramLong) {
            int j = this.counts.size() - 1;
            this.counts.put(j, this.counts.get(j) + paramInt);
          } else {
            this.elements.addLast(paramLong);
            this.counts.addLast(paramInt);
          } 
          this.size = i + paramInt;
          return;
        } finally {
          verifyInvariants();
        }  
    } else {
      throw new IllegalArgumentException(String.format("count must be >= 0: %d", new Object[] { Integer.valueOf(paramInt) }));
    } 
    verifyInvariants();
  }
  
  public void clear() {
    this.elements.clear();
    this.counts.clear();
    this.size = 0;
    verifyInvariants();
  }
  
  protected int computedSize() {
    int i = 0;
    int j = 0;
    while (i < this.counts.size()) {
      j += this.counts.get(i);
      i++;
    } 
    return j;
  }
  
  public long getFirst() {
    if (!isEmpty())
      return this.elements.getFirst(); 
    throw new NoSuchElementException("getFirst");
  }
  
  public long getLast() {
    if (!isEmpty())
      return this.elements.getLast(); 
    throw new NoSuchElementException("getLast");
  }
  
  public boolean isEmpty() {
    return this.counts.isEmpty();
  }
  
  public boolean offerLast(long paramLong) {
    addLast(paramLong);
    return true;
  }
  
  public long removeFirstCount(int paramInt) {
    if (paramInt >= 0)
      try {
        if (paramInt <= this.size) {
          long l1 = 0L;
          long l2 = l1;
          if (paramInt > 0) {
            int j = size();
            for (int i = paramInt; i > 0; i = 0) {
              int k = this.counts.get(0);
              if (k <= i) {
                i -= k;
                this.counts.removeFirst();
                l1 = this.elements.removeFirst();
                continue;
              } 
              this.counts.put(0, k - i);
              l1 = this.elements.getFirst();
            } 
            this.size = j - paramInt;
            l2 = l1;
          } 
          return l2;
        } 
        throw new NoSuchElementException(String.format("count must be <= size: count=%d size=%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.size) }));
      } finally {
        verifyInvariants();
      }  
    throw new IllegalArgumentException(String.format("count must be >= 0: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public int size() {
    return this.size;
  }
  
  protected void verifyInvariants() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\ArrayRunQueueLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */