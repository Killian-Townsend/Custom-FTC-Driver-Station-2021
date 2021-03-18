package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.ftccommon.configuration.RobotConfigMap;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.lynx.LynxModuleWarningManager;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.internal.hardware.CachedLynxFirmwareVersions;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;

public class FtcEventLoop extends FtcEventLoopBase {
  protected final OpModeManagerImpl opModeManager;
  
  protected final AtomicReference<OpMode> opModeStopRequested;
  
  protected final Map<String, Long> recentlyAttachedUsbDevices;
  
  protected UsbModuleAttachmentHandler usbModuleAttachmentHandler;
  
  protected final Utility utility;
  
  public FtcEventLoop(HardwareFactory paramHardwareFactory, OpModeRegister paramOpModeRegister, UpdateUI.Callback paramCallback, Activity paramActivity) {
    super(paramHardwareFactory, paramOpModeRegister, paramCallback, paramActivity);
    this.opModeManager = createOpModeManager(paramActivity);
    this.usbModuleAttachmentHandler = new DefaultUsbModuleAttachmentHandler();
    this.recentlyAttachedUsbDevices = new ConcurrentHashMap<String, Long>();
    this.opModeStopRequested = new AtomicReference<OpMode>();
    this.utility = new Utility(paramActivity);
  }
  
  protected static OpModeManagerImpl createOpModeManager(Activity paramActivity) {
    return new OpModeManagerImpl(paramActivity, new HardwareMap((Context)paramActivity));
  }
  
  private LynxUsbDevice ensureControlHubAddressIsSetCorrectly() throws RobotCoreException, InterruptedException {
    RobotLog.vv("FtcEventLoop", "Ensuring that the Control Hub address is set correctly");
    LynxUsbDevice lynxUsbDevice = (LynxUsbDevice)startUsbScanMangerIfNecessary().getDeviceManager().createLynxUsbDevice(SerialNumber.createEmbedded(), null);
    if (lynxUsbDevice.setControlHubModuleAddressIfNecessary()) {
      updateEditableConfigFilesWithNewControlHubAddress();
      lynxUsbDevice.close();
      return null;
    } 
    return lynxUsbDevice;
  }
  
  private void processOpModeStopRequest(OpMode paramOpMode) {
    if (paramOpMode != null && this.opModeManager.getActiveOpMode() == paramOpMode) {
      RobotLog.ii("FtcEventLoop", "auto-stopping OpMode '%s'", new Object[] { this.opModeManager.getActiveOpModeName() });
      this.opModeManager.stopActiveOpMode();
    } 
  }
  
  private void updateEditableConfigFilesWithNewControlHubAddress() throws RobotCoreException {
    RobotLog.vv("FtcEventLoop", "We just auto-changed the Control Hub's address. Now auto-updating configuration files.");
    ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler(startUsbScanMangerIfNecessary().getDeviceManager());
    RobotConfigFileManager robotConfigFileManager = new RobotConfigFileManager();
    try {
      for (RobotConfigFile robotConfigFile : robotConfigFileManager.getXMLFiles()) {
        if (!robotConfigFile.isReadOnly()) {
          RobotLog.vv("FtcEventLoop", "Updating \"%s\" config file", new Object[] { robotConfigFile.getName() });
          robotConfigFileManager.writeToFile(robotConfigFile, false, robotConfigFileManager.toXml(new RobotConfigMap(readXMLFileHandler.parse(robotConfigFile.getXml()))));
        } 
      } 
    } catch (IOException iOException) {
      RobotLog.ee("FtcEventLoop", iOException, "Failed to auto-update config files after automatically changing embedded Control Hub module address. This is OK.");
    } 
  }
  
  public OpModeManagerImpl getOpModeManager() {
    return this.opModeManager;
  }
  
  protected SerialNumber getSerialNumberOfUsbDevice(UsbDevice paramUsbDevice) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getSerialNumber : ()Ljava/lang/String;
    //   4: invokestatic fromStringOrNull : (Ljava/lang/String;)Lcom/qualcomm/robotcore/util/SerialNumber;
    //   7: astore #4
    //   9: aload #4
    //   11: astore_2
    //   12: aload #4
    //   14: ifnonnull -> 104
    //   17: aconst_null
    //   18: astore_3
    //   19: aconst_null
    //   20: astore_2
    //   21: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceManager;
    //   24: aload_1
    //   25: invokevirtual openByUsbDevice : (Landroid/hardware/usb/UsbDevice;)Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDevice;
    //   28: astore #5
    //   30: aload #4
    //   32: astore_3
    //   33: aload #5
    //   35: ifnull -> 60
    //   38: aload #5
    //   40: astore_2
    //   41: aload #5
    //   43: astore_3
    //   44: aload #5
    //   46: invokevirtual getDeviceInfo : ()Lorg/firstinspires/ftc/robotcore/internal/ftdi/FtDeviceInfo;
    //   49: getfield serialNumber : Ljava/lang/String;
    //   52: invokestatic fromStringOrNull : (Ljava/lang/String;)Lcom/qualcomm/robotcore/util/SerialNumber;
    //   55: astore #6
    //   57: aload #6
    //   59: astore_3
    //   60: aload_3
    //   61: astore_2
    //   62: aload #5
    //   64: ifnull -> 104
    //   67: aload_3
    //   68: astore_2
    //   69: aload #5
    //   71: invokevirtual close : ()V
    //   74: goto -> 104
    //   77: astore_1
    //   78: aload_2
    //   79: ifnull -> 86
    //   82: aload_2
    //   83: invokevirtual close : ()V
    //   86: aload_1
    //   87: athrow
    //   88: aload #4
    //   90: astore_2
    //   91: aload_3
    //   92: ifnull -> 104
    //   95: aload #4
    //   97: astore_2
    //   98: aload_3
    //   99: astore #5
    //   101: goto -> 69
    //   104: aload_2
    //   105: astore_3
    //   106: aload_2
    //   107: ifnonnull -> 126
    //   110: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/external/ClassFactory;
    //   113: invokevirtual getCameraManager : ()Lorg/firstinspires/ftc/robotcore/external/hardware/camera/CameraManager;
    //   116: checkcast org/firstinspires/ftc/robotcore/internal/camera/CameraManagerInternal
    //   119: aload_1
    //   120: invokeinterface getRealOrVendorProductSerialNumber : (Landroid/hardware/usb/UsbDevice;)Lcom/qualcomm/robotcore/util/SerialNumber;
    //   125: astore_3
    //   126: aload_3
    //   127: areturn
    //   128: astore_2
    //   129: goto -> 88
    //   132: astore_1
    //   133: aload_2
    //   134: areturn
    // Exception table:
    //   from	to	target	type
    //   21	30	128	java/lang/RuntimeException
    //   21	30	77	finally
    //   44	57	128	java/lang/RuntimeException
    //   44	57	77	finally
    //   110	126	132	java/lang/RuntimeException
  }
  
  public UsbModuleAttachmentHandler getUsbModuleAttachmentHandler() {
    return this.usbModuleAttachmentHandler;
  }
  
  protected void handleCommandInitOpMode(String paramString) {
    paramString = this.ftcEventLoopHandler.getOpMode(paramString);
    this.opModeManager.initActiveOpMode(paramString);
  }
  
  protected void handleCommandRunOpMode(String paramString) {
    paramString = this.ftcEventLoopHandler.getOpMode(paramString);
    if (!this.opModeManager.getActiveOpModeName().equals(paramString))
      this.opModeManager.initActiveOpMode(paramString); 
    this.opModeManager.startActiveOpMode();
  }
  
  protected void handleCommandSetMatchNumber(String paramString) {
    try {
      this.opModeManager.setMatchNumber(Integer.parseInt(paramString));
      return;
    } catch (NumberFormatException numberFormatException) {
      RobotLog.logStackTrace(numberFormatException);
      return;
    } 
  }
  
  public void handleUsbModuleAttach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException {
    UsbModuleAttachmentHandler usbModuleAttachmentHandler = this.usbModuleAttachmentHandler;
    if (usbModuleAttachmentHandler != null)
      usbModuleAttachmentHandler.handleUsbModuleAttach(paramRobotUsbModule); 
  }
  
  public void handleUsbModuleDetach(RobotUsbModule paramRobotUsbModule) throws RobotCoreException, InterruptedException {
    UsbModuleAttachmentHandler usbModuleAttachmentHandler = this.usbModuleAttachmentHandler;
    if (usbModuleAttachmentHandler != null)
      usbModuleAttachmentHandler.handleUsbModuleDetach(paramRobotUsbModule); 
  }
  
  public void init(EventLoopManager paramEventLoopManager) throws RobotCoreException, InterruptedException {
    RobotLog.ii("FtcEventLoop", "======= INIT START =======");
    super.init(paramEventLoopManager);
    this.opModeManager.init(paramEventLoopManager);
    this.registeredOpModes.registerAllOpModes(this.userOpmodeRegister);
    sendUIState();
    this.ftcEventLoopHandler.init(paramEventLoopManager);
    LynxUsbDevice lynxUsbDevice2 = null;
    paramEventLoopManager = null;
    LynxUsbDevice lynxUsbDevice1 = lynxUsbDevice2;
    try {
      LynxUsbDevice lynxUsbDevice;
      if (LynxConstants.isRevControlHub()) {
        lynxUsbDevice1 = lynxUsbDevice2;
        lynxUsbDevice = ensureControlHubAddressIsSetCorrectly();
      } 
      lynxUsbDevice1 = lynxUsbDevice;
      HardwareMap hardwareMap = this.ftcEventLoopHandler.getHardwareMap();
      lynxUsbDevice1 = lynxUsbDevice;
      this.opModeManager.setHardwareMap(hardwareMap);
      lynxUsbDevice1 = lynxUsbDevice;
      hardwareMap.logDevices();
      lynxUsbDevice1 = lynxUsbDevice;
      CachedLynxFirmwareVersions.update(hardwareMap);
      lynxUsbDevice1 = lynxUsbDevice;
      LynxModuleWarningManager.getInstance().init(this.opModeManager, hardwareMap);
      if (lynxUsbDevice != null)
        lynxUsbDevice.close(); 
      return;
    } finally {
      if (lynxUsbDevice1 != null)
        lynxUsbDevice1.close(); 
    } 
  }
  
  public void loop() throws RobotCoreException {
    OpMode opMode = this.opModeStopRequested.getAndSet(null);
    if (opMode != null)
      processOpModeStopRequest(opMode); 
    checkForChangedOpModes();
    this.ftcEventLoopHandler.displayGamePadInfo(this.opModeManager.getActiveOpModeName());
    Gamepad[] arrayOfGamepad = this.ftcEventLoopHandler.getGamepads();
    this.opModeManager.runActiveOpMode(arrayOfGamepad);
  }
  
  public void onUsbDeviceAttached(UsbDevice paramUsbDevice) {
    SerialNumber serialNumber = getSerialNumberOfUsbDevice(paramUsbDevice);
    if (serialNumber != null) {
      pendUsbDeviceAttachment(serialNumber, 0L, TimeUnit.MILLISECONDS);
      return;
    } 
    RobotLog.ee("FtcEventLoop", "ignoring: unable get serial number of attached UsbDevice vendor=0x%04x, product=0x%04x device=0x%04x name=%s", new Object[] { Integer.valueOf(paramUsbDevice.getVendorId()), Integer.valueOf(paramUsbDevice.getProductId()), Integer.valueOf(paramUsbDevice.getDeviceId()), paramUsbDevice.getDeviceName() });
  }
  
  public void pendUsbDeviceAttachment(SerialNumber paramSerialNumber, long paramLong, TimeUnit paramTimeUnit) {
    long l = 0L;
    if (paramLong == 0L) {
      paramLong = l;
    } else {
      paramLong = System.nanoTime() + paramTimeUnit.toNanos(paramLong);
    } 
    this.recentlyAttachedUsbDevices.put(paramSerialNumber.getString(), Long.valueOf(paramLong));
  }
  
  public CallbackResult processCommand(Command paramCommand) throws InterruptedException, RobotCoreException {
    this.ftcEventLoopHandler.sendBatteryInfo();
    CallbackResult callbackResult2 = super.processCommand(paramCommand);
    CallbackResult callbackResult1 = callbackResult2;
    if (!callbackResult2.stopDispatch()) {
      CallbackResult callbackResult;
      callbackResult1 = CallbackResult.HANDLED;
      String str2 = paramCommand.getName();
      String str1 = paramCommand.getExtra();
      if (str2.equals("CMD_INIT_OP_MODE")) {
        handleCommandInitOpMode(str1);
        callbackResult = callbackResult1;
      } else if (str2.equals("CMD_RUN_OP_MODE")) {
        handleCommandRunOpMode((String)callbackResult);
        callbackResult = callbackResult1;
      } else if (str2.equals("CMD_SET_MATCH_NUMBER")) {
        handleCommandSetMatchNumber((String)callbackResult);
        callbackResult = callbackResult1;
      } else {
        callbackResult = CallbackResult.NOT_HANDLED;
      } 
      callbackResult1 = callbackResult2;
      if (callbackResult == CallbackResult.HANDLED)
        callbackResult1 = callbackResult; 
    } 
    return callbackResult1;
  }
  
  public void processedRecentlyAttachedUsbDevices() throws RobotCoreException, InterruptedException {
    HashSet<?> hashSet = new HashSet();
    long l = System.nanoTime();
    for (Map.Entry<String, Long> entry : this.recentlyAttachedUsbDevices.entrySet()) {
      if (((Long)entry.getValue()).longValue() <= l) {
        hashSet.add(entry.getKey());
        this.recentlyAttachedUsbDevices.remove(entry.getKey());
      } 
    } 
    if (this.usbModuleAttachmentHandler != null && !hashSet.isEmpty()) {
      List list = this.ftcEventLoopHandler.getHardwareMap().getAll(RobotUsbModule.class);
      for (String str : new ArrayList(hashSet)) {
        boolean bool;
        SerialNumber serialNumber = SerialNumber.fromString(str);
        Iterator<RobotUsbModule> iterator = list.iterator();
        while (true) {
          if (iterator.hasNext()) {
            RobotUsbModule robotUsbModule = iterator.next();
            if (serialNumber.matches(robotUsbModule.getSerialNumber()) && robotUsbModule.getArmingState() != RobotArmingStateNotifier.ARMINGSTATE.ARMED) {
              hashSet.remove(str);
              handleUsbModuleAttach(robotUsbModule);
              boolean bool1 = true;
              break;
            } 
            continue;
          } 
          bool = false;
          break;
        } 
        if (!bool)
          RobotLog.vv("FtcEventLoop", "processedRecentlyAttachedUsbDevices(): %s not in hwmap; ignoring", new Object[] { serialNumber }); 
      } 
    } 
  }
  
  public void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble) {
    this.ftcEventLoopHandler.refreshUserTelemetry(paramTelemetryMessage, paramDouble);
  }
  
  public void requestOpModeStop(OpMode paramOpMode) {
    this.opModeStopRequested.set(paramOpMode);
  }
  
  protected void sendUIState() {
    super.sendUIState();
    EventLoopManager eventLoopManager = this.ftcEventLoopHandler.getEventLoopManager();
    if (eventLoopManager != null)
      eventLoopManager.refreshSystemTelemetryNow(); 
  }
  
  public void setUsbModuleAttachmentHandler(UsbModuleAttachmentHandler paramUsbModuleAttachmentHandler) {
    this.usbModuleAttachmentHandler = paramUsbModuleAttachmentHandler;
  }
  
  public void teardown() throws RobotCoreException, InterruptedException {
    RobotLog.ii("FtcEventLoop", "======= TEARDOWN =======");
    super.teardown();
    this.opModeManager.stopActiveOpMode();
    this.opModeManager.teardown();
    this.ftcEventLoopHandler.close();
    RobotLog.ii("FtcEventLoop", "======= TEARDOWN COMPLETE =======");
  }
  
  public class DefaultUsbModuleAttachmentHandler implements UsbModuleAttachmentHandler {
    public void handleUsbModuleAttach(RobotUsbModule param1RobotUsbModule) throws RobotCoreException, InterruptedException {
      String str = nameOfUsbModule(param1RobotUsbModule);
      RobotLog.ii("FtcEventLoop", "vv===== MODULE ATTACH: disarm %s=====vv", new Object[] { str });
      param1RobotUsbModule.disarm();
      RobotLog.ii("FtcEventLoop", "======= MODULE ATTACH: arm or pretend %s=======", new Object[] { str });
      param1RobotUsbModule.armOrPretend();
      RobotLog.ii("FtcEventLoop", "^^===== MODULE ATTACH: complete %s=====^^", new Object[] { str });
    }
    
    public void handleUsbModuleDetach(RobotUsbModule param1RobotUsbModule) throws RobotCoreException, InterruptedException {
      String str = nameOfUsbModule(param1RobotUsbModule);
      RobotLog.ii("FtcEventLoop", "vv===== MODULE DETACH RECOVERY: disarm %s=====vv", new Object[] { str });
      param1RobotUsbModule.disarm();
      RobotLog.ii("FtcEventLoop", "======= MODULE DETACH RECOVERY: pretend %s=======", new Object[] { str });
      param1RobotUsbModule.pretend();
      RobotLog.ii("FtcEventLoop", "^^===== MODULE DETACH RECOVERY: complete %s=====^^", new Object[] { str });
    }
    
    String nameOfUsbModule(RobotUsbModule param1RobotUsbModule) {
      return HardwareFactory.getDeviceDisplayName((Context)FtcEventLoop.this.activityContext, param1RobotUsbModule.getSerialNumber());
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcEventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */