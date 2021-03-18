package com.qualcomm.robotcore.hardware.configuration;

import android.content.Context;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public enum BuiltInConfigurationType implements ConfigurationType {
  ACCELEROMETER,
  ADAFRUIT_COLOR_SENSOR,
  ANALOG_OUTPUT,
  COLOR_SENSOR,
  COMPASS,
  DEVICE_INTERFACE_MODULE,
  GYRO("Gyro", ConfigurationType.DeviceFlavor.I2C),
  I2C_DEVICE("Gyro", ConfigurationType.DeviceFlavor.I2C),
  I2C_DEVICE_SYNCH("Gyro", ConfigurationType.DeviceFlavor.I2C),
  IR_SEEKER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  IR_SEEKER_V3("Gyro", ConfigurationType.DeviceFlavor.I2C),
  LEGACY_MODULE_CONTROLLER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  LIGHT_SENSOR("Gyro", ConfigurationType.DeviceFlavor.I2C),
  LYNX_COLOR_SENSOR("Gyro", ConfigurationType.DeviceFlavor.I2C),
  LYNX_MODULE("Gyro", ConfigurationType.DeviceFlavor.I2C),
  LYNX_USB_DEVICE("Gyro", ConfigurationType.DeviceFlavor.I2C),
  MATRIX_CONTROLLER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  MOTOR_CONTROLLER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  NOTHING("Gyro", ConfigurationType.DeviceFlavor.I2C),
  PULSE_WIDTH_DEVICE("Gyro", ConfigurationType.DeviceFlavor.I2C),
  ROBOT("Gyro", ConfigurationType.DeviceFlavor.I2C),
  SERVO_CONTROLLER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  TOUCH_SENSOR("Gyro", ConfigurationType.DeviceFlavor.I2C),
  TOUCH_SENSOR_MULTIPLEXER("Gyro", ConfigurationType.DeviceFlavor.I2C),
  ULTRASONIC_SENSOR("Gyro", ConfigurationType.DeviceFlavor.I2C),
  UNKNOWN("Gyro", ConfigurationType.DeviceFlavor.I2C),
  WEBCAM("Gyro", ConfigurationType.DeviceFlavor.I2C);
  
  private static final List<BuiltInConfigurationType> valuesCache;
  
  private final Context context = AppUtil.getDefContext();
  
  private final ConfigurationType.DeviceFlavor deviceFlavor;
  
  private final String xmlTag;
  
  static {
    COMPASS = new BuiltInConfigurationType("COMPASS", 1, "Compass", null);
    IR_SEEKER = new BuiltInConfigurationType("IR_SEEKER", 2, "IrSeeker", null);
    LIGHT_SENSOR = new BuiltInConfigurationType("LIGHT_SENSOR", 3, "LightSensor", null);
    ACCELEROMETER = new BuiltInConfigurationType("ACCELEROMETER", 4, "Accelerometer", null);
    MOTOR_CONTROLLER = new BuiltInConfigurationType("MOTOR_CONTROLLER", 5, "MotorController", null);
    SERVO_CONTROLLER = new BuiltInConfigurationType("SERVO_CONTROLLER", 6, "ServoController", null);
    LEGACY_MODULE_CONTROLLER = new BuiltInConfigurationType("LEGACY_MODULE_CONTROLLER", 7, "LegacyModuleController", null);
    DEVICE_INTERFACE_MODULE = new BuiltInConfigurationType("DEVICE_INTERFACE_MODULE", 8, "DeviceInterfaceModule", null);
    I2C_DEVICE = new BuiltInConfigurationType("I2C_DEVICE", 9, "I2cDevice", ConfigurationType.DeviceFlavor.I2C);
    I2C_DEVICE_SYNCH = new BuiltInConfigurationType("I2C_DEVICE_SYNCH", 10, "I2cDeviceSynch", ConfigurationType.DeviceFlavor.I2C);
    TOUCH_SENSOR = new BuiltInConfigurationType("TOUCH_SENSOR", 11, "TouchSensor", ConfigurationType.DeviceFlavor.DIGITAL_IO);
    ANALOG_OUTPUT = new BuiltInConfigurationType("ANALOG_OUTPUT", 12, "AnalogOutput", ConfigurationType.DeviceFlavor.ANALOG_OUTPUT);
    PULSE_WIDTH_DEVICE = new BuiltInConfigurationType("PULSE_WIDTH_DEVICE", 13, "PulseWidthDevice", null);
    IR_SEEKER_V3 = new BuiltInConfigurationType("IR_SEEKER_V3", 14, "IrSeekerV3", ConfigurationType.DeviceFlavor.I2C);
    TOUCH_SENSOR_MULTIPLEXER = new BuiltInConfigurationType("TOUCH_SENSOR_MULTIPLEXER", 15, "TouchSensorMultiplexer", null);
    MATRIX_CONTROLLER = new BuiltInConfigurationType("MATRIX_CONTROLLER", 16, "MatrixController", null);
    ULTRASONIC_SENSOR = new BuiltInConfigurationType("ULTRASONIC_SENSOR", 17, "UltrasonicSensor", null);
    ADAFRUIT_COLOR_SENSOR = new BuiltInConfigurationType("ADAFRUIT_COLOR_SENSOR", 18, "AdafruitColorSensor", ConfigurationType.DeviceFlavor.I2C);
    COLOR_SENSOR = new BuiltInConfigurationType("COLOR_SENSOR", 19, "ColorSensor", ConfigurationType.DeviceFlavor.I2C);
    LYNX_COLOR_SENSOR = new BuiltInConfigurationType("LYNX_COLOR_SENSOR", 20, "LynxColorSensor", ConfigurationType.DeviceFlavor.I2C);
    LYNX_USB_DEVICE = new BuiltInConfigurationType("LYNX_USB_DEVICE", 21, "LynxUsbDevice", null);
    LYNX_MODULE = new BuiltInConfigurationType("LYNX_MODULE", 22, "LynxModule", null);
    WEBCAM = new BuiltInConfigurationType("WEBCAM", 23, "Webcam", null);
    ROBOT = new BuiltInConfigurationType("ROBOT", 24, "Robot", null);
    NOTHING = new BuiltInConfigurationType("NOTHING", 25, "Nothing", null);
    BuiltInConfigurationType builtInConfigurationType = new BuiltInConfigurationType("UNKNOWN", 26, "<unknown>", null);
    UNKNOWN = builtInConfigurationType;
    $VALUES = new BuiltInConfigurationType[] { 
        GYRO, COMPASS, IR_SEEKER, LIGHT_SENSOR, ACCELEROMETER, MOTOR_CONTROLLER, SERVO_CONTROLLER, LEGACY_MODULE_CONTROLLER, DEVICE_INTERFACE_MODULE, I2C_DEVICE, 
        I2C_DEVICE_SYNCH, TOUCH_SENSOR, ANALOG_OUTPUT, PULSE_WIDTH_DEVICE, IR_SEEKER_V3, TOUCH_SENSOR_MULTIPLEXER, MATRIX_CONTROLLER, ULTRASONIC_SENSOR, ADAFRUIT_COLOR_SENSOR, COLOR_SENSOR, 
        LYNX_COLOR_SENSOR, LYNX_USB_DEVICE, LYNX_MODULE, WEBCAM, ROBOT, NOTHING, builtInConfigurationType };
    valuesCache = Collections.unmodifiableList(Arrays.asList(values()));
  }
  
  BuiltInConfigurationType(String paramString1, ConfigurationType.DeviceFlavor paramDeviceFlavor) {
    this.xmlTag = paramString1;
    this.deviceFlavor = paramDeviceFlavor;
  }
  
  public static ConfigurationType fromString(String paramString) {
    for (ConfigurationType configurationType : valuesCache) {
      if (paramString.equalsIgnoreCase(configurationType.toString()))
        return configurationType; 
    } 
    return UNKNOWN;
  }
  
  public static ConfigurationType fromUSBDeviceType(DeviceManager.UsbDeviceType paramUsbDeviceType) {
    switch (paramUsbDeviceType) {
      default:
        return UNKNOWN;
      case null:
        return WEBCAM;
      case null:
        return LYNX_USB_DEVICE;
      case null:
        return LEGACY_MODULE_CONTROLLER;
      case null:
        return DEVICE_INTERFACE_MODULE;
      case null:
        return SERVO_CONTROLLER;
      case null:
        break;
    } 
    return MOTOR_CONTROLLER;
  }
  
  public static BuiltInConfigurationType fromXmlTag(String paramString) {
    for (BuiltInConfigurationType builtInConfigurationType : valuesCache) {
      if (paramString.equalsIgnoreCase(builtInConfigurationType.xmlTag))
        return builtInConfigurationType; 
    } 
    return UNKNOWN;
  }
  
  public ConfigurationType.DeviceFlavor getDeviceFlavor() {
    ConfigurationType.DeviceFlavor deviceFlavor = this.deviceFlavor;
    return (deviceFlavor != null) ? deviceFlavor : ConfigurationType.DeviceFlavor.BUILT_IN;
  }
  
  public String getDisplayName(ConfigurationType.DisplayNameFlavor paramDisplayNameFlavor) {
    switch (this) {
      default:
        return this.context.getString(R.string.configTypeUnknown);
      case null:
        return (paramDisplayNameFlavor == ConfigurationType.DisplayNameFlavor.Legacy) ? this.context.getString(R.string.configTypeHTColorSensor) : this.context.getString(R.string.configTypeMRColorSensor);
      case null:
        return (paramDisplayNameFlavor == ConfigurationType.DisplayNameFlavor.Legacy) ? this.context.getString(R.string.configTypeHTGyro) : this.context.getString(R.string.configTypeMRGyro);
      case null:
        return (paramDisplayNameFlavor == ConfigurationType.DisplayNameFlavor.Legacy) ? this.context.getString(R.string.configTypeNXTTouchSensor) : this.context.getString(R.string.configTypeMRTouchSensor);
      case null:
        return this.context.getString(R.string.configTypeNothing);
      case null:
        return this.context.getString(R.string.configTypeLynxModule);
      case null:
        return this.context.getString(R.string.configTypeLynxColorSensor);
      case null:
        return this.context.getString(R.string.configTypeAdafruitColorSensor);
      case null:
        return this.context.getString(R.string.configTypeNXTUltrasonicSensor);
      case null:
        return this.context.getString(R.string.configTypeMatrixController);
      case null:
        return this.context.getString(R.string.configTypeHTTouchSensorMultiplexer);
      case null:
        return this.context.getString(R.string.configTypeIrSeekerV3);
      case null:
        return this.context.getString(R.string.configTypePulseWidthDevice);
      case null:
        return this.context.getString(R.string.configTypeAnalogOutput);
      case null:
        return this.context.getString(R.string.configTypeI2cDeviceSynch);
      case null:
        return this.context.getString(R.string.configTypeI2cDevice);
      case null:
        return this.context.getString(R.string.configTypeHTAccelerometer);
      case null:
        return this.context.getString(R.string.configTypeHTLightSensor);
      case null:
        return this.context.getString(R.string.configTypeHTIrSeeker);
      case null:
        return this.context.getString(R.string.configTypeHTCompass);
      case null:
        return this.context.getString(R.string.configTypeWebcam);
      case null:
        return this.context.getString(R.string.configTypeLynxUSBDevice);
      case null:
        return this.context.getString(R.string.configTypeLegacyModuleController);
      case null:
        return this.context.getString(R.string.configTypeDeviceInterfaceModule);
      case null:
        return this.context.getString(R.string.configTypeServoController);
      case null:
        break;
    } 
    return this.context.getString(R.string.configTypeMotorController);
  }
  
  public String getXmlTag() {
    return this.xmlTag;
  }
  
  public String[] getXmlTagAliases() {
    return new String[0];
  }
  
  public boolean isDeprecated() {
    try {
      return BuiltInConfigurationType.class.getField(toString()).isAnnotationPresent((Class)Deprecated.class);
    } catch (NoSuchFieldException noSuchFieldException) {
      RobotLog.logStackTrace(noSuchFieldException);
      return false;
    } 
  }
  
  public boolean isDeviceFlavor(ConfigurationType.DeviceFlavor paramDeviceFlavor) {
    return (paramDeviceFlavor == ConfigurationType.DeviceFlavor.BUILT_IN) ? true : ((paramDeviceFlavor == this.deviceFlavor));
  }
  
  public DeviceManager.UsbDeviceType toUSBDeviceType() {
    switch (this) {
      default:
        return DeviceManager.UsbDeviceType.FTDI_USB_UNKNOWN_DEVICE;
      case null:
        return DeviceManager.UsbDeviceType.WEBCAM;
      case null:
        return DeviceManager.UsbDeviceType.LYNX_USB_DEVICE;
      case null:
        return DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE;
      case null:
        return DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE;
      case null:
        return DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER;
      case null:
        break;
    } 
    return DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\BuiltInConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */