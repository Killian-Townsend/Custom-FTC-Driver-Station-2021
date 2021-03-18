package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AnalogOutput implements HardwareDevice {
  private int channel = -1;
  
  private AnalogOutputController controller = null;
  
  public AnalogOutput(AnalogOutputController paramAnalogOutputController, int paramInt) {
    this.controller = paramAnalogOutputController;
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
    return AppUtil.getDefContext().getString(R.string.configTypeAnalogOutput);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setAnalogOutputFrequency(int paramInt) {
    this.controller.setAnalogOutputFrequency(this.channel, paramInt);
  }
  
  public void setAnalogOutputMode(byte paramByte) {
    this.controller.setAnalogOutputMode(this.channel, paramByte);
  }
  
  public void setAnalogOutputVoltage(int paramInt) {
    this.controller.setAnalogOutputVoltage(this.channel, paramInt);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\AnalogOutput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */