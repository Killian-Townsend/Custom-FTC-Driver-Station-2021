package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

public abstract class CstLiteral64 extends CstLiteralBits {
  private final long bits;
  
  CstLiteral64(long paramLong) {
    this.bits = paramLong;
  }
  
  protected int compareTo0(Constant paramConstant) {
    long l1 = ((CstLiteral64)paramConstant).bits;
    long l2 = this.bits;
    return (l2 < l1) ? -1 : ((l2 > l1) ? 1 : 0);
  }
  
  public final boolean equals(Object paramObject) {
    return (paramObject != null && getClass() == paramObject.getClass() && this.bits == ((CstLiteral64)paramObject).bits);
  }
  
  public final boolean fitsInInt() {
    long l = this.bits;
    return ((int)l == l);
  }
  
  public final int getIntBits() {
    return (int)this.bits;
  }
  
  public final long getLongBits() {
    return this.bits;
  }
  
  public final int hashCode() {
    long l = this.bits;
    int i = (int)l;
    return (int)(l >> 32L) ^ i;
  }
  
  public final boolean isCategory2() {
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstLiteral64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */