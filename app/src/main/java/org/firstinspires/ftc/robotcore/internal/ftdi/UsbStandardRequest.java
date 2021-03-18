package org.firstinspires.ftc.robotcore.internal.ftdi;

public enum UsbStandardRequest {
  CLEAR_FEATURE,
  GET_CONFIGURATION,
  GET_DESCRIPTOR,
  GET_ENCRYPTION,
  GET_HANDSHAKE,
  GET_INTERFACE,
  GET_SECURITY_DATA,
  GET_STATUS(0),
  LOOPBACK_DATA_READ(0),
  LOOPBACK_DATA_WRITE(0),
  Reserved0(0),
  Reserved1(0),
  SET_ADDRESS(0),
  SET_CONFIGURATION(0),
  SET_CONNECTION(0),
  SET_DESCRIPTOR(0),
  SET_ENCRYPTION(0),
  SET_FEATURE(0),
  SET_HANDSHAKE(0),
  SET_INTERFACE(0),
  SET_INTERFACE_DS(0),
  SET_ISOCH_DELAY(0),
  SET_SECURITY_DATA(0),
  SET_SEL(0),
  SET_WUSB_DATA(0),
  SYNCH_FRAME(0);
  
  final int value;
  
  static {
    CLEAR_FEATURE = new UsbStandardRequest("CLEAR_FEATURE", 1, 1);
    Reserved0 = new UsbStandardRequest("Reserved0", 2, 2);
    SET_FEATURE = new UsbStandardRequest("SET_FEATURE", 3, 3);
    Reserved1 = new UsbStandardRequest("Reserved1", 4, 4);
    SET_ADDRESS = new UsbStandardRequest("SET_ADDRESS", 5, 5);
    GET_DESCRIPTOR = new UsbStandardRequest("GET_DESCRIPTOR", 6, 6);
    SET_DESCRIPTOR = new UsbStandardRequest("SET_DESCRIPTOR", 7, 7);
    GET_CONFIGURATION = new UsbStandardRequest("GET_CONFIGURATION", 8, 8);
    SET_CONFIGURATION = new UsbStandardRequest("SET_CONFIGURATION", 9, 9);
    GET_INTERFACE = new UsbStandardRequest("GET_INTERFACE", 10, 10);
    SET_INTERFACE = new UsbStandardRequest("SET_INTERFACE", 11, 11);
    SYNCH_FRAME = new UsbStandardRequest("SYNCH_FRAME", 12, 12);
    SET_ENCRYPTION = new UsbStandardRequest("SET_ENCRYPTION", 13, 13);
    GET_ENCRYPTION = new UsbStandardRequest("GET_ENCRYPTION", 14, 14);
    SET_HANDSHAKE = new UsbStandardRequest("SET_HANDSHAKE", 15, 15);
    GET_HANDSHAKE = new UsbStandardRequest("GET_HANDSHAKE", 16, 16);
    SET_CONNECTION = new UsbStandardRequest("SET_CONNECTION", 17, 17);
    SET_SECURITY_DATA = new UsbStandardRequest("SET_SECURITY_DATA", 18, 18);
    GET_SECURITY_DATA = new UsbStandardRequest("GET_SECURITY_DATA", 19, 19);
    SET_WUSB_DATA = new UsbStandardRequest("SET_WUSB_DATA", 20, 20);
    LOOPBACK_DATA_WRITE = new UsbStandardRequest("LOOPBACK_DATA_WRITE", 21, 21);
    LOOPBACK_DATA_READ = new UsbStandardRequest("LOOPBACK_DATA_READ", 22, 22);
    SET_INTERFACE_DS = new UsbStandardRequest("SET_INTERFACE_DS", 23, 23);
    SET_SEL = new UsbStandardRequest("SET_SEL", 24, 48);
    UsbStandardRequest usbStandardRequest = new UsbStandardRequest("SET_ISOCH_DELAY", 25, 49);
    SET_ISOCH_DELAY = usbStandardRequest;
    $VALUES = new UsbStandardRequest[] { 
        GET_STATUS, CLEAR_FEATURE, Reserved0, SET_FEATURE, Reserved1, SET_ADDRESS, GET_DESCRIPTOR, SET_DESCRIPTOR, GET_CONFIGURATION, SET_CONFIGURATION, 
        GET_INTERFACE, SET_INTERFACE, SYNCH_FRAME, SET_ENCRYPTION, GET_ENCRYPTION, SET_HANDSHAKE, GET_HANDSHAKE, SET_CONNECTION, SET_SECURITY_DATA, GET_SECURITY_DATA, 
        SET_WUSB_DATA, LOOPBACK_DATA_WRITE, LOOPBACK_DATA_READ, SET_INTERFACE_DS, SET_SEL, usbStandardRequest };
  }
  
  UsbStandardRequest(int paramInt1) {
    this.value = paramInt1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\UsbStandardRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */