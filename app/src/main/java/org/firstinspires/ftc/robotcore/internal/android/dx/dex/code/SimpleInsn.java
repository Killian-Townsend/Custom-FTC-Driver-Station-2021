package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;

public final class SimpleInsn extends FixedSizeInsn {
  public SimpleInsn(Dop paramDop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList) {
    super(paramDop, paramSourcePosition, paramRegisterSpecList);
  }
  
  protected String argString() {
    return null;
  }
  
  public DalvInsn withOpcode(Dop paramDop) {
    return new SimpleInsn(paramDop, getPosition(), getRegisters());
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new SimpleInsn(getOpcode(), getPosition(), paramRegisterSpecList);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\SimpleInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */