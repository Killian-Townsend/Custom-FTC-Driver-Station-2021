package com.qualcomm.ftccommon.configuration;

import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

public abstract class EditPortListCheckboxActivity<ITEM_T extends DeviceConfiguration> extends EditPortListActivity<ITEM_T> {
  protected int idItemCheckbox;
  
  protected void addCheckBoxListenerOnIndex(int paramInt) {
    View view = findViewByIndex(paramInt);
    final EditText name = (EditText)view.findViewById(this.idItemEditTextResult);
    final DeviceConfiguration device = (DeviceConfiguration)this.itemList.get(paramInt);
    ((CheckBox)view.findViewById(this.idItemCheckbox)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (((CheckBox)param1View).isChecked()) {
              name.setEnabled(true);
              name.setText("");
              device.setEnabled(true);
              device.setName("");
              return;
            } 
            name.setEnabled(false);
            name.setText(EditPortListCheckboxActivity.this.disabledDeviceName());
            device.setEnabled(false);
            device.setName(EditPortListCheckboxActivity.this.disabledDeviceName());
          }
        });
  }
  
  protected void addViewListenersOnIndex(int paramInt) {
    addCheckBoxListenerOnIndex(paramInt);
    addNameTextChangeWatcherOnIndex(paramInt);
    handleDisabledDeviceByIndex(paramInt);
  }
  
  protected void handleDisabledDeviceByIndex(int paramInt) {
    View view = findViewByIndex(paramInt);
    CheckBox checkBox = (CheckBox)view.findViewById(this.idItemCheckbox);
    DeviceConfiguration deviceConfiguration = (DeviceConfiguration)this.itemList.get(paramInt);
    if (deviceConfiguration.isEnabled()) {
      checkBox.setChecked(true);
      ((EditText)view.findViewById(this.idItemEditTextResult)).setText(deviceConfiguration.getName());
      return;
    } 
    checkBox.setChecked(true);
    checkBox.performClick();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditPortListCheckboxActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */