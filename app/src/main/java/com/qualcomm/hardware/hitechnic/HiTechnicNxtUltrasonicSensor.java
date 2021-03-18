package com.qualcomm.hardware.hitechnic;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.LegacyModulePortDeviceImpl;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtUltrasonicSensor extends LegacyModulePortDeviceImpl implements UltrasonicSensor, DistanceSensor, I2cController.I2cPortReadyCallback {
  public static final int ADDRESS_DISTANCE = 66;
  
  public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
  
  public static final int MAX_PORT = 5;
  
  public static final int MIN_PORT = 4;
  
  protected static final int cmUltrasonicMax = 255;
  
  byte[] readBuffer;
  
  Lock readLock;
  
  public HiTechnicNxtUltrasonicSensor(ModernRoboticsUsbLegacyModule paramModernRoboticsUsbLegacyModule, int paramInt) {
    super((LegacyModule)paramModernRoboticsUsbLegacyModule, paramInt);
    throwIfPortIsInvalid(paramInt);
    finishConstruction();
  }
  
  private void throwIfPortIsInvalid(int paramInt) {
    if (paramInt >= 4 && paramInt <= 5)
      return; 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Port %d is invalid for ");
    stringBuilder.append(getDeviceName());
    stringBuilder.append("; valid ports are %d or %d");
    throw new IllegalArgumentException(String.format(stringBuilder.toString(), new Object[] { Integer.valueOf(paramInt), Integer.valueOf(4), Integer.valueOf(5) }));
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
    return AppUtil.getDefContext().getString(R.string.configTypeNXTUltrasonicSensor);
  }
  
  public double getDistance(DistanceUnit paramDistanceUnit) {
    int i = TypeConversion.unsignedByteToInt(rawUltrasonic());
    return (i == 255) ? Double.MAX_VALUE : paramDistanceUnit.fromUnit(DistanceUnit.CM, i);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lego;
  }
  
  public double getUltrasonicLevel() {
    return TypeConversion.unsignedByteToDouble(rawUltrasonic());
  }
  
  public int getVersion() {
    return 1;
  }
  
  protected void moduleNowArmedOrPretending() {
    this.readLock = this.module.getI2cReadCacheLock(this.physicalPort);
    this.readBuffer = this.module.getI2cReadCache(this.physicalPort);
    this.module.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 1);
    this.module.enable9v(this.physicalPort, true);
    this.module.setI2cPortActionFlag(this.physicalPort);
    this.module.readI2cCacheFromController(this.physicalPort);
    this.module.registerForI2cPortReadyCallback(this, this.physicalPort);
  }
  
  public void portIsReady(int paramInt) {
    this.module.setI2cPortActionFlag(this.physicalPort);
    this.module.writeI2cCacheToController(this.physicalPort);
    this.module.readI2cCacheFromController(this.physicalPort);
  }
  
  protected byte rawUltrasonic() {
    try {
      this.readLock.lock();
      return this.readBuffer[4];
    } finally {
      this.readLock.unlock();
    } 
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("%s, connected via device %s, port %d", new Object[] { getDeviceName(), this.module.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return String.format("Ultrasonic: %6.1f", new Object[] { Double.valueOf(getUltrasonicLevel()) });
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtUltrasonicSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */