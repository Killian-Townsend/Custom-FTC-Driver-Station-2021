package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

public abstract class CstLiteral32 extends CstLiteralBits {
  private final int bits;
  
  CstLiteral32(int paramInt) {
    this.bits = paramInt;
  }
  
  protected int compareTo0(Constant paramConstant) {
    int i = ((CstLiteral32)paramConstant).bits;
    int j = this.bits;
    return (j < i) ? -1 : ((j > i) ? 1 : 0);
  }
  
  public final boolean equals(Object paramObject) {
    return (paramObject != null && getClass() == paramObject.getClass() && this.bits == ((CstLiteral32)paramObject).bits);
  }
  
  public final boolean fitsInInt() {
    return true;
  }
  
  public final int getIntBits() {
    return this.bits;
  }
  
  public final long getLongBits() {
    return this.bits;
  }
  
  public final int hashCode() {
    return this.bits;
  }
  
  public final boolean isCategory2() {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstLiteral32.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */