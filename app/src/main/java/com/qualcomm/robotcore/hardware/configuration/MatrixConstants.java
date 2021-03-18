package com.qualcomm.robotcore.hardware.configuration;

public class MatrixConstants {
  public static final int INITIAL_MOTOR_PORT = 1;
  
  public static final int INITIAL_SERVO_PORT = 1;
  
  public static final int NUMBER_OF_MOTORS = 4;
  
  public static final int NUMBER_OF_SERVOS = 4;
  
  public static void validateMotorZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid motor: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateServoChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid servo channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\MatrixConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */