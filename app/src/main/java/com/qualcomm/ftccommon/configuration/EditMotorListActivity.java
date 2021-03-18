package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

public class EditMotorListActivity extends EditPortListSpinnerActivity<DeviceConfiguration> {
  ConfigurationType unspecifiedMotorType = (ConfigurationType)MotorConfigurationType.getUnspecifiedMotorType();
  
  protected ConfigurationType getDefaultEnabledSelection() {
    ConfigurationType configurationType = this.unspecifiedMotorType;
    return (configurationType != null) ? configurationType : super.getDefaultEnabledSelection();
  }
  
  protected ConfigurationType.DeviceFlavor getDeviceFlavorBeingConfigured() {
    return ConfigurationType.DeviceFlavor.MOTOR;
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditMotorListActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */