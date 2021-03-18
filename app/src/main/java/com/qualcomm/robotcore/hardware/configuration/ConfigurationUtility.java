package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DeviceManager;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.I2cDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.function.Supplier;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class ConfigurationUtility {
  public static final String TAG = "ConfigurationUtility";
  
  public static final int firstNamedDeviceNumber = 1;
  
  protected Set<String> existingNames;
  
  public ConfigurationUtility() {
    resetNameUniquifiers();
  }
  
  protected static List<DeviceConfiguration> buildEmptyDevices(int paramInt1, int paramInt2, ConfigurationType paramConfigurationType) {
    ArrayList<DeviceConfiguration> arrayList = new ArrayList();
    for (int i = 0; i < paramInt2; i++)
      arrayList.add(new DeviceConfiguration(i + paramInt1, paramConfigurationType, "NO$DEVICE$ATTACHED", false)); 
    return arrayList;
  }
  
  public static List<DeviceConfiguration> buildEmptyMotors(int paramInt1, int paramInt2) {
    return buildEmptyDevices(paramInt1, paramInt2, (ConfigurationType)MotorConfigurationType.getUnspecifiedMotorType());
  }
  
  public static List<DeviceConfiguration> buildEmptyServos(int paramInt1, int paramInt2) {
    return buildEmptyDevices(paramInt1, paramInt2, (ConfigurationType)ServoConfigurationType.getStandardServoType());
  }
  
  private String createUniqueName(ConfigurationType paramConfigurationType, String paramString1, String paramString2, int paramInt) {
    Set<String> set = getExistingNames(paramConfigurationType);
    if (paramString1 != null && !set.contains(paramString1)) {
      noteExistingName(paramConfigurationType, paramString1);
      return paramString1;
    } 
    paramString1 = Misc.formatForUser(paramString2, new Object[] { Integer.valueOf(paramInt) });
    if (!set.contains(paramString1)) {
      noteExistingName(paramConfigurationType, paramString1);
      return paramString1;
    } 
    for (paramInt = 1;; paramInt++) {
      paramString1 = Misc.formatForUser(paramString2, new Object[] { Integer.valueOf(paramInt) });
      if (!set.contains(paramString1)) {
        noteExistingName(paramConfigurationType, paramString1);
        return paramString1;
      } 
    } 
  }
  
  protected LynxModuleConfiguration buildEmptyLynxModule(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    RobotLog.vv("ConfigurationUtility", "buildEmptyLynxModule() mod#=%d...", new Object[] { Integer.valueOf(paramInt) });
    noteExistingName(BuiltInConfigurationType.LYNX_MODULE, paramString);
    LynxModuleConfiguration lynxModuleConfiguration = new LynxModuleConfiguration(paramString);
    lynxModuleConfiguration.setModuleAddress(paramInt);
    lynxModuleConfiguration.setIsParent(paramBoolean1);
    lynxModuleConfiguration.setEnabled(paramBoolean2);
    RobotLog.vv("ConfigurationUtility", "...buildEmptyLynxModule() mod#=%d", new Object[] { Integer.valueOf(paramInt) });
    return lynxModuleConfiguration;
  }
  
  public ControllerConfiguration buildNewControllerConfiguration(SerialNumber paramSerialNumber, DeviceManager.UsbDeviceType paramUsbDeviceType, Supplier<LynxModuleMetaList> paramSupplier) {
    switch (paramUsbDeviceType) {
      default:
        return null;
      case null:
        return buildNewLynxUsbDevice(paramSerialNumber, paramSupplier);
      case null:
        return buildNewWebcam(paramSerialNumber);
      case null:
        return buildNewDeviceInterfaceModule(paramSerialNumber);
      case null:
        return buildNewLegacyModule(paramSerialNumber);
      case null:
        return buildNewModernServoController(paramSerialNumber);
      case null:
        break;
    } 
    return buildNewModernMotorController(paramSerialNumber);
  }
  
  public DeviceInterfaceModuleConfiguration buildNewDeviceInterfaceModule(SerialNumber paramSerialNumber) {
    DeviceInterfaceModuleConfiguration deviceInterfaceModuleConfiguration = new DeviceInterfaceModuleConfiguration(createUniqueName(BuiltInConfigurationType.DEVICE_INTERFACE_MODULE, R.string.counted_device_interface_module_name), paramSerialNumber);
    deviceInterfaceModuleConfiguration.setPwmOutputs(buildEmptyDevices(0, 2, BuiltInConfigurationType.PULSE_WIDTH_DEVICE));
    deviceInterfaceModuleConfiguration.setI2cDevices(buildEmptyDevices(0, 6, BuiltInConfigurationType.I2C_DEVICE));
    deviceInterfaceModuleConfiguration.setAnalogInputDevices(buildEmptyDevices(0, 8, BuiltInConfigurationType.NOTHING));
    deviceInterfaceModuleConfiguration.setDigitalDevices(buildEmptyDevices(0, 8, BuiltInConfigurationType.NOTHING));
    deviceInterfaceModuleConfiguration.setAnalogOutputDevices(buildEmptyDevices(0, 2, BuiltInConfigurationType.NOTHING));
    return deviceInterfaceModuleConfiguration;
  }
  
  protected LynxUsbDeviceConfiguration buildNewEmbeddedLynxUsbDevice(final DeviceManager deviceManager) {
    LynxUsbDeviceConfiguration lynxUsbDeviceConfiguration = buildNewLynxUsbDevice(LynxConstants.SERIAL_NUMBER_EMBEDDED, new Supplier<LynxModuleMetaList>() {
          public LynxModuleMetaList get() {
            // Byte code:
            //   0: aload_0
            //   1: getfield val$deviceManager : Lcom/qualcomm/robotcore/hardware/DeviceManager;
            //   4: getstatic com/qualcomm/robotcore/hardware/configuration/LynxConstants.SERIAL_NUMBER_EMBEDDED : Lcom/qualcomm/robotcore/util/SerialNumber;
            //   7: aconst_null
            //   8: invokeinterface createLynxUsbDevice : (Lcom/qualcomm/robotcore/util/SerialNumber;Ljava/lang/String;)Lcom/qualcomm/robotcore/hardware/RobotCoreLynxUsbDevice;
            //   13: astore_2
            //   14: aload_2
            //   15: astore_1
            //   16: aload_2
            //   17: invokeinterface discoverModules : ()Lcom/qualcomm/robotcore/hardware/LynxModuleMetaList;
            //   22: astore_3
            //   23: aload_2
            //   24: ifnull -> 33
            //   27: aload_2
            //   28: invokeinterface close : ()V
            //   33: aload_3
            //   34: areturn
            //   35: astore_3
            //   36: goto -> 48
            //   39: astore_1
            //   40: aconst_null
            //   41: astore_2
            //   42: goto -> 90
            //   45: astore_3
            //   46: aconst_null
            //   47: astore_2
            //   48: aload_2
            //   49: astore_1
            //   50: ldc 'ConfigurationUtility'
            //   52: aload_3
            //   53: ldc 'exception in buildNewEmbeddedLynxUsbDevice()'
            //   55: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
            //   58: aload_2
            //   59: ifnull -> 83
            //   62: goto -> 77
            //   65: aload_2
            //   66: astore_1
            //   67: invokestatic currentThread : ()Ljava/lang/Thread;
            //   70: invokevirtual interrupt : ()V
            //   73: aload_2
            //   74: ifnull -> 83
            //   77: aload_2
            //   78: invokeinterface close : ()V
            //   83: aconst_null
            //   84: areturn
            //   85: astore_3
            //   86: aload_1
            //   87: astore_2
            //   88: aload_3
            //   89: astore_1
            //   90: aload_2
            //   91: ifnull -> 100
            //   94: aload_2
            //   95: invokeinterface close : ()V
            //   100: aload_1
            //   101: athrow
            //   102: astore_1
            //   103: goto -> 110
            //   106: astore_1
            //   107: goto -> 65
            //   110: aconst_null
            //   111: astore_2
            //   112: goto -> 65
            // Exception table:
            //   from	to	target	type
            //   0	14	102	java/lang/InterruptedException
            //   0	14	45	com/qualcomm/robotcore/exception/RobotCoreException
            //   0	14	39	finally
            //   16	23	106	java/lang/InterruptedException
            //   16	23	35	com/qualcomm/robotcore/exception/RobotCoreException
            //   16	23	85	finally
            //   50	58	85	finally
            //   67	73	85	finally
          }
        });
    lynxUsbDeviceConfiguration.setEnabled(true);
    lynxUsbDeviceConfiguration.setSystemSynthetic(true);
    Iterator<LynxModuleConfiguration> iterator = lynxUsbDeviceConfiguration.getModules().iterator();
    while (iterator.hasNext())
      ((LynxModuleConfiguration)iterator.next()).setSystemSynthetic(true); 
    return lynxUsbDeviceConfiguration;
  }
  
  public LegacyModuleControllerConfiguration buildNewLegacyModule(SerialNumber paramSerialNumber) {
    ArrayList<DeviceConfiguration> arrayList = new ArrayList();
    for (int i = 0; i < 6; i++)
      arrayList.add(new DeviceConfiguration(i + 0, BuiltInConfigurationType.NOTHING)); 
    return new LegacyModuleControllerConfiguration(createUniqueName(BuiltInConfigurationType.LEGACY_MODULE_CONTROLLER, R.string.counted_legacy_module_name), arrayList, paramSerialNumber);
  }
  
  public LynxModuleConfiguration buildNewLynxModule(int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    String str1;
    if (paramBoolean3) {
      str1 = createUniqueName(BuiltInConfigurationType.LYNX_MODULE, R.string.control_hub_module_name, R.string.counted_lynx_module_name, 0);
    } else {
      str1 = createUniqueName(BuiltInConfigurationType.LYNX_MODULE, R.string.counted_lynx_module_name, paramInt);
    } 
    LynxModuleConfiguration lynxModuleConfiguration = buildEmptyLynxModule(str1, paramInt, paramBoolean1, paramBoolean2);
    AppUtil.getDefContext();
    I2cDeviceConfigurationType i2cDeviceConfigurationType = I2cDeviceConfigurationType.getLynxEmbeddedIMUType();
    if (i2cDeviceConfigurationType != null && i2cDeviceConfigurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.I2C)) {
      paramBoolean1 = true;
    } else {
      paramBoolean1 = false;
    } 
    Assert.assertTrue(paramBoolean1);
    String str2 = createUniqueName((ConfigurationType)i2cDeviceConfigurationType, R.string.preferred_imu_name, R.string.counted_imu_name, 1);
    LynxI2cDeviceConfiguration lynxI2cDeviceConfiguration = new LynxI2cDeviceConfiguration();
    lynxI2cDeviceConfiguration.setConfigurationType((ConfigurationType)i2cDeviceConfigurationType);
    lynxI2cDeviceConfiguration.setName(str2);
    lynxI2cDeviceConfiguration.setEnabled(true);
    lynxI2cDeviceConfiguration.setBus(0);
    lynxModuleConfiguration.getI2cDevices().add(lynxI2cDeviceConfiguration);
    return lynxModuleConfiguration;
  }
  
  public LynxUsbDeviceConfiguration buildNewLynxUsbDevice(SerialNumber paramSerialNumber, LynxModuleMetaList paramLynxModuleMetaList) {
    // Byte code:
    //   0: ldc 'ConfigurationUtility'
    //   2: ldc_w 'buildNewLynxUsbDevice(%s)...'
    //   5: iconst_1
    //   6: anewarray java/lang/Object
    //   9: dup
    //   10: iconst_0
    //   11: aload_1
    //   12: aastore
    //   13: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   16: aload_1
    //   17: invokevirtual isEmbedded : ()Z
    //   20: istore #4
    //   22: aload_2
    //   23: astore #5
    //   25: aload_2
    //   26: ifnonnull -> 42
    //   29: new com/qualcomm/robotcore/hardware/LynxModuleMetaList
    //   32: dup
    //   33: aload_1
    //   34: invokespecial <init> : (Lcom/qualcomm/robotcore/util/SerialNumber;)V
    //   37: astore #5
    //   39: goto -> 42
    //   42: ldc 'ConfigurationUtility'
    //   44: ldc_w 'buildLynxUsbDevice(): discovered lynx modules: %s'
    //   47: iconst_1
    //   48: anewarray java/lang/Object
    //   51: dup
    //   52: iconst_0
    //   53: aload #5
    //   55: aastore
    //   56: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   59: new java/util/LinkedList
    //   62: dup
    //   63: invokespecial <init> : ()V
    //   66: astore #6
    //   68: aload #5
    //   70: invokevirtual iterator : ()Ljava/util/Iterator;
    //   73: astore_2
    //   74: aload_2
    //   75: invokeinterface hasNext : ()Z
    //   80: ifeq -> 150
    //   83: aload_2
    //   84: invokeinterface next : ()Ljava/lang/Object;
    //   89: checkcast com/qualcomm/robotcore/hardware/LynxModuleMeta
    //   92: astore #5
    //   94: iload #4
    //   96: ifeq -> 270
    //   99: aload #5
    //   101: invokevirtual isParent : ()Z
    //   104: ifeq -> 270
    //   107: aload #5
    //   109: invokevirtual getModuleAddress : ()I
    //   112: sipush #173
    //   115: if_icmpne -> 270
    //   118: iconst_1
    //   119: istore_3
    //   120: goto -> 123
    //   123: aload #6
    //   125: aload_0
    //   126: aload #5
    //   128: invokevirtual getModuleAddress : ()I
    //   131: aload #5
    //   133: invokevirtual isParent : ()Z
    //   136: iconst_1
    //   137: iload_3
    //   138: invokevirtual buildNewLynxModule : (IZZZ)Lcom/qualcomm/robotcore/hardware/configuration/LynxModuleConfiguration;
    //   141: invokeinterface add : (Ljava/lang/Object;)Z
    //   146: pop
    //   147: goto -> 74
    //   150: aload #6
    //   152: invokestatic sortByName : (Ljava/util/List;)V
    //   155: ldc 'ConfigurationUtility'
    //   157: ldc_w 'buildNewLynxUsbDevice(%s): %d modules'
    //   160: iconst_2
    //   161: anewarray java/lang/Object
    //   164: dup
    //   165: iconst_0
    //   166: aload_1
    //   167: aastore
    //   168: dup
    //   169: iconst_1
    //   170: aload #6
    //   172: invokeinterface size : ()I
    //   177: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   180: aastore
    //   181: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   184: iload #4
    //   186: ifeq -> 207
    //   189: aload_0
    //   190: getstatic com/qualcomm/robotcore/hardware/configuration/BuiltInConfigurationType.LYNX_USB_DEVICE : Lcom/qualcomm/robotcore/hardware/configuration/BuiltInConfigurationType;
    //   193: getstatic com/qualcomm/robotcore/R$string.control_hub_usb_device_name : I
    //   196: getstatic com/qualcomm/robotcore/R$string.counted_lynx_usb_device_name : I
    //   199: iconst_0
    //   200: invokevirtual createUniqueName : (Lcom/qualcomm/robotcore/hardware/configuration/ConfigurationType;III)Ljava/lang/String;
    //   203: astore_2
    //   204: goto -> 218
    //   207: aload_0
    //   208: getstatic com/qualcomm/robotcore/hardware/configuration/BuiltInConfigurationType.LYNX_USB_DEVICE : Lcom/qualcomm/robotcore/hardware/configuration/BuiltInConfigurationType;
    //   211: getstatic com/qualcomm/robotcore/R$string.counted_lynx_usb_device_name : I
    //   214: invokevirtual createUniqueName : (Lcom/qualcomm/robotcore/hardware/configuration/ConfigurationType;I)Ljava/lang/String;
    //   217: astore_2
    //   218: new com/qualcomm/robotcore/hardware/configuration/LynxUsbDeviceConfiguration
    //   221: dup
    //   222: aload_2
    //   223: aload #6
    //   225: aload_1
    //   226: invokespecial <init> : (Ljava/lang/String;Ljava/util/List;Lcom/qualcomm/robotcore/util/SerialNumber;)V
    //   229: astore_2
    //   230: ldc 'ConfigurationUtility'
    //   232: ldc_w '...buildNewLynxUsbDevice(%s): '
    //   235: iconst_1
    //   236: anewarray java/lang/Object
    //   239: dup
    //   240: iconst_0
    //   241: aload_1
    //   242: aastore
    //   243: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   246: aload_2
    //   247: areturn
    //   248: ldc 'ConfigurationUtility'
    //   250: ldc_w '...buildNewLynxUsbDevice(%s): '
    //   253: iconst_1
    //   254: anewarray java/lang/Object
    //   257: dup
    //   258: iconst_0
    //   259: aload_1
    //   260: aastore
    //   261: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   264: aload_2
    //   265: athrow
    //   266: astore_2
    //   267: goto -> 248
    //   270: iconst_0
    //   271: istore_3
    //   272: goto -> 123
    // Exception table:
    //   from	to	target	type
    //   29	39	266	finally
    //   42	74	266	finally
    //   74	94	266	finally
    //   99	118	266	finally
    //   123	147	266	finally
    //   150	184	266	finally
    //   189	204	266	finally
    //   207	218	266	finally
    //   218	230	266	finally
  }
  
  public LynxUsbDeviceConfiguration buildNewLynxUsbDevice(SerialNumber paramSerialNumber, Supplier<LynxModuleMetaList> paramSupplier) {
    return buildNewLynxUsbDevice(paramSerialNumber, (LynxModuleMetaList)paramSupplier.get());
  }
  
  public MotorControllerConfiguration buildNewModernMotorController(SerialNumber paramSerialNumber) {
    List<DeviceConfiguration> list = buildEmptyMotors(1, 2);
    return new MotorControllerConfiguration(createUniqueName(BuiltInConfigurationType.MOTOR_CONTROLLER, R.string.counted_motor_controller_name), list, paramSerialNumber);
  }
  
  public ServoControllerConfiguration buildNewModernServoController(SerialNumber paramSerialNumber) {
    List<DeviceConfiguration> list = buildEmptyServos(1, 6);
    return new ServoControllerConfiguration(createUniqueName(BuiltInConfigurationType.SERVO_CONTROLLER, R.string.counted_servo_controller_name), list, paramSerialNumber);
  }
  
  public WebcamConfiguration buildNewWebcam(SerialNumber paramSerialNumber) {
    return new WebcamConfiguration(createUniqueName(BuiltInConfigurationType.WEBCAM, R.string.counted_camera_name), paramSerialNumber);
  }
  
  protected String createUniqueName(ConfigurationType paramConfigurationType, int paramInt) {
    return createUniqueName(paramConfigurationType, AppUtil.getDefContext().getString(paramInt), 1);
  }
  
  protected String createUniqueName(ConfigurationType paramConfigurationType, int paramInt1, int paramInt2) {
    return createUniqueName(paramConfigurationType, AppUtil.getDefContext().getString(paramInt1), paramInt2);
  }
  
  protected String createUniqueName(ConfigurationType paramConfigurationType, int paramInt1, int paramInt2, int paramInt3) {
    return createUniqueName(paramConfigurationType, AppUtil.getDefContext().getString(paramInt1), AppUtil.getDefContext().getString(paramInt2), paramInt3);
  }
  
  protected String createUniqueName(ConfigurationType paramConfigurationType, String paramString, int paramInt) {
    return createUniqueName(paramConfigurationType, (String)null, paramString, paramInt);
  }
  
  public Set<String> getExistingNames(ConfigurationType paramConfigurationType) {
    return this.existingNames;
  }
  
  protected void noteExistingName(ConfigurationType paramConfigurationType, String paramString) {
    getExistingNames(paramConfigurationType).add(paramString);
  }
  
  public void resetNameUniquifiers() {
    this.existingNames = new HashSet<String>();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ConfigurationUtility.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */