package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Engagable;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.GlobalWarningSource;

public interface LynxUsbDevice extends RobotUsbModule, GlobalWarningSource, RobotCoreLynxUsbDevice, HardwareDevice, SyncdDevice, Engagable {
  void acquireNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException;
  
  void addConfiguredModule(LynxModule paramLynxModule) throws RobotCoreException, InterruptedException, LynxNackException;
  
  void changeModuleAddress(LynxModule paramLynxModule, int paramInt, Runnable paramRunnable);
  
  LynxModuleMetaList discoverModules() throws RobotCoreException, InterruptedException;
  
  void failSafe();
  
  LynxModule getConfiguredModule(int paramInt);
  
  LynxUsbDeviceImpl getDelegationTarget();
  
  RobotUsbDevice getRobotUsbDevice();
  
  boolean isSystemSynthetic();
  
  void noteMissingModule(LynxModule paramLynxModule, String paramString);
  
  void releaseNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException;
  
  void removeConfiguredModule(LynxModule paramLynxModule);
  
  boolean setControlHubModuleAddressIfNecessary() throws InterruptedException, RobotCoreException;
  
  void setSystemSynthetic(boolean paramBoolean);
  
  void transmit(LynxMessage paramLynxMessage) throws InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */