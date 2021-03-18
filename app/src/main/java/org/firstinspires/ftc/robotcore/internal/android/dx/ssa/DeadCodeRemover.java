package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;

public class DeadCodeRemover {
  private final int regCount;
  
  private final SsaMethod ssaMeth;
  
  private final ArrayList<SsaInsn>[] useList;
  
  private final BitSet worklist;
  
  private DeadCodeRemover(SsaMethod paramSsaMethod) {
    this.ssaMeth = paramSsaMethod;
    this.regCount = paramSsaMethod.getRegCount();
    this.worklist = new BitSet(this.regCount);
    this.useList = this.ssaMeth.getUseListCopy();
  }
  
  private static boolean hasSideEffect(SsaInsn paramSsaInsn) {
    return (paramSsaInsn == null) ? true : paramSsaInsn.hasSideEffect();
  }
  
  private boolean isCircularNoSideEffect(int paramInt, BitSet paramBitSet) {
    if (paramBitSet != null && paramBitSet.get(paramInt))
      return true; 
    Iterator<SsaInsn> iterator2 = this.useList[paramInt].iterator();
    while (iterator2.hasNext()) {
      if (hasSideEffect(iterator2.next()))
        return false; 
    } 
    BitSet bitSet = paramBitSet;
    if (paramBitSet == null)
      bitSet = new BitSet(this.regCount); 
    bitSet.set(paramInt);
    Iterator<SsaInsn> iterator1 = this.useList[paramInt].iterator();
    while (iterator1.hasNext()) {
      RegisterSpec registerSpec = ((SsaInsn)iterator1.next()).getResult();
      if (registerSpec == null || !isCircularNoSideEffect(registerSpec.getReg(), bitSet))
        return false; 
    } 
    return true;
  }
  
  public static void process(SsaMethod paramSsaMethod) {
    (new DeadCodeRemover(paramSsaMethod)).run();
  }
  
  private void pruneDeadInstructions() {
    HashSet<SsaInsn> hashSet = new HashSet();
    this.ssaMeth.computeReachability();
    for (SsaBasicBlock ssaBasicBlock : this.ssaMeth.getBlocks()) {
      if (ssaBasicBlock.isReachable())
        continue; 
      for (int i = 0; i < ssaBasicBlock.getInsns().size(); i++) {
        SsaInsn ssaInsn = ssaBasicBlock.getInsns().get(i);
        RegisterSpecList registerSpecList = ssaInsn.getSources();
        int k = registerSpecList.size();
        if (k != 0)
          hashSet.add(ssaInsn); 
        for (int j = 0; j < k; j++) {
          RegisterSpec registerSpec1 = registerSpecList.get(j);
          this.useList[registerSpec1.getReg()].remove(ssaInsn);
        } 
        RegisterSpec registerSpec = ssaInsn.getResult();
        if (registerSpec != null)
          for (SsaInsn ssaInsn1 : this.useList[registerSpec.getReg()]) {
            if (ssaInsn1 instanceof PhiInsn)
              ((PhiInsn)ssaInsn1).removePhiRegister(registerSpec); 
          }  
      } 
    } 
    this.ssaMeth.deleteInsns(hashSet);
  }
  
  private void run() {
    pruneDeadInstructions();
    HashSet<SsaInsn> hashSet = new HashSet();
    this.ssaMeth.forEachInsn(new NoSideEffectVisitor(this.worklist));
    while (true) {
      BitSet bitSet = this.worklist;
      int i = 0;
      int j = bitSet.nextSetBit(0);
      if (j >= 0) {
        this.worklist.clear(j);
        if (this.useList[j].size() == 0 || isCircularNoSideEffect(j, null)) {
          SsaInsn ssaInsn = this.ssaMeth.getDefinitionForRegister(j);
          if (hashSet.contains(ssaInsn))
            continue; 
          RegisterSpecList registerSpecList = ssaInsn.getSources();
          j = registerSpecList.size();
          while (i < j) {
            RegisterSpec registerSpec = registerSpecList.get(i);
            this.useList[registerSpec.getReg()].remove(ssaInsn);
            if (!hasSideEffect(this.ssaMeth.getDefinitionForRegister(registerSpec.getReg())))
              this.worklist.set(registerSpec.getReg()); 
            i++;
          } 
          hashSet.add(ssaInsn);
        } 
        continue;
      } 
      this.ssaMeth.deleteInsns(hashSet);
      return;
    } 
  }
  
  private static class NoSideEffectVisitor implements SsaInsn.Visitor {
    BitSet noSideEffectRegs;
    
    public NoSideEffectVisitor(BitSet param1BitSet) {
      this.noSideEffectRegs = param1BitSet;
    }
    
    public void visitMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
      if (!DeadCodeRemover.hasSideEffect(param1NormalSsaInsn))
        this.noSideEffectRegs.set(param1NormalSsaInsn.getResult().getReg()); 
    }
    
    public void visitNonMoveInsn(NormalSsaInsn param1NormalSsaInsn) {
      RegisterSpec registerSpec = param1NormalSsaInsn.getResult();
      if (!DeadCodeRemover.hasSideEffect(param1NormalSsaInsn) && registerSpec != null)
        this.noSideEffectRegs.set(registerSpec.getReg()); 
    }
    
    public void visitPhiInsn(PhiInsn param1PhiInsn) {
      if (!DeadCodeRemover.hasSideEffect(param1PhiInsn))
        this.noSideEffectRegs.set(param1PhiInsn.getResult().getReg()); 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\DeadCodeRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */