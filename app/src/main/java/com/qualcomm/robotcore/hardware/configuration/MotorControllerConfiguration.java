package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class MotorControllerConfiguration extends ControllerConfiguration<DeviceConfiguration> implements Serializable {
  public MotorControllerConfiguration() {
    super("", ConfigurationUtility.buildEmptyMotors(1, 2), SerialNumber.createFake(), BuiltInConfigurationType.MOTOR_CONTROLLER);
  }
  
  public MotorControllerConfiguration(String paramString, List<DeviceConfiguration> paramList, SerialNumber paramSerialNumber) {
    super(paramString, paramList, paramSerialNumber, BuiltInConfigurationType.MOTOR_CONTROLLER);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.MOTOR)) {
      DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getMotors().set(deviceConfiguration.getPort() - 1, deviceConfiguration);
    } 
  }
  
  public List<DeviceConfiguration> getMotors() {
    return (List)getDevices();
  }
  
  public void setMotors(List<DeviceConfiguration> paramList) {
    setDevices((List)paramList);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\MotorControllerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */