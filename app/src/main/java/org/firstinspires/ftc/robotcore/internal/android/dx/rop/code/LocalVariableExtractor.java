package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.Bits;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public final class LocalVariableExtractor {
  private final BasicBlockList blocks;
  
  private final RopMethod method;
  
  private final LocalVariableInfo resultInfo;
  
  private final int[] workSet;
  
  private LocalVariableExtractor(RopMethod paramRopMethod) {
    if (paramRopMethod != null) {
      BasicBlockList basicBlockList = paramRopMethod.getBlocks();
      int i = basicBlockList.getMaxLabel();
      this.method = paramRopMethod;
      this.blocks = basicBlockList;
      this.resultInfo = new LocalVariableInfo(paramRopMethod);
      this.workSet = Bits.makeBitSet(i);
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  private LocalVariableInfo doit() {
    for (int i = this.method.getFirstLabel(); i >= 0; i = Bits.findFirst(this.workSet, 0)) {
      Bits.clear(this.workSet, i);
      processBlock(i);
    } 
    this.resultInfo.setImmutable();
    return this.resultInfo;
  }
  
  public static LocalVariableInfo extract(RopMethod paramRopMethod) {
    return (new LocalVariableExtractor(paramRopMethod)).doit();
  }
  
  private void processBlock(int paramInt) {
    RegisterSpecSet registerSpecSet2 = this.resultInfo.mutableCopyOfStarts(paramInt);
    BasicBlock basicBlock = this.blocks.labelToBlock(paramInt);
    InsnList insnList = basicBlock.getInsns();
    int k = insnList.size();
    boolean bool = basicBlock.hasExceptionHandlers();
    int j = 0;
    if (bool && insnList.getLast().getResult() != null) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    RegisterSpecSet registerSpecSet1 = registerSpecSet2;
    int i = 0;
    while (i < k) {
      RegisterSpec registerSpec1;
      RegisterSpecSet registerSpecSet = registerSpecSet1;
      if (paramInt != 0) {
        registerSpecSet = registerSpecSet1;
        if (i == k - 1) {
          registerSpecSet1.setImmutable();
          registerSpecSet = registerSpecSet1.mutableCopy();
        } 
      } 
      Insn insn = insnList.get(i);
      RegisterSpec registerSpec2 = insn.getLocalAssignment();
      if (registerSpec2 == null) {
        registerSpec1 = insn.getResult();
        if (registerSpec1 != null && registerSpecSet.get(registerSpec1.getReg()) != null)
          registerSpecSet.remove(registerSpecSet.get(registerSpec1.getReg())); 
      } else {
        registerSpec2 = registerSpec2.withSimpleType();
        if (!registerSpec2.equals(registerSpecSet.get(registerSpec2))) {
          RegisterSpec registerSpec = registerSpecSet.localItemToSpec(registerSpec2.getLocalItem());
          if (registerSpec != null && registerSpec.getReg() != registerSpec2.getReg())
            registerSpecSet.remove(registerSpec); 
          this.resultInfo.addAssignment((Insn)registerSpec1, registerSpec2);
          registerSpecSet.put(registerSpec2);
        } 
      } 
      i++;
      registerSpecSet1 = registerSpecSet;
    } 
    registerSpecSet1.setImmutable();
    IntList intList = basicBlock.getSuccessors();
    i = intList.size();
    k = basicBlock.getPrimarySuccessor();
    for (paramInt = j; paramInt < i; paramInt++) {
      RegisterSpecSet registerSpecSet;
      j = intList.get(paramInt);
      if (j == k) {
        registerSpecSet = registerSpecSet1;
      } else {
        registerSpecSet = registerSpecSet2;
      } 
      if (this.resultInfo.mergeStarts(j, registerSpecSet))
        Bits.set(this.workSet, j); 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\LocalVariableExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */