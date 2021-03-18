package com.qualcomm.hardware.rev;

import com.qualcomm.hardware.broadcom.BroadcomColorSensor;
import com.qualcomm.hardware.broadcom.BroadcomColorSensorImpl;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ControlSystem;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.util.Range;
import java.nio.ByteOrder;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@DeviceProperties(builtIn = true, compatibleControlSystems = {ControlSystem.REV_HUB}, description = "@string/rev_color_sensor_v3_description", name = "@string/rev_color_sensor_v3_name", xmlTag = "RevColorSensorV3")
@I2cDeviceType
public class RevColorSensorV3 extends BroadcomColorSensorImpl implements DistanceSensor, OpticalDistanceSensor, ColorRangeSensor {
  protected static final double apiLevelMax = 1.0D;
  
  protected static final double apiLevelMin = 0.0D;
  
  double aParam = 325.961D;
  
  double binvParam = -0.75934D;
  
  double cParam = 26.98D;
  
  double maxDist = 6.0D;
  
  public RevColorSensorV3(I2cDeviceSynchSimple paramI2cDeviceSynchSimple) {
    super(BroadcomColorSensor.Parameters.createForAPDS9151(), paramI2cDeviceSynchSimple, true);
  }
  
  public String getDeviceName() {
    return "Rev Color Sensor v3";
  }
  
  public double getDistance(DistanceUnit paramDistanceUnit) {
    double d = inFromOptical(rawOptical());
    return paramDistanceUnit.fromUnit(DistanceUnit.INCH, d);
  }
  
  public double getLightDetected() {
    return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Broadcom;
  }
  
  public double getRawLightDetected() {
    return rawOptical();
  }
  
  public double getRawLightDetectedMax() {
    return ((BroadcomColorSensor.Parameters)this.parameters).proximitySaturation;
  }
  
  protected double inFromOptical(int paramInt) {
    double d1 = paramInt;
    double d2 = this.cParam;
    return (d1 <= d2) ? this.maxDist : Math.min(Math.pow((d1 - d2) / this.aParam, this.binvParam), this.maxDist);
  }
  
  public int rawOptical() {
    return readUnsignedShort(BroadcomColorSensor.Register.PS_DATA, ByteOrder.LITTLE_ENDIAN) & 0x7FF;
  }
  
  public String status() {
    return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\rev\RevColorSensorV3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */