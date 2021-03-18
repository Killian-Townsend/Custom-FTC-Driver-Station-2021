package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class CstChar extends CstLiteral32 {
  public static final CstChar VALUE_0 = make(false);
  
  private CstChar(char paramChar) {
    super(paramChar);
  }
  
  public static CstChar make(char paramChar) {
    return new CstChar(paramChar);
  }
  
  public static CstChar make(int paramInt) {
    char c = (char)paramInt;
    if (c == paramInt)
      return make(c); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bogus char value: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public Type getType() {
    return Type.CHAR;
  }
  
  public char getValue() {
    return (char)getIntBits();
  }
  
  public String toHuman() {
    return Integer.toString(getIntBits());
  }
  
  public String toString() {
    int i = getIntBits();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("char{0x");
    stringBuilder.append(Hex.u2(i));
    stringBuilder.append(" / ");
    stringBuilder.append(i);
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public String typeName() {
    return "char";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstChar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */