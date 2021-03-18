package org.firstinspires.ftc.robotcore.internal.ftdi;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public abstract class BulkPacketBuffer {
  protected final ByteBuffer byteBuffer;
  
  protected int currentLength;
  
  protected long timestamp;
  
  public BulkPacketBuffer(int paramInt) {
    this.byteBuffer = ByteBuffer.allocate(paramInt);
    setCurrentLength(0);
  }
  
  public byte[] array() {
    return this.byteBuffer.array();
  }
  
  public int arrayOffset() {
    return this.byteBuffer.arrayOffset();
  }
  
  public int capacity() {
    return this.byteBuffer.capacity();
  }
  
  public ByteBuffer getByteBuffer() {
    return this.byteBuffer;
  }
  
  public int getCurrentLength() {
    return this.currentLength;
  }
  
  public long getTimestamp(TimeUnit paramTimeUnit) {
    return paramTimeUnit.convert(this.timestamp, TimeUnit.NANOSECONDS);
  }
  
  public void setCurrentLength(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield currentLength : I
    //   7: aload_0
    //   8: invokestatic nanoTime : ()J
    //   11: putfield timestamp : J
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_2
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_2
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\BulkPacketBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */