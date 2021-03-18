package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModuleIntf;

public abstract class LynxInterfaceResponse extends LynxResponse {
  public LynxInterfaceResponse(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public int getBaseCommandNumber() {
    return (getInterface() == null) ? 0 : this.module.getInterfaceBaseCommandNumber(getInterface().getInterfaceName());
  }
  
  public int getCommandNumber() {
    return getBaseCommandNumber() + getInterfaceResponseIndex() | 0x8000;
  }
  
  public abstract LynxInterface getInterface();
  
  public int getInterfaceResponseIndex() {
    return (getInterface() == null) ? 0 : getInterface().getResponseIndex((Class)getClass());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxInterfaceResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */