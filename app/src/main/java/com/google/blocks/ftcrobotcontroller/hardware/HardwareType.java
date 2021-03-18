package com.google.blocks.ftcrobotcontroller.hardware;

import com.google.blocks.ftcrobotcontroller.util.ToolboxFolder;
import com.google.blocks.ftcrobotcontroller.util.ToolboxIcon;
import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.hardware.lynx.LynxEmbeddedIMU;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsAnalogOpticalDistanceSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cCompassSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.hardware.rev.RevTouchSensor;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelImpl;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import java.util.Comparator;
import java.util.LinkedList;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public enum HardwareType {
  ACCELERATION_SENSOR("createAccelerationSensorDropdown", "accelerationSensor", "AsAccelerationSensor", "_AccelerationSensor", ToolboxFolder.SENSORS, "AccelerationSensor", ToolboxIcon.ACCELERATION_SENSOR, (Class)AccelerationSensor.class, new String[] { BuiltInConfigurationType.ACCELEROMETER.getXmlTag() }),
  ANALOG_INPUT("createAnalogInputDropdown", "analogInput", "AsAnalogInput", "_AnalogInput", ToolboxFolder.OTHER, "AnalogInput", ToolboxIcon.ANALOG_INPUT, (Class)AnalogInput.class, new String[] { ConfigurationTypeManager.getXmlTag(AnalogInput.class) }),
  ANALOG_OUTPUT("createAnalogOutputDropdown", "analogOutput", "AsAnalogOutput", "_AnalogOutput", ToolboxFolder.OTHER, "AnalogOutput", ToolboxIcon.ANALOG_OUTPUT, (Class)AnalogOutput.class, new String[] { BuiltInConfigurationType.ANALOG_OUTPUT.getXmlTag() }),
  BNO055IMU("createBNO055IMUDropdown", "bno055imu", "AsBNO055IMU", "_IMU_BNO055", ToolboxFolder.SENSORS, "IMU-BNO055", null, (Class)BNO055IMUImpl.class, new String[] { ConfigurationTypeManager.getXmlTag(AdafruitBNO055IMU.class), ConfigurationTypeManager.getXmlTag(LynxEmbeddedIMU.class) }),
  COLOR_RANGE_SENSOR("createColorRangeSensorDropdown", "lynxI2cColorRangeSensor", "AsREVColorRangeSensor", "_REV_ColorRangeSensor", ToolboxFolder.SENSORS, "REV Color/Range Sensor", ToolboxIcon.COLOR_SENSOR, (Class)ColorRangeSensor.class, new String[] { BuiltInConfigurationType.LYNX_COLOR_SENSOR.getXmlTag(), ConfigurationTypeManager.getXmlTag(RevColorSensorV3.class) }),
  COLOR_SENSOR("createColorSensorDropdown", "colorSensor", "AsColorSensor", "_ColorSensor", ToolboxFolder.SENSORS, "ColorSensor", ToolboxIcon.COLOR_SENSOR, (Class)ColorSensor.class, new String[] { BuiltInConfigurationType.COLOR_SENSOR.getXmlTag(), BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR.getXmlTag(), BuiltInConfigurationType.LYNX_COLOR_SENSOR.getXmlTag(), ConfigurationTypeManager.getXmlTag(RevColorSensorV3.class) }),
  COMPASS_SENSOR("createCompassSensorDropdown", "compassSensor", "AsCompassSensor", "_CompassSensor", ToolboxFolder.SENSORS, "CompassSensor", ToolboxIcon.COMPASS_SENSOR, (Class)CompassSensor.class, new String[] { BuiltInConfigurationType.COMPASS.getXmlTag() }),
  CR_SERVO("createCRServoDropdown", "crServo", "AsCRServo", "_CRServo", ToolboxFolder.ACTUATORS, "CRServo", ToolboxIcon.CR_SERVO, (Class)CRServo.class, getContinuousServoXmlTags()),
  DC_MOTOR("createDcMotorDropdown", "dcMotor", "AsDcMotor", "_DcMotor", ToolboxFolder.ACTUATORS, "DcMotor", ToolboxIcon.DC_MOTOR, (Class)DcMotor.class, getMotorXmlTags()),
  DIGITAL_CHANNEL("createDigitalChannelDropdown", "digitalChannel", "AsDigitalChannel", "_DigitalChannel", ToolboxFolder.OTHER, "DigitalChannel", ToolboxIcon.DIGITAL_CHANNEL, (Class)DigitalChannel.class, new String[] { ConfigurationTypeManager.getXmlTag(DigitalChannelImpl.class) }),
  DISTANCE_SENSOR("createDistanceSensorDropdown", "distanceSensor", "AsDistanceSensor", "_DistanceSensor", ToolboxFolder.SENSORS, "DistanceSensor", ToolboxIcon.ULTRASONIC_SENSOR, (Class)DistanceSensor.class, new String[] { BuiltInConfigurationType.LYNX_COLOR_SENSOR.getXmlTag(), ConfigurationTypeManager.getXmlTag(Rev2mDistanceSensor.class), ConfigurationTypeManager.getXmlTag(RevColorSensorV3.class) }),
  GYRO_SENSOR("createGyroSensorDropdown", "gyroSensor", "AsGyroSensor", "_GyroSensor", ToolboxFolder.SENSORS, "GyroSensor", ToolboxIcon.GYRO_SENSOR, (Class)GyroSensor.class, new String[] { BuiltInConfigurationType.GYRO.getXmlTag() }),
  IR_SEEKER_SENSOR("createIrSeekerSensorDropdown", "irSeekerSensor", "AsIrSeekerSensor", "_IrSeekerSensor", ToolboxFolder.SENSORS, "IrSeekerSensor", ToolboxIcon.IR_SEEKER_SENSOR, (Class)IrSeekerSensor.class, new String[] { BuiltInConfigurationType.IR_SEEKER.getXmlTag(), BuiltInConfigurationType.IR_SEEKER_V3.getXmlTag() }),
  LED("createLedDropdown", "led", "AsLED", "_LED", ToolboxFolder.OTHER, "LED", ToolboxIcon.LED, (Class)LED.class, new String[] { ConfigurationTypeManager.getXmlTag(LED.class) }),
  LIGHT_SENSOR("createLightSensorDropdown", "lightSensor", "AsLightSensor", "_LightSensor", ToolboxFolder.SENSORS, "LightSensor", ToolboxIcon.LIGHT_SENSOR, (Class)LightSensor.class, new String[] { BuiltInConfigurationType.LIGHT_SENSOR.getXmlTag() }),
  LYNX_MODULE(null, null, "AsREVModule", "_REV_Module", null, null, null, (Class)LynxModule.class, new String[] { BuiltInConfigurationType.LYNX_MODULE.getXmlTag() }),
  MR_I2C_COMPASS_SENSOR("createMrI2cCompassSensorDropdown", "mrI2cCompassSensor", "AsMrI2cCompassSensor", "_MR_I2cCompassSensor", ToolboxFolder.SENSORS, "MrI2cCompassSensor", ToolboxIcon.COMPASS_SENSOR, (Class)ModernRoboticsI2cCompassSensor.class, new String[] { ConfigurationTypeManager.getXmlTag(ModernRoboticsI2cCompassSensor.class) }),
  MR_I2C_RANGE_SENSOR("createMrI2cRangeSensorDropdown", "mrI2cRangeSensor", "AsMrI2cRangeSensor", "_MR_I2cRangeSensor", ToolboxFolder.SENSORS, "MrI2cRangeSensor", ToolboxIcon.OPTICAL_DISTANCE_SENSOR, (Class)ModernRoboticsI2cRangeSensor.class, new String[] { ConfigurationTypeManager.getXmlTag(ModernRoboticsI2cRangeSensor.class) }),
  OPTICAL_DISTANCE_SENSOR("createOpticalDistanceSensorDropdown", "opticalDistanceSensor", "AsOpticalDistanceSensor", "_OpticalDistanceSensor", ToolboxFolder.SENSORS, "OpticalDistanceSensor", ToolboxIcon.OPTICAL_DISTANCE_SENSOR, (Class)OpticalDistanceSensor.class, new String[] { ConfigurationTypeManager.getXmlTag(ModernRoboticsAnalogOpticalDistanceSensor.class), BuiltInConfigurationType.LYNX_COLOR_SENSOR.getXmlTag(), ConfigurationTypeManager.getXmlTag(RevColorSensorV3.class) }),
  REV_BLINKIN_LED_DRIVER("createRevBlinkinLedDriverDropdown", "revBlinkinLedDriver", "AsRevBlinkinLedDriver", "_RevBlinkinLedDriver", ToolboxFolder.ACTUATORS, "RevBlinkinLedDriver", ToolboxIcon.LED, (Class)RevBlinkinLedDriver.class, new String[] { ConfigurationTypeManager.getXmlTag(RevBlinkinLedDriver.class) }),
  SERVO("createServoDropdown", "servo", "AsServo", "_Servo", ToolboxFolder.ACTUATORS, "Servo", ToolboxIcon.SERVO, (Class)Servo.class, getStandardServoXmlTags()),
  SERVO_CONTROLLER("createServoControllerDropdown", "servoController", "AsServoController", "_ServoController", ToolboxFolder.ACTUATORS, "ServoController", ToolboxIcon.SERVO_CONTROLLER, (Class)ServoController.class, new String[] { BuiltInConfigurationType.SERVO_CONTROLLER.getXmlTag(), BuiltInConfigurationType.MATRIX_CONTROLLER.getXmlTag() }),
  TOUCH_SENSOR("createTouchSensorDropdown", "touchSensor", "AsTouchSensor", "_TouchSensor", ToolboxFolder.SENSORS, "TouchSensor", ToolboxIcon.TOUCH_SENSOR, (Class)TouchSensor.class, new String[] { BuiltInConfigurationType.TOUCH_SENSOR.getXmlTag(), ConfigurationTypeManager.getXmlTag(ModernRoboticsTouchSensor.class), ConfigurationTypeManager.getXmlTag(RevTouchSensor.class) }),
  ULTRASONIC_SENSOR("createUltrasonicSensorDropdown", "ultrasonicSensor", "AsUltrasonicSensor", "_UltrasonicSensor", ToolboxFolder.SENSORS, "UltrasonicSensor", ToolboxIcon.ULTRASONIC_SENSOR, (Class)UltrasonicSensor.class, new String[] { BuiltInConfigurationType.ULTRASONIC_SENSOR.getXmlTag() }),
  VOLTAGE_SENSOR("createVoltageSensorDropdown", "voltageSensor", "AsVoltageSensor", "_VoltageSensor", ToolboxFolder.SENSORS, "VoltageSensor", ToolboxIcon.VOLTAGE_SENSOR, (Class)VoltageSensor.class, new String[] { BuiltInConfigurationType.MOTOR_CONTROLLER.getXmlTag(), BuiltInConfigurationType.LYNX_MODULE.getXmlTag() }),
  WEBCAM_NAME("createVoltageSensorDropdown", "voltageSensor", "AsVoltageSensor", "_VoltageSensor", ToolboxFolder.SENSORS, "VoltageSensor", ToolboxIcon.VOLTAGE_SENSOR, (Class)VoltageSensor.class, new String[] { BuiltInConfigurationType.MOTOR_CONTROLLER.getXmlTag(), BuiltInConfigurationType.LYNX_MODULE.getXmlTag() });
  
  static final Comparator<HardwareType> BY_TOOLBOX_CATEGORY_NAME;
  
  public final String blockTypePrefix;
  
  public final String createDropdownFunctionName;
  
  public final Class<? extends HardwareDevice> deviceType;
  
  public final String identifierSuffixForFtcJava;
  
  public final String identifierSuffixForJavaScript;
  
  public final String toolboxCategoryName;
  
  public final ToolboxFolder toolboxFolder;
  
  public final ToolboxIcon toolboxIcon;
  
  public final String[] xmlTags;
  
  static {
    HardwareType hardwareType = new HardwareType("WEBCAM_NAME", 25, null, null, "AsWebcamName", "_WebcamName", null, null, null, (Class)WebcamName.class, new String[] { BuiltInConfigurationType.WEBCAM.getXmlTag() });
    WEBCAM_NAME = hardwareType;
    $VALUES = new HardwareType[] { 
        ACCELERATION_SENSOR, ANALOG_INPUT, ANALOG_OUTPUT, BNO055IMU, COLOR_RANGE_SENSOR, COLOR_SENSOR, COMPASS_SENSOR, CR_SERVO, DC_MOTOR, DIGITAL_CHANNEL, 
        DISTANCE_SENSOR, GYRO_SENSOR, IR_SEEKER_SENSOR, LED, LIGHT_SENSOR, LYNX_MODULE, MR_I2C_COMPASS_SENSOR, MR_I2C_RANGE_SENSOR, OPTICAL_DISTANCE_SENSOR, REV_BLINKIN_LED_DRIVER, 
        SERVO, SERVO_CONTROLLER, TOUCH_SENSOR, ULTRASONIC_SENSOR, VOLTAGE_SENSOR, hardwareType };
    BY_TOOLBOX_CATEGORY_NAME = new Comparator<HardwareType>() {
        public int compare(HardwareType param1HardwareType1, HardwareType param1HardwareType2) {
          String str1;
          String str2;
          String str4 = param1HardwareType1.toolboxCategoryName;
          String str3 = "";
          if (str4 == null) {
            str1 = "";
          } else {
            str1 = ((HardwareType)str1).toolboxCategoryName;
          } 
          if (param1HardwareType2.toolboxCategoryName == null) {
            str2 = str3;
          } else {
            str2 = ((HardwareType)str2).toolboxCategoryName;
          } 
          return str1.compareToIgnoreCase(str2);
        }
      };
  }
  
  HardwareType(String paramString1, String paramString2, String paramString3, String paramString4, ToolboxFolder paramToolboxFolder, String paramString5, ToolboxIcon paramToolboxIcon, Class<? extends HardwareDevice> paramClass, String... paramVarArgs) {
    if (paramString3 != null && !paramString3.isEmpty()) {
      if (paramString4 != null && !paramString4.isEmpty()) {
        this.createDropdownFunctionName = paramString1;
        this.blockTypePrefix = paramString2;
        this.identifierSuffixForJavaScript = paramString3;
        this.identifierSuffixForFtcJava = paramString4;
        this.toolboxFolder = paramToolboxFolder;
        this.toolboxCategoryName = paramString5;
        this.toolboxIcon = paramToolboxIcon;
        this.deviceType = paramClass;
        this.xmlTags = paramVarArgs;
        return;
      } 
      throw new IllegalArgumentException("identifierSuffixForFtcJava");
    } 
    throw new IllegalArgumentException("identifierSuffixForJavaScript");
  }
  
  private static String[] getContinuousServoXmlTags() {
    LinkedList<String> linkedList = new LinkedList();
    for (ConfigurationType configurationType : ConfigurationTypeManager.getInstance().getApplicableConfigTypes(ConfigurationType.DeviceFlavor.SERVO, null)) {
      if (configurationType == BuiltInConfigurationType.NOTHING || ((ServoConfigurationType)configurationType).getServoFlavor() != ServoFlavor.CONTINUOUS)
        continue; 
      linkedList.add(configurationType.getXmlTag());
      String[] arrayOfString = configurationType.getXmlTagAliases();
      int j = arrayOfString.length;
      for (int i = 0; i < j; i++)
        linkedList.add(arrayOfString[i]); 
    } 
    return linkedList.<String>toArray(new String[linkedList.size()]);
  }
  
  private static String[] getMotorXmlTags() {
    LinkedList<String> linkedList = new LinkedList();
    for (ConfigurationType configurationType : ConfigurationTypeManager.getInstance().getApplicableConfigTypes(ConfigurationType.DeviceFlavor.MOTOR, null)) {
      if (configurationType == BuiltInConfigurationType.NOTHING)
        continue; 
      linkedList.add(configurationType.getXmlTag());
      String[] arrayOfString = configurationType.getXmlTagAliases();
      int j = arrayOfString.length;
      for (int i = 0; i < j; i++)
        linkedList.add(arrayOfString[i]); 
    } 
    return linkedList.<String>toArray(new String[linkedList.size()]);
  }
  
  private static String[] getStandardServoXmlTags() {
    LinkedList<String> linkedList = new LinkedList();
    for (ConfigurationType configurationType : ConfigurationTypeManager.getInstance().getApplicableConfigTypes(ConfigurationType.DeviceFlavor.SERVO, null)) {
      if (configurationType == BuiltInConfigurationType.NOTHING || ((ServoConfigurationType)configurationType).getServoFlavor() != ServoFlavor.STANDARD)
        continue; 
      linkedList.add(configurationType.getXmlTag());
      String[] arrayOfString = configurationType.getXmlTagAliases();
      int j = arrayOfString.length;
      for (int i = 0; i < j; i++)
        linkedList.add(arrayOfString[i]); 
    } 
    return linkedList.<String>toArray(new String[linkedList.size()]);
  }
  
  boolean isContainer() {
    Class<? extends HardwareDevice> clazz = this.deviceType;
    return (clazz == LynxModule.class || clazz == ServoController.class);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\hardware\HardwareType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */