package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;

public final class CstInsn extends FixedSizeInsn {
  private int classIndex;
  
  private final Constant constant;
  
  private int index;
  
  public CstInsn(Dop paramDop, SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList, Constant paramConstant) {
    super(paramDop, paramSourcePosition, paramRegisterSpecList);
    if (paramConstant != null) {
      this.constant = paramConstant;
      this.index = -1;
      this.classIndex = -1;
      return;
    } 
    throw new NullPointerException("constant == null");
  }
  
  protected String argString() {
    return this.constant.toHuman();
  }
  
  public int getClassIndex() {
    int i = this.classIndex;
    if (i >= 0)
      return i; 
    throw new RuntimeException("class index not yet set");
  }
  
  public Constant getConstant() {
    return this.constant;
  }
  
  public int getIndex() {
    int i = this.index;
    if (i >= 0)
      return i; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("index not yet set for ");
    stringBuilder.append(this.constant);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public boolean hasClassIndex() {
    return (this.classIndex >= 0);
  }
  
  public boolean hasIndex() {
    return (this.index >= 0);
  }
  
  public void setClassIndex(int paramInt) {
    if (paramInt >= 0) {
      if (this.classIndex < 0) {
        this.classIndex = paramInt;
        return;
      } 
      throw new RuntimeException("class index already set");
    } 
    throw new IllegalArgumentException("index < 0");
  }
  
  public void setIndex(int paramInt) {
    if (paramInt >= 0) {
      if (this.index < 0) {
        this.index = paramInt;
        return;
      } 
      throw new RuntimeException("index already set");
    } 
    throw new IllegalArgumentException("index < 0");
  }
  
  public DalvInsn withOpcode(Dop paramDop) {
    CstInsn cstInsn = new CstInsn(paramDop, getPosition(), getRegisters(), this.constant);
    int i = this.index;
    if (i >= 0)
      cstInsn.setIndex(i); 
    i = this.classIndex;
    if (i >= 0)
      cstInsn.setClassIndex(i); 
    return cstInsn;
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    CstInsn cstInsn = new CstInsn(getOpcode(), getPosition(), paramRegisterSpecList, this.constant);
    int i = this.index;
    if (i >= 0)
      cstInsn.setIndex(i); 
    i = this.classIndex;
    if (i >= 0)
      cstInsn.setClassIndex(i); 
    return cstInsn;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\CstInsn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */