package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.List;

public interface RobotUsbManager {
  RobotUsbDevice openBySerialNumber(SerialNumber paramSerialNumber) throws RobotCoreException;
  
  List<SerialNumber> scanForDevices() throws RobotCoreException;
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotUsbManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */