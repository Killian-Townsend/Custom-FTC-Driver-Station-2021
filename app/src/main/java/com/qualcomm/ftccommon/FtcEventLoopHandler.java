package com.qualcomm.ftccommon;

import android.content.Context;
import com.qualcomm.ftccommon.configuration.USBScanManager;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxUsbDeviceImpl;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareDeviceCloseOnTearDown;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationUtility;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.MovingStatistics;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.function.Supplier;

public class FtcEventLoopHandler implements BatteryChecker.BatteryWatcher {
  protected static final boolean DEBUG = false;
  
  public static final String NO_VOLTAGE_SENSOR = "$no$voltage$sensor$";
  
  public static final String TAG = "FtcEventLoopHandler";
  
  protected final UpdateUI.Callback callback;
  
  protected EventLoopManager eventLoopManager;
  
  protected final HardwareFactory hardwareFactory;
  
  protected HardwareMap hardwareMap = null;
  
  protected HardwareMap hardwareMapExtra = null;
  
  protected final Object refreshUserTelemetryLock = new Object();
  
  protected double robotBatteryInterval = 3.0D;
  
  protected double robotBatteryLoggingInterval = this.robotControllerBatteryCheckerInterval;
  
  protected ElapsedTime robotBatteryLoggingTimer = null;
  
  protected MovingStatistics robotBatteryStatistics = new MovingStatistics(10);
  
  protected ElapsedTime robotBatteryTimer = new ElapsedTime();
  
  protected BatteryChecker robotControllerBatteryChecker;
  
  protected double robotControllerBatteryCheckerInterval = 180.0D;
  
  protected final Context robotControllerContext;
  
  protected double updateUIInterval = 0.25D;
  
  protected ElapsedTime updateUITimer = new ElapsedTime();
  
  protected double userTelemetryInterval = 0.25D;
  
  protected ElapsedTime userTelemetryTimer = new ElapsedTime(0L);
  
  public FtcEventLoopHandler(HardwareFactory paramHardwareFactory, UpdateUI.Callback paramCallback, Context paramContext) {
    this.hardwareFactory = paramHardwareFactory;
    this.callback = paramCallback;
    this.robotControllerContext = paramContext;
    this.robotControllerBatteryChecker = new BatteryChecker(this, (long)(this.robotControllerBatteryCheckerInterval * 1000.0D));
  }
  
  private String buildRobotBatteryMsg() {
    HardwareMap hardwareMap = this.hardwareMap;
    if (hardwareMap == null)
      return null; 
    Iterator<VoltageSensor> iterator = hardwareMap.voltageSensor.iterator();
    double d = Double.POSITIVE_INFINITY;
    while (iterator.hasNext()) {
      VoltageSensor voltageSensor = iterator.next();
      long l1 = System.nanoTime();
      double d1 = voltageSensor.getVoltage();
      long l2 = System.nanoTime();
      if (d1 >= 1.0D) {
        this.robotBatteryStatistics.add((l2 - l1) / 1000000.0D);
        if (d1 < d)
          d = d1; 
      } 
    } 
    if (d == Double.POSITIVE_INFINITY)
      return "$no$voltage$sensor$"; 
    String str = Integer.toString((int)(d * 100.0D));
    return (new StringBuilder(str)).insert(str.length() - 2, ".").toString();
  }
  
  protected static void closeAutoCloseOnTeardown(HardwareMap paramHardwareMap) {
    if (paramHardwareMap != null) {
      Iterator<HardwareDeviceCloseOnTearDown> iterator = paramHardwareMap.getAll(HardwareDeviceCloseOnTearDown.class).iterator();
      while (iterator.hasNext())
        ((HardwareDeviceCloseOnTearDown)iterator.next()).close(); 
    } 
  }
  
  protected static void closeHardwareMap(HardwareMap paramHardwareMap) {
    closeMotorControllers(paramHardwareMap);
    closeServoControllers(paramHardwareMap);
    closeAutoCloseOnTeardown(paramHardwareMap);
  }
  
  protected static void closeMotorControllers(HardwareMap paramHardwareMap) {
    if (paramHardwareMap != null) {
      Iterator<DcMotorController> iterator = paramHardwareMap.getAll(DcMotorController.class).iterator();
      while (iterator.hasNext())
        ((DcMotorController)iterator.next()).close(); 
    } 
  }
  
  protected static void closeServoControllers(HardwareMap paramHardwareMap) {
    if (paramHardwareMap != null) {
      Iterator<ServoController> iterator = paramHardwareMap.getAll(ServoController.class).iterator();
      while (iterator.hasNext())
        ((ServoController)iterator.next()).close(); 
    } 
  }
  
  public void close() {
    closeHardwareMap(this.hardwareMap);
    closeHardwareMap(this.hardwareMapExtra);
    closeBatteryMonitoring();
    this.eventLoopManager = null;
  }
  
  protected void closeBatteryMonitoring() {
    this.robotControllerBatteryChecker.close();
  }
  
  public void displayGamePadInfo(String paramString) {
    if (this.updateUITimer.time() > this.updateUIInterval) {
      this.updateUITimer.reset();
      Gamepad[] arrayOfGamepad = getGamepads();
      this.callback.updateUi(paramString, arrayOfGamepad);
    } 
  }
  
  public EventLoopManager getEventLoopManager() {
    return this.eventLoopManager;
  }
  
  public List<LynxUsbDeviceImpl> getExtantLynxDeviceImpls() {
    synchronized (this.hardwareFactory) {
      ArrayList<LynxUsbDeviceImpl> arrayList = new ArrayList();
      if (this.hardwareMap != null)
        for (LynxUsbDevice lynxUsbDevice : this.hardwareMap.getAll(LynxUsbDevice.class)) {
          if (lynxUsbDevice.getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.ARMED)
            arrayList.add(lynxUsbDevice.getDelegationTarget()); 
        }  
      if (this.hardwareMapExtra != null)
        for (LynxUsbDevice lynxUsbDevice : this.hardwareMapExtra.getAll(LynxUsbDevice.class)) {
          if (lynxUsbDevice.getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.ARMED)
            arrayList.add(lynxUsbDevice.getDelegationTarget()); 
        }  
      return arrayList;
    } 
  }
  
  public Gamepad[] getGamepads() {
    EventLoopManager eventLoopManager = this.eventLoopManager;
    return (eventLoopManager != null) ? eventLoopManager.getGamepads() : new Gamepad[2];
  }
  
  public <T> T getHardwareDevice(Class<? extends T> paramClass, SerialNumber paramSerialNumber, Supplier<USBScanManager> paramSupplier) {
    synchronized (this.hardwareFactory) {
      RobotLog.vv("FtcEventLoopHandler", "getHardwareDevice(%s)...", new Object[] { paramSerialNumber });
      Supplier<USBScanManager> supplier = null;
      try {
        getHardwareMap();
        Object object2 = this.hardwareMap.get(Object.class, paramSerialNumber);
        Object object1 = object2;
        if (object2 == null)
          object1 = this.hardwareMapExtra.get(Object.class, paramSerialNumber); 
        object2 = object1;
        if (object1 == null) {
          boolean bool;
          SerialNumber serialNumber = paramSerialNumber.getScannableDeviceSerialNumber();
          if (!serialNumber.equals(paramSerialNumber) && (this.hardwareMap.get(Object.class, serialNumber) != null || this.hardwareMapExtra.get(Object.class, serialNumber) != null)) {
            RobotLog.ee("FtcEventLoopHandler", "internal error: %s absent but scannable %s present", new Object[] { paramSerialNumber, serialNumber });
            bool = false;
          } else {
            bool = true;
          } 
          object2 = object1;
          if (bool) {
            USBScanManager uSBScanManager = (USBScanManager)paramSupplier.get();
            if (uSBScanManager != null) {
              object2 = object1;
              Object object = object1;
              try {
                ScannedDevices scannedDevices = uSBScanManager.awaitScannedDevices();
                object2 = object1;
                object = object1;
                if (scannedDevices.containsKey(serialNumber)) {
                  object2 = object1;
                  object = object1;
                  ControllerConfiguration controllerConfiguration = (new ConfigurationUtility()).buildNewControllerConfiguration(serialNumber, scannedDevices.get(serialNumber), uSBScanManager.getLynxModuleMetaListSupplier(serialNumber));
                  if (controllerConfiguration != null) {
                    object2 = object1;
                    object = object1;
                    controllerConfiguration.setEnabled(true);
                    object2 = object1;
                    object = object1;
                    controllerConfiguration.setKnownToBeAttached(true);
                    object2 = object1;
                    object = object1;
                    this.hardwareFactory.instantiateConfiguration(this.hardwareMapExtra, controllerConfiguration, (SyncdDevice.Manager)this.eventLoopManager);
                    object2 = object1;
                    object = object1;
                    object1 = this.hardwareMapExtra.get(Object.class, paramSerialNumber);
                    object2 = object1;
                    object = object1;
                    RobotLog.ii("FtcEventLoopHandler", "found %s: hardwareMapExtra:", new Object[] { paramSerialNumber });
                    object2 = object1;
                    object = object1;
                    this.hardwareMapExtra.logDevices();
                    object2 = object1;
                  } else {
                    object2 = object1;
                    object = object1;
                    RobotLog.ee("FtcEventLoopHandler", "buildNewControllerConfiguration(%s) failed", new Object[] { serialNumber });
                    object2 = object1;
                  } 
                } else {
                  object2 = object1;
                  object = object1;
                  RobotLog.ee("FtcEventLoopHandler", "");
                  object2 = object1;
                } 
              } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                object2 = object;
              } catch (RobotCoreException robotCoreException) {
                RobotLog.ee("FtcEventLoopHandler", (Throwable)robotCoreException, "exception in getHardwareDevice(%s)", new Object[] { paramSerialNumber });
              } 
            } else {
              RobotLog.ee("FtcEventLoopHandler", "usbScanManager supplied as null");
              object2 = interruptedException;
            } 
          } 
        } 
        paramSupplier = supplier;
        if (object2 != null) {
          paramSupplier = supplier;
          if (paramClass.isInstance(object2))
            paramSupplier = (Supplier<USBScanManager>)paramClass.cast(object2); 
        } 
        RobotLog.vv("FtcEventLoopHandler", "...getHardwareDevice(%s)=%s,%s", new Object[] { paramSerialNumber, object2, paramSupplier });
        return (T)paramSupplier;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        return null;
      } catch (RobotCoreException robotCoreException) {}
      return null;
    } 
  }
  
  public HardwareMap getHardwareMap() throws RobotCoreException, InterruptedException {
    synchronized (this.hardwareFactory) {
      if (this.hardwareMap == null) {
        this.hardwareMap = this.hardwareFactory.createHardwareMap((SyncdDevice.Manager)this.eventLoopManager);
        this.hardwareMapExtra = new HardwareMap(this.robotControllerContext);
      } 
      return this.hardwareMap;
    } 
  }
  
  public String getOpMode(String paramString) {
    EventLoopManager eventLoopManager = this.eventLoopManager;
    return (eventLoopManager == null || eventLoopManager.state != RobotState.RUNNING) ? "$Stop$Robot$" : paramString;
  }
  
  public void init(EventLoopManager paramEventLoopManager) {
    this.eventLoopManager = paramEventLoopManager;
    this.robotControllerBatteryChecker.startBatteryMonitoring();
  }
  
  public void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: getfield refreshUserTelemetryLock : Ljava/lang/Object;
    //   4: astore #9
    //   6: aload #9
    //   8: monitorenter
    //   9: dload_2
    //   10: dstore #4
    //   12: dload_2
    //   13: invokestatic isNaN : (D)Z
    //   16: ifeq -> 25
    //   19: aload_0
    //   20: getfield userTelemetryInterval : D
    //   23: dstore #4
    //   25: aload_0
    //   26: getfield userTelemetryTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   29: invokevirtual seconds : ()D
    //   32: dstore_2
    //   33: iconst_1
    //   34: istore #8
    //   36: dload_2
    //   37: dload #4
    //   39: dcmpl
    //   40: iflt -> 165
    //   43: iconst_1
    //   44: istore #6
    //   46: goto -> 49
    //   49: iload #8
    //   51: istore #7
    //   53: aload_0
    //   54: getfield robotBatteryTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   57: invokevirtual seconds : ()D
    //   60: aload_0
    //   61: getfield robotBatteryInterval : D
    //   64: dcmpl
    //   65: ifge -> 174
    //   68: iload #6
    //   70: ifeq -> 171
    //   73: aload_0
    //   74: getfield robotBatteryStatistics : Lcom/qualcomm/robotcore/util/MovingStatistics;
    //   77: invokevirtual getMean : ()D
    //   80: ldc2_w 2.0
    //   83: dcmpg
    //   84: ifge -> 171
    //   87: iload #8
    //   89: istore #7
    //   91: goto -> 174
    //   94: iload #6
    //   96: ifeq -> 106
    //   99: aload_0
    //   100: getfield userTelemetryTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   103: invokevirtual reset : ()V
    //   106: iload #7
    //   108: ifeq -> 129
    //   111: aload_1
    //   112: ldc_w '$Robot$Battery$Level$'
    //   115: aload_0
    //   116: invokespecial buildRobotBatteryMsg : ()Ljava/lang/String;
    //   119: invokevirtual addData : (Ljava/lang/String;Ljava/lang/String;)V
    //   122: aload_0
    //   123: getfield robotBatteryTimer : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   126: invokevirtual reset : ()V
    //   129: aload_1
    //   130: invokevirtual hasData : ()Z
    //   133: ifeq -> 155
    //   136: aload_0
    //   137: getfield eventLoopManager : Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
    //   140: ifnull -> 151
    //   143: aload_0
    //   144: getfield eventLoopManager : Lcom/qualcomm/robotcore/eventloop/EventLoopManager;
    //   147: aload_1
    //   148: invokevirtual sendTelemetryData : (Lcom/qualcomm/robotcore/robocol/TelemetryMessage;)V
    //   151: aload_1
    //   152: invokevirtual clearData : ()V
    //   155: aload #9
    //   157: monitorexit
    //   158: return
    //   159: astore_1
    //   160: aload #9
    //   162: monitorexit
    //   163: aload_1
    //   164: athrow
    //   165: iconst_0
    //   166: istore #6
    //   168: goto -> 49
    //   171: iconst_0
    //   172: istore #7
    //   174: iload #6
    //   176: ifne -> 94
    //   179: iload #7
    //   181: ifeq -> 155
    //   184: goto -> 94
    // Exception table:
    //   from	to	target	type
    //   12	25	159	finally
    //   25	33	159	finally
    //   53	68	159	finally
    //   73	87	159	finally
    //   99	106	159	finally
    //   111	129	159	finally
    //   129	151	159	finally
    //   151	155	159	finally
    //   155	158	159	finally
    //   160	163	159	finally
  }
  
  public void restartRobot() {
    RobotLog.dd("FtcEventLoopHandler", "restarting robot...");
    closeBatteryMonitoring();
    this.callback.restartRobot();
  }
  
  public void sendBatteryInfo() {
    this.robotControllerBatteryChecker.pollBatteryLevel(this);
    String str = buildRobotBatteryMsg();
    if (str != null)
      sendTelemetry("$Robot$Battery$Level$", str); 
  }
  
  public void sendTelemetry(String paramString1, String paramString2) {
    TelemetryMessage telemetryMessage = new TelemetryMessage();
    telemetryMessage.setTag(paramString1);
    telemetryMessage.addData(paramString1, paramString2);
    EventLoopManager eventLoopManager = this.eventLoopManager;
    if (eventLoopManager != null) {
      eventLoopManager.sendTelemetryData(telemetryMessage);
    } else {
      RobotLog.vv("FtcEventLoopHandler", "sendTelemetry() with null EventLoopManager; ignored");
    } 
    telemetryMessage.clearData();
  }
  
  public void updateBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus) {
    sendTelemetry("$RobotController$Battery$Status$", paramBatteryStatus.serialize());
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcEventLoopHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */