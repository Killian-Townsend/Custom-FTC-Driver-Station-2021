package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CstLong extends CstLiteral64 {
  public static final CstLong VALUE_0 = make(0L);
  
  public static final CstLong VALUE_1 = make(1L);
  
  private CstLong(long paramLong) {
    super(paramLong);
  }
  
  public static CstLong make(long paramLong) {
    return new CstLong(paramLong);
  }
  
  public Type getType() {
    return Type.LONG;
  }
  
  public long getValue() {
    return getLongBits();
  }
  
  public String toHuman() {
    return Long.toString(getLongBits());
  }
  
  public String toString() {
    long l = getLongBits();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("long{0x");
    stringBuilder.append(Hex.u8(l));
    stringBuilder.append(" / ");
    stringBuilder.append(l);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public String typeName() {
    return "long";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstLong.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */