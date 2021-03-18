package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;

public class LynxSetNewModuleAddressCommand extends LynxStandardCommand<LynxAck> {
  byte moduleAddress;
  
  public LynxSetNewModuleAddressCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public LynxSetNewModuleAddressCommand(LynxModule paramLynxModule, byte paramByte) {
    this(paramLynxModule);
    this.moduleAddress = paramByte;
  }
  
  public static int getStandardCommandNumber() {
    return 32518;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.moduleAddress = paramArrayOfbyte[0];
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.moduleAddress };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxSetNewModuleAddressCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */