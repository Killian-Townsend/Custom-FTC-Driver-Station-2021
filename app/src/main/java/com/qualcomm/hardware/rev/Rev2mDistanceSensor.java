package com.qualcomm.hardware.rev;

import com.qualcomm.hardware.stmicroelectronics.VL53L0X;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/rev_laser_sensor_name", name = "@string/rev_laser_sensor_name", xmlTag = "REV_VL53L0X_RANGE_SENSOR")
@I2cDeviceType
public class Rev2mDistanceSensor extends VL53L0X {
  public Rev2mDistanceSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super(paramI2cDeviceSynch);
  }
  
  public String getDeviceName() {
    return "REV 2M ToF Distance Sensor";
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\rev\Rev2mDistanceSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */