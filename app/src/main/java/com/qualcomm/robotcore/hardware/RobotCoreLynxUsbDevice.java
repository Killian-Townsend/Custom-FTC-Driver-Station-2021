package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotCoreLynxUsbDevice {
  void close();
  
  LynxModuleMetaList discoverModules() throws RobotCoreException, InterruptedException;
  
  void failSafe();
  
  void lockNetworkLockAcquisitions();
  
  void setThrowOnNetworkLockAcquisition(boolean paramBoolean);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\RobotCoreLynxUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */