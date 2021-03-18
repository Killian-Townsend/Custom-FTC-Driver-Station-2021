package com.qualcomm.hardware.modernrobotics.comm;

import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbProtocolException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTimeoutException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbTooManySequentialErrorsException;

public class ModernRoboticsReaderWriter {
  public static final String COMM_ERROR_READ = "comm error read";
  
  public static final String COMM_ERROR_WRITE = "comm error write";
  
  public static final String COMM_FAILURE_READ = "comm failure read";
  
  public static final String COMM_FAILURE_WRITE = "comm failure write";
  
  public static final String COMM_PAYLOAD_ERROR_READ = "comm payload error read";
  
  public static final String COMM_PAYLOAD_ERROR_WRITE = "comm payload error write";
  
  public static final String COMM_SYNC_LOST = "comm sync lost";
  
  public static final String COMM_TIMEOUT_READ = "comm timeout awaiting response (read)";
  
  public static final String COMM_TIMEOUT_WRITE = "comm timeout awaiting response (write)";
  
  public static final String COMM_TYPE_ERROR_READ = "comm type error read";
  
  public static final String COMM_TYPE_ERROR_WRITE = "comm type error write";
  
  public static boolean DEBUG = false;
  
  public static int MAX_SEQUENTIAL_USB_ERROR_COUNT = 5;
  
  public static int MS_COMM_ERROR_WAIT = 100;
  
  public static int MS_FAILURE_WAIT = 40;
  
  public static int MS_GARBAGE_COLLECTION_SPURT = 40;
  
  public static int MS_INTER_BYTE_TIMEOUT = 10;
  
  public static int MS_MAX_TIMEOUT = 100;
  
  public static int MS_REQUEST_RESPONSE_TIMEOUT = 2 * 2 + 50;
  
  public static int MS_RESYNCH_TIMEOUT = 1000;
  
  public static int MS_USB_HUB_LATENCY = 2;
  
  public static final String TAG = "MRReaderWriter";
  
  protected final RobotUsbDevice device;
  
  protected boolean isSynchronized = false;
  
  protected int msUsbReadRetryInterval = 20;
  
  protected int msUsbWriteRetryInterval = 20;
  
  protected ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest> requestAllocationContext = new ModernRoboticsDatagram.AllocationContext<ModernRoboticsRequest>();
  
  protected ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse> responseAllocationContext = new ModernRoboticsDatagram.AllocationContext<ModernRoboticsResponse>();
  
  protected Deadline responseDeadline = new Deadline(MS_RESYNCH_TIMEOUT, TimeUnit.MILLISECONDS);
  
  protected int usbReadRetryCount = 4;
  
  protected int usbSequentialCommReadErrorCount = 0;
  
  protected int usbSequentialCommWriteErrorCount = 0;
  
  protected int usbWriteRetryCount = 4;
  
  public ModernRoboticsReaderWriter(RobotUsbDevice paramRobotUsbDevice) {
    this.device = paramRobotUsbDevice;
    paramRobotUsbDevice.setDebugRetainBuffers(true);
  }
  
  protected static String bufferToString(byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("[");
    if (paramArrayOfbyte.length > 0)
      stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(paramArrayOfbyte[0]) })); 
    int j = Math.min(paramArrayOfbyte.length, 16);
    for (int i = 1; i < j; i++) {
      stringBuilder.append(String.format(" %02x", new Object[] { Byte.valueOf(paramArrayOfbyte[i]) }));
    } 
    if (j < paramArrayOfbyte.length)
      stringBuilder.append(" ..."); 
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
  
  public void close() {
    this.device.close();
  }
  
  protected void doExceptionBookkeeping() {
    this.isSynchronized = false;
  }
  
  protected void logAndRethrowTimeout(RobotUsbTimeoutException paramRobotUsbTimeoutException, ModernRoboticsRequest paramModernRoboticsRequest, String paramString) throws RobotUsbTimeoutException {
    ModernRoboticsRequest modernRoboticsRequest = ModernRoboticsRequest.newInstance(this.requestAllocationContext, 0);
    System.arraycopy(paramModernRoboticsRequest.data, 0, modernRoboticsRequest.data, 0, modernRoboticsRequest.data.length);
    RobotLog.ee("MRReaderWriter", "%s: %s request=%s", new Object[] { this.device.getSerialNumber(), paramString, bufferToString(modernRoboticsRequest.data) });
    this.device.logRetainedBuffers(paramRobotUsbTimeoutException.nsTimerStart, paramRobotUsbTimeoutException.nsTimerExpire, "MRReaderWriter", "recent data on %s", new Object[] { this.device.getSerialNumber() });
    doExceptionBookkeeping();
    throw paramRobotUsbTimeoutException;
  }
  
  protected void logAndThrowProtocol(ModernRoboticsRequest paramModernRoboticsRequest, ModernRoboticsResponse paramModernRoboticsResponse, String paramString) throws RobotUsbProtocolException {
    ModernRoboticsRequest modernRoboticsRequest = ModernRoboticsRequest.newInstance(this.requestAllocationContext, 0);
    System.arraycopy(paramModernRoboticsRequest.data, 0, modernRoboticsRequest.data, 0, modernRoboticsRequest.data.length);
    ModernRoboticsResponse modernRoboticsResponse = ModernRoboticsResponse.newInstance(this.responseAllocationContext, 0);
    System.arraycopy(paramModernRoboticsResponse.data, 0, modernRoboticsResponse.data, 0, modernRoboticsResponse.data.length);
    RobotLog.ee("MRReaderWriter", "%s: %s: request:%s response:%s", new Object[] { this.device.getSerialNumber(), paramString, bufferToString(modernRoboticsRequest.data), bufferToString(modernRoboticsResponse.data) });
    doExceptionBookkeeping();
    throw new RobotUsbProtocolException(paramString);
  }
  
  protected void logAndThrowProtocol(String paramString, Object... paramVarArgs) throws RobotUsbProtocolException {
    paramString = String.format(paramString, paramVarArgs);
    RobotLog.ee("MRReaderWriter", "%s: %s", new Object[] { this.device.getSerialNumber(), paramString });
    doExceptionBookkeeping();
    throw new RobotUsbProtocolException(paramString);
  }
  
  public void read(boolean paramBoolean, int paramInt, byte[] paramArrayOfbyte, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException {
    if (DEBUG)
      RobotLog.vv("MRReaderWriter", "%s: read(addr=%d cb=%d)", new Object[] { this.device.getSerialNumber(), Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfbyte.length) }); 
    robotUsbException = null;
    int i = 0;
    while (i < this.usbReadRetryCount) {
      if (i > 0)
        RobotLog.ee("MRReaderWriter", "%s: retry #%d read(addr=%d cb=%d)", new Object[] { this.device.getSerialNumber(), Integer.valueOf(i), Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfbyte.length) }); 
      try {
        readOnce(paramInt, paramArrayOfbyte, paramTimeWindow);
        return;
      } catch (RobotUsbException robotUsbException) {
        if (!this.device.isOpen())
          return; 
        if (!paramBoolean) {
          RobotLog.ee("MRReaderWriter", "%s: ignoring failed read(addr=%d cb=%d)", new Object[] { this.device.getSerialNumber(), Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfbyte.length) });
          return;
        } 
        Thread.sleep(this.msUsbReadRetryInterval);
        this.device.resetAndFlushBuffers();
        i++;
      } 
    } 
    if (robotUsbException == null)
      return; 
    throw robotUsbException;
  }
  
  protected void readIncomingBytes(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, TimeWindow paramTimeWindow, String paramString) throws RobotUsbException, InterruptedException {
    if (paramInt2 > 0) {
      long l1 = Math.min((MS_INTER_BYTE_TIMEOUT * (paramInt2 + 2) + paramInt3 + MS_GARBAGE_COLLECTION_SPURT), MS_MAX_TIMEOUT);
      long l2 = System.nanoTime();
      paramInt1 = this.device.read(paramArrayOfbyte, paramInt1, paramInt2, l1, paramTimeWindow);
      if (paramInt1 == paramInt2)
        return; 
      if (paramInt1 != 0) {
        logAndThrowProtocol("readIncomingBytes(%s) cbToRead=%d cbRead=%d", new Object[] { paramString, Integer.valueOf(paramInt2), Integer.valueOf(paramInt1) });
        return;
      } 
      throw new RobotUsbTimeoutException(l2, "%s: unable to read %d bytes in %d ms", new Object[] { paramString, Integer.valueOf(paramInt2), Long.valueOf(l1) });
    } 
  }
  
  protected void readOnce(int paramInt, byte[] paramArrayOfbyte, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #8
    //   3: aconst_null
    //   4: astore #9
    //   6: aconst_null
    //   7: astore #5
    //   9: aconst_null
    //   10: astore #6
    //   12: aconst_null
    //   13: astore #4
    //   15: aload_0
    //   16: getfield requestAllocationContext : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsDatagram$AllocationContext;
    //   19: iconst_0
    //   20: invokestatic newInstance : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsDatagram$AllocationContext;I)Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;
    //   23: astore #7
    //   25: aload #8
    //   27: astore #5
    //   29: aload #9
    //   31: astore #6
    //   33: aload #7
    //   35: iconst_0
    //   36: invokevirtual setRead : (I)V
    //   39: aload #8
    //   41: astore #5
    //   43: aload #9
    //   45: astore #6
    //   47: aload #7
    //   49: iload_1
    //   50: invokevirtual setAddress : (I)V
    //   53: aload #8
    //   55: astore #5
    //   57: aload #9
    //   59: astore #6
    //   61: aload #7
    //   63: aload_2
    //   64: arraylength
    //   65: invokevirtual setPayloadLength : (I)V
    //   68: aload #8
    //   70: astore #5
    //   72: aload #9
    //   74: astore #6
    //   76: aload_0
    //   77: getfield device : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
    //   80: aload #7
    //   82: getfield data : [B
    //   85: invokeinterface write : ([B)V
    //   90: aload #8
    //   92: astore #5
    //   94: aload #9
    //   96: astore #6
    //   98: aload_0
    //   99: aload #7
    //   101: aload_3
    //   102: invokevirtual readResponse : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lorg/firstinspires/ftc/robotcore/internal/hardware/TimeWindow;)Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;
    //   105: astore_3
    //   106: aload_3
    //   107: astore #4
    //   109: aload_3
    //   110: astore #5
    //   112: aload_3
    //   113: astore #6
    //   115: aload_3
    //   116: invokevirtual isFailure : ()Z
    //   119: ifeq -> 159
    //   122: aload_3
    //   123: astore #4
    //   125: aload_3
    //   126: astore #5
    //   128: aload_3
    //   129: astore #6
    //   131: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_FAILURE_WAIT : I
    //   134: i2l
    //   135: invokestatic sleep : (J)V
    //   138: aload_3
    //   139: astore #4
    //   141: aload_3
    //   142: astore #5
    //   144: aload_3
    //   145: astore #6
    //   147: aload_0
    //   148: aload #7
    //   150: aload_3
    //   151: ldc 'comm failure read'
    //   153: invokevirtual logAndThrowProtocol : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;Ljava/lang/String;)V
    //   156: goto -> 342
    //   159: aload_3
    //   160: astore #4
    //   162: aload_3
    //   163: astore #5
    //   165: aload_3
    //   166: astore #6
    //   168: aload_3
    //   169: invokevirtual isRead : ()Z
    //   172: ifeq -> 264
    //   175: aload_3
    //   176: astore #4
    //   178: aload_3
    //   179: astore #5
    //   181: aload_3
    //   182: astore #6
    //   184: aload_3
    //   185: invokevirtual getFunction : ()I
    //   188: ifne -> 264
    //   191: aload_3
    //   192: astore #4
    //   194: aload_3
    //   195: astore #5
    //   197: aload_3
    //   198: astore #6
    //   200: aload_3
    //   201: invokevirtual getAddress : ()I
    //   204: iload_1
    //   205: if_icmpne -> 264
    //   208: aload_3
    //   209: astore #4
    //   211: aload_3
    //   212: astore #5
    //   214: aload_3
    //   215: astore #6
    //   217: aload_3
    //   218: invokevirtual getPayloadLength : ()I
    //   221: aload_2
    //   222: arraylength
    //   223: if_icmpne -> 264
    //   226: aload_3
    //   227: astore #4
    //   229: aload_3
    //   230: astore #5
    //   232: aload_3
    //   233: astore #6
    //   235: aload_0
    //   236: iconst_0
    //   237: putfield usbSequentialCommReadErrorCount : I
    //   240: aload_3
    //   241: astore #4
    //   243: aload_3
    //   244: astore #5
    //   246: aload_3
    //   247: astore #6
    //   249: aload_3
    //   250: getfield data : [B
    //   253: iconst_5
    //   254: aload_2
    //   255: iconst_0
    //   256: aload_2
    //   257: arraylength
    //   258: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   261: goto -> 342
    //   264: aload_3
    //   265: astore #4
    //   267: aload_3
    //   268: astore #5
    //   270: aload_3
    //   271: astore #6
    //   273: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_COMM_ERROR_WAIT : I
    //   276: i2l
    //   277: invokestatic sleep : (J)V
    //   280: aload_3
    //   281: astore #4
    //   283: aload_3
    //   284: astore #5
    //   286: aload_3
    //   287: astore #6
    //   289: aload_0
    //   290: aload #7
    //   292: aload_3
    //   293: ldc 'comm error read'
    //   295: invokevirtual logAndThrowProtocol : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;Ljava/lang/String;)V
    //   298: goto -> 342
    //   301: astore_2
    //   302: aload #4
    //   304: astore #5
    //   306: aload #4
    //   308: astore #6
    //   310: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_COMM_ERROR_WAIT : I
    //   313: i2l
    //   314: invokestatic sleep : (J)V
    //   317: aload #4
    //   319: astore #5
    //   321: aload #4
    //   323: astore #6
    //   325: aload_0
    //   326: aload_2
    //   327: aload #7
    //   329: aload_0
    //   330: ldc 'comm timeout awaiting response (read)'
    //   332: aload_2
    //   333: invokevirtual timeoutMessage : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException;)Ljava/lang/String;
    //   336: invokevirtual logAndRethrowTimeout : (Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Ljava/lang/String;)V
    //   339: aload #4
    //   341: astore_3
    //   342: aload_3
    //   343: ifnull -> 350
    //   346: aload_3
    //   347: invokevirtual close : ()V
    //   350: aload #7
    //   352: ifnull -> 360
    //   355: aload #7
    //   357: invokevirtual close : ()V
    //   360: return
    //   361: astore_3
    //   362: aload #7
    //   364: astore_2
    //   365: goto -> 423
    //   368: astore #4
    //   370: aload #6
    //   372: astore_2
    //   373: aload #7
    //   375: astore_3
    //   376: goto -> 396
    //   379: astore_3
    //   380: aconst_null
    //   381: astore_2
    //   382: aload #6
    //   384: astore #5
    //   386: goto -> 423
    //   389: astore #4
    //   391: aconst_null
    //   392: astore_2
    //   393: aload #5
    //   395: astore_3
    //   396: aload_0
    //   397: aload_0
    //   398: getfield usbSequentialCommReadErrorCount : I
    //   401: iconst_1
    //   402: iadd
    //   403: putfield usbSequentialCommReadErrorCount : I
    //   406: aload #4
    //   408: athrow
    //   409: astore #6
    //   411: aload_3
    //   412: astore #4
    //   414: aload_2
    //   415: astore #5
    //   417: aload #6
    //   419: astore_3
    //   420: aload #4
    //   422: astore_2
    //   423: aload #5
    //   425: ifnull -> 433
    //   428: aload #5
    //   430: invokevirtual close : ()V
    //   433: aload_2
    //   434: ifnull -> 441
    //   437: aload_2
    //   438: invokevirtual close : ()V
    //   441: aload_3
    //   442: athrow
    // Exception table:
    //   from	to	target	type
    //   15	25	389	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   15	25	379	finally
    //   33	39	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   33	39	361	finally
    //   47	53	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   47	53	361	finally
    //   61	68	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   61	68	361	finally
    //   76	90	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   76	90	361	finally
    //   98	106	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   98	106	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   98	106	361	finally
    //   115	122	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   115	122	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   115	122	361	finally
    //   131	138	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   131	138	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   131	138	361	finally
    //   147	156	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   147	156	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   147	156	361	finally
    //   168	175	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   168	175	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   168	175	361	finally
    //   184	191	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   184	191	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   184	191	361	finally
    //   200	208	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   200	208	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   200	208	361	finally
    //   217	226	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   217	226	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   217	226	361	finally
    //   235	240	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   235	240	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   235	240	361	finally
    //   249	261	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   249	261	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   249	261	361	finally
    //   273	280	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   273	280	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   273	280	361	finally
    //   289	298	301	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   289	298	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   289	298	361	finally
    //   310	317	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   310	317	361	finally
    //   325	339	368	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   325	339	361	finally
    //   396	409	409	finally
  }
  
  protected ModernRoboticsResponse readResponse(ModernRoboticsRequest paramModernRoboticsRequest, TimeWindow paramTimeWindow) throws RobotUsbException, InterruptedException {
    this.responseDeadline.reset();
    while (!this.responseDeadline.hasExpired()) {
      if (!this.device.mightBeAtUsbPacketStart())
        this.isSynchronized = false; 
      ModernRoboticsResponse modernRoboticsResponse = ModernRoboticsResponse.newInstance(this.responseAllocationContext, 0);
      try {
        int i;
        if (!this.isSynchronized) {
          byte[] arrayOfByte1 = new byte[1];
          byte[] arrayOfByte2 = new byte[3];
          this.device.skipToLikelyUsbPacketStart();
          i = readSingleByte(arrayOfByte1, MS_REQUEST_RESPONSE_TIMEOUT, null, "sync0");
          byte b = ModernRoboticsResponse.syncBytes[0];
          if (i != b) {
            if (modernRoboticsResponse != null)
              continue; 
            continue;
          } 
          if (readSingleByte(arrayOfByte1, 0, null, "sync1") != ModernRoboticsResponse.syncBytes[1]) {
            if (modernRoboticsResponse != null)
              continue; 
            continue;
          } 
          readIncomingBytes(arrayOfByte2, 0, 3, 0, null, "syncSuffix");
          System.arraycopy(ModernRoboticsResponse.syncBytes, 0, modernRoboticsResponse.data, 0, 2);
          System.arraycopy(arrayOfByte2, 0, modernRoboticsResponse.data, 2, 3);
        } else {
          readIncomingBytes(modernRoboticsResponse.data, 0, modernRoboticsResponse.data.length, MS_REQUEST_RESPONSE_TIMEOUT, null, "header");
          if (!modernRoboticsResponse.syncBytesValid())
            logAndThrowProtocol(paramModernRoboticsRequest, modernRoboticsResponse, "comm sync lost"); 
        } 
        if (!modernRoboticsResponse.isFailure() && (paramModernRoboticsRequest.isRead() != modernRoboticsResponse.isRead() || paramModernRoboticsRequest.getFunction() != modernRoboticsResponse.getFunction())) {
          String str;
          if (paramModernRoboticsRequest.isWrite()) {
            str = "comm type error write";
          } else {
            str = "comm type error read";
          } 
          logAndThrowProtocol(paramModernRoboticsRequest, modernRoboticsResponse, str);
        } 
        if (modernRoboticsResponse.isFailure() || paramModernRoboticsRequest.isWrite()) {
          i = 0;
        } else {
          i = paramModernRoboticsRequest.getPayloadLength();
        } 
        if (i != modernRoboticsResponse.getPayloadLength()) {
          String str;
          if (paramModernRoboticsRequest.isWrite()) {
            str = "comm payload error write";
          } else {
            str = "comm payload error read";
          } 
          logAndThrowProtocol(paramModernRoboticsRequest, modernRoboticsResponse, str);
        } 
        ModernRoboticsResponse modernRoboticsResponse1 = ModernRoboticsResponse.newInstance(this.responseAllocationContext, modernRoboticsResponse.getPayloadLength());
        System.arraycopy(modernRoboticsResponse.data, 0, modernRoboticsResponse1.data, 0, modernRoboticsResponse.data.length);
        readIncomingBytes(modernRoboticsResponse1.data, modernRoboticsResponse.data.length, modernRoboticsResponse.getPayloadLength(), 0, paramTimeWindow, "payload");
        this.isSynchronized = true;
        return modernRoboticsResponse1;
      } finally {
        if (modernRoboticsResponse != null)
          modernRoboticsResponse.close(); 
      } 
      SYNTHETIC_LOCAL_VARIABLE_6.close();
    } 
    throw new RobotUsbTimeoutException(this.responseDeadline.startTimeNanoseconds(), "timeout waiting %d ms for response", new Object[] { Long.valueOf(this.responseDeadline.getDuration(TimeUnit.MILLISECONDS)) });
  }
  
  protected byte readSingleByte(byte[] paramArrayOfbyte, int paramInt, TimeWindow paramTimeWindow, String paramString) throws RobotUsbException, InterruptedException {
    readIncomingBytes(paramArrayOfbyte, 0, 1, paramInt, paramTimeWindow, paramString);
    return paramArrayOfbyte[0];
  }
  
  public void throwIfTooManySequentialCommErrors() throws RobotUsbTooManySequentialErrorsException {
    if (this.device.isOpen()) {
      int i = this.usbSequentialCommReadErrorCount;
      int j = MAX_SEQUENTIAL_USB_ERROR_COUNT;
      if (i <= j && this.usbSequentialCommWriteErrorCount <= j)
        return; 
      throw new RobotUsbTooManySequentialErrorsException("%s: too many sequential USB comm errors on device", new Object[] { this.device.getSerialNumber() });
    } 
  }
  
  protected String timeoutMessage(String paramString, RobotUsbTimeoutException paramRobotUsbTimeoutException) {
    return String.format("%s: %s", new Object[] { paramString, paramRobotUsbTimeoutException.getMessage() });
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) throws RobotUsbException, InterruptedException {
    if (DEBUG)
      RobotLog.vv("MRReaderWriter", "%s: write(addr=%d cb=%d)", new Object[] { this.device.getSerialNumber(), Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfbyte.length) }); 
    robotUsbException = null;
    int i = 0;
    while (i < this.usbWriteRetryCount) {
      if (i > 0)
        RobotLog.ee("MRReaderWriter", "%s: retry #%d write(addr=%d cb=%d)", new Object[] { this.device.getSerialNumber(), Integer.valueOf(i), Integer.valueOf(paramInt), Integer.valueOf(paramArrayOfbyte.length) }); 
      try {
        writeOnce(paramInt, paramArrayOfbyte);
        return;
      } catch (RobotUsbException robotUsbException) {
        if (!this.device.isOpen())
          return; 
        Thread.sleep(this.msUsbWriteRetryInterval);
        this.device.resetAndFlushBuffers();
        i++;
      } 
    } 
    if (robotUsbException == null)
      return; 
    throw robotUsbException;
  }
  
  protected void writeOnce(int paramInt, byte[] paramArrayOfbyte) throws RobotUsbException, InterruptedException {
    // Byte code:
    //   0: aconst_null
    //   1: astore #5
    //   3: aconst_null
    //   4: astore #7
    //   6: aconst_null
    //   7: astore_3
    //   8: aconst_null
    //   9: astore #4
    //   11: aconst_null
    //   12: astore #8
    //   14: aload_0
    //   15: getfield requestAllocationContext : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsDatagram$AllocationContext;
    //   18: aload_2
    //   19: arraylength
    //   20: invokestatic newInstance : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsDatagram$AllocationContext;I)Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;
    //   23: astore #6
    //   25: aload #5
    //   27: astore_3
    //   28: aload #7
    //   30: astore #4
    //   32: aload #6
    //   34: iconst_0
    //   35: invokevirtual setWrite : (I)V
    //   38: aload #5
    //   40: astore_3
    //   41: aload #7
    //   43: astore #4
    //   45: aload #6
    //   47: iload_1
    //   48: invokevirtual setAddress : (I)V
    //   51: aload #5
    //   53: astore_3
    //   54: aload #7
    //   56: astore #4
    //   58: aload #6
    //   60: aload_2
    //   61: invokevirtual setPayload : ([B)V
    //   64: aload #5
    //   66: astore_3
    //   67: aload #7
    //   69: astore #4
    //   71: aload_0
    //   72: getfield device : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
    //   75: aload #6
    //   77: getfield data : [B
    //   80: invokeinterface write : ([B)V
    //   85: aload #8
    //   87: astore_2
    //   88: aload #5
    //   90: astore_3
    //   91: aload #7
    //   93: astore #4
    //   95: aload_0
    //   96: aload #6
    //   98: aconst_null
    //   99: invokevirtual readResponse : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lorg/firstinspires/ftc/robotcore/internal/hardware/TimeWindow;)Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;
    //   102: astore #5
    //   104: aload #5
    //   106: astore_2
    //   107: aload #5
    //   109: astore_3
    //   110: aload #5
    //   112: astore #4
    //   114: aload #5
    //   116: invokevirtual isFailure : ()Z
    //   119: ifeq -> 165
    //   122: aload #5
    //   124: astore_2
    //   125: aload #5
    //   127: astore_3
    //   128: aload #5
    //   130: astore #4
    //   132: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_FAILURE_WAIT : I
    //   135: i2l
    //   136: invokestatic sleep : (J)V
    //   139: aload #5
    //   141: astore_2
    //   142: aload #5
    //   144: astore_3
    //   145: aload #5
    //   147: astore #4
    //   149: aload_0
    //   150: aload #6
    //   152: aload #5
    //   154: ldc 'comm failure write'
    //   156: invokevirtual logAndThrowProtocol : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;Ljava/lang/String;)V
    //   159: aload #5
    //   161: astore_2
    //   162: goto -> 337
    //   165: aload #5
    //   167: astore_2
    //   168: aload #5
    //   170: astore_3
    //   171: aload #5
    //   173: astore #4
    //   175: aload #5
    //   177: invokevirtual isWrite : ()Z
    //   180: ifeq -> 259
    //   183: aload #5
    //   185: astore_2
    //   186: aload #5
    //   188: astore_3
    //   189: aload #5
    //   191: astore #4
    //   193: aload #5
    //   195: invokevirtual getFunction : ()I
    //   198: ifne -> 259
    //   201: aload #5
    //   203: astore_2
    //   204: aload #5
    //   206: astore_3
    //   207: aload #5
    //   209: astore #4
    //   211: aload #5
    //   213: invokevirtual getAddress : ()I
    //   216: iload_1
    //   217: if_icmpne -> 259
    //   220: aload #5
    //   222: astore_2
    //   223: aload #5
    //   225: astore_3
    //   226: aload #5
    //   228: astore #4
    //   230: aload #5
    //   232: invokevirtual getPayloadLength : ()I
    //   235: ifne -> 259
    //   238: aload #5
    //   240: astore_2
    //   241: aload #5
    //   243: astore_3
    //   244: aload #5
    //   246: astore #4
    //   248: aload_0
    //   249: iconst_0
    //   250: putfield usbSequentialCommWriteErrorCount : I
    //   253: aload #5
    //   255: astore_2
    //   256: goto -> 337
    //   259: aload #5
    //   261: astore_2
    //   262: aload #5
    //   264: astore_3
    //   265: aload #5
    //   267: astore #4
    //   269: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_COMM_ERROR_WAIT : I
    //   272: i2l
    //   273: invokestatic sleep : (J)V
    //   276: aload #5
    //   278: astore_2
    //   279: aload #5
    //   281: astore_3
    //   282: aload #5
    //   284: astore #4
    //   286: aload_0
    //   287: aload #6
    //   289: aload #5
    //   291: ldc 'comm error write'
    //   293: invokevirtual logAndThrowProtocol : (Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsResponse;Ljava/lang/String;)V
    //   296: aload #5
    //   298: astore_2
    //   299: goto -> 337
    //   302: astore #5
    //   304: aload_2
    //   305: astore_3
    //   306: aload_2
    //   307: astore #4
    //   309: getstatic com/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter.MS_COMM_ERROR_WAIT : I
    //   312: i2l
    //   313: invokestatic sleep : (J)V
    //   316: aload_2
    //   317: astore_3
    //   318: aload_2
    //   319: astore #4
    //   321: aload_0
    //   322: aload #5
    //   324: aload #6
    //   326: aload_0
    //   327: ldc 'comm timeout awaiting response (write)'
    //   329: aload #5
    //   331: invokevirtual timeoutMessage : (Ljava/lang/String;Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException;)Ljava/lang/String;
    //   334: invokevirtual logAndRethrowTimeout : (Lorg/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException;Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsRequest;Ljava/lang/String;)V
    //   337: aload_2
    //   338: ifnull -> 345
    //   341: aload_2
    //   342: invokevirtual close : ()V
    //   345: aload #6
    //   347: ifnull -> 355
    //   350: aload #6
    //   352: invokevirtual close : ()V
    //   355: return
    //   356: astore #4
    //   358: aload #6
    //   360: astore_2
    //   361: goto -> 420
    //   364: astore #5
    //   366: aload #4
    //   368: astore_2
    //   369: aload #6
    //   371: astore_3
    //   372: aload #5
    //   374: astore #4
    //   376: goto -> 397
    //   379: astore #5
    //   381: aconst_null
    //   382: astore_2
    //   383: aload #4
    //   385: astore_3
    //   386: aload #5
    //   388: astore #4
    //   390: goto -> 420
    //   393: astore #4
    //   395: aconst_null
    //   396: astore_2
    //   397: aload_0
    //   398: aload_0
    //   399: getfield usbSequentialCommWriteErrorCount : I
    //   402: iconst_1
    //   403: iadd
    //   404: putfield usbSequentialCommWriteErrorCount : I
    //   407: aload #4
    //   409: athrow
    //   410: astore #4
    //   412: aload_2
    //   413: astore #5
    //   415: aload_3
    //   416: astore_2
    //   417: aload #5
    //   419: astore_3
    //   420: aload_3
    //   421: ifnull -> 428
    //   424: aload_3
    //   425: invokevirtual close : ()V
    //   428: aload_2
    //   429: ifnull -> 436
    //   432: aload_2
    //   433: invokevirtual close : ()V
    //   436: aload #4
    //   438: athrow
    // Exception table:
    //   from	to	target	type
    //   14	25	393	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   14	25	379	finally
    //   32	38	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   32	38	356	finally
    //   45	51	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   45	51	356	finally
    //   58	64	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   58	64	356	finally
    //   71	85	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   71	85	356	finally
    //   95	104	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   95	104	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   95	104	356	finally
    //   114	122	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   114	122	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   114	122	356	finally
    //   132	139	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   132	139	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   132	139	356	finally
    //   149	159	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   149	159	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   149	159	356	finally
    //   175	183	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   175	183	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   175	183	356	finally
    //   193	201	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   193	201	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   193	201	356	finally
    //   211	220	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   211	220	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   211	220	356	finally
    //   230	238	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   230	238	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   230	238	356	finally
    //   248	253	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   248	253	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   248	253	356	finally
    //   269	276	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   269	276	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   269	276	356	finally
    //   286	296	302	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
    //   286	296	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   286	296	356	finally
    //   309	316	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   309	316	356	finally
    //   321	337	364	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
    //   321	337	356	finally
    //   397	410	410	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ModernRoboticsReaderWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */