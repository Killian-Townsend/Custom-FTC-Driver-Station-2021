package com.qualcomm.hardware.modernrobotics;

import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class ModernRoboticsI2cGyro extends I2cDeviceSynchDevice<I2cDeviceSynch> implements GyroSensor, Gyroscope, IntegratingGyroscope, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(32);
  
  protected float degreesPerSecondPerDigit = 0.00875F;
  
  protected float degreesPerZAxisTick;
  
  protected HeadingMode headingMode = HeadingMode.HEADING_CARTESIAN;
  
  public ModernRoboticsI2cGyro(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    setOptimalReadWindow();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  public void calibrate() {
    writeCommand(Command.CALIBRATE);
  }
  
  protected float degreesZFromIntegratedZ(int paramInt) {
    return paramInt * this.degreesPerZAxisTick;
  }
  
  protected boolean doInitialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$Command.NORMAL : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$Command;
    //   6: invokevirtual writeCommand : (Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$Command;)V
    //   9: aload_0
    //   10: invokevirtual resetZAxisIntegrator : ()V
    //   13: aload_0
    //   14: sipush #256
    //   17: invokevirtual setZAxisScalingCoefficient : (I)V
    //   20: aload_0
    //   21: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode.HEADING_CARTESIAN : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode;
    //   24: putfield headingMode : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode;
    //   27: aload_0
    //   28: monitorexit
    //   29: iconst_1
    //   30: ireturn
    //   31: astore_1
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_1
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	31	finally
  }
  
  public Orientation getAngularOrientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit) {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.INTEGRATED_Z_VALUE.bVal, 2);
    float f = AngleUnit.normalizeDegrees(degreesZFromIntegratedZ(TypeConversion.byteArrayToShort(timestampedData.data, ByteOrder.LITTLE_ENDIAN)));
    return (new Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES, f, 0.0F, 0.0F, timestampedData.nanoTime)).toAxesReference(paramAxesReference).toAxesOrder(paramAxesOrder).toAngleUnit(paramAngleUnit);
  }
  
  public Set<Axis> getAngularOrientationAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  public AngularVelocity getAngularVelocity(AngleUnit paramAngleUnit) {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.RAW_X_VAL.bVal, 6);
    short s1 = TypeConversion.byteArrayToShort(timestampedData.data, 0, ByteOrder.LITTLE_ENDIAN);
    short s2 = TypeConversion.byteArrayToShort(timestampedData.data, 2, ByteOrder.LITTLE_ENDIAN);
    short s3 = TypeConversion.byteArrayToShort(timestampedData.data, 4, ByteOrder.LITTLE_ENDIAN);
    float f1 = s1;
    float f2 = this.degreesPerSecondPerDigit;
    float f3 = s2;
    float f4 = s3;
    return (new AngularVelocity(AngleUnit.DEGREES, f1 * f2, f3 * f2, f4 * f2, timestampedData.nanoTime)).toAngleUnit(paramAngleUnit);
  }
  
  public Set<Axis> getAngularVelocityAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.X);
    hashSet.add(Axis.Y);
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  public String getDeviceName() {
    RobotUsbDevice.FirmwareVersion firmwareVersion = new RobotUsbDevice.FirmwareVersion(read8(Register.FIRMWARE_REV));
    return String.format(Locale.getDefault(), "Modern Robotics Gyroscope %s", new Object[] { firmwareVersion });
  }
  
  public int getHeading() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: aload_0
    //   5: invokevirtual getIntegratedZValue : ()I
    //   8: invokevirtual degreesZFromIntegratedZ : (I)F
    //   11: invokevirtual normalize0359 : (F)F
    //   14: fstore_1
    //   15: aload_0
    //   16: getfield headingMode : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode;
    //   19: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode.HEADING_CARDINAL : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode;
    //   22: if_acmpne -> 53
    //   25: fload_1
    //   26: fconst_0
    //   27: fcmpl
    //   28: ifne -> 34
    //   31: goto -> 43
    //   34: fload_1
    //   35: ldc_w 360.0
    //   38: fsub
    //   39: invokestatic abs : (F)F
    //   42: fstore_1
    //   43: aload_0
    //   44: fload_1
    //   45: invokevirtual truncate : (F)I
    //   48: istore_2
    //   49: aload_0
    //   50: monitorexit
    //   51: iload_2
    //   52: ireturn
    //   53: aload_0
    //   54: fload_1
    //   55: invokevirtual truncate : (F)I
    //   58: istore_2
    //   59: aload_0
    //   60: monitorexit
    //   61: iload_2
    //   62: ireturn
    //   63: astore_3
    //   64: aload_0
    //   65: monitorexit
    //   66: aload_3
    //   67: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	63	finally
    //   34	43	63	finally
    //   43	49	63	finally
    //   53	59	63	finally
  }
  
  public HeadingMode getHeadingMode() {
    return this.headingMode;
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public int getIntegratedZValue() {
    return readShort(Register.INTEGRATED_Z_VALUE);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  @Deprecated
  public MeasurementMode getMeasurementMode() {
    return isCalibrating() ? MeasurementMode.GYRO_CALIBRATING : MeasurementMode.GYRO_NORMAL;
  }
  
  @Deprecated
  public double getRotationFraction() {
    notSupported();
    return 0.0D;
  }
  
  public int getZAxisOffset() {
    return readShort(Register.Z_AXIS_OFFSET);
  }
  
  public int getZAxisScalingCoefficient() {
    return TypeConversion.unsignedShortToInt(readShort(Register.Z_AXIS_SCALE_COEF));
  }
  
  public boolean isCalibrating() {
    return (readCommand() == Command.CALIBRATE);
  }
  
  protected float normalize0359(float paramFloat) {
    float f = AngleUnit.normalizeDegrees(paramFloat);
    paramFloat = f;
    if (f < 0.0F)
      paramFloat = f + 360.0F; 
    return paramFloat;
  }
  
  protected void notSupported() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("This method is not supported for ");
    stringBuilder.append(getDeviceName());
    throw new UnsupportedOperationException(stringBuilder.toString());
  }
  
  public int rawX() {
    return readShort(Register.RAW_X_VAL);
  }
  
  public int rawY() {
    return readShort(Register.RAW_Y_VAL);
  }
  
  public int rawZ() {
    return readShort(Register.RAW_Z_VAL);
  }
  
  public byte read8(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  public Command readCommand() {
    return Command.fromByte(read8(Register.COMMAND));
  }
  
  public short readShort(Register paramRegister) {
    return TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(paramRegister.bVal, 2), ByteOrder.LITTLE_ENDIAN);
  }
  
  public void resetZAxisIntegrator() {
    writeCommand(Command.RESET_Z_AXIS);
  }
  
  public void setHeadingMode(HeadingMode paramHeadingMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield headingMode : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsI2cGyro$HeadingMode;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  protected void setOptimalReadWindow() {
    I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow(Register.READ_WINDOW_FIRST.bVal, Register.READ_WINDOW_LAST.bVal - Register.READ_WINDOW_FIRST.bVal + 1, I2cDeviceSynch.ReadMode.REPEAT);
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(readWindow);
  }
  
  public void setZAxisOffset(short paramShort) {
    writeShort(Register.Z_AXIS_OFFSET, paramShort);
  }
  
  public void setZAxisScalingCoefficient(int paramInt) {
    writeShort(Register.Z_AXIS_SCALE_COEF, (short)paramInt);
    this.degreesPerZAxisTick = 256.0F / paramInt;
  }
  
  public String status() {
    return String.format(Locale.getDefault(), "%s on %s", new Object[] { getDeviceName(), getConnectionInfo() });
  }
  
  protected int truncate(float paramFloat) {
    return (int)paramFloat;
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
    CALIBRATE,
    NORMAL(0),
    RESET_Z_AXIS(0),
    UNKNOWN(0),
    WRITE_EEPROM(0);
    
    public byte bVal;
    
    static {
      Command command = new Command("UNKNOWN", 4, -1);
      UNKNOWN = command;
      $VALUES = new Command[] { NORMAL, CALIBRATE, RESET_Z_AXIS, WRITE_EEPROM, command };
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
  
  public enum HeadingMode {
    HEADING_CARDINAL(0),
    HEADING_CARTESIAN;
    
    static {
      HeadingMode headingMode = new HeadingMode("HEADING_CARDINAL", 1);
      HEADING_CARDINAL = headingMode;
      $VALUES = new HeadingMode[] { HEADING_CARTESIAN, headingMode };
    }
  }
  
  @Deprecated
  public enum MeasurementMode {
    GYRO_CALIBRATING, GYRO_CALIBRATION_PENDING, GYRO_NORMAL;
    
    static {
      MeasurementMode measurementMode = new MeasurementMode("GYRO_NORMAL", 2);
      GYRO_NORMAL = measurementMode;
      $VALUES = new MeasurementMode[] { GYRO_CALIBRATION_PENDING, GYRO_CALIBRATING, measurementMode };
    }
  }
  
  public enum Register {
    COMMAND,
    FIRMWARE_REV,
    HEADING_DATA,
    INTEGRATED_Z_VALUE,
    MANUFACTURE_CODE,
    RAW_X_VAL,
    RAW_Y_VAL,
    RAW_Z_VAL,
    READ_WINDOW_FIRST(0),
    READ_WINDOW_LAST(0),
    SENSOR_ID(0),
    UNKNOWN(0),
    Z_AXIS_OFFSET(0),
    Z_AXIS_SCALE_COEF(0);
    
    public byte bVal;
    
    static {
      COMMAND = new Register("COMMAND", 4, 3);
      HEADING_DATA = new Register("HEADING_DATA", 5, 4);
      INTEGRATED_Z_VALUE = new Register("INTEGRATED_Z_VALUE", 6, 6);
      RAW_X_VAL = new Register("RAW_X_VAL", 7, 8);
      RAW_Y_VAL = new Register("RAW_Y_VAL", 8, 10);
      RAW_Z_VAL = new Register("RAW_Z_VAL", 9, 12);
      Z_AXIS_OFFSET = new Register("Z_AXIS_OFFSET", 10, 14);
      Z_AXIS_SCALE_COEF = new Register("Z_AXIS_SCALE_COEF", 11, 16);
      READ_WINDOW_LAST = new Register("READ_WINDOW_LAST", 12, Z_AXIS_SCALE_COEF.bVal + 1);
      Register register = new Register("UNKNOWN", 13, -1);
      UNKNOWN = register;
      $VALUES = new Register[] { 
          READ_WINDOW_FIRST, FIRMWARE_REV, MANUFACTURE_CODE, SENSOR_ID, COMMAND, HEADING_DATA, INTEGRATED_Z_VALUE, RAW_X_VAL, RAW_Y_VAL, RAW_Z_VAL, 
          Z_AXIS_OFFSET, Z_AXIS_SCALE_COEF, READ_WINDOW_LAST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static Register fromByte(byte param1Byte) {
      for (Register register : values()) {
        if (register.bVal == param1Byte)
          return register; 
      } 
      return UNKNOWN;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsI2cGyro.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */