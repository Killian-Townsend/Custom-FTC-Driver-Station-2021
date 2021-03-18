package com.qualcomm.hardware;

import android.content.Context;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.hardware.lynx.EmbeddedControlHubModule;
import com.qualcomm.hardware.lynx.LynxAnalogInputController;
import com.qualcomm.hardware.lynx.LynxDcMotorController;
import com.qualcomm.hardware.lynx.LynxDigitalChannelController;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.LynxServoController;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxVoltageSensor;
import com.qualcomm.hardware.matrix.MatrixDcMotorController;
import com.qualcomm.hardware.matrix.MatrixMasterController;
import com.qualcomm.hardware.matrix.MatrixServoController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.configuration.BuiltInConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.DeviceInterfaceModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LegacyModuleControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.LynxI2cDeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxModuleConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxUsbDeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MatrixControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.MotorControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ReadXMLFileHandler;
import com.qualcomm.robotcore.hardware.configuration.ServoControllerConfiguration;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.WebcamConfiguration;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.AnalogSensorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.DigitalIoDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.I2cDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.xmlpull.v1.XmlPullParser;

public class HardwareFactory {
  public static final String TAG = "HardwareFactory";
  
  private Context context;
  
  private XmlPullParser xmlPullParser = null;
  
  public HardwareFactory(Context paramContext) {
    this.context = paramContext;
  }
  
  private void addUserDeviceToMap(HardwareMap paramHardwareMap, DeviceConfiguration paramDeviceConfiguration, HardwareDevice paramHardwareDevice) {
    paramHardwareMap.put(paramDeviceConfiguration.getName(), paramHardwareDevice);
    for (HardwareMap.DeviceMapping<HardwareDevice> deviceMapping : (Iterable<HardwareMap.DeviceMapping<HardwareDevice>>)paramHardwareMap.allDeviceMappings) {
      if (deviceMapping.getDeviceTypeClass().isInstance(paramHardwareDevice))
        maybeAddToMapping(deviceMapping, paramDeviceConfiguration.getName(), deviceMapping.cast(paramHardwareDevice)); 
    } 
  }
  
  private void buildDevices(List<DeviceConfiguration> paramList, HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DeviceInterfaceModule paramDeviceInterfaceModule) {
    for (DeviceConfiguration deviceConfiguration : paramList) {
      ConfigurationType configurationType = deviceConfiguration.getConfigurationType();
      if (configurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_SENSOR)) {
        mapAnalogSensor(paramHardwareMap, paramDeviceManager, (AnalogInputController)paramDeviceInterfaceModule, deviceConfiguration);
        continue;
      } 
      if (configurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.DIGITAL_IO)) {
        mapDigitalDevice(paramHardwareMap, paramDeviceManager, (DigitalChannelController)paramDeviceInterfaceModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.PULSE_WIDTH_DEVICE) {
        mapPwmOutputDevice(paramHardwareMap, paramDeviceManager, (PWMOutputController)paramDeviceInterfaceModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.ANALOG_OUTPUT) {
        mapAnalogOutputDevice(paramHardwareMap, paramDeviceManager, (AnalogOutputController)paramDeviceInterfaceModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.NOTHING)
        continue; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected device type connected to Device Interface Module while parsing XML: ");
      stringBuilder.append(configurationType.toString());
      RobotLog.w(stringBuilder.toString());
    } 
  }
  
  private void buildI2cDevices(List<DeviceConfiguration> paramList, HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController) {
    for (DeviceConfiguration deviceConfiguration : paramList) {
      ConfigurationType configurationType = deviceConfiguration.getConfigurationType();
      if (configurationType == BuiltInConfigurationType.I2C_DEVICE) {
        mapI2cDevice(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.I2C_DEVICE_SYNCH) {
        mapI2cDeviceSynch(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.IR_SEEKER_V3) {
        mapIrSeekerV3Device(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR) {
        mapAdafruitColorSensor(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.COLOR_SENSOR) {
        mapModernRoboticsColorSensor(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.GYRO) {
        mapModernRoboticsGyro(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.NOTHING)
        continue; 
      if (configurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.I2C) && configurationType instanceof I2cDeviceConfigurationType) {
        mapUserI2cDevice(paramHardwareMap, paramDeviceManager, paramI2cController, deviceConfiguration);
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected device type connected to I2c Controller while parsing XML: ");
      stringBuilder.append(configurationType.toString());
      RobotLog.w(stringBuilder.toString());
    } 
  }
  
  private void buildLynxDevices(List<DeviceConfiguration> paramList, HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, AnalogInputController paramAnalogInputController) {
    for (DeviceConfiguration deviceConfiguration : paramList) {
      if (deviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_SENSOR))
        mapAnalogSensor(paramHardwareMap, paramDeviceManager, paramAnalogInputController, deviceConfiguration); 
    } 
  }
  
  private void buildLynxDevices(List<DeviceConfiguration> paramList, HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DigitalChannelController paramDigitalChannelController) {
    for (DeviceConfiguration deviceConfiguration : paramList) {
      if (deviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.DIGITAL_IO))
        mapDigitalDevice(paramHardwareMap, paramDeviceManager, paramDigitalChannelController, deviceConfiguration); 
    } 
  }
  
  private void buildLynxI2cDevices(List<LynxI2cDeviceConfiguration> paramList, HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule) {
    for (LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration : paramList) {
      ConfigurationType configurationType = lynxI2cDeviceConfiguration.getConfigurationType();
      if (configurationType == BuiltInConfigurationType.I2C_DEVICE_SYNCH) {
        mapI2cDeviceSynch(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.IR_SEEKER_V3) {
        mapIrSeekerV3Device(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR) {
        mapAdafruitColorSensor(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.LYNX_COLOR_SENSOR) {
        mapLynxColorSensor(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.COLOR_SENSOR) {
        mapModernRoboticsColorSensor(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.GYRO) {
        mapModernRoboticsGyro(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.NOTHING)
        continue; 
      if (configurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.I2C)) {
        if (configurationType instanceof I2cDeviceConfigurationType)
          mapUserI2cDevice(paramHardwareMap, paramDeviceManager, paramLynxModule, (DeviceConfiguration)lynxI2cDeviceConfiguration); 
        continue;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected device type connected to I2c Controller while parsing XML: ");
      stringBuilder.append(configurationType.toString());
      RobotLog.w(stringBuilder.toString());
    } 
  }
  
  private void connectModule(LynxUsbDevice paramLynxUsbDevice, LynxModule paramLynxModule, Map<Integer, String> paramMap, Map<Integer, LynxModule> paramMap1, boolean paramBoolean) throws InterruptedException {
    try {
      paramLynxUsbDevice.addConfiguredModule(paramLynxModule);
      if (paramBoolean)
        paramLynxModule.enablePhoneCharging(true); 
      paramMap1.put(Integer.valueOf(paramLynxModule.getModuleAddress()), paramLynxModule);
      return;
    } catch (RobotCoreException|LynxNackException|RuntimeException robotCoreException) {
      paramLynxUsbDevice.noteMissingModule(paramLynxModule, paramMap.get(Integer.valueOf(paramLynxModule.getModuleAddress())));
      return;
    } 
  }
  
  public static String getDeviceDisplayName(Context paramContext, SerialNumber paramSerialNumber) {
    return SerialNumber.getDeviceDisplayName(paramSerialNumber);
  }
  
  private void mapAdafruitColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    ColorSensor colorSensor = paramDeviceManager.createAdafruitI2cColorSensor((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapAdafruitColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    ColorSensor colorSensor = paramDeviceManager.createAdafruitI2cColorSensor(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapAnalogOutputDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, AnalogOutputController paramAnalogOutputController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    AnalogOutput analogOutput = paramDeviceManager.createAnalogOutputDevice(paramAnalogOutputController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.analogOutput.put(paramDeviceConfiguration.getName(), (HardwareDevice)analogOutput);
  }
  
  private void mapAnalogSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, AnalogInputController paramAnalogInputController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    if (paramDeviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.ANALOG_SENSOR)) {
      AnalogSensorConfigurationType analogSensorConfigurationType = (AnalogSensorConfigurationType)paramDeviceConfiguration.getConfigurationType();
      HardwareDevice hardwareDevice = paramDeviceManager.createAnalogSensor(paramAnalogInputController, paramDeviceConfiguration.getPort(), analogSensorConfigurationType);
      if (hardwareDevice != null)
        addUserDeviceToMap(paramHardwareMap, paramDeviceConfiguration, hardwareDevice); 
    } 
  }
  
  private void mapCoreInterfaceDeviceModule(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DeviceInterfaceModuleConfiguration paramDeviceInterfaceModuleConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramDeviceInterfaceModuleConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramDeviceInterfaceModuleConfiguration.getSerialNumber();
    DeviceInterfaceModule deviceInterfaceModule = paramDeviceManager.createDeviceInterfaceModule(serialNumber, paramDeviceInterfaceModuleConfiguration.getName());
    paramHardwareMap.deviceInterfaceModule.put(serialNumber, paramDeviceInterfaceModuleConfiguration.getName(), (HardwareDevice)deviceInterfaceModule);
    buildDevices(paramDeviceInterfaceModuleConfiguration.getPwmOutputs(), paramHardwareMap, paramDeviceManager, deviceInterfaceModule);
    buildI2cDevices(paramDeviceInterfaceModuleConfiguration.getI2cDevices(), paramHardwareMap, paramDeviceManager, (I2cController)deviceInterfaceModule);
    buildDevices(paramDeviceInterfaceModuleConfiguration.getAnalogInputDevices(), paramHardwareMap, paramDeviceManager, deviceInterfaceModule);
    buildDevices(paramDeviceInterfaceModuleConfiguration.getDigitalDevices(), paramHardwareMap, paramDeviceManager, deviceInterfaceModule);
    buildDevices(paramDeviceInterfaceModuleConfiguration.getAnalogOutputDevices(), paramHardwareMap, paramDeviceManager, deviceInterfaceModule);
  }
  
  private void mapDigitalDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DigitalChannelController paramDigitalChannelController, DeviceConfiguration paramDeviceConfiguration) {
    HardwareDevice hardwareDevice;
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    TouchSensor touchSensor = null;
    if (paramDeviceConfiguration.getConfigurationType() == BuiltInConfigurationType.TOUCH_SENSOR) {
      touchSensor = paramDeviceManager.createMRDigitalTouchSensor(paramDigitalChannelController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    } else if (paramDeviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.DIGITAL_IO)) {
      DigitalIoDeviceConfigurationType digitalIoDeviceConfigurationType = (DigitalIoDeviceConfigurationType)paramDeviceConfiguration.getConfigurationType();
      hardwareDevice = paramDeviceManager.createDigitalDevice(paramDigitalChannelController, paramDeviceConfiguration.getPort(), digitalIoDeviceConfigurationType);
    } 
    if (hardwareDevice != null)
      addUserDeviceToMap(paramHardwareMap, paramDeviceConfiguration, hardwareDevice); 
  }
  
  private void mapI2cDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    I2cDevice i2cDevice = paramDeviceManager.createI2cDevice(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.i2cDevice.put(paramDeviceConfiguration.getName(), (HardwareDevice)i2cDevice);
  }
  
  private void mapI2cDeviceSynch(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    I2cDeviceSynch i2cDeviceSynch = paramDeviceManager.createI2cDeviceSynch((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.i2cDeviceSynch.put(paramDeviceConfiguration.getName(), (HardwareDevice)i2cDeviceSynch);
  }
  
  private void mapI2cDeviceSynch(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    I2cDeviceSynchImpl i2cDeviceSynchImpl = new I2cDeviceSynchImpl(paramDeviceManager.createI2cDevice(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName()), true);
    paramHardwareMap.i2cDeviceSynch.put(paramDeviceConfiguration.getName(), (HardwareDevice)i2cDeviceSynchImpl);
  }
  
  private void mapIrSeekerV3Device(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    IrSeekerSensor irSeekerSensor = paramDeviceManager.createMRI2cIrSeekerSensorV3((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.irSeekerSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)irSeekerSensor);
  }
  
  private void mapIrSeekerV3Device(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    IrSeekerSensor irSeekerSensor = paramDeviceManager.createMRI2cIrSeekerSensorV3(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.irSeekerSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)irSeekerSensor);
  }
  
  private void mapLED(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DigitalChannelController paramDigitalChannelController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    LED lED = paramDeviceManager.createLED(paramDigitalChannelController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.led.put(paramDeviceConfiguration.getName(), (HardwareDevice)lED);
  }
  
  private void mapLynxColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    ColorSensor colorSensor = paramDeviceManager.createLynxColorRangeSensor((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
    paramHardwareMap.opticalDistanceSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapLynxModuleComponents(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxUsbDeviceConfiguration paramLynxUsbDeviceConfiguration, LynxUsbDevice paramLynxUsbDevice, Map<Integer, LynxModule> paramMap) throws LynxNackException, RobotCoreException, InterruptedException {
    for (DeviceConfiguration deviceConfiguration : paramLynxUsbDeviceConfiguration.getModules()) {
      LynxModule lynxModule = paramMap.get(Integer.valueOf(deviceConfiguration.getPort()));
      if (lynxModule == null)
        continue; 
      LynxModuleConfiguration lynxModuleConfiguration = (LynxModuleConfiguration)deviceConfiguration;
      LynxDcMotorController lynxDcMotorController = new LynxDcMotorController(this.context, lynxModule);
      paramHardwareMap.dcMotorController.put(deviceConfiguration.getName(), (HardwareDevice)lynxDcMotorController);
      for (DeviceConfiguration deviceConfiguration1 : lynxModuleConfiguration.getMotors()) {
        if (deviceConfiguration1.isEnabled()) {
          DcMotor dcMotor = paramDeviceManager.createDcMotorEx((DcMotorController)lynxDcMotorController, deviceConfiguration1.getPort(), (MotorConfigurationType)deviceConfiguration1.getConfigurationType(), deviceConfiguration1.getName());
          paramHardwareMap.dcMotor.put(deviceConfiguration1.getName(), (HardwareDevice)dcMotor);
        } 
      } 
      LynxServoController lynxServoController = new LynxServoController(this.context, lynxModule);
      paramHardwareMap.servoController.put(deviceConfiguration.getName(), (HardwareDevice)lynxServoController);
      Iterator<DeviceConfiguration> iterator = lynxModuleConfiguration.getServos().iterator();
      while (iterator.hasNext())
        mapLynxServoDevice(paramHardwareMap, paramDeviceManager, iterator.next(), (ServoControllerEx)lynxServoController); 
      LynxVoltageSensor lynxVoltageSensor = new LynxVoltageSensor(this.context, lynxModule);
      paramHardwareMap.voltageSensor.put(deviceConfiguration.getName(), (HardwareDevice)lynxVoltageSensor);
      LynxAnalogInputController lynxAnalogInputController = new LynxAnalogInputController(this.context, lynxModule);
      paramHardwareMap.put(deviceConfiguration.getName(), (HardwareDevice)lynxAnalogInputController);
      buildLynxDevices(lynxModuleConfiguration.getAnalogInputs(), paramHardwareMap, paramDeviceManager, (AnalogInputController)lynxAnalogInputController);
      LynxDigitalChannelController lynxDigitalChannelController = new LynxDigitalChannelController(this.context, lynxModule);
      paramHardwareMap.put(deviceConfiguration.getName(), (HardwareDevice)lynxDigitalChannelController);
      buildLynxDevices(lynxModuleConfiguration.getDigitalDevices(), paramHardwareMap, paramDeviceManager, (DigitalChannelController)lynxDigitalChannelController);
      buildLynxI2cDevices(lynxModuleConfiguration.getI2cDevices(), paramHardwareMap, paramDeviceManager, lynxModule);
    } 
  }
  
  private void mapLynxServoDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DeviceConfiguration paramDeviceConfiguration, ServoControllerEx paramServoControllerEx) {
    if (paramDeviceConfiguration.isEnabled()) {
      Servo servo;
      HardwareDevice hardwareDevice;
      if (!paramDeviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.SERVO))
        return; 
      ServoConfigurationType servoConfigurationType = (ServoConfigurationType)paramDeviceConfiguration.getConfigurationType();
      if (servoConfigurationType.getServoFlavor() == ServoFlavor.STANDARD) {
        servo = paramDeviceManager.createServoEx(paramServoControllerEx, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName(), servoConfigurationType);
      } else {
        CRServo cRServo;
        if (servoConfigurationType.getServoFlavor() == ServoFlavor.CONTINUOUS) {
          cRServo = servo.createCRServoEx(paramServoControllerEx, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName(), servoConfigurationType);
        } else {
          hardwareDevice = cRServo.createLynxCustomServoDevice(paramServoControllerEx, paramDeviceConfiguration.getPort(), servoConfigurationType);
        } 
      } 
      if (hardwareDevice != null)
        addUserDeviceToMap(paramHardwareMap, paramDeviceConfiguration, hardwareDevice); 
    } 
  }
  
  private void mapLynxUsbDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxUsbDeviceConfiguration paramLynxUsbDeviceConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramLynxUsbDeviceConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramLynxUsbDeviceConfiguration.getSerialNumber();
    LynxUsbDevice lynxUsbDevice = (LynxUsbDevice)paramDeviceManager.createLynxUsbDevice(serialNumber, paramLynxUsbDeviceConfiguration.getName());
    try {
      int j;
      if (paramLynxUsbDeviceConfiguration.isSystemSynthetic())
        lynxUsbDevice.setSystemSynthetic(true); 
      if (!LynxConstants.isEmbeddedSerialNumber(serialNumber)) {
        i = 1;
      } else {
        i = 0;
      } 
      Iterator<LynxUsbDevice> iterator = paramHardwareMap.getAll(LynxUsbDevice.class).iterator();
      while (true) {
        j = i;
        if (iterator.hasNext()) {
          if (!LynxConstants.isEmbeddedSerialNumber(((LynxUsbDevice)iterator.next()).getSerialNumber())) {
            j = 0;
            break;
          } 
          continue;
        } 
        break;
      } 
      ArrayList<LynxModule> arrayList = new ArrayList();
      HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
      int i = paramLynxUsbDeviceConfiguration.getParentModuleAddress();
      Iterator<DeviceConfiguration> iterator1 = paramLynxUsbDeviceConfiguration.getModules().iterator();
      while (true) {
        boolean bool1;
        LynxModule lynxModule;
        if (iterator1.hasNext()) {
          DeviceConfiguration deviceConfiguration = iterator1.next();
          int k = deviceConfiguration.getPort();
          hashMap.put(Integer.valueOf(k), deviceConfiguration.getName());
          if (i == k) {
            bool1 = true;
          } else {
            bool1 = false;
          } 
          lynxModule = (LynxModule)paramDeviceManager.createLynxModule((RobotCoreLynxUsbDevice)lynxUsbDevice, k, bool1, deviceConfiguration.getName());
          arrayList.add(lynxModule);
          if (((LynxModuleConfiguration)deviceConfiguration).isSystemSynthetic())
            lynxModule.setSystemSynthetic(true); 
          continue;
        } 
        HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
        Iterator<LynxModule> iterator2 = arrayList.iterator();
        while (true) {
          if (iterator2.hasNext()) {
            lynxModule = iterator2.next();
            if (lynxModule.isParent()) {
              if (j != 0 && hashMap1.isEmpty()) {
                bool1 = true;
                continue;
              } 
            } else {
              continue;
            } 
          } else {
            for (LynxModule lynxModule1 : arrayList) {
              if (!lynxModule1.isParent())
                connectModule(lynxUsbDevice, lynxModule1, (Map)hashMap, (Map)hashMap1, false); 
            } 
            mapLynxModuleComponents(paramHardwareMap, paramDeviceManager, paramLynxUsbDeviceConfiguration, lynxUsbDevice, (Map)hashMap1);
            for (Map.Entry<Object, Object> entry : hashMap1.entrySet()) {
              i = ((Integer)entry.getKey()).intValue();
              LynxModule lynxModule1 = (LynxModule)entry.getValue();
              paramHardwareMap.put(lynxModule1.getModuleSerialNumber(), (String)hashMap.get(Integer.valueOf(i)), (HardwareDevice)lynxModule1);
            } 
            paramHardwareMap.put(serialNumber, paramLynxUsbDeviceConfiguration.getName(), (HardwareDevice)lynxUsbDevice);
            return;
          } 
          bool1 = false;
          continue;
          connectModule(lynxUsbDevice, lynxModule, (Map)hashMap, (Map)hashMap1, bool1);
        } 
      } 
    } catch (LynxNackException lynxNackException) {
      throw lynxNackException.wrap();
    } catch (RobotCoreException robotCoreException) {
      lynxUsbDevice.close();
      lynxNackException.remove(serialNumber, paramLynxUsbDeviceConfiguration.getName(), (HardwareDevice)lynxUsbDevice);
      throw robotCoreException;
    } catch (RuntimeException runtimeException) {
      lynxUsbDevice.close();
      lynxNackException.remove(serialNumber, paramLynxUsbDeviceConfiguration.getName(), (HardwareDevice)lynxUsbDevice);
      throw runtimeException;
    } 
    boolean bool = false;
    continue;
  }
  
  private void mapMatrixController(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    MatrixMasterController matrixMasterController = new MatrixMasterController((ModernRoboticsUsbLegacyModule)paramLegacyModule, paramDeviceConfiguration.getPort());
    MatrixDcMotorController matrixDcMotorController = new MatrixDcMotorController(matrixMasterController);
    HardwareMap.DeviceMapping deviceMapping1 = paramHardwareMap.dcMotorController;
    StringBuilder stringBuilder2 = new StringBuilder();
    stringBuilder2.append(paramDeviceConfiguration.getName());
    stringBuilder2.append("Motor");
    deviceMapping1.put(stringBuilder2.toString(), (HardwareDevice)matrixDcMotorController);
    paramHardwareMap.dcMotorController.put(paramDeviceConfiguration.getName(), (HardwareDevice)matrixDcMotorController);
    MatrixControllerConfiguration matrixControllerConfiguration = (MatrixControllerConfiguration)paramDeviceConfiguration;
    Iterator<DeviceConfiguration> iterator2 = matrixControllerConfiguration.getMotors().iterator();
    while (iterator2.hasNext())
      mapMotor(paramHardwareMap, paramDeviceManager, iterator2.next(), (DcMotorController)matrixDcMotorController); 
    MatrixServoController matrixServoController = new MatrixServoController(matrixMasterController);
    HardwareMap.DeviceMapping deviceMapping2 = paramHardwareMap.servoController;
    StringBuilder stringBuilder1 = new StringBuilder();
    stringBuilder1.append(paramDeviceConfiguration.getName());
    stringBuilder1.append("Servo");
    deviceMapping2.put(stringBuilder1.toString(), (HardwareDevice)matrixServoController);
    paramHardwareMap.servoController.put(paramDeviceConfiguration.getName(), (HardwareDevice)matrixServoController);
    Iterator<DeviceConfiguration> iterator1 = matrixControllerConfiguration.getServos().iterator();
    while (iterator1.hasNext())
      mapServoDevice(paramHardwareMap, paramDeviceManager, iterator1.next(), (ServoController)matrixServoController); 
  }
  
  private void mapModernRoboticsColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    ColorSensor colorSensor = paramDeviceManager.createModernRoboticsI2cColorSensor((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapModernRoboticsColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    ColorSensor colorSensor = paramDeviceManager.createModernRoboticsI2cColorSensor(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapModernRoboticsGyro(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    GyroSensor gyroSensor = paramDeviceManager.createModernRoboticsI2cGyroSensor((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.gyroSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)gyroSensor);
  }
  
  private void mapModernRoboticsGyro(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    GyroSensor gyroSensor = paramDeviceManager.createModernRoboticsI2cGyroSensor(paramI2cController, paramDeviceConfiguration.getI2cChannel(), paramDeviceConfiguration.getName());
    paramHardwareMap.gyroSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)gyroSensor);
  }
  
  private void mapMotor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DeviceConfiguration paramDeviceConfiguration, DcMotorController paramDcMotorController) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    DcMotor dcMotor = paramDeviceManager.createDcMotor(paramDcMotorController, paramDeviceConfiguration.getPort(), (MotorConfigurationType)paramDeviceConfiguration.getConfigurationType(), paramDeviceConfiguration.getName());
    paramHardwareMap.dcMotor.put(paramDeviceConfiguration.getName(), (HardwareDevice)dcMotor);
  }
  
  private void mapNxtAccelerationSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    AccelerationSensor accelerationSensor = paramDeviceManager.createHTAccelerationSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.accelerationSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)accelerationSensor);
  }
  
  private void mapNxtColorSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    ColorSensor colorSensor = paramDeviceManager.createHTColorSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.colorSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)colorSensor);
  }
  
  private void mapNxtCompassSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    CompassSensor compassSensor = paramDeviceManager.createHTCompassSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.compassSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)compassSensor);
  }
  
  private void mapNxtDcMotorController(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    HiTechnicNxtDcMotorController hiTechnicNxtDcMotorController = (HiTechnicNxtDcMotorController)paramDeviceManager.createHTDcMotorController(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.dcMotorController.put(paramDeviceConfiguration.getName(), (HardwareDevice)hiTechnicNxtDcMotorController);
    Iterator<DeviceConfiguration> iterator = ((MotorControllerConfiguration)paramDeviceConfiguration).getMotors().iterator();
    while (iterator.hasNext())
      mapMotor(paramHardwareMap, paramDeviceManager, iterator.next(), (DcMotorController)hiTechnicNxtDcMotorController); 
    paramHardwareMap.voltageSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)hiTechnicNxtDcMotorController);
  }
  
  private void mapNxtGyroSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    GyroSensor gyroSensor = paramDeviceManager.createHTGyroSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.gyroSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)gyroSensor);
  }
  
  private void mapNxtIrSeekerSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    IrSeekerSensor irSeekerSensor = paramDeviceManager.createHTIrSeekerSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.irSeekerSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)irSeekerSensor);
  }
  
  private void mapNxtLightSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    LightSensor lightSensor = paramDeviceManager.createHTLightSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.lightSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)lightSensor);
  }
  
  private void mapNxtServoController(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    ServoController servoController = paramDeviceManager.createHTServoController(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.servoController.put(paramDeviceConfiguration.getName(), (HardwareDevice)servoController);
    Iterator<DeviceConfiguration> iterator = ((ServoControllerConfiguration)paramDeviceConfiguration).getServos().iterator();
    while (iterator.hasNext())
      mapServoDevice(paramHardwareMap, paramDeviceManager, iterator.next(), servoController); 
  }
  
  private void mapNxtTouchSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    TouchSensor touchSensor = paramDeviceManager.createNxtTouchSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.touchSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)touchSensor);
  }
  
  private void mapNxtTouchSensorMultiplexer(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    TouchSensorMultiplexer touchSensorMultiplexer = paramDeviceManager.createHTTouchSensorMultiplexer(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.touchSensorMultiplexer.put(paramDeviceConfiguration.getName(), (HardwareDevice)touchSensorMultiplexer);
  }
  
  private void mapPwmOutputDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, PWMOutputController paramPWMOutputController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    PWMOutput pWMOutput = paramDeviceManager.createPwmOutputDevice(paramPWMOutputController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.pwmOutput.put(paramDeviceConfiguration.getName(), (HardwareDevice)pWMOutput);
  }
  
  private void mapServoDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, DeviceConfiguration paramDeviceConfiguration, ServoController paramServoController) {
    if (paramDeviceConfiguration.isEnabled()) {
      Servo servo;
      HardwareDevice hardwareDevice;
      if (!paramDeviceConfiguration.getConfigurationType().isDeviceFlavor(ConfigurationType.DeviceFlavor.SERVO))
        return; 
      ServoConfigurationType servoConfigurationType = (ServoConfigurationType)paramDeviceConfiguration.getConfigurationType();
      if (servoConfigurationType.getServoFlavor() == ServoFlavor.STANDARD) {
        servo = paramDeviceManager.createServo(paramServoController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
      } else {
        CRServo cRServo;
        if (servoConfigurationType.getServoFlavor() == ServoFlavor.CONTINUOUS) {
          cRServo = servo.createCRServo(paramServoController, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
        } else {
          hardwareDevice = cRServo.createCustomServoDevice(paramServoController, paramDeviceConfiguration.getPort(), servoConfigurationType);
        } 
      } 
      if (hardwareDevice != null)
        addUserDeviceToMap(paramHardwareMap, paramDeviceConfiguration, hardwareDevice); 
    } 
  }
  
  private void mapSonarSensor(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModule paramLegacyModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    UltrasonicSensor ultrasonicSensor = paramDeviceManager.createNxtUltrasonicSensor(paramLegacyModule, paramDeviceConfiguration.getPort(), paramDeviceConfiguration.getName());
    paramHardwareMap.ultrasonicSensor.put(paramDeviceConfiguration.getName(), (HardwareDevice)ultrasonicSensor);
  }
  
  private void mapUsbLegacyModule(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LegacyModuleControllerConfiguration paramLegacyModuleControllerConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramLegacyModuleControllerConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramLegacyModuleControllerConfiguration.getSerialNumber();
    LegacyModule legacyModule = paramDeviceManager.createUsbLegacyModule(serialNumber, paramLegacyModuleControllerConfiguration.getName());
    paramHardwareMap.legacyModule.put(serialNumber, paramLegacyModuleControllerConfiguration.getName(), (HardwareDevice)legacyModule);
    for (DeviceConfiguration deviceConfiguration : paramLegacyModuleControllerConfiguration.getDevices()) {
      ConfigurationType configurationType = deviceConfiguration.getConfigurationType();
      if (configurationType == BuiltInConfigurationType.GYRO) {
        mapNxtGyroSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.COMPASS) {
        mapNxtCompassSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.IR_SEEKER) {
        mapNxtIrSeekerSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.LIGHT_SENSOR) {
        mapNxtLightSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.ACCELEROMETER) {
        mapNxtAccelerationSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
        mapNxtDcMotorController(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
        mapNxtServoController(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.TOUCH_SENSOR) {
        mapNxtTouchSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.TOUCH_SENSOR_MULTIPLEXER) {
        mapNxtTouchSensorMultiplexer(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.ULTRASONIC_SENSOR) {
        mapSonarSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.COLOR_SENSOR) {
        mapNxtColorSensor(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.MATRIX_CONTROLLER) {
        mapMatrixController(paramHardwareMap, paramDeviceManager, legacyModule, deviceConfiguration);
        continue;
      } 
      if (configurationType == BuiltInConfigurationType.NOTHING)
        continue; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unexpected device type connected to Legacy Module while parsing XML: ");
      stringBuilder.append(configurationType.toString());
      RobotLog.w(stringBuilder.toString());
    } 
  }
  
  private void mapUsbMotorController(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, MotorControllerConfiguration paramMotorControllerConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramMotorControllerConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramMotorControllerConfiguration.getSerialNumber();
    ModernRoboticsUsbDcMotorController modernRoboticsUsbDcMotorController = (ModernRoboticsUsbDcMotorController)paramDeviceManager.createUsbDcMotorController(serialNumber, paramMotorControllerConfiguration.getName());
    paramHardwareMap.dcMotorController.put(serialNumber, paramMotorControllerConfiguration.getName(), (HardwareDevice)modernRoboticsUsbDcMotorController);
    Iterator<DeviceConfiguration> iterator = paramMotorControllerConfiguration.getMotors().iterator();
    while (iterator.hasNext())
      mapMotor(paramHardwareMap, paramDeviceManager, iterator.next(), (DcMotorController)modernRoboticsUsbDcMotorController); 
    paramHardwareMap.voltageSensor.put(paramMotorControllerConfiguration.getName(), (HardwareDevice)modernRoboticsUsbDcMotorController);
  }
  
  private void mapUsbServoController(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, ServoControllerConfiguration paramServoControllerConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramServoControllerConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramServoControllerConfiguration.getSerialNumber();
    ServoController servoController = paramDeviceManager.createUsbServoController(serialNumber, paramServoControllerConfiguration.getName());
    paramHardwareMap.servoController.put(serialNumber, paramServoControllerConfiguration.getName(), (HardwareDevice)servoController);
    Iterator<DeviceConfiguration> iterator = paramServoControllerConfiguration.getDevices().iterator();
    while (iterator.hasNext())
      mapServoDevice(paramHardwareMap, paramDeviceManager, iterator.next(), servoController); 
  }
  
  private void mapUserI2cDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, LynxModule paramLynxModule, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    I2cDeviceConfigurationType i2cDeviceConfigurationType = (I2cDeviceConfigurationType)paramDeviceConfiguration.getConfigurationType();
    HardwareDevice hardwareDevice = paramDeviceManager.createUserI2cDevice((RobotCoreLynxModule)paramLynxModule, paramDeviceConfiguration.getI2cChannel(), i2cDeviceConfigurationType, paramDeviceConfiguration.getName());
    if (hardwareDevice != null)
      paramHardwareMap.put(paramDeviceConfiguration.getName(), hardwareDevice); 
  }
  
  private void mapUserI2cDevice(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, I2cController paramI2cController, DeviceConfiguration paramDeviceConfiguration) {
    if (!paramDeviceConfiguration.isEnabled())
      return; 
    I2cDeviceConfigurationType i2cDeviceConfigurationType = (I2cDeviceConfigurationType)paramDeviceConfiguration.getConfigurationType();
    HardwareDevice hardwareDevice = paramDeviceManager.createUserI2cDevice(paramI2cController, paramDeviceConfiguration.getI2cChannel(), i2cDeviceConfigurationType, paramDeviceConfiguration.getName());
    if (hardwareDevice != null)
      addUserDeviceToMap(paramHardwareMap, paramDeviceConfiguration, hardwareDevice); 
  }
  
  private void mapWebcam(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, WebcamConfiguration paramWebcamConfiguration) throws RobotCoreException, InterruptedException {
    if (!paramWebcamConfiguration.isEnabled())
      return; 
    SerialNumber serialNumber = paramWebcamConfiguration.getSerialNumber();
    if (paramWebcamConfiguration.getAutoOpen()) {
      RobotLog.ee("HardwareFactory", "support for auto-opening webcams is not yet implemented: %s", new Object[] { serialNumber });
      return;
    } 
    WebcamName webcamName = paramDeviceManager.createWebcamName(serialNumber, paramWebcamConfiguration.getName());
    if (webcamName != null)
      paramHardwareMap.put(serialNumber, paramWebcamConfiguration.getName(), (HardwareDevice)webcamName); 
  }
  
  private <T extends HardwareDevice> void maybeAddToMapping(HardwareMap.DeviceMapping<T> paramDeviceMapping, String paramString, T paramT) {
    if (!paramDeviceMapping.contains(paramString))
      paramDeviceMapping.putLocal(paramString, (HardwareDevice)paramT); 
  }
  
  public static void noteSerialNumberType(Context paramContext, SerialNumber paramSerialNumber, String paramString) {
    SerialNumber.noteSerialNumberType(paramSerialNumber, paramString);
  }
  
  public HardwareMap createHardwareMap(SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    synchronized (HardwareDeviceManager.scanDevicesLock) {
      RobotLog.vv("HardwareFactory", "createHardwareMap()");
      EmbeddedControlHubModule.clear();
      HardwareMap hardwareMap = new HardwareMap(this.context);
      if (this.xmlPullParser != null) {
        HardwareDeviceManager hardwareDeviceManager = new HardwareDeviceManager(this.context, paramManager);
        Iterator<ControllerConfiguration> iterator = (new ReadXMLFileHandler(hardwareDeviceManager)).parse(this.xmlPullParser).iterator();
        while (iterator.hasNext())
          mapControllerConfiguration(hardwareMap, hardwareDeviceManager, iterator.next()); 
      } else {
        RobotLog.vv("HardwareFactory", "no xml to parse: using empty map");
      } 
      return hardwareMap;
    } 
  }
  
  public XmlPullParser getXmlPullParser() {
    return this.xmlPullParser;
  }
  
  public void instantiateConfiguration(HardwareMap paramHardwareMap, ControllerConfiguration paramControllerConfiguration, SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    synchronized (HardwareDeviceManager.scanDevicesLock) {
      mapControllerConfiguration(paramHardwareMap, new HardwareDeviceManager(this.context, paramManager), paramControllerConfiguration);
      return;
    } 
  }
  
  protected void mapControllerConfiguration(HardwareMap paramHardwareMap, DeviceManager paramDeviceManager, ControllerConfiguration paramControllerConfiguration) throws RobotCoreException, InterruptedException {
    ConfigurationType configurationType = paramControllerConfiguration.getConfigurationType();
    if (configurationType == BuiltInConfigurationType.MOTOR_CONTROLLER) {
      mapUsbMotorController(paramHardwareMap, paramDeviceManager, (MotorControllerConfiguration)paramControllerConfiguration);
      return;
    } 
    if (configurationType == BuiltInConfigurationType.SERVO_CONTROLLER) {
      mapUsbServoController(paramHardwareMap, paramDeviceManager, (ServoControllerConfiguration)paramControllerConfiguration);
      return;
    } 
    if (configurationType == BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER) {
      mapUsbLegacyModule(paramHardwareMap, paramDeviceManager, (LegacyModuleControllerConfiguration)paramControllerConfiguration);
      return;
    } 
    if (configurationType == BuiltInConfigurationType.DEVICE_INTERFACE_MODULE) {
      mapCoreInterfaceDeviceModule(paramHardwareMap, paramDeviceManager, (DeviceInterfaceModuleConfiguration)paramControllerConfiguration);
      return;
    } 
    if (configurationType == BuiltInConfigurationType.LYNX_USB_DEVICE) {
      mapLynxUsbDevice(paramHardwareMap, paramDeviceManager, (LynxUsbDeviceConfiguration)paramControllerConfiguration);
      return;
    } 
    if (configurationType == BuiltInConfigurationType.WEBCAM) {
      mapWebcam(paramHardwareMap, paramDeviceManager, (WebcamConfiguration)paramControllerConfiguration);
      return;
    } 
    RobotLog.ee("HardwareFactory", "unexpected controller configuration type: %s", new Object[] { configurationType });
  }
  
  public void setXmlPullParser(XmlPullParser paramXmlPullParser) {
    this.xmlPullParser = paramXmlPullParser;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\HardwareFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */