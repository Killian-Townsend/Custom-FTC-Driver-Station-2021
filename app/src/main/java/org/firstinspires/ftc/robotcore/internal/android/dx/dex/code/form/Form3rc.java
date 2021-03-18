package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form3rc extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form3rc();
  
  public int codeSize() {
    return 3;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(regRangeString(paramDalvInsn.getRegisters()));
    stringBuilder.append(", ");
    stringBuilder.append(cstString(paramDalvInsn));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return paramBoolean ? cstComment(paramDalvInsn) : "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    null = paramDalvInsn instanceof CstInsn;
    boolean bool = false;
    if (!null)
      return false; 
    CstInsn cstInsn = (CstInsn)paramDalvInsn;
    int i = cstInsn.getIndex();
    Constant constant = cstInsn.getConstant();
    if (!unsignedFitsInShort(i))
      return false; 
    if (!(constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef) && !(constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType))
      return false; 
    RegisterSpecList registerSpecList = cstInsn.getRegisters();
    registerSpecList.size();
    if (registerSpecList.size() != 0) {
      null = bool;
      if (isRegListSequential(registerSpecList)) {
        null = bool;
        if (unsignedFitsInShort(registerSpecList.get(0).getReg())) {
          null = bool;
          if (unsignedFitsInByte(registerSpecList.getWordCount()))
            return true; 
        } 
      } 
      return null;
    } 
    return true;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int j = ((CstInsn)paramDalvInsn).getIndex();
    int k = registerSpecList.size();
    int i = 0;
    if (k != 0)
      i = registerSpecList.get(0).getReg(); 
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, registerSpecList.getWordCount()), (short)j, (short)i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form3rc.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */