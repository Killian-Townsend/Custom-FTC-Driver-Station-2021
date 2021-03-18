package com.qualcomm.robotcore.util;

public class Statistics {
  double m2;
  
  double mean;
  
  int n;
  
  public Statistics() {
    clear();
  }
  
  public void add(double paramDouble) {
    int i = this.n + 1;
    this.n = i;
    double d2 = this.mean;
    double d1 = paramDouble - d2;
    d2 += d1 / i;
    this.mean = d2;
    this.m2 += d1 * (paramDouble - d2);
  }
  
  public void clear() {
    this.n = 0;
    this.mean = 0.0D;
    this.m2 = 0.0D;
  }
  
  public int getCount() {
    return this.n;
  }
  
  public double getMean() {
    return this.mean;
  }
  
  public double getStandardDeviation() {
    return Math.sqrt(getVariance());
  }
  
  public double getVariance() {
    return this.m2 / (this.n - 1);
  }
  
  public void remove(double paramDouble) {
    int i = this.n;
    int j = i - 1;
    if (j == 0) {
      clear();
      return;
    } 
    double d1 = this.mean;
    double d2 = paramDouble - d1;
    double d4 = i;
    double d3 = j;
    d4 = d4 * d2 / d3;
    this.m2 -= d4 * d2;
    this.mean = (d1 * i - paramDouble) / d3;
    this.n = j;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\Statistics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */