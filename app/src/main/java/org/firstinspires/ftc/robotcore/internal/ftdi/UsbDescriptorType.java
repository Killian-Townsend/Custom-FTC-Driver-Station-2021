package org.firstinspires.ftc.robotcore.internal.ftdi;

public enum UsbDescriptorType {
  BOS,
  CONFIGURATION,
  DEBUG,
  DEVICE(1),
  DEVICE_CAPABILITY(1),
  ENDPOINT(1),
  INTERFACE(1),
  INTERFACE_ASSOCIATION(1),
  INTERFACE_POWER(1),
  OTG(1),
  Reserved0(1),
  Reserved1(1),
  STRING(1),
  SUPERSPEEDPLUS_ISOCHRONOUS_ENDPOINT_COMPANION(1),
  SUPERSPEED_USB_ENDPOINT_COMPANION(1);
  
  final int value;
  
  static {
    CONFIGURATION = new UsbDescriptorType("CONFIGURATION", 1, 2);
    STRING = new UsbDescriptorType("STRING", 2, 3);
    INTERFACE = new UsbDescriptorType("INTERFACE", 3, 4);
    ENDPOINT = new UsbDescriptorType("ENDPOINT", 4, 5);
    Reserved0 = new UsbDescriptorType("Reserved0", 5, 6);
    Reserved1 = new UsbDescriptorType("Reserved1", 6, 7);
    INTERFACE_POWER = new UsbDescriptorType("INTERFACE_POWER", 7, 8);
    OTG = new UsbDescriptorType("OTG", 8, 9);
    DEBUG = new UsbDescriptorType("DEBUG", 9, 10);
    INTERFACE_ASSOCIATION = new UsbDescriptorType("INTERFACE_ASSOCIATION", 10, 11);
    BOS = new UsbDescriptorType("BOS", 11, 15);
    DEVICE_CAPABILITY = new UsbDescriptorType("DEVICE_CAPABILITY", 12, 16);
    SUPERSPEED_USB_ENDPOINT_COMPANION = new UsbDescriptorType("SUPERSPEED_USB_ENDPOINT_COMPANION", 13, 48);
    UsbDescriptorType usbDescriptorType = new UsbDescriptorType("SUPERSPEEDPLUS_ISOCHRONOUS_ENDPOINT_COMPANION", 14, 49);
    SUPERSPEEDPLUS_ISOCHRONOUS_ENDPOINT_COMPANION = usbDescriptorType;
    $VALUES = new UsbDescriptorType[] { 
        DEVICE, CONFIGURATION, STRING, INTERFACE, ENDPOINT, Reserved0, Reserved1, INTERFACE_POWER, OTG, DEBUG, 
        INTERFACE_ASSOCIATION, BOS, DEVICE_CAPABILITY, SUPERSPEED_USB_ENDPOINT_COMPANION, usbDescriptorType };
  }
  
  UsbDescriptorType(int paramInt1) {
    this.value = paramInt1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\UsbDescriptorType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */