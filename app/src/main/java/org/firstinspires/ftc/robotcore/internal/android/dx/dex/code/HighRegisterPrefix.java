package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpec;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;

public final class HighRegisterPrefix extends VariableSizeInsn {
  private SimpleInsn[] insns;
  
  public HighRegisterPrefix(SourcePosition paramSourcePosition, RegisterSpecList paramRegisterSpecList) {
    super(paramSourcePosition, paramRegisterSpecList);
    if (paramRegisterSpecList.size() != 0) {
      this.insns = null;
      return;
    } 
    throw new IllegalArgumentException("registers.size() == 0");
  }
  
  private void calculateInsnsIfNecessary() {
    if (this.insns != null)
      return; 
    RegisterSpecList registerSpecList = getRegisters();
    int k = registerSpecList.size();
    this.insns = new SimpleInsn[k];
    int i = 0;
    int j = 0;
    while (i < k) {
      RegisterSpec registerSpec = registerSpecList.get(i);
      this.insns[i] = moveInsnFor(registerSpec, j);
      j += registerSpec.getCategory();
      i++;
    } 
  }
  
  private static SimpleInsn moveInsnFor(RegisterSpec paramRegisterSpec, int paramInt) {
    return DalvInsn.makeMove(SourcePosition.NO_INFO, RegisterSpec.make(paramInt, (TypeBearer)paramRegisterSpec.getType()), paramRegisterSpec);
  }
  
  protected String argString() {
    return null;
  }
  
  public int codeSize() {
    calculateInsnsIfNecessary();
    SimpleInsn[] arrayOfSimpleInsn = this.insns;
    int k = arrayOfSimpleInsn.length;
    int i = 0;
    int j = 0;
    while (i < k) {
      j += arrayOfSimpleInsn[i].codeSize();
      i++;
    } 
    return j;
  }
  
  protected String listingString0(boolean paramBoolean) {
    RegisterSpecList registerSpecList = getRegisters();
    int k = registerSpecList.size();
    StringBuffer stringBuffer = new StringBuffer(100);
    int i = 0;
    int j = 0;
    while (i < k) {
      RegisterSpec registerSpec = registerSpecList.get(i);
      SimpleInsn simpleInsn = moveInsnFor(registerSpec, j);
      if (i != 0)
        stringBuffer.append('\n'); 
      stringBuffer.append(simpleInsn.listingString0(paramBoolean));
      j += registerSpec.getCategory();
      i++;
    } 
    return stringBuffer.toString();
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new HighRegisterPrefix(getPosition(), paramRegisterSpecList);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput) {
    calculateInsnsIfNecessary();
    SimpleInsn[] arrayOfSimpleInsn = this.insns;
    int j = arrayOfSimpleInsn.length;
    for (int i = 0; i < j; i++)
      arrayOfSimpleInsn[i].writeTo(paramAnnotatedOutput); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\HighRegisterPrefix.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */