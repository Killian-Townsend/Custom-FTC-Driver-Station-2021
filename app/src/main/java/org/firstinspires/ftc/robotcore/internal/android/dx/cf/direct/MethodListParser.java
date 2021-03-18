package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.AttributeList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Method;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.StdMethodList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.AccessFlags;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstNat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;

final class MethodListParser extends MemberListParser {
  private final StdMethodList methods = new StdMethodList(getCount());
  
  public MethodListParser(DirectClassFile paramDirectClassFile, CstType paramCstType, int paramInt, AttributeFactory paramAttributeFactory) {
    super(paramDirectClassFile, paramCstType, paramInt, paramAttributeFactory);
  }
  
  protected int getAttributeContext() {
    return 2;
  }
  
  public StdMethodList getList() {
    parseIfNecessary();
    return this.methods;
  }
  
  protected String humanAccessFlags(int paramInt) {
    return AccessFlags.methodString(paramInt);
  }
  
  protected String humanName() {
    return "method";
  }
  
  protected Member set(int paramInt1, int paramInt2, CstNat paramCstNat, AttributeList paramAttributeList) {
    StdMethod stdMethod = new StdMethod(getDefiner(), paramInt2, paramCstNat, paramAttributeList);
    this.methods.set(paramInt1, (Method)stdMethod);
    return (Member)stdMethod;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\MethodListParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */