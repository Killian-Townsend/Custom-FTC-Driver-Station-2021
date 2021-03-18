package org.firstinspires.ftc.robotcore.internal.android.dx.cf.attrib;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.TypedConstant;

public final class AttConstantValue extends BaseAttribute {
  public static final String ATTRIBUTE_NAME = "ConstantValue";
  
  private final TypedConstant constantValue;
  
  public AttConstantValue(TypedConstant paramTypedConstant) {
    super("ConstantValue");
    if (!(paramTypedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString) && !(paramTypedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger) && !(paramTypedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLong) && !(paramTypedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFloat) && !(paramTypedConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstDouble)) {
      if (paramTypedConstant == null)
        throw new NullPointerException("constantValue == null"); 
      throw new IllegalArgumentException("bad type for constantValue");
    } 
    this.constantValue = paramTypedConstant;
  }
  
  public int byteLength() {
    return 8;
  }
  
  public TypedConstant getConstantValue() {
    return this.constantValue;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\attrib\AttConstantValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */