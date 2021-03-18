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
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxUsbDeviceConfiguration;
import java.util.List;

public class EditLynxUsbDeviceActivity extends EditUSBDeviceActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_LYNX_USB_DEVICE;
  
  private AdapterView.OnItemClickListener editLaunchListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        EditActivity.DisplayNameAndInteger displayNameAndInteger = EditLynxUsbDeviceActivity.this.listKeys[param1Int];
        EditLynxUsbDeviceActivity.this.handleLaunchEdit(EditLynxModuleActivity.requestCode, EditLynxModuleActivity.class, EditLynxUsbDeviceActivity.this.lynxUsbDeviceConfiguration.getModules().get(displayNameAndInteger.value));
      }
    };
  
  private EditActivity.DisplayNameAndInteger[] listKeys;
  
  private LynxUsbDeviceConfiguration lynxUsbDeviceConfiguration;
  
  private EditText textLynxUsbDeviceName;
  
  protected void finishOk() {
    this.controllerConfiguration.setName(this.textLynxUsbDeviceName.getText().toString());
    finishOk(new EditParameters<DeviceConfiguration>(this, (DeviceConfiguration)this.controllerConfiguration, getRobotConfigMap()));
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    logActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt2 == -1) {
      EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, paramIntent);
      RequestCode requestCode = RequestCode.fromValue(paramInt1);
      if (requestCode == EditSwapUsbDevices.requestCode) {
        completeSwapConfiguration(paramInt1, paramInt2, paramIntent);
      } else if (requestCode == EditLynxModuleActivity.requestCode) {
        LynxModuleConfiguration lynxModuleConfiguration = (LynxModuleConfiguration)editParameters.getConfiguration();
        if (lynxModuleConfiguration != null) {
          for (paramInt1 = 0; paramInt1 < this.lynxUsbDeviceConfiguration.getModules().size(); paramInt1++) {
            if (((LynxModuleConfiguration)this.lynxUsbDeviceConfiguration.getModules().get(paramInt1)).getModuleAddress() == lynxModuleConfiguration.getModuleAddress()) {
              this.lynxUsbDeviceConfiguration.getModules().set(paramInt1, lynxModuleConfiguration);
              break;
            } 
          } 
          populateModules();
        } 
      } 
      this.currentCfgFile.markDirty();
      this.robotConfigFileManager.setActiveConfig(this.currentCfgFile);
    } 
  }
  
  public void onCancelButtonPressed(View paramView) {
    finishCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(R.layout.lynx_usb_device);
    ((ListView)findViewById(R.id.lynxUsbDeviceModules)).setOnItemClickListener(this.editLaunchListener);
    this.textLynxUsbDeviceName = (EditText)findViewById(R.id.lynxUsbDeviceName);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    this.lynxUsbDeviceConfiguration = (LynxUsbDeviceConfiguration)this.controllerConfiguration;
    this.textLynxUsbDeviceName.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, (DeviceConfiguration)this.controllerConfiguration));
    this.textLynxUsbDeviceName.setText(this.controllerConfiguration.getName());
    populateModules();
    showFixSwapButtons();
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
  
  protected void populateModules() {
    ListView listView = (ListView)findViewById(R.id.lynxUsbDeviceModules);
    List<LynxModuleConfiguration> list = this.lynxUsbDeviceConfiguration.getModules();
    this.listKeys = new EditActivity.DisplayNameAndInteger[list.size()];
    int i = 0;
    while (true) {
      EditActivity.DisplayNameAndInteger[] arrayOfDisplayNameAndInteger = this.listKeys;
      if (i < arrayOfDisplayNameAndInteger.length) {
        arrayOfDisplayNameAndInteger[i] = new EditActivity.DisplayNameAndInteger(((LynxModuleConfiguration)list.get(i)).getName(), i);
        i++;
        continue;
      } 
      listView.setAdapter((ListAdapter)new ArrayAdapter((Context)this, 17367043, (Object[])this.listKeys));
      return;
    } 
  }
  
  protected void refreshSerialNumber() {
    ((TextView)findViewById(R.id.serialNumber)).setText(formatSerialNumber((Context)this, this.controllerConfiguration));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditLynxUsbDeviceActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */