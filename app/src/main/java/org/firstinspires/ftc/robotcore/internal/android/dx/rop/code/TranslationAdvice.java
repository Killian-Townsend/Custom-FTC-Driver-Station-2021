package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

public interface TranslationAdvice {
  int getMaxOptimalRegisterCount();
  
  boolean hasConstantOperation(Rop paramRop, RegisterSpec paramRegisterSpec1, RegisterSpec paramRegisterSpec2);
  
  boolean requiresSourcesInOrder(Rop paramRop, RegisterSpecList paramRegisterSpecList);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\TranslationAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */