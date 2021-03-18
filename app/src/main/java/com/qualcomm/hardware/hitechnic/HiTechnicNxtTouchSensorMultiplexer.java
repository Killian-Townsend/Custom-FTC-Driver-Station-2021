package com.qualcomm.hardware.hitechnic;

import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
import com.qualcomm.robotcore.hardware.TouchSensorMultiplexer;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtTouchSensorMultiplexer extends LegacyModulePortDeviceImpl implements TouchSensorMultiplexer {
  public static final int INVALID = -1;
  
  public static final int[] MASK_MAP = new int[] { -1, 1, 2, 4, 8 };
  
  public static final int MASK_TOUCH_SENSOR_1 = 1;
  
  public static final int MASK_TOUCH_SENSOR_2 = 2;
  
  public static final int MASK_TOUCH_SENSOR_3 = 4;
  
  public static final int MASK_TOUCH_SENSOR_4 = 8;
  
  int NUM_TOUCH_SENSORS = 4;
  
  public HiTechnicNxtTouchSensorMultiplexer(LegacyModule paramLegacyModule, int paramInt) {
    super(paramLegacyModule, paramInt);
    finishConstruction();
  }
  
  private int getAllSwitches() {
    int i = 1023 - TypeConversion.byteArrayToShort(this.module.readAnalogRaw(3), ByteOrder.LITTLE_ENDIAN);
    return (i * 339 / (1023 - i) + 5) / 10;
  }
  
  private void throwIfChannelInvalid(int paramInt) {
    if (paramInt > 0 && paramInt <= this.NUM_TOUCH_SENSORS)
      return; 
    throw new IllegalArgumentException(String.format("Channel %d is invalid; valid channels are 1..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(this.NUM_TOUCH_SENSORS) }));
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
    return AppUtil.getDefContext().getString(R.string.configTypeHTTouchSensorMultiplexer);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public int getSwitches() {
    return getAllSwitches();
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isTouchSensorPressed(int paramInt) {
    throwIfChannelInvalid(paramInt);
    int i = getAllSwitches();
    return ((MASK_MAP[paramInt] & i) > 0);
  }
  
  protected void moduleNowArmedOrPretending() {
    this.module.enableAnalogReadMode(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("NXT Touch Sensor Multiplexer, connected via device %s, port %d", new Object[] { this.module.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtTouchSensorMultiplexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */