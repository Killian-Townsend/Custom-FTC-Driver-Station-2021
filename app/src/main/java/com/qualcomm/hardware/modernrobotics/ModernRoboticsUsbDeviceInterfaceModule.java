package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnable;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableSegment;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public class ModernRoboticsUsbDeviceInterfaceModule extends ModernRoboticsUsbI2cController implements DeviceInterfaceModule {
  public static final int ADDRESS_ANALOG_PORT_A0 = 4;
  
  public static final int ADDRESS_ANALOG_PORT_A1 = 6;
  
  public static final int ADDRESS_ANALOG_PORT_A2 = 8;
  
  public static final int ADDRESS_ANALOG_PORT_A3 = 10;
  
  public static final int ADDRESS_ANALOG_PORT_A4 = 12;
  
  public static final int ADDRESS_ANALOG_PORT_A5 = 14;
  
  public static final int ADDRESS_ANALOG_PORT_A6 = 16;
  
  public static final int ADDRESS_ANALOG_PORT_A7 = 18;
  
  public static final int[] ADDRESS_ANALOG_PORT_MAP;
  
  public static final int ADDRESS_BUFFER_STATUS = 3;
  
  public static final int[] ADDRESS_DIGITAL_BIT_MASK;
  
  public static final int ADDRESS_DIGITAL_INPUT_STATE = 20;
  
  public static final int ADDRESS_DIGITAL_IO_CONTROL = 21;
  
  public static final int ADDRESS_DIGITAL_OUTPUT_STATE = 22;
  
  public static final int ADDRESS_I2C0 = 48;
  
  public static final int ADDRESS_I2C1 = 80;
  
  public static final int ADDRESS_I2C2 = 112;
  
  public static final int ADDRESS_I2C3 = 144;
  
  public static final int ADDRESS_I2C4 = 176;
  
  public static final int ADDRESS_I2C5 = 208;
  
  public static final int[] ADDRESS_I2C_PORT_MAP;
  
  public static final int ADDRESS_LED_SET = 23;
  
  public static final int ADDRESS_PULSE_OUTPUT_PORT_0 = 36;
  
  public static final int ADDRESS_PULSE_OUTPUT_PORT_1 = 40;
  
  public static final int[] ADDRESS_PULSE_OUTPUT_PORT_MAP;
  
  public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_0 = 24;
  
  public static final int ADDRESS_VOLTAGE_OUTPUT_PORT_1 = 30;
  
  public static final int[] ADDRESS_VOLTAGE_OUTPUT_PORT_MAP;
  
  public static final int ANALOG_VOLTAGE_OUTPUT_BUFFER_SIZE = 5;
  
  public static final byte BUFFER_FLAG_I2C0 = 1;
  
  public static final byte BUFFER_FLAG_I2C1 = 2;
  
  public static final byte BUFFER_FLAG_I2C2 = 4;
  
  public static final byte BUFFER_FLAG_I2C3 = 8;
  
  public static final byte BUFFER_FLAG_I2C4 = 16;
  
  public static final byte BUFFER_FLAG_I2C5 = 32;
  
  public static final int[] BUFFER_FLAG_MAP;
  
  public static final int D0_MASK = 1;
  
  public static final int D1_MASK = 2;
  
  public static final int D2_MASK = 4;
  
  public static final int D3_MASK = 8;
  
  public static final int D4_MASK = 16;
  
  public static final int D5_MASK = 32;
  
  public static final int D6_MASK = 64;
  
  public static final int D7_MASK = 128;
  
  public static final boolean DEBUG_LOGGING = false;
  
  public static final byte I2C_ACTION_FLAG = -1;
  
  public static final byte I2C_MODE_READ = -128;
  
  public static final byte I2C_MODE_WRITE = 0;
  
  public static final byte I2C_NO_ACTION_FLAG = 0;
  
  public static final int I2C_PORT_BUFFER_SIZE = 32;
  
  public static final int LED_0_BIT_MASK = 1;
  
  public static final int LED_1_BIT_MASK = 2;
  
  public static final int[] LED_BIT_MASK_MAP = new int[] { 1, 2 };
  
  public static final double MAX_ANALOG_INPUT_VOLTAGE = 5.0D;
  
  public static final int MAX_ANALOG_PORT_NUMBER = 7;
  
  public static final int MAX_I2C_PORT_NUMBER = 5;
  
  public static final int MAX_NEW_I2C_ADDRESS = 126;
  
  public static final int MIN_ANALOG_PORT_NUMBER = 0;
  
  public static final int MIN_I2C_PORT_NUMBER = 0;
  
  public static final int MIN_NEW_I2C_ADDRESS = 16;
  
  public static final int MONITOR_LENGTH = 21;
  
  public static final int NUMBER_OF_PORTS = 6;
  
  public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_FREQ = 2;
  
  public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_MODE = 4;
  
  public static final int OFFSET_ANALOG_VOLTAGE_OUTPUT_VOLTAGE = 0;
  
  public static final int OFFSET_I2C_PORT_FLAG = 31;
  
  public static final int OFFSET_I2C_PORT_I2C_ADDRESS = 1;
  
  public static final int OFFSET_I2C_PORT_MEMORY_ADDRESS = 2;
  
  public static final int OFFSET_I2C_PORT_MEMORY_BUFFER = 4;
  
  public static final int OFFSET_I2C_PORT_MEMORY_LENGTH = 3;
  
  public static final int OFFSET_I2C_PORT_MODE = 0;
  
  public static final int OFFSET_PULSE_OUTPUT_PERIOD = 2;
  
  public static final int OFFSET_PULSE_OUTPUT_TIME = 0;
  
  public static final int PULSE_OUTPUT_BUFFER_SIZE = 4;
  
  private static final int SEGMENT_KEY_ANALOG_VOLTAGE_OUTPUT_PORT_0 = 0;
  
  private static final int SEGMENT_KEY_ANALOG_VOLTAGE_OUTPUT_PORT_1 = 1;
  
  private static final int[] SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP;
  
  private static final int SEGMENT_KEY_I2C_PORT_0 = 4;
  
  private static final int SEGMENT_KEY_I2C_PORT_0_FLAG_ONLY = 10;
  
  private static final int SEGMENT_KEY_I2C_PORT_1 = 5;
  
  private static final int SEGMENT_KEY_I2C_PORT_1_FLAG_ONLY = 11;
  
  private static final int SEGMENT_KEY_I2C_PORT_2 = 6;
  
  private static final int SEGMENT_KEY_I2C_PORT_2_FLAG_ONLY = 12;
  
  private static final int SEGMENT_KEY_I2C_PORT_3 = 7;
  
  private static final int SEGMENT_KEY_I2C_PORT_3_FLAG_ONLY = 13;
  
  private static final int SEGMENT_KEY_I2C_PORT_4 = 8;
  
  private static final int SEGMENT_KEY_I2C_PORT_4_FLAG_ONLY = 14;
  
  private static final int SEGMENT_KEY_I2C_PORT_5 = 9;
  
  private static final int SEGMENT_KEY_I2C_PORT_5_FLAG_ONLY = 15;
  
  private static final int[] SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP;
  
  private static final int[] SEGMENT_KEY_I2C_PORT_MAP;
  
  private static final int SEGMENT_KEY_PULSE_OUTPUT_PORT_0 = 2;
  
  private static final int SEGMENT_KEY_PULSE_OUTPUT_PORT_1 = 3;
  
  private static final int[] SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP;
  
  public static final int SIZE_ANALOG_BUFFER = 2;
  
  public static final int SIZE_I2C_BUFFER = 27;
  
  public static final int START_ADDRESS = 3;
  
  public static final String TAG = "MRDeviceInterfaceModule";
  
  public static final int WORD_SIZE = 2;
  
  private ReadWriteRunnableSegment[] analogVoltagePortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP.length];
  
  private ReadWriteRunnableSegment[] i2cPortFlagOnlySegments = new ReadWriteRunnableSegment[SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP.length];
  
  private final I2cController.I2cPortReadyCallback[] i2cPortReadyCallback = new I2cController.I2cPortReadyCallback[6];
  
  private ReadWriteRunnableSegment[] i2cPortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_I2C_PORT_MAP.length];
  
  protected final byte[] lastI2cPortModes = new byte[6];
  
  private ReadWriteRunnableSegment[] pulseOutputPortSegments = new ReadWriteRunnableSegment[SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP.length];
  
  static {
    ADDRESS_DIGITAL_BIT_MASK = new int[] { 1, 2, 4, 8, 16, 32, 64, 128 };
    ADDRESS_ANALOG_PORT_MAP = new int[] { 4, 6, 8, 10, 12, 14, 16, 18 };
    ADDRESS_VOLTAGE_OUTPUT_PORT_MAP = new int[] { 24, 30 };
    ADDRESS_PULSE_OUTPUT_PORT_MAP = new int[] { 36, 40 };
    ADDRESS_I2C_PORT_MAP = new int[] { 48, 80, 112, 144, 176, 208 };
    BUFFER_FLAG_MAP = new int[] { 1, 2, 4, 8, 16, 32 };
    SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP = new int[] { 0, 1 };
    SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP = new int[] { 2, 3 };
    SEGMENT_KEY_I2C_PORT_MAP = new int[] { 4, 5, 6, 7, 8, 9 };
    SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP = new int[] { 10, 11, 12, 13, 14, 15 };
  }
  
  public ModernRoboticsUsbDeviceInterfaceModule(Context paramContext, SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    super(6, paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable(paramContext, paramSerialNumber) {
          public ReadWriteRunnable create(RobotUsbDevice param1RobotUsbDevice) {
            return (ReadWriteRunnable)new ReadWriteRunnableStandard(context, serialNumber, param1RobotUsbDevice, 21, 3, false);
          }
        });
  }
  
  private DigitalChannel.Mode byteToRunMode(int paramInt1, int paramInt2) {
    return ((ADDRESS_DIGITAL_BIT_MASK[paramInt1] & paramInt2) > 0) ? DigitalChannel.Mode.OUTPUT : DigitalChannel.Mode.INPUT;
  }
  
  private boolean isI2cPortReady(int paramInt, byte paramByte) {
    boolean bool1 = isArmed();
    boolean bool = true;
    if (bool1) {
      if ((BUFFER_FLAG_MAP[paramInt] & paramByte) == 0)
        return true; 
      bool = false;
    } 
    return bool;
  }
  
  private int runModeToBitMask(int paramInt, DigitalChannel.Mode paramMode) {
    return (paramMode == DigitalChannel.Mode.OUTPUT) ? ADDRESS_DIGITAL_BIT_MASK[paramInt] : ADDRESS_DIGITAL_BIT_MASK[paramInt];
  }
  
  private void throwIfAnalogOutputPortIsInvalid(int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1)
        return; 
      throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(paramInt) }));
    } 
  }
  
  private void throwIfAnalogPortIsInvalid(int paramInt) {
    if (paramInt >= 0 && paramInt <= 7)
      return; 
    throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(7) }));
  }
  
  private void throwIfBufferIsTooLarge(int paramInt) {
    if (paramInt <= 27)
      return; 
    throw new IllegalArgumentException(String.format("buffer is too large (%d byte), max size is %d bytes", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(27) }));
  }
  
  private void throwIfDigitalLineIsInvalid(int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1)
        return; 
      throw new IllegalArgumentException("line is invalid, valid lines are 0 and 1");
    } 
  }
  
  private void throwIfLEDChannelIsInvalid(int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1)
        return; 
      throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(paramInt) }));
    } 
  }
  
  public static void throwIfModernRoboticsI2cAddressIsInvalid(I2cAddr paramI2cAddr) {
    if (paramI2cAddr.get8Bit() >= 16 && paramI2cAddr.get8Bit() <= 126)
      return; 
    throw new IllegalArgumentException(String.format("New I2C address %d is invalid; valid range is: %d..%d", new Object[] { Integer.valueOf(paramI2cAddr.get8Bit()), Integer.valueOf(16), Integer.valueOf(126) }));
  }
  
  private void throwIfPulseWidthPortIsInvalid(int paramInt) {
    if (paramInt != 0) {
      if (paramInt == 1)
        return; 
      throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are 0 and 1.", new Object[] { Integer.valueOf(paramInt) }));
    } 
  }
  
  public void clearI2cPortActionFlag(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.i2cPortSegments[paramInt].getWriteLock().lock();
      this.i2cPortSegments[paramInt].getWriteBuffer()[31] = 0;
      return;
    } finally {
      this.i2cPortSegments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public void copyBufferIntoWriteBuffer(int paramInt, byte[] paramArrayOfbyte) {
    throwIfI2cPortIsInvalid(paramInt);
    throwIfBufferIsTooLarge(paramArrayOfbyte.length);
    try {
      this.i2cPortSegments[paramInt].getWriteLock().lock();
      System.arraycopy(paramArrayOfbyte, 0, this.i2cPortSegments[paramInt].getWriteBuffer(), 4, paramArrayOfbyte.length);
      return;
    } finally {
      this.i2cPortSegments[paramInt].getWriteLock().unlock();
    } 
  }
  
  protected void createSegments() {
    int i;
    for (i = 0; i < SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP.length; i++)
      this.analogVoltagePortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_ANALOG_VOLTAGE_PORT_MAP[i], ADDRESS_VOLTAGE_OUTPUT_PORT_MAP[i], 5); 
    for (i = 0; i < SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP.length; i++)
      this.pulseOutputPortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_PULSE_OUTPUT_PORT_MAP[i], ADDRESS_PULSE_OUTPUT_PORT_MAP[i], 4); 
    for (i = 0; i < SEGMENT_KEY_I2C_PORT_MAP.length; i++) {
      this.i2cPortSegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_I2C_PORT_MAP[i], ADDRESS_I2C_PORT_MAP[i], 32);
      this.i2cPortFlagOnlySegments[i] = this.readWriteRunnable.createSegment(SEGMENT_KEY_I2C_PORT_FLAG_ONLY_MAP[i], ADDRESS_I2C_PORT_MAP[i] + 31, 1);
      this.lastI2cPortModes[i] = 0;
    } 
  }
  
  public void deregisterForPortReadyCallback(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.i2cPortReadyCallback[paramInt] = null;
  }
  
  protected void doArm() throws RobotCoreException, InterruptedException {
    super.doArm();
    createSegments();
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    super.doPretend();
    createSegments();
  }
  
  public void enableI2cReadMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfBufferIsTooLarge(paramInt3);
    try {
      this.i2cPortSegments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.i2cPortSegments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = Byte.MIN_VALUE;
      arrayOfByte2[paramInt1] = Byte.MIN_VALUE;
      arrayOfByte1[1] = (byte)paramI2cAddr.get8Bit();
      arrayOfByte1[2] = (byte)paramInt2;
      arrayOfByte1[3] = (byte)paramInt3;
      return;
    } finally {
      this.i2cPortSegments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public void enableI2cWriteMode(int paramInt1, I2cAddr paramI2cAddr, int paramInt2, int paramInt3) {
    throwIfI2cPortIsInvalid(paramInt1);
    throwIfBufferIsTooLarge(paramInt3);
    try {
      this.i2cPortSegments[paramInt1].getWriteLock().lock();
      byte[] arrayOfByte1 = this.i2cPortSegments[paramInt1].getWriteBuffer();
      byte[] arrayOfByte2 = this.lastI2cPortModes;
      arrayOfByte1[0] = 0;
      arrayOfByte2[paramInt1] = 0;
      arrayOfByte1[1] = (byte)paramI2cAddr.get8Bit();
      arrayOfByte1[2] = (byte)paramInt2;
      arrayOfByte1[3] = (byte)paramInt3;
      return;
    } finally {
      this.i2cPortSegments[paramInt1].getWriteLock().unlock();
    } 
  }
  
  public double getAnalogInputVoltage(int paramInt) {
    throwIfAnalogPortIsInvalid(paramInt);
    return Range.scale((TypeConversion.byteArrayToShort(read(ADDRESS_ANALOG_PORT_MAP[paramInt], 2), ByteOrder.LITTLE_ENDIAN) & 0x3FF), 0.0D, 1023.0D, 0.0D, getMaxAnalogInputVoltage());
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
      this.i2cPortSegments[paramInt].getReadLock().lock();
      byte[] arrayOfByte1 = this.i2cPortSegments[paramInt].getReadBuffer();
      byte b = arrayOfByte1[3];
      byte[] arrayOfByte2 = new byte[b];
      System.arraycopy(arrayOfByte1, 4, arrayOfByte2, 0, b);
      return arrayOfByte2;
    } finally {
      this.i2cPortSegments[paramInt].getReadLock().unlock();
    } 
  }
  
  public byte[] getCopyOfWriteBuffer(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.i2cPortSegments[paramInt].getWriteLock().lock();
      byte[] arrayOfByte1 = this.i2cPortSegments[paramInt].getWriteBuffer();
      byte b = arrayOfByte1[3];
      byte[] arrayOfByte2 = new byte[b];
      System.arraycopy(arrayOfByte1, 4, arrayOfByte2, 0, b);
      return arrayOfByte2;
    } finally {
      this.i2cPortSegments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public String getDeviceName() {
    return String.format("%s %s", new Object[] { this.context.getString(R.string.moduleDisplayNameCDIM), this.robotUsbDevice.getFirmwareVersion() });
  }
  
  public DigitalChannel.Mode getDigitalChannelMode(int paramInt) {
    return byteToRunMode(paramInt, getDigitalIOControlByte());
  }
  
  public boolean getDigitalChannelState(int paramInt) {
    int i;
    if (DigitalChannel.Mode.OUTPUT == getDigitalChannelMode(paramInt)) {
      i = getDigitalOutputStateByte();
    } else {
      i = getDigitalInputStateByte();
    } 
    return ((ADDRESS_DIGITAL_BIT_MASK[paramInt] & i) > 0);
  }
  
  public byte getDigitalIOControlByte() {
    return read8(21);
  }
  
  public int getDigitalInputStateByte() {
    return TypeConversion.unsignedByteToInt(read8(20));
  }
  
  public byte getDigitalOutputStateByte() {
    return read8(22);
  }
  
  public I2cController.I2cPortReadyCallback getI2cPortReadyCallback(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    return this.i2cPortReadyCallback[paramInt];
  }
  
  public byte[] getI2cReadCache(int paramInt) {
    return this.i2cPortSegments[paramInt].getReadBuffer();
  }
  
  public Lock getI2cReadCacheLock(int paramInt) {
    return this.i2cPortSegments[paramInt].getReadLock();
  }
  
  public TimeWindow getI2cReadCacheTimeWindow(int paramInt) {
    return this.i2cPortSegments[paramInt].getTimeWindow();
  }
  
  public byte[] getI2cWriteCache(int paramInt) {
    return this.i2cPortSegments[paramInt].getWriteBuffer();
  }
  
  public Lock getI2cWriteCacheLock(int paramInt) {
    return this.i2cPortSegments[paramInt].getWriteLock();
  }
  
  public boolean getLEDState(int paramInt) {
    throwIfLEDChannelIsInvalid(paramInt);
    byte b = read8(23);
    return ((LED_BIT_MASK_MAP[paramInt] & b) > 0);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public double getMaxAnalogInputVoltage() {
    return 5.0D;
  }
  
  public int getMaxI2cWriteLatency(int paramInt) {
    return 20;
  }
  
  public int getPulseWidthOutputTime(int paramInt) {
    throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
  }
  
  public int getPulseWidthPeriod(int paramInt) {
    throw new UnsupportedOperationException("getPulseWidthOutputTime is not implemented.");
  }
  
  protected String getTag() {
    return "MRDeviceInterfaceModule";
  }
  
  public void initializeHardware() {}
  
  public boolean isI2cPortActionFlagSet(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      boolean bool;
      this.i2cPortSegments[paramInt].getReadLock().lock();
      byte b = this.i2cPortSegments[paramInt].getReadBuffer()[31];
      if (b == -1) {
        bool = true;
      } else {
        bool = false;
      } 
      return bool;
    } finally {
      this.i2cPortSegments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortInReadMode(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      byte b;
      this.i2cPortSegments[paramInt].getReadLock().lock();
      byte[] arrayOfByte = this.i2cPortSegments[paramInt].getReadBuffer();
      boolean bool1 = isArmed();
      boolean bool = false;
      if (bool1) {
        b = arrayOfByte[0];
      } else {
        b = this.lastI2cPortModes[paramInt];
      } 
      if (b == Byte.MIN_VALUE)
        bool = true; 
      return bool;
    } finally {
      this.i2cPortSegments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortInWriteMode(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      byte b;
      this.i2cPortSegments[paramInt].getReadLock().lock();
      byte[] arrayOfByte = this.i2cPortSegments[paramInt].getReadBuffer();
      boolean bool1 = isArmed();
      boolean bool = false;
      if (bool1) {
        b = arrayOfByte[0];
      } else {
        b = this.lastI2cPortModes[paramInt];
      } 
      if (b == 0)
        bool = true; 
      return bool;
    } finally {
      this.i2cPortSegments[paramInt].getReadLock().unlock();
    } 
  }
  
  public boolean isI2cPortReady(int paramInt) {
    return isI2cPortReady(paramInt, read8(3));
  }
  
  public void readComplete() throws InterruptedException {
    if (this.i2cPortReadyCallback == null)
      return; 
    byte b = read8(3);
    for (int i = 0; i < 6; i++) {
      if (this.i2cPortReadyCallback[i] != null && isI2cPortReady(i, b))
        this.i2cPortReadyCallback[i].portIsReady(i); 
    } 
  }
  
  public void readI2cCacheFromController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.readWriteRunnable.queueSegmentRead(SEGMENT_KEY_I2C_PORT_MAP[paramInt]);
  }
  
  @Deprecated
  public void readI2cCacheFromModule(int paramInt) {
    readI2cCacheFromController(paramInt);
  }
  
  public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback paramI2cPortReadyCallback, int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.i2cPortReadyCallback[paramInt] = paramI2cPortReadyCallback;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setAnalogOutputFrequency(int paramInt1, int paramInt2) {
    throwIfAnalogOutputPortIsInvalid(paramInt1);
    Lock lock = this.analogVoltagePortSegments[paramInt1].getWriteLock();
    null = this.analogVoltagePortSegments[paramInt1].getWriteBuffer();
    byte[] arrayOfByte = TypeConversion.shortToByteArray((short)paramInt2, ByteOrder.LITTLE_ENDIAN);
    try {
      lock.lock();
      System.arraycopy(arrayOfByte, 0, null, 2, arrayOfByte.length);
      lock.unlock();
      return;
    } finally {
      lock.unlock();
    } 
  }
  
  public void setAnalogOutputMode(int paramInt, byte paramByte) {
    throwIfAnalogOutputPortIsInvalid(paramInt);
    Lock lock = this.analogVoltagePortSegments[paramInt].getWriteLock();
    null = this.analogVoltagePortSegments[paramInt].getWriteBuffer();
    try {
      lock.lock();
      null[4] = paramByte;
      lock.unlock();
      return;
    } finally {
      lock.unlock();
    } 
  }
  
  public void setAnalogOutputVoltage(int paramInt1, int paramInt2) {
    throwIfAnalogOutputPortIsInvalid(paramInt1);
    Lock lock = this.analogVoltagePortSegments[paramInt1].getWriteLock();
    null = this.analogVoltagePortSegments[paramInt1].getWriteBuffer();
    byte[] arrayOfByte = TypeConversion.shortToByteArray((short)paramInt2, ByteOrder.LITTLE_ENDIAN);
    try {
      lock.lock();
      System.arraycopy(arrayOfByte, 0, null, 0, arrayOfByte.length);
      lock.unlock();
      return;
    } finally {
      lock.unlock();
    } 
  }
  
  public void setDigitalChannelMode(int paramInt, DigitalChannel.Mode paramMode) {
    paramInt = runModeToBitMask(paramInt, paramMode);
    byte b = readFromWriteCache(21);
    if (paramMode == DigitalChannel.Mode.OUTPUT) {
      paramInt |= b;
    } else {
      paramInt &= b;
    } 
    write8(21, paramInt);
  }
  
  @Deprecated
  public void setDigitalChannelMode(int paramInt, DigitalChannelController.Mode paramMode) {
    setDigitalChannelMode(paramInt, paramMode.migrate());
  }
  
  public void setDigitalChannelState(int paramInt, boolean paramBoolean) {
    if (DigitalChannel.Mode.OUTPUT == getDigitalChannelMode(paramInt)) {
      byte b = readFromWriteCache(22);
      if (paramBoolean) {
        paramInt = ADDRESS_DIGITAL_BIT_MASK[paramInt] | b;
      } else {
        paramInt = ADDRESS_DIGITAL_BIT_MASK[paramInt] & b;
      } 
      setDigitalOutputByte((byte)paramInt);
    } 
  }
  
  public void setDigitalIOControlByte(byte paramByte) {
    write8(21, paramByte);
  }
  
  public void setDigitalOutputByte(byte paramByte) {
    write8(22, paramByte);
  }
  
  public void setI2cPortActionFlag(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    try {
      this.i2cPortSegments[paramInt].getWriteLock().lock();
      this.i2cPortSegments[paramInt].getWriteBuffer()[31] = -1;
      return;
    } finally {
      this.i2cPortSegments[paramInt].getWriteLock().unlock();
    } 
  }
  
  public void setLED(int paramInt, boolean paramBoolean) {
    throwIfLEDChannelIsInvalid(paramInt);
    byte b = readFromWriteCache(23);
    if (paramBoolean) {
      paramInt = LED_BIT_MASK_MAP[paramInt] | b;
    } else {
      paramInt = LED_BIT_MASK_MAP[paramInt] & b;
    } 
    write8(23, paramInt);
  }
  
  public void setPulseWidthOutputTime(int paramInt1, int paramInt2) {
    throwIfPulseWidthPortIsInvalid(paramInt1);
    Lock lock = this.pulseOutputPortSegments[paramInt1].getWriteLock();
    null = this.pulseOutputPortSegments[paramInt1].getWriteBuffer();
    byte[] arrayOfByte = TypeConversion.shortToByteArray((short)paramInt2, ByteOrder.LITTLE_ENDIAN);
    try {
      lock.lock();
      System.arraycopy(arrayOfByte, 0, null, 0, arrayOfByte.length);
      lock.unlock();
      return;
    } finally {
      lock.unlock();
    } 
  }
  
  public void setPulseWidthPeriod(int paramInt1, int paramInt2) {
    throwIfI2cPortIsInvalid(paramInt1);
    Lock lock = this.pulseOutputPortSegments[paramInt1].getWriteLock();
    null = this.pulseOutputPortSegments[paramInt1].getWriteBuffer();
    byte[] arrayOfByte = TypeConversion.shortToByteArray((short)paramInt2, ByteOrder.LITTLE_ENDIAN);
    try {
      lock.lock();
      System.arraycopy(arrayOfByte, 0, null, 2, arrayOfByte.length);
      lock.unlock();
      return;
    } finally {
      lock.unlock();
    } 
  }
  
  public void writeI2cCacheToController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    this.readWriteRunnable.queueSegmentWrite(SEGMENT_KEY_I2C_PORT_MAP[paramInt]);
  }
  
  @Deprecated
  public void writeI2cCacheToModule(int paramInt) {
    writeI2cCacheToController(paramInt);
  }
  
  public void writeI2cPortFlagOnlyToController(int paramInt) {
    throwIfI2cPortIsInvalid(paramInt);
    ReadWriteRunnableSegment readWriteRunnableSegment1 = this.i2cPortSegments[paramInt];
    ReadWriteRunnableSegment readWriteRunnableSegment2 = this.i2cPortFlagOnlySegments[paramInt];
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


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDeviceInterfaceModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */