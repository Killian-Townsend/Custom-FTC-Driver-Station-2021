package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConstructorPrototype;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class AnalogSensorConfigurationType extends InstantiableUserConfigurationType {
  private static final ConstructorPrototype ctorAnalogSensor = new ConstructorPrototype(new Class[] { AnalogInputController.class, int.class });
  
  public AnalogSensorConfigurationType() {
    super(ConfigurationType.DeviceFlavor.ANALOG_SENSOR);
  }
  
  public AnalogSensorConfigurationType(Class<? extends HardwareDevice> paramClass, String paramString) {
    super(paramClass, ConfigurationType.DeviceFlavor.ANALOG_SENSOR, paramString, new ConstructorPrototype[] { ctorAnalogSensor });
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public HardwareDevice createInstance(AnalogInputController paramAnalogInputController, int paramInt) {
    try {
      Constructor<HardwareDevice> constructor = findMatch(ctorAnalogSensor);
      if (constructor != null)
        return constructor.newInstance(new Object[] { paramAnalogInputController, Integer.valueOf(paramInt) }); 
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
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\AnalogSensorConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */