package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.bosch.BNO055IMUImpl;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/lynx_embedded_imu_description", name = "@string/lynx_embedded_imu_name", xmlTag = "LynxEmbeddedIMU")
@I2cDeviceType
public class LynxEmbeddedIMU extends BNO055IMUImpl {
  public LynxEmbeddedIMU(I2cDeviceSynch paramI2cDeviceSynch) {
    super(paramI2cDeviceSynch);
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.lynx_embedded_imu_name);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxEmbeddedIMU.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */