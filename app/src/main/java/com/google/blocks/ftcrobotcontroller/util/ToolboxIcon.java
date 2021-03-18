package com.google.blocks.ftcrobotcontroller.util;

public enum ToolboxIcon {
  ACCELERATION_SENSOR("AccelerationSensor-icon"),
  ANALOG_INPUT("AnalogInput-icon"),
  ANALOG_OUTPUT("AnalogOutput-icon"),
  COLOR_SENSOR("AnalogOutput-icon"),
  COMPASS_SENSOR("AnalogOutput-icon"),
  CR_SERVO("CRServo-icon"),
  DC_MOTOR("CRServo-icon"),
  DC_MOTOR_CONTROLLER("CRServo-icon"),
  DEVICE_INTERFACE_MODULE("CRServo-icon"),
  DIGITAL_CHANNEL("CRServo-icon"),
  ELAPSED_TIME("CRServo-icon"),
  GAMEPAD("CRServo-icon"),
  GYRO_SENSOR("CRServo-icon"),
  I2C_DEVICE("CRServo-icon"),
  I2C_DEVICER_EADER("CRServo-icon"),
  I2C_DEVICE_SYNCH("CRServo-icon"),
  IR_SEEKER_SENSOR("CRServo-icon"),
  LED("CRServo-icon"),
  LEGACY_MODULE("CRServo-icon"),
  LIGHT_SENSOR("CRServo-icon"),
  LINEAR_OPMODE("CRServo-icon"),
  OPTICAL_DISTANCE_SENSOR("CRServo-icon"),
  OP_MODE("CRServo-icon"),
  PWM_OUTPUT("CRServo-icon"),
  ROBOT_CONTROLLER("CRServo-icon"),
  SERVO("CRServo-icon"),
  SERVO_CONTROLLER("CRServo-icon"),
  TOUCH_SENSOR("CRServo-icon"),
  TOUCH_SENSOR_MULTIPLEXER("CRServo-icon"),
  ULTRASONIC_SENSOR("CRServo-icon"),
  VOLTAGE_SENSOR("CRServo-icon");
  
  public final String cssClass;
  
  static {
    COLOR_SENSOR = new ToolboxIcon("COLOR_SENSOR", 4, "ColorSensor-icon");
    COMPASS_SENSOR = new ToolboxIcon("COMPASS_SENSOR", 5, "CompassSensor-icon");
    DC_MOTOR = new ToolboxIcon("DC_MOTOR", 6, "DcMotor-icon");
    DC_MOTOR_CONTROLLER = new ToolboxIcon("DC_MOTOR_CONTROLLER", 7, "DcMotorController-icon");
    DEVICE_INTERFACE_MODULE = new ToolboxIcon("DEVICE_INTERFACE_MODULE", 8, "DeviceInterfaceModule-icon");
    DIGITAL_CHANNEL = new ToolboxIcon("DIGITAL_CHANNEL", 9, "DigitalChannel-icon");
    ELAPSED_TIME = new ToolboxIcon("ELAPSED_TIME", 10, "ElapsedTime-icon");
    GAMEPAD = new ToolboxIcon("GAMEPAD", 11, "Gamepad-icon");
    GYRO_SENSOR = new ToolboxIcon("GYRO_SENSOR", 12, "GyroSensor-icon");
    I2C_DEVICE = new ToolboxIcon("I2C_DEVICE", 13, "I2cDevice-icon");
    I2C_DEVICER_EADER = new ToolboxIcon("I2C_DEVICER_EADER", 14, "I2cDeviceReader-icon");
    I2C_DEVICE_SYNCH = new ToolboxIcon("I2C_DEVICE_SYNCH", 15, "I2cDeviceSynch-icon");
    IR_SEEKER_SENSOR = new ToolboxIcon("IR_SEEKER_SENSOR", 16, "IrSeekerSensor-icon");
    LED = new ToolboxIcon("LED", 17, "LED-icon");
    LEGACY_MODULE = new ToolboxIcon("LEGACY_MODULE", 18, "LegacyModule-icon");
    LIGHT_SENSOR = new ToolboxIcon("LIGHT_SENSOR", 19, "LightSensor-icon");
    LINEAR_OPMODE = new ToolboxIcon("LINEAR_OPMODE", 20, "LinearOpMode-icon");
    OP_MODE = new ToolboxIcon("OP_MODE", 21, "OpMode-icon");
    OPTICAL_DISTANCE_SENSOR = new ToolboxIcon("OPTICAL_DISTANCE_SENSOR", 22, "OpticalDistanceSensor-icon");
    PWM_OUTPUT = new ToolboxIcon("PWM_OUTPUT", 23, "PwmOutput-icon");
    ROBOT_CONTROLLER = new ToolboxIcon("ROBOT_CONTROLLER", 24, "RobotController-icon");
    SERVO = new ToolboxIcon("SERVO", 25, "Servo-icon");
    SERVO_CONTROLLER = new ToolboxIcon("SERVO_CONTROLLER", 26, "ServoController-icon");
    TOUCH_SENSOR = new ToolboxIcon("TOUCH_SENSOR", 27, "TouchSensor-icon");
    TOUCH_SENSOR_MULTIPLEXER = new ToolboxIcon("TOUCH_SENSOR_MULTIPLEXER", 28, "TouchSensorMultiplexer-icon");
    ULTRASONIC_SENSOR = new ToolboxIcon("ULTRASONIC_SENSOR", 29, "UltrasonicSensor-icon");
    ToolboxIcon toolboxIcon = new ToolboxIcon("VOLTAGE_SENSOR", 30, "VoltageSensor-icon");
    VOLTAGE_SENSOR = toolboxIcon;
    $VALUES = new ToolboxIcon[] { 
        ACCELERATION_SENSOR, ANALOG_INPUT, ANALOG_OUTPUT, CR_SERVO, COLOR_SENSOR, COMPASS_SENSOR, DC_MOTOR, DC_MOTOR_CONTROLLER, DEVICE_INTERFACE_MODULE, DIGITAL_CHANNEL, 
        ELAPSED_TIME, GAMEPAD, GYRO_SENSOR, I2C_DEVICE, I2C_DEVICER_EADER, I2C_DEVICE_SYNCH, IR_SEEKER_SENSOR, LED, LEGACY_MODULE, LIGHT_SENSOR, 
        LINEAR_OPMODE, OP_MODE, OPTICAL_DISTANCE_SENSOR, PWM_OUTPUT, ROBOT_CONTROLLER, SERVO, SERVO_CONTROLLER, TOUCH_SENSOR, TOUCH_SENSOR_MULTIPLEXER, ULTRASONIC_SENSOR, 
        toolboxIcon };
  }
  
  ToolboxIcon(String paramString1) {
    this.cssClass = paramString1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontrolle\\util\ToolboxIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */