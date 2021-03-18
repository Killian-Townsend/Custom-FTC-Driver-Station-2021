package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxRespondable;
import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.util.Locale;

public class LynxNackException extends Exception {
  private LynxRespondable command;
  
  public LynxNackException(LynxRespondable paramLynxRespondable, String paramString) {
    super(paramString);
    this.command = paramLynxRespondable;
  }
  
  public LynxNackException(LynxRespondable paramLynxRespondable, String paramString, Object... paramVarArgs) {
    super(String.format(Locale.getDefault(), "(%s #%d) %s", new Object[] { paramLynxRespondable.getModule().getSerialNumber(), Integer.valueOf(paramLynxRespondable.getModuleAddress()), String.format(Locale.getDefault(), paramString, paramVarArgs) }));
    this.command = paramLynxRespondable;
  }
  
  public LynxRespondable getCommand() {
    return this.command;
  }
  
  public LynxNack getNack() {
    return this.command.getNackReceived();
  }
  
  public RobotCoreException wrap() {
    return RobotCoreException.createChained(this, getMessage(), new Object[0]);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxNackException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */