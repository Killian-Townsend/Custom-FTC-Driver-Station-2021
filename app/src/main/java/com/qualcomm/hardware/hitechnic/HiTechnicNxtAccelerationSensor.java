package com.qualcomm.hardware.hitechnic;

import com.qualcomm.hardware.R;
import com.qualcomm.robotcore.hardware.AccelerationSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class HiTechnicNxtAccelerationSensor extends I2cControllerPortDeviceImpl implements AccelerationSensor, I2cController.I2cPortReadyCallback {
  public static final int ACCEL_LENGTH = 6;
  
  public static final int ADDRESS_ACCEL_START = 66;
  
  private static final double HIGH_BYTE_SCALING_VALUE = 4.0D;
  
  public static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
  
  private static final double ONE_G = 200.0D;
  
  private static final int X_HIGH_BYTE = 4;
  
  private static final int X_LOW_BYTE = 7;
  
  private static final int Y_HIGH_BYTE = 5;
  
  private static final int Y_LOW_BYTE = 8;
  
  private static final int Z_HIGH_BYTE = 6;
  
  private static final int Z_LOW_BYTE = 9;
  
  private byte[] readBuffer;
  
  private Lock readBufferLock;
  
  public HiTechnicNxtAccelerationSensor(I2cController paramI2cController, int paramInt) {
    super(paramI2cController, paramInt);
    finishConstruction();
  }
  
  private double rawToG(double paramDouble1, double paramDouble2) {
    return (paramDouble1 * 4.0D + paramDouble2) / 200.0D;
  }
  
  public void close() {}
  
  protected void controllerNowArmedOrPretending() {
    this.controller.enableI2cReadMode(this.physicalPort, I2C_ADDRESS, 66, 6);
    this.readBuffer = this.controller.getI2cReadCache(this.physicalPort);
    this.readBufferLock = this.controller.getI2cReadCacheLock(this.physicalPort);
    this.controller.registerForI2cPortReadyCallback(this, this.physicalPort);
  }
  
  public Acceleration getAcceleration() {
    try {
      this.readBufferLock.lock();
      return Acceleration.fromGravity(rawToG(this.readBuffer[4], this.readBuffer[7]), rawToG(this.readBuffer[5], this.readBuffer[8]), rawToG(this.readBuffer[6], this.readBuffer[9]), System.nanoTime());
    } finally {
      this.readBufferLock.unlock();
    } 
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeHTAccelerometer);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public void portIsReady(int paramInt) {
    this.controller.setI2cPortActionFlag(this.physicalPort);
    this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
    this.controller.readI2cCacheFromController(this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public String status() {
    return String.format("NXT Acceleration Sensor, connected via device %s, port %d", new Object[] { this.controller.getSerialNumber(), Integer.valueOf(this.physicalPort) });
  }
  
  public String toString() {
    return getAcceleration().toString();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtAccelerationSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */