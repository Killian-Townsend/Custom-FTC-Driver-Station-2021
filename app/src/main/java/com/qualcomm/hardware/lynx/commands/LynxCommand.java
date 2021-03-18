package com.qualcomm.hardware.lynx.commands;

import com.qualcomm.hardware.lynx.LynxModuleIntf;
import java.lang.reflect.InvocationTargetException;

public abstract class LynxCommand<RESPONSE extends LynxMessage> extends LynxRespondable<RESPONSE> {
  public LynxCommand(LynxModuleIntf paramLynxModuleIntf) {
    super(paramLynxModuleIntf);
  }
  
  public static Class<? extends LynxResponse> getResponseClass(Class paramClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    return (Class<? extends LynxResponse>)invokeStaticNullaryMethod(paramClass, "getResponseClass");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\commands\LynxCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */