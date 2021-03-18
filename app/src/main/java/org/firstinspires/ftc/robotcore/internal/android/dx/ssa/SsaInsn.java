package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Insn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.LocalItem;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public abstract class SsaInsn implements ToHuman, Cloneable {
  private final SsaBasicBlock block;
  
  private RegisterSpec result;
  
  protected SsaInsn(RegisterSpec paramRegisterSpec, SsaBasicBlock paramSsaBasicBlock) {
    if (paramSsaBasicBlock != null) {
      this.block = paramSsaBasicBlock;
      this.result = paramRegisterSpec;
      return;
    } 
    throw new NullPointerException("block == null");
  }
  
  public static SsaInsn makeFromRop(Insn paramInsn, SsaBasicBlock paramSsaBasicBlock) {
    return new NormalSsaInsn(paramInsn, paramSsaBasicBlock);
  }
  
  public abstract void accept(Visitor paramVisitor);
  
  public abstract boolean canThrow();
  
  public void changeResultReg(int paramInt) {
    RegisterSpec registerSpec = this.result;
    if (registerSpec != null)
      this.result = registerSpec.withReg(paramInt); 
  }
  
  public SsaInsn clone() {
    try {
      return (SsaInsn)super.clone();
    } catch (CloneNotSupportedException cloneNotSupportedException) {
      throw new RuntimeException("unexpected", cloneNotSupportedException);
    } 
  }
  
  public SsaBasicBlock getBlock() {
    return this.block;
  }
  
  public RegisterSpec getLocalAssignment() {
    RegisterSpec registerSpec = this.result;
    return (registerSpec != null && registerSpec.getLocalItem() != null) ? this.result : null;
  }
  
  public abstract Rop getOpcode();
  
  public abstract Insn getOriginalRopInsn();
  
  public RegisterSpec getResult() {
    return this.result;
  }
  
  public abstract RegisterSpecList getSources();
  
  public abstract boolean hasSideEffect();
  
  public boolean isMoveException() {
    return false;
  }
  
  public boolean isNormalMoveInsn() {
    return false;
  }
  
  public abstract boolean isPhiOrMove();
  
  public boolean isRegASource(int paramInt) {
    return (getSources().specForRegister(paramInt) != null);
  }
  
  public boolean isResultReg(int paramInt) {
    RegisterSpec registerSpec = this.result;
    return (registerSpec != null && registerSpec.getReg() == paramInt);
  }
  
  public final void mapRegisters(RegisterMapper paramRegisterMapper) {
    RegisterSpec registerSpec = this.result;
    this.result = paramRegisterMapper.map(registerSpec);
    this.block.getParent().updateOneDefinition(this, registerSpec);
    mapSourceRegisters(paramRegisterMapper);
  }
  
  public abstract void mapSourceRegisters(RegisterMapper paramRegisterMapper);
  
  protected void setResult(RegisterSpec paramRegisterSpec) {
    if (paramRegisterSpec != null) {
      this.result = paramRegisterSpec;
      return;
    } 
    throw new NullPointerException("result == null");
  }
  
  public final void setResultLocal(LocalItem paramLocalItem) {
    if (paramLocalItem != this.result.getLocalItem() && (paramLocalItem == null || !paramLocalItem.equals(this.result.getLocalItem())))
      this.result = RegisterSpec.makeLocalOptional(this.result.getReg(), (TypeBearer)this.result.getType(), paramLocalItem); 
  }
  
  public abstract Insn toRopInsn();
  
  public static interface Visitor {
    void visitMoveInsn(NormalSsaInsn param1NormalSsaInsn);
    
    void visitNonMoveInsn(NormalSsaInsn param1NormalSsaInsn);
    
    void visitPhiInsn(PhiInsn param1PhiInsn);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\SsaInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */