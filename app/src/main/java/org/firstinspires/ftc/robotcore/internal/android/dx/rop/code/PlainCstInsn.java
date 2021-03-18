package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public final class PlainCstInsn extends CstInsn {
  public PlainCstInsn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList, Constant paramConstant) {
    super(paramRop, paramSourcePosition, paramRegisterSpec, paramRegisterSpecList, paramConstant);
    if (paramRop.getBranchingness() == 1)
      return; 
    throw new IllegalArgumentException("bogus branchingness");
  }
  
  public void accept(Insn.Visitor paramVisitor) {
    paramVisitor.visitPlainCstInsn(this);
  }
  
  public TypeList getCatches() {
    return (TypeList)StdTypeList.EMPTY;
  }
  
  public Insn withAddedCatch(Type paramType) {
    throw new UnsupportedOperationException("unsupported");
  }
  
  public Insn withNewRegisters(RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList) {
    return new PlainCstInsn(getOpcode(), getPosition(), paramRegisterSpec, paramRegisterSpecList, getConstant());
  }
  
  public Insn withRegisterOffset(int paramInt) {
    return new PlainCstInsn(getOpcode(), getPosition(), getResult().withOffset(paramInt), getSources().withOffset(paramInt), getConstant());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\PlainCstInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */