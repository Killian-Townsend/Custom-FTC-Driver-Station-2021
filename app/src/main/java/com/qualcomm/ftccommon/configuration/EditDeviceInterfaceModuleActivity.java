package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import java.util.List;

public class EditDeviceInterfaceModuleActivity extends EditUSBDeviceActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_DEVICE_INTERFACE_MODULE;
  
  private DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration;
  
  private EditText device_interface_module_name;
  
  private AdapterView.OnItemClickListener editLaunchListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        EditActivity.DisplayNameAndRequestCode displayNameAndRequestCode = EditDeviceInterfaceModuleActivity.this.listKeys[param1Int];
        if (displayNameAndRequestCode.requestCode == EditPWMDevicesActivity.requestCode) {
          EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(displayNameAndRequestCode.requestCode, EditPWMDevicesActivity.class, EditDeviceInterfaceModuleActivity.this.deviceInterfaceModuleConfiguration.getPwmOutputs());
          return;
        } 
        if (displayNameAndRequestCode.requestCode == EditI2cDevicesActivity.requestCode) {
          EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity = EditDeviceInterfaceModuleActivity.this;
          EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>(editDeviceInterfaceModuleActivity, DeviceConfiguration.class, editDeviceInterfaceModuleActivity.deviceInterfaceModuleConfiguration.getI2cDevices(), 6);
          editParameters.setControlSystem(ControlSystem.MODERN_ROBOTICS);
          EditDeviceInterfaceModuleActivity.this.handleLaunchEdit(displayNameAndRequestCode.requestCode, EditI2cDevicesActivity.class, editParameters);
          return;
        } 
        if (displayNameAndRequestCode.requestCode == EditAnalogInputDevicesActivity.requestCode) {
          EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity = EditDeviceInterfaceModuleActivity.this;
          editDeviceInterfaceModuleActivity.editSimple(displayNameAndRequestCode, EditAnalogInputDevicesActivity.class, editDeviceInterfaceModuleActivity.deviceInterfaceModuleConfiguration.getAnalogInputDevices());
          return;
        } 
        if (displayNameAndRequestCode.requestCode == EditDigitalDevicesActivity.requestCode) {
          EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity = EditDeviceInterfaceModuleActivity.this;
          editDeviceInterfaceModuleActivity.editSimple(displayNameAndRequestCode, EditDigitalDevicesActivity.class, editDeviceInterfaceModuleActivity.deviceInterfaceModuleConfiguration.getDigitalDevices());
          return;
        } 
        if (displayNameAndRequestCode.requestCode == EditAnalogOutputDevicesActivity.requestCode) {
          EditDeviceInterfaceModuleActivity editDeviceInterfaceModuleActivity = EditDeviceInterfaceModuleActivity.this;
          editDeviceInterfaceModuleActivity.editSimple(displayNameAndRequestCode, EditAnalogOutputDevicesActivity.class, editDeviceInterfaceModuleActivity.deviceInterfaceModuleConfiguration.getAnalogOutputDevices());
        } 
      }
    };
  
  private EditActivity.DisplayNameAndRequestCode[] listKeys;
  
  private void editSimple(EditActivity.DisplayNameAndRequestCode paramDisplayNameAndRequestCode, Class paramClass, List<DeviceConfiguration> paramList) {
    EditParameters editParameters = initParameters(paramList);
    handleLaunchEdit(paramDisplayNameAndRequestCode.requestCode, paramClass, editParameters);
  }
  
  private EditParameters initParameters(List<DeviceConfiguration> paramList) {
    EditParameters<DeviceConfiguration> editParameters = new EditParameters<DeviceConfiguration>(this, DeviceConfiguration.class, paramList);
    editParameters.setControlSystem(ControlSystem.MODERN_ROBOTICS);
    return editParameters;
  }
  
  protected void finishOk() {
    this.controllerConfiguration.setName(this.device_interface_module_name.getText().toString());
    finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration, getRobotConfigMap()));
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    logActivityResult(paramInt1, paramInt2, paramIntent);
    RequestCode requestCode = RequestCode.fromValue(paramInt1);
    if (paramInt2 == -1) {
      EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, paramIntent);
      if (requestCode == EditSwapUsbDevices.requestCode) {
        completeSwapConfiguration(paramInt1, paramInt2, paramIntent);
      } else if (requestCode == EditPWMDevicesActivity.requestCode) {
        this.deviceInterfaceModuleConfiguration.setPwmOutputs(editParameters.getCurrentItems());
      } else if (requestCode == EditAnalogInputDevicesActivity.requestCode) {
        this.deviceInterfaceModuleConfiguration.setAnalogInputDevices(editParameters.getCurrentItems());
      } else if (requestCode == EditDigitalDevicesActivity.requestCode) {
        this.deviceInterfaceModuleConfiguration.setDigitalDevices(editParameters.getCurrentItems());
      } else if (requestCode == EditI2cDevicesActivity.requestCode) {
        this.deviceInterfaceModuleConfiguration.setI2cDevices(editParameters.getCurrentItems());
      } else if (requestCode == EditAnalogOutputDevicesActivity.requestCode) {
        this.deviceInterfaceModuleConfiguration.setAnalogOutputDevices(editParameters.getCurrentItems());
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
    setContentView(R.layout.device_interface_module);
    this.listKeys = EditActivity.DisplayNameAndRequestCode.fromArray(getResources().getStringArray(R.array.device_interface_module_options_array));
    ListView listView = (ListView)findViewById(R.id.listView_devices);
    listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 17367043, (Object[])this.listKeys));
    listView.setOnItemClickListener(this.editLaunchListener);
    this.device_interface_module_name = (EditText)findViewById(R.id.device_interface_module_name);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    this.device_interface_module_name.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, (DeviceConfiguration)this.controllerConfiguration));
    this.device_interface_module_name.setText(this.controllerConfiguration.getName());
    showFixSwapButtons();
    this.deviceInterfaceModuleConfiguration = (DeviceInterfaceModuleConfiguration)this.controllerConfiguration;
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
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditDeviceInterfaceModuleActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */