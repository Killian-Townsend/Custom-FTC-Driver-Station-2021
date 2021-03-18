package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class IdenticalBlockCombiner {
  private final BasicBlockList blocks;
  
  private final BasicBlockList newBlocks;
  
  private final RopMethod ropMethod;
  
  public IdenticalBlockCombiner(RopMethod paramRopMethod) {
    this.ropMethod = paramRopMethod;
    BasicBlockList basicBlockList = paramRopMethod.getBlocks();
    this.blocks = basicBlockList;
    this.newBlocks = basicBlockList.getMutableCopy();
  }
  
  private void combineBlocks(int paramInt, IntList paramIntList) {
    int j = paramIntList.size();
    for (int i = 0; i < j; i++) {
      int m = paramIntList.get(i);
      BasicBlock basicBlock = this.blocks.labelToBlock(m);
      IntList intList = this.ropMethod.labelToPredecessors(basicBlock.getLabel());
      int n = intList.size();
      int k;
      for (k = 0; k < n; k++)
        replaceSucc(this.newBlocks.labelToBlock(intList.get(k)), m, paramInt); 
    } 
  }
  
  private static boolean compareInsns(BasicBlock paramBasicBlock1, BasicBlock paramBasicBlock2) {
    return paramBasicBlock1.getInsns().contentEquals(paramBasicBlock2.getInsns());
  }
  
  private void replaceSucc(BasicBlock paramBasicBlock, int paramInt1, int paramInt2) {
    IntList intList = paramBasicBlock.getSuccessors().mutableCopy();
    intList.set(intList.indexOf(paramInt1), paramInt2);
    int i = paramBasicBlock.getPrimarySuccessor();
    if (i == paramInt1) {
      paramInt1 = paramInt2;
    } else {
      paramInt1 = i;
    } 
    intList.setImmutable();
    BasicBlock basicBlock = new BasicBlock(paramBasicBlock.getLabel(), paramBasicBlock.getInsns(), intList, paramInt1);
    BasicBlockList basicBlockList = this.newBlocks;
    basicBlockList.set(basicBlockList.indexOfLabel(paramBasicBlock.getLabel()), basicBlock);
  }
  
  public RopMethod process() {
    int j = this.blocks.size();
    BitSet bitSet = new BitSet(this.blocks.getMaxLabel());
    int i;
    for (i = 0; i < j; i++) {
      BasicBlock basicBlock = this.blocks.get(i);
      if (!bitSet.get(basicBlock.getLabel())) {
        IntList intList = this.ropMethod.labelToPredecessors(basicBlock.getLabel());
        int m = intList.size();
        for (int k = 0; k < m; k++) {
          int n = intList.get(k);
          BasicBlock basicBlock1 = this.blocks.labelToBlock(n);
          if (!bitSet.get(n) && basicBlock1.getSuccessors().size() <= 1 && basicBlock1.getFirstInsn().getOpcode().getOpcode() != 55) {
            IntList intList1 = new IntList();
            for (int i1 = k + 1; i1 < m; i1++) {
              int i2 = intList.get(i1);
              BasicBlock basicBlock2 = this.blocks.labelToBlock(i2);
              if (basicBlock2.getSuccessors().size() == 1 && compareInsns(basicBlock1, basicBlock2)) {
                intList1.add(i2);
                bitSet.set(i2);
              } 
            } 
            combineBlocks(n, intList1);
          } 
        } 
      } 
    } 
    for (i = j - 1; i >= 0; i--) {
      if (bitSet.get(this.newBlocks.get(i).getLabel()))
        this.newBlocks.set(i, null); 
    } 
    this.newBlocks.shrinkToFit();
    this.newBlocks.setImmutable();
    return new RopMethod(this.newBlocks, this.ropMethod.getFirstLabel());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\IdenticalBlockCombiner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */