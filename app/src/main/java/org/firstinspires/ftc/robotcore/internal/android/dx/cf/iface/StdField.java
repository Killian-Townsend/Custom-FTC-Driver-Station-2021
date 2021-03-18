package org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib.AttConstantValue;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant;

public final class StdField extends StdMember implements Field {
  public StdField(CstType paramCstType, int paramInt, CstNat paramCstNat, AttributeList paramAttributeList) {
    super(paramCstType, paramInt, paramCstNat, paramAttributeList);
  }
  
  public TypedConstant getConstantValue() {
    AttConstantValue attConstantValue = (AttConstantValue)getAttributes().findFirst("ConstantValue");
    return (attConstantValue == null) ? null : attConstantValue.getConstantValue();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\iface\StdField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */