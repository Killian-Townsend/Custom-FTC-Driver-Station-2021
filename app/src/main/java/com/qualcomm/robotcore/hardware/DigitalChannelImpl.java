package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@DeviceProperties(builtIn = true, name = "@string/configTypeDigitalDevice", xmlTag = "DigitalDevice")
@DigitalIoDeviceType
public class DigitalChannelImpl implements DigitalChannel {
  private int channel = -1;
  
  private DigitalChannelController controller = null;
  
  public DigitalChannelImpl(DigitalChannelController paramDigitalChannelController, int paramInt) {
    this.controller = paramDigitalChannelController;
    this.channel = paramInt;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; digital port ");
    stringBuilder.append(this.channel);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeDigitalDevice);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public DigitalChannel.Mode getMode() {
    return this.controller.getDigitalChannelMode(this.channel);
  }
  
  public boolean getState() {
    return this.controller.getDigitalChannelState(this.channel);
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setMode(DigitalChannel.Mode paramMode) {
    this.controller.setDigitalChannelMode(this.channel, paramMode);
  }
  
  @Deprecated
  public void setMode(DigitalChannelController.Mode paramMode) {
    this.controller.setDigitalChannelMode(this.channel, paramMode);
  }
  
  public void setState(boolean paramBoolean) {
    this.controller.setDigitalChannelState(this.channel, paramBoolean);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DigitalChannelImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */