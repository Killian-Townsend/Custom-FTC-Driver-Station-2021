package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public final class ThrowingInsn extends Insn {
  private final TypeList catches;
  
  public ThrowingInsn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList, TypeList paramTypeList) {
    super(paramRop, paramSourcePosition, null, paramRegisterSpecList);
    if (paramRop.getBranchingness() == 6) {
      if (paramTypeList != null) {
        this.catches = paramTypeList;
        return;
      } 
      throw new NullPointerException("catches == null");
    } 
    throw new IllegalArgumentException("bogus branchingness");
  }
  
  public static String toCatchString(TypeList paramTypeList) {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append("catch");
    int j = paramTypeList.size();
    for (int i = 0; i < j; i++) {
      stringBuffer.append(" ");
      stringBuffer.append(paramTypeList.getType(i).toHuman());
    } 
    return stringBuffer.toString();
  }
  
  public void accept(Insn.Visitor paramVisitor) {
    paramVisitor.visitThrowingInsn(this);
  }
  
  public TypeList getCatches() {
    return this.catches;
  }
  
  public String getInlineString() {
    return toCatchString(this.catches);
  }
  
  public Insn withAddedCatch(Type paramType) {
    return new ThrowingInsn(getOpcode(), getPosition(), getSources(), this.catches.withAddedType(paramType));
  }
  
  public Insn withNewRegisters(RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList) {
    return new ThrowingInsn(getOpcode(), getPosition(), paramRegisterSpecList, this.catches);
  }
  
  public Insn withRegisterOffset(int paramInt) {
    return new ThrowingInsn(getOpcode(), getPosition(), getSources().withOffset(paramInt), this.catches);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\ThrowingInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */