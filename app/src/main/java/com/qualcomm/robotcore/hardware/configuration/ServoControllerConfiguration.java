package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ServoControllerConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  public ServoControllerConfiguration() {
    super("", ConfigurationUtility.buildEmptyServos(1, 6), SerialNumber.createFake(), BuiltInConfigurationType.SERVO_CONTROLLER);
  }
  
  public ServoControllerConfiguration(String paramString, List<DeviceConfiguration> paramList, SerialNumber paramSerialNumber) {
    super(paramString, paramList, paramSerialNumber, BuiltInConfigurationType.SERVO_CONTROLLER);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (paramConfigurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.SERVO)) {
      DeviceConfiguration deviceConfiguration = new DeviceConfiguration();
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getServos().set(deviceConfiguration.getPort() - 1, deviceConfiguration);
    } 
  }
  
  public List<DeviceConfiguration> getServos() {
    return (List)getDevices();
  }
  
  public void setServos(List<DeviceConfiguration> paramList) {
    setDevices((List)paramList);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ServoControllerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */