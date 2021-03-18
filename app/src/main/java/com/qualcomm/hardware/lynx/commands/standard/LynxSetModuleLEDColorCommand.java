package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;

public class LynxSetModuleLEDColorCommand extends LynxStandardCommand<LynxAck> {
  byte blue;
  
  byte green;
  
  byte red;
  
  public LynxSetModuleLEDColorCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public LynxSetModuleLEDColorCommand(LynxModule paramLynxModule, byte paramByte1, byte paramByte2, byte paramByte3) {
    this(paramLynxModule);
    this.red = paramByte1;
    this.green = paramByte2;
    this.blue = paramByte3;
  }
  
  public static int getStandardCommandNumber() {
    return 32522;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.red = paramArrayOfbyte[0];
    this.green = paramArrayOfbyte[1];
    this.blue = paramArrayOfbyte[2];
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.red, this.green, this.blue };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxSetModuleLEDColorCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */