package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import java.nio.ByteBuffer;

public class LynxFtdiResetQueryResponse extends LynxDekaInterfaceResponse {
  public static final int cbPayload = 1;
  
  private byte enabled = 0;
  
  public LynxFtdiResetQueryResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.enabled = ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN).get();
  }
  
  public boolean isEnabled() {
    return (this.enabled != 0);
  }
  
  public byte[] toPayloadByteArray() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(1).order(LynxDatagram.LYNX_ENDIAN);
    byteBuffer.put(this.enabled);
    return byteBuffer.array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxFtdiResetQueryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */