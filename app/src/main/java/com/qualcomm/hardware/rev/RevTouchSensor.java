package com.qualcomm.hardware.rev;

import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/rev_touch_sensor_description", name = "@string/configTypeRevTouchSensor", xmlTag = "RevTouchSensor")
@DigitalIoDeviceType
public class RevTouchSensor implements TouchSensor {
  private final DigitalChannelController digitalChannelController;
  
  private final int physicalPort;
  
  public RevTouchSensor(DigitalChannelController paramDigitalChannelController, int paramInt) {
    this.digitalChannelController = paramDigitalChannelController;
    this.physicalPort = paramInt;
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.digitalChannelController.getConnectionInfo());
    stringBuilder.append("; digital channel ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return "REV Touch Sensor";
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  public double getValue() {
    return isPressed() ? 1.0D : 0.0D;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isPressed() {
    return this.digitalChannelController.getDigitalChannelState(this.physicalPort) ^ true;
  }
  
  public void resetDeviceConfigurationForOpMode() {
    this.digitalChannelController.setDigitalChannelMode(this.physicalPort, DigitalChannel.Mode.INPUT);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\rev\RevTouchSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */