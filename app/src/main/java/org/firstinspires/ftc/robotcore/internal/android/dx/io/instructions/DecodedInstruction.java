package org.firstinspires.ftc.robotcore.internal.android.dx.io.instructions;

import java.io.EOFException;
import org.firstinspires.ftc.robotcore.internal.android.dex.DexException;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.IndexType;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.OpcodeInfo;
import org.firstinspires.ftc.robotcore.internal.android.dx.io.Opcodes;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public abstract class DecodedInstruction {
  private final InstructionCodec format;
  
  private final int index;
  
  private final IndexType indexType;
  
  private final long literal;
  
  private final int opcode;
  
  private final int target;
  
  public DecodedInstruction(InstructionCodec paramInstructionCodec, int paramInt1, int paramInt2, IndexType paramIndexType, int paramInt3, long paramLong) {
    if (paramInstructionCodec != null) {
      if (Opcodes.isValidShape(paramInt1)) {
        this.format = paramInstructionCodec;
        this.opcode = paramInt1;
        this.index = paramInt2;
        this.indexType = paramIndexType;
        this.target = paramInt3;
        this.literal = paramLong;
        return;
      } 
      throw new IllegalArgumentException("invalid opcode");
    } 
    throw new NullPointerException("format == null");
  }
  
  public static DecodedInstruction decode(CodeInput paramCodeInput) throws EOFException {
    int i = paramCodeInput.read();
    return OpcodeInfo.getFormat(Opcodes.extractOpcodeFromUnit(i)).decode(i, paramCodeInput);
  }
  
  public static DecodedInstruction[] decodeAll(short[] paramArrayOfshort) {
    DecodedInstruction[] arrayOfDecodedInstruction = new DecodedInstruction[paramArrayOfshort.length];
    ShortArrayCodeInput shortArrayCodeInput = new ShortArrayCodeInput(paramArrayOfshort);
    try {
      while (shortArrayCodeInput.hasMore())
        arrayOfDecodedInstruction[shortArrayCodeInput.cursor()] = decode(shortArrayCodeInput); 
      return arrayOfDecodedInstruction;
    } catch (EOFException eOFException) {
      throw new DexException(eOFException);
    } 
  }
  
  public final void encode(CodeOutput paramCodeOutput) {
    this.format.encode(this, paramCodeOutput);
  }
  
  public int getA() {
    return 0;
  }
  
  public final short getAByte() {
    int i = getA();
    if ((i & 0xFFFFFF00) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register A out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getANibble() {
    int i = getA();
    if ((i & 0xFFFFFFF0) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register A out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getAUnit() {
    int i = getA();
    if ((0xFFFF0000 & i) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register A out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public int getB() {
    return 0;
  }
  
  public final short getBByte() {
    int i = getB();
    if ((i & 0xFFFFFF00) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register B out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getBNibble() {
    int i = getB();
    if ((i & 0xFFFFFFF0) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register B out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getBUnit() {
    int i = getB();
    if ((0xFFFF0000 & i) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register B out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public int getC() {
    return 0;
  }
  
  public final short getCByte() {
    int i = getC();
    if ((i & 0xFFFFFF00) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register C out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getCNibble() {
    int i = getC();
    if ((i & 0xFFFFFFF0) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register C out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getCUnit() {
    int i = getC();
    if ((0xFFFF0000 & i) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register C out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public int getD() {
    return 0;
  }
  
  public final short getDByte() {
    int i = getD();
    if ((i & 0xFFFFFF00) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register D out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getDNibble() {
    int i = getD();
    if ((i & 0xFFFFFFF0) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register D out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getDUnit() {
    int i = getD();
    if ((0xFFFF0000 & i) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register D out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public int getE() {
    return 0;
  }
  
  public final short getENibble() {
    int i = getE();
    if ((i & 0xFFFFFFF0) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register E out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final InstructionCodec getFormat() {
    return this.format;
  }
  
  public final int getIndex() {
    return this.index;
  }
  
  public final IndexType getIndexType() {
    return this.indexType;
  }
  
  public final short getIndexUnit() {
    return (short)this.index;
  }
  
  public final long getLiteral() {
    return this.literal;
  }
  
  public final int getLiteralByte() {
    long l = this.literal;
    if (l == (byte)(int)l)
      return (int)l & 0xFF; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Literal out of range: ");
    stringBuilder.append(Hex.u8(this.literal));
    throw new DexException(stringBuilder.toString());
  }
  
  public final int getLiteralInt() {
    long l = this.literal;
    if (l == (int)l)
      return (int)l; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Literal out of range: ");
    stringBuilder.append(Hex.u8(this.literal));
    throw new DexException(stringBuilder.toString());
  }
  
  public final int getLiteralNibble() {
    long l = this.literal;
    if (l >= -8L && l <= 7L)
      return (int)l & 0xF; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Literal out of range: ");
    stringBuilder.append(Hex.u8(this.literal));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getLiteralUnit() {
    long l = this.literal;
    if (l == (short)(int)l)
      return (short)(int)l; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Literal out of range: ");
    stringBuilder.append(Hex.u8(this.literal));
    throw new DexException(stringBuilder.toString());
  }
  
  public final int getOpcode() {
    return this.opcode;
  }
  
  public final short getOpcodeUnit() {
    return (short)this.opcode;
  }
  
  public abstract int getRegisterCount();
  
  public final short getRegisterCountUnit() {
    int i = getRegisterCount();
    if ((0xFFFF0000 & i) == 0)
      return (short)i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Register count out of range: ");
    stringBuilder.append(Hex.u8(i));
    throw new DexException(stringBuilder.toString());
  }
  
  public final int getTarget() {
    return this.target;
  }
  
  public final int getTarget(int paramInt) {
    return this.target - paramInt;
  }
  
  public final int getTargetByte(int paramInt) {
    paramInt = getTarget(paramInt);
    if (paramInt == (byte)paramInt)
      return paramInt & 0xFF; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Target out of range: ");
    stringBuilder.append(Hex.s4(paramInt));
    throw new DexException(stringBuilder.toString());
  }
  
  public final short getTargetUnit(int paramInt) {
    paramInt = getTarget(paramInt);
    short s = (short)paramInt;
    if (paramInt == s)
      return s; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Target out of range: ");
    stringBuilder.append(Hex.s4(paramInt));
    throw new DexException(stringBuilder.toString());
  }
  
  public abstract DecodedInstruction withIndex(int paramInt);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\io\instructions\DecodedInstruction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */