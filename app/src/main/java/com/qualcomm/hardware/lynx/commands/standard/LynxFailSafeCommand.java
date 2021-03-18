package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;

public class LynxFailSafeCommand extends LynxStandardCommand<LynxAck> {
  public LynxFailSafeCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return 32517;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {}
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[0];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxFailSafeCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */