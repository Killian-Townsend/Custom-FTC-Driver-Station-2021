package org.firstinspires.ftc.robotcore.internal.android.dx.ssa;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IntList;

public class BasicRegisterMapper extends RegisterMapper {
  private IntList oldToNew;
  
  private int runningCountNewRegisters;
  
  public BasicRegisterMapper(int paramInt) {
    this.oldToNew = new IntList(paramInt);
  }
  
  public void addMapping(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 >= this.oldToNew.size()) {
      int i;
      for (i = paramInt1 - this.oldToNew.size(); i >= 0; i--)
        this.oldToNew.add(-1); 
    } 
    this.oldToNew.set(paramInt1, paramInt2);
    paramInt1 = this.runningCountNewRegisters;
    paramInt2 += paramInt3;
    if (paramInt1 < paramInt2)
      this.runningCountNewRegisters = paramInt2; 
  }
  
  public int getNewRegisterCount() {
    return this.runningCountNewRegisters;
  }
  
  public RegisterSpec map(RegisterSpec paramRegisterSpec) {
    byte b;
    if (paramRegisterSpec == null)
      return null; 
    try {
      b = this.oldToNew.get(paramRegisterSpec.getReg());
    } catch (IndexOutOfBoundsException indexOutOfBoundsException) {
      b = -1;
    } 
    if (b >= 0)
      return paramRegisterSpec.withReg(b); 
    throw new RuntimeException("no mapping specified for register");
  }
  
  public int oldToNew(int paramInt) {
    return (paramInt >= this.oldToNew.size()) ? -1 : this.oldToNew.get(paramInt);
  }
  
  public String toHuman() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Old\tNew\n");
    int j = this.oldToNew.size();
    for (int i = 0; i < j; i++) {
      stringBuilder.append(i);
      stringBuilder.append('\t');
      stringBuilder.append(this.oldToNew.get(i));
      stringBuilder.append('\n');
    } 
    stringBuilder.append("new reg count:");
    stringBuilder.append(this.runningCountNewRegisters);
    stringBuilder.append('\n');
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\ssa\BasicRegisterMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */