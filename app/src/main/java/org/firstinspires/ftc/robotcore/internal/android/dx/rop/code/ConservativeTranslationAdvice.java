package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

public final class ConservativeTranslationAdvice implements TranslationAdvice {
  public static final ConservativeTranslationAdvice THE_ONE = new ConservativeTranslationAdvice();
  
  public int getMaxOptimalRegisterCount() {
    return Integer.MAX_VALUE;
  }
  
  public boolean hasConstantOperation(Rop paramRop, RegisterSpec paramRegisterSpec1, RegisterSpec paramRegisterSpec2) {
    return false;
  }
  
  public boolean requiresSourcesInOrder(Rop paramRop, RegisterSpecList paramRegisterSpecList) {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\ConservativeTranslationAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */