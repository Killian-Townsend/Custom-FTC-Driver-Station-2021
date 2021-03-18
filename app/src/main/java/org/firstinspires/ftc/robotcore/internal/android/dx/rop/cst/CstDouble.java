package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CstDouble extends CstLiteral64 {
  public static final CstDouble VALUE_0 = new CstDouble(Double.doubleToLongBits(0.0D));
  
  public static final CstDouble VALUE_1 = new CstDouble(Double.doubleToLongBits(1.0D));
  
  private CstDouble(long paramLong) {
    super(paramLong);
  }
  
  public static CstDouble make(long paramLong) {
    return new CstDouble(paramLong);
  }
  
  public Type getType() {
    return Type.DOUBLE;
  }
  
  public double getValue() {
    return Double.longBitsToDouble(getLongBits());
  }
  
  public String toHuman() {
    return Double.toString(Double.longBitsToDouble(getLongBits()));
  }
  
  public String toString() {
    long l = getLongBits();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("double{0x");
    stringBuilder.append(Hex.u8(l));
    stringBuilder.append(" / ");
    stringBuilder.append(Double.longBitsToDouble(l));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public String typeName() {
    return "double";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstDouble.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */