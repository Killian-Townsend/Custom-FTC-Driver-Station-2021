package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.AnalogSensorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.DigitalIoDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.I2cDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;

public interface DeviceManager {
  ColorSensor createAdafruitI2cColorSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  ColorSensor createAdafruitI2cColorSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  AnalogOutput createAnalogOutputDevice(AnalogOutputController paramAnalogOutputController, int paramInt, String paramString);
  
  HardwareDevice createAnalogSensor(AnalogInputController paramAnalogInputController, int paramInt, AnalogSensorConfigurationType paramAnalogSensorConfigurationType);
  
  CRServo createCRServo(ServoController paramServoController, int paramInt, String paramString);
  
  CRServo createCRServoEx(ServoControllerEx paramServoControllerEx, int paramInt, String paramString, ServoConfigurationType paramServoConfigurationType);
  
  HardwareDevice createCustomServoDevice(ServoController paramServoController, int paramInt, ServoConfigurationType paramServoConfigurationType);
  
  DcMotor createDcMotor(DcMotorController paramDcMotorController, int paramInt, MotorConfigurationType paramMotorConfigurationType, String paramString);
  
  DcMotor createDcMotorEx(DcMotorController paramDcMotorController, int paramInt, MotorConfigurationType paramMotorConfigurationType, String paramString);
  
  DeviceInterfaceModule createDeviceInterfaceModule(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  HardwareDevice createDigitalDevice(DigitalChannelController paramDigitalChannelController, int paramInt, DigitalIoDeviceConfigurationType paramDigitalIoDeviceConfigurationType);
  
  AccelerationSensor createHTAccelerationSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  ColorSensor createHTColorSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  CompassSensor createHTCompassSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  DcMotorController createHTDcMotorController(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  GyroSensor createHTGyroSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  IrSeekerSensor createHTIrSeekerSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  LightSensor createHTLightSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  ServoController createHTServoController(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  TouchSensorMultiplexer createHTTouchSensorMultiplexer(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  I2cDevice createI2cDevice(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  I2cDeviceSynch createI2cDeviceSynch(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  LED createLED(DigitalChannelController paramDigitalChannelController, int paramInt, String paramString);
  
  ColorSensor createLynxColorRangeSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  HardwareDevice createLynxCustomServoDevice(ServoControllerEx paramServoControllerEx, int paramInt, ServoConfigurationType paramServoConfigurationType);
  
  RobotCoreLynxModule createLynxModule(RobotCoreLynxUsbDevice paramRobotCoreLynxUsbDevice, int paramInt, boolean paramBoolean, String paramString);
  
  RobotCoreLynxUsbDevice createLynxUsbDevice(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  TouchSensor createMRDigitalTouchSensor(DigitalChannelController paramDigitalChannelController, int paramInt, String paramString);
  
  IrSeekerSensor createMRI2cIrSeekerSensorV3(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  IrSeekerSensor createMRI2cIrSeekerSensorV3(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  ColorSensor createModernRoboticsI2cColorSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  ColorSensor createModernRoboticsI2cColorSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  GyroSensor createModernRoboticsI2cGyroSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  GyroSensor createModernRoboticsI2cGyroSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString);
  
  TouchSensor createNxtTouchSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  UltrasonicSensor createNxtUltrasonicSensor(LegacyModule paramLegacyModule, int paramInt, String paramString);
  
  PWMOutput createPwmOutputDevice(PWMOutputController paramPWMOutputController, int paramInt, String paramString);
  
  Servo createServo(ServoController paramServoController, int paramInt, String paramString);
  
  Servo createServoEx(ServoControllerEx paramServoControllerEx, int paramInt, String paramString, ServoConfigurationType paramServoConfigurationType);
  
  DcMotorController createUsbDcMotorController(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  LegacyModule createUsbLegacyModule(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  ServoController createUsbServoController(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  HardwareDevice createUserI2cDevice(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, I2cDeviceConfigurationType paramI2cDeviceConfigurationType, String paramString);
  
  HardwareDevice createUserI2cDevice(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, I2cDeviceConfigurationType paramI2cDeviceConfigurationType, String paramString);
  
  WebcamName createWebcamName(SerialNumber paramSerialNumber, String paramString) throws RobotCoreException, InterruptedException;
  
  ScannedDevices scanForUsbDevices() throws RobotCoreException;
  
  public enum UsbDeviceType {
    FTDI_USB_UNKNOWN_DEVICE, LYNX_USB_DEVICE, MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, MODERN_ROBOTICS_USB_LEGACY_MODULE, MODERN_ROBOTICS_USB_SENSOR_MUX, MODERN_ROBOTICS_USB_SERVO_CONTROLLER, MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, UNKNOWN_DEVICE, WEBCAM;
    
    static {
      MODERN_ROBOTICS_USB_LEGACY_MODULE = new UsbDeviceType("MODERN_ROBOTICS_USB_LEGACY_MODULE", 4);
      MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE = new UsbDeviceType("MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE", 5);
      MODERN_ROBOTICS_USB_SENSOR_MUX = new UsbDeviceType("MODERN_ROBOTICS_USB_SENSOR_MUX", 6);
      LYNX_USB_DEVICE = new UsbDeviceType("LYNX_USB_DEVICE", 7);
      WEBCAM = new UsbDeviceType("WEBCAM", 8);
      UsbDeviceType usbDeviceType = new UsbDeviceType("UNKNOWN_DEVICE", 9);
      UNKNOWN_DEVICE = usbDeviceType;
      $VALUES = new UsbDeviceType[] { FTDI_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_UNKNOWN_DEVICE, MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER, MODERN_ROBOTICS_USB_SERVO_CONTROLLER, MODERN_ROBOTICS_USB_LEGACY_MODULE, MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE, MODERN_ROBOTICS_USB_SENSOR_MUX, LYNX_USB_DEVICE, WEBCAM, usbDeviceType };
    }
    
    public static UsbDeviceType from(String param1String) {
      for (UsbDeviceType usbDeviceType : values()) {
        if (usbDeviceType.toString().equals(param1String))
          return usbDeviceType; 
      } 
      return UNKNOWN_DEVICE;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\DeviceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */