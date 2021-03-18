package org.firstinspires.ftc.robotcore.internal.android.dx.cf.direct;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ByteOps;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.BytecodeArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.SwitchList;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstDouble;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstFloat;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLong;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.type.Type;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public class CodeObserver implements BytecodeArray.Visitor {
  private final ByteArray bytes;
  
  private final ParseObserver observer;
  
  public CodeObserver(ByteArray paramByteArray, ParseObserver paramParseObserver) {
    if (paramByteArray != null) {
      if (paramParseObserver != null) {
        this.bytes = paramByteArray;
        this.observer = paramParseObserver;
        return;
      } 
      throw new NullPointerException("observer == null");
    } 
    throw new NullPointerException("bytes == null");
  }
  
  private String header(int paramInt) {
    int i = this.bytes.getUnsignedByte(paramInt);
    String str2 = ByteOps.opName(i);
    String str1 = str2;
    if (i == 196) {
      i = this.bytes.getUnsignedByte(paramInt + 1);
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(str2);
      stringBuilder1.append(" ");
      stringBuilder1.append(ByteOps.opName(i));
      str1 = stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(Hex.u2(paramInt));
    stringBuilder.append(": ");
    stringBuilder.append(str1);
    return stringBuilder.toString();
  }
  
  private void visitLiteralDouble(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
    String str;
    if (paramInt3 != 1) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(" #");
      stringBuilder1.append(Hex.u8(paramLong));
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(str);
    stringBuilder.append(" // ");
    stringBuilder.append(Double.longBitsToDouble(paramLong));
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  private void visitLiteralFloat(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    String str;
    if (paramInt3 != 1) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(" #");
      stringBuilder1.append(Hex.u4(paramInt4));
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(str);
    stringBuilder.append(" // ");
    stringBuilder.append(Float.intBitsToFloat(paramInt4));
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  private void visitLiteralInt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    String str1;
    String str2;
    if (paramInt3 == 1) {
      str2 = " // ";
    } else {
      str2 = " ";
    } 
    paramInt1 = this.bytes.getUnsignedByte(paramInt2);
    if (paramInt3 == 1 || paramInt1 == 16) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("#");
      stringBuilder1.append(Hex.s1(paramInt4));
      str1 = stringBuilder1.toString();
    } else if (paramInt1 == 17) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("#");
      stringBuilder1.append(Hex.s2(paramInt4));
      str1 = stringBuilder1.toString();
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("#");
      stringBuilder1.append(Hex.s4(paramInt4));
      str1 = stringBuilder1.toString();
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(str2);
    stringBuilder.append(str1);
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  private void visitLiteralLong(int paramInt1, int paramInt2, int paramInt3, long paramLong) {
    String str1;
    String str2;
    if (paramInt3 == 1) {
      str1 = " // ";
    } else {
      str1 = " #";
    } 
    if (paramInt3 == 1) {
      str2 = Hex.s1((int)paramLong);
    } else {
      str2 = Hex.s8(paramLong);
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(str1);
    stringBuilder.append(str2);
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  public int getPreviousOffset() {
    return -1;
  }
  
  public void setPreviousOffset(int paramInt) {}
  
  public void visitBranch(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    String str;
    if (paramInt3 <= 3) {
      str = Hex.u2(paramInt4);
    } else {
      str = Hex.u4(paramInt4);
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(" ");
    stringBuilder.append(str);
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  public void visitConstant(int paramInt1, int paramInt2, int paramInt3, Constant paramConstant, int paramInt4) {
    String str;
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstKnownNull) {
      visitNoArgs(paramInt1, paramInt2, paramInt3, null);
      return;
    } 
    if (paramConstant instanceof org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstInteger) {
      visitLiteralInt(paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (paramConstant instanceof CstLong) {
      visitLiteralLong(paramInt1, paramInt2, paramInt3, ((CstLong)paramConstant).getValue());
      return;
    } 
    if (paramConstant instanceof CstFloat) {
      visitLiteralFloat(paramInt1, paramInt2, paramInt3, ((CstFloat)paramConstant).getIntBits());
      return;
    } 
    if (paramConstant instanceof CstDouble) {
      visitLiteralDouble(paramInt1, paramInt2, paramInt3, ((CstDouble)paramConstant).getLongBits());
      return;
    } 
    if (paramInt4 != 0) {
      if (paramInt1 == 197) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(", ");
        stringBuilder1.append(Hex.u1(paramInt4));
        str = stringBuilder1.toString();
      } else {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(", ");
        stringBuilder1.append(Hex.u2(paramInt4));
        str = stringBuilder1.toString();
      } 
    } else {
      str = "";
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    stringBuilder.append(" ");
    stringBuilder.append(paramConstant);
    stringBuilder.append(str);
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  public void visitInvalid(int paramInt1, int paramInt2, int paramInt3) {
    this.observer.parsed(this.bytes, paramInt2, paramInt3, header(paramInt2));
  }
  
  public void visitLocal(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Type paramType, int paramInt5) {
    String str1;
    String str2;
    String str3;
    if (paramInt3 <= 3) {
      str2 = Hex.u1(paramInt4);
    } else {
      str2 = Hex.u2(paramInt4);
    } 
    paramInt4 = 1;
    if (paramInt3 != 1)
      paramInt4 = 0; 
    String str4 = "";
    if (paramInt1 == 132) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(", #");
      if (paramInt3 <= 3) {
        str3 = Hex.s1(paramInt5);
      } else {
        str3 = Hex.s2(paramInt5);
      } 
      stringBuilder1.append(str3);
      str3 = stringBuilder1.toString();
    } else {
      str3 = "";
    } 
    if (paramType.isCategory2()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      if (paramInt4 != 0) {
        str1 = ",";
      } else {
        str1 = " //";
      } 
      stringBuilder1.append(str1);
      stringBuilder1.append(" category-2");
      str4 = stringBuilder1.toString();
    } 
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt2));
    if (paramInt4 != 0) {
      str1 = " // ";
    } else {
      str1 = " ";
    } 
    stringBuilder.append(str1);
    stringBuilder.append(str2);
    stringBuilder.append(str3);
    stringBuilder.append(str4);
    parseObserver.parsed(byteArray, paramInt2, paramInt3, stringBuilder.toString());
  }
  
  public void visitNewarray(int paramInt1, int paramInt2, CstType paramCstType, ArrayList<Constant> paramArrayList) {
    String str2;
    if (paramInt2 == 1) {
      str2 = " // ";
    } else {
      str2 = " ";
    } 
    String str1 = paramCstType.getClassType().getComponentType().toHuman();
    ParseObserver parseObserver = this.observer;
    ByteArray byteArray = this.bytes;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(header(paramInt1));
    stringBuilder.append(str2);
    stringBuilder.append(str1);
    parseObserver.parsed(byteArray, paramInt1, paramInt2, stringBuilder.toString());
  }
  
  public void visitNoArgs(int paramInt1, int paramInt2, int paramInt3, Type paramType) {
    this.observer.parsed(this.bytes, paramInt2, paramInt3, header(paramInt2));
  }
  
  public void visitSwitch(int paramInt1, int paramInt2, int paramInt3, SwitchList paramSwitchList, int paramInt4) {
    int i = paramSwitchList.size();
    StringBuffer stringBuffer = new StringBuffer(i * 20 + 100);
    stringBuffer.append(header(paramInt2));
    if (paramInt4 != 0) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(" // padding: ");
      stringBuilder.append(Hex.u4(paramInt4));
      stringBuffer.append(stringBuilder.toString());
    } 
    stringBuffer.append('\n');
    for (paramInt1 = 0; paramInt1 < i; paramInt1++) {
      stringBuffer.append("  ");
      stringBuffer.append(Hex.s4(paramSwitchList.getValue(paramInt1)));
      stringBuffer.append(": ");
      stringBuffer.append(Hex.u2(paramSwitchList.getTarget(paramInt1)));
      stringBuffer.append('\n');
    } 
    stringBuffer.append("  default: ");
    stringBuffer.append(Hex.u2(paramSwitchList.getDefaultTarget()));
    this.observer.parsed(this.bytes, paramInt2, paramInt3, stringBuffer.toString());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\cf\direct\CodeObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */