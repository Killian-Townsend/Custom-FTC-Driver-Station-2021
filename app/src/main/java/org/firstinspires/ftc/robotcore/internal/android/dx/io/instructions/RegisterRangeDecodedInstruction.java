package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import org.firstinspires.ftc.robotcore.internal.android.dx.io.IndexType;

public final class RegisterRangeDecodedInstruction extends DecodedInstruction {
  private final int a;
  
  private final int registerCount;
  
  public RegisterRangeDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt1, int paramInt2, IndexType paramIndexType, int paramInt3, long paramLong, int paramInt4, int paramInt5) {
    super(paramInstructionCodec, paramInt1, paramInt2, paramIndexType, paramInt3, paramLong);
    this.a = paramInt4;
    this.registerCount = paramInt5;
  }
  
  public int getA() {
    return this.a;
  }
  
  public int getRegisterCount() {
    return this.registerCount;
  }
  
  public DecodedInstruction withIndex(int paramInt) {
    return new RegisterRangeDecodedInstruction(getFormat(), getOpcode(), paramInt, getIndexType(), getTarget(), getLiteral(), this.a, this.registerCount);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\RegisterRangeDecodedInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */