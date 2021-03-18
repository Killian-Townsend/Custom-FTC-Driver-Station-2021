package com.qualcomm.robotcore.util;

public class LastKnown<T> {
  protected boolean isValid = false;
  
  protected double msFreshness;
  
  protected ElapsedTime timer = new ElapsedTime();
  
  protected T value = null;
  
  public LastKnown() {
    this(500.0D);
  }
  
  public LastKnown(double paramDouble) {
    this.msFreshness = paramDouble;
  }
  
  public static <X> LastKnown<X>[] createArray(int paramInt) {
    LastKnown[] arrayOfLastKnown = new LastKnown[paramInt];
    for (int i = 0; i < paramInt; i++)
      arrayOfLastKnown[i] = new LastKnown(); 
    return (LastKnown<X>[])arrayOfLastKnown;
  }
  
  public static <X> void invalidateArray(LastKnown<X>[] paramArrayOfLastKnown) {
    for (int i = 0; i < paramArrayOfLastKnown.length; i++)
      paramArrayOfLastKnown[i].invalidate(); 
  }
  
  public T getNonTimedValue() {
    return this.isValid ? this.value : null;
  }
  
  public T getRawValue() {
    return this.value;
  }
  
  public T getValue() {
    return isValid() ? this.value : null;
  }
  
  public void invalidate() {
    this.isValid = false;
  }
  
  public boolean isValid() {
    return (this.isValid && this.timer.milliseconds() <= this.msFreshness);
  }
  
  public boolean isValue(T paramT) {
    return isValid() ? this.value.equals(paramT) : false;
  }
  
  public T setValue(T paramT) {
    T t = this.value;
    this.value = paramT;
    this.isValid = true;
    if (paramT == null) {
      invalidate();
      return t;
    } 
    this.timer.reset();
    return t;
  }
  
  public boolean updateValue(T paramT) {
    if (!isValue(paramT)) {
      setValue(paramT);
      return true;
    } 
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\LastKnown.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */