package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class DeviceInterfaceModuleConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  private List<DeviceConfiguration> analogInputDevices = ConfigurationUtility.buildEmptyDevices(0, 8, BuiltInConfigurationType.NOTHING);
  
  private List<DeviceConfiguration> analogOutputDevices = ConfigurationUtility.buildEmptyDevices(0, 2, BuiltInConfigurationType.NOTHING);
  
  private List<DeviceConfiguration> digitalDevices = ConfigurationUtility.buildEmptyDevices(0, 8, BuiltInConfigurationType.NOTHING);
  
  private List<DeviceConfiguration> i2cDevices = ConfigurationUtility.buildEmptyDevices(0, 6, BuiltInConfigurationType.I2C_DEVICE);
  
  private List<DeviceConfiguration> pwmOutputs = ConfigurationUtility.buildEmptyDevices(0, 2, BuiltInConfigurationType.PULSE_WIDTH_DEVICE);
  
  public DeviceInterfaceModuleConfiguration() {
    this("", SerialNumber.createFake());
  }
  
  public DeviceInterfaceModuleConfiguration(String paramString, SerialNumber paramSerialNumber) {
    super(paramString, paramSerialNumber, BuiltInConfigurationType.DEVICE_INTERFACE_MODULE);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    DeviceConfiguration deviceConfiguration;
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      this.pwmOutputs.set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.I2C)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      this.i2cDevices.set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_SENSOR)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      this.analogInputDevices.set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.DIGITAL_IO)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      this.digitalDevices.set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_OUTPUT)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      this.analogOutputDevices.set(deviceConfiguration.getPort(), deviceConfiguration);
    } 
  }
  
  public List<DeviceConfiguration> getAnalogInputDevices() {
    return this.analogInputDevices;
  }
  
  public List<DeviceConfiguration> getAnalogOutputDevices() {
    return this.analogOutputDevices;
  }
  
  public List<DeviceConfiguration> getDigitalDevices() {
    return this.digitalDevices;
  }
  
  public List<DeviceConfiguration> getI2cDevices() {
    return this.i2cDevices;
  }
  
  public List<DeviceConfiguration> getPwmOutputs() {
    return this.pwmOutputs;
  }
  
  public void setAnalogInputDevices(List<DeviceConfiguration> paramList) {
    this.analogInputDevices = paramList;
  }
  
  public void setAnalogOutputDevices(List<DeviceConfiguration> paramList) {
    this.analogOutputDevices = paramList;
  }
  
  public void setDigitalDevices(List<DeviceConfiguration> paramList) {
    this.digitalDevices = paramList;
  }
  
  public void setI2cDevices(List<DeviceConfiguration> paramList) {
    this.i2cDevices = paramList;
  }
  
  public void setPwmOutputs(List<DeviceConfiguration> paramList) {
    this.pwmOutputs = paramList;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\DeviceInterfaceModuleConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */