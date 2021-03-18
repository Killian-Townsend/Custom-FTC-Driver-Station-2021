package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.TimeZone;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class Heartbeat extends RobocolParsableBase {
  public static final short BASE_PAYLOAD_SIZE = 33;
  
  private static final Charset charset = Charset.forName("UTF-8");
  
  private RobotState robotState = RobotState.NOT_STARTED;
  
  public long t0 = 0L;
  
  public long t1 = 0L;
  
  public long t2 = 0L;
  
  private String timeZoneId = TimeZone.getDefault().getID();
  
  private byte[] timeZoneIdBytes;
  
  private long timestamp = 0L;
  
  public static Heartbeat createWithTimeStamp() {
    Heartbeat heartbeat = new Heartbeat();
    heartbeat.timestamp = System.nanoTime();
    return heartbeat;
  }
  
  protected int cbPayload() {
    return this.timeZoneIdBytes.length + 34;
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    try {
      ByteBuffer byteBuffer = getReadBuffer(paramArrayOfbyte);
      this.timestamp = byteBuffer.getLong();
      this.robotState = RobotState.fromByte(byteBuffer.get());
      this.t0 = byteBuffer.getLong();
      this.t1 = byteBuffer.getLong();
      this.t2 = byteBuffer.getLong();
      byte[] arrayOfByte = new byte[byteBuffer.get()];
      this.timeZoneIdBytes = arrayOfByte;
      byteBuffer.get(arrayOfByte);
      this.timeZoneId = new String(this.timeZoneIdBytes, charset);
      return;
    } catch (BufferUnderflowException bufferUnderflowException) {
      throw RobotCoreException.createChained(bufferUnderflowException, "incoming packet too small", new Object[0]);
    } 
  }
  
  public double getElapsedSeconds() {
    return (System.nanoTime() - this.timestamp) / 1.0E9D;
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.HEARTBEAT;
  }
  
  public byte getRobotState() {
    return this.robotState.asByte();
  }
  
  public String getTimeZoneId() {
    return this.timeZoneId;
  }
  
  public long getTimestamp() {
    return this.timestamp;
  }
  
  public void setRobotState(RobotState paramRobotState) {
    this.robotState = paramRobotState;
  }
  
  public void setTimeZoneId(String paramString) {
    if ((paramString.getBytes(charset)).length <= 127) {
      this.timeZoneId = paramString;
      this.timeZoneIdBytes = paramString.getBytes(charset);
      return;
    } 
    throw Misc.illegalArgumentException("timezone id too long");
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    ByteBuffer byteBuffer = getWriteBuffer(cbPayload());
    try {
      byteBuffer.putLong(this.timestamp);
      byteBuffer.put(this.robotState.asByte());
      byteBuffer.putLong(this.t0);
      byteBuffer.putLong(this.t1);
      byteBuffer.putLong(this.t2);
      byteBuffer.put((byte)this.timeZoneIdBytes.length);
      byteBuffer.put(this.timeZoneIdBytes);
    } catch (BufferOverflowException bufferOverflowException) {
      RobotLog.logStackTrace(bufferOverflowException);
    } 
    return byteBuffer.array();
  }
  
  public String toString() {
    return String.format("Heartbeat - seq: %4d, time: %d", new Object[] { Integer.valueOf(getSequenceNumber()), Long.valueOf(this.timestamp) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\Heartbeat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */