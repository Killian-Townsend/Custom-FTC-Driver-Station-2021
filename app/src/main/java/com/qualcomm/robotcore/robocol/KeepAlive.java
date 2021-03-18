package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public class KeepAlive extends RobocolParsableBase {
  public static final short BASE_PAYLOAD_SIZE = 1;
  
  private byte id = 0;
  
  private long timestamp = 0L;
  
  public static KeepAlive createWithTimeStamp() {
    KeepAlive keepAlive = new KeepAlive();
    keepAlive.timestamp = System.nanoTime();
    return keepAlive;
  }
  
  protected int cbPayload() {
    return 1;
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    try {
      this.id = getReadBuffer(paramArrayOfbyte).get();
      return;
    } catch (BufferUnderflowException bufferUnderflowException) {
      throw RobotCoreException.createChained(bufferUnderflowException, "incoming packet too small", new Object[0]);
    } 
  }
  
  public double getElapsedSeconds() {
    return (System.nanoTime() - this.timestamp) / 1.0E9D;
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.KEEPALIVE;
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    ByteBuffer byteBuffer = getWriteBuffer(cbPayload());
    try {
      byteBuffer.put(this.id);
    } catch (BufferOverflowException bufferOverflowException) {
      RobotLog.logStackTrace(bufferOverflowException);
    } 
    return byteBuffer.array();
  }
  
  public String toString() {
    return String.format("KeepAlive - time: %d", new Object[] { Long.valueOf(this.timestamp) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\KeepAlive.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */