package com.qualcomm.ftccommon;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.qualcomm.robotcore.eventloop.EventLoop;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.EventLoopManagerClient;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.factory.RobotFactory;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.hardware.LightBlinker;
import com.qualcomm.robotcore.hardware.LightMultiplexor;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.robot.Robot;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.robot.RobotStatus;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.WebServer;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.hardware.android.DragonboardIndicatorLED;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatus;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterRC;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotserver.internal.webserver.CoreRobotWebServer;

public class FtcRobotControllerService extends Service implements NetworkConnection.NetworkConnectionCallback, WifiDirectAgent.Callback, EventLoopManagerClient {
  private static final int NETWORK_WAIT = 1000;
  
  public static final String TAG = "FTCService";
  
  private static final int USB_WAIT = 5000;
  
  private final IBinder binder = (IBinder)new FtcRobotControllerBinder();
  
  private SwitchableLight bootIndicator = null;
  
  private Future bootIndicatorOff = null;
  
  private UpdateUI.Callback callback = null;
  
  private EventLoop eventLoop;
  
  private EventLoopManager eventLoopManager;
  
  private final EventLoopMonitor eventLoopMonitor = new EventLoopMonitor();
  
  private EventLoop idleEventLoop;
  
  private LightBlinker livenessIndicatorBlinker = null;
  
  private NetworkConnection networkConnection;
  
  private NetworkConnection.NetworkEvent networkConnectionStatus = NetworkConnection.NetworkEvent.UNKNOWN;
  
  private final PreferencesHelper preferencesHelper = new PreferencesHelper("FTCService");
  
  private Robot robot;
  
  private Future robotSetupFuture = null;
  
  private volatile boolean robotSetupHasBeenStarted = false;
  
  private RobotStatus robotStatus = RobotStatus.NONE;
  
  private WebServer webServer;
  
  private WifiDirectAgent wifiDirectAgent = WifiDirectAgent.getInstance();
  
  private final Object wifiDirectCallbackLock = new Object();
  
  private void updateNetworkConnectionStatus(NetworkConnection.NetworkEvent paramNetworkEvent) {
    this.networkConnectionStatus = paramNetworkEvent;
    UpdateUI.Callback callback = this.callback;
    if (callback != null)
      callback.networkConnectionUpdate(paramNetworkEvent); 
  }
  
  private void updateRobotStatus(RobotStatus paramRobotStatus) {
    this.robotStatus = paramRobotStatus;
    UpdateUI.Callback callback = this.callback;
    if (callback != null)
      callback.updateRobotStatus(paramRobotStatus); 
  }
  
  public NetworkConnection getNetworkConnection() {
    return this.networkConnection;
  }
  
  public NetworkConnection.NetworkEvent getNetworkConnectionStatus() {
    return this.networkConnectionStatus;
  }
  
  public Robot getRobot() {
    return this.robot;
  }
  
  public RobotStatus getRobotStatus() {
    return this.robotStatus;
  }
  
  public WebServer getWebServer() {
    return this.webServer;
  }
  
  public IBinder onBind(Intent paramIntent) {
    RobotLog.vv("FTCService", "onBind()");
    this.preferencesHelper.writeBooleanPrefIfDifferent(getString(R.string.pref_wifip2p_remote_channel_change_works), Device.wifiP2pRemoteChannelChangeWorks());
    this.preferencesHelper.writeBooleanPrefIfDifferent(getString(R.string.pref_has_independent_phone_battery), LynxConstants.isRevControlHub() ^ true);
    int i = LynxConstants.isRevControlHub() ^ true;
    this.preferencesHelper.writeBooleanPrefIfDifferent(getString(R.string.pref_has_speaker), i);
    if (i == 0)
      this.preferencesHelper.writeBooleanPrefIfDifferent(getString(R.string.pref_sound_on_off), false); 
    FtcLynxFirmwareUpdateActivity.initializeDirectories();
    NetworkType networkType = (NetworkType)paramIntent.getSerializableExtra("NETWORK_CONNECTION_TYPE");
    this.webServer = (WebServer)new CoreRobotWebServer(networkType);
    NetworkConnection networkConnection = NetworkConnectionFactory.getNetworkConnection(networkType, getBaseContext());
    this.networkConnection = networkConnection;
    networkConnection.setCallback(this);
    this.networkConnection.enable();
    this.networkConnection.createConnection();
    return this.binder;
  }
  
  public void onCreate() {
    super.onCreate();
    RobotLog.vv("FTCService", "onCreate()");
    this.wifiDirectAgent.registerCallback(this);
    startLEDS();
  }
  
  public void onDestroy() {
    super.onDestroy();
    RobotLog.vv("FTCService", "onDestroy()");
    this.webServer.stop();
    stopLEDS();
    this.wifiDirectAgent.unregisterCallback(this);
  }
  
  public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onNetworkConnectionEvent: ");
    stringBuilder.append(paramNetworkEvent.toString());
    RobotLog.ii("FTCService", stringBuilder.toString());
    int i = null.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkConnection$NetworkEvent[paramNetworkEvent.ordinal()];
    if (i != 1) {
      if (i != 2) {
        if (i != 3) {
          if (i != 4) {
            if (i == 5) {
              stringBuilder = new StringBuilder();
              stringBuilder.append("Network Connection created: ");
              stringBuilder.append(this.networkConnection.getConnectionOwnerName());
              RobotLog.ii("FTCService", stringBuilder.toString());
            } 
          } else {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Network Connection Error: ");
            stringBuilder.append(this.networkConnection.getFailureReason());
            RobotLog.ee("FTCService", stringBuilder.toString());
          } 
        } else {
          stringBuilder = new StringBuilder();
          stringBuilder.append("Network Connection Passphrase: ");
          stringBuilder.append(this.networkConnection.getPassphrase());
          RobotLog.ii("FTCService", stringBuilder.toString());
          this.webServer.start();
        } 
      } else {
        RobotLog.ee("FTCService", "Wifi Direct - connected as peer, was expecting Group Owner");
        ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_FIX_CONFIG);
        callbackResult = CallbackResult.HANDLED;
      } 
    } else {
      RobotLog.ii("FTCService", "Wifi Direct - connected as group owner");
      if (!NetworkConnection.isDeviceNameValid(this.networkConnection.getDeviceName())) {
        RobotLog.ee("FTCService", "Network Connection device name contains non-printable characters");
        ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_DEVICE_NAME_INVALID);
        callbackResult = CallbackResult.HANDLED;
      } 
    } 
    updateNetworkConnectionStatus(paramNetworkEvent);
    return callbackResult;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    synchronized (this.wifiDirectCallbackLock) {
      this.wifiDirectCallbackLock.notifyAll();
      return;
    } 
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    RobotLog.vv("FTCService", "onStartCommand()");
    return super.onStartCommand(paramIntent, paramInt1, paramInt2);
  }
  
  public boolean onUnbind(Intent paramIntent) {
    RobotLog.vv("FTCService", "onUnbind()");
    this.networkConnection.disable();
    shutdownRobot();
    EventLoopManager eventLoopManager = this.eventLoopManager;
    if (eventLoopManager != null) {
      eventLoopManager.close();
      this.eventLoopManager = null;
    } 
    return false;
  }
  
  public void setCallback(UpdateUI.Callback paramCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield callback : Lcom/qualcomm/ftccommon/UpdateUI$Callback;
    //   7: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler;
    //   10: invokevirtual isPeerConnected : ()Z
    //   13: ifeq -> 23
    //   16: getstatic org/firstinspires/ftc/robotcore/internal/network/PeerStatus.CONNECTED : Lorg/firstinspires/ftc/robotcore/internal/network/PeerStatus;
    //   19: astore_2
    //   20: goto -> 27
    //   23: getstatic org/firstinspires/ftc/robotcore/internal/network/PeerStatus.DISCONNECTED : Lorg/firstinspires/ftc/robotcore/internal/network/PeerStatus;
    //   26: astore_2
    //   27: aload_1
    //   28: aload_2
    //   29: invokevirtual updatePeerStatus : (Lorg/firstinspires/ftc/robotcore/internal/network/PeerStatus;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	35	finally
    //   23	27	35	finally
    //   27	32	35	finally
  }
  
  public void setupRobot(EventLoop paramEventLoop1, EventLoop paramEventLoop2, Runnable paramRunnable) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual shutdownRobotSetup : ()V
    //   6: aload_0
    //   7: aload_1
    //   8: putfield eventLoop : Lcom/qualcomm/robotcore/eventloop/EventLoop;
    //   11: aload_0
    //   12: aload_2
    //   13: putfield idleEventLoop : Lcom/qualcomm/robotcore/eventloop/EventLoop;
    //   16: aload_0
    //   17: invokestatic getDefault : ()Ljava/util/concurrent/ExecutorService;
    //   20: new com/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable
    //   23: dup
    //   24: aload_0
    //   25: aload_3
    //   26: invokespecial <init> : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;Ljava/lang/Runnable;)V
    //   29: invokeinterface submit : (Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
    //   34: putfield robotSetupFuture : Ljava/util/concurrent/Future;
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
    //   2	37	40	finally
  }
  
  public void shutdownRobot() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual shutdownRobotSetup : ()V
    //   6: aload_0
    //   7: getfield robot : Lcom/qualcomm/robotcore/robot/Robot;
    //   10: ifnull -> 20
    //   13: aload_0
    //   14: getfield robot : Lcom/qualcomm/robotcore/robot/Robot;
    //   17: invokevirtual shutdown : ()V
    //   20: aload_0
    //   21: aconst_null
    //   22: putfield robot : Lcom/qualcomm/robotcore/robot/Robot;
    //   25: aload_0
    //   26: getstatic com/qualcomm/robotcore/robot/RobotStatus.NONE : Lcom/qualcomm/robotcore/robot/RobotStatus;
    //   29: invokespecial updateRobotStatus : (Lcom/qualcomm/robotcore/robot/RobotStatus;)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	35	finally
    //   20	32	35	finally
  }
  
  void shutdownRobotSetup() {
    Future future = this.robotSetupFuture;
    if (future != null) {
      ThreadPool.cancelFutureOrExitApplication(future, 10L, TimeUnit.SECONDS, "robot setup", "internal error");
      this.robotSetupFuture = null;
    } 
  }
  
  protected void startLEDS() {
    if (LynxConstants.useIndicatorLEDS()) {
      for (int i = 1; i <= 4; i++)
        DragonboardIndicatorLED.forIndex(i).enableLight(false); 
      LightMultiplexor lightMultiplexor = LightMultiplexor.forLight((SwitchableLight)DragonboardIndicatorLED.forIndex(4));
      this.bootIndicator = (SwitchableLight)lightMultiplexor;
      lightMultiplexor.enableLight(true);
      this.bootIndicatorOff = ThreadPool.getDefaultScheduler().schedule(new Runnable() {
            public void run() {
              FtcRobotControllerService.this.bootIndicator.enableLight(false);
            }
          },  10L, TimeUnit.SECONDS);
      this.livenessIndicatorBlinker = new LightBlinker((SwitchableLight)LightMultiplexor.forLight((SwitchableLight)DragonboardIndicatorLED.forIndex(1)));
      ArrayList<Blinker.Step> arrayList = new ArrayList();
      arrayList.add(new Blinker.Step(-16711936, 4500L, TimeUnit.MILLISECONDS));
      arrayList.add(new Blinker.Step(-16777216, 500L, TimeUnit.MILLISECONDS));
      this.livenessIndicatorBlinker.setPattern(arrayList);
    } 
  }
  
  protected void stopLEDS() {
    Future future = this.bootIndicatorOff;
    if (future != null) {
      future.cancel(false);
      this.bootIndicatorOff = null;
    } 
    SwitchableLight switchableLight = this.bootIndicator;
    if (switchableLight != null) {
      switchableLight.enableLight(false);
      this.bootIndicator = null;
    } 
    LightBlinker lightBlinker = this.livenessIndicatorBlinker;
    if (lightBlinker != null) {
      lightBlinker.stopBlinking();
      this.livenessIndicatorBlinker = null;
    } 
  }
  
  void waitForNextWifiDirectCallback() throws InterruptedException {
    synchronized (this.wifiDirectCallbackLock) {
      this.wifiDirectCallbackLock.wait();
      return;
    } 
  }
  
  private class EventLoopMonitor implements EventLoopManager.EventLoopMonitor {
    private EventLoopMonitor() {}
    
    private void updatePeerStatus(PeerStatus param1PeerStatus) {
      if (FtcRobotControllerService.this.callback == null)
        return; 
      FtcRobotControllerService.this.callback.updatePeerStatus(param1PeerStatus);
    }
    
    public void onPeerConnected() {
      updatePeerStatus(PeerStatus.CONNECTED);
      RobotLog.dd("FTCService", "Sending user device types and preferences to driver station");
      ConfigurationTypeManager.getInstance().sendUserDeviceTypes();
      PreferenceRemoterRC.getInstance().sendAllPreferences();
    }
    
    public void onPeerDisconnected() {
      updatePeerStatus(PeerStatus.DISCONNECTED);
    }
    
    public void onStateChange(RobotState param1RobotState) {
      if (FtcRobotControllerService.this.callback == null)
        return; 
      FtcRobotControllerService.this.callback.updateRobotState(param1RobotState);
      if (param1RobotState == RobotState.RUNNING)
        FtcRobotControllerService.this.updateRobotStatus(RobotStatus.NONE); 
    }
    
    public void onTelemetryTransmitted() {
      if (FtcRobotControllerService.this.callback == null)
        return; 
      FtcRobotControllerService.this.callback.refreshErrorTextOnUiThread();
    }
  }
  
  public class FtcRobotControllerBinder extends Binder {
    public FtcRobotControllerService getService() {
      return FtcRobotControllerService.this;
    }
  }
  
  private class RobotSetupRunnable implements Runnable {
    Runnable runOnComplete;
    
    RobotSetupRunnable(Runnable param1Runnable) {
      this.runOnComplete = param1Runnable;
    }
    
    void awaitUSB() throws InterruptedException {
      FtcRobotControllerService.this.updateRobotStatus(RobotStatus.SCANNING_USB);
      Thread.sleep(5000L);
    }
    
    void initializeEventLoopAndRobot() throws RobotCoreException {
      if (FtcRobotControllerService.this.eventLoopManager == null) {
        FtcRobotControllerService ftcRobotControllerService1 = FtcRobotControllerService.this;
        FtcRobotControllerService ftcRobotControllerService2 = FtcRobotControllerService.this;
        FtcRobotControllerService.access$402(ftcRobotControllerService1, new EventLoopManager((Context)ftcRobotControllerService2, ftcRobotControllerService2, ftcRobotControllerService2.idleEventLoop));
      } 
      FtcRobotControllerService ftcRobotControllerService = FtcRobotControllerService.this;
      FtcRobotControllerService.access$302(ftcRobotControllerService, RobotFactory.createRobot(ftcRobotControllerService.eventLoopManager));
    }
    
    public void run() {
      boolean bool = FtcRobotControllerService.this.robotSetupHasBeenStarted;
      FtcRobotControllerService.access$1202(FtcRobotControllerService.this, true);
      ThreadPool.logThreadLifeCycle("RobotSetupRunnable.run()", new Runnable() {
            public void run() {
              // Byte code:
              //   0: ldc 'FTCService'
              //   2: ldc 'Processing robot setup'
              //   4: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
              //   7: aload_0
              //   8: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   11: invokevirtual shutdownRobot : ()V
              //   14: aload_0
              //   15: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   18: invokevirtual awaitUSB : ()V
              //   21: aload_0
              //   22: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   25: invokevirtual initializeEventLoopAndRobot : ()V
              //   28: aload_0
              //   29: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   32: invokevirtual waitForNetwork : ()V
              //   35: aload_0
              //   36: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   39: invokevirtual startRobot : ()V
              //   42: aload_0
              //   43: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   46: getfield runOnComplete : Ljava/lang/Runnable;
              //   49: ifnull -> 141
              //   52: goto -> 82
              //   55: astore_1
              //   56: goto -> 169
              //   59: aload_0
              //   60: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   63: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
              //   66: getstatic com/qualcomm/robotcore/robot/RobotStatus.ABORT_DUE_TO_INTERRUPT : Lcom/qualcomm/robotcore/robot/RobotStatus;
              //   69: invokestatic access$200 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;Lcom/qualcomm/robotcore/robot/RobotStatus;)V
              //   72: aload_0
              //   73: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   76: getfield runOnComplete : Ljava/lang/Runnable;
              //   79: ifnull -> 141
              //   82: aload_0
              //   83: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   86: getfield runOnComplete : Ljava/lang/Runnable;
              //   89: invokeinterface run : ()V
              //   94: goto -> 141
              //   97: astore_1
              //   98: aload_0
              //   99: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   102: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
              //   105: getstatic com/qualcomm/robotcore/robot/RobotStatus.UNABLE_TO_START_ROBOT : Lcom/qualcomm/robotcore/robot/RobotStatus;
              //   108: invokestatic access$200 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;Lcom/qualcomm/robotcore/robot/RobotStatus;)V
              //   111: aload_1
              //   112: aload_0
              //   113: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   116: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
              //   119: getstatic com/qualcomm/ftccommon/R$string.globalErrorFailedToCreateRobot : I
              //   122: invokevirtual getString : (I)Ljava/lang/String;
              //   125: invokestatic setGlobalErrorMsg : (Lcom/qualcomm/robotcore/exception/RobotCoreException;Ljava/lang/String;)V
              //   128: aload_0
              //   129: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   132: getfield runOnComplete : Ljava/lang/Runnable;
              //   135: ifnull -> 141
              //   138: goto -> 82
              //   141: aload_0
              //   142: getfield val$isFirstRun : Z
              //   145: ifeq -> 168
              //   148: ldc 'FTCService'
              //   150: ldc 'Detecting WiFi reset'
              //   152: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;)V
              //   155: aload_0
              //   156: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   159: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
              //   162: invokestatic access$800 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;)Lcom/qualcomm/robotcore/wifi/NetworkConnection;
              //   165: invokevirtual detectWifiReset : ()V
              //   168: return
              //   169: aload_0
              //   170: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   173: getfield runOnComplete : Ljava/lang/Runnable;
              //   176: ifnull -> 191
              //   179: aload_0
              //   180: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
              //   183: getfield runOnComplete : Ljava/lang/Runnable;
              //   186: invokeinterface run : ()V
              //   191: aload_1
              //   192: athrow
              //   193: astore_1
              //   194: goto -> 59
              // Exception table:
              //   from	to	target	type
              //   7	42	97	com/qualcomm/robotcore/exception/RobotCoreException
              //   7	42	193	java/lang/InterruptedException
              //   7	42	55	finally
              //   59	72	55	finally
              //   98	128	55	finally
            }
          });
    }
    
    void shutdownRobot() {
      if (FtcRobotControllerService.this.robot != null) {
        FtcRobotControllerService.this.robot.shutdown();
        FtcRobotControllerService.access$302(FtcRobotControllerService.this, (Robot)null);
      } 
    }
    
    void startRobot() throws RobotCoreException {
      FtcRobotControllerService.this.updateRobotStatus(RobotStatus.STARTING_ROBOT);
      FtcRobotControllerService.this.robot.eventLoopManager.setMonitor(FtcRobotControllerService.this.eventLoopMonitor);
      FtcRobotControllerService.this.robot.start(FtcRobotControllerService.this.eventLoop);
    }
    
    void waitForNetwork() throws InterruptedException {
      if (FtcRobotControllerService.this.networkConnection.getNetworkType() == NetworkType.WIFIDIRECT) {
        waitForWifi();
        waitForWifiDirect();
        FtcRobotControllerService.this.networkConnection.createConnection();
      } 
      waitForNetworkConnection();
      FtcRobotControllerService.this.webServer.start();
    }
    
    boolean waitForNetworkConnection() throws InterruptedException {
      RobotLog.vv("FTCService", "Waiting for a connection to a wifi service");
      FtcRobotControllerService.this.updateRobotStatus(RobotStatus.WAITING_ON_NETWORK_CONNECTION);
      boolean bool = false;
      while (true) {
        if (FtcRobotControllerService.this.networkConnection.isConnected())
          return bool; 
        bool = true;
        FtcRobotControllerService.this.networkConnection.onWaitForConnection();
        Thread.sleep(1000L);
      } 
    }
    
    boolean waitForWifi() throws InterruptedException {
      FtcRobotControllerService.this.updateRobotStatus(RobotStatus.WAITING_ON_WIFI);
      boolean bool = false;
      while (true) {
        synchronized (FtcRobotControllerService.this.wifiDirectCallbackLock) {
          if (FtcRobotControllerService.this.wifiDirectAgent.isWifiEnabled())
            return bool; 
          bool = true;
          FtcRobotControllerService.this.waitForNextWifiDirectCallback();
        } 
      } 
    }
    
    boolean waitForWifiDirect() throws InterruptedException {
      FtcRobotControllerService.this.updateRobotStatus(RobotStatus.WAITING_ON_WIFI_DIRECT);
      boolean bool = false;
      while (true) {
        synchronized (FtcRobotControllerService.this.wifiDirectCallbackLock) {
          if (FtcRobotControllerService.this.wifiDirectAgent.isWifiDirectEnabled())
            return bool; 
          bool = true;
          FtcRobotControllerService.this.waitForNextWifiDirectCallback();
        } 
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      // Byte code:
      //   0: ldc 'FTCService'
      //   2: ldc 'Processing robot setup'
      //   4: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
      //   7: aload_0
      //   8: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   11: invokevirtual shutdownRobot : ()V
      //   14: aload_0
      //   15: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   18: invokevirtual awaitUSB : ()V
      //   21: aload_0
      //   22: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   25: invokevirtual initializeEventLoopAndRobot : ()V
      //   28: aload_0
      //   29: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   32: invokevirtual waitForNetwork : ()V
      //   35: aload_0
      //   36: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   39: invokevirtual startRobot : ()V
      //   42: aload_0
      //   43: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   46: getfield runOnComplete : Ljava/lang/Runnable;
      //   49: ifnull -> 141
      //   52: goto -> 82
      //   55: astore_1
      //   56: goto -> 169
      //   59: aload_0
      //   60: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   63: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
      //   66: getstatic com/qualcomm/robotcore/robot/RobotStatus.ABORT_DUE_TO_INTERRUPT : Lcom/qualcomm/robotcore/robot/RobotStatus;
      //   69: invokestatic access$200 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;Lcom/qualcomm/robotcore/robot/RobotStatus;)V
      //   72: aload_0
      //   73: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   76: getfield runOnComplete : Ljava/lang/Runnable;
      //   79: ifnull -> 141
      //   82: aload_0
      //   83: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   86: getfield runOnComplete : Ljava/lang/Runnable;
      //   89: invokeinterface run : ()V
      //   94: goto -> 141
      //   97: astore_1
      //   98: aload_0
      //   99: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   102: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
      //   105: getstatic com/qualcomm/robotcore/robot/RobotStatus.UNABLE_TO_START_ROBOT : Lcom/qualcomm/robotcore/robot/RobotStatus;
      //   108: invokestatic access$200 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;Lcom/qualcomm/robotcore/robot/RobotStatus;)V
      //   111: aload_1
      //   112: aload_0
      //   113: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   116: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
      //   119: getstatic com/qualcomm/ftccommon/R$string.globalErrorFailedToCreateRobot : I
      //   122: invokevirtual getString : (I)Ljava/lang/String;
      //   125: invokestatic setGlobalErrorMsg : (Lcom/qualcomm/robotcore/exception/RobotCoreException;Ljava/lang/String;)V
      //   128: aload_0
      //   129: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   132: getfield runOnComplete : Ljava/lang/Runnable;
      //   135: ifnull -> 141
      //   138: goto -> 82
      //   141: aload_0
      //   142: getfield val$isFirstRun : Z
      //   145: ifeq -> 168
      //   148: ldc 'FTCService'
      //   150: ldc 'Detecting WiFi reset'
      //   152: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;)V
      //   155: aload_0
      //   156: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   159: getfield this$0 : Lcom/qualcomm/ftccommon/FtcRobotControllerService;
      //   162: invokestatic access$800 : (Lcom/qualcomm/ftccommon/FtcRobotControllerService;)Lcom/qualcomm/robotcore/wifi/NetworkConnection;
      //   165: invokevirtual detectWifiReset : ()V
      //   168: return
      //   169: aload_0
      //   170: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   173: getfield runOnComplete : Ljava/lang/Runnable;
      //   176: ifnull -> 191
      //   179: aload_0
      //   180: getfield this$1 : Lcom/qualcomm/ftccommon/FtcRobotControllerService$RobotSetupRunnable;
      //   183: getfield runOnComplete : Ljava/lang/Runnable;
      //   186: invokeinterface run : ()V
      //   191: aload_1
      //   192: athrow
      //   193: astore_1
      //   194: goto -> 59
      // Exception table:
      //   from	to	target	type
      //   7	42	97	com/qualcomm/robotcore/exception/RobotCoreException
      //   7	42	193	java/lang/InterruptedException
      //   7	42	55	finally
      //   59	72	55	finally
      //   98	128	55	finally
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\FtcRobotControllerService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */