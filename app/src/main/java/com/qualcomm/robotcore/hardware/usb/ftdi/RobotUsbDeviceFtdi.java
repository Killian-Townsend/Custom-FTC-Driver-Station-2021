package com.qualcomm.robotcore.hardware.usb.ftdi;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import org.firstinspires.ftc.robotcore.internal.ftdi.FtDevice;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbDeviceClosedException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbFTDIException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbUnspecifiedException;

public class RobotUsbDeviceFtdi extends RobotUsbDeviceImplBase implements RobotUsbDevice {
  public static boolean DEBUG = false;
  
  public static final String TAG = "RobotUsbDeviceFtdi";
  
  private int cbus_mask = 0;
  
  private int cbus_outputs = 0;
  
  private final FtDevice device;
  
  public RobotUsbDeviceFtdi(FtDevice paramFtDevice, SerialNumber paramSerialNumber) {
    super(paramSerialNumber);
    this.device = paramFtDevice;
    this.firmwareVersion = new RobotUsbDevice.FirmwareVersion();
  }
  
  private void cbus_setBitMode(int paramInt1, int paramInt2) throws InterruptedException, RobotUsbException {
    if (this.device.setBitMode((byte)paramInt1, (byte)paramInt2))
      return; 
    throw new RobotUsbUnspecifiedException("setBitMode(0x%02x 0x02x) failed", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  public void cbus_setup(int paramInt1, int paramInt2) throws InterruptedException, RobotUsbException {
    paramInt1 &= 0xF;
    this.cbus_mask = paramInt1;
    paramInt2 &= 0xF;
    this.cbus_outputs = paramInt2;
    cbus_setBitMode(paramInt1 & paramInt2 | paramInt1 << 4, 32);
  }
  
  public void cbus_write(int paramInt) throws InterruptedException, RobotUsbException {
    paramInt &= 0xF;
    this.cbus_outputs = paramInt;
    int i = this.cbus_mask;
    cbus_setBitMode(paramInt & i | i << 4, 32);
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: ldc 'RobotUsbDeviceFtdi'
    //   4: ldc 'closing %s'
    //   6: iconst_1
    //   7: anewarray java/lang/Object
    //   10: dup
    //   11: iconst_0
    //   12: aload_0
    //   13: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
    //   16: aastore
    //   17: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   20: aload_0
    //   21: getfield device : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   24: invokevirtual close : ()V
    //   27: aload_0
    //   28: invokevirtual removeFromExtantDevices : ()V
    //   31: aload_0
    //   32: monitorexit
    //   33: return
    //   34: astore_1
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    // Exception table:
    //   from	to	target	type
    //   2	31	34	finally
  }
  
  public boolean getDebugRetainBuffers() {
    return this.device.getDebugRetainBuffers();
  }
  
  public String getProductName() {
    return (this.device.getDeviceInfo()).productName;
  }
  
  public String getTag() {
    return "RobotUsbDeviceFtdi";
  }
  
  public RobotUsbDevice.USBIdentifiers getUsbIdentifiers() {
    RobotUsbDevice.USBIdentifiers uSBIdentifiers = new RobotUsbDevice.USBIdentifiers();
    int i = (this.device.getDeviceInfo()).id;
    uSBIdentifiers.vendorId = i >> 16 & 0xFFFF;
    uSBIdentifiers.productId = i & 0xFFFF;
    uSBIdentifiers.bcdDevice = (this.device.getDeviceInfo()).bcdDevice;
    return uSBIdentifiers;
  }
  
  public boolean isAttached() {
    return (new File(this.device.getUsbDevice().getDeviceName())).exists();
  }
  
  public boolean isOpen() {
    return FtDevice.isOpen(this.device);
  }
  
  public void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs) {
    this.device.logRetainedBuffers(paramLong1, paramLong2, paramString1, paramString2, paramVarArgs);
  }
  
  public boolean mightBeAtUsbPacketStart() {
    return this.device.mightBeAtUsbPacketStart();
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException {
    int i;
    if (paramInt2 > 0) {
      try {
        i = this.device.read(paramArrayOfbyte, paramInt1, paramInt2, paramLong, paramTimeWindow);
        if (i == paramInt2) {
          if (DEBUG) {
            dumpBytesReceived(paramArrayOfbyte, paramInt1, i);
            return i;
          } 
        } else {
          if (i < 0) {
            if (i != -3) {
              if (i != -2) {
                if (i == -1) {
                  RobotUsbDeviceClosedException robotUsbDeviceClosedException;
                  RobotUsbException robotUsbException = this.device.getDeviceClosedReason();
                  if (robotUsbException == null)
                    robotUsbDeviceClosedException = new RobotUsbDeviceClosedException("error: closed: FT_Device.read()==RC_DEVICE_CLOSED"); 
                  throw robotUsbDeviceClosedException;
                } 
                throw new RobotUsbUnspecifiedException("error: FT_Device.read()=%d", new Object[] { Integer.valueOf(i) });
              } 
              throw new IllegalArgumentException("illegal argument passed to RobotUsbDevice.read()");
            } 
            throw new RobotUsbUnspecifiedException("error: illegal state");
          } 
          if (i == 0)
            return 0; 
          throw new RobotUsbUnspecifiedException("unexpected result %d from FT_Device_.read()", new Object[] { Integer.valueOf(i) });
        } 
      } catch (RuntimeException runtimeException) {
        throw RobotUsbFTDIException.createChained(runtimeException, "runtime exception during read() of %d bytes on %s", new Object[] { Integer.valueOf(paramInt2), this.serialNumber });
      } 
    } else {
      return 0;
    } 
    return i;
  }
  
  public void requestReadInterrupt(boolean paramBoolean) {
    this.device.requestReadInterrupt(paramBoolean);
  }
  
  public void resetAndFlushBuffers() throws RobotUsbException {
    this.device.resetDevice();
    this.device.flushBuffers();
  }
  
  public void setBaudRate(int paramInt) throws RobotUsbException {
    this.device.setBaudRate(paramInt);
  }
  
  public void setBreak(boolean paramBoolean) throws RobotUsbException {
    if (paramBoolean) {
      this.device.setBreakOn();
      return;
    } 
    this.device.setBreakOff();
  }
  
  public void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) throws RobotUsbException {
    this.device.setDataCharacteristics(paramByte1, paramByte2, paramByte3);
  }
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    this.device.setDebugRetainBuffers(paramBoolean);
  }
  
  public void setLatencyTimer(int paramInt) throws RobotUsbException {
    this.device.setLatencyTimer((byte)paramInt);
  }
  
  public void skipToLikelyUsbPacketStart() {
    this.device.skipToLikelyUsbPacketStart();
  }
  
  public boolean supportsCbusBitbang() {
    return true;
  }
  
  public void write(byte[] paramArrayOfbyte) throws InterruptedException, RobotUsbException {
    this.device.write(paramArrayOfbyte);
  }
  
  protected static interface RunnableWithRobotUsbCommException {
    void run() throws RobotUsbException;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\ftdi\RobotUsbDeviceFtdi.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */