package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.BitSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class LocalVariableExtractor {
  private final ArrayList<SsaBasicBlock> blocks;
  
  private final SsaMethod method;
  
  private final LocalVariableInfo resultInfo;
  
  private final BitSet workSet;
  
  private LocalVariableExtractor(SsaMethod paramSsaMethod) {
    if (paramSsaMethod != null) {
      ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
      this.method = paramSsaMethod;
      this.blocks = arrayList;
      this.resultInfo = new LocalVariableInfo(paramSsaMethod);
      this.workSet = new BitSet(arrayList.size());
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  private LocalVariableInfo doit() {
    if (this.method.getRegCount() > 0)
      for (int i = this.method.getEntryBlockIndex(); i >= 0; i = this.workSet.nextSetBit(0)) {
        this.workSet.clear(i);
        processBlock(i);
      }  
    this.resultInfo.setImmutable();
    return this.resultInfo;
  }
  
  public static LocalVariableInfo extract(SsaMethod paramSsaMethod) {
    return (new LocalVariableExtractor(paramSsaMethod)).doit();
  }
  
  private void processBlock(int paramInt) {
    RegisterSpecSet registerSpecSet2 = this.resultInfo.mutableCopyOfStarts(paramInt);
    SsaBasicBlock ssaBasicBlock = this.blocks.get(paramInt);
    ArrayList<SsaInsn> arrayList = ssaBasicBlock.getInsns();
    int k = arrayList.size();
    if (paramInt == this.method.getExitBlockIndex())
      return; 
    int m = k - 1;
    SsaInsn ssaInsn = arrayList.get(m);
    paramInt = ssaInsn.getOriginalRopInsn().getCatches().size();
    int j = 0;
    int i = 1;
    if (paramInt != 0) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    if (paramInt != 0 && ssaInsn.getResult() != null) {
      paramInt = i;
    } else {
      paramInt = 0;
    } 
    RegisterSpecSet registerSpecSet1 = registerSpecSet2;
    i = 0;
    while (i < k) {
      RegisterSpec registerSpec1;
      RegisterSpecSet registerSpecSet = registerSpecSet1;
      if (paramInt != 0) {
        registerSpecSet = registerSpecSet1;
        if (i == m) {
          registerSpecSet1.setImmutable();
          registerSpecSet = registerSpecSet1.mutableCopy();
        } 
      } 
      SsaInsn ssaInsn1 = arrayList.get(i);
      RegisterSpec registerSpec2 = ssaInsn1.getLocalAssignment();
      if (registerSpec2 == null) {
        registerSpec1 = ssaInsn1.getResult();
        if (registerSpec1 != null && registerSpecSet.get(registerSpec1.getReg()) != null)
          registerSpecSet.remove(registerSpecSet.get(registerSpec1.getReg())); 
      } else {
        registerSpec2 = registerSpec2.withSimpleType();
        if (!registerSpec2.equals(registerSpecSet.get(registerSpec2))) {
          RegisterSpec registerSpec = registerSpecSet.localItemToSpec(registerSpec2.getLocalItem());
          if (registerSpec != null && registerSpec.getReg() != registerSpec2.getReg())
            registerSpecSet.remove(registerSpec); 
          this.resultInfo.addAssignment((SsaInsn)registerSpec1, registerSpec2);
          registerSpecSet.put(registerSpec2);
        } 
      } 
      i++;
      registerSpecSet1 = registerSpecSet;
    } 
    registerSpecSet1.setImmutable();
    IntList intList = ssaBasicBlock.getSuccessorList();
    i = intList.size();
    k = ssaBasicBlock.getPrimarySuccessorIndex();
    for (paramInt = j; paramInt < i; paramInt++) {
      RegisterSpecSet registerSpecSet;
      j = intList.get(paramInt);
      if (j == k) {
        registerSpecSet = registerSpecSet1;
      } else {
        registerSpecSet = registerSpecSet2;
      } 
      if (this.resultInfo.mergeStarts(j, registerSpecSet))
        this.workSet.set(j); 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\LocalVariableExtractor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */