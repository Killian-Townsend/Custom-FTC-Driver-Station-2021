package com.qualcomm.robotcore.util;

import java.util.LinkedList;
import java.util.Queue;

public class MovingStatistics {
  final int capacity;
  
  final Queue<Double> samples;
  
  final Statistics statistics;
  
  public MovingStatistics(int paramInt) {
    if (paramInt > 0) {
      this.statistics = new Statistics();
      this.capacity = paramInt;
      this.samples = new LinkedList<Double>();
      return;
    } 
    throw new IllegalArgumentException("MovingStatistics capacity must be positive");
  }
  
  public void add(double paramDouble) {
    this.statistics.add(paramDouble);
    this.samples.add(Double.valueOf(paramDouble));
    if (this.samples.size() > this.capacity)
      this.statistics.remove(((Double)this.samples.remove()).doubleValue()); 
  }
  
  public void clear() {
    this.statistics.clear();
    this.samples.clear();
  }
  
  public int getCount() {
    return this.statistics.getCount();
  }
  
  public double getMean() {
    return this.statistics.getMean();
  }
  
  public double getStandardDeviation() {
    return this.statistics.getStandardDeviation();
  }
  
  public double getVariance() {
    return this.statistics.getVariance();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\MovingStatistics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */