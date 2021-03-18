package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RopMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntIterator;

public class SsaConverter {
  public static final boolean DEBUG = false;
  
  public static SsaMethod convertToSsaMethod(RopMethod paramRopMethod, int paramInt, boolean paramBoolean) {
    SsaMethod ssaMethod = SsaMethod.newFromRopMethod(paramRopMethod, paramInt, paramBoolean);
    edgeSplit(ssaMethod);
    placePhiFunctions(ssaMethod, LocalVariableExtractor.extract(ssaMethod), 0);
    (new SsaRenamer(ssaMethod)).run();
    ssaMethod.makeExitBlock();
    return ssaMethod;
  }
  
  private static void edgeSplit(SsaMethod paramSsaMethod) {
    edgeSplitPredecessors(paramSsaMethod);
    edgeSplitMoveExceptionsAndResults(paramSsaMethod);
    edgeSplitSuccessors(paramSsaMethod);
  }
  
  private static void edgeSplitMoveExceptionsAndResults(SsaMethod paramSsaMethod) {
    ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
    for (int i = arrayList.size() - 1; i >= 0; i--) {
      SsaBasicBlock ssaBasicBlock = arrayList.get(i);
      if (!ssaBasicBlock.isExitBlock() && ssaBasicBlock.getPredecessors().cardinality() > 1 && ((SsaInsn)ssaBasicBlock.getInsns().get(0)).isMoveException()) {
        BitSet bitSet = (BitSet)ssaBasicBlock.getPredecessors().clone();
        for (int j = bitSet.nextSetBit(0); j >= 0; j = bitSet.nextSetBit(j + 1))
          ((SsaBasicBlock)arrayList.get(j)).insertNewSuccessor(ssaBasicBlock).getInsns().add(0, ((SsaInsn)ssaBasicBlock.getInsns().get(0)).clone()); 
        ssaBasicBlock.getInsns().remove(0);
      } 
    } 
  }
  
  private static void edgeSplitPredecessors(SsaMethod paramSsaMethod) {
    ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
    for (int i = arrayList.size() - 1; i >= 0; i--) {
      SsaBasicBlock ssaBasicBlock = arrayList.get(i);
      if (nodeNeedsUniquePredecessor(ssaBasicBlock))
        ssaBasicBlock.insertNewPredecessor(); 
    } 
  }
  
  private static void edgeSplitSuccessors(SsaMethod paramSsaMethod) {
    ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
    for (int i = arrayList.size() - 1; i >= 0; i--) {
      SsaBasicBlock ssaBasicBlock = arrayList.get(i);
      BitSet bitSet = (BitSet)ssaBasicBlock.getSuccessors().clone();
      for (int j = bitSet.nextSetBit(0); j >= 0; j = bitSet.nextSetBit(j + 1)) {
        SsaBasicBlock ssaBasicBlock1 = arrayList.get(j);
        if (needsNewSuccessor(ssaBasicBlock, ssaBasicBlock1))
          ssaBasicBlock.insertNewSuccessor(ssaBasicBlock1); 
      } 
    } 
  }
  
  private static boolean needsNewSuccessor(SsaBasicBlock paramSsaBasicBlock1, SsaBasicBlock paramSsaBasicBlock2) {
    ArrayList<SsaInsn> arrayList = paramSsaBasicBlock1.getInsns();
    SsaInsn ssaInsn = arrayList.get(arrayList.size() - 1);
    return ((ssaInsn.getResult() != null || ssaInsn.getSources().size() > 0) && paramSsaBasicBlock2.getPredecessors().cardinality() > 1);
  }
  
  private static boolean nodeNeedsUniquePredecessor(SsaBasicBlock paramSsaBasicBlock) {
    int i = paramSsaBasicBlock.getPredecessors().cardinality();
    int j = paramSsaBasicBlock.getSuccessors().cardinality();
    return (i > 1 && j > 1);
  }
  
  private static void placePhiFunctions(SsaMethod paramSsaMethod, LocalVariableInfo paramLocalVariableInfo, int paramInt) {
    ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
    int k = arrayList.size();
    int j = paramSsaMethod.getRegCount() - paramInt;
    DomFront.DomInfo[] arrayOfDomInfo = (new DomFront(paramSsaMethod)).run();
    BitSet[] arrayOfBitSet1 = new BitSet[j];
    BitSet[] arrayOfBitSet2 = new BitSet[j];
    int i;
    for (i = 0; i < j; i++) {
      arrayOfBitSet1[i] = new BitSet(k);
      arrayOfBitSet2[i] = new BitSet(k);
    } 
    k = arrayList.size();
    for (i = 0; i < k; i++) {
      Iterator<SsaInsn> iterator = ((SsaBasicBlock)arrayList.get(i)).getInsns().iterator();
      while (iterator.hasNext()) {
        RegisterSpec registerSpec = ((SsaInsn)iterator.next()).getResult();
        if (registerSpec != null && registerSpec.getReg() - paramInt >= 0)
          arrayOfBitSet1[registerSpec.getReg() - paramInt].set(i); 
      } 
    } 
    i = 0;
    while (i < j) {
      BitSet bitSet = (BitSet)arrayOfBitSet1[i].clone();
      while (true) {
        k = bitSet.nextSetBit(0);
        if (k >= 0) {
          bitSet.clear(k);
          IntIterator intIterator = (arrayOfDomInfo[k]).dominanceFrontiers.iterator();
          while (intIterator.hasNext()) {
            k = intIterator.next();
            if (!arrayOfBitSet2[i].get(k)) {
              arrayOfBitSet2[i].set(k);
              int m = i + paramInt;
              RegisterSpec registerSpec = paramLocalVariableInfo.getStarts(k).get(m);
              if (registerSpec == null) {
                ((SsaBasicBlock)arrayList.get(k)).addPhiInsnForReg(m);
              } else {
                ((SsaBasicBlock)arrayList.get(k)).addPhiInsnForReg(registerSpec);
              } 
              if (!arrayOfBitSet1[i].get(k))
                bitSet.set(k); 
            } 
          } 
          continue;
        } 
        i++;
      } 
    } 
  }
  
  public static SsaMethod testEdgeSplit(RopMethod paramRopMethod, int paramInt, boolean paramBoolean) {
    SsaMethod ssaMethod = SsaMethod.newFromRopMethod(paramRopMethod, paramInt, paramBoolean);
    edgeSplit(ssaMethod);
    return ssaMethod;
  }
  
  public static SsaMethod testPhiPlacement(RopMethod paramRopMethod, int paramInt, boolean paramBoolean) {
    SsaMethod ssaMethod = SsaMethod.newFromRopMethod(paramRopMethod, paramInt, paramBoolean);
    edgeSplit(ssaMethod);
    placePhiFunctions(ssaMethod, LocalVariableExtractor.extract(ssaMethod), 0);
    return ssaMethod;
  }
  
  public static void updateSsaMethod(SsaMethod paramSsaMethod, int paramInt) {
    placePhiFunctions(paramSsaMethod, LocalVariableExtractor.extract(paramSsaMethod), paramInt);
    (new SsaRenamer(paramSsaMethod, paramInt)).run();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\SsaConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */