package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationUtility;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.Locale;

public class EditLegacyModuleControllerActivity extends EditUSBDeviceActivity {
  private static boolean DEBUG;
  
  public static final RequestCode requestCode = RequestCode.EDIT_LEGACY_MODULE;
  
  private EditText controller_name;
  
  private ArrayList<DeviceConfiguration> devices = new ArrayList<DeviceConfiguration>();
  
  private View info_port0;
  
  private View info_port1;
  
  private View info_port2;
  
  private View info_port3;
  
  private View info_port4;
  
  private View info_port5;
  
  static {
    DEBUG = false;
  }
  
  private void createController(int paramInt, ConfigurationType paramConfigurationType) {
    DeviceConfiguration deviceConfiguration = this.devices.get(paramInt);
    String str = deviceConfiguration.getName();
    SerialNumber serialNumber = SerialNumber.createFake();
    if (deviceConfiguration.getConfigurationType() != paramConfigurationType) {
      MotorControllerConfiguration motorControllerConfiguration;
      ServoControllerConfiguration servoControllerConfiguration;
      if (paramConfigurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
        motorControllerConfiguration = new MotorControllerConfiguration(str, ConfigurationUtility.buildEmptyMotors(1, 2), serialNumber);
        motorControllerConfiguration.setPort(paramInt);
      } else if (motorControllerConfiguration == BuiltInConfigurationType.SERVO_CONTROLLER) {
        servoControllerConfiguration = new ServoControllerConfiguration(str, ConfigurationUtility.buildEmptyServos(1, 6), serialNumber);
        servoControllerConfiguration.setPort(paramInt);
      } else if (servoControllerConfiguration == BuiltInConfigurationType.MATRIX_CONTROLLER) {
        MatrixControllerConfiguration matrixControllerConfiguration = new MatrixControllerConfiguration(str, ConfigurationUtility.buildEmptyMotors(1, 4), ConfigurationUtility.buildEmptyServos(1, 4), serialNumber);
        matrixControllerConfiguration.setPort(paramInt);
      } else {
        servoControllerConfiguration = null;
      } 
      if (servoControllerConfiguration != null) {
        servoControllerConfiguration.setEnabled(true);
        setModule((DeviceConfiguration)servoControllerConfiguration);
      } 
    } 
  }
  
  private View createPortView(int paramInt1, int paramInt2) {
    LinearLayout linearLayout = (LinearLayout)findViewById(paramInt1);
    View view = getLayoutInflater().inflate(R.layout.simple_device, (ViewGroup)linearLayout, true);
    ((TextView)view.findViewById(R.id.portNumber)).setText(String.format(Locale.getDefault(), "%d", new Object[] { Integer.valueOf(paramInt2) }));
    Spinner spinner = (Spinner)view.findViewById(R.id.choiceSpinner);
    localizeConfigTypeSpinner(ConfigurationType.DisplayNameFlavor.Legacy, spinner);
    return view;
  }
  
  private void editController_general(DeviceConfiguration paramDeviceConfiguration) {
    EditParameters<DeviceConfiguration> editParameters;
    paramDeviceConfiguration.setName(((EditText)((LinearLayout)findViewByPort(paramDeviceConfiguration.getPort())).findViewById(R.id.editTextResult)).getText().toString());
    if (paramDeviceConfiguration.getConfigurationType() == BuiltInConfigurationType.MOTOR_CONTROLLER) {
      editParameters = new EditParameters<DeviceConfiguration>(this, paramDeviceConfiguration, DeviceConfiguration.class, ((MotorControllerConfiguration)paramDeviceConfiguration).getMotors());
      editParameters.setInitialPortNumber(1);
      handleLaunchEdit(EditLegacyMotorControllerActivity.requestCode, EditLegacyMotorControllerActivity.class, editParameters);
      return;
    } 
    if (editParameters.getConfigurationType() == BuiltInConfigurationType.SERVO_CONTROLLER) {
      editParameters = new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)editParameters, DeviceConfiguration.class, ((ServoControllerConfiguration)editParameters).getServos());
      editParameters.setInitialPortNumber(1);
      editParameters.setControlSystem(ControlSystem.MODERN_ROBOTICS);
      handleLaunchEdit(EditLegacyServoControllerActivity.requestCode, EditLegacyServoControllerActivity.class, editParameters);
      return;
    } 
    if (editParameters.getConfigurationType() == BuiltInConfigurationType.MATRIX_CONTROLLER)
      handleLaunchEdit(EditMatrixControllerActivity.requestCode, EditMatrixControllerActivity.class, (DeviceConfiguration)editParameters); 
  }
  
  private View findViewByPort(int paramInt) {
    return (paramInt != 0) ? ((paramInt != 1) ? ((paramInt != 2) ? ((paramInt != 3) ? ((paramInt != 4) ? ((paramInt != 5) ? null : this.info_port5) : this.info_port4) : this.info_port3) : this.info_port2) : this.info_port1) : this.info_port0;
  }
  
  private boolean isController(DeviceConfiguration paramDeviceConfiguration) {
    return (paramDeviceConfiguration.getConfigurationType() == BuiltInConfigurationType.MOTOR_CONTROLLER || paramDeviceConfiguration.getConfigurationType() == BuiltInConfigurationType.SERVO_CONTROLLER);
  }
  
  private void populatePort(View paramView, DeviceConfiguration paramDeviceConfiguration) {
    handleSpinner(paramView, R.id.choiceSpinner, paramDeviceConfiguration, true);
    String str = paramDeviceConfiguration.getName();
    EditText editText = (EditText)paramView.findViewById(R.id.editTextResult);
    int i = Integer.parseInt(((TextView)paramView.findViewById(R.id.portNumber)).getText().toString());
    editText.addTextChangedListener(new UsefulTextWatcher(findViewByPort(i)));
    editText.setText(str);
    if (DEBUG) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[populatePort] name: ");
      stringBuilder.append(str);
      stringBuilder.append(", port: ");
      stringBuilder.append(i);
      stringBuilder.append(", type: ");
      stringBuilder.append(paramDeviceConfiguration.getConfigurationType());
      RobotLog.e(stringBuilder.toString());
    } 
  }
  
  private void setButtonVisibility(int paramInt1, int paramInt2) {
    ((Button)findViewByPort(paramInt1).findViewById(R.id.edit_controller_btn)).setVisibility(paramInt2);
  }
  
  private void setModule(DeviceConfiguration paramDeviceConfiguration) {
    int i = paramDeviceConfiguration.getPort();
    this.devices.set(i, paramDeviceConfiguration);
  }
  
  protected void changeDevice(View paramView, ConfigurationType paramConfigurationType) {
    int i = Integer.parseInt(((TextView)paramView.findViewById(R.id.portNumber)).getText().toString());
    EditText editText = (EditText)paramView.findViewById(R.id.editTextResult);
    DeviceConfiguration deviceConfiguration = this.devices.get(i);
    editText.setEnabled(true);
    clearNameIfNecessary(editText, deviceConfiguration);
    if (paramConfigurationType == BuiltInConfigurationType.MOTOR_CONTROLLER || paramConfigurationType == BuiltInConfigurationType.SERVO_CONTROLLER || paramConfigurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
      createController(i, paramConfigurationType);
      setButtonVisibility(i, 0);
    } else {
      deviceConfiguration.setConfigurationType(paramConfigurationType);
      if (paramConfigurationType == BuiltInConfigurationType.NOTHING) {
        deviceConfiguration.setEnabled(false);
      } else {
        deviceConfiguration.setEnabled(true);
      } 
      setButtonVisibility(i, 8);
    } 
    if (DEBUG) {
      DeviceConfiguration deviceConfiguration1 = this.devices.get(i);
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("[changeDevice] modules.get(port) name: ");
      stringBuilder.append(deviceConfiguration1.getName());
      stringBuilder.append(", port: ");
      stringBuilder.append(deviceConfiguration1.getPort());
      stringBuilder.append(", type: ");
      stringBuilder.append(deviceConfiguration1.getConfigurationType());
      RobotLog.e(stringBuilder.toString());
    } 
  }
  
  protected void clearDevice(View paramView) {
    int i = Integer.parseInt(((TextView)paramView.findViewById(R.id.portNumber)).getText().toString());
    EditText editText = (EditText)paramView.findViewById(R.id.editTextResult);
    editText.setEnabled(false);
    editText.setText(disabledDeviceName());
    DeviceConfiguration deviceConfiguration = new DeviceConfiguration((ConfigurationType)BuiltInConfigurationType.NOTHING);
    deviceConfiguration.setPort(i);
    setModule(deviceConfiguration);
    setButtonVisibility(i, 8);
  }
  
  public void editController_portALL(View paramView) {
    LinearLayout linearLayout = (LinearLayout)paramView.getParent().getParent();
    int i = Integer.parseInt(((TextView)linearLayout.findViewById(R.id.portNumber)).getText().toString());
    DeviceConfiguration deviceConfiguration = this.devices.get(i);
    if (!isController(deviceConfiguration))
      createController(i, ((EditActivity.ConfigurationTypeAndDisplayName)((Spinner)linearLayout.findViewById(R.id.choiceSpinner)).getSelectedItem()).configurationType); 
    editController_general(deviceConfiguration);
  }
  
  protected void finishOk() {
    this.controllerConfiguration.setName(this.controller_name.getText().toString());
    finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration, getRobotConfigMap()));
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    logActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == -1) {
      EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, paramIntent);
      if (RequestCode.fromValue(paramInt1) == EditSwapUsbDevices.requestCode) {
        completeSwapConfiguration(paramInt1, paramInt2, paramIntent);
      } else {
        ControllerConfiguration controllerConfiguration = (ControllerConfiguration)editParameters.getConfiguration();
        setModule((DeviceConfiguration)controllerConfiguration);
        populatePort(findViewByPort(controllerConfiguration.getPort()), this.devices.get(controllerConfiguration.getPort()));
      } 
      this.currentCfgFile.markDirty();
      this.robotConfigFileManager.setActiveConfigAndUpdateUI(this.currentCfgFile);
    } 
  }
  
  public void onCancelButtonPressed(View paramView) {
    finishCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.legacy);
    int j = R.id.linearLayout0;
    int i = 0;
    this.info_port0 = createPortView(j, 0);
    this.info_port1 = createPortView(R.id.linearLayout1, 1);
    this.info_port2 = createPortView(R.id.linearLayout2, 2);
    this.info_port3 = createPortView(R.id.linearLayout3, 3);
    this.info_port4 = createPortView(R.id.linearLayout4, 4);
    this.info_port5 = createPortView(R.id.linearLayout5, 5);
    this.controller_name = (EditText)findViewById(R.id.device_interface_module_name);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    this.devices = (ArrayList<DeviceConfiguration>)this.controllerConfiguration.getDevices();
    this.controller_name.setText(this.controllerConfiguration.getName());
    this.controller_name.addTextChangedListener(new UsefulTextWatcher());
    showFixSwapButtons();
    while (i < this.devices.size()) {
      DeviceConfiguration deviceConfiguration = this.devices.get(i);
      if (DEBUG) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[onStart] device name: ");
        stringBuilder.append(deviceConfiguration.getName());
        stringBuilder.append(", port: ");
        stringBuilder.append(deviceConfiguration.getPort());
        stringBuilder.append(", type: ");
        stringBuilder.append(deviceConfiguration.getConfigurationType());
        RobotLog.e(stringBuilder.toString());
      } 
      populatePort(findViewByPort(i), deviceConfiguration);
      i++;
    } 
  }
  
  public void onDoneButtonPressed(View paramView) {
    finishOk();
  }
  
  public void onFixButtonPressed(View paramView) {
    fixConfiguration();
  }
  
  protected void onStart() {
    super.onStart();
  }
  
  public void onSwapButtonPressed(View paramView) {
    swapConfiguration();
  }
  
  protected void refreshSerialNumber() {
    ((TextView)findViewById(R.id.serialNumber)).setText(formatSerialNumber((Context)this, this.controllerConfiguration));
  }
  
  private class UsefulTextWatcher implements TextWatcher {
    private boolean isController = false;
    
    private int port;
    
    private UsefulTextWatcher() {
      this.isController = true;
    }
    
    private UsefulTextWatcher(View param1View) {
      this.port = Integer.parseInt(((TextView)param1View.findViewById(R.id.portNumber)).getText().toString());
    }
    
    public void afterTextChanged(Editable param1Editable) {
      String str = param1Editable.toString();
      if (this.isController) {
        EditLegacyModuleControllerActivity.this.controllerConfiguration.setName(str);
        return;
      } 
      ((DeviceConfiguration)EditLegacyModuleControllerActivity.this.devices.get(this.port)).setName(str);
    }
    
    public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditLegacyModuleControllerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */