package org.firstinspires.ftc.robotcore.internal.collections;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;

public class ArrayRunQueue<E> {
  protected CircularIntBuffer counts;
  
  protected ArrayDeque<E> elements;
  
  protected int size;
  
  public ArrayRunQueue() {
    this(8);
  }
  
  public ArrayRunQueue(int paramInt) {
    this.elements = new ArrayDeque<E>(paramInt);
    this.counts = new CircularIntBuffer(paramInt);
    clear();
  }
  
  public void addLast(E paramE) {
    addLast(paramE, 1);
  }
  
  public void addLast(E paramE, int paramInt) {
    if (paramInt >= 0) {
      if (paramInt > 0)
        try {
          int i = size();
          if (!isEmpty() && same(this.elements.getLast(), paramE)) {
            int j = this.counts.size() - 1;
            this.counts.put(j, this.counts.get(j) + paramInt);
          } else {
            this.elements.addLast(paramE);
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
  
  public E getFirst() {
    if (!isEmpty())
      return this.elements.getFirst(); 
    throw new NoSuchElementException("getFirst");
  }
  
  public E getLast() {
    if (!isEmpty())
      return this.elements.getLast(); 
    throw new NoSuchElementException("getLast");
  }
  
  public boolean isEmpty() {
    return this.counts.isEmpty();
  }
  
  public boolean offerLast(E paramE) {
    addLast(paramE);
    return true;
  }
  
  public E removeFirstCount(int paramInt) {
    if (paramInt >= 0)
      try {
        if (paramInt <= this.size) {
          E e1 = null;
          E e2 = null;
          if (paramInt > 0) {
            int j = size();
            int i = paramInt;
            e1 = e2;
            while (i > 0) {
              int k = this.counts.get(0);
              if (k <= i) {
                i -= k;
                this.counts.removeFirst();
                e1 = this.elements.removeFirst();
                continue;
              } 
              this.counts.put(0, k - i);
              e1 = this.elements.getFirst();
              i = 0;
            } 
            this.size = j - paramInt;
          } 
          return e1;
        } 
        throw new NoSuchElementException(String.format("count must be <= size: count=%d size=%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.size) }));
      } finally {
        verifyInvariants();
      }  
    throw new IllegalArgumentException(String.format("count must be >= 0: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  protected boolean same(E paramE1, E paramE2) {
    return paramE1.equals(paramE2);
  }
  
  public int size() {
    return this.size;
  }
  
  protected void verifyInvariants() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\ArrayRunQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */