package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public abstract class ZeroSizeInsn extends DalvInsn {
  public ZeroSizeInsn(SourcePosition paramSourcePosition) {
    super(Dops.SPECIAL_FORMAT, paramSourcePosition, RegisterSpecList.EMPTY);
  }
  
  public final int codeSize() {
    return 0;
  }
  
  public final DalvInsn withOpcode(Dop paramDop) {
    throw new RuntimeException("unsupported");
  }
  
  public DalvInsn withRegisterOffset(int paramInt) {
    return withRegisters(getRegisters().withOffset(paramInt));
  }
  
  public final void writeTo(AnnotatedOutput paramAnnotatedOutput) {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\ZeroSizeInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */