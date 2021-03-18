package org.firstinspires.ftc.robotcore.internal.android.dx.ssa.back;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.BasicBlockList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.InsnList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rop;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.Rops;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.BasicRegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.PhiInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.RegisterMapper;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaBasicBlock;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaInsn;
import org.firstinspires.ftc.robotcore.internal.android.dx.ssa.SsaMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class SsaToRop {
  private static final boolean DEBUG = false;
  
  private final InterferenceGraph interference;
  
  private final boolean minimizeRegisters;
  
  private final SsaMethod ssaMeth;
  
  private SsaToRop(SsaMethod paramSsaMethod, boolean paramBoolean) {
    this.minimizeRegisters = paramBoolean;
    this.ssaMeth = paramSsaMethod;
    this.interference = LivenessAnalyzer.constructInterferenceGraph(paramSsaMethod);
  }
  
  private RopMethod convert() {
    FirstFitLocalCombiningAllocator firstFitLocalCombiningAllocator = new FirstFitLocalCombiningAllocator(this.ssaMeth, this.interference, this.minimizeRegisters);
    RegisterMapper registerMapper = firstFitLocalCombiningAllocator.allocateRegisters();
    this.ssaMeth.setBackMode();
    this.ssaMeth.mapRegisters(registerMapper);
    removePhiFunctions();
    if (firstFitLocalCombiningAllocator.wantsParamsMovedHigh())
      moveParametersToHighRegisters(); 
    removeEmptyGotos();
    BasicBlockList basicBlockList = convertBasicBlocks();
    SsaMethod ssaMethod = this.ssaMeth;
    return (new IdenticalBlockCombiner(new RopMethod(basicBlockList, ssaMethod.blockIndexToRopLabel(ssaMethod.getEntryBlockIndex())))).process();
  }
  
  private BasicBlock convertBasicBlock(SsaBasicBlock paramSsaBasicBlock) {
    int i;
    StringBuilder stringBuilder;
    IntList intList2 = paramSsaBasicBlock.getRopLabelSuccessorList();
    int j = paramSsaBasicBlock.getPrimarySuccessorRopLabel();
    SsaBasicBlock ssaBasicBlock = this.ssaMeth.getExitBlock();
    if (ssaBasicBlock == null) {
      i = -1;
    } else {
      i = ssaBasicBlock.getRopLabel();
    } 
    IntList intList1 = intList2;
    if (intList2.contains(i))
      if (intList2.size() <= 1) {
        intList1 = IntList.EMPTY;
        verifyValidExitPredecessor(paramSsaBasicBlock);
        j = -1;
      } else {
        stringBuilder = new StringBuilder();
        stringBuilder.append("Exit predecessor must have no other successors");
        stringBuilder.append(Hex.u2(paramSsaBasicBlock.getRopLabel()));
        throw new RuntimeException(stringBuilder.toString());
      }  
    stringBuilder.setImmutable();
    return new BasicBlock(paramSsaBasicBlock.getRopLabel(), convertInsns(paramSsaBasicBlock.getInsns()), (IntList)stringBuilder, j);
  }
  
  private BasicBlockList convertBasicBlocks() {
    ArrayList arrayList = this.ssaMeth.getBlocks();
    SsaBasicBlock ssaBasicBlock = this.ssaMeth.getExitBlock();
    this.ssaMeth.computeReachability();
    int j = this.ssaMeth.getCountReachableBlocks();
    byte b = 0;
    if (ssaBasicBlock != null && ssaBasicBlock.isReachable()) {
      i = 1;
    } else {
      i = 0;
    } 
    BasicBlockList basicBlockList = new BasicBlockList(j - i);
    Iterator<SsaBasicBlock> iterator = arrayList.iterator();
    int i = b;
    while (iterator.hasNext()) {
      SsaBasicBlock ssaBasicBlock1 = iterator.next();
      if (ssaBasicBlock1.isReachable() && ssaBasicBlock1 != ssaBasicBlock) {
        basicBlockList.set(i, convertBasicBlock(ssaBasicBlock1));
        i++;
      } 
    } 
    if (ssaBasicBlock != null) {
      if (ssaBasicBlock.getInsns().size() == 0)
        return basicBlockList; 
      throw new RuntimeException("Exit block must have no insns when leaving SSA form");
    } 
    return basicBlockList;
  }
  
  private InsnList convertInsns(ArrayList<SsaInsn> paramArrayList) {
    int j = paramArrayList.size();
    InsnList insnList = new InsnList(j);
    for (int i = 0; i < j; i++)
      insnList.set(i, ((SsaInsn)paramArrayList.get(i)).toRopInsn()); 
    insnList.setImmutable();
    return insnList;
  }
  
  public static RopMethod convertToRopMethod(SsaMethod paramSsaMethod, boolean paramBoolean) {
    return (new SsaToRop(paramSsaMethod, paramBoolean)).convert();
  }
  
  private void moveParametersToHighRegisters() {
    int j = this.ssaMeth.getParamWidth();
    BasicRegisterMapper basicRegisterMapper = new BasicRegisterMapper(this.ssaMeth.getRegCount());
    int k = this.ssaMeth.getRegCount();
    for (int i = 0; i < k; i++) {
      if (i < j) {
        basicRegisterMapper.addMapping(i, k - j + i, 1);
      } else {
        basicRegisterMapper.addMapping(i, i - j, 1);
      } 
    } 
    this.ssaMeth.mapRegisters((RegisterMapper)basicRegisterMapper);
  }
  
  private void removeEmptyGotos() {
    final ArrayList blocks = this.ssaMeth.getBlocks();
    this.ssaMeth.forEachBlockDepthFirst(false, new SsaBasicBlock.Visitor() {
          public void visitBlock(SsaBasicBlock param1SsaBasicBlock1, SsaBasicBlock param1SsaBasicBlock2) {
            ArrayList<SsaInsn> arrayList = param1SsaBasicBlock1.getInsns();
            if (arrayList.size() == 1 && ((SsaInsn)arrayList.get(0)).getOpcode() == Rops.GOTO) {
              BitSet bitSet = (BitSet)param1SsaBasicBlock1.getPredecessors().clone();
              for (int i = bitSet.nextSetBit(0); i >= 0; i = bitSet.nextSetBit(i + 1))
                ((SsaBasicBlock)blocks.get(i)).replaceSuccessor(param1SsaBasicBlock1.getIndex(), param1SsaBasicBlock1.getPrimarySuccessorIndex()); 
            } 
          }
        });
  }
  
  private void removePhiFunctions() {
    ArrayList<SsaBasicBlock> arrayList = this.ssaMeth.getBlocks();
    for (SsaBasicBlock ssaBasicBlock : arrayList) {
      ssaBasicBlock.forEachPhiInsn(new PhiVisitor(arrayList));
      ssaBasicBlock.removeAllPhiInsns();
    } 
    Iterator<SsaBasicBlock> iterator = arrayList.iterator();
    while (iterator.hasNext())
      ((SsaBasicBlock)iterator.next()).scheduleMovesFromPhis(); 
  }
  
  private void verifyValidExitPredecessor(SsaBasicBlock paramSsaBasicBlock) {
    ArrayList<SsaInsn> arrayList = paramSsaBasicBlock.getInsns();
    Rop rop = ((SsaInsn)arrayList.get(arrayList.size() - 1)).getOpcode();
    if (rop.getBranchingness() != 2) {
      if (rop == Rops.THROW)
        return; 
      throw new RuntimeException("Exit predecessor must end in valid exit statement.");
    } 
  }
  
  public int[] getRegistersByFrequency() {
    int j = this.ssaMeth.getRegCount();
    Integer[] arrayOfInteger = new Integer[j];
    boolean bool = false;
    int i;
    for (i = 0; i < j; i++)
      arrayOfInteger[i] = Integer.valueOf(i); 
    Arrays.sort(arrayOfInteger, new Comparator<Integer>() {
          public int compare(Integer param1Integer1, Integer param1Integer2) {
            return SsaToRop.this.ssaMeth.getUseListForRegister(param1Integer2.intValue()).size() - SsaToRop.this.ssaMeth.getUseListForRegister(param1Integer1.intValue()).size();
          }
        });
    int[] arrayOfInt = new int[j];
    for (i = bool; i < j; i++)
      arrayOfInt[i] = arrayOfInteger[i].intValue(); 
    return arrayOfInt;
  }
  
  private static class PhiVisitor implements PhiInsn.Visitor {
    private final ArrayList<SsaBasicBlock> blocks;
    
    public PhiVisitor(ArrayList<SsaBasicBlock> param1ArrayList) {
      this.blocks = param1ArrayList;
    }
    
    public void visitPhiInsn(PhiInsn param1PhiInsn) {
      RegisterSpecList registerSpecList = param1PhiInsn.getSources();
      RegisterSpec registerSpec = param1PhiInsn.getResult();
      int j = registerSpecList.size();
      for (int i = 0; i < j; i++) {
        RegisterSpec registerSpec1 = registerSpecList.get(i);
        ((SsaBasicBlock)this.blocks.get(param1PhiInsn.predBlockIndexForSourcesIndex(i))).addMoveToEnd(registerSpec, registerSpec1);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\back\SsaToRop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */