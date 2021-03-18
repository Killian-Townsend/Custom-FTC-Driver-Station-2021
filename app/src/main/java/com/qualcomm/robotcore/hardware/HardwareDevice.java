package com.qualcomm.robotcore.hardware;

public interface HardwareDevice {
  void close();
  
  String getConnectionInfo();
  
  String getDeviceName();
  
  Manufacturer getManufacturer();
  
  int getVersion();
  
  void resetDeviceConfigurationForOpMode();
  
  public enum Manufacturer {
    AMS, Adafruit, Broadcom, HiTechnic, Lego, Lynx, Matrix, ModernRobotics, Other, STMicroelectronics, Unknown;
    
    static {
      Lego = new Manufacturer("Lego", 2);
      HiTechnic = new Manufacturer("HiTechnic", 3);
      ModernRobotics = new Manufacturer("ModernRobotics", 4);
      Adafruit = new Manufacturer("Adafruit", 5);
      Matrix = new Manufacturer("Matrix", 6);
      Lynx = new Manufacturer("Lynx", 7);
      AMS = new Manufacturer("AMS", 8);
      STMicroelectronics = new Manufacturer("STMicroelectronics", 9);
      Manufacturer manufacturer = new Manufacturer("Broadcom", 10);
      Broadcom = manufacturer;
      $VALUES = new Manufacturer[] { 
          Unknown, Other, Lego, HiTechnic, ModernRobotics, Adafruit, Matrix, Lynx, AMS, STMicroelectronics, 
          manufacturer };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\HardwareDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */