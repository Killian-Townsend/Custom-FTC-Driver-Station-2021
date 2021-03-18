package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Engagable;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;

public interface LynxModuleIntf extends RobotCoreLynxModule, HardwareDevice, Engagable {
  <T> T acquireI2cLockWhile(Supplier<T> paramSupplier) throws InterruptedException, RobotCoreException, LynxNackException;
  
  void acquireNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException;
  
  void finishedWithMessage(LynxMessage paramLynxMessage) throws InterruptedException;
  
  int getInterfaceBaseCommandNumber(String paramString);
  
  boolean isCommandSupported(Class<? extends LynxCommand> paramClass);
  
  boolean isNotResponding();
  
  void noteAttentionRequired();
  
  void noteDatagramReceived();
  
  void noteNotResponding();
  
  void releaseNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException;
  
  void resetPingTimer(LynxMessage paramLynxMessage);
  
  void retransmit(LynxMessage paramLynxMessage) throws InterruptedException;
  
  void sendCommand(LynxMessage paramLynxMessage) throws InterruptedException, LynxUnsupportedCommandException;
  
  void validateCommand(LynxMessage paramLynxMessage) throws LynxUnsupportedCommandException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxModuleIntf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */