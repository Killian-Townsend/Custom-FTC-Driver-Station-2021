package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.LynxResponse;

public class LynxGetModuleLEDColorCommand extends LynxStandardCommand<LynxGetModuleLEDColorResponse> {
  public LynxGetModuleLEDColorCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
    this.response = (LynxMessage)new LynxGetModuleLEDColorResponse(paramLynxModule);
  }
  
  public static Class<? extends LynxResponse> getResponseClass() {
    return (Class)LynxGetModuleLEDColorResponse.class;
  }
  
  public static int getStandardCommandNumber() {
    return 32523;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {}
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return true;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[0];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxGetModuleLEDColorCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */