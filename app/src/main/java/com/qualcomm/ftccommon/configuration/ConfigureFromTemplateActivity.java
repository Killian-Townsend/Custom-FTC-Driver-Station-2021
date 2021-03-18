package com.qualcomm.ftccommon.configuration;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.StringReader;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.inspection.R;
import org.xmlpull.v1.XmlPullParser;

public class ConfigureFromTemplateActivity extends EditActivity implements RecvLoopRunnable.RecvLoopCallback {
  public static final String TAG = "ConfigFromTemplate";
  
  public static final RequestCode requestCode = RequestCode.CONFIG_FROM_TEMPLATE;
  
  protected List<RobotConfigFile> configurationList = new CopyOnWriteArrayList<RobotConfigFile>();
  
  protected ViewGroup feedbackAnchor;
  
  protected NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  protected final Deque<StringProcessor> receivedConfigProcessors = new LinkedList<StringProcessor>();
  
  protected Map<String, String> remoteTemplates = new ConcurrentHashMap<String, String>();
  
  protected List<RobotConfigFile> templateList = new CopyOnWriteArrayList<RobotConfigFile>();
  
  protected USBScanManager usbScanManager;
  
  private CallbackResult handleCommandScanResp(String paramString) throws RobotCoreException {
    Assert.assertTrue(this.remoteConfigure);
    this.usbScanManager.handleCommandScanResponse(paramString);
    return CallbackResult.HANDLED_CONTINUE;
  }
  
  private String indent(int paramInt, String paramString) {
    String str = "";
    for (int i = 0; i < paramInt; i++) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str);
      stringBuilder.append(" ");
      str = stringBuilder.toString();
    } 
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(str);
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append("\n");
    stringBuilder2.append(str);
    stringBuilder1.append(paramString.replace("\n", stringBuilder2.toString()));
    return stringBuilder1.toString();
  }
  
  protected ScannedDevices awaitScannedDevices() {
    try {
      this.scannedDevices = this.usbScanManager.awaitScannedDevices();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    return this.scannedDevices;
  }
  
  public CallbackResult commandEvent(Command paramCommand) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    try {
      CallbackResult callbackResult1;
      String str2 = paramCommand.getName();
      String str1 = paramCommand.getExtra();
      if (str2.equals("CMD_SCAN_RESP")) {
        callbackResult1 = handleCommandScanResp(str1);
      } else if (str2.equals("CMD_REQUEST_CONFIGURATIONS_RESP")) {
        callbackResult1 = handleCommandRequestConfigurationsResp((String)callbackResult1);
      } else if (str2.equals("CMD_REQUEST_CONFIGURATION_TEMPLATES_RESP")) {
        callbackResult1 = handleCommandRequestTemplatesResp((String)callbackResult1);
      } else if (str2.equals("CMD_REQUEST_PARTICULAR_CONFIGURATION_RESP")) {
        callbackResult1 = handleCommandRequestParticularConfigurationResp((String)callbackResult1);
      } else if (str2.equals("CMD_NOTIFY_ACTIVE_CONFIGURATION")) {
        callbackResult1 = handleCommandNotifyActiveConfig((String)callbackResult1);
      } else {
        return callbackResult;
      } 
    } catch (RobotCoreException robotCoreException) {
      RobotLog.logStacktrace((Throwable)robotCoreException);
      return callbackResult;
    } 
    return (CallbackResult)robotCoreException;
  }
  
  void configureFromTemplate(RobotConfigFile paramRobotConfigFile, XmlPullParser paramXmlPullParser) {
    try {
      RobotConfigMap robotConfigMap = instantiateTemplate(paramRobotConfigFile, paramXmlPullParser);
      awaitScannedDevices();
      EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>(this);
      editParameters.setRobotConfigMap(robotConfigMap);
      editParameters.setExtantRobotConfigurations(this.configurationList);
      editParameters.setScannedDevices(this.scannedDevices);
      Intent intent = new Intent(this.context, FtcConfigurationActivity.class);
      editParameters.putIntent(intent);
      this.robotConfigFileManager.setActiveConfig(RobotConfigFile.noConfig(this.robotConfigFileManager));
      startActivityForResult(intent, FtcConfigurationActivity.requestCode.value);
      return;
    } catch (RobotCoreException robotCoreException) {
      return;
    } 
  }
  
  public CallbackResult emptyEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult gamepadEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected FrameLayout getBackBar() {
    return (FrameLayout)findViewById(R.id.backbar);
  }
  
  public String getTag() {
    return "ConfigFromTemplate";
  }
  
  protected void getTemplateAndThen(final RobotConfigFile templateMeta, final TemplateProcessor processor) {
    if (this.remoteConfigure) {
      String str = this.remoteTemplates.get(templateMeta.getName());
      if (str != null) {
        processor.processTemplate(templateMeta, xmlPullParserFromString(str));
        return;
      } 
      synchronized (this.receivedConfigProcessors) {
        this.receivedConfigProcessors.addLast(new StringProcessor() {
              public void processString(String param1String) {
                ConfigureFromTemplateActivity.this.remoteTemplates.put(templateMeta.getName(), param1String);
                XmlPullParser xmlPullParser = ConfigureFromTemplateActivity.this.xmlPullParserFromString(param1String);
                processor.processTemplate(templateMeta, xmlPullParser);
              }
            });
        this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_PARTICULAR_CONFIGURATION", templateMeta.toString()));
        return;
      } 
    } 
    processor.processTemplate(templateMeta, templateMeta.getXml());
  }
  
  protected RobotConfigFile getTemplateMeta(View paramView) {
    return (RobotConfigFile)((TextView)((ViewGroup)paramView.getParent()).findViewById(R.id.templateNameText)).getTag();
  }
  
  protected CallbackResult handleCommandRequestConfigurationsResp(String paramString) throws RobotCoreException {
    RobotConfigFileManager robotConfigFileManager = this.robotConfigFileManager;
    this.configurationList = RobotConfigFileManager.deserializeXMLConfigList(paramString);
    return CallbackResult.HANDLED;
  }
  
  protected CallbackResult handleCommandRequestParticularConfigurationResp(String paramString) throws RobotCoreException {
    synchronized (this.receivedConfigProcessors) {
      StringProcessor stringProcessor = this.receivedConfigProcessors.pollFirst();
      if (stringProcessor != null)
        stringProcessor.processString(paramString); 
      return CallbackResult.HANDLED;
    } 
  }
  
  protected CallbackResult handleCommandRequestTemplatesResp(String paramString) throws RobotCoreException {
    RobotConfigFileManager robotConfigFileManager = this.robotConfigFileManager;
    this.templateList = RobotConfigFileManager.deserializeXMLConfigList(paramString);
    warnIfNoTemplates();
    populate();
    return CallbackResult.HANDLED;
  }
  
  public CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong) {
    return CallbackResult.NOT_HANDLED;
  }
  
  RobotConfigMap instantiateTemplate(RobotConfigFile paramRobotConfigFile, XmlPullParser paramXmlPullParser) throws RobotCoreException {
    awaitScannedDevices();
    RobotConfigMap robotConfigMap = new RobotConfigMap((new ReadXMLFileHandler()).parse(paramXmlPullParser));
    robotConfigMap.bindUnboundControllers(this.scannedDevices);
    return robotConfigMap;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    if (paramInt1 == FtcConfigurationActivity.requestCode.value)
      this.currentCfgFile = this.robotConfigFileManager.getActiveConfigAndUpdateUI(); 
  }
  
  public void onConfigureButtonPressed(View paramView) {
    getTemplateAndThen(getTemplateMeta(paramView), new TemplateProcessor() {
          public void processTemplate(RobotConfigFile param1RobotConfigFile, XmlPullParser param1XmlPullParser) {
            ConfigureFromTemplateActivity.this.configureFromTemplate(param1RobotConfigFile, param1XmlPullParser);
          }
        });
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.activity_configure_from_template);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    if (this.remoteConfigure)
      this.networkConnectionHandler.pushReceiveLoopCallback(this); 
    USBScanManager uSBScanManager = new USBScanManager(this.context, this.remoteConfigure);
    this.usbScanManager = uSBScanManager;
    uSBScanManager.startExecutorService();
    this.usbScanManager.startDeviceScanIfNecessary();
    this.feedbackAnchor = (ViewGroup)findViewById(R.id.feedbackAnchor);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    this.usbScanManager.stopExecutorService();
    this.usbScanManager = null;
    if (this.remoteConfigure)
      this.networkConnectionHandler.removeReceiveLoopCallback(this); 
  }
  
  public void onInfoButtonPressed(View paramView) {
    getTemplateAndThen(getTemplateMeta(paramView), new TemplateProcessor() {
          public void processTemplate(RobotConfigFile param1RobotConfigFile, XmlPullParser param1XmlPullParser) {
            ConfigureFromTemplateActivity.this.showInfo(param1RobotConfigFile, param1XmlPullParser);
          }
        });
  }
  
  protected void onStart() {
    super.onStart();
    this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
    if (!this.remoteConfigure) {
      this.configurationList = this.robotConfigFileManager.getXMLFiles();
      this.templateList = this.robotConfigFileManager.getXMLTemplates();
      warnIfNoTemplates();
    } else {
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATION_TEMPLATES"));
    } 
    populate();
  }
  
  public CallbackResult packetReceived(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  public CallbackResult peerDiscoveryEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void populate() {
    runOnUiThread(new Runnable() {
          public void run() {
            ViewGroup viewGroup = (ViewGroup)ConfigureFromTemplateActivity.this.findViewById(R.id.templateList);
            viewGroup.removeAllViews();
            final Collator coll = Collator.getInstance();
            collator.setStrength(0);
            Collections.sort(ConfigureFromTemplateActivity.this.templateList, new Comparator<RobotConfigFile>() {
                  public int compare(RobotConfigFile param2RobotConfigFile1, RobotConfigFile param2RobotConfigFile2) {
                    return coll.compare(param2RobotConfigFile1.getName(), param2RobotConfigFile2.getName());
                  }
                });
            for (RobotConfigFile robotConfigFile : ConfigureFromTemplateActivity.this.templateList) {
              View view = LayoutInflater.from(ConfigureFromTemplateActivity.this.context).inflate(R.layout.template_info, null);
              viewGroup.addView(view);
              TextView textView = (TextView)view.findViewById(R.id.templateNameText);
              textView.setText(robotConfigFile.getName());
              textView.setTag(robotConfigFile);
            } 
          }
        });
  }
  
  public CallbackResult reportGlobalError(String paramString, boolean paramBoolean) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void showInfo(RobotConfigFile paramRobotConfigFile, XmlPullParser paramXmlPullParser) {
    String str = indent(3, this.robotConfigFileManager.getRobotConfigDescription(paramXmlPullParser));
    runOnUiThread(new Runnable() {
          public void run() {
            ConfigureFromTemplateActivity.this.utility.setFeedbackText(title, message.trim(), R.id.feedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1, R.id.feedbackOKButton);
          }
        });
  }
  
  public CallbackResult telemetryEvent(RobocolDatagram paramRobocolDatagram) {
    return CallbackResult.NOT_HANDLED;
  }
  
  protected void warnIfNoTemplates() {
    if (this.templateList.size() == 0) {
      this.feedbackAnchor.setVisibility(4);
      runOnUiThread(new Runnable() {
            public void run() {
              ConfigureFromTemplateActivity.this.utility.setFeedbackText(msg0, msg1, R.id.feedbackAnchor, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
            }
          });
      return;
    } 
    runOnUiThread(new Runnable() {
          public void run() {
            ConfigureFromTemplateActivity.this.feedbackAnchor.removeAllViews();
            ConfigureFromTemplateActivity.this.feedbackAnchor.setVisibility(8);
          }
        });
  }
  
  protected XmlPullParser xmlPullParserFromString(String paramString) {
    return ReadXMLFileHandler.xmlPullParserFromReader(new StringReader(paramString));
  }
  
  protected static interface StringProcessor {
    void processString(String param1String);
  }
  
  protected static interface TemplateProcessor {
    void processTemplate(RobotConfigFile param1RobotConfigFile, XmlPullParser param1XmlPullParser);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\ConfigureFromTemplateActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */