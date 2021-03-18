package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

public final class AttEnclosingMethod extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "EnclosingMethod";
  
  private final CstNat method;
  
  private final CstType type;
  
  public AttEnclosingMethod(CstType paramCstType, CstNat paramCstNat) {
    super("EnclosingMethod");
    if (paramCstType != null) {
      this.type = paramCstType;
      this.method = paramCstNat;
      return;
    } 
    throw new NullPointerException("type == null");
  }
  
  public int byteLength() {
    return 10;
  }
  
  public CstType getEnclosingClass() {
    return this.type;
  }
  
  public CstNat getMethod() {
    return this.method;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttEnclosingMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */