package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;

public final class TargetInsn extends FixedSizeInsn {
  private CodeAddress target;
  
  public TargetInsn(Dop paramDop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList, CodeAddress paramCodeAddress) {
    super(paramDop, paramSourcePosition, paramRegisterSpecList);
    if (paramCodeAddress != null) {
      this.target = paramCodeAddress;
      return;
    } 
    throw new NullPointerException("target == null");
  }
  
  protected String argString() {
    CodeAddress codeAddress = this.target;
    return (codeAddress == null) ? "????" : codeAddress.identifierString();
  }
  
  public CodeAddress getTarget() {
    return this.target;
  }
  
  public int getTargetAddress() {
    return this.target.getAddress();
  }
  
  public int getTargetOffset() {
    return this.target.getAddress() - getAddress();
  }
  
  public boolean hasTargetOffset() {
    return (hasAddress() && this.target.hasAddress());
  }
  
  public TargetInsn withNewTargetAndReversed(CodeAddress paramCodeAddress) {
    return new TargetInsn(getOpcode().getOppositeTest(), getPosition(), getRegisters(), paramCodeAddress);
  }
  
  public DalvInsn withOpcode(Dop paramDop) {
    return new TargetInsn(paramDop, getPosition(), getRegisters(), this.target);
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new TargetInsn(getOpcode(), getPosition(), paramRegisterSpecList, this.target);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\TargetInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */