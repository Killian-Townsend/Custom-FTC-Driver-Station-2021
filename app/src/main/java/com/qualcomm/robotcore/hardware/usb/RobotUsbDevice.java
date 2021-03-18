package com.qualcomm.robotcore.hardware.usb;

import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public interface RobotUsbDevice {
  void close();
  
  boolean getDebugRetainBuffers();
  
  DeviceManager.UsbDeviceType getDeviceType();
  
  FirmwareVersion getFirmwareVersion();
  
  String getProductName();
  
  SerialNumber getSerialNumber();
  
  USBIdentifiers getUsbIdentifiers();
  
  boolean isAttached();
  
  boolean isOpen();
  
  void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs);
  
  boolean mightBeAtUsbPacketStart();
  
  int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException;
  
  void requestReadInterrupt(boolean paramBoolean);
  
  void resetAndFlushBuffers() throws RobotUsbException;
  
  void setBaudRate(int paramInt) throws RobotUsbException;
  
  void setBreak(boolean paramBoolean) throws RobotUsbException;
  
  void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) throws RobotUsbException;
  
  void setDebugRetainBuffers(boolean paramBoolean);
  
  void setDeviceType(DeviceManager.UsbDeviceType paramUsbDeviceType);
  
  void setFirmwareVersion(FirmwareVersion paramFirmwareVersion);
  
  void setLatencyTimer(int paramInt) throws RobotUsbException;
  
  void skipToLikelyUsbPacketStart();
  
  void write(byte[] paramArrayOfbyte) throws InterruptedException, RobotUsbException;
  
  public enum Channel {
    BOTH, NONE, RX, TX;
    
    static {
      Channel channel = new Channel("BOTH", 3);
      BOTH = channel;
      $VALUES = new Channel[] { RX, TX, NONE, channel };
    }
  }
  
  public static class FirmwareVersion {
    public int majorVersion;
    
    public int minorVersion;
    
    public FirmwareVersion() {
      this(0, 0);
    }
    
    public FirmwareVersion(int param1Int) {
      this.majorVersion = param1Int >> 4 & 0xF;
      this.minorVersion = param1Int >> 0 & 0xF;
    }
    
    public FirmwareVersion(int param1Int1, int param1Int2) {
      this.majorVersion = param1Int1;
      this.minorVersion = param1Int2;
    }
    
    public String toString() {
      return Misc.formatInvariant("v%d.%d", new Object[] { Integer.valueOf(this.majorVersion), Integer.valueOf(this.minorVersion) });
    }
  }
  
  public static class USBIdentifiers {
    private static final Set<Integer> bcdDevicesLynx;
    
    private static final Set<Integer> bcdDevicesModernRobotics = new HashSet<Integer>(Arrays.asList(new Integer[] { Integer.valueOf(1536) }));
    
    private static final Set<Integer> productIdsLynx = new HashSet<Integer>(Arrays.asList(new Integer[] { Integer.valueOf(24597) }));
    
    private static final Set<Integer> productIdsModernRobotics = new HashSet<Integer>(Arrays.asList(new Integer[] { Integer.valueOf(24577) }));
    
    private static final int vendorIdFTDI = 1027;
    
    public int bcdDevice;
    
    public int productId;
    
    public int vendorId;
    
    static {
      bcdDevicesLynx = new HashSet<Integer>(Arrays.asList(new Integer[] { Integer.valueOf(4096) }));
    }
    
    public static USBIdentifiers createLynxIdentifiers() {
      USBIdentifiers uSBIdentifiers = new USBIdentifiers();
      uSBIdentifiers.vendorId = 1027;
      uSBIdentifiers.productId = ((Integer)first(productIdsLynx)).intValue();
      uSBIdentifiers.bcdDevice = ((Integer)first(bcdDevicesLynx)).intValue();
      Assert.assertTrue(uSBIdentifiers.isLynxDevice());
      return uSBIdentifiers;
    }
    
    protected static <T> T first(Set<T> param1Set) {
      Iterator<T> iterator = param1Set.iterator();
      return iterator.hasNext() ? iterator.next() : null;
    }
    
    public boolean isLynxDevice() {
      return (this.vendorId == 1027 && productIdsLynx.contains(Integer.valueOf(this.productId)) && bcdDevicesLynx.contains(Integer.valueOf(this.bcdDevice & 0xFF00)));
    }
    
    public boolean isModernRoboticsDevice() {
      return (this.vendorId == 1027 && productIdsModernRobotics.contains(Integer.valueOf(this.productId)) && bcdDevicesModernRobotics.contains(Integer.valueOf(this.bcdDevice & 0xFF00)));
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\RobotUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */