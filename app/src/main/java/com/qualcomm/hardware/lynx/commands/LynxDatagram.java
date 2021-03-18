package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxUnsupportedCommandException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;

public class LynxDatagram {
  public static final ByteOrder LYNX_ENDIAN = ByteOrder.LITTLE_ENDIAN;
  
  public static final int cbFrameBytesAndPacketLength = 4;
  
  public static final byte[] frameBytes = new byte[] { 68, 75 };
  
  private byte checksum;
  
  private byte destModuleAddress = 0;
  
  private byte messageNumber = 0;
  
  private short packetId = 0;
  
  private short packetLength;
  
  private byte[] payloadData = new byte[0];
  
  private TimeWindow payloadTimeWindow;
  
  private byte referenceNumber = 0;
  
  private byte sourceModuleAddress = 0;
  
  public LynxDatagram() {}
  
  public LynxDatagram(LynxMessage paramLynxMessage) throws LynxUnsupportedCommandException {
    this();
    int i = paramLynxMessage.getCommandNumber();
    paramLynxMessage.getModule().validateCommand(paramLynxMessage);
    setDestModuleAddress(paramLynxMessage.getDestModuleAddress());
    setMessageNumber(paramLynxMessage.getMessageNumber());
    setReferenceNumber(paramLynxMessage.getReferenceNumber());
    setPacketId(i);
    setPayloadData(paramLynxMessage.toPayloadByteArray());
  }
  
  public static boolean beginsWithFraming(ByteBuffer paramByteBuffer) {
    byte b = paramByteBuffer.get();
    byte[] arrayOfByte = frameBytes;
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (b == arrayOfByte[0]) {
      bool1 = bool2;
      if (paramByteBuffer.get() == frameBytes[1])
        bool1 = true; 
    } 
    return bool1;
  }
  
  public static boolean beginsWithFraming(byte[] paramArrayOfbyte) {
    int i = paramArrayOfbyte.length;
    byte[] arrayOfByte = frameBytes;
    return (i >= arrayOfByte.length && paramArrayOfbyte[0] == arrayOfByte[0] && paramArrayOfbyte[1] == arrayOfByte[1]);
  }
  
  private static byte checksumBytes(byte paramByte, byte[] paramArrayOfbyte) {
    for (int i = 0; i < paramArrayOfbyte.length; i++)
      paramByte = (byte)(paramByte + paramArrayOfbyte[i]); 
    return paramByte;
  }
  
  public static int getFixedPacketLength() {
    return 11;
  }
  
  private RobotCoreException illegalDatagram() {
    return new RobotCoreException("illegal Lynx datagram format");
  }
  
  public byte computeChecksum() {
    return checksumBytes(checksumBytes((byte)((byte)((byte)((byte)(checksumBytes(checksumBytes((byte)0, frameBytes), TypeConversion.shortToByteArray(this.packetLength, LYNX_ENDIAN)) + this.destModuleAddress) + this.sourceModuleAddress) + this.messageNumber) + this.referenceNumber), TypeConversion.shortToByteArray(this.packetId, LYNX_ENDIAN)), this.payloadData);
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte);
    byteBuffer.order(LYNX_ENDIAN);
    try {
      if (beginsWithFraming(byteBuffer)) {
        this.packetLength = byteBuffer.getShort();
        this.destModuleAddress = byteBuffer.get();
        this.sourceModuleAddress = byteBuffer.get();
        this.messageNumber = byteBuffer.get();
        this.referenceNumber = byteBuffer.get();
        this.packetId = byteBuffer.getShort();
        byte[] arrayOfByte = new byte[getPacketLength() - getFixedPacketLength()];
        this.payloadData = arrayOfByte;
        byteBuffer.get(arrayOfByte);
        this.checksum = byteBuffer.get();
        return;
      } 
      throw illegalDatagram();
    } catch (BufferUnderflowException bufferUnderflowException) {
      throw RobotCoreException.createChained(bufferUnderflowException, "Lynx datagram buffer underflow", new Object[0]);
    } 
  }
  
  public int getChecksum() {
    return TypeConversion.unsignedByteToInt(this.checksum);
  }
  
  public int getCommandNumber() {
    return getPacketId() & 0xFFFF7FFF;
  }
  
  public int getDestModuleAddress() {
    return TypeConversion.unsignedByteToInt(this.destModuleAddress);
  }
  
  public int getMessageNumber() {
    return TypeConversion.unsignedByteToInt(this.messageNumber);
  }
  
  public int getPacketId() {
    return TypeConversion.unsignedShortToInt(this.packetId);
  }
  
  public int getPacketLength() {
    return TypeConversion.unsignedShortToInt(this.packetLength);
  }
  
  public byte[] getPayloadData() {
    return this.payloadData;
  }
  
  public TimeWindow getPayloadTimeWindow() {
    TimeWindow timeWindow2 = this.payloadTimeWindow;
    TimeWindow timeWindow1 = timeWindow2;
    if (timeWindow2 == null)
      timeWindow1 = new TimeWindow(); 
    return timeWindow1;
  }
  
  public int getReferenceNumber() {
    return TypeConversion.unsignedByteToInt(this.referenceNumber);
  }
  
  public int getSourceModuleAddress() {
    return TypeConversion.unsignedByteToInt(this.sourceModuleAddress);
  }
  
  public boolean isChecksumValid() {
    return (this.checksum == computeChecksum());
  }
  
  public boolean isResponse() {
    return (getPacketId() >= 32768);
  }
  
  public void setChecksum(int paramInt) {
    this.checksum = (byte)paramInt;
  }
  
  public void setDestModuleAddress(int paramInt) {
    this.destModuleAddress = (byte)paramInt;
  }
  
  public void setMessageNumber(int paramInt) {
    this.messageNumber = (byte)paramInt;
  }
  
  public void setPacketId(int paramInt) {
    this.packetId = (short)paramInt;
  }
  
  public void setPacketLength(int paramInt) {
    this.packetLength = (short)(byte)paramInt;
  }
  
  public void setPayloadData(byte[] paramArrayOfbyte) {
    this.payloadData = paramArrayOfbyte;
  }
  
  public void setPayloadTimeWindow(TimeWindow paramTimeWindow) {
    this.payloadTimeWindow = paramTimeWindow;
  }
  
  public void setReferenceNumber(int paramInt) {
    this.referenceNumber = (byte)paramInt;
  }
  
  public void setSourceModuleAddress(int paramInt) {
    this.sourceModuleAddress = (byte)paramInt;
  }
  
  public byte[] toByteArray() {
    int i = updatePacketLength();
    setChecksum(computeChecksum());
    ByteBuffer byteBuffer = ByteBuffer.allocate(i);
    byteBuffer.order(LYNX_ENDIAN);
    byteBuffer.put(frameBytes);
    byteBuffer.putShort(this.packetLength);
    byteBuffer.put(this.destModuleAddress);
    byteBuffer.put(this.sourceModuleAddress);
    byteBuffer.put(this.messageNumber);
    byteBuffer.put(this.referenceNumber);
    byteBuffer.putShort(this.packetId);
    byteBuffer.put(this.payloadData);
    byteBuffer.put(this.checksum);
    return byteBuffer.array();
  }
  
  public int updatePacketLength() {
    int i = getFixedPacketLength() + this.payloadData.length;
    setPacketLength(i);
    return i;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxDatagram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */