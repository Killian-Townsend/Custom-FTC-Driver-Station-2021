package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.exception.DuplicateNameException;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxUsbDeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.xmlpull.v1.XmlPullParser;

public class FtcConfigurationActivity extends EditActivity implements RecvLoopRunnable.RecvLoopCallback, NetworkConnection.NetworkConnectionCallback {
  protected static final boolean DEBUG = false;
  
  public static final String TAG = "FtcConfigTag";
  
  public static final RequestCode requestCode = RequestCode.EDIT_FILE;
  
  DialogInterface.OnClickListener doNothingAndCloseListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
    };
  
  protected Semaphore feedbackPosted = new Semaphore(0);
  
  protected int idFeedbackAnchor = R.id.feedbackAnchor;
  
  protected long msSaveSplashDelay = 1000L;
  
  protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  protected final Object robotConfigMapLock = new Object();
  
  protected ThreadPool.Singleton scanButtonSingleton = new ThreadPool.Singleton();
  
  protected USBScanManager usbScanManager = null;
  
  private void buildControllersFromXMLResults(List<ControllerConfiguration> paramList) {
    synchronized (this.robotConfigMapLock) {
      this.robotConfigMap = new RobotConfigMap(paramList);
      return;
    } 
  }
  
  private RobotConfigMap buildRobotConfigMapFromScanned(RobotConfigMap paramRobotConfigMap, ScannedDevices paramScannedDevices) {
    RobotConfigMap robotConfigMap = new RobotConfigMap();
    this.configurationUtility.resetNameUniquifiers();
    for (Map.Entry entry : paramScannedDevices.entrySet()) {
      ControllerConfiguration controllerConfiguration;
      SerialNumber serialNumber = (SerialNumber)entry.getKey();
      if (carryOver(serialNumber, paramRobotConfigMap)) {
        RobotLog.vv("FtcConfigTag", "carrying over %s", new Object[] { serialNumber });
        controllerConfiguration = paramRobotConfigMap.get(serialNumber);
      } else {
        controllerConfiguration = this.configurationUtility.buildNewControllerConfiguration(serialNumber, (DeviceManager.UsbDeviceType)controllerConfiguration.getValue(), this.usbScanManager.getLynxModuleMetaListSupplier(serialNumber));
      } 
      if (controllerConfiguration != null) {
        controllerConfiguration.setKnownToBeAttached(true);
        robotConfigMap.put(serialNumber, controllerConfiguration);
      } 
    } 
    return robotConfigMap;
  }
  
  private void buildRobotConfigMapFromScanned(ScannedDevices paramScannedDevices) {
    synchronized (this.robotConfigMapLock) {
      this.robotConfigMap = buildRobotConfigMapFromScanned(getRobotConfigMap(), paramScannedDevices);
      return;
    } 
  }
  
  private boolean carryOver(SerialNumber paramSerialNumber, RobotConfigMap paramRobotConfigMap) {
    if (paramRobotConfigMap == null)
      return false; 
    if (!paramRobotConfigMap.contains(paramSerialNumber))
      return false; 
    if (paramRobotConfigMap.get(paramSerialNumber).isSystemSynthetic()) {
      RobotLog.vv("FtcConfigTag", "not carrying over synthetic controller: serial=%s", new Object[] { paramSerialNumber });
      return false;
    } 
    return true;
  }
  
  private void clearDuplicateWarning() {
    LinearLayout linearLayout = (LinearLayout)findViewById(R.id.feedbackAnchorDuplicateNames);
    linearLayout.removeAllViews();
    linearLayout.setVisibility(8);
  }
  
  private void confirmSave() {
    Toast toast = Toast.makeText((Context)this, R.string.toastSaved, 0);
    toast.setGravity(80, 0, 50);
    toast.show();
  }
  
  private void doBackOrCancel() {
    if (this.currentCfgFile.isDirty()) {
      DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            FtcConfigurationActivity.this.currentCfgFile.markClean();
            FtcConfigurationActivity.this.robotConfigFileManager.setActiveConfig(FtcConfigurationActivity.this.remoteConfigure, FtcConfigurationActivity.this.currentCfgFile);
            FtcConfigurationActivity.this.finishCancel();
          }
        };
      AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.saveChangesTitle), getString(R.string.saveChangesMessage));
      builder.setPositiveButton(R.string.buttonExitWithoutSaving, onClickListener);
      builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
      builder.show();
      return;
    } 
    finishCancel();
  }
  
  private void pauseAfterSave() {
    try {
      Thread.sleep(this.msSaveSplashDelay);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  private void populateList() {
    ListView listView = (ListView)findViewById(R.id.controllersList);
    try {
      this.scannedDevices = this.usbScanManager.awaitScannedDevices();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    tellControllersAboutAttachment();
    listView.setAdapter(new DeviceInfoAdapter(this, 17367044, new LinkedList<ControllerConfiguration>(getRobotConfigMap().controllerConfigurations())));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            EditParameters editParameters;
            ControllerConfiguration controllerConfiguration = (ControllerConfiguration)param1AdapterView.getItemAtPosition(param1Int);
            ConfigurationType configurationType = controllerConfiguration.getConfigurationType();
            if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
              editParameters = FtcConfigurationActivity.this.initParameters(1, DeviceConfiguration.class, controllerConfiguration, ((MotorControllerConfiguration)controllerConfiguration).getMotors());
              FtcConfigurationActivity.this.handleLaunchEdit(EditMotorControllerActivity.requestCode, EditMotorControllerActivity.class, editParameters);
              return;
            } 
            if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
              editParameters = FtcConfigurationActivity.this.initParameters(1, DeviceConfiguration.class, (ControllerConfiguration)editParameters, ((ServoControllerConfiguration)editParameters).getServos());
              editParameters.setControlSystem(ControlSystem.MODERN_ROBOTICS);
              FtcConfigurationActivity.this.handleLaunchEdit(EditServoControllerActivity.requestCode, EditServoControllerActivity.class, editParameters);
              return;
            } 
            if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
              editParameters = FtcConfigurationActivity.this.initParameters(0, DeviceConfiguration.class, (ControllerConfiguration)editParameters, ((LegacyModuleControllerConfiguration)editParameters).getDevices());
              FtcConfigurationActivity.this.handleLaunchEdit(EditLegacyModuleControllerActivity.requestCode, EditLegacyModuleControllerActivity.class, editParameters);
              return;
            } 
            if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
              editParameters = FtcConfigurationActivity.this.initParameters(0, DeviceConfiguration.class, (ControllerConfiguration)editParameters, ((DeviceInterfaceModuleConfiguration)editParameters).getDevices());
              FtcConfigurationActivity.this.handleLaunchEdit(EditDeviceInterfaceModuleActivity.requestCode, EditDeviceInterfaceModuleActivity.class, editParameters);
              return;
            } 
            if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
              editParameters = FtcConfigurationActivity.this.initParameters(0, LynxModuleConfiguration.class, (ControllerConfiguration)editParameters, ((LynxUsbDeviceConfiguration)editParameters).getDevices());
              FtcConfigurationActivity.this.handleLaunchEdit(EditLynxUsbDeviceActivity.requestCode, EditLynxUsbDeviceActivity.class, editParameters);
              return;
            } 
            if (configurationType == BuiltInConfigurationType.WEBCAM) {
              editParameters = FtcConfigurationActivity.this.initParameters((ControllerConfiguration)editParameters);
              FtcConfigurationActivity.this.handleLaunchEdit(EditWebcamActivity.requestCode, EditWebcamActivity.class, editParameters);
            } 
          }
        });
  }
  
  private void populateListAndWarnDevices() {
    this.appUtil.runOnUiThread(new Runnable() {
          public void run() {
            FtcConfigurationActivity.this.populateList();
            FtcConfigurationActivity.this.warnIncompleteDevices();
          }
        });
  }
  
  private void readFile() {
    try {
      XmlPullParser xmlPullParser = this.currentCfgFile.getXml();
      if (xmlPullParser != null) {
        buildControllersFromXMLResults((new ReadXMLFileHandler()).parse(xmlPullParser));
        populateListAndWarnDevices();
        return;
      } 
      throw new RobotCoreException("can't access configuration");
    } catch (Exception exception) {
      String str = String.format(getString(R.string.errorParsingConfiguration), new Object[] { this.currentCfgFile.getName() });
      RobotLog.ee("FtcConfigTag", exception, str);
      this.appUtil.showToast(UILocation.ONLY_LOCAL, str);
      return;
    } 
  }
  
  private void startExecutorService() throws RobotCoreException {
    USBScanManager uSBScanManager = new USBScanManager((Context)this, this.remoteConfigure);
    this.usbScanManager = uSBScanManager;
    uSBScanManager.startExecutorService();
    this.scanButtonSingleton.reset();
    this.scanButtonSingleton.setService(this.usbScanManager.getExecutorService());
    this.usbScanManager.startDeviceScanIfNecessary();
  }
  
  private void stopExecutorService() {
    this.usbScanManager.stopExecutorService();
    this.usbScanManager = null;
  }
  
  private void synchronouslySetFeedbackWhile(final String title, final String message, Runnable paramRunnable) {
    final CharSequence[] prev = this.utility.getFeedbackText(this.idFeedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
    try {
      this.appUtil.synchronousRunOnUiThread(new Runnable() {
            public void run() {
              FtcConfigurationActivity.this.utility.setFeedbackText(title, message, FtcConfigurationActivity.this.idFeedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
              FtcConfigurationActivity.this.feedbackPosted.release();
            }
          });
      try {
        this.feedbackPosted.acquire();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
      paramRunnable.run();
      return;
    } finally {
      this.appUtil.runOnUiThread(new Runnable() {
            public void run() {
              if (prev != null) {
                FtcConfigurationActivity.this.utility.setFeedbackText(prev, FtcConfigurationActivity.this.idFeedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
                return;
              } 
              FtcConfigurationActivity.this.utility.hideFeedbackText(FtcConfigurationActivity.this.idFeedbackAnchor);
            }
          });
    } 
  }
  
  private void warnDuplicateNames(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Found ");
    stringBuilder.append(paramString);
    paramString = stringBuilder.toString();
    this.utility.setFeedbackText(paramString, "Please fix and re-save.", R.id.feedbackAnchorDuplicateNames, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
  }
  
  private void warnIncompleteDevices() {
    String str1 = this.scannedDevices.getErrorMessage();
    String str2 = null;
    if (str1 != null) {
      str2 = getString(R.string.errorScanningDevicesTitle);
      str1 = this.scannedDevices.getErrorMessage();
    } else if (!getRobotConfigMap().allControllersAreBound()) {
      str2 = getString(R.string.notAllDevicesFoundTitle);
      str1 = Misc.formatForUser(R.string.notAllDevicesFoundMessage, new Object[] { getString(R.string.noSerialNumber) });
    } else if (getRobotConfigMap().size() == 0) {
      str2 = getString(R.string.noDevicesFoundTitle);
      str1 = getString(R.string.noDevicesFoundMessage);
      clearDuplicateWarning();
    } else {
      str1 = null;
    } 
    if (str2 != null || str1 != null) {
      if (str2 == null)
        str2 = ""; 
      if (str1 == null)
        str1 = ""; 
      this.utility.setFeedbackText(str2, str1, this.idFeedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1, R.id.feedbackOKButton);
      return;
    } 
    this.utility.hideFeedbackText(this.idFeedbackAnchor);
  }
  
  public CallbackResult commandEvent(Command paramCommand) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    try {
      CallbackResult callbackResult1;
      String str2 = paramCommand.getName();
      String str1 = paramCommand.getExtra();
      if (str2.equals("CMD_SCAN_RESP")) {
        callbackResult1 = handleCommandScanResp(str1);
      } else if (str2.equals("CMD_DISCOVER_LYNX_MODULES_RESP")) {
        callbackResult1 = handleCommandDiscoverLynxModulesResp((String)callbackResult1);
      } else if (str2.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP")) {
        callbackResult1 = handleCommandRequestParticularConfigurationResp((String)callbackResult1);
      } else {
        return callbackResult;
      } 
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStacktrace((Throwable)robotCoreException);
      return callbackResult;
    } 
    return (CallbackResult)robotCoreException;
  }
  
  void dirtyCheckThenSingletonUSBScanAndUpdateUI(final boolean showFeedback) {
    DialogInterface.OnClickListener onClickListener;
    final Runnable runnable = new Runnable() {
        public void run() {
          ThreadPool.logThreadLifeCycle("USB bus scan handler", new Runnable() {
                public void run() {
                  if (showFeedback) {
                    FtcConfigurationActivity.this.synchronouslySetFeedbackWhile(FtcConfigurationActivity.this.getString(R.string.ftcConfigScanning), "", new Runnable() {
                          public void run() {
                            FtcConfigurationActivity.this.doUSBScanAndUpdateUI();
                          }
                        });
                    return;
                  } 
                  FtcConfigurationActivity.this.doUSBScanAndUpdateUI();
                }
              });
        }
      };
    if (this.currentCfgFile.isDirty()) {
      AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleUnsavedChanges), getString(R.string.msgAlertBeforeScan));
      onClickListener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            FtcConfigurationActivity.this.scanButtonSingleton.submit(ThreadPool.Singleton.INFINITE_TIMEOUT, runnable);
          }
        };
      builder.setPositiveButton(R.string.buttonNameOK, onClickListener);
      builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
      builder.show();
      return;
    } 
    this.scanButtonSingleton.submit(ThreadPool.Singleton.INFINITE_TIMEOUT, (Runnable)onClickListener);
  }
  
  protected void doUSBScanAndUpdateUI() {
    RobotLog.vv("FtcConfigTag", "doUSBScanAndUpdateUI()...");
    try {
      ScannedDevices scannedDevices = (ScannedDevices)this.usbScanManager.startDeviceScanIfNecessary().await();
    } catch (InterruptedException interruptedException) {
    
    } finally {
      RobotLog.vv("FtcConfigTag", "...doUSBScanAndUpdateUI()");
    } 
    RobotLog.vv("FtcConfigTag", "...doUSBScanAndUpdateUI()");
  }
  
  public CallbackResult emptyEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void ensureConfigFileIsFresh() {
    if (this.haveRobotConfigMapParameter) {
      populateListAndWarnDevices();
      return;
    } 
    if (this.remoteConfigure) {
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION", this.currentCfgFile.toString()));
      return;
    } 
    readFile();
  }
  
  public CallbackResult gamepadEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected RobotConfigMap getRobotConfigMap() {
    synchronized (this.robotConfigMapLock) {
      return super.getRobotConfigMap();
    } 
  }
  
  public String getTag() {
    return "FtcConfigTag";
  }
  
  protected CallbackResult handleCommandDiscoverLynxModulesResp(String paramString) throws RobotCoreException {
    USBScanManager uSBScanManager = this.usbScanManager;
    if (uSBScanManager != null)
      uSBScanManager.handleCommandDiscoverLynxModulesResponse(paramString); 
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandRequestParticularConfigurationResp(String paramString) throws RobotCoreException {
    buildControllersFromXMLResults((new ReadXMLFileHandler()).parse(new StringReader(paramString)));
    populateListAndWarnDevices();
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandScanResp(String paramString) throws RobotCoreException {
    USBScanManager uSBScanManager = this.usbScanManager;
    if (uSBScanManager != null) {
      uSBScanManager.handleCommandScanResponse(paramString);
      populateListAndWarnDevices();
    } 
    return CallbackResult.HANDLED_CONTINUE;
  }
  
  public CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong) {
    return CallbackResult.NOT_HANDLED;
  }
  
  <ITEM_T extends DeviceConfiguration> EditParameters initParameters(int paramInt, Class<ITEM_T> paramClass, ControllerConfiguration paramControllerConfiguration, List<ITEM_T> paramList) {
    EditParameters<ITEM_T> editParameters = new EditParameters<ITEM_T>(this, (DeviceConfiguration)paramControllerConfiguration, paramClass, paramList);
    editParameters.setInitialPortNumber(paramInt);
    editParameters.setScannedDevices(this.scannedDevices);
    editParameters.setRobotConfigMap(getRobotConfigMap());
    return editParameters;
  }
  
  <ITEM_T extends DeviceConfiguration> EditParameters initParameters(ControllerConfiguration paramControllerConfiguration) {
    return initParameters(0, DeviceConfiguration.class, paramControllerConfiguration, new ArrayList<DeviceConfiguration>());
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    try {
      logActivityResult(paramInt1, paramInt2, paramIntent);
      if (paramInt2 == 0)
        return; 
      RequestCode requestCode = RequestCode.fromValue(paramInt1);
      null = EditParameters.fromIntent(this, paramIntent);
      RobotLog.vv("FtcConfigTag", "onActivityResult(%s)", new Object[] { requestCode.toString() });
      synchronized (this.robotConfigMapLock) {
        deserializeConfigMap(null);
        this.scannedDevices = this.usbScanManager.awaitScannedDevices();
        this.appUtil.runOnUiThread(new Runnable() {
              public void run() {
                FtcConfigurationActivity.this.currentCfgFile.markDirty();
                FtcConfigurationActivity.this.robotConfigFileManager.updateActiveConfigHeader(FtcConfigurationActivity.this.currentCfgFile);
                FtcConfigurationActivity.this.populateList();
              }
            });
        return;
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public void onBackPressed() {
    RobotLog.vv("FtcConfigTag", "onBackPressed()");
    doBackOrCancel();
  }
  
  public void onCancelButtonPressed(View paramView) {
    RobotLog.vv("FtcConfigTag", "onCancelButtonPressed()");
    doBackOrCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    RobotLog.vv("FtcConfigTag", "onCreate()");
    setContentView(R.layout.activity_ftc_configuration);
    try {
      deserialize(EditParameters.fromIntent(this, getIntent()));
      ((Button)findViewById(R.id.scanButton)).setVisibility(0);
      ((Button)findViewById(R.id.doneButton)).setText(R.string.buttonNameSave);
      startExecutorService();
      return;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.ee("FtcConfigTag", "exception thrown during FtcConfigurationActivity.onCreate()");
      finishCancel();
      return;
    } 
  }
  
  protected void onDestroy() {
    RobotLog.vv("FtcConfigTag", "FtcConfigurationActivity.onDestroy()");
    super.onDestroy();
    stopExecutorService();
  }
  
  public void onDevicesInfoButtonPressed(View paramView) {
    RobotLog.vv("FtcConfigTag", "onDevicesInfoButtonPressed()");
    AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleDevices), getString(R.string.msgInfoHowToUse));
    builder.setPositiveButton(getString(R.string.buttonNameOK), this.doNothingAndCloseListener);
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
    ((TextView)alertDialog.findViewById(16908299)).setTextSize(14.0F);
  }
  
  public void onDoneButtonPressed(View paramView) {
    String str1;
    RobotLog.vv("FtcConfigTag", "onDoneButtonPressed()");
    final String data = this.robotConfigFileManager.toXml(getRobotConfigMap());
    if (str2 == null)
      return; 
    String str3 = getString(R.string.configNamePromptBanter);
    final EditText input = new EditText((Context)this);
    if (this.currentCfgFile.isNoConfig()) {
      str1 = "";
    } else {
      str1 = this.currentCfgFile.getName();
    } 
    editText.setText(str1);
    AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.configNamePromptTitle), str3);
    builder.setView((View)editText);
    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface param1DialogInterface, int param1Int) {
          String str = input.getText().toString();
          RobotConfigFileManager.ConfigNameCheckResult configNameCheckResult = FtcConfigurationActivity.this.robotConfigFileManager.isPlausibleConfigName(FtcConfigurationActivity.this.currentCfgFile, str, FtcConfigurationActivity.this.extantRobotConfigurations);
          if (!configNameCheckResult.success) {
            str = String.format(configNameCheckResult.errorFormat, new Object[] { str });
            FtcConfigurationActivity.this.appUtil.showToast(UILocation.ONLY_LOCAL, String.format("%s %s", new Object[] { str, this.this$0.getString(R.string.configurationNotSaved) }));
            return;
          } 
          try {
            if (!FtcConfigurationActivity.this.currentCfgFile.getName().equals(str))
              FtcConfigurationActivity.this.currentCfgFile = new RobotConfigFile(FtcConfigurationActivity.this.robotConfigFileManager, str); 
            FtcConfigurationActivity.this.robotConfigFileManager.writeToFile(FtcConfigurationActivity.this.currentCfgFile, FtcConfigurationActivity.this.remoteConfigure, data);
            FtcConfigurationActivity.this.robotConfigFileManager.setActiveConfigAndUpdateUI(FtcConfigurationActivity.this.remoteConfigure, FtcConfigurationActivity.this.currentCfgFile);
            FtcConfigurationActivity.this.clearDuplicateWarning();
            FtcConfigurationActivity.this.confirmSave();
            FtcConfigurationActivity.this.pauseAfterSave();
            FtcConfigurationActivity.this.finishOk();
            return;
          } catch (DuplicateNameException duplicateNameException) {
            FtcConfigurationActivity.this.warnDuplicateNames(duplicateNameException.getMessage());
            RobotLog.ee("FtcConfigTag", duplicateNameException.getMessage());
            return;
          } catch (RobotCoreException robotCoreException) {
            FtcConfigurationActivity.this.appUtil.showToast(UILocation.ONLY_LOCAL, robotCoreException.getMessage());
            RobotLog.ee("FtcConfigTag", robotCoreException.getMessage());
            return;
          } catch (IOException iOException) {
            FtcConfigurationActivity.this.appUtil.showToast(UILocation.ONLY_LOCAL, iOException.getMessage());
            RobotLog.ee("FtcConfigTag", iOException.getMessage());
            return;
          } 
        }
      };
    builder.setPositiveButton(getString(R.string.buttonNameOK), onClickListener);
    builder.setNegativeButton(getString(R.string.buttonNameCancel), this.doNothingAndCloseListener);
    builder.show();
  }
  
  public void onDoneInfoButtonPressed(View paramView) {
    RobotLog.vv("FtcConfigTag", "onDoneInfoButtonPressed()");
    AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.titleSaveConfiguration), getString(R.string.msgInfoSave));
    builder.setPositiveButton(getString(R.string.buttonNameOK), this.doNothingAndCloseListener);
    AlertDialog alertDialog = builder.create();
    alertDialog.show();
    ((TextView)alertDialog.findViewById(16908299)).setTextSize(14.0F);
  }
  
  public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public void onPause() {
    super.onPause();
  }
  
  protected void onResume() {
    super.onResume();
  }
  
  public void onScanButtonPressed(View paramView) {
    dirtyCheckThenSingletonUSBScanAndUpdateUI(true);
  }
  
  protected void onStart() {
    super.onStart();
    if (this.remoteConfigure) {
      this.networkConnectionHandler.pushNetworkConnectionCallback(this);
      this.networkConnectionHandler.pushReceiveLoopCallback(this);
    } 
    if (!this.remoteConfigure)
      this.robotConfigFileManager.createConfigFolder(); 
    if (!this.currentCfgFile.isDirty())
      ensureConfigFileIsFresh(); 
  }
  
  protected void onStop() {
    super.onStop();
    if (this.remoteConfigure) {
      this.networkConnectionHandler.removeNetworkConnectionCallback(this);
      this.networkConnectionHandler.removeReceiveLoopCallback(this);
    } 
  }
  
  public CallbackResult packetReceived(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult peerDiscoveryEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult reportGlobalError(String paramString, boolean paramBoolean) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult telemetryEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void tellControllersAboutAttachment() {
    for (ControllerConfiguration controllerConfiguration : getRobotConfigMap().controllerConfigurations())
      controllerConfiguration.setKnownToBeAttached(this.scannedDevices.containsKey(controllerConfiguration.getSerialNumber())); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\FtcConfigurationActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */