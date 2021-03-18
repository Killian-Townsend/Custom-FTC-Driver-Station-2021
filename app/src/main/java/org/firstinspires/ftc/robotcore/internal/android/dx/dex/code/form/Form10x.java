package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class Form10x extends InsnFormat {
  public static final InsnFormat THE_ONE = new Form10x();
  
  public int codeSize() {
    return 1;
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    return "";
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    return "";
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    return (paramDalvInsn instanceof org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.SimpleInsn && paramDalvInsn.getRegisters().size() == 0);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    write(paramAnnotatedOutput, opcodeUnit(paramDalvInsn, 0));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\Form10x.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */