package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@AnalogSensorType
@DeviceProperties(builtIn = true, description = "@string/mr_touch_sensor_description", name = "@string/configTypeMRTouchSensor", xmlTag = "ModernRoboticsAnalogTouchSensor")
public class ModernRoboticsTouchSensor implements TouchSensor {
  private AnalogInputController analogInputController = null;
  
  private double analogThreshold;
  
  private DigitalChannelController digitalController = null;
  
  private int physicalPort = -1;
  
  public ModernRoboticsTouchSensor(AnalogInputController paramAnalogInputController, int paramInt) {
    this.analogInputController = paramAnalogInputController;
    this.physicalPort = paramInt;
    this.analogThreshold = paramAnalogInputController.getMaxAnalogInputVoltage() / 2.0D;
  }
  
  public ModernRoboticsTouchSensor(DigitalChannelController paramDigitalChannelController, int paramInt) {
    this.digitalController = paramDigitalChannelController;
    this.physicalPort = paramInt;
    paramDigitalChannelController.setDigitalChannelMode(paramInt, DigitalChannel.Mode.INPUT);
  }
  
  public void close() {}
  
  public double getAnalogVoltageThreshold() {
    return this.analogThreshold;
  }
  
  public String getConnectionInfo() {
    if (isDigital()) {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(this.digitalController.getConnectionInfo());
      stringBuilder1.append("; digital port ");
      stringBuilder1.append(this.physicalPort);
      return stringBuilder1.toString();
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.analogInputController.getConnectionInfo());
    stringBuilder.append("; analog port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeMRTouchSensor);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public double getValue() {
    return isPressed() ? 1.0D : 0.0D;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isAnalog() {
    return isDigital() ^ true;
  }
  
  public boolean isDigital() {
    return (this.digitalController != null);
  }
  
  public boolean isPressed() {
    return isDigital() ? this.digitalController.getDigitalChannelState(this.physicalPort) : ((this.analogInputController.getAnalogInputVoltage(this.physicalPort) > getAnalogVoltageThreshold()));
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setAnalogVoltageThreshold(double paramDouble) {
    this.analogThreshold = paramDouble;
  }
  
  public String toString() {
    return String.format("Touch Sensor: %1.2f", new Object[] { Double.valueOf(getValue()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsTouchSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */