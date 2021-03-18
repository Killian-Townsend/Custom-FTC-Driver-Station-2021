package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.LynxMessage;

public class LynxGetAllDIOInputsCommand extends LynxDekaInterfaceCommand<LynxGetAllDIOInputsResponse> {
  private static final int cbPayload = 0;
  
  public LynxGetAllDIOInputsCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
    this.response = (LynxMessage)new LynxGetAllDIOInputsResponse(paramLynxModuleIntf);
  }
  
  public static Class<? extends LynxInterfaceResponse> getResponseClass() {
    return (Class)LynxGetSingleDIOInputResponse.class;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {}
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[0];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxGetAllDIOInputsCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */