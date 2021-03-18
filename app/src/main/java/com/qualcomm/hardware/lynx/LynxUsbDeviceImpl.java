package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.standard.LynxDiscoveryCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxDiscoveryResponse;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LynxModuleMeta;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbDeviceFtdi;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.TypeConversion;
import com.qualcomm.robotcore.util.Util;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbFTDIException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbUnspecifiedException;

public class LynxUsbDeviceImpl extends ArmableUsbDevice implements LynxUsbDevice {
  public static boolean DEBUG_LOG_DATAGRAMS = false;
  
  public static boolean DEBUG_LOG_DATAGRAMS_FINISH = false;
  
  public static boolean DEBUG_LOG_DATAGRAMS_LOCK = false;
  
  public static boolean DEBUG_LOG_MESSAGES = false;
  
  protected static final String SEPARATOR = " / ";
  
  public static final String TAG = "LynxUsb";
  
  protected static final int cbusBothAsserted = 0;
  
  protected static final int cbusMask = 3;
  
  protected static final int cbusNProg = 2;
  
  protected static final int cbusNReset = 1;
  
  protected static final int cbusNeitherAsserted = 3;
  
  protected static final int cbusProgAsserted = 1;
  
  protected static final int cbusResetAsserted = 2;
  
  protected static final LynxCommExceptionHandler exceptionHandler;
  
  protected static final WeakReferenceSet<LynxUsbDeviceImpl> extantDevices;
  
  protected static final int msCbusWiggle = 75;
  
  protected static final int msNetworkTransmissionLockAcquisitionTimeMax = 500;
  
  protected static final int msResetRecovery = 200;
  
  protected static long randSeed = System.nanoTime();
  
  protected final ConcurrentHashMap<Integer, LynxModuleMeta> discoveredModules = new ConcurrentHashMap<Integer, LynxModuleMeta>();
  
  protected final Object engageLock = new Object();
  
  protected boolean hasShutdownAbnormally = false;
  
  protected ExecutorService incomingDatagramPoller = null;
  
  protected boolean isEngaged = true;
  
  protected boolean isSystemSynthetic = false;
  
  protected final ConcurrentHashMap<Integer, LynxModule> knownModules = new ConcurrentHashMap<Integer, LynxModule>();
  
  protected final ConcurrentHashMap<Integer, LynxModule> knownModulesChanging = new ConcurrentHashMap<Integer, LynxModule>();
  
  protected final ConcurrentHashMap<Integer, String> missingModules = new ConcurrentHashMap<Integer, String>();
  
  protected final MessageKeyedLock networkTransmissionLock = new MessageKeyedLock("lynx xmit lock", 500);
  
  protected boolean resetAttempted = false;
  
  protected boolean wasPollingWhenEngaged = true;
  
  static {
    extantDevices = new WeakReferenceSet();
    exceptionHandler = new LynxCommExceptionHandler("LynxUsb");
  }
  
  protected LynxUsbDeviceImpl(Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice) {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice);
    extantDevices.add(this);
    finishConstruction();
  }
  
  protected static RobotUsbDeviceFtdi accessCBus(RobotUsbDevice paramRobotUsbDevice) {
    if (paramRobotUsbDevice instanceof RobotUsbDeviceFtdi) {
      RobotUsbDeviceFtdi robotUsbDeviceFtdi = (RobotUsbDeviceFtdi)paramRobotUsbDevice;
      if (robotUsbDeviceFtdi.supportsCbusBitbang())
        return robotUsbDeviceFtdi; 
    } 
    RobotLog.ee("LynxUsb", "accessCBus() unexpectedly failed; ignoring");
    return null;
  }
  
  public static boolean enterFirmwareUpdateModeControlHub() {
    RobotLog.vv("LynxModule", "enterFirmwareUpdateModeControlHub()");
    if (LynxConstants.isRevControlHub())
      try {
        boolean bool = AndroidBoard.getInstance().getAndroidBoardIsPresentPin().getState();
        RobotLog.vv("LynxModule", "fw update embedded usb device: isPresent: was=%s", new Object[] { Boolean.valueOf(bool) });
        if (!bool) {
          AndroidBoard.getInstance().getAndroidBoardIsPresentPin().setState(true);
          Thread.sleep(75L);
        } 
        AndroidBoard.getInstance().getProgrammingPin().setState(true);
        long l = 75L;
        Thread.sleep(l);
        AndroidBoard.getInstance().getLynxModuleResetPin().setState(true);
        Thread.sleep(l);
        AndroidBoard.getInstance().getLynxModuleResetPin().setState(false);
        Thread.sleep(l);
        AndroidBoard.getInstance().getProgrammingPin().setState(false);
        Thread.sleep(l);
        return true;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return false;
      }  
    RobotLog.ee("LynxUsb", "enterFirmwareUpdateModeControlHub() issued on non-Control Hub");
    return false;
  }
  
  public static boolean enterFirmwareUpdateModeUSB(RobotUsbDevice paramRobotUsbDevice) {
    RobotLog.vv("LynxModule", "enterFirmwareUpdateModeUSB() serial=%s", new Object[] { paramRobotUsbDevice.getSerialNumber() });
    if (!LynxConstants.isEmbeddedSerialNumber(paramRobotUsbDevice.getSerialNumber())) {
      RobotUsbDeviceFtdi robotUsbDeviceFtdi = accessCBus(paramRobotUsbDevice);
      if (robotUsbDeviceFtdi != null) {
        try {
          robotUsbDeviceFtdi.cbus_setup(3, 3);
          long l = 75L;
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(1);
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(0);
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(1);
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(3);
          Thread.sleep(200L);
          return true;
        } catch (InterruptedException interruptedException) {
        
        } catch (RobotUsbException robotUsbException) {}
        exceptionHandler.handleException((Exception)robotUsbException);
        return false;
      } 
      RobotLog.ee("LynxUsb", "enterFirmwareUpdateModeUSB() can't access FTDI device");
      return false;
    } 
    RobotLog.ee("LynxUsb", "enterFirmwareUpdateModeUSB() issued on Control Hub's embedded Expansion Hub");
    return false;
  }
  
  public static LynxUsbDevice findOrCreateAndArm(Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice) throws RobotCoreException, InterruptedException {
    synchronized (extantDevices) {
      LynxUsbDeviceDelegate lynxUsbDeviceDelegate;
      for (LynxUsbDeviceImpl lynxUsbDeviceImpl1 : extantDevices) {
        if (lynxUsbDeviceImpl1.getSerialNumber().equals(paramSerialNumber) && lynxUsbDeviceImpl1.getArmingState() != RobotArmingStateNotifier.ARMINGSTATE.CLOSED) {
          lynxUsbDeviceImpl1.addRef();
          RobotLog.vv("LynxUsb", "using existing [%s]: 0x%08x", new Object[] { paramSerialNumber, Integer.valueOf(lynxUsbDeviceImpl1.hashCode()) });
          lynxUsbDeviceDelegate = new LynxUsbDeviceDelegate(lynxUsbDeviceImpl1);
          return lynxUsbDeviceDelegate;
        } 
      } 
      LynxUsbDeviceImpl lynxUsbDeviceImpl = new LynxUsbDeviceImpl((Context)lynxUsbDeviceDelegate, paramSerialNumber, paramManager, paramOpenRobotUsbDevice);
      RobotLog.vv("LynxUsb", "creating new [%s]: 0x%08x", new Object[] { paramSerialNumber, Integer.valueOf(lynxUsbDeviceImpl.hashCode()) });
      lynxUsbDeviceImpl.armOrPretend();
      return new LynxUsbDeviceDelegate(lynxUsbDeviceImpl);
    } 
  }
  
  public static void resetDevice(RobotUsbDevice paramRobotUsbDevice) {
    RobotLog.vv("LynxModule", "resetDevice() serial=%s", new Object[] { paramRobotUsbDevice.getSerialNumber() });
    try {
      if (LynxConstants.isEmbeddedSerialNumber(paramRobotUsbDevice.getSerialNumber())) {
        boolean bool = AndroidBoard.getInstance().getAndroidBoardIsPresentPin().getState();
        RobotLog.vv("LynxModule", "resetting embedded usb device: isPresent: was=%s", new Object[] { Boolean.valueOf(bool) });
        if (!bool) {
          AndroidBoard.getInstance().getAndroidBoardIsPresentPin().setState(true);
          Thread.sleep(75L);
        } 
        AndroidBoard.getInstance().getLynxModuleResetPin().setState(true);
        long l = 75L;
        Thread.sleep(l);
        AndroidBoard.getInstance().getLynxModuleResetPin().setState(false);
        Thread.sleep(l);
      } else {
        RobotUsbDeviceFtdi robotUsbDeviceFtdi = accessCBus(paramRobotUsbDevice);
        if (robotUsbDeviceFtdi != null) {
          robotUsbDeviceFtdi.cbus_setup(3, 3);
          long l = 75L;
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(2);
          Thread.sleep(l);
          robotUsbDeviceFtdi.cbus_write(3);
        } 
      } 
      Thread.sleep(200L);
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (RobotUsbException robotUsbException) {}
    exceptionHandler.handleException((Exception)robotUsbException);
  }
  
  private void setControlHubModuleAddress() throws InterruptedException, RobotCoreException {
    LynxModuleMeta lynxModuleMeta = discoverModules().getParent();
    if (lynxModuleMeta != null) {
      int i = lynxModuleMeta.getModuleAddress();
      RobotLog.vv("LynxUsb", "Found embedded module at address %d", new Object[] { Integer.valueOf(i) });
      null = new LynxModule(this, i, true);
      null.setUserModule(false);
      synchronized (this.knownModules) {
        this.knownModules.put(Integer.valueOf(null.getModuleAddress()), null);
        try {
          null.pingInitialContact();
          RobotLog.vv("LynxUsb", "Setting embedded module address to %d", new Object[] { Integer.valueOf(173) });
          null.setNewModuleAddress(173);
          return;
        } finally {
          null.close();
          removeConfiguredModule(null);
        } 
      } 
    } 
    throw new RobotCoreException("Unable to communicate with internal Expansion Hub");
  }
  
  protected void abandonUnfinishedCommands() {
    Iterator<LynxModule> iterator = getKnownModules().iterator();
    while (iterator.hasNext())
      ((LynxModule)iterator.next()).abandonUnfinishedCommands(); 
  }
  
  public void acquireNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    this.networkTransmissionLock.acquire(paramLynxMessage);
  }
  
  public void addConfiguredModule(LynxModule paramLynxModule) throws RobotCoreException, InterruptedException, LynxNackException {
    ConcurrentHashMap<Integer, LynxModule> concurrentHashMap;
    RobotLog.vv("LynxUsb", "addConfiguredModule() module#=%d", new Object[] { Integer.valueOf(paramLynxModule.getModuleAddress()) });
    synchronized (this.knownModules) {
      boolean bool;
      if (!this.knownModules.containsKey(Integer.valueOf(paramLynxModule.getModuleAddress()))) {
        this.knownModules.put(Integer.valueOf(paramLynxModule.getModuleAddress()), paramLynxModule);
        bool = true;
      } else {
        RobotLog.ee("LynxUsb", "addConfiguredModule() module#=%d: already exists; ignored", new Object[] { Integer.valueOf(paramLynxModule.getModuleAddress()) });
        bool = false;
      } 
      if (bool) {
        try {
          paramLynxModule.pingAndQueryKnownInterfacesAndEtc(randSeed);
          return;
        } catch (RobotCoreException robotCoreException) {
        
        } catch (InterruptedException interruptedException) {
        
        } catch (RuntimeException runtimeException) {}
        RobotLog.logExceptionHeader("LynxUsb", runtimeException, "addConfiguredModule() module#=%d", new Object[] { Integer.valueOf(paramLynxModule.getModuleAddress()) });
        RobotLog.ee("LynxUsb", "Unable to communicate with REV Hub #%d at robot startup. A Robot Restart will be required to use this hub.");
        paramLynxModule.close();
        synchronized (this.knownModules) {
          this.knownModules.remove(Integer.valueOf(paramLynxModule.getModuleAddress()));
          throw runtimeException;
        } 
      } 
      return;
    } 
  }
  
  protected void armDevice(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException, InterruptedException {
    synchronized (this.armingLock) {
      boolean bool;
      RobotLog.vv("LynxUsb", "armDevice() serial=%s...", new Object[] { this.serialNumber });
      if (paramRobotUsbDevice != null) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      this.robotUsbDevice = paramRobotUsbDevice;
      if (!this.resetAttempted) {
        this.resetAttempted = true;
        resetDevice(this.robotUsbDevice);
      } 
      this.hasShutdownAbnormally = false;
      if (this.syncdDeviceManager != null)
        this.syncdDeviceManager.registerSyncdDevice(this); 
      resetNetworkTransmissionLock();
      startPollingForIncomingDatagrams();
      pingAndQueryKnownInterfaces();
      startRegularPinging();
      RobotLog.vv("LynxUsb", "...done armDevice()");
      return;
    } 
  }
  
  public void changeModuleAddress(LynxModule paramLynxModule, int paramInt, Runnable paramRunnable) {
    int i = paramLynxModule.getModuleAddress();
    if (paramInt != i)
      synchronized (this.knownModules) {
        this.knownModulesChanging.put(Integer.valueOf(paramInt), paramLynxModule);
        paramRunnable.run();
        synchronized (this.knownModules) {
          this.knownModules.put(Integer.valueOf(paramInt), paramLynxModule);
          this.knownModules.remove(Integer.valueOf(i));
          this.knownModulesChanging.remove(Integer.valueOf(paramInt));
          return;
        } 
      }  
  }
  
  protected void closeModules() {
    Iterator<LynxModule> iterator = getKnownModules().iterator();
    while (iterator.hasNext())
      ((LynxModule)iterator.next()).close(); 
  }
  
  protected String composeGlobalWarning() {
    ArrayList<String> arrayList = new ArrayList();
    String str = super.composeGlobalWarning();
    arrayList.add(str);
    if (str.isEmpty()) {
      for (String str1 : this.missingModules.values()) {
        arrayList.add(AppUtil.getDefContext().getString(R.string.errorExpansionHubIsMissing, new Object[] { str1 }));
      } 
      Iterator<LynxModule> iterator = getKnownModules().iterator();
      while (iterator.hasNext())
        arrayList.addAll(((LynxModule)iterator.next()).getGlobalWarnings()); 
    } 
    return RobotLog.combineGlobalWarnings(arrayList);
  }
  
  protected void disarmDevice() throws InterruptedException {
    synchronized (this.armingLock) {
      RobotLog.vv("LynxUsb", "disarmDevice() serial=%s...", new Object[] { this.serialNumber });
      Assert.assertFalse(isArmedOrArming());
      pretendFinishExtantCommands();
      abandonUnfinishedCommands();
      stopRegularPinging();
      stopPollingForIncomingDatagrams();
      if (this.robotUsbDevice != null) {
        this.robotUsbDevice.close();
        this.robotUsbDevice = null;
      } 
      resetNetworkTransmissionLock();
      if (this.syncdDeviceManager != null)
        this.syncdDeviceManager.unregisterSyncdDevice(this); 
      RobotLog.vv("LynxUsb", "...done disarmDevice()");
      return;
    } 
  }
  
  public LynxModuleMetaList discoverModules() throws RobotCoreException, InterruptedException {
    LynxModuleMetaList lynxModuleMetaList;
    Exception exception;
    RobotLog.vv("LynxUsb", "lynx discovery beginning...transmitting LynxDiscoveryCommand()...");
    this.discoveredModules.clear();
    LynxModule lynxModule = new LynxModule(this, 0, false);
    try {
      (new LynxDiscoveryCommand(lynxModule)).send();
      long l2 = 254L * 3000000L + 50000000L + 200000000L;
      long l1 = l2 / 1000000L;
      Long.signum(l1);
      l2 -= 1000000L * l1;
      RobotLog.vv("LynxUsb", "discovery waiting %dms and %dns", new Object[] { Long.valueOf(l1), Long.valueOf(l2) });
      Thread.sleep(l1, (int)l2);
      RobotLog.vv("LynxUsb", "discovery waiting complete: #modules=%d", new Object[] { Integer.valueOf(this.discoveredModules.size()) });
      lynxModule.close();
      lynxModuleMetaList = new LynxModuleMetaList(this.serialNumber, this.discoveredModules.values());
      RobotLog.vv("LynxUsb", "...lynx discovery completed");
      return lynxModuleMetaList;
    } catch (LynxNackException null) {
      throw exception.wrap();
    } finally {}
    lynxModuleMetaList.close();
    throw exception;
  }
  
  public void disengage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield engageLock : Ljava/lang/Object;
    //   6: astore_1
    //   7: aload_1
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield isEngaged : Z
    //   13: ifeq -> 63
    //   16: aload_0
    //   17: iconst_0
    //   18: putfield isEngaged : Z
    //   21: aload_0
    //   22: invokevirtual getKnownModules : ()Ljava/util/Collection;
    //   25: invokeinterface iterator : ()Ljava/util/Iterator;
    //   30: astore_2
    //   31: aload_2
    //   32: invokeinterface hasNext : ()Z
    //   37: ifeq -> 55
    //   40: aload_2
    //   41: invokeinterface next : ()Ljava/lang/Object;
    //   46: checkcast com/qualcomm/hardware/lynx/LynxModule
    //   49: invokevirtual disengage : ()V
    //   52: goto -> 31
    //   55: aload_0
    //   56: aload_0
    //   57: invokevirtual stopPollingForIncomingDatagrams : ()Z
    //   60: putfield wasPollingWhenEngaged : Z
    //   63: aload_1
    //   64: monitorexit
    //   65: aload_0
    //   66: monitorexit
    //   67: return
    //   68: astore_2
    //   69: aload_1
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	73	finally
    //   9	31	68	finally
    //   31	52	68	finally
    //   55	63	68	finally
    //   63	65	68	finally
    //   69	71	68	finally
    //   71	73	73	finally
  }
  
  protected void doClose() {
    synchronized (extantDevices) {
      super.doClose();
      extantDevices.remove(this);
      return;
    } 
  }
  
  protected void doCloseFromArmed() throws RobotCoreException, InterruptedException {
    failSafe();
    closeModules();
    super.doCloseFromArmed();
  }
  
  protected void doCloseFromOther() throws RobotCoreException, InterruptedException {
    closeModules();
    super.doCloseFromOther();
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    RobotLog.vv("LynxUsb", "doPretend() serial=%s", new Object[] { this.serialNumber });
  }
  
  public void engage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield engageLock : Ljava/lang/Object;
    //   6: astore_1
    //   7: aload_1
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield isEngaged : Z
    //   13: ifne -> 73
    //   16: aload_0
    //   17: getfield wasPollingWhenEngaged : Z
    //   20: ifeq -> 34
    //   23: aload_0
    //   24: invokevirtual isArmed : ()Z
    //   27: ifeq -> 34
    //   30: aload_0
    //   31: invokevirtual startPollingForIncomingDatagrams : ()V
    //   34: aload_0
    //   35: invokevirtual getKnownModules : ()Ljava/util/Collection;
    //   38: invokeinterface iterator : ()Ljava/util/Iterator;
    //   43: astore_2
    //   44: aload_2
    //   45: invokeinterface hasNext : ()Z
    //   50: ifeq -> 68
    //   53: aload_2
    //   54: invokeinterface next : ()Ljava/lang/Object;
    //   59: checkcast com/qualcomm/hardware/lynx/LynxModule
    //   62: invokevirtual engage : ()V
    //   65: goto -> 44
    //   68: aload_0
    //   69: iconst_1
    //   70: putfield isEngaged : Z
    //   73: aload_1
    //   74: monitorexit
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    //   78: astore_2
    //   79: aload_1
    //   80: monitorexit
    //   81: aload_2
    //   82: athrow
    //   83: astore_1
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_1
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	83	finally
    //   9	34	78	finally
    //   34	44	78	finally
    //   44	65	78	finally
    //   68	73	78	finally
    //   73	75	78	finally
    //   79	81	78	finally
    //   81	83	83	finally
  }
  
  public void failSafe() {
    for (LynxModule lynxModule : getKnownModules()) {
      try {
        if (lynxModule.isUserModule())
          lynxModule.failSafe(); 
        continue;
      } catch (RobotCoreException robotCoreException) {
      
      } catch (LynxNackException lynxNackException) {
      
      } catch (InterruptedException interruptedException) {}
      exceptionHandler.handleException(interruptedException);
    } 
  }
  
  protected LynxModule findKnownModule(int paramInt) {
    synchronized (this.knownModules) {
      LynxModule lynxModule2 = this.knownModules.get(Integer.valueOf(paramInt));
      LynxModule lynxModule1 = lynxModule2;
      if (lynxModule2 == null)
        lynxModule1 = this.knownModulesChanging.get(Integer.valueOf(paramInt)); 
      return lynxModule1;
    } 
  }
  
  public List<String> getAllModuleFirmwareVersions() {
    ArrayList<String> arrayList = new ArrayList();
    for (LynxModule lynxModule : getKnownModules()) {
      lynxModule.getFirmwareVersionString();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(lynxModule.getModuleAddress());
      stringBuilder.append(" / ");
      stringBuilder.append(lynxModule.getFirmwareVersionString());
      arrayList.add(stringBuilder.toString());
    } 
    return arrayList;
  }
  
  public LynxModule getConfiguredModule(int paramInt) {
    synchronized (this.knownModules) {
      return this.knownModules.get(Integer.valueOf(paramInt));
    } 
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("USB ");
    stringBuilder.append(getSerialNumber());
    return stringBuilder.toString();
  }
  
  public LynxUsbDeviceImpl getDelegationTarget() {
    return this;
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.moduleDisplayNameLynxUsbDevice);
  }
  
  protected Collection<LynxModule> getKnownModules() {
    synchronized (this.knownModules) {
      return this.knownModules.values();
    } 
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  public RobotUsbModule getOwner() {
    return this;
  }
  
  public SyncdDevice.ShutdownReason getShutdownReason() {
    return (this.hasShutdownAbnormally || this.robotUsbDevice == null || !this.robotUsbDevice.isOpen()) ? SyncdDevice.ShutdownReason.ABNORMAL : SyncdDevice.ShutdownReason.NORMAL;
  }
  
  protected String getTag() {
    return "LynxUsb";
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected boolean hasShutdownAbnormally() {
    return (getShutdownReason() != SyncdDevice.ShutdownReason.NORMAL);
  }
  
  public boolean isEngaged() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield engageLock : Ljava/lang/Object;
    //   6: astore_2
    //   7: aload_2
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield isEngaged : Z
    //   13: istore_1
    //   14: aload_2
    //   15: monitorexit
    //   16: aload_0
    //   17: monitorexit
    //   18: iload_1
    //   19: ireturn
    //   20: astore_3
    //   21: aload_2
    //   22: monitorexit
    //   23: aload_3
    //   24: athrow
    //   25: astore_2
    //   26: aload_0
    //   27: monitorexit
    //   28: aload_2
    //   29: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	25	finally
    //   9	16	20	finally
    //   21	23	20	finally
    //   23	25	25	finally
  }
  
  public boolean isSystemSynthetic() {
    return this.isSystemSynthetic;
  }
  
  public void lockNetworkLockAcquisitions() {
    this.networkTransmissionLock.lockAcquisitions();
  }
  
  public void noteMissingModule(LynxModule paramLynxModule, String paramString) {
    this.missingModules.put(Integer.valueOf(paramLynxModule.getModuleAddress()), paramString);
    RobotLog.ee("LynxUsb", "module #%d did not connect at startup: skip adding its hardware items to the hardwareMap", new Object[] { Integer.valueOf(paramLynxModule.getModuleAddress()) });
  }
  
  protected void onLynxDiscoveryResponseReceived(LynxDatagram paramLynxDatagram) {
    LynxDiscoveryResponse lynxDiscoveryResponse = new LynxDiscoveryResponse();
    lynxDiscoveryResponse.setSerialization(paramLynxDatagram);
    lynxDiscoveryResponse.loadFromSerialization();
    RobotLog.vv("LynxUsb", "onLynxDiscoveryResponseReceived()... module#=%d isParent=%s", new Object[] { Integer.valueOf(lynxDiscoveryResponse.getDiscoveredModuleAddress()), Boolean.toString(lynxDiscoveryResponse.isParent()) });
    try {
    
    } finally {
      RobotLog.vv("LynxUsb", "...onLynxDiscoveryResponseReceived()");
    } 
  }
  
  protected void pingAndQueryKnownInterfaces() throws RobotCoreException, InterruptedException {
    for (LynxModule lynxModule : getKnownModules()) {
      if (lynxModule.isParent())
        lynxModule.pingAndQueryKnownInterfacesAndEtc(randSeed); 
    } 
    for (LynxModule lynxModule : getKnownModules()) {
      if (!lynxModule.isParent())
        lynxModule.pingAndQueryKnownInterfacesAndEtc(randSeed); 
    } 
  }
  
  protected void pretendFinishExtantCommands() throws InterruptedException {
    Iterator<LynxModule> iterator = getKnownModules().iterator();
    while (iterator.hasNext())
      ((LynxModule)iterator.next()).pretendFinishExtantCommands(); 
  }
  
  public void releaseNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    this.networkTransmissionLock.release(paramLynxMessage);
  }
  
  public void removeConfiguredModule(LynxModule paramLynxModule) {
    synchronized (this.knownModules) {
      if (this.knownModules.remove(Integer.valueOf(paramLynxModule.getModuleAddress())) == null)
        RobotLog.ee("LynxUsb", "removeConfiguredModule(): mod#=%d wasn't there", new Object[] { Integer.valueOf(paramLynxModule.getModuleAddress()) }); 
      return;
    } 
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  protected void resetNetworkTransmissionLock() throws InterruptedException {
    this.networkTransmissionLock.reset();
  }
  
  public boolean setControlHubModuleAddressIfNecessary() throws InterruptedException, RobotCoreException {
    if (!getSerialNumber().isEmbedded()) {
      RobotLog.ww("LynxUsb", "assertControlHubParentModuleAddress() called on non-embedded USB device");
      return false;
    } 
    LynxModule lynxModule = getConfiguredModule(173);
    ConcurrentHashMap<Integer, LynxModule> concurrentHashMap = null;
    null = lynxModule;
    if (lynxModule == null) {
      LynxModule lynxModule1;
      null = new LynxModule(this, 173, true);
      null.setUserModule(false);
      null.setSystemSynthetic(true);
      synchronized (this.knownModules) {
        this.knownModules.put(Integer.valueOf(null.getModuleAddress()), null);
        lynxModule1 = null;
      } 
    } 
    try {
      null.pingInitialContact();
      RobotLog.vv("LynxUsb", "Verified that the embedded Control Hub module has the correct address");
      return false;
    } catch (RobotCoreException robotCoreException) {
      if (concurrentHashMap != null) {
        concurrentHashMap.close();
        removeConfiguredModule((LynxModule)concurrentHashMap);
      } 
      RobotLog.ww("LynxUsb", "Unable to find embedded Control Hub module at address %d. Attempting to find module and change address.", new Object[] { Integer.valueOf(173) });
      setControlHubModuleAddress();
      return true;
    } finally {
      if (concurrentHashMap != null) {
        concurrentHashMap.close();
        removeConfiguredModule((LynxModule)concurrentHashMap);
      } 
    } 
  }
  
  public void setOwner(RobotUsbModule paramRobotUsbModule) {}
  
  public void setSystemSynthetic(boolean paramBoolean) {
    this.isSystemSynthetic = paramBoolean;
  }
  
  public void setThrowOnNetworkLockAcquisition(boolean paramBoolean) {
    this.networkTransmissionLock.throwOnLockAcquisitions(paramBoolean);
  }
  
  protected void shutdownAbnormally() {
    int i;
    this.hasShutdownAbnormally = true;
    Context context = this.context;
    if (this.robotUsbDevice.isAttached()) {
      i = R.string.warningProblemCommunicatingWithUSBDevice;
    } else {
      i = R.string.warningUSBDeviceDetached;
    } 
    setGlobalWarning(String.format(context.getString(i), new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) }));
  }
  
  protected void startPollingForIncomingDatagrams() {
    if (this.incomingDatagramPoller == null) {
      ExecutorService executorService = ThreadPool.newSingleThreadExecutor("lynx dg poller");
      this.incomingDatagramPoller = executorService;
      executorService.execute(new IncomingDatagramPoller());
    } 
  }
  
  protected void startRegularPinging() {
    Iterator<LynxModule> iterator = getKnownModules().iterator();
    while (iterator.hasNext())
      ((LynxModule)iterator.next()).startPingTimer(); 
  }
  
  protected boolean stopPollingForIncomingDatagrams() {
    boolean bool;
    if (this.incomingDatagramPoller != null) {
      bool = true;
    } else {
      bool = false;
    } 
    if (this.robotUsbDevice != null)
      this.robotUsbDevice.requestReadInterrupt(true); 
    if (this.incomingDatagramPoller != null) {
      RobotLog.vv("LynxUsb", "shutting down incoming datagrams");
      this.incomingDatagramPoller.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.incomingDatagramPoller, 5L, TimeUnit.SECONDS, "Lynx incoming datagram poller", "internal error");
      this.incomingDatagramPoller = null;
    } 
    if (this.robotUsbDevice != null)
      this.robotUsbDevice.requestReadInterrupt(false); 
    return bool;
  }
  
  void stopRegularPinging() {
    Iterator<LynxModule> iterator = getKnownModules().iterator();
    while (iterator.hasNext())
      ((LynxModule)iterator.next()).stopPingTimer(true); 
  }
  
  public void transmit(LynxMessage paramLynxMessage) throws InterruptedException {
    synchronized (this.engageLock) {
      if (isArmedOrArming() && !hasShutdownAbnormally() && this.isEngaged) {
        LynxDatagram lynxDatagram = paramLynxMessage.getSerialization();
        if (lynxDatagram != null) {
          if (DEBUG_LOG_DATAGRAMS || DEBUG_LOG_MESSAGES)
            RobotLog.vv("LynxUsb", "xmit'ing: mod=%d cmd=0x%02x(%s) msg#=%d ref#=%d ", new Object[] { Integer.valueOf(paramLynxMessage.getModuleAddress()), Integer.valueOf(paramLynxMessage.getCommandNumber()), paramLynxMessage.getClass().getSimpleName(), Integer.valueOf(paramLynxMessage.getMessageNumber()), Integer.valueOf(paramLynxMessage.getReferenceNumber()) }); 
          byte[] arrayOfByte = lynxDatagram.toByteArray();
          try {
            this.robotUsbDevice.write(arrayOfByte);
            paramLynxMessage.setNanotimeLastTransmit(System.nanoTime());
            paramLynxMessage.resetModulePingTimer();
            paramLynxMessage.noteHasBeenTransmitted();
            return;
          } catch (RobotUsbException robotUsbException) {
          
          } catch (RuntimeException runtimeException) {}
          shutdownAbnormally();
          RobotLog.ee("LynxUsb", runtimeException, "exception thrown in LynxUsbDevice.transmit");
          return;
        } 
        runtimeException.onPretendTransmit();
      } else {
        runtimeException.onPretendTransmit();
      } 
      runtimeException.noteHasBeenTransmitted();
      return;
    } 
  }
  
  class IncomingDatagramPoller implements Runnable {
    boolean isSynchronized = false;
    
    byte[] prefix = new byte[4];
    
    byte[] scratch = new byte[2];
    
    boolean stopRequested = false;
    
    LynxDatagram pollForIncomingDatagram() {
      while (true) {
        LynxDatagram lynxDatagram;
        if (!this.stopRequested && !Thread.currentThread().isInterrupted() && !LynxUsbDeviceImpl.this.hasShutdownAbnormally()) {
          try {
            if (!this.isSynchronized) {
              if (readSingleByte(this.scratch) != LynxDatagram.frameBytes[0] || readSingleByte(this.scratch) != LynxDatagram.frameBytes[1])
                continue; 
              readIncomingBytes(this.scratch, 2, null);
              System.arraycopy(LynxDatagram.frameBytes, 0, this.prefix, 0, 2);
              System.arraycopy(this.scratch, 0, this.prefix, 2, 2);
              RobotLog.vv("LynxUsb", "synchronization gained: serial=%s", new Object[] { LynxUsbDeviceImpl.access$100(this.this$0) });
              this.isSynchronized = true;
            } else {
              readIncomingBytes(this.prefix, 4, null);
              if (!LynxDatagram.beginsWithFraming(this.prefix)) {
                RobotLog.vv("LynxUsb", "synchronization lost: serial=%s", new Object[] { LynxUsbDeviceImpl.access$200(this.this$0) });
                this.isSynchronized = false;
                continue;
              } 
            } 
            int i = TypeConversion.unsignedShortToInt(TypeConversion.byteArrayToShort(this.prefix, 2, LynxDatagram.LYNX_ENDIAN)) - 4;
            byte[] arrayOfByte = new byte[i];
            TimeWindow timeWindow = new TimeWindow();
            readIncomingBytes(arrayOfByte, i, timeWindow);
            arrayOfByte = Util.concatenateByteArrays(this.prefix, arrayOfByte);
            lynxDatagram = new LynxDatagram();
            lynxDatagram.setPayloadTimeWindow(timeWindow);
            lynxDatagram.fromByteArray(arrayOfByte);
            if (lynxDatagram.isChecksumValid()) {
              if (LynxUsbDeviceImpl.DEBUG_LOG_DATAGRAMS) {
                RobotLog.vv("LynxUsb", "rec'd: mod=%d cmd=0x%02x msg#=%d ref#=%d ", new Object[] { Integer.valueOf(lynxDatagram.getSourceModuleAddress()), Integer.valueOf(lynxDatagram.getPacketId()), Integer.valueOf(lynxDatagram.getMessageNumber()), Integer.valueOf(lynxDatagram.getReferenceNumber()) });
                return lynxDatagram;
              } 
            } else {
              RobotLog.ee("LynxUsb", "invalid checksum received; message ignored");
              continue;
            } 
          } catch (RobotUsbFTDIException|org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbDeviceClosedException|RuntimeException robotUsbFTDIException) {
            RobotLog.vv("LynxUsb", "device closed in incoming datagram loop");
            LynxUsbDeviceImpl.this.shutdownAbnormally();
            LynxUsbDeviceImpl.this.robotUsbDevice.close();
            try {
              LynxUsbDeviceImpl.this.pretendFinishExtantCommands();
            } catch (InterruptedException interruptedException) {
              this.stopRequested = true;
            } 
            continue;
          } catch (RobotUsbException robotUsbException) {
            RobotLog.vv("LynxUsb", (Throwable)robotUsbException, "exception thrown in incoming datagram loop; ignored");
            continue;
          } catch (RobotCoreException robotCoreException) {
            RobotLog.vv("LynxUsb", (Throwable)robotCoreException, "exception thrown in incoming datagram loop; ignored");
            continue;
          } catch (InterruptedException interruptedException) {
            this.stopRequested = true;
            continue;
          } 
        } else {
          return null;
        } 
        return lynxDatagram;
      } 
    }
    
    void readIncomingBytes(byte[] param1ArrayOfbyte, int param1Int, TimeWindow param1TimeWindow) throws InterruptedException, RobotUsbException {
      int i = LynxUsbDeviceImpl.this.robotUsbDevice.read(param1ArrayOfbyte, 0, param1Int, 2147483647L, param1TimeWindow);
      if (i == param1Int)
        return; 
      if (i == 0) {
        RobotLog.ee("LynxUsb", "readIncomingBytes() cbToRead=%d cbRead=%d: throwing InterruptedException", new Object[] { Integer.valueOf(param1Int), Integer.valueOf(i) });
        throw new InterruptedException("interrupt during robotUsbDevice.read()");
      } 
      RobotLog.ee("LynxUsb", "readIncomingBytes() cbToRead=%d cbRead=%d: throwing RobotCoreException", new Object[] { Integer.valueOf(param1Int), Integer.valueOf(i) });
      throw new RobotUsbUnspecifiedException("readIncomingBytes() cbToRead=%d cbRead=%d", new Object[] { Integer.valueOf(param1Int), Integer.valueOf(i) });
    }
    
    byte readSingleByte(byte[] param1ArrayOfbyte) throws InterruptedException, RobotUsbException {
      readIncomingBytes(param1ArrayOfbyte, 1, null);
      return param1ArrayOfbyte[0];
    }
    
    public void run() {
      ThreadPool.logThreadLifeCycle("lynx incoming datagrams", new Runnable() {
            public void run() {
              Thread.currentThread().setPriority(6);
              while (!LynxUsbDeviceImpl.IncomingDatagramPoller.this.stopRequested && !Thread.currentThread().isInterrupted() && !LynxUsbDeviceImpl.this.hasShutdownAbnormally()) {
                LynxDatagram lynxDatagram = LynxUsbDeviceImpl.IncomingDatagramPoller.this.pollForIncomingDatagram();
                if (lynxDatagram != null) {
                  if (lynxDatagram.getPacketId() == LynxDiscoveryResponse.getStandardCommandNumber()) {
                    LynxUsbDeviceImpl.this.onLynxDiscoveryResponseReceived(lynxDatagram);
                    continue;
                  } 
                  LynxModule lynxModule = LynxUsbDeviceImpl.this.findKnownModule(lynxDatagram.getSourceModuleAddress());
                  if (lynxModule != null)
                    lynxModule.onIncomingDatagramReceived(lynxDatagram); 
                } 
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      Thread.currentThread().setPriority(6);
      while (!this.this$1.stopRequested && !Thread.currentThread().isInterrupted() && !LynxUsbDeviceImpl.this.hasShutdownAbnormally()) {
        LynxDatagram lynxDatagram = this.this$1.pollForIncomingDatagram();
        if (lynxDatagram != null) {
          if (lynxDatagram.getPacketId() == LynxDiscoveryResponse.getStandardCommandNumber()) {
            LynxUsbDeviceImpl.this.onLynxDiscoveryResponseReceived(lynxDatagram);
            continue;
          } 
          LynxModule lynxModule = LynxUsbDeviceImpl.this.findKnownModule(lynxDatagram.getSourceModuleAddress());
          if (lynxModule != null)
            lynxModule.onIncomingDatagramReceived(lynxDatagram); 
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxUsbDeviceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */