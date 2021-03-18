package org.firstinspires.ftc.robotcore.internal.android.dx.util;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;

public final class IndentingWriter extends FilterWriter {
  private boolean collectingIndent;
  
  private int column;
  
  private int indent;
  
  private final int maxIndent;
  
  private final String prefix;
  
  private final int width;
  
  public IndentingWriter(Writer paramWriter, int paramInt) {
    this(paramWriter, paramInt, "");
  }
  
  public IndentingWriter(Writer paramWriter, int paramInt, String paramString) {
    super(paramWriter);
    if (paramWriter != null) {
      if (paramInt >= 0) {
        if (paramString != null) {
          int i;
          if (paramInt != 0) {
            i = paramInt;
          } else {
            i = Integer.MAX_VALUE;
          } 
          this.width = i;
          this.maxIndent = paramInt >> 1;
          String str = paramString;
          if (paramString.length() == 0)
            str = null; 
          this.prefix = str;
          bol();
          return;
        } 
        throw new NullPointerException("prefix == null");
      } 
      throw new IllegalArgumentException("width < 0");
    } 
    throw new NullPointerException("out == null");
  }
  
  private void bol() {
    boolean bool;
    this.column = 0;
    if (this.maxIndent != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.collectingIndent = bool;
    this.indent = 0;
  }
  
  public void write(int paramInt) throws IOException {
    synchronized (this.lock) {
      boolean bool = this.collectingIndent;
      int i = 0;
      if (bool)
        if (paramInt == 32) {
          int j = this.indent + 1;
          this.indent = j;
          if (j >= this.maxIndent) {
            this.indent = this.maxIndent;
            this.collectingIndent = false;
          } 
        } else {
          this.collectingIndent = false;
        }  
      if (this.column == this.width && paramInt != 10) {
        this.out.write(10);
        this.column = 0;
      } 
      if (this.column == 0) {
        if (this.prefix != null)
          this.out.write(this.prefix); 
        if (!this.collectingIndent) {
          while (i < this.indent) {
            this.out.write(32);
            i++;
          } 
          this.column = this.indent;
        } 
      } 
      this.out.write(paramInt);
      if (paramInt == 10) {
        bol();
      } else {
        this.column++;
      } 
      return;
    } 
  }
  
  public void write(String paramString, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield lock : Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: iload_3
    //   10: ifle -> 33
    //   13: aload_0
    //   14: aload_1
    //   15: iload_2
    //   16: invokevirtual charAt : (I)C
    //   19: invokevirtual write : (I)V
    //   22: iload_2
    //   23: iconst_1
    //   24: iadd
    //   25: istore_2
    //   26: iload_3
    //   27: iconst_1
    //   28: isub
    //   29: istore_3
    //   30: goto -> 9
    //   33: aload #4
    //   35: monitorexit
    //   36: return
    //   37: astore_1
    //   38: aload #4
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   13	22	37	finally
    //   33	36	37	finally
    //   38	41	37	finally
  }
  
  public void write(char[] paramArrayOfchar, int paramInt1, int paramInt2) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: getfield lock : Ljava/lang/Object;
    //   4: astore #4
    //   6: aload #4
    //   8: monitorenter
    //   9: iload_3
    //   10: ifle -> 31
    //   13: aload_0
    //   14: aload_1
    //   15: iload_2
    //   16: caload
    //   17: invokevirtual write : (I)V
    //   20: iload_2
    //   21: iconst_1
    //   22: iadd
    //   23: istore_2
    //   24: iload_3
    //   25: iconst_1
    //   26: isub
    //   27: istore_3
    //   28: goto -> 9
    //   31: aload #4
    //   33: monitorexit
    //   34: return
    //   35: astore_1
    //   36: aload #4
    //   38: monitorexit
    //   39: aload_1
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   13	20	35	finally
    //   31	34	35	finally
    //   36	39	35	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\android\d\\util\IndentingWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */