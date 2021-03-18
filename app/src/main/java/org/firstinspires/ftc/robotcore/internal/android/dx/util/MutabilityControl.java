package org.firstinspires.ftc.robotcore.internal.android.dx.util;

public class MutabilityControl {
  private boolean mutable = true;
  
  public MutabilityControl() {}
  
  public MutabilityControl(boolean paramBoolean) {}
  
  public final boolean isImmutable() {
    return this.mutable ^ true;
  }
  
  public final boolean isMutable() {
    return this.mutable;
  }
  
  public void setImmutable() {
    this.mutable = false;
  }
  
  public final void throwIfImmutable() {
    if (this.mutable)
      return; 
    throw new MutabilityException("immutable instance");
  }
  
  public final void throwIfMutable() {
    if (!this.mutable)
      return; 
    throw new MutabilityException("mutable instance");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\MutabilityControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */