package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public final class ThrowingCstInsn extends CstInsn {
  private final TypeList catches;
  
  public ThrowingCstInsn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList, TypeList paramTypeList, Constant paramConstant) {
    super(paramRop, paramSourcePosition, (RegisterSpec)null, paramRegisterSpecList, paramConstant);
    if (paramRop.getBranchingness() == 6) {
      if (paramTypeList != null) {
        this.catches = paramTypeList;
        return;
      } 
      throw new NullPointerException("catches == null");
    } 
    throw new IllegalArgumentException("bogus branchingness");
  }
  
  public void accept(Insn.Visitor paramVisitor) {
    paramVisitor.visitThrowingCstInsn(this);
  }
  
  public TypeList getCatches() {
    return this.catches;
  }
  
  public String getInlineString() {
    Constant constant = getConstant();
    String str = constant.toHuman();
    if (constant instanceof CstString)
      str = ((CstString)constant).toQuoted(); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(str);
    stringBuilder.append(" ");
    stringBuilder.append(ThrowingInsn.toCatchString(this.catches));
    return stringBuilder.toString();
  }
  
  public Insn withAddedCatch(Type paramType) {
    return new ThrowingCstInsn(getOpcode(), getPosition(), getSources(), this.catches.withAddedType(paramType), getConstant());
  }
  
  public Insn withNewRegisters(RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList) {
    return new ThrowingCstInsn(getOpcode(), getPosition(), paramRegisterSpecList, this.catches, getConstant());
  }
  
  public Insn withRegisterOffset(int paramInt) {
    return new ThrowingCstInsn(getOpcode(), getPosition(), getSources().withOffset(paramInt), this.catches, getConstant());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\ThrowingCstInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */