package com.qualcomm.robotcore.eventloop;

import android.content.Context;
import com.qualcomm.robotcore.eventloop.opmode.EventLoopManagerClient;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.WebServer;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkType;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatusCallback;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class EventLoopManager implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback, PeerStatusCallback, SyncdDevice.Manager {
  private static final boolean DEBUG = false;
  
  private static final int HEARTBEAT_WAIT_DELAY = 500;
  
  private static final int MAX_COMMAND_CACHE = 8;
  
  public static final String RC_BATTERY_STATUS_KEY = "$RobotController$Battery$Status$";
  
  public static final String ROBOT_BATTERY_LEVEL_KEY = "$Robot$Battery$Level$";
  
  private static final double SECONDS_UNTIL_FORCED_SHUTDOWN = 2.0D;
  
  public static final String SYSTEM_ERROR_KEY = "$System$Error$";
  
  public static final String SYSTEM_NONE_KEY = "$System$None$";
  
  public static final String SYSTEM_WARNING_KEY = "$System$Warning$";
  
  public static final String TAG = "EventLoopManager";
  
  private AppUtil appUtil = AppUtil.getInstance();
  
  private EventLoopMonitor callback = null;
  
  private final Command[] commandRecvCache = new Command[8];
  
  private int commandRecvCachePosition = 0;
  
  private final Context context;
  
  private EventLoop eventLoop = null;
  
  private final Object eventLoopLock = new Object();
  
  private final EventLoopManagerClient eventLoopManagerClient;
  
  private ExecutorService executorEventLoop = ThreadPool.newSingleThreadExecutor("executorEventLoop");
  
  private final Gamepad[] gamepads = new Gamepad[] { new Gamepad(), new Gamepad() };
  
  private Heartbeat heartbeat = new Heartbeat();
  
  private final EventLoop idleEventLoop;
  
  private ElapsedTime lastHeartbeatReceived = new ElapsedTime();
  
  private String lastSystemTelemetryKey = null;
  
  private String lastSystemTelemetryMessage = null;
  
  private long lastSystemTelemetryNanoTime = 0L;
  
  private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  private boolean receivedTimeFromCurrentPeer = false;
  
  private final Object refreshSystemTelemetryLock = new Object();
  
  private InetAddress remoteAddr;
  
  public RobotState state = RobotState.NOT_STARTED;
  
  private final Set<SyncdDevice> syncdDevices = new CopyOnWriteArraySet<SyncdDevice>();
  
  public EventLoopManager(Context paramContext, EventLoopManagerClient paramEventLoopManagerClient, EventLoop paramEventLoop) {
    this.context = paramContext;
    this.eventLoopManagerClient = paramEventLoopManagerClient;
    this.idleEventLoop = paramEventLoop;
    this.eventLoop = paramEventLoop;
    changeState(RobotState.NOT_STARTED);
    NetworkConnectionHandler.getInstance().registerPeerStatusCallback(this);
  }
  
  private void changeState(RobotState paramRobotState) {
    this.state = paramRobotState;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("EventLoopManager state is ");
    stringBuilder.append(paramRobotState.toString());
    RobotLog.v(stringBuilder.toString());
    EventLoopMonitor eventLoopMonitor = this.callback;
    if (eventLoopMonitor != null)
      eventLoopMonitor.onStateChange(paramRobotState); 
    this.networkConnectionHandler.sendCommand(new Command("CMD_NOTIFY_ROBOT_STATE", Integer.toString(paramRobotState.asByte())));
  }
  
  private void startEventLoop() throws RobotCoreException {
    try {
      changeState(RobotState.INIT);
      synchronized (this.eventLoopLock) {
        this.eventLoop.init(this);
        this.lastHeartbeatReceived = new ElapsedTime(0L);
        changeState(RobotState.RUNNING);
        null = ThreadPool.newSingleThreadExecutor("executorEventLoop");
        this.executorEventLoop = (ExecutorService)null;
        null.execute(new Runnable() {
              public void run() {
                boolean bool;
                (new EventLoopManager.EventLoopRunnable()).run();
                EventLoopManager eventLoopManager = EventLoopManager.this;
                EventLoopManager.access$302(eventLoopManager, eventLoopManager.idleEventLoop);
                if (!Thread.currentThread().isInterrupted()) {
                  RobotLog.vv("EventLoopManager", "switching to idleEventLoop");
                  try {
                    synchronized (EventLoopManager.this.eventLoopLock) {
                      EventLoopManager.this.eventLoop.init(EventLoopManager.this);
                    } 
                  } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                  } catch (RobotCoreException robotCoreException) {
                    RobotLog.ee("EventLoopManager", (Throwable)robotCoreException, "internal error");
                    boolean bool1 = false;
                    if (bool1)
                      (new EventLoopManager.EventLoopRunnable()).run(); 
                    return;
                  } 
                  bool = true;
                } else {
                  return;
                } 
                if (bool)
                  (new EventLoopManager.EventLoopRunnable()).run(); 
              }
            });
        return;
      } 
    } catch (Exception exception) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Caught exception during looper init: ");
      stringBuilder.append(exception.toString());
      RobotLog.ww("EventLoopManager", exception, stringBuilder.toString());
      changeState(RobotState.EMERGENCY_STOP);
      refreshSystemTelemetry();
      stringBuilder = new StringBuilder();
      stringBuilder.append("Robot failed to start: ");
      stringBuilder.append(exception.getMessage());
      throw new RobotCoreException(stringBuilder.toString());
    } 
  }
  
  private void stopEventLoop() {
    if (this.eventLoop.getOpModeManager() != null)
      this.eventLoop.getOpModeManager().stopActiveOpMode(); 
    this.executorEventLoop.shutdownNow();
    ThreadPool.awaitTerminationOrExitApplication(this.executorEventLoop, 10L, TimeUnit.SECONDS, "EventLoop", "possible infinite loop in user code?");
    changeState(RobotState.STOPPED);
    this.eventLoop = this.idleEventLoop;
    this.syncdDevices.clear();
  }
  
  public void buildAndSendTelemetry(String paramString1, String paramString2) {
    TelemetryMessage telemetryMessage = new TelemetryMessage();
    telemetryMessage.setTag(paramString1);
    telemetryMessage.addData(paramString1, paramString2);
    sendTelemetryData(telemetryMessage);
  }
  
  public void close() {
    RobotLog.vv("Robocol", "EventLoopManager.close()");
    this.networkConnectionHandler.shutdown();
    this.networkConnectionHandler.removeNetworkConnectionCallback(this);
    this.networkConnectionHandler.removeReceiveLoopCallback(this);
  }
  
  public CallbackResult commandEvent(Command paramCommand) throws RobotCoreException {
    CallbackResult callbackResult1 = CallbackResult.NOT_HANDLED;
    for (Command command : this.commandRecvCache) {
      if (command != null && command.equals(paramCommand))
        return CallbackResult.HANDLED; 
    } 
    Command[] arrayOfCommand = this.commandRecvCache;
    int i = this.commandRecvCachePosition;
    this.commandRecvCachePosition = i + 1;
    arrayOfCommand[i % arrayOfCommand.length] = paramCommand;
    CallbackResult callbackResult2 = callbackResult1;
    try {
      Object object = this.eventLoopLock;
      synchronized (callbackResult1) {
        CallbackResult callbackResult = this.eventLoop.processCommand(paramCommand);
        callbackResult1 = callbackResult;
        /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
        return callbackResult;
      } 
    } catch (Exception exception) {
      RobotLog.ee("EventLoopManager", exception, "Event loop threw an exception while processing a command");
      return callbackResult2;
    } 
  }
  
  public CallbackResult emptyEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult gamepadEvent(RobocolDatagram paramRobocolDatagram) throws RobotCoreException {
    Gamepad gamepad = new Gamepad();
    gamepad.fromByteArray(paramRobocolDatagram.getData());
    if (gamepad.getUser() == null) {
      RobotLog.ee("EventLoopManager", "gamepad with user %d received; only users 1 and 2 are valid", new Object[] { Byte.valueOf((gamepad.getUser()).id) });
      return CallbackResult.HANDLED;
    } 
    byte b = (gamepad.getUser()).id;
    this.gamepads[b - 1].copy(gamepad);
    if (gamepad.getGamepadId() == -2) {
      RobotLog.vv("EventLoopManager", "synthetic gamepad received: id=%d user=%s atRest=%s ", new Object[] { Integer.valueOf(gamepad.getGamepadId()), gamepad.getUser(), Boolean.valueOf(gamepad.atRest()) });
      gamepad.setGamepadId(-1);
    } 
    return CallbackResult.HANDLED;
  }
  
  public EventLoop getEventLoop() {
    return this.eventLoop;
  }
  
  public Gamepad getGamepad(int paramInt) {
    Range.throwIfRangeIsInvalid(paramInt, 0, 1);
    return this.gamepads[paramInt];
  }
  
  public Gamepad[] getGamepads() {
    return this.gamepads;
  }
  
  public Heartbeat getHeartbeat() {
    return this.heartbeat;
  }
  
  public EventLoopMonitor getMonitor() {
    return this.callback;
  }
  
  public WebServer getWebServer() {
    return this.eventLoopManagerClient.getWebServer();
  }
  
  public CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong) throws RobotCoreException {
    Heartbeat heartbeat = new Heartbeat();
    heartbeat.fromByteArray(paramRobocolDatagram.getData());
    heartbeat.setRobotState(this.state);
    if (!this.receivedTimeFromCurrentPeer) {
      long l = heartbeat.t0;
      if (this.appUtil.isSaneWalkClockTime(l)) {
        this.receivedTimeFromCurrentPeer = true;
        RobotLog.vv("EventLoopManager", "Setting authoritative wall clock based on connected DS.");
        this.appUtil.setWallClockTime(l);
        this.appUtil.setTimeZone(heartbeat.getTimeZoneId());
      } 
    } 
    heartbeat.t1 = paramLong;
    heartbeat.t2 = this.appUtil.getWallClockTime();
    this.networkConnectionHandler.sendDataToPeer((RobocolParsable)heartbeat);
    this.lastHeartbeatReceived.reset();
    this.heartbeat = heartbeat;
    return CallbackResult.HANDLED;
  }
  
  public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("onNetworkConnectionEvent: ");
    stringBuilder.append(paramNetworkEvent.toString());
    RobotLog.ii("EventLoopManager", stringBuilder.toString());
    int i = null.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkConnection$NetworkEvent[paramNetworkEvent.ordinal()];
    if (i != 1) {
      if (i != 2)
        return callbackResult; 
      RobotLog.ii("Robocol", "Received network connection event");
      return this.networkConnectionHandler.handleConnectionInfoAvailable();
    } 
    return this.networkConnectionHandler.handlePeersAvailable();
  }
  
  public void onPeerConnected() {
    EventLoopMonitor eventLoopMonitor = this.callback;
    if (eventLoopMonitor != null)
      eventLoopMonitor.onPeerConnected(); 
  }
  
  public void onPeerDisconnected() {
    EventLoopMonitor eventLoopMonitor = this.callback;
    if (eventLoopMonitor != null)
      eventLoopMonitor.onPeerDisconnected(); 
    OpModeManagerImpl opModeManagerImpl = this.eventLoop.getOpModeManager();
    if (opModeManagerImpl != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Lost connection while running op mode: ");
      stringBuilder.append(opModeManagerImpl.getActiveOpModeName());
      String str = stringBuilder.toString();
      opModeManagerImpl.initActiveOpMode("$Stop$Robot$");
      RobotLog.i(str);
    } else {
      RobotLog.i("Lost connection while main event loop not active");
    } 
    this.remoteAddr = null;
    this.lastHeartbeatReceived = new ElapsedTime(0L);
    this.receivedTimeFromCurrentPeer = false;
  }
  
  public CallbackResult packetReceived(RobocolDatagram paramRobocolDatagram) {
    refreshSystemTelemetry();
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult peerDiscoveryEvent(RobocolDatagram paramRobocolDatagram) throws RobotCoreException {
    if (!this.networkConnectionHandler.isPeerConnected() || paramRobocolDatagram.getAddress().equals(this.networkConnectionHandler.getCurrentPeerAddr())) {
      PeerDiscovery.PeerType peerType1 = PeerDiscovery.PeerType.PEER;
      try {
        this.networkConnectionHandler.updateConnection(paramRobocolDatagram);
      } catch (RobotProtocolException robotProtocolException) {
        RobotLog.setGlobalErrorMsg(robotProtocolException.getMessage());
        RobotLog.ee("EventLoopManager", robotProtocolException.getMessage());
      } 
      paramRobocolDatagram = new RobocolDatagram((RobocolParsable)new PeerDiscovery(peerType1), paramRobocolDatagram.getAddress());
      this.networkConnectionHandler.sendDatagram(paramRobocolDatagram);
      return CallbackResult.HANDLED;
    } 
    PeerDiscovery.PeerType peerType = PeerDiscovery.PeerType.NOT_CONNECTED_DUE_TO_PREEXISTING_CONNECTION;
    paramRobocolDatagram = new RobocolDatagram((RobocolParsable)new PeerDiscovery(peerType), paramRobocolDatagram.getAddress());
    this.networkConnectionHandler.sendDatagram(paramRobocolDatagram);
    return CallbackResult.HANDLED;
  }
  
  public void refreshSystemTelemetry() {
    synchronized (this.refreshSystemTelemetryLock) {
      boolean bool1;
      boolean bool2;
      String str2;
      long l = System.nanoTime();
      String str1 = RobotLog.getGlobalErrorMsg();
      String str3 = RobotLog.getGlobalWarningMessage();
      if (!str1.isEmpty()) {
        str2 = "$System$Error$";
      } else if (!str3.isEmpty()) {
        str2 = "$System$Warning$";
        str1 = str3;
      } else {
        str1 = "";
        str2 = "$System$None$";
      } 
      if (!str1.equals(this.lastSystemTelemetryMessage) || !str2.equals(this.lastSystemTelemetryKey)) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1 || l - this.lastSystemTelemetryNanoTime > 5000000000L) {
        bool2 = true;
      } else {
        bool2 = false;
      } 
      if (bool1)
        RobotLog.d("system telemetry: key=%s msg=\"%s\"", new Object[] { str2, str1 }); 
      if (bool2) {
        this.lastSystemTelemetryMessage = str1;
        this.lastSystemTelemetryKey = str2;
        this.lastSystemTelemetryNanoTime = l;
        buildAndSendTelemetry(str2, str1);
        if (this.callback != null)
          this.callback.onTelemetryTransmitted(); 
      } 
      return;
    } 
  }
  
  public void refreshSystemTelemetryNow() {
    this.lastSystemTelemetryNanoTime = 0L;
    refreshSystemTelemetry();
  }
  
  public void registerSyncdDevice(SyncdDevice paramSyncdDevice) {
    this.syncdDevices.add(paramSyncdDevice);
  }
  
  public CallbackResult reportGlobalError(String paramString, boolean paramBoolean) {
    RobotLog.setGlobalErrorMsg(paramString);
    return CallbackResult.HANDLED;
  }
  
  public void sendTelemetryData(TelemetryMessage paramTelemetryMessage) {
    try {
      paramTelemetryMessage.setRobotState(this.state);
      this.networkConnectionHandler.sendDataToPeer((RobocolParsable)paramTelemetryMessage);
    } catch (RobotCoreException robotCoreException) {
      RobotLog.ww("EventLoopManager", (Throwable)robotCoreException, "Failed to send telemetry data");
    } 
    paramTelemetryMessage.clearData();
  }
  
  public void setEventLoop(EventLoop paramEventLoop) throws RobotCoreException {
    stopEventLoop();
    synchronized (this.eventLoopLock) {
      this.eventLoop = paramEventLoop;
      RobotLog.vv("Robocol", "eventLoop=%s", new Object[] { paramEventLoop.getClass().getSimpleName() });
      startEventLoop();
      return;
    } 
  }
  
  public void setMonitor(EventLoopMonitor paramEventLoopMonitor) {
    this.callback = paramEventLoopMonitor;
    if (NetworkConnectionHandler.getInstance().isPeerConnected()) {
      this.callback.onPeerConnected();
      return;
    } 
    this.callback.onPeerDisconnected();
  }
  
  public void shutdown() {
    RobotLog.vv("Robocol", "EventLoopManager.shutdown()");
    stopEventLoop();
  }
  
  public void start(EventLoop paramEventLoop) throws RobotCoreException {
    RobotLog.vv("Robocol", "EventLoopManager.start()");
    this.networkConnectionHandler.pushNetworkConnectionCallback(this);
    this.networkConnectionHandler.pushReceiveLoopCallback(this);
    NetworkType networkType = NetworkConnectionHandler.getDefaultNetworkType(this.context);
    this.networkConnectionHandler.init(networkType, this.context);
    if (this.networkConnectionHandler.isNetworkConnected()) {
      RobotLog.vv("Robocol", "Spoofing a Network Connection event...");
      onNetworkConnectionEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE);
    } else {
      RobotLog.vv("Robocol", "Network not yet available, deferring network connection event...");
    } 
    setEventLoop(paramEventLoop);
  }
  
  public CallbackResult telemetryEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public void unregisterSyncdDevice(SyncdDevice paramSyncdDevice) {
    this.syncdDevices.remove(paramSyncdDevice);
  }
  
  public static interface EventLoopMonitor {
    void onPeerConnected();
    
    void onPeerDisconnected();
    
    void onStateChange(RobotState param1RobotState);
    
    void onTelemetryTransmitted();
  }
  
  private class EventLoopRunnable implements Runnable {
    private EventLoopRunnable() {}
    
    public void run() {
      ThreadPool.logThreadLifeCycle("opmode loop()", new Runnable() {
            public void run() {
              try {
                ElapsedTime elapsedTime = new ElapsedTime();
                while (!Thread.currentThread().isInterrupted()) {
                  while (elapsedTime.time() < 0.001D)
                    Thread.sleep(5L); 
                  elapsedTime.reset();
                  EventLoopManager.this.refreshSystemTelemetry();
                  if (EventLoopManager.this.lastHeartbeatReceived.startTime() == 0.0D)
                    Thread.sleep(500L); 
                  for (SyncdDevice syncdDevice : EventLoopManager.this.syncdDevices) {
                    SyncdDevice.ShutdownReason shutdownReason = syncdDevice.getShutdownReason();
                    if (shutdownReason != SyncdDevice.ShutdownReason.NORMAL) {
                      RobotLog.v("event loop: device has shutdown abnormally: %s", new Object[] { shutdownReason });
                      RobotUsbModule robotUsbModule = syncdDevice.getOwner();
                      if (robotUsbModule != null) {
                        RobotLog.vv("EventLoopManager", "event loop: detaching device %s", new Object[] { robotUsbModule.getSerialNumber() });
                        synchronized (EventLoopManager.this.eventLoopLock) {
                          EventLoopManager.this.eventLoop.handleUsbModuleDetach(robotUsbModule);
                          if (shutdownReason == SyncdDevice.ShutdownReason.ABNORMAL_ATTEMPT_REOPEN) {
                            RobotLog.vv("EventLoopManager", "event loop: auto-reattaching device %s", new Object[] { robotUsbModule.getSerialNumber() });
                            EventLoopManager.this.eventLoop.pendUsbDeviceAttachment(robotUsbModule.getSerialNumber(), 250L, TimeUnit.MILLISECONDS);
                          } 
                        } 
                      } 
                    } 
                  } 
                  synchronized (EventLoopManager.this.eventLoopLock) {
                    EventLoopManager.this.eventLoop.processedRecentlyAttachedUsbDevices();
                    try {
                      synchronized (EventLoopManager.this.eventLoopLock) {
                        EventLoopManager.this.eventLoop.loop();
                      } 
                      continue;
                    } catch (Exception exception) {
                      RobotLog.ee("EventLoopManager", exception, "Event loop threw an exception");
                      null = new StringBuilder();
                      null.append(exception.getClass().getSimpleName());
                      if (exception.getMessage() != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(" - ");
                        stringBuilder.append(exception.getMessage());
                        str = stringBuilder.toString();
                        null.append(str);
                        str = null.toString();
                        null = new StringBuilder();
                        null.append("User code threw an uncaught exception: ");
                        null.append(str);
                        RobotLog.setGlobalErrorMsg(null.toString());
                        throw new RobotCoreException("EventLoop Exception in loop(): %s", new Object[] { str });
                      } 
                    } 
                    null.append(str);
                    String str = null.toString();
                    null = new StringBuilder();
                    null.append("User code threw an uncaught exception: ");
                    null.append(str);
                    RobotLog.setGlobalErrorMsg(null.toString());
                    throw new RobotCoreException("EventLoop Exception in loop(): %s", new Object[] { str });
                  } 
                } 
              } catch (InterruptedException interruptedException) {
                RobotLog.vv("EventLoopManager", interruptedException, "EventLoopRunnable interrupted");
                Thread.currentThread().interrupt();
                EventLoopManager.this.changeState(RobotState.STOPPED);
              } catch (CancellationException cancellationException) {
                RobotLog.vv("EventLoopManager", cancellationException, "EventLoopRunnable cancelled");
                EventLoopManager.this.changeState(RobotState.STOPPED);
              } catch (RobotCoreException robotCoreException) {
                RobotLog.vv("EventLoopManager", (Throwable)robotCoreException, "RobotCoreException in EventLoopManager");
                EventLoopManager.this.changeState(RobotState.EMERGENCY_STOP);
                EventLoopManager.this.refreshSystemTelemetry();
              } 
              try {
                synchronized (EventLoopManager.this.eventLoopLock) {
                  EventLoopManager.this.eventLoop.teardown();
                  return;
                } 
              } catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Caught exception during looper teardown: ");
                stringBuilder.append(exception.toString());
                RobotLog.ww("EventLoopManager", exception, stringBuilder.toString());
                EventLoopManager.this.refreshSystemTelemetry();
                return;
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      try {
        ElapsedTime elapsedTime = new ElapsedTime();
        while (!Thread.currentThread().isInterrupted()) {
          while (elapsedTime.time() < 0.001D)
            Thread.sleep(5L); 
          elapsedTime.reset();
          EventLoopManager.this.refreshSystemTelemetry();
          if (EventLoopManager.this.lastHeartbeatReceived.startTime() == 0.0D)
            Thread.sleep(500L); 
          for (SyncdDevice syncdDevice : EventLoopManager.this.syncdDevices) {
            SyncdDevice.ShutdownReason shutdownReason = syncdDevice.getShutdownReason();
            if (shutdownReason != SyncdDevice.ShutdownReason.NORMAL) {
              RobotLog.v("event loop: device has shutdown abnormally: %s", new Object[] { shutdownReason });
              RobotUsbModule robotUsbModule = syncdDevice.getOwner();
              if (robotUsbModule != null) {
                RobotLog.vv("EventLoopManager", "event loop: detaching device %s", new Object[] { robotUsbModule.getSerialNumber() });
                synchronized (EventLoopManager.this.eventLoopLock) {
                  EventLoopManager.this.eventLoop.handleUsbModuleDetach(robotUsbModule);
                  if (shutdownReason == SyncdDevice.ShutdownReason.ABNORMAL_ATTEMPT_REOPEN) {
                    RobotLog.vv("EventLoopManager", "event loop: auto-reattaching device %s", new Object[] { robotUsbModule.getSerialNumber() });
                    EventLoopManager.this.eventLoop.pendUsbDeviceAttachment(robotUsbModule.getSerialNumber(), 250L, TimeUnit.MILLISECONDS);
                  } 
                } 
              } 
            } 
          } 
          synchronized (EventLoopManager.this.eventLoopLock) {
            EventLoopManager.this.eventLoop.processedRecentlyAttachedUsbDevices();
            try {
              synchronized (EventLoopManager.this.eventLoopLock) {
                EventLoopManager.this.eventLoop.loop();
              } 
              continue;
            } catch (Exception exception) {
              RobotLog.ee("EventLoopManager", exception, "Event loop threw an exception");
              null = new StringBuilder();
              null.append(exception.getClass().getSimpleName());
              if (exception.getMessage() != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" - ");
                stringBuilder.append(exception.getMessage());
                str = stringBuilder.toString();
                null.append(str);
                str = null.toString();
                null = new StringBuilder();
                null.append("User code threw an uncaught exception: ");
                null.append(str);
                RobotLog.setGlobalErrorMsg(null.toString());
                throw new RobotCoreException("EventLoop Exception in loop(): %s", new Object[] { str });
              } 
            } 
            null.append(str);
            String str = null.toString();
            null = new StringBuilder();
            null.append("User code threw an uncaught exception: ");
            null.append(str);
            RobotLog.setGlobalErrorMsg(null.toString());
            throw new RobotCoreException("EventLoop Exception in loop(): %s", new Object[] { str });
          } 
        } 
      } catch (InterruptedException interruptedException) {
        RobotLog.vv("EventLoopManager", interruptedException, "EventLoopRunnable interrupted");
        Thread.currentThread().interrupt();
        EventLoopManager.this.changeState(RobotState.STOPPED);
      } catch (CancellationException cancellationException) {
        RobotLog.vv("EventLoopManager", cancellationException, "EventLoopRunnable cancelled");
        EventLoopManager.this.changeState(RobotState.STOPPED);
      } catch (RobotCoreException robotCoreException) {
        RobotLog.vv("EventLoopManager", (Throwable)robotCoreException, "RobotCoreException in EventLoopManager");
        EventLoopManager.this.changeState(RobotState.EMERGENCY_STOP);
        EventLoopManager.this.refreshSystemTelemetry();
      } 
      try {
        synchronized (EventLoopManager.this.eventLoopLock) {
          EventLoopManager.this.eventLoop.teardown();
          return;
        } 
      } catch (Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Caught exception during looper teardown: ");
        stringBuilder.append(exception.toString());
        RobotLog.ww("EventLoopManager", exception, stringBuilder.toString());
        EventLoopManager.this.refreshSystemTelemetry();
        return;
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\eventloop\EventLoopManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */