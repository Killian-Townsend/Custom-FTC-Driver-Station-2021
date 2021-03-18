package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@AnalogSensorType
@DeviceProperties(builtIn = true, name = "@string/configTypeAnalogInput", xmlTag = "AnalogInput")
public class AnalogInput implements HardwareDevice {
  private int channel = -1;
  
  private AnalogInputController controller = null;
  
  public AnalogInput(AnalogInputController paramAnalogInputController, int paramInt) {
    this.controller = paramAnalogInputController;
    this.channel = paramInt;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; analog port ");
    stringBuilder.append(this.channel);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeAnalogInput);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public double getMaxVoltage() {
    return this.controller.getMaxAnalogInputVoltage();
  }
  
  public int getVersion() {
    return 1;
  }
  
  public double getVoltage() {
    return this.controller.getAnalogInputVoltage(this.channel);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\AnalogInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */