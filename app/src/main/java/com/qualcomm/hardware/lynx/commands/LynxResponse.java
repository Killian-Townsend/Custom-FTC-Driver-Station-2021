package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModuleIntf;

public abstract class LynxResponse extends LynxRespondable {
  public static final int RESPONSE_BIT = 32768;
  
  public LynxResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public boolean isResponse() {
    return true;
  }
  
  public final boolean isResponseExpected() {
    return false;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */