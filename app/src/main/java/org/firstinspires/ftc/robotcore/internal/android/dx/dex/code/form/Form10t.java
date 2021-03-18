package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.TargetInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form10t extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form10t();
  
  public boolean branchFits(TargetInsn paramTargetInsn) {
    int i = paramTargetInsn.getTargetOffset();
    return (i != 0 && signedFitsInByte(i));
  }
  
  public int codeSize() {
    return 1;
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
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, ((TargetInsn)paramDalvInsn).getTargetOffset() & 0xFF));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form10t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */