package com.qualcomm.ftccommon;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;

public interface UsbModuleAttachmentHandler {
  void handleUsbModuleAttach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException;
  
  void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\UsbModuleAttachmentHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */