package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public abstract class FixedSizeInsn extends DalvInsn {
  public FixedSizeInsn(Dop paramDop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList) {
    super(paramDop, paramSourcePosition, paramRegisterSpecList);
  }
  
  public final int codeSize() {
    return getOpcode().getFormat().codeSize();
  }
  
  protected final String listingString0(boolean paramBoolean) {
    return getOpcode().getFormat().listingString(this, paramBoolean);
  }
  
  public final DalvInsn withRegisterOffset(int paramInt) {
    return withRegisters(getRegisters().withOffset(paramInt));
  }
  
  public final void writeTo(AnnotatedOutput paramAnnotatedOutput) {
    getOpcode().getFormat().writeTo(paramAnnotatedOutput, this);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\FixedSizeInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */