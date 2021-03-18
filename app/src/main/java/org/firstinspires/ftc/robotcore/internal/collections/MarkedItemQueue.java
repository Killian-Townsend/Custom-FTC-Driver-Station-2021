package org.firstinspires.ftc.robotcore.internal.collections;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class MarkedItemQueue {
  protected Deque<Integer> countsToMarks;
  
  protected int size;
  
  protected Integer unmarkedAtEnd;
  
  public MarkedItemQueue() {
    this(8);
  }
  
  public MarkedItemQueue(int paramInt) {
    this.countsToMarks = new ArrayDeque<Integer>(paramInt);
    clear();
  }
  
  public void addMarkedItem() {
    this.size++;
    this.countsToMarks.addLast(Integer.valueOf(this.unmarkedAtEnd.intValue() + 1));
    this.unmarkedAtEnd = Integer.valueOf(0);
    verifyInvariants();
  }
  
  public void addUnmarkedItems(int paramInt) {
    if (paramInt >= 0) {
      this.unmarkedAtEnd = Integer.valueOf(this.unmarkedAtEnd.intValue() + paramInt);
      this.size += paramInt;
      verifyInvariants();
      return;
    } 
    throw new IllegalArgumentException(String.format("count must be >= 0: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public void clear() {
    this.countsToMarks.clear();
    this.unmarkedAtEnd = Integer.valueOf(0);
    this.size = 0;
    verifyInvariants();
  }
  
  protected int computedSize() {
    int i = this.unmarkedAtEnd.intValue();
    Iterator<Integer> iterator = this.countsToMarks.iterator();
    while (iterator.hasNext())
      i += ((Integer)iterator.next()).intValue(); 
    return i;
  }
  
  public boolean hasMarkedItem() {
    return this.countsToMarks.isEmpty() ^ true;
  }
  
  public boolean isAtMarkedItem() {
    return (!this.countsToMarks.isEmpty() && ((Integer)this.countsToMarks.peekFirst()).intValue() == 1);
  }
  
  public boolean isEmpty() {
    return (this.countsToMarks.isEmpty() && this.unmarkedAtEnd.intValue() == 0);
  }
  
  public void removeItems(int paramInt) {
    if (paramInt >= 0) {
      if (paramInt <= this.size)
        while (true) {
          if (paramInt > 0)
            try {
              if (!this.countsToMarks.isEmpty()) {
                int i = ((Integer)this.countsToMarks.removeFirst()).intValue();
                if (paramInt >= i) {
                  paramInt -= i;
                  this.size -= i;
                  continue;
                } 
                this.countsToMarks.addFirst(Integer.valueOf(i - paramInt));
                this.size -= paramInt;
                return;
              } 
              paramInt = Math.min(this.unmarkedAtEnd.intValue(), paramInt);
              this.unmarkedAtEnd = Integer.valueOf(this.unmarkedAtEnd.intValue() - paramInt);
              this.size -= paramInt;
              return;
            } finally {
              verifyInvariants();
            }  
          paramInt = Math.min(this.unmarkedAtEnd.intValue(), paramInt);
          this.unmarkedAtEnd = Integer.valueOf(this.unmarkedAtEnd.intValue() - paramInt);
          this.size -= paramInt;
          verifyInvariants();
          return;
        }  
      throw new IllegalArgumentException(String.format("remove count must be <= size: count=%d, size=%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.size) }));
    } 
    throw new IllegalArgumentException(String.format("remove count must be >=0: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public int removeUpToNextMarkedItemOrEnd() {
    try {
      int i;
      if (!this.countsToMarks.isEmpty()) {
        i = ((Integer)this.countsToMarks.removeFirst()).intValue() - 1 + 0;
        this.countsToMarks.addFirst(Integer.valueOf(1));
        Assert.assertTrue(isAtMarkedItem());
      } else {
        i = this.unmarkedAtEnd.intValue() + 0;
        this.unmarkedAtEnd = Integer.valueOf(0);
        Assert.assertTrue(isEmpty());
      } 
      this.size -= i;
      return i;
    } finally {
      verifyInvariants();
    } 
  }
  
  public int size() {
    return this.size;
  }
  
  protected void verifyInvariants() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\MarkedItemQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */