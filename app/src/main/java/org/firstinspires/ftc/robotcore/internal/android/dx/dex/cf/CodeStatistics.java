package org.firstinspires.ftc.robotcore.internal.android.dx.dex.cf;

import java.io.PrintStream;
import org.firstinspires.ftc.robotcore.internal.android.dx.dex.code.DalvCode;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;

public final class CodeStatistics {
  private static final boolean DEBUG = false;
  
  public static int dexRunningDeltaInsns;
  
  public static int dexRunningDeltaRegisters;
  
  public static int dexRunningTotalInsns;
  
  public static int runningDeltaInsns;
  
  public static int runningDeltaRegisters;
  
  public static int runningOriginalBytes;
  
  public static int runningTotalInsns;
  
  public static void dumpStatistics(PrintStream paramPrintStream) {
    int i = runningDeltaInsns;
    int j = runningTotalInsns;
    int k = runningDeltaInsns;
    paramPrintStream.printf("Optimizer Delta Rop Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Double.valueOf((k / (runningTotalInsns + Math.abs(k))) * 100.0D), Integer.valueOf(runningDeltaRegisters) });
    i = dexRunningDeltaInsns;
    j = dexRunningTotalInsns;
    k = dexRunningDeltaInsns;
    paramPrintStream.printf("Optimizer Delta Dex Insns: Insns: %d total: %d (%.2f%%) Delta Registers: %d\n", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Double.valueOf((k / (dexRunningTotalInsns + Math.abs(k))) * 100.0D), Integer.valueOf(dexRunningDeltaRegisters) });
    paramPrintStream.printf("Original bytecode byte count: %d\n", new Object[] { Integer.valueOf(runningOriginalBytes) });
  }
  
  public static void updateDexStatistics(DalvCode paramDalvCode1, DalvCode paramDalvCode2) {
    dexRunningDeltaInsns += paramDalvCode2.getInsns().codeSize() - paramDalvCode1.getInsns().codeSize();
    dexRunningDeltaRegisters += paramDalvCode2.getInsns().getRegistersSize() - paramDalvCode1.getInsns().getRegistersSize();
    dexRunningTotalInsns += paramDalvCode2.getInsns().codeSize();
  }
  
  public static void updateOriginalByteCount(int paramInt) {
    runningOriginalBytes += paramInt;
  }
  
  public static void updateRopStatistics(RopMethod paramRopMethod1, RopMethod paramRopMethod2) {
    int i = paramRopMethod1.getBlocks().getEffectiveInstructionCount();
    int j = paramRopMethod1.getBlocks().getRegCount();
    int k = paramRopMethod2.getBlocks().getEffectiveInstructionCount();
    runningDeltaInsns += k - i;
    runningDeltaRegisters += paramRopMethod2.getBlocks().getRegCount() - j;
    runningTotalInsns += k;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\cf\CodeStatistics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */