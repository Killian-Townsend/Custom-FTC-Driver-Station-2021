package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import com.qualcomm.hardware.HardwareDeviceManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.NextLock;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;

public class USBScanManager {
  public static final String TAG = "FtcConfigTag";
  
  public static final int msWaitDefault = 4000;
  
  protected Context context;
  
  protected DeviceManager deviceManager;
  
  protected ExecutorService executorService = null;
  
  protected boolean isRemoteConfig;
  
  protected final Map<String, LynxModuleDiscoveryState> lynxModuleDiscoveryStateMap = new ConcurrentHashMap<String, LynxModuleDiscoveryState>();
  
  protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  protected ScannedDevices remoteScannedDevices;
  
  protected final Object remoteScannedDevicesLock = new Object();
  
  protected NextLock scanResultsSequence;
  
  protected ThreadPool.Singleton<ScannedDevices> scanningSingleton = new ThreadPool.Singleton();
  
  public USBScanManager(Context paramContext, boolean paramBoolean) {
    this.context = paramContext;
    this.isRemoteConfig = paramBoolean;
    this.scanResultsSequence = new NextLock();
    if (!paramBoolean)
      this.deviceManager = (DeviceManager)new HardwareDeviceManager(paramContext, null); 
  }
  
  public LynxModuleMetaList awaitLynxModules(SerialNumber paramSerialNumber) throws InterruptedException {
    LynxModuleMetaList lynxModuleMetaList2 = (LynxModuleMetaList)(getDiscoveryState(paramSerialNumber)).lynxDiscoverySingleton.await();
    LynxModuleMetaList lynxModuleMetaList1 = lynxModuleMetaList2;
    if (lynxModuleMetaList2 == null) {
      RobotLog.vv("FtcConfigTag", "USBScanManager.awaitLynxModules() returning made-up result");
      lynxModuleMetaList1 = new LynxModuleMetaList(paramSerialNumber);
    } 
    return lynxModuleMetaList1;
  }
  
  public ScannedDevices awaitScannedDevices() throws InterruptedException {
    ScannedDevices scannedDevices2 = (ScannedDevices)this.scanningSingleton.await();
    ScannedDevices scannedDevices1 = scannedDevices2;
    if (scannedDevices2 == null) {
      RobotLog.vv("FtcConfigTag", "USBScanManager.await() returning made-up scan result");
      scannedDevices1 = new ScannedDevices();
    } 
    return scannedDevices1;
  }
  
  public DeviceManager getDeviceManager() {
    return this.deviceManager;
  }
  
  LynxModuleDiscoveryState getDiscoveryState(SerialNumber paramSerialNumber) {
    synchronized (this.lynxModuleDiscoveryStateMap) {
      LynxModuleDiscoveryState lynxModuleDiscoveryState2 = this.lynxModuleDiscoveryStateMap.get(paramSerialNumber.getString());
      LynxModuleDiscoveryState lynxModuleDiscoveryState1 = lynxModuleDiscoveryState2;
      if (lynxModuleDiscoveryState2 == null) {
        lynxModuleDiscoveryState1 = new LynxModuleDiscoveryState(paramSerialNumber);
        this.lynxModuleDiscoveryStateMap.put(paramSerialNumber.getString(), lynxModuleDiscoveryState1);
      } 
      return lynxModuleDiscoveryState1;
    } 
  }
  
  public ExecutorService getExecutorService() {
    return this.executorService;
  }
  
  public Supplier<LynxModuleMetaList> getLynxModuleMetaListSupplier(final SerialNumber serialNumber) {
    return new Supplier<LynxModuleMetaList>() {
        public LynxModuleMetaList get() {
          try {
            return (LynxModuleMetaList)USBScanManager.this.startLynxModuleEnumerationIfNecessary(serialNumber).await();
          } catch (InterruptedException interruptedException) {
            Thread.currentThread().interrupt();
            return null;
          } 
        }
      };
  }
  
  public void handleCommandDiscoverLynxModulesResponse(String paramString) throws RobotCoreException {
    RobotLog.vv("FtcConfigTag", "handleCommandDiscoverLynxModulesResponse()...");
    null = LynxModuleMetaList.fromSerializationString(paramString);
    LynxModuleDiscoveryState lynxModuleDiscoveryState = getDiscoveryState(null.serialNumber);
    synchronized (lynxModuleDiscoveryState.remoteLynxDiscoveryLock) {
      lynxModuleDiscoveryState.remoteLynxModules = null;
      lynxModuleDiscoveryState.lynxDiscoverySequence.advanceNext();
      RobotLog.vv("FtcConfigTag", "...handleCommandDiscoverLynxModulesResponse()");
      return;
    } 
  }
  
  public void handleCommandScanResponse(String paramString) throws RobotCoreException {
    RobotLog.vv("FtcConfigTag", "handleCommandScanResponse()...");
    null = ScannedDevices.fromSerializationString(paramString);
    synchronized (this.remoteScannedDevicesLock) {
      this.remoteScannedDevices = null;
      this.scanResultsSequence.advanceNext();
      RobotLog.vv("FtcConfigTag", "...handleCommandScanResponse()");
      return;
    } 
  }
  
  public String packageCommandResponse(LynxModuleMetaList paramLynxModuleMetaList) {
    return paramLynxModuleMetaList.toSerializationString();
  }
  
  public String packageCommandResponse(ScannedDevices paramScannedDevices) {
    return paramScannedDevices.toSerializationString();
  }
  
  public ThreadPool.SingletonResult<ScannedDevices> startDeviceScanIfNecessary() {
    return this.scanningSingleton.submit(4000, new Callable<ScannedDevices>() {
          public ScannedDevices call() throws InterruptedException {
            Exception exception;
            if (USBScanManager.this.isRemoteConfig) {
              NextLock.Waiter waiter = USBScanManager.this.scanResultsSequence.getNextWaiter();
              RobotLog.vv("FtcConfigTag", "sending remote scan request...");
              USBScanManager.this.networkConnectionHandler.sendCommand(new Command("CMD_SCAN"));
              waiter.awaitNext();
              RobotLog.vv("FtcConfigTag", "...remote scan request completed.");
              synchronized (USBScanManager.this.remoteScannedDevicesLock) {
                return USBScanManager.this.remoteScannedDevices;
              } 
            } 
            RobotLog.vv("FtcConfigTag", "scanning USB bus...");
            try {
              String str;
              ScannedDevices scannedDevices = USBScanManager.this.deviceManager.scanForUsbDevices();
              if (scannedDevices == null) {
                str = "null";
              } else {
                str = scannedDevices.keySet().toString();
              } 
              RobotLog.vv("FtcConfigTag", ".. scanning complete: %s", new Object[] { str });
              return scannedDevices;
            } catch (RobotCoreException robotCoreException) {
              RobotLog.ee("FtcConfigTag", (Throwable)robotCoreException, "USB bus scan threw exception");
              RobotLog.vv("FtcConfigTag", ".. scanning complete: %s", new Object[] { "null" });
              return null;
            } finally {}
            RobotLog.vv("FtcConfigTag", ".. scanning complete: %s", new Object[] { "null" });
            throw exception;
          }
        });
  }
  
  public void startExecutorService() {
    this.executorService = ThreadPool.newCachedThreadPool("USBScanManager");
    this.scanningSingleton.reset();
    this.scanningSingleton.setService(this.executorService);
    Iterator<LynxModuleDiscoveryState> iterator = this.lynxModuleDiscoveryStateMap.values().iterator();
    while (iterator.hasNext())
      ((LynxModuleDiscoveryState)iterator.next()).startExecutorService(); 
  }
  
  public ThreadPool.SingletonResult<LynxModuleMetaList> startLynxModuleEnumerationIfNecessary(final SerialNumber serialNumber) {
    final LynxModuleDiscoveryState discoveryState = getDiscoveryState(serialNumber);
    return lynxModuleDiscoveryState.lynxDiscoverySingleton.submit(4000, new Callable<LynxModuleMetaList>() {
          public LynxModuleMetaList call() throws InterruptedException {
            // Byte code:
            //   0: aload_0
            //   1: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/USBScanManager;
            //   4: getfield isRemoteConfig : Z
            //   7: ifeq -> 92
            //   10: aload_0
            //   11: getfield val$discoveryState : Lcom/qualcomm/ftccommon/configuration/USBScanManager$LynxModuleDiscoveryState;
            //   14: getfield lynxDiscoverySequence : Lcom/qualcomm/robotcore/util/NextLock;
            //   17: invokevirtual getNextWaiter : ()Lcom/qualcomm/robotcore/util/NextLock$Waiter;
            //   20: astore_1
            //   21: ldc 'FtcConfigTag'
            //   23: ldc 'sending remote lynx module discovery request...'
            //   25: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
            //   28: aload_0
            //   29: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/USBScanManager;
            //   32: getfield networkConnectionHandler : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
            //   35: new com/qualcomm/robotcore/robocol/Command
            //   38: dup
            //   39: ldc 'CMD_DISCOVER_LYNX_MODULES'
            //   41: aload_0
            //   42: getfield val$serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   45: invokevirtual getString : ()Ljava/lang/String;
            //   48: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
            //   51: invokevirtual sendCommand : (Lcom/qualcomm/robotcore/robocol/Command;)V
            //   54: aload_1
            //   55: invokevirtual awaitNext : ()V
            //   58: ldc 'FtcConfigTag'
            //   60: ldc '...remote scan lynx module discovery completed.'
            //   62: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
            //   65: aload_0
            //   66: getfield val$discoveryState : Lcom/qualcomm/ftccommon/configuration/USBScanManager$LynxModuleDiscoveryState;
            //   69: getfield remoteLynxDiscoveryLock : Ljava/lang/Object;
            //   72: astore_1
            //   73: aload_1
            //   74: monitorenter
            //   75: aload_0
            //   76: getfield val$discoveryState : Lcom/qualcomm/ftccommon/configuration/USBScanManager$LynxModuleDiscoveryState;
            //   79: getfield remoteLynxModules : Lcom/qualcomm/robotcore/hardware/LynxModuleMetaList;
            //   82: astore_2
            //   83: aload_1
            //   84: monitorexit
            //   85: aload_2
            //   86: areturn
            //   87: astore_2
            //   88: aload_1
            //   89: monitorexit
            //   90: aload_2
            //   91: athrow
            //   92: ldc 'FtcConfigTag'
            //   94: ldc 'discovering lynx modules on lynx device=%s...'
            //   96: iconst_1
            //   97: anewarray java/lang/Object
            //   100: dup
            //   101: iconst_0
            //   102: aload_0
            //   103: getfield val$serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   106: aastore
            //   107: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   110: aload_0
            //   111: getfield this$0 : Lcom/qualcomm/ftccommon/configuration/USBScanManager;
            //   114: getfield deviceManager : Lcom/qualcomm/robotcore/hardware/DeviceManager;
            //   117: aload_0
            //   118: getfield val$serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   121: aconst_null
            //   122: invokeinterface createLynxUsbDevice : (Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/lang/String;)Lcom/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice;
            //   127: astore_3
            //   128: aload_3
            //   129: invokeinterface discoverModules : ()Lcom/qualcomm/robotcore/hardware/LynxModuleMetaList;
            //   134: astore_2
            //   135: aload_3
            //   136: ifnull -> 154
            //   139: aload_2
            //   140: astore_1
            //   141: aload_3
            //   142: invokeinterface close : ()V
            //   147: goto -> 154
            //   150: astore_3
            //   151: goto -> 216
            //   154: aload_2
            //   155: ifnonnull -> 164
            //   158: ldc 'null'
            //   160: astore_1
            //   161: goto -> 169
            //   164: aload_2
            //   165: invokevirtual toString : ()Ljava/lang/String;
            //   168: astore_1
            //   169: ldc 'FtcConfigTag'
            //   171: ldc '...discovering lynx modules complete: %s'
            //   173: iconst_1
            //   174: anewarray java/lang/Object
            //   177: dup
            //   178: iconst_0
            //   179: aload_1
            //   180: aastore
            //   181: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   184: aload_2
            //   185: areturn
            //   186: astore_2
            //   187: aload_3
            //   188: astore_1
            //   189: goto -> 195
            //   192: astore_2
            //   193: aconst_null
            //   194: astore_1
            //   195: aload_1
            //   196: ifnull -> 205
            //   199: aload_1
            //   200: invokeinterface close : ()V
            //   205: aload_2
            //   206: athrow
            //   207: astore_1
            //   208: aconst_null
            //   209: astore_2
            //   210: goto -> 284
            //   213: astore_3
            //   214: aconst_null
            //   215: astore_2
            //   216: aload_2
            //   217: astore_1
            //   218: new java/lang/StringBuilder
            //   221: dup
            //   222: invokespecial <init> : ()V
            //   225: astore #4
            //   227: aload_2
            //   228: astore_1
            //   229: aload #4
            //   231: ldc 'discovering lynx modules threw exception: '
            //   233: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   236: pop
            //   237: aload_2
            //   238: astore_1
            //   239: aload #4
            //   241: aload_3
            //   242: invokevirtual toString : ()Ljava/lang/String;
            //   245: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   248: pop
            //   249: aload_2
            //   250: astore_1
            //   251: ldc 'FtcConfigTag'
            //   253: aload #4
            //   255: invokevirtual toString : ()Ljava/lang/String;
            //   258: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
            //   261: ldc 'FtcConfigTag'
            //   263: ldc '...discovering lynx modules complete: %s'
            //   265: iconst_1
            //   266: anewarray java/lang/Object
            //   269: dup
            //   270: iconst_0
            //   271: ldc 'null'
            //   273: aastore
            //   274: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   277: aconst_null
            //   278: areturn
            //   279: astore_3
            //   280: aload_1
            //   281: astore_2
            //   282: aload_3
            //   283: astore_1
            //   284: aload_2
            //   285: ifnonnull -> 294
            //   288: ldc 'null'
            //   290: astore_2
            //   291: goto -> 299
            //   294: aload_2
            //   295: invokevirtual toString : ()Ljava/lang/String;
            //   298: astore_2
            //   299: ldc 'FtcConfigTag'
            //   301: ldc '...discovering lynx modules complete: %s'
            //   303: iconst_1
            //   304: anewarray java/lang/Object
            //   307: dup
            //   308: iconst_0
            //   309: aload_2
            //   310: aastore
            //   311: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
            //   314: aload_1
            //   315: athrow
            // Exception table:
            //   from	to	target	type
            //   75	85	87	finally
            //   88	90	87	finally
            //   110	128	192	finally
            //   128	135	186	finally
            //   141	147	150	com/qualcomm/robotcore/exception/RobotCoreException
            //   141	147	279	finally
            //   199	205	213	com/qualcomm/robotcore/exception/RobotCoreException
            //   199	205	207	finally
            //   205	207	213	com/qualcomm/robotcore/exception/RobotCoreException
            //   205	207	207	finally
            //   218	227	279	finally
            //   229	237	279	finally
            //   239	249	279	finally
            //   251	261	279	finally
          }
        });
  }
  
  public void stopExecutorService() {
    this.executorService.shutdownNow();
    ThreadPool.awaitTerminationOrExitApplication(this.executorService, 5L, TimeUnit.SECONDS, "USBScanManager service", "internal error");
    this.executorService = null;
  }
  
  protected class LynxModuleDiscoveryState {
    protected NextLock lynxDiscoverySequence = new NextLock();
    
    protected ThreadPool.Singleton<LynxModuleMetaList> lynxDiscoverySingleton = new ThreadPool.Singleton();
    
    protected final Object remoteLynxDiscoveryLock = new Object();
    
    protected LynxModuleMetaList remoteLynxModules;
    
    protected SerialNumber serialNumber;
    
    protected LynxModuleDiscoveryState(SerialNumber param1SerialNumber) {
      this.serialNumber = param1SerialNumber;
      this.remoteLynxModules = new LynxModuleMetaList(param1SerialNumber);
      startExecutorService();
    }
    
    protected void startExecutorService() {
      ExecutorService executorService = USBScanManager.this.executorService;
      if (executorService != null) {
        this.lynxDiscoverySingleton.reset();
        this.lynxDiscoverySingleton.setService(executorService);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\USBScanManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */