package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@AnalogSensorType
@DeviceProperties(builtIn = true, description = "@string/optical_distance_sensor_description", name = "@string/configTypeOpticalDistanceSensor", xmlTag = "OpticalDistanceSensor")
public class ModernRoboticsAnalogOpticalDistanceSensor implements OpticalDistanceSensor, AnalogSensor {
  protected static final double apiLevelMax = 1.0D;
  
  protected static final double apiLevelMin = 0.0D;
  
  private final AnalogInputController analogInputController;
  
  private final int physicalPort;
  
  public ModernRoboticsAnalogOpticalDistanceSensor(AnalogInputController paramAnalogInputController, int paramInt) {
    this.analogInputController = paramAnalogInputController;
    this.physicalPort = paramInt;
  }
  
  public void close() {}
  
  public void enableLed(boolean paramBoolean) {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.analogInputController.getConnectionInfo());
    stringBuilder.append("; analog port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeOpticalDistanceSensor);
  }
  
  public double getLightDetected() {
    return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public double getMaxVoltage() {
    return Math.min(5.0D, this.analogInputController.getMaxAnalogInputVoltage());
  }
  
  public double getRawLightDetected() {
    return Range.clip(readRawVoltage(), 0.0D, getMaxVoltage());
  }
  
  public double getRawLightDetectedMax() {
    return getMaxVoltage();
  }
  
  public int getVersion() {
    return 0;
  }
  
  public double readRawVoltage() {
    return this.analogInputController.getAnalogInputVoltage(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("Optical Distance Sensor, connected via device %s, port %d", new Object[] { this.analogInputController.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("OpticalDistanceSensor: %1.2f", new Object[] { Double.valueOf(getLightDetected()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsAnalogOpticalDistanceSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */