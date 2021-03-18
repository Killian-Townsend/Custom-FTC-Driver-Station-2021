package com.google.blocks.ftcrobotcontroller.runtime;

import com.google.blocks.ftcrobotcontroller.hardware.HardwareItem;
import com.google.blocks.ftcrobotcontroller.hardware.HardwareType;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

abstract class HardwareAccess<DEVICE_TYPE extends HardwareDevice> extends Access {
  protected final DEVICE_TYPE hardwareDevice;
  
  protected final HardwareItem hardwareItem;
  
  protected HardwareAccess(BlocksOpMode paramBlocksOpMode, HardwareItem paramHardwareItem, HardwareMap paramHardwareMap, Class<DEVICE_TYPE> paramClass) {
    super(paramBlocksOpMode, paramHardwareItem.identifier, paramHardwareItem.visibleName);
    String str;
    this.hardwareItem = paramHardwareItem;
    try {
      HardwareDevice hardwareDevice = (HardwareDevice)paramHardwareMap.get(paramClass, paramHardwareItem.deviceName);
    } catch (Exception exception) {
      try {
        paramHardwareMap.get(paramHardwareItem.deviceName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The name \"");
        stringBuilder.append(paramHardwareItem.deviceName);
        stringBuilder.append("\" is present in the active configuration, but it does not correspond to a ");
        stringBuilder.append(paramClass.getSimpleName());
        stringBuilder.append(".");
        str = stringBuilder.toString();
      } catch (Exception exception1) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The name \"");
        stringBuilder.append(paramHardwareItem.deviceName);
        stringBuilder.append("\" is not present in the active configuration.");
        str = stringBuilder.toString();
      } 
      reportHardwareError(str);
      str = null;
    } 
    this.hardwareDevice = (DEVICE_TYPE)str;
  }
  
  static HardwareAccess newHardwareAccess(BlocksOpMode paramBlocksOpMode, HardwareType paramHardwareType, HardwareMap paramHardwareMap, HardwareItem paramHardwareItem) {
    StringBuilder stringBuilder;
    switch (paramHardwareType) {
      default:
        stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown hardware type ");
        stringBuilder.append(paramHardwareType);
        throw new IllegalArgumentException(stringBuilder.toString());
      case null:
        return null;
      case null:
        return new VoltageSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new UltrasonicSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new TouchSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new ServoControllerAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new ServoAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new RevBlinkinLedDriverAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new OpticalDistanceSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new MrI2cRangeSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new MrI2cCompassSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return null;
      case null:
        return new LightSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new LedAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new IrSeekerSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new GyroSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new DistanceSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new DigitalChannelAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new DcMotorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new CRServoAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new CompassSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new ColorSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new ColorRangeSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new BNO055IMUAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new AnalogOutputAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        return new AnalogInputAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
      case null:
        break;
    } 
    return new AccelerationSensorAccess((BlocksOpMode)stringBuilder, paramHardwareItem, paramHardwareMap);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\google\blocks\ftcrobotcontroller\runtime\HardwareAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */