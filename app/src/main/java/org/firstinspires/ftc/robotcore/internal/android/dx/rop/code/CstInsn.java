package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;

public abstract class CstInsn extends Insn {
  private final Constant cst;
  
  public CstInsn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList, Constant paramConstant) {
    super(paramRop, paramSourcePosition, paramRegisterSpec, paramRegisterSpecList);
    if (paramConstant != null) {
      this.cst = paramConstant;
      return;
    } 
    throw new NullPointerException("cst == null");
  }
  
  public boolean contentEquals(Insn paramInsn) {
    return (super.contentEquals(paramInsn) && this.cst.equals(((CstInsn)paramInsn).getConstant()));
  }
  
  public Constant getConstant() {
    return this.cst;
  }
  
  public String getInlineString() {
    return this.cst.toHuman();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\CstInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */