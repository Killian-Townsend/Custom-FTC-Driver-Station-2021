package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;

public final class FillArrayDataInsn extends Insn {
  private final Constant arrayType;
  
  private final ArrayList<Constant> initValues;
  
  public FillArrayDataInsn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList, ArrayList<Constant> paramArrayList, Constant paramConstant) {
    super(paramRop, paramSourcePosition, null, paramRegisterSpecList);
    if (paramRop.getBranchingness() == 1) {
      this.initValues = paramArrayList;
      this.arrayType = paramConstant;
      return;
    } 
    throw new IllegalArgumentException("bogus branchingness");
  }
  
  public void accept(Insn.Visitor paramVisitor) {
    paramVisitor.visitFillArrayDataInsn(this);
  }
  
  public TypeList getCatches() {
    return (TypeList)StdTypeList.EMPTY;
  }
  
  public Constant getConstant() {
    return this.arrayType;
  }
  
  public ArrayList<Constant> getInitValues() {
    return this.initValues;
  }
  
  public Insn withAddedCatch(Type paramType) {
    throw new UnsupportedOperationException("unsupported");
  }
  
  public Insn withNewRegisters(RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList) {
    return new FillArrayDataInsn(getOpcode(), getPosition(), paramRegisterSpecList, this.initValues, this.arrayType);
  }
  
  public Insn withRegisterOffset(int paramInt) {
    return new FillArrayDataInsn(getOpcode(), getPosition(), getSources().withOffset(paramInt), this.initValues, this.arrayType);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\FillArrayDataInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */