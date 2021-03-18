package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.RobotCoreLynxModule;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.ConstructorPrototype;
import com.qualcomm.robotcore.hardware.configuration.I2cSensor;
import com.qualcomm.robotcore.util.ClassUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.firstinspires.ftc.robotcore.external.Func;

public final class I2cDeviceConfigurationType extends InstantiableUserConfigurationType {
  private static final ConstructorPrototype[] allowableConstructorPrototypes;
  
  private static final ConstructorPrototype ctorI2cControllerPort;
  
  private static final ConstructorPrototype ctorI2cDevice;
  
  private static final ConstructorPrototype ctorI2cDeviceSynch;
  
  private static final ConstructorPrototype ctorI2cDeviceSynchSimple = new ConstructorPrototype(new Class[] { I2cDeviceSynchSimple.class });
  
  static {
    ctorI2cDeviceSynch = new ConstructorPrototype(new Class[] { I2cDeviceSynch.class });
    ctorI2cDevice = new ConstructorPrototype(new Class[] { I2cDevice.class });
    ConstructorPrototype constructorPrototype = new ConstructorPrototype(new Class[] { I2cController.class, int.class });
    ctorI2cControllerPort = constructorPrototype;
    allowableConstructorPrototypes = new ConstructorPrototype[] { ctorI2cDeviceSynchSimple, ctorI2cDeviceSynch, ctorI2cDevice, constructorPrototype };
  }
  
  public I2cDeviceConfigurationType() {
    super(ConfigurationType.DeviceFlavor.I2C);
  }
  
  public I2cDeviceConfigurationType(Class<? extends HardwareDevice> paramClass, String paramString) {
    super(paramClass, ConfigurationType.DeviceFlavor.I2C, paramString, allowableConstructorPrototypes);
  }
  
  public static I2cDeviceConfigurationType getLynxEmbeddedIMUType() {
    return (I2cDeviceConfigurationType)ConfigurationTypeManager.getInstance().configurationTypeFromTag("LynxEmbeddedIMU");
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public HardwareDevice createInstance(I2cController paramI2cController, int paramInt) {
    try {
      Constructor<HardwareDevice> constructor2 = findMatch(ctorI2cDeviceSynch);
      Constructor<HardwareDevice> constructor1 = constructor2;
      if (constructor2 == null)
        constructor1 = findMatch(ctorI2cDeviceSynchSimple); 
      if (constructor1 != null)
        return constructor1.newInstance(new Object[] { new I2cDeviceSynchImpl((I2cDevice)new I2cDeviceImpl(paramI2cController, paramInt), true) }); 
      constructor1 = findMatch(ctorI2cDevice);
      if (constructor1 != null)
        return constructor1.newInstance(new Object[] { new I2cDeviceImpl(paramI2cController, paramInt) }); 
      constructor1 = findMatch(ctorI2cControllerPort);
      if (constructor1 != null)
        return constructor1.newInstance(new Object[] { paramI2cController, Integer.valueOf(paramInt) }); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("internal error: unable to locate constructor for user sensor type ");
      stringBuilder.append(getName());
      throw new RuntimeException(stringBuilder.toString());
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    handleConstructorExceptions(invocationTargetException);
    return null;
  }
  
  public HardwareDevice createInstance(RobotCoreLynxModule paramRobotCoreLynxModule, Func<I2cDeviceSynchSimple> paramFunc, Func<I2cDeviceSynch> paramFunc1) {
    try {
      Constructor<HardwareDevice> constructor = findMatch(ctorI2cDeviceSynchSimple);
      if (constructor != null)
        return constructor.newInstance(new Object[] { paramFunc.value() }); 
      constructor = findMatch(ctorI2cDeviceSynch);
      if (constructor != null)
        return constructor.newInstance(new Object[] { paramFunc1.value() }); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("internal error: unable to locate constructor for user sensor type ");
      stringBuilder.append(getName());
      throw new RuntimeException(stringBuilder.toString());
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    handleConstructorExceptions(invocationTargetException);
    return null;
  }
  
  public void processAnnotation(I2cSensor paramI2cSensor) {
    if (paramI2cSensor != null) {
      if (this.name.isEmpty())
        this.name = ClassUtil.decodeStringRes(paramI2cSensor.name().trim()); 
      this.description = ClassUtil.decodeStringRes(paramI2cSensor.description());
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\I2cDeviceConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */