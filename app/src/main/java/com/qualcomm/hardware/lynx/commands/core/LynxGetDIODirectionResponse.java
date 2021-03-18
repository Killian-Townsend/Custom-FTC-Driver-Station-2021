package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import java.nio.ByteBuffer;

public class LynxGetDIODirectionResponse extends LynxDekaInterfaceResponse {
  private static final int cbPayload = 1;
  
  private byte direction = 0;
  
  public LynxGetDIODirectionResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.direction = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).get();
  }
  
  public DigitalChannel.Mode getMode() {
    return (this.direction == 0) ? DigitalChannel.Mode.INPUT : DigitalChannel.Mode.OUTPUT;
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.direction);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetDIODirectionResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */