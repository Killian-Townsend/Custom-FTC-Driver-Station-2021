package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.TypeBearer;

public abstract class TypedConstant extends Constant implements TypeBearer {
  public final int getBasicFrameType() {
    return getType().getBasicFrameType();
  }
  
  public final int getBasicType() {
    return getType().getBasicType();
  }
  
  public final TypeBearer getFrameType() {
    return this;
  }
  
  public final boolean isConstant() {
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\TypedConstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */