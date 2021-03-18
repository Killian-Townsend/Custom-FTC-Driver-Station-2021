package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import java.nio.ByteBuffer;

public class LynxReadVersionStringCommand extends LynxDekaInterfaceCommand<LynxReadVersionStringResponse> {
  public static final int cbPayload = 0;
  
  public LynxReadVersionStringCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxReadVersionStringResponse(paramLynxModuleIntf);
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxReadVersionStringResponse.class;
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
  
  protected boolean usePretendResponseIfRealModuleDoesntSupport() {
    return true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxReadVersionStringCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */