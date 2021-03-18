package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import java.nio.ByteBuffer;

public class LynxPhoneChargeQueryCommand extends LynxDekaInterfaceCommand<LynxPhoneChargeQueryResponse> {
  public static final int cbPayload = 0;
  
  public LynxPhoneChargeQueryCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxPhoneChargeQueryCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */