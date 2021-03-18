package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public abstract class RobotUsbDeviceImplBase implements RobotUsbDevice {
  protected static final ConcurrentHashMap<SerialNumber, DeviceManager.UsbDeviceType> deviceTypes;
  
  protected static final ConcurrentHashMap<SerialNumber, RobotUsbDevice> extantDevices = new ConcurrentHashMap<SerialNumber, RobotUsbDevice>();
  
  protected DeviceManager.UsbDeviceType deviceType;
  
  protected RobotUsbDevice.FirmwareVersion firmwareVersion = new RobotUsbDevice.FirmwareVersion();
  
  protected final SerialNumber serialNumber;
  
  static {
    deviceTypes = new ConcurrentHashMap<SerialNumber, DeviceManager.UsbDeviceType>();
  }
  
  protected RobotUsbDeviceImplBase(SerialNumber paramSerialNumber) {
    this.serialNumber = paramSerialNumber;
    DeviceManager.UsbDeviceType usbDeviceType = deviceTypes.get(paramSerialNumber);
    this.deviceType = usbDeviceType;
    if (usbDeviceType == null)
      this.deviceType = DeviceManager.UsbDeviceType.UNKNOWN_DEVICE; 
    Assert.assertFalse(extantDevices.contains(paramSerialNumber));
    extantDevices.put(paramSerialNumber, this);
  }
  
  public static DeviceManager.UsbDeviceType getDeviceType(SerialNumber paramSerialNumber) {
    DeviceManager.UsbDeviceType usbDeviceType2 = deviceTypes.get(paramSerialNumber);
    DeviceManager.UsbDeviceType usbDeviceType1 = usbDeviceType2;
    if (usbDeviceType2 == null)
      usbDeviceType1 = DeviceManager.UsbDeviceType.UNKNOWN_DEVICE; 
    return usbDeviceType1;
  }
  
  public static Collection<RobotUsbDevice> getExtantDevices() {
    return extantDevices.values();
  }
  
  public static boolean isOpen(SerialNumber paramSerialNumber) {
    return extantDevices.containsKey(paramSerialNumber);
  }
  
  protected void dumpBytesReceived(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    RobotLog.logBytes(getTag(), "received", paramArrayOfbyte, paramInt1, paramInt2);
  }
  
  protected void dumpBytesSent(byte[] paramArrayOfbyte) {
    RobotLog.logBytes(getTag(), "sent", paramArrayOfbyte, paramArrayOfbyte.length);
  }
  
  public DeviceManager.UsbDeviceType getDeviceType() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deviceType : Lcom/qualcomm/robotcore/hardware/DeviceManager$UsbDeviceType;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public RobotUsbDevice.FirmwareVersion getFirmwareVersion() {
    return this.firmwareVersion;
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }
  
  public abstract String getTag();
  
  protected void removeFromExtantDevices() {
    extantDevices.remove(this.serialNumber);
  }
  
  public void setDeviceType(DeviceManager.UsbDeviceType paramUsbDeviceType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield deviceType : Lcom/qualcomm/robotcore/hardware/DeviceManager$UsbDeviceType;
    //   7: getstatic com/qualcomm/robotcore/hardware/usb/RobotUsbDeviceImplBase.deviceTypes : Ljava/util/concurrent/ConcurrentHashMap;
    //   10: aload_0
    //   11: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
    //   14: aload_1
    //   15: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   18: pop
    //   19: aload_0
    //   20: monitorexit
    //   21: return
    //   22: astore_1
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_1
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	22	finally
  }
  
  public void setFirmwareVersion(RobotUsbDevice.FirmwareVersion paramFirmwareVersion) {
    this.firmwareVersion = paramFirmwareVersion;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotUsbDeviceImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */