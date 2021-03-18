package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Field;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdField;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdFieldList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

final class FieldListParser extends MemberListParser {
  private final StdFieldList fields = new StdFieldList(getCount());
  
  public FieldListParser(DirectClassFile paramDirectClassFile, CstType paramCstType, int paramInt, AttributeFactory paramAttributeFactory) {
    super(paramDirectClassFile, paramCstType, paramInt, paramAttributeFactory);
  }
  
  protected int getAttributeContext() {
    return 1;
  }
  
  public StdFieldList getList() {
    parseIfNecessary();
    return this.fields;
  }
  
  protected String humanAccessFlags(int paramInt) {
    return AccessFlags.fieldString(paramInt);
  }
  
  protected String humanName() {
    return "field";
  }
  
  protected Member set(int paramInt1, int paramInt2, CstNat paramCstNat, AttributeList paramAttributeList) {
    StdField stdField = new StdField(getDefiner(), paramInt2, paramCstNat, paramAttributeList);
    this.fields.set(paramInt1, (Field)stdField);
    return (Member)stdField;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\FieldListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */