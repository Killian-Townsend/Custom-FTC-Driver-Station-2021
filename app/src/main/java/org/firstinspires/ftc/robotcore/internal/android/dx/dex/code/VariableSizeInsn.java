package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;

public abstract class VariableSizeInsn extends DalvInsn {
  public VariableSizeInsn(SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList) {
    super(Dops.SPECIAL_FORMAT, paramSourcePosition, paramRegisterSpecList);
  }
  
  public final DalvInsn withOpcode(Dop paramDop) {
    throw new RuntimeException("unsupported");
  }
  
  public final DalvInsn withRegisterOffset(int paramInt) {
    return withRegisters(getRegisters().withOffset(paramInt));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\VariableSizeInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */