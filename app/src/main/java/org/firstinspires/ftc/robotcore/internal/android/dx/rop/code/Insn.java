package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.StdTypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ToHuman;

public abstract class Insn implements ToHuman {
  private final Rop opcode;
  
  private final SourcePosition position;
  
  private final RegisterSpec result;
  
  private final RegisterSpecList sources;
  
  public Insn(Rop paramRop, SourcePosition paramSourcePosition, RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList) {
    if (paramRop != null) {
      if (paramSourcePosition != null) {
        if (paramRegisterSpecList != null) {
          this.opcode = paramRop;
          this.position = paramSourcePosition;
          this.result = paramRegisterSpec;
          this.sources = paramRegisterSpecList;
          return;
        } 
        throw new NullPointerException("sources == null");
      } 
      throw new NullPointerException("position == null");
    } 
    throw new NullPointerException("opcode == null");
  }
  
  private static boolean equalsHandleNulls(Object paramObject1, Object paramObject2) {
    return (paramObject1 == paramObject2 || (paramObject1 != null && paramObject1.equals(paramObject2)));
  }
  
  public abstract void accept(Visitor paramVisitor);
  
  public final boolean canThrow() {
    return this.opcode.canThrow();
  }
  
  public boolean contentEquals(Insn paramInsn) {
    return (this.opcode == paramInsn.getOpcode() && this.position.equals(paramInsn.getPosition()) && getClass() == paramInsn.getClass() && equalsHandleNulls(this.result, paramInsn.getResult()) && equalsHandleNulls(this.sources, paramInsn.getSources()) && StdTypeList.equalContents(getCatches(), paramInsn.getCatches()));
  }
  
  public Insn copy() {
    return withRegisterOffset(0);
  }
  
  public final boolean equals(Object paramObject) {
    return (this == paramObject);
  }
  
  public abstract TypeList getCatches();
  
  public String getInlineString() {
    return null;
  }
  
  public final RegisterSpec getLocalAssignment() {
    RegisterSpec registerSpec;
    if (this.opcode.getOpcode() == 54) {
      registerSpec = this.sources.get(0);
    } else {
      registerSpec = this.result;
    } 
    return (registerSpec == null) ? null : ((registerSpec.getLocalItem() == null) ? null : registerSpec);
  }
  
  public final Rop getOpcode() {
    return this.opcode;
  }
  
  public final SourcePosition getPosition() {
    return this.position;
  }
  
  public final RegisterSpec getResult() {
    return this.result;
  }
  
  public final RegisterSpecList getSources() {
    return this.sources;
  }
  
  public final int hashCode() {
    return System.identityHashCode(this);
  }
  
  public String toHuman() {
    return toHumanWithInline(getInlineString());
  }
  
  protected final String toHumanWithInline(String paramString) {
    StringBuffer stringBuffer = new StringBuffer(80);
    stringBuffer.append(this.position);
    stringBuffer.append(": ");
    stringBuffer.append(this.opcode.getNickname());
    if (paramString != null) {
      stringBuffer.append("(");
      stringBuffer.append(paramString);
      stringBuffer.append(")");
    } 
    if (this.result == null) {
      stringBuffer.append(" .");
    } else {
      stringBuffer.append(" ");
      stringBuffer.append(this.result.toHuman());
    } 
    stringBuffer.append(" <-");
    int i = this.sources.size();
    if (i == 0) {
      stringBuffer.append(" .");
    } else {
      for (int j = 0; j < i; j++) {
        stringBuffer.append(" ");
        stringBuffer.append(this.sources.get(j).toHuman());
      } 
    } 
    return stringBuffer.toString();
  }
  
  public String toString() {
    return toStringWithInline(getInlineString());
  }
  
  protected final String toStringWithInline(String paramString) {
    StringBuffer stringBuffer = new StringBuffer(80);
    stringBuffer.append("Insn{");
    stringBuffer.append(this.position);
    stringBuffer.append(' ');
    stringBuffer.append(this.opcode);
    if (paramString != null) {
      stringBuffer.append(' ');
      stringBuffer.append(paramString);
    } 
    stringBuffer.append(" :: ");
    RegisterSpec registerSpec = this.result;
    if (registerSpec != null) {
      stringBuffer.append(registerSpec);
      stringBuffer.append(" <- ");
    } 
    stringBuffer.append(this.sources);
    stringBuffer.append('}');
    return stringBuffer.toString();
  }
  
  public abstract Insn withAddedCatch(Type paramType);
  
  public abstract Insn withNewRegisters(RegisterSpec paramRegisterSpec, RegisterSpecList paramRegisterSpecList);
  
  public abstract Insn withRegisterOffset(int paramInt);
  
  public Insn withSourceLiteral() {
    return this;
  }
  
  public static class BaseVisitor implements Visitor {
    public void visitFillArrayDataInsn(FillArrayDataInsn param1FillArrayDataInsn) {}
    
    public void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn) {}
    
    public void visitPlainInsn(PlainInsn param1PlainInsn) {}
    
    public void visitSwitchInsn(SwitchInsn param1SwitchInsn) {}
    
    public void visitThrowingCstInsn(ThrowingCstInsn param1ThrowingCstInsn) {}
    
    public void visitThrowingInsn(ThrowingInsn param1ThrowingInsn) {}
  }
  
  public static interface Visitor {
    void visitFillArrayDataInsn(FillArrayDataInsn param1FillArrayDataInsn);
    
    void visitPlainCstInsn(PlainCstInsn param1PlainCstInsn);
    
    void visitPlainInsn(PlainInsn param1PlainInsn);
    
    void visitSwitchInsn(SwitchInsn param1SwitchInsn);
    
    void visitThrowingCstInsn(ThrowingCstInsn param1ThrowingCstInsn);
    
    void visitThrowingInsn(ThrowingInsn param1ThrowingInsn);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\Insn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */