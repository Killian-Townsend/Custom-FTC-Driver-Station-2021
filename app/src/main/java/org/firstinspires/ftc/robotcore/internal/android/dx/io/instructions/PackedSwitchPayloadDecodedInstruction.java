package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

public final class PackedSwitchPayloadDecodedInstruction extends DecodedInstruction {
  private final int firstKey;
  
  private final int[] targets;
  
  public PackedSwitchPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt1, int paramInt2, int[] paramArrayOfint) {
    super(paramInstructionCodec, paramInt1, 0, null, 0, 0L);
    this.firstKey = paramInt2;
    this.targets = paramArrayOfint;
  }
  
  public int getFirstKey() {
    return this.firstKey;
  }
  
  public int getRegisterCount() {
    return 0;
  }
  
  public int[] getTargets() {
    return this.targets;
  }
  
  public DecodedInstruction withIndex(int paramInt) {
    throw new UnsupportedOperationException("no index in instruction");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\PackedSwitchPayloadDecodedInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */