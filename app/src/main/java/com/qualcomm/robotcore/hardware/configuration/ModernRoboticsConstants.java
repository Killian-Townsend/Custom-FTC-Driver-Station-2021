package com.qualcomm.robotcore.hardware.configuration;

public class ModernRoboticsConstants {
  public static final int INITIAL_MOTOR_PORT = 1;
  
  public static final int INITIAL_SERVO_PORT = 1;
  
  public static final int LATENCY_TIMER = 1;
  
  public static final int NUMBER_OF_ANALOG_INPUTS = 8;
  
  public static final int NUMBER_OF_ANALOG_OUTPUTS = 2;
  
  public static final int NUMBER_OF_DIGITAL_IOS = 8;
  
  public static final int NUMBER_OF_I2C_CHANNELS = 6;
  
  public static final int NUMBER_OF_LEGACY_MODULE_PORTS = 6;
  
  public static final int NUMBER_OF_MOTORS = 2;
  
  public static final int NUMBER_OF_PWM_CHANNELS = 2;
  
  public static final int NUMBER_OF_SERVOS = 6;
  
  public static final int USB_BAUD_RATE = 250000;
  
  public static void validateAnalogInputZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 8)
      return; 
    throw new IllegalArgumentException(String.format("invalid analog input: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateDigitalIOZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 8)
      return; 
    throw new IllegalArgumentException(String.format("invalid digital pin: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateI2cChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 6)
      return; 
    throw new IllegalArgumentException(String.format("invalid i2c channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateMotorZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 2)
      return; 
    throw new IllegalArgumentException(String.format("invalid motor: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validatePwmChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 2)
      return; 
    throw new IllegalArgumentException(String.format("invalid pwm channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateServoChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 6)
      return; 
    throw new IllegalArgumentException(String.format("invalid servo channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ModernRoboticsConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */