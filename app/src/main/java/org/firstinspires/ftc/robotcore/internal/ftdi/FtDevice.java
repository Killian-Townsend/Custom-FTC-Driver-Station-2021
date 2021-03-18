package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.ftdi.eeprom.FT_EEPROM;
import org.firstinspires.ftc.robotcore.internal.ftdi.eeprom.FT_EE_Ctrl;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbDeviceClosedException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbUnspecifiedException;

public class FtDevice extends FtConstants {
  public static final int RC_BITMODE_UNAVAILABLE = -4;
  
  public static final int RC_DEVICE_CLOSED = -1;
  
  public static final int RC_ILLEGAL_ARGUMENT = -2;
  
  public static final int RC_ILLEGAL_STATE = -3;
  
  public static final int RC_PARANOIA = -1000;
  
  public static final String TAG = "FtDevice";
  
  UsbEndpoint mBulkInEndpoint;
  
  UsbEndpoint mBulkOutEndpoint;
  
  private ThreadHelper mBulkPacketInThread;
  
  private BulkPacketInWorker mBulkPacketInWorker;
  
  private Context mContext;
  
  private boolean mDebugRetainBuffers;
  
  private RobotUsbException mDeviceClosedReason;
  
  FtDeviceInfo mDeviceInfo;
  
  private FT_EE_Ctrl mEEPROM;
  
  private int mEndpointMaxPacketSize;
  
  long mEventMask;
  
  private final int mInterfaceID;
  
  private boolean mIsOpen;
  
  private byte mLatencyTimer;
  
  private FtDeviceManagerParams mParams;
  
  private ReadBufferManager mReadBufferManager;
  
  private ThreadHelper mReadBufferManagerThread;
  
  private MonitoredUsbDeviceConnection mUsbConnection;
  
  private final UsbDevice mUsbDevice;
  
  private final UsbInterface mUsbInterface;
  
  private final Object openCloseLock = new Object();
  
  public FtDevice(Context paramContext, UsbManager paramUsbManager, UsbDevice paramUsbDevice, UsbInterface paramUsbInterface) throws FtDeviceIOException, RobotUsbException {
    this.mContext = paramContext;
    this.mUsbDevice = paramUsbDevice;
    this.mUsbInterface = paramUsbInterface;
    this.mInterfaceID = paramUsbInterface.getId() + 1;
    initialize(paramUsbManager);
  }
  
  private void dualQuadChannelDevice() {
    int i = this.mInterfaceID;
    if (i == 1) {
      FtDeviceInfo ftDeviceInfo = this.mDeviceInfo;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.serialNumber);
      stringBuilder.append("A");
      ftDeviceInfo.serialNumber = stringBuilder.toString();
      ftDeviceInfo = this.mDeviceInfo;
      stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.productName);
      stringBuilder.append(" A");
      ftDeviceInfo.productName = stringBuilder.toString();
      return;
    } 
    if (i == 2) {
      FtDeviceInfo ftDeviceInfo = this.mDeviceInfo;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.serialNumber);
      stringBuilder.append("B");
      ftDeviceInfo.serialNumber = stringBuilder.toString();
      ftDeviceInfo = this.mDeviceInfo;
      stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.productName);
      stringBuilder.append(" B");
      ftDeviceInfo.productName = stringBuilder.toString();
      return;
    } 
    if (i == 3) {
      FtDeviceInfo ftDeviceInfo = this.mDeviceInfo;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.serialNumber);
      stringBuilder.append("C");
      ftDeviceInfo.serialNumber = stringBuilder.toString();
      ftDeviceInfo = this.mDeviceInfo;
      stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.productName);
      stringBuilder.append(" C");
      ftDeviceInfo.productName = stringBuilder.toString();
      return;
    } 
    if (i == 4) {
      FtDeviceInfo ftDeviceInfo = this.mDeviceInfo;
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.serialNumber);
      stringBuilder.append("D");
      ftDeviceInfo.serialNumber = stringBuilder.toString();
      ftDeviceInfo = this.mDeviceInfo;
      stringBuilder = new StringBuilder();
      stringBuilder.append(this.mDeviceInfo.productName);
      stringBuilder.append(" D");
      ftDeviceInfo.productName = stringBuilder.toString();
    } 
  }
  
  private boolean findDeviceEndpoints() {
    this.mBulkInEndpoint = null;
    this.mBulkOutEndpoint = null;
    boolean bool2 = false;
    for (int i = 0; i < this.mUsbInterface.getEndpointCount(); i++) {
      if (this.mUsbInterface.getEndpoint(i).getType() == 2)
        if (this.mUsbInterface.getEndpoint(i).getDirection() == 128) {
          UsbEndpoint usbEndpoint = this.mUsbInterface.getEndpoint(i);
          this.mBulkInEndpoint = usbEndpoint;
          this.mEndpointMaxPacketSize = usbEndpoint.getMaxPacketSize();
        } else if (this.mUsbInterface.getEndpoint(i).getDirection() == 0) {
          this.mBulkOutEndpoint = this.mUsbInterface.getEndpoint(i);
        }  
    } 
    boolean bool1 = bool2;
    if (this.mBulkOutEndpoint != null) {
      bool1 = bool2;
      if (this.mBulkInEndpoint != null)
        bool1 = true; 
    } 
    return bool1;
  }
  
  private boolean ifFt8u232am() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 512 && this.mDeviceInfo.iSerialNumber != 0);
  }
  
  private boolean isBitModeDevice() {
    return (isFt232b() || isFt2232() || isFt232r() || isFt2232h() || isFt4232h() || isFt232h() || isFt232ex());
  }
  
  private boolean isFt2232() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 1280);
  }
  
  private boolean isFt2232h() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 1792);
  }
  
  private boolean isFt232b() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 1024 || ((this.mDeviceInfo.bcdDevice & 0xFF00) == 512 && this.mDeviceInfo.iSerialNumber == 0));
  }
  
  private boolean isFt232ex() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 4096);
  }
  
  private boolean isFt232h() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 2304);
  }
  
  private boolean isFt232r() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 1536);
  }
  
  private boolean isHiSpeed() {
    return (isFt232h() || isFt2232h() || isFt4232h());
  }
  
  public static boolean isOpen(FtDevice paramFtDevice) {
    return (paramFtDevice != null && paramFtDevice.isOpen());
  }
  
  private boolean purgeRxTx(boolean paramBoolean1, boolean paramBoolean2) throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (paramBoolean1) {
      int i = 0;
      int j = i;
      while (i < 6) {
        j = vendorCmdSet(0, 1);
        i++;
      } 
      if (j > 0)
        return false; 
      this.mReadBufferManager.purgeInputData();
    } 
    paramBoolean1 = bool;
    if (paramBoolean2) {
      paramBoolean1 = bool;
      if (vendorCmdSet(0, 2) == 0)
        paramBoolean1 = true; 
    } 
    return paramBoolean1;
  }
  
  private void setBreak(int paramInt) throws RobotUsbException {
    int i = this.mDeviceInfo.breakOnParam;
    if (isOpen()) {
      throwIfStatus(getConnection().controlTransfer(64, 4, i | paramInt, this.mInterfaceID, (byte[])null, 0, 0), "setBreak");
      return;
    } 
    throw new RobotUsbDeviceClosedException("setBreak");
  }
  
  private void setClosed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield mIsOpen : Z
    //   7: aload_0
    //   8: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   11: astore_1
    //   12: aload_1
    //   13: aload_1
    //   14: getfield flags : I
    //   17: bipush #-2
    //   19: iand
    //   20: putfield flags : I
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  private void setOpen() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield mIsOpen : Z
    //   7: aload_0
    //   8: aconst_null
    //   9: putfield mDeviceClosedReason : Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException;
    //   12: aload_0
    //   13: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   16: astore_1
    //   17: aload_1
    //   18: iconst_1
    //   19: aload_1
    //   20: getfield flags : I
    //   23: ior
    //   24: putfield flags : I
    //   27: aload_0
    //   28: monitorexit
    //   29: return
    //   30: astore_1
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	30	finally
  }
  
  private String stringFromUtf16le(byte[] paramArrayOfbyte) throws UnsupportedEncodingException {
    return new String(paramArrayOfbyte, 2, paramArrayOfbyte[0] - 2, "UTF-16LE");
  }
  
  public static void throwIfStatus(int paramInt, String paramString) throws RobotUsbException {
    if (paramInt == 0)
      return; 
    throw new RobotUsbUnspecifiedException("%s: status=%d", new Object[] { paramString, Integer.valueOf(paramInt) });
  }
  
  public void close() {
    synchronized (this.openCloseLock) {
      boolean bool = isOpen();
      if (bool)
        RobotLog.vv("FtDevice", "vv********************%s closing********************vv 0x%08x", new Object[] { getSerialNumber(), Integer.valueOf(hashCode()) }); 
      setClosed();
      if (this.mReadBufferManagerThread != null) {
        this.mReadBufferManagerThread.stop();
        this.mReadBufferManagerThread = null;
      } 
      if (this.mBulkPacketInThread != null) {
        this.mBulkPacketInThread.stop();
        this.mBulkPacketInThread = null;
      } 
      if (this.mReadBufferManager != null) {
        this.mReadBufferManager.close();
        this.mReadBufferManager = null;
      } 
      if (this.mUsbConnection != null) {
        this.mUsbConnection.releaseInterface(this.mUsbInterface);
        this.mUsbConnection.close();
        this.mUsbConnection = null;
      } 
      this.mBulkPacketInWorker = null;
      if (bool)
        RobotLog.vv("FtDevice", "^^********************%s closed ********************^^", new Object[] { getSerialNumber() }); 
      return;
    } 
  }
  
  public boolean clrDtr() throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (vendorCmdSet(1, 256) == 0)
      bool = true; 
    return bool;
  }
  
  public boolean clrRts() throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (vendorCmdSet(1, 512) == 0)
      bool = true; 
    return bool;
  }
  
  public boolean eepromErase() throws RobotUsbException {
    return (isOpen() && this.mEEPROM.eraseEeprom() == 0);
  }
  
  public int eepromGetUserAreaSize() throws RobotUsbException {
    return !isOpen() ? -1 : this.mEEPROM.getUserSize();
  }
  
  public FT_EEPROM eepromRead() {
    return !isOpen() ? null : this.mEEPROM.readEeprom();
  }
  
  public byte[] eepromReadUserArea(int paramInt) throws RobotUsbException {
    return !isOpen() ? null : this.mEEPROM.readUserData(paramInt);
  }
  
  public int eepromReadWord(short paramShort) throws RobotUsbException {
    return !isOpen() ? -1 : this.mEEPROM.readWord(paramShort);
  }
  
  public short eepromWrite(FT_EEPROM paramFT_EEPROM) throws RobotUsbException {
    return !isOpen() ? -1 : this.mEEPROM.programEeprom(paramFT_EEPROM);
  }
  
  public int eepromWriteUserArea(byte[] paramArrayOfbyte) throws RobotUsbException {
    return !isOpen() ? 0 : this.mEEPROM.writeUserData(paramArrayOfbyte);
  }
  
  public void eepromWriteWord(short paramShort1, short paramShort2) throws RobotUsbException {
    if (isOpen()) {
      this.mEEPROM.writeWord(paramShort1, paramShort2);
      return;
    } 
    throw new RobotUsbDeviceClosedException("eepromWriteWord");
  }
  
  public void flushBuffers() {
    this.mBulkPacketInWorker.awaitTrivialInput(20L, TimeUnit.MILLISECONDS);
    this.mReadBufferManager.purgeInputData();
  }
  
  public byte getBitMode() throws RobotUsbException {
    byte[] arrayOfByte = new byte[1];
    return !isOpen() ? -1 : (!isBitModeDevice() ? -3 : ((getConnection().controlTransfer(192, 12, 0, this.mInterfaceID, arrayOfByte, 1, 0) == 1) ? arrayOfByte[0] : -4));
  }
  
  public MonitoredUsbDeviceConnection getConnection() {
    return this.mUsbConnection;
  }
  
  public boolean getDebugRetainBuffers() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mDebugRetainBuffers : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  protected int getDescriptor(int paramInt1, int paramInt2, byte[] paramArrayOfbyte) throws RobotUsbException {
    return getConnection().controlTransfer(128, UsbStandardRequest.GET_DESCRIPTOR.value, paramInt1 << 8 | paramInt2, 0, paramArrayOfbyte, paramArrayOfbyte.length, 0);
  }
  
  public RobotUsbException getDeviceClosedReason() {
    return this.mDeviceClosedReason;
  }
  
  public FtDeviceInfo getDeviceInfo() {
    return this.mDeviceInfo;
  }
  
  FtDeviceManagerParams getDriverParameters() {
    return this.mParams;
  }
  
  int getEndpointMaxPacketSize() {
    return this.mEndpointMaxPacketSize;
  }
  
  public long getEventStatus() {
    if (!isOpen())
      return -1L; 
    long l = this.mEventMask;
    this.mEventMask = 0L;
    return l;
  }
  
  public byte getLatencyTimer() throws RobotUsbException {
    byte[] arrayOfByte = new byte[1];
    if (!isOpen())
      return -1; 
    int i = getConnection().controlTransfer(192, 10, 0, this.mInterfaceID, arrayOfByte, 1, 0);
    byte b = 0;
    if (i == 1)
      b = arrayOfByte[0]; 
    return b;
  }
  
  public short getLineStatus() {
    return !isOpen() ? -1 : this.mDeviceInfo.lineStatus;
  }
  
  public short getModemStatus() {
    if (!isOpen())
      return -1; 
    this.mEventMask &= 0xFFFFFFFFFFFFFFFDL;
    return (short)(this.mDeviceInfo.modemStatus & 0xFF);
  }
  
  public int getReadBufferSize() {
    return !isOpen() ? -1 : this.mReadBufferManager.getReadBufferSize();
  }
  
  public int getReadTimeout() {
    return this.mParams.getBulkInReadTimeout();
  }
  
  public SerialNumber getSerialNumber() {
    return SerialNumber.fromString((getDeviceInfo()).serialNumber);
  }
  
  protected String getStringDescriptor(int paramInt) throws RobotUsbException {
    byte[] arrayOfByte = new byte[255];
    return (getDescriptor(UsbDescriptorType.STRING.value, paramInt, arrayOfByte) < 0) ? "<unknown string>" : (new UsbStringDescriptor(arrayOfByte)).string;
  }
  
  public UsbDevice getUsbDevice() {
    return this.mUsbDevice;
  }
  
  protected void initialize(UsbManager paramUsbManager) throws FtDeviceIOException, RobotUsbException {
    // Byte code:
    //   0: new org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #4
    //   9: aload_0
    //   10: aload #4
    //   12: putfield mParams : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManagerParams;
    //   15: aload_0
    //   16: aload #4
    //   18: invokevirtual isDebugRetainBuffers : ()Z
    //   21: putfield mDebugRetainBuffers : Z
    //   24: aload_0
    //   25: aconst_null
    //   26: putfield mBulkOutEndpoint : Landroid/hardware/usb/UsbEndpoint;
    //   29: aload_0
    //   30: aconst_null
    //   31: putfield mBulkInEndpoint : Landroid/hardware/usb/UsbEndpoint;
    //   34: iconst_0
    //   35: istore_2
    //   36: aload_0
    //   37: iconst_0
    //   38: putfield mEndpointMaxPacketSize : I
    //   41: aload_0
    //   42: new org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo
    //   45: dup
    //   46: invokespecial <init> : ()V
    //   49: putfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   52: aload_0
    //   53: aload_1
    //   54: aload_0
    //   55: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   58: invokevirtual openDevice : (Landroid/hardware/usb/UsbDevice;)Landroid/hardware/usb/UsbDeviceConnection;
    //   61: invokevirtual setConnection : (Landroid/hardware/usb/UsbDeviceConnection;)V
    //   64: aload_0
    //   65: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   68: ifnull -> 1011
    //   71: aload_0
    //   72: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   75: aload_0
    //   76: getfield mUsbInterface : Landroid/hardware/usb/UsbInterface;
    //   79: iconst_0
    //   80: invokevirtual claimInterface : (Landroid/hardware/usb/UsbInterface;Z)Z
    //   83: pop
    //   84: new org/firstinspires/ftc/robotcore/internal/ftdi/UsbDeviceDescriptor
    //   87: dup
    //   88: aload_0
    //   89: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   92: invokevirtual getRawDescriptors : ()[B
    //   95: invokespecial <init> : ([B)V
    //   98: astore_1
    //   99: aload_0
    //   100: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   103: aload_1
    //   104: getfield bcdDevice : I
    //   107: i2s
    //   108: putfield bcdDevice : S
    //   111: aload_0
    //   112: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   115: aload_1
    //   116: getfield idVendor : I
    //   119: bipush #16
    //   121: ishl
    //   122: aload_1
    //   123: getfield idProduct : I
    //   126: ior
    //   127: putfield id : I
    //   130: aload_0
    //   131: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   134: aload_1
    //   135: getfield iSerialNumber : I
    //   138: i2b
    //   139: putfield iSerialNumber : B
    //   142: aload_0
    //   143: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   146: aload_0
    //   147: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   150: invokevirtual getSerialNumber : ()Ljava/lang/String;
    //   153: putfield serialNumber : Ljava/lang/String;
    //   156: aload_0
    //   157: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   160: aload_0
    //   161: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   164: invokevirtual getManufacturerName : ()Ljava/lang/String;
    //   167: putfield manufacturerName : Ljava/lang/String;
    //   170: aload_0
    //   171: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   174: aload_0
    //   175: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   178: invokevirtual getProductName : ()Ljava/lang/String;
    //   181: putfield productName : Ljava/lang/String;
    //   184: aload_0
    //   185: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   188: aload_0
    //   189: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   192: invokevirtual getDeviceId : ()I
    //   195: iconst_4
    //   196: ishl
    //   197: aload_0
    //   198: getfield mInterfaceID : I
    //   201: bipush #15
    //   203: iand
    //   204: ior
    //   205: putfield location : I
    //   208: aload_0
    //   209: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   212: bipush #8
    //   214: putfield breakOnParam : I
    //   217: ldc 'FtDevice'
    //   219: ldc_w 'initialize(%s bcdDevice=0x%04x)'
    //   222: iconst_2
    //   223: anewarray java/lang/Object
    //   226: dup
    //   227: iconst_0
    //   228: aload_0
    //   229: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   232: invokestatic from : (Landroid/hardware/usb/UsbDevice;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/VendorAndProductIds;
    //   235: aastore
    //   236: dup
    //   237: iconst_1
    //   238: aload_0
    //   239: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   242: getfield bcdDevice : S
    //   245: invokestatic valueOf : (S)Ljava/lang/Short;
    //   248: aastore
    //   249: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   252: aload_0
    //   253: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   256: getfield bcdDevice : S
    //   259: ldc 65280
    //   261: iand
    //   262: lookupswitch default -> 1035, 512 -> 734, 1024 -> 711, 1280 -> 684, 1536 -> 606, 1792 -> 578, 2048 -> 550, 2304 -> 526, 4096 -> 502, 5888 -> 482, 6144 -> 443, 6400 -> 368
    //   360: aload_0
    //   361: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   364: astore_1
    //   365: goto -> 790
    //   368: aload_0
    //   369: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   372: bipush #11
    //   374: putfield type : I
    //   377: aload_0
    //   378: getfield mInterfaceID : I
    //   381: iconst_4
    //   382: if_icmpne -> 432
    //   385: aload_0
    //   386: getfield mUsbDevice : Landroid/hardware/usb/UsbDevice;
    //   389: aload_0
    //   390: getfield mInterfaceID : I
    //   393: iconst_1
    //   394: isub
    //   395: invokevirtual getInterface : (I)Landroid/hardware/usb/UsbInterface;
    //   398: iconst_0
    //   399: invokevirtual getEndpoint : (I)Landroid/hardware/usb/UsbEndpoint;
    //   402: invokevirtual getMaxPacketSize : ()I
    //   405: bipush #8
    //   407: if_icmpne -> 421
    //   410: aload_0
    //   411: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   414: iconst_0
    //   415: putfield flags : I
    //   418: goto -> 807
    //   421: aload_0
    //   422: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   425: iconst_2
    //   426: putfield flags : I
    //   429: goto -> 807
    //   432: aload_0
    //   433: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   436: iconst_2
    //   437: putfield flags : I
    //   440: goto -> 807
    //   443: aload_0
    //   444: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   447: bipush #10
    //   449: putfield type : I
    //   452: aload_0
    //   453: getfield mInterfaceID : I
    //   456: iconst_1
    //   457: if_icmpne -> 471
    //   460: aload_0
    //   461: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   464: iconst_2
    //   465: putfield flags : I
    //   468: goto -> 807
    //   471: aload_0
    //   472: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   475: iconst_0
    //   476: putfield flags : I
    //   479: goto -> 807
    //   482: aload_0
    //   483: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   486: bipush #12
    //   488: putfield type : I
    //   491: aload_0
    //   492: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   495: iconst_2
    //   496: putfield flags : I
    //   499: goto -> 807
    //   502: aload_0
    //   503: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   506: bipush #9
    //   508: putfield type : I
    //   511: aload_0
    //   512: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_X_Ctrl
    //   515: dup
    //   516: aload_0
    //   517: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   520: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   523: goto -> 807
    //   526: aload_0
    //   527: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   530: bipush #8
    //   532: putfield type : I
    //   535: aload_0
    //   536: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_232H_Ctrl
    //   539: dup
    //   540: aload_0
    //   541: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   544: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   547: goto -> 807
    //   550: aload_0
    //   551: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   554: bipush #7
    //   556: putfield type : I
    //   559: aload_0
    //   560: invokespecial dualQuadChannelDevice : ()V
    //   563: aload_0
    //   564: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_4232H_Ctrl
    //   567: dup
    //   568: aload_0
    //   569: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   572: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   575: goto -> 807
    //   578: aload_0
    //   579: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   582: bipush #6
    //   584: putfield type : I
    //   587: aload_0
    //   588: invokespecial dualQuadChannelDevice : ()V
    //   591: aload_0
    //   592: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_2232H_Ctrl
    //   595: dup
    //   596: aload_0
    //   597: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   600: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   603: goto -> 807
    //   606: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl
    //   609: dup
    //   610: aload_0
    //   611: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   614: astore_1
    //   615: aload_0
    //   616: aload_1
    //   617: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   620: aload_1
    //   621: iconst_0
    //   622: invokevirtual readWord : (S)I
    //   625: iconst_1
    //   626: iand
    //   627: i2s
    //   628: istore_3
    //   629: aload_0
    //   630: aconst_null
    //   631: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   634: iload_3
    //   635: ifne -> 661
    //   638: aload_0
    //   639: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   642: iconst_5
    //   643: putfield type : I
    //   646: aload_0
    //   647: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_232R_Ctrl
    //   650: dup
    //   651: aload_0
    //   652: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   655: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   658: goto -> 807
    //   661: aload_0
    //   662: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   665: iconst_5
    //   666: putfield type : I
    //   669: aload_0
    //   670: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_245R_Ctrl
    //   673: dup
    //   674: aload_0
    //   675: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   678: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   681: goto -> 807
    //   684: aload_0
    //   685: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_2232_Ctrl
    //   688: dup
    //   689: aload_0
    //   690: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   693: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   696: aload_0
    //   697: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   700: iconst_4
    //   701: putfield type : I
    //   704: aload_0
    //   705: invokespecial dualQuadChannelDevice : ()V
    //   708: goto -> 807
    //   711: aload_0
    //   712: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_232B_Ctrl
    //   715: dup
    //   716: aload_0
    //   717: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   720: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   723: aload_0
    //   724: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   727: iconst_0
    //   728: putfield type : I
    //   731: goto -> 807
    //   734: aload_0
    //   735: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   738: getfield iSerialNumber : B
    //   741: ifne -> 767
    //   744: aload_0
    //   745: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_232B_Ctrl
    //   748: dup
    //   749: aload_0
    //   750: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   753: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   756: aload_0
    //   757: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   760: iconst_0
    //   761: putfield type : I
    //   764: goto -> 807
    //   767: aload_0
    //   768: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   771: iconst_1
    //   772: putfield type : I
    //   775: aload_0
    //   776: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_232A_Ctrl
    //   779: dup
    //   780: aload_0
    //   781: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   784: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   787: goto -> 807
    //   790: aload_1
    //   791: iconst_3
    //   792: putfield type : I
    //   795: aload_0
    //   796: new org/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl
    //   799: dup
    //   800: aload_0
    //   801: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;)V
    //   804: putfield mEEPROM : Lorg/firstinspires/ftc/robotcore/internal/ftdi/eeprom/FT_EE_Ctrl;
    //   807: aload_0
    //   808: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   811: getfield bcdDevice : S
    //   814: ldc 65280
    //   816: iand
    //   817: istore_3
    //   818: iload_3
    //   819: sipush #5888
    //   822: if_icmpeq -> 842
    //   825: iload_3
    //   826: sipush #6144
    //   829: if_icmpeq -> 842
    //   832: iload_3
    //   833: sipush #6400
    //   836: if_icmpeq -> 842
    //   839: goto -> 947
    //   842: aload_0
    //   843: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   846: getfield serialNumber : Ljava/lang/String;
    //   849: ifnonnull -> 947
    //   852: bipush #16
    //   854: newarray byte
    //   856: astore #4
    //   858: aload_0
    //   859: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   862: sipush #192
    //   865: sipush #144
    //   868: iconst_0
    //   869: bipush #27
    //   871: aload #4
    //   873: bipush #16
    //   875: iconst_0
    //   876: invokevirtual controlTransfer : (IIII[BII)I
    //   879: pop
    //   880: ldc_w ''
    //   883: astore_1
    //   884: iload_2
    //   885: bipush #8
    //   887: if_icmpge -> 932
    //   890: new java/lang/StringBuilder
    //   893: dup
    //   894: invokespecial <init> : ()V
    //   897: astore #5
    //   899: aload #5
    //   901: aload_1
    //   902: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   905: pop
    //   906: aload #5
    //   908: aload #4
    //   910: iload_2
    //   911: iconst_2
    //   912: imul
    //   913: baload
    //   914: i2c
    //   915: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   918: pop
    //   919: aload #5
    //   921: invokevirtual toString : ()Ljava/lang/String;
    //   924: astore_1
    //   925: iload_2
    //   926: iconst_1
    //   927: iadd
    //   928: istore_2
    //   929: goto -> 884
    //   932: aload_0
    //   933: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   936: new java/lang/String
    //   939: dup
    //   940: aload_1
    //   941: invokespecial <init> : (Ljava/lang/String;)V
    //   944: putfield serialNumber : Ljava/lang/String;
    //   947: aload_0
    //   948: getfield mDeviceInfo : Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   951: getfield bcdDevice : S
    //   954: ldc 65280
    //   956: iand
    //   957: istore_2
    //   958: iload_2
    //   959: sipush #6144
    //   962: if_icmpeq -> 975
    //   965: iload_2
    //   966: sipush #6400
    //   969: if_icmpeq -> 975
    //   972: goto -> 979
    //   975: aload_0
    //   976: invokespecial dualQuadChannelDevice : ()V
    //   979: aload_0
    //   980: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   983: aload_0
    //   984: getfield mUsbInterface : Landroid/hardware/usb/UsbInterface;
    //   987: invokevirtual releaseInterface : (Landroid/hardware/usb/UsbInterface;)Z
    //   990: pop
    //   991: aload_0
    //   992: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   995: invokevirtual close : ()V
    //   998: aload_0
    //   999: aconst_null
    //   1000: checkcast android/hardware/usb/UsbDeviceConnection
    //   1003: invokevirtual setConnection : (Landroid/hardware/usb/UsbDeviceConnection;)V
    //   1006: aload_0
    //   1007: invokespecial setClosed : ()V
    //   1010: return
    //   1011: new org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceIOException
    //   1014: dup
    //   1015: ldc_w 'failed to open device'
    //   1018: invokespecial <init> : (Ljava/lang/String;)V
    //   1021: athrow
    //   1022: astore_1
    //   1023: new org/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceIOException
    //   1026: dup
    //   1027: ldc_w 'exception instantiating FT_Device '
    //   1030: aload_1
    //   1031: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
    //   1034: athrow
    //   1035: goto -> 360
    // Exception table:
    //   from	to	target	type
    //   15	34	1022	java/lang/RuntimeException
    //   36	360	1022	java/lang/RuntimeException
    //   360	365	1022	java/lang/RuntimeException
    //   368	418	1022	java/lang/RuntimeException
    //   421	429	1022	java/lang/RuntimeException
    //   432	440	1022	java/lang/RuntimeException
    //   443	468	1022	java/lang/RuntimeException
    //   471	479	1022	java/lang/RuntimeException
    //   482	499	1022	java/lang/RuntimeException
    //   502	523	1022	java/lang/RuntimeException
    //   526	547	1022	java/lang/RuntimeException
    //   550	575	1022	java/lang/RuntimeException
    //   578	603	1022	java/lang/RuntimeException
    //   606	634	1022	java/lang/RuntimeException
    //   638	658	1022	java/lang/RuntimeException
    //   661	681	1022	java/lang/RuntimeException
    //   684	708	1022	java/lang/RuntimeException
    //   711	731	1022	java/lang/RuntimeException
    //   734	764	1022	java/lang/RuntimeException
    //   767	787	1022	java/lang/RuntimeException
    //   790	807	1022	java/lang/RuntimeException
    //   807	818	1022	java/lang/RuntimeException
    //   842	880	1022	java/lang/RuntimeException
    //   890	925	1022	java/lang/RuntimeException
    //   932	947	1022	java/lang/RuntimeException
    //   947	958	1022	java/lang/RuntimeException
    //   975	979	1022	java/lang/RuntimeException
    //   979	1010	1022	java/lang/RuntimeException
    //   1011	1022	1022	java/lang/RuntimeException
  }
  
  final boolean isFt4232h() {
    return ((this.mDeviceInfo.bcdDevice & 0xFF00) == 2048);
  }
  
  final boolean isMultiInterfaceDevice() {
    return (isFt2232() || isFt2232h() || isFt4232h());
  }
  
  public boolean isOpen() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mIsOpen : Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield openCloseLock : Ljava/lang/Object;
    //   6: astore #8
    //   8: aload #8
    //   10: monitorenter
    //   11: aload_0
    //   12: getfield mReadBufferManager : Lorg/firstinspires/ftc/robotcore/internal/ftdi/ReadBufferManager;
    //   15: ifnull -> 33
    //   18: aload_0
    //   19: getfield mReadBufferManager : Lorg/firstinspires/ftc/robotcore/internal/ftdi/ReadBufferManager;
    //   22: lload_1
    //   23: lload_3
    //   24: aload #5
    //   26: aload #6
    //   28: aload #7
    //   30: invokevirtual logRetainedBuffers : (JJLjava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   33: aload #8
    //   35: monitorexit
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore #5
    //   41: aload #8
    //   43: monitorexit
    //   44: aload #5
    //   46: athrow
    //   47: astore #5
    //   49: aload_0
    //   50: monitorexit
    //   51: aload #5
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	47	finally
    //   11	33	39	finally
    //   33	36	39	finally
    //   41	44	39	finally
    //   44	47	47	finally
  }
  
  public boolean mightBeAtUsbPacketStart() {
    ReadBufferManager readBufferManager = this.mReadBufferManager;
    return (readBufferManager != null) ? readBufferManager.mightBeAtUsbPacketStart() : true;
  }
  
  boolean openDevice(UsbManager paramUsbManager) throws FtDeviceIOException {
    synchronized (this.openCloseLock) {
      boolean bool1 = isOpen();
      boolean bool = false;
      if (!bool1)
        if (paramUsbManager == null) {
          RobotLog.ee("FtDevice", "usbManager cannot be null.");
        } else if (getConnection() != null) {
          RobotLog.ee("FtDevice", "there should not be an existing USB connection");
        } else {
          setConnection(paramUsbManager.openDevice(this.mUsbDevice));
          if (getConnection() == null) {
            RobotLog.ee("FtDevice", "failed to open device");
          } else if (!getConnection().claimInterface(this.mUsbInterface, true)) {
            RobotLog.ee("FtDevice", "claimInterface() returned false.");
          } else if (!findDeviceEndpoints()) {
            RobotLog.ee("FtDevice", "failed to find USB device bulk transfer endpoints");
          } else {
            try {
              RobotLog.vv("FtDevice", "vv********************%s opening********************vv 0x%08x", new Object[] { getSerialNumber(), Integer.valueOf(hashCode()) });
              this.mReadBufferManager = new ReadBufferManager(this, this.mDebugRetainBuffers);
              this.mBulkPacketInWorker = new BulkPacketInWorker(this, this.mReadBufferManager, getConnection(), this.mBulkInEndpoint);
              ThreadHelper threadHelper = new ThreadHelper(this.mBulkPacketInWorker, Thread.currentThread().getPriority() + 1);
              this.mBulkPacketInThread = threadHelper;
              threadHelper.setName("bulkPacketInWorker");
              threadHelper = new ThreadHelper(new ReadBufferWorker(this.mReadBufferManager), Thread.currentThread().getPriority());
              this.mReadBufferManagerThread = threadHelper;
              threadHelper.setName("readBufferManager");
              purgeRxTx(true, true);
              this.mBulkPacketInThread.start();
              this.mReadBufferManagerThread.start();
              setOpen();
              RobotLog.vv("FtDevice", "^^********************%s opened ********************^^", new Object[] { getSerialNumber() });
              bool = true;
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
              close();
            } catch (Exception exception) {
              close();
            } 
          } 
        }  
      return bool;
    } 
  }
  
  public boolean purge(byte paramByte) throws RobotUsbException {
    boolean bool1;
    boolean bool2 = false;
    if ((paramByte & 0x1) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if ((paramByte & 0x2) == 2)
      bool2 = true; 
    return purgeRxTx(bool1, bool2);
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) throws InterruptedException {
    return !isOpen() ? -1 : ((paramInt2 <= 0) ? -2 : this.mReadBufferManager.readBulkInData(paramArrayOfbyte, paramInt1, paramInt2, paramLong, paramTimeWindow));
  }
  
  public boolean readBufferFull() {
    return this.mReadBufferManager.isReadBufferFull();
  }
  
  public void requestReadInterrupt(boolean paramBoolean) {
    ReadBufferManager readBufferManager = this.mReadBufferManager;
    if (readBufferManager != null)
      readBufferManager.requestReadInterrupt(paramBoolean); 
  }
  
  public boolean resetDevice() throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    RobotLog.vv("FtDevice", "resetting %s", new Object[] { getSerialNumber() });
    if (getConnection().controlTransfer(64, 0, 0, 0, (byte[])null, 0, 0) == 0)
      bool = true; 
    return bool;
  }
  
  public void setBaudRate(int paramInt) throws RobotUsbException {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual isOpen : ()Z
    //   4: ifeq -> 404
    //   7: iconst_2
    //   8: newarray int
    //   10: astore_3
    //   11: iload_1
    //   12: lookupswitch default -> 128, 300 -> 256, 600 -> 247, 1200 -> 238, 2400 -> 229, 4800 -> 220, 9600 -> 211, 19200 -> 202, 38400 -> 193, 57600 -> 185, 115200 -> 177, 230400 -> 169, 460800 -> 160, 921600 -> 151
    //   128: aload_0
    //   129: invokespecial isHiSpeed : ()Z
    //   132: ifeq -> 267
    //   135: iload_1
    //   136: sipush #1200
    //   139: if_icmplt -> 267
    //   142: iload_1
    //   143: aload_3
    //   144: invokestatic getDivisorHi : (I[I)B
    //   147: istore_2
    //   148: goto -> 277
    //   151: aload_3
    //   152: iconst_0
    //   153: ldc_w 32771
    //   156: iastore
    //   157: goto -> 262
    //   160: aload_3
    //   161: iconst_0
    //   162: sipush #16390
    //   165: iastore
    //   166: goto -> 262
    //   169: aload_3
    //   170: iconst_0
    //   171: bipush #13
    //   173: iastore
    //   174: goto -> 262
    //   177: aload_3
    //   178: iconst_0
    //   179: bipush #26
    //   181: iastore
    //   182: goto -> 262
    //   185: aload_3
    //   186: iconst_0
    //   187: bipush #52
    //   189: iastore
    //   190: goto -> 262
    //   193: aload_3
    //   194: iconst_0
    //   195: ldc_w 49230
    //   198: iastore
    //   199: goto -> 262
    //   202: aload_3
    //   203: iconst_0
    //   204: ldc_w 32924
    //   207: iastore
    //   208: goto -> 262
    //   211: aload_3
    //   212: iconst_0
    //   213: sipush #16696
    //   216: iastore
    //   217: goto -> 262
    //   220: aload_3
    //   221: iconst_0
    //   222: sipush #625
    //   225: iastore
    //   226: goto -> 262
    //   229: aload_3
    //   230: iconst_0
    //   231: sipush #1250
    //   234: iastore
    //   235: goto -> 262
    //   238: aload_3
    //   239: iconst_0
    //   240: sipush #2500
    //   243: iastore
    //   244: goto -> 262
    //   247: aload_3
    //   248: iconst_0
    //   249: sipush #5000
    //   252: iastore
    //   253: goto -> 262
    //   256: aload_3
    //   257: iconst_0
    //   258: sipush #10000
    //   261: iastore
    //   262: iconst_1
    //   263: istore_2
    //   264: goto -> 277
    //   267: iload_1
    //   268: aload_3
    //   269: aload_0
    //   270: invokespecial isBitModeDevice : ()Z
    //   273: invokestatic getDivisor : (I[IZ)B
    //   276: istore_2
    //   277: aload_0
    //   278: invokevirtual isMultiInterfaceDevice : ()Z
    //   281: ifne -> 298
    //   284: aload_0
    //   285: invokespecial isFt232h : ()Z
    //   288: ifne -> 298
    //   291: aload_0
    //   292: invokespecial isFt232ex : ()Z
    //   295: ifeq -> 327
    //   298: aload_3
    //   299: iconst_1
    //   300: aload_3
    //   301: iconst_1
    //   302: iaload
    //   303: bipush #8
    //   305: ishl
    //   306: iastore
    //   307: aload_3
    //   308: iconst_1
    //   309: aload_3
    //   310: iconst_1
    //   311: iaload
    //   312: ldc 65280
    //   314: iand
    //   315: iastore
    //   316: aload_3
    //   317: iconst_1
    //   318: aload_3
    //   319: iconst_1
    //   320: iaload
    //   321: aload_0
    //   322: getfield mInterfaceID : I
    //   325: ior
    //   326: iastore
    //   327: iload_2
    //   328: iconst_1
    //   329: if_icmpne -> 382
    //   332: aload_0
    //   333: invokevirtual getConnection : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/MonitoredUsbDeviceConnection;
    //   336: bipush #64
    //   338: iconst_3
    //   339: aload_3
    //   340: iconst_0
    //   341: iaload
    //   342: aload_3
    //   343: iconst_1
    //   344: iaload
    //   345: aconst_null
    //   346: checkcast [B
    //   349: iconst_0
    //   350: iconst_0
    //   351: invokevirtual controlTransfer : (IIII[BII)I
    //   354: istore_1
    //   355: iload_1
    //   356: ifne -> 360
    //   359: return
    //   360: new org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbUnspecifiedException
    //   363: dup
    //   364: ldc_w 'setBaudRate: status=%d'
    //   367: iconst_1
    //   368: anewarray java/lang/Object
    //   371: dup
    //   372: iconst_0
    //   373: iload_1
    //   374: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   377: aastore
    //   378: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   381: athrow
    //   382: new org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbUnspecifiedException
    //   385: dup
    //   386: ldc_w 'setBaudRate: rc=%d'
    //   389: iconst_1
    //   390: anewarray java/lang/Object
    //   393: dup
    //   394: iconst_0
    //   395: iload_2
    //   396: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   399: aastore
    //   400: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
    //   403: athrow
    //   404: new org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbDeviceClosedException
    //   407: dup
    //   408: ldc_w 'setBaudRate'
    //   411: invokespecial <init> : (Ljava/lang/String;)V
    //   414: athrow
  }
  
  public boolean setBitMode(byte paramByte1, byte paramByte2) throws RobotUsbException {
    int i = this.mDeviceInfo.type;
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (i == 1)
      return false; 
    Assert.assertTrue(true);
    Assert.assertTrue(true);
    Assert.assertTrue(true);
    Assert.assertTrue(true);
    Assert.assertTrue(true);
    if (i == 0 && paramByte2 != 0) {
      if ((paramByte2 & 0x1) == 0)
        return false; 
    } else if (i == 4 && paramByte2 != 0) {
      byte b;
      if ((paramByte2 & 0x1F) == 0)
        return false; 
      if (paramByte2 == 2) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.mUsbInterface.getId() != 0) {
        b = 1;
      } else {
        b = 0;
      } 
      if ((i & b) != 0)
        return false; 
    } else if (i == 5 && paramByte2 != 0) {
      if ((paramByte2 & 0x25) == 0)
        return false; 
    } else if (i == 6 && paramByte2 != 0) {
      byte b;
      if ((paramByte2 & 0x5F) == 0)
        return false; 
      if ((paramByte2 & 0x48) > 0) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.mUsbInterface.getId() != 0) {
        b = 1;
      } else {
        b = 0;
      } 
      if ((i & b) != 0)
        return false; 
    } else if (i == 7 && paramByte2 != 0) {
      byte b;
      boolean bool2;
      if ((paramByte2 & 0x7) == 0)
        return false; 
      if (paramByte2 == 2) {
        i = 1;
      } else {
        i = 0;
      } 
      if (this.mUsbInterface.getId() != 0) {
        b = 1;
      } else {
        b = 0;
      } 
      if (this.mUsbInterface.getId() != 1) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if ((i & b & bool2) != 0)
        return false; 
    } else if (i == 8 && paramByte2 != 0 && paramByte2 > 64) {
      return false;
    } 
    if (vendorCmdSet(11, paramByte1 & 0xFF | paramByte2 << 8) == 0)
      bool = true; 
    return bool;
  }
  
  public void setBreakOff() throws RobotUsbException {
    setBreak(0);
  }
  
  public void setBreakOn() throws RobotUsbException {
    setBreak(16384);
  }
  
  protected void setConnection(UsbDeviceConnection paramUsbDeviceConnection) {
    MonitoredUsbDeviceConnection monitoredUsbDeviceConnection = this.mUsbConnection;
    if (monitoredUsbDeviceConnection != null) {
      monitoredUsbDeviceConnection.close();
      this.mUsbConnection = null;
    } 
    if (paramUsbDeviceConnection != null)
      this.mUsbConnection = new MonitoredUsbDeviceConnection(this, paramUsbDeviceConnection); 
  }
  
  boolean setContext(Context paramContext) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_2
    //   4: aload_1
    //   5: ifnull -> 23
    //   8: aload_0
    //   9: aload_1
    //   10: putfield mContext : Landroid/content/Context;
    //   13: iconst_1
    //   14: istore_2
    //   15: goto -> 23
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    //   23: aload_0
    //   24: monitorexit
    //   25: iload_2
    //   26: ireturn
    // Exception table:
    //   from	to	target	type
    //   8	13	18	finally
  }
  
  public void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) throws RobotUsbException {
    if (isOpen()) {
      short s = (short)((short)(paramByte1 | paramByte3 << 8) | paramByte2 << 11);
      this.mDeviceInfo.breakOnParam = s;
      throwIfStatus(vendorCmdSet(4, s), "setDataCharacteristics");
      return;
    } 
    throw new RobotUsbDeviceClosedException("setDataCharacteristics");
  }
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    synchronized (this.openCloseLock) {
      this.mDebugRetainBuffers = paramBoolean;
      if (this.mReadBufferManager != null)
        this.mReadBufferManager.setDebugRetainBuffers(paramBoolean); 
      return;
    } 
  }
  
  public void setDeviceClosedReason(RobotUsbException paramRobotUsbException) {
    this.mDeviceClosedReason = paramRobotUsbException;
  }
  
  protected void setDriverParameters(FtDeviceManagerParams paramFtDeviceManagerParams) {
    this.mParams.setMaxReadBufferSize(paramFtDeviceManagerParams.getMaxReadBufferSize());
    this.mParams.setPacketBufferCacheSize(paramFtDeviceManagerParams.getPacketBufferCacheSize());
    this.mParams.setBuildInReadTimeout(paramFtDeviceManagerParams.getBulkInReadTimeout());
  }
  
  public boolean setDtr() throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (vendorCmdSet(1, 257) == 0)
      bool = true; 
    return bool;
  }
  
  public boolean setFlowControl(short paramShort, byte paramByte1, byte paramByte2) throws RobotUsbException {
    boolean bool2 = isOpen();
    boolean bool1 = false;
    if (!bool2)
      return false; 
    if (paramShort == 1024) {
      short s = (short)(paramByte1 & 0xFF | (short)(paramByte2 << 8));
    } else {
      paramByte1 = 0;
    } 
    if (getConnection().controlTransfer(64, 2, paramByte1, this.mInterfaceID | paramShort, (byte[])null, 0, 0) == 0) {
      bool1 = true;
      if (paramShort == 256)
        return setRts(); 
      if (paramShort == 512)
        bool1 = setDtr(); 
    } 
    return bool1;
  }
  
  public void setLatencyTimer(byte paramByte) throws RobotUsbException {
    if (isOpen()) {
      int i = vendorCmdSet(9, paramByte & 0xFF);
      if (i == 0) {
        this.mLatencyTimer = paramByte;
        return;
      } 
      throwIfStatus(i, "setLatencyTimer");
    } 
  }
  
  public boolean setRts() throws RobotUsbException {
    boolean bool1 = isOpen();
    boolean bool = false;
    if (!bool1)
      return false; 
    if (vendorCmdSet(1, 514) == 0)
      bool = true; 
    return bool;
  }
  
  public void skipToLikelyUsbPacketStart() {
    ReadBufferManager readBufferManager = this.mReadBufferManager;
    if (readBufferManager != null)
      readBufferManager.skipToLikelyUsbPacketStart(); 
  }
  
  public int vendorCmdGet(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) throws RobotUsbException {
    if (!isOpen()) {
      RobotLog.ee("FtDevice", "VendorCmdGet: Device not open");
      return -1;
    } 
    if (paramInt3 < 0) {
      RobotLog.ee("FtDevice", "VendorCmdGet: Invalid data length");
      return -2;
    } 
    if (paramArrayOfbyte == null) {
      RobotLog.ee("FtDevice", "VendorCmdGet: buf is null");
      return -2;
    } 
    if (paramArrayOfbyte.length < paramInt3) {
      RobotLog.ee("FtDevice", "VendorCmdGet: length of buffer is smaller than data length to get");
      return -2;
    } 
    return getConnection().controlTransfer(192, paramInt1, paramInt2, this.mInterfaceID, paramArrayOfbyte, paramInt3, 0);
  }
  
  public int vendorCmdSet(int paramInt1, int paramInt2) throws RobotUsbException {
    return !isOpen() ? -1 : getConnection().controlTransfer(64, paramInt1, paramInt2, this.mInterfaceID, (byte[])null, 0, 0);
  }
  
  public int vendorCmdSet(int paramInt1, int paramInt2, byte[] paramArrayOfbyte, int paramInt3) throws RobotUsbException {
    if (!isOpen()) {
      RobotLog.ee("FtDevice", "VendorCmdSet: Device not open");
      return -1;
    } 
    if (paramInt3 < 0) {
      RobotLog.ee("FtDevice", "VendorCmdSet: Invalid data length");
      return -2;
    } 
    if (paramArrayOfbyte == null) {
      if (paramInt3 > 0) {
        RobotLog.ee("FtDevice", "VendorCmdSet: buf is null!");
        return -2;
      } 
    } else if (paramArrayOfbyte.length < paramInt3) {
      RobotLog.ee("FtDevice", "VendorCmdSet: length of buffer is smaller than data length to set");
      return -2;
    } 
    return getConnection().controlTransfer(64, paramInt1, paramInt2, this.mInterfaceID, paramArrayOfbyte, paramInt3, 0);
  }
  
  public int write(byte[] paramArrayOfbyte) throws InterruptedException, RobotUsbException {
    return write(paramArrayOfbyte, 0, paramArrayOfbyte.length);
  }
  
  public int write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws InterruptedException, RobotUsbException {
    if (!isOpen())
      return -1; 
    if (paramInt2 < 0)
      return -2; 
    if (paramInt2 == 0)
      return 0; 
    if (this.mReadBufferManager.getDebugRetainBuffers()) {
      BulkPacketBufferOut bulkPacketBufferOut = this.mReadBufferManager.newOutputBuffer(paramArrayOfbyte, paramInt1, paramInt2);
      this.mReadBufferManager.retainRecentBuffer(bulkPacketBufferOut);
    } 
    return this.mUsbConnection.bulkTransfer(this.mBulkOutEndpoint, paramArrayOfbyte, paramInt1, paramInt2, 2147483647);
  }
  
  private class ThreadHelper {
    protected Thread thread;
    
    protected CountDownLatch threadComplete = new CountDownLatch(1);
    
    public ThreadHelper(final Runnable runnable, int param1Int) {
      Thread thread = new Thread(new Runnable() {
            public void run() {
              try {
                runnable.run();
                return;
              } finally {
                RobotLog.vv("FtDevice", "%s thread %s is stopped", new Object[] { this.this$1.this$0.getSerialNumber(), this.this$1.thread.getName() });
                FtDevice.ThreadHelper.this.threadComplete.countDown();
              } 
            }
          });
      this.thread = thread;
      thread.setPriority(param1Int);
    }
    
    public void setName(String param1String) {
      this.thread.setName(param1String);
    }
    
    public void start() {
      this.thread.start();
    }
    
    public void stop() {
      RobotLog.vv("FtDevice", "%s stopping thread %s", new Object[] { this.this$0.getSerialNumber(), this.thread.getName() });
      this.thread.interrupt();
      try {
        this.threadComplete.await(1000L, TimeUnit.MILLISECONDS);
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return;
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        runnable.run();
        return;
      } finally {
        RobotLog.vv("FtDevice", "%s thread %s is stopped", new Object[] { this.this$1.this$0.getSerialNumber(), this.this$1.thread.getName() });
        this.this$1.threadComplete.countDown();
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\FtDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */