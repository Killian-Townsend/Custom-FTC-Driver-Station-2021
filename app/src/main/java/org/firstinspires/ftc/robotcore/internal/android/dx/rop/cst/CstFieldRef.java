package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class CstFieldRef extends CstMemberRef {
  public CstFieldRef(CstType paramCstType, CstNat paramCstNat) {
    super(paramCstType, paramCstNat);
  }
  
  public static CstFieldRef forPrimitiveType(Type paramType) {
    return new CstFieldRef(CstType.forBoxedPrimitiveType(paramType), CstNat.PRIMITIVE_TYPE_NAT);
  }
  
  protected int compareTo0(Constant paramConstant) {
    int i = super.compareTo0(paramConstant);
    if (i != 0)
      return i; 
    paramConstant = paramConstant;
    return getNat().getDescriptor().compareTo(paramConstant.getNat().getDescriptor());
  }
  
  public Type getType() {
    return getNat().getFieldType();
  }
  
  public String typeName() {
    return "field";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstFieldRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */