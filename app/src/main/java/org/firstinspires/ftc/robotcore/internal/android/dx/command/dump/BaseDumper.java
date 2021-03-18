package org.firstinspires.ftc.robotcore.internal.android.dx.command.dump;

import java.io.IOException;
import java.io.PrintStream;
import java.io.StringWriter;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.code.ConcreteMethod;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.Member;
import org.firstinspires.ftc.robotcore.internal.android.dx.cf.iface.ParseObserver;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.ByteArray;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.Hex;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.IndentingWriter;
import org.firstinspires.ftc.robotcore.internal.android.dx.util.TwoColumnOutput;

public abstract class BaseDumper implements ParseObserver {
  protected Args args;
  
  private int at;
  
  private final byte[] bytes;
  
  private final String filePath;
  
  private final int hexCols;
  
  private int indent;
  
  private final PrintStream out;
  
  private final boolean rawBytes;
  
  private String separator;
  
  private final boolean strictParse;
  
  private final int width;
  
  public BaseDumper(byte[] paramArrayOfbyte, PrintStream paramPrintStream, String paramString, Args paramArgs) {
    String str;
    int i;
    this.bytes = paramArrayOfbyte;
    this.rawBytes = paramArgs.rawBytes;
    this.out = paramPrintStream;
    if (paramArgs.width <= 0) {
      i = 79;
    } else {
      i = paramArgs.width;
    } 
    this.width = i;
    this.filePath = paramString;
    this.strictParse = paramArgs.strictParse;
    this.indent = 0;
    if (this.rawBytes) {
      str = "|";
    } else {
      str = "";
    } 
    this.separator = str;
    this.at = 0;
    this.args = paramArgs;
    int j = (this.width - 5) / 15 + 1 & 0xFFFFFFFE;
    if (j < 6) {
      i = 6;
    } else {
      i = j;
      if (j > 10)
        i = 10; 
    } 
    this.hexCols = i;
  }
  
  static int computeParamWidth(ConcreteMethod paramConcreteMethod, boolean paramBoolean) {
    return paramConcreteMethod.getEffectiveDescriptor().getParameterTypes().getWordCount();
  }
  
  public void changeIndent(int paramInt) {
    String str;
    this.indent += paramInt;
    if (this.rawBytes) {
      str = "|";
    } else {
      str = "";
    } 
    this.separator = str;
    for (paramInt = 0; paramInt < this.indent; paramInt++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.separator);
      stringBuilder.append("  ");
      this.separator = stringBuilder.toString();
    } 
  }
  
  public void endParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2, Member paramMember) {}
  
  protected final int getAt() {
    return this.at;
  }
  
  protected final byte[] getBytes() {
    return this.bytes;
  }
  
  protected final String getFilePath() {
    return this.filePath;
  }
  
  protected final boolean getRawBytes() {
    return this.rawBytes;
  }
  
  protected final boolean getStrictParse() {
    return this.strictParse;
  }
  
  protected final int getWidth1() {
    if (this.rawBytes) {
      int i = this.hexCols;
      return i * 2 + 5 + i / 2;
    } 
    return 0;
  }
  
  protected final int getWidth2() {
    byte b;
    if (this.rawBytes) {
      b = getWidth1() + 1;
    } else {
      b = 0;
    } 
    return this.width - b - this.indent * 2;
  }
  
  protected final String hexDump(int paramInt1, int paramInt2) {
    return Hex.dump(this.bytes, paramInt1, paramInt2, paramInt1, this.hexCols, 4);
  }
  
  public void parsed(ByteArray paramByteArray, int paramInt1, int paramInt2, String paramString) {
    paramInt1 = paramByteArray.underlyingOffset(paramInt1, getBytes());
    boolean bool = getRawBytes();
    int i = this.at;
    String str2 = "";
    if (paramInt1 < i) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<dump skipped backwards to ");
      stringBuilder.append(Hex.u4(paramInt1));
      stringBuilder.append(">");
      println(stringBuilder.toString());
      this.at = paramInt1;
    } else if (paramInt1 > i) {
      String str;
      if (bool) {
        str = hexDump(i, paramInt1 - i);
      } else {
        str = "";
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("<skipped to ");
      stringBuilder.append(Hex.u4(paramInt1));
      stringBuilder.append(">");
      print(twoColumns(str, stringBuilder.toString()));
      this.at = paramInt1;
    } 
    String str1 = str2;
    if (bool)
      str1 = hexDump(paramInt1, paramInt2); 
    print(twoColumns(str1, paramString));
    this.at += paramInt2;
  }
  
  protected final void print(String paramString) {
    this.out.print(paramString);
  }
  
  protected final void println(String paramString) {
    this.out.println(paramString);
  }
  
  protected final void setAt(ByteArray paramByteArray, int paramInt) {
    this.at = paramByteArray.underlyingOffset(paramInt, this.bytes);
  }
  
  public void startParsingMember(ByteArray paramByteArray, int paramInt, String paramString1, String paramString2) {}
  
  protected final String twoColumns(String paramString1, String paramString2) {
    int j = getWidth1();
    int i = getWidth2();
    if (j == 0)
      try {
        j = paramString2.length();
        StringWriter stringWriter = new StringWriter(j * 2);
        IndentingWriter indentingWriter = new IndentingWriter(stringWriter, i, this.separator);
        indentingWriter.write(paramString2);
        if (j == 0 || paramString2.charAt(j - 1) != '\n')
          indentingWriter.write(10); 
        indentingWriter.flush();
        return stringWriter.toString();
      } catch (IOException iOException) {
        throw new RuntimeException(iOException);
      }  
    return TwoColumnOutput.toString((String)iOException, j, this.separator, paramString2, i);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\dx\command\dump\BaseDumper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */