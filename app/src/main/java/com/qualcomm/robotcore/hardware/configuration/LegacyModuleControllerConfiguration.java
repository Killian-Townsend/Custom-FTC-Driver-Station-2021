package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LegacyModuleControllerConfiguration extends ControllerConfiguration<DeviceConfiguration> {
  private static final ConfigurationType[] simpleLegacyTypes = new ConfigurationType[] { BuiltInConfigurationType.COMPASS, BuiltInConfigurationType.LIGHT_SENSOR, BuiltInConfigurationType.IR_SEEKER, BuiltInConfigurationType.ACCELEROMETER, BuiltInConfigurationType.GYRO, BuiltInConfigurationType.TOUCH_SENSOR, BuiltInConfigurationType.TOUCH_SENSOR_MULTIPLEXER, BuiltInConfigurationType.ULTRASONIC_SENSOR, BuiltInConfigurationType.COLOR_SENSOR };
  
  public LegacyModuleControllerConfiguration() {
    super("", ConfigurationUtility.buildEmptyDevices(0, 6, BuiltInConfigurationType.NOTHING), SerialNumber.createFake(), BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER);
  }
  
  public LegacyModuleControllerConfiguration(String paramString, List<DeviceConfiguration> paramList, SerialNumber paramSerialNumber) {
    super(paramString, paramList, paramSerialNumber, BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER);
  }
  
  protected void deserializeChildElement(ConfigurationType paramConfigurationType, XmlPullParser paramXmlPullParser, ReadXMLFileHandler paramReadXMLFileHandler) throws IOException, XmlPullParserException, RobotCoreException {
    DeviceConfiguration deviceConfiguration;
    super.deserializeChildElement(paramConfigurationType, paramXmlPullParser, paramReadXMLFileHandler);
    if (Arrays.<ConfigurationType>asList(simpleLegacyTypes).contains(paramConfigurationType)) {
      deviceConfiguration = new DeviceConfiguration();
    } else if (deviceConfiguration == BuiltInConfigurationType.MOTOR_CONTROLLER) {
      deviceConfiguration = new MotorControllerConfiguration();
    } else if (deviceConfiguration == BuiltInConfigurationType.SERVO_CONTROLLER) {
      deviceConfiguration = new ServoControllerConfiguration();
    } else if (deviceConfiguration == BuiltInConfigurationType.MATRIX_CONTROLLER) {
      deviceConfiguration = new MatrixControllerConfiguration();
    } else {
      deviceConfiguration = null;
    } 
    if (deviceConfiguration != null) {
      deviceConfiguration.deserialize(paramXmlPullParser, paramReadXMLFileHandler);
      getDevices().set(deviceConfiguration.getPort(), deviceConfiguration);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\LegacyModuleControllerConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */