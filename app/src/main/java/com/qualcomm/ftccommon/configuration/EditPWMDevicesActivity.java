package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

public class EditPWMDevicesActivity extends EditPortListCheckboxActivity<DeviceConfiguration> {
  public static final RequestCode requestCode = RequestCode.EDIT_PWM_PORT;
  
  public String getTag() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditPWMDevicesActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */