package com.qualcomm.ftccommon.configuration;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.qualcomm.ftccommon.R;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.WebcamConfiguration;

public class EditWebcamActivity extends EditUSBDeviceActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_USB_CAMERA;
  
  private EditText textCameraName;
  
  private WebcamConfiguration webcamConfiguration;
  
  protected void finishOk() {
    this.controllerConfiguration.setName(this.textCameraName.getText().toString());
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
      } else if (requestCode == requestCode) {
        WebcamConfiguration webcamConfiguration = (WebcamConfiguration)editParameters.getConfiguration();
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
    setContentView(R.layout.webcam_device);
    this.textCameraName = (EditText)findViewById(R.id.cameraName);
    deserialize(EditParameters.fromIntent(this, getIntent()));
    this.webcamConfiguration = (WebcamConfiguration)this.controllerConfiguration;
    this.textCameraName.addTextChangedListener(new EditActivity.SetNameTextWatcher(this, (DeviceConfiguration)this.controllerConfiguration));
    this.textCameraName.setText(this.controllerConfiguration.getName());
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
  
  protected void refreshSerialNumber() {
    ((TextView)findViewById(R.id.serialNumber)).setText(formatSerialNumber((Context)this, this.controllerConfiguration));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditWebcamActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */