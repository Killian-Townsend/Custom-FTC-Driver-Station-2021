package com.qualcomm.robotcore.hardware.usb.serial;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class RobotUsbManagerTty implements RobotUsbManager {
  public static final String TAG = "RobotUsbManagerTty";
  
  protected SerialNumber serialNumberEmbedded = LynxConstants.SERIAL_NUMBER_EMBEDDED;
  
  Object getLock() {
    return RobotUsbManagerTty.class;
  }
  
  public RobotUsbDevice openBySerialNumber(SerialNumber paramSerialNumber) throws RobotCoreException {
    synchronized (getLock()) {
      RobotUsbDeviceTty robotUsbDeviceTty;
      if (this.serialNumberEmbedded.equals(paramSerialNumber)) {
        if (!RobotUsbDeviceImplBase.isOpen(paramSerialNumber)) {
          File file = AndroidBoard.getInstance().getUartLocation();
          try {
            SerialPort serialPort = new SerialPort(file, 460800);
            robotUsbDeviceTty = new RobotUsbDeviceTty(serialPort, this.serialNumberEmbedded, file);
            robotUsbDeviceTty.setFirmwareVersion(new RobotUsbDevice.FirmwareVersion(1, 0));
            robotUsbDeviceTty.setDeviceType(DeviceManager.UsbDeviceType.LYNX_USB_DEVICE);
            robotUsbDeviceTty.setUsbIdentifiers(RobotUsbDevice.USBIdentifiers.createLynxIdentifiers());
            robotUsbDeviceTty.setProductName(AppUtil.getDefContext().getString(R.string.descriptionLynxEmbeddedModule));
            try {
              robotUsbDeviceTty.setBaudRate(460800);
            } catch (RobotUsbException robotUsbException) {}
          } catch (IOException iOException) {
            throw RobotCoreException.createChained(iOException, "exception in %s.open(%s)", new Object[] { "RobotUsbManagerTty", robotUsbDeviceTty.getPath() });
          } 
        } 
        throw new RobotCoreException("RobotUsbManagerTty", new Object[] { "%s is already open: unable to open second time", robotUsbDeviceTty });
      } 
      throw new RobotCoreException("RobotUsbManagerTty", new Object[] { "%s not found", robotUsbDeviceTty });
    } 
  }
  
  public List<SerialNumber> scanForDevices() throws RobotCoreException {
    ArrayList<SerialNumber> arrayList = new ArrayList();
    arrayList.add(this.serialNumberEmbedded);
    return arrayList;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\serial\RobotUsbManagerTty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */