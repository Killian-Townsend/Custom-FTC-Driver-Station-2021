package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;

public class LynxSetDebugLogLevelCommand extends LynxStandardCommand<LynxAck> {
  LynxModule.DebugGroup debugGroup = LynxModule.DebugGroup.MAIN;
  
  LynxModule.DebugVerbosity verbosity = LynxModule.DebugVerbosity.OFF;
  
  public LynxSetDebugLogLevelCommand(LynxModule paramLynxModule) {
    super(paramLynxModule);
  }
  
  public LynxSetDebugLogLevelCommand(LynxModule paramLynxModule, LynxModule.DebugGroup paramDebugGroup, LynxModule.DebugVerbosity paramDebugVerbosity) {
    this(paramLynxModule);
    this.debugGroup = paramDebugGroup;
    this.verbosity = paramDebugVerbosity;
  }
  
  public static int getStandardCommandNumber() {
    return 32526;
  }
  
  public void fromPayloadByteArray(byte[] paramArrayOfbyte) {
    this.debugGroup = LynxModule.DebugGroup.fromInt(paramArrayOfbyte[0]);
    this.verbosity = LynxModule.DebugVerbosity.fromInt(paramArrayOfbyte[1]);
  }
  
  public int getCommandNumber() {
    return getStandardCommandNumber();
  }
  
  public boolean isResponseExpected() {
    return false;
  }
  
  public byte[] toPayloadByteArray() {
    return new byte[] { this.debugGroup.bVal, this.verbosity.bVal };
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxSetDebugLogLevelCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */