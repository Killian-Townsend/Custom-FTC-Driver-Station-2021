package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import org.firstinspires.ftc.robotcore.internal.android.dx.io.IndexType;

public final class ZeroRegisterDecodedInstruction extends DecodedInstruction {
  public ZeroRegisterDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt1, int paramInt2, IndexType paramIndexType, int paramInt3, long paramLong) {
    super(paramInstructionCodec, paramInt1, paramInt2, paramIndexType, paramInt3, paramLong);
  }
  
  public int getRegisterCount() {
    return 0;
  }
  
  public DecodedInstruction withIndex(int paramInt) {
    return new ZeroRegisterDecodedInstruction(getFormat(), getOpcode(), paramInt, getIndexType(), getTarget(), getLiteral());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\ZeroRegisterDecodedInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */