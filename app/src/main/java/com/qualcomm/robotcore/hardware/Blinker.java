package com.qualcomm.robotcore.hardware;

import android.graphics.Color;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public interface Blinker {
  int getBlinkerPatternMaxLength();
  
  Collection<Step> getPattern();
  
  boolean patternStackNotEmpty();
  
  boolean popPattern();
  
  void pushPattern(Collection<Step> paramCollection);
  
  void setConstant(int paramInt);
  
  void setPattern(Collection<Step> paramCollection);
  
  void stopBlinking();
  
  public static class Step {
    protected int color = 0;
    
    protected int msDuration = 0;
    
    public Step() {}
    
    public Step(int param1Int, long param1Long, TimeUnit param1TimeUnit) {
      this.color = param1Int & 0xFFFFFF;
      setDuration(param1Long, param1TimeUnit);
    }
    
    public static Step nullStep() {
      return new Step();
    }
    
    public boolean equals(Step param1Step) {
      return (this.color == param1Step.color && this.msDuration == param1Step.msDuration);
    }
    
    public boolean equals(Object param1Object) {
      return (param1Object instanceof Step) ? equals((Step)param1Object) : false;
    }
    
    public int getColor() {
      return this.color;
    }
    
    public int getDurationMs() {
      return this.msDuration;
    }
    
    public int hashCode() {
      return (this.color << 5 | this.msDuration) ^ 0x2EDA;
    }
    
    public boolean isLit() {
      return (Color.red(this.color) != 0 || Color.green(this.color) != 0 || Color.blue(this.color) != 0);
    }
    
    public void setColor(int param1Int) {
      this.color = param1Int;
    }
    
    public void setDuration(long param1Long, TimeUnit param1TimeUnit) {
      this.msDuration = (int)param1TimeUnit.toMillis(param1Long);
    }
    
    public void setLit(boolean param1Boolean) {
      int i;
      if (param1Boolean) {
        i = -1;
      } else {
        i = -16777216;
      } 
      setColor(i);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\Blinker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */