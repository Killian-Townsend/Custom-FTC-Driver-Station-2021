package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import java.util.ArrayList;
import java.util.HashMap;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecSet;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.MutabilityControl;

public class LocalVariableInfo extends MutabilityControl {
  private final RegisterSpecSet[] blockStarts;
  
  private final RegisterSpecSet emptySet;
  
  private final HashMap<SsaInsn, RegisterSpec> insnAssignments;
  
  private final int regCount;
  
  public LocalVariableInfo(SsaMethod paramSsaMethod) {
    if (paramSsaMethod != null) {
      ArrayList<SsaBasicBlock> arrayList = paramSsaMethod.getBlocks();
      this.regCount = paramSsaMethod.getRegCount();
      this.emptySet = new RegisterSpecSet(this.regCount);
      this.blockStarts = new RegisterSpecSet[arrayList.size()];
      this.insnAssignments = new HashMap<SsaInsn, RegisterSpec>();
      this.emptySet.setImmutable();
      return;
    } 
    throw new NullPointerException("method == null");
  }
  
  private RegisterSpecSet getStarts0(int paramInt) {
    try {
      return this.blockStarts[paramInt];
    } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
      throw new IllegalArgumentException("bogus index");
    } 
  }
  
  public void addAssignment(SsaInsn paramSsaInsn, RegisterSpec paramRegisterSpec) {
    throwIfImmutable();
    if (paramSsaInsn != null) {
      if (paramRegisterSpec != null) {
        this.insnAssignments.put(paramSsaInsn, paramRegisterSpec);
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
  
  public RegisterSpec getAssignment(SsaInsn paramSsaInsn) {
    return this.insnAssignments.get(paramSsaInsn);
  }
  
  public int getAssignmentCount() {
    return this.insnAssignments.size();
  }
  
  public RegisterSpecSet getStarts(int paramInt) {
    RegisterSpecSet registerSpecSet = getStarts0(paramInt);
    return (registerSpecSet != null) ? registerSpecSet : this.emptySet;
  }
  
  public RegisterSpecSet getStarts(SsaBasicBlock paramSsaBasicBlock) {
    return getStarts(paramSsaBasicBlock.getIndex());
  }
  
  public boolean mergeStarts(int paramInt, RegisterSpecSet paramRegisterSpecSet) {
    RegisterSpecSet registerSpecSet1 = getStarts0(paramInt);
    if (registerSpecSet1 == null) {
      setStarts(paramInt, paramRegisterSpecSet);
      return true;
    } 
    RegisterSpecSet registerSpecSet2 = registerSpecSet1.mutableCopy();
    registerSpecSet2.intersect(paramRegisterSpecSet, true);
    if (registerSpecSet1.equals(registerSpecSet2))
      return false; 
    registerSpecSet2.setImmutable();
    setStarts(paramInt, registerSpecSet2);
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
        throw new IllegalArgumentException("bogus index");
      }  
    throw new NullPointerException("specs == null");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\LocalVariableInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */