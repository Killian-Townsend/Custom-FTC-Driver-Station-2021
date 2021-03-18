package com.qualcomm.ftccommon.configuration;

import android.view.View;
import android.widget.Spinner;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.LynxI2cDeviceConfiguration;
import java.util.List;

public class EditI2cDevicesActivityLynx extends EditI2cDevicesActivityAbstract<LynxI2cDeviceConfiguration> {
  private int i2cBus;
  
  protected void deserialize(EditParameters paramEditParameters) {
    super.deserialize(paramEditParameters);
    this.i2cBus = paramEditParameters.getI2cBus();
  }
  
  public String getTag() {
    return getClass().getSimpleName();
  }
  
  protected void localizeSpinner(View paramView) {
    Spinner spinner = (Spinner)paramView.findViewById(this.idItemSpinner);
    List<ConfigurationType> list = ConfigurationTypeManager.getInstance().getApplicableConfigTypes(ConfigurationType.DeviceFlavor.I2C, this.controlSystem, this.i2cBus);
    localizeConfigTypeSpinnerTypes(ConfigurationType.DisplayNameFlavor.Normal, spinner, list);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\EditI2cDevicesActivityLynx.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */