package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

public final class FillArrayDataPayloadDecodedInstruction extends DecodedInstruction {
  private final Object data;
  
  private final int elementWidth;
  
  private final int size;
  
  private FillArrayDataPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt1, Object paramObject, int paramInt2, int paramInt3) {
    super(paramInstructionCodec, paramInt1, 0, null, 0, 0L);
    this.data = paramObject;
    this.size = paramInt2;
    this.elementWidth = paramInt3;
  }
  
  public FillArrayDataPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt, byte[] paramArrayOfbyte) {
    this(paramInstructionCodec, paramInt, paramArrayOfbyte, paramArrayOfbyte.length, 1);
  }
  
  public FillArrayDataPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt, int[] paramArrayOfint) {
    this(paramInstructionCodec, paramInt, paramArrayOfint, paramArrayOfint.length, 4);
  }
  
  public FillArrayDataPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt, long[] paramArrayOflong) {
    this(paramInstructionCodec, paramInt, paramArrayOflong, paramArrayOflong.length, 8);
  }
  
  public FillArrayDataPayloadDecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt, short[] paramArrayOfshort) {
    this(paramInstructionCodec, paramInt, paramArrayOfshort, paramArrayOfshort.length, 2);
  }
  
  public Object getData() {
    return this.data;
  }
  
  public short getElementWidthUnit() {
    return (short)this.elementWidth;
  }
  
  public int getRegisterCount() {
    return 0;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public DecodedInstruction withIndex(int paramInt) {
    throw new UnsupportedOperationException("no index in instruction");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\FillArrayDataPayloadDecodedInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */