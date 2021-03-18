package org.firstinspires.ftc.robotcore.internal.system;

public abstract class MemberwiseCloneable<T> implements Cloneable {
  protected T memberwiseClone() {
    try {
      return (T)clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw AppUtil.getInstance().unreachable();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\MemberwiseCloneable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */