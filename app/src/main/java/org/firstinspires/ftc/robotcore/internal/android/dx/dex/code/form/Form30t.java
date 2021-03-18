package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.TargetInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form30t extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form30t();
  
  public boolean branchFits(TargetInsn paramTargetInsn) {
    return true;
  }
  
  public int codeSize() {
    return 3;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    return branchString(paramDalvInsn);
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return branchComment(paramDalvInsn);
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    return !(!(paramDalvInsn instanceof TargetInsn) || paramDalvInsn.getRegisters().size() != 0);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    int i = ((TargetInsn)paramDalvInsn).getTargetOffset();
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, 0), i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form30t.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */