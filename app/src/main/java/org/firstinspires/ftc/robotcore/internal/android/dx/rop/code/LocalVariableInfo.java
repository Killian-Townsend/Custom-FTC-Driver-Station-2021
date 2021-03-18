package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityControl;

public final class LocalVariableInfo extends MutabilityControl {
  private final RegisterSpecSet[] blockStarts;
  
  private final RegisterSpecSet emptySet;
  
  private final HashMap<Insn, RegisterSpec> insnAssignments;
  
  private final int regCount;
  
  public LocalVariableInfo(RopMethod paramRopMethod) {
    if (paramRopMethod != null) {
      BasicBlockList basicBlockList = paramRopMethod.getBlocks();
      int i = basicBlockList.getMaxLabel();
      this.regCount = basicBlockList.getRegCount();
      this.emptySet = new RegisterSpecSet(this.regCount);
      this.blockStarts = new RegisterSpecSet[i];
      this.insnAssignments = new HashMap<Insn, RegisterSpec>(basicBlockList.getInstructionCount());
      this.emptySet.setImmutable();
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  private RegisterSpecSet getStarts0(int paramInt) {
    try {
      return this.blockStarts[paramInt];
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new IllegalArgumentException("bogus label");
    } 
  }
  
  public void addAssignment(Insn paramInsn, RegisterSpec paramRegisterSpec) {
    throwIfImmutable();
    if (paramInsn != null) {
      if (paramRegisterSpec != null) {
        this.insnAssignments.put(paramInsn, paramRegisterSpec);
        return;
      } 
      throw new NullPointerException("spec == null");
    } 
    throw new NullPointerException("insn == null");
  }
  
  public void debugDump() {
    int i = 0;
    while (true) {
      RegisterSpecSet[] arrayOfRegisterSpecSet = this.blockStarts;
      if (i < arrayOfRegisterSpecSet.length) {
        if (arrayOfRegisterSpecSet[i] != null)
          if (arrayOfRegisterSpecSet[i] == this.emptySet) {
            System.out.printf("%04x: empty set\n", new Object[] { Integer.valueOf(i) });
          } else {
            System.out.printf("%04x: %s\n", new Object[] { Integer.valueOf(i), this.blockStarts[i] });
          }  
        i++;
        continue;
      } 
      break;
    } 
  }
  
  public RegisterSpec getAssignment(Insn paramInsn) {
    return this.insnAssignments.get(paramInsn);
  }
  
  public int getAssignmentCount() {
    return this.insnAssignments.size();
  }
  
  public RegisterSpecSet getStarts(int paramInt) {
    RegisterSpecSet registerSpecSet = getStarts0(paramInt);
    return (registerSpecSet != null) ? registerSpecSet : this.emptySet;
  }
  
  public RegisterSpecSet getStarts(BasicBlock paramBasicBlock) {
    return getStarts(paramBasicBlock.getLabel());
  }
  
  public boolean mergeStarts(int paramInt, RegisterSpecSet paramRegisterSpecSet) {
    RegisterSpecSet registerSpecSet2 = getStarts0(paramInt);
    if (registerSpecSet2 == null) {
      setStarts(paramInt, paramRegisterSpecSet);
      return true;
    } 
    RegisterSpecSet registerSpecSet1 = registerSpecSet2.mutableCopy();
    if (registerSpecSet2.size() != 0) {
      registerSpecSet1.intersect(paramRegisterSpecSet, true);
      paramRegisterSpecSet = registerSpecSet1;
    } else {
      paramRegisterSpecSet = paramRegisterSpecSet.mutableCopy();
    } 
    if (registerSpecSet2.equals(paramRegisterSpecSet))
      return false; 
    paramRegisterSpecSet.setImmutable();
    setStarts(paramInt, paramRegisterSpecSet);
    return true;
  }
  
  public RegisterSpecSet mutableCopyOfStarts(int paramInt) {
    RegisterSpecSet registerSpecSet = getStarts0(paramInt);
    return (registerSpecSet != null) ? registerSpecSet.mutableCopy() : new RegisterSpecSet(this.regCount);
  }
  
  public void setStarts(int paramInt, RegisterSpecSet paramRegisterSpecSet) {
    throwIfImmutable();
    if (paramRegisterSpecSet != null)
      try {
        this.blockStarts[paramInt] = paramRegisterSpecSet;
        return;
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        throw new IllegalArgumentException("bogus label");
      }  
    throw new NullPointerException("specs == null");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\LocalVariableInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */