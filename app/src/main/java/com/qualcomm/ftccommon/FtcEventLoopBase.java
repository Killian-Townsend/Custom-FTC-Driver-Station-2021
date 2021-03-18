package com.qualcomm.ftccommon;

import android.app.Activity;
import android.content.Context;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.ftccommon.configuration.USBScanManager;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.lynx.EmbeddedControlHubModule;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxUsbDeviceImpl;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.USBAccessibleLynxModule;
import com.qualcomm.robotcore.hardware.VisuallyIdentifiableHardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.WriteXMLFileHandler;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamServer;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterRC;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectGroupName;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectPersistentGroupManager;
import org.firstinspires.ftc.robotcore.internal.opmode.OnBotJavaBuildLocker;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.firstinspires.ftc.robotcore.internal.stellaris.FlashLoaderManager;
import org.firstinspires.ftc.robotcore.internal.stellaris.FlashLoaderProtocolException;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.inspection.InspectionState;

public abstract class FtcEventLoopBase implements EventLoop {
  public static final String TAG = "FtcEventLoop";
  
  protected Activity activityContext;
  
  protected final AtomicBoolean firmwareUpdateInProgress;
  
  protected FtcEventLoopHandler ftcEventLoopHandler;
  
  protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  protected final RegisteredOpModes registeredOpModes;
  
  protected RobotConfigFileManager robotCfgFileMgr;
  
  protected boolean runningOnDriverStation = false;
  
  protected USBScanManager usbScanManager;
  
  protected final OpModeRegister userOpmodeRegister;
  
  protected FtcEventLoopBase(HardwareFactory paramHardwareFactory, OpModeRegister paramOpModeRegister, UpdateUI.Callback paramCallback, Activity paramActivity) {
    this.userOpmodeRegister = paramOpModeRegister;
    this.registeredOpModes = RegisteredOpModes.getInstance();
    this.activityContext = paramActivity;
    this.robotCfgFileMgr = new RobotConfigFileManager(paramActivity);
    this.ftcEventLoopHandler = new FtcEventLoopHandler(paramHardwareFactory, paramCallback, (Context)paramActivity);
    this.firmwareUpdateInProgress = new AtomicBoolean(false);
    this.usbScanManager = null;
  }
  
  protected void checkForChangedOpModes() {
    if (this.registeredOpModes.getOnBotJavaChanged()) {
      OnBotJavaBuildLocker.lockBuildExclusiveWhile(new Runnable() {
            public void run() {
              FtcEventLoopBase.this.registeredOpModes.clearOnBotJavaChanged();
              FtcEventLoopBase.this.registeredOpModes.registerOnBotJavaOpModes();
            }
          });
      sendUIState();
    } 
    if (this.registeredOpModes.getBlocksOpModesChanged()) {
      this.registeredOpModes.clearBlocksOpModesChanged();
      this.registeredOpModes.registerInstanceOpModes();
      sendUIState();
    } 
  }
  
  protected boolean enterFirmwareUpdateMode(RobotUsbDevice paramRobotUsbDevice) {
    boolean bool;
    if (LynxConstants.isEmbeddedSerialNumber(paramRobotUsbDevice.getSerialNumber())) {
      RobotLog.vv("FtcEventLoop", "putting embedded lynx into firmware update mode");
      bool = LynxUsbDeviceImpl.enterFirmwareUpdateModeControlHub();
    } else {
      RobotLog.vv("FtcEventLoop", "putting lynx(serial=%s) into firmware update mode", new Object[] { paramRobotUsbDevice.getSerialNumber() });
      bool = LynxUsbDeviceImpl.enterFirmwareUpdateModeUSB(paramRobotUsbDevice);
    } 
    try {
      Thread.sleep(100L);
      return bool;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return bool;
    } 
  }
  
  protected LynxUsbDevice getLynxUsbDeviceForFirmwareUpdate(SerialNumber paramSerialNumber) {
    try {
      return (LynxUsbDevice)startUsbScanMangerIfNecessary().getDeviceManager().createLynxUsbDevice(paramSerialNumber, null);
    } catch (RobotCoreException robotCoreException) {
      RobotLog.ee("FtcEventLoop", (Throwable)robotCoreException, "getLynxUsbDeviceForFirmwareUpdate(): exception opening lynx usb device: %s", new Object[] { paramSerialNumber });
      return null;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      RobotLog.ee("FtcEventLoop", "Thread interrupted in getLynxUsbDeviceForFirmwareUpdate");
      return null;
    } 
  }
  
  protected List<USBAccessibleLynxModule> getUSBAccessibleLynxDevices(boolean paramBoolean) throws RobotCoreException {
    // Byte code:
    //   0: ldc 'FtcEventLoop'
    //   2: ldc 'getUSBAccessibleLynxDevices(includeModuleAddresses=%s)...'
    //   4: iconst_1
    //   5: anewarray java/lang/Object
    //   8: dup
    //   9: iconst_0
    //   10: iload_1
    //   11: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   14: aastore
    //   15: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   18: aload_0
    //   19: invokevirtual startUsbScanMangerIfNecessary : ()Lcom/qualcomm/ftccommon/configuration/USBScanManager;
    //   22: astore #6
    //   24: aload #6
    //   26: invokevirtual startDeviceScanIfNecessary : ()Lcom/qualcomm/robotcore/util/ThreadPool$SingletonResult;
    //   29: astore #5
    //   31: aload #5
    //   33: invokevirtual await : ()Ljava/lang/Object;
    //   36: checkcast com/qualcomm/robotcore/hardware/ScannedDevices
    //   39: astore #5
    //   41: new java/util/ArrayList
    //   44: dup
    //   45: invokespecial <init> : ()V
    //   48: astore #7
    //   50: aload #5
    //   52: invokevirtual entrySet : ()Ljava/util/Set;
    //   55: invokeinterface iterator : ()Ljava/util/Iterator;
    //   60: astore #5
    //   62: aload #5
    //   64: invokeinterface hasNext : ()Z
    //   69: ifeq -> 145
    //   72: aload #5
    //   74: invokeinterface next : ()Ljava/lang/Object;
    //   79: checkcast java/util/Map$Entry
    //   82: astore #8
    //   84: aload #8
    //   86: invokeinterface getValue : ()Ljava/lang/Object;
    //   91: getstatic com/qualcomm/robotcore/hardware/DeviceManager$UsbDeviceType.LYNX_USB_DEVICE : Lcom/qualcomm/robotcore/hardware/DeviceManager$UsbDeviceType;
    //   94: if_acmpne -> 62
    //   97: aload #8
    //   99: invokeinterface getKey : ()Ljava/lang/Object;
    //   104: checkcast com/qualcomm/robotcore/util/SerialNumber
    //   107: astore #8
    //   109: aload #8
    //   111: invokevirtual isEmbedded : ()Z
    //   114: ifne -> 635
    //   117: iconst_1
    //   118: istore #4
    //   120: goto -> 123
    //   123: aload #7
    //   125: new com/qualcomm/robotcore/hardware/USBAccessibleLynxModule
    //   128: dup
    //   129: aload #8
    //   131: iload #4
    //   133: invokespecial <init> : (Lcom/qualcomm/robotcore/util/SerialNumber;Z)V
    //   136: invokeinterface add : (Ljava/lang/Object;)Z
    //   141: pop
    //   142: goto -> 62
    //   145: invokestatic isRevControlHub : ()Z
    //   148: ifeq -> 220
    //   151: aload #7
    //   153: invokeinterface iterator : ()Ljava/util/Iterator;
    //   158: astore #5
    //   160: aload #5
    //   162: invokeinterface hasNext : ()Z
    //   167: ifeq -> 641
    //   170: aload #5
    //   172: invokeinterface next : ()Ljava/lang/Object;
    //   177: checkcast com/qualcomm/robotcore/hardware/USBAccessibleLynxModule
    //   180: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   183: getstatic com/qualcomm/robotcore/hardware/configuration/LynxConstants.SERIAL_NUMBER_EMBEDDED : Lcom/qualcomm/robotcore/util/SerialNumber;
    //   186: invokevirtual equals : (Ljava/lang/Object;)Z
    //   189: ifeq -> 160
    //   192: iconst_1
    //   193: istore_2
    //   194: goto -> 197
    //   197: iload_2
    //   198: ifne -> 220
    //   201: aload #7
    //   203: new com/qualcomm/robotcore/hardware/USBAccessibleLynxModule
    //   206: dup
    //   207: getstatic com/qualcomm/robotcore/hardware/configuration/LynxConstants.SERIAL_NUMBER_EMBEDDED : Lcom/qualcomm/robotcore/util/SerialNumber;
    //   210: iconst_0
    //   211: invokespecial <init> : (Lcom/qualcomm/robotcore/util/SerialNumber;Z)V
    //   214: invokeinterface add : (Ljava/lang/Object;)Z
    //   219: pop
    //   220: aload #7
    //   222: invokeinterface iterator : ()Ljava/util/Iterator;
    //   227: astore #5
    //   229: aload #5
    //   231: invokeinterface hasNext : ()Z
    //   236: ifeq -> 270
    //   239: ldc 'FtcEventLoop'
    //   241: ldc_w 'getUSBAccessibleLynxDevices: found serial=%s'
    //   244: iconst_1
    //   245: anewarray java/lang/Object
    //   248: dup
    //   249: iconst_0
    //   250: aload #5
    //   252: invokeinterface next : ()Ljava/lang/Object;
    //   257: checkcast com/qualcomm/robotcore/hardware/USBAccessibleLynxModule
    //   260: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   263: aastore
    //   264: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   267: goto -> 229
    //   270: iload_1
    //   271: ifeq -> 552
    //   274: ldc 'FtcEventLoop'
    //   276: ldc_w 'finding module addresses and current firmware versions'
    //   279: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   282: iconst_0
    //   283: istore_2
    //   284: iload_2
    //   285: aload #7
    //   287: invokeinterface size : ()I
    //   292: if_icmpge -> 552
    //   295: aload #7
    //   297: iload_2
    //   298: invokeinterface get : (I)Ljava/lang/Object;
    //   303: checkcast com/qualcomm/robotcore/hardware/USBAccessibleLynxModule
    //   306: astore #5
    //   308: ldc 'FtcEventLoop'
    //   310: ldc_w 'getUSBAccessibleLynxDevices: finding module address for usbModule %s'
    //   313: iconst_1
    //   314: anewarray java/lang/Object
    //   317: dup
    //   318: iconst_0
    //   319: aload #5
    //   321: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   324: aastore
    //   325: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   328: aload #6
    //   330: invokevirtual getDeviceManager : ()Lcom/qualcomm/robotcore/hardware/DeviceManager;
    //   333: aload #5
    //   335: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   338: aconst_null
    //   339: invokeinterface createLynxUsbDevice : (Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/lang/String;)Lcom/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice;
    //   344: checkcast com/qualcomm/hardware/lynx/LynxUsbDevice
    //   347: astore #8
    //   349: aload #8
    //   351: invokeinterface discoverModules : ()Lcom/qualcomm/robotcore/hardware/LynxModuleMetaList;
    //   356: astore #9
    //   358: aload #5
    //   360: iconst_0
    //   361: invokevirtual setModuleAddress : (I)V
    //   364: aload #9
    //   366: invokevirtual iterator : ()Ljava/util/Iterator;
    //   369: astore #9
    //   371: iconst_0
    //   372: istore_3
    //   373: aload #9
    //   375: invokeinterface hasNext : ()Z
    //   380: ifeq -> 454
    //   383: aload #9
    //   385: invokeinterface next : ()Ljava/lang/Object;
    //   390: checkcast com/qualcomm/robotcore/hardware/LynxModuleMeta
    //   393: astore #10
    //   395: ldc 'FtcEventLoop'
    //   397: ldc_w 'assessing %s'
    //   400: iconst_1
    //   401: anewarray java/lang/Object
    //   404: dup
    //   405: iconst_0
    //   406: aload #10
    //   408: aastore
    //   409: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   412: aload #10
    //   414: invokevirtual getModuleAddress : ()I
    //   417: ifne -> 431
    //   420: ldc 'FtcEventLoop'
    //   422: ldc_w 'ignoring module with address zero'
    //   425: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   428: goto -> 373
    //   431: aload #10
    //   433: invokevirtual isParent : ()Z
    //   436: ifeq -> 373
    //   439: aload #5
    //   441: aload #10
    //   443: invokevirtual getModuleAddress : ()I
    //   446: invokevirtual setModuleAddress : (I)V
    //   449: iconst_1
    //   450: istore_3
    //   451: goto -> 373
    //   454: aload #5
    //   456: ldc_w ''
    //   459: invokevirtual setFirmwareVersionString : (Ljava/lang/String;)V
    //   462: iload_3
    //   463: ifeq -> 512
    //   466: aload_0
    //   467: aload #6
    //   469: invokevirtual getDeviceManager : ()Lcom/qualcomm/robotcore/hardware/DeviceManager;
    //   472: aload #8
    //   474: aload #5
    //   476: invokevirtual getModuleAddress : ()I
    //   479: new com/qualcomm/ftccommon/FtcEventLoopBase$7
    //   482: dup
    //   483: aload_0
    //   484: aload #5
    //   486: invokespecial <init> : (Lcom/qualcomm/ftccommon/FtcEventLoopBase;Lcom/qualcomm/robotcore/hardware/USBAccessibleLynxModule;)V
    //   489: invokevirtual talkToParentLynxModule : (Lcom/qualcomm/robotcore/hardware/DeviceManager;Lcom/qualcomm/hardware/lynx/LynxUsbDevice;ILorg/firstinspires/ftc/robotcore/external/Consumer;)V
    //   492: goto -> 512
    //   495: astore #5
    //   497: goto -> 502
    //   500: astore #5
    //   502: ldc 'FtcEventLoop'
    //   504: aload #5
    //   506: ldc_w 'exception retrieving fw version; ignoring'
    //   509: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   512: iload_2
    //   513: iconst_1
    //   514: iadd
    //   515: istore_3
    //   516: iload_3
    //   517: istore_2
    //   518: aload #8
    //   520: ifnull -> 284
    //   523: aload #8
    //   525: invokeinterface close : ()V
    //   530: iload_3
    //   531: istore_2
    //   532: goto -> 284
    //   535: astore #5
    //   537: aload #8
    //   539: ifnull -> 549
    //   542: aload #8
    //   544: invokeinterface close : ()V
    //   549: aload #5
    //   551: athrow
    //   552: ldc 'FtcEventLoop'
    //   554: ldc_w 'getUSBAccessibleLynxDevices(): %d modules found'
    //   557: iconst_1
    //   558: anewarray java/lang/Object
    //   561: dup
    //   562: iconst_0
    //   563: aload #7
    //   565: invokeinterface size : ()I
    //   570: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   573: aastore
    //   574: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   577: ldc 'FtcEventLoop'
    //   579: ldc_w '...getUSBAccessibleLynxDevices()'
    //   582: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   585: aload #7
    //   587: areturn
    //   588: astore #5
    //   590: goto -> 619
    //   593: invokestatic currentThread : ()Ljava/lang/Thread;
    //   596: invokevirtual interrupt : ()V
    //   599: new java/util/ArrayList
    //   602: dup
    //   603: invokespecial <init> : ()V
    //   606: astore #5
    //   608: ldc 'FtcEventLoop'
    //   610: ldc_w '...getUSBAccessibleLynxDevices()'
    //   613: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   616: aload #5
    //   618: areturn
    //   619: ldc 'FtcEventLoop'
    //   621: ldc_w '...getUSBAccessibleLynxDevices()'
    //   624: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   627: aload #5
    //   629: athrow
    //   630: astore #5
    //   632: goto -> 593
    //   635: iconst_0
    //   636: istore #4
    //   638: goto -> 123
    //   641: iconst_0
    //   642: istore_2
    //   643: goto -> 197
    // Exception table:
    //   from	to	target	type
    //   31	62	630	java/lang/InterruptedException
    //   31	62	588	finally
    //   62	117	630	java/lang/InterruptedException
    //   62	117	588	finally
    //   123	142	630	java/lang/InterruptedException
    //   123	142	588	finally
    //   145	160	630	java/lang/InterruptedException
    //   145	160	588	finally
    //   160	192	630	java/lang/InterruptedException
    //   160	192	588	finally
    //   201	220	630	java/lang/InterruptedException
    //   201	220	588	finally
    //   220	229	630	java/lang/InterruptedException
    //   220	229	588	finally
    //   229	267	630	java/lang/InterruptedException
    //   229	267	588	finally
    //   274	282	630	java/lang/InterruptedException
    //   274	282	588	finally
    //   284	349	630	java/lang/InterruptedException
    //   284	349	588	finally
    //   349	371	535	finally
    //   373	428	535	finally
    //   431	449	535	finally
    //   454	462	535	finally
    //   466	492	500	com/qualcomm/robotcore/exception/RobotCoreException
    //   466	492	495	com/qualcomm/hardware/lynx/LynxNackException
    //   466	492	535	finally
    //   502	512	535	finally
    //   523	530	630	java/lang/InterruptedException
    //   523	530	588	finally
    //   542	549	630	java/lang/InterruptedException
    //   542	549	588	finally
    //   549	552	630	java/lang/InterruptedException
    //   549	552	588	finally
    //   552	577	630	java/lang/InterruptedException
    //   552	577	588	finally
    //   593	608	588	finally
  }
  
  protected void handleCommandActivateConfiguration(String paramString) {
    RobotConfigFile robotConfigFile = this.robotCfgFileMgr.getConfigFromString(paramString);
    this.robotCfgFileMgr.setActiveConfigAndUpdateUI(this.runningOnDriverStation, robotConfigFile);
  }
  
  protected void handleCommandClearRememberedGroups() {
    (new WifiDirectPersistentGroupManager(WifiDirectAgent.getInstance())).deleteAllPersistentGroups();
    AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastWifiP2pRememberedGroupsCleared));
  }
  
  protected void handleCommandDeleteConfiguration(String paramString) {
    RobotConfigFile robotConfigFile = this.robotCfgFileMgr.getConfigFromString(paramString);
    if (RobotConfigFileManager.getFullPath(robotConfigFile.getName()).delete())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Tried to delete a file that does not exist: ");
    stringBuilder.append(robotConfigFile.getName());
    RobotLog.ee("FtcEventLoop", stringBuilder.toString());
  }
  
  protected void handleCommandDisconnectWifiDirect() {
    if (WifiDirectAgent.getInstance().disconnectFromWifiDirect()) {
      AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastDisconnectedFromWifiDirect));
      return;
    } 
    AppUtil.getInstance().showToast(UILocation.BOTH, AppUtil.getDefContext().getString(R.string.toastErrorDisconnectingFromWifiDirect));
  }
  
  protected void handleCommandDiscoverLynxModules(String paramString) throws RobotCoreException {
    RobotLog.vv("FtcConfigTag", "handling command DiscoverLynxModules");
    final SerialNumber serialNumber = SerialNumber.fromString(paramString);
    final USBScanManager usbScanManager = startUsbScanMangerIfNecessary();
    final ThreadPool.SingletonResult future = this.usbScanManager.startLynxModuleEnumerationIfNecessary(serialNumber);
    ThreadPool.getDefault().execute(new Runnable() {
          public void run() {
            try {
              LynxModuleMetaList lynxModuleMetaList2 = (LynxModuleMetaList)future.await();
              LynxModuleMetaList lynxModuleMetaList1 = lynxModuleMetaList2;
              if (lynxModuleMetaList2 == null)
                lynxModuleMetaList1 = new LynxModuleMetaList(serialNumber); 
              String str = usbScanManager.packageCommandResponse(lynxModuleMetaList1);
              RobotLog.vv("FtcConfigTag", "DiscoverLynxModules data='%s'", new Object[] { str });
              FtcEventLoopBase.this.networkConnectionHandler.sendCommand(new Command("CMD_DISCOVER_LYNX_MODULES_RESP", str));
              return;
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
              return;
            } 
          }
        });
  }
  
  protected void handleCommandDismissAllDialogs(Command paramCommand) {
    AppUtil.getInstance().dismissAllDialogs(UILocation.ONLY_LOCAL);
  }
  
  protected void handleCommandDismissDialog(Command paramCommand) {
    AppUtil.getInstance().dismissDialog(UILocation.ONLY_LOCAL, RobotCoreCommandList.DismissDialog.deserialize(paramCommand.getExtra()));
  }
  
  protected void handleCommandDismissProgress(Command paramCommand) {
    AppUtil.getInstance().dismissProgress(UILocation.ONLY_LOCAL);
  }
  
  protected void handleCommandGetCandidateLynxFirmwareImages(Command paramCommand) {
    final Pattern pattern = Pattern.compile("(?i).*\\.bin");
    File file = AppUtil.LYNX_FIRMWARE_UPDATE_DIR;
    File[] arrayOfFile = file.listFiles(new FileFilter() {
          public boolean accept(File param1File) {
            Assert.assertTrue(param1File.isAbsolute());
            return pattern.matcher(param1File.getName()).matches();
          }
        });
    RobotCoreCommandList.LynxFirmwareImagesResp lynxFirmwareImagesResp = new RobotCoreCommandList.LynxFirmwareImagesResp();
    lynxFirmwareImagesResp.firstFolder = AppUtil.FIRST_FOLDER;
    int j = arrayOfFile.length;
    int i;
    for (i = 0; i < j; i++) {
      File file1 = arrayOfFile[i];
      lynxFirmwareImagesResp.firmwareImages.add(new RobotCoreCommandList.FWImage(file1, false));
    } 
    try {
      file = new File(file.getParentFile().getName(), file.getName());
      String[] arrayOfString = this.activityContext.getAssets().list(file.getPath());
      j = arrayOfString.length;
      i = 0;
      while (true) {
        if (i < j) {
          String str = arrayOfString[i];
          if (pattern.matcher(str).matches()) {
            boolean bool;
            File file1 = new File(file, str);
            if (!file1.isAbsolute()) {
              bool = true;
            } else {
              bool = false;
            } 
            Assert.assertTrue(bool);
            lynxFirmwareImagesResp.firmwareImages.add(new RobotCoreCommandList.FWImage(file1, true));
          } 
          i++;
          continue;
        } 
        this.networkConnectionHandler.sendReply(paramCommand, new Command("CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES_RESP", lynxFirmwareImagesResp.serialize()));
        return;
      } 
    } catch (IOException iOException) {}
    this.networkConnectionHandler.sendReply(paramCommand, new Command("CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES_RESP", lynxFirmwareImagesResp.serialize()));
  }
  
  protected void handleCommandGetUSBAccessibleLynxModules(final Command commandRequest) {
    ThreadPool.getDefault().execute(new Runnable() {
          public void run() {
            RobotCoreCommandList.USBAccessibleLynxModulesRequest uSBAccessibleLynxModulesRequest = RobotCoreCommandList.USBAccessibleLynxModulesRequest.deserialize(commandRequest.getExtra());
            ArrayList<USBAccessibleLynxModule> arrayList = new ArrayList();
            try {
              arrayList.addAll(FtcEventLoopBase.this.getUSBAccessibleLynxDevices(uSBAccessibleLynxModulesRequest.forFirmwareUpdate));
            } catch (RobotCoreException robotCoreException) {}
            Collections.sort(arrayList, new Comparator<USBAccessibleLynxModule>() {
                  public int compare(USBAccessibleLynxModule param2USBAccessibleLynxModule1, USBAccessibleLynxModule param2USBAccessibleLynxModule2) {
                    return param2USBAccessibleLynxModule1.getSerialNumber().getString().compareTo(param2USBAccessibleLynxModule2.getSerialNumber().getString());
                  }
                });
            RobotCoreCommandList.USBAccessibleLynxModulesResp uSBAccessibleLynxModulesResp = new RobotCoreCommandList.USBAccessibleLynxModulesResp();
            uSBAccessibleLynxModulesResp.modules = arrayList;
            FtcEventLoopBase.this.networkConnectionHandler.sendReply(commandRequest, new Command("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES_RESP", uSBAccessibleLynxModulesResp.serialize()));
          }
        });
  }
  
  protected void handleCommandLynxChangeModuleAddresses(final Command commandRequest) {
    ThreadPool.getDefault().execute(new Runnable() {
          public void run() {
            // Byte code:
            //   0: iconst_1
            //   1: istore #4
            //   3: iconst_1
            //   4: istore #5
            //   6: iconst_1
            //   7: istore_1
            //   8: iload #4
            //   10: istore_2
            //   11: iload #5
            //   13: istore_3
            //   14: aload_0
            //   15: getfield val$commandRequest : Lcom/qualcomm/robotcore/robocol/Command;
            //   18: invokevirtual getExtra : ()Ljava/lang/String;
            //   21: invokestatic deserialize : (Ljava/lang/String;)Lcom/qualcomm/ftccommon/CommandList$LynxAddressChangeRequest;
            //   24: astore #7
            //   26: iload #4
            //   28: istore_2
            //   29: iload #5
            //   31: istore_3
            //   32: aload_0
            //   33: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   36: invokevirtual startUsbScanMangerIfNecessary : ()Lcom/qualcomm/ftccommon/configuration/USBScanManager;
            //   39: invokevirtual getDeviceManager : ()Lcom/qualcomm/robotcore/hardware/DeviceManager;
            //   42: astore #6
            //   44: iload #4
            //   46: istore_2
            //   47: iload #5
            //   49: istore_3
            //   50: aload #7
            //   52: getfield modulesToChange : Ljava/util/ArrayList;
            //   55: invokevirtual iterator : ()Ljava/util/Iterator;
            //   58: astore #9
            //   60: iload #4
            //   62: istore_2
            //   63: iload #5
            //   65: istore_3
            //   66: aload #9
            //   68: invokeinterface hasNext : ()Z
            //   73: ifeq -> 250
            //   76: iload #4
            //   78: istore_2
            //   79: iload #5
            //   81: istore_3
            //   82: aload #9
            //   84: invokeinterface next : ()Ljava/lang/Object;
            //   89: checkcast com/qualcomm/ftccommon/CommandList$LynxAddressChangeRequest$AddressChange
            //   92: astore #8
            //   94: iload #4
            //   96: istore_2
            //   97: iload #5
            //   99: istore_3
            //   100: aload #6
            //   102: aload #8
            //   104: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   107: aconst_null
            //   108: invokeinterface createLynxUsbDevice : (Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/lang/String;)Lcom/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice;
            //   113: checkcast com/qualcomm/hardware/lynx/LynxUsbDevice
            //   116: astore #7
            //   118: aload_0
            //   119: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   122: aload #6
            //   124: aload #7
            //   126: aload #8
            //   128: getfield oldAddress : I
            //   131: new com/qualcomm/ftccommon/FtcEventLoopBase$8$1
            //   134: dup
            //   135: aload_0
            //   136: aload #8
            //   138: invokespecial <init> : (Lcom/qualcomm/ftccommon/FtcEventLoopBase$8;Lcom/qualcomm/ftccommon/CommandList$LynxAddressChangeRequest$AddressChange;)V
            //   141: invokevirtual talkToParentLynxModule : (Lcom/qualcomm/robotcore/hardware/DeviceManager;Lcom/qualcomm/hardware/lynx/LynxUsbDevice;ILorg/firstinspires/ftc/robotcore/external/Consumer;)V
            //   144: aload #7
            //   146: ifnull -> 60
            //   149: iload #4
            //   151: istore_2
            //   152: iload #5
            //   154: istore_3
            //   155: aload #7
            //   157: invokeinterface close : ()V
            //   162: goto -> 60
            //   165: astore #6
            //   167: goto -> 227
            //   170: astore #6
            //   172: goto -> 177
            //   175: astore #6
            //   177: ldc 'FtcEventLoop'
            //   179: aload #6
            //   181: ldc 'failure during module address change'
            //   183: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
            //   186: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   189: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
            //   192: aload_0
            //   193: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   196: getfield activityContext : Landroid/app/Activity;
            //   199: getstatic com/qualcomm/ftccommon/R$string.toastLynxAddressChangeFailed : I
            //   202: iconst_1
            //   203: anewarray java/lang/Object
            //   206: dup
            //   207: iconst_0
            //   208: aload #8
            //   210: getfield serialNumber : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   213: aastore
            //   214: invokevirtual getString : (I[Ljava/lang/Object;)Ljava/lang/String;
            //   217: invokevirtual showToast : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;)V
            //   220: aload #6
            //   222: athrow
            //   223: astore #6
            //   225: iconst_0
            //   226: istore_1
            //   227: aload #7
            //   229: ifnull -> 243
            //   232: iload_1
            //   233: istore_2
            //   234: iload_1
            //   235: istore_3
            //   236: aload #7
            //   238: invokeinterface close : ()V
            //   243: iload_1
            //   244: istore_2
            //   245: iload_1
            //   246: istore_3
            //   247: aload #6
            //   249: athrow
            //   250: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   253: astore #6
            //   255: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
            //   258: astore #7
            //   260: aload_0
            //   261: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   264: getfield activityContext : Landroid/app/Activity;
            //   267: astore #8
            //   269: getstatic com/qualcomm/ftccommon/R$string.toastLynxAddressChangeComplete : I
            //   272: istore_1
            //   273: aload #6
            //   275: aload #7
            //   277: aload #8
            //   279: iload_1
            //   280: invokevirtual getString : (I)Ljava/lang/String;
            //   283: invokevirtual showToast : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;)V
            //   286: return
            //   287: astore #6
            //   289: iload_2
            //   290: ifeq -> 315
            //   293: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   296: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
            //   299: aload_0
            //   300: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   303: getfield activityContext : Landroid/app/Activity;
            //   306: getstatic com/qualcomm/ftccommon/R$string.toastLynxAddressChangeComplete : I
            //   309: invokevirtual getString : (I)Ljava/lang/String;
            //   312: invokevirtual showToast : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;)V
            //   315: aload #6
            //   317: athrow
            //   318: iload_3
            //   319: ifeq -> 354
            //   322: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
            //   325: astore #6
            //   327: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
            //   330: astore #7
            //   332: aload_0
            //   333: getfield this$0 : Lcom/qualcomm/ftccommon/FtcEventLoopBase;
            //   336: getfield activityContext : Landroid/app/Activity;
            //   339: astore #8
            //   341: getstatic com/qualcomm/ftccommon/R$string.toastLynxAddressChangeComplete : I
            //   344: istore_1
            //   345: goto -> 273
            //   348: invokestatic currentThread : ()Ljava/lang/Thread;
            //   351: invokevirtual interrupt : ()V
            //   354: return
            //   355: astore #6
            //   357: goto -> 318
            //   360: astore #6
            //   362: goto -> 348
            // Exception table:
            //   from	to	target	type
            //   14	26	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   14	26	355	com/qualcomm/hardware/lynx/LynxNackException
            //   14	26	287	finally
            //   32	44	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   32	44	355	com/qualcomm/hardware/lynx/LynxNackException
            //   32	44	287	finally
            //   50	60	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   50	60	355	com/qualcomm/hardware/lynx/LynxNackException
            //   50	60	287	finally
            //   66	76	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   66	76	355	com/qualcomm/hardware/lynx/LynxNackException
            //   66	76	287	finally
            //   82	94	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   82	94	355	com/qualcomm/hardware/lynx/LynxNackException
            //   82	94	287	finally
            //   100	118	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   100	118	355	com/qualcomm/hardware/lynx/LynxNackException
            //   100	118	287	finally
            //   118	144	175	com/qualcomm/robotcore/exception/RobotCoreException
            //   118	144	170	com/qualcomm/hardware/lynx/LynxNackException
            //   118	144	165	finally
            //   155	162	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   155	162	355	com/qualcomm/hardware/lynx/LynxNackException
            //   155	162	287	finally
            //   177	220	165	finally
            //   220	223	223	finally
            //   236	243	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   236	243	355	com/qualcomm/hardware/lynx/LynxNackException
            //   236	243	287	finally
            //   247	250	355	com/qualcomm/robotcore/exception/RobotCoreException
            //   247	250	355	com/qualcomm/hardware/lynx/LynxNackException
            //   247	250	287	finally
            //   250	273	360	java/lang/InterruptedException
            //   273	286	360	java/lang/InterruptedException
            //   293	315	360	java/lang/InterruptedException
            //   315	318	360	java/lang/InterruptedException
            //   322	345	360	java/lang/InterruptedException
          }
        });
  }
  
  protected void handleCommandLynxFirmwareUpdate(final Command commandRequest) {
    RobotLog.vv("FtcEventLoop", "handleCommandLynxFirmwareUpdate received");
    final RobotCoreCommandList.LynxFirmwareUpdate params = RobotCoreCommandList.LynxFirmwareUpdate.deserialize(commandRequest.getExtra());
    ThreadPool.getDefault().submit(new Runnable() {
          public void run() {
            RobotCoreCommandList.LynxFirmwareUpdateResp lynxFirmwareUpdateResp = FtcEventLoopBase.this.updateLynxFirmware(params.serialNumber, params.firmwareImageFile, params.originatorId);
            FtcEventLoopBase.this.networkConnectionHandler.sendReply(commandRequest, new Command("CMD_LYNX_FIRMWARE_UPDATE_RESP", lynxFirmwareUpdateResp.serialize()));
          }
        });
  }
  
  protected void handleCommandRequestAboutInfo(Command paramCommand) {
    String str = FtcAboutActivity.getLocalAboutInfo().serialize();
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_ABOUT_INFO_RESP", str));
  }
  
  protected void handleCommandRequestConfigurationTemplates() {
    String str = RobotConfigFileManager.serializeXMLConfigList(this.robotCfgFileMgr.getXMLTemplates());
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP", str));
  }
  
  protected void handleCommandRequestConfigurations() {
    String str = RobotConfigFileManager.serializeXMLConfigList(this.robotCfgFileMgr.getXMLFiles());
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS_RESP", str));
  }
  
  protected void handleCommandRequestInspectionReport() {
    InspectionState inspectionState = new InspectionState();
    inspectionState.initializeLocal();
    String str = inspectionState.serialize();
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_INSPECTION_REPORT_RESP", str));
  }
  
  protected void handleCommandRequestParticularConfiguration(String paramString) {
    RobotConfigFile robotConfigFile = this.robotCfgFileMgr.getConfigFromString(paramString);
    ReadXMLFileHandler readXMLFileHandler = new ReadXMLFileHandler();
    if (robotConfigFile.isNoConfig())
      return; 
    try {
      String str = (new WriteXMLFileHandler()).toXml(readXMLFileHandler.parse(robotConfigFile.getXml()));
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("FtcEventLoop: handleCommandRequestParticularConfigFile, data: ");
      stringBuilder.append(str);
      RobotLog.vv("FtcConfigTag", stringBuilder.toString());
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP", str));
      return;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStackTrace((Throwable)robotCoreException);
      return;
    } 
  }
  
  protected void handleCommandRequestRememberedGroups() {
    String str = WifiDirectGroupName.serializeNames((new WifiDirectPersistentGroupManager(WifiDirectAgent.getInstance())).getPersistentGroups());
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_REMEMBERED_GROUPS_RESP", str));
  }
  
  protected void handleCommandRestartRobot() {
    this.ftcEventLoopHandler.restartRobot();
  }
  
  protected void handleCommandSaveConfiguration(String paramString) {
    String[] arrayOfString = paramString.split(";");
    try {
      RobotConfigFile robotConfigFile = this.robotCfgFileMgr.getConfigFromString(arrayOfString[0]);
      this.robotCfgFileMgr.writeToFile(robotConfigFile, false, arrayOfString[1]);
      this.robotCfgFileMgr.setActiveConfigAndUpdateUI(false, robotConfigFile);
      return;
    } catch (RobotCoreException robotCoreException) {
    
    } catch (IOException iOException) {}
    iOException.printStackTrace();
  }
  
  protected void handleCommandScan(String paramString) throws RobotCoreException, InterruptedException {
    RobotLog.vv("FtcConfigTag", "handling command SCAN");
    final USBScanManager usbScanManager = startUsbScanMangerIfNecessary();
    final ThreadPool.SingletonResult future = uSBScanManager.startDeviceScanIfNecessary();
    ThreadPool.getDefault().execute(new Runnable() {
          public void run() {
            try {
              ScannedDevices scannedDevices2 = (ScannedDevices)future.await();
              ScannedDevices scannedDevices1 = scannedDevices2;
              if (scannedDevices2 == null)
                scannedDevices1 = new ScannedDevices(); 
              String str = usbScanManager.packageCommandResponse(scannedDevices1);
              RobotLog.vv("FtcConfigTag", "handleCommandScan data='%s'", new Object[] { str });
              FtcEventLoopBase.this.networkConnectionHandler.sendCommand(new Command("CMD_SCAN_RESP", str));
              return;
            } catch (InterruptedException interruptedException) {
              Thread.currentThread().interrupt();
              return;
            } 
          }
        });
  }
  
  protected void handleCommandShowDialog(Command paramCommand) {
    RobotCoreCommandList.ShowDialog showDialog = RobotCoreCommandList.ShowDialog.deserialize(paramCommand.getExtra());
    AppUtil.DialogParams dialogParams = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, showDialog.title, showDialog.message);
    dialogParams.uuidString = showDialog.uuidString;
    AppUtil.getInstance().showDialog(dialogParams);
  }
  
  protected void handleCommandShowProgress(Command paramCommand) {
    RobotCoreCommandList.ShowProgress showProgress = RobotCoreCommandList.ShowProgress.deserialize(paramCommand.getExtra());
    AppUtil.getInstance().showProgress(UILocation.ONLY_LOCAL, showProgress.message, (ProgressParameters)showProgress);
  }
  
  protected void handleCommandShowToast(Command paramCommand) {
    RobotCoreCommandList.ShowToast showToast = RobotCoreCommandList.ShowToast.deserialize(paramCommand.getExtra());
    AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, showToast.message, showToast.duration);
  }
  
  protected void handleCommandStartDriverStationProgramAndManage() {
    EventLoopManager eventLoopManager = this.ftcEventLoopHandler.getEventLoopManager();
    if (eventLoopManager != null) {
      String str = eventLoopManager.getWebServer().getConnectionInformation().toJson();
      RobotLog.vv("FtcEventLoop", "sending p&m resp: %s", new Object[] { str });
      this.networkConnectionHandler.sendCommand(new Command("CMD_START_DS_PROGRAM_AND_MANAGE_RESP", str));
      return;
    } 
    RobotLog.vv("FtcEventLoop", "handleCommandStartDriverStationProgramAndManage() with null EventLoopManager; ignored");
  }
  
  protected CallbackResult handleCommandVisuallyConfirmWifiBandSwitch(Command paramCommand) {
    if (!LynxConstants.isRevControlHub())
      return CallbackResult.HANDLED; 
    final int newBand = Integer.parseInt(paramCommand.getExtra());
    ThreadPool.getDefaultSerial().execute(new Runnable() {
          public void run() {
            LynxModule lynxModule = EmbeddedControlHubModule.get();
            if (lynxModule != null) {
              short s;
              if (newBand == 1) {
                s = -65281;
              } else {
                s = -256;
              } 
              ArrayList<Blinker.Step> arrayList = new ArrayList();
              arrayList.add(new Blinker.Step(s, 200L, TimeUnit.MILLISECONDS));
              arrayList.add(new Blinker.Step(-16777216, 100L, TimeUnit.MILLISECONDS));
              lynxModule.pushPattern(arrayList);
              try {
                Thread.sleep(6000L);
              } catch (InterruptedException interruptedException) {
                RobotLog.ee("FtcEventLoop", interruptedException, "Thread interrupted while visually confirming WiFi band switch");
                Thread.currentThread().interrupt();
              } 
              lynxModule.popPattern();
            } 
          }
        });
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandVisuallyConfirmWifiReset() {
    if (!LynxConstants.isRevControlHub())
      return CallbackResult.HANDLED; 
    ThreadPool.getDefaultSerial().execute(new Runnable() {
          public void run() {
            LynxModule lynxModule = EmbeddedControlHubModule.get();
            if (lynxModule != null) {
              ArrayList<Blinker.Step> arrayList = new ArrayList();
              arrayList.add(new Blinker.Step(-65281, 100L, TimeUnit.MILLISECONDS));
              arrayList.add(new Blinker.Step(-256, 100L, TimeUnit.MILLISECONDS));
              arrayList.add(new Blinker.Step(-16711681, 100L, TimeUnit.MILLISECONDS));
              arrayList.add(new Blinker.Step(-65536, 100L, TimeUnit.MILLISECONDS));
              lynxModule.pushPattern(arrayList);
              try {
                Thread.sleep(4000L);
              } catch (InterruptedException interruptedException) {
                RobotLog.ee("FtcEventLoop", interruptedException, "Thread interrupted while visually confirming WiFi reset");
                Thread.currentThread().interrupt();
              } 
              lynxModule.popPattern();
            } 
          }
        });
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandVisuallyIdentify(Command paramCommand) {
    final CommandList.CmdVisuallyIdentify cmdVisuallyIdentify = CommandList.CmdVisuallyIdentify.deserialize(paramCommand.getExtra());
    ThreadPool.getDefaultSerial().execute(new Runnable() {
          public void run() {
            VisuallyIdentifiableHardwareDevice visuallyIdentifiableHardwareDevice = FtcEventLoopBase.this.ftcEventLoopHandler.<VisuallyIdentifiableHardwareDevice>getHardwareDevice(VisuallyIdentifiableHardwareDevice.class, cmdVisuallyIdentify.serialNumber, new Supplier<USBScanManager>() {
                  public USBScanManager get() {
                    return FtcEventLoopBase.this.startUsbScanMangerIfNecessary();
                  }
                });
            if (visuallyIdentifiableHardwareDevice != null)
              visuallyIdentifiableHardwareDevice.visuallyIdentify(cmdVisuallyIdentify.shouldIdentify); 
          }
        });
    return CallbackResult.HANDLED;
  }
  
  public void init(EventLoopManager paramEventLoopManager) throws RobotCoreException, InterruptedException {}
  
  public CallbackResult processCommand(Command paramCommand) throws InterruptedException, RobotCoreException {
    CallbackResult callbackResult = CallbackResult.HANDLED;
    String str1 = paramCommand.getName();
    String str2 = paramCommand.getExtra();
    if (str1.equals("CMD_RESTART_ROBOT")) {
      handleCommandRestartRobot();
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_CONFIGURATIONS")) {
      handleCommandRequestConfigurations();
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_REMEMBERED_GROUPS")) {
      handleCommandRequestRememberedGroups();
      return callbackResult;
    } 
    if (str1.equals("CMD_CLEAR_REMEMBERED_GROUPS")) {
      handleCommandClearRememberedGroups();
      return callbackResult;
    } 
    if (str1.equals("CMD_SCAN")) {
      handleCommandScan(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_DISCOVER_LYNX_MODULES")) {
      handleCommandDiscoverLynxModules(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_LYNX_FIRMWARE_UPDATE")) {
      handleCommandLynxFirmwareUpdate(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_GET_USB_ACCESSIBLE_LYNX_MODULES")) {
      handleCommandGetUSBAccessibleLynxModules(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_LYNX_ADDRESS_CHANGE")) {
      handleCommandLynxChangeModuleAddresses(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_GET_CANDIDATE_LYNX_FIRMWARE_IMAGES")) {
      handleCommandGetCandidateLynxFirmwareImages(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_INSPECTION_REPORT")) {
      handleCommandRequestInspectionReport();
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_ABOUT_INFO")) {
      handleCommandRequestAboutInfo(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_DISCONNECT_FROM_WIFI_DIRECT")) {
      handleCommandDisconnectWifiDirect();
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_CONFIGURATION_TEMPLATES")) {
      handleCommandRequestConfigurationTemplates();
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION")) {
      handleCommandRequestParticularConfiguration(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_ACTIVATE_CONFIGURATION")) {
      handleCommandActivateConfiguration(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_REQUEST_UI_STATE")) {
      sendUIState();
      return callbackResult;
    } 
    if (str1.equals("CMD_SAVE_CONFIGURATION")) {
      handleCommandSaveConfiguration(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_DELETE_CONFIGURATION")) {
      handleCommandDeleteConfiguration(str2);
      return callbackResult;
    } 
    if (str1.equals("CMD_START_DS_PROGRAM_AND_MANAGE")) {
      handleCommandStartDriverStationProgramAndManage();
      return callbackResult;
    } 
    if (str1.equals("CMD_SHOW_TOAST")) {
      handleCommandShowToast(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_SHOW_DIALOG")) {
      handleCommandShowDialog(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_DISMISS_DIALOG")) {
      handleCommandDismissDialog(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_DISMISS_ALL_DIALOGS")) {
      handleCommandDismissAllDialogs(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_SHOW_PROGRESS")) {
      handleCommandShowProgress(paramCommand);
      return callbackResult;
    } 
    if (str1.equals("CMD_DISMISS_PROGRESS")) {
      handleCommandDismissProgress(paramCommand);
      return callbackResult;
    } 
    return str1.equals("CMD_ROBOT_CONTROLLER_PREFERENCE") ? PreferenceRemoterRC.getInstance().handleCommandRobotControllerPreference(str2) : (str1.equals("CMD_PLAY_SOUND") ? SoundPlayer.getInstance().handleCommandPlaySound(str2) : (str1.equals("CMD_REQUEST_SOUND") ? SoundPlayer.getInstance().handleCommandRequestSound(paramCommand) : (str1.equals("CMD_STOP_PLAYING_SOUNDS") ? SoundPlayer.getInstance().handleCommandStopPlayingSounds(paramCommand) : (str1.equals("CMD_REQUEST_FRAME") ? CameraStreamServer.getInstance().handleRequestFrame() : (str1.equals("CMD_VISUALLY_IDENTIFY") ? handleCommandVisuallyIdentify(paramCommand) : (str1.equals("CMD_VISUALLY_CONFIRM_WIFI_RESET") ? handleCommandVisuallyConfirmWifiReset() : (str1.equals("CMD_VISUALLY_CONFIRM_WIFI_BAND_SWITCH") ? handleCommandVisuallyConfirmWifiBandSwitch(paramCommand) : CallbackResult.NOT_HANDLED)))))));
  }
  
  protected void sendUIState() {
    String str = this.robotCfgFileMgr.getActiveConfig().toString();
    this.networkConnectionHandler.sendCommand(new Command("CMD_NOTIFY_ACTIVE_CONFIGURATION", str));
    ConfigurationTypeManager.getInstance().sendUserDeviceTypes();
    this.registeredOpModes.waitOpModesRegistered();
    str = SimpleGson.getInstance().toJson(this.registeredOpModes.getOpModes());
    this.networkConnectionHandler.sendCommand(new Command("CMD_NOTIFY_OP_MODE_LIST", str));
  }
  
  protected USBScanManager startUsbScanMangerIfNecessary() {
    USBScanManager uSBScanManager2 = this.usbScanManager;
    USBScanManager uSBScanManager1 = uSBScanManager2;
    if (uSBScanManager2 == null) {
      uSBScanManager1 = new USBScanManager((Context)this.activityContext, false);
      this.usbScanManager = uSBScanManager1;
      uSBScanManager1.startExecutorService();
    } 
    return uSBScanManager1;
  }
  
  protected void talkToParentLynxModule(DeviceManager paramDeviceManager, LynxUsbDevice paramLynxUsbDevice, int paramInt, Consumer<LynxModule> paramConsumer) throws RobotCoreException, InterruptedException, LynxNackException {
    LynxModule lynxModule2 = paramLynxUsbDevice.getConfiguredModule(paramInt);
    if (lynxModule2 != null) {
      paramConsumer.accept(lynxModule2);
      return;
    } 
    LynxModule lynxModule1 = (LynxModule)paramDeviceManager.createLynxModule((RobotCoreLynxUsbDevice)paramLynxUsbDevice, paramInt, true, null);
    lynxModule1.setUserModule(false);
    paramLynxUsbDevice.addConfiguredModule(lynxModule1);
    try {
      paramConsumer.accept(lynxModule1);
      return;
    } finally {
      lynxModule1.removeAsConfigured();
      lynxModule1.close();
    } 
  }
  
  public void teardown() throws RobotCoreException, InterruptedException {
    USBScanManager uSBScanManager = this.usbScanManager;
    if (uSBScanManager != null) {
      uSBScanManager.stopExecutorService();
      this.usbScanManager = null;
    } 
  }
  
  protected boolean updateFirmwareOnce(LynxUsbDevice paramLynxUsbDevice, byte[] paramArrayOfbyte, SerialNumber paramSerialNumber, Consumer<ProgressParameters> paramConsumer) {
    if (enterFirmwareUpdateMode(paramLynxUsbDevice.getRobotUsbDevice())) {
      FlashLoaderManager flashLoaderManager = new FlashLoaderManager(paramLynxUsbDevice.getRobotUsbDevice(), paramArrayOfbyte);
      try {
        flashLoaderManager.updateFirmware(paramConsumer);
        return true;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        RobotLog.ee("FtcEventLoop", "interrupt while updating firmware: serial=%s", new Object[] { paramSerialNumber });
        return false;
      } catch (FlashLoaderProtocolException flashLoaderProtocolException) {
        RobotLog.ee("FtcEventLoop", (Throwable)flashLoaderProtocolException, "exception while updating firmware: serial=%s", new Object[] { paramSerialNumber });
        return false;
      } 
    } 
    RobotLog.ee("FtcEventLoop", "failed to enter firmware update mode");
    return false;
  }
  
  protected RobotCoreCommandList.LynxFirmwareUpdateResp updateLynxFirmware(final SerialNumber serialNumber, final RobotCoreCommandList.FWImage image, String paramString) {
    RobotCoreCommandList.LynxFirmwareUpdateResp lynxFirmwareUpdateResp = new RobotCoreCommandList.LynxFirmwareUpdateResp();
    lynxFirmwareUpdateResp.success = false;
    lynxFirmwareUpdateResp.originatorId = paramString;
    if (!this.firmwareUpdateInProgress.compareAndSet(false, true)) {
      lynxFirmwareUpdateResp.errorMessage = AppUtil.getDefContext().getString(R.string.lynxFirmwareUpdateAlreadyInProgress);
      RobotLog.vv("FtcEventLoop", "Cannot update firmware: a firmware update is already in progress");
      return lynxFirmwareUpdateResp;
    } 
    RobotLog.vv("FtcEventLoop", "updateLynxFirmware(%s, %s)", new Object[] { serialNumber, image.getName() });
    Consumer<ProgressParameters> consumer = new Consumer<ProgressParameters>() {
        Double prevPercentComplete = null;
        
        public void accept(ProgressParameters param1ProgressParameters) {
          double d = Math.round(param1ProgressParameters.fractionComplete() * 100.0D);
          Double double_ = this.prevPercentComplete;
          if (double_ == null || double_.doubleValue() != d) {
            String str;
            this.prevPercentComplete = Double.valueOf(d);
            if (updatingControlHub) {
              str = String.format(FtcEventLoopBase.this.activityContext.getString(R.string.controlHubFirmwareUpdateMessage), new Object[] { this.val$image.getName() });
            } else {
              str = String.format(FtcEventLoopBase.this.activityContext.getString(R.string.expansionHubFirmwareUpdateMessage), new Object[] { this.val$serialNumber, this.val$image.getName() });
            } 
            AppUtil.getInstance().showProgress(UILocation.BOTH, str, param1ProgressParameters.fractionComplete(), 100);
          } 
        }
      };
    try {
      consumer.accept(new ProgressParameters(0, 1));
      LynxUsbDevice lynxUsbDevice = getLynxUsbDeviceForFirmwareUpdate(serialNumber);
      if (lynxUsbDevice != null) {
        try {
          byte[] arrayOfByte = ReadWriteFile.readBytes(image);
          if (arrayOfByte.length > 0) {
            RobotLog.vv("FtcEventLoop", "disengaging lynx usb device %s", new Object[] { lynxUsbDevice.getSerialNumber() });
            lynxUsbDevice.disengage();
            int i = 0;
            while (i < 2) {
              try {
                RobotLog.vv("FtcEventLoop", "trying firmware update: count=%d", new Object[] { Integer.valueOf(i) });
              } finally {
                RobotLog.vv("FtcEventLoop", "reengaging lynx usb device %s", new Object[] { lynxUsbDevice.getSerialNumber() });
                lynxUsbDevice.engage();
              } 
            } 
            RobotLog.vv("FtcEventLoop", "reengaging lynx usb device %s", new Object[] { lynxUsbDevice.getSerialNumber() });
            lynxUsbDevice.engage();
          } else {
            RobotLog.ee("FtcEventLoop", "firmware image file unexpectedly empty");
          } 
        } finally {
          lynxUsbDevice.close();
          this.firmwareUpdateInProgress.set(false);
        } 
      } else {
        RobotLog.ee("FtcEventLoop", "unable to obtain lynx usb device for fw update: %s", new Object[] { serialNumber });
      } 
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("FtcEventLoop", runtimeException, "RuntimeException in updateLynxFirmware()");
    } finally {}
    AppUtil.getInstance().dismissProgress(UILocation.BOTH);
    RobotLog.vv("FtcEventLoop", "updateLynxFirmware(%s, %s): result=%s", new Object[] { serialNumber, image.getName(), lynxFirmwareUpdateResp.serialize() });
    return lynxFirmwareUpdateResp;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcEventLoopBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */