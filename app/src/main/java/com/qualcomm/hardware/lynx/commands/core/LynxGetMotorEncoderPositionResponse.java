package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxGetMotorEncoderPositionResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 4;
  
  private int position;
  
  public LynxGetMotorEncoderPositionResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.position = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).getInt();
  }
  
  public int getPosition() {
    return this.position;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(4).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putInt(this.position);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorEncoderPositionResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */