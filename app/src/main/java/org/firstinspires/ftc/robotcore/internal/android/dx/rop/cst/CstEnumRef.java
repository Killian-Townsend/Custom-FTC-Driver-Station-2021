package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class CstEnumRef extends CstMemberRef {
  private CstFieldRef fieldRef = null;
  
  public CstEnumRef(CstNat paramCstNat) {
    super(new CstType(paramCstNat.getFieldType()), paramCstNat);
  }
  
  public CstFieldRef getFieldRef() {
    if (this.fieldRef == null)
      this.fieldRef = new CstFieldRef(getDefiningClass(), getNat()); 
    return this.fieldRef;
  }
  
  public Type getType() {
    return getDefiningClass().getClassType();
  }
  
  public String typeName() {
    return "enum";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstEnumRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */