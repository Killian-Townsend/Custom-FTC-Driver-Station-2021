package com.qualcomm.hardware.modernrobotics.comm;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.collections.CircularByteBuffer;
import org.firstinspires.ftc.robotcore.internal.collections.MarkedItemQueue;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class RobotUsbDevicePretendModernRobotics implements RobotUsbDevice {
  protected CircularByteBuffer circularByteBuffer = new CircularByteBuffer(0);
  
  protected boolean debugRetainBuffers = false;
  
  protected DeviceManager.UsbDeviceType deviceType = DeviceManager.UsbDeviceType.FTDI_USB_UNKNOWN_DEVICE;
  
  protected RobotUsbDevice.FirmwareVersion firmwareVersion = new RobotUsbDevice.FirmwareVersion();
  
  protected boolean interruptRequested = false;
  
  protected MarkedItemQueue markedItemQueue = new MarkedItemQueue();
  
  protected ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> requestAllocationContext = new ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest>();
  
  protected ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse> responseAllocationContext = new ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse>();
  
  protected SerialNumber serialNumber;
  
  public RobotUsbDevicePretendModernRobotics(SerialNumber paramSerialNumber) {
    this.serialNumber = paramSerialNumber;
  }
  
  public void close() {}
  
  public boolean getDebugRetainBuffers() {
    return this.debugRetainBuffers;
  }
  
  public DeviceManager.UsbDeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public RobotUsbDevice.FirmwareVersion getFirmwareVersion() {
    return this.firmwareVersion;
  }
  
  public String getProductName() {
    return Misc.formatForUser("pretend %s", new Object[] { this.deviceType });
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }
  
  public RobotUsbDevice.USBIdentifiers getUsbIdentifiers() {
    RobotUsbDevice.USBIdentifiers uSBIdentifiers = new RobotUsbDevice.USBIdentifiers();
    uSBIdentifiers.vendorId = 1027;
    uSBIdentifiers.productId = 0;
    uSBIdentifiers.bcdDevice = 0;
    return uSBIdentifiers;
  }
  
  public boolean isAttached() {
    return true;
  }
  
  public boolean isOpen() {
    return true;
  }
  
  public void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs) {
    RobotLog.ee(paramString1, paramString2, paramVarArgs);
  }
  
  public boolean mightBeAtUsbPacketStart() {
    return (this.markedItemQueue.isAtMarkedItem() || this.markedItemQueue.isEmpty());
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) {
    paramInt1 = this.circularByteBuffer.read(paramArrayOfbyte, paramInt1, paramInt2);
    this.markedItemQueue.removeItems(paramInt1);
    if (paramTimeWindow != null)
      paramTimeWindow.clear(); 
    return paramInt1;
  }
  
  public void requestReadInterrupt(boolean paramBoolean) {
    this.interruptRequested = paramBoolean;
  }
  
  public void resetAndFlushBuffers() {
    this.circularByteBuffer.clear();
    this.markedItemQueue.clear();
  }
  
  public void setBaudRate(int paramInt) {}
  
  public void setBreak(boolean paramBoolean) {}
  
  public void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) {}
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    this.debugRetainBuffers = paramBoolean;
  }
  
  public void setDeviceType(DeviceManager.UsbDeviceType paramUsbDeviceType) {
    this.deviceType = paramUsbDeviceType;
  }
  
  public void setFirmwareVersion(RobotUsbDevice.FirmwareVersion paramFirmwareVersion) {
    this.firmwareVersion = paramFirmwareVersion;
  }
  
  public void setLatencyTimer(int paramInt) {}
  
  public void skipToLikelyUsbPacketStart() {
    int i = this.markedItemQueue.removeUpToNextMarkedItemOrEnd();
    this.circularByteBuffer.skip(i);
  }
  
  public void write(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder1;
    ModernRoboticsRequest modernRoboticsRequest;
    StringBuilder stringBuilder2 = null;
    byte[] arrayOfByte = null;
    try {
      modernRoboticsRequest = ModernRoboticsRequest.from(this.requestAllocationContext, paramArrayOfbyte);
    } finally {
      arrayOfByte = null;
      modernRoboticsRequest = null;
    } 
    if (stringBuilder1 != null)
      stringBuilder1.close(); 
    if (modernRoboticsRequest != null)
      modernRoboticsRequest.close(); 
    throw arrayOfByte;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\RobotUsbDevicePretendModernRobotics.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */