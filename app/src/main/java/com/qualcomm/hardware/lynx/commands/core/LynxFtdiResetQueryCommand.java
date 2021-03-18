package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import java.nio.ByteBuffer;

public class LynxFtdiResetQueryCommand extends LynxDekaInterfaceCommand<LynxFtdiResetQueryResponse> {
  public static final int cbPayload = 0;
  
  public LynxFtdiResetQueryCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxFtdiResetQueryResponse(paramLynxModuleIntf);
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxPhoneChargeQueryResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    ByteBuffer.wrap(paramArrayOfbyte).order(LynxDatagram.LYNX_ENDIAN);
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    return ByteBuffer.allocate(0).order(LynxDatagram.LYNX_ENDIAN).array();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxFtdiResetQueryCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */