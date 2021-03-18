package com.qualcomm.hardware.lynx.commands.standard;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxResponse;

public abstract class LynxStandardResponse extends LynxResponse {
  public LynxStandardResponse(LynxModule paramLynxModule) {
    super((LynxModuleIntf)paramLynxModule);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\standard\LynxStandardResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */