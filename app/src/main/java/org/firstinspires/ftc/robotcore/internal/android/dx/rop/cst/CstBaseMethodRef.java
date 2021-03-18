package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public abstract class CstBaseMethodRef extends CstMemberRef {
  private Prototype instancePrototype = null;
  
  private final Prototype prototype = Prototype.intern(getNat().getDescriptor().getString());
  
  CstBaseMethodRef(CstType paramCstType, CstNat paramCstNat) {
    super(paramCstType, paramCstNat);
  }
  
  protected final int compareTo0(Constant paramConstant) {
    int i = super.compareTo0(paramConstant);
    if (i != 0)
      return i; 
    paramConstant = paramConstant;
    return this.prototype.compareTo(((CstBaseMethodRef)paramConstant).prototype);
  }
  
  public final int getParameterWordCount(boolean paramBoolean) {
    return getPrototype(paramBoolean).getParameterTypes().getWordCount();
  }
  
  public final Prototype getPrototype() {
    return this.prototype;
  }
  
  public final Prototype getPrototype(boolean paramBoolean) {
    if (paramBoolean)
      return this.prototype; 
    if (this.instancePrototype == null) {
      Type type = getDefiningClass().getClassType();
      this.instancePrototype = this.prototype.withFirstParameter(type);
    } 
    return this.instancePrototype;
  }
  
  public final Type getType() {
    return this.prototype.getReturnType();
  }
  
  public final boolean isClassInit() {
    return getNat().isClassInit();
  }
  
  public final boolean isInstanceInit() {
    return getNat().isInstanceInit();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\CstBaseMethodRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */