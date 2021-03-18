package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxI2cConfigureQueryResponse extends LynxDekaInterfaceResponse {
  public static final int cbPayload = 1;
  
  private byte speedCode;
  
  public LynxI2cConfigureQueryResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.speedCode = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).get();
  }
  
  public LynxI2cConfigureChannelCommand.SpeedCode getSpeedCode() {
    return LynxI2cConfigureChannelCommand.SpeedCode.fromByte(this.speedCode);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.speedCode);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cConfigureQueryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */