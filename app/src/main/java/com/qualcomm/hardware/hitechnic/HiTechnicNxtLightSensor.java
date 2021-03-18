package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtLightSensor extends LegacyModulePortDeviceImpl implements LightSensor, AnalogSensor {
  public static final byte LED_DIGITAL_LINE_NUMBER = 0;
  
  public static final double MAX_LIGHT_FRACTION = 0.8504398826979472D;
  
  public static final double MIN_LIGHT_FRACTION = 0.11730205278592376D;
  
  protected static final double apiLevelMax = 1.0D;
  
  protected static final double apiLevelMin = 0.0D;
  
  public HiTechnicNxtLightSensor(LegacyModule paramLegacyModule, int paramInt) {
    super(paramLegacyModule, paramInt);
    finishConstruction();
  }
  
  public void close() {}
  
  public void enableLed(boolean paramBoolean) {
    this.module.setDigitalLine(this.physicalPort, 0, paramBoolean);
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.module.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeHTLightSensor);
  }
  
  public double getLightDetected() {
    double d = getRawLightDetectedMax();
    return Range.clip(Range.scale(getRawLightDetected(), 0.11730205278592376D * d, 0.8504398826979472D * d, 0.0D, 1.0D), 0.0D, 1.0D);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public double getRawLightDetected() {
    double d = getRawLightDetectedMax();
    return Range.clip(d - readRawVoltage(), 0.0D, d);
  }
  
  public double getRawLightDetectedMax() {
    return Math.min(5.0D, this.module.getMaxAnalogInputVoltage());
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected void moduleNowArmedOrPretending() {
    this.module.enableAnalogReadMode(this.physicalPort);
  }
  
  public double readRawVoltage() {
    return this.module.readAnalogVoltage(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("NXT Light Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("Light Level: %1.2f", new Object[] { Double.valueOf(getLightDetected()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtLightSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */