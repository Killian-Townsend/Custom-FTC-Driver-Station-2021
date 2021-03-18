package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@DeviceProperties(builtIn = true, description = "@string/led_description", name = "@string/configTypeLED", xmlTag = "Led")
@DigitalIoDeviceType
public class LED implements HardwareDevice, SwitchableLight {
  private DigitalChannelController controller = null;
  
  private int physicalPort = -1;
  
  public LED(DigitalChannelController paramDigitalChannelController, int paramInt) {
    this.controller = paramDigitalChannelController;
    this.physicalPort = paramInt;
    paramDigitalChannelController.setDigitalChannelMode(paramInt, DigitalChannel.Mode.OUTPUT);
  }
  
  public void close() {}
  
  public void enable(boolean paramBoolean) {
    this.controller.setDigitalChannelState(this.physicalPort, paramBoolean);
  }
  
  public void enableLight(boolean paramBoolean) {
    enable(paramBoolean);
  }
  
  public String getConnectionInfo() {
    return String.format("%s; port %d", new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeLED);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getVersion() {
    return 0;
  }
  
  public boolean isLightOn() {
    return this.controller.getDigitalChannelState(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\LED.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */