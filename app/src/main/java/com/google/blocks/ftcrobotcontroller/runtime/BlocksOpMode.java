package com.google.blocks.ftcrobotcontroller.runtime;

import android.app.Activity;
import android.content.Context;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareItemMap;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareType;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareUtil;
import com.google.blocks.ftcrobotcontroller.util.FileUtil;
import com.google.blocks.ftcrobotcontroller.util.Identifier;
import com.google.blocks.ftcrobotcontroller.util.ProjectsUtil;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase;
import org.firstinspires.ftc.robotcore.internal.opmode.InstanceOpModeManager;
import org.firstinspires.ftc.robotcore.internal.opmode.InstanceOpModeRegistrar;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public final class BlocksOpMode extends LinearOpMode {
  private static final String BLOCK_EXECUTION_ERROR = "Error: Error calling method on NPObject.";
  
  private static final String LOG_PREFIX = "BlocksOpMode - ";
  
  private static Activity activity;
  
  private static final AtomicReference<String> fatalErrorMessageHolder;
  
  private static final AtomicReference<RuntimeException> fatalExceptionHolder = new AtomicReference<RuntimeException>();
  
  static final Map<String, Access> javascriptInterfaces;
  
  private static final AtomicReference<String> nameOfOpModeLoadedIntoWebView;
  
  private static WebView webView;
  
  private volatile String currentBlockFirstName;
  
  private volatile String currentBlockLastName;
  
  private volatile BlockType currentBlockType;
  
  private volatile boolean forceStopped = false;
  
  private final AtomicLong interruptedTime = new AtomicLong();
  
  private volatile Thread javaBridgeThread;
  
  private final String logPrefix;
  
  private final String project;
  
  private CameraName switchableCamera;
  
  static {
    fatalErrorMessageHolder = new AtomicReference<String>();
    nameOfOpModeLoadedIntoWebView = new AtomicReference<String>();
    javascriptInterfaces = new ConcurrentHashMap<String, Access>();
  }
  
  BlocksOpMode(String paramString) {
    this.project = paramString;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("BlocksOpMode - \"");
    stringBuilder.append(paramString);
    stringBuilder.append("\" - ");
    this.logPrefix = stringBuilder.toString();
  }
  
  private void addJavascriptInterfaces(HardwareItemMap paramHardwareItemMap) {
    addJavascriptInterfacesForIdentifiers();
    addJavascriptInterfacesForHardware(paramHardwareItemMap);
    for (Map.Entry<String, Access> entry : javascriptInterfaces.entrySet()) {
      String str = (String)entry.getKey();
      Access access = (Access)entry.getValue();
      webView.addJavascriptInterface(access, str);
    } 
  }
  
  private void addJavascriptInterfacesForHardware(HardwareItemMap paramHardwareItemMap) {
    for (HardwareType hardwareType : HardwareType.values()) {
      if (paramHardwareItemMap.contains(hardwareType))
        for (HardwareItem hardwareItem : paramHardwareItemMap.getHardwareItems(hardwareType)) {
          if (javascriptInterfaces.containsKey(hardwareItem.identifier)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getLogPrefix());
            stringBuilder.append("There is already a JavascriptInterface for identifier \"");
            stringBuilder.append(hardwareItem.identifier);
            stringBuilder.append("\". Ignoring hardware type ");
            stringBuilder.append(hardwareType);
            stringBuilder.append(".");
            RobotLog.w(stringBuilder.toString());
            continue;
          } 
          HardwareAccess hardwareAccess = HardwareAccess.newHardwareAccess(this, hardwareType, this.hardwareMap, hardwareItem);
          if (hardwareAccess != null)
            javascriptInterfaces.put(hardwareItem.identifier, hardwareAccess); 
        }  
    } 
  }
  
  private static void addOpModeRegistrar() {
    RegisteredOpModes.getInstance().addInstanceOpModeRegistrar(new InstanceOpModeRegistrar() {
          public void register(InstanceOpModeManager param1InstanceOpModeManager) {
            try {
              for (OpModeMeta opModeMeta : ProjectsUtil.fetchEnabledProjectsWithJavaScript())
                param1InstanceOpModeManager.register(opModeMeta, (OpMode)new BlocksOpMode(opModeMeta.name)); 
            } catch (Exception exception) {
              RobotLog.logStackTrace(exception);
            } 
          }
        });
  }
  
  private void checkIfStopRequested() {
    if (this.interruptedTime.get() != 0L && isStopRequested()) {
      if (System.currentTimeMillis() - this.interruptedTime.get() < (this.msStuckDetectStop - 100))
        return; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getLogPrefix());
      stringBuilder.append("checkIfStopRequested - about to stop opmode by throwing RuntimeException");
      RobotLog.i(stringBuilder.toString());
      this.forceStopped = true;
      stringBuilder = new StringBuilder();
      stringBuilder.append("Stopping opmode ");
      stringBuilder.append(this.project);
      stringBuilder.append(" by force.");
      throw new RuntimeException(stringBuilder.toString());
    } 
  }
  
  private void cleanUpPreviousBlocksOpMode() {
    String str = nameOfOpModeLoadedIntoWebView.get();
    if (str != null) {
      StringBuilder stringBuilder1;
      StringBuilder stringBuilder2 = new StringBuilder();
      stringBuilder2.append(getLogPrefix());
      stringBuilder2.append("cleanUpPreviousBlocksOpMode - Warning: The Blocks runtime system is still loaded with the Blocks op mode named ");
      stringBuilder2.append(str);
      stringBuilder2.append(".");
      RobotLog.w(stringBuilder2.toString());
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(getLogPrefix());
      stringBuilder2.append("cleanUpPreviousBlocksOpMode - Trying to clean up now.");
      RobotLog.w(stringBuilder2.toString());
      AppUtil.getInstance().synchronousRunOnUiThread(new Runnable() {
            public void run() {
              try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(BlocksOpMode.this.getLogPrefix());
                stringBuilder.append("cleanUpPreviousBlocksOpMode run - before clearScript");
                RobotLog.w(stringBuilder.toString());
                BlocksOpMode.this.clearScript();
                stringBuilder = new StringBuilder();
                stringBuilder.append(BlocksOpMode.this.getLogPrefix());
                stringBuilder.append("cleanUpPreviousBlocksOpMode run - after clearScript");
                RobotLog.w(stringBuilder.toString());
                return;
              } catch (Exception exception) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(BlocksOpMode.this.getLogPrefix());
                stringBuilder.append("cleanUpPreviousBlocksOpMode run - caught ");
                stringBuilder.append(exception);
                RobotLog.e(stringBuilder.toString());
                if (exception.getStackTrace() != null)
                  RobotLog.logStackTrace(exception); 
                return;
              } 
            }
          });
      if (nameOfOpModeLoadedIntoWebView.get() != null) {
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append(getLogPrefix());
        stringBuilder1.append("cleanUpPreviousBlocksOpMode - Clean up was successful.");
        RobotLog.w(stringBuilder1.toString());
        return;
      } 
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append(getLogPrefix());
      stringBuilder2.append("cleanUpPreviousBlocksOpMode - Error: Clean up failed.");
      RobotLog.e(stringBuilder2.toString());
      stringBuilder2 = new StringBuilder();
      stringBuilder2.append("Unable to start running the Blocks op mode named ");
      stringBuilder2.append(this.project);
      stringBuilder2.append(". The Blocks runtime system is still loaded with the previous Blocks op mode named ");
      stringBuilder2.append((String)stringBuilder1);
      stringBuilder2.append(". Please restart the Robot Controller app.");
      throw new RuntimeException(stringBuilder2.toString());
    } 
  }
  
  private void clearScript() {
    removeJavascriptInterfaces();
    if (!javascriptInterfaces.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getLogPrefix());
      stringBuilder.append("clearScript - Warning: javascriptInterfaces is not empty.");
      RobotLog.w(stringBuilder.toString());
    } 
    javascriptInterfaces.clear();
    webView.loadDataWithBaseURL(null, "", "text/html", "UTF-8", null);
    nameOfOpModeLoadedIntoWebView.set(null);
  }
  
  private void clearSwitchableCamera() {
    this.switchableCamera = null;
  }
  
  private String getLogPrefix() {
    Thread thread = Thread.currentThread();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.logPrefix);
    stringBuilder.append(thread.getThreadGroup().getName());
    stringBuilder.append("/");
    stringBuilder.append(thread.getName());
    stringBuilder.append(" - ");
    return stringBuilder.toString();
  }
  
  private boolean isInterrupted() {
    return (this.interruptedTime.get() != 0L);
  }
  
  private void loadScript() throws IOException {
    nameOfOpModeLoadedIntoWebView.set(this.project);
    HardwareItemMap hardwareItemMap = HardwareItemMap.newHardwareItemMap(this.hardwareMap);
    addJavascriptInterfaces(hardwareItemMap);
    String str = HardwareUtil.upgradeJs(ProjectsUtil.fetchJsFileContent(this.project), hardwareItemMap);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("<html><body onload='callRunOpMode()'><script type='text/javascript'>\n");
    FileUtil.readAsset(stringBuilder, activity.getAssets(), "blocks/runtime.js");
    stringBuilder.append("\n");
    stringBuilder.append(str);
    stringBuilder.append("\n</script></body></html>\n");
    webView.loadDataWithBaseURL(null, stringBuilder.toString(), "text/html", "UTF-8", null);
  }
  
  @Deprecated
  public static void registerAll(OpModeManager paramOpModeManager) {
    RobotLog.w(BlocksOpMode.class.getSimpleName(), new Object[] { "registerAll(OpModeManager) is deprecated and will be removed soon, as calling it is unnecessary in this and future API version" });
  }
  
  private void removeJavascriptInterfaces() {
    Iterator<Map.Entry> iterator = javascriptInterfaces.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry entry = iterator.next();
      String str = (String)entry.getKey();
      Access access = (Access)entry.getValue();
      webView.removeJavascriptInterface(str);
      access.close();
      iterator.remove();
    } 
  }
  
  public static void setActivityAndWebView(Activity paramActivity, WebView paramWebView) {
    if (activity == null && webView == null)
      addOpModeRegistrar(); 
    activity = paramActivity;
    webView = paramWebView;
    paramWebView.getSettings().setJavaScriptEnabled(true);
    webView.setWebChromeClient(new WebChromeClient() {
          public boolean onConsoleMessage(ConsoleMessage param1ConsoleMessage) {
            return false;
          }
        });
  }
  
  void addJavascriptInterfacesForIdentifiers() {
    javascriptInterfaces.put(Identifier.ACCELERATION.identifierForJavaScript, new AccelerationAccess(this, Identifier.ACCELERATION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANDROID_ACCELEROMETER.identifierForJavaScript, new AndroidAccelerometerAccess(this, Identifier.ANDROID_ACCELEROMETER.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANDROID_GYROSCOPE.identifierForJavaScript, new AndroidGyroscopeAccess(this, Identifier.ANDROID_GYROSCOPE.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANDROID_ORIENTATION.identifierForJavaScript, new AndroidOrientationAccess(this, Identifier.ANDROID_ORIENTATION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANDROID_SOUND_POOL.identifierForJavaScript, new AndroidSoundPoolAccess(this, Identifier.ANDROID_SOUND_POOL.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANDROID_TEXT_TO_SPEECH.identifierForJavaScript, new AndroidTextToSpeechAccess(this, Identifier.ANDROID_TEXT_TO_SPEECH.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ANGULAR_VELOCITY.identifierForJavaScript, new AngularVelocityAccess(this, Identifier.ANGULAR_VELOCITY.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.BLINKIN_PATTERN.identifierForJavaScript, new BlinkinPatternAccess(this, Identifier.BLINKIN_PATTERN.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.BNO055IMU_PARAMETERS.identifierForJavaScript, new BNO055IMUParametersAccess(this, Identifier.BNO055IMU_PARAMETERS.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.COLOR.identifierForJavaScript, new ColorAccess(this, Identifier.COLOR.identifierForJavaScript, activity));
    javascriptInterfaces.put(Identifier.DBG_LOG.identifierForJavaScript, new DbgLogAccess(this, Identifier.DBG_LOG.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ELAPSED_TIME.identifierForJavaScript, new ElapsedTimeAccess(this, Identifier.ELAPSED_TIME.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.GAMEPAD_1.identifierForJavaScript, new GamepadAccess(this, Identifier.GAMEPAD_1.identifierForJavaScript, this.gamepad1));
    javascriptInterfaces.put(Identifier.GAMEPAD_2.identifierForJavaScript, new GamepadAccess(this, Identifier.GAMEPAD_2.identifierForJavaScript, this.gamepad2));
    javascriptInterfaces.put(Identifier.LINEAR_OP_MODE.identifierForJavaScript, new LinearOpModeAccess(this, Identifier.LINEAR_OP_MODE.identifierForJavaScript, this.project));
    javascriptInterfaces.put(Identifier.MAGNETIC_FLUX.identifierForJavaScript, new MagneticFluxAccess(this, Identifier.MAGNETIC_FLUX.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.MATRIX_F.identifierForJavaScript, new MatrixFAccess(this, Identifier.MATRIX_F.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.MISC.identifierForJavaScript, new MiscAccess(this, Identifier.MISC.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.NAVIGATION.identifierForJavaScript, new NavigationAccess(this, Identifier.NAVIGATION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.OPEN_GL_MATRIX.identifierForJavaScript, new OpenGLMatrixAccess(this, Identifier.OPEN_GL_MATRIX.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.ORIENTATION.identifierForJavaScript, new OrientationAccess(this, Identifier.ORIENTATION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.PIDF_COEFFICIENTS.identifierForJavaScript, new PIDFCoefficientsAccess(this, Identifier.PIDF_COEFFICIENTS.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.POSITION.identifierForJavaScript, new PositionAccess(this, Identifier.POSITION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.QUATERNION.identifierForJavaScript, new QuaternionAccess(this, Identifier.QUATERNION.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.RANGE.identifierForJavaScript, new RangeAccess(this, Identifier.RANGE.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.SYSTEM.identifierForJavaScript, new SystemAccess(this, Identifier.SYSTEM.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.TELEMETRY.identifierForJavaScript, new TelemetryAccess(this, Identifier.TELEMETRY.identifierForJavaScript, this.telemetry));
    javascriptInterfaces.put(Identifier.TEMPERATURE.identifierForJavaScript, new TemperatureAccess(this, Identifier.TEMPERATURE.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.TFOD_CURRENT_GAME.identifierForJavaScript, new TfodCurrentGameAccess(this, Identifier.TFOD_CURRENT_GAME.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.TFOD_CUSTOM_MODEL.identifierForJavaScript, new TfodCustomModelAccess(this, Identifier.TFOD_CUSTOM_MODEL.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.TFOD_ROVER_RUCKUS.identifierForJavaScript, new TfodRoverRuckusAccess(this, Identifier.TFOD_ROVER_RUCKUS.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.TFOD_SKY_STONE.identifierForJavaScript, new TfodSkyStoneAccess(this, Identifier.TFOD_SKY_STONE.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VECTOR_F.identifierForJavaScript, new VectorFAccess(this, Identifier.VECTOR_F.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.VELOCITY.identifierForJavaScript, new VelocityAccess(this, Identifier.VELOCITY.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.VUFORIA_CURRENT_GAME.identifierForJavaScript, new VuforiaCurrentGameAccess(this, Identifier.VUFORIA_CURRENT_GAME.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_RELIC_RECOVERY.identifierForJavaScript, new VuforiaRelicRecoveryAccess(this, Identifier.VUFORIA_RELIC_RECOVERY.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_ROVER_RUCKUS.identifierForJavaScript, new VuforiaRoverRuckusAccess(this, Identifier.VUFORIA_ROVER_RUCKUS.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_SKY_STONE.identifierForJavaScript, new VuforiaSkyStoneAccess(this, Identifier.VUFORIA_SKY_STONE.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_LOCALIZER.identifierForJavaScript, new VuforiaLocalizerAccess(this, Identifier.VUFORIA_LOCALIZER.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.VUFORIA_LOCALIZER_PARAMETERS.identifierForJavaScript, new VuforiaLocalizerParametersAccess(this, Identifier.VUFORIA_LOCALIZER_PARAMETERS.identifierForJavaScript, (Context)activity, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_TRACKABLE.identifierForJavaScript, new VuforiaTrackableAccess(this, Identifier.VUFORIA_TRACKABLE.identifierForJavaScript));
    javascriptInterfaces.put(Identifier.VUFORIA_TRACKABLE_DEFAULT_LISTENER.identifierForJavaScript, new VuforiaTrackableDefaultListenerAccess(this, Identifier.VUFORIA_TRACKABLE_DEFAULT_LISTENER.identifierForJavaScript, this.hardwareMap));
    javascriptInterfaces.put(Identifier.VUFORIA_TRACKABLES.identifierForJavaScript, new VuforiaTrackablesAccess(this, Identifier.VUFORIA_TRACKABLES.identifierForJavaScript));
  }
  
  String getFullBlockLabel() {
    switch (this.currentBlockType) {
      default:
        return "to runOpmode";
      case null:
        stringBuilder = new StringBuilder();
        stringBuilder.append("call ");
        stringBuilder.append(this.currentBlockFirstName);
        stringBuilder.append(this.currentBlockLastName);
        return stringBuilder.toString();
      case null:
        stringBuilder = new StringBuilder();
        stringBuilder.append(this.currentBlockFirstName);
        stringBuilder.append(this.currentBlockLastName);
        return stringBuilder.toString();
      case null:
        stringBuilder = new StringBuilder();
        stringBuilder.append("set ");
        stringBuilder.append(this.currentBlockFirstName);
        stringBuilder.append(this.currentBlockLastName);
        stringBuilder.append(" to");
        return stringBuilder.toString();
      case null:
        stringBuilder = new StringBuilder();
        stringBuilder.append("new ");
        stringBuilder.append(this.currentBlockFirstName);
        return stringBuilder.toString();
      case null:
        stringBuilder = new StringBuilder();
        stringBuilder.append("to ");
        stringBuilder.append(this.currentBlockFirstName);
        stringBuilder.append(this.currentBlockLastName);
        return stringBuilder.toString();
      case null:
        break;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.currentBlockFirstName);
    stringBuilder.append(this.currentBlockLastName);
    return stringBuilder.toString();
  }
  
  CameraName getSwitchableCamera() {
    if (this.switchableCamera == null)
      this.switchableCamera = VuforiaBase.getSwitchableCamera(this.hardwareMap); 
    return this.switchableCamera;
  }
  
  boolean isStartedForBlocks() {
    return (isStarted() || isInterrupted());
  }
  
  boolean isStopRequestedForBlocks() {
    return (isStopRequested() || isInterrupted());
  }
  
  public void runOpMode() {
    null = new StringBuilder();
    null.append(getLogPrefix());
    null.append("runOpMode - start");
    RobotLog.i(null.toString());
    cleanUpPreviousBlocksOpMode();
    BlocksOpModeCompanion.opMode = (OpMode)this;
    BlocksOpModeCompanion.linearOpMode = this;
    BlocksOpModeCompanion.hardwareMap = this.hardwareMap;
    BlocksOpModeCompanion.telemetry = this.telemetry;
    BlocksOpModeCompanion.gamepad1 = this.gamepad1;
    BlocksOpModeCompanion.gamepad2 = this.gamepad2;
    try {
      Object object;
      fatalExceptionHolder.set(null);
      fatalErrorMessageHolder.set(null);
      this.currentBlockType = BlockType.EVENT;
      this.currentBlockFirstName = "";
      this.currentBlockLastName = "runOpMode";
      boolean bool = false;
      this.interruptedTime.set(0L);
      null = new AtomicBoolean();
    } finally {
      long l = this.interruptedTime.get();
      if (l != 0L) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLogPrefix());
        stringBuilder.append("runOpMode - end - ");
        stringBuilder.append(System.currentTimeMillis() - l);
        stringBuilder.append("ms after InterruptedException");
        RobotLog.i(stringBuilder.toString());
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getLogPrefix());
        stringBuilder.append("runOpMode - end - no InterruptedException");
        RobotLog.i(stringBuilder.toString());
      } 
      BlocksOpModeCompanion.opMode = null;
      BlocksOpModeCompanion.linearOpMode = null;
    } 
  }
  
  void sleepForBlocks(long paramLong) {
    null = new StringBuilder();
    null.append(getLogPrefix());
    null.append("sleepForBlocks - start");
    RobotLog.i(null.toString());
    try {
      long l = System.currentTimeMillis();
      while (!isInterrupted()) {
        long l1 = Math.min(100L, l + paramLong - System.currentTimeMillis());
        if (l1 <= 0L)
          break; 
        sleep(l1);
      } 
      return;
    } finally {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(getLogPrefix());
      stringBuilder.append("sleepForBlocks - end");
      RobotLog.i(stringBuilder.toString());
    } 
  }
  
  void startBlockExecution(BlockType paramBlockType, String paramString1, String paramString2) {
    this.currentBlockType = paramBlockType;
    this.currentBlockFirstName = paramString1;
    this.currentBlockLastName = paramString2;
    checkIfStopRequested();
  }
  
  void throwException(Exception paramException) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramException.getClass().getSimpleName());
    if (paramException.getMessage() != null) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(" - ");
      stringBuilder1.append(paramException.getMessage());
      str = stringBuilder1.toString();
    } else {
      str = "";
    } 
    stringBuilder.append(str);
    String str = stringBuilder.toString();
    stringBuilder = new StringBuilder();
    stringBuilder.append("Fatal error occurred while executing the block labeled \"");
    stringBuilder.append(getFullBlockLabel());
    stringBuilder.append("\". ");
    stringBuilder.append(str);
    paramException = new RuntimeException(stringBuilder.toString(), paramException);
    fatalExceptionHolder.set(paramException);
    throw paramException;
  }
  
  void waitForStartForBlocks() {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_1
    //   8: aload_1
    //   9: aload_0
    //   10: invokespecial getLogPrefix : ()Ljava/lang/String;
    //   13: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   16: pop
    //   17: aload_1
    //   18: ldc_w 'waitForStartForBlocks - start'
    //   21: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   24: pop
    //   25: aload_1
    //   26: invokevirtual toString : ()Ljava/lang/String;
    //   29: invokestatic i : (Ljava/lang/String;)V
    //   32: aload_0
    //   33: invokevirtual isStartedForBlocks : ()Z
    //   36: ifne -> 98
    //   39: aload_0
    //   40: monitorenter
    //   41: aload_0
    //   42: ldc2_w 100
    //   45: invokevirtual wait : (J)V
    //   48: aload_0
    //   49: monitorexit
    //   50: goto -> 32
    //   53: invokestatic currentThread : ()Ljava/lang/Thread;
    //   56: invokevirtual interrupt : ()V
    //   59: aload_0
    //   60: monitorexit
    //   61: new java/lang/StringBuilder
    //   64: dup
    //   65: invokespecial <init> : ()V
    //   68: astore_1
    //   69: aload_1
    //   70: aload_0
    //   71: invokespecial getLogPrefix : ()Ljava/lang/String;
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: pop
    //   78: aload_1
    //   79: ldc_w 'waitForStartForBlocks - end'
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: pop
    //   86: aload_1
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: invokestatic i : (Ljava/lang/String;)V
    //   93: return
    //   94: aload_0
    //   95: monitorexit
    //   96: aload_1
    //   97: athrow
    //   98: new java/lang/StringBuilder
    //   101: dup
    //   102: invokespecial <init> : ()V
    //   105: astore_1
    //   106: aload_1
    //   107: aload_0
    //   108: invokespecial getLogPrefix : ()Ljava/lang/String;
    //   111: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   114: pop
    //   115: aload_1
    //   116: ldc_w 'waitForStartForBlocks - end'
    //   119: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   122: pop
    //   123: aload_1
    //   124: invokevirtual toString : ()Ljava/lang/String;
    //   127: invokestatic i : (Ljava/lang/String;)V
    //   130: return
    //   131: astore_1
    //   132: new java/lang/StringBuilder
    //   135: dup
    //   136: invokespecial <init> : ()V
    //   139: astore_2
    //   140: aload_2
    //   141: aload_0
    //   142: invokespecial getLogPrefix : ()Ljava/lang/String;
    //   145: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   148: pop
    //   149: aload_2
    //   150: ldc_w 'waitForStartForBlocks - end'
    //   153: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   156: pop
    //   157: aload_2
    //   158: invokevirtual toString : ()Ljava/lang/String;
    //   161: invokestatic i : (Ljava/lang/String;)V
    //   164: aload_1
    //   165: athrow
    //   166: astore_1
    //   167: goto -> 53
    //   170: astore_1
    //   171: goto -> 94
    // Exception table:
    //   from	to	target	type
    //   32	41	131	finally
    //   41	48	166	java/lang/InterruptedException
    //   41	48	170	finally
    //   48	50	170	finally
    //   53	61	170	finally
    //   94	96	170	finally
    //   96	98	131	finally
  }
  
  private class BlocksOpModeAccess extends Access {
    private final AtomicBoolean scriptFinished;
    
    private final Object scriptFinishedLock;
    
    private BlocksOpModeAccess(String param1String, Object param1Object, AtomicBoolean param1AtomicBoolean) {
      super(BlocksOpMode.this, param1String, "");
      this.scriptFinishedLock = param1Object;
      this.scriptFinished = param1AtomicBoolean;
    }
    
    @JavascriptInterface
    public void caughtException(String param1String) {
      if (param1String != null) {
        if (param1String.startsWith("ReferenceError: ") && param1String.endsWith(" is not defined")) {
          param1String = param1String.substring(16, param1String.length() - 15);
          AtomicReference<String> atomicReference1 = BlocksOpMode.fatalErrorMessageHolder;
          StringBuilder stringBuilder2 = new StringBuilder();
          stringBuilder2.append("Could not find hardware device: ");
          stringBuilder2.append(param1String);
          atomicReference1.compareAndSet(null, stringBuilder2.toString());
          return;
        } 
        if (BlocksOpMode.this.forceStopped) {
          AppUtil.getInstance().showAlertDialog(UILocation.BOTH, "OpMode Force-Stopped", "User OpMode was stuck in stop(), but was able to be force stopped without restarting the app. Please make sure you are calling opModeIsActive() in any loops!");
          return;
        } 
        AtomicReference<String> atomicReference = BlocksOpMode.fatalErrorMessageHolder;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Fatal error occurred while executing the block labeled \"");
        stringBuilder1.append(BlocksOpMode.this.getFullBlockLabel());
        stringBuilder1.append("\".");
        atomicReference.compareAndSet(null, stringBuilder1.toString());
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(BlocksOpMode.this.getLogPrefix());
      stringBuilder.append("caughtException - message is ");
      stringBuilder.append(param1String);
      RobotLog.e(stringBuilder.toString());
    }
    
    @JavascriptInterface
    public void scriptFinished() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(BlocksOpMode.this.getLogPrefix());
      stringBuilder.append("scriptFinished");
      RobotLog.i(stringBuilder.toString());
      synchronized (this.scriptFinishedLock) {
        this.scriptFinished.set(true);
        this.scriptFinishedLock.notifyAll();
        return;
      } 
    }
    
    @JavascriptInterface
    public void scriptStarting() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(BlocksOpMode.this.getLogPrefix());
      stringBuilder.append("scriptStarting");
      RobotLog.i(stringBuilder.toString());
      Thread.interrupted();
      BlocksOpMode.access$402(BlocksOpMode.this, Thread.currentThread());
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\BlocksOpMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */