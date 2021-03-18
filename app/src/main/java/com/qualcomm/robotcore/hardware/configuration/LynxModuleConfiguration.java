package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.usb.LynxModuleSerialNumber;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LynxModuleConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  public static final String TAG = "LynxModuleConfiguration";
  
  private List<DeviceConfiguration> analogInputs = new LinkedList<DeviceConfiguration>();
  
  private List<DeviceConfiguration> digitalDevices = new LinkedList<DeviceConfiguration>();
  
  private List<LynxI2cDeviceConfiguration> i2cDevices = new LinkedList<LynxI2cDeviceConfiguration>();
  
  private boolean isParent = false;
  
  private List<DeviceConfiguration> motors = new LinkedList<DeviceConfiguration>();
  
  private List<DeviceConfiguration> pwmOutputs = new LinkedList<DeviceConfiguration>();
  
  private List<DeviceConfiguration> servos = new LinkedList<DeviceConfiguration>();
  
  private SerialNumber usbDeviceSerialNumber = SerialNumber.createFake();
  
  public LynxModuleConfiguration() {
    this("");
  }
  
  public LynxModuleConfiguration(String paramString) {
    super(paramString, new ArrayList<DeviceConfiguration>(), SerialNumber.createFake(), BuiltInConfigurationType.LYNX_MODULE);
    this.servos = ConfigurationUtility.buildEmptyServos(0, 6);
    this.motors = ConfigurationUtility.buildEmptyMotors(0, 4);
    this.pwmOutputs = ConfigurationUtility.buildEmptyDevices(0, 4, BuiltInConfigurationType.NOTHING);
    this.analogInputs = ConfigurationUtility.buildEmptyDevices(0, 4, BuiltInConfigurationType.NOTHING);
    this.digitalDevices = ConfigurationUtility.buildEmptyDevices(0, 8, BuiltInConfigurationType.NOTHING);
    this.i2cDevices = new LinkedList<LynxI2cDeviceConfiguration>();
  }
  
  protected void deserializeAttributes(XmlPullParser paramXmlPullParser) {
    super.deserializeAttributes(paramXmlPullParser);
    setModuleAddress(getPort());
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    DeviceConfiguration deviceConfiguration;
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.SERVO)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getServos().set(deviceConfiguration.getPort() + 0, deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.MOTOR)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getMotors().set(deviceConfiguration.getPort() + 0, deviceConfiguration);
      return;
    } 
    if (deviceConfiguration == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getPwmOutputs().set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_SENSOR)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getAnalogInputs().set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.DIGITAL_IO)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getDigitalDevices().set(deviceConfiguration.getPort(), deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.I2C)) {
      deviceConfiguration = new LynxI2cDeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getI2cDevices().add(deviceConfiguration);
    } 
  }
  
  public List<DeviceConfiguration> getAnalogInputs() {
    return this.analogInputs;
  }
  
  public List<DeviceConfiguration> getDigitalDevices() {
    return this.digitalDevices;
  }
  
  public List<LynxI2cDeviceConfiguration> getI2cDevices() {
    return this.i2cDevices;
  }
  
  public List<LynxI2cDeviceConfiguration> getI2cDevices(int paramInt) {
    LinkedList<LynxI2cDeviceConfiguration> linkedList = new LinkedList();
    for (LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration : this.i2cDevices) {
      if (lynxI2cDeviceConfiguration.getBus() == paramInt)
        linkedList.add(lynxI2cDeviceConfiguration); 
    } 
    return linkedList;
  }
  
  public int getModuleAddress() {
    return getPort();
  }
  
  public SerialNumber getModuleSerialNumber() {
    return getSerialNumber();
  }
  
  public List<DeviceConfiguration> getMotors() {
    return this.motors;
  }
  
  public List<DeviceConfiguration> getPwmOutputs() {
    return this.pwmOutputs;
  }
  
  public List<DeviceConfiguration> getServos() {
    return this.servos;
  }
  
  public SerialNumber getUsbDeviceSerialNumber() {
    return this.usbDeviceSerialNumber;
  }
  
  public boolean isParent() {
    return this.isParent;
  }
  
  public void setAnalogInputs(List<DeviceConfiguration> paramList) {
    this.analogInputs = paramList;
  }
  
  public void setDigitalDevices(List<DeviceConfiguration> paramList) {
    this.digitalDevices = paramList;
  }
  
  public void setI2cDevices(int paramInt, List<LynxI2cDeviceConfiguration> paramList) {
    LinkedList<LynxI2cDeviceConfiguration> linkedList = new LinkedList();
    for (LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration : this.i2cDevices) {
      if (lynxI2cDeviceConfiguration.getBus() != paramInt)
        linkedList.add(lynxI2cDeviceConfiguration); 
    } 
    for (LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration : paramList) {
      if (lynxI2cDeviceConfiguration.isEnabled()) {
        lynxI2cDeviceConfiguration.setBus(paramInt);
        linkedList.add(lynxI2cDeviceConfiguration);
      } 
    } 
    this.i2cDevices = linkedList;
  }
  
  public void setI2cDevices(List<LynxI2cDeviceConfiguration> paramList) {
    this.i2cDevices = new LinkedList<LynxI2cDeviceConfiguration>();
    for (LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration : paramList) {
      if (lynxI2cDeviceConfiguration.isEnabled() && lynxI2cDeviceConfiguration.getPort() >= 0 && lynxI2cDeviceConfiguration.getPort() < 4)
        this.i2cDevices.add(lynxI2cDeviceConfiguration); 
    } 
  }
  
  public void setIsParent(boolean paramBoolean) {
    this.isParent = paramBoolean;
  }
  
  public void setModuleAddress(int paramInt) {
    setPort(paramInt);
  }
  
  public void setMotors(List<DeviceConfiguration> paramList) {
    this.motors = paramList;
  }
  
  public void setPort(int paramInt) {
    super.setPort(paramInt);
    setSerialNumber((SerialNumber)new LynxModuleSerialNumber(this.usbDeviceSerialNumber, paramInt));
  }
  
  public void setPwmOutputs(List<DeviceConfiguration> paramList) {
    this.pwmOutputs = paramList;
  }
  
  public void setSerialNumber(SerialNumber paramSerialNumber) {
    super.setSerialNumber(paramSerialNumber);
  }
  
  public void setServos(List<DeviceConfiguration> paramList) {
    this.servos = paramList;
  }
  
  public void setUsbDeviceSerialNumber(SerialNumber paramSerialNumber) {
    this.usbDeviceSerialNumber = paramSerialNumber;
    setSerialNumber((SerialNumber)new LynxModuleSerialNumber(paramSerialNumber, getModuleAddress()));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\LynxModuleConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */