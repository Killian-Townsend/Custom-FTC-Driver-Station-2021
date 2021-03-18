package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;

public interface RobotUsbModule extends RobotArmingStateNotifier {
  void arm() throws RobotCoreException, InterruptedException;
  
  void armOrPretend() throws RobotCoreException, InterruptedException;
  
  void close();
  
  void disarm() throws RobotCoreException, InterruptedException;
  
  void pretend() throws RobotCoreException, InterruptedException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotUsbModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */