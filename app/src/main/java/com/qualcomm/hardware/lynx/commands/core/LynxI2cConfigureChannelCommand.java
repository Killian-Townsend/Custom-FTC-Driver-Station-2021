package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import java.nio.ByteBuffer;

public class LynxI2cConfigureChannelCommand extends LynxDekaInterfaceCommand<LynxAck> {
  public static final int cbPayload = 2;
  
  private byte i2cBus;
  
  private byte speedCode;
  
  public LynxI2cConfigureChannelCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxI2cConfigureChannelCommand(LynxModuleIntf paramLynxModuleIntf, int paramInt, SpeedCode paramSpeedCode) {
    this(paramLynxModuleIntf);
    LynxConstants.validateI2cBusZ(paramInt);
    this.i2cBus = (byte)paramInt;
    this.speedCode = paramSpeedCode.bVal;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer byteBuffer = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
    this.i2cBus = byteBuffer.get();
    this.speedCode = byteBuffer.get();
  }
  
  public SpeedCode getSpeedCode() {
    return SpeedCode.fromByte(this.speedCode);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.i2cBus);
    byteBuffer.put(this.speedCode);
    return byteBuffer.array();
  }
  
  public enum SpeedCode {
    FASTPLUS_1M,
    FAST_400K,
    HIGH_3_4M,
    STANDARD_100K,
    UNKNOWN(-1);
    
    public byte bVal;
    
    static {
      FAST_400K = new SpeedCode("FAST_400K", 2, 1);
      FASTPLUS_1M = new SpeedCode("FASTPLUS_1M", 3, 2);
      SpeedCode speedCode = new SpeedCode("HIGH_3_4M", 4, 3);
      HIGH_3_4M = speedCode;
      $VALUES = new SpeedCode[] { UNKNOWN, STANDARD_100K, FAST_400K, FASTPLUS_1M, speedCode };
    }
    
    SpeedCode(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static SpeedCode fromByte(int param1Int) {
      for (SpeedCode speedCode : values()) {
        if (speedCode.bVal == param1Int)
          return speedCode; 
      } 
      return UNKNOWN;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cConfigureChannelCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */