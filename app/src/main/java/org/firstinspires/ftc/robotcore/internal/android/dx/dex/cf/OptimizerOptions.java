package org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.EnumSet;
import java.util.HashSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.TranslationAdvice;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.Optimizer;

public class OptimizerOptions {
  private static HashSet<String> dontOptimizeList;
  
  private static HashSet<String> optimizeList;
  
  private static boolean optimizeListsLoaded;
  
  public static void compareOptimizerStep(RopMethod paramRopMethod1, int paramInt, boolean paramBoolean, CfOptions paramCfOptions, TranslationAdvice paramTranslationAdvice, RopMethod paramRopMethod2) {
    EnumSet<Optimizer.OptionalStep> enumSet = EnumSet.allOf(Optimizer.OptionalStep.class);
    enumSet.remove(Optimizer.OptionalStep.CONST_COLLECTOR);
    paramRopMethod1 = Optimizer.optimize(paramRopMethod1, paramInt, paramBoolean, paramCfOptions.localInfo, paramTranslationAdvice, enumSet);
    paramInt = paramRopMethod2.getBlocks().getEffectiveInstructionCount();
    int i = paramRopMethod1.getBlocks().getEffectiveInstructionCount();
    System.err.printf("optimize step regs:(%d/%d/%.2f%%) insns:(%d/%d/%.2f%%)\n", new Object[] { Integer.valueOf(paramRopMethod2.getBlocks().getRegCount()), Integer.valueOf(paramRopMethod1.getBlocks().getRegCount()), Double.valueOf(((paramRopMethod1.getBlocks().getRegCount() - paramRopMethod2.getBlocks().getRegCount()) / paramRopMethod1.getBlocks().getRegCount()) * 100.0D), Integer.valueOf(paramInt), Integer.valueOf(i), Double.valueOf(((i - paramInt) / i) * 100.0D) });
  }
  
  public static void loadOptimizeLists(String paramString1, String paramString2) {
    if (optimizeListsLoaded)
      return; 
    if (paramString1 == null || paramString2 == null) {
      if (paramString1 != null)
        optimizeList = loadStringsFromFile(paramString1); 
      if (paramString2 != null)
        dontOptimizeList = loadStringsFromFile(paramString2); 
      optimizeListsLoaded = true;
      return;
    } 
    throw new RuntimeException("optimize and don't optimize lists  are mutually exclusive.");
  }
  
  private static HashSet<String> loadStringsFromFile(String paramString) {
    HashSet<String> hashSet = new HashSet();
    try {
      FileReader fileReader = new FileReader(paramString);
      BufferedReader bufferedReader = new BufferedReader(fileReader);
      while (true) {
        String str = bufferedReader.readLine();
        if (str != null) {
          hashSet.add(str);
          continue;
        } 
        fileReader.close();
        return hashSet;
      } 
    } catch (IOException iOException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Error with optimize list: ");
      stringBuilder.append(paramString);
      throw new RuntimeException(stringBuilder.toString(), iOException);
    } 
  }
  
  public static boolean shouldOptimize(String paramString) {
    HashSet<String> hashSet = optimizeList;
    if (hashSet != null)
      return hashSet.contains(paramString); 
    hashSet = dontOptimizeList;
    return (hashSet != null) ? (hashSet.contains(paramString) ^ true) : true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\cf\OptimizerOptions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */