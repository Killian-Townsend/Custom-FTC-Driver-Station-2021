package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.net.InetSocketAddress;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.Comparator;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public class Command extends RobocolParsableBase implements Comparable<Command>, Comparator<Command> {
  private static final short cbPayloadBase = 9;
  
  private static final short cbStringLength = 2;
  
  boolean mAcknowledged = false;
  
  byte mAttempts = 0;
  
  String mExtra;
  
  boolean mIsInjected = false;
  
  String mName;
  
  InetSocketAddress mSender;
  
  long mTimestamp;
  
  Deadline mTransmissionDeadline = null;
  
  public Command(RobocolDatagram paramRobocolDatagram) throws RobotCoreException {
    fromByteArray(paramRobocolDatagram.getData());
    this.mSender = new InetSocketAddress(paramRobocolDatagram.getAddress(), paramRobocolDatagram.getPort());
  }
  
  public Command(String paramString) {
    this(paramString, "");
  }
  
  public Command(String paramString1, String paramString2) {
    this.mName = paramString1;
    this.mExtra = paramString2;
    this.mTimestamp = generateTimestamp();
  }
  
  public static long generateTimestamp() {
    return System.nanoTime();
  }
  
  public void acknowledge() {
    this.mAcknowledged = true;
  }
  
  public int compare(Command paramCommand1, Command paramCommand2) {
    return paramCommand1.compareTo(paramCommand2);
  }
  
  public int compareTo(Command paramCommand) {
    int i = this.mName.compareTo(paramCommand.mName);
    if (i != 0)
      return i; 
    long l1 = this.mTimestamp;
    long l2 = paramCommand.mTimestamp;
    return (l1 < l2) ? -1 : ((l1 > l2) ? 1 : 0);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject instanceof Command) {
      paramObject = paramObject;
      if (this.mName.equals(((Command)paramObject).mName) && this.mTimestamp == ((Command)paramObject).mTimestamp)
        return true; 
    } 
    return false;
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    boolean bool;
    ByteBuffer byteBuffer = getReadBuffer(paramArrayOfbyte);
    this.mTimestamp = byteBuffer.getLong();
    if (byteBuffer.get() != 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mAcknowledged = bool;
    byte[] arrayOfByte = new byte[TypeConversion.unsignedShortToInt(byteBuffer.getShort())];
    byteBuffer.get(arrayOfByte);
    this.mName = TypeConversion.utf8ToString(arrayOfByte);
    if (!this.mAcknowledged) {
      arrayOfByte = new byte[TypeConversion.unsignedShortToInt(byteBuffer.getShort())];
      byteBuffer.get(arrayOfByte);
      this.mExtra = TypeConversion.utf8ToString(arrayOfByte);
    } 
  }
  
  public byte getAttempts() {
    return this.mAttempts;
  }
  
  public String getExtra() {
    return this.mExtra;
  }
  
  public String getName() {
    return this.mName;
  }
  
  int getPayloadSize(int paramInt1, int paramInt2) {
    return this.mAcknowledged ? (paramInt1 + 11) : (paramInt1 + 11 + 2 + paramInt2);
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.COMMAND;
  }
  
  public InetSocketAddress getSender() {
    return this.mSender;
  }
  
  public boolean hasExpired() {
    Deadline deadline = this.mTransmissionDeadline;
    return (deadline != null && deadline.hasExpired());
  }
  
  public int hashCode() {
    return this.mName.hashCode() ^ (int)this.mTimestamp;
  }
  
  public boolean isAcknowledged() {
    return this.mAcknowledged;
  }
  
  public boolean isInjected() {
    return this.mIsInjected;
  }
  
  public void setIsInjected(boolean paramBoolean) {
    this.mIsInjected = paramBoolean;
  }
  
  public void setTransmissionDeadline(Deadline paramDeadline) {
    this.mTransmissionDeadline = paramDeadline;
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    byte b = this.mAttempts;
    boolean bool = true;
    if (b != Byte.MAX_VALUE)
      this.mAttempts = (byte)(b + 1); 
    byte[] arrayOfByte1 = TypeConversion.stringToUtf8(this.mName);
    byte[] arrayOfByte2 = TypeConversion.stringToUtf8(this.mExtra);
    short s = (short)getPayloadSize(arrayOfByte1.length, arrayOfByte2.length);
    if (s <= Short.MAX_VALUE) {
      ByteBuffer byteBuffer = getWriteBuffer(s);
      try {
        byteBuffer.putLong(this.mTimestamp);
        if (!this.mAcknowledged)
          bool = false; 
        byteBuffer.put((byte)bool);
        byteBuffer.putShort((short)arrayOfByte1.length);
        byteBuffer.put(arrayOfByte1);
        if (!this.mAcknowledged) {
          byteBuffer.putShort((short)arrayOfByte2.length);
          byteBuffer.put(arrayOfByte2);
        } 
      } catch (BufferOverflowException bufferOverflowException) {
        RobotLog.logStacktrace(bufferOverflowException);
      } 
      return byteBuffer.array();
    } 
    throw new IllegalArgumentException(String.format("command payload is too large: %d", new Object[] { Short.valueOf(s) }));
  }
  
  public String toString() {
    return String.format("command: %20d %5s %s", new Object[] { Long.valueOf(this.mTimestamp), Boolean.valueOf(this.mAcknowledged), this.mName });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\Command.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */