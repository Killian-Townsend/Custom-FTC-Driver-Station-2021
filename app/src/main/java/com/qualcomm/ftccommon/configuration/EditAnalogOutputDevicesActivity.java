package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;

public class EditAnalogOutputDevicesActivity extends EditPortListSpinnerActivity {
  public static final RequestCode requestCode = RequestCode.EDIT_ANALOG_OUTPUT;
  
  protected ConfigurationType.DeviceFlavor getDeviceFlavorBeingConfigured() {
    return ConfigurationType.DeviceFlavor.ANALOG_OUTPUT;
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditAnalogOutputDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */