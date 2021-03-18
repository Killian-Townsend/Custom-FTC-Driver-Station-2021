package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnable;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableSegment;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public class ModernRoboticsUsbLegacyModule extends ModernRoboticsUsbI2cController implements LegacyModule {
  public static final int[] ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14 };
  
  public static final int ADDRESS_ANALOG_PORT_S0 = 4;
  
  public static final int ADDRESS_ANALOG_PORT_S1 = 6;
  
  public static final int ADDRESS_ANALOG_PORT_S2 = 8;
  
  public static final int ADDRESS_ANALOG_PORT_S3 = 10;
  
  public static final int ADDRESS_ANALOG_PORT_S4 = 12;
  
  public static final int ADDRESS_ANALOG_PORT_S5 = 14;
  
  public static final int ADDRESS_BUFFER_STATUS = 3;
  
  public static final int[] ADDRESS_I2C_PORT_MAP = new int[] { 16, 48, 80, 112, 144, 176 };
  
  public static final int ADDRESS_I2C_PORT_S1 = 48;
  
  public static final int ADDRESS_I2C_PORT_S2 = 80;
  
  public static final int ADDRESS_I2C_PORT_S3 = 112;
  
  public static final int ADDRESS_I2C_PORT_S4 = 144;
  
  public static final int ADDRESS_I2C_PORT_S5 = 176;
  
  public static final int ADDRESS_I2C_PORT_SO = 16;
  
  public static final int[] BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
  
  public static final byte BUFFER_FLAG_S0 = 1;
  
  public static final byte BUFFER_FLAG_S1 = 2;
  
  public static final byte BUFFER_FLAG_S2 = 4;
  
  public static final byte BUFFER_FLAG_S3 = 8;
  
  public static final byte BUFFER_FLAG_S4 = 16;
  
  public static final byte BUFFER_FLAG_S5 = 32;
  
  public static final boolean DEBUG_LOGGING = false;
  
  public static final int[] DIGITAL_LINE = new int[] { 4, 8 };
  
  public static final byte I2C_ACTION_FLAG = -1;
  
  public static final byte I2C_NO_ACTION_FLAG = 0;
  
  private static final int LEGACY_MODULE_FORCE_IO_DELAY = 15;
  
  public static final double MAX_ANALOG_INPUT_VOLTAGE = 5.0D;
  
  private static final double MIN_I2C_WRITE_RATE = 2.0D;
  
  public static final int MONITOR_LENGTH = 13;
  
  public static final byte NUMBER_OF_PORTS = 6;
  
  public static final byte NXT_MODE_9V_ENABLED = 2;
  
  public static final byte NXT_MODE_ANALOG = 0;
  
  public static final byte NXT_MODE_DIGITAL_0 = 4;
  
  public static final byte NXT_MODE_DIGITAL_1 = 8;
  
  public static final byte NXT_MODE_I2C = 1;
  
  public static final byte NXT_MODE_READ = -128;
  
  public static final byte NXT_MODE_WRITE = 0;
  
  public static final byte OFFSET_I2C_PORT_FLAG = 31;
  
  public static final byte OFFSET_I2C_PORT_I2C_ADDRESS = 1;
  
  public static final byte OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
  
  public static final byte OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
  
  public static final byte OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
  
  public static final byte OFFSET_I2C_PORT_MODE = 0;
  
  public static final int[] PORT_9V_CAPABLE = new int[] { 4, 5 };
  
  private static final int SEGMENT_OFFSET_PORT_FLAG_ONLY = 6;
  
  public static final byte SIZE_ANALOG_BUFFER = 2;
  
  public static final byte SIZE_I2C_BUFFER = 27;
  
  public static final byte SIZE_OF_PORT_BUFFER = 32;
  
  public static final byte START_ADDRESS = 3;
  
  public static final String TAG = "MRLegacyModule";
  
  protected final byte[] lastI2cPortModes = new byte[6];
  
  private final I2cController.I2cPortReadyCallback[] portReadyCallback = new I2cController.I2cPortReadyCallback[6];
  
  private final ReadWriteRunnableSegment[] segments = new ReadWriteRunnableSegment[12];
  
  public ModernRoboticsUsbLegacyModule(Context paramContext, SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    super(6, paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable(paramContext, paramSerialNumber) {
          public ReadWriteRunnable create(RobotUsbDevice param1RobotUsbDevice) {
            return (ReadWriteRunnable)new ReadWriteRunnableStandard(context, serialNumber, param1RobotUsbDevice, 13, 3, false);
          }
        });
  }
  
  private boolean isPortReady(int paramInt, byte paramByte) {
    boolean bool1 = isArmed();
    boolean bool = true;
    if (bool1) {
      if ((BUFFER_FLAG_MAP[paramInt] & paramByte) == 0)
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  private void throwIfBufferLengthIsInvalid(int paramInt) {
    if (paramInt >= 0 && paramInt <= 27)
      return; 
    throw new IllegalArgumentException(String.format("buffer length of %d is invalid; max value is %d", new Object[] { Integer.valueOf(paramInt), Byte.valueOf((byte)27) }));
  }
  
  private void throwIfDigitalLineIsInvalid(int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1)
        return; 
      throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
    } 
  }
  
  public void clearI2cPortActionFlag(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.segments[paramInt].getWriteLock().lock();
      this.segments[paramInt].getWriteBuffer()[31] = 0;
      return;
    } finally {
      this.segments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public void copyBufferIntoWriteBuffer(int paramInt, byte[] paramArrayOfbyte) {
    throwIfI2cPortIsInvalid(paramInt);
    throwIfBufferLengthIsInvalid(paramArrayOfbyte.length);
    try {
      this.segments[paramInt].getWriteLock().lock();
      System.arraycopy(paramArrayOfbyte, 0, this.segments[paramInt].getWriteBuffer(), 4, paramArrayOfbyte.length);
      return;
    } finally {
      this.segments[paramInt].getWriteLock().unlock();
    } 
  }
  
  protected void createSegments() {
    for (int i = 0; i < 6; i++) {
      this.segments[i] = this.readWriteRunnable.createSegment(i, ADDRESS_I2C_PORT_MAP[i], 32);
      ReadWriteRunnableSegment[] arrayOfReadWriteRunnableSegment = this.segments;
      int j = i + 6;
      arrayOfReadWriteRunnableSegment[j] = this.readWriteRunnable.createSegment(j, ADDRESS_I2C_PORT_MAP[i] + 31, 1);
    } 
  }
  
  public void deregisterForPortReadyCallback(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.portReadyCallback[paramInt] = null;
  }
  
  protected void doArm() throws RobotCoreException, InterruptedException {
    super.doArm();
    createSegments();
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    super.doPretend();
    createSegments();
  }
  
  public void enable9v(int paramInt, boolean paramBoolean) {
    int i;
    if (Arrays.binarySearch(PORT_9V_CAPABLE, paramInt) >= 0) {
      try {
        this.segments[paramInt].getWriteLock().lock();
        i = this.segments[paramInt].getWriteBuffer()[0];
      } finally {
        this.segments[paramInt].getWriteLock().unlock();
      } 
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("9v is only available on the following ports: ");
      stringBuilder.append(Arrays.toString(PORT_9V_CAPABLE));
      throw new IllegalArgumentException(stringBuilder.toString());
    } 
    byte b = (byte)i;
    this.segments[paramInt].getWriteBuffer()[0] = b;
    this.segments[paramInt].getWriteLock().unlock();
    writeI2cCacheToController(paramInt);
  }
  
  public void enableAnalogReadMode(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.segments[paramInt].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = 0;
      arrayOfByte2[paramInt] = 0;
      this.segments[paramInt].getWriteLock().unlock();
      return;
    } finally {
      this.segments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public void enableI2cReadMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfBufferLengthIsInvalid(paramInt3);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = -127;
      arrayOfByte2[paramInt1] = -127;
      arrayOfByte1[1] = (byte)paramI2cAddr.get8Bit();
      arrayOfByte1[2] = (byte)paramInt2;
      arrayOfByte1[3] = (byte)paramInt3;
      return;
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public void enableI2cWriteMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfBufferLengthIsInvalid(paramInt3);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = 1;
      arrayOfByte2[paramInt1] = 1;
      arrayOfByte1[1] = (byte)paramI2cAddr.get8Bit();
      arrayOfByte1[2] = (byte)paramInt2;
      arrayOfByte1[3] = (byte)paramInt3;
      return;
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("USB ");
    stringBuilder.append(getSerialNumber());
    return stringBuilder.toString();
  }
  
  public byte[] getCopyOfReadBuffer(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.segments[paramInt].getReadLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt].getReadBuffer();
      byte b = arrayOfByte1[3];
      byte[] arrayOfByte2 = new byte[b];
      System.arraycopy(arrayOfByte1, 4, arrayOfByte2, 0, b);
      return arrayOfByte2;
    } finally {
      this.segments[paramInt].getReadLock().unlock();
    } 
  }
  
  public byte[] getCopyOfWriteBuffer(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.segments[paramInt].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt].getWriteBuffer();
      byte b = arrayOfByte1[3];
      byte[] arrayOfByte2 = new byte[b];
      System.arraycopy(arrayOfByte1, 4, arrayOfByte2, 0, b);
      return arrayOfByte2;
    } finally {
      this.segments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public String getDeviceName() {
    return String.format("%s %s", new Object[] { this.context.getString(R.string.moduleDisplayNameLegacyModule), this.robotUsbDevice.getFirmwareVersion() });
  }
  
  public I2cController.I2cPortReadyCallback getI2cPortReadyCallback(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.portReadyCallback[paramInt];
  }
  
  public byte[] getI2cReadCache(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.segments[paramInt].getReadBuffer();
  }
  
  public Lock getI2cReadCacheLock(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.segments[paramInt].getReadLock();
  }
  
  public TimeWindow getI2cReadCacheTimeWindow(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.segments[paramInt].getTimeWindow();
  }
  
  public byte[] getI2cWriteCache(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.segments[paramInt].getWriteBuffer();
  }
  
  public Lock getI2cWriteCacheLock(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.segments[paramInt].getWriteLock();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public double getMaxAnalogInputVoltage() {
    return 5.0D;
  }
  
  public int getMaxI2cWriteLatency(int paramInt) {
    return 60;
  }
  
  protected String getTag() {
    return "MRLegacyModule";
  }
  
  public void initializeHardware() {
    for (int i = 0; i < 6; i++) {
      enableAnalogReadMode(i);
      this.readWriteRunnable.queueSegmentWrite(i);
    } 
  }
  
  public boolean isI2cPortActionFlagSet(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      boolean bool;
      this.segments[paramInt].getReadLock().lock();
      byte b = this.segments[paramInt].getReadBuffer()[31];
      if (b == -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } finally {
      this.segments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortInReadMode(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      byte b;
      this.segments[paramInt].getReadLock().lock();
      byte[] arrayOfByte = this.segments[paramInt].getReadBuffer();
      boolean bool1 = isArmed();
      boolean bool = false;
      if (bool1) {
        b = arrayOfByte[0];
      } else {
        b = this.lastI2cPortModes[paramInt];
      } 
      if (b == -127)
        bool = true; 
      return bool;
    } finally {
      this.segments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortInWriteMode(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      byte b;
      this.segments[paramInt].getReadLock().lock();
      byte[] arrayOfByte = this.segments[paramInt].getReadBuffer();
      boolean bool1 = isArmed();
      boolean bool = false;
      if (bool1) {
        b = arrayOfByte[0];
      } else {
        b = this.lastI2cPortModes[paramInt];
      } 
      if (b == 1)
        bool = true; 
      return bool;
    } finally {
      this.segments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortReady(int paramInt) {
    return isPortReady(paramInt, read8(3));
  }
  
  public byte[] readAnalogRaw(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return read(ADDRESS_ANALOG_PORT_MAP[paramInt], 2);
  }
  
  public double readAnalogVoltage(int paramInt) {
    return Range.scale((TypeConversion.byteArrayToShort(readAnalogRaw(paramInt), ByteOrder.LITTLE_ENDIAN) & 0x3FF), 0.0D, 1023.0D, 0.0D, getMaxAnalogInputVoltage());
  }
  
  public void readComplete() throws InterruptedException {
    if (this.portReadyCallback == null)
      return; 
    byte b = read8(3);
    for (int i = 0; i < 6; i++) {
      if (this.portReadyCallback[i] != null && isPortReady(i, b))
        this.portReadyCallback[i].portIsReady(i); 
    } 
  }
  
  public void readI2cCacheFromController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.readWriteRunnable.queueSegmentRead(paramInt);
  }
  
  @Deprecated
  public void readI2cCacheFromModule(int paramInt) {
    readI2cCacheFromController(paramInt);
  }
  
  public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback paramI2cPortReadyCallback, int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.portReadyCallback[paramInt] = paramI2cPortReadyCallback;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setData(int paramInt1, byte[] paramArrayOfbyte, int paramInt2) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfBufferLengthIsInvalid(paramInt2);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte = this.segments[paramInt1].getWriteBuffer();
      System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 4, paramInt2);
      arrayOfByte[3] = (byte)paramInt2;
      return;
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public void setDigitalLine(int paramInt1, int paramInt2, boolean paramBoolean) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfDigitalLineIsInvalid(paramInt2);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte b1 = this.segments[paramInt1].getWriteBuffer()[0];
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
    byte b = (byte)paramInt2;
    this.segments[paramInt1].getWriteBuffer()[0] = b;
    this.segments[paramInt1].getWriteLock().unlock();
    writeI2cCacheToController(paramInt1);
  }
  
  public void setI2cPortActionFlag(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.segments[paramInt].getWriteLock().lock();
      this.segments[paramInt].getWriteBuffer()[31] = -1;
      return;
    } finally {
      this.segments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public void setReadMode(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    throwIfI2cPortIsInvalid(paramInt1);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = -127;
      arrayOfByte2[paramInt1] = -127;
      arrayOfByte1[1] = (byte)paramInt2;
      arrayOfByte1[2] = (byte)paramInt3;
      arrayOfByte1[3] = (byte)paramInt4;
      return;
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public void setWriteMode(int paramInt1, int paramInt2, int paramInt3) {
    throwIfI2cPortIsInvalid(paramInt1);
    try {
      this.segments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.segments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = 1;
      arrayOfByte2[paramInt1] = 1;
      arrayOfByte1[1] = (byte)paramInt2;
      arrayOfByte1[2] = (byte)paramInt3;
      return;
    } finally {
      this.segments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public void writeI2cCacheToController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.readWriteRunnable.queueSegmentWrite(paramInt);
  }
  
  @Deprecated
  public void writeI2cCacheToModule(int paramInt) {
    writeI2cCacheToController(paramInt);
  }
  
  public void writeI2cPortFlagOnlyToController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    ReadWriteRunnableSegment[] arrayOfReadWriteRunnableSegment = this.segments;
    ReadWriteRunnableSegment readWriteRunnableSegment1 = arrayOfReadWriteRunnableSegment[paramInt];
    paramInt += 6;
    ReadWriteRunnableSegment readWriteRunnableSegment2 = arrayOfReadWriteRunnableSegment[paramInt];
    try {
      readWriteRunnableSegment1.getWriteLock().lock();
      readWriteRunnableSegment2.getWriteLock().lock();
      readWriteRunnableSegment2.getWriteBuffer()[0] = readWriteRunnableSegment1.getWriteBuffer()[31];
      readWriteRunnableSegment1.getWriteLock().unlock();
      return;
    } finally {
      readWriteRunnableSegment1.getWriteLock().unlock();
      readWriteRunnableSegment2.getWriteLock().unlock();
    } 
  }
  
  @Deprecated
  public void writeI2cPortFlagOnlyToModule(int paramInt) {
    writeI2cPortFlagOnlyToController(paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbLegacyModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */