package com.qualcomm.robotcore.hardware.configuration;

import android.os.Build;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.system.SystemProperties;

public class LynxConstants {
  public static final int CH_EMBEDDED_MODULE_ADDRESS = 173;
  
  public static final int DEFAULT_PARENT_MODULE_ADDRESS = 1;
  
  public static final int DEFAULT_TARGET_POSITION_TOLERANCE = 5;
  
  public static final int DRAGONBOARD_CH_VERSION = 0;
  
  private static final String DRAGONBOARD_MODEL = "FIRST Control Hub";
  
  public static final int EMBEDDED_IMU_BUS = 0;
  
  public static final String EMBEDDED_IMU_XML_TAG = "LynxEmbeddedIMU";
  
  private static final String EMPTY_STRING = "";
  
  public static final int INDICATOR_LED_BOOT = 4;
  
  public static final int INDICATOR_LED_INVITE_DIALOG_ACTIVE = 2;
  
  public static final int INDICATOR_LED_ROBOT_CONTROLLER_ALIVE = 1;
  
  public static final int INITIAL_MOTOR_PORT = 0;
  
  public static final int INITIAL_SERVO_PORT = 0;
  
  public static final int LATENCY_TIMER = 1;
  
  public static final int MAX_MODULES_DISCOVER = 254;
  
  public static final int MAX_NUMBER_OF_MODULES = 254;
  
  public static final int MAX_UNRESERVED_MODULE_ADDRESS = 10;
  
  public static final int NUMBER_OF_ANALOG_INPUTS = 4;
  
  public static final int NUMBER_OF_DIGITAL_IOS = 8;
  
  public static final int NUMBER_OF_I2C_BUSSES = 4;
  
  public static final int NUMBER_OF_MOTORS = 4;
  
  public static final int NUMBER_OF_PWM_CHANNELS = 4;
  
  public static final int NUMBER_OF_SERVO_CHANNELS = 6;
  
  private static final int ORIGINAL_CH_OS_VERSIONNUM = 1;
  
  public static final int SERIAL_MODULE_BAUD_RATE = 460800;
  
  public static final SerialNumber SERIAL_NUMBER_EMBEDDED = SerialNumber.createEmbedded();
  
  public static final String TAG = "LynxConstants";
  
  public static final int USB_BAUD_RATE = 460800;
  
  public static boolean autorunRobotController() {
    return SystemProperties.getBoolean("persist.ftcandroid.autorunrc", false);
  }
  
  public static String getControlHubOsVersion() {
    String str2 = SystemProperties.get("ro.controlhub.os.version", "");
    String str1 = str2;
    if ("".equals(str2))
      str1 = null; 
    return str1;
  }
  
  public static int getControlHubOsVersionNum() {
    return SystemProperties.getInt("ro.controlhub.os.versionnum", 1);
  }
  
  public static int getControlHubVersion() {
    int j = SystemProperties.getInt("ro.ftcandroid.controlhubversion", -1);
    int i = j;
    if (j == -1) {
      i = j;
      if (Build.MODEL.equalsIgnoreCase("FIRST Control Hub"))
        i = 0; 
    } 
    return i;
  }
  
  public static boolean isEmbeddedSerialNumber(SerialNumber paramSerialNumber) {
    return SERIAL_NUMBER_EMBEDDED.equals(paramSerialNumber);
  }
  
  public static boolean isRevControlHub() {
    return SystemProperties.getBoolean("persist.ftcandroid.serialasusb", false);
  }
  
  public static boolean useIndicatorLEDS() {
    return SystemProperties.getBoolean("persist.ftcandroid.rcuseleds", false);
  }
  
  public static void validateAnalogInputZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid analog input: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateDigitalIOZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 8)
      return; 
    throw new IllegalArgumentException(String.format("invalid digital pin: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateI2cBusZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid i2c bus: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateMotorZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid motor: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validatePwmChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 4)
      return; 
    throw new IllegalArgumentException(String.format("invalid pwm channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
  
  public static void validateServoChannelZ(int paramInt) {
    if (paramInt >= 0 && paramInt < 6)
      return; 
    throw new IllegalArgumentException(String.format("invalid servo channel: %d", new Object[] { Integer.valueOf(paramInt) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\LynxConstants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */