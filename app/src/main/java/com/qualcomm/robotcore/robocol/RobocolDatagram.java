package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RobocolDatagram {
  public static final String TAG = "Robocol";
  
  static Queue<byte[]> receiveBuffers = (Queue)new ConcurrentLinkedQueue<byte>();
  
  private DatagramPacket packet;
  
  private byte[] receiveBuffer = null;
  
  private RobocolDatagram() {
    this.packet = null;
  }
  
  public RobocolDatagram(RobocolParsable paramRobocolParsable, InetAddress paramInetAddress) throws RobotCoreException {
    byte[] arrayOfByte = paramRobocolParsable.toByteArrayForTransmission();
    this.packet = new DatagramPacket(arrayOfByte, arrayOfByte.length, paramInetAddress, 20884);
  }
  
  public static RobocolDatagram forReceive(int paramInt) {
    byte[] arrayOfByte2 = receiveBuffers.poll();
    if (arrayOfByte2 != null) {
      byte[] arrayOfByte = arrayOfByte2;
      if (arrayOfByte2.length != paramInt) {
        arrayOfByte = new byte[paramInt];
        DatagramPacket datagramPacket2 = new DatagramPacket(arrayOfByte, arrayOfByte.length);
        RobocolDatagram robocolDatagram2 = new RobocolDatagram();
        robocolDatagram2.packet = datagramPacket2;
        robocolDatagram2.receiveBuffer = arrayOfByte;
        return robocolDatagram2;
      } 
      DatagramPacket datagramPacket1 = new DatagramPacket(arrayOfByte, arrayOfByte.length);
      RobocolDatagram robocolDatagram1 = new RobocolDatagram();
      robocolDatagram1.packet = datagramPacket1;
      robocolDatagram1.receiveBuffer = arrayOfByte;
      return robocolDatagram1;
    } 
    byte[] arrayOfByte1 = new byte[paramInt];
    DatagramPacket datagramPacket = new DatagramPacket(arrayOfByte1, arrayOfByte1.length);
    RobocolDatagram robocolDatagram = new RobocolDatagram();
    robocolDatagram.packet = datagramPacket;
    robocolDatagram.receiveBuffer = arrayOfByte1;
    return robocolDatagram;
  }
  
  public void close() {
    byte[] arrayOfByte = this.receiveBuffer;
    if (arrayOfByte != null) {
      receiveBuffers.add(arrayOfByte);
      this.receiveBuffer = null;
    } 
    this.packet = null;
  }
  
  public InetAddress getAddress() {
    return this.packet.getAddress();
  }
  
  public byte[] getData() {
    return this.packet.getData();
  }
  
  public int getLength() {
    return this.packet.getLength();
  }
  
  public RobocolParsable.MsgType getMsgType() {
    return RobocolParsable.MsgType.fromByte(this.packet.getData()[0]);
  }
  
  protected DatagramPacket getPacket() {
    return this.packet;
  }
  
  public int getPayloadLength() {
    return this.packet.getLength() - 5;
  }
  
  public int getPort() {
    return this.packet.getPort();
  }
  
  public void setAddress(InetAddress paramInetAddress) {
    this.packet.setAddress(paramInetAddress);
  }
  
  protected void setPacket(DatagramPacket paramDatagramPacket) {
    this.packet = paramDatagramPacket;
  }
  
  public String toString() {
    boolean bool;
    String str;
    Object object;
    DatagramPacket datagramPacket = this.packet;
    if (datagramPacket != null && datagramPacket.getAddress() != null && this.packet.getLength() > 0) {
      str = RobocolParsable.MsgType.fromByte(this.packet.getData()[0]).name();
      bool = this.packet.getLength();
      object = this.packet.getAddress().getHostAddress();
    } else {
      str = "NONE";
      object = null;
      bool = false;
    } 
    return String.format("RobocolDatagram - type:%s, addr:%s, size:%d", new Object[] { str, object, Integer.valueOf(bool) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\RobocolDatagram.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */