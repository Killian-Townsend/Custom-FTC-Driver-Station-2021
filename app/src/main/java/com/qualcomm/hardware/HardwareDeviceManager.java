package com.qualcomm.hardware;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtAccelerationSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtColorSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtCompassSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtDcMotorController;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtGyroSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtIrSeekerSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtLightSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtServoController;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensor;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtTouchSensorMultiplexer;
import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.hardware.lynx.LynxI2cColorRangeSensor;
import com.qualcomm.hardware.lynx.LynxI2cDeviceSynch;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxUsbDevice;
import com.qualcomm.hardware.lynx.LynxUsbDeviceImpl;
import com.qualcomm.hardware.lynx.LynxUsbUtil;
import com.qualcomm.hardware.lynx.commands.core.LynxFirmwareVersionManager;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cColorSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cIrSeekerSensorV3;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsTouchSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDcMotorController;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbDeviceInterfaceModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbServoController;
import com.qualcomm.hardware.modernrobotics.comm.ModernRoboticsUsbUtil;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.AnalogOutput;
import com.qualcomm.robotcore.hardware.AnalogOutputController;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.CRServoImpl;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorImpl;
import com.qualcomm.robotcore.hardware.DcMotorImplEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImplOnSimple;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.IrSeekerSensor;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.PWMOutputImpl;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import com.qualcomm.robotcore.hardware.RobotCoreLynxUsbDevice;
import com.qualcomm.robotcore.hardware.ScannedDevices;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.ServoImpl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.hardware.configuration.DeviceConfiguration;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.AnalogSensorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.DigitalIoDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.I2cDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDeviceImplBase;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManager;
import com.qualcomm.robotcore.hardware.usb.RobotUsbManagerCombining;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.hardware.usb.ftdi.RobotUsbManagerFtdi;
import com.qualcomm.robotcore.hardware.usb.serial.RobotUsbManagerTty;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.CameraManagerInternal;
import org.firstinspires.ftc.robotcore.internal.hardware.UserNameable;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.usb.VendorProductSerialNumber;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class HardwareDeviceManager implements DeviceManager {
  public static final String TAG = "HardwareDeviceManager";
  
  public static final String TAG_USB_SCAN = "USBScan";
  
  public static final Object scanDevicesLock = new Object();
  
  private final Context context;
  
  private final SyncdDevice.Manager manager;
  
  private RobotUsbManager usbManager;
  
  public HardwareDeviceManager(Context paramContext, SyncdDevice.Manager paramManager) {
    this.context = paramContext;
    this.manager = paramManager;
    this.usbManager = createUsbManager();
  }
  
  private void closeAndThrowOnFailedDeviceTypeCheck(RobotUsbDevice paramRobotUsbDevice, SerialNumber paramSerialNumber) throws RobotCoreException {
    String str = String.format("%s is returning garbage data on the USB bus", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, paramSerialNumber) });
    paramRobotUsbDevice.close();
    logAndThrow(str);
  }
  
  public static RobotUsbManager createUsbManager() {
    RobotUsbManagerCombining robotUsbManagerCombining;
    RobotUsbManagerFtdi robotUsbManagerFtdi2 = new RobotUsbManagerFtdi();
    RobotUsbManagerFtdi robotUsbManagerFtdi1 = robotUsbManagerFtdi2;
    if (LynxConstants.isRevControlHub()) {
      robotUsbManagerCombining = new RobotUsbManagerCombining();
      robotUsbManagerCombining.addManager((RobotUsbManager)robotUsbManagerFtdi2);
      robotUsbManagerCombining.addManager((RobotUsbManager)new RobotUsbManagerTty());
    } 
    return (RobotUsbManager)robotUsbManagerCombining;
  }
  
  private RobotUsbDevice.FirmwareVersion getModernRoboticsFirmwareVersion(byte[] paramArrayOfbyte) {
    return new RobotUsbDevice.FirmwareVersion(paramArrayOfbyte[0]);
  }
  
  private void logAndThrow(String paramString) throws RobotCoreException {
    System.err.println(paramString);
    throw new RobotCoreException(paramString);
  }
  
  private ModernRoboticsUsbDeviceInterfaceModule promote(DeviceInterfaceModule paramDeviceInterfaceModule) {
    if (paramDeviceInterfaceModule instanceof ModernRoboticsUsbDeviceInterfaceModule)
      return (ModernRoboticsUsbDeviceInterfaceModule)paramDeviceInterfaceModule; 
    throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics Device Interface Module");
  }
  
  private ModernRoboticsUsbLegacyModule promote(LegacyModule paramLegacyModule) {
    if (paramLegacyModule instanceof ModernRoboticsUsbLegacyModule)
      return (ModernRoboticsUsbLegacyModule)paramLegacyModule; 
    throw new IllegalArgumentException("Modern Robotics Device Manager needs a Modern Robotics LegacyModule");
  }
  
  void addVidPid(Map<Pair<Integer, Integer>, Integer> paramMap, VendorProductSerialNumber paramVendorProductSerialNumber, int paramInt) {
    int i = countVidPid(paramMap, paramVendorProductSerialNumber).intValue();
    paramMap.put(new Pair(Integer.valueOf(paramVendorProductSerialNumber.getVendorId()), Integer.valueOf(paramVendorProductSerialNumber.getProductId())), Integer.valueOf(i + paramInt));
  }
  
  Integer countVidPid(Map<Pair<Integer, Integer>, Integer> paramMap, VendorProductSerialNumber paramVendorProductSerialNumber) {
    Integer integer = paramMap.get(new Pair(Integer.valueOf(paramVendorProductSerialNumber.getVendorId()), Integer.valueOf(paramVendorProductSerialNumber.getProductId())));
    return (integer != null) ? integer : Integer.valueOf(0);
  }
  
  public ColorSensor createAdafruitI2cColorSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Adafruit I2C Color Sensor - ");
    stringBuilder.append(paramI2cChannel);
    RobotLog.v(stringBuilder.toString());
    return (ColorSensor)new AdafruitI2cColorSensor(createI2cDeviceSynchSimple(paramI2cController, paramI2cChannel.channel, paramString));
  }
  
  public ColorSensor createAdafruitI2cColorSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating Adafruit Color Sensor (Lynx) - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (ColorSensor)new AdafruitI2cColorSensor(createI2cDeviceSynchSimple(paramRobotCoreLynxModule, paramI2cChannel, paramString));
  }
  
  public AnalogOutput createAnalogOutputDevice(AnalogOutputController paramAnalogOutputController, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Analog Output Device - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return new AnalogOutput(paramAnalogOutputController, paramInt);
  }
  
  public HardwareDevice createAnalogSensor(AnalogInputController paramAnalogInputController, int paramInt, AnalogSensorConfigurationType paramAnalogSensorConfigurationType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Analog Sensor - Type: ");
    stringBuilder.append(paramAnalogSensorConfigurationType.getName());
    stringBuilder.append(" - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return paramAnalogSensorConfigurationType.createInstance(paramAnalogInputController, paramInt);
  }
  
  public CRServo createCRServo(ServoController paramServoController, int paramInt, String paramString) {
    return (CRServo)new CRServoImpl(paramServoController, paramInt, DcMotorSimple.Direction.FORWARD);
  }
  
  public CRServo createCRServoEx(ServoControllerEx paramServoControllerEx, int paramInt, String paramString, ServoConfigurationType paramServoConfigurationType) {
    return (CRServo)new CRServoImplEx(paramServoControllerEx, paramInt, DcMotorSimple.Direction.FORWARD, paramServoConfigurationType);
  }
  
  public HardwareDevice createCustomServoDevice(ServoController paramServoController, int paramInt, ServoConfigurationType paramServoConfigurationType) {
    return paramServoConfigurationType.createInstanceMr(paramServoController, paramInt);
  }
  
  public DcMotor createDcMotor(DcMotorController paramDcMotorController, int paramInt, MotorConfigurationType paramMotorConfigurationType, String paramString) {
    return (DcMotor)new DcMotorImpl(paramDcMotorController, paramInt, DcMotorSimple.Direction.FORWARD, paramMotorConfigurationType);
  }
  
  public DcMotor createDcMotorEx(DcMotorController paramDcMotorController, int paramInt, MotorConfigurationType paramMotorConfigurationType, String paramString) {
    return (DcMotor)new DcMotorImplEx(paramDcMotorController, paramInt, DcMotorSimple.Direction.FORWARD, paramMotorConfigurationType);
  }
  
  public DeviceInterfaceModule createDeviceInterfaceModule(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameCDIM));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          RobotUsbDevice robotUsbDevice2 = null;
          RobotUsbDevice robotUsbDevice1 = null;
          try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openRobotUsbDevice(true, HardwareDeviceManager.this.usbManager, serialNumber);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            byte[] arrayOfByte = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(robotUsbDevice);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            if (HardwareDeviceManager.this.getModernRoboticsDeviceType(robotUsbDevice, arrayOfByte) != DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DEVICE_INTERFACE_MODULE) {
              robotUsbDevice1 = robotUsbDevice;
              robotUsbDevice2 = robotUsbDevice;
              HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(robotUsbDevice, serialNumber);
            } 
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            robotUsbDevice.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(arrayOfByte));
            return robotUsbDevice;
          } catch (RobotCoreException robotCoreException) {
            if (robotUsbDevice2 != null)
              robotUsbDevice2.close(); 
            throw robotCoreException;
          } catch (RuntimeException runtimeException) {
            if (robotCoreException != null)
              robotCoreException.close(); 
            throw runtimeException;
          } 
        }
      };
    ModernRoboticsUsbDeviceInterfaceModule modernRoboticsUsbDeviceInterfaceModule = new ModernRoboticsUsbDeviceInterfaceModule(this.context, serialNumber, openRobotUsbDevice, this.manager);
    modernRoboticsUsbDeviceInterfaceModule.armOrPretend();
    modernRoboticsUsbDeviceInterfaceModule.initializeHardware();
    return (DeviceInterfaceModule)modernRoboticsUsbDeviceInterfaceModule;
  }
  
  public HardwareDevice createDigitalDevice(DigitalChannelController paramDigitalChannelController, int paramInt, DigitalIoDeviceConfigurationType paramDigitalIoDeviceConfigurationType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Digital Channel Device - Type: ");
    stringBuilder.append(paramDigitalIoDeviceConfigurationType.getName());
    stringBuilder.append(" - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return paramDigitalIoDeviceConfigurationType.createInstance(paramDigitalChannelController, paramInt);
  }
  
  public AccelerationSensor createHTAccelerationSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Acceleration Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (AccelerationSensor)new HiTechnicNxtAccelerationSensor((I2cController)promote(paramLegacyModule), paramInt);
  }
  
  public ColorSensor createHTColorSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Color Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (ColorSensor)new HiTechnicNxtColorSensor(createI2cDeviceSynch((I2cController)paramLegacyModule, paramInt, paramString));
  }
  
  public CompassSensor createHTCompassSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Compass Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (CompassSensor)new HiTechnicNxtCompassSensor((I2cController)promote(paramLegacyModule), paramInt);
  }
  
  public DcMotorController createHTDcMotorController(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT DC Motor Controller - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (DcMotorController)new HiTechnicNxtDcMotorController(this.context, paramLegacyModule, paramInt);
  }
  
  public GyroSensor createHTGyroSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Gyro Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (GyroSensor)new HiTechnicNxtGyroSensor((LegacyModule)promote(paramLegacyModule), paramInt);
  }
  
  public IrSeekerSensor createHTIrSeekerSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT IR Seeker Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (IrSeekerSensor)new HiTechnicNxtIrSeekerSensor((I2cController)promote(paramLegacyModule), paramInt);
  }
  
  public LightSensor createHTLightSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Light Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (LightSensor)new HiTechnicNxtLightSensor((LegacyModule)promote(paramLegacyModule), paramInt);
  }
  
  public ServoController createHTServoController(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Servo Controller - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (ServoController)new HiTechnicNxtServoController(this.context, (I2cController)paramLegacyModule, paramInt);
  }
  
  public TouchSensorMultiplexer createHTTouchSensorMultiplexer(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Touch Sensor Multiplexer - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (TouchSensorMultiplexer)new HiTechnicNxtTouchSensorMultiplexer((LegacyModule)promote(paramLegacyModule), paramInt);
  }
  
  public I2cDevice createI2cDevice(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating I2C Device - ");
    stringBuilder.append(paramI2cChannel);
    RobotLog.v(stringBuilder.toString());
    return (I2cDevice)new I2cDeviceImpl(paramI2cController, paramI2cChannel.channel);
  }
  
  protected I2cDeviceSynch createI2cDeviceSynch(I2cController paramI2cController, int paramInt, String paramString) {
    I2cDeviceSynchImpl i2cDeviceSynchImpl = new I2cDeviceSynchImpl((I2cDevice)new I2cDeviceImpl(paramI2cController, paramInt), true);
    i2cDeviceSynchImpl.setUserConfiguredName(paramString);
    return (I2cDeviceSynch)i2cDeviceSynchImpl;
  }
  
  public I2cDeviceSynch createI2cDeviceSynch(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating I2cDeviceSynch (Lynx) - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (I2cDeviceSynch)new I2cDeviceSynchImplOnSimple(createI2cDeviceSynchSimple(paramRobotCoreLynxModule, paramI2cChannel, paramString), true);
  }
  
  protected I2cDeviceSynchSimple createI2cDeviceSynchSimple(I2cController paramI2cController, int paramInt, String paramString) {
    return (I2cDeviceSynchSimple)createI2cDeviceSynch(paramI2cController, paramInt, paramString);
  }
  
  protected I2cDeviceSynchSimple createI2cDeviceSynchSimple(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    LynxI2cDeviceSynch lynxI2cDeviceSynch = LynxFirmwareVersionManager.createLynxI2cDeviceSynch(this.context, (LynxModule)paramRobotCoreLynxModule, paramI2cChannel.channel);
    lynxI2cDeviceSynch.setUserConfiguredName(paramString);
    return (I2cDeviceSynchSimple)lynxI2cDeviceSynch;
  }
  
  public LED createLED(DigitalChannelController paramDigitalChannelController, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating LED - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return new LED(paramDigitalChannelController, paramInt);
  }
  
  public ColorSensor createLynxColorRangeSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating Lynx Color/Range Sensor - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (ColorSensor)new LynxI2cColorRangeSensor(createI2cDeviceSynchSimple(paramRobotCoreLynxModule, paramI2cChannel, paramString));
  }
  
  public HardwareDevice createLynxCustomServoDevice(ServoControllerEx paramServoControllerEx, int paramInt, ServoConfigurationType paramServoConfigurationType) {
    return paramServoConfigurationType.createInstanceRev(paramServoControllerEx, paramInt);
  }
  
  public RobotCoreLynxModule createLynxModule(RobotCoreLynxUsbDevice paramRobotCoreLynxUsbDevice, int paramInt, boolean paramBoolean, String paramString) {
    RobotLog.v("Creating Lynx Module - mod=%d parent=%s", new Object[] { Integer.valueOf(paramInt), Boolean.toString(paramBoolean) });
    return (RobotCoreLynxModule)new LynxModule((LynxUsbDevice)paramRobotCoreLynxUsbDevice, paramInt, paramBoolean);
  }
  
  public RobotCoreLynxUsbDevice createLynxUsbDevice(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameLynxUsbDevice));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          RobotCoreException robotCoreException1;
          RuntimeException runtimeException;
          RobotUsbDevice robotUsbDevice4 = null;
          RobotUsbDevice robotUsbDevice3 = null;
          RobotUsbDevice robotUsbDevice2 = robotUsbDevice3;
          RobotUsbDevice robotUsbDevice1 = robotUsbDevice4;
          try {
            RobotUsbManager robotUsbManager = HardwareDeviceManager.this.usbManager;
            robotUsbDevice2 = robotUsbDevice3;
            robotUsbDevice1 = robotUsbDevice4;
            SerialNumber serialNumber = serialNumber;
            boolean bool = true;
            robotUsbDevice2 = robotUsbDevice3;
            robotUsbDevice1 = robotUsbDevice4;
            robotUsbDevice3 = LynxUsbUtil.openUsbDevice(true, robotUsbManager, serialNumber);
            robotUsbDevice2 = robotUsbDevice3;
            robotUsbDevice1 = robotUsbDevice3;
            if (!robotUsbDevice3.getUsbIdentifiers().isLynxDevice()) {
              robotUsbDevice2 = robotUsbDevice3;
              robotUsbDevice1 = robotUsbDevice3;
              HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(robotUsbDevice3, serialNumber);
            } 
            robotUsbDevice2 = robotUsbDevice3;
            robotUsbDevice1 = robotUsbDevice3;
            if (HardwareDeviceManager.this.getLynxDeviceType(robotUsbDevice3) != DeviceManager.UsbDeviceType.LYNX_USB_DEVICE)
              bool = false; 
            robotUsbDevice2 = robotUsbDevice3;
            robotUsbDevice1 = robotUsbDevice3;
            Assert.assertTrue(bool);
            return robotUsbDevice3;
          } catch (RobotCoreException robotCoreException2) {
          
          } catch (RuntimeException runtimeException1) {
            robotCoreException1 = robotCoreException2;
            runtimeException = runtimeException1;
          } 
          if (robotCoreException1 != null)
            robotCoreException1.close(); 
          throw runtimeException;
        }
      };
    return (RobotCoreLynxUsbDevice)LynxUsbDeviceImpl.findOrCreateAndArm(this.context, serialNumber, this.manager, openRobotUsbDevice);
  }
  
  public TouchSensor createMRDigitalTouchSensor(DigitalChannelController paramDigitalChannelController, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Modern Robotics digital Touch Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (TouchSensor)new ModernRoboticsTouchSensor(paramDigitalChannelController, paramInt);
  }
  
  public IrSeekerSensor createMRI2cIrSeekerSensorV3(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Modern Robotics I2C IR Seeker Sensor V3 - ");
    stringBuilder.append(paramI2cChannel);
    RobotLog.v(stringBuilder.toString());
    return (IrSeekerSensor)new ModernRoboticsI2cIrSeekerSensorV3(createI2cDeviceSynch(paramI2cController, paramI2cChannel.channel, paramString));
  }
  
  public IrSeekerSensor createMRI2cIrSeekerSensorV3(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating Modern Robotics I2C IR Seeker Sensor V3 - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (IrSeekerSensor)new ModernRoboticsI2cIrSeekerSensorV3(createI2cDeviceSynch(paramRobotCoreLynxModule, paramI2cChannel, paramString));
  }
  
  public ColorSensor createModernRoboticsI2cColorSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Modern Robotics I2C Color Sensor - ");
    stringBuilder.append(paramI2cChannel);
    RobotLog.v(stringBuilder.toString());
    return (ColorSensor)new ModernRoboticsI2cColorSensor(createI2cDeviceSynch(paramI2cController, paramI2cChannel.channel, paramString));
  }
  
  public ColorSensor createModernRoboticsI2cColorSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating Modern Robotics I2C Color Sensor - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (ColorSensor)new ModernRoboticsI2cColorSensor(createI2cDeviceSynch(paramRobotCoreLynxModule, paramI2cChannel, paramString));
  }
  
  public GyroSensor createModernRoboticsI2cGyroSensor(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating Modern Robotics I2C Gyro Sensor - ");
    stringBuilder.append(paramI2cChannel);
    RobotLog.v(stringBuilder.toString());
    return (GyroSensor)new ModernRoboticsI2cGyro(createI2cDeviceSynch(paramI2cController, paramI2cChannel.channel, paramString));
  }
  
  public GyroSensor createModernRoboticsI2cGyroSensor(RobotCoreLynxModule paramRobotCoreLynxModule, DeviceConfiguration.I2cChannel paramI2cChannel, String paramString) {
    RobotLog.v("Creating Modern Robotics I2C Gyro Sensor - mod=%d bus=%d", new Object[] { Integer.valueOf(paramRobotCoreLynxModule.getModuleAddress()), Integer.valueOf(paramI2cChannel.channel) });
    return (GyroSensor)new ModernRoboticsI2cGyro(createI2cDeviceSynch(paramRobotCoreLynxModule, paramI2cChannel, paramString));
  }
  
  public TouchSensor createNxtTouchSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Touch Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (TouchSensor)new HiTechnicNxtTouchSensor((LegacyModule)promote(paramLegacyModule), paramInt);
  }
  
  public UltrasonicSensor createNxtUltrasonicSensor(LegacyModule paramLegacyModule, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating HiTechnic NXT Ultrasonic Sensor - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (UltrasonicSensor)new HiTechnicNxtUltrasonicSensor(promote(paramLegacyModule), paramInt);
  }
  
  public PWMOutput createPwmOutputDevice(PWMOutputController paramPWMOutputController, int paramInt, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Creating PWM Output Device - Port: ");
    stringBuilder.append(paramInt);
    RobotLog.v(stringBuilder.toString());
    return (PWMOutput)new PWMOutputImpl(paramPWMOutputController, paramInt);
  }
  
  public Servo createServo(ServoController paramServoController, int paramInt, String paramString) {
    return (Servo)new ServoImpl(paramServoController, paramInt, Servo.Direction.FORWARD);
  }
  
  public Servo createServoEx(ServoControllerEx paramServoControllerEx, int paramInt, String paramString, ServoConfigurationType paramServoConfigurationType) {
    return (Servo)new ServoImplEx(paramServoControllerEx, paramInt, Servo.Direction.FORWARD, paramServoConfigurationType);
  }
  
  public DcMotorController createUsbDcMotorController(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameMotorController));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          RobotUsbDevice robotUsbDevice2 = null;
          RobotUsbDevice robotUsbDevice1 = null;
          try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openRobotUsbDevice(true, HardwareDeviceManager.this.usbManager, serialNumber);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            byte[] arrayOfByte = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(robotUsbDevice);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            if (HardwareDeviceManager.this.getModernRoboticsDeviceType(robotUsbDevice, arrayOfByte) != DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_DC_MOTOR_CONTROLLER) {
              robotUsbDevice1 = robotUsbDevice;
              robotUsbDevice2 = robotUsbDevice;
              HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(robotUsbDevice, serialNumber);
            } 
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            robotUsbDevice.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(arrayOfByte));
            return robotUsbDevice;
          } catch (RobotCoreException robotCoreException2) {
            robotUsbDevice1 = robotUsbDevice2;
            RobotCoreException robotCoreException1 = robotCoreException2;
          } catch (RuntimeException runtimeException) {}
          if (robotUsbDevice1 != null)
            robotUsbDevice1.close(); 
          throw runtimeException;
        }
      };
    ModernRoboticsUsbDcMotorController modernRoboticsUsbDcMotorController = new ModernRoboticsUsbDcMotorController(this.context, serialNumber, openRobotUsbDevice, this.manager);
    modernRoboticsUsbDcMotorController.armOrPretend();
    modernRoboticsUsbDcMotorController.initializeHardware();
    return (DcMotorController)modernRoboticsUsbDcMotorController;
  }
  
  public LegacyModule createUsbLegacyModule(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameLegacyModule));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          RobotUsbDevice robotUsbDevice2 = null;
          RobotUsbDevice robotUsbDevice1 = null;
          try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openRobotUsbDevice(true, HardwareDeviceManager.this.usbManager, serialNumber);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            byte[] arrayOfByte = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(robotUsbDevice);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            if (HardwareDeviceManager.this.getModernRoboticsDeviceType(robotUsbDevice, arrayOfByte) != DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_LEGACY_MODULE) {
              robotUsbDevice1 = robotUsbDevice;
              robotUsbDevice2 = robotUsbDevice;
              HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(robotUsbDevice, serialNumber);
            } 
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            robotUsbDevice.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(arrayOfByte));
            return robotUsbDevice;
          } catch (RobotCoreException robotCoreException2) {
            robotUsbDevice1 = robotUsbDevice2;
            RobotCoreException robotCoreException1 = robotCoreException2;
          } catch (RuntimeException runtimeException) {}
          if (robotUsbDevice1 != null)
            robotUsbDevice1.close(); 
          throw runtimeException;
        }
      };
    ModernRoboticsUsbLegacyModule modernRoboticsUsbLegacyModule = new ModernRoboticsUsbLegacyModule(this.context, serialNumber, openRobotUsbDevice, this.manager);
    modernRoboticsUsbLegacyModule.armOrPretend();
    modernRoboticsUsbLegacyModule.initializeHardware();
    return (LegacyModule)modernRoboticsUsbLegacyModule;
  }
  
  public ServoController createUsbServoController(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameServoController));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          RobotUsbDevice robotUsbDevice2 = null;
          RobotUsbDevice robotUsbDevice1 = null;
          try {
            RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openRobotUsbDevice(true, HardwareDeviceManager.this.usbManager, serialNumber);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            byte[] arrayOfByte = HardwareDeviceManager.this.getModernRoboticsDeviceHeader(robotUsbDevice);
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            if (HardwareDeviceManager.this.getModernRoboticsDeviceType(robotUsbDevice, arrayOfByte) != DeviceManager.UsbDeviceType.MODERN_ROBOTICS_USB_SERVO_CONTROLLER) {
              robotUsbDevice1 = robotUsbDevice;
              robotUsbDevice2 = robotUsbDevice;
              HardwareDeviceManager.this.closeAndThrowOnFailedDeviceTypeCheck(robotUsbDevice, serialNumber);
            } 
            robotUsbDevice1 = robotUsbDevice;
            robotUsbDevice2 = robotUsbDevice;
            robotUsbDevice.setFirmwareVersion(HardwareDeviceManager.this.getModernRoboticsFirmwareVersion(arrayOfByte));
            return robotUsbDevice;
          } catch (RobotCoreException robotCoreException) {
            if (robotUsbDevice2 != null)
              robotUsbDevice2.close(); 
            throw robotCoreException;
          } catch (RuntimeException runtimeException) {
            if (robotCoreException != null)
              robotCoreException.close(); 
            throw runtimeException;
          } 
        }
      };
    ModernRoboticsUsbServoController modernRoboticsUsbServoController = new ModernRoboticsUsbServoController(this.context, serialNumber, openRobotUsbDevice, this.manager);
    modernRoboticsUsbServoController.armOrPretend();
    modernRoboticsUsbServoController.initializeHardware();
    return (ServoController)modernRoboticsUsbServoController;
  }
  
  public HardwareDevice createUserI2cDevice(I2cController paramI2cController, DeviceConfiguration.I2cChannel paramI2cChannel, I2cDeviceConfigurationType paramI2cDeviceConfigurationType, String paramString) {
    RobotLog.v("Creating user sensor %s - Channel: %d", new Object[] { paramI2cDeviceConfigurationType.getName(), Integer.valueOf(paramI2cChannel.channel) });
    return paramI2cDeviceConfigurationType.createInstance(paramI2cController, paramI2cChannel.channel);
  }
  
  public HardwareDevice createUserI2cDevice(final RobotCoreLynxModule lynxModule, final DeviceConfiguration.I2cChannel bus, I2cDeviceConfigurationType paramI2cDeviceConfigurationType, final String name) {
    RobotLog.v("Creating user sensor %s - on Lynx module=%d bus=%d", new Object[] { paramI2cDeviceConfigurationType.getName(), Integer.valueOf(lynxModule.getModuleAddress()), Integer.valueOf(bus.channel) });
    return paramI2cDeviceConfigurationType.createInstance(lynxModule, new Func<I2cDeviceSynchSimple>() {
          public I2cDeviceSynchSimple value() {
            return HardwareDeviceManager.this.createI2cDeviceSynchSimple(lynxModule, bus, name);
          }
        }new Func<I2cDeviceSynch>() {
          public I2cDeviceSynch value() {
            return HardwareDeviceManager.this.createI2cDeviceSynch(lynxModule, bus, name);
          }
        });
  }
  
  public WebcamName createWebcamName(final SerialNumber serialNumber, String paramString) throws RobotCoreException, InterruptedException {
    Context context = this.context;
    HardwareFactory.noteSerialNumberType(context, serialNumber, context.getString(R.string.moduleDisplayNameWebcam));
    RobotLog.v("Creating %s", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, serialNumber) });
    ArmableUsbDevice.OpenRobotUsbDevice openRobotUsbDevice = new ArmableUsbDevice.OpenRobotUsbDevice() {
        public RobotUsbDevice open() throws RobotCoreException {
          if (!((CameraManagerInternal)ClassFactory.getInstance().getCameraManager()).isWebcamAttached(serialNumber)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to find webcam with serial number ");
            stringBuilder.append(serialNumber);
            RobotLog.logAndThrow(stringBuilder.toString());
          } 
          return null;
        }
      };
    WebcamName webcamName = ((CameraManagerInternal)ClassFactory.getInstance().getCameraManager()).webcamNameFromSerialNumber(serialNumber, openRobotUsbDevice, this.manager);
    if (webcamName instanceof UserNameable)
      ((UserNameable)webcamName).setUserName(paramString); 
    ((RobotUsbModule)webcamName).armOrPretend();
    return webcamName;
  }
  
  void determineDeviceType(RobotUsbDevice paramRobotUsbDevice, SerialNumber paramSerialNumber, ScannedDevices paramScannedDevices) {
    DeviceManager.UsbDeviceType usbDeviceType2 = RobotUsbDeviceImplBase.getDeviceType(paramSerialNumber);
    DeviceManager.UsbDeviceType usbDeviceType1 = usbDeviceType2;
    if (usbDeviceType2 == DeviceManager.UsbDeviceType.UNKNOWN_DEVICE) {
      RobotUsbDevice.USBIdentifiers uSBIdentifiers = paramRobotUsbDevice.getUsbIdentifiers();
      if (uSBIdentifiers.isModernRoboticsDevice())
        try {
          RobotLog.vv("USBScan", "getting MR device device header %s ...", new Object[] { paramSerialNumber });
          usbDeviceType1 = getModernRoboticsDeviceType(paramRobotUsbDevice);
          RobotLog.vv("USBScan", "... done getting MR device device header %s type=%s", new Object[] { paramSerialNumber, usbDeviceType1 });
          paramScannedDevices.put(paramSerialNumber, usbDeviceType1);
          return;
        } catch (RobotCoreException robotCoreException) {
          RobotLog.vv("USBScan", "exception retrieving MR device device header %s", new Object[] { paramSerialNumber });
          return;
        }  
      if (usbDeviceType1.isLynxDevice()) {
        RobotLog.vv("USBScan", "%s is a lynx device", new Object[] { paramSerialNumber });
        usbDeviceType1 = getLynxDeviceType((RobotUsbDevice)robotCoreException);
      } else {
        return;
      } 
    } 
    paramScannedDevices.put(paramSerialNumber, usbDeviceType1);
  }
  
  DeviceManager.UsbDeviceType getLynxDeviceType(RobotUsbDevice paramRobotUsbDevice) {
    DeviceManager.UsbDeviceType usbDeviceType = DeviceManager.UsbDeviceType.LYNX_USB_DEVICE;
    paramRobotUsbDevice.setDeviceType(usbDeviceType);
    return usbDeviceType;
  }
  
  byte[] getModernRoboticsDeviceHeader(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException {
    try {
      return ModernRoboticsUsbUtil.getUsbDeviceHeader(paramRobotUsbDevice);
    } catch (RobotUsbException robotUsbException) {
      throw RobotCoreException.createChained(robotUsbException, "comm error retrieving MR device header(%s)", new Object[] { robotUsbException.getStackTrace()[0] });
    } 
  }
  
  DeviceManager.UsbDeviceType getModernRoboticsDeviceType(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException {
    return getModernRoboticsDeviceType(paramRobotUsbDevice, getModernRoboticsDeviceHeader(paramRobotUsbDevice));
  }
  
  DeviceManager.UsbDeviceType getModernRoboticsDeviceType(RobotUsbDevice paramRobotUsbDevice, byte[] paramArrayOfbyte) {
    DeviceManager.UsbDeviceType usbDeviceType = ModernRoboticsUsbUtil.getDeviceType(paramArrayOfbyte);
    paramRobotUsbDevice.setDeviceType(usbDeviceType);
    return usbDeviceType;
  }
  
  public ScannedDevices scanForUsbDevices() throws RobotCoreException {
    synchronized (scanDevicesLock) {
      long l = System.nanoTime();
      ScannedDevices scannedDevices = new ScannedDevices();
      List list = this.usbManager.scanForDevices();
      int i = list.size();
      RobotLog.vv("USBScan", "device count=%d", new Object[] { Integer.valueOf(i) });
      if (i > 0) {
        ExecutorService executorService = ThreadPool.newFixedThreadPool(i, "hw mgr usb scan");
        final ConcurrentHashMap<Object, Object> newlyFoundDevices = new ConcurrentHashMap<Object, Object>();
        try {
          Iterator<SerialNumber> iterator = list.iterator();
          while (iterator.hasNext()) {
            executorService.execute(new Runnable() {
                  public void run() {
                    Exception exception;
                    try {
                      RobotLog.vv("USBScan", "opening %s...", new Object[] { this.val$serialNumber });
                      RobotUsbDevice robotUsbDevice = ModernRoboticsUsbUtil.openRobotUsbDevice(false, HardwareDeviceManager.this.usbManager, serialNumber);
                      newlyFoundDevices.put(serialNumber, robotUsbDevice);
                      RobotLog.vv("USBScan", "... done opening %s", new Object[] { this.val$serialNumber });
                      return;
                    } catch (Exception null) {
                      RobotLog.vv("USBScan", "%s(%s) exception while opening %s", new Object[] { exception.getClass().getSimpleName(), exception.getMessage(), this.val$serialNumber });
                      RobotLog.vv("USBScan", "... done opening %s", new Object[] { this.val$serialNumber });
                      return;
                    } finally {}
                    RobotLog.vv("USBScan", "... done opening %s", new Object[] { this.val$serialNumber });
                    throw exception;
                  }
                });
          } 
          executorService.shutdown();
          ThreadPool.awaitTerminationOrExitApplication(executorService, 30L, TimeUnit.SECONDS, "USB Scanning Service", "internal error");
          for (Map.Entry<Object, Object> entry : concurrentHashMap.entrySet())
            determineDeviceType((RobotUsbDevice)entry.getValue(), (SerialNumber)entry.getKey(), scannedDevices); 
          for (RobotUsbDevice robotUsbDevice : RobotUsbDeviceImplBase.getExtantDevices()) {
            SerialNumber serialNumber = robotUsbDevice.getSerialNumber();
            if (!concurrentHashMap.containsKey(serialNumber)) {
              DeviceManager.UsbDeviceType usbDeviceType = robotUsbDevice.getDeviceType();
              if (usbDeviceType != DeviceManager.UsbDeviceType.FTDI_USB_UNKNOWN_DEVICE) {
                RobotLog.vv("USBScan", "added extant device %s type=%s", new Object[] { serialNumber, usbDeviceType.toString() });
                scannedDevices.put(serialNumber, usbDeviceType);
              } 
            } 
          } 
        } finally {
          for (Map.Entry<Object, Object> entry : concurrentHashMap.entrySet()) {
            RobotLog.vv("USBScan", "closing %s", new Object[] { entry.getKey() });
            ((RobotUsbDevice)entry.getValue()).close();
          } 
        } 
      } 
      scanForWebcams(scannedDevices);
      RobotLog.vv("USBScan", "scanForUsbDevices() took %dms count=%d", new Object[] { Integer.valueOf((int)((System.nanoTime() - l) / 1000000L)), Integer.valueOf(scannedDevices.size()) });
      return scannedDevices;
    } 
  }
  
  protected void scanForWebcams(ScannedDevices paramScannedDevices) {
    synchronized (scanDevicesLock) {
      List list = ClassFactory.getInstance().getCameraManager().getAllWebcams();
      HashMap<Object, Object> hashMap1 = new HashMap<Object, Object>();
      HashMap<Object, Object> hashMap2 = new HashMap<Object, Object>();
      Iterator<WebcamName> iterator1 = list.iterator();
      while (iterator1.hasNext()) {
        SerialNumber serialNumber = ((WebcamName)iterator1.next()).getSerialNumber();
        if (serialNumber.isVendorProduct()) {
          VendorProductSerialNumber vendorProductSerialNumber = (VendorProductSerialNumber)serialNumber;
          if (TextUtils.isEmpty(vendorProductSerialNumber.getConnectionPath())) {
            addVidPid((Map)hashMap2, vendorProductSerialNumber, 1);
            continue;
          } 
          addVidPid((Map)hashMap1, vendorProductSerialNumber, 1);
        } 
      } 
      Iterator<WebcamName> iterator2 = list.iterator();
      while (iterator2.hasNext()) {
        SerialNumber serialNumber2 = ((WebcamName)iterator2.next()).getSerialNumber();
        SerialNumber serialNumber1 = serialNumber2;
        if (serialNumber2.isVendorProduct()) {
          VendorProductSerialNumber vendorProductSerialNumber = (VendorProductSerialNumber)serialNumber2;
          int i = countVidPid((Map)hashMap2, vendorProductSerialNumber).intValue();
          if (i > 1) {
            RobotLog.ee("HardwareDeviceManager", "%d serialnumless webcams w/o connection info; ignoring", new Object[] { Integer.valueOf(i), vendorProductSerialNumber });
            continue;
          } 
          serialNumber1 = serialNumber2;
          if (countVidPid((Map)hashMap2, vendorProductSerialNumber).intValue() == 0) {
            serialNumber1 = serialNumber2;
            if (countVidPid((Map)hashMap1, vendorProductSerialNumber).intValue() == 1)
              serialNumber1 = SerialNumber.fromVidPid(vendorProductSerialNumber.getVendorId(), vendorProductSerialNumber.getProductId(), ""); 
          } 
        } 
        RobotLog.vv("HardwareDeviceManager", "scanned webcam serial=%s", new Object[] { serialNumber1 });
        paramScannedDevices.put(serialNumber1, DeviceManager.UsbDeviceType.WEBCAM);
      } 
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\HardwareDeviceManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */