package com.qualcomm.robotcore.hardware.usb.serial;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class RobotUsbDeviceTty extends RobotUsbDeviceImplBase implements RobotUsbDevice {
  public static boolean DEBUG = false;
  
  public static final String TAG = "RobotUsbDeviceTTY";
  
  protected int baudRate;
  
  protected boolean debugRetainBuffers = false;
  
  protected final File file;
  
  protected int msDefaultTimeout = 100;
  
  protected String productName = "";
  
  protected Queue<Byte> readAhead = new ArrayDeque<Byte>();
  
  protected final Object readLock = new Object();
  
  protected SerialPort serialPort;
  
  protected final Object startStopLock = new Object();
  
  protected RobotUsbDevice.USBIdentifiers usbIdentifiers = new RobotUsbDevice.USBIdentifiers();
  
  protected final Object writeLock = new Object();
  
  public RobotUsbDeviceTty(SerialPort paramSerialPort, SerialNumber paramSerialNumber, File paramFile) {
    super(paramSerialNumber);
    RobotLog.vv("RobotUsbDeviceTTY", "opening serial=%s file=%s", new Object[] { paramSerialNumber, paramFile.getPath() });
    this.file = paramFile;
    this.serialPort = paramSerialPort;
    this.baudRate = paramSerialPort.getBaudRate();
  }
  
  public void close() {
    synchronized (this.startStopLock) {
      if (this.serialPort != null) {
        RobotLog.vv("RobotUsbDeviceTTY", "closing serial=%s file=%s", new Object[] { this.serialNumber, this.file.getPath() });
        this.serialPort.close();
        this.serialPort = null;
        removeFromExtantDevices();
      } 
      return;
    } 
  }
  
  public boolean getDebugRetainBuffers() {
    return this.debugRetainBuffers;
  }
  
  public int getMsDefaultTimeout() {
    return this.msDefaultTimeout;
  }
  
  public String getProductName() {
    return this.productName;
  }
  
  public String getTag() {
    return "RobotUsbDeviceTTY";
  }
  
  public RobotUsbDevice.USBIdentifiers getUsbIdentifiers() {
    return this.usbIdentifiers;
  }
  
  public boolean isAttached() {
    return true;
  }
  
  public boolean isOpen() {
    synchronized (this.startStopLock) {
      if (this.serialPort != null)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return bool;
  }
  
  public void logRetainedBuffers(long paramLong1, long paramLong2, String paramString1, String paramString2, Object... paramVarArgs) {
    RobotLog.ee(paramString1, paramString2, paramVarArgs);
  }
  
  public boolean mightBeAtUsbPacketStart() {
    return true;
  }
  
  public int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, long paramLong, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException {
    // Byte code:
    //   0: aload_0
    //   1: getfield readLock : Ljava/lang/Object;
    //   4: astore #11
    //   6: aload #11
    //   8: monitorenter
    //   9: new com/qualcomm/robotcore/util/ElapsedTime
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: astore #12
    //   18: iconst_0
    //   19: istore #8
    //   21: iload #8
    //   23: istore #7
    //   25: iload #8
    //   27: iload_3
    //   28: if_icmpge -> 75
    //   31: iload #8
    //   33: istore #7
    //   35: aload_0
    //   36: getfield readAhead : Ljava/util/Queue;
    //   39: invokeinterface size : ()I
    //   44: ifle -> 75
    //   47: aload_1
    //   48: iload #8
    //   50: aload_0
    //   51: getfield readAhead : Ljava/util/Queue;
    //   54: invokeinterface remove : ()Ljava/lang/Object;
    //   59: checkcast java/lang/Byte
    //   62: invokevirtual byteValue : ()B
    //   65: bastore
    //   66: iload #8
    //   68: iconst_1
    //   69: iadd
    //   70: istore #8
    //   72: goto -> 21
    //   75: iload #7
    //   77: istore #8
    //   79: aload_0
    //   80: invokevirtual isOpen : ()Z
    //   83: ifeq -> 197
    //   86: iload #7
    //   88: istore #8
    //   90: iload #7
    //   92: iload_3
    //   93: if_icmpge -> 197
    //   96: aload_0
    //   97: getfield serialPort : Lcom/qualcomm/robotcore/hardware/usb/serial/SerialPort;
    //   100: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   103: aload_1
    //   104: iload_2
    //   105: iload #7
    //   107: iadd
    //   108: iload_3
    //   109: iload #7
    //   111: isub
    //   112: invokevirtual read : ([BII)I
    //   115: istore #9
    //   117: iload #9
    //   119: istore #8
    //   121: iload #9
    //   123: iconst_m1
    //   124: if_icmpne -> 361
    //   127: iconst_0
    //   128: istore #8
    //   130: goto -> 361
    //   133: iload #10
    //   135: invokestatic assertTrue : (Z)V
    //   138: iload #7
    //   140: iload #8
    //   142: iadd
    //   143: istore #7
    //   145: iload #7
    //   147: iload_3
    //   148: if_icmpne -> 158
    //   151: iload #7
    //   153: istore #8
    //   155: goto -> 197
    //   158: aload #12
    //   160: invokevirtual milliseconds : ()D
    //   163: lload #4
    //   165: l2d
    //   166: dcmpl
    //   167: ifle -> 177
    //   170: iload #7
    //   172: istore #8
    //   174: goto -> 197
    //   177: invokestatic interrupted : ()Z
    //   180: ifne -> 189
    //   183: invokestatic yield : ()V
    //   186: goto -> 75
    //   189: new java/lang/InterruptedException
    //   192: dup
    //   193: invokespecial <init> : ()V
    //   196: athrow
    //   197: iload #8
    //   199: iload_3
    //   200: if_icmpne -> 233
    //   203: getstatic com/qualcomm/robotcore/hardware/usb/serial/RobotUsbDeviceTty.DEBUG : Z
    //   206: ifeq -> 217
    //   209: aload_0
    //   210: aload_1
    //   211: iload_2
    //   212: iload #8
    //   214: invokevirtual dumpBytesReceived : ([BII)V
    //   217: aload #6
    //   219: ifnull -> 227
    //   222: aload #6
    //   224: invokevirtual clear : ()V
    //   227: aload #11
    //   229: monitorexit
    //   230: iload #8
    //   232: ireturn
    //   233: iconst_0
    //   234: istore_2
    //   235: iload_2
    //   236: iload #8
    //   238: if_icmpge -> 264
    //   241: aload_0
    //   242: getfield readAhead : Ljava/util/Queue;
    //   245: aload_1
    //   246: iload_2
    //   247: baload
    //   248: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   251: invokeinterface add : (Ljava/lang/Object;)Z
    //   256: pop
    //   257: iload_2
    //   258: iconst_1
    //   259: iadd
    //   260: istore_2
    //   261: goto -> 235
    //   264: ldc 'RobotUsbDeviceTTY'
    //   266: ldc 'didn't read enough data cbToRead=%d cbRead=%d msTimeout=%d'
    //   268: iconst_3
    //   269: anewarray java/lang/Object
    //   272: dup
    //   273: iconst_0
    //   274: iload_3
    //   275: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   278: aastore
    //   279: dup
    //   280: iconst_1
    //   281: iload #8
    //   283: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   286: aastore
    //   287: dup
    //   288: iconst_2
    //   289: lload #4
    //   291: invokestatic valueOf : (J)Ljava/lang/Long;
    //   294: aastore
    //   295: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   298: aload #11
    //   300: monitorexit
    //   301: iconst_0
    //   302: ireturn
    //   303: astore_1
    //   304: aload_1
    //   305: ldc 'exception in %s.read()'
    //   307: iconst_1
    //   308: anewarray java/lang/Object
    //   311: dup
    //   312: iconst_0
    //   313: ldc 'RobotUsbDeviceTTY'
    //   315: aastore
    //   316: invokestatic createChained : (Ljava/lang/Exception;Ljava/lang/String;[Ljava/lang/Object;)Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbUnspecifiedException;
    //   319: athrow
    //   320: astore_1
    //   321: aload_1
    //   322: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   325: instanceof java/lang/InterruptedException
    //   328: ifeq -> 342
    //   331: aload_1
    //   332: invokevirtual getCause : ()Ljava/lang/Throwable;
    //   335: checkcast java/lang/InterruptedException
    //   338: astore_1
    //   339: goto -> 354
    //   342: new java/lang/InterruptedException
    //   345: dup
    //   346: aload_1
    //   347: invokevirtual getMessage : ()Ljava/lang/String;
    //   350: invokespecial <init> : (Ljava/lang/String;)V
    //   353: astore_1
    //   354: aload_1
    //   355: athrow
    //   356: aload #11
    //   358: monitorexit
    //   359: aload_1
    //   360: athrow
    //   361: iload #8
    //   363: iflt -> 372
    //   366: iconst_1
    //   367: istore #10
    //   369: goto -> 133
    //   372: iconst_0
    //   373: istore #10
    //   375: goto -> 133
    //   378: astore_1
    //   379: goto -> 356
    // Exception table:
    //   from	to	target	type
    //   9	18	320	java/io/InterruptedIOException
    //   9	18	303	java/io/IOException
    //   9	18	378	finally
    //   35	66	320	java/io/InterruptedIOException
    //   35	66	303	java/io/IOException
    //   35	66	378	finally
    //   79	86	320	java/io/InterruptedIOException
    //   79	86	303	java/io/IOException
    //   79	86	378	finally
    //   96	117	320	java/io/InterruptedIOException
    //   96	117	303	java/io/IOException
    //   96	117	378	finally
    //   133	138	320	java/io/InterruptedIOException
    //   133	138	303	java/io/IOException
    //   133	138	378	finally
    //   158	170	320	java/io/InterruptedIOException
    //   158	170	303	java/io/IOException
    //   158	170	378	finally
    //   177	186	320	java/io/InterruptedIOException
    //   177	186	303	java/io/IOException
    //   177	186	378	finally
    //   189	197	320	java/io/InterruptedIOException
    //   189	197	303	java/io/IOException
    //   189	197	378	finally
    //   203	217	320	java/io/InterruptedIOException
    //   203	217	303	java/io/IOException
    //   203	217	378	finally
    //   222	227	320	java/io/InterruptedIOException
    //   222	227	303	java/io/IOException
    //   222	227	378	finally
    //   227	230	378	finally
    //   241	257	320	java/io/InterruptedIOException
    //   241	257	303	java/io/IOException
    //   241	257	378	finally
    //   264	298	320	java/io/InterruptedIOException
    //   264	298	303	java/io/IOException
    //   264	298	378	finally
    //   298	301	378	finally
    //   304	320	378	finally
    //   321	339	378	finally
    //   342	354	378	finally
    //   354	356	378	finally
    //   356	359	378	finally
  }
  
  public void requestReadInterrupt(boolean paramBoolean) {}
  
  public void resetAndFlushBuffers() {}
  
  public void setBaudRate(int paramInt) throws RobotUsbException {}
  
  public void setBreak(boolean paramBoolean) throws RobotUsbException {}
  
  public void setDataCharacteristics(byte paramByte1, byte paramByte2, byte paramByte3) throws RobotUsbException {}
  
  public void setDebugRetainBuffers(boolean paramBoolean) {
    this.debugRetainBuffers = paramBoolean;
  }
  
  public void setLatencyTimer(int paramInt) throws RobotUsbException {}
  
  public void setMsDefaultTimeout(int paramInt) {
    this.msDefaultTimeout = paramInt;
  }
  
  public void setProductName(String paramString) {
    this.productName = paramString;
  }
  
  public void setUsbIdentifiers(RobotUsbDevice.USBIdentifiers paramUSBIdentifiers) {
    this.usbIdentifiers = paramUSBIdentifiers;
  }
  
  public void skipToLikelyUsbPacketStart() {}
  
  public void write(byte[] paramArrayOfbyte) throws RobotUsbException {
    synchronized (this.writeLock) {
      this.serialPort.getOutputStream().write(paramArrayOfbyte);
      if (DEBUG)
        dumpBytesSent(paramArrayOfbyte); 
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\serial\RobotUsbDeviceTty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */