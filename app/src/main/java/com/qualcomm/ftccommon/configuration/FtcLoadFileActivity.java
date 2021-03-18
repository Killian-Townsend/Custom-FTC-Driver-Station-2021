package com.qualcomm.ftccommon.configuration;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RecvLoopRunnable;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class FtcLoadFileActivity extends EditActivity implements RecvLoopRunnable.RecvLoopCallback {
  public static final String TAG = "FtcConfigTag";
  
  DialogInterface.OnClickListener doNothingAndCloseListener = new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
    };
  
  private List<RobotConfigFile> fileList = new CopyOnWriteArrayList<RobotConfigFile>();
  
  private NetworkConnectionHandler networkConnectionHandler = NetworkConnectionHandler.getInstance();
  
  private void buildInfoButtons() {
    ((Button)findViewById(R.id.files_holder).findViewById(R.id.info_btn)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AlertDialog.Builder builder = FtcLoadFileActivity.this.utility.buildBuilder(FtcLoadFileActivity.this.getString(R.string.availableConfigListCaption), FtcLoadFileActivity.this.getString(R.string.availableConfigsInfoMessage));
            builder.setPositiveButton(FtcLoadFileActivity.this.getString(R.string.buttonNameOK), FtcLoadFileActivity.this.doNothingAndCloseListener);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            ((TextView)alertDialog.findViewById(16908299)).setTextSize(14.0F);
          }
        });
    ((Button)findViewById(R.id.configureFromTemplateArea).findViewById(R.id.info_btn)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            AlertDialog.Builder builder = FtcLoadFileActivity.this.utility.buildBuilder(FtcLoadFileActivity.this.getString(R.string.configFromTemplateInfoTitle), FtcLoadFileActivity.this.getString(R.string.configFromTemplateInfoMessage));
            builder.setPositiveButton(FtcLoadFileActivity.this.getString(R.string.buttonNameOK), FtcLoadFileActivity.this.doNothingAndCloseListener);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            ((TextView)alertDialog.findViewById(16908299)).setTextSize(14.0F);
          }
        });
  }
  
  private RobotConfigFile getFile(View paramView) {
    return (RobotConfigFile)((TextView)((LinearLayout)((LinearLayout)paramView.getParent()).getParent()).findViewById(R.id.filename_editText)).getTag();
  }
  
  private void populate() {
    runOnUiThread(new Runnable() {
          public void run() {
            View view = FtcLoadFileActivity.this.findViewById(R.id.readOnlyExplanation);
            view.setVisibility(8);
            ViewGroup viewGroup = (ViewGroup)FtcLoadFileActivity.this.findViewById(R.id.inclusionlayout);
            viewGroup.removeAllViews();
            final Collator coll = Collator.getInstance();
            collator.setStrength(0);
            Collections.sort(FtcLoadFileActivity.this.fileList, new Comparator<RobotConfigFile>() {
                  public int compare(RobotConfigFile param2RobotConfigFile1, RobotConfigFile param2RobotConfigFile2) {
                    return coll.compare(param2RobotConfigFile1.getName(), param2RobotConfigFile2.getName());
                  }
                });
            for (RobotConfigFile robotConfigFile : FtcLoadFileActivity.this.fileList) {
              byte b;
              View view1 = LayoutInflater.from(FtcLoadFileActivity.this.context).inflate(R.layout.file_info, null);
              viewGroup.addView(view1);
              if (robotConfigFile.isReadOnly()) {
                Button button = (Button)view1.findViewById(R.id.file_delete_button);
                button.setEnabled(false);
                button.setClickable(false);
                view.setVisibility(0);
              } 
              TextView textView = (TextView)view1.findViewById(R.id.filename_editText);
              textView.setText(robotConfigFile.getName());
              textView.setTag(robotConfigFile);
              view1 = view1.findViewById(R.id.configIsReadOnlyFeedback);
              if (robotConfigFile.isReadOnly()) {
                b = 0;
              } else {
                b = 8;
              } 
              view1.setVisibility(b);
            } 
          }
        });
  }
  
  private void warnIfNoFiles() {
    if (this.fileList.size() == 0) {
      runOnUiThread(new Runnable() {
            public void run() {
              FtcLoadFileActivity.this.utility.setFeedbackText(msg0, msg1, R.id.empty_filelist, R.layout.feedback, R.id.feedbackText0, R.id.feedbackText1);
            }
          });
      return;
    } 
    runOnUiThread(new Runnable() {
          public void run() {
            ViewGroup viewGroup = (ViewGroup)FtcLoadFileActivity.this.findViewById(R.id.empty_filelist);
            viewGroup.removeAllViews();
            viewGroup.setVisibility(8);
          }
        });
  }
  
  public CallbackResult commandEvent(Command paramCommand) {
    CallbackResult callbackResult = CallbackResult.NOT_HANDLED;
    try {
      CallbackResult callbackResult1;
      String str2 = paramCommand.getName();
      String str1 = paramCommand.getExtra();
      if (str2.equals("CMD_REQUEST_CONFIGURATIONS_RESP")) {
        callbackResult1 = handleCommandRequestConfigFilesResp(str1);
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
  
  void doDeleteConfiguration(RobotConfigFile paramRobotConfigFile) {
    if (this.remoteConfigure) {
      if (paramRobotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
        this.networkConnectionHandler.sendCommand(new Command("CMD_DELETE_CONFIGURATION", paramRobotConfigFile.toString()));
        this.fileList.remove(paramRobotConfigFile);
        populate();
      } 
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
    } else {
      if (paramRobotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
        File file = paramRobotConfigFile.getFullPath();
        if (!file.delete()) {
          String str = file.getName();
          this.appUtil.showToast(UILocation.ONLY_LOCAL, String.format(getString(R.string.configToDeleteDoesNotExist), new Object[] { str }));
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("Tried to delete a file that does not exist: ");
          stringBuilder.append(str);
          RobotLog.ee("FtcConfigTag", stringBuilder.toString());
        } 
      } 
      this.fileList = this.robotConfigFileManager.getXMLFiles();
      populate();
    } 
    paramRobotConfigFile = RobotConfigFile.noConfig(this.robotConfigFileManager);
    this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, paramRobotConfigFile);
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
    return "FtcConfigTag";
  }
  
  protected CallbackResult handleCommandRequestConfigFilesResp(String paramString) throws RobotCoreException {
    RobotConfigFileManager robotConfigFileManager = this.robotConfigFileManager;
    this.fileList = RobotConfigFileManager.deserializeXMLConfigList(paramString);
    warnIfNoFiles();
    populate();
    return CallbackResult.HANDLED;
  }
  
  public CallbackResult heartbeatEvent(RobocolDatagram paramRobocolDatagram, long paramLong) {
    return CallbackResult.NOT_HANDLED;
  }
  
  Intent makeEditConfigIntent(Class paramClass, RobotConfigFile paramRobotConfigFile) {
    EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>(this);
    editParameters.setExtantRobotConfigurations(this.fileList);
    if (paramRobotConfigFile != null)
      editParameters.setCurrentCfgFile(paramRobotConfigFile); 
    Intent intent = new Intent(this.context, paramClass);
    editParameters.putIntent(intent);
    return intent;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    logActivityResult(paramInt1, paramInt2, paramIntent);
    this.currentCfgFile = this.robotConfigFileManager.getActiveConfigAndUpdateUI();
  }
  
  public void onBackPressed() {
    logBackPressed();
    finishOk();
  }
  
  public void onConfigureFromTemplatePressed(View paramView) {
    startActivityForResult(makeEditConfigIntent(ConfigureFromTemplateActivity.class, (RobotConfigFile)null), ConfigureFromTemplateActivity.requestCode.value);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    RobotLog.vv("FtcConfigTag", "FtcLoadFileActivity started");
    setContentView(R.layout.activity_load);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    buildInfoButtons();
    if (this.remoteConfigure)
      this.networkConnectionHandler.pushReceiveLoopCallback(this); 
  }
  
  protected void onDestroy() {
    super.onDestroy();
    if (this.remoteConfigure)
      this.networkConnectionHandler.removeReceiveLoopCallback(this); 
  }
  
  public void onFileActivateButtonPressed(View paramView) {
    RobotConfigFile robotConfigFile = getFile(paramView);
    this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, robotConfigFile);
    if (this.remoteConfigure)
      this.networkConnectionHandler.sendCommand(new Command("CMD_ACTIVATE_CONFIGURATION", robotConfigFile.toString())); 
  }
  
  public void onFileDeleteButtonPressed(View paramView) {
    final RobotConfigFile robotConfigFile = getFile(paramView);
    if (robotConfigFile.getLocation() == RobotConfigFile.FileLocation.LOCAL_STORAGE) {
      AlertDialog.Builder builder = this.utility.buildBuilder(getString(R.string.confirmConfigDeleteTitle), getString(R.string.confirmConfigDeleteMessage));
      DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            FtcLoadFileActivity.this.doDeleteConfiguration(robotConfigFile);
          }
        };
      builder.setPositiveButton(R.string.buttonNameOK, onClickListener);
      builder.setNegativeButton(R.string.buttonNameCancel, this.doNothingAndCloseListener);
      builder.show();
    } 
  }
  
  public void onFileEditButtonPressed(View paramView) {
    RobotConfigFile robotConfigFile = getFile(paramView);
    this.robotConfigFileManager.setActiveConfig(this.remoteConfigure, robotConfigFile);
    startActivityForResult(makeEditConfigIntent(FtcConfigurationActivity.class, robotConfigFile), FtcConfigurationActivity.requestCode.value);
  }
  
  public void onNewButtonPressed(View paramView) {
    RobotConfigFile robotConfigFile = RobotConfigFile.noConfig(this.robotConfigFileManager);
    this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.remoteConfigure, robotConfigFile);
    startActivityForResult(makeEditConfigIntent(FtcNewFileActivity.class, (RobotConfigFile)null), FtcNewFileActivity.requestCode.value);
  }
  
  public void onResume() {
    super.onResume();
  }
  
  protected void onStart() {
    super.onStart();
    if (!this.remoteConfigure)
      this.robotConfigFileManager.createConfigFolder(); 
    if (!this.remoteConfigure) {
      this.fileList = this.robotConfigFileManager.getXMLFiles();
      warnIfNoFiles();
    } else {
      this.networkConnectionHandler.sendCommand(new Command("CMD_REQUEST_CONFIGURATIONS"));
    } 
    populate();
  }
  
  protected void onStop() {
    super.onStop();
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
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\FtcLoadFileActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */