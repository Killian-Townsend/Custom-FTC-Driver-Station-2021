package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.util.FixedSizeList;

public final class InsnList extends FixedSizeList {
  public InsnList(int paramInt) {
    super(paramInt);
  }
  
  public boolean contentEquals(InsnList paramInsnList) {
    if (paramInsnList == null)
      return false; 
    int j = size();
    if (j != paramInsnList.size())
      return false; 
    for (int i = 0; i < j; i++) {
      if (!get(i).contentEquals(paramInsnList.get(i)))
        return false; 
    } 
    return true;
  }
  
  public void forEach(Insn.Visitor paramVisitor) {
    int j = size();
    for (int i = 0; i < j; i++)
      get(i).accept(paramVisitor); 
  }
  
  public Insn get(int paramInt) {
    return (Insn)get0(paramInt);
  }
  
  public Insn getLast() {
    return get(size() - 1);
  }
  
  public void set(int paramInt, Insn paramInsn) {
    set0(paramInt, paramInsn);
  }
  
  public InsnList withRegisterOffset(int paramInt) {
    int j = size();
    InsnList insnList = new InsnList(j);
    for (int i = 0; i < j; i++) {
      Insn insn = (Insn)get0(i);
      if (insn != null)
        insnList.set0(i, insn.withRegisterOffset(paramInt)); 
    } 
    if (isImmutable())
      insnList.setImmutable(); 
    return insnList;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\InsnList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */