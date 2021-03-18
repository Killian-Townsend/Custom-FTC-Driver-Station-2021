package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.LabeledItem;

public final class BasicBlock implements LabeledItem {
  private final InsnList insns;
  
  private final int label;
  
  private final int primarySuccessor;
  
  private final IntList successors;
  
  public BasicBlock(int paramInt1, InsnList paramInsnList, IntList paramIntList, int paramInt2) {
    if (paramInt1 >= 0)
      try {
        paramInsnList.throwIfMutable();
        int i = paramInsnList.size();
        if (i != 0) {
          StringBuilder stringBuilder;
          int j = i - 2;
          while (j >= 0) {
            if (paramInsnList.get(j).getOpcode().getBranchingness() == 1) {
              j--;
              continue;
            } 
            stringBuilder = new StringBuilder();
            stringBuilder.append("insns[");
            stringBuilder.append(j);
            stringBuilder.append("] is a branch or can throw");
            throw new IllegalArgumentException(stringBuilder.toString());
          } 
          if (stringBuilder.get(i - 1).getOpcode().getBranchingness() != 1)
            try {
              paramIntList.throwIfMutable();
              if (paramInt2 >= -1) {
                if (paramInt2 < 0 || paramIntList.contains(paramInt2)) {
                  this.label = paramInt1;
                  this.insns = (InsnList)stringBuilder;
                  this.successors = paramIntList;
                  this.primarySuccessor = paramInt2;
                  return;
                } 
                stringBuilder = new StringBuilder();
                stringBuilder.append("primarySuccessor ");
                stringBuilder.append(paramInt2);
                stringBuilder.append(" not in successors ");
                stringBuilder.append(paramIntList);
                throw new IllegalArgumentException(stringBuilder.toString());
              } 
              throw new IllegalArgumentException("primarySuccessor < -1");
            } catch (NullPointerException nullPointerException) {
              throw new NullPointerException("successors == null");
            }  
          throw new IllegalArgumentException("insns does not end with a branch or throwing instruction");
        } 
        throw new IllegalArgumentException("insns.size() == 0");
      } catch (NullPointerException nullPointerException) {
        throw new NullPointerException("insns == null");
      }  
    throw new IllegalArgumentException("label < 0");
  }
  
  public boolean canThrow() {
    return this.insns.getLast().canThrow();
  }
  
  public boolean equals(Object paramObject) {
    return (this == paramObject);
  }
  
  public TypeList getExceptionHandlerTypes() {
    return this.insns.getLast().getCatches();
  }
  
  public Insn getFirstInsn() {
    return this.insns.get(0);
  }
  
  public InsnList getInsns() {
    return this.insns;
  }
  
  public int getLabel() {
    return this.label;
  }
  
  public Insn getLastInsn() {
    return this.insns.getLast();
  }
  
  public int getPrimarySuccessor() {
    return this.primarySuccessor;
  }
  
  public int getSecondarySuccessor() {
    if (this.successors.size() == 2) {
      int j = this.successors.get(0);
      int i = j;
      if (j == this.primarySuccessor)
        i = this.successors.get(1); 
      return i;
    } 
    throw new UnsupportedOperationException("block doesn't have exactly two successors");
  }
  
  public IntList getSuccessors() {
    return this.successors;
  }
  
  public boolean hasExceptionHandlers() {
    return (this.insns.getLast().getCatches().size() != 0);
  }
  
  public int hashCode() {
    return System.identityHashCode(this);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('{');
    stringBuilder.append(Hex.u2(this.label));
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public BasicBlock withRegisterOffset(int paramInt) {
    return new BasicBlock(this.label, this.insns.withRegisterOffset(paramInt), this.successors, this.primarySuccessor);
  }
  
  public static interface Visitor {
    void visitBlock(BasicBlock param1BasicBlock);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\BasicBlock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */