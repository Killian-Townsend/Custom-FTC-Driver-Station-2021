package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConstructorPrototype;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class DigitalIoDeviceConfigurationType extends InstantiableUserConfigurationType {
  private static final ConstructorPrototype ctorDigitalDevice = new ConstructorPrototype(new Class[] { DigitalChannelController.class, int.class });
  
  public DigitalIoDeviceConfigurationType() {
    super(ConfigurationType.DeviceFlavor.DIGITAL_IO);
  }
  
  public DigitalIoDeviceConfigurationType(Class<? extends HardwareDevice> paramClass, String paramString) {
    super(paramClass, ConfigurationType.DeviceFlavor.DIGITAL_IO, paramString, new ConstructorPrototype[] { ctorDigitalDevice });
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public HardwareDevice createInstance(DigitalChannelController paramDigitalChannelController, int paramInt) {
    try {
      Constructor<HardwareDevice> constructor = findMatch(ctorDigitalDevice);
      if (constructor != null)
        return constructor.newInstance(new Object[] { paramDigitalChannelController, Integer.valueOf(paramInt) }); 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("internal error: unable to locate constructor for user device type ");
      stringBuilder.append(getName());
      throw new RuntimeException(stringBuilder.toString());
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InstantiationException instantiationException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    handleConstructorExceptions(invocationTargetException);
    return null;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\DigitalIoDeviceConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */