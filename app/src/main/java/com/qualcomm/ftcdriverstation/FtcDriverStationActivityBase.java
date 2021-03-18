package com.qualcomm.ftcdriverstation;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.input.InputManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qualcomm.ftccommon.ClassManagerFactory;
import com.qualcomm.ftccommon.ConfigWifiDirectActivity;
import com.qualcomm.ftccommon.FtcAboutActivity;
import com.qualcomm.ftccommon.LaunchActivityConstantsList;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.ftccommon.configuration.EditParameters;
import com.qualcomm.ftccommon.configuration.FtcLoadFileActivity;
import com.qualcomm.ftccommon.configuration.RobotConfigFile;
import com.qualcomm.ftccommon.configuration.RobotConfigFileManager;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.Heartbeat;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolParsableBase;
import com.qualcomm.robotcore.robocol.TelemetryMessage;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.BatteryChecker;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.ImmersiveMode;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.RollingAverage;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.firstinspires.ftc.driverstation.internal.StopWatchDrawable;
import org.firstinspires.ftc.ftccommon.external.SoundPlayingRobotMonitor;
import org.firstinspires.ftc.ftccommon.internal.ProgramAndManageActivity;
import org.firstinspires.ftc.robotcore.external.Event;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.android.AndroidTextToSpeech;
import org.firstinspires.ftc.robotcore.external.stream.CameraStreamClient;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameListener;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PeerStatusCallback;
import org.firstinspires.ftc.robotcore.internal.network.PreferenceRemoterDS;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.network.StartResult;
import org.firstinspires.ftc.robotcore.internal.network.WifiMuteEvent;
import org.firstinspires.ftc.robotcore.internal.network.WifiMuteStateMachine;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.FilledPolygonDrawable;
import org.firstinspires.ftc.robotcore.internal.ui.GamepadUser;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public abstract class FtcDriverStationActivityBase extends ThemedActivity implements NetworkConnection.NetworkConnectionCallback, RecvLoopRunnable.RecvLoopCallback, SharedPreferences.OnSharedPreferenceChangeListener, OpModeSelectionDialogFragment.OpModeSelectionDialogListener, BatteryChecker.BatteryWatcher, PeerStatusCallback, WifiMuteStateMachine.Callback {
  protected static final float FULLY_OPAQUE = 1.0F;
  
  protected static final int MATCH_NUMBER_LOWER_BOUND = 0;
  
  protected static final int MATCH_NUMBER_UPPER_BOUND = 1000;
  
  protected static final float PARTLY_OPAQUE = 0.3F;
  
  public static final String TAG = "DriverStation";
  
  protected static final boolean debugBattery = false;
  
  protected static boolean permissionsValidated = false;
  
  protected double V12BatteryMin;
  
  protected String V12BatteryMinString;
  
  protected TextView activeConfigText;
  
  private final AndroidTextToSpeech androidTextToSpeech;
  
  protected AppUtil appUtil;
  
  protected BatteryChecker batteryChecker;
  
  protected View batteryInfo;
  
  protected Button buttonAutonomous;
  
  protected View buttonInit;
  
  protected View buttonInitStop;
  
  protected ImageButton buttonMenu;
  
  protected View buttonStart;
  
  protected ImageButton buttonStop;
  
  protected Button buttonTeleOp;
  
  protected ImageView cameraStreamImageView;
  
  protected LinearLayout cameraStreamLayout;
  
  protected boolean cameraStreamOpen;
  
  protected View chooseOpModePrompt;
  
  protected boolean clientConnected;
  
  protected String connectionOwner;
  
  protected String connectionOwnerPassword;
  
  protected Context context;
  
  protected View controlPanelBack;
  
  protected TextView currentOpModeName;
  
  protected boolean debugLogging;
  
  protected final OpModeMeta defaultOpMode;
  
  protected DeviceNameManagerCallback deviceNameManagerCallback;
  
  protected StartResult deviceNameManagerStartResult;
  
  protected boolean disconnectFromPeerOnActivityStop;
  
  protected ImageView dsBatteryIcon;
  
  protected TextView dsBatteryInfo;
  
  protected Map<GamepadUser, GamepadIndicator> gamepadIndicators = new HashMap<GamepadUser, GamepadIndicator>();
  
  protected GamepadManager gamepadManager;
  
  protected Heartbeat heartbeatRecv = new Heartbeat();
  
  protected ImmersiveMode immersion;
  
  protected ElapsedTime lastUiUpdate;
  
  private InputManager mInputManager;
  
  protected NetworkConnectionHandler networkConnectionHandler;
  
  protected OpModeCountDownTimer opModeCountDown;
  
  protected boolean opModeUseTimer;
  
  protected List<OpModeMeta> opModes;
  
  protected RollingAverage pingAverage;
  
  protected StartResult prefRemoterStartResult;
  
  protected SharedPreferences preferences;
  
  protected PreferencesHelper preferencesHelper;
  
  protected boolean processUserActivity;
  
  protected OpModeMeta queuedOpMode;
  
  protected OpModeMeta queuedOpModeWhenMuted;
  
  protected View rcBatteryContainer;
  
  protected ImageView rcBatteryIcon;
  
  protected TextView rcBatteryTelemetry;
  
  protected boolean rcHasIndependentBattery;
  
  protected TextView robotBatteryMinimum;
  
  protected TextView robotBatteryTelemetry;
  
  protected RobotConfigFileManager robotConfigFileManager;
  
  protected RobotState robotState;
  
  protected TextView systemTelemetry;
  
  protected int systemTelemetryOriginalColor;
  
  protected Telemetry.DisplayFormat telemetryMode;
  
  protected TextView textBytesPerSecond;
  
  protected TextView textDeviceName;
  
  protected TextView textDsUiStateIndicator;
  
  protected TextView textPingStatus;
  
  protected TextView textTelemetry;
  
  protected TextView textWifiChannel;
  
  protected TextView textWifiDirectStatus;
  
  protected boolean textWifiDirectStatusShowingRC;
  
  protected View timerAndTimerSwitch;
  
  protected UIState uiState;
  
  protected Thread uiThread;
  
  protected Utility utility;
  
  protected View wifiInfo;
  
  protected WifiMuteStateMachine wifiMuteStateMachine;
  
  public FtcDriverStationActivityBase() {
    OpModeMeta opModeMeta = new OpModeMeta("$Stop$Robot$");
    this.defaultOpMode = opModeMeta;
    this.queuedOpMode = opModeMeta;
    this.queuedOpModeWhenMuted = opModeMeta;
    this.opModes = new LinkedList<OpModeMeta>();
    this.opModeUseTimer = false;
    this.pingAverage = new RollingAverage(10);
    this.lastUiUpdate = new ElapsedTime();
    this.uiState = UIState.UNKNOWN;
    this.telemetryMode = Telemetry.DisplayFormat.CLASSIC;
    this.debugLogging = false;
    this.networkConnectionHandler = NetworkConnectionHandler.getInstance();
    this.appUtil = AppUtil.getInstance();
    this.deviceNameManagerStartResult = new StartResult();
    this.prefRemoterStartResult = new StartResult();
    this.deviceNameManagerCallback = new DeviceNameManagerCallback();
    this.processUserActivity = false;
    this.disconnectFromPeerOnActivityStop = true;
    this.androidTextToSpeech = new AndroidTextToSpeech();
  }
  
  private void checkRcIndependentBattery(SharedPreferences paramSharedPreferences) {
    this.rcHasIndependentBattery = paramSharedPreferences.getBoolean(getString(2131624419), true);
  }
  
  private String getBestRobotControllerName() {
    return this.networkConnectionHandler.getConnectionOwnerName();
  }
  
  private CallbackResult handleCommandSetTelemetryDisplayFormat(String paramString) {
    try {
      Telemetry.DisplayFormat displayFormat = Telemetry.DisplayFormat.valueOf(paramString);
      if (displayFormat != this.telemetryMode) {
        int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$Telemetry$DisplayFormat[displayFormat.ordinal()];
        if (i != 1) {
          if (i == 2 || i == 3)
            this.textTelemetry.setTypeface(Typeface.DEFAULT); 
        } else {
          this.textTelemetry.setTypeface(Typeface.MONOSPACE);
        } 
      } 
      this.telemetryMode = displayFormat;
    } catch (IllegalArgumentException illegalArgumentException) {}
    return CallbackResult.HANDLED;
  }
  
  private CallbackResult handleCommandStartProgramAndManageResp(String paramString) {
    if (paramString != null && !paramString.isEmpty()) {
      Intent intent = new Intent(AppUtil.getDefContext(), ProgramAndManageActivity.class);
      intent.putExtra("RC_WEB_INFO", paramString);
      startActivityForResult(intent, LaunchActivityConstantsList.RequestCode.PROGRAM_AND_MANAGE.ordinal());
    } 
    return CallbackResult.HANDLED;
  }
  
  private CallbackResult handleCommandTextToSpeech(String paramString) {
    RobotCoreCommandList.TextToSpeech textToSpeech = RobotCoreCommandList.TextToSpeech.deserialize(paramString);
    paramString = textToSpeech.getText();
    String str1 = textToSpeech.getLanguageCode();
    String str2 = textToSpeech.getCountryCode();
    if (str1 != null && !str1.isEmpty())
      if (str2 != null && !str2.isEmpty()) {
        this.androidTextToSpeech.setLanguageAndCountry(str1, str2);
      } else {
        this.androidTextToSpeech.setLanguage(str1);
      }  
    this.androidTextToSpeech.speak(paramString);
    return CallbackResult.HANDLED;
  }
  
  private void onPeersAvailableSoftAP() {
    if (this.networkConnectionHandler.connectionMatches(getString(2131624149))) {
      showWifiStatus(false, getString(2131624683));
    } else {
      showWifiStatus(false, getString(2131624684));
    } 
    this.networkConnectionHandler.handlePeersAvailable();
  }
  
  private void onPeersAvailableWifiDirect() {
    if (this.networkConnectionHandler.connectingOrConnected())
      return; 
    onPeersAvailableSoftAP();
  }
  
  public static void setPermissionsValidated() {
    permissionsValidated = true;
  }
  
  private void updateRcBatteryIndependence(SharedPreferences paramSharedPreferences) {
    updateRcBatteryIndependence(paramSharedPreferences, true);
  }
  
  private void updateRcBatteryIndependence(SharedPreferences paramSharedPreferences, boolean paramBoolean) {
    checkRcIndependentBattery(paramSharedPreferences);
    RobotLog.vv("DriverStation", "updateRcBatteryIndependence(%s)", new Object[] { Boolean.valueOf(this.rcHasIndependentBattery) });
    if (paramBoolean)
      displayRcBattery(this.rcHasIndependentBattery); 
  }
  
  protected void assertUiThread() {
    boolean bool;
    if (Thread.currentThread() == this.uiThread) {
      bool = true;
    } else {
      bool = false;
    } 
    Assert.assertTrue(bool);
  }
  
  protected void assumeClientConnect(ControlPanelBack paramControlPanelBack) {
    RobotLog.vv("DriverStation", "Assuming client connected");
    if (this.uiState == UIState.UNKNOWN || this.uiState == UIState.DISCONNECTED || this.uiState == UIState.CANT_CONTINUE) {
      setClientConnected(true);
      uiRobotControllerIsConnected(paramControlPanelBack);
    } 
  }
  
  protected void assumeClientConnectAndRefreshUI(ControlPanelBack paramControlPanelBack) {
    assumeClientConnect(paramControlPanelBack);
    requestUIState();
  }
  
  protected void assumeClientDisconnect() {
    RobotLog.vv("DriverStation", "Assuming client disconnected");
    setClientConnected(false);
    enableAndResetTimer(false);
    this.opModeCountDown.disable();
    this.queuedOpMode = this.defaultOpMode;
    this.opModes.clear();
    pingStatus(2131624391);
    stopKeepAlives();
    this.networkConnectionHandler.clientDisconnect();
    RobocolParsableBase.initializeSequenceNumber(10000);
    RobotLog.clearGlobalErrorMsg();
    setRobotState(RobotState.UNKNOWN);
    uiRobotControllerIsDisconnected();
  }
  
  protected void brightenControlPanelBack() {
    setOpacity(this.controlPanelBack, 1.0F);
  }
  
  protected void checkConnectedEnableBrighten(ControlPanelBack paramControlPanelBack) {
    if (!this.clientConnected) {
      RobotLog.vv("DriverStation", "auto-rebrightening for connected state");
      enableAndBrightenForConnected(paramControlPanelBack);
      setClientConnected(true);
      requestUIState();
    } 
  }
  
  protected abstract void clearMatchNumber();
  
  protected void clearMatchNumberIfNecessary() {
    if (this.queuedOpMode.flavor == OpModeMeta.Flavor.TELEOP)
      clearMatchNumber(); 
  }
  
  protected void clearSystemTelemetry() {
    setVisibility((View)this.systemTelemetry, 8);
    setTextView(this.systemTelemetry, "");
    setTextColor(this.systemTelemetry, this.systemTelemetryOriginalColor);
    RobotLog.clearGlobalErrorMsg();
    RobotLog.clearGlobalWarningMsg();
  }
  
  protected void clearUserTelemetry() {
    setTextView(this.textTelemetry, "");
  }
  
  public CallbackResult commandEvent(Command paramCommand) {
    byte b;
    String str;
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    try {
      String str1 = paramCommand.getName();
      str = paramCommand.getExtra();
      b = -1;
      switch (str1.hashCode()) {
        case 1852830809:
          if (str1.equals("CMD_TEXT_TO_SPEECH"))
            b = 20; 
          break;
        case 1661597945:
          if (str1.equals("CMD_START_DS_PROGRAM_AND_MANAGE_RESP"))
            b = 12; 
          break;
        case 1510318778:
          if (str1.equals("CMD_RECEIVE_FRAME_CHUNK"))
            b = 19; 
          break;
        case 1509292278:
          if (str1.equals("CMD_RECEIVE_FRAME_BEGIN"))
            b = 18; 
          break;
        case 1506024019:
          if (str1.equals("CMD_DISMISS_ALL_DIALOGS"))
            b = 11; 
          break;
        case 1332202628:
          if (str1.equals("CMD_NOTIFY_USER_DEVICE_LIST"))
            b = 2; 
          break;
        case 899701436:
          if (str1.equals("CMD_NOTIFY_RUN_OP_MODE"))
            b = 5; 
          break;
        case 857479075:
          if (str1.equals("CMD_NOTIFY_INIT_OP_MODE"))
            b = 4; 
          break;
        case 739339659:
          if (str1.equals("CMD_NOTIFY_ROBOT_STATE"))
            b = 0; 
          break;
        case 619130094:
          if (str1.equals("CMD_NOTIFY_ACTIVE_CONFIGURATION"))
            b = 3; 
          break;
        case 323288778:
          if (str1.equals("CMD_SHOW_PROGRESS"))
            b = 7; 
          break;
        case 202444237:
          if (str1.equals("CMD_STOP_PLAYING_SOUNDS"))
            b = 16; 
          break;
        case 78754538:
          if (str1.equals("CMD_STREAM_CHANGE"))
            b = 17; 
          break;
        case -44710726:
          if (str1.equals("CMD_REQUEST_SOUND"))
            b = 15; 
          break;
        case -206959740:
          if (str1.equals("CMD_ROBOT_CONTROLLER_PREFERENCE"))
            b = 13; 
          break;
        case -321815447:
          if (str1.equals("CMD_PLAY_SOUND"))
            b = 14; 
          break;
        case -362340438:
          if (str1.equals("CMD_SET_TELEM_DISPL_FORMAT"))
            b = 21; 
          break;
        case -856964827:
          if (str1.equals("CMD_SHOW_DIALOG"))
            b = 9; 
          break;
        case -939314969:
          if (str1.equals("CMD_DISMISS_PROGRESS"))
            b = 8; 
          break;
        case -992356734:
          if (str1.equals("CMD_DISMISS_DIALOG"))
            b = 10; 
          break;
        case -1121067382:
          if (str1.equals("CMD_SHOW_TOAST"))
            b = 6; 
          break;
        case -1530733715:
          if (str1.equals("CMD_NOTIFY_OP_MODE_LIST"))
            b = 1; 
          break;
      } 
    } catch (Exception exception) {
      RobotLog.logStackTrace(exception);
      return callbackResult;
    } 
    switch (b) {
      case 21:
        return handleCommandSetTelemetryDisplayFormat(str);
      case 20:
        return handleCommandTextToSpeech(str);
      case 19:
        return CameraStreamClient.getInstance().handleReceiveFrameChunk(str);
      case 18:
        return CameraStreamClient.getInstance().handleReceiveFrameBegin(str);
      case 17:
        return CameraStreamClient.getInstance().handleStreamChange(str);
      case 16:
        return SoundPlayer.getInstance().handleCommandStopPlayingSounds((Command)exception);
      case 15:
        return SoundPlayer.getInstance().handleCommandRequestSound((Command)exception);
      case 14:
        return SoundPlayer.getInstance().handleCommandPlaySound(str);
      case 13:
        return PreferenceRemoterDS.getInstance().handleCommandRobotControllerPreference(str);
      case 12:
        return handleCommandStartProgramAndManageResp(str);
      case 11:
        return handleCommandDismissAllDialogs((Command)exception);
      case 10:
        return handleCommandDismissDialog((Command)exception);
      case 9:
        return handleCommandShowDialog(str);
      case 8:
        return handleCommandDismissProgress();
      case 7:
        return handleCommandShowProgress(str);
      case 6:
        return handleCommandShowToast(str);
      case 5:
        return handleCommandNotifyStartOpMode(str);
      case 4:
        return handleCommandNotifyInitOpMode(str);
      case 3:
        return handleCommandNotifyActiveConfig(str);
      case 2:
        return handleCommandNotifyUserDeviceList(str);
      case 1:
        return handleCommandNotifyOpModeList(str);
      case 0:
        return handleNotifyRobotState(str);
    } 
    return callbackResult;
  }
  
  protected void dimAndDisableAllControls() {
    dimControlPanelBack();
    setOpacity(this.wifiInfo, 0.3F);
    setOpacity(this.batteryInfo, 0.3F);
    disableAndDimOpModeMenu();
    disableOpModeControls();
  }
  
  protected void dimControlPanelBack() {
    setOpacity(this.controlPanelBack, 0.3F);
  }
  
  protected void disableAndDim(View paramView) {
    setOpacity(paramView, 0.3F);
    setEnabled(paramView, false);
  }
  
  protected void disableAndDimOpModeMenu() {
    disableAndDim((View)this.buttonAutonomous);
    disableAndDim((View)this.buttonTeleOp);
    disableAndDim((View)this.currentOpModeName);
    disableAndDim(this.chooseOpModePrompt);
  }
  
  protected abstract void disableMatchLoggingUI();
  
  protected void disableOpModeControls() {
    setEnabled(this.buttonInit, false);
    setVisibility(this.buttonInit, 0);
    setVisibility(this.buttonStart, 4);
    setVisibility((View)this.buttonStop, 4);
    setVisibility(this.buttonInitStop, 4);
    setVisibility(this.timerAndTimerSwitch, 4);
    hideCameraStream();
  }
  
  public boolean dispatchGenericMotionEvent(MotionEvent paramMotionEvent) {
    if (Gamepad.isGamepadDevice(paramMotionEvent.getDeviceId())) {
      this.gamepadManager.handleGamepadEvent(paramMotionEvent);
      return true;
    } 
    return super.dispatchGenericMotionEvent(paramMotionEvent);
  }
  
  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
    if (Gamepad.isGamepadDevice(paramKeyEvent.getDeviceId())) {
      this.gamepadManager.handleGamepadEvent(paramKeyEvent);
      return true;
    } 
    return super.dispatchKeyEvent(paramKeyEvent);
  }
  
  protected void displayDeviceName(final String name) {
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityBase.this.textDeviceName.setText(name);
          }
        });
  }
  
  protected void displayRcBattery(boolean paramBoolean) {
    byte b;
    View view = this.rcBatteryContainer;
    if (paramBoolean) {
      b = 0;
    } else {
      b = 8;
    } 
    view.setVisibility(b);
  }
  
  protected abstract void doMatchNumFieldBehaviorInit();
  
  public CallbackResult emptyEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void enableAndBrighten(View paramView) {
    setOpacity(paramView, 1.0F);
    setEnabled(paramView, true);
  }
  
  protected void enableAndBrightenForConnected(ControlPanelBack paramControlPanelBack) {
    setControlPanelBack(paramControlPanelBack);
    setOpacity(this.wifiInfo, 1.0F);
    setOpacity(this.batteryInfo, 1.0F);
    enableAndBrightenOpModeMenu();
  }
  
  protected void enableAndBrightenOpModeMenu() {
    enableAndBrighten((View)this.buttonAutonomous);
    enableAndBrighten((View)this.buttonTeleOp);
    setOpacity((View)this.currentOpModeName, 1.0F);
    setOpacity(this.chooseOpModePrompt, 1.0F);
  }
  
  protected void enableAndResetTimer(boolean paramBoolean) {
    if (!paramBoolean) {
      this.opModeCountDown.disable();
    } else {
      stopTimerAndReset();
      this.opModeCountDown.enable();
    } 
    this.opModeUseTimer = paramBoolean;
  }
  
  protected void enableAndResetTimerForQueued() {
    boolean bool;
    if (this.queuedOpMode.flavor == OpModeMeta.Flavor.AUTONOMOUS) {
      bool = true;
    } else {
      bool = false;
    } 
    enableAndResetTimer(bool);
  }
  
  protected abstract void enableMatchLoggingUI();
  
  protected void enforcePermissionValidator() {
    if (!permissionsValidated) {
      RobotLog.vv("DriverStation", "Redirecting to permission validator");
      startActivity(new Intent(AppUtil.getDefContext(), PermissionValidatorWrapper.class));
      finish();
      return;
    } 
    RobotLog.vv("DriverStation", "Permissions validated already");
  }
  
  protected List<OpModeMeta> filterOpModes(Predicate<OpModeMeta> paramPredicate) {
    LinkedList<OpModeMeta> linkedList = new LinkedList();
    for (OpModeMeta opModeMeta : this.opModes) {
      if (paramPredicate.test(opModeMeta))
        linkedList.add(opModeMeta); 
    } 
    return linkedList;
  }
  
  public CallbackResult gamepadEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected abstract int getMatchNumber() throws NumberFormatException;
  
  protected OpModeMeta getOpModeMeta(String paramString) {
    synchronized (this.opModes) {
      for (OpModeMeta opModeMeta : this.opModes) {
        if (opModeMeta.name.equals(paramString))
          return opModeMeta; 
      } 
      return new OpModeMeta(paramString);
    } 
  }
  
  public abstract View getPopupMenuAnchor();
  
  public String getTag() {
    return "DriverStation";
  }
  
  protected CallbackResult handleCommandDismissAllDialogs(Command paramCommand) {
    this.appUtil.dismissAllDialogs(UILocation.ONLY_LOCAL);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandDismissDialog(Command paramCommand) {
    this.appUtil.dismissDialog(UILocation.ONLY_LOCAL, RobotCoreCommandList.DismissDialog.deserialize(paramCommand.getExtra()));
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandDismissProgress() {
    this.appUtil.dismissProgress(UILocation.ONLY_LOCAL);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandNotifyActiveConfig(String paramString) {
    RobotLog.vv("DriverStation", "%s.handleCommandRequestActiveConfigResp(%s)", new Object[] { getClass().getSimpleName(), paramString });
    final RobotConfigFile configFile = this.robotConfigFileManager.getConfigFromString(paramString);
    this.robotConfigFileManager.setActiveConfig(robotConfigFile);
    this.appUtil.runOnUiThread((Activity)this, new Runnable() {
          public void run() {
            FtcDriverStationActivityBase.this.activeConfigText.setText(configFile.getName());
          }
        });
    return CallbackResult.HANDLED_CONTINUE;
  }
  
  protected CallbackResult handleCommandNotifyInitOpMode(String paramString) {
    if (this.uiState == UIState.CANT_CONTINUE)
      return CallbackResult.HANDLED; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Robot Controller initializing op mode: ");
    stringBuilder.append(paramString);
    RobotLog.vv("DriverStation", stringBuilder.toString());
    stopTimerPreservingRemainingTime();
    if (isDefaultOpMode(paramString)) {
      this.androidTextToSpeech.stop();
      stopKeepAlives();
      runOnUiThread(new Runnable() {
            public void run() {
              FtcDriverStationActivityBase.this.telemetryMode = Telemetry.DisplayFormat.CLASSIC;
              FtcDriverStationActivityBase.this.textTelemetry.setTypeface(Typeface.DEFAULT);
            }
          });
      handleDefaultOpModeInitOrStart(false);
    } else {
      clearUserTelemetry();
      startKeepAlives();
      if (setQueuedOpModeIfDifferent(paramString)) {
        RobotLog.vv("DriverStation", "timer: init new opmode");
        enableAndResetTimerForQueued();
      } else if (this.opModeCountDown.isEnabled()) {
        RobotLog.vv("DriverStation", "timer: init w/ timer enabled");
        this.opModeCountDown.resetCountdown();
      } else {
        RobotLog.vv("DriverStation", "timer: init w/o timer enabled");
      } 
      uiWaitingForStartEvent();
    } 
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandNotifyOpModeList(String paramString) {
    assumeClientConnect(ControlPanelBack.NO_CHANGE);
    this.opModes = (List<OpModeMeta>)(new Gson()).fromJson(paramString, (new TypeToken<Collection<OpModeMeta>>() {
        
        }).getType());
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Received the following op modes: ");
    stringBuilder.append(this.opModes.toString());
    RobotLog.vv("DriverStation", stringBuilder.toString());
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandNotifyStartOpMode(String paramString) {
    if (this.uiState == UIState.CANT_CONTINUE)
      return CallbackResult.HANDLED; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Robot Controller starting op mode: ");
    stringBuilder.append(paramString);
    RobotLog.vv("DriverStation", stringBuilder.toString());
    if (isDefaultOpMode(paramString)) {
      this.androidTextToSpeech.stop();
      stopKeepAlives();
      handleDefaultOpModeInitOrStart(true);
    } else {
      if (setQueuedOpModeIfDifferent(paramString)) {
        RobotLog.vv("DriverStation", "timer: started new opmode: auto-initing timer");
        enableAndResetTimerForQueued();
      } 
      uiWaitingForStopEvent();
      if (this.opModeUseTimer) {
        this.opModeCountDown.start();
      } else {
        stopTimerAndReset();
      } 
    } 
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandNotifyUserDeviceList(String paramString) {
    ConfigurationTypeManager.getInstance().deserializeUserDeviceTypes(paramString);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandShowDialog(String paramString) {
    RobotCoreCommandList.ShowDialog showDialog = RobotCoreCommandList.ShowDialog.deserialize(paramString);
    AppUtil.DialogParams dialogParams = new AppUtil.DialogParams(UILocation.ONLY_LOCAL, showDialog.title, showDialog.message);
    dialogParams.uuidString = showDialog.uuidString;
    this.appUtil.showDialog(dialogParams);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandShowProgress(String paramString) {
    RobotCoreCommandList.ShowProgress showProgress = RobotCoreCommandList.ShowProgress.deserialize(paramString);
    this.appUtil.showProgress(UILocation.ONLY_LOCAL, showProgress.message, (ProgressParameters)showProgress);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandShowToast(String paramString) {
    RobotCoreCommandList.ShowToast showToast = RobotCoreCommandList.ShowToast.deserialize(paramString);
    this.appUtil.showToast(UILocation.ONLY_LOCAL, showToast.message, showToast.duration);
    return CallbackResult.HANDLED;
  }
  
  protected void handleDefaultOpModeInitOrStart(boolean paramBoolean) {
    if (isDefaultOpMode(this.queuedOpMode)) {
      uiWaitingForOpModeSelection();
      return;
    } 
    uiWaitingForInitEvent();
    if (!paramBoolean)
      runDefaultOpMode(); 
  }
  
  protected CallbackResult handleNotifyRobotState(String paramString) {
    setRobotState(RobotState.fromByte(Integer.valueOf(paramString).intValue()));
    return CallbackResult.HANDLED;
  }
  
  protected void handleOpModeInit() {
    if (this.uiState != UIState.WAITING_FOR_INIT_EVENT)
      return; 
    traceUiStateChange("ui:uiWaitingForAck", UIState.WAITING_FOR_ACK);
    sendMatchNumberIfNecessary();
    this.networkConnectionHandler.sendCommand(new Command("CMD_INIT_OP_MODE", this.queuedOpMode.name));
    if (!this.queuedOpMode.name.equals(this.defaultOpMode.name))
      this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.RUNNING_OPMODE); 
    hideCameraStream();
  }
  
  protected void handleOpModeQueued(OpModeMeta paramOpModeMeta) {
    if (setQueuedOpModeIfDifferent(paramOpModeMeta))
      enableAndResetTimerForQueued(); 
    uiWaitingForInitEvent();
  }
  
  protected void handleOpModeStart() {
    if (this.uiState != UIState.WAITING_FOR_START_EVENT)
      return; 
    traceUiStateChange("ui:uiWaitingForAck", UIState.WAITING_FOR_ACK);
    this.networkConnectionHandler.sendCommand(new Command("CMD_RUN_OP_MODE", this.queuedOpMode.name));
  }
  
  protected void handleOpModeStop() {
    if (this.uiState != UIState.WAITING_FOR_START_EVENT && this.uiState != UIState.WAITING_FOR_STOP_EVENT)
      return; 
    traceUiStateChange("ui:uiWaitingForAck", UIState.WAITING_FOR_ACK);
    clearMatchNumberIfNecessary();
    initDefaultOpMode();
    this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.STOPPED_OPMODE);
  }
  
  protected CallbackResult handleReportGlobalError(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Received error from robot controller: ");
    stringBuilder.append(paramString);
    RobotLog.ee("DriverStation", stringBuilder.toString());
    RobotLog.setGlobalErrorMsg(paramString);
    return CallbackResult.HANDLED;
  }
  
  public CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong) {
    try {
      this.heartbeatRecv.fromByteArray(paramRobocolDatagram.getData());
      RobotLog.processTimeSynch(this.heartbeatRecv.t0, this.heartbeatRecv.t1, this.heartbeatRecv.t2, paramLong);
      double d = this.heartbeatRecv.getElapsedSeconds();
      this.heartbeatRecv.getSequenceNumber();
      setRobotState(RobotState.fromByte(this.heartbeatRecv.getRobotState()));
      this.pingAverage.addNumber((int)(d * 1000.0D));
      if (this.lastUiUpdate.time() > 0.5D) {
        this.lastUiUpdate.reset();
        networkStatus();
      } 
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStackTrace((Throwable)robotCoreException);
    } 
    return CallbackResult.HANDLED;
  }
  
  protected void hideCameraStream() {
    this.cameraStreamOpen = false;
    this.gamepadManager.setEnabled(true);
    setVisibility((View)this.cameraStreamLayout, 4);
    setVisibility(this.buttonStart, 0);
  }
  
  protected void initDefaultOpMode() {
    this.networkConnectionHandler.sendCommand(new Command("CMD_INIT_OP_MODE", this.defaultOpMode.name));
  }
  
  protected void initializeNetwork() {
    updateLoggingPrefs();
    NetworkType networkType = NetworkConnectionHandler.getDefaultNetworkType((Context)this);
    this.connectionOwner = this.preferences.getString(getString(2131624404), getString(2131624149));
    this.connectionOwnerPassword = this.preferences.getString(getString(2131624405), getString(2131624150));
    this.networkConnectionHandler.init(NetworkConnectionHandler.newWifiLock(), networkType, this.connectionOwner, this.connectionOwnerPassword, (Context)this, this.gamepadManager);
    if (this.networkConnectionHandler.isNetworkConnected()) {
      RobotLog.vv("Robocol", "Spoofing a Network Connection event...");
      onNetworkConnectionEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE);
    } 
  }
  
  protected boolean isDefaultOpMode(String paramString) {
    return this.defaultOpMode.name.equals(paramString);
  }
  
  protected boolean isDefaultOpMode(OpModeMeta paramOpModeMeta) {
    return isDefaultOpMode(paramOpModeMeta.name);
  }
  
  protected void networkStatus() {
    pingStatus(String.format("%dms", new Object[] { Integer.valueOf(this.pingAverage.getAverage()) }));
    long l = this.networkConnectionHandler.getBytesPerSecond();
    if (l > 0L)
      showBytesPerSecond(l); 
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    RobotLog.vv("DriverStation", "onActivityResult(request=%d)", new Object[] { Integer.valueOf(paramInt1) });
    if (paramInt1 == LaunchActivityConstantsList.RequestCode.SETTINGS_DRIVER_STATION.ordinal()) {
      if (paramIntent != null) {
        FtcDriverStationSettingsActivity.Result result = FtcDriverStationSettingsActivity.Result.deserialize(paramIntent.getExtras().getString("RESULT"));
        if (result.prefLogsClicked)
          updateLoggingPrefs(); 
        if (result.prefPairingMethodChanged) {
          RobotLog.ii("DriverStation", "Pairing method changed in settings activity, shutdown network to force complete restart");
          startOrRestartNetwork();
        } 
        if (result.prefPairClicked)
          startOrRestartNetwork(); 
        if (result.prefAdvancedClicked) {
          this.networkConnectionHandler.sendCommand(new Command("CMD_RESTART_ROBOT"));
          return;
        } 
      } 
    } else if (paramInt1 == LaunchActivityConstantsList.RequestCode.CONFIGURE_DRIVER_STATION.ordinal()) {
      requestUIState();
      this.networkConnectionHandler.sendCommand(new Command("CMD_RESTART_ROBOT"));
    } 
  }
  
  public void onClickButtonAutonomous(View paramView) {
    showOpModeDialog(filterOpModes(new Predicate<OpModeMeta>() {
            public boolean test(OpModeMeta param1OpModeMeta) {
              return (param1OpModeMeta.flavor == OpModeMeta.Flavor.AUTONOMOUS);
            }
          },  ), 2131624357);
  }
  
  public void onClickButtonInit(View paramView) {
    handleOpModeInit();
  }
  
  public void onClickButtonStart(View paramView) {
    handleOpModeStart();
  }
  
  public void onClickButtonStop(View paramView) {
    handleOpModeStop();
  }
  
  public void onClickButtonTeleOp(View paramView) {
    showOpModeDialog(filterOpModes(new Predicate<OpModeMeta>() {
            public boolean test(OpModeMeta param1OpModeMeta) {
              return (param1OpModeMeta.flavor == OpModeMeta.Flavor.TELEOP);
            }
          },  ), 2131624358);
  }
  
  public void onClickDSBatteryToast(View paramView) {
    showToast(getString(2131624622));
  }
  
  public void onClickRCBatteryToast(View paramView) {
    showToast(getString(2131624635));
  }
  
  public void onClickRobotBatteryToast(View paramView) {
    resetBatteryStats();
    showToast(getString(2131624634));
  }
  
  public void onClickTimer(View paramView) {
    int i = this.opModeUseTimer ^ true;
    this.opModeUseTimer = i;
    enableAndResetTimer(i);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    enforcePermissionValidator();
    this.uiThread = Thread.currentThread();
    subclassOnCreate();
    this.gamepadManager = new GamepadManager((Context)this);
    this.context = (Context)this;
    this.utility = new Utility((Activity)this);
    this.opModeCountDown = new OpModeCountDownTimer();
    this.rcHasIndependentBattery = false;
    PreferenceManager.setDefaultValues((Context)this, 2131820545, false);
    this.preferences = PreferenceManager.getDefaultSharedPreferences((Context)this);
    this.preferencesHelper = new PreferencesHelper("DriverStation", this.preferences);
    DeviceNameManagerFactory.getInstance().start(this.deviceNameManagerStartResult);
    PreferenceRemoterDS.getInstance().start(this.prefRemoterStartResult);
    NetworkConnectionHandler.getInstance().registerPeerStatusCallback(this);
    setClientConnected(false);
    if (permissionsValidated) {
      RobotLog.ii("DriverStation", "Processing all classes through class filter");
      ClassManagerFactory.registerResourceFilters();
      ClassManagerFactory.processAllClasses();
    } 
    this.robotConfigFileManager = new RobotConfigFileManager((Activity)this);
    this.textDeviceName = (TextView)findViewById(2131231118);
    this.textDsUiStateIndicator = (TextView)findViewById(2131231119);
    this.textWifiDirectStatus = (TextView)findViewById(2131231147);
    this.textWifiDirectStatusShowingRC = false;
    this.textWifiChannel = (TextView)findViewById(2131231185);
    this.textPingStatus = (TextView)findViewById(2131231120);
    this.textBytesPerSecond = (TextView)findViewById(2131230816);
    this.textTelemetry = (TextView)findViewById(2131231125);
    TextView textView2 = (TextView)findViewById(2131231124);
    this.systemTelemetry = textView2;
    this.systemTelemetryOriginalColor = textView2.getCurrentTextColor();
    this.rcBatteryContainer = findViewById(2131231038);
    this.rcBatteryTelemetry = (TextView)findViewById(2131231040);
    this.robotBatteryMinimum = (TextView)findViewById(2131231047);
    this.rcBatteryIcon = (ImageView)findViewById(2131231041);
    this.dsBatteryInfo = (TextView)findViewById(2131230885);
    this.robotBatteryTelemetry = (TextView)findViewById(2131231048);
    this.dsBatteryIcon = (ImageView)findViewById(2131230722);
    this.immersion = new ImmersiveMode(getWindow().getDecorView());
    doMatchNumFieldBehaviorInit();
    LinearLayout linearLayout = (LinearLayout)findViewById(2131230834);
    this.cameraStreamLayout = linearLayout;
    linearLayout.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            FtcDriverStationActivityBase.this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_FRAME"));
          }
        });
    this.cameraStreamImageView = (ImageView)findViewById(2131230833);
    CameraStreamClient.getInstance().setListener(new CameraStreamClient.Listener() {
          public void onFrameBitmap(final Bitmap frameBitmap) {
            FtcDriverStationActivityBase.this.runOnUiThread(new Runnable() {
                  public void run() {
                    FtcDriverStationActivityBase.this.cameraStreamImageView.setImageBitmap(frameBitmap);
                  }
                });
          }
          
          public void onStreamAvailableChange(boolean param1Boolean) {
            FtcDriverStationActivityBase.this.invalidateOptionsMenu();
            if (FtcDriverStationActivityBase.this.cameraStreamOpen && !param1Boolean)
              FtcDriverStationActivityBase.this.hideCameraStream(); 
          }
        });
    this.buttonInit = findViewById(2131230818);
    this.buttonInitStop = findViewById(2131230819);
    this.buttonStart = findViewById(2131230822);
    this.controlPanelBack = findViewById(2131230860);
    this.batteryInfo = findViewById(2131230810);
    this.wifiInfo = findViewById(2131231192);
    ((ImageButton)findViewById(2131230823)).setImageDrawable((Drawable)new FilledPolygonDrawable(((ColorDrawable)findViewById(2131230824).getBackground()).getColor(), 3));
    ((ImageView)findViewById(2131231155)).setImageDrawable((Drawable)new StopWatchDrawable(((ColorDrawable)findViewById(2131231156).getBackground()).getColor()));
    this.gamepadIndicators.put(GamepadUser.ONE, new GamepadIndicator((Activity)this, 2131231179, 2131231178));
    this.gamepadIndicators.put(GamepadUser.TWO, new GamepadIndicator((Activity)this, 2131231181, 2131231180));
    this.gamepadManager.setGamepadIndicators(this.gamepadIndicators);
    TextView textView1 = (TextView)findViewById(2131230787);
    this.activeConfigText = textView1;
    textView1.setText(" ");
    this.timerAndTimerSwitch = findViewById(2131231151);
    this.buttonAutonomous = (Button)findViewById(2131230817);
    this.buttonTeleOp = (Button)findViewById(2131230826);
    this.currentOpModeName = (TextView)findViewById(2131230868);
    this.chooseOpModePrompt = findViewById(2131230844);
    this.buttonStop = (ImageButton)findViewById(2131230825);
    ImageButton imageButton = (ImageButton)findViewById(2131230989);
    this.buttonMenu = imageButton;
    imageButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            FtcDriverStationActivityBase ftcDriverStationActivityBase = FtcDriverStationActivityBase.this;
            PopupMenu popupMenu = new PopupMenu((Context)ftcDriverStationActivityBase, ftcDriverStationActivityBase.getPopupMenuAnchor());
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  public boolean onMenuItemClick(MenuItem param2MenuItem) {
                    return FtcDriverStationActivityBase.this.onOptionsItemSelected(param2MenuItem);
                  }
                });
            FtcDriverStationActivityBase.this.onCreateOptionsMenu(popupMenu.getMenu());
            popupMenu.show();
          }
        });
    this.preferences.registerOnSharedPreferenceChangeListener(this);
    this.gamepadManager.open();
    BatteryChecker batteryChecker = new BatteryChecker(this, 300000L);
    this.batteryChecker = batteryChecker;
    batteryChecker.startBatteryMonitoring();
    resetBatteryStats();
    pingStatus(2131624391);
    this.mInputManager = (InputManager)getSystemService("input");
    this.networkConnectionHandler.pushNetworkConnectionCallback(this);
    this.networkConnectionHandler.pushReceiveLoopCallback(this);
    startOrRestartNetwork();
    DeviceNameManagerFactory.getInstance().registerCallback(this.deviceNameManagerCallback);
    ((WifiManager)AppUtil.getDefContext().getApplicationContext().getSystemService("wifi")).setWifiEnabled(true);
    WifiMuteStateMachine wifiMuteStateMachine = new WifiMuteStateMachine();
    this.wifiMuteStateMachine = wifiMuteStateMachine;
    wifiMuteStateMachine.initialize();
    this.wifiMuteStateMachine.start();
    this.wifiMuteStateMachine.registerCallback(this);
    this.processUserActivity = true;
    SoundPlayingRobotMonitor.prefillSoundCache();
    RobotLog.logBuildConfig(BuildConfig.class);
    RobotLog.logDeviceInfo();
    this.androidTextToSpeech.initialize();
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    getMenuInflater().inflate(2131492864, paramMenu);
    if (this.uiState == UIState.WAITING_FOR_START_EVENT && CameraStreamClient.getInstance().isStreamAvailable()) {
      paramMenu.findItem(2131230768).setVisible(true);
      return true;
    } 
    paramMenu.findItem(2131230768).setVisible(false);
    return true;
  }
  
  protected void onDestroy() {
    super.onDestroy();
    RobotLog.vv("DriverStation", "onDestroy()");
    this.androidTextToSpeech.close();
    this.gamepadManager.close();
    DeviceNameManagerFactory.getInstance().unregisterCallback(this.deviceNameManagerCallback);
    this.networkConnectionHandler.removeNetworkConnectionCallback(this);
    this.networkConnectionHandler.removeReceiveLoopCallback(this);
    shutdown();
    PreferenceRemoterDS.getInstance().stop(this.prefRemoterStartResult);
    DeviceNameManagerFactory.getInstance().stop(this.deviceNameManagerStartResult);
    RobotLog.cancelWriteLogcatToDisk();
  }
  
  public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    String str;
    StringBuilder stringBuilder1;
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("Received networkConnectionEvent: ");
    stringBuilder2.append(paramNetworkEvent.toString());
    RobotLog.i(stringBuilder2.toString());
    switch (paramNetworkEvent) {
      default:
        return callbackResult;
      case null:
        str = getString(2131624180, new Object[] { this.networkConnectionHandler.getFailureReason() });
        showWifiStatus(false, str);
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Network Connection - ");
        stringBuilder2.append(str);
        RobotLog.vv("DriverStation", stringBuilder2.toString());
        return callbackResult;
      case null:
        str = getString(2131624680);
        showWifiStatus(false, str);
        stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Network Connection - ");
        stringBuilder1.append(str);
        RobotLog.vv("DriverStation", stringBuilder1.toString());
        this.networkConnectionHandler.discoverPotentialConnections();
        assumeClientDisconnect();
        return CallbackResult.HANDLED;
      case null:
        showWifiStatus(true, getBestRobotControllerName());
        showWifiChannel();
        if (!NetworkConnection.isDeviceNameValid(this.networkConnectionHandler.getDeviceName())) {
          RobotLog.ee("DriverStation", "Wifi-Direct device name contains non-printable characters");
          ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_DEVICE_NAME_INVALID);
        } else if (this.networkConnectionHandler.connectedWithUnexpectedDevice()) {
          showWifiStatus(false, getString(2131624682));
          if (this.networkConnectionHandler.isWifiDirect()) {
            ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_FIX_CONFIG);
          } else {
            if (this.connectionOwner == null && this.connectionOwnerPassword == null) {
              showWifiStatus(false, getString(2131624683));
              return CallbackResult.HANDLED;
            } 
            this.networkConnectionHandler.startConnection(this.connectionOwner, this.connectionOwnerPassword);
          } 
          return CallbackResult.HANDLED;
        } 
        this.networkConnectionHandler.handleConnectionInfoAvailable();
        this.networkConnectionHandler.cancelConnectionSearch();
        assumeClientConnectAndRefreshUI(ControlPanelBack.NO_CHANGE);
        return CallbackResult.HANDLED;
      case null:
        showWifiStatus(false, getString(2131624678));
        return CallbackResult.HANDLED;
      case null:
        showWifiStatus(false, getString(2131624679));
        return CallbackResult.HANDLED;
      case null:
        RobotLog.ee("DriverStation", "Wifi Direct - connected as Group Owner, was expecting Peer");
        showWifiStatus(false, getString(2131624681));
        ConfigWifiDirectActivity.launch(getBaseContext(), ConfigWifiDirectActivity.Flag.WIFI_DIRECT_DEVICE_NAME_INVALID);
        return CallbackResult.HANDLED;
      case null:
        break;
    } 
    if (this.networkConnectionHandler.isWifiDirect()) {
      onPeersAvailableWifiDirect();
    } else {
      onPeersAvailableSoftAP();
    } 
    return CallbackResult.HANDLED;
  }
  
  public void onOpModeSelectionClick(OpModeMeta paramOpModeMeta) {
    handleOpModeQueued(paramOpModeMeta);
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    Iterator<ActivityManager.AppTask> iterator;
    EditParameters editParameters;
    Intent intent;
    this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.ACTIVITY_OTHER);
    this.wifiMuteStateMachine.maskEvent((Event)WifiMuteEvent.STOPPED_OPMODE);
    switch (paramMenuItem.getItemId()) {
      default:
        return super.onOptionsItemSelected(paramMenuItem);
      case 2131230784:
        startActivityForResult(new Intent(getBaseContext(), FtcDriverStationSettingsActivity.class), LaunchActivityConstantsList.RequestCode.SETTINGS_DRIVER_STATION.ordinal());
        return true;
      case 2131230783:
        this.networkConnectionHandler.sendCommand(new Command("CMD_RESTART_ROBOT"));
        this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.ACTIVITY_START);
        this.wifiMuteStateMachine.maskEvent((Event)WifiMuteEvent.STOPPED_OPMODE);
        return true;
      case 2131230782:
        RobotLog.vv("DriverStation", "action_program_and_manage clicked");
        this.networkConnectionHandler.sendCommand(new Command("CMD_START_DS_PROGRAM_AND_MANAGE"));
        return true;
      case 2131230776:
        startActivityForResult(new Intent(getBaseContext(), FtcDriverStationInspectionReportsActivity.class), LaunchActivityConstantsList.RequestCode.INSPECTIONS.ordinal());
        return true;
      case 2131230773:
        finishAffinity();
        iterator = ((ActivityManager)getSystemService("activity")).getAppTasks().iterator();
        while (iterator.hasNext())
          ((ActivityManager.AppTask)iterator.next()).finishAndRemoveTask(); 
        AppUtil.getInstance().exitApplication();
        return true;
      case 2131230769:
        editParameters = new EditParameters();
        intent = new Intent(AppUtil.getDefContext(), FtcLoadFileActivity.class);
        editParameters.putIntent(intent);
        startActivityForResult(intent, LaunchActivityConstantsList.RequestCode.CONFIGURE_DRIVER_STATION.ordinal());
        return true;
      case 2131230768:
        if (this.cameraStreamOpen) {
          hideCameraStream();
          return true;
        } 
        showCameraStream();
        return true;
      case 2131230760:
        break;
    } 
    startActivity(new Intent(AppUtil.getDefContext(), FtcAboutActivity.class));
    return true;
  }
  
  protected void onPause() {
    super.onPause();
    RobotLog.vv("DriverStation", "onPause()");
    this.gamepadManager.clearGamepadAssignments();
    this.gamepadManager.clearTrackedGamepads();
    this.mInputManager.unregisterInputDeviceListener(this.gamepadManager);
    initDefaultOpMode();
  }
  
  public void onPeerConnected() {
    RobotLog.vv("DriverStation", "robot controller connected");
    assumeClientConnectAndRefreshUI(ControlPanelBack.NO_CHANGE);
    PreferenceRemoterDS.getInstance().sendInformationalPrefsToRc();
  }
  
  public void onPeerDisconnected() {
    RobotLog.logStackTrace(new Throwable("Peer disconnected"));
    RobotLog.vv("DriverStation", "robot controller disconnected");
    assumeClientDisconnect();
  }
  
  public void onPendingCancel() {
    this.processUserActivity = true;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Pending Wifi Cancel: ");
    stringBuilder.append(this.queuedOpMode.name);
    RobotLog.ii("DriverStation", stringBuilder.toString());
  }
  
  public void onPendingOn() {
    this.processUserActivity = false;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Pending Wifi Off: ");
    stringBuilder.append(this.queuedOpMode.name);
    RobotLog.ii("DriverStation", stringBuilder.toString());
  }
  
  protected void onResume() {
    super.onResume();
    RobotLog.vv("DriverStation", "onResume()");
    this.disconnectFromPeerOnActivityStop = true;
    updateRcBatteryIndependence(this.preferences);
    resetBatteryStats();
    this.mInputManager.registerInputDeviceListener(this.gamepadManager, null);
    pingStatus(2131624391);
  }
  
  public void onSharedPreferenceChanged(SharedPreferences paramSharedPreferences, String paramString) {
    RobotLog.vv("DriverStation", "onSharedPreferenceChanged() pref=%s", new Object[] { paramString });
    if (paramString.equals(this.context.getString(2131624411))) {
      final String rcName = paramSharedPreferences.getString(paramString, "");
      if (str.length() > 0)
        runOnUiThread(new Runnable() {
              public void run() {
                if (FtcDriverStationActivityBase.this.textWifiDirectStatusShowingRC)
                  FtcDriverStationActivityBase.this.textWifiDirectStatus.setText(rcName); 
              }
            }); 
    } else if (paramString.equals(getString(2131624419))) {
      updateRcBatteryIndependence(this.preferences);
    } else if (!paramString.equals(getString(2131624397)) && paramString.equals("pref_wifip2p_channel")) {
      RobotLog.vv("DriverStation", "pref_wifip2p_channel changed.");
      showWifiChannel();
    } 
    updateLoggingPrefs();
  }
  
  protected void onStart() {
    super.onStart();
    RobotLog.onApplicationStart();
    RobotLog.vv("DriverStation", "onStart()");
    Iterator<GamepadIndicator> iterator = this.gamepadIndicators.values().iterator();
    while (iterator.hasNext())
      ((GamepadIndicator)iterator.next()).setState(GamepadIndicator.State.INVISIBLE); 
    this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.ACTIVITY_START);
    this.wifiMuteStateMachine.unMaskEvent((Event)WifiMuteEvent.STOPPED_OPMODE);
    FtcAboutActivity.setBuildTimeFromBuildConfig("2020-09-21T09:05:36.688-0700");
  }
  
  protected void onStop() {
    super.onStop();
    RobotLog.vv("DriverStation", "onStop()");
    pingStatus(2131624392);
    this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.ACTIVITY_STOP);
    if (this.disconnectFromPeerOnActivityStop) {
      RobotLog.ii("DriverStation", "App appears to be exiting. Destroying activity so that another DS can connect");
      finish();
    } 
  }
  
  public void onUserInteraction() {
    if (this.processUserActivity)
      this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.USER_ACTIVITY); 
  }
  
  public void onWifiOff() {
    this.queuedOpModeWhenMuted = this.queuedOpMode;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Wifi Off: ");
    stringBuilder.append(this.queuedOpMode.name);
    RobotLog.ii("DriverStation", stringBuilder.toString());
  }
  
  public void onWifiOn() {
    this.queuedOpMode = this.queuedOpModeWhenMuted;
    this.processUserActivity = true;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Wifi On: ");
    stringBuilder.append(this.queuedOpMode.name);
    RobotLog.ii("DriverStation", stringBuilder.toString());
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    super.onWindowFocusChanged(paramBoolean);
    if (paramBoolean) {
      this.immersion.hideSystemUI();
      getWindow().setFlags(134217728, 134217728);
    } 
  }
  
  public CallbackResult packetReceived(RobocolDatagram paramRobocolDatagram) throws RobotCoreException {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult peerDiscoveryEvent(RobocolDatagram paramRobocolDatagram) throws RobotCoreException {
    try {
      PeerDiscovery peerDiscovery = PeerDiscovery.forReceive();
      peerDiscovery.fromByteArray(paramRobocolDatagram.getData());
      if (peerDiscovery.getPeerType() == PeerDiscovery.PeerType.NOT_CONNECTED_DUE_TO_PREEXISTING_CONNECTION) {
        reportGlobalError(getString(2131624013), false);
        showRobotBatteryVoltage("$no$voltage$sensor$");
      } else {
        this.networkConnectionHandler.updateConnection(paramRobocolDatagram);
      } 
    } catch (RobotProtocolException robotProtocolException) {
      reportGlobalError(robotProtocolException.getMessage(), false);
      this.networkConnectionHandler.stopPeerDiscovery();
      RobotLog.setGlobalErrorMsgSticky(true);
      Thread.currentThread().interrupt();
      showRobotBatteryVoltage("$no$voltage$sensor$");
    } 
    return CallbackResult.HANDLED;
  }
  
  protected void pingStatus(int paramInt) {
    pingStatus(this.context.getString(paramInt));
  }
  
  protected abstract void pingStatus(String paramString);
  
  public CallbackResult reportGlobalError(String paramString, boolean paramBoolean) {
    if (!RobotLog.getGlobalErrorMsg().equals(paramString)) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("System telemetry error: ");
      stringBuilder1.append(paramString);
      RobotLog.ee("DriverStation", stringBuilder1.toString());
      RobotLog.clearGlobalErrorMsg();
      RobotLog.setGlobalErrorMsg(paramString);
    } 
    TextView textView = this.systemTelemetry;
    AppUtil.getInstance();
    setTextColor(textView, AppUtil.getColor(2131034272));
    setVisibility((View)this.systemTelemetry, 0);
    StringBuilder stringBuilder = new StringBuilder();
    RobotState robotState = this.robotState;
    if (robotState != null && robotState != RobotState.UNKNOWN)
      stringBuilder.append(String.format(getString(2131624182), new Object[] { this.robotState.toString((Context)this) })); 
    if (paramBoolean)
      stringBuilder.append(getString(2131624183)); 
    stringBuilder.append(String.format(getString(2131624180), new Object[] { paramString }));
    setTextView(this.systemTelemetry, stringBuilder.toString());
    stopTimerAndReset();
    uiRobotCantContinue();
    return CallbackResult.HANDLED;
  }
  
  protected void reportGlobalWarning(String paramString) {
    if (!RobotLog.getGlobalWarningMessage().equals(paramString)) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("System telemetry warning: ");
      stringBuilder1.append(paramString);
      RobotLog.ee("DriverStation", stringBuilder1.toString());
      RobotLog.clearGlobalWarningMsg();
      RobotLog.setGlobalWarningMessage(paramString);
    } 
    TextView textView = this.systemTelemetry;
    AppUtil.getInstance();
    setTextColor(textView, AppUtil.getColor(2131034274));
    setVisibility((View)this.systemTelemetry, 0);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(String.format(getString(2131624184), new Object[] { paramString }));
    setTextView(this.systemTelemetry, stringBuilder.toString());
  }
  
  protected void requestUIState() {
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_UI_STATE"));
  }
  
  protected void resetBatteryStats() {
    this.V12BatteryMin = Double.POSITIVE_INFINITY;
    this.V12BatteryMinString = "";
  }
  
  protected void runDefaultOpMode() {
    this.networkConnectionHandler.sendCommand(new Command("CMD_RUN_OP_MODE", this.defaultOpMode.name));
    this.wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.STOPPED_OPMODE);
  }
  
  protected void sendMatchNumber(int paramInt) {
    sendMatchNumber(String.valueOf(paramInt));
  }
  
  protected void sendMatchNumber(String paramString) {
    this.networkConnectionHandler.sendCommand(new Command("CMD_SET_MATCH_NUMBER", paramString));
  }
  
  protected void sendMatchNumberIfNecessary() {
    try {
      sendMatchNumber(getMatchNumber());
      return;
    } catch (NumberFormatException numberFormatException) {
      sendMatchNumber(0);
      return;
    } 
  }
  
  protected void setBG(final View view, final Drawable drawable) {
    runOnUiThread(new Runnable() {
          public void run() {
            view.setBackground(drawable);
          }
        });
  }
  
  protected void setBGColor(final View view, final int color) {
    runOnUiThread(new Runnable() {
          public void run() {
            view.setBackgroundColor(color);
          }
        });
  }
  
  protected void setBatteryIcon(final BatteryChecker.BatteryStatus status, final ImageView icon) {
    runOnUiThread(new Runnable() {
          public void run() {
            int i;
            if (status.percent <= 15.0D) {
              ImageView imageView1 = icon;
              if (status.isCharging) {
                i = 2131165329;
              } else {
                i = 2131165328;
              } 
              imageView1.setImageResource(i);
              icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034247), PorterDuff.Mode.MULTIPLY);
              return;
            } 
            if (status.percent > 15.0D && status.percent <= 45.0D) {
              ImageView imageView1 = icon;
              if (status.isCharging) {
                i = 2131165333;
              } else {
                i = 2131165332;
              } 
              imageView1.setImageResource(i);
              if (status.percent <= 30.0D) {
                icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034248), PorterDuff.Mode.MULTIPLY);
                return;
              } 
              icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034275), PorterDuff.Mode.MULTIPLY);
              return;
            } 
            if (status.percent > 45.0D && status.percent <= 65.0D) {
              ImageView imageView1 = icon;
              if (status.isCharging) {
                i = 2131165335;
              } else {
                i = 2131165334;
              } 
              imageView1.setImageResource(i);
              icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034275), PorterDuff.Mode.MULTIPLY);
              return;
            } 
            if (status.percent > 65.0D && status.percent <= 85.0D) {
              ImageView imageView1 = icon;
              if (status.isCharging) {
                i = 2131165337;
              } else {
                i = 2131165336;
              } 
              imageView1.setImageResource(i);
              icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034275), PorterDuff.Mode.MULTIPLY);
              return;
            } 
            ImageView imageView = icon;
            if (status.isCharging) {
              i = 2131165331;
            } else {
              i = 2131165330;
            } 
            imageView.setImageResource(i);
            icon.setColorFilter(FtcDriverStationActivityBase.this.getResources().getColor(2131034275), PorterDuff.Mode.MULTIPLY);
          }
        });
  }
  
  protected void setButtonText(final Button button, final String text) {
    runOnUiThread(new Runnable() {
          public void run() {
            button.setText(text);
          }
        });
  }
  
  protected boolean setClientConnected(boolean paramBoolean) {
    boolean bool = this.clientConnected;
    this.clientConnected = paramBoolean;
    this.preferencesHelper.writeBooleanPrefIfDifferent(getString(2131624448), paramBoolean);
    return bool;
  }
  
  protected void setControlPanelBack(ControlPanelBack paramControlPanelBack) {
    int i = null.$SwitchMap$com$qualcomm$ftcdriverstation$FtcDriverStationActivityBase$ControlPanelBack[paramControlPanelBack.ordinal()];
    if (i != 2) {
      if (i != 3)
        return; 
      brightenControlPanelBack();
      return;
    } 
    dimControlPanelBack();
  }
  
  protected void setEnabled(final View view, final boolean enabled) {
    runOnUiThread(new Runnable() {
          public void run() {
            view.setEnabled(enabled);
          }
        });
  }
  
  protected void setImageResource(final ImageButton button, final int resourceId) {
    runOnUiThread(new Runnable() {
          public void run() {
            button.setImageResource(resourceId);
          }
        });
  }
  
  protected void setOpacity(final View v, final float opacity) {
    runOnUiThread(new Runnable() {
          public void run() {
            v.setAlpha(opacity);
          }
        });
  }
  
  protected boolean setQueuedOpModeIfDifferent(String paramString) {
    return setQueuedOpModeIfDifferent(getOpModeMeta(paramString));
  }
  
  protected boolean setQueuedOpModeIfDifferent(OpModeMeta paramOpModeMeta) {
    if (!paramOpModeMeta.name.equals(this.queuedOpMode.name)) {
      this.queuedOpMode = paramOpModeMeta;
      showQueuedOpModeName();
      return true;
    } 
    return false;
  }
  
  protected void setRobotState(RobotState paramRobotState) {
    if (this.robotState != paramRobotState) {
      this.robotState = paramRobotState;
      if (paramRobotState == RobotState.STOPPED) {
        traceUiStateChange("ui:uiRobotStopped", UIState.ROBOT_STOPPED);
        disableAndDimOpModeMenu();
        disableOpModeControls();
        dimControlPanelBack();
      } 
      if (paramRobotState == RobotState.EMERGENCY_STOP) {
        WifiMuteStateMachine wifiMuteStateMachine = this.wifiMuteStateMachine;
        if (wifiMuteStateMachine != null)
          wifiMuteStateMachine.consumeEvent((Event)WifiMuteEvent.STOPPED_OPMODE); 
      } 
    } 
  }
  
  protected void setTextColor(final TextView textView, final int color) {
    runOnUiThread(new Runnable() {
          public void run() {
            textView.setTextColor(color);
          }
        });
  }
  
  protected void setTextView(final TextView textView, final CharSequence text) {
    runOnUiThread(new Runnable() {
          public void run() {
            textView.setText(text);
          }
        });
  }
  
  protected void setTimerButtonEnabled(boolean paramBoolean) {
    setEnabled(this.timerAndTimerSwitch, paramBoolean);
    setEnabled(findViewById(2131231152), paramBoolean);
    setEnabled(findViewById(2131231155), paramBoolean);
    setEnabled(findViewById(2131231160), paramBoolean);
    setEnabled(findViewById(2131231159), paramBoolean);
    setEnabled(findViewById(2131231158), paramBoolean);
  }
  
  protected void setUserTelemetry(String paramString) {
    int i = null.$SwitchMap$org$firstinspires$ftc$robotcore$external$Telemetry$DisplayFormat[this.telemetryMode.ordinal()];
    if (i != 1 && i != 2) {
      if (i != 3)
        return; 
      setTextView(this.textTelemetry, (CharSequence)Html.fromHtml(paramString.replace("\n", "<br>")));
      return;
    } 
    setTextView(this.textTelemetry, paramString);
  }
  
  protected void setVisibility(final View view, final int visibility) {
    runOnUiThread(new Runnable() {
          public void run() {
            view.setVisibility(visibility);
          }
        });
  }
  
  protected void showBytesPerSecond(final long bps) {
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityBase.this.textBytesPerSecond.setText(String.valueOf(bps));
          }
        });
  }
  
  protected void showCameraStream() {
    this.cameraStreamOpen = true;
    this.gamepadManager.setEnabled(false);
    setVisibility((View)this.cameraStreamLayout, 0);
    setVisibility(this.buttonStart, 4);
    this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_FRAME"));
    showToast(getString(2131624618));
  }
  
  protected void showOpModeDialog(List<OpModeMeta> paramList, int paramInt) {
    stopTimerPreservingRemainingTime();
    initDefaultOpMode();
    OpModeSelectionDialogFragment opModeSelectionDialogFragment = new OpModeSelectionDialogFragment();
    opModeSelectionDialogFragment.setOnSelectionDialogListener(this);
    opModeSelectionDialogFragment.setOpModes(paramList);
    opModeSelectionDialogFragment.setTitle(paramInt);
    opModeSelectionDialogFragment.show(getFragmentManager(), "op_mode_selection");
  }
  
  protected void showQueuedOpModeName() {
    showQueuedOpModeName(this.queuedOpMode);
  }
  
  protected void showQueuedOpModeName(OpModeMeta paramOpModeMeta) {
    if (isDefaultOpMode(paramOpModeMeta)) {
      setVisibility((View)this.currentOpModeName, 8);
      setVisibility(this.chooseOpModePrompt, 0);
      return;
    } 
    setTextView(this.currentOpModeName, paramOpModeMeta.name);
    setVisibility((View)this.currentOpModeName, 0);
    setVisibility(this.chooseOpModePrompt, 8);
  }
  
  protected void showRobotBatteryVoltage(String paramString) {
    RelativeLayout relativeLayout = (RelativeLayout)findViewById(2131231050);
    View view = findViewById(2131231042);
    TextView textView3 = (TextView)findViewById(2131231043);
    if (paramString.equals("$no$voltage$sensor$")) {
      setVisibility(view, 8);
      setVisibility((View)textView3, 0);
      resetBatteryStats();
      setBG((View)relativeLayout, findViewById(2131231037).getBackground());
      return;
    } 
    setVisibility(view, 0);
    setVisibility((View)textView3, 8);
    double d1 = Double.valueOf(paramString).doubleValue();
    if (d1 < this.V12BatteryMin) {
      this.V12BatteryMin = d1;
      this.V12BatteryMinString = paramString;
    } 
    TextView textView2 = this.robotBatteryTelemetry;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramString);
    stringBuilder2.append(" V");
    setTextView(textView2, stringBuilder2.toString());
    TextView textView1 = this.robotBatteryMinimum;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append("( ");
    stringBuilder1.append(this.V12BatteryMinString);
    stringBuilder1.append(" V )");
    setTextView(textView1, stringBuilder1.toString());
    double d2 = 10.0F;
    double d3 = 14.0F;
    setBGColor((View)relativeLayout, Color.HSVToColor(new float[] { (float)Range.scale(Range.clip(d1, d2, d3), d2, d3, 0.0F, 128.0F), 1.0F, 0.6F }));
  }
  
  public void showToast(String paramString) {
    this.appUtil.showToast(UILocation.ONLY_LOCAL, paramString);
  }
  
  protected void showWifiChannel() {
    runOnUiThread(new Runnable() {
          public void run() {
            if (FtcDriverStationActivityBase.this.networkConnectionHandler.getWifiChannel() > 0) {
              StringBuilder stringBuilder1 = new StringBuilder();
              stringBuilder1.append("ch ");
              stringBuilder1.append(FtcDriverStationActivityBase.this.networkConnectionHandler.getWifiChannel());
              String str1 = stringBuilder1.toString();
              FtcDriverStationActivityBase.this.textWifiChannel.setText(str1);
              FtcDriverStationActivityBase.this.textWifiChannel.setVisibility(0);
              return;
            } 
            int i = FtcDriverStationActivityBase.this.preferences.getInt(FtcDriverStationActivityBase.this.getString(2131624455), -1);
            if (i == -1) {
              RobotLog.vv("DriverStation", "pref_wifip2p_channel: showWifiChannel prefChannel not found");
              FtcDriverStationActivityBase.this.textWifiChannel.setVisibility(8);
              return;
            } 
            RobotLog.vv("DriverStation", "pref_wifip2p_channel: showWifiChannel prefChannel = %d", new Object[] { Integer.valueOf(i) });
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ch ");
            stringBuilder.append(Integer.toString(i));
            String str = stringBuilder.toString();
            FtcDriverStationActivityBase.this.textWifiChannel.setText(str);
            FtcDriverStationActivityBase.this.textWifiChannel.setVisibility(0);
          }
        });
  }
  
  protected void showWifiStatus(final boolean showingRCName, final String status) {
    runOnUiThread(new Runnable() {
          public void run() {
            FtcDriverStationActivityBase.this.textWifiDirectStatusShowingRC = showingRCName;
            FtcDriverStationActivityBase.this.textWifiDirectStatus.setText(status);
          }
        });
  }
  
  protected void shutdown() {
    this.networkConnectionHandler.stop();
    this.networkConnectionHandler.shutdown();
  }
  
  public void startActivity(Intent paramIntent, Bundle paramBundle) {
    this.disconnectFromPeerOnActivityStop = false;
    super.startActivity(paramIntent, paramBundle);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt, Bundle paramBundle) {
    this.disconnectFromPeerOnActivityStop = false;
    super.startActivityForResult(paramIntent, paramInt, paramBundle);
  }
  
  protected void startKeepAlives() {
    NetworkConnectionHandler networkConnectionHandler = this.networkConnectionHandler;
    if (networkConnectionHandler != null)
      networkConnectionHandler.startKeepAlives(); 
  }
  
  protected void startOrRestartNetwork() {
    RobotLog.vv("DriverStation", "startOrRestartNetwork()");
    assumeClientDisconnect();
    showWifiStatus(false, getString(2131624680));
    initializeNetwork();
  }
  
  protected void stopKeepAlives() {
    NetworkConnectionHandler networkConnectionHandler = this.networkConnectionHandler;
    if (networkConnectionHandler != null)
      networkConnectionHandler.stopKeepAlives(); 
  }
  
  void stopTimerAndReset() {
    this.opModeCountDown.stop();
    this.opModeCountDown.resetCountdown();
  }
  
  void stopTimerPreservingRemainingTime() {
    this.opModeCountDown.stopPreservingRemainingTime();
  }
  
  public abstract void subclassOnCreate();
  
  public CallbackResult telemetryEvent(RobocolDatagram paramRobocolDatagram) {
    try {
      Set set2;
      Set set1;
      TelemetryMessage telemetryMessage = new TelemetryMessage(paramRobocolDatagram.getData());
      if (telemetryMessage.getRobotState() != RobotState.UNKNOWN)
        setRobotState(telemetryMessage.getRobotState()); 
      Map map1 = telemetryMessage.getDataStrings();
      if (telemetryMessage.isSorted()) {
        set2 = new TreeSet(map1.keySet());
      } else {
        set2 = map1.keySet();
      } 
      Iterator<String> iterator1 = set2.iterator();
      String str2 = "";
      boolean bool;
      for (bool = false; iterator1.hasNext(); bool = true) {
        String str5 = iterator1.next();
        if (str5.equals("$Robot$Battery$Level$")) {
          showRobotBatteryVoltage((String)map1.get(str5));
          continue;
        } 
        String str4 = str2;
        if (str5.length() > 0) {
          str4 = str2;
          if (str5.charAt(0) != '\000') {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(str5);
            stringBuilder2.append(": ");
            str4 = stringBuilder2.toString();
          } 
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str4);
        stringBuilder1.append((String)map1.get(str5));
        stringBuilder1.append("\n");
        str2 = stringBuilder1.toString();
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append("\n");
      String str3 = stringBuilder.toString();
      Map map2 = telemetryMessage.getDataNumbers();
      if (telemetryMessage.isSorted()) {
        set1 = new TreeSet(map2.keySet());
      } else {
        set1 = map2.keySet();
      } 
      Iterator<String> iterator2 = set1.iterator();
      String str1 = str3;
      while (iterator2.hasNext()) {
        String str4;
        String str5 = iterator2.next();
        str3 = str1;
        if (str5.length() > 0) {
          str3 = str1;
          if (str5.charAt(0) != '\000') {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str1);
            stringBuilder2.append(str5);
            stringBuilder2.append(": ");
            str4 = stringBuilder2.toString();
          } 
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append(str4);
        stringBuilder1.append(map2.get(str5));
        stringBuilder1.append("\n");
        str1 = stringBuilder1.toString();
        bool = true;
      } 
      str3 = telemetryMessage.getTag();
      if (str3.equals("$System$None$")) {
        clearSystemTelemetry();
      } else if (str3.equals("$System$Error$")) {
        reportGlobalError((String)map1.get(str3), true);
      } else if (str3.equals("$System$Warning$")) {
        reportGlobalWarning((String)map1.get(str3));
      } else if (str3.equals("$RobotController$Battery$Status$")) {
        updateRcBatteryStatus(BatteryChecker.BatteryStatus.deserialize((String)map1.get(str3)));
      } else if (str3.equals("$Robot$Battery$Level$")) {
        showRobotBatteryVoltage((String)map1.get(str3));
      } else if (bool) {
        setUserTelemetry(str1);
      } 
      return CallbackResult.HANDLED;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStackTrace((Throwable)robotCoreException);
      return CallbackResult.HANDLED;
    } 
  }
  
  protected void traceUiStateChange(String paramString, UIState paramUIState) {
    RobotLog.vv("DriverStation", paramString);
    this.uiState = paramUIState;
    setTextView(this.textDsUiStateIndicator, paramUIState.indicator);
    invalidateOptionsMenu();
  }
  
  protected void uiRobotCantContinue() {
    traceUiStateChange("ui:uiRobotCantContinue", UIState.CANT_CONTINUE);
    disableAndDimOpModeMenu();
    disableOpModeControls();
    dimControlPanelBack();
  }
  
  protected void uiRobotControllerIsConnected(ControlPanelBack paramControlPanelBack) {
    traceUiStateChange("ui:uiRobotControllerIsConnected", UIState.CONNNECTED);
    enableAndBrightenForConnected(paramControlPanelBack);
    AppUtil.getInstance().dismissAllDialogs(UILocation.ONLY_LOCAL);
    AppUtil.getInstance().dismissProgress(UILocation.ONLY_LOCAL);
    setTextView(this.rcBatteryTelemetry, "");
    setTextView(this.robotBatteryTelemetry, "");
    showWifiChannel();
    hideCameraStream();
  }
  
  protected void uiRobotControllerIsDisconnected() {
    traceUiStateChange("ui:uiRobotControllerIsDisconnected", UIState.DISCONNECTED);
    dimAndDisableAllControls();
  }
  
  protected void uiWaitingForInitEvent() {
    traceUiStateChange("ui:uiWaitingForInitEvent", UIState.WAITING_FOR_INIT_EVENT);
    checkConnectedEnableBrighten(ControlPanelBack.BRIGHT);
    brightenControlPanelBack();
    showQueuedOpModeName();
    enableAndBrightenOpModeMenu();
    setEnabled(this.buttonInit, true);
    setVisibility(this.buttonInit, 0);
    setVisibility(this.buttonStart, 4);
    setVisibility((View)this.buttonStop, 4);
    setVisibility(this.buttonInitStop, 4);
    setTimerButtonEnabled(true);
    setVisibility(this.timerAndTimerSwitch, 0);
    hideCameraStream();
  }
  
  protected void uiWaitingForOpModeSelection() {
    traceUiStateChange("ui:uiWaitingForOpModeSelection", UIState.WAITING_FOR_OPMODE_SELECTION);
    checkConnectedEnableBrighten(ControlPanelBack.DIM);
    dimControlPanelBack();
    enableAndBrightenOpModeMenu();
    showQueuedOpModeName();
    disableOpModeControls();
  }
  
  protected void uiWaitingForStartEvent() {
    traceUiStateChange("ui:uiWaitingForStartEvent", UIState.WAITING_FOR_START_EVENT);
    checkConnectedEnableBrighten(ControlPanelBack.BRIGHT);
    showQueuedOpModeName();
    enableAndBrightenOpModeMenu();
    setVisibility(this.buttonStart, 0);
    setVisibility(this.buttonInit, 4);
    setVisibility((View)this.buttonStop, 4);
    setVisibility(this.buttonInitStop, 0);
    setTimerButtonEnabled(true);
    setVisibility(this.timerAndTimerSwitch, 0);
    hideCameraStream();
  }
  
  protected void uiWaitingForStopEvent() {
    traceUiStateChange("ui:uiWaitingForStopEvent", UIState.WAITING_FOR_STOP_EVENT);
    checkConnectedEnableBrighten(ControlPanelBack.BRIGHT);
    showQueuedOpModeName();
    enableAndBrightenOpModeMenu();
    setVisibility((View)this.buttonStop, 0);
    setVisibility(this.buttonInit, 4);
    setVisibility(this.buttonStart, 4);
    setVisibility(this.buttonInitStop, 4);
    setTimerButtonEnabled(false);
    setVisibility(this.timerAndTimerSwitch, 0);
    hideCameraStream();
  }
  
  protected void updateLoggingPrefs() {
    boolean bool = this.preferences.getBoolean(getString(2131624406), false);
    this.debugLogging = bool;
    this.gamepadManager.setDebug(bool);
    if (this.preferences.getBoolean(getString(2131624434), false)) {
      enableMatchLoggingUI();
      return;
    } 
    disableMatchLoggingUI();
  }
  
  protected abstract void updateRcBatteryStatus(BatteryChecker.BatteryStatus paramBatteryStatus);
  
  protected int validateMatchEntry(String paramString) {
    try {
      int i = Integer.parseInt(paramString);
      if (i >= 0 && i <= 1000)
        return i; 
    } catch (NumberFormatException numberFormatException) {
      RobotLog.logStackTrace(numberFormatException);
    } 
    return -1;
  }
  
  protected enum ControlPanelBack {
    BRIGHT, DIM, NO_CHANGE;
    
    static {
      ControlPanelBack controlPanelBack = new ControlPanelBack("BRIGHT", 2);
      BRIGHT = controlPanelBack;
      $VALUES = new ControlPanelBack[] { NO_CHANGE, DIM, controlPanelBack };
    }
  }
  
  protected class DeviceNameManagerCallback implements DeviceNameListener {
    public void onDeviceNameChanged(String param1String) {
      FtcDriverStationActivityBase.this.displayDeviceName(param1String);
    }
  }
  
  private class OpModeCountDownTimer {
    public static final long MS_COUNTDOWN_INTERVAL = 30000L;
    
    public static final long MS_PER_S = 1000L;
    
    public static final long MS_TICK = 1000L;
    
    public static final long TICK_INTERVAL = 1L;
    
    private CountDownTimer countDownTimer = null;
    
    private boolean enabled = false;
    
    private long msRemaining = 30000L;
    
    private View timerStopWatch = FtcDriverStationActivityBase.this.findViewById(2131231155);
    
    private View timerSwitchOff = FtcDriverStationActivityBase.this.findViewById(2131231158);
    
    private View timerSwitchOn = FtcDriverStationActivityBase.this.findViewById(2131231159);
    
    private TextView timerText = (TextView)FtcDriverStationActivityBase.this.findViewById(2131231160);
    
    private void displaySecondsRemaining(long param1Long) {
      if (this.enabled)
        FtcDriverStationActivityBase.this.setTextView(this.timerText, String.valueOf(param1Long)); 
    }
    
    public void disable() {
      FtcDriverStationActivityBase.this.setTextView(this.timerText, "");
      FtcDriverStationActivityBase.this.setVisibility((View)this.timerText, 8);
      FtcDriverStationActivityBase.this.setVisibility(this.timerStopWatch, 0);
      FtcDriverStationActivityBase.this.setVisibility(this.timerSwitchOn, 8);
      FtcDriverStationActivityBase.this.setVisibility(this.timerSwitchOff, 0);
      this.enabled = false;
    }
    
    public void enable() {
      if (!this.enabled) {
        FtcDriverStationActivityBase.this.setVisibility((View)this.timerText, 0);
        FtcDriverStationActivityBase.this.setVisibility(this.timerStopWatch, 8);
        FtcDriverStationActivityBase.this.setVisibility(this.timerSwitchOn, 0);
        FtcDriverStationActivityBase.this.setVisibility(this.timerSwitchOff, 8);
        this.enabled = true;
        displaySecondsRemaining(getSecondsRemaining());
      } 
    }
    
    public long getSecondsRemaining() {
      return this.msRemaining / 1000L;
    }
    
    public boolean isEnabled() {
      return this.enabled;
    }
    
    public void resetCountdown() {
      setMsRemaining(30000L);
    }
    
    public void setMsRemaining(long param1Long) {
      this.msRemaining = param1Long;
      if (this.enabled)
        displaySecondsRemaining(param1Long / 1000L); 
    }
    
    public void start() {
      if (this.enabled) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Starting to run current op mode for ");
        stringBuilder.append(getSecondsRemaining());
        stringBuilder.append(" seconds");
        RobotLog.vv("DriverStation", stringBuilder.toString());
        FtcDriverStationActivityBase.this.appUtil.synchronousRunOnUiThread(new Runnable() {
              public void run() {
                CountDownTimer countDownTimer = FtcDriverStationActivityBase.OpModeCountDownTimer.this.countDownTimer;
                if (countDownTimer != null)
                  countDownTimer.cancel(); 
                FtcDriverStationActivityBase.OpModeCountDownTimer.access$002(FtcDriverStationActivityBase.OpModeCountDownTimer.this, (new CountDownTimer(FtcDriverStationActivityBase.OpModeCountDownTimer.this.msRemaining, 1000L) {
                      public void onFinish() {
                        FtcDriverStationActivityBase.this.assertUiThread();
                        RobotLog.vv("DriverStation", "Stopping current op mode, timer expired");
                        FtcDriverStationActivityBase.OpModeCountDownTimer.this.resetCountdown();
                        FtcDriverStationActivityBase.this.handleOpModeStop();
                      }
                      
                      public void onTick(long param3Long) {
                        FtcDriverStationActivityBase.this.assertUiThread();
                        FtcDriverStationActivityBase.OpModeCountDownTimer.this.setMsRemaining(param3Long);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Running current op mode for ");
                        stringBuilder.append(param3Long / 1000L);
                        stringBuilder.append(" seconds");
                        RobotLog.vv("DriverStation", stringBuilder.toString());
                      }
                    }).start());
              }
            });
      } 
    }
    
    public void stop() {
      FtcDriverStationActivityBase.this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              if (FtcDriverStationActivityBase.OpModeCountDownTimer.this.countDownTimer != null) {
                FtcDriverStationActivityBase.OpModeCountDownTimer.this.countDownTimer.cancel();
                FtcDriverStationActivityBase.OpModeCountDownTimer.access$002(FtcDriverStationActivityBase.OpModeCountDownTimer.this, null);
              } 
            }
          });
    }
    
    public void stopPreservingRemainingTime() {
      // Byte code:
      //   0: aload_0
      //   1: getfield countDownTimer : Landroid/os/CountDownTimer;
      //   4: astore_3
      //   5: aload_0
      //   6: getfield msRemaining : J
      //   9: lstore_1
      //   10: aload_3
      //   11: ifnull -> 33
      //   14: aload_3
      //   15: monitorenter
      //   16: aload_0
      //   17: getfield msRemaining : J
      //   20: lstore_1
      //   21: aload_3
      //   22: monitorexit
      //   23: goto -> 33
      //   26: astore #4
      //   28: aload_3
      //   29: monitorexit
      //   30: aload #4
      //   32: athrow
      //   33: aload_0
      //   34: invokevirtual stop : ()V
      //   37: aload_0
      //   38: lload_1
      //   39: invokevirtual setMsRemaining : (J)V
      //   42: return
      // Exception table:
      //   from	to	target	type
      //   16	23	26	finally
      //   28	30	26	finally
    }
  }
  
  class null implements Runnable {
    public void run() {
      CountDownTimer countDownTimer = this.this$1.countDownTimer;
      if (countDownTimer != null)
        countDownTimer.cancel(); 
      FtcDriverStationActivityBase.OpModeCountDownTimer.access$002(this.this$1, (new CountDownTimer(this.this$1.msRemaining, 1000L) {
            public void onFinish() {
              FtcDriverStationActivityBase.this.assertUiThread();
              RobotLog.vv("DriverStation", "Stopping current op mode, timer expired");
              this.this$2.this$1.resetCountdown();
              FtcDriverStationActivityBase.this.handleOpModeStop();
            }
            
            public void onTick(long param3Long) {
              FtcDriverStationActivityBase.this.assertUiThread();
              this.this$2.this$1.setMsRemaining(param3Long);
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("Running current op mode for ");
              stringBuilder.append(param3Long / 1000L);
              stringBuilder.append(" seconds");
              RobotLog.vv("DriverStation", stringBuilder.toString());
            }
          }).start());
    }
  }
  
  class null extends CountDownTimer {
    public void onFinish() {
      FtcDriverStationActivityBase.this.assertUiThread();
      RobotLog.vv("DriverStation", "Stopping current op mode, timer expired");
      this.this$2.this$1.resetCountdown();
      FtcDriverStationActivityBase.this.handleOpModeStop();
    }
    
    public void onTick(long param1Long) {
      FtcDriverStationActivityBase.this.assertUiThread();
      this.this$2.this$1.setMsRemaining(param1Long);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Running current op mode for ");
      stringBuilder.append(param1Long / 1000L);
      stringBuilder.append(" seconds");
      RobotLog.vv("DriverStation", stringBuilder.toString());
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (this.this$1.countDownTimer != null) {
        this.this$1.countDownTimer.cancel();
        FtcDriverStationActivityBase.OpModeCountDownTimer.access$002(this.this$1, null);
      } 
    }
  }
  
  protected enum UIState {
    CANT_CONTINUE,
    CONNNECTED,
    DISCONNECTED,
    ROBOT_STOPPED,
    UNKNOWN("U"),
    WAITING_FOR_ACK("U"),
    WAITING_FOR_INIT_EVENT("U"),
    WAITING_FOR_OPMODE_SELECTION("U"),
    WAITING_FOR_START_EVENT("U"),
    WAITING_FOR_STOP_EVENT("U");
    
    public final String indicator;
    
    static {
      CONNNECTED = new UIState("CONNNECTED", 3, "C");
      WAITING_FOR_OPMODE_SELECTION = new UIState("WAITING_FOR_OPMODE_SELECTION", 4, "M");
      WAITING_FOR_INIT_EVENT = new UIState("WAITING_FOR_INIT_EVENT", 5, "K");
      WAITING_FOR_ACK = new UIState("WAITING_FOR_ACK", 6, "KW");
      WAITING_FOR_START_EVENT = new UIState("WAITING_FOR_START_EVENT", 7, "S");
      WAITING_FOR_STOP_EVENT = new UIState("WAITING_FOR_STOP_EVENT", 8, "P");
      UIState uIState = new UIState("ROBOT_STOPPED", 9, "Z");
      ROBOT_STOPPED = uIState;
      $VALUES = new UIState[] { UNKNOWN, CANT_CONTINUE, DISCONNECTED, CONNNECTED, WAITING_FOR_OPMODE_SELECTION, WAITING_FOR_INIT_EVENT, WAITING_FOR_ACK, WAITING_FOR_START_EVENT, WAITING_FOR_STOP_EVENT, uIState };
    }
    
    UIState(String param1String1) {
      this.indicator = param1String1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftcdriverstation\FtcDriverStationActivityBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */