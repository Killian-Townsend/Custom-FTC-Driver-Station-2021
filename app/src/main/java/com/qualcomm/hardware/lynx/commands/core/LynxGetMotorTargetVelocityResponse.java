package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxGetMotorTargetVelocityResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 2;
  
  private short velocity = 0;
  
  public LynxGetMotorTargetVelocityResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.velocity = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).getShort();
  }
  
  public int getVelocity() {
    return this.velocity;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(2).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.putShort(this.velocity);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetMotorTargetVelocityResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */