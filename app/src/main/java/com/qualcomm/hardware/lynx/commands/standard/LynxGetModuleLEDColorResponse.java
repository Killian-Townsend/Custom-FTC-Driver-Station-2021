package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.util.TypeConversion;

public class LynxGetModuleLEDColorResponse extends LynxStandardResponse {
  byte blue;
  
  byte green;
  
  byte red;
  
  public LynxGetModuleLEDColorResponse(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return LynxGetModuleLEDColorCommand.getStandardCommandNumber() | 0x8000;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.red = paramArrayOfbyte[0];
    this.green = paramArrayOfbyte[1];
    this.blue = paramArrayOfbyte[2];
  }
  
  int getBlue() {
    return TypeConversion.unsignedByteToInt(this.blue);
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  int getGreen() {
    return TypeConversion.unsignedByteToInt(this.green);
  }
  
  int getRed() {
    return TypeConversion.unsignedByteToInt(this.red);
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.red, this.green, this.blue };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxGetModuleLEDColorResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */