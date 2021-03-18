package com.qualcomm.ftccommon.configuration;

import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;

public abstract class EditI2cDevicesActivityAbstract<ITEM_T extends DeviceConfiguration> extends EditPortListSpinnerActivity<ITEM_T> {
  protected ConfigurationType.DeviceFlavor getDeviceFlavorBeingConfigured() {
    return ConfigurationType.DeviceFlavor.I2C;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditI2cDevicesActivityAbstract.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */