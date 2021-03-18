package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class RollingAverage {
  public static final int DEFAULT_SIZE = 100;
  
  private final Queue<Integer> queue = new LinkedList<Integer>();
  
  private int size;
  
  private long total;
  
  public RollingAverage() {
    resize(100);
  }
  
  public RollingAverage(int paramInt) {
    resize(paramInt);
  }
  
  public void addNumber(int paramInt) {
    if (this.queue.size() >= this.size) {
      int i = ((Integer)this.queue.remove()).intValue();
      this.total -= i;
    } 
    this.queue.add(Integer.valueOf(paramInt));
    this.total += paramInt;
  }
  
  public int getAverage() {
    return this.queue.isEmpty() ? 0 : (int)(this.total / this.queue.size());
  }
  
  public void reset() {
    this.queue.clear();
  }
  
  public void resize(int paramInt) {
    this.size = paramInt;
    this.queue.clear();
  }
  
  public int size() {
    return this.size;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\RollingAverage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */