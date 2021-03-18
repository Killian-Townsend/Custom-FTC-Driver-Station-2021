package com.qualcomm.ftccommon.configuration;

public enum RequestCode {
  AUTO_CONFIGURE,
  CONFIG_FROM_TEMPLATE,
  EDIT_ANALOG_INPUT,
  EDIT_ANALOG_OUTPUT,
  EDIT_DEVICE_INTERFACE_MODULE,
  EDIT_DIGITAL,
  EDIT_FILE,
  EDIT_I2C_BUS0,
  EDIT_I2C_BUS1,
  EDIT_I2C_BUS2,
  EDIT_I2C_BUS3,
  EDIT_I2C_PORT,
  EDIT_LEGACY_MODULE,
  EDIT_LYNX_MODULE,
  EDIT_LYNX_USB_DEVICE,
  EDIT_MATRIX_CONTROLLER,
  EDIT_MOTOR_CONTROLLER,
  EDIT_MOTOR_LIST,
  EDIT_PWM_PORT,
  EDIT_SERVO_CONTROLLER,
  EDIT_SERVO_LIST,
  EDIT_SWAP_USB_DEVICES,
  EDIT_USB_CAMERA,
  NEW_FILE,
  NOTHING(0);
  
  public final int value;
  
  static {
    EDIT_MOTOR_CONTROLLER = new RequestCode("EDIT_MOTOR_CONTROLLER", 1, 1);
    EDIT_SERVO_CONTROLLER = new RequestCode("EDIT_SERVO_CONTROLLER", 2, 2);
    EDIT_LEGACY_MODULE = new RequestCode("EDIT_LEGACY_MODULE", 3, 3);
    EDIT_DEVICE_INTERFACE_MODULE = new RequestCode("EDIT_DEVICE_INTERFACE_MODULE", 4, 4);
    EDIT_MATRIX_CONTROLLER = new RequestCode("EDIT_MATRIX_CONTROLLER", 5, 5);
    EDIT_PWM_PORT = new RequestCode("EDIT_PWM_PORT", 6, 6);
    EDIT_I2C_PORT = new RequestCode("EDIT_I2C_PORT", 7, 7);
    EDIT_ANALOG_INPUT = new RequestCode("EDIT_ANALOG_INPUT", 8, 8);
    EDIT_DIGITAL = new RequestCode("EDIT_DIGITAL", 9, 9);
    EDIT_ANALOG_OUTPUT = new RequestCode("EDIT_ANALOG_OUTPUT", 10, 10);
    EDIT_LYNX_MODULE = new RequestCode("EDIT_LYNX_MODULE", 11, 11);
    EDIT_LYNX_USB_DEVICE = new RequestCode("EDIT_LYNX_USB_DEVICE", 12, 12);
    EDIT_I2C_BUS0 = new RequestCode("EDIT_I2C_BUS0", 13, 13);
    EDIT_I2C_BUS1 = new RequestCode("EDIT_I2C_BUS1", 14, 14);
    EDIT_I2C_BUS2 = new RequestCode("EDIT_I2C_BUS2", 15, 15);
    EDIT_I2C_BUS3 = new RequestCode("EDIT_I2C_BUS3", 16, 16);
    EDIT_MOTOR_LIST = new RequestCode("EDIT_MOTOR_LIST", 17, 17);
    EDIT_SERVO_LIST = new RequestCode("EDIT_SERVO_LIST", 18, 18);
    EDIT_SWAP_USB_DEVICES = new RequestCode("EDIT_SWAP_USB_DEVICES", 19, 19);
    EDIT_FILE = new RequestCode("EDIT_FILE", 20, 20);
    NEW_FILE = new RequestCode("NEW_FILE", 21, 21);
    AUTO_CONFIGURE = new RequestCode("AUTO_CONFIGURE", 22, 22);
    CONFIG_FROM_TEMPLATE = new RequestCode("CONFIG_FROM_TEMPLATE", 23, 23);
    RequestCode requestCode = new RequestCode("EDIT_USB_CAMERA", 24, 24);
    EDIT_USB_CAMERA = requestCode;
    $VALUES = new RequestCode[] { 
        NOTHING, EDIT_MOTOR_CONTROLLER, EDIT_SERVO_CONTROLLER, EDIT_LEGACY_MODULE, EDIT_DEVICE_INTERFACE_MODULE, EDIT_MATRIX_CONTROLLER, EDIT_PWM_PORT, EDIT_I2C_PORT, EDIT_ANALOG_INPUT, EDIT_DIGITAL, 
        EDIT_ANALOG_OUTPUT, EDIT_LYNX_MODULE, EDIT_LYNX_USB_DEVICE, EDIT_I2C_BUS0, EDIT_I2C_BUS1, EDIT_I2C_BUS2, EDIT_I2C_BUS3, EDIT_MOTOR_LIST, EDIT_SERVO_LIST, EDIT_SWAP_USB_DEVICES, 
        EDIT_FILE, NEW_FILE, AUTO_CONFIGURE, CONFIG_FROM_TEMPLATE, requestCode };
  }
  
  RequestCode(int paramInt1) {
    this.value = paramInt1;
  }
  
  public static RequestCode fromString(String paramString) {
    for (RequestCode requestCode : values()) {
      if (requestCode.toString().equals(paramString))
        return requestCode; 
    } 
    return NOTHING;
  }
  
  public static RequestCode fromValue(int paramInt) {
    for (RequestCode requestCode : values()) {
      if (requestCode.value == paramInt)
        return requestCode; 
    } 
    return NOTHING;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\ftccommon\configuration\RequestCode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */