package com.qualcomm.hardware.modernrobotics.comm;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class ModernRoboticsUsbUtil {
  public static final int ADDRESS_DEVICE_ID = 2;
  
  public static final int ADDRESS_MANUFACTURER_CODE = 1;
  
  public static final int ADDRESS_VERSION_NUMBER = 0;
  
  public static final int DEVICE_ID_DC_MOTOR_CONTROLLER = 77;
  
  public static final int DEVICE_ID_DEVICE_INTERFACE_MODULE = 65;
  
  public static final int DEVICE_ID_LEGACY_MODULE = 73;
  
  public static final int DEVICE_ID_SERVO_CONTROLLER = 83;
  
  public static final int MFG_CODE_MODERN_ROBOTICS = 77;
  
  public static DeviceManager.UsbDeviceType getDeviceType(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte[1] != 77)
      return DeviceManager.UsbDeviceType.FTDI_USB_UNKNOWN_DEVICE; 
    byte b = paramArrayOfbyte[2];
    return (b != 65) ? ((b != 73) ? ((b != 77) ? ((b != 83) ? DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_UNKNOWN_DEVICE : DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) : DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) : DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) : DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
  }
  
  public static byte[] getUsbDeviceHeader(RobotUsbDevice paramRobotUsbDevice) throws RobotUsbException {
    byte[] arrayOfByte = new byte[3];
    try {
      (new ModernRoboticsReaderWriter(paramRobotUsbDevice)).read(true, 0, arrayOfByte, null);
      return arrayOfByte;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return arrayOfByte;
    } 
  }
  
  public static RobotUsbDevice openRobotUsbDevice(boolean paramBoolean, RobotUsbManager paramRobotUsbManager, SerialNumber paramSerialNumber) throws RobotCoreException {
    if (paramBoolean)
      paramRobotUsbManager.scanForDevices(); 
    try {
      RobotUsbDevice robotUsbDevice = paramRobotUsbManager.openBySerialNumber(paramSerialNumber);
      try {
        robotUsbDevice.setBaudRate(250000);
        robotUsbDevice.setDataCharacteristics((byte)8, (byte)0, (byte)0);
        robotUsbDevice.setLatencyTimer(1);
        try {
          Thread.sleep(400L);
          return robotUsbDevice;
        } catch (InterruptedException interruptedException) {}
        Thread.currentThread().interrupt();
        return robotUsbDevice;
      } catch (Exception exception) {
        robotUsbDevice.close();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to parameterize USB device ");
        stringBuilder.append(interruptedException);
        stringBuilder.append(" - ");
        stringBuilder.append(robotUsbDevice.getProductName());
        stringBuilder.append(": ");
        stringBuilder.append(exception.getMessage());
        throw RobotCoreException.createChained(exception, stringBuilder.toString(), new Object[0]);
      } 
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to open USB device ");
      stringBuilder.append(interruptedException);
      stringBuilder.append(": ");
      stringBuilder.append(exception.getMessage());
      throw RobotCoreException.createChained(exception, stringBuilder.toString(), new Object[0]);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ModernRoboticsUsbUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */