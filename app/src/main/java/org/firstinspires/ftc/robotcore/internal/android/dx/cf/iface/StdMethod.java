package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Prototype;

public final class StdMethod extends StdMember implements Method {
  private final Prototype effectiveDescriptor;
  
  public StdMethod(CstType paramCstType, int paramInt, CstNat paramCstNat, AttributeList paramAttributeList) {
    super(paramCstType, paramInt, paramCstNat, paramAttributeList);
    this.effectiveDescriptor = Prototype.intern(getDescriptor().getString(), paramCstType.getClassType(), AccessFlags.isStatic(paramInt), paramCstNat.isInstanceInit());
  }
  
  public Prototype getEffectiveDescriptor() {
    return this.effectiveDescriptor;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdMethod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */