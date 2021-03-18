package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.robotcore.exception.TargetPositionNotSetException;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public class LynxCommExceptionHandler {
  protected String tag = "RobotCore";
  
  public LynxCommExceptionHandler() {}
  
  public LynxCommExceptionHandler(String paramString) {
    this.tag = paramString;
  }
  
  protected String getTag() {
    return this.tag;
  }
  
  protected boolean handleException(Exception paramException) {
    if (paramException instanceof InterruptedException)
      handleSpecificException((InterruptedException)paramException); 
    if (!(paramException instanceof OpModeManagerImpl.ForceStopException)) {
      if (paramException instanceof LynxNackException) {
        paramException = paramException;
        handleSpecificException((LynxNackException)paramException);
        return true ^ paramException.getNack().getNackReasonCode().isUnsupportedReason();
      } 
      if (paramException instanceof TargetPositionNotSetException) {
        handleSpecificException((TargetPositionNotSetException)paramException);
        return true;
      } 
      if (paramException instanceof RuntimeException) {
        handleSpecificException((RuntimeException)paramException);
        return true;
      } 
      RobotLog.ee(getTag(), paramException, "unexpected exception thrown during lynx communication");
      return true;
    } 
    throw (OpModeManagerImpl.ForceStopException)paramException;
  }
  
  protected void handleSpecificException(LynxNackException paramLynxNackException) {
    switch (paramLynxNackException.getNack().getNackReasonCodeAsEnum()) {
      default:
        RobotLog.ee(getTag(), paramLynxNackException, "exception thrown during lynx communication");
        return;
      case null:
        RobotLog.ee(getTag(), "%s not supported by module mod#=%d cmd#=%d", new Object[] { paramLynxNackException.getCommand().getClass().getSimpleName(), Integer.valueOf(paramLynxNackException.getNack().getModuleAddress()), Integer.valueOf(paramLynxNackException.getNack().getCommandNumber()) });
        return;
      case null:
        RobotLog.ee(getTag(), "%s not delivered in module mod#=%d cmd#=%d", new Object[] { paramLynxNackException.getCommand().getClass().getSimpleName(), Integer.valueOf(paramLynxNackException.getNack().getModuleAddress()), Integer.valueOf(paramLynxNackException.getNack().getCommandNumber()) });
        return;
      case null:
        RobotLog.ww(getTag(), "%s not implemented by lynx hw; ignoring", new Object[] { paramLynxNackException.getCommand().getClass().getSimpleName() });
        break;
      case null:
      case null:
      case null:
      case null:
        break;
    } 
  }
  
  protected void handleSpecificException(TargetPositionNotSetException paramTargetPositionNotSetException) {
    throw paramTargetPositionNotSetException;
  }
  
  protected void handleSpecificException(InterruptedException paramInterruptedException) {
    Thread.currentThread().interrupt();
  }
  
  protected void handleSpecificException(RuntimeException paramRuntimeException) {
    RobotLog.ee(getTag(), paramRuntimeException, "exception thrown during lynx communication");
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxCommExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */