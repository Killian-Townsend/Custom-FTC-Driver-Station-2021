package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteral64;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteralBits;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public abstract class InsnFormat {
  public static boolean ALLOW_EXTENDED_OPCODES = true;
  
  protected static int argIndex(DalvInsn paramDalvInsn) {
    int i = ((CstInteger)((CstInsn)paramDalvInsn).getConstant()).getValue();
    if (i >= 0)
      return i; 
    throw new IllegalArgumentException("bogus insn");
  }
  
  protected static String branchComment(DalvInsn paramDalvInsn) {
    int i = ((TargetInsn)paramDalvInsn).getTargetOffset();
    return (i == (short)i) ? Hex.s2(i) : Hex.s4(i);
  }
  
  protected static String branchString(DalvInsn paramDalvInsn) {
    int i = ((TargetInsn)paramDalvInsn).getTargetAddress();
    return (i == (char)i) ? Hex.u2(i) : Hex.u4(i);
  }
  
  protected static short codeUnit(int paramInt1, int paramInt2) {
    if ((paramInt1 & 0xFF) == paramInt1) {
      if ((paramInt2 & 0xFF) == paramInt2)
        return (short)(paramInt1 | paramInt2 << 8); 
      throw new IllegalArgumentException("high out of range 0..255");
    } 
    throw new IllegalArgumentException("low out of range 0..255");
  }
  
  protected static short codeUnit(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if ((paramInt1 & 0xF) == paramInt1) {
      if ((paramInt2 & 0xF) == paramInt2) {
        if ((paramInt3 & 0xF) == paramInt3) {
          if ((paramInt4 & 0xF) == paramInt4)
            return (short)(paramInt1 | paramInt2 << 4 | paramInt3 << 8 | paramInt4 << 12); 
          throw new IllegalArgumentException("n3 out of range 0..15");
        } 
        throw new IllegalArgumentException("n2 out of range 0..15");
      } 
      throw new IllegalArgumentException("n1 out of range 0..15");
    } 
    throw new IllegalArgumentException("n0 out of range 0..15");
  }
  
  protected static String cstComment(DalvInsn paramDalvInsn) {
    paramDalvInsn = paramDalvInsn;
    if (!paramDalvInsn.hasIndex())
      return ""; 
    StringBuilder stringBuilder = new StringBuilder(20);
    int i = paramDalvInsn.getIndex();
    stringBuilder.append(paramDalvInsn.getConstant().typeName());
    stringBuilder.append('@');
    if (i < 65536) {
      stringBuilder.append(Hex.u2(i));
    } else {
      stringBuilder.append(Hex.u4(i));
    } 
    return stringBuilder.toString();
  }
  
  protected static String cstString(DalvInsn paramDalvInsn) {
    Constant constant = ((CstInsn)paramDalvInsn).getConstant();
    return (constant instanceof CstString) ? ((CstString)constant).toQuoted() : constant.toHuman();
  }
  
  protected static boolean isRegListSequential(RegisterSpecList paramRegisterSpecList) {
    int k = paramRegisterSpecList.size();
    if (k < 2)
      return true; 
    int j = paramRegisterSpecList.get(0).getReg();
    for (int i = 0; i < k; i++) {
      RegisterSpec registerSpec = paramRegisterSpecList.get(i);
      if (registerSpec.getReg() != j)
        return false; 
      j += registerSpec.getCategory();
    } 
    return true;
  }
  
  protected static String literalBitsComment(CstLiteralBits paramCstLiteralBits, int paramInt) {
    long l;
    StringBuffer stringBuffer = new StringBuffer(20);
    stringBuffer.append("#");
    if (paramCstLiteralBits instanceof CstLiteral64) {
      l = ((CstLiteral64)paramCstLiteralBits).getLongBits();
    } else {
      l = paramCstLiteralBits.getIntBits();
    } 
    if (paramInt != 4) {
      if (paramInt != 8) {
        if (paramInt != 16) {
          if (paramInt != 32) {
            if (paramInt == 64) {
              stringBuffer.append(Hex.u8(l));
            } else {
              throw new RuntimeException("shouldn't happen");
            } 
          } else {
            stringBuffer.append(Hex.u4((int)l));
          } 
        } else {
          stringBuffer.append(Hex.u2((int)l));
        } 
      } else {
        stringBuffer.append(Hex.u1((int)l));
      } 
    } else {
      stringBuffer.append(Hex.uNibble((int)l));
    } 
    return stringBuffer.toString();
  }
  
  protected static String literalBitsString(CstLiteralBits paramCstLiteralBits) {
    StringBuffer stringBuffer = new StringBuffer(100);
    stringBuffer.append('#');
    if (paramCstLiteralBits instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstKnownNull) {
      stringBuffer.append("null");
    } else {
      stringBuffer.append(paramCstLiteralBits.typeName());
      stringBuffer.append(' ');
      stringBuffer.append(paramCstLiteralBits.toHuman());
    } 
    return stringBuffer.toString();
  }
  
  protected static int makeByte(int paramInt1, int paramInt2) {
    if ((paramInt1 & 0xF) == paramInt1) {
      if ((paramInt2 & 0xF) == paramInt2)
        return paramInt1 | paramInt2 << 4; 
      throw new IllegalArgumentException("high out of range 0..15");
    } 
    throw new IllegalArgumentException("low out of range 0..15");
  }
  
  protected static short opcodeUnit(DalvInsn paramDalvInsn) {
    int i = paramDalvInsn.getOpcode().getOpcode();
    if (i >= 256 && i <= 65535)
      return (short)i; 
    throw new IllegalArgumentException("opcode out of range 0..65535");
  }
  
  protected static short opcodeUnit(DalvInsn paramDalvInsn, int paramInt) {
    if ((paramInt & 0xFF) == paramInt) {
      int i = paramDalvInsn.getOpcode().getOpcode();
      if ((i & 0xFF) == i)
        return (short)(i | paramInt << 8); 
      throw new IllegalArgumentException("opcode out of range 0..255");
    } 
    throw new IllegalArgumentException("arg out of range 0..255");
  }
  
  protected static String regListString(RegisterSpecList paramRegisterSpecList) {
    int j = paramRegisterSpecList.size();
    StringBuffer stringBuffer = new StringBuffer(j * 5 + 2);
    stringBuffer.append('{');
    for (int i = 0; i < j; i++) {
      if (i != 0)
        stringBuffer.append(", "); 
      stringBuffer.append(paramRegisterSpecList.get(i).regString());
    } 
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
  
  protected static String regRangeString(RegisterSpecList paramRegisterSpecList) {
    int i = paramRegisterSpecList.size();
    StringBuilder stringBuilder = new StringBuilder(30);
    stringBuilder.append("{");
    if (i != 0)
      if (i != 1) {
        RegisterSpec registerSpec2 = paramRegisterSpecList.get(i - 1);
        RegisterSpec registerSpec1 = registerSpec2;
        if (registerSpec2.getCategory() == 2)
          registerSpec1 = registerSpec2.withOffset(1); 
        stringBuilder.append(paramRegisterSpecList.get(0).regString());
        stringBuilder.append("..");
        stringBuilder.append(registerSpec1.regString());
      } else {
        stringBuilder.append(paramRegisterSpecList.get(0).regString());
      }  
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  protected static boolean signedFitsInByte(int paramInt) {
    return ((byte)paramInt == paramInt);
  }
  
  protected static boolean signedFitsInNibble(int paramInt) {
    return (paramInt >= -8 && paramInt <= 7);
  }
  
  protected static boolean signedFitsInShort(int paramInt) {
    return ((short)paramInt == paramInt);
  }
  
  protected static boolean unsignedFitsInByte(int paramInt) {
    return (paramInt == (paramInt & 0xFF));
  }
  
  protected static boolean unsignedFitsInNibble(int paramInt) {
    return (paramInt == (paramInt & 0xF));
  }
  
  protected static boolean unsignedFitsInShort(int paramInt) {
    return (paramInt == (0xFFFF & paramInt));
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort) {
    paramAnnotatedOutput.writeShort(paramShort);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort, int paramInt) {
    write(paramAnnotatedOutput, paramShort, (short)paramInt, (short)(paramInt >> 16));
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, int paramInt, short paramShort2) {
    write(paramAnnotatedOutput, paramShort1, (short)paramInt, (short)(paramInt >> 16), paramShort2);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, int paramInt, short paramShort2, short paramShort3) {
    write(paramAnnotatedOutput, paramShort1, (short)paramInt, (short)(paramInt >> 16), paramShort2, paramShort3);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort, long paramLong) {
    write(paramAnnotatedOutput, paramShort, (short)(int)paramLong, (short)(int)(paramLong >> 16L), (short)(int)(paramLong >> 32L), (short)(int)(paramLong >> 48L));
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, short paramShort2) {
    paramAnnotatedOutput.writeShort(paramShort1);
    paramAnnotatedOutput.writeShort(paramShort2);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, short paramShort2, short paramShort3) {
    paramAnnotatedOutput.writeShort(paramShort1);
    paramAnnotatedOutput.writeShort(paramShort2);
    paramAnnotatedOutput.writeShort(paramShort3);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, short paramShort2, short paramShort3, short paramShort4) {
    paramAnnotatedOutput.writeShort(paramShort1);
    paramAnnotatedOutput.writeShort(paramShort2);
    paramAnnotatedOutput.writeShort(paramShort3);
    paramAnnotatedOutput.writeShort(paramShort4);
  }
  
  protected static void write(AnnotatedOutput paramAnnotatedOutput, short paramShort1, short paramShort2, short paramShort3, short paramShort4, short paramShort5) {
    paramAnnotatedOutput.writeShort(paramShort1);
    paramAnnotatedOutput.writeShort(paramShort2);
    paramAnnotatedOutput.writeShort(paramShort3);
    paramAnnotatedOutput.writeShort(paramShort4);
    paramAnnotatedOutput.writeShort(paramShort5);
  }
  
  public boolean branchFits(TargetInsn paramTargetInsn) {
    return false;
  }
  
  public abstract int codeSize();
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    return new BitSet();
  }
  
  public abstract String insnArgString(DalvInsn paramDalvInsn);
  
  public abstract String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean);
  
  public abstract boolean isCompatible(DalvInsn paramDalvInsn);
  
  public final String listingString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    String str2 = paramDalvInsn.getOpcode().getName();
    String str3 = insnArgString(paramDalvInsn);
    String str1 = insnCommentString(paramDalvInsn, paramBoolean);
    StringBuilder stringBuilder = new StringBuilder(100);
    stringBuilder.append(str2);
    if (str3.length() != 0) {
      stringBuilder.append(' ');
      stringBuilder.append(str3);
    } 
    if (str1.length() != 0) {
      stringBuilder.append(" // ");
      stringBuilder.append(str1);
    } 
    return stringBuilder.toString();
  }
  
  public abstract void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\InsnFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */