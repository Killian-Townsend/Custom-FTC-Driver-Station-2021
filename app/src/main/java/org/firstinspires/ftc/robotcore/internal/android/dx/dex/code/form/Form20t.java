package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.TargetInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form20t extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form20t();
  
  public boolean branchFits(TargetInsn paramTargetInsn) {
    int i = paramTargetInsn.getTargetOffset();
    return (i != 0 && signedFitsInShort(i));
  }
  
  public int codeSize() {
    return 2;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    return branchString(paramDalvInsn);
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return branchComment(paramDalvInsn);
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    if (!(paramDalvInsn instanceof TargetInsn) || paramDalvInsn.getRegisters().size() != 0)
      return false; 
    TargetInsn targetInsn = (TargetInsn)paramDalvInsn;
    return targetInsn.hasTargetOffset() ? branchFits(targetInsn) : true;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    int i = ((TargetInsn)paramDalvInsn).getTargetOffset();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, 0), (short)i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form20t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */