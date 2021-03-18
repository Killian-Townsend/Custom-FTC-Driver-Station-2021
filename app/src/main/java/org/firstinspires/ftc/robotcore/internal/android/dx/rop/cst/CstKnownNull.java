package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class CstKnownNull extends CstLiteralBits {
  public static final CstKnownNull THE_ONE = new CstKnownNull();
  
  protected int compareTo0(Constant paramConstant) {
    return 0;
  }
  
  public boolean equals(Object paramObject) {
    return paramObject instanceof CstKnownNull;
  }
  
  public boolean fitsInInt() {
    return true;
  }
  
  public int getIntBits() {
    return 0;
  }
  
  public long getLongBits() {
    return 0L;
  }
  
  public Type getType() {
    return Type.KNOWN_NULL;
  }
  
  public int hashCode() {
    return 1147565434;
  }
  
  public boolean isCategory2() {
    return false;
  }
  
  public String toHuman() {
    return "null";
  }
  
  public String toString() {
    return "known-null";
  }
  
  public String typeName() {
    return "known-null";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstKnownNull.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */