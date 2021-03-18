package com.qualcomm.ftccommon.configuration;

import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import java.util.List;

public abstract class EditPortListSpinnerActivity<ITEM_T extends DeviceConfiguration> extends EditPortListActivity<ITEM_T> {
  protected ControlSystem controlSystem;
  
  protected int idItemSpinner;
  
  private void handleDisabledDevice(View paramView, DeviceConfiguration paramDeviceConfiguration) {
    EditText editText = (EditText)paramView.findViewById(this.idItemEditTextResult);
    if (paramDeviceConfiguration.isEnabled()) {
      editText.setText(paramDeviceConfiguration.getName());
      editText.setEnabled(true);
      return;
    } 
    editText.setText(disabledDeviceName());
    editText.setEnabled(false);
  }
  
  protected void addViewListenersOnIndex(int paramInt) {
    View view = findViewByIndex(paramInt);
    DeviceConfiguration deviceConfiguration = findConfigByIndex(paramInt);
    addNameTextChangeWatcherOnIndex(paramInt);
    handleDisabledDevice(view, deviceConfiguration);
    handleSpinner(view, this.idItemSpinner, deviceConfiguration);
  }
  
  protected void changeDevice(View paramView, ConfigurationType paramConfigurationType) {
    int i = Integer.parseInt(((TextView)paramView.findViewById(this.idItemPortNumber)).getText().toString());
    EditText editText = (EditText)paramView.findViewById(this.idItemEditTextResult);
    editText.setEnabled(true);
    DeviceConfiguration deviceConfiguration = findConfigByPort(i);
    clearNameIfNecessary(editText, deviceConfiguration);
    deviceConfiguration.setConfigurationType(paramConfigurationType);
    deviceConfiguration.setEnabled(true);
  }
  
  protected void clearDevice(View paramView) {
    int i = Integer.parseInt(((TextView)paramView.findViewById(this.idItemPortNumber)).getText().toString());
    EditText editText = (EditText)paramView.findViewById(this.idItemEditTextResult);
    editText.setEnabled(false);
    editText.setText(disabledDeviceName());
    findConfigByPort(i).setEnabled(false);
  }
  
  protected View createItemViewForPort(int paramInt) {
    View view = super.createItemViewForPort(paramInt);
    localizeSpinner(view);
    return view;
  }
  
  protected void deserialize(EditParameters paramEditParameters) {
    super.deserialize(paramEditParameters);
    this.controlSystem = paramEditParameters.getControlSystem();
  }
  
  protected abstract ConfigurationType.DeviceFlavor getDeviceFlavorBeingConfigured();
  
  protected void localizeSpinner(View paramView) {
    Spinner spinner = (Spinner)paramView.findViewById(this.idItemSpinner);
    List<ConfigurationType> list = ConfigurationTypeManager.getInstance().getApplicableConfigTypes(getDeviceFlavorBeingConfigured(), this.controlSystem);
    localizeConfigTypeSpinnerTypes(ConfigurationType.DisplayNameFlavor.Normal, spinner, list);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditPortListSpinnerActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */