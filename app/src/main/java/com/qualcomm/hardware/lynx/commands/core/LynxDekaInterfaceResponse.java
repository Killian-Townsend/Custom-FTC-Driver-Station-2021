package com.qualcomm.hardware.lynx.commands.core;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import com.qualcomm.hardware.lynx.commands.LynxInterface;
import com.qualcomm.hardware.lynx.commands.LynxInterfaceResponse;

public abstract class LynxDekaInterfaceResponse extends LynxInterfaceResponse {
  public LynxDekaInterfaceResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public LynxInterface getInterface() {
    return LynxDekaInterfaceCommand.theInterface;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\core\LynxDekaInterfaceResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */