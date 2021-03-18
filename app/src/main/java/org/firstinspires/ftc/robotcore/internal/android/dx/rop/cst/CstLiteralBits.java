package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

public abstract class CstLiteralBits extends TypedConstant {
  public boolean fitsIn16Bits() {
    boolean bool1 = fitsInInt();
    boolean bool = false;
    if (!bool1)
      return false; 
    int i = getIntBits();
    if ((short)i == i)
      bool = true; 
    return bool;
  }
  
  public boolean fitsIn8Bits() {
    boolean bool1 = fitsInInt();
    boolean bool = false;
    if (!bool1)
      return false; 
    int i = getIntBits();
    if ((byte)i == i)
      bool = true; 
    return bool;
  }
  
  public abstract boolean fitsInInt();
  
  public abstract int getIntBits();
  
  public abstract long getLongBits();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstLiteralBits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */