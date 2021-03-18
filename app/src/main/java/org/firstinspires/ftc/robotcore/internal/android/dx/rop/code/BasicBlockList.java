package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.LabeledList;

public final class BasicBlockList extends LabeledList {
  private int regCount = -1;
  
  public BasicBlockList(int paramInt) {
    super(paramInt);
  }
  
  private BasicBlockList(BasicBlockList paramBasicBlockList) {
    super(paramBasicBlockList);
  }
  
  public boolean catchesEqual(BasicBlock paramBasicBlock1, BasicBlock paramBasicBlock2) {
    if (!StdTypeList.equalContents(paramBasicBlock1.getExceptionHandlerTypes(), paramBasicBlock2.getExceptionHandlerTypes()))
      return false; 
    IntList intList1 = paramBasicBlock1.getSuccessors();
    IntList intList2 = paramBasicBlock2.getSuccessors();
    int j = intList1.size();
    int k = paramBasicBlock1.getPrimarySuccessor();
    int m = paramBasicBlock2.getPrimarySuccessor();
    if ((k == -1 || m == -1) && k != m)
      return false; 
    for (int i = 0; i < j; i++) {
      int n = intList1.get(i);
      int i1 = intList2.get(i);
      if (n == k) {
        if (i1 != m)
          return false; 
      } else if (n != i1) {
        return false;
      } 
    } 
    return true;
  }
  
  public void forEachInsn(Insn.Visitor paramVisitor) {
    int j = size();
    for (int i = 0; i < j; i++)
      get(i).getInsns().forEach(paramVisitor); 
  }
  
  public BasicBlock get(int paramInt) {
    return (BasicBlock)get0(paramInt);
  }
  
  public int getEffectiveInstructionCount() {
    int k = size();
    int j = 0;
    int i;
    for (i = j; j < k; i = m) {
      BasicBlock basicBlock = (BasicBlock)getOrNull0(j);
      int m = i;
      if (basicBlock != null) {
        InsnList insnList = basicBlock.getInsns();
        int i1 = insnList.size();
        int n = 0;
        while (true) {
          m = i;
          if (n < i1) {
            m = i;
            if (insnList.get(n).getOpcode().getOpcode() != 54)
              m = i + 1; 
            n++;
            i = m;
            continue;
          } 
          break;
        } 
      } 
      j++;
    } 
    return i;
  }
  
  public int getInstructionCount() {
    int k = size();
    int i = 0;
    int j;
    for (j = 0; i < k; j = m) {
      BasicBlock basicBlock = (BasicBlock)getOrNull0(i);
      int m = j;
      if (basicBlock != null)
        m = j + basicBlock.getInsns().size(); 
      i++;
    } 
    return j;
  }
  
  public BasicBlockList getMutableCopy() {
    return new BasicBlockList(this);
  }
  
  public int getRegCount() {
    if (this.regCount == -1) {
      RegCountVisitor regCountVisitor = new RegCountVisitor();
      forEachInsn(regCountVisitor);
      this.regCount = regCountVisitor.getRegCount();
    } 
    return this.regCount;
  }
  
  public BasicBlock labelToBlock(int paramInt) {
    int i = indexOfLabel(paramInt);
    if (i >= 0)
      return get(i); 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("no such label: ");
    stringBuilder.append(Hex.u2(paramInt));
    throw new IllegalArgumentException(stringBuilder.toString());
  }
  
  public BasicBlock preferredSuccessorOf(BasicBlock paramBasicBlock) {
    int i = paramBasicBlock.getPrimarySuccessor();
    IntList intList = paramBasicBlock.getSuccessors();
    int j = intList.size();
    return (j != 0) ? ((j != 1) ? ((i != -1) ? labelToBlock(i) : labelToBlock(intList.get(0))) : labelToBlock(intList.get(0))) : null;
  }
  
  public void set(int paramInt, BasicBlock paramBasicBlock) {
    set(paramInt, paramBasicBlock);
    this.regCount = -1;
  }
  
  public BasicBlockList withRegisterOffset(int paramInt) {
    int j = size();
    BasicBlockList basicBlockList = new BasicBlockList(j);
    for (int i = 0; i < j; i++) {
      BasicBlock basicBlock = (BasicBlock)get0(i);
      if (basicBlock != null)
        basicBlockList.set(i, basicBlock.withRegisterOffset(paramInt)); 
    } 
    if (isImmutable())
      basicBlockList.setImmutable(); 
    return basicBlockList;
  }
  
  private static class RegCountVisitor implements Insn.Visitor {
    private int regCount = 0;
    
    private void processReg(RegisterSpec param1RegisterSpec) {
      int i = param1RegisterSpec.getNextReg();
      if (i > this.regCount)
        this.regCount = i; 
    }
    
    private void visit(Insn param1Insn) {
      RegisterSpec registerSpec = param1Insn.getResult();
      if (registerSpec != null)
        processReg(registerSpec); 
      RegisterSpecList registerSpecList = param1Insn.getSources();
      int j = registerSpecList.size();
      for (int i = 0; i < j; i++)
        processReg(registerSpecList.get(i)); 
    }
    
    public int getRegCount() {
      return this.regCount;
    }
    
    public void visitFillArrayDataInsn(FillArrayDataInsn param1FillArrayDataInsn) {
      visit(param1FillArrayDataInsn);
    }
    
    public void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn) {
      visit(param1PlainCstInsn);
    }
    
    public void visitPlainInsn(PlainInsn param1PlainInsn) {
      visit(param1PlainInsn);
    }
    
    public void visitSwitchInsn(SwitchInsn param1SwitchInsn) {
      visit(param1SwitchInsn);
    }
    
    public void visitThrowingCstInsn(ThrowingCstInsn param1ThrowingCstInsn) {
      visit(param1ThrowingCstInsn);
    }
    
    public void visitThrowingInsn(ThrowingInsn param1ThrowingInsn) {
      visit(param1ThrowingInsn);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\BasicBlockList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */