package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.util.RobotLog;

public class EditSwapUsbDevices extends EditActivity {
  public static final String TAG = "EditSwapUsbDevices";
  
  public static final RequestCode requestCode = RequestCode.EDIT_SWAP_USB_DEVICES;
  
  protected ControllerConfiguration targetConfiguration;
  
  protected void doBackOrCancel() {
    finishCancel();
  }
  
  public String getTag() {
    return "EditSwapUsbDevices";
  }
  
  public void onBackPressed() {
    RobotLog.vv("EditSwapUsbDevices", "onBackPressed()");
    doBackOrCancel();
  }
  
  public void onCancelButtonPressed(View paramView) {
    RobotLog.vv("EditSwapUsbDevices", "onCancelButtonPressed()");
    doBackOrCancel();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    RobotLog.vv("EditSwapUsbDevices", "onCreate()");
    setContentView(R.layout.activity_swap_usb_devices);
    EditParameters<DeviceConfiguration> editParameters = EditParameters.fromIntent(this, getIntent());
    deserialize(editParameters);
    this.targetConfiguration = (ControllerConfiguration)editParameters.getConfiguration();
    String str = String.format(getString(R.string.swapPrompt), new Object[] { this.targetConfiguration.getName() });
    ((TextView)findViewById(R.id.swapCaption)).setText(str);
    ((Button)findViewById(R.id.doneButton)).setVisibility(8);
    populateList();
  }
  
  protected void populateList() {
    ListView listView = (ListView)findViewById(R.id.controllersList);
    listView.setAdapter(new DeviceInfoAdapter(this, 17367044, getRobotConfigMap().getEligibleSwapTargets(this.targetConfiguration, this.scannedDevices, (Context)this)));
    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
          public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            ControllerConfiguration controllerConfiguration = (ControllerConfiguration)param1AdapterView.getItemAtPosition(param1Int);
            EditSwapUsbDevices.this.finishOk(new EditParameters<DeviceConfiguration>(EditSwapUsbDevices.this, (DeviceConfiguration)controllerConfiguration));
          }
        });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditSwapUsbDevices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */