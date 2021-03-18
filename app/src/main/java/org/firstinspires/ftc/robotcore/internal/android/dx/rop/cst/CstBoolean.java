package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class CstBoolean extends CstLiteral32 {
  public static final CstBoolean VALUE_FALSE = new CstBoolean(false);
  
  public static final CstBoolean VALUE_TRUE = new CstBoolean(true);
  
  private CstBoolean(boolean paramBoolean) {}
  
  public static CstBoolean make(int paramInt) {
    if (paramInt == 0)
      return VALUE_FALSE; 
    if (paramInt == 1)
      return VALUE_TRUE; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("bogus value: ");
    stringBuilder.append(paramInt);
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public static CstBoolean make(boolean paramBoolean) {
    return paramBoolean ? VALUE_TRUE : VALUE_FALSE;
  }
  
  public Type getType() {
    return Type.BOOLEAN;
  }
  
  public boolean getValue() {
    return !(getIntBits() == 0);
  }
  
  public String toHuman() {
    return getValue() ? "true" : "false";
  }
  
  public String toString() {
    return getValue() ? "boolean{true}" : "boolean{false}";
  }
  
  public String typeName() {
    return "boolean";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstBoolean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */