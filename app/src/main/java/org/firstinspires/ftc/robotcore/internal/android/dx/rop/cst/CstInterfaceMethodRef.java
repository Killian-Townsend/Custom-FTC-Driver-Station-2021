package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

public final class CstInterfaceMethodRef extends CstBaseMethodRef {
  private CstMethodRef methodRef = null;
  
  public CstInterfaceMethodRef(CstType paramCstType, CstNat paramCstNat) {
    super(paramCstType, paramCstNat);
  }
  
  public CstMethodRef toMethodRef() {
    if (this.methodRef == null)
      this.methodRef = new CstMethodRef(getDefiningClass(), getNat()); 
    return this.methodRef;
  }
  
  public String typeName() {
    return "ifaceMethod";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstInterfaceMethodRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */