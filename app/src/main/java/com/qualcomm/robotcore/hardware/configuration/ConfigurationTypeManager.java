package com.qualcomm.robotcore.hardware.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.configuration.annotations.AnalogSensorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.DigitalIoDeviceType;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFPositionParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.ExpansionHubPIDFVelocityParams;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.hardware.configuration.annotations.MotorType;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.AnalogSensorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.DigitalIoDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.I2cDeviceConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.InstantiableUserConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.UserConfigurationType;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.opmode.ClassFilter;

public final class ConfigurationTypeManager implements ClassFilter {
  public static boolean DEBUG = true;
  
  public static final String TAG = "UserDeviceTypeManager";
  
  private static String standardServoTypeXmlTag;
  
  private static ConfigurationTypeManager theInstance = new ConfigurationTypeManager();
  
  private static final Class[] typeAnnotationsArray;
  
  private static final List<Class> typeAnnotationsList;
  
  private static String unspecifiedMotorTypeXmlTag = getXmlTag(UnspecifiedMotor.class);
  
  private Map<ConfigurationType.DeviceFlavor, Set<String>> existingTypeDisplayNamesMap = new HashMap<ConfigurationType.DeviceFlavor, Set<String>>();
  
  private Set<String> existingXmlTags = new HashSet<String>();
  
  private Gson gson = newGson();
  
  private Map<String, UserConfigurationType> mapTagToUserType = new HashMap<String, UserConfigurationType>();
  
  private Comparator<? super ConfigurationType> simpleConfigTypeComparator = new Comparator<ConfigurationType>() {
      public int compare(ConfigurationType param1ConfigurationType1, ConfigurationType param1ConfigurationType2) {
        return param1ConfigurationType1.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal).compareTo(param1ConfigurationType2.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal));
      }
    };
  
  static {
    standardServoTypeXmlTag = getXmlTag(Servo.class);
    Class[] arrayOfClass = new Class[5];
    arrayOfClass[0] = ServoType.class;
    arrayOfClass[1] = AnalogSensorType.class;
    arrayOfClass[2] = DigitalIoDeviceType.class;
    arrayOfClass[3] = I2cDeviceType.class;
    arrayOfClass[4] = MotorType.class;
    typeAnnotationsArray = arrayOfClass;
    typeAnnotationsList = Arrays.asList(arrayOfClass);
  }
  
  public ConfigurationTypeManager() {
    for (ConfigurationType.DeviceFlavor deviceFlavor : ConfigurationType.DeviceFlavor.values())
      this.existingTypeDisplayNamesMap.put(deviceFlavor, new HashSet<String>()); 
    addBuiltinConfigurationTypes();
  }
  
  private void add(UserConfigurationType paramUserConfigurationType) {
    this.mapTagToUserType.put(paramUserConfigurationType.getXmlTag(), paramUserConfigurationType);
    ((Set<String>)this.existingTypeDisplayNamesMap.get(paramUserConfigurationType.getDeviceFlavor())).add(paramUserConfigurationType.getName());
    this.existingXmlTags.add(paramUserConfigurationType.getXmlTag());
    for (String str : paramUserConfigurationType.getXmlTagAliases()) {
      this.mapTagToUserType.put(str, paramUserConfigurationType);
      this.existingXmlTags.add(str);
    } 
  }
  
  private void addBuiltinConfigurationTypes() {
    for (BuiltInConfigurationType builtInConfigurationType : BuiltInConfigurationType.values()) {
      this.existingXmlTags.add(builtInConfigurationType.getXmlTag());
      ((Set<String>)this.existingTypeDisplayNamesMap.get(builtInConfigurationType.getDeviceFlavor())).add(builtInConfigurationType.getDisplayName(ConfigurationType.DisplayNameFlavor.Normal));
    } 
  }
  
  private boolean addI2cTypeFromDeprecatedAnnotation(Class paramClass) {
    if (isHardwareDevice(paramClass) && paramClass.isAnnotationPresent((Class)I2cSensor.class)) {
      I2cSensor i2cSensor = (I2cSensor)paramClass.getAnnotation(I2cSensor.class);
      I2cDeviceConfigurationType i2cDeviceConfigurationType = new I2cDeviceConfigurationType(paramClass, getXmlTag(i2cSensor));
      i2cDeviceConfigurationType.processAnnotation(i2cSensor);
      i2cDeviceConfigurationType.finishedAnnotations(paramClass);
      if (!checkInstantiableTypeConstraints((InstantiableUserConfigurationType)i2cDeviceConfigurationType))
        return false; 
      add((UserConfigurationType)i2cDeviceConfigurationType);
      return true;
    } 
    return false;
  }
  
  private boolean addMotorTypeFromDeprecatedAnnotation(Class<?> paramClass) {
    if (paramClass.isAnnotationPresent((Class)MotorType.class)) {
      MotorType motorType = (MotorType)paramClass.getAnnotation(MotorType.class);
      MotorConfigurationType motorConfigurationType = new MotorConfigurationType(paramClass, getXmlTag(motorType));
      motorConfigurationType.processAnnotation(motorType);
      processMotorSupportAnnotations(paramClass, motorConfigurationType);
      motorConfigurationType.finishedAnnotations(paramClass);
      if (!checkAnnotationParameterConstraints((UserConfigurationType)motorConfigurationType))
        return false; 
      add((UserConfigurationType)motorConfigurationType);
      return true;
    } 
    return false;
  }
  
  private boolean checkAnnotationParameterConstraints(UserConfigurationType paramUserConfigurationType) {
    if (!isLegalDeviceTypeName(paramUserConfigurationType.getName())) {
      reportConfigurationError("\"%s\" is not a legal device type name", new Object[] { paramUserConfigurationType.getName() });
      return false;
    } 
    if (((Set)this.existingTypeDisplayNamesMap.get(paramUserConfigurationType.getDeviceFlavor())).contains(paramUserConfigurationType.getName())) {
      reportConfigurationError("the device type \"%s\" is already defined", new Object[] { paramUserConfigurationType.getName() });
      return false;
    } 
    if (!isLegalXmlTag(paramUserConfigurationType.getXmlTag())) {
      reportConfigurationError("\"%s\" is not a legal XML tag for the device type \"%s\"", new Object[] { paramUserConfigurationType.getXmlTag(), paramUserConfigurationType.getName() });
      return false;
    } 
    if (this.existingXmlTags.contains(paramUserConfigurationType.getXmlTag())) {
      reportConfigurationError("the XML tag \"%s\" is already defined", new Object[] { paramUserConfigurationType.getXmlTag() });
      return false;
    } 
    return true;
  }
  
  private boolean checkInstantiableTypeConstraints(InstantiableUserConfigurationType paramInstantiableUserConfigurationType) {
    if (!checkAnnotationParameterConstraints((UserConfigurationType)paramInstantiableUserConfigurationType))
      return false; 
    if (!isHardwareDevice(paramInstantiableUserConfigurationType.getClazz())) {
      reportConfigurationError("'%s' class doesn't inherit from the class 'HardwareDevice'", new Object[] { paramInstantiableUserConfigurationType.getClazz().getSimpleName() });
      return false;
    } 
    if (!Modifier.isPublic(paramInstantiableUserConfigurationType.getClazz().getModifiers())) {
      reportConfigurationError("'%s' class is not declared 'public'", new Object[] { paramInstantiableUserConfigurationType.getClazz().getSimpleName() });
      return false;
    } 
    if (!paramInstantiableUserConfigurationType.hasConstructors()) {
      reportConfigurationError("'%s' class lacks necessary constructor", new Object[] { paramInstantiableUserConfigurationType.getClazz().getSimpleName() });
      return false;
    } 
    return true;
  }
  
  private void clearOnBotJavaTypes() {
    for (UserConfigurationType userConfigurationType : new ArrayList(this.mapTagToUserType.values())) {
      if (userConfigurationType.isOnBotJava()) {
        ((Set)this.existingTypeDisplayNamesMap.get(userConfigurationType.getDeviceFlavor())).remove(userConfigurationType.getName());
        this.existingXmlTags.remove(userConfigurationType.getXmlTag());
        this.mapTagToUserType.remove(userConfigurationType.getXmlTag());
      } 
    } 
  }
  
  private void clearUserTypes() {
    for (UserConfigurationType userConfigurationType : new ArrayList(this.mapTagToUserType.values())) {
      ((Set)this.existingTypeDisplayNamesMap.get(userConfigurationType.getDeviceFlavor())).remove(userConfigurationType.getName());
      this.existingXmlTags.remove(userConfigurationType.getXmlTag());
      this.mapTagToUserType.remove(userConfigurationType.getXmlTag());
    } 
  }
  
  private UserConfigurationType createAppropriateConfigurationType(Annotation paramAnnotation, DeviceProperties paramDeviceProperties, Class<?> paramClass) {
    ServoConfigurationType servoConfigurationType;
    MotorConfigurationType motorConfigurationType;
    if (paramAnnotation instanceof ServoType) {
      servoConfigurationType = new ServoConfigurationType(paramClass, getXmlTag(paramDeviceProperties));
      servoConfigurationType.processAnnotation((ServoType)paramAnnotation);
      return (UserConfigurationType)servoConfigurationType;
    } 
    if (paramAnnotation instanceof MotorType) {
      motorConfigurationType = new MotorConfigurationType(paramClass, getXmlTag((DeviceProperties)servoConfigurationType));
      MotorConfigurationType motorConfigurationType1 = motorConfigurationType;
      processMotorSupportAnnotations(paramClass, motorConfigurationType1);
      motorConfigurationType1.processAnnotation((MotorType)paramAnnotation);
      return (UserConfigurationType)motorConfigurationType;
    } 
    return (UserConfigurationType)((paramAnnotation instanceof AnalogSensorType) ? new AnalogSensorConfigurationType(paramClass, getXmlTag((DeviceProperties)motorConfigurationType)) : ((paramAnnotation instanceof DigitalIoDeviceType) ? new DigitalIoDeviceConfigurationType(paramClass, getXmlTag((DeviceProperties)motorConfigurationType)) : ((paramAnnotation instanceof I2cDeviceType) ? new I2cDeviceConfigurationType(paramClass, getXmlTag((DeviceProperties)motorConfigurationType)) : null)));
  }
  
  private <A extends Annotation> A findAnnotation(Class<?> paramClass, final Class<A> annotationType) {
    final ArrayList<Annotation> result = new ArrayList(1);
    arrayList.add(null);
    ClassUtil.searchInheritance(paramClass, new Predicate<Class<?>>() {
          public boolean test(Class<?> param1Class) {
            param1Class = param1Class.getAnnotation(annotationType);
            if (param1Class != null) {
              result.set(0, param1Class);
              return true;
            } 
            return false;
          }
        });
    return (A)arrayList.get(0);
  }
  
  private List<BuiltInConfigurationType> getApplicableBuiltInTypes(ConfigurationType.DeviceFlavor paramDeviceFlavor, ControlSystem paramControlSystem) {
    LinkedList<BuiltInConfigurationType> linkedList = new LinkedList();
    int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$configuration$ConfigurationType$DeviceFlavor[paramDeviceFlavor.ordinal()];
    if (i != 1) {
      if (i != 3) {
        if (i != 4)
          return linkedList; 
        if (paramControlSystem == null || paramControlSystem == ControlSystem.MODERN_ROBOTICS) {
          linkedList.add(BuiltInConfigurationType.TOUCH_SENSOR);
          return linkedList;
        } 
      } else {
        linkedList.add(BuiltInConfigurationType.ANALOG_OUTPUT);
        return linkedList;
      } 
    } else {
      linkedList.add(BuiltInConfigurationType.IR_SEEKER_V3);
      linkedList.add(BuiltInConfigurationType.ADAFRUIT_COLOR_SENSOR);
      linkedList.add(BuiltInConfigurationType.COLOR_SENSOR);
      linkedList.add(BuiltInConfigurationType.GYRO);
      if (paramControlSystem == ControlSystem.REV_HUB)
        linkedList.add(BuiltInConfigurationType.LYNX_COLOR_SENSOR); 
    } 
    return linkedList;
  }
  
  private List<BuiltInConfigurationType> getDeprecatedConfigTypes(ConfigurationType.DeviceFlavor paramDeviceFlavor, ControlSystem paramControlSystem) {
    LinkedList<BuiltInConfigurationType> linkedList = new LinkedList();
    if (null.$SwitchMap$com$qualcomm$robotcore$hardware$configuration$ConfigurationType$DeviceFlavor[paramDeviceFlavor.ordinal()] != 1)
      return linkedList; 
    if (paramControlSystem == null || paramControlSystem == ControlSystem.MODERN_ROBOTICS)
      linkedList.add(BuiltInConfigurationType.I2C_DEVICE); 
    linkedList.add(BuiltInConfigurationType.I2C_DEVICE_SYNCH);
    return linkedList;
  }
  
  public static ConfigurationTypeManager getInstance() {
    return theInstance;
  }
  
  private Annotation getTypeAnnotation(Class paramClass) {
    for (Annotation annotation : paramClass.getAnnotations()) {
      if (typeAnnotationsList.contains(annotation.annotationType()))
        return annotation; 
    } 
    return null;
  }
  
  private static String getXmlTag(I2cSensor paramI2cSensor) {
    return ClassUtil.decodeStringRes(paramI2cSensor.xmlTag().trim());
  }
  
  private static String getXmlTag(MotorType paramMotorType) {
    return ClassUtil.decodeStringRes(paramMotorType.xmlTag().trim());
  }
  
  private static String getXmlTag(DeviceProperties paramDeviceProperties) {
    return ClassUtil.decodeStringRes(paramDeviceProperties.xmlTag().trim());
  }
  
  public static String getXmlTag(Class paramClass) {
    return getXmlTag((DeviceProperties)paramClass.getAnnotation(DeviceProperties.class));
  }
  
  private boolean isHardwareDevice(Class paramClass) {
    return ClassUtil.inheritsFrom(paramClass, HardwareDevice.class);
  }
  
  private boolean isLegalDeviceTypeName(String paramString) {
    return Util.isGoodString(paramString);
  }
  
  private boolean isLegalXmlTag(String paramString) {
    if (!Util.isGoodString(paramString))
      return false; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("^[");
    stringBuilder.append("\\p{Alpha}_:");
    stringBuilder.append("][");
    stringBuilder.append("\\p{Alpha}_:0-9\\-\\.");
    stringBuilder.append("]*$");
    return paramString.matches(stringBuilder.toString());
  }
  
  private Gson newGson() {
    RuntimeTypeAdapterFactory runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory.of(ConfigurationType.class, "flavor").registerSubtype(BuiltInConfigurationType.class, ConfigurationType.DeviceFlavor.BUILT_IN.toString()).registerSubtype(I2cDeviceConfigurationType.class, ConfigurationType.DeviceFlavor.I2C.toString()).registerSubtype(MotorConfigurationType.class, ConfigurationType.DeviceFlavor.MOTOR.toString()).registerSubtype(ServoConfigurationType.class, ConfigurationType.DeviceFlavor.SERVO.toString()).registerSubtype(AnalogSensorConfigurationType.class, ConfigurationType.DeviceFlavor.ANALOG_SENSOR.toString()).registerSubtype(DigitalIoDeviceConfigurationType.class, ConfigurationType.DeviceFlavor.DIGITAL_IO.toString());
    return (new GsonBuilder()).excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(BuiltInConfigurationType.class, new BuiltInConfigurationTypeJsonAdapter()).registerTypeAdapterFactory((TypeAdapterFactory)runtimeTypeAdapterFactory).create();
  }
  
  private void processMotorSupportAnnotations(Class<?> paramClass, MotorConfigurationType paramMotorConfigurationType) {
    paramMotorConfigurationType.processAnnotation(findAnnotation(paramClass, ModernRoboticsMotorControllerParams.class));
    paramMotorConfigurationType.processAnnotation(findAnnotation(paramClass, DistributorInfo.class));
    processNewOldAnnotations(paramMotorConfigurationType, paramClass, ExpansionHubPIDFVelocityParams.class, ExpansionHubMotorControllerVelocityParams.class);
    processNewOldAnnotations(paramMotorConfigurationType, paramClass, ExpansionHubPIDFPositionParams.class, ExpansionHubMotorControllerPositionParams.class);
  }
  
  private void reportConfigurationError(String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    RobotLog.ee("UserDeviceTypeManager", String.format("configuration error: %s", new Object[] { paramString }));
    RobotLog.setGlobalErrorMsg(paramString);
  }
  
  private String serializeUserDeviceTypes() {
    return this.gson.toJson(this.mapTagToUserType.values());
  }
  
  public ConfigurationType configurationTypeFromTag(String paramString) {
    BuiltInConfigurationType builtInConfigurationType = BuiltInConfigurationType.fromXmlTag(paramString);
    ConfigurationType configurationType = builtInConfigurationType;
    if (builtInConfigurationType == BuiltInConfigurationType.UNKNOWN) {
      ConfigurationType configurationType1 = (ConfigurationType)this.mapTagToUserType.get(paramString);
      configurationType = configurationType1;
      if (configurationType1 == null)
        configurationType = BuiltInConfigurationType.UNKNOWN; 
    } 
    return configurationType;
  }
  
  public void deserializeUserDeviceTypes(String paramString) {
    clearUserTypes();
    for (ConfigurationType configurationType : (ConfigurationType[])this.gson.fromJson(paramString, ConfigurationType[].class)) {
      if (!configurationType.isDeviceFlavor(ConfigurationType.DeviceFlavor.BUILT_IN))
        add((UserConfigurationType)configurationType); 
    } 
    if (DEBUG)
      for (Map.Entry<String, UserConfigurationType> entry : this.mapTagToUserType.entrySet()) {
        RobotLog.vv("UserDeviceTypeManager", "deserialized: xmltag=%s name=%s class=%s", new Object[] { ((UserConfigurationType)entry.getValue()).getXmlTag(), ((UserConfigurationType)entry.getValue()).getName(), ((UserConfigurationType)entry.getValue()).getClass().getSimpleName() });
      }  
  }
  
  public void filterAllClassesComplete() {}
  
  public void filterAllClassesStart() {
    clearUserTypes();
  }
  
  public void filterClass(Class paramClass) {
    StringBuilder stringBuilder;
    if (addMotorTypeFromDeprecatedAnnotation(paramClass))
      return; 
    if (addI2cTypeFromDeprecatedAnnotation(paramClass))
      return; 
    Annotation annotation = getTypeAnnotation(paramClass);
    if (annotation == null)
      return; 
    DeviceProperties deviceProperties = (DeviceProperties)paramClass.getAnnotation(DeviceProperties.class);
    if (deviceProperties == null) {
      stringBuilder = new StringBuilder();
      stringBuilder.append("Class ");
      stringBuilder.append(paramClass.getSimpleName());
      stringBuilder.append(" annotated with ");
      stringBuilder.append(annotation);
      stringBuilder.append(" is missing @DeviceProperties annotation.");
      reportConfigurationError(stringBuilder.toString(), new Object[0]);
      return;
    } 
    UserConfigurationType userConfigurationType = createAppropriateConfigurationType(annotation, (DeviceProperties)stringBuilder, paramClass);
    userConfigurationType.processAnnotation((DeviceProperties)stringBuilder);
    userConfigurationType.finishedAnnotations(paramClass);
    if (userConfigurationType instanceof InstantiableUserConfigurationType) {
      InstantiableUserConfigurationType instantiableUserConfigurationType = (InstantiableUserConfigurationType)userConfigurationType;
      if (instantiableUserConfigurationType.classMustBeInstantiable()) {
        if (checkInstantiableTypeConstraints(instantiableUserConfigurationType)) {
          add(userConfigurationType);
          return;
        } 
        return;
      } 
    } 
    if (checkAnnotationParameterConstraints(userConfigurationType))
      add(userConfigurationType); 
  }
  
  public void filterOnBotJavaClass(Class paramClass) {
    filterClass(paramClass);
  }
  
  public void filterOnBotJavaClassesComplete() {
    filterAllClassesComplete();
  }
  
  public void filterOnBotJavaClassesStart() {
    clearOnBotJavaTypes();
  }
  
  public List<ConfigurationType> getApplicableConfigTypes(ConfigurationType.DeviceFlavor paramDeviceFlavor, ControlSystem paramControlSystem) {
    return getApplicableConfigTypes(paramDeviceFlavor, paramControlSystem, 0);
  }
  
  public List<ConfigurationType> getApplicableConfigTypes(ConfigurationType.DeviceFlavor paramDeviceFlavor, ControlSystem paramControlSystem, int paramInt) {
    LinkedList<UserConfigurationType> linkedList = new LinkedList();
    for (UserConfigurationType userConfigurationType : this.mapTagToUserType.values()) {
      if (linkedList.contains(userConfigurationType) || userConfigurationType.getDeviceFlavor() != paramDeviceFlavor || (paramControlSystem != null && !userConfigurationType.isCompatibleWith(paramControlSystem)) || (userConfigurationType == I2cDeviceConfigurationType.getLynxEmbeddedIMUType() && paramControlSystem != null && (paramControlSystem != ControlSystem.REV_HUB || paramInt != 0)))
        continue; 
      linkedList.add(userConfigurationType);
    } 
    linkedList.addAll(getApplicableBuiltInTypes(paramDeviceFlavor, paramControlSystem));
    Collections.sort(linkedList, this.simpleConfigTypeComparator);
    linkedList.addAll(getDeprecatedConfigTypes(paramDeviceFlavor, paramControlSystem));
    linkedList.addFirst(BuiltInConfigurationType.NOTHING);
    return (List)linkedList;
  }
  
  public Gson getGson() {
    return this.gson;
  }
  
  public ServoConfigurationType getStandardServoType() {
    return (ServoConfigurationType)configurationTypeFromTag(standardServoTypeXmlTag);
  }
  
  public MotorConfigurationType getUnspecifiedMotorType() {
    return (MotorConfigurationType)configurationTypeFromTag(unspecifiedMotorTypeXmlTag);
  }
  
  protected <A extends Annotation> boolean processAnnotationIfPresent(MotorConfigurationType paramMotorConfigurationType, Class<?> paramClass, Class<A> paramClass1) {
    paramClass = paramClass.getAnnotation((Class)paramClass1);
    if (paramClass != null) {
      paramMotorConfigurationType.processAnnotation(paramClass);
      return true;
    } 
    return false;
  }
  
  protected <NewType extends Annotation, OldType extends Annotation> void processNewOldAnnotations(final MotorConfigurationType motorConfigurationType, final Class<?> clazz, final Class<NewType> newType, final Class<OldType> oldType) {
    if (!ClassUtil.searchInheritance(clazz, new Predicate<Class<?>>() {
          public boolean test(Class<?> param1Class) {
            return ConfigurationTypeManager.this.processAnnotationIfPresent(motorConfigurationType, clazz, newType);
          }
        }))
      ClassUtil.searchInheritance(clazz, new Predicate<Class<?>>() {
            public boolean test(Class<?> param1Class) {
              return ConfigurationTypeManager.this.processAnnotationIfPresent(motorConfigurationType, clazz, oldType);
            }
          }); 
  }
  
  public void sendUserDeviceTypes() {
    String str = serializeUserDeviceTypes();
    NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_NOTIFY_USER_DEVICE_LIST", str));
  }
  
  public UserConfigurationType userTypeFromClass(ConfigurationType.DeviceFlavor paramDeviceFlavor, Class<?> paramClass) {
    String str;
    DeviceProperties deviceProperties1 = paramClass.<DeviceProperties>getAnnotation(DeviceProperties.class);
    if (deviceProperties1 != null) {
      String str1 = getXmlTag(deviceProperties1);
    } else {
      deviceProperties1 = null;
    } 
    DeviceProperties deviceProperties2 = deviceProperties1;
    if (deviceProperties1 == null) {
      int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$configuration$ConfigurationType$DeviceFlavor[paramDeviceFlavor.ordinal()];
      if (i != 1) {
        if (i != 2) {
          deviceProperties2 = deviceProperties1;
        } else {
          MotorType motorType = paramClass.<MotorType>getAnnotation(MotorType.class);
          deviceProperties2 = deviceProperties1;
          if (motorType != null)
            str = getXmlTag(motorType); 
        } 
      } else {
        I2cSensor i2cSensor = paramClass.<I2cSensor>getAnnotation(I2cSensor.class);
        deviceProperties2 = deviceProperties1;
        if (i2cSensor != null)
          str = getXmlTag(i2cSensor); 
      } 
    } 
    return (str == null) ? null : (UserConfigurationType)configurationTypeFromTag(str);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ConfigurationTypeManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */