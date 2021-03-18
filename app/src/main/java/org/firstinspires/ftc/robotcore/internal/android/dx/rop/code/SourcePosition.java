package org.firstinspires.ftc.robotcore.internal.android.dx.rop.code;

import org.firstinspires.ftc.robotcore.internal.android.dx.rop.cst.CstString;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;

public final class SourcePosition {
  public static final SourcePosition NO_INFO = new SourcePosition(null, -1, -1);
  
  private final int address;
  
  private final int line;
  
  private final CstString sourceFile;
  
  public SourcePosition(CstString paramCstString, int paramInt1, int paramInt2) {
    if (paramInt1 >= -1) {
      if (paramInt2 >= -1) {
        this.sourceFile = paramCstString;
        this.address = paramInt1;
        this.line = paramInt2;
        return;
      } 
      throw new IllegalArgumentException("line < -1");
    } 
    throw new IllegalArgumentException("address < -1");
  }
  
  public boolean equals(Object paramObject) {
    boolean bool = paramObject instanceof SourcePosition;
    boolean bool1 = false;
    if (!bool)
      return false; 
    if (this == paramObject)
      return true; 
    paramObject = paramObject;
    bool = bool1;
    if (this.address == ((SourcePosition)paramObject).address) {
      bool = bool1;
      if (sameLineAndFile((SourcePosition)paramObject))
        bool = true; 
    } 
    return bool;
  }
  
  public int getAddress() {
    return this.address;
  }
  
  public int getLine() {
    return this.line;
  }
  
  public CstString getSourceFile() {
    return this.sourceFile;
  }
  
  public int hashCode() {
    return this.sourceFile.hashCode() + this.address + this.line;
  }
  
  public boolean sameLine(SourcePosition paramSourcePosition) {
    return (this.line == paramSourcePosition.line);
  }
  
  public boolean sameLineAndFile(SourcePosition paramSourcePosition) {
    if (this.line == paramSourcePosition.line) {
      CstString cstString2 = this.sourceFile;
      CstString cstString1 = paramSourcePosition.sourceFile;
      if (cstString2 == cstString1 || (cstString2 != null && cstString2.equals(cstString1)))
        return true; 
    } 
    return false;
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer(50);
    CstString cstString = this.sourceFile;
    if (cstString != null) {
      stringBuffer.append(cstString.toHuman());
      stringBuffer.append(":");
    } 
    int i = this.line;
    if (i >= 0)
      stringBuffer.append(i); 
    stringBuffer.append('@');
    i = this.address;
    if (i < 0) {
      stringBuffer.append("????");
    } else {
      stringBuffer.append(Hex.u2(i));
    } 
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\rop\code\SourcePosition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */