package com.qualcomm.hardware.lynx;

import android.graphics.Color;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxDatagram;
import com.qualcomm.hardware.lynx.commands.LynxInterface;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.hardware.lynx.commands.LynxRespondable;
import com.qualcomm.hardware.lynx.commands.LynxResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxDekaInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxFtdiResetControlCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxPhoneChargeControlCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxPhoneChargeQueryCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxPhoneChargeQueryResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxReadVersionStringCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxReadVersionStringResponse;
import com.qualcomm.hardware.lynx.commands.standard.LynxAck;
import com.qualcomm.hardware.lynx.commands.standard.LynxDiscoveryCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxFailSafeCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxGetModuleLEDColorCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxGetModuleStatusCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxGetModuleStatusResponse;
import com.qualcomm.hardware.lynx.commands.standard.LynxKeepAliveCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.hardware.lynx.commands.standard.LynxQueryInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxQueryInterfaceResponse;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetDebugLogLevelCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDColorCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetModuleLEDPatternCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxSetNewModuleAddressCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxStandardCommand;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDeviceHealth;
import com.qualcomm.robotcore.hardware.RobotConfigNameable;
import com.qualcomm.robotcore.hardware.VisuallyIdentifiableHardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.TypeConversion;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.robotcore.external.navigation.TempUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.usb.LynxModuleSerialNumber;

public class LynxModule extends LynxCommExceptionHandler implements LynxModuleIntf, RobotArmingStateNotifier, RobotArmingStateNotifier.Callback, Blinker, VisuallyIdentifiableHardwareDevice {
  public static final String TAG = "LynxModule";
  
  public static BlinkerPolicy blinkerPolicy = new CountModuleAddressBlinkerPolicy();
  
  protected static final int msInitialContact = 500;
  
  protected static final int msKeepAliveTimeout = 2500;
  
  protected static Map<Class<? extends LynxCommand>, MessageClassAndCtor> responseClasses;
  
  protected static Map<Integer, MessageClassAndCtor> standardMessages = new HashMap<Integer, MessageClassAndCtor>();
  
  protected Future<?> attentionRequiredFuture;
  
  protected Map<String, List<LynxDekaInterfaceCommand<?>>> bulkCachingHistory;
  
  protected final Object bulkCachingLock;
  
  protected BulkCachingMode bulkCachingMode;
  
  protected final ConcurrentHashMap<Integer, MessageClassAndCtor> commandClasses;
  
  protected List<LynxController> controllers;
  
  protected ArrayList<Blinker.Step> currentSteps;
  
  protected final Object engagementLock = this;
  
  protected ScheduledExecutorService executor;
  
  protected boolean ftdiResetWatchdogActive;
  
  protected boolean ftdiResetWatchdogActiveWhenEngaged;
  
  protected final Object futureLock;
  
  protected final Object i2cLock;
  
  protected final ConcurrentHashMap<String, LynxInterface> interfacesQueried;
  
  protected boolean isEngaged;
  
  protected volatile boolean isNotResponding = false;
  
  protected boolean isOpen;
  
  protected boolean isParent;
  
  protected boolean isSystemSynthetic;
  
  protected boolean isUserModule;
  
  protected boolean isVisuallyIdentifying;
  
  protected BulkData lastBulkData;
  
  protected LynxUsbDevice lynxUsbDevice;
  
  protected int moduleAddress;
  
  protected SerialNumber moduleSerialNumber;
  
  protected AtomicInteger nextMessageNumber;
  
  protected Future<?> pingFuture;
  
  protected Deque<ArrayList<Blinker.Step>> previousSteps;
  
  protected final Object startStopLock;
  
  protected final Set<Class<? extends LynxCommand>> supportedCommands;
  
  protected final ConcurrentHashMap<Integer, LynxRespondable> unfinishedCommands;
  
  static {
    responseClasses = new HashMap<Class<? extends LynxCommand>, MessageClassAndCtor>();
    addStandardMessage((Class)LynxAck.class);
    addStandardMessage((Class)LynxNack.class);
    addStandardMessage((Class)LynxKeepAliveCommand.class);
    addStandardMessage((Class)LynxGetModuleStatusCommand.class);
    addStandardMessage((Class)LynxFailSafeCommand.class);
    addStandardMessage((Class)LynxSetNewModuleAddressCommand.class);
    addStandardMessage((Class)LynxQueryInterfaceCommand.class);
    addStandardMessage((Class)LynxSetNewModuleAddressCommand.class);
    addStandardMessage((Class)LynxSetModuleLEDColorCommand.class);
    addStandardMessage((Class)LynxGetModuleLEDColorCommand.class);
    correlateStandardResponse((Class)LynxGetModuleStatusCommand.class);
    correlateStandardResponse((Class)LynxQueryInterfaceCommand.class);
    correlateStandardResponse((Class)LynxGetModuleLEDColorCommand.class);
  }
  
  public LynxModule(LynxUsbDevice paramLynxUsbDevice, int paramInt, boolean paramBoolean) {
    this.lynxUsbDevice = paramLynxUsbDevice;
    this.controllers = new CopyOnWriteArrayList<LynxController>();
    this.moduleAddress = paramInt;
    this.moduleSerialNumber = (SerialNumber)new LynxModuleSerialNumber(paramLynxUsbDevice.getSerialNumber(), paramInt);
    this.isParent = paramBoolean;
    this.isSystemSynthetic = false;
    this.isEngaged = true;
    this.isUserModule = true;
    this.isOpen = true;
    this.startStopLock = new Object();
    this.nextMessageNumber = new AtomicInteger(0);
    this.commandClasses = new ConcurrentHashMap<Integer, MessageClassAndCtor>(standardMessages);
    this.supportedCommands = new HashSet<Class<? extends LynxCommand>>();
    for (MessageClassAndCtor messageClassAndCtor : this.commandClasses.values()) {
      if (ClassUtil.inheritsFrom(messageClassAndCtor.clazz, LynxCommand.class))
        this.supportedCommands.add(messageClassAndCtor.clazz); 
    } 
    this.interfacesQueried = new ConcurrentHashMap<String, LynxInterface>();
    this.unfinishedCommands = new ConcurrentHashMap<Integer, LynxRespondable>();
    this.i2cLock = new Object();
    this.currentSteps = new ArrayList<Blinker.Step>();
    this.previousSteps = new ArrayDeque<ArrayList<Blinker.Step>>();
    this.isVisuallyIdentifying = false;
    this.executor = null;
    this.pingFuture = null;
    this.attentionRequiredFuture = null;
    this.futureLock = new Object();
    this.ftdiResetWatchdogActive = false;
    this.ftdiResetWatchdogActiveWhenEngaged = false;
    this.bulkCachingMode = BulkCachingMode.OFF;
    this.bulkCachingHistory = new HashMap<String, List<LynxDekaInterfaceCommand<?>>>();
    this.bulkCachingLock = new Object();
    startExecutor();
    this.lynxUsbDevice.registerCallback(this, false);
  }
  
  protected static void addStandardMessage(Class<? extends LynxMessage> paramClass) {
    try {
      boolean bool;
      Integer integer = (Integer)LynxMessage.invokeStaticNullaryMethod(paramClass, "getStandardCommandNumber");
      if ((integer.intValue() & 0x8000) == 0) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      MessageClassAndCtor messageClassAndCtor = new MessageClassAndCtor();
      messageClassAndCtor.clazz = paramClass;
      messageClassAndCtor.assignCtor();
      standardMessages.put(integer, messageClassAndCtor);
      return;
    } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException noSuchMethodException) {
      RobotLog.ee("LynxModule", "error registering %s", new Object[] { paramClass.getSimpleName() });
      return;
    } 
  }
  
  public static void correlateResponse(Class<? extends LynxCommand> paramClass, Class<? extends LynxResponse> paramClass1) throws NoSuchMethodException {
    MessageClassAndCtor messageClassAndCtor = new MessageClassAndCtor();
    messageClassAndCtor.clazz = (Class)paramClass1;
    messageClassAndCtor.assignCtor();
    responseClasses.put(paramClass, messageClassAndCtor);
  }
  
  protected static void correlateStandardResponse(Class<? extends LynxCommand> paramClass) {
    try {
      correlateResponse(paramClass, LynxCommand.getResponseClass(paramClass));
      return;
    } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException noSuchMethodException) {
      RobotLog.ee("LynxModule", "error registering response to %s", new Object[] { paramClass.getSimpleName() });
      return;
    } 
  }
  
  public static String getHealthStatusWarningMessage(HardwareDeviceHealth paramHardwareDeviceHealth) {
    if (null.$SwitchMap$com$qualcomm$robotcore$hardware$HardwareDeviceHealth$HealthStatus[paramHardwareDeviceHealth.getHealthStatus().ordinal()] != 1)
      return ""; 
    String str2 = null;
    if (paramHardwareDeviceHealth instanceof RobotConfigNameable) {
      String str = ((RobotConfigNameable)paramHardwareDeviceHealth).getUserConfiguredName();
      str2 = str;
      if (str != null)
        str2 = AppUtil.getDefContext().getString(R.string.quotes, new Object[] { str }); 
    } 
    String str3 = str2;
    if (str2 == null) {
      str3 = str2;
      if (paramHardwareDeviceHealth instanceof HardwareDevice) {
        HardwareDevice hardwareDevice = (HardwareDevice)paramHardwareDeviceHealth;
        String str4 = hardwareDevice.getDeviceName();
        String str5 = hardwareDevice.getConnectionInfo();
        str3 = AppUtil.getDefContext().getString(R.string.hwDeviceDescriptionAndConnection, new Object[] { str4, str5 });
      } 
    } 
    String str1 = str3;
    if (str3 == null)
      str1 = AppUtil.getDefContext().getString(R.string.hwPoorlyNamedDevice); 
    return AppUtil.getDefContext().getString(R.string.unhealthyDevice, new Object[] { str1 });
  }
  
  public void abandonUnfinishedCommands() {
    this.unfinishedCommands.clear();
  }
  
  public <T> T acquireI2cLockWhile(Supplier<T> paramSupplier) throws InterruptedException, RobotCoreException, LynxNackException {
    synchronized (this.i2cLock) {
      paramSupplier = (Supplier<T>)paramSupplier.get();
      return (T)paramSupplier;
    } 
  }
  
  public void acquireNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    this.lynxUsbDevice.acquireNetworkTransmissionLock(paramLynxMessage);
  }
  
  public void clearBulkCache() {
    synchronized (this.bulkCachingLock) {
      Iterator<List> iterator = this.bulkCachingHistory.values().iterator();
      while (iterator.hasNext())
        ((List)iterator.next()).clear(); 
      this.lastBulkData = null;
      return;
    } 
  }
  
  public void close() {
    synchronized (this.startStopLock) {
      RobotLog.vv("LynxModule", "close(#%d)", new Object[] { Integer.valueOf(this.moduleAddress) });
      stopFtdiResetWatchdog();
      this.isOpen = false;
      stopAttentionRequired();
      stopPingTimer(true);
      stopExecutor();
      return;
    } 
  }
  
  public void disengage() {
    synchronized (this.engagementLock) {
      if (this.isEngaged) {
        RobotLog.vv("LynxModule", "disengaging lynx module #%d", new Object[] { Integer.valueOf(getModuleAddress()) });
        stopFtdiResetWatchdog(true);
        this.isEngaged = false;
        nackUnfinishedCommands();
        Iterator<LynxController> iterator = this.controllers.iterator();
        while (iterator.hasNext())
          ((LynxController)iterator.next()).disengage(); 
        nackUnfinishedCommands();
      } 
      return;
    } 
  }
  
  public void enablePhoneCharging(boolean paramBoolean) throws RobotCoreException, InterruptedException, LynxNackException {
    (new LynxPhoneChargeControlCommand(this, paramBoolean)).send();
  }
  
  public void engage() {
    synchronized (this.engagementLock) {
      if (!this.isEngaged) {
        RobotLog.vv("LynxModule", "engaging lynx module #%d", new Object[] { Integer.valueOf(getModuleAddress()) });
        Iterator<LynxController> iterator = this.controllers.iterator();
        while (iterator.hasNext())
          ((LynxController)iterator.next()).engage(); 
        this.isEngaged = true;
        if (this.ftdiResetWatchdogActiveWhenEngaged)
          startFtdiResetWatchdog(); 
      } 
      return;
    } 
  }
  
  public void failSafe() throws RobotCoreException, InterruptedException, LynxNackException {
    (new LynxFailSafeCommand(this)).send();
    forgetLastKnown();
  }
  
  public void finishedWithMessage(LynxMessage paramLynxMessage) {
    if (LynxUsbDeviceImpl.DEBUG_LOG_DATAGRAMS_FINISH)
      RobotLog.vv("LynxModule", "finishing mod=%d msg#=%d", new Object[] { Integer.valueOf(paramLynxMessage.getModuleAddress()), Integer.valueOf(paramLynxMessage.getMessageNumber()) }); 
    int i = paramLynxMessage.getMessageNumber();
    this.unfinishedCommands.remove(Integer.valueOf(i));
    paramLynxMessage.forgetSerialization();
  }
  
  protected void forgetLastKnown() {
    Iterator<LynxController> iterator = this.controllers.iterator();
    while (iterator.hasNext())
      ((LynxController)iterator.next()).forgetLastKnown(); 
  }
  
  public RobotArmingStateNotifier.ARMINGSTATE getArmingState() {
    return this.lynxUsbDevice.getArmingState();
  }
  
  public double getAuxiliaryVoltage(VoltageUnit paramVoltageUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.FIVE_VOLT_MONITOR, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramVoltageUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), VoltageUnit.MILLIVOLTS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public int getBlinkerPatternMaxLength() {
    return 16;
  }
  
  public BulkCachingMode getBulkCachingMode() {
    return this.bulkCachingMode;
  }
  
  public BulkData getBulkData() {
    synchronized (this.bulkCachingLock) {
      clearBulkCache();
      LynxGetBulkInputDataCommand lynxGetBulkInputDataCommand = new LynxGetBulkInputDataCommand(this);
      try {
        BulkData bulkData = new BulkData((LynxGetBulkInputDataResponse)lynxGetBulkInputDataCommand.sendReceive(), false);
        this.lastBulkData = bulkData;
        return bulkData;
      } catch (InterruptedException interruptedException) {
      
      } catch (RuntimeException runtimeException) {
      
      } catch (LynxNackException lynxNackException) {}
    } 
  }
  
  public String getConnectionInfo() {
    return String.format("%s; module %d", new Object[] { this.lynxUsbDevice.getConnectionInfo(), Integer.valueOf(getModuleAddress()) });
  }
  
  public double getCurrent(CurrentUnit paramCurrentUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.BATTERY_CURRENT, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramCurrentUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), CurrentUnit.MILLIAMPS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public String getDeviceName() {
    return String.format("%s (%s)", new Object[] { AppUtil.getDefContext().getString(R.string.expansionHubDisplayName), getFirmwareVersionString() });
  }
  
  public String getFirmwareVersionString() {
    String str2 = getNullableFirmwareVersionString();
    String str1 = str2;
    if (str2 == null)
      str1 = AppUtil.getDefContext().getString(R.string.lynxUnavailableFWVersionString); 
    return str1;
  }
  
  public List<String> getGlobalWarnings() {
    ArrayList<String> arrayList = new ArrayList();
    Iterator<LynxController> iterator = this.controllers.iterator();
    while (iterator.hasNext()) {
      String str = getHealthStatusWarningMessage(iterator.next());
      if (!str.isEmpty())
        arrayList.add(str); 
    } 
    return arrayList;
  }
  
  public double getGpioBusCurrent(CurrentUnit paramCurrentUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.GPIO_CURRENT, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramCurrentUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), CurrentUnit.MILLIAMPS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public double getI2cBusCurrent(CurrentUnit paramCurrentUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.I2C_BUS_CURRENT, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramCurrentUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), CurrentUnit.MILLIAMPS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public double getInputVoltage(VoltageUnit paramVoltageUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.BATTERY_MONITOR, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramVoltageUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), VoltageUnit.MILLIVOLTS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public int getInterfaceBaseCommandNumber(String paramString) {
    synchronized (this.interfacesQueried) {
      LynxInterface lynxInterface = this.interfacesQueried.get(paramString);
      if (lynxInterface == null)
        return 0; 
      if (!lynxInterface.wasNacked())
        return lynxInterface.getBaseCommandNumber().intValue(); 
      throw new IllegalArgumentException(String.format("interface \"%s\" not supported", new Object[] { paramString }));
    } 
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  public int getModuleAddress() {
    return this.moduleAddress;
  }
  
  public SerialNumber getModuleSerialNumber() {
    return this.moduleSerialNumber;
  }
  
  protected int getMsModulePingInterval() {
    return 1950;
  }
  
  protected byte getNewMessageNumber() {
    while (true) {
      byte b = (byte)this.nextMessageNumber.getAndIncrement();
      int i = TypeConversion.unsignedByteToInt(b);
      if (b != 0 && !this.unfinishedCommands.containsKey(Integer.valueOf(i)))
        return b; 
    } 
  }
  
  public String getNullableFirmwareVersionString() {
    try {
      return ((LynxReadVersionStringResponse)(new LynxReadVersionStringCommand(this)).sendReceive()).getNullableVersionString();
    } catch (LynxNackException lynxNackException) {
    
    } catch (InterruptedException interruptedException) {}
    handleException(interruptedException);
    return null;
  }
  
  public Collection<Blinker.Step> getPattern() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new java/util/ArrayList
    //   5: dup
    //   6: aload_0
    //   7: getfield currentSteps : Ljava/util/ArrayList;
    //   10: invokespecial <init> : (Ljava/util/Collection;)V
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: astore_1
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_1
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  public SerialNumber getSerialNumber() {
    return this.lynxUsbDevice.getSerialNumber();
  }
  
  protected String getTag() {
    return "LynxModule";
  }
  
  public double getTemperature(TempUnit paramTempUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(this, LynxGetADCCommand.Channel.CONTROLLER_TEMPERATURE, LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramTempUnit.fromCelsius(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue() / 10.0D);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected void initializeDebugLogging() throws RobotCoreException, InterruptedException {
    setDebug(DebugGroup.MODULELED, DebugVerbosity.HIGH);
  }
  
  protected void initializeLEDS() {
    setPattern(blinkerPolicy.getIdlePattern(this));
  }
  
  protected void internalPushPattern(Collection<Blinker.Step> paramCollection) {
    this.previousSteps.push(this.currentSteps);
    setPattern(paramCollection);
  }
  
  public boolean isCommandSupported(Class<? extends LynxCommand> paramClass) {
    synchronized (this.interfacesQueried) {
      if (this.moduleAddress == 0) {
        if (paramClass == LynxDiscoveryCommand.class)
          return true; 
      } else {
        return this.supportedCommands.contains(paramClass);
      } 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_3} */
    return bool;
  }
  
  public boolean isEngaged() {
    synchronized (this.engagementLock) {
      return this.isEngaged;
    } 
  }
  
  public boolean isNotResponding() {
    return this.isNotResponding;
  }
  
  public boolean isParent() {
    return this.isParent;
  }
  
  public boolean isPhoneChargingEnabled() throws RobotCoreException, InterruptedException, LynxNackException {
    return ((LynxPhoneChargeQueryResponse)(new LynxPhoneChargeQueryCommand(this)).sendReceive()).isChargeEnabled();
  }
  
  public boolean isSystemSynthetic() {
    return this.isSystemSynthetic;
  }
  
  public boolean isUserModule() {
    return this.isUserModule;
  }
  
  protected void nackUnfinishedCommands() {
    while (!this.unfinishedCommands.isEmpty()) {
      for (LynxRespondable lynxRespondable : this.unfinishedCommands.values()) {
        LynxNack.StandardReasonCode standardReasonCode;
        RobotLog.vv("RobotCore", "force-nacking unfinished command=%s mod=%d msg#=%d", new Object[] { lynxRespondable.getClass().getSimpleName(), Integer.valueOf(lynxRespondable.getModuleAddress()), Integer.valueOf(lynxRespondable.getMessageNumber()) });
        if (lynxRespondable.isResponseExpected()) {
          standardReasonCode = LynxNack.StandardReasonCode.ABANDONED_WAITING_FOR_RESPONSE;
        } else {
          standardReasonCode = LynxNack.StandardReasonCode.ABANDONED_WAITING_FOR_ACK;
        } 
        lynxRespondable.onNackReceived(new LynxNack(this, (LynxNack.ReasonCode)standardReasonCode));
        finishedWithMessage((LynxMessage)lynxRespondable);
      } 
    } 
  }
  
  public void noteAttentionRequired() {
    if (isUserModule())
      synchronized (this.futureLock) {
        if (this.isOpen) {
          if (this.attentionRequiredFuture != null)
            this.attentionRequiredFuture.cancel(false); 
          this.attentionRequiredFuture = this.executor.submit(new Runnable() {
                public void run() {
                  LynxModule.this.sendGetModuleStatusAndProcessResponse(true);
                }
              });
        } 
        forgetLastKnown();
        return;
      }  
  }
  
  public void noteController(LynxController paramLynxController) {
    this.controllers.add(paramLynxController);
  }
  
  public void noteDatagramReceived() {
    if (this.isNotResponding) {
      this.isNotResponding = false;
      RobotLog.vv("LynxModule", "REV Hub #%d has reconnected", new Object[] { Integer.valueOf(this.moduleAddress) });
    } 
  }
  
  public void noteNotResponding() {
    this.isNotResponding = true;
  }
  
  public void onIncomingDatagramReceived(LynxDatagram paramLynxDatagram) {
    noteDatagramReceived();
    try {
      MessageClassAndCtor messageClassAndCtor = this.commandClasses.get(Integer.valueOf(paramLynxDatagram.getCommandNumber()));
      if (messageClassAndCtor != null) {
        MessageClassAndCtor messageClassAndCtor1 = messageClassAndCtor;
        if (paramLynxDatagram.isResponse())
          messageClassAndCtor1 = responseClasses.get(messageClassAndCtor.clazz); 
        if (messageClassAndCtor1 != null) {
          LynxMessage lynxMessage = messageClassAndCtor1.ctor.newInstance(new Object[] { this });
          lynxMessage.setSerialization(paramLynxDatagram);
          lynxMessage.loadFromSerialization();
          if (LynxUsbDeviceImpl.DEBUG_LOG_MESSAGES)
            RobotLog.vv("LynxModule", "rec'd: mod=%d cmd=0x%02x(%s) msg#=%d ref#=%d", new Object[] { Integer.valueOf(paramLynxDatagram.getSourceModuleAddress()), Integer.valueOf(paramLynxDatagram.getPacketId()), lynxMessage.getClass().getSimpleName(), Integer.valueOf(lynxMessage.getMessageNumber()), Integer.valueOf(lynxMessage.getReferenceNumber()) }); 
          if (lynxMessage.isAck() || lynxMessage.isNack()) {
            LynxRespondable lynxRespondable1 = this.unfinishedCommands.get(Integer.valueOf(paramLynxDatagram.getReferenceNumber()));
            if (lynxRespondable1 != null) {
              if (lynxMessage.isNack()) {
                lynxRespondable1.onNackReceived((LynxNack)lynxMessage);
              } else {
                lynxRespondable1.onAckReceived((LynxAck)lynxMessage);
              } 
              finishedWithMessage((LynxMessage)lynxRespondable1);
              return;
            } 
            RobotLog.ee("LynxModule", "unable to find originating LynxRespondable for mod=%d msg#=%d ref#=%d", new Object[] { Integer.valueOf(paramLynxDatagram.getSourceModuleAddress()), Integer.valueOf(paramLynxDatagram.getMessageNumber()), Integer.valueOf(paramLynxDatagram.getReferenceNumber()) });
            return;
          } 
          LynxRespondable lynxRespondable = this.unfinishedCommands.get(Integer.valueOf(paramLynxDatagram.getReferenceNumber()));
          if (lynxRespondable != null) {
            Assert.assertTrue(lynxMessage.isResponse());
            lynxRespondable.onResponseReceived(lynxMessage);
            finishedWithMessage((LynxMessage)lynxRespondable);
            return;
          } 
          RobotLog.ee("LynxModule", "unable to find originating command for packetid=0x%04x msg#=%d ref#=%d", new Object[] { Integer.valueOf(paramLynxDatagram.getPacketId()), Integer.valueOf(paramLynxDatagram.getMessageNumber()), Integer.valueOf(paramLynxDatagram.getReferenceNumber()) });
          return;
        } 
      } else {
        RobotLog.ee("LynxModule", "no command class known for command=0x%02x", new Object[] { Integer.valueOf(paramLynxDatagram.getCommandNumber()) });
        return;
      } 
    } catch (InstantiationException instantiationException) {
      RobotLog.ee("LynxModule", instantiationException, "internal error in LynxModule.noteIncomingDatagramReceived()");
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (RuntimeException runtimeException) {}
  }
  
  public void onModuleStateChange(RobotArmingStateNotifier paramRobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE) {
    int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[paramARMINGSTATE.ordinal()];
  }
  
  public boolean patternStackNotEmpty() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield previousSteps : Ljava/util/Deque;
    //   6: invokeinterface size : ()I
    //   11: istore_1
    //   12: iload_1
    //   13: ifle -> 21
    //   16: iconst_1
    //   17: istore_2
    //   18: goto -> 23
    //   21: iconst_0
    //   22: istore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: iload_2
    //   26: ireturn
    //   27: astore_3
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	27	finally
  }
  
  protected void ping() {
    try {
      ping(false);
      return;
    } catch (RobotCoreException robotCoreException) {
    
    } catch (InterruptedException interruptedException) {
    
    } catch (LynxNackException lynxNackException) {
    
    } catch (RuntimeException runtimeException) {}
    handleException(runtimeException);
  }
  
  protected void ping(boolean paramBoolean) throws RobotCoreException, InterruptedException, LynxNackException {
    (new LynxKeepAliveCommand(this, paramBoolean)).send();
  }
  
  public void pingAndQueryKnownInterfacesAndEtc(long paramLong) throws RobotCoreException, InterruptedException {
    RobotLog.vv("LynxModule", "pingAndQueryKnownInterfaces mod=%d", new Object[] { Integer.valueOf(getModuleAddress()) });
    pingInitialContact();
    queryInterface(LynxDekaInterfaceCommand.theInterface);
    startFtdiResetWatchdog();
    if (isUserModule()) {
      initializeDebugLogging();
      initializeLEDS();
      if (isParent() && LynxConstants.isEmbeddedSerialNumber(getSerialNumber())) {
        RobotLog.vv("LynxModule", "setAsControlHubEmbeddedModule(mod=%d)", new Object[] { Integer.valueOf(getModuleAddress()) });
        EmbeddedControlHubModule.set(this);
      } 
    } 
  }
  
  protected void pingInitialContact() throws RobotCoreException, InterruptedException {
    ElapsedTime elapsedTime = new ElapsedTime();
    while (true) {
      if (elapsedTime.milliseconds() < 500.0D)
        try {
          ping(true);
          return;
        } catch (RobotCoreException|LynxNackException|RuntimeException robotCoreException) {
          RobotLog.vv("LynxModule", "retrying ping mod=%d", new Object[] { Integer.valueOf(getModuleAddress()) });
          continue;
        }  
      throw new RobotCoreException("initial ping contact failed: mod=%d", new Object[] { Integer.valueOf(getModuleAddress()) });
    } 
  }
  
  public boolean popPattern() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield previousSteps : Ljava/util/Deque;
    //   7: invokeinterface pop : ()Ljava/lang/Object;
    //   12: checkcast java/util/Collection
    //   15: invokevirtual setPattern : (Ljava/util/Collection;)V
    //   18: aload_0
    //   19: monitorexit
    //   20: iconst_1
    //   21: ireturn
    //   22: astore_1
    //   23: goto -> 35
    //   26: aload_0
    //   27: aconst_null
    //   28: invokevirtual setPattern : (Ljava/util/Collection;)V
    //   31: aload_0
    //   32: monitorexit
    //   33: iconst_0
    //   34: ireturn
    //   35: aload_0
    //   36: monitorexit
    //   37: aload_1
    //   38: athrow
    //   39: astore_1
    //   40: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   2	18	39	java/util/NoSuchElementException
    //   2	18	22	finally
    //   26	31	22	finally
  }
  
  public void pretendFinishExtantCommands() throws InterruptedException {
    Iterator<LynxRespondable> iterator = this.unfinishedCommands.values().iterator();
    while (iterator.hasNext())
      ((LynxRespondable)iterator.next()).pretendFinish(); 
  }
  
  public void pushPattern(Collection<Blinker.Step> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: invokevirtual visuallyIdentify : (Z)V
    //   7: aload_0
    //   8: aload_1
    //   9: invokevirtual internalPushPattern : (Ljava/util/Collection;)V
    //   12: aload_0
    //   13: monitorexit
    //   14: return
    //   15: astore_1
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_1
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	15	finally
  }
  
  protected boolean queryInterface(LynxInterface paramLynxInterface) throws InterruptedException {
    synchronized (this.interfacesQueried) {
      LynxQueryInterfaceCommand lynxQueryInterfaceCommand = new LynxQueryInterfaceCommand(this, paramLynxInterface.getInterfaceName());
      this.interfacesQueried.put(paramLynxInterface.getInterfaceName(), paramLynxInterface);
      boolean bool = true;
      try {
        boolean bool1;
        LynxQueryInterfaceResponse lynxQueryInterfaceResponse = (LynxQueryInterfaceResponse)lynxQueryInterfaceCommand.sendReceive();
        paramLynxInterface.setWasNacked(lynxQueryInterfaceResponse.isNackReceived());
        paramLynxInterface.setBaseCommandNumber(Integer.valueOf(lynxQueryInterfaceResponse.getCommandNumberFirst()));
        RobotLog.vv("LynxModule", "mod#=%d queryInterface(%s)=%d commands starting at %d", new Object[] { Integer.valueOf(getModuleAddress()), paramLynxInterface.getInterfaceName(), Integer.valueOf(lynxQueryInterfaceResponse.getNumberOfCommands()), Integer.valueOf(lynxQueryInterfaceResponse.getCommandNumberFirst()) });
        List list = paramLynxInterface.getCommandClasses();
        for (Map.Entry<Integer, MessageClassAndCtor> entry : this.commandClasses.entrySet()) {
          if (list.contains(((MessageClassAndCtor)entry.getValue()).clazz)) {
            this.commandClasses.remove(entry.getKey());
            this.supportedCommands.remove(((MessageClassAndCtor)entry.getValue()).clazz);
          } 
        } 
        Iterator<Class<?>> iterator = list.iterator();
        int i = 0;
        while (true) {
          bool1 = bool;
          if (iterator.hasNext()) {
            Class<? extends LynxMessage> clazz = (Class)iterator.next();
            if (i >= lynxQueryInterfaceResponse.getNumberOfCommands()) {
              RobotLog.vv("LynxModule", "mod#=%d intf=%s: expected %d commands; found %d", new Object[] { Integer.valueOf(getModuleAddress()), paramLynxInterface.getInterfaceName(), Integer.valueOf(list.size()), Integer.valueOf(lynxQueryInterfaceResponse.getNumberOfCommands()) });
              bool1 = bool;
              break;
            } 
            if (clazz != null)
              try {
                int j = lynxQueryInterfaceResponse.getCommandNumberFirst();
                MessageClassAndCtor messageClassAndCtor = new MessageClassAndCtor();
                messageClassAndCtor.clazz = clazz;
                messageClassAndCtor.assignCtor();
                this.commandClasses.put(Integer.valueOf(j + i), messageClassAndCtor);
                this.supportedCommands.add(clazz);
              } catch (NoSuchMethodException noSuchMethodException) {
                RobotLog.ee("LynxModule", noSuchMethodException, "exception registering %s", new Object[] { clazz.getSimpleName() });
              } catch (RuntimeException runtimeException) {} 
            i++;
            continue;
          } 
          break;
        } 
        return bool1;
      } catch (LynxNackException lynxNackException) {
        RobotLog.vv("LynxModule", "mod#=%d queryInterface(): interface %s is not supported", new Object[] { Integer.valueOf(getModuleAddress()), paramLynxInterface.getInterfaceName() });
      } catch (RuntimeException runtimeException) {
        RobotLog.ee("LynxModule", runtimeException, "exception during queryInterface(%s)", new Object[] { paramLynxInterface.getInterfaceName() });
      } 
    } 
  }
  
  BulkData recordBulkCachingCommandIntent(LynxDekaInterfaceCommand<?> paramLynxDekaInterfaceCommand) {
    return recordBulkCachingCommandIntent(paramLynxDekaInterfaceCommand, "");
  }
  
  BulkData recordBulkCachingCommandIntent(LynxDekaInterfaceCommand<?> paramLynxDekaInterfaceCommand, String paramString) {
    synchronized (this.bulkCachingLock) {
      List<LynxDekaInterfaceCommand<?>> list2 = this.bulkCachingHistory.get(paramString);
      List<LynxDekaInterfaceCommand<?>> list1 = list2;
      if (this.bulkCachingMode == BulkCachingMode.AUTO) {
        List<LynxDekaInterfaceCommand<?>> list = list2;
        if (list2 == null) {
          list = new ArrayList();
          this.bulkCachingHistory.put(paramString, list);
        } 
        Iterator<LynxDekaInterfaceCommand<?>> iterator = list.iterator();
        while (true) {
          list1 = list;
          if (iterator.hasNext()) {
            LynxDekaInterfaceCommand lynxDekaInterfaceCommand = iterator.next();
            if (lynxDekaInterfaceCommand.getDestModuleAddress() == paramLynxDekaInterfaceCommand.getDestModuleAddress() && lynxDekaInterfaceCommand.getCommandNumber() == paramLynxDekaInterfaceCommand.getCommandNumber() && Arrays.equals(lynxDekaInterfaceCommand.toPayloadByteArray(), paramLynxDekaInterfaceCommand.toPayloadByteArray())) {
              clearBulkCache();
              list1 = list;
              break;
            } 
            continue;
          } 
          break;
        } 
      } 
      if (this.lastBulkData == null)
        getBulkData(); 
      if (this.bulkCachingMode == BulkCachingMode.AUTO)
        list1.add(paramLynxDekaInterfaceCommand); 
      return this.lastBulkData;
    } 
  }
  
  public void registerCallback(RobotArmingStateNotifier.Callback paramCallback, boolean paramBoolean) {
    this.lynxUsbDevice.registerCallback(paramCallback, paramBoolean);
  }
  
  public void releaseNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    this.lynxUsbDevice.releaseNetworkTransmissionLock(paramLynxMessage);
  }
  
  public void removeAsConfigured() {
    this.lynxUsbDevice.removeConfiguredModule(this);
  }
  
  protected void resendCurrentPattern() {
    RobotLog.vv("LynxModule", "resendCurrentPattern()");
    sendLEDPatternSteps(this.currentSteps);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    setBulkCachingMode(BulkCachingMode.OFF);
  }
  
  public void resetPingTimer(LynxMessage paramLynxMessage) {
    startPingTimer();
  }
  
  public void retransmit(LynxMessage paramLynxMessage) throws InterruptedException {
    RobotLog.vv("LynxModule", "retransmitting: mod=%d cmd=0x%02x msg#=%d ref#=%d ", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramLynxMessage.getCommandNumber()), Integer.valueOf(paramLynxMessage.getMessageNumber()), Integer.valueOf(paramLynxMessage.getReferenceNumber()) });
    this.lynxUsbDevice.transmit(paramLynxMessage);
  }
  
  public void sendCommand(LynxMessage paramLynxMessage) throws InterruptedException, LynxUnsupportedCommandException {
    boolean bool;
    paramLynxMessage.setMessageNumber(getNewMessageNumber());
    int i = paramLynxMessage.getMessageNumber();
    paramLynxMessage.setSerialization(new LynxDatagram(paramLynxMessage));
    if (paramLynxMessage.isAckable() || paramLynxMessage.isResponseExpected()) {
      bool = true;
    } else {
      bool = false;
    } 
    this.unfinishedCommands.put(Integer.valueOf(i), (LynxRespondable)paramLynxMessage);
    this.lynxUsbDevice.transmit(paramLynxMessage);
    if (!bool)
      finishedWithMessage(paramLynxMessage); 
  }
  
  protected void sendGetModuleStatusAndProcessResponse(boolean paramBoolean) {
    LynxGetModuleStatusCommand lynxGetModuleStatusCommand = new LynxGetModuleStatusCommand(this, paramBoolean);
    try {
      LynxGetModuleStatusResponse lynxGetModuleStatusResponse = (LynxGetModuleStatusResponse)lynxGetModuleStatusCommand.sendReceive();
      if (lynxGetModuleStatusResponse.testAnyBits(-21))
        RobotLog.vv("LynxModule", "received status: %s", new Object[] { lynxGetModuleStatusResponse.toString() }); 
      if (lynxGetModuleStatusResponse.isKeepAliveTimeout())
        resendCurrentPattern(); 
      if (lynxGetModuleStatusResponse.isDeviceReset()) {
        LynxModuleWarningManager.getInstance().reportModuleReset(this);
        resendCurrentPattern();
      } 
      if (lynxGetModuleStatusResponse.isBatteryLow()) {
        LynxModuleWarningManager.getInstance().reportModuleLowBattery(this);
        return;
      } 
    } catch (LynxNackException lynxNackException) {
      handleException(lynxNackException);
    } catch (RuntimeException runtimeException) {
    
    } catch (InterruptedException interruptedException) {}
  }
  
  void sendLEDPatternSteps(Collection<Blinker.Step> paramCollection) {
    RobotLog.vv("LynxModule", "sendLEDPatternSteps(): #steps=%d", new Object[] { Integer.valueOf(paramCollection.size()) });
    ping();
    LynxSetModuleLEDPatternCommand.Steps steps = new LynxSetModuleLEDPatternCommand.Steps();
    Iterator<Blinker.Step> iterator = paramCollection.iterator();
    while (iterator.hasNext())
      steps.add(iterator.next()); 
    LynxSetModuleLEDPatternCommand lynxSetModuleLEDPatternCommand = new LynxSetModuleLEDPatternCommand(this, steps);
    try {
      lynxSetModuleLEDPatternCommand.sendReceive();
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (LynxNackException lynxNackException) {
    
    } catch (RuntimeException runtimeException) {}
    handleException(runtimeException);
  }
  
  public void setBulkCachingMode(BulkCachingMode paramBulkCachingMode) {
    synchronized (this.bulkCachingLock) {
      if (paramBulkCachingMode == BulkCachingMode.OFF)
        clearBulkCache(); 
      this.bulkCachingMode = paramBulkCachingMode;
      return;
    } 
  }
  
  public void setConstant(int paramInt) {
    Blinker.Step step = new Blinker.Step(paramInt, 1L, TimeUnit.SECONDS);
    ArrayList<Blinker.Step> arrayList = new ArrayList();
    arrayList.add(step);
    setPattern(arrayList);
  }
  
  public void setDebug(DebugGroup paramDebugGroup, DebugVerbosity paramDebugVerbosity) throws InterruptedException {
    try {
      (new LynxSetDebugLogLevelCommand(this, paramDebugGroup, paramDebugVerbosity)).send();
      return;
    } catch (LynxNackException lynxNackException) {
    
    } catch (RuntimeException runtimeException) {}
    handleException(runtimeException);
  }
  
  protected void setFtdiResetWatchdog(boolean paramBoolean) {
    if (isCommandSupported((Class)LynxFtdiResetControlCommand.class)) {
      boolean bool = Thread.interrupted();
      RobotLog.vv("LynxModule", "sending LynxFtdiResetControlCommand(%s) wasInterrupted=%s", new Object[] { Boolean.valueOf(paramBoolean), Boolean.valueOf(bool) });
      try {
        (new LynxFtdiResetControlCommand(this, paramBoolean)).sendReceive();
      } catch (InterruptedException interruptedException) {
        handleException(interruptedException);
      } catch (LynxNackException lynxNackException) {
      
      } catch (RuntimeException runtimeException) {}
      if (bool)
        Thread.currentThread().interrupt(); 
    } 
  }
  
  public void setNewModuleAddress(final int newModuleAddress) {
    if (newModuleAddress != getModuleAddress())
      this.lynxUsbDevice.changeModuleAddress(this, newModuleAddress, new Runnable() {
            public void run() {
              try {
                LynxSetNewModuleAddressCommand lynxSetNewModuleAddressCommand = new LynxSetNewModuleAddressCommand(LynxModule.this, (byte)newModuleAddress);
                lynxSetNewModuleAddressCommand.acquireNetworkLock();
                try {
                  lynxSetNewModuleAddressCommand.send();
                  LynxModule.this.moduleAddress = newModuleAddress;
                  return;
                } finally {
                  lynxSetNewModuleAddressCommand.releaseNetworkLock();
                } 
              } catch (LynxNackException lynxNackException) {
              
              } catch (InterruptedException interruptedException) {}
              LynxModule.this.handleException(interruptedException);
            }
          }); 
  }
  
  public void setPattern(Collection<Blinker.Step> paramCollection) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: ifnonnull -> 17
    //   6: new java/util/ArrayList
    //   9: dup
    //   10: invokespecial <init> : ()V
    //   13: astore_1
    //   14: goto -> 26
    //   17: new java/util/ArrayList
    //   20: dup
    //   21: aload_1
    //   22: invokespecial <init> : (Ljava/util/Collection;)V
    //   25: astore_1
    //   26: aload_0
    //   27: aload_1
    //   28: putfield currentSteps : Ljava/util/ArrayList;
    //   31: aload_0
    //   32: aload_1
    //   33: invokevirtual sendLEDPatternSteps : (Ljava/util/Collection;)V
    //   36: aload_0
    //   37: monitorexit
    //   38: return
    //   39: astore_1
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_1
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   6	14	39	finally
    //   17	26	39	finally
    //   26	36	39	finally
  }
  
  public void setSystemSynthetic(boolean paramBoolean) {
    this.isSystemSynthetic = paramBoolean;
  }
  
  public void setUserModule(boolean paramBoolean) {
    this.isUserModule = paramBoolean;
  }
  
  protected void startExecutor() {
    if (this.executor == null)
      this.executor = (ScheduledExecutorService)ThreadPool.newScheduledExecutor(1, "lynx module executor"); 
  }
  
  protected void startFtdiResetWatchdog() {
    synchronized (this.engagementLock) {
      if (!this.ftdiResetWatchdogActive) {
        this.ftdiResetWatchdogActive = true;
        setFtdiResetWatchdog(true);
      } 
      if (this.isEngaged)
        this.ftdiResetWatchdogActiveWhenEngaged = this.ftdiResetWatchdogActive; 
      return;
    } 
  }
  
  protected void startPingTimer() {
    synchronized (this.futureLock) {
      stopPingTimer(false);
      boolean bool = this.isOpen;
      if (bool)
        try {
          this.pingFuture = this.executor.schedule(new Runnable() {
                public void run() {
                  LynxModule.this.ping();
                }
              },  getMsModulePingInterval(), TimeUnit.MILLISECONDS);
        } catch (RejectedExecutionException rejectedExecutionException) {
          RobotLog.vv("LynxModule", "mod#=%d: scheduling of ping rejected: ignored", new Object[] { Integer.valueOf(getModuleAddress()) });
          this.pingFuture = null;
        }  
      return;
    } 
  }
  
  protected void stopAttentionRequired() {
    synchronized (this.futureLock) {
      if (this.attentionRequiredFuture != null) {
        this.attentionRequiredFuture.cancel(true);
        ThreadPool.awaitFuture(this.attentionRequiredFuture, 250L, TimeUnit.MILLISECONDS);
        this.attentionRequiredFuture = null;
      } 
      return;
    } 
  }
  
  public void stopBlinking() {
    setConstant(-16777216);
  }
  
  protected void stopExecutor() {
    ScheduledExecutorService scheduledExecutorService = this.executor;
    if (scheduledExecutorService != null) {
      scheduledExecutorService.shutdownNow();
      try {
        ThreadPool.awaitTermination(this.executor, 2L, TimeUnit.SECONDS, "lynx module executor");
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  protected void stopFtdiResetWatchdog() {
    stopFtdiResetWatchdog(false);
  }
  
  protected void stopFtdiResetWatchdog(boolean paramBoolean) {
    synchronized (this.engagementLock) {
      if (this.ftdiResetWatchdogActive) {
        this.ftdiResetWatchdogActive = false;
        setFtdiResetWatchdog(false);
      } 
      if (this.isEngaged && !paramBoolean)
        this.ftdiResetWatchdogActiveWhenEngaged = this.ftdiResetWatchdogActive; 
      return;
    } 
  }
  
  protected void stopPingTimer(boolean paramBoolean) {
    synchronized (this.futureLock) {
      if (this.pingFuture != null) {
        this.pingFuture.cancel(false);
        if (paramBoolean && !ThreadPool.awaitFuture(this.pingFuture, 250L, TimeUnit.MILLISECONDS))
          RobotLog.vv("LynxModule", "mod#=%d: unable to await ping future cancellation", new Object[] { Integer.valueOf(getModuleAddress()) }); 
        this.pingFuture = null;
      } 
      return;
    } 
  }
  
  public String toString() {
    return Misc.formatForUser("LynxModule(mod#=%d)", new Object[] { Integer.valueOf(this.moduleAddress) });
  }
  
  public void unregisterCallback(RobotArmingStateNotifier.Callback paramCallback) {
    this.lynxUsbDevice.unregisterCallback(paramCallback);
  }
  
  public void validateCommand(LynxMessage paramLynxMessage) throws LynxUnsupportedCommandException {
    synchronized (this.interfacesQueried) {
      if (this.lynxUsbDevice.getArmingState() != RobotArmingStateNotifier.ARMINGSTATE.ARMED)
        return; 
      int i = paramLynxMessage.getCommandNumber();
      if (LynxStandardCommand.isStandardCommandNumber(i))
        return; 
      if (this.commandClasses.containsKey(Integer.valueOf(i)))
        return; 
      throw new LynxUnsupportedCommandException(this, paramLynxMessage);
    } 
  }
  
  public void visuallyIdentify(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isVisuallyIdentifying : Z
    //   6: iload_1
    //   7: if_icmpeq -> 43
    //   10: aload_0
    //   11: getfield isVisuallyIdentifying : Z
    //   14: ifne -> 33
    //   17: aload_0
    //   18: getstatic com/qualcomm/hardware/lynx/LynxModule.blinkerPolicy : Lcom/qualcomm/hardware/lynx/LynxModule$BlinkerPolicy;
    //   21: aload_0
    //   22: invokeinterface getVisuallyIdentifyPattern : (Lcom/qualcomm/hardware/lynx/LynxModule;)Ljava/util/List;
    //   27: invokevirtual internalPushPattern : (Ljava/util/Collection;)V
    //   30: goto -> 38
    //   33: aload_0
    //   34: invokevirtual popPattern : ()Z
    //   37: pop
    //   38: aload_0
    //   39: iload_1
    //   40: putfield isVisuallyIdentifying : Z
    //   43: aload_0
    //   44: monitorexit
    //   45: return
    //   46: astore_2
    //   47: aload_0
    //   48: monitorexit
    //   49: aload_2
    //   50: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	46	finally
    //   33	38	46	finally
    //   38	43	46	finally
    //   43	45	46	finally
    //   47	49	46	finally
  }
  
  public static interface BlinkerPolicy {
    List<Blinker.Step> getIdlePattern(LynxModule param1LynxModule);
    
    List<Blinker.Step> getVisuallyIdentifyPattern(LynxModule param1LynxModule);
  }
  
  public static class BreathingBlinkerPolicy implements BlinkerPolicy {
    public List<Blinker.Step> getIdlePattern(LynxModule param1LynxModule) {
      float[] arrayOfFloat = new float[3];
      arrayOfFloat[0] = 0.0F;
      arrayOfFloat[1] = 0.0F;
      arrayOfFloat[2] = 0.0F;
      Color.colorToHSV(-16711681, arrayOfFloat);
      int i = 0;
      final float hue = arrayOfFloat[0];
      final ArrayList<Blinker.Step> steps = new ArrayList();
      Consumer<Integer> consumer = new Consumer<Integer>() {
          public void accept(Integer param2Integer) {
            float f = (float)Math.sqrt((1.0F - param2Integer.intValue() / 8.0F * 0.95F + 0.05F));
            int i = Color.HSVToColor(new float[] { this.val$hue, 1.0F, 1.0F - f });
            steps.add(new Blinker.Step(i, 125L, TimeUnit.MILLISECONDS));
          }
        };
      while (i <= 8) {
        consumer.accept(Integer.valueOf(i));
        i++;
      } 
      for (i = 7; i > 0; i--)
        consumer.accept(Integer.valueOf(i)); 
      return arrayList;
    }
    
    public List<Blinker.Step> getVisuallyIdentifyPattern(LynxModule param1LynxModule) {
      ArrayList<Blinker.Step> arrayList = new ArrayList();
      long l1 = 150L;
      arrayList.add(new Blinker.Step(-16711681, l1, TimeUnit.MILLISECONDS));
      long l2 = 75L;
      arrayList.add(new Blinker.Step(-16777216, l2, TimeUnit.MILLISECONDS));
      arrayList.add(new Blinker.Step(-65281, l1, TimeUnit.MILLISECONDS));
      arrayList.add(new Blinker.Step(-16777216, l2, TimeUnit.MILLISECONDS));
      return arrayList;
    }
  }
  
  class null implements Consumer<Integer> {
    public void accept(Integer param1Integer) {
      float f = (float)Math.sqrt((1.0F - param1Integer.intValue() / 8.0F * 0.95F + 0.05F));
      int i = Color.HSVToColor(new float[] { this.val$hue, 1.0F, 1.0F - f });
      steps.add(new Blinker.Step(i, 125L, TimeUnit.MILLISECONDS));
    }
  }
  
  public enum BulkCachingMode {
    AUTO, MANUAL, OFF;
    
    static {
      BulkCachingMode bulkCachingMode = new BulkCachingMode("AUTO", 2);
      AUTO = bulkCachingMode;
      $VALUES = new BulkCachingMode[] { OFF, MANUAL, bulkCachingMode };
    }
  }
  
  public static class BulkData {
    private final boolean fake;
    
    private final LynxGetBulkInputDataResponse resp;
    
    private BulkData(LynxGetBulkInputDataResponse param1LynxGetBulkInputDataResponse, boolean param1Boolean) {
      this.resp = param1LynxGetBulkInputDataResponse;
      this.fake = param1Boolean;
    }
    
    public double getAnalogInputVoltage(int param1Int) {
      return getAnalogInputVoltage(param1Int, VoltageUnit.VOLTS);
    }
    
    public double getAnalogInputVoltage(int param1Int, VoltageUnit param1VoltageUnit) {
      return param1VoltageUnit.convert(this.resp.getAnalogInput(param1Int), VoltageUnit.MILLIVOLTS);
    }
    
    public boolean getDigitalChannelState(int param1Int) {
      return this.resp.getDigitalInput(param1Int);
    }
    
    public int getMotorCurrentPosition(int param1Int) {
      return this.resp.getEncoder(param1Int);
    }
    
    public int getMotorVelocity(int param1Int) {
      return this.resp.getVelocity(param1Int);
    }
    
    public boolean isFake() {
      return this.fake;
    }
    
    public boolean isMotorBusy(int param1Int) {
      return this.resp.isAtTarget(param1Int) ^ true;
    }
    
    public boolean isMotorOverCurrent(int param1Int) {
      return this.resp.isOverCurrent(param1Int);
    }
  }
  
  public static class CountModuleAddressBlinkerPolicy extends BreathingBlinkerPolicy {
    public List<Blinker.Step> getIdlePattern(LynxModule param1LynxModule) {
      ArrayList<Blinker.Step> arrayList = new ArrayList();
      if (param1LynxModule.getModuleAddress() == 173) {
        arrayList.add(new Blinker.Step(-16711936, 25L, TimeUnit.SECONDS));
        return arrayList;
      } 
      arrayList.add(new Blinker.Step(-16711936, 4500L, TimeUnit.MILLISECONDS));
      long l = 500L;
      arrayList.add(new Blinker.Step(-16777216, l, TimeUnit.MILLISECONDS));
      int i = arrayList.size();
      int j = Math.min(param1LynxModule.getModuleAddress(), (16 - i) / 2);
      for (i = 0; i < j; i++) {
        arrayList.add(new Blinker.Step(-16776961, l, TimeUnit.MILLISECONDS));
        arrayList.add(new Blinker.Step(-16777216, l, TimeUnit.MILLISECONDS));
      } 
      return arrayList;
    }
  }
  
  public enum DebugGroup {
    ADC,
    DIGITALIO,
    FROMHOST,
    I2C,
    MAIN,
    MODULELED,
    MOTOR0,
    MOTOR1,
    MOTOR2,
    MOTOR3,
    NONE(0),
    PWMSERVO(0),
    TOHOST(0);
    
    public final byte bVal;
    
    static {
      FROMHOST = new DebugGroup("FROMHOST", 3, 3);
      ADC = new DebugGroup("ADC", 4, 4);
      PWMSERVO = new DebugGroup("PWMSERVO", 5, 5);
      MODULELED = new DebugGroup("MODULELED", 6, 6);
      DIGITALIO = new DebugGroup("DIGITALIO", 7, 7);
      I2C = new DebugGroup("I2C", 8, 8);
      MOTOR0 = new DebugGroup("MOTOR0", 9, 9);
      MOTOR1 = new DebugGroup("MOTOR1", 10, 10);
      MOTOR2 = new DebugGroup("MOTOR2", 11, 11);
      DebugGroup debugGroup = new DebugGroup("MOTOR3", 12, 12);
      MOTOR3 = debugGroup;
      $VALUES = new DebugGroup[] { 
          NONE, MAIN, TOHOST, FROMHOST, ADC, PWMSERVO, MODULELED, DIGITALIO, I2C, MOTOR0, 
          MOTOR1, MOTOR2, debugGroup };
    }
    
    DebugGroup(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static DebugGroup fromInt(int param1Int) {
      for (DebugGroup debugGroup : values()) {
        if (debugGroup.bVal == (byte)param1Int)
          return debugGroup; 
      } 
      return NONE;
    }
  }
  
  public enum DebugVerbosity {
    HIGH(0),
    LOW(0),
    MEDIUM(0),
    OFF(0);
    
    public final byte bVal;
    
    static {
      DebugVerbosity debugVerbosity = new DebugVerbosity("HIGH", 3, 3);
      HIGH = debugVerbosity;
      $VALUES = new DebugVerbosity[] { OFF, LOW, MEDIUM, debugVerbosity };
    }
    
    DebugVerbosity(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static DebugVerbosity fromInt(int param1Int) {
      for (DebugVerbosity debugVerbosity : values()) {
        if (debugVerbosity.bVal == (byte)param1Int)
          return debugVerbosity; 
      } 
      return OFF;
    }
  }
  
  protected static class MessageClassAndCtor {
    public Class<? extends LynxMessage> clazz;
    
    public Constructor<? extends LynxMessage> ctor;
    
    public void assignCtor() throws NoSuchMethodException {
      try {
        this.ctor = this.clazz.getConstructor(new Class[] { LynxModule.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        try {
          this.ctor = this.clazz.getConstructor(new Class[] { LynxModuleIntf.class });
          return;
        } catch (NoSuchMethodException noSuchMethodException1) {
          this.ctor = null;
          return;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */