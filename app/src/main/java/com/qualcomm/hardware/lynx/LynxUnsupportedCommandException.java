package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxMessage;

public class LynxUnsupportedCommandException extends Exception {
  private Class<? extends LynxMessage> clazz;
  
  private int commandNumber;
  
  private LynxModule lynxModule;
  
  public LynxUnsupportedCommandException(LynxModule paramLynxModule, LynxMessage paramLynxMessage) {
    this.lynxModule = paramLynxModule;
    this.commandNumber = paramLynxMessage.getCommandNumber();
    this.clazz = (Class)paramLynxMessage.getClass();
  }
  
  public Class<? extends LynxMessage> getClazz() {
    return this.clazz;
  }
  
  public int getCommandNumber() {
    return this.commandNumber;
  }
  
  public LynxModuleIntf getLynxModule() {
    return this.lynxModule;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxUnsupportedCommandException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */