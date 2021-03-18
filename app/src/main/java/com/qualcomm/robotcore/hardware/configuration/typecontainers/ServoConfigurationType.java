package com.qualcomm.robotcore.hardware.configuration.typecontainers;

import com.google.gson.annotations.Expose;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationType;
import com.qualcomm.robotcore.hardware.configuration.ConfigurationTypeManager;
import com.qualcomm.robotcore.hardware.configuration.ConstructorPrototype;
import com.qualcomm.robotcore.hardware.configuration.ServoFlavor;
import com.qualcomm.robotcore.hardware.configuration.annotations.ServoType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class ServoConfigurationType extends InstantiableUserConfigurationType {
  private static final ConstructorPrototype ctorServo = new ConstructorPrototype(new Class[] { ServoController.class, int.class });
  
  private static final ConstructorPrototype ctorServoEx = new ConstructorPrototype(new Class[] { ServoControllerEx.class, int.class });
  
  @Expose
  private ServoFlavor servoFlavor;
  
  @Expose
  private double usFrame;
  
  @Expose
  private double usPulseLower;
  
  @Expose
  private double usPulseUpper;
  
  public ServoConfigurationType() {
    super(ConfigurationType.DeviceFlavor.SERVO);
  }
  
  public ServoConfigurationType(Class<? extends HardwareDevice> paramClass, String paramString) {
    super(paramClass, ConfigurationType.DeviceFlavor.SERVO, paramString, new ConstructorPrototype[] { ctorServo, ctorServoEx });
  }
  
  public static ServoConfigurationType getStandardServoType() {
    return ConfigurationTypeManager.getInstance().getStandardServoType();
  }
  
  private Object writeReplace() {
    return new UserConfigurationType.SerializationProxy(this);
  }
  
  public boolean classMustBeInstantiable() {
    return (this.servoFlavor == ServoFlavor.CUSTOM);
  }
  
  public HardwareDevice createInstanceMr(ServoController paramServoController, int paramInt) {
    if (this.servoFlavor == ServoFlavor.CUSTOM) {
      try {
        Constructor<HardwareDevice> constructor = findMatch(ctorServo);
        if (constructor != null)
          return constructor.newInstance(new Object[] { paramServoController, Integer.valueOf(paramInt) }); 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("internal error: unable to locate constructor for user device type ");
        stringBuilder1.append(getName());
        throw new RuntimeException(stringBuilder1.toString());
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (InvocationTargetException invocationTargetException) {}
      handleConstructorExceptions(invocationTargetException);
      return null;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Can't create instance of noninstantiable servo type ");
    stringBuilder.append(this.name);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public HardwareDevice createInstanceRev(ServoControllerEx paramServoControllerEx, int paramInt) {
    if (this.servoFlavor == ServoFlavor.CUSTOM) {
      try {
        Constructor<HardwareDevice> constructor = findMatch(ctorServoEx);
        if (constructor != null) {
          paramServoControllerEx.setServoType(paramInt, this);
          return constructor.newInstance(new Object[] { paramServoControllerEx, Integer.valueOf(paramInt) });
        } 
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("internal error: unable to locate constructor for user device type ");
        stringBuilder1.append(getName());
        throw new RuntimeException(stringBuilder1.toString());
      } catch (IllegalAccessException illegalAccessException) {
      
      } catch (InstantiationException instantiationException) {
      
      } catch (InvocationTargetException invocationTargetException) {}
      handleConstructorExceptions(invocationTargetException);
      return null;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Can't create instance of noninstantiable servo type ");
    stringBuilder.append(this.name);
    throw new RuntimeException(stringBuilder.toString());
  }
  
  public ServoFlavor getServoFlavor() {
    return this.servoFlavor;
  }
  
  public double getUsFrame() {
    return this.usFrame;
  }
  
  public double getUsPulseLower() {
    return this.usPulseLower;
  }
  
  public double getUsPulseUpper() {
    return this.usPulseUpper;
  }
  
  public void processAnnotation(ServoType paramServoType) {
    if (paramServoType != null) {
      this.servoFlavor = paramServoType.flavor();
      this.usPulseLower = paramServoType.usPulseLower();
      this.usPulseUpper = paramServoType.usPulseUpper();
      this.usFrame = paramServoType.usPulseFrameRate();
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\typecontainers\ServoConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */