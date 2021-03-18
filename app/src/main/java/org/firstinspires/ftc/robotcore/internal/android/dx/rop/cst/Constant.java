package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public abstract class Constant implements ToHuman, Comparable<Constant> {
  public final int compareTo(Constant paramConstant) {
    Class<?> clazz1 = getClass();
    Class<?> clazz2 = paramConstant.getClass();
    return (clazz1 != clazz2) ? clazz1.getName().compareTo(clazz2.getName()) : compareTo0(paramConstant);
  }
  
  protected abstract int compareTo0(Constant paramConstant);
  
  public abstract boolean isCategory2();
  
  public abstract String typeName();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\Constant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */