package org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;

public final class Zeroes {
  public static Constant zeroFor(Type paramType) {
    StringBuilder stringBuilder;
    switch (paramType.getBasicType()) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("no zero for type: ");
        stringBuilder.append(paramType.toHuman());
        throw new UnsupportedOperationException(stringBuilder.toString());
      case 9:
        return CstKnownNull.THE_ONE;
      case 8:
        return CstShort.VALUE_0;
      case 7:
        return CstLong.VALUE_0;
      case 6:
        return CstInteger.VALUE_0;
      case 5:
        return CstFloat.VALUE_0;
      case 4:
        return CstDouble.VALUE_0;
      case 3:
        return CstChar.VALUE_0;
      case 2:
        return CstByte.VALUE_0;
      case 1:
        break;
    } 
    return CstBoolean.VALUE_FALSE;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\cst\Zeroes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */