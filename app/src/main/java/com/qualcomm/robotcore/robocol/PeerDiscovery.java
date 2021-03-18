package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.util.RobotLog;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class PeerDiscovery extends RobocolParsableBase {
  public static final String TAG = "PeerDiscovery";
  
  static final int cbBufferHistorical = 13;
  
  static final int cbPayloadHistorical = 10;
  
  private PeerType peerType;
  
  public PeerDiscovery(PeerType paramPeerType) {
    this.peerType = paramPeerType;
  }
  
  public static PeerDiscovery forReceive() {
    return new PeerDiscovery(PeerType.NOT_SET);
  }
  
  public void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException, RobotProtocolException {
    String str;
    if (paramArrayOfbyte.length >= 13) {
      ByteBuffer byteBuffer = getWholeReadBuffer(paramArrayOfbyte);
      byteBuffer.get();
      byteBuffer.getShort();
      byte b1 = byteBuffer.get();
      byte b2 = byteBuffer.get();
      short s = byteBuffer.getShort();
      if (b1 != 121) {
        RobotLog.ee("PeerDiscovery", "Incompatible robocol versions, remote: %d, local: %d", new Object[] { Byte.valueOf(b1), Byte.valueOf((byte)121) });
        boolean bool = AppUtil.getInstance().isRobotController();
        str = "Robot Controller";
        if (bool ? (b1 < 121) : (b1 >= 121))
          str = "Driver Station"; 
        throw new RobotProtocolException(AppUtil.getDefContext().getString(R.string.incompatibleAppsError), new Object[] { str });
      } 
      this.peerType = PeerType.fromByte(b2);
      if (b1 > 1)
        setSequenceNumber(s); 
      return;
    } 
    throw new RobotCoreException("Expected buffer of at least %d bytes, received %d", new Object[] { Integer.valueOf(13), Integer.valueOf(str.length) });
  }
  
  public PeerType getPeerType() {
    return this.peerType;
  }
  
  public RobocolParsable.MsgType getRobocolMsgType() {
    return RobocolParsable.MsgType.PEER_DISCOVERY;
  }
  
  public byte[] toByteArray() throws RobotCoreException {
    ByteBuffer byteBuffer = allocateWholeWriteBuffer(13);
    try {
      byteBuffer.put(getRobocolMsgType().asByte());
      byteBuffer.putShort((short)10);
      byteBuffer.put((byte)121);
      byteBuffer.put(this.peerType.asByte());
      byteBuffer.putShort((short)this.sequenceNumber);
    } catch (BufferOverflowException bufferOverflowException) {
      RobotLog.logStacktrace(bufferOverflowException);
    } 
    return byteBuffer.array();
  }
  
  public String toString() {
    return String.format("Peer Discovery - peer type: %s", new Object[] { this.peerType.name() });
  }
  
  public enum PeerType {
    GROUP_OWNER,
    NOT_CONNECTED_DUE_TO_PREEXISTING_CONNECTION,
    NOT_SET(0),
    PEER(1);
    
    private static final PeerType[] VALUES_CACHE;
    
    private int type;
    
    static {
      PeerType peerType = new PeerType("NOT_CONNECTED_DUE_TO_PREEXISTING_CONNECTION", 3, 3);
      NOT_CONNECTED_DUE_TO_PREEXISTING_CONNECTION = peerType;
      $VALUES = new PeerType[] { NOT_SET, PEER, GROUP_OWNER, peerType };
      VALUES_CACHE = values();
    }
    
    PeerType(int param1Int1) {
      this.type = param1Int1;
    }
    
    public static PeerType fromByte(byte param1Byte) {
      PeerType peerType = NOT_SET;
      try {
        return VALUES_CACHE[param1Byte];
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        RobotLog.w(String.format("Cannot convert %d to Peer: %s", new Object[] { Byte.valueOf(param1Byte), arrayIndexOutOfBoundsException.toString() }));
        return peerType;
      } 
    }
    
    public byte asByte() {
      return (byte)this.type;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\PeerDiscovery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */