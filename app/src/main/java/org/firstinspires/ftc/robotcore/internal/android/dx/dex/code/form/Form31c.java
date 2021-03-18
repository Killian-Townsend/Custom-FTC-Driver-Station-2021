package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form31c extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form31c();
  
  public int codeSize() {
    return 3;
  }
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int i = registerSpecList.size();
    BitSet bitSet = new BitSet(i);
    boolean bool = unsignedFitsInByte(registerSpecList.get(0).getReg());
    if (i == 1) {
      bitSet.set(0, bool);
      return bitSet;
    } 
    if (registerSpecList.get(0).getReg() == registerSpecList.get(1).getReg()) {
      bitSet.set(0, bool);
      bitSet.set(1, bool);
    } 
    return bitSet;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(registerSpecList.get(0).regString());
    stringBuilder.append(", ");
    stringBuilder.append(cstString(paramDalvInsn));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return paramBoolean ? cstComment(paramDalvInsn) : "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    RegisterSpec registerSpec;
    boolean bool1 = paramDalvInsn instanceof CstInsn;
    boolean bool = false;
    if (!bool1)
      return false; 
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int i = registerSpecList.size();
    if (i != 1) {
      if (i != 2)
        return false; 
      RegisterSpec registerSpec1 = registerSpecList.get(0);
      registerSpec = registerSpec1;
      if (registerSpec1.getReg() != registerSpecList.get(1).getReg())
        return false; 
    } else {
      registerSpec = registerSpecList.get(0);
    } 
    if (!unsignedFitsInByte(registerSpec.getReg()))
      return false; 
    Constant constant = ((CstInsn)paramDalvInsn).getConstant();
    if (constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType || constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFieldRef || constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString)
      bool = true; 
    return bool;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int i = ((CstInsn)paramDalvInsn).getIndex();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, registerSpecList.get(0).getReg()), i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form31c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */