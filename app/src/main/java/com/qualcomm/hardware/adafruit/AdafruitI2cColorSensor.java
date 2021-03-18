package com.qualcomm.hardware.adafruit;

import com.qualcomm.hardware.R;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensorImpl;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class AdafruitI2cColorSensor extends AMSColorSensorImpl {
  public AdafruitI2cColorSensor(I2cDeviceSynchSimple paramI2cDeviceSynchSimple) {
    super(AMSColorSensor.Parameters.createForTCS34725(), paramI2cDeviceSynchSimple, true);
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeAdafruitColorSensor);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Adafruit;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\adafruit\AdafruitI2cColorSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */