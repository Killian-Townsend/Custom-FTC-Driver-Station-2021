package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.TargetInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form22t extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form22t();
  
  public boolean branchFits(TargetInsn paramTargetInsn) {
    int i = paramTargetInsn.getTargetOffset();
    return (i != 0 && signedFitsInShort(i));
  }
  
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
    stringBuilder.append(branchString(paramDalvInsn));
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return branchComment(paramDalvInsn);
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    if (paramDalvInsn instanceof TargetInsn && registerSpecList.size() == 2 && unsignedFitsInNibble(registerSpecList.get(0).getReg())) {
      boolean bool = true;
      if (!unsignedFitsInNibble(registerSpecList.get(1).getReg()))
        return false; 
      TargetInsn targetInsn = (TargetInsn)paramDalvInsn;
      if (targetInsn.hasTargetOffset())
        bool = branchFits(targetInsn); 
      return bool;
    } 
    return false;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    int i = ((TargetInsn)paramDalvInsn).getTargetOffset();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, makeByte(registerSpecList.get(0).getReg(), registerSpecList.get(1).getReg())), (short)i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form22t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */