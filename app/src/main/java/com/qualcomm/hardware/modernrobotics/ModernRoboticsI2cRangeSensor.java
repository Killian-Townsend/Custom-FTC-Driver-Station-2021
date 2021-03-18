package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@DeviceProperties(builtIn = true, description = "@string/mr_range_description", name = "@string/mr_range_name", xmlTag = "ModernRoboticsI2cRangeSensor")
@I2cDeviceType
public class ModernRoboticsI2cRangeSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> implements DistanceSensor, OpticalDistanceSensor, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(40);
  
  protected static final double apiLevelMax = 1.0D;
  
  protected static final double apiLevelMin = 0.0D;
  
  protected static final int cmUltrasonicMax = 255;
  
  public double aParam = 5.11595056535567D;
  
  public double bParam = 457.048400147437D;
  
  public double cParam = -0.8061002068394054D;
  
  public double dParam = 0.004048820370701007D;
  
  public int rawOpticalMinValid = 3;
  
  public ModernRoboticsI2cRangeSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    setOptimalReadWindow();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  protected double cmFromOptical(int paramInt) {
    return (this.dParam + Math.log((-this.aParam + paramInt) / this.bParam)) / this.cParam;
  }
  
  public double cmOptical() {
    int i = rawOptical();
    return (i >= this.rawOpticalMinValid) ? cmFromOptical(i) : Double.MAX_VALUE;
  }
  
  public double cmUltrasonic() {
    return rawUltrasonic();
  }
  
  protected boolean doInitialize() {
    /* monitor enter ThisExpression{ObjectType{com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cRangeSensor}} */
    /* monitor exit ThisExpression{ObjectType{com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cRangeSensor}} */
    return true;
  }
  
  public void enableLed(boolean paramBoolean) {}
  
  public String getDeviceName() {
    return String.format(Locale.getDefault(), "Modern Robotics Range Sensor %s", new Object[] { new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV)) });
  }
  
  public double getDistance(DistanceUnit paramDistanceUnit) {
    double d;
    int i = rawOptical();
    if (i >= this.rawOpticalMinValid) {
      d = cmFromOptical(i);
    } else {
      double d1 = cmUltrasonic();
      d = d1;
      if (d1 == 255.0D)
        return Double.MAX_VALUE; 
    } 
    return paramDistanceUnit.fromUnit(DistanceUnit.CM, d);
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public double getLightDetected() {
    return Range.clip(Range.scale(getRawLightDetected(), 0.0D, getRawLightDetectedMax(), 0.0D, 1.0D), 0.0D, 1.0D);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public double getRawLightDetected() {
    return rawOptical();
  }
  
  public double getRawLightDetectedMax() {
    return 255.0D;
  }
  
  public int rawOptical() {
    return readUnsignedByte(Register.OPTICAL);
  }
  
  public int rawUltrasonic() {
    return readUnsignedByte(Register.ULTRASONIC);
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  protected int readUnsignedByte(Register paramRegister) {
    return TypeConversion.unsignedByteToInt(read8(paramRegister));
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  protected void setOptimalReadWindow() {
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.FIRST.bVal, Register.LAST.bVal - Register.FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
  }
  
  public String status() {
    return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
  }
  
  public void write8(Register paramRegister, byte paramByte) {
    write8(paramRegister, paramByte, I2cWaitControl.NONE);
  }
  
  public void write8(Register paramRegister, byte paramByte, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramByte, paramI2cWaitControl);
  }
  
  public enum Register {
    FIRMWARE_REV,
    FIRST(0),
    LAST(0),
    MANUFACTURE_CODE(0),
    OPTICAL(0),
    SENSOR_ID(0),
    ULTRASONIC(0),
    UNKNOWN(0);
    
    public byte bVal;
    
    static {
      OPTICAL = new Register("OPTICAL", 5, 5);
      LAST = new Register("LAST", 6, OPTICAL.bVal);
      Register register = new Register("UNKNOWN", 7, -1);
      UNKNOWN = register;
      $VALUES = new Register[] { FIRST, FIRMWARE_REV, MANUFACTURE_CODE, SENSOR_ID, ULTRASONIC, OPTICAL, LAST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cRangeSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */