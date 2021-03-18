package org.firstinspires.ftc.robotcore.internal.files;

import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import org.firstinspires.ftc.robotcore.internal.collections.CircularByteBuffer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class LogOutputStream extends OutputStream {
  protected CircularByteBuffer byteBuffer;
  
  protected final Charset charset;
  
  protected final int priority;
  
  protected final String tag;
  
  public LogOutputStream(int paramInt, String paramString, Charset paramCharset) {
    this.priority = paramInt;
    this.tag = paramString;
    this.charset = paramCharset;
    this.byteBuffer = new CircularByteBuffer(32);
  }
  
  public static PrintStream printStream(String paramString) {
    Charset charset = Charset.forName("UTF-8");
    try {
      return new PrintStream(new LogOutputStream(6, paramString, charset), true, charset.name());
    } catch (UnsupportedEncodingException unsupportedEncodingException) {
      throw AppUtil.getInstance().unreachable();
    } 
  }
  
  public void close() throws IOException {
    writeToLog();
  }
  
  public void flush() throws IOException {}
  
  public void write(int paramInt) throws IOException {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield byteBuffer : Lorg/firstinspires/ftc/robotcore/internal/collections/CircularByteBuffer;
    //   6: iconst_1
    //   7: newarray byte
    //   9: dup
    //   10: iconst_0
    //   11: iload_1
    //   12: i2b
    //   13: bastore
    //   14: invokevirtual write : ([B)I
    //   17: pop
    //   18: iload_1
    //   19: bipush #10
    //   21: if_icmpne -> 28
    //   24: aload_0
    //   25: invokevirtual writeToLog : ()V
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_2
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	31	finally
    //   24	28	31	finally
  }
  
  protected void writeToLog() {
    CharBuffer charBuffer = this.charset.decode(ByteBuffer.wrap(this.byteBuffer.readAll()));
    if (charBuffer.length() > 0)
      RobotLog.internalLog(this.priority, this.tag, charBuffer.toString()); 
    this.byteBuffer.clear();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\LogOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */