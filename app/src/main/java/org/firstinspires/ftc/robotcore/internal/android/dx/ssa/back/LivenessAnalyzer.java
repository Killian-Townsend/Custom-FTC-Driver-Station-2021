package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.PhiInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaBasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;

public class LivenessAnalyzer {
  private SsaBasicBlock blockN;
  
  private final InterferenceGraph interference;
  
  private final BitSet liveOutBlocks;
  
  private NextFunction nextFunction;
  
  private final int regV;
  
  private final SsaMethod ssaMeth;
  
  private int statementIndex;
  
  private final BitSet visitedBlocks;
  
  private LivenessAnalyzer(SsaMethod paramSsaMethod, int paramInt, InterferenceGraph paramInterferenceGraph) {
    int i = paramSsaMethod.getBlocks().size();
    this.ssaMeth = paramSsaMethod;
    this.regV = paramInt;
    this.visitedBlocks = new BitSet(i);
    this.liveOutBlocks = new BitSet(i);
    this.interference = paramInterferenceGraph;
  }
  
  private static void coInterferePhis(SsaMethod paramSsaMethod, InterferenceGraph paramInterferenceGraph) {
    Iterator<SsaBasicBlock> iterator = paramSsaMethod.getBlocks().iterator();
    while (iterator.hasNext()) {
      List<SsaInsn> list = ((SsaBasicBlock)iterator.next()).getPhiInsns();
      int j = list.size();
      for (int i = 0; i < j; i++) {
        for (int k = 0; k < j; k++) {
          if (i != k)
            paramInterferenceGraph.add(((SsaInsn)list.get(i)).getResult().getReg(), ((SsaInsn)list.get(k)).getResult().getReg()); 
        } 
      } 
    } 
  }
  
  public static InterferenceGraph constructInterferenceGraph(SsaMethod paramSsaMethod) {
    int j = paramSsaMethod.getRegCount();
    InterferenceGraph interferenceGraph = new InterferenceGraph(j);
    for (int i = 0; i < j; i++)
      (new LivenessAnalyzer(paramSsaMethod, i, interferenceGraph)).run(); 
    coInterferePhis(paramSsaMethod, interferenceGraph);
    return interferenceGraph;
  }
  
  private void handleTailRecursion() {
    while (this.nextFunction != NextFunction.DONE) {
      int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$android$dx$ssa$back$LivenessAnalyzer$NextFunction[this.nextFunction.ordinal()];
      if (i != 1) {
        if (i != 2) {
          if (i != 3)
            continue; 
          this.nextFunction = NextFunction.DONE;
          liveOutAtBlock();
          continue;
        } 
        this.nextFunction = NextFunction.DONE;
        liveOutAtStatement();
        continue;
      } 
      this.nextFunction = NextFunction.DONE;
      liveInAtStatement();
    } 
  }
  
  private void liveInAtStatement() {
    int i = this.statementIndex;
    if (i == 0) {
      this.blockN.addLiveIn(this.regV);
      BitSet bitSet = this.blockN.getPredecessors();
      this.liveOutBlocks.or(bitSet);
      return;
    } 
    this.statementIndex = i - 1;
    this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
  }
  
  private void liveOutAtBlock() {
    if (!this.visitedBlocks.get(this.blockN.getIndex())) {
      this.visitedBlocks.set(this.blockN.getIndex());
      this.blockN.addLiveOut(this.regV);
      this.statementIndex = this.blockN.getInsns().size() - 1;
      this.nextFunction = NextFunction.LIVE_OUT_AT_STATEMENT;
    } 
  }
  
  private void liveOutAtStatement() {
    SsaInsn ssaInsn = this.blockN.getInsns().get(this.statementIndex);
    RegisterSpec registerSpec = ssaInsn.getResult();
    if (!ssaInsn.isResultReg(this.regV)) {
      if (registerSpec != null)
        this.interference.add(this.regV, registerSpec.getReg()); 
      this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
    } 
  }
  
  public void run() {
    for (SsaInsn ssaInsn : this.ssaMeth.getUseListForRegister(this.regV)) {
      Iterator<SsaBasicBlock> iterator;
      this.nextFunction = NextFunction.DONE;
      if (ssaInsn instanceof PhiInsn) {
        iterator = ((PhiInsn)ssaInsn).predBlocksForReg(this.regV, this.ssaMeth).iterator();
        while (iterator.hasNext()) {
          this.blockN = iterator.next();
          this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
          handleTailRecursion();
        } 
        continue;
      } 
      SsaBasicBlock ssaBasicBlock = iterator.getBlock();
      this.blockN = ssaBasicBlock;
      int i = ssaBasicBlock.getInsns().indexOf(iterator);
      this.statementIndex = i;
      if (i >= 0) {
        this.nextFunction = NextFunction.LIVE_IN_AT_STATEMENT;
        handleTailRecursion();
        continue;
      } 
      throw new RuntimeException("insn not found in it's own block");
    } 
    while (true) {
      int i = this.liveOutBlocks.nextSetBit(0);
      if (i >= 0) {
        this.blockN = this.ssaMeth.getBlocks().get(i);
        this.liveOutBlocks.clear(i);
        this.nextFunction = NextFunction.LIVE_OUT_AT_BLOCK;
        handleTailRecursion();
        continue;
      } 
      break;
    } 
  }
  
  private enum NextFunction {
    DONE, LIVE_IN_AT_STATEMENT, LIVE_OUT_AT_BLOCK, LIVE_OUT_AT_STATEMENT;
    
    static {
      NextFunction nextFunction = new NextFunction("DONE", 3);
      DONE = nextFunction;
      $VALUES = new NextFunction[] { LIVE_IN_AT_STATEMENT, LIVE_OUT_AT_STATEMENT, LIVE_OUT_AT_BLOCK, nextFunction };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\LivenessAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */