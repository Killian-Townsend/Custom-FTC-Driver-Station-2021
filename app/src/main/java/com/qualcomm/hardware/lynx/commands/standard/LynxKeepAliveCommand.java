package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;

public class LynxKeepAliveCommand extends LynxStandardCommand<LynxAck> {
  boolean initialPing;
  
  public LynxKeepAliveCommand(LynxModule paramLynxModule, boolean paramBoolean) {
    super(paramLynxModule);
    this.initialPing = paramBoolean;
  }
  
  public static int getStandardCommandNumber() {
    return 32516;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {}
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  protected int getMsAwaitInterval() {
    return 500;
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  protected void noteAttentionRequired() {
    if (!this.initialPing)
      super.noteAttentionRequired(); 
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[0];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxKeepAliveCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */