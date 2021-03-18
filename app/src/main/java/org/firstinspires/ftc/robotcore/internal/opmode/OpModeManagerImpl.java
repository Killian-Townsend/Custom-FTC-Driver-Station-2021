package org.firstinspires.ftc.robotcore.internal.opmode;

import android.app.Activity;
import android.content.Context;
import android.os.Debug;
import com.qualcomm.robotcore.eventloop.EventLoopManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;

public class OpModeManagerImpl implements OpModeServices, OpModeManagerNotifier {
  public static final OpMode DEFAULT_OP_MODE = new DefaultOpMode();
  
  public static final String DEFAULT_OP_MODE_NAME = "$Stop$Robot$";
  
  public static final String TAG = "OpModeManager";
  
  protected static final WeakHashMap<Activity, OpModeManagerImpl> mapActivityToOpModeManager;
  
  protected static int matchNumber = 0;
  
  protected OpMode activeOpMode = DEFAULT_OP_MODE;
  
  protected String activeOpModeName = "$Stop$Robot$";
  
  protected boolean callToInitNeeded = false;
  
  protected boolean callToStartNeeded = false;
  
  protected Context context;
  
  protected EventLoopManager eventLoopManager = null;
  
  protected boolean gamepadResetNeeded = false;
  
  protected HardwareMap hardwareMap = null;
  
  protected final WeakReferenceSet<OpModeManagerNotifier.Notifications> listeners = new WeakReferenceSet();
  
  protected AtomicReference<OpModeStateTransition> nextOpModeState = new AtomicReference<OpModeStateTransition>(null);
  
  protected OpModeState opModeState = OpModeState.INIT;
  
  protected boolean opModeSwapNeeded = false;
  
  protected String queuedOpModeName = "$Stop$Robot$";
  
  protected boolean skipCallToStop = false;
  
  protected OpModeStuckCodeMonitor stuckMonitor = null;
  
  protected boolean telemetryClearNeeded = false;
  
  static {
    mapActivityToOpModeManager = new WeakHashMap<Activity, OpModeManagerImpl>();
  }
  
  public OpModeManagerImpl(Activity paramActivity, HardwareMap paramHardwareMap) {
    this.hardwareMap = paramHardwareMap;
    initActiveOpMode("$Stop$Robot$");
    this.context = (Context)paramActivity;
    synchronized (mapActivityToOpModeManager) {
      mapActivityToOpModeManager.put(paramActivity, this);
      return;
    } 
  }
  
  private void failedToSwapOpMode() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to start op mode ");
    stringBuilder.append(this.activeOpModeName);
    RobotLog.ee("OpModeManager", stringBuilder.toString());
    setActiveOpMode(DEFAULT_OP_MODE, "$Stop$Robot$");
  }
  
  private void failedToSwapOpMode(Exception paramException) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Unable to start op mode ");
    stringBuilder.append(this.activeOpModeName);
    RobotLog.ee("OpModeManager", paramException, stringBuilder.toString());
    setActiveOpMode(DEFAULT_OP_MODE, "$Stop$Robot$");
  }
  
  public static OpModeManagerImpl getOpModeManagerOfActivity(Activity paramActivity) {
    synchronized (mapActivityToOpModeManager) {
      return mapActivityToOpModeManager.get(paramActivity);
    } 
  }
  
  private void performOpModeSwap() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Attempting to switch to op mode ");
    stringBuilder.append(this.queuedOpModeName);
    RobotLog.i(stringBuilder.toString());
    OpMode opMode = RegisteredOpModes.getInstance().getOpMode(this.queuedOpModeName);
    if (opMode != null) {
      setActiveOpMode(opMode, this.queuedOpModeName);
      return;
    } 
    failedToSwapOpMode();
  }
  
  public static void updateTelemetryNow(OpMode paramOpMode, TelemetryMessage paramTelemetryMessage) {
    paramOpMode.internalUpdateTelemetryNow(paramTelemetryMessage);
  }
  
  protected void callActiveOpModeInit() {
    WeakReferenceSet<OpModeManagerNotifier.Notifications> weakReferenceSet;
    synchronized (this.listeners) {
      Iterator<OpModeManagerNotifier.Notifications> iterator = this.listeners.iterator();
      while (iterator.hasNext())
        ((OpModeManagerNotifier.Notifications)iterator.next()).onOpModePreInit(this.activeOpMode); 
      for (HardwareDevice hardwareDevice : this.hardwareMap) {
        if (hardwareDevice instanceof OpModeManagerNotifier.Notifications)
          ((OpModeManagerNotifier.Notifications)hardwareDevice).onOpModePreInit(this.activeOpMode); 
      } 
      this.activeOpMode.internalPreInit();
      try {
        detectStuck(this.activeOpMode.msStuckDetectInit, "init()", new Runnable() {
              public void run() {
                OpModeManagerImpl.this.activeOpMode.init();
              }
            },  true);
        return;
      } catch (ForceStopException forceStopException) {
        initActiveOpMode("$Stop$Robot$");
        this.skipCallToStop = true;
      } 
      return;
    } 
  }
  
  protected void callActiveOpModeInitLoop() {
    try {
      detectStuck(this.activeOpMode.msStuckDetectInitLoop, "init_loop()", new Runnable() {
            public void run() {
              OpModeManagerImpl.this.activeOpMode.init_loop();
            }
          });
    } catch (ForceStopException forceStopException) {
      initActiveOpMode("$Stop$Robot$");
      this.skipCallToStop = true;
    } 
    this.activeOpMode.internalPostInitLoop();
  }
  
  protected void callActiveOpModeLoop() {
    try {
      detectStuck(this.activeOpMode.msStuckDetectLoop, "loop()", new Runnable() {
            public void run() {
              OpModeManagerImpl.this.activeOpMode.loop();
            }
          });
    } catch (ForceStopException forceStopException) {
      initActiveOpMode("$Stop$Robot$");
      this.skipCallToStop = true;
    } 
    this.activeOpMode.internalPostLoop();
  }
  
  protected void callActiveOpModeStart() {
    WeakReferenceSet<OpModeManagerNotifier.Notifications> weakReferenceSet;
    synchronized (this.listeners) {
      Iterator<OpModeManagerNotifier.Notifications> iterator = this.listeners.iterator();
      while (iterator.hasNext())
        ((OpModeManagerNotifier.Notifications)iterator.next()).onOpModePreStart(this.activeOpMode); 
      for (HardwareDevice hardwareDevice : this.hardwareMap) {
        if (hardwareDevice instanceof OpModeManagerNotifier.Notifications)
          ((OpModeManagerNotifier.Notifications)hardwareDevice).onOpModePreStart(this.activeOpMode); 
      } 
      try {
        detectStuck(this.activeOpMode.msStuckDetectStart, "start()", new Runnable() {
              public void run() {
                OpModeManagerImpl.this.activeOpMode.start();
              }
            });
        return;
      } catch (ForceStopException forceStopException) {
        initActiveOpMode("$Stop$Robot$");
        this.skipCallToStop = true;
      } 
      return;
    } 
  }
  
  protected void callActiveOpModeStop() {
    try {
      detectStuck(this.activeOpMode.msStuckDetectStop, "stop()", new Runnable() {
            public void run() {
              OpModeManagerImpl.this.activeOpMode.stop();
            }
          });
    } catch (ForceStopException forceStopException) {}
    synchronized (this.listeners) {
      Iterator<OpModeManagerNotifier.Notifications> iterator = this.listeners.iterator();
      while (iterator.hasNext())
        ((OpModeManagerNotifier.Notifications)iterator.next()).onOpModePostStop(this.activeOpMode); 
      for (HardwareDevice hardwareDevice : this.hardwareMap) {
        if (hardwareDevice instanceof OpModeManagerNotifier.Notifications)
          ((OpModeManagerNotifier.Notifications)hardwareDevice).onOpModePostStop(this.activeOpMode); 
      } 
      return;
    } 
  }
  
  protected void detectStuck(int paramInt, String paramString, Runnable paramRunnable) {
    detectStuck(paramInt, paramString, paramRunnable, false);
  }
  
  protected void detectStuck(int paramInt, String paramString, Runnable paramRunnable, boolean paramBoolean) {
    this.stuckMonitor.startMonitoring(paramInt, paramString, paramBoolean);
    try {
      paramRunnable.run();
    } finally {
      this.stuckMonitor.stopMonitoring();
      try {
        this.stuckMonitor.acquired.await();
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  protected void doMatchLoggingWork(String paramString) {
    if (!paramString.equals("$Stop$Robot$"))
      try {
        RobotLog.startMatchLogging(this.context, paramString, matchNumber);
        return;
      } catch (RobotCoreException robotCoreException) {
        RobotLog.ee("OpModeManager", "Could not start match logging");
        robotCoreException.printStackTrace();
        return;
      }  
    RobotLog.stopMatchLogging();
  }
  
  public OpMode getActiveOpMode() {
    return this.activeOpMode;
  }
  
  public String getActiveOpModeName() {
    return this.activeOpModeName;
  }
  
  public HardwareMap getHardwareMap() {
    return this.hardwareMap;
  }
  
  public void init(EventLoopManager paramEventLoopManager) {
    this.stuckMonitor = new OpModeStuckCodeMonitor();
    this.eventLoopManager = paramEventLoopManager;
  }
  
  public void initActiveOpMode(String paramString) {
    OpModeStateTransition opModeStateTransition = new OpModeStateTransition();
    opModeStateTransition.queuedOpModeName = paramString;
    Boolean bool = Boolean.valueOf(true);
    opModeStateTransition.opModeSwapNeeded = bool;
    opModeStateTransition.callToInitNeeded = bool;
    opModeStateTransition.gamepadResetNeeded = bool;
    opModeStateTransition.telemetryClearNeeded = Boolean.valueOf(true ^ paramString.equals("$Stop$Robot$"));
    opModeStateTransition.callToStartNeeded = Boolean.valueOf(false);
    doMatchLoggingWork(paramString);
    this.nextOpModeState.set(opModeStateTransition);
  }
  
  public void refreshUserTelemetry(TelemetryMessage paramTelemetryMessage, double paramDouble) {
    this.eventLoopManager.getEventLoop().refreshUserTelemetry(paramTelemetryMessage, paramDouble);
  }
  
  public OpMode registerListener(OpModeManagerNotifier.Notifications paramNotifications) {
    synchronized (this.listeners) {
      this.listeners.add(paramNotifications);
      return this.activeOpMode;
    } 
  }
  
  public void requestOpModeStop(OpMode paramOpMode) {
    this.eventLoopManager.getEventLoop().requestOpModeStop(paramOpMode);
  }
  
  protected void resetHardwareForOpMode() {
    Iterator<HardwareDevice> iterator = this.hardwareMap.iterator();
    while (iterator.hasNext())
      ((HardwareDevice)iterator.next()).resetDeviceConfigurationForOpMode(); 
  }
  
  public void runActiveOpMode(Gamepad[] paramArrayOfGamepad) {
    OpModeStateTransition opModeStateTransition = this.nextOpModeState.getAndSet(null);
    if (opModeStateTransition != null)
      opModeStateTransition.apply(); 
    OpMode opMode = this.activeOpMode;
    opMode.time = opMode.getRuntime();
    this.activeOpMode.gamepad1 = paramArrayOfGamepad[0];
    this.activeOpMode.gamepad2 = paramArrayOfGamepad[1];
    if (this.gamepadResetNeeded) {
      this.activeOpMode.gamepad1.reset();
      this.activeOpMode.gamepad2.reset();
      this.gamepadResetNeeded = false;
    } 
    if (this.telemetryClearNeeded && this.eventLoopManager != null) {
      TelemetryMessage telemetryMessage = new TelemetryMessage();
      telemetryMessage.addData("\000", "");
      this.eventLoopManager.sendTelemetryData(telemetryMessage);
      this.telemetryClearNeeded = false;
      RobotLog.clearGlobalErrorMsg();
      RobotLog.clearGlobalWarningMsg();
    } 
    if (this.opModeSwapNeeded) {
      if (!this.skipCallToStop || this.activeOpMode instanceof com.qualcomm.robotcore.eventloop.opmode.LinearOpMode)
        callActiveOpModeStop(); 
      this.skipCallToStop = false;
      performOpModeSwap();
      this.opModeSwapNeeded = false;
    } 
    if (this.callToInitNeeded) {
      this.activeOpMode.gamepad1 = paramArrayOfGamepad[0];
      this.activeOpMode.gamepad2 = paramArrayOfGamepad[1];
      this.activeOpMode.hardwareMap = this.hardwareMap;
      this.activeOpMode.internalOpModeServices = this;
      if (!this.activeOpModeName.equals("$Stop$Robot$"))
        resetHardwareForOpMode(); 
      this.activeOpMode.resetStartTime();
      callActiveOpModeInit();
      this.opModeState = OpModeState.INIT;
      this.callToInitNeeded = false;
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_NOTIFY_INIT_OP_MODE", this.activeOpModeName));
      return;
    } 
    if (this.callToStartNeeded) {
      callActiveOpModeStart();
      this.opModeState = OpModeState.LOOPING;
      this.callToStartNeeded = false;
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_NOTIFY_RUN_OP_MODE", this.activeOpModeName));
      return;
    } 
    if (this.opModeState == OpModeState.INIT) {
      callActiveOpModeInitLoop();
      return;
    } 
    if (this.opModeState == OpModeState.LOOPING)
      callActiveOpModeLoop(); 
  }
  
  protected void setActiveOpMode(OpMode paramOpMode, String paramString) {
    synchronized (this.listeners) {
      this.activeOpMode = paramOpMode;
      this.activeOpModeName = paramString;
      return;
    } 
  }
  
  public void setHardwareMap(HardwareMap paramHardwareMap) {
    this.hardwareMap = paramHardwareMap;
  }
  
  public void setMatchNumber(int paramInt) {
    matchNumber = paramInt;
  }
  
  public void startActiveOpMode() {
    for (OpModeStateTransition opModeStateTransition = null;; opModeStateTransition = this.nextOpModeState.get()) {
      OpModeStateTransition opModeStateTransition1;
      if (opModeStateTransition != null) {
        opModeStateTransition1 = opModeStateTransition.copy();
      } else {
        opModeStateTransition1 = new OpModeStateTransition();
      } 
      opModeStateTransition1.callToStartNeeded = Boolean.valueOf(true);
      if (this.nextOpModeState.compareAndSet(opModeStateTransition, opModeStateTransition1))
        return; 
      Thread.yield();
    } 
  }
  
  public void stopActiveOpMode() {
    callActiveOpModeStop();
    RobotLog.stopMatchLogging();
    initActiveOpMode("$Stop$Robot$");
  }
  
  public void teardown() {
    this.stuckMonitor.shutdown();
  }
  
  public void unregisterListener(OpModeManagerNotifier.Notifications paramNotifications) {
    synchronized (this.listeners) {
      this.listeners.remove(paramNotifications);
      return;
    } 
  }
  
  public static class DefaultOpMode extends OpMode {
    private static final long SAFE_WAIT_NANOS = 100000000L;
    
    private ElapsedTime blinkerTimer = new ElapsedTime();
    
    private boolean firstTimeRun = true;
    
    private long nanoNextSafe;
    
    public DefaultOpMode() {
      this.firstTimeRun = true;
    }
    
    private boolean isLynxDevice(HardwareDevice param1HardwareDevice) {
      return (param1HardwareDevice.getManufacturer() == HardwareDevice.Manufacturer.Lynx);
    }
    
    private boolean isLynxDevice(Object param1Object) {
      return isLynxDevice((HardwareDevice)param1Object);
    }
    
    private void startSafe() {
      for (DcMotorSimple dcMotorSimple : this.hardwareMap.getAll(DcMotorSimple.class)) {
        if (dcMotorSimple.getPower() != 0.0D)
          dcMotorSimple.setPower(0.0D); 
      } 
      if (this.firstTimeRun) {
        this.firstTimeRun = false;
        this.nanoNextSafe = System.nanoTime();
        this.blinkerTimer.reset();
        return;
      } 
      this.nanoNextSafe = System.nanoTime() + 100000000L;
    }
    
    private void staySafe() {
      if (System.nanoTime() > this.nanoNextSafe) {
        null = this.hardwareMap.getAll(RobotCoreLynxUsbDevice.class).iterator();
        while (null.hasNext())
          ((RobotCoreLynxUsbDevice)null.next()).failSafe(); 
        for (ServoController servoController : this.hardwareMap.getAll(ServoController.class)) {
          if (!isLynxDevice((HardwareDevice)servoController))
            servoController.pwmDisable(); 
        } 
        for (DcMotor dcMotor : this.hardwareMap.getAll(DcMotor.class)) {
          if (!isLynxDevice((HardwareDevice)dcMotor)) {
            dcMotor.setPower(0.0D);
            dcMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
          } 
        } 
        Iterator<LightSensor> iterator = this.hardwareMap.getAll(LightSensor.class).iterator();
        while (iterator.hasNext())
          ((LightSensor)iterator.next()).enableLed(false); 
        this.nanoNextSafe = System.nanoTime() + 100000000L;
      } 
    }
    
    public void init() {
      startSafe();
      this.telemetry.addData("Status", "Robot is stopping");
    }
    
    public void init_loop() {
      staySafe();
      this.telemetry.addData("Status", "Robot is stopped");
    }
    
    public void loop() {
      staySafe();
      this.telemetry.addData("Status", "Robot is stopped");
    }
    
    public void stop() {}
  }
  
  public static class ForceStopException extends RuntimeException {}
  
  protected enum OpModeState {
    INIT, LOOPING;
    
    static {
      OpModeState opModeState = new OpModeState("LOOPING", 1);
      LOOPING = opModeState;
      $VALUES = new OpModeState[] { INIT, opModeState };
    }
  }
  
  class OpModeStateTransition {
    Boolean callToInitNeeded = null;
    
    Boolean callToStartNeeded = null;
    
    Boolean gamepadResetNeeded = null;
    
    Boolean opModeSwapNeeded = null;
    
    String queuedOpModeName = null;
    
    Boolean telemetryClearNeeded = null;
    
    void apply() {
      String str = this.queuedOpModeName;
      if (str != null)
        OpModeManagerImpl.this.queuedOpModeName = str; 
      Boolean bool = this.opModeSwapNeeded;
      if (bool != null)
        OpModeManagerImpl.this.opModeSwapNeeded = bool.booleanValue(); 
      bool = this.callToInitNeeded;
      if (bool != null)
        OpModeManagerImpl.this.callToInitNeeded = bool.booleanValue(); 
      bool = this.gamepadResetNeeded;
      if (bool != null)
        OpModeManagerImpl.this.gamepadResetNeeded = bool.booleanValue(); 
      bool = this.telemetryClearNeeded;
      if (bool != null)
        OpModeManagerImpl.this.telemetryClearNeeded = bool.booleanValue(); 
      bool = this.callToStartNeeded;
      if (bool != null)
        OpModeManagerImpl.this.callToStartNeeded = bool.booleanValue(); 
    }
    
    OpModeStateTransition copy() {
      OpModeStateTransition opModeStateTransition = new OpModeStateTransition();
      opModeStateTransition.queuedOpModeName = this.queuedOpModeName;
      opModeStateTransition.opModeSwapNeeded = this.opModeSwapNeeded;
      opModeStateTransition.callToInitNeeded = this.callToInitNeeded;
      opModeStateTransition.gamepadResetNeeded = this.gamepadResetNeeded;
      opModeStateTransition.telemetryClearNeeded = this.telemetryClearNeeded;
      opModeStateTransition.callToStartNeeded = this.callToStartNeeded;
      return opModeStateTransition;
    }
  }
  
  protected class OpModeStuckCodeMonitor {
    CountDownLatch acquired = null;
    
    boolean debuggerDetected = false;
    
    ExecutorService executorService = ThreadPool.newSingleThreadExecutor("OpModeStuckCodeMonitor");
    
    String method;
    
    int msTimeout;
    
    Semaphore stopped = new Semaphore(0);
    
    protected boolean checkForDebugger() {
      if (this.debuggerDetected || Debug.isDebuggerConnected()) {
        boolean bool1 = true;
        this.debuggerDetected = bool1;
        return bool1;
      } 
      boolean bool = false;
      this.debuggerDetected = bool;
      return bool;
    }
    
    public void shutdown() {
      this.executorService.shutdownNow();
    }
    
    public void startMonitoring(int param1Int, String param1String, boolean param1Boolean) {
      CountDownLatch countDownLatch = this.acquired;
      if (countDownLatch != null)
        try {
          countDownLatch.await();
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
        }  
      this.msTimeout = param1Int;
      this.method = param1String;
      this.stopped.drainPermits();
      this.acquired = new CountDownLatch(1);
      this.executorService.execute(new Runner());
      if (param1Boolean)
        this.debuggerDetected = false; 
    }
    
    public void stopMonitoring() {
      this.stopped.release();
    }
    
    protected class Runner implements Runnable {
      static final String msgForceStoppedCommon = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. ";
      
      static final String msgForceStoppedPopupIterative = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was an iterative OpMode; make sure you aren't using your own loops.";
      
      static final String msgForceStoppedPopupLinear = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was a linear OpMode; make sure you are calling opModeIsActive() in any loops.";
      
      public void run() {
        // Byte code:
        //   0: iconst_0
        //   1: istore_1
        //   2: aload_0
        //   3: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   6: invokevirtual checkForDebugger : ()Z
        //   9: istore_2
        //   10: iload_2
        //   11: ifeq -> 25
        //   14: aload_0
        //   15: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   18: getfield acquired : Ljava/util/concurrent/CountDownLatch;
        //   21: invokevirtual countDown : ()V
        //   24: return
        //   25: aload_0
        //   26: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   29: getfield stopped : Ljava/util/concurrent/Semaphore;
        //   32: aload_0
        //   33: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   36: getfield msTimeout : I
        //   39: i2l
        //   40: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
        //   43: invokevirtual tryAcquire : (JLjava/util/concurrent/TimeUnit;)Z
        //   46: ifne -> 528
        //   49: aload_0
        //   50: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   53: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   56: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   59: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   61: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
        //   64: invokeinterface iterator : ()Ljava/util/Iterator;
        //   69: astore_3
        //   70: aload_3
        //   71: invokeinterface hasNext : ()Z
        //   76: ifeq -> 97
        //   79: aload_3
        //   80: invokeinterface next : ()Ljava/lang/Object;
        //   85: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   88: iconst_1
        //   89: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
        //   94: goto -> 70
        //   97: aload_0
        //   98: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   101: getfield stopped : Ljava/util/concurrent/Semaphore;
        //   104: ldc2_w 100
        //   107: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
        //   110: invokevirtual tryAcquire : (JLjava/util/concurrent/TimeUnit;)Z
        //   113: ifeq -> 216
        //   116: aload_0
        //   117: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   120: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   123: getfield activeOpMode : Lcom/qualcomm/robotcore/eventloop/opmode/OpMode;
        //   126: instanceof com/qualcomm/robotcore/eventloop/opmode/LinearOpMode
        //   129: ifeq -> 551
        //   132: ldc 'User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was a linear OpMode; make sure you are calling opModeIsActive() in any loops.'
        //   134: astore_3
        //   135: goto -> 138
        //   138: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
        //   141: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
        //   144: ldc 'OpMode Force-Stopped'
        //   146: aload_3
        //   147: iconst_1
        //   148: anewarray java/lang/Object
        //   151: dup
        //   152: iconst_0
        //   153: aload_0
        //   154: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   157: getfield method : Ljava/lang/String;
        //   160: aastore
        //   161: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   164: invokevirtual showAlertDialog : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil$DialogContext;
        //   167: pop
        //   168: aload_0
        //   169: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   172: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   175: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   178: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   180: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
        //   183: invokeinterface iterator : ()Ljava/util/Iterator;
        //   188: astore_3
        //   189: aload_3
        //   190: invokeinterface hasNext : ()Z
        //   195: ifeq -> 14
        //   198: aload_3
        //   199: invokeinterface next : ()Ljava/lang/Object;
        //   204: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   207: iconst_0
        //   208: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
        //   213: goto -> 189
        //   216: aload_0
        //   217: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   220: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   223: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
        //   226: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   228: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
        //   231: invokeinterface iterator : ()Ljava/util/Iterator;
        //   236: astore_3
        //   237: aload_3
        //   238: invokeinterface hasNext : ()Z
        //   243: ifeq -> 264
        //   246: aload_3
        //   247: invokeinterface next : ()Ljava/lang/Object;
        //   252: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
        //   255: iconst_0
        //   256: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
        //   261: goto -> 237
        //   264: aload_0
        //   265: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   268: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   271: getfield context : Landroid/content/Context;
        //   274: getstatic com/qualcomm/robotcore/R$string.errorOpModeStuck : I
        //   277: invokevirtual getString : (I)Ljava/lang/String;
        //   280: iconst_2
        //   281: anewarray java/lang/Object
        //   284: dup
        //   285: iconst_0
        //   286: aload_0
        //   287: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   290: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   293: getfield activeOpModeName : Ljava/lang/String;
        //   296: aastore
        //   297: dup
        //   298: iconst_1
        //   299: aload_0
        //   300: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   303: getfield method : Ljava/lang/String;
        //   306: aastore
        //   307: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   310: astore_3
        //   311: aload_3
        //   312: invokestatic setGlobalErrorMsg : (Ljava/lang/String;)Z
        //   315: istore_2
        //   316: aload_3
        //   317: invokestatic e : (Ljava/lang/String;)V
        //   320: new java/util/concurrent/CountDownLatch
        //   323: dup
        //   324: iconst_1
        //   325: invokespecial <init> : (I)V
        //   328: astore_3
        //   329: new java/lang/Thread
        //   332: dup
        //   333: new org/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor$Runner$1
        //   336: dup
        //   337: aload_0
        //   338: aload_3
        //   339: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor$Runner;Ljava/util/concurrent/CountDownLatch;)V
        //   342: invokespecial <init> : (Ljava/lang/Runnable;)V
        //   345: invokevirtual start : ()V
        //   348: aload_3
        //   349: ldc2_w 250
        //   352: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
        //   355: invokevirtual await : (JLjava/util/concurrent/TimeUnit;)Z
        //   358: ifeq -> 369
        //   361: ldc 'Successfully sent failsafe commands to Lynx modules before app restart'
        //   363: invokestatic e : (Ljava/lang/String;)V
        //   366: goto -> 374
        //   369: ldc 'Timed out while sending failsafe commands to Lynx modules before app restart'
        //   371: invokestatic e : (Ljava/lang/String;)V
        //   374: ldc 'Begin thread dump'
        //   376: invokestatic e : (Ljava/lang/String;)V
        //   379: invokestatic getAllStackTraces : ()Ljava/util/Map;
        //   382: invokeinterface entrySet : ()Ljava/util/Set;
        //   387: invokeinterface iterator : ()Ljava/util/Iterator;
        //   392: astore_3
        //   393: aload_3
        //   394: invokeinterface hasNext : ()Z
        //   399: ifeq -> 439
        //   402: aload_3
        //   403: invokeinterface next : ()Ljava/lang/Object;
        //   408: checkcast java/util/Map$Entry
        //   411: astore #4
        //   413: aload #4
        //   415: invokeinterface getKey : ()Ljava/lang/Object;
        //   420: checkcast java/lang/Thread
        //   423: aload #4
        //   425: invokeinterface getValue : ()Ljava/lang/Object;
        //   430: checkcast [Ljava/lang/StackTraceElement;
        //   433: invokestatic logStackTrace : (Ljava/lang/Thread;[Ljava/lang/StackTraceElement;)V
        //   436: goto -> 393
        //   439: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
        //   442: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
        //   445: aload_0
        //   446: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   449: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
        //   452: getfield context : Landroid/content/Context;
        //   455: getstatic com/qualcomm/robotcore/R$string.toastOpModeStuck : I
        //   458: invokevirtual getString : (I)Ljava/lang/String;
        //   461: iconst_1
        //   462: anewarray java/lang/Object
        //   465: dup
        //   466: iconst_0
        //   467: aload_0
        //   468: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   471: getfield method : Ljava/lang/String;
        //   474: aastore
        //   475: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   478: invokevirtual showToast : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;)V
        //   481: ldc2_w 1000
        //   484: invokestatic sleep : (J)V
        //   487: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
        //   490: iconst_m1
        //   491: invokevirtual restartApp : (I)V
        //   494: goto -> 528
        //   497: iload_2
        //   498: istore_1
        //   499: goto -> 506
        //   502: astore_3
        //   503: goto -> 516
        //   506: iload_1
        //   507: ifeq -> 528
        //   510: invokestatic clearGlobalErrorMsg : ()V
        //   513: goto -> 528
        //   516: aload_0
        //   517: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   520: getfield acquired : Ljava/util/concurrent/CountDownLatch;
        //   523: invokevirtual countDown : ()V
        //   526: aload_3
        //   527: athrow
        //   528: aload_0
        //   529: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
        //   532: getfield acquired : Ljava/util/concurrent/CountDownLatch;
        //   535: invokevirtual countDown : ()V
        //   538: return
        //   539: astore_3
        //   540: goto -> 506
        //   543: astore_3
        //   544: goto -> 497
        //   547: astore_3
        //   548: goto -> 374
        //   551: ldc 'User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was an iterative OpMode; make sure you aren't using your own loops.'
        //   553: astore_3
        //   554: goto -> 138
        // Exception table:
        //   from	to	target	type
        //   2	10	539	java/lang/InterruptedException
        //   2	10	502	finally
        //   25	70	539	java/lang/InterruptedException
        //   25	70	502	finally
        //   70	94	539	java/lang/InterruptedException
        //   70	94	502	finally
        //   97	132	539	java/lang/InterruptedException
        //   97	132	502	finally
        //   138	189	539	java/lang/InterruptedException
        //   138	189	502	finally
        //   189	213	539	java/lang/InterruptedException
        //   189	213	502	finally
        //   216	237	539	java/lang/InterruptedException
        //   216	237	502	finally
        //   237	261	539	java/lang/InterruptedException
        //   237	261	502	finally
        //   264	316	539	java/lang/InterruptedException
        //   264	316	502	finally
        //   316	320	543	java/lang/InterruptedException
        //   316	320	502	finally
        //   320	366	547	java/lang/Exception
        //   320	366	543	java/lang/InterruptedException
        //   320	366	502	finally
        //   369	374	547	java/lang/Exception
        //   369	374	543	java/lang/InterruptedException
        //   369	374	502	finally
        //   374	393	543	java/lang/InterruptedException
        //   374	393	502	finally
        //   393	436	543	java/lang/InterruptedException
        //   393	436	502	finally
        //   439	494	543	java/lang/InterruptedException
        //   439	494	502	finally
        //   510	513	502	finally
      }
    }
    
    class null implements Runnable {
      public void run() {
        for (RobotCoreLynxUsbDevice robotCoreLynxUsbDevice : OpModeManagerImpl.this.hardwareMap.getAll(RobotCoreLynxUsbDevice.class)) {
          robotCoreLynxUsbDevice.lockNetworkLockAcquisitions();
          robotCoreLynxUsbDevice.failSafe();
        } 
        lastDitchEffortFailsafeDone.countDown();
      }
    }
  }
  
  protected class Runner implements Runnable {
    static final String msgForceStoppedCommon = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. ";
    
    static final String msgForceStoppedPopupIterative = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was an iterative OpMode; make sure you aren't using your own loops.";
    
    static final String msgForceStoppedPopupLinear = "User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was a linear OpMode; make sure you are calling opModeIsActive() in any loops.";
    
    public void run() {
      // Byte code:
      //   0: iconst_0
      //   1: istore_1
      //   2: aload_0
      //   3: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   6: invokevirtual checkForDebugger : ()Z
      //   9: istore_2
      //   10: iload_2
      //   11: ifeq -> 25
      //   14: aload_0
      //   15: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   18: getfield acquired : Ljava/util/concurrent/CountDownLatch;
      //   21: invokevirtual countDown : ()V
      //   24: return
      //   25: aload_0
      //   26: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   29: getfield stopped : Ljava/util/concurrent/Semaphore;
      //   32: aload_0
      //   33: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   36: getfield msTimeout : I
      //   39: i2l
      //   40: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
      //   43: invokevirtual tryAcquire : (JLjava/util/concurrent/TimeUnit;)Z
      //   46: ifne -> 528
      //   49: aload_0
      //   50: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   53: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   56: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
      //   59: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   61: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
      //   64: invokeinterface iterator : ()Ljava/util/Iterator;
      //   69: astore_3
      //   70: aload_3
      //   71: invokeinterface hasNext : ()Z
      //   76: ifeq -> 97
      //   79: aload_3
      //   80: invokeinterface next : ()Ljava/lang/Object;
      //   85: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   88: iconst_1
      //   89: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
      //   94: goto -> 70
      //   97: aload_0
      //   98: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   101: getfield stopped : Ljava/util/concurrent/Semaphore;
      //   104: ldc2_w 100
      //   107: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
      //   110: invokevirtual tryAcquire : (JLjava/util/concurrent/TimeUnit;)Z
      //   113: ifeq -> 216
      //   116: aload_0
      //   117: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   120: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   123: getfield activeOpMode : Lcom/qualcomm/robotcore/eventloop/opmode/OpMode;
      //   126: instanceof com/qualcomm/robotcore/eventloop/opmode/LinearOpMode
      //   129: ifeq -> 551
      //   132: ldc 'User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was a linear OpMode; make sure you are calling opModeIsActive() in any loops.'
      //   134: astore_3
      //   135: goto -> 138
      //   138: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
      //   141: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
      //   144: ldc 'OpMode Force-Stopped'
      //   146: aload_3
      //   147: iconst_1
      //   148: anewarray java/lang/Object
      //   151: dup
      //   152: iconst_0
      //   153: aload_0
      //   154: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   157: getfield method : Ljava/lang/String;
      //   160: aastore
      //   161: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   164: invokevirtual showAlertDialog : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;Ljava/lang/String;)Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil$DialogContext;
      //   167: pop
      //   168: aload_0
      //   169: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   172: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   175: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
      //   178: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   180: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
      //   183: invokeinterface iterator : ()Ljava/util/Iterator;
      //   188: astore_3
      //   189: aload_3
      //   190: invokeinterface hasNext : ()Z
      //   195: ifeq -> 14
      //   198: aload_3
      //   199: invokeinterface next : ()Ljava/lang/Object;
      //   204: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   207: iconst_0
      //   208: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
      //   213: goto -> 189
      //   216: aload_0
      //   217: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   220: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   223: getfield hardwareMap : Lcom/qualcomm/robotcore/hardware/HardwareMap;
      //   226: ldc com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   228: invokevirtual getAll : (Ljava/lang/Class;)Ljava/util/List;
      //   231: invokeinterface iterator : ()Ljava/util/Iterator;
      //   236: astore_3
      //   237: aload_3
      //   238: invokeinterface hasNext : ()Z
      //   243: ifeq -> 264
      //   246: aload_3
      //   247: invokeinterface next : ()Ljava/lang/Object;
      //   252: checkcast com/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice
      //   255: iconst_0
      //   256: invokeinterface setThrowOnNetworkLockAcquisition : (Z)V
      //   261: goto -> 237
      //   264: aload_0
      //   265: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   268: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   271: getfield context : Landroid/content/Context;
      //   274: getstatic com/qualcomm/robotcore/R$string.errorOpModeStuck : I
      //   277: invokevirtual getString : (I)Ljava/lang/String;
      //   280: iconst_2
      //   281: anewarray java/lang/Object
      //   284: dup
      //   285: iconst_0
      //   286: aload_0
      //   287: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   290: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   293: getfield activeOpModeName : Ljava/lang/String;
      //   296: aastore
      //   297: dup
      //   298: iconst_1
      //   299: aload_0
      //   300: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   303: getfield method : Ljava/lang/String;
      //   306: aastore
      //   307: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   310: astore_3
      //   311: aload_3
      //   312: invokestatic setGlobalErrorMsg : (Ljava/lang/String;)Z
      //   315: istore_2
      //   316: aload_3
      //   317: invokestatic e : (Ljava/lang/String;)V
      //   320: new java/util/concurrent/CountDownLatch
      //   323: dup
      //   324: iconst_1
      //   325: invokespecial <init> : (I)V
      //   328: astore_3
      //   329: new java/lang/Thread
      //   332: dup
      //   333: new org/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor$Runner$1
      //   336: dup
      //   337: aload_0
      //   338: aload_3
      //   339: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor$Runner;Ljava/util/concurrent/CountDownLatch;)V
      //   342: invokespecial <init> : (Ljava/lang/Runnable;)V
      //   345: invokevirtual start : ()V
      //   348: aload_3
      //   349: ldc2_w 250
      //   352: getstatic java/util/concurrent/TimeUnit.MILLISECONDS : Ljava/util/concurrent/TimeUnit;
      //   355: invokevirtual await : (JLjava/util/concurrent/TimeUnit;)Z
      //   358: ifeq -> 369
      //   361: ldc 'Successfully sent failsafe commands to Lynx modules before app restart'
      //   363: invokestatic e : (Ljava/lang/String;)V
      //   366: goto -> 374
      //   369: ldc 'Timed out while sending failsafe commands to Lynx modules before app restart'
      //   371: invokestatic e : (Ljava/lang/String;)V
      //   374: ldc 'Begin thread dump'
      //   376: invokestatic e : (Ljava/lang/String;)V
      //   379: invokestatic getAllStackTraces : ()Ljava/util/Map;
      //   382: invokeinterface entrySet : ()Ljava/util/Set;
      //   387: invokeinterface iterator : ()Ljava/util/Iterator;
      //   392: astore_3
      //   393: aload_3
      //   394: invokeinterface hasNext : ()Z
      //   399: ifeq -> 439
      //   402: aload_3
      //   403: invokeinterface next : ()Ljava/lang/Object;
      //   408: checkcast java/util/Map$Entry
      //   411: astore #4
      //   413: aload #4
      //   415: invokeinterface getKey : ()Ljava/lang/Object;
      //   420: checkcast java/lang/Thread
      //   423: aload #4
      //   425: invokeinterface getValue : ()Ljava/lang/Object;
      //   430: checkcast [Ljava/lang/StackTraceElement;
      //   433: invokestatic logStackTrace : (Ljava/lang/Thread;[Ljava/lang/StackTraceElement;)V
      //   436: goto -> 393
      //   439: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
      //   442: getstatic org/firstinspires/ftc/robotcore/internal/ui/UILocation.BOTH : Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;
      //   445: aload_0
      //   446: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   449: getfield this$0 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl;
      //   452: getfield context : Landroid/content/Context;
      //   455: getstatic com/qualcomm/robotcore/R$string.toastOpModeStuck : I
      //   458: invokevirtual getString : (I)Ljava/lang/String;
      //   461: iconst_1
      //   462: anewarray java/lang/Object
      //   465: dup
      //   466: iconst_0
      //   467: aload_0
      //   468: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   471: getfield method : Ljava/lang/String;
      //   474: aastore
      //   475: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   478: invokevirtual showToast : (Lorg/firstinspires/ftc/robotcore/internal/ui/UILocation;Ljava/lang/String;)V
      //   481: ldc2_w 1000
      //   484: invokestatic sleep : (J)V
      //   487: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/system/AppUtil;
      //   490: iconst_m1
      //   491: invokevirtual restartApp : (I)V
      //   494: goto -> 528
      //   497: iload_2
      //   498: istore_1
      //   499: goto -> 506
      //   502: astore_3
      //   503: goto -> 516
      //   506: iload_1
      //   507: ifeq -> 528
      //   510: invokestatic clearGlobalErrorMsg : ()V
      //   513: goto -> 528
      //   516: aload_0
      //   517: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   520: getfield acquired : Ljava/util/concurrent/CountDownLatch;
      //   523: invokevirtual countDown : ()V
      //   526: aload_3
      //   527: athrow
      //   528: aload_0
      //   529: getfield this$1 : Lorg/firstinspires/ftc/robotcore/internal/opmode/OpModeManagerImpl$OpModeStuckCodeMonitor;
      //   532: getfield acquired : Ljava/util/concurrent/CountDownLatch;
      //   535: invokevirtual countDown : ()V
      //   538: return
      //   539: astore_3
      //   540: goto -> 506
      //   543: astore_3
      //   544: goto -> 497
      //   547: astore_3
      //   548: goto -> 374
      //   551: ldc 'User OpMode was stuck in %s, but was able to be force stopped without restarting the app. It appears this was an iterative OpMode; make sure you aren't using your own loops.'
      //   553: astore_3
      //   554: goto -> 138
      // Exception table:
      //   from	to	target	type
      //   2	10	539	java/lang/InterruptedException
      //   2	10	502	finally
      //   25	70	539	java/lang/InterruptedException
      //   25	70	502	finally
      //   70	94	539	java/lang/InterruptedException
      //   70	94	502	finally
      //   97	132	539	java/lang/InterruptedException
      //   97	132	502	finally
      //   138	189	539	java/lang/InterruptedException
      //   138	189	502	finally
      //   189	213	539	java/lang/InterruptedException
      //   189	213	502	finally
      //   216	237	539	java/lang/InterruptedException
      //   216	237	502	finally
      //   237	261	539	java/lang/InterruptedException
      //   237	261	502	finally
      //   264	316	539	java/lang/InterruptedException
      //   264	316	502	finally
      //   316	320	543	java/lang/InterruptedException
      //   316	320	502	finally
      //   320	366	547	java/lang/Exception
      //   320	366	543	java/lang/InterruptedException
      //   320	366	502	finally
      //   369	374	547	java/lang/Exception
      //   369	374	543	java/lang/InterruptedException
      //   369	374	502	finally
      //   374	393	543	java/lang/InterruptedException
      //   374	393	502	finally
      //   393	436	543	java/lang/InterruptedException
      //   393	436	502	finally
      //   439	494	543	java/lang/InterruptedException
      //   439	494	502	finally
      //   510	513	502	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      for (RobotCoreLynxUsbDevice robotCoreLynxUsbDevice : OpModeManagerImpl.this.hardwareMap.getAll(RobotCoreLynxUsbDevice.class)) {
        robotCoreLynxUsbDevice.lockNetworkLockAcquisitions();
        robotCoreLynxUsbDevice.failSafe();
      } 
      lastDitchEffortFailsafeDone.countDown();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\opmode\OpModeManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */