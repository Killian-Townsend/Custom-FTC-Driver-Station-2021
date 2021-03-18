package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class RobocolParsableBase implements RobocolParsable {
  protected static final long nanotimeTransmitInterval = 200000000L;
  
  protected static AtomicInteger nextSequenceNumber = new AtomicInteger();
  
  protected long nanotimeTransmit;
  
  protected int sequenceNumber;
  
  public RobocolParsableBase() {
    setSequenceNumber();
    this.nanotimeTransmit = 0L;
  }
  
  public static void initializeSequenceNumber(int paramInt) {
    nextSequenceNumber = new AtomicInteger(paramInt);
  }
  
  protected ByteBuffer allocateWholeWriteBuffer(int paramInt) {
    return ByteBuffer.allocate(paramInt);
  }
  
  protected ByteBuffer getReadBuffer(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte, 3, paramArrayOfbyte.length - 3);
    setSequenceNumber(byteBuffer.getShort());
    return byteBuffer;
  }
  
  public int getSequenceNumber() {
    return this.sequenceNumber;
  }
  
  protected ByteBuffer getWholeReadBuffer(byte[] paramArrayOfbyte) {
    return ByteBuffer.wrap(paramArrayOfbyte);
  }
  
  protected ByteBuffer getWriteBuffer(int paramInt) {
    ByteBuffer byteBuffer = allocateWholeWriteBuffer(paramInt + 5);
    byteBuffer.put(getRobocolMsgType().asByte());
    byteBuffer.putShort((short)paramInt);
    byteBuffer.putShort((short)this.sequenceNumber);
    return byteBuffer;
  }
  
  public void setSequenceNumber() {
    setSequenceNumber((short)nextSequenceNumber.getAndIncrement());
  }
  
  public void setSequenceNumber(short paramShort) {
    this.sequenceNumber = TypeConversion.unsignedShortToInt(paramShort);
  }
  
  public boolean shouldTransmit(long paramLong) {
    long l = this.nanotimeTransmit;
    return (l == 0L || paramLong - l > 200000000L);
  }
  
  public byte[] toByteArrayForTransmission() throws RobotCoreException {
    byte[] arrayOfByte = toByteArray();
    this.nanotimeTransmit = System.nanoTime();
    return arrayOfByte;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\RobocolParsableBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */