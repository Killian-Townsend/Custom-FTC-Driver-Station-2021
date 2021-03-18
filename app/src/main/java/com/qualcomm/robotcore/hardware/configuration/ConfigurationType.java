package com.qualcomm.robotcore.hardware.configuration;

import com.qualcomm.robotcore.hardware.DeviceManager;

public interface ConfigurationType {
  DeviceFlavor getDeviceFlavor();
  
  String getDisplayName(DisplayNameFlavor paramDisplayNameFlavor);
  
  String getXmlTag();
  
  String[] getXmlTagAliases();
  
  boolean isDeprecated();
  
  boolean isDeviceFlavor(DeviceFlavor paramDeviceFlavor);
  
  DeviceManager.UsbDeviceType toUSBDeviceType();
  
  public enum DeviceFlavor {
    ANALOG_OUTPUT, ANALOG_SENSOR, BUILT_IN, DIGITAL_IO, I2C, MOTOR, SERVO;
    
    static {
      DIGITAL_IO = new DeviceFlavor("DIGITAL_IO", 5);
      DeviceFlavor deviceFlavor = new DeviceFlavor("ANALOG_OUTPUT", 6);
      ANALOG_OUTPUT = deviceFlavor;
      $VALUES = new DeviceFlavor[] { BUILT_IN, I2C, MOTOR, ANALOG_SENSOR, SERVO, DIGITAL_IO, deviceFlavor };
    }
  }
  
  public enum DisplayNameFlavor {
    Normal, Legacy;
    
    static {
      DisplayNameFlavor displayNameFlavor = new DisplayNameFlavor("Legacy", 1);
      Legacy = displayNameFlavor;
      $VALUES = new DisplayNameFlavor[] { Normal, displayNameFlavor };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\configuration\ConfigurationType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */