package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensorImpl;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.util.Range;
import java.nio.ByteOrder;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class LynxI2cColorRangeSensor extends AMSColorSensorImpl implements DistanceSensor, OpticalDistanceSensor, ColorRangeSensor {
  protected static final double apiLevelMax = 1.0D;
  
  protected static final double apiLevelMin = 0.0D;
  
  public double aParam = 186.347D;
  
  public double bParam = 30403.5D;
  
  public double cParam = 0.576649D;
  
  public LynxI2cColorRangeSensor(I2cDeviceSynchSimple paramI2cDeviceSynchSimple) {
    super(AMSColorSensor.Parameters.createForTMD37821(), paramI2cDeviceSynchSimple, true);
  }
  
  protected double cmFromOptical(int paramInt) {
    double d4 = this.aParam;
    double d1 = -d4;
    double d2 = this.cParam;
    double d3 = paramInt;
    d4 = -d4;
    double d5 = this.bParam;
    return (d1 * d2 + d2 * d3 - Math.sqrt(d4 * d5 + d5 * d3)) / (this.aParam - d3);
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeLynxColorSensor);
  }
  
  public double getDistance(DistanceUnit paramDistanceUnit) {
    double d = cmFromOptical(rawOptical());
    return paramDistanceUnit.fromUnit(DistanceUnit.CM, d);
  }
  
  public double getLightDetected() {
    return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  public double getRawLightDetected() {
    return rawOptical();
  }
  
  public double getRawLightDetectedMax() {
    return ((AMSColorSensor.Parameters)this.parameters).proximitySaturation;
  }
  
  public int rawOptical() {
    return readUnsignedShort(AMSColorSensor.Register.PDATA, ByteOrder.LITTLE_ENDIAN);
  }
  
  public String status() {
    return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxI2cColorRangeSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */