package com.qualcomm.robotcore.hardware;

import android.util.Log;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public final class I2cDeviceSynchImpl extends I2cDeviceSynchReadHistoryImpl implements I2cDeviceSynch, Engagable {
  protected static final int dibCacheOverhead = 4;
  
  protected static final int msCallbackLockAbandon = 500;
  
  protected static final int msCallbackLockWaitQuantum = 60;
  
  protected Callback callback;
  
  protected final Object callbackLock = new Object();
  
  protected final Object concurrentClientLock = new Object();
  
  protected I2cController controller;
  
  protected volatile CONTROLLER_PORT_MODE controllerPortMode;
  
  protected volatile int cregWrite;
  
  protected boolean disableReadWindows;
  
  protected final Object engagementLock = new Object();
  
  protected HardwareDeviceHealthImpl hardwareDeviceHealth;
  
  protected volatile boolean hasReadWindowChanged;
  
  protected volatile I2cDeviceSynch.HeartbeatAction heartbeatAction;
  
  protected volatile ExecutorService heartbeatExecutor;
  
  protected I2cAddr i2cAddr;
  
  protected I2cDevice i2cDevice;
  
  protected volatile int iregWriteFirst;
  
  protected boolean isClosing;
  
  protected boolean isControllerLegacy;
  
  protected boolean isEngaged;
  
  protected boolean isHooked;
  
  protected boolean isI2cDeviceOwned;
  
  protected volatile boolean isReadWindowSentToControllerInitialized;
  
  protected boolean isWriteCoalescingEnabled;
  
  protected boolean loggingEnabled;
  
  protected String loggingTag;
  
  protected volatile int msHeartbeatInterval;
  
  protected String name;
  
  protected volatile long nanoTimeReadCacheValid;
  
  protected byte[] readCache;
  
  protected Lock readCacheLock;
  
  protected volatile READ_CACHE_STATUS readCacheStatus;
  
  protected TimeWindow readCacheTimeWindow;
  
  protected volatile I2cDeviceSynch.ReadWindow readWindow;
  
  protected volatile I2cDeviceSynch.ReadWindow readWindowActuallyRead;
  
  protected volatile I2cDeviceSynch.ReadWindow readWindowSentToController;
  
  protected AtomicInteger readerWriterCount;
  
  protected ReadWriteLock readerWriterGate;
  
  protected AtomicInteger readerWriterPreventionCount;
  
  protected RobotUsbModule robotUsbModule;
  
  protected ElapsedTime timeSinceLastHeartbeat;
  
  protected byte[] writeCache;
  
  protected Lock writeCacheLock;
  
  protected final WriteCacheStatus writeCacheStatus;
  
  public I2cDeviceSynchImpl(I2cDevice paramI2cDevice, I2cAddr paramI2cAddr, boolean paramBoolean) {
    this.loggingTag = String.format("%s:i2cSynch(%s)", new Object[] { "RobotCore", paramI2cDevice.getConnectionInfo() });
    this.i2cAddr = paramI2cAddr;
    this.i2cDevice = paramI2cDevice;
    this.isI2cDeviceOwned = paramBoolean;
    I2cController i2cController2 = paramI2cDevice.getI2cController();
    this.controller = i2cController2;
    this.isControllerLegacy = i2cController2 instanceof LegacyModule;
    this.hardwareDeviceHealth = new HardwareDeviceHealthImpl(this.loggingTag);
    this.isEngaged = false;
    this.isClosing = false;
    this.isHooked = false;
    this.readerWriterPreventionCount = new AtomicInteger(0);
    this.readerWriterGate = new ReentrantReadWriteLock();
    this.readerWriterCount = new AtomicInteger(0);
    this.callback = new Callback();
    ElapsedTime elapsedTime = new ElapsedTime();
    this.timeSinceLastHeartbeat = elapsedTime;
    elapsedTime.reset();
    this.msHeartbeatInterval = 0;
    this.heartbeatAction = null;
    this.heartbeatExecutor = null;
    this.isWriteCoalescingEnabled = false;
    this.loggingEnabled = false;
    this.disableReadWindows = false;
    this.readWindow = null;
    this.writeCacheStatus = new WriteCacheStatus();
    I2cController i2cController1 = this.controller;
    if (i2cController1 instanceof RobotUsbModule) {
      RobotUsbModule robotUsbModule = (RobotUsbModule)i2cController1;
      this.robotUsbModule = robotUsbModule;
      robotUsbModule.registerCallback(this.callback, false);
      this.i2cDevice.registerForPortReadyBeginEndCallback(this.callback);
      return;
    } 
    throw new IllegalArgumentException("I2cController must also be a RobotUsbModule");
  }
  
  public I2cDeviceSynchImpl(I2cDevice paramI2cDevice, boolean paramBoolean) {
    this(paramI2cDevice, I2cAddr.zero(), paramBoolean);
  }
  
  protected static byte[] concatenateByteArrays(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    byte[] arrayOfByte = new byte[paramArrayOfbyte1.length + paramArrayOfbyte2.length];
    System.arraycopy(paramArrayOfbyte1, 0, arrayOfByte, 0, paramArrayOfbyte1.length);
    System.arraycopy(paramArrayOfbyte2, 0, arrayOfByte, paramArrayOfbyte1.length, paramArrayOfbyte2.length);
    return arrayOfByte;
  }
  
  protected void acquireReaderLockShared() throws InterruptedException {
    this.readerWriterGate.readLock().lockInterruptibly();
    this.readerWriterCount.incrementAndGet();
  }
  
  protected void adjustHooking() {
    synchronized (this.engagementLock) {
      if (!this.isHooked && this.isEngaged) {
        hook();
      } else if (this.isHooked && !this.isEngaged) {
        unhook();
      } 
      return;
    } 
  }
  
  protected void assignReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow) {
    this.readWindow = paramReadWindow;
    this.hasReadWindowChanged = true;
  }
  
  void attachToController() {
    this.readCacheTimeWindow = this.i2cDevice.getI2cReadCacheTimeWindow();
    this.readCache = this.i2cDevice.getI2cReadCache();
    this.readCacheLock = this.i2cDevice.getI2cReadCacheLock();
    this.writeCache = this.i2cDevice.getI2cWriteCache();
    this.writeCacheLock = this.i2cDevice.getI2cWriteCacheLock();
    resetControllerState();
  }
  
  public void close() {
    this.hardwareDeviceHealth.close();
    this.isClosing = true;
    this.i2cDevice.deregisterForPortReadyBeginEndCallback();
    disengage();
    if (this.isI2cDeviceOwned)
      this.i2cDevice.close(); 
  }
  
  protected void disableReadsAndWrites() {
    this.readerWriterPreventionCount.incrementAndGet();
  }
  
  public void disengage() {
    synchronized (this.engagementLock) {
      this.isEngaged = false;
      adjustHooking();
      return;
    } 
  }
  
  protected void enableReadsAndWrites() {
    this.readerWriterPreventionCount.decrementAndGet();
  }
  
  public void enableWriteCoalescing(boolean paramBoolean) {
    synchronized (this.concurrentClientLock) {
      this.isWriteCoalescingEnabled = paramBoolean;
      return;
    } 
  }
  
  public void engage() {
    synchronized (this.engagementLock) {
      this.isEngaged = true;
      adjustHooking();
      return;
    } 
  }
  
  public void ensureReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow1, I2cDeviceSynch.ReadWindow paramReadWindow2) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        if (this.readWindow == null || !this.readWindow.containsWithSameMode(paramReadWindow1))
          setReadWindow(paramReadWindow2); 
        return;
      } 
    } 
  }
  
  protected void forceDrainReadersAndWriters() {
    disableReadsAndWrites();
    boolean bool3 = true;
    boolean bool2 = false;
    boolean bool1 = bool2;
    while (true) {
      synchronized (this.callbackLock) {
        setWriteCacheStatus(WRITE_CACHE_STATUS.IDLE);
        this.readCacheStatus = READ_CACHE_STATUS.VALID_QUEUED;
        this.hasReadWindowChanged = false;
        Assert.assertTrue(readCacheIsValid());
        this.callbackLock.notifyAll();
        if (bool2) {
          if (bool1)
            Thread.currentThread().interrupt(); 
          if (this.readerWriterCount.get() != 0)
            bool3 = false; 
          Assert.assertTrue(bool3);
          enableReadsAndWrites();
          return;
        } 
        try {
          if (this.readerWriterGate.writeLock().tryLock(20L, TimeUnit.MILLISECONDS)) {
            this.readerWriterGate.writeLock().unlock();
            bool2 = true;
          } 
        } catch (InterruptedException null) {
          bool1 = true;
        } 
      } 
    } 
  }
  
  public String getConnectionInfo() {
    return this.i2cDevice.getConnectionInfo();
  }
  
  public String getDeviceName() {
    return this.i2cDevice.getDeviceName();
  }
  
  public HardwareDeviceHealth.HealthStatus getHealthStatus() {
    return this.hardwareDeviceHealth.getHealthStatus();
  }
  
  public I2cDeviceSynch.HeartbeatAction getHeartbeatAction() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.heartbeatAction;
      } 
    } 
  }
  
  public int getHeartbeatInterval() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.msHeartbeatInterval;
      } 
    } 
  }
  
  @Deprecated
  public I2cAddr getI2cAddr() {
    return getI2cAddress();
  }
  
  public I2cAddr getI2cAddress() {
    return this.i2cAddr;
  }
  
  public boolean getLogging() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.loggingEnabled;
      } 
    } 
  }
  
  public String getLoggingTag() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.loggingTag;
      } 
    } 
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public I2cDeviceSynch.ReadWindow getReadWindow() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.readWindow;
      } 
    } 
  }
  
  public String getUserConfiguredName() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        return this.name;
      } 
    } 
  }
  
  public int getVersion() {
    return this.i2cDevice.getVersion();
  }
  
  protected WRITE_CACHE_STATUS getWriteCacheStatus() {
    return this.writeCacheStatus.getStatus();
  }
  
  protected void gracefullyDrainReadersAndWriters() {
    disableReadsAndWrites();
    boolean bool2 = true;
    boolean bool1 = false;
    while (true) {
      try {
        do {
        
        } while (!this.readerWriterGate.writeLock().tryLock(20L, TimeUnit.MILLISECONDS));
        this.readerWriterGate.writeLock().unlock();
        if (bool1)
          Thread.currentThread().interrupt(); 
        if (this.readerWriterCount.get() != 0)
          bool2 = false; 
        Assert.assertTrue(bool2);
        enableReadsAndWrites();
        return;
      } catch (InterruptedException interruptedException) {
        bool1 = true;
      } 
    } 
  }
  
  protected void hook() {
    synchronized (this.engagementLock) {
      if (!this.isHooked) {
        log(2, "hooking ...");
        synchronized (this.callbackLock) {
          this.heartbeatExecutor = ThreadPool.newSingleThreadExecutor("I2cDeviceSyncImpl heartbeat");
          this.i2cDevice.registerForI2cPortReadyCallback(this.callback);
          this.isHooked = true;
          log(2, "... hooking complete");
        } 
      } 
      return;
    } 
  }
  
  protected void initWriteCacheStatus(WRITE_CACHE_STATUS paramWRITE_CACHE_STATUS) {
    this.writeCacheStatus.initStatus(paramWRITE_CACHE_STATUS);
  }
  
  public boolean isArmed() {
    synchronized (this.engagementLock) {
      if (this.isHooked)
        return this.i2cDevice.isArmed(); 
      return false;
    } 
  }
  
  public boolean isEngaged() {
    return this.isEngaged;
  }
  
  protected boolean isOpenForReading() {
    return (this.isHooked && newReadsAndWritesAllowed());
  }
  
  protected boolean isOpenForWriting() {
    return (this.isHooked && newReadsAndWritesAllowed());
  }
  
  public boolean isWriteCoalescingEnabled() {
    synchronized (this.concurrentClientLock) {
      return this.isWriteCoalescingEnabled;
    } 
  }
  
  protected void log(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return;
      case 7:
        Log.wtf(this.loggingTag, paramString);
        return;
      case 6:
        Log.e(this.loggingTag, paramString);
        return;
      case 5:
        Log.w(this.loggingTag, paramString);
        return;
      case 4:
        Log.i(this.loggingTag, paramString);
        return;
      case 3:
        Log.d(this.loggingTag, paramString);
        return;
      case 2:
        break;
    } 
    Log.v(this.loggingTag, paramString);
  }
  
  protected void log(int paramInt, String paramString, Object... paramVarArgs) {
    log(paramInt, String.format(paramString, paramVarArgs));
  }
  
  protected boolean newReadsAndWritesAllowed() {
    return (this.readerWriterPreventionCount.get() == 0);
  }
  
  public byte[] read(int paramInt1, int paramInt2) {
    return (readTimeStamped(paramInt1, paramInt2)).data;
  }
  
  public byte read8(int paramInt) {
    return read(paramInt, 1)[0];
  }
  
  protected boolean readCacheIsValid() {
    return (this.readCacheStatus.isValid() && !this.hasReadWindowChanged);
  }
  
  protected boolean readCacheValidityCurrentOrImminent() {
    return (this.readCacheStatus != READ_CACHE_STATUS.IDLE && !this.hasReadWindowChanged);
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2) {
    try {
      acquireReaderLockShared();
      try {
        if (!isOpenForReading())
          return TimestampedI2cData.makeFakeData((Object)null, getI2cAddress(), paramInt1, paramInt2); 
      } finally {
        releaseReaderLockShared();
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return TimestampedI2cData.makeFakeData((Object)null, getI2cAddress(), paramInt1, paramInt2);
    } 
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2, I2cDeviceSynch.ReadWindow paramReadWindow1, I2cDeviceSynch.ReadWindow paramReadWindow2) {
    ensureReadWindow(paramReadWindow1, paramReadWindow2);
    return readTimeStamped(paramInt1, paramInt2);
  }
  
  protected byte[] readWriteCache() {
    this.writeCacheLock.lock();
    try {
      return Arrays.copyOfRange(this.writeCache, 4, this.cregWrite + 4);
    } finally {
      this.writeCacheLock.unlock();
    } 
  }
  
  protected void releaseReaderLockShared() {
    this.readerWriterCount.decrementAndGet();
    this.readerWriterGate.readLock().unlock();
  }
  
  void resetControllerState() {
    this.nanoTimeReadCacheValid = 0L;
    this.readCacheStatus = READ_CACHE_STATUS.IDLE;
    initWriteCacheStatus(WRITE_CACHE_STATUS.IDLE);
    this.controllerPortMode = CONTROLLER_PORT_MODE.UNKNOWN;
    this.readWindowActuallyRead = null;
    this.readWindowSentToController = null;
    this.isReadWindowSentToControllerInitialized = false;
    this.hasReadWindowChanged = true;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setHealthStatus(HardwareDeviceHealth.HealthStatus paramHealthStatus) {
    this.hardwareDeviceHealth.setHealthStatus(paramHealthStatus);
  }
  
  public void setHeartbeatAction(I2cDeviceSynch.HeartbeatAction paramHeartbeatAction) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        this.heartbeatAction = paramHeartbeatAction;
        return;
      } 
    } 
  }
  
  public void setHeartbeatInterval(int paramInt) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        this.msHeartbeatInterval = Math.max(0, paramInt);
        return;
      } 
    } 
  }
  
  @Deprecated
  public void setI2cAddr(I2cAddr paramI2cAddr) {
    setI2cAddress(paramI2cAddr);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    synchronized (this.engagementLock) {
      if (this.i2cAddr.get7Bit() != paramI2cAddr.get7Bit()) {
        boolean bool = this.isHooked;
        disengage();
        this.i2cAddr = paramI2cAddr;
        if (bool)
          engage(); 
      } 
      return;
    } 
  }
  
  public void setLogging(boolean paramBoolean) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        this.loggingEnabled = paramBoolean;
        return;
      } 
    } 
  }
  
  public void setLoggingTag(String paramString) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString);
        stringBuilder.append("I2C");
        this.loggingTag = stringBuilder.toString();
        return;
      } 
    } 
  }
  
  public void setReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow) {
    synchronized (this.concurrentClientLock) {
      if (!this.disableReadWindows)
        setReadWindowInternal(paramReadWindow); 
      return;
    } 
  }
  
  protected void setReadWindowInternal(I2cDeviceSynch.ReadWindow paramReadWindow) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        if (this.readWindow == null || !this.readWindow.canBeUsedToRead() || !this.readWindow.mayInitiateSwitchToReadMode() || !this.readWindow.sameAsIncludingMode(paramReadWindow)) {
          boolean bool;
          assignReadWindow(paramReadWindow.readableCopy());
          if (this.readWindow.canBeUsedToRead() && this.readWindow.mayInitiateSwitchToReadMode()) {
            bool = true;
          } else {
            bool = false;
          } 
          Assert.assertTrue(bool);
        } 
        return;
      } 
    } 
  }
  
  public void setUserConfiguredName(String paramString) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        this.name = paramString;
        return;
      } 
    } 
  }
  
  protected void setWriteCacheStatus(WRITE_CACHE_STATUS paramWRITE_CACHE_STATUS) {
    this.writeCacheStatus.setStatus(paramWRITE_CACHE_STATUS);
  }
  
  void setWriteCacheStatusIfHooked(WRITE_CACHE_STATUS paramWRITE_CACHE_STATUS) {
    if (this.isHooked && newReadsAndWritesAllowed())
      setWriteCacheStatus(paramWRITE_CACHE_STATUS); 
  }
  
  protected void unhook() {
    try {
      synchronized (this.engagementLock) {
        if (this.isHooked) {
          log(2, "unhooking ...");
          this.heartbeatExecutor.shutdown();
          ThreadPool.awaitTerminationOrExitApplication(this.heartbeatExecutor, 10L, TimeUnit.SECONDS, "I2c Heartbeat", "internal error");
          disableReadsAndWrites();
          gracefullyDrainReadersAndWriters();
          synchronized (this.callbackLock) {
            waitForWriteCompletionInternal(I2cWaitControl.ATOMIC);
            this.heartbeatExecutor = null;
            this.i2cDevice.deregisterForPortReadyCallback();
            this.isHooked = false;
            resetControllerState();
            enableReadsAndWrites();
            log(2, "...unhooking complete");
          } 
        } 
        return;
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  protected long waitForIdleWriteCache() throws InterruptedException {
    return this.writeCacheStatus.waitForIdle();
  }
  
  protected void waitForValidReadCache() throws InterruptedException {
    ElapsedTime elapsedTime = null;
    while (!readCacheIsValid()) {
      ElapsedTime elapsedTime1 = elapsedTime;
      if (elapsedTime == null)
        elapsedTime1 = new ElapsedTime(); 
      if (elapsedTime1.milliseconds() <= 500.0D) {
        this.callbackLock.wait(60L);
        elapsedTime = elapsedTime1;
        continue;
      } 
      throw new InterruptedException();
    } 
  }
  
  protected void waitForWriteCompletionInternal(I2cWaitControl paramI2cWaitControl) throws InterruptedException {
    if (paramI2cWaitControl != I2cWaitControl.NONE) {
      long l = waitForIdleWriteCache();
      if (paramI2cWaitControl == I2cWaitControl.WRITTEN) {
        l = Math.max(0L, l + this.i2cDevice.getMaxI2cWriteLatency() - System.nanoTime());
        if (l > 0L) {
          long l1 = l / 1000000L;
          Thread.sleep(l1, (int)(l - 1000000L * l1));
        } 
      } 
    } 
  }
  
  public void waitForWriteCompletions(I2cWaitControl paramI2cWaitControl) {
    try {
      synchronized (this.concurrentClientLock) {
        synchronized (this.callbackLock) {
          waitForWriteCompletionInternal(paramI2cWaitControl);
          return;
        } 
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    write(paramInt, paramArrayOfbyte, I2cWaitControl.ATOMIC);
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte, I2cWaitControl paramI2cWaitControl) {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual acquireReaderLockShared : ()V
    //   4: aload_0
    //   5: invokevirtual isOpenForWriting : ()Z
    //   8: istore #6
    //   10: iload #6
    //   12: ifne -> 20
    //   15: aload_0
    //   16: invokevirtual releaseReaderLockShared : ()V
    //   19: return
    //   20: aload_0
    //   21: getfield concurrentClientLock : Ljava/lang/Object;
    //   24: astore #7
    //   26: aload #7
    //   28: monitorenter
    //   29: aload_2
    //   30: arraylength
    //   31: istore #5
    //   33: iconst_1
    //   34: istore #4
    //   36: iload #5
    //   38: bipush #26
    //   40: if_icmpgt -> 226
    //   43: aload_0
    //   44: getfield callbackLock : Ljava/lang/Object;
    //   47: astore #8
    //   49: aload #8
    //   51: monitorenter
    //   52: aload_0
    //   53: getfield isWriteCoalescingEnabled : Z
    //   56: ifeq -> 284
    //   59: aload_0
    //   60: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
    //   63: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.DIRTY : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
    //   66: if_acmpne -> 284
    //   69: aload_0
    //   70: getfield cregWrite : I
    //   73: aload_2
    //   74: arraylength
    //   75: iadd
    //   76: bipush #26
    //   78: if_icmpgt -> 284
    //   81: aload_2
    //   82: arraylength
    //   83: iload_1
    //   84: iadd
    //   85: aload_0
    //   86: getfield iregWriteFirst : I
    //   89: if_icmpne -> 104
    //   92: aload_2
    //   93: aload_0
    //   94: invokevirtual readWriteCache : ()[B
    //   97: invokestatic concatenateByteArrays : ([B[B)[B
    //   100: astore_2
    //   101: goto -> 134
    //   104: aload_0
    //   105: getfield iregWriteFirst : I
    //   108: aload_0
    //   109: getfield cregWrite : I
    //   112: iadd
    //   113: iload_1
    //   114: if_icmpne -> 284
    //   117: aload_0
    //   118: getfield iregWriteFirst : I
    //   121: istore_1
    //   122: aload_0
    //   123: invokevirtual readWriteCache : ()[B
    //   126: aload_2
    //   127: invokestatic concatenateByteArrays : ([B[B)[B
    //   130: astore_2
    //   131: goto -> 134
    //   134: iload #4
    //   136: ifne -> 144
    //   139: aload_0
    //   140: invokevirtual waitForIdleWriteCache : ()J
    //   143: pop2
    //   144: aload_0
    //   145: iload_1
    //   146: putfield iregWriteFirst : I
    //   149: aload_0
    //   150: aload_2
    //   151: arraylength
    //   152: putfield cregWrite : I
    //   155: aload_0
    //   156: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.DIRTY : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
    //   159: invokevirtual setWriteCacheStatusIfHooked : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;)V
    //   162: aload_0
    //   163: getfield writeCacheLock : Ljava/util/concurrent/locks/Lock;
    //   166: invokeinterface lock : ()V
    //   171: aload_2
    //   172: iconst_0
    //   173: aload_0
    //   174: getfield writeCache : [B
    //   177: iconst_4
    //   178: aload_2
    //   179: arraylength
    //   180: invokestatic arraycopy : (Ljava/lang/Object;ILjava/lang/Object;II)V
    //   183: aload_0
    //   184: getfield writeCacheLock : Ljava/util/concurrent/locks/Lock;
    //   187: invokeinterface unlock : ()V
    //   192: aload_0
    //   193: aload_3
    //   194: invokevirtual waitForWriteCompletionInternal : (Lcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
    //   197: aload #8
    //   199: monitorexit
    //   200: aload #7
    //   202: monitorexit
    //   203: aload_0
    //   204: invokevirtual releaseReaderLockShared : ()V
    //   207: return
    //   208: astore_2
    //   209: aload_0
    //   210: getfield writeCacheLock : Ljava/util/concurrent/locks/Lock;
    //   213: invokeinterface unlock : ()V
    //   218: aload_2
    //   219: athrow
    //   220: astore_2
    //   221: aload #8
    //   223: monitorexit
    //   224: aload_2
    //   225: athrow
    //   226: new java/lang/IllegalArgumentException
    //   229: dup
    //   230: ldc_w 'write request of %d bytes is too large; max is %d'
    //   233: iconst_2
    //   234: anewarray java/lang/Object
    //   237: dup
    //   238: iconst_0
    //   239: aload_2
    //   240: arraylength
    //   241: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   244: aastore
    //   245: dup
    //   246: iconst_1
    //   247: bipush #26
    //   249: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   252: aastore
    //   253: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   256: invokespecial <init> : (Ljava/lang/String;)V
    //   259: athrow
    //   260: astore_2
    //   261: aload #7
    //   263: monitorexit
    //   264: aload_2
    //   265: athrow
    //   266: astore_2
    //   267: aload_0
    //   268: invokevirtual releaseReaderLockShared : ()V
    //   271: aload_2
    //   272: athrow
    //   273: invokestatic currentThread : ()Ljava/lang/Thread;
    //   276: invokevirtual interrupt : ()V
    //   279: return
    //   280: astore_2
    //   281: goto -> 273
    //   284: iconst_0
    //   285: istore #4
    //   287: goto -> 134
    // Exception table:
    //   from	to	target	type
    //   0	4	280	java/lang/InterruptedException
    //   4	10	266	finally
    //   15	19	280	java/lang/InterruptedException
    //   20	29	266	finally
    //   29	33	260	finally
    //   43	52	260	finally
    //   52	101	220	finally
    //   104	131	220	finally
    //   139	144	220	finally
    //   144	171	220	finally
    //   171	183	208	finally
    //   183	200	220	finally
    //   200	203	260	finally
    //   203	207	280	java/lang/InterruptedException
    //   209	220	220	finally
    //   221	224	220	finally
    //   224	226	260	finally
    //   226	260	260	finally
    //   261	264	260	finally
    //   264	266	266	finally
    //   267	273	280	java/lang/InterruptedException
  }
  
  public void write8(int paramInt1, int paramInt2) {
    write(paramInt1, new byte[] { (byte)paramInt2 });
  }
  
  public void write8(int paramInt1, int paramInt2, I2cWaitControl paramI2cWaitControl) {
    write(paramInt1, new byte[] { (byte)paramInt2 }, paramI2cWaitControl);
  }
  
  protected enum CONTROLLER_PORT_MODE {
    READ, SWITCHINGTOREADMODE, UNKNOWN, WRITE;
    
    static {
      CONTROLLER_PORT_MODE cONTROLLER_PORT_MODE = new CONTROLLER_PORT_MODE("READ", 3);
      READ = cONTROLLER_PORT_MODE;
      $VALUES = new CONTROLLER_PORT_MODE[] { UNKNOWN, WRITE, SWITCHINGTOREADMODE, cONTROLLER_PORT_MODE };
    }
  }
  
  protected class Callback implements I2cController.I2cPortReadyCallback, I2cController.I2cPortReadyBeginEndNotifications, RobotArmingStateNotifier.Callback {
    protected boolean doModuleIsArmedWorkEnabledWrites = false;
    
    protected boolean enabledReadMode = false;
    
    protected boolean enabledWriteMode = false;
    
    protected boolean haveSeenModuleIsArmedWork = false;
    
    protected boolean heartbeatRequired = false;
    
    protected I2cDeviceSynchImpl.READ_CACHE_STATUS prevReadCacheStatus = I2cDeviceSynchImpl.READ_CACHE_STATUS.IDLE;
    
    protected I2cDeviceSynchImpl.WRITE_CACHE_STATUS prevWriteCacheStatus = I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE;
    
    protected boolean queueFullWrite = false;
    
    protected boolean queueRead = false;
    
    protected boolean setActionFlag = false;
    
    protected void doModuleIsArmedWork(boolean param1Boolean) {
      try {
        I2cDeviceSynchImpl.this.log(2, "doModuleIsArmedWork ...");
      } finally {
        I2cDeviceSynchImpl.this.log(2, "... doModuleIsArmedWork complete");
      } 
    }
    
    protected void issueWrite() {
      I2cDeviceSynchImpl.this.setWriteCacheStatusIfHooked(I2cDeviceSynchImpl.WRITE_CACHE_STATUS.QUEUED);
      I2cDeviceSynchImpl.this.i2cDevice.enableI2cWriteMode(I2cDeviceSynchImpl.this.i2cAddr, I2cDeviceSynchImpl.this.iregWriteFirst, I2cDeviceSynchImpl.this.cregWrite);
      this.enabledWriteMode = true;
      I2cDeviceSynchImpl.this.readWindowSentToController = null;
      I2cDeviceSynchImpl.this.isReadWindowSentToControllerInitialized = true;
      this.setActionFlag = true;
      this.queueFullWrite = true;
    }
    
    public void onModuleStateChange(RobotArmingStateNotifier param1RobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE param1ARMINGSTATE) {
      int i = I2cDeviceSynchImpl.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[param1ARMINGSTATE.ordinal()];
      if (i != 1) {
        if (i != 2)
          return; 
        I2cDeviceSynchImpl.this.log(2, "onPretending ...");
        doModuleIsArmedWork(false);
        I2cDeviceSynchImpl.this.log(2, "... onPretending");
        return;
      } 
      I2cDeviceSynchImpl.this.log(2, "onArmed ...");
      doModuleIsArmedWork(true);
      I2cDeviceSynchImpl.this.log(2, "... onArmed");
    }
    
    public void onPortIsReadyCallbacksBegin(int param1Int) {
      I2cDeviceSynchImpl.this.log(2, "doPortIsReadyCallbackBeginWork ...");
      try {
        param1Int = I2cDeviceSynchImpl.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[I2cDeviceSynchImpl.this.robotUsbModule.getArmingState().ordinal()];
        if (param1Int != 1) {
          if (param1Int == 2)
            doModuleIsArmedWork(false); 
        } else {
          doModuleIsArmedWork(true);
        } 
        return;
      } finally {
        I2cDeviceSynchImpl.this.log(2, "... doPortIsReadyCallbackBeginWork complete");
      } 
    }
    
    public void onPortIsReadyCallbacksEnd(int param1Int) {
      try {
        I2cDeviceSynchImpl.this.log(2, "onPortIsReadyCallbacksEnd ...");
        boolean bool = I2cDeviceSynchImpl.this.isClosing;
        if (bool || !this.haveSeenModuleIsArmedWork)
          return; 
      } finally {
        I2cDeviceSynchImpl.this.log(2, "... onPortIsReadyCallbacksEnd complete");
      } 
    }
    
    public void portIsReady(int param1Int) {
      updateStateMachines();
    }
    
    protected void startSwitchingToReadMode(I2cDeviceSynch.ReadWindow param1ReadWindow) {
      I2cDeviceSynchImpl.READ_CACHE_STATUS rEAD_CACHE_STATUS;
      I2cDeviceSynchImpl i2cDeviceSynchImpl = I2cDeviceSynchImpl.this;
      if (i2cDeviceSynchImpl.isControllerLegacy) {
        rEAD_CACHE_STATUS = I2cDeviceSynchImpl.READ_CACHE_STATUS.SWITCHINGTOREADMODE;
      } else {
        rEAD_CACHE_STATUS = I2cDeviceSynchImpl.READ_CACHE_STATUS.QUEUED;
      } 
      i2cDeviceSynchImpl.readCacheStatus = rEAD_CACHE_STATUS;
      I2cDeviceSynchImpl.this.i2cDevice.enableI2cReadMode(I2cDeviceSynchImpl.this.i2cAddr, param1ReadWindow.getRegisterFirst(), param1ReadWindow.getRegisterCount());
      this.enabledReadMode = true;
      I2cDeviceSynchImpl.this.readWindowSentToController = param1ReadWindow;
      I2cDeviceSynchImpl.this.isReadWindowSentToControllerInitialized = true;
      this.setActionFlag = true;
      this.queueFullWrite = true;
      if (!I2cDeviceSynchImpl.this.isControllerLegacy)
        this.queueRead = true; 
    }
    
    protected void updateStateMachines() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   4: getfield callbackLock : Ljava/lang/Object;
      //   7: astore #4
      //   9: aload #4
      //   11: monitorenter
      //   12: aload_0
      //   13: iconst_0
      //   14: putfield setActionFlag : Z
      //   17: aload_0
      //   18: iconst_0
      //   19: putfield queueFullWrite : Z
      //   22: aload_0
      //   23: iconst_0
      //   24: putfield queueRead : Z
      //   27: aload_0
      //   28: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   31: getfield msHeartbeatInterval : I
      //   34: ifle -> 1689
      //   37: aload_0
      //   38: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   41: getfield timeSinceLastHeartbeat : Lcom/qualcomm/robotcore/util/ElapsedTime;
      //   44: invokevirtual milliseconds : ()D
      //   47: aload_0
      //   48: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   51: getfield msHeartbeatInterval : I
      //   54: i2d
      //   55: dcmpl
      //   56: iflt -> 1689
      //   59: iconst_1
      //   60: istore_2
      //   61: goto -> 64
      //   64: aload_0
      //   65: iload_2
      //   66: putfield heartbeatRequired : Z
      //   69: aload_0
      //   70: iconst_0
      //   71: putfield enabledReadMode : Z
      //   74: aload_0
      //   75: iconst_0
      //   76: putfield enabledWriteMode : Z
      //   79: aload_0
      //   80: aload_0
      //   81: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   84: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   87: putfield prevReadCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   90: aload_0
      //   91: aload_0
      //   92: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   95: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   98: putfield prevWriteCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   101: aload_0
      //   102: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   105: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   108: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.SWITCHINGTOREADMODE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   111: if_acmpne -> 124
      //   114: aload_0
      //   115: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   118: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   121: putfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   124: aload_0
      //   125: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   128: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   131: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   134: if_acmpeq -> 150
      //   137: aload_0
      //   138: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   141: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   144: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.VALID_QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   147: if_acmpne -> 203
      //   150: aload_0
      //   151: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   154: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUE_COMPLETED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   157: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   160: aload_0
      //   161: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   164: getfield readCacheTimeWindow : Lorg/firstinspires/ftc/robotcore/internal/hardware/TimeWindow;
      //   167: invokevirtual isCleared : ()Z
      //   170: ifne -> 193
      //   173: aload_0
      //   174: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   177: aload_0
      //   178: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   181: getfield readCacheTimeWindow : Lorg/firstinspires/ftc/robotcore/internal/hardware/TimeWindow;
      //   184: invokevirtual getNanosecondsLast : ()J
      //   187: putfield nanoTimeReadCacheValid : J
      //   190: goto -> 203
      //   193: aload_0
      //   194: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   197: invokestatic nanoTime : ()J
      //   200: putfield nanoTimeReadCacheValid : J
      //   203: aload_0
      //   204: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   207: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   210: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   213: if_acmpne -> 226
      //   216: aload_0
      //   217: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   220: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   223: invokevirtual setWriteCacheStatus : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;)V
      //   226: aload_0
      //   227: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   230: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   233: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   236: if_acmpeq -> 1699
      //   239: aload_0
      //   240: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   243: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   246: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.SWITCHINGTOREADMODE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   249: if_acmpne -> 262
      //   252: aload_0
      //   253: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   256: getfield isControllerLegacy : Z
      //   259: ifne -> 1699
      //   262: aload_0
      //   263: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   266: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   269: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.VALID_ONLYONCE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   272: if_acmpeq -> 1699
      //   275: aload_0
      //   276: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   279: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   282: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUE_COMPLETED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   285: if_acmpne -> 1694
      //   288: goto -> 1699
      //   291: iload_2
      //   292: invokestatic assertTrue : (Z)V
      //   295: aload_0
      //   296: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   299: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   302: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   305: if_acmpeq -> 1709
      //   308: aload_0
      //   309: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   312: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   315: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.DIRTY : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   318: if_acmpne -> 1704
      //   321: goto -> 1709
      //   324: iload_2
      //   325: invokestatic assertTrue : (Z)V
      //   328: aload_0
      //   329: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   332: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   335: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   338: if_acmpeq -> 1719
      //   341: aload_0
      //   342: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   345: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   348: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.WRITE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   351: if_acmpeq -> 1719
      //   354: aload_0
      //   355: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   358: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   361: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.UNKNOWN : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   364: if_acmpne -> 1714
      //   367: goto -> 1719
      //   370: iload_2
      //   371: invokestatic assertTrue : (Z)V
      //   374: aload_0
      //   375: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   378: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   381: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.SWITCHINGTOREADMODE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   384: if_acmpne -> 441
      //   387: aload_0
      //   388: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   391: getfield isControllerLegacy : Z
      //   394: invokestatic assertTrue : (Z)V
      //   397: aload_0
      //   398: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   401: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   404: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   407: if_acmpne -> 433
      //   410: aload_0
      //   411: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   414: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   417: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   420: aload_0
      //   421: iconst_1
      //   422: putfield setActionFlag : Z
      //   425: aload_0
      //   426: iconst_1
      //   427: putfield queueRead : Z
      //   430: goto -> 786
      //   433: aload_0
      //   434: iconst_1
      //   435: putfield queueRead : Z
      //   438: goto -> 786
      //   441: aload_0
      //   442: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   445: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   448: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.DIRTY : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   451: if_acmpne -> 471
      //   454: aload_0
      //   455: invokevirtual issueWrite : ()V
      //   458: aload_0
      //   459: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   462: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   465: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   468: goto -> 786
      //   471: aload_0
      //   472: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   475: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   478: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   481: if_acmpeq -> 584
      //   484: aload_0
      //   485: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   488: getfield hasReadWindowChanged : Z
      //   491: ifeq -> 497
      //   494: goto -> 584
      //   497: aload_0
      //   498: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   501: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   504: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUE_COMPLETED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   507: if_acmpne -> 569
      //   510: aload_0
      //   511: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   514: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   517: ifnull -> 556
      //   520: aload_0
      //   521: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   524: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   527: invokevirtual canBeUsedToRead : ()Z
      //   530: ifeq -> 556
      //   533: aload_0
      //   534: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   537: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.VALID_QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   540: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   543: aload_0
      //   544: iconst_1
      //   545: putfield setActionFlag : Z
      //   548: aload_0
      //   549: iconst_1
      //   550: putfield queueRead : Z
      //   553: goto -> 786
      //   556: aload_0
      //   557: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   560: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.VALID_ONLYONCE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   563: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   566: goto -> 786
      //   569: aload_0
      //   570: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   573: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   576: astore_3
      //   577: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.VALID_ONLYONCE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   580: astore_3
      //   581: goto -> 786
      //   584: aload_0
      //   585: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   588: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   591: ifnull -> 1734
      //   594: aload_0
      //   595: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   598: getfield isReadWindowSentToControllerInitialized : Z
      //   601: ifeq -> 1724
      //   604: aload_0
      //   605: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   608: getfield readWindowSentToController : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   611: ifnull -> 1724
      //   614: aload_0
      //   615: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   618: getfield readWindowSentToController : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   621: aload_0
      //   622: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   625: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   628: invokevirtual contains : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)Z
      //   631: ifeq -> 1724
      //   634: aload_0
      //   635: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   638: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   641: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   644: if_acmpne -> 1724
      //   647: iconst_1
      //   648: istore_1
      //   649: goto -> 652
      //   652: aload_0
      //   653: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   656: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   659: invokevirtual canBeUsedToRead : ()Z
      //   662: ifeq -> 1734
      //   665: iload_1
      //   666: ifne -> 682
      //   669: aload_0
      //   670: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   673: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   676: invokevirtual mayInitiateSwitchToReadMode : ()Z
      //   679: ifeq -> 1734
      //   682: iload_1
      //   683: ifeq -> 723
      //   686: aload_0
      //   687: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   690: aload_0
      //   691: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   694: getfield readWindowSentToController : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   697: putfield readWindowActuallyRead : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   700: aload_0
      //   701: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   704: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.QUEUED : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   707: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   710: aload_0
      //   711: iconst_1
      //   712: putfield setActionFlag : Z
      //   715: aload_0
      //   716: iconst_1
      //   717: putfield queueRead : Z
      //   720: goto -> 1729
      //   723: aload_0
      //   724: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   727: aload_0
      //   728: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   731: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   734: putfield readWindowActuallyRead : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   737: aload_0
      //   738: aload_0
      //   739: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   742: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   745: invokevirtual startSwitchingToReadMode : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
      //   748: goto -> 1729
      //   751: iload_1
      //   752: ifeq -> 768
      //   755: aload_0
      //   756: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   759: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   762: invokevirtual noteWindowUsedForRead : ()V
      //   765: goto -> 778
      //   768: aload_0
      //   769: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   772: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   775: putfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   778: aload_0
      //   779: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   782: iconst_0
      //   783: putfield hasReadWindowChanged : Z
      //   786: aload_0
      //   787: getfield setActionFlag : Z
      //   790: ifne -> 1001
      //   793: aload_0
      //   794: getfield heartbeatRequired : Z
      //   797: ifeq -> 1001
      //   800: aload_0
      //   801: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   804: getfield heartbeatAction : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
      //   807: ifnull -> 1001
      //   810: aload_0
      //   811: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   814: getfield isReadWindowSentToControllerInitialized : Z
      //   817: ifeq -> 899
      //   820: aload_0
      //   821: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   824: getfield readWindowSentToController : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   827: ifnull -> 899
      //   830: aload_0
      //   831: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   834: getfield heartbeatAction : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
      //   837: getfield rereadLastRead : Z
      //   840: ifeq -> 899
      //   843: aload_0
      //   844: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   847: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   850: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   853: if_acmpne -> 864
      //   856: aload_0
      //   857: iconst_1
      //   858: putfield setActionFlag : Z
      //   861: goto -> 1001
      //   864: aload_0
      //   865: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   868: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   871: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS.SWITCHINGTOREADMODE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   874: if_acmpne -> 1739
      //   877: aload_0
      //   878: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   881: getfield isControllerLegacy : Z
      //   884: ifeq -> 1739
      //   887: iconst_1
      //   888: istore_2
      //   889: goto -> 892
      //   892: iload_2
      //   893: invokestatic assertTrue : (Z)V
      //   896: goto -> 1001
      //   899: aload_0
      //   900: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   903: getfield isReadWindowSentToControllerInitialized : Z
      //   906: ifeq -> 945
      //   909: aload_0
      //   910: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   913: getfield readWindowSentToController : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   916: ifnonnull -> 945
      //   919: aload_0
      //   920: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   923: getfield heartbeatAction : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
      //   926: getfield rewriteLastWritten : Z
      //   929: ifeq -> 945
      //   932: aload_0
      //   933: iconst_1
      //   934: putfield queueFullWrite : Z
      //   937: aload_0
      //   938: iconst_1
      //   939: putfield setActionFlag : Z
      //   942: goto -> 1001
      //   945: aload_0
      //   946: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   949: getfield heartbeatAction : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
      //   952: getfield heartbeatReadWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   955: ifnull -> 1001
      //   958: aload_0
      //   959: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   962: getfield heartbeatAction : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$HeartbeatAction;
      //   965: getfield heartbeatReadWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   968: astore_3
      //   969: aload_0
      //   970: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   973: getfield heartbeatExecutor : Ljava/util/concurrent/ExecutorService;
      //   976: ifnull -> 1001
      //   979: aload_0
      //   980: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   983: getfield heartbeatExecutor : Ljava/util/concurrent/ExecutorService;
      //   986: new com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$Callback$1
      //   989: dup
      //   990: aload_0
      //   991: aload_3
      //   992: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$Callback;Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
      //   995: invokeinterface submit : (Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
      //   1000: pop
      //   1001: aload_0
      //   1002: getfield setActionFlag : Z
      //   1005: ifeq -> 1018
      //   1008: aload_0
      //   1009: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1012: getfield timeSinceLastHeartbeat : Lcom/qualcomm/robotcore/util/ElapsedTime;
      //   1015: invokevirtual reset : ()V
      //   1018: aload_0
      //   1019: getfield enabledReadMode : Z
      //   1022: ifne -> 1032
      //   1025: aload_0
      //   1026: getfield enabledWriteMode : Z
      //   1029: ifeq -> 1115
      //   1032: aload_0
      //   1033: getfield queueFullWrite : Z
      //   1036: invokestatic assertTrue : (Z)V
      //   1039: aload_0
      //   1040: getfield enabledReadMode : Z
      //   1043: ifeq -> 1744
      //   1046: aload_0
      //   1047: getfield enabledWriteMode : Z
      //   1050: ifeq -> 1744
      //   1053: iconst_1
      //   1054: istore_2
      //   1055: goto -> 1058
      //   1058: iload_2
      //   1059: invokestatic assertFalse : (Z)V
      //   1062: aload_0
      //   1063: getfield enabledWriteMode : Z
      //   1066: ifeq -> 1082
      //   1069: aload_0
      //   1070: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1073: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.WRITE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1076: putfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1079: goto -> 1115
      //   1082: aload_0
      //   1083: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1086: astore #5
      //   1088: aload_0
      //   1089: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1092: getfield isControllerLegacy : Z
      //   1095: ifeq -> 1105
      //   1098: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.SWITCHINGTOREADMODE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1101: astore_3
      //   1102: goto -> 1109
      //   1105: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE.READ : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1108: astore_3
      //   1109: aload #5
      //   1111: aload_3
      //   1112: putfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1115: aload_0
      //   1116: getfield setActionFlag : Z
      //   1119: ifeq -> 1137
      //   1122: aload_0
      //   1123: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1126: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1129: invokeinterface setI2cPortActionFlag : ()V
      //   1134: goto -> 1149
      //   1137: aload_0
      //   1138: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1141: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1144: invokeinterface clearI2cPortActionFlag : ()V
      //   1149: aload_0
      //   1150: getfield setActionFlag : Z
      //   1153: ifeq -> 1178
      //   1156: aload_0
      //   1157: getfield queueFullWrite : Z
      //   1160: ifne -> 1178
      //   1163: aload_0
      //   1164: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1167: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1170: invokeinterface writeI2cPortFlagOnlyToController : ()V
      //   1175: goto -> 1197
      //   1178: aload_0
      //   1179: getfield queueFullWrite : Z
      //   1182: ifeq -> 1197
      //   1185: aload_0
      //   1186: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1189: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1192: invokeinterface writeI2cCacheToController : ()V
      //   1197: aload_0
      //   1198: getfield queueRead : Z
      //   1201: ifeq -> 1216
      //   1204: aload_0
      //   1205: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1208: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1211: invokeinterface readI2cCacheFromController : ()V
      //   1216: aload_0
      //   1217: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1220: getfield loggingEnabled : Z
      //   1223: ifeq -> 1665
      //   1226: new java/lang/StringBuilder
      //   1229: dup
      //   1230: invokespecial <init> : ()V
      //   1233: astore_3
      //   1234: aload_3
      //   1235: ldc_w 'cyc %d'
      //   1238: iconst_1
      //   1239: anewarray java/lang/Object
      //   1242: dup
      //   1243: iconst_0
      //   1244: aload_0
      //   1245: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1248: getfield i2cDevice : Lcom/qualcomm/robotcore/hardware/I2cDevice;
      //   1251: invokeinterface getCallbackCount : ()I
      //   1256: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   1259: aastore
      //   1260: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   1263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1266: pop
      //   1267: aload_0
      //   1268: getfield setActionFlag : Z
      //   1271: ifne -> 1291
      //   1274: aload_0
      //   1275: getfield queueFullWrite : Z
      //   1278: ifne -> 1291
      //   1281: aload_0
      //   1282: getfield queueRead : Z
      //   1285: ifeq -> 1749
      //   1288: goto -> 1291
      //   1291: aload_3
      //   1292: ldc_w '|'
      //   1295: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1298: pop
      //   1299: aload_0
      //   1300: getfield setActionFlag : Z
      //   1303: ifeq -> 1314
      //   1306: aload_3
      //   1307: ldc_w 'f'
      //   1310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1313: pop
      //   1314: aload_0
      //   1315: getfield queueFullWrite : Z
      //   1318: ifeq -> 1329
      //   1321: aload_3
      //   1322: ldc_w 'w'
      //   1325: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1328: pop
      //   1329: aload_0
      //   1330: getfield queueRead : Z
      //   1333: ifeq -> 1754
      //   1336: aload_3
      //   1337: ldc_w 'r'
      //   1340: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1343: pop
      //   1344: goto -> 1754
      //   1347: aload_0
      //   1348: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1351: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   1354: aload_0
      //   1355: getfield prevReadCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   1358: if_acmpeq -> 1429
      //   1361: new java/lang/StringBuilder
      //   1364: dup
      //   1365: invokespecial <init> : ()V
      //   1368: astore #5
      //   1370: aload #5
      //   1372: ldc_w '| R.'
      //   1375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1378: pop
      //   1379: aload #5
      //   1381: aload_0
      //   1382: getfield prevReadCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   1385: invokevirtual toString : ()Ljava/lang/String;
      //   1388: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1391: pop
      //   1392: aload #5
      //   1394: ldc_w '->'
      //   1397: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1400: pop
      //   1401: aload #5
      //   1403: aload_0
      //   1404: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1407: getfield readCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$READ_CACHE_STATUS;
      //   1410: invokevirtual toString : ()Ljava/lang/String;
      //   1413: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1416: pop
      //   1417: aload_3
      //   1418: aload #5
      //   1420: invokevirtual toString : ()Ljava/lang/String;
      //   1423: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1426: pop
      //   1427: iconst_0
      //   1428: istore_1
      //   1429: aload_0
      //   1430: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1433: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   1436: aload_0
      //   1437: getfield prevWriteCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   1440: if_acmpeq -> 1511
      //   1443: new java/lang/StringBuilder
      //   1446: dup
      //   1447: invokespecial <init> : ()V
      //   1450: astore #5
      //   1452: aload #5
      //   1454: ldc_w '| W.'
      //   1457: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1460: pop
      //   1461: aload #5
      //   1463: aload_0
      //   1464: getfield prevWriteCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   1467: invokevirtual toString : ()Ljava/lang/String;
      //   1470: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1473: pop
      //   1474: aload #5
      //   1476: ldc_w '->'
      //   1479: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1482: pop
      //   1483: aload #5
      //   1485: aload_0
      //   1486: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1489: getfield writeCacheStatus : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WriteCacheStatus;
      //   1492: invokevirtual toString : ()Ljava/lang/String;
      //   1495: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1498: pop
      //   1499: aload_3
      //   1500: aload #5
      //   1502: invokevirtual toString : ()Ljava/lang/String;
      //   1505: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1508: pop
      //   1509: iconst_0
      //   1510: istore_1
      //   1511: aload_0
      //   1512: getfield enabledWriteMode : Z
      //   1515: ifeq -> 1561
      //   1518: aload_3
      //   1519: ldc_w '| write(0x%02x,%d)'
      //   1522: iconst_2
      //   1523: anewarray java/lang/Object
      //   1526: dup
      //   1527: iconst_0
      //   1528: aload_0
      //   1529: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1532: getfield iregWriteFirst : I
      //   1535: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   1538: aastore
      //   1539: dup
      //   1540: iconst_1
      //   1541: aload_0
      //   1542: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1545: getfield cregWrite : I
      //   1548: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   1551: aastore
      //   1552: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   1555: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1558: pop
      //   1559: iconst_0
      //   1560: istore_1
      //   1561: aload_0
      //   1562: getfield enabledReadMode : Z
      //   1565: ifeq -> 1617
      //   1568: aload_3
      //   1569: ldc_w '| read(0x%02x,%d)'
      //   1572: iconst_2
      //   1573: anewarray java/lang/Object
      //   1576: dup
      //   1577: iconst_0
      //   1578: aload_0
      //   1579: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1582: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   1585: invokevirtual getRegisterFirst : ()I
      //   1588: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   1591: aastore
      //   1592: dup
      //   1593: iconst_1
      //   1594: aload_0
      //   1595: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1598: getfield readWindow : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;
      //   1601: invokevirtual getRegisterCount : ()I
      //   1604: invokestatic valueOf : (I)Ljava/lang/Integer;
      //   1607: aastore
      //   1608: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   1611: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1614: pop
      //   1615: iconst_0
      //   1616: istore_1
      //   1617: iload_1
      //   1618: ifne -> 1649
      //   1621: aload_3
      //   1622: ldc_w '| port=%s'
      //   1625: iconst_1
      //   1626: anewarray java/lang/Object
      //   1629: dup
      //   1630: iconst_0
      //   1631: aload_0
      //   1632: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1635: getfield controllerPortMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$CONTROLLER_PORT_MODE;
      //   1638: invokevirtual toString : ()Ljava/lang/String;
      //   1641: aastore
      //   1642: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   1645: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1648: pop
      //   1649: iload_1
      //   1650: ifne -> 1665
      //   1653: aload_0
      //   1654: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1657: iconst_3
      //   1658: aload_3
      //   1659: invokevirtual toString : ()Ljava/lang/String;
      //   1662: invokevirtual log : (ILjava/lang/String;)V
      //   1665: aload_0
      //   1666: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   1669: getfield callbackLock : Ljava/lang/Object;
      //   1672: invokevirtual notifyAll : ()V
      //   1675: aload #4
      //   1677: monitorexit
      //   1678: return
      //   1679: astore_3
      //   1680: aload #4
      //   1682: monitorexit
      //   1683: aload_3
      //   1684: athrow
      //   1685: astore_3
      //   1686: goto -> 1001
      //   1689: iconst_0
      //   1690: istore_2
      //   1691: goto -> 64
      //   1694: iconst_0
      //   1695: istore_2
      //   1696: goto -> 291
      //   1699: iconst_1
      //   1700: istore_2
      //   1701: goto -> 291
      //   1704: iconst_0
      //   1705: istore_2
      //   1706: goto -> 324
      //   1709: iconst_1
      //   1710: istore_2
      //   1711: goto -> 324
      //   1714: iconst_0
      //   1715: istore_2
      //   1716: goto -> 370
      //   1719: iconst_1
      //   1720: istore_2
      //   1721: goto -> 370
      //   1724: iconst_0
      //   1725: istore_1
      //   1726: goto -> 652
      //   1729: iconst_1
      //   1730: istore_1
      //   1731: goto -> 751
      //   1734: iconst_0
      //   1735: istore_1
      //   1736: goto -> 751
      //   1739: iconst_0
      //   1740: istore_2
      //   1741: goto -> 892
      //   1744: iconst_0
      //   1745: istore_2
      //   1746: goto -> 1058
      //   1749: iconst_1
      //   1750: istore_1
      //   1751: goto -> 1347
      //   1754: iconst_0
      //   1755: istore_1
      //   1756: goto -> 1347
      // Exception table:
      //   from	to	target	type
      //   12	59	1679	finally
      //   64	124	1679	finally
      //   124	150	1679	finally
      //   150	190	1679	finally
      //   193	203	1679	finally
      //   203	226	1679	finally
      //   226	262	1679	finally
      //   262	288	1679	finally
      //   291	321	1679	finally
      //   324	367	1679	finally
      //   370	430	1679	finally
      //   433	438	1679	finally
      //   441	468	1679	finally
      //   471	494	1679	finally
      //   497	553	1679	finally
      //   556	566	1679	finally
      //   569	581	1679	finally
      //   584	647	1679	finally
      //   652	665	1679	finally
      //   669	682	1679	finally
      //   686	720	1679	finally
      //   723	748	1679	finally
      //   755	765	1679	finally
      //   768	778	1679	finally
      //   778	786	1679	finally
      //   786	861	1679	finally
      //   864	887	1679	finally
      //   892	896	1679	finally
      //   899	942	1679	finally
      //   945	969	1679	finally
      //   969	1001	1685	java/util/concurrent/RejectedExecutionException
      //   969	1001	1679	finally
      //   1001	1018	1679	finally
      //   1018	1032	1679	finally
      //   1032	1053	1679	finally
      //   1058	1079	1679	finally
      //   1082	1102	1679	finally
      //   1105	1109	1679	finally
      //   1109	1115	1679	finally
      //   1115	1134	1679	finally
      //   1137	1149	1679	finally
      //   1149	1175	1679	finally
      //   1178	1197	1679	finally
      //   1197	1216	1679	finally
      //   1216	1288	1679	finally
      //   1291	1314	1679	finally
      //   1314	1329	1679	finally
      //   1329	1344	1679	finally
      //   1347	1427	1679	finally
      //   1429	1509	1679	finally
      //   1511	1559	1679	finally
      //   1561	1615	1679	finally
      //   1621	1649	1679	finally
      //   1653	1665	1679	finally
      //   1665	1678	1679	finally
      //   1680	1683	1679	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        I2cDeviceSynchImpl.this.read(window.getRegisterFirst(), window.getRegisterCount());
        return;
      } catch (Exception exception) {
        return;
      } 
    }
  }
  
  protected enum READ_CACHE_STATUS {
    IDLE, QUEUED, QUEUE_COMPLETED, SWITCHINGTOREADMODE, VALID_ONLYONCE, VALID_QUEUED;
    
    static {
      READ_CACHE_STATUS rEAD_CACHE_STATUS = new READ_CACHE_STATUS("VALID_QUEUED", 5);
      VALID_QUEUED = rEAD_CACHE_STATUS;
      $VALUES = new READ_CACHE_STATUS[] { IDLE, SWITCHINGTOREADMODE, QUEUED, QUEUE_COMPLETED, VALID_ONLYONCE, rEAD_CACHE_STATUS };
    }
    
    boolean isQueued() {
      return (this == QUEUED || this == VALID_QUEUED);
    }
    
    boolean isValid() {
      return (this == VALID_QUEUED || this == VALID_ONLYONCE);
    }
  }
  
  protected enum WRITE_CACHE_STATUS {
    IDLE, QUEUED, DIRTY;
    
    static {
      WRITE_CACHE_STATUS wRITE_CACHE_STATUS = new WRITE_CACHE_STATUS("QUEUED", 2);
      QUEUED = wRITE_CACHE_STATUS;
      $VALUES = new WRITE_CACHE_STATUS[] { IDLE, DIRTY, wRITE_CACHE_STATUS };
    }
  }
  
  protected class WriteCacheStatus {
    private volatile long nanoTimeIdle = 0L;
    
    private volatile I2cDeviceSynchImpl.WRITE_CACHE_STATUS status = I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE;
    
    public I2cDeviceSynchImpl.WRITE_CACHE_STATUS getStatus() {
      return this.status;
    }
    
    public void initStatus(I2cDeviceSynchImpl.WRITE_CACHE_STATUS param1WRITE_CACHE_STATUS) {
      synchronized (I2cDeviceSynchImpl.this.callback) {
        this.status = param1WRITE_CACHE_STATUS;
        this.nanoTimeIdle = 0L;
        return;
      } 
    }
    
    public void setStatus(I2cDeviceSynchImpl.WRITE_CACHE_STATUS param1WRITE_CACHE_STATUS) {
      synchronized (I2cDeviceSynchImpl.this.callback) {
        boolean bool1;
        I2cDeviceSynchImpl.WRITE_CACHE_STATUS wRITE_CACHE_STATUS1 = this.status;
        I2cDeviceSynchImpl.WRITE_CACHE_STATUS wRITE_CACHE_STATUS2 = I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE;
        boolean bool2 = true;
        if (wRITE_CACHE_STATUS1 == wRITE_CACHE_STATUS2) {
          bool1 = true;
        } else {
          bool1 = false;
        } 
        this.status = param1WRITE_CACHE_STATUS;
        if (this.status != I2cDeviceSynchImpl.WRITE_CACHE_STATUS.IDLE)
          bool2 = false; 
        if (!bool1 && bool2)
          this.nanoTimeIdle = System.nanoTime(); 
        return;
      } 
    }
    
    public long waitForIdle() throws InterruptedException {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   4: getfield callbackLock : Ljava/lang/Object;
      //   7: astore #5
      //   9: aload #5
      //   11: monitorenter
      //   12: aconst_null
      //   13: astore_3
      //   14: aload_0
      //   15: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   18: invokevirtual getWriteCacheStatus : ()Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   21: getstatic com/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS.IDLE : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl$WRITE_CACHE_STATUS;
      //   24: if_acmpeq -> 82
      //   27: aload_3
      //   28: astore #4
      //   30: aload_3
      //   31: ifnonnull -> 43
      //   34: new com/qualcomm/robotcore/util/ElapsedTime
      //   37: dup
      //   38: invokespecial <init> : ()V
      //   41: astore #4
      //   43: aload #4
      //   45: invokevirtual milliseconds : ()D
      //   48: ldc2_w 500.0
      //   51: dcmpl
      //   52: ifgt -> 74
      //   55: aload_0
      //   56: getfield this$0 : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchImpl;
      //   59: getfield callbackLock : Ljava/lang/Object;
      //   62: ldc2_w 60
      //   65: invokevirtual wait : (J)V
      //   68: aload #4
      //   70: astore_3
      //   71: goto -> 14
      //   74: new java/lang/InterruptedException
      //   77: dup
      //   78: invokespecial <init> : ()V
      //   81: athrow
      //   82: aload_0
      //   83: getfield nanoTimeIdle : J
      //   86: lstore_1
      //   87: aload #5
      //   89: monitorexit
      //   90: lload_1
      //   91: lreturn
      //   92: astore_3
      //   93: aload #5
      //   95: monitorexit
      //   96: aload_3
      //   97: athrow
      // Exception table:
      //   from	to	target	type
      //   14	27	92	finally
      //   34	43	92	finally
      //   43	68	92	finally
      //   74	82	92	finally
      //   82	90	92	finally
      //   93	96	92	finally
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */