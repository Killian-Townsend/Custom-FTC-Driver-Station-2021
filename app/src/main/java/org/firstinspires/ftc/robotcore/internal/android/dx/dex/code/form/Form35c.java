package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.CstInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form35c extends InsnFormat {
  private static final int MAX_NUM_OPS = 5;
  
  public static final InsnFormat THE_ONE = new Form35c();
  
  private static RegisterSpecList explicitize(RegisterSpecList paramRegisterSpecList) {
    int i = wordCount(paramRegisterSpecList);
    int k = paramRegisterSpecList.size();
    if (i == k)
      return paramRegisterSpecList; 
    RegisterSpecList registerSpecList = new RegisterSpecList(i);
    int j = 0;
    i = 0;
    while (j < k) {
      RegisterSpec registerSpec = paramRegisterSpecList.get(j);
      registerSpecList.set(i, registerSpec);
      if (registerSpec.getCategory() == 2) {
        registerSpecList.set(i + 1, RegisterSpec.make(registerSpec.getReg() + 1, (TypeBearer)Type.VOID));
        i += 2;
      } else {
        i++;
      } 
      j++;
    } 
    registerSpecList.setImmutable();
    return registerSpecList;
  }
  
  private static int wordCount(RegisterSpecList paramRegisterSpecList) {
    int k = paramRegisterSpecList.size();
    byte b = -1;
    if (k > 5)
      return -1; 
    int j = 0;
    int i = 0;
    while (j < k) {
      RegisterSpec registerSpec = paramRegisterSpecList.get(j);
      i += registerSpec.getCategory();
      if (!unsignedFitsInNibble(registerSpec.getReg() + registerSpec.getCategory() - 1))
        return -1; 
      j++;
    } 
    j = b;
    if (i <= 5)
      j = i; 
    return j;
  }
  
  public int codeSize() {
    return 3;
  }
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int j = registerSpecList.size();
    BitSet bitSet = new BitSet(j);
    for (int i = 0; i < j; i++) {
      RegisterSpec registerSpec = registerSpecList.get(i);
      bitSet.set(i, unsignedFitsInNibble(registerSpec.getReg() + registerSpec.getCategory() - 1));
    } 
    return bitSet;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = explicitize(paramDalvInsn.getRegisters());
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(regListString(registerSpecList));
    stringBuilder.append(", ");
    stringBuilder.append(cstString(paramDalvInsn));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return paramBoolean ? cstComment(paramDalvInsn) : "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    boolean bool1 = paramDalvInsn instanceof CstInsn;
    boolean bool = false;
    if (!bool1)
      return false; 
    CstInsn cstInsn = (CstInsn)paramDalvInsn;
    if (!unsignedFitsInShort(cstInsn.getIndex()))
      return false; 
    Constant constant = cstInsn.getConstant();
    if (!(constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstMethodRef) && !(constant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType))
      return false; 
    if (wordCount(cstInsn.getRegisters()) >= 0)
      bool = true; 
    return bool;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    boolean bool1;
    boolean bool2;
    boolean bool3;
    boolean bool4;
    int j = ((CstInsn)paramDalvInsn).getIndex();
    RegisterSpecList registerSpecList = explicitize(paramDalvInsn.getRegisters());
    int k = registerSpecList.size();
    int i = 0;
    if (k > 0) {
      bool1 = registerSpecList.get(0).getReg();
    } else {
      bool1 = false;
    } 
    if (k > 1) {
      bool2 = registerSpecList.get(1).getReg();
    } else {
      bool2 = false;
    } 
    if (k > 2) {
      bool3 = registerSpecList.get(2).getReg();
    } else {
      bool3 = false;
    } 
    if (k > 3) {
      bool4 = registerSpecList.get(3).getReg();
    } else {
      bool4 = false;
    } 
    if (k > 4)
      i = registerSpecList.get(4).getReg(); 
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, makeByte(i, k)), (short)j, codeUnit(bool1, bool2, bool3, bool4));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form35c.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */