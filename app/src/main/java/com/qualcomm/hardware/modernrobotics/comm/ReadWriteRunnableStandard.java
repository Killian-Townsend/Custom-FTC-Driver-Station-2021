package com.qualcomm.hardware.modernrobotics.comm;

import android.content.Context;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbProtocolException;

public class ReadWriteRunnableStandard implements ReadWriteRunnable {
  protected static boolean DEBUG_SEGMENTS = false;
  
  public static final String TAG = "ReadWriteRunnable";
  
  protected volatile boolean acceptingWrites = false;
  
  protected final Object acceptingWritesLock = new Object();
  
  protected byte[] activeBuffer;
  
  protected TimeWindow activeBufferTimeWindow;
  
  protected ReadWriteRunnable.Callback callback;
  
  protected final Context context;
  
  protected final boolean debugLogging;
  
  protected volatile boolean fullWriteNeeded;
  
  protected int ibActiveFirst;
  
  protected final byte[] localDeviceReadCache = new byte[256];
  
  protected final byte[] localDeviceWriteCache = new byte[256];
  
  protected int monitorLength;
  
  protected RobotUsbModule owner;
  
  protected boolean pruneBufferAfterRead;
  
  protected final Object readSupressionLock = new Object();
  
  protected RobotUsbDevice robotUsbDevice;
  
  protected volatile boolean running = false;
  
  protected CountDownLatch runningInterlock = new CountDownLatch(1);
  
  protected ConcurrentLinkedQueue<Integer> segmentReadQueue = new ConcurrentLinkedQueue<Integer>();
  
  protected ConcurrentLinkedQueue<Integer> segmentWriteQueue = new ConcurrentLinkedQueue<Integer>();
  
  protected Map<Integer, ReadWriteRunnableSegment> segments = new HashMap<Integer, ReadWriteRunnableSegment>();
  
  protected final SerialNumber serialNumber;
  
  protected volatile boolean shutdownComplete = false;
  
  protected volatile SyncdDevice.ShutdownReason shutdownReason = SyncdDevice.ShutdownReason.NORMAL;
  
  protected int startAddress;
  
  protected volatile boolean suppressReads = false;
  
  protected ModernRoboticsReaderWriter usbHandler;
  
  private volatile boolean writeNeeded = false;
  
  public ReadWriteRunnableStandard(Context paramContext, SerialNumber paramSerialNumber, RobotUsbDevice paramRobotUsbDevice, int paramInt1, int paramInt2, boolean paramBoolean) {
    this.context = paramContext;
    this.serialNumber = paramSerialNumber;
    this.startAddress = paramInt2;
    this.monitorLength = paramInt1;
    this.fullWriteNeeded = false;
    this.pruneBufferAfterRead = true;
    this.debugLogging = paramBoolean;
    this.callback = new ReadWriteRunnable.EmptyCallback();
    this.owner = null;
    this.robotUsbDevice = paramRobotUsbDevice;
    this.usbHandler = new ModernRoboticsReaderWriter(paramRobotUsbDevice);
  }
  
  protected void awaitRunning() {
    try {
      this.runningInterlock.await();
      return;
    } catch (InterruptedException interruptedException) {
      while (this.runningInterlock.getCount() != 0L)
        Thread.yield(); 
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield running : Z
    //   6: ifeq -> 37
    //   9: aload_0
    //   10: getfield robotUsbDevice : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
    //   13: iconst_1
    //   14: invokeinterface requestReadInterrupt : (Z)V
    //   19: aload_0
    //   20: iconst_0
    //   21: putfield running : Z
    //   24: aload_0
    //   25: getfield shutdownComplete : Z
    //   28: ifne -> 37
    //   31: invokestatic yield : ()V
    //   34: goto -> 24
    //   37: aload_0
    //   38: monitorexit
    //   39: return
    //   40: astore_1
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_1
    //   44: athrow
    // Exception table:
    //   from	to	target	type
    //   2	24	40	finally
    //   24	34	40	finally
    //   37	39	40	finally
    //   41	43	40	finally
  }
  
  public ReadWriteRunnableSegment createSegment(int paramInt1, int paramInt2, int paramInt3) {
    ReadWriteRunnableSegment readWriteRunnableSegment = new ReadWriteRunnableSegment(paramInt1, paramInt2, paramInt3);
    this.segments.put(Integer.valueOf(paramInt1), readWriteRunnableSegment);
    return readWriteRunnableSegment;
  }
  
  public void destroySegment(int paramInt) {
    this.segments.remove(Integer.valueOf(paramInt));
  }
  
  protected void doReadCycle() throws InterruptedException, RobotUsbException {
    synchronized (this.readSupressionLock) {
      boolean bool = this.suppressReads;
      if (!bool) {
        try {
          ModernRoboticsReaderWriter modernRoboticsReaderWriter = this.usbHandler;
          if (!isFullActive()) {
            bool = true;
          } else {
            bool = false;
          } 
          modernRoboticsReaderWriter.read(bool, this.ibActiveFirst, this.activeBuffer, this.activeBufferTimeWindow);
          while (!this.segmentReadQueue.isEmpty()) {
            ReadWriteRunnableSegment readWriteRunnableSegment = this.segments.get(this.segmentReadQueue.remove());
            null = new byte[(readWriteRunnableSegment.getReadBuffer()).length];
            this.usbHandler.read(readWriteRunnableSegment.getRetryOnReadFailure(), readWriteRunnableSegment.getAddress(), null, readWriteRunnableSegment.getTimeWindow());
            try {
              readWriteRunnableSegment.getReadLock().lock();
              System.arraycopy(null, 0, readWriteRunnableSegment.getReadBuffer(), 0, (readWriteRunnableSegment.getReadBuffer()).length);
              if (DEBUG_SEGMENTS) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("segment ");
                stringBuilder.append(readWriteRunnableSegment.getAddress());
                stringBuilder.append(" read");
                dumpBuffers(stringBuilder.toString(), null);
              } 
            } finally {
              readWriteRunnableSegment.getReadLock().unlock();
            } 
          } 
        } catch (RobotUsbProtocolException robotUsbProtocolException) {
          RobotLog.w(String.format("could not read %s: %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber), robotUsbProtocolException.getMessage() }));
        } 
        synchronized (this.localDeviceReadCache) {
          System.arraycopy(this.activeBuffer, 0, this.localDeviceReadCache, this.ibActiveFirst, this.activeBuffer.length);
        } 
      } 
      if (this.debugLogging)
        dumpBuffers("read", this.localDeviceReadCache); 
      this.callback.readComplete();
      return;
    } 
  }
  
  protected void doWriteCycle() throws InterruptedException, RobotUsbException {
    byte[] arrayOfByte;
    synchronized (this.localDeviceWriteCache) {
      if (this.fullWriteNeeded) {
        setFullActive();
        this.fullWriteNeeded = false;
        this.pruneBufferAfterRead = true;
      } else if (this.pruneBufferAfterRead) {
        setSuffixActive();
        this.pruneBufferAfterRead = false;
      } 
      System.arraycopy(this.localDeviceWriteCache, this.ibActiveFirst, this.activeBuffer, 0, this.activeBuffer.length);
      try {
        if (writeNeeded()) {
          this.usbHandler.write(this.ibActiveFirst, this.activeBuffer);
          resetWriteNeeded();
        } 
        while (!this.segmentWriteQueue.isEmpty()) {
          ReadWriteRunnableSegment readWriteRunnableSegment = this.segments.get(this.segmentWriteQueue.remove());
          try {
            readWriteRunnableSegment.getWriteLock().lock();
            byte[] arrayOfByte1 = Arrays.copyOf(readWriteRunnableSegment.getWriteBuffer(), (readWriteRunnableSegment.getWriteBuffer()).length);
            readWriteRunnableSegment.getWriteLock().unlock();
          } finally {
            readWriteRunnableSegment.getWriteLock().unlock();
          } 
        } 
      } catch (RobotUsbProtocolException robotUsbProtocolException) {
        RobotLog.w(String.format("could not write to %s: %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber), robotUsbProtocolException.getMessage() }));
      } 
      if (this.debugLogging)
        dumpBuffers("write", this.localDeviceWriteCache); 
      this.callback.writeComplete();
      return;
    } 
  }
  
  public void drainPendingWrites() {
    while (this.running && hasPendingWrites())
      Thread.yield(); 
  }
  
  protected void dumpBuffers(String paramString, byte[] paramArrayOfbyte) {
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Dumping ");
    stringBuilder2.append(paramString);
    stringBuilder2.append(" buffers for ");
    stringBuilder2.append(this.serialNumber);
    RobotLog.v(stringBuilder2.toString());
    StringBuilder stringBuilder1 = new StringBuilder(1024);
    int i = 0;
    while (i < this.startAddress + this.monitorLength) {
      stringBuilder1.append(String.format(" %02x", new Object[] { Integer.valueOf(TypeConversion.unsignedByteToInt(paramArrayOfbyte[i])) }));
      int j = i + 1;
      i = j;
      if (j % 16 == 0) {
        stringBuilder1.append("\n");
        i = j;
      } 
    } 
    RobotLog.v(stringBuilder1.toString());
  }
  
  public void executeUsing(ExecutorService paramExecutorService) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: aload_0
    //   4: invokeinterface execute : (Ljava/lang/Runnable;)V
    //   9: aload_0
    //   10: invokevirtual awaitRunning : ()V
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	16	finally
    //   17	19	16	finally
  }
  
  public boolean getAcceptingWrites() {
    return this.acceptingWrites;
  }
  
  public RobotUsbModule getOwner() {
    return this.owner;
  }
  
  public ReadWriteRunnableSegment getSegment(int paramInt) {
    return this.segments.get(Integer.valueOf(paramInt));
  }
  
  public SyncdDevice.ShutdownReason getShutdownReason() {
    return this.shutdownReason;
  }
  
  boolean hasPendingWrites() {
    return (this.writeNeeded || !this.segmentWriteQueue.isEmpty());
  }
  
  protected boolean isFullActive() {
    return (this.ibActiveFirst == 0 && this.startAddress > 0);
  }
  
  protected void queueIfNotAlreadyQueued(int paramInt, ConcurrentLinkedQueue<Integer> paramConcurrentLinkedQueue) {
    if (!paramConcurrentLinkedQueue.contains(Integer.valueOf(paramInt)))
      paramConcurrentLinkedQueue.add(Integer.valueOf(paramInt)); 
  }
  
  public void queueSegmentRead(int paramInt) {
    queueIfNotAlreadyQueued(paramInt, this.segmentReadQueue);
  }
  
  public void queueSegmentWrite(int paramInt) {
    synchronized (this.acceptingWritesLock) {
      if (this.acceptingWrites)
        queueIfNotAlreadyQueued(paramInt, this.segmentWriteQueue); 
      return;
    } 
  }
  
  public byte[] read(int paramInt1, int paramInt2) {
    synchronized (this.localDeviceReadCache) {
      return Arrays.copyOfRange(this.localDeviceReadCache, paramInt1, paramInt2 + paramInt1);
    } 
  }
  
  public byte[] readFromWriteCache(int paramInt1, int paramInt2) {
    synchronized (this.localDeviceWriteCache) {
      return Arrays.copyOfRange(this.localDeviceWriteCache, paramInt1, paramInt2 + paramInt1);
    } 
  }
  
  public void resetWriteNeeded() {
    this.writeNeeded = false;
  }
  
  public void run() {
    if (this.shutdownComplete)
      return; 
    ThreadPool.logThreadLifeCycle(String.format("r/w loop: %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) }), new Runnable() {
          public void run() {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   4: iconst_0
            //   5: putfield fullWriteNeeded : Z
            //   8: aload_0
            //   9: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   12: iconst_1
            //   13: putfield pruneBufferAfterRead : Z
            //   16: aload_0
            //   17: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   20: invokevirtual setFullActive : ()V
            //   23: new com/qualcomm/robotcore/util/ElapsedTime
            //   26: dup
            //   27: invokespecial <init> : ()V
            //   30: astore_2
            //   31: new java/lang/StringBuilder
            //   34: dup
            //   35: invokespecial <init> : ()V
            //   38: astore_3
            //   39: aload_3
            //   40: ldc 'Device '
            //   42: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   45: pop
            //   46: aload_3
            //   47: aload_0
            //   48: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   51: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   54: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //   57: pop
            //   58: aload_3
            //   59: invokevirtual toString : ()Ljava/lang/String;
            //   62: astore_3
            //   63: aload_0
            //   64: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   67: iconst_1
            //   68: putfield running : Z
            //   71: aload_0
            //   72: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   75: getfield callback : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnable$Callback;
            //   78: invokeinterface startupComplete : ()V
            //   83: goto -> 94
            //   86: aload_0
            //   87: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   90: iconst_0
            //   91: putfield running : Z
            //   94: aload_0
            //   95: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   98: getfield runningInterlock : Ljava/util/concurrent/CountDownLatch;
            //   101: invokevirtual countDown : ()V
            //   104: aload_0
            //   105: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   108: getfield running : Z
            //   111: ifeq -> 204
            //   114: aload_0
            //   115: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   118: getfield debugLogging : Z
            //   121: ifeq -> 133
            //   124: aload_2
            //   125: aload_3
            //   126: invokevirtual log : (Ljava/lang/String;)V
            //   129: aload_2
            //   130: invokevirtual reset : ()V
            //   133: aload_0
            //   134: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   137: invokevirtual doReadCycle : ()V
            //   140: aload_0
            //   141: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   144: invokevirtual doWriteCycle : ()V
            //   147: aload_0
            //   148: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   151: getfield usbHandler : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter;
            //   154: invokevirtual throwIfTooManySequentialCommErrors : ()V
            //   157: aload_0
            //   158: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   161: getfield robotUsbDevice : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
            //   164: invokeinterface isOpen : ()Z
            //   169: ifeq -> 175
            //   172: goto -> 104
            //   175: new org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbDeviceClosedException
            //   178: dup
            //   179: ldc '%s: closed'
            //   181: iconst_1
            //   182: anewarray java/lang/Object
            //   185: dup
            //   186: iconst_0
            //   187: aload_0
            //   188: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   191: getfield robotUsbDevice : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
            //   194: invokeinterface getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
            //   199: aastore
            //   200: invokespecial <init> : (Ljava/lang/String;[Ljava/lang/Object;)V
            //   203: athrow
            //   204: aload_0
            //   205: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   208: getfield usbHandler : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter;
            //   211: invokevirtual close : ()V
            //   214: aload_0
            //   215: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   218: iconst_0
            //   219: putfield running : Z
            //   222: aload_0
            //   223: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   226: getfield callback : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnable$Callback;
            //   229: astore_2
            //   230: aload_2
            //   231: invokeinterface shutdownComplete : ()V
            //   236: aload_0
            //   237: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   240: iconst_1
            //   241: putfield shutdownComplete : Z
            //   244: return
            //   245: astore_2
            //   246: goto -> 502
            //   249: astore_2
            //   250: goto -> 254
            //   253: astore_2
            //   254: aload_2
            //   255: invokevirtual getClass : ()Ljava/lang/Class;
            //   258: ldc org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbDeviceClosedException
            //   260: if_acmpeq -> 295
            //   263: ldc 'ReadWriteRunnable'
            //   265: aload_2
            //   266: ldc 'exception while communicating with %s'
            //   268: iconst_1
            //   269: anewarray java/lang/Object
            //   272: dup
            //   273: iconst_0
            //   274: aload_0
            //   275: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   278: getfield context : Landroid/content/Context;
            //   281: aload_0
            //   282: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   285: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   288: invokestatic getDeviceDisplayName : (Landroid/content/Context;Lcom/qualcomm/robotcore/util/SerialNumber;)Ljava/lang/String;
            //   291: aastore
            //   292: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)V
            //   295: aload_0
            //   296: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   299: getfield context : Landroid/content/Context;
            //   302: astore_3
            //   303: aload_0
            //   304: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   307: getfield robotUsbDevice : Lcom/qualcomm/robotcore/hardware/usb/RobotUsbDevice;
            //   310: invokeinterface isAttached : ()Z
            //   315: ifeq -> 325
            //   318: getstatic com/qualcomm/hardware/R$string.warningProblemCommunicatingWithUSBDevice : I
            //   321: istore_1
            //   322: goto -> 329
            //   325: getstatic com/qualcomm/hardware/R$string.warningUSBDeviceDetached : I
            //   328: istore_1
            //   329: aload_3
            //   330: iload_1
            //   331: invokevirtual getString : (I)Ljava/lang/String;
            //   334: astore_3
            //   335: aload_0
            //   336: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   339: aload_3
            //   340: iconst_1
            //   341: anewarray java/lang/Object
            //   344: dup
            //   345: iconst_0
            //   346: aload_0
            //   347: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   350: getfield context : Landroid/content/Context;
            //   353: aload_0
            //   354: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   357: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   360: invokestatic getDeviceDisplayName : (Landroid/content/Context;Lcom/qualcomm/robotcore/util/SerialNumber;)Ljava/lang/String;
            //   363: aastore
            //   364: invokevirtual setOwnerWarningMessage : (Ljava/lang/String;[Ljava/lang/Object;)V
            //   367: aload_2
            //   368: invokevirtual getClass : ()Ljava/lang/Class;
            //   371: ldc org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbTimeoutException
            //   373: if_acmpeq -> 401
            //   376: aload_2
            //   377: invokevirtual getClass : ()Ljava/lang/Class;
            //   380: ldc org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbStuckUsbWriteException
            //   382: if_acmpne -> 388
            //   385: goto -> 401
            //   388: aload_0
            //   389: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   392: getstatic com/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason.ABNORMAL : Lcom/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason;
            //   395: putfield shutdownReason : Lcom/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason;
            //   398: goto -> 411
            //   401: aload_0
            //   402: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   405: getstatic com/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason.ABNORMAL_ATTEMPT_REOPEN : Lcom/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason;
            //   408: putfield shutdownReason : Lcom/qualcomm/robotcore/eventloop/SyncdDevice$ShutdownReason;
            //   411: aload_0
            //   412: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   415: getfield usbHandler : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter;
            //   418: invokevirtual close : ()V
            //   421: aload_0
            //   422: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   425: iconst_0
            //   426: putfield running : Z
            //   429: aload_0
            //   430: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   433: getfield callback : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnable$Callback;
            //   436: astore_2
            //   437: goto -> 230
            //   440: astore_2
            //   441: ldc 'ReadWriteRunnable'
            //   443: aload_2
            //   444: ldc 'thread interrupt while communicating with %s'
            //   446: iconst_1
            //   447: anewarray java/lang/Object
            //   450: dup
            //   451: iconst_0
            //   452: aload_0
            //   453: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   456: getfield context : Landroid/content/Context;
            //   459: aload_0
            //   460: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   463: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   466: invokestatic getDeviceDisplayName : (Landroid/content/Context;Lcom/qualcomm/robotcore/util/SerialNumber;)Ljava/lang/String;
            //   469: aastore
            //   470: invokestatic logExceptionHeader : (Ljava/lang/String;Ljava/lang/Exception;Ljava/lang/String;[Ljava/lang/Object;)V
            //   473: aload_0
            //   474: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   477: getfield usbHandler : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter;
            //   480: invokevirtual close : ()V
            //   483: aload_0
            //   484: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   487: iconst_0
            //   488: putfield running : Z
            //   491: aload_0
            //   492: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   495: getfield callback : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnable$Callback;
            //   498: astore_2
            //   499: goto -> 230
            //   502: aload_0
            //   503: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   506: getfield usbHandler : Lcom/qualcomm/hardware/modernrobotics/comm/ModernRoboticsReaderWriter;
            //   509: invokevirtual close : ()V
            //   512: aload_0
            //   513: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   516: iconst_0
            //   517: putfield running : Z
            //   520: aload_0
            //   521: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   524: getfield callback : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnable$Callback;
            //   527: invokeinterface shutdownComplete : ()V
            //   532: aload_0
            //   533: getfield this$0 : Lcom/qualcomm/hardware/modernrobotics/comm/ReadWriteRunnableStandard;
            //   536: iconst_1
            //   537: putfield shutdownComplete : Z
            //   540: aload_2
            //   541: athrow
            //   542: astore #4
            //   544: goto -> 86
            //   547: astore_2
            //   548: goto -> 236
            //   551: astore_3
            //   552: goto -> 532
            // Exception table:
            //   from	to	target	type
            //   71	83	542	java/lang/InterruptedException
            //   104	133	440	java/lang/InterruptedException
            //   104	133	253	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
            //   104	133	249	java/lang/RuntimeException
            //   104	133	245	finally
            //   133	172	440	java/lang/InterruptedException
            //   133	172	253	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
            //   133	172	249	java/lang/RuntimeException
            //   133	172	245	finally
            //   175	204	440	java/lang/InterruptedException
            //   175	204	253	org/firstinspires/ftc/robotcore/internal/usb/exception/RobotUsbException
            //   175	204	249	java/lang/RuntimeException
            //   175	204	245	finally
            //   222	230	547	java/lang/InterruptedException
            //   230	236	547	java/lang/InterruptedException
            //   254	295	245	finally
            //   295	322	245	finally
            //   325	329	245	finally
            //   329	385	245	finally
            //   388	398	245	finally
            //   401	411	245	finally
            //   429	437	547	java/lang/InterruptedException
            //   441	473	245	finally
            //   491	499	547	java/lang/InterruptedException
            //   520	532	551	java/lang/InterruptedException
          }
        });
  }
  
  public void setAcceptingWrites(boolean paramBoolean) {
    synchronized (this.acceptingWritesLock) {
      this.acceptingWrites = paramBoolean;
      return;
    } 
  }
  
  public void setCallback(ReadWriteRunnable.Callback paramCallback) {
    this.callback = paramCallback;
  }
  
  protected void setFullActive() {
    this.ibActiveFirst = 0;
    this.activeBuffer = new byte[this.monitorLength + this.startAddress];
    this.activeBufferTimeWindow = new TimeWindow();
  }
  
  public void setOwner(RobotUsbModule paramRobotUsbModule) {
    this.owner = paramRobotUsbModule;
  }
  
  void setOwnerWarningMessage(String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    RobotUsbModule robotUsbModule = this.owner;
    if (robotUsbModule != null && robotUsbModule instanceof GlobalWarningSource) {
      ((GlobalWarningSource)robotUsbModule).setGlobalWarning(paramString);
      return;
    } 
    RobotLog.setGlobalWarningMessage(paramString);
  }
  
  protected void setSuffixActive() {
    this.ibActiveFirst = this.startAddress;
    this.activeBuffer = new byte[this.monitorLength];
    this.activeBufferTimeWindow = new TimeWindow();
  }
  
  public void suppressReads(boolean paramBoolean) {
    synchronized (this.readSupressionLock) {
      this.suppressReads = paramBoolean;
      return;
    } 
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    synchronized (this.acceptingWritesLock) {
      if (this.acceptingWrites)
        synchronized (this.localDeviceWriteCache) {
          System.arraycopy(paramArrayOfbyte, 0, this.localDeviceWriteCache, paramInt, paramArrayOfbyte.length);
          this.writeNeeded = true;
          if (paramInt < this.startAddress)
            this.fullWriteNeeded = true; 
        }  
      return;
    } 
  }
  
  public boolean writeNeeded() {
    return this.writeNeeded;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\comm\ReadWriteRunnableStandard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */