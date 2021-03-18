package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConstructorPrototype;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;

public abstract class InstantiableUserConfigurationType extends UserConfigurationType {
  private Class<? extends HardwareDevice> clazz;
  
  private List<Constructor> constructors;
  
  protected InstantiableUserConfigurationType(ConfigurationType.DeviceFlavor paramDeviceFlavor) {
    super(paramDeviceFlavor);
  }
  
  protected InstantiableUserConfigurationType(Class<? extends HardwareDevice> paramClass, ConfigurationType.DeviceFlavor paramDeviceFlavor, String paramString, ConstructorPrototype[] paramArrayOfConstructorPrototype) {
    super(paramClass, paramDeviceFlavor, paramString);
    this.clazz = paramClass;
    this.constructors = findUsableConstructors(paramArrayOfConstructorPrototype);
  }
  
  private List<Constructor> findUsableConstructors(ConstructorPrototype[] paramArrayOfConstructorPrototype) {
    LinkedList<Constructor> linkedList = new LinkedList();
    for (Constructor constructor : ClassUtil.getDeclaredConstructors(getClazz())) {
      if ((constructor.getModifiers() & 0x1) != 1)
        continue; 
      int j = paramArrayOfConstructorPrototype.length;
      for (int i = 0; i < j; i++) {
        if (paramArrayOfConstructorPrototype[i].matches(constructor)) {
          linkedList.add(constructor);
          break;
        } 
      } 
    } 
    return linkedList;
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public boolean classMustBeInstantiable() {
    return true;
  }
  
  protected final Constructor<HardwareDevice> findMatch(ConstructorPrototype paramConstructorPrototype) {
    for (Constructor<HardwareDevice> constructor : this.constructors) {
      if (paramConstructorPrototype.matches(constructor))
        return constructor; 
    } 
    return null;
  }
  
  public final Class<? extends HardwareDevice> getClazz() {
    return this.clazz;
  }
  
  protected final void handleConstructorExceptions(Exception paramException) {
    RobotLog.v("Creating user sensor %s failed: ", new Object[] { getName() });
    RobotLog.logStackTrace(paramException);
    if (paramException instanceof InvocationTargetException) {
      Throwable throwable = ((InvocationTargetException)paramException).getTargetException();
      if (throwable != null) {
        RobotLog.e("InvocationTargetException caused by: ");
        RobotLog.logStackTrace(throwable);
      } 
      if (!isBuiltIn()) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("Constructor of device type ");
        stringBuilder1.append(getName());
        stringBuilder1.append(" threw an exception. See log.");
        throw new RuntimeException(stringBuilder1.toString());
      } 
    } 
    if (isBuiltIn())
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Internal error while creating device of type ");
    stringBuilder.append(getName());
    stringBuilder.append(". See log.");
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public final boolean hasConstructors() {
    return (this.constructors.size() > 0);
  }
  
  public void processAnnotation(DeviceProperties paramDeviceProperties) {
    super.processAnnotation(paramDeviceProperties);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\InstantiableUserConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */