package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtTouchSensor extends LegacyModulePortDeviceImpl implements TouchSensor {
  public HiTechnicNxtTouchSensor(LegacyModule paramLegacyModule, int paramInt) {
    super(paramLegacyModule, paramInt);
    finishConstruction();
  }
  
  public void close() {}
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.module.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeNXTTouchSensor);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lego;
  }
  
  public double getValue() {
    return (TypeConversion.byteArrayToShort(this.module.readAnalogRaw(this.physicalPort), ByteOrder.LITTLE_ENDIAN) > 675.0D) ? 0.0D : 1.0D;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isPressed() {
    return (getValue() > 0.0D);
  }
  
  protected void moduleNowArmedOrPretending() {
    this.module.enableAnalogReadMode(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("NXT Touch Sensor, connected via device %s, port %d", new Object[] { this.module.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("Touch Sensor: %1.2f", new Object[] { Double.valueOf(getValue()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtTouchSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */