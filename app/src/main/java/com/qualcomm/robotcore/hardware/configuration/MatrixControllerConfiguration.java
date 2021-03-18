package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MatrixControllerConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  private List<DeviceConfiguration> motors;
  
  private List<DeviceConfiguration> servos;
  
  public MatrixControllerConfiguration() {
    this("", ConfigurationUtility.buildEmptyMotors(1, 4), ConfigurationUtility.buildEmptyServos(1, 4), SerialNumber.createFake());
  }
  
  public MatrixControllerConfiguration(String paramString, List<DeviceConfiguration> paramList1, List<DeviceConfiguration> paramList2, SerialNumber paramSerialNumber) {
    super(paramString, paramSerialNumber, BuiltInConfigurationType.MATRIX_CONTROLLER);
    this.servos = paramList2;
    this.motors = paramList1;
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    DeviceConfiguration deviceConfiguration;
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.SERVO)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getServos().set(deviceConfiguration.getPort() - 1, deviceConfiguration);
      return;
    } 
    if (deviceConfiguration.isDeviceFlavor(ConfigurationType.DeviceFlavor.MOTOR)) {
      deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getMotors().set(deviceConfiguration.getPort() - 1, deviceConfiguration);
    } 
  }
  
  public List<DeviceConfiguration> getMotors() {
    return this.motors;
  }
  
  public List<DeviceConfiguration> getServos() {
    return this.servos;
  }
  
  public void setMotors(List<DeviceConfiguration> paramList) {
    this.motors = paramList;
  }
  
  public void setServos(List<DeviceConfiguration> paramList) {
    this.servos = paramList;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\MatrixControllerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */