package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.form;

import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.InsnFormat;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class SpecialFormat extends InsnFormat {
  public static final InsnFormat THE_ONE = new SpecialFormat();
  
  public int codeSize() {
    throw new RuntimeException("unsupported");
  }
  
  public String insnArgString(DalvInsn paramDalvInsn) {
    throw new RuntimeException("unsupported");
  }
  
  public String insnCommentString(DalvInsn paramDalvInsn, boolean paramBoolean) {
    throw new RuntimeException("unsupported");
  }
  
  public boolean isCompatible(DalvInsn paramDalvInsn) {
    return true;
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput, DalvInsn paramDalvInsn) {
    throw new RuntimeException("unsupported");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\form\SpecialFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */