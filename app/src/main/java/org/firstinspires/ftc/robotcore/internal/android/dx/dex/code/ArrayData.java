package org.firstinspires.ftc.robotcore.internal.android.dx.dex.code;

import java.util.ArrayList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.RegisterSpecList;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.code.SourcePosition;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.Constant;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteral32;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstLiteral64;
import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstType;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.AnnotatedOutput;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class ArrayData extends VariableSizeInsn {
  private final Constant arrayType;
  
  private final int elemWidth;
  
  private final int initLength;
  
  private final CodeAddress user;
  
  private final ArrayList<Constant> values;
  
  public ArrayData(SourcePosition paramSourcePosition, CodeAddress paramCodeAddress, ArrayList<Constant> paramArrayList, Constant paramConstant) {
    super(paramSourcePosition, RegisterSpecList.EMPTY);
    if (paramCodeAddress != null) {
      if (paramArrayList != null) {
        if (paramArrayList.size() > 0) {
          this.arrayType = paramConstant;
          if (paramConstant == CstType.BYTE_ARRAY || paramConstant == CstType.BOOLEAN_ARRAY) {
            this.elemWidth = 1;
          } else if (paramConstant == CstType.SHORT_ARRAY || paramConstant == CstType.CHAR_ARRAY) {
            this.elemWidth = 2;
          } else if (paramConstant == CstType.INT_ARRAY || paramConstant == CstType.FLOAT_ARRAY) {
            this.elemWidth = 4;
          } else if (paramConstant == CstType.LONG_ARRAY || paramConstant == CstType.DOUBLE_ARRAY) {
            this.elemWidth = 8;
          } else {
            throw new IllegalArgumentException("Unexpected constant type");
          } 
          this.user = paramCodeAddress;
          this.values = paramArrayList;
          this.initLength = paramArrayList.size();
          return;
        } 
        throw new IllegalArgumentException("Illegal number of init values");
      } 
      throw new NullPointerException("values == null");
    } 
    throw new NullPointerException("user == null");
  }
  
  protected String argString() {
    StringBuffer stringBuffer = new StringBuffer(100);
    int j = this.values.size();
    for (int i = 0; i < j; i++) {
      stringBuffer.append("\n    ");
      stringBuffer.append(i);
      stringBuffer.append(": ");
      stringBuffer.append(((Constant)this.values.get(i)).toHuman());
    } 
    return stringBuffer.toString();
  }
  
  public int codeSize() {
    return (this.initLength * this.elemWidth + 1) / 2 + 4;
  }
  
  protected String listingString0(boolean paramBoolean) {
    int i = this.user.getAddress();
    StringBuffer stringBuffer = new StringBuffer(100);
    int j = this.values.size();
    stringBuffer.append("fill-array-data-payload // for fill-array-data @ ");
    stringBuffer.append(Hex.u2(i));
    for (i = 0; i < j; i++) {
      stringBuffer.append("\n  ");
      stringBuffer.append(i);
      stringBuffer.append(": ");
      stringBuffer.append(((Constant)this.values.get(i)).toHuman());
    } 
    return stringBuffer.toString();
  }
  
  public DalvInsn withRegisters(RegisterSpecList paramRegisterSpecList) {
    return new ArrayData(getPosition(), this.user, this.values, this.arrayType);
  }
  
  public void writeTo(AnnotatedOutput paramAnnotatedOutput) {
    int j = this.values.size();
    paramAnnotatedOutput.writeShort(768);
    paramAnnotatedOutput.writeShort(this.elemWidth);
    paramAnnotatedOutput.writeInt(this.initLength);
    int i = this.elemWidth;
    if (i != 1) {
      if (i != 2) {
        if (i != 4) {
          if (i == 8)
            for (i = 0; i < j; i++)
              paramAnnotatedOutput.writeLong(((CstLiteral64)this.values.get(i)).getLongBits());  
        } else {
          for (i = 0; i < j; i++)
            paramAnnotatedOutput.writeInt(((CstLiteral32)this.values.get(i)).getIntBits()); 
        } 
      } else {
        for (i = 0; i < j; i++)
          paramAnnotatedOutput.writeShort((short)((CstLiteral32)this.values.get(i)).getIntBits()); 
      } 
    } else {
      for (i = 0; i < j; i++)
        paramAnnotatedOutput.writeByte((byte)((CstLiteral32)this.values.get(i)).getIntBits()); 
    } 
    if (this.elemWidth == 1 && j % 2 != 0)
      paramAnnotatedOutput.writeByte(0); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\dex\code\ArrayData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */