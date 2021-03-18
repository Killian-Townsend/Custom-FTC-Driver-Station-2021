package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.TypeConversion;

public class LynxGetModuleStatusResponse extends LynxStandardResponse {
  public static final int bitBatteryLow = 16;
  
  public static final int bitControllerOverTemp = 8;
  
  public static final int bitDeviceReset = 2;
  
  public static final int bitFailSafe = 4;
  
  public static final int bitHIBFault = 32;
  
  public static final int bitKeepAliveTimeout = 1;
  
  byte motorAlerts;
  
  byte status;
  
  public LynxGetModuleStatusResponse(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public static int getStandardCommandNumber() {
    return LynxGetModuleStatusCommand.getStandardCommandNumber() | 0x8000;
  }
  
  protected void appendBit(StringBuilder paramStringBuilder, int paramInt, String paramString) {
    if (testBitsOn(paramInt)) {
      if (paramStringBuilder.length() > 0)
        paramStringBuilder.append("|"); 
      paramStringBuilder.append(paramString);
    } 
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.status = paramArrayOfbyte[0];
    this.motorAlerts = paramArrayOfbyte[1];
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public int getMotorAlerts() {
    return TypeConversion.unsignedByteToInt(this.motorAlerts);
  }
  
  public int getStatus() {
    return TypeConversion.unsignedByteToInt(this.status);
  }
  
  public boolean hasMotorLostCounts(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    paramInt = 1 << paramInt;
    return ((getMotorAlerts() & paramInt) == paramInt);
  }
  
  public boolean isBatteryLow() {
    return testBitsOn(16);
  }
  
  public boolean isControllerOverTemp() {
    return testBitsOn(8);
  }
  
  public boolean isDeviceReset() {
    return testBitsOn(2);
  }
  
  public boolean isFailSafe() {
    return testBitsOn(4);
  }
  
  public boolean isHIBFault() {
    return testBitsOn(32);
  }
  
  public boolean isKeepAliveTimeout() {
    return testBitsOn(1);
  }
  
  public boolean isMotorBridgeOverTemp(int paramInt) {
    LynxConstants.validateMotorZ(paramInt);
    paramInt = 1 << paramInt + 4;
    return ((getMotorAlerts() & paramInt) == paramInt);
  }
  
  public boolean testAnyBits(int paramInt) {
    return ((paramInt & getStatus()) != 0);
  }
  
  public boolean testBitsOn(int paramInt) {
    return ((getStatus() & paramInt) == paramInt);
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.status, this.motorAlerts };
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    appendBit(stringBuilder, 1, "KeepAliveTimeout");
    appendBit(stringBuilder, 2, "Reset");
    appendBit(stringBuilder, 4, "FailSafe");
    appendBit(stringBuilder, 8, "Temp");
    appendBit(stringBuilder, 16, "Battery");
    appendBit(stringBuilder, 32, "HIB Fault");
    String str2 = stringBuilder.toString();
    String str1 = str2;
    if (str2.length() > 0) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(": ");
      stringBuilder1.append(str2);
      str1 = stringBuilder1.toString();
    } 
    return String.format("LynxGetModuleStatusResponse(status=0x%02x alerts=0x%02x%s)", new Object[] { Byte.valueOf(this.status), Byte.valueOf(this.motorAlerts), str1 });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxGetModuleStatusResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */