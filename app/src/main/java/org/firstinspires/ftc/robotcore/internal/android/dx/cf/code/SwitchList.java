package org.firstinspires.ftc.robotcore.internal.android.dx.cf.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityControl;

public final class SwitchList extends MutabilityControl {
  private int size;
  
  private final IntList targets;
  
  private final IntList values;
  
  public SwitchList(int paramInt) {
    super(true);
    this.values = new IntList(paramInt);
    this.targets = new IntList(paramInt + 1);
    this.size = paramInt;
  }
  
  public void add(int paramInt1, int paramInt2) {
    throwIfImmutable();
    if (paramInt2 >= 0) {
      this.values.add(paramInt1);
      this.targets.add(paramInt2);
      return;
    } 
    throw new IllegalArgumentException("target < 0");
  }
  
  public int getDefaultTarget() {
    return this.targets.get(this.size);
  }
  
  public int getTarget(int paramInt) {
    return this.targets.get(paramInt);
  }
  
  public IntList getTargets() {
    return this.targets;
  }
  
  public int getValue(int paramInt) {
    return this.values.get(paramInt);
  }
  
  public IntList getValues() {
    return this.values;
  }
  
  public void removeSuperfluousDefaults() {
    throwIfImmutable();
    int i = this.size;
    if (i == this.targets.size() - 1) {
      int m = this.targets.get(i);
      int j = 0;
      int k;
      for (k = 0; j < i; k = n) {
        int i1 = this.targets.get(j);
        int n = k;
        if (i1 != m) {
          if (j != k) {
            this.targets.set(k, i1);
            IntList intList = this.values;
            intList.set(k, intList.get(j));
          } 
          n = k + 1;
        } 
        j++;
      } 
      if (k != i) {
        this.values.shrink(k);
        this.targets.set(k, m);
        this.targets.shrink(k + 1);
        this.size = k;
      } 
      return;
    } 
    throw new IllegalArgumentException("incomplete instance");
  }
  
  public void setDefaultTarget(int paramInt) {
    throwIfImmutable();
    if (paramInt >= 0) {
      if (this.targets.size() == this.size) {
        this.targets.add(paramInt);
        return;
      } 
      throw new RuntimeException("non-default elements not all set");
    } 
    throw new IllegalArgumentException("target < 0");
  }
  
  public void setImmutable() {
    this.values.setImmutable();
    this.targets.setImmutable();
    super.setImmutable();
  }
  
  public int size() {
    return this.size;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\code\SwitchList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */