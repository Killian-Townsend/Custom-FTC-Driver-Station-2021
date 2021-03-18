package com.qualcomm.ftccommon.configuration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationUtility;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.Utility;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.ThemedActivity;

public abstract class EditActivity extends ThemedActivity {
  public static final String TAG = "EditActivity";
  
  protected AppUtil appUtil = AppUtil.getInstance();
  
  protected ConfigurationUtility configurationUtility;
  
  protected Context context;
  
  protected ControllerConfiguration controllerConfiguration;
  
  protected RobotConfigFile currentCfgFile;
  
  protected List<RobotConfigFile> extantRobotConfigurations = new LinkedList<RobotConfigFile>();
  
  protected boolean haveRobotConfigMapParameter = false;
  
  protected int idAddButton = R.id.addButton;
  
  protected int idFixButton = R.id.fixButton;
  
  protected int idSwapButton = R.id.swapButton;
  
  protected boolean remoteConfigure = AppUtil.getInstance().isDriverStation();
  
  protected RobotConfigFileManager robotConfigFileManager;
  
  protected RobotConfigMap robotConfigMap = new RobotConfigMap();
  
  protected ScannedDevices scannedDevices = new ScannedDevices();
  
  protected AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
      protected View itemViewFromSpinnerItem(View param1View) {
        return (View)param1View.getParent().getParent().getParent();
      }
      
      public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        EditActivity.ConfigurationTypeAndDisplayName configurationTypeAndDisplayName = (EditActivity.ConfigurationTypeAndDisplayName)param1AdapterView.getItemAtPosition(param1Int);
        param1View = itemViewFromSpinnerItem(param1View);
        if (configurationTypeAndDisplayName.configurationType == BuiltInConfigurationType.NOTHING) {
          EditActivity.this.clearDevice(param1View);
          return;
        } 
        EditActivity.this.changeDevice(param1View, configurationTypeAndDisplayName.configurationType);
      }
      
      public void onNothingSelected(AdapterView<?> param1AdapterView) {}
    };
  
  protected Utility utility;
  
  public static String formatSerialNumber(Context paramContext, ControllerConfiguration paramControllerConfiguration) {
    String str2 = paramControllerConfiguration.getSerialNumber().toString();
    if (paramControllerConfiguration.getSerialNumber().isFake())
      return str2; 
    String str1 = str2;
    if (!paramControllerConfiguration.isKnownToBeAttached()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str2);
      stringBuilder.append(paramContext.getString(R.string.serialNumberNotAttached));
      str1 = stringBuilder.toString();
    } 
    return str1;
  }
  
  private void handleLaunchEdit(RequestCode paramRequestCode, Class paramClass, Bundle paramBundle) {
    Intent intent = new Intent(this.context, paramClass);
    intent.putExtras(paramBundle);
    setResult(-1, intent);
    RobotLog.v("%s: starting activity %s code=%d", new Object[] { getClass().getSimpleName(), intent.getComponent().getShortClassName(), Integer.valueOf(paramRequestCode.value) });
    startActivityForResult(intent, paramRequestCode.value);
  }
  
  protected void changeDevice(View paramView, ConfigurationType paramConfigurationType) {}
  
  protected void clearDevice(View paramView) {}
  
  protected void clearNameIfNecessary(EditText paramEditText, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled()) {
      paramEditText.setText("");
      paramDeviceConfiguration.setName("");
      return;
    } 
    paramEditText.setText(paramDeviceConfiguration.getName());
  }
  
  protected void deserialize(EditParameters paramEditParameters) {
    ControllerConfiguration controllerConfiguration;
    this.scannedDevices = paramEditParameters.getScannedDevices();
    this.extantRobotConfigurations = paramEditParameters.getExtantRobotConfigurations();
    if (paramEditParameters.getConfiguration() instanceof ControllerConfiguration) {
      controllerConfiguration = (ControllerConfiguration)paramEditParameters.getConfiguration();
    } else {
      controllerConfiguration = null;
    } 
    this.controllerConfiguration = controllerConfiguration;
    if (paramEditParameters.getCurrentCfgFile() != null)
      this.currentCfgFile = paramEditParameters.getCurrentCfgFile(); 
    deserializeConfigMap(paramEditParameters);
  }
  
  protected void deserializeConfigMap(EditParameters paramEditParameters) {
    this.robotConfigMap = new RobotConfigMap(paramEditParameters.getRobotConfigMap());
    this.haveRobotConfigMapParameter = paramEditParameters.haveRobotConfigMapParameter();
    RobotConfigMap robotConfigMap = this.robotConfigMap;
    if (robotConfigMap != null) {
      ControllerConfiguration controllerConfiguration = this.controllerConfiguration;
      if (controllerConfiguration != null && robotConfigMap.contains(controllerConfiguration.getSerialNumber()))
        this.controllerConfiguration = this.robotConfigMap.get(this.controllerConfiguration.getSerialNumber()); 
    } 
  }
  
  public String disabledDeviceName() {
    return getString(R.string.noDeviceAttached);
  }
  
  public String displayNameOfConfigurationType(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor, ConfigurationType paramConfigurationType) {
    return paramConfigurationType.getDisplayName(paramDisplayNameFlavor);
  }
  
  protected int findPosition(Spinner paramSpinner, ConfigurationType paramConfigurationType) {
    ArrayAdapter arrayAdapter = (ArrayAdapter)paramSpinner.getAdapter();
    for (int i = 0; i < arrayAdapter.getCount(); i++) {
      if (((ConfigurationTypeAndDisplayName)arrayAdapter.getItem(i)).configurationType == paramConfigurationType)
        return i; 
    } 
    return -1;
  }
  
  protected int findPosition(Spinner paramSpinner, ConfigurationType paramConfigurationType1, ConfigurationType paramConfigurationType2) {
    int j = findPosition(paramSpinner, paramConfigurationType1);
    int i = j;
    if (j < 0)
      i = findPosition(paramSpinner, paramConfigurationType2); 
    return i;
  }
  
  protected void finishCancel() {
    RobotLog.v("%s: cancelling", new Object[] { getClass().getSimpleName() });
    setResult(0, new Intent());
    finish();
  }
  
  protected void finishOk() {
    finishOk(new Intent());
  }
  
  protected void finishOk(Intent paramIntent) {
    setResult(-1, paramIntent);
    finish();
  }
  
  protected void finishOk(EditParameters paramEditParameters) {
    RobotLog.v("%s: OK", new Object[] { getClass().getSimpleName() });
    Intent intent = new Intent();
    paramEditParameters.putIntent(intent);
    finishOk(intent);
  }
  
  protected ConfigurationType getDefaultEnabledSelection() {
    return (ConfigurationType)BuiltInConfigurationType.NOTHING;
  }
  
  protected RobotConfigMap getRobotConfigMap() {
    RobotConfigMap robotConfigMap2 = this.robotConfigMap;
    RobotConfigMap robotConfigMap1 = robotConfigMap2;
    if (robotConfigMap2 == null)
      robotConfigMap1 = new RobotConfigMap(); 
    return robotConfigMap1;
  }
  
  public String getTag() {
    return "EditActivity";
  }
  
  protected CallbackResult handleCommandNotifyActiveConfig(String paramString) {
    RobotLog.vv("EditActivity", "%s.handleCommandRequestActiveConfigResp(%s)", new Object[] { getClass().getSimpleName(), paramString });
    RobotConfigFile robotConfigFile = this.robotConfigFileManager.getConfigFromString(paramString);
    this.robotConfigFileManager.setActiveConfigAndUpdateUI(robotConfigFile);
    return CallbackResult.HANDLED_CONTINUE;
  }
  
  protected void handleLaunchEdit(RequestCode paramRequestCode, Class paramClass, EditParameters paramEditParameters) {
    handleLaunchEdit(paramRequestCode, paramClass, paramEditParameters.toBundle());
  }
  
  protected void handleLaunchEdit(RequestCode paramRequestCode, Class paramClass, DeviceConfiguration paramDeviceConfiguration) {
    handleLaunchEdit(paramRequestCode, paramClass, new EditParameters<DeviceConfiguration>(this, paramDeviceConfiguration));
  }
  
  protected void handleLaunchEdit(RequestCode paramRequestCode, Class paramClass, List<DeviceConfiguration> paramList) {
    handleLaunchEdit(paramRequestCode, paramClass, new EditParameters<DeviceConfiguration>(this, DeviceConfiguration.class, paramList));
  }
  
  protected void handleSpinner(View paramView, int paramInt, DeviceConfiguration paramDeviceConfiguration) {
    handleSpinner(paramView, paramInt, paramDeviceConfiguration, false);
  }
  
  protected void handleSpinner(View paramView, int paramInt, DeviceConfiguration paramDeviceConfiguration, boolean paramBoolean) {
    Spinner spinner = (Spinner)paramView.findViewById(paramInt);
    if (paramBoolean || paramDeviceConfiguration.isEnabled()) {
      spinner.setSelection(findPosition(spinner, paramDeviceConfiguration.getSpinnerChoiceType(), getDefaultEnabledSelection()));
    } else {
      spinner.setSelection(findPosition(spinner, (ConfigurationType)BuiltInConfigurationType.NOTHING));
    } 
    spinner.setOnItemSelectedListener(this.spinnerListener);
  }
  
  protected void localizeConfigTypeSpinner(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor, Spinner paramSpinner) {
    ArrayAdapter arrayAdapter = (ArrayAdapter)paramSpinner.getAdapter();
    ArrayList<Object> arrayList = new ArrayList();
    for (int i = 0; i < arrayAdapter.getCount(); i++)
      arrayList.add(arrayAdapter.getItem(i)); 
    localizeConfigTypeSpinnerStrings(paramDisplayNameFlavor, paramSpinner, arrayList);
  }
  
  protected void localizeConfigTypeSpinnerStrings(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor, Spinner paramSpinner, List<String> paramList) {
    LinkedList<ConfigurationType> linkedList = new LinkedList();
    Iterator<String> iterator = paramList.iterator();
    while (iterator.hasNext())
      linkedList.add(BuiltInConfigurationType.fromString(iterator.next())); 
    localizeConfigTypeSpinnerTypes(paramDisplayNameFlavor, paramSpinner, linkedList);
  }
  
  protected void localizeConfigTypeSpinnerTypes(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor, Spinner paramSpinner, List<ConfigurationType> paramList) {
    ConfigurationTypeAndDisplayName[] arrayOfConfigurationTypeAndDisplayName = new ConfigurationTypeAndDisplayName[paramList.size()];
    int i;
    for (i = 0; i < paramList.size(); i++)
      arrayOfConfigurationTypeAndDisplayName[i] = new ConfigurationTypeAndDisplayName(paramDisplayNameFlavor, paramList.get(i)); 
    paramSpinner.setAdapter((SpinnerAdapter)new ConfigurationTypeArrayAdapter((Context)this, arrayOfConfigurationTypeAndDisplayName));
  }
  
  protected void logActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    RobotLog.v("%s: activity result: code=%d result=%d", new Object[] { getClass().getSimpleName(), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
  }
  
  protected void logBackPressed() {
    RobotLog.v("%s: backPressed received", new Object[] { getClass().getSimpleName() });
  }
  
  public String nameOf(DeviceConfiguration paramDeviceConfiguration) {
    return nameOf(paramDeviceConfiguration.getName());
  }
  
  public String nameOf(String paramString) {
    String str = paramString;
    if (paramString.equals("NO$DEVICE$ATTACHED"))
      str = getString(R.string.noDeviceAttached); 
    return str;
  }
  
  public void onBackPressed() {
    logBackPressed();
    finishOk();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.context = (Context)this;
    PreferenceManager.setDefaultValues((Context)this, R.xml.app_settings, false);
    this.utility = new Utility((Activity)this);
    this.configurationUtility = new ConfigurationUtility();
    RobotConfigFileManager robotConfigFileManager = new RobotConfigFileManager((Activity)this);
    this.robotConfigFileManager = robotConfigFileManager;
    this.currentCfgFile = robotConfigFileManager.getActiveConfig();
  }
  
  protected void onStart() {
    super.onStart();
    this.robotConfigFileManager.updateActiveConfigHeader(this.currentCfgFile);
  }
  
  protected void sendOrInject(Command paramCommand) {
    if (this.remoteConfigure) {
      NetworkConnectionHandler.getInstance().sendCommand(paramCommand);
      return;
    } 
    NetworkConnectionHandler.getInstance().injectReceivedCommand(paramCommand);
  }
  
  protected class ConfigurationTypeAndDisplayName {
    public final ConfigurationType configurationType;
    
    public final String displayName;
    
    public final ConfigurationType.DisplayNameFlavor flavor;
    
    public ConfigurationTypeAndDisplayName(ConfigurationType.DisplayNameFlavor param1DisplayNameFlavor, ConfigurationType param1ConfigurationType) {
      this.flavor = param1DisplayNameFlavor;
      this.configurationType = param1ConfigurationType;
      this.displayName = EditActivity.this.displayNameOfConfigurationType(param1DisplayNameFlavor, param1ConfigurationType);
    }
    
    public String toString() {
      return this.displayName;
    }
  }
  
  protected static class DisplayNameAndInteger implements Comparable<DisplayNameAndInteger> {
    public final String displayName;
    
    public final int value;
    
    public DisplayNameAndInteger(String param1String, int param1Int) {
      this.displayName = param1String;
      this.value = param1Int;
    }
    
    public int compareTo(DisplayNameAndInteger param1DisplayNameAndInteger) {
      return this.displayName.compareTo(param1DisplayNameAndInteger.displayName);
    }
    
    public String toString() {
      return this.displayName;
    }
  }
  
  protected static class DisplayNameAndRequestCode implements Comparable<DisplayNameAndRequestCode> {
    public final String displayName;
    
    public final RequestCode requestCode;
    
    public DisplayNameAndRequestCode(String param1String) {
      String[] arrayOfString = param1String.split("\\|");
      this.displayName = arrayOfString[0];
      this.requestCode = RequestCode.fromString(arrayOfString[1]);
    }
    
    public DisplayNameAndRequestCode(String param1String, RequestCode param1RequestCode) {
      this.displayName = param1String;
      this.requestCode = param1RequestCode;
    }
    
    public static DisplayNameAndRequestCode[] fromArray(String[] param1ArrayOfString) {
      int j = param1ArrayOfString.length;
      DisplayNameAndRequestCode[] arrayOfDisplayNameAndRequestCode = new DisplayNameAndRequestCode[j];
      for (int i = 0; i < j; i++)
        arrayOfDisplayNameAndRequestCode[i] = new DisplayNameAndRequestCode(param1ArrayOfString[i]); 
      return arrayOfDisplayNameAndRequestCode;
    }
    
    public int compareTo(DisplayNameAndRequestCode param1DisplayNameAndRequestCode) {
      return this.displayName.compareTo(param1DisplayNameAndRequestCode.displayName);
    }
    
    public String toString() {
      return this.displayName;
    }
  }
  
  protected class SetNameTextWatcher implements TextWatcher {
    private final DeviceConfiguration deviceConfiguration;
    
    protected SetNameTextWatcher(DeviceConfiguration param1DeviceConfiguration) {
      this.deviceConfiguration = param1DeviceConfiguration;
    }
    
    public void afterTextChanged(Editable param1Editable) {
      String str = param1Editable.toString();
      this.deviceConfiguration.setName(str);
    }
    
    public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */