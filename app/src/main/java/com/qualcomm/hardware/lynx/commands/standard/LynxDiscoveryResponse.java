package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.TypeConversion;

public class LynxDiscoveryResponse extends LynxStandardResponse {
  byte parentIndicator;
  
  public LynxDiscoveryResponse() {
    super((LynxModule)null);
  }
  
  public static int getStandardCommandNumber() {
    return LynxDiscoveryCommand.getStandardCommandNumber() | 0x8000;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.parentIndicator = paramArrayOfbyte[0];
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public int getDiscoveredModuleAddress() {
    return this.serialization.getSourceModuleAddress();
  }
  
  public int getParentIndicator() {
    return TypeConversion.unsignedByteToInt(this.parentIndicator);
  }
  
  public boolean isAckable() {
    return false;
  }
  
  public boolean isChild() {
    return (getParentIndicator() == 0);
  }
  
  public boolean isParent() {
    return isChild() ^ true;
  }
  
  public boolean isRetransmittable() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.parentIndicator };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxDiscoveryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */