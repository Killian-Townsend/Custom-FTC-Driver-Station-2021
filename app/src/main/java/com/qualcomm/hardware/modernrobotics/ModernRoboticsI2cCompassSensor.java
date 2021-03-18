package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.CompassSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;

@DeviceProperties(builtIn = true, description = "@string/mr_compass_description", name = "@string/mr_compass_name", xmlTag = "ModernRoboticsI2cCompassSensor")
@I2cDeviceType
public class ModernRoboticsI2cCompassSensor extends I2cDeviceSynchDevice<I2cDeviceSynch> implements CompassSensor, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(36);
  
  public ModernRoboticsI2cCompassSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    setOptimalReadWindow();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  public boolean calibrationFailed() {
    return (readCommand() == Command.CALIBRATION_FAILED);
  }
  
  protected boolean doInitialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/robotcore/hardware/CompassSensor$CompassMode.MEASUREMENT_MODE : Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;
    //   6: invokevirtual setMode : (Lcom/qualcomm/robotcore/hardware/CompassSensor$CompassMode;)V
    //   9: aload_0
    //   10: monitorexit
    //   11: iconst_1
    //   12: ireturn
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	13	finally
  }
  
  public Acceleration getAcceleration() {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.ACCELX.bVal, 6);
    ByteBuffer byteBuffer = ByteBuffer.wrap(timestampedData.data).order(ByteOrder.LITTLE_ENDIAN);
    short s1 = byteBuffer.getShort();
    short s2 = byteBuffer.getShort();
    short s3 = byteBuffer.getShort();
    return Acceleration.fromGravity(s1 * 0.001D, s2 * 0.001D, s3 * 0.001D, timestampedData.nanoTime);
  }
  
  public String getDeviceName() {
    RobotUsbDevice.FirmwareVersion firmwareVersion = new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV));
    return String.format(Locale.getDefault(), "Modern Robotics Compass Sensor %s", new Object[] { firmwareVersion });
  }
  
  public double getDirection() {
    return readShort(Register.HEADING);
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public MagneticFlux getMagneticFlux() {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.MAGX.bVal, 6);
    ByteBuffer byteBuffer = ByteBuffer.wrap(timestampedData.data).order(ByteOrder.LITTLE_ENDIAN);
    short s1 = byteBuffer.getShort();
    short s2 = byteBuffer.getShort();
    short s3 = byteBuffer.getShort();
    return new MagneticFlux(s1 * 1.0E-4D, s2 * 1.0E-4D, s3 * 1.0E-4D, timestampedData.nanoTime);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public boolean isCalibrating() {
    return (readCommand() == Command.CALIBRATE_IRON);
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  public Command readCommand() {
    return Command.fromByte(read8(Register.COMMAND));
  }
  
  public int readShort(Register paramRegister) {
    return TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(paramRegister.bVal, 2), ByteOrder.LITTLE_ENDIAN);
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  public void setMode(CompassSensor.CompassMode paramCompassMode) {
    Command command;
    if (paramCompassMode == CompassSensor.CompassMode.CALIBRATION_MODE) {
      command = Command.CALIBRATE_IRON;
    } else {
      command = Command.NORMAL;
    } 
    writeCommand(command);
  }
  
  protected void setOptimalReadWindow() {
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.READ_WINDOW_FIRST.bVal, Register.READ_WINDOW_LAST.bVal - Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
  }
  
  public String status() {
    return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
  }
  
  public void write8(Register paramRegister, byte paramByte) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramByte);
  }
  
  public void writeCommand(Command paramCommand) {
    ((I2cDeviceSynch)this.deviceClient).waitForWriteCompletions(I2cWaitControl.ATOMIC);
    write8(Register.COMMAND, paramCommand.bVal);
  }
  
  public void writeShort(Register paramRegister, short paramShort) {
    ((I2cDeviceSynch)this.deviceClient).write(paramRegister.bVal, TypeConversion.shortToByteArray(paramShort, ByteOrder.LITTLE_ENDIAN));
  }
  
  public enum Command {
    ACCEL_GAIN_ADJUST,
    ACCEL_NULL_X,
    ACCEL_NULL_Y,
    ACCEL_NULL_Z,
    CALIBRATE_IRON,
    CALIBRATION_FAILED,
    MEASURE_TILT_DOWN,
    MEASURE_TILT_UP,
    NORMAL(0),
    UNKNOWN(0),
    WRITE_EEPROM(0);
    
    public byte bVal;
    
    static {
      ACCEL_NULL_X = new Command("ACCEL_NULL_X", 2, 88);
      ACCEL_NULL_Y = new Command("ACCEL_NULL_Y", 3, 89);
      ACCEL_NULL_Z = new Command("ACCEL_NULL_Z", 4, 90);
      ACCEL_GAIN_ADJUST = new Command("ACCEL_GAIN_ADJUST", 5, 71);
      MEASURE_TILT_UP = new Command("MEASURE_TILT_UP", 6, 85);
      MEASURE_TILT_DOWN = new Command("MEASURE_TILT_DOWN", 7, 68);
      WRITE_EEPROM = new Command("WRITE_EEPROM", 8, 87);
      CALIBRATION_FAILED = new Command("CALIBRATION_FAILED", 9, 70);
      Command command = new Command("UNKNOWN", 10, -1);
      UNKNOWN = command;
      $VALUES = new Command[] { 
          NORMAL, CALIBRATE_IRON, ACCEL_NULL_X, ACCEL_NULL_Y, ACCEL_NULL_Z, ACCEL_GAIN_ADJUST, MEASURE_TILT_UP, MEASURE_TILT_DOWN, WRITE_EEPROM, CALIBRATION_FAILED, 
          command };
    }
    
    Command(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static Command fromByte(byte param1Byte) {
      for (Command command : values()) {
        if (command.bVal == param1Byte)
          return command; 
      } 
      return UNKNOWN;
    }
  }
  
  public enum Register {
    ACCELX(0),
    ACCELX_OFFSET(0),
    ACCELY(0),
    ACCELY_OFFSET(0),
    ACCELZ(0),
    ACCELZ_OFFSET(0),
    ACCEL_SCALE_COEFF(0),
    COMMAND(0),
    FIRMWARE_REV(0),
    HEADING(0),
    MAGX(0),
    MAGX_OFFSET(0),
    MAGY(0),
    MAGY_OFFSET(0),
    MAGZ(0),
    MAGZ_OFFSET(0),
    MAG_SCALE_COEFF_X(0),
    MAG_SCALE_COEFF_Y(0),
    MAG_TILT_COEFF(0),
    MANUFACTURE_CODE(0),
    READ_WINDOW_FIRST(0),
    READ_WINDOW_LAST(0),
    SENSOR_ID(0),
    UNKNOWN(0);
    
    public byte bVal;
    
    static {
      COMMAND = new Register("COMMAND", 4, 3);
      HEADING = new Register("HEADING", 5, 4);
      ACCELX = new Register("ACCELX", 6, 6);
      ACCELY = new Register("ACCELY", 7, 8);
      ACCELZ = new Register("ACCELZ", 8, 10);
      MAGX = new Register("MAGX", 9, 12);
      MAGY = new Register("MAGY", 10, 14);
      MAGZ = new Register("MAGZ", 11, 16);
      READ_WINDOW_LAST = new Register("READ_WINDOW_LAST", 12, MAGZ.bVal + 1);
      ACCELX_OFFSET = new Register("ACCELX_OFFSET", 13, 18);
      ACCELY_OFFSET = new Register("ACCELY_OFFSET", 14, 20);
      ACCELZ_OFFSET = new Register("ACCELZ_OFFSET", 15, 22);
      MAGX_OFFSET = new Register("MAGX_OFFSET", 16, 24);
      MAGY_OFFSET = new Register("MAGY_OFFSET", 17, 26);
      MAGZ_OFFSET = new Register("MAGZ_OFFSET", 18, 28);
      MAG_TILT_COEFF = new Register("MAG_TILT_COEFF", 19, 30);
      ACCEL_SCALE_COEFF = new Register("ACCEL_SCALE_COEFF", 20, 32);
      MAG_SCALE_COEFF_X = new Register("MAG_SCALE_COEFF_X", 21, 34);
      MAG_SCALE_COEFF_Y = new Register("MAG_SCALE_COEFF_Y", 22, 36);
      Register register = new Register("UNKNOWN", 23, -1);
      UNKNOWN = register;
      $VALUES = new Register[] { 
          READ_WINDOW_FIRST, FIRMWARE_REV, MANUFACTURE_CODE, SENSOR_ID, COMMAND, HEADING, ACCELX, ACCELY, ACCELZ, MAGX, 
          MAGY, MAGZ, READ_WINDOW_LAST, ACCELX_OFFSET, ACCELY_OFFSET, ACCELZ_OFFSET, MAGX_OFFSET, MAGY_OFFSET, MAGZ_OFFSET, MAG_TILT_COEFF, 
          ACCEL_SCALE_COEFF, MAG_SCALE_COEFF_X, MAG_SCALE_COEFF_Y, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cCompassSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */