package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form22c extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form22c();
  
  public int codeSize() {
    return 2;
  }
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    BitSet bitSet = new BitSet(2);
    bitSet.set(0, unsignedFitsInNibble(registerSpecList.get(0).getReg()));
    bitSet.set(1, unsignedFitsInNibble(registerSpecList.get(1).getReg()));
    return bitSet;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(registerSpecList.get(0).regString());
    stringBuilder.append(", ");
    stringBuilder.append(registerSpecList.get(1).regString());
    stringBuilder.append(", ");
    stringBuilder.append(cstString(paramDalvInsn));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return paramBoolean ? cstComment(paramDalvInsn) : "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    boolean bool1 = paramDalvInsn instanceof CstInsn;
    boolean bool = false;
    null = bool;
    if (bool1) {
      null = bool;
      if (registerSpecList.size() == 2) {
        null = bool;
        if (unsignedFitsInNibble(registerSpecList.get(0).getReg())) {
          if (!unsignedFitsInNibble(registerSpecList.get(1).getReg()))
            return false; 
          CstInsn cstInsn = (CstInsn)paramDalvInsn;
          if (!unsignedFitsInShort(cstInsn.getIndex()))
            return false; 
          Constant constant = cstInsn.getConstant();
          if (!(constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType)) {
            null = bool;
            return (constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef) ? true : null;
          } 
        } else {
          return null;
        } 
      } else {
        return null;
      } 
    } else {
      return null;
    } 
    return true;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int i = ((CstInsn)paramDalvInsn).getIndex();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, makeByte(registerSpecList.get(0).getReg(), registerSpecList.get(1).getReg())), (short)i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form22c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */