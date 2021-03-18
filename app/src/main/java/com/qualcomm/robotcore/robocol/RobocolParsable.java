package com.qualcomm.robotcore.robocol;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.util.RobotLog;

public interface RobocolParsable {
  public static final int HEADER_LENGTH = 5;
  
  void fromByteArray(byte[] paramArrayOfbyte) throws RobotCoreException, RobotProtocolException;
  
  MsgType getRobocolMsgType();
  
  int getSequenceNumber();
  
  void setSequenceNumber();
  
  boolean shouldTransmit(long paramLong);
  
  byte[] toByteArray() throws RobotCoreException;
  
  byte[] toByteArrayForTransmission() throws RobotCoreException;
  
  public enum MsgType {
    COMMAND,
    EMPTY(0),
    GAMEPAD(0),
    HEARTBEAT(1),
    KEEPALIVE(1),
    PEER_DISCOVERY(1),
    TELEMETRY(1);
    
    private static final MsgType[] VALUES_CACHE;
    
    private final int type;
    
    static {
      COMMAND = new MsgType("COMMAND", 4, 4);
      TELEMETRY = new MsgType("TELEMETRY", 5, 5);
      MsgType msgType = new MsgType("KEEPALIVE", 6, 6);
      KEEPALIVE = msgType;
      $VALUES = new MsgType[] { EMPTY, HEARTBEAT, GAMEPAD, PEER_DISCOVERY, COMMAND, TELEMETRY, msgType };
      VALUES_CACHE = values();
    }
    
    MsgType(int param1Int1) {
      this.type = param1Int1;
    }
    
    public static MsgType fromByte(byte param1Byte) {
      MsgType msgType = EMPTY;
      try {
        return VALUES_CACHE[param1Byte];
      } catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
        RobotLog.w(String.format("Cannot convert %d to MsgType: %s", new Object[] { Byte.valueOf(param1Byte), arrayIndexOutOfBoundsException.toString() }));
        return msgType;
      } 
    }
    
    public byte asByte() {
      return (byte)this.type;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\robocol\RobocolParsable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */