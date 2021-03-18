package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteralBits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form21h extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form21h();
  
  public int codeSize() {
    return 2;
  }
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    BitSet bitSet = new BitSet(1);
    bitSet.set(0, unsignedFitsInByte(registerSpecList.get(0).getReg()));
    return bitSet;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)paramDalvInsn).getConstant();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(registerSpecList.get(0).regString());
    stringBuilder.append(", ");
    stringBuilder.append(literalBitsString(cstLiteralBits));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    byte b;
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)paramDalvInsn).getConstant();
    if (registerSpecList.get(0).getCategory() == 1) {
      b = 32;
    } else {
      b = 64;
    } 
    return literalBitsComment(cstLiteralBits, b);
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    boolean bool = paramDalvInsn instanceof CstInsn;
    boolean bool3 = false;
    boolean bool2 = false;
    boolean bool1 = bool3;
    if (bool) {
      bool1 = bool3;
      if (registerSpecList.size() == 1) {
        if (!unsignedFitsInByte(registerSpecList.get(0).getReg()))
          return false; 
        Constant constant = ((CstInsn)paramDalvInsn).getConstant();
        if (!(constant instanceof CstLiteralBits))
          return false; 
        CstLiteralBits cstLiteralBits = (CstLiteralBits)constant;
        if (registerSpecList.get(0).getCategory() == 1) {
          bool1 = bool2;
          if ((cstLiteralBits.getIntBits() & 0xFFFF) == 0)
            bool1 = true; 
          return bool1;
        } 
        bool1 = bool3;
        if ((cstLiteralBits.getLongBits() & 0xFFFFFFFFFFFFL) == 0L)
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    int i;
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    CstLiteralBits cstLiteralBits = (CstLiteralBits)((CstInsn)paramDalvInsn).getConstant();
    if (registerSpecList.get(0).getCategory() == 1) {
      i = cstLiteralBits.getIntBits() >>> 16;
    } else {
      i = (int)(cstLiteralBits.getLongBits() >>> 48L);
    } 
    short s = (short)i;
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, registerSpecList.get(0).getReg()), s);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form21h.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */