package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.robotcore.util.RobotLog;

public abstract class LynxI2cResponse extends LynxDekaInterfaceResponse {
  protected byte i2cStatus = 0;
  
  public LynxI2cResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public static boolean isAddressAcknowledged(byte paramByte) {
    return ((paramByte & 0x1) == 0);
  }
  
  public static boolean isArbitrationLost(byte paramByte) {
    return ((paramByte & 0x4) != 0);
  }
  
  public static boolean isClockTimeout(byte paramByte) {
    return ((paramByte & 0x8) != 0);
  }
  
  public static boolean isDataAcknowledged(byte paramByte) {
    return ((paramByte & 0x2) == 0);
  }
  
  public static boolean isStatusOk(byte paramByte) {
    return ((paramByte & 0xF) == 0);
  }
  
  public byte getI2cStatus() {
    return this.i2cStatus;
  }
  
  public boolean isAddressAcknowledged() {
    return isAddressAcknowledged(getI2cStatus());
  }
  
  public boolean isArbitrationLost() {
    return isArbitrationLost(getI2cStatus());
  }
  
  public boolean isClockTimeout() {
    return isClockTimeout(getI2cStatus());
  }
  
  public boolean isDataAcknowledged() {
    return isDataAcknowledged(getI2cStatus());
  }
  
  public boolean isStatusOk() {
    return isStatusOk(getI2cStatus());
  }
  
  public void logResponse() {
    if (getI2cStatus() != 0)
      RobotLog.v("addr=%s data=%s arb=%s clock=%s", new Object[] { Boolean.valueOf(isAddressAcknowledged()), Boolean.valueOf(isDataAcknowledged()), Boolean.valueOf(isArbitrationLost()), Boolean.valueOf(isClockTimeout()) }); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxI2cResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */