package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxUnsupportedCommandException;
import com.qualcomm.hardware.lynx.commands.LynxMessage;

public class LynxDiscoveryCommand extends LynxStandardCommand<LynxAck> {
  public LynxDiscoveryCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return 32527;
  }
  
  public void acquireNetworkLock() throws InterruptedException {}
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {}
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public int getDestModuleAddress() {
    return 255;
  }
  
  public boolean isAckable() {
    return false;
  }
  
  public boolean isRetransmittable() {
    return false;
  }
  
  protected void noteAttentionRequired() {}
  
  public void releaseNetworkLock() throws InterruptedException {}
  
  public void send() throws LynxNackException, InterruptedException {
    try {
      this.module.sendCommand((LynxMessage)this);
      return;
    } catch (LynxUnsupportedCommandException lynxUnsupportedCommandException) {
      throwNackForUnsupportedCommand(lynxUnsupportedCommandException);
      return;
    } 
  }
  
  public LynxAck sendReceive() throws LynxNackException, InterruptedException {
    try {
      this.module.sendCommand((LynxMessage)this);
    } catch (LynxUnsupportedCommandException lynxUnsupportedCommandException) {
      throwNackForUnsupportedCommand(lynxUnsupportedCommandException);
    } 
    return null;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[0];
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxDiscoveryCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */