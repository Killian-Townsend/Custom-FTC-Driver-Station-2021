package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form22x extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form22x();
  
  public int codeSize() {
    return 2;
  }
  
  public BitSet compatibleRegs(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    BitSet bitSet = new BitSet(2);
    bitSet.set(0, unsignedFitsInByte(registerSpecList.get(0).getReg()));
    bitSet.set(1, unsignedFitsInShort(registerSpecList.get(1).getReg()));
    return bitSet;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(registerSpecList.get(0).regString());
    stringBuilder.append(", ");
    stringBuilder.append(registerSpecList.get(1).regString());
    return stringBuilder.toString();
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    return (paramDalvInsn instanceof org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.SimpleInsn && registerSpecList.size() == 2 && unsignedFitsInByte(registerSpecList.get(0).getReg()) && unsignedFitsInShort(registerSpecList.get(1).getReg()));
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    RegisterSpecList registerSpecList = paramDalvInsn.getRegisters();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, registerSpecList.get(0).getReg()), (short)registerSpecList.get(1).getReg());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form22x.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */