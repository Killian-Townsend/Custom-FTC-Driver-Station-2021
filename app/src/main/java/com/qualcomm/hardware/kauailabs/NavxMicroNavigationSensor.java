package com.qualcomm.hardware.kauailabs;

import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cAddrConfig;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.configuration.annotations.DeviceProperties;
import com.qualcomm.robotcore.hardware.configuration.annotations.I2cDeviceType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Axis;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@DeviceProperties(builtIn = true, description = "@string/navx_micro_description", name = "@string/navx_micro_name", xmlTag = "KauaiLabsNavxMicro")
@I2cDeviceType
public class NavxMicroNavigationSensor extends I2cDeviceSynchDeviceWithParameters<I2cDeviceSynch, NavxMicroNavigationSensor.Parameters> implements Gyroscope, IntegratingGyroscope, I2cAddrConfig {
  public static final I2cAddr ADDRESS_I2C_DEFAULT;
  
  protected static final I2cDeviceSynch.ReadWindow lowerWindow;
  
  protected static final I2cDeviceSynch.ReadMode readMode = I2cDeviceSynch.ReadMode.REPEAT;
  
  protected static final I2cDeviceSynch.ReadWindow upperWindow;
  
  public final int NAVX_WRITE_COMMAND_BIT = 128;
  
  protected float gyroScaleFactor;
  
  static {
    lowerWindow = newWindow(Register.SENSOR_STATUS_L, Register.LINEAR_ACC_Z_H);
    upperWindow = newWindow(Register.GYRO_X_L, Register.MAG_Z_H);
    ADDRESS_I2C_DEFAULT = I2cAddr.create7bit(50);
  }
  
  public NavxMicroNavigationSensor(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true, new Parameters());
    setReadWindow();
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(true);
    ((I2cDeviceSynch)this.deviceClient).engage();
  }
  
  protected static I2cDeviceSynch.ReadWindow newWindow(Register paramRegister1, Register paramRegister2) {
    return new I2cDeviceSynch.ReadWindow(paramRegister1.bVal, paramRegister2.bVal - paramRegister1.bVal, readMode);
  }
  
  protected void ensureReadWindow(I2cDeviceSynch.ReadWindow paramReadWindow) {
    I2cDeviceSynch.ReadWindow readWindow;
    if (lowerWindow.containsWithSameMode(paramReadWindow)) {
      readWindow = lowerWindow;
    } else if (upperWindow.containsWithSameMode(paramReadWindow)) {
      readWindow = upperWindow;
    } else {
      readWindow = paramReadWindow;
    } 
    ((I2cDeviceSynch)this.deviceClient).ensureReadWindow(paramReadWindow, readWindow);
  }
  
  public Orientation getAngularOrientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, AngleUnit paramAngleUnit) {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.YAW_L.bVal, 6);
    float f1 = -shortToSignedHundredths(TypeConversion.byteArrayToShort(timestampedData.data, 0, ByteOrder.LITTLE_ENDIAN));
    float f2 = shortToSignedHundredths(TypeConversion.byteArrayToShort(timestampedData.data, 2, ByteOrder.LITTLE_ENDIAN));
    float f3 = shortToSignedHundredths(TypeConversion.byteArrayToShort(timestampedData.data, 4, ByteOrder.LITTLE_ENDIAN));
    return (new Orientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES, f1, f2, f3, timestampedData.nanoTime)).toAxesReference(paramAxesReference).toAxesOrder(paramAxesOrder).toAngleUnit(paramAngleUnit);
  }
  
  public Set<Axis> getAngularOrientationAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.X);
    hashSet.add(Axis.Y);
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  public AngularVelocity getAngularVelocity(AngleUnit paramAngleUnit) {
    TimestampedData timestampedData = ((I2cDeviceSynch)this.deviceClient).readTimeStamped(Register.GYRO_X_L.bVal, 6);
    float f1 = TypeConversion.byteArrayToShort(timestampedData.data, 0, ByteOrder.LITTLE_ENDIAN);
    float f2 = this.gyroScaleFactor;
    float f3 = TypeConversion.byteArrayToShort(timestampedData.data, 2, ByteOrder.LITTLE_ENDIAN);
    float f4 = this.gyroScaleFactor;
    float f5 = TypeConversion.byteArrayToShort(timestampedData.data, 4, ByteOrder.LITTLE_ENDIAN);
    float f6 = this.gyroScaleFactor;
    return (new AngularVelocity(AngleUnit.DEGREES, f1 * f2, f3 * f4, f5 * f6, timestampedData.nanoTime)).toAngleUnit(paramAngleUnit);
  }
  
  public Set<Axis> getAngularVelocityAxes() {
    HashSet<Axis> hashSet = new HashSet();
    hashSet.add(Axis.X);
    hashSet.add(Axis.Y);
    hashSet.add(Axis.Z);
    return hashSet;
  }
  
  public String getDeviceName() {
    return String.format("Kauai Labs navX-Micro Gyro %s", new Object[] { getFirmwareVersion() });
  }
  
  public RobotUsbDevice.FirmwareVersion getFirmwareVersion() {
    return new RobotUsbDevice.FirmwareVersion(read8(Register.FW_VER_MAJOR), read8(Register.FW_VER_MINOR));
  }
  
  public I2cAddr getI2cAddress() {
    return ((I2cDeviceSynch)this.deviceClient).getI2cAddress();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Other;
  }
  
  protected boolean internalInitialize(Parameters paramParameters) {
    this.parameters = paramParameters.clone();
    write8(Register.UPDATE_RATE_HZ, (byte)paramParameters.updateRate);
    write8(Register.INTEGRATION_CTL, IntegrationControl.RESET_ALL.bVal);
    this.gyroScaleFactor = readShort(Register.GYRO_FSR_DPS_L) / 32768.0F;
    return true;
  }
  
  public boolean isCalibrating() {
    boolean bool;
    if ((read8(Register.SENSOR_STATUS_H) & CalibrationStatus.IMU_CAL_MASK.bVal) == CalibrationStatus.IMU_CAL_COMPLETE.bVal) {
      bool = true;
    } else {
      bool = false;
    } 
    return bool ^ true;
  }
  
  public byte read8(Register paramRegister) {
    return (readTimeStamped(paramRegister, 1)).data[0];
  }
  
  public short readShort(Register paramRegister) {
    return TypeConversion.byteArrayToShort((readTimeStamped(paramRegister, 2)).data, ByteOrder.LITTLE_ENDIAN);
  }
  
  public float readSignedHundredthsFloat(Register paramRegister) {
    return shortToSignedHundredths(readShort(paramRegister));
  }
  
  public TimestampedData readTimeStamped(Register paramRegister, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: new com/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow
    //   6: dup
    //   7: aload_1
    //   8: getfield bVal : B
    //   11: iload_2
    //   12: getstatic com/qualcomm/hardware/kauailabs/NavxMicroNavigationSensor.readMode : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;
    //   15: invokespecial <init> : (IILcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadMode;)V
    //   18: invokevirtual ensureReadWindow : (Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch$ReadWindow;)V
    //   21: aload_0
    //   22: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   25: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   28: aload_1
    //   29: getfield bVal : B
    //   32: iload_2
    //   33: invokeinterface readTimeStamped : (II)Lcom/qualcomm/robotcore/hardware/TimestampedData;
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: areturn
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	43	finally
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(paramI2cAddr);
  }
  
  protected void setReadWindow() {
    ((I2cDeviceSynch)this.deviceClient).setReadWindow(lowerWindow);
  }
  
  protected float shortToSignedHundredths(short paramShort) {
    return paramShort * 0.01F;
  }
  
  public void write8(Register paramRegister, byte paramByte) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal | 0x80, paramByte);
  }
  
  public void writeShort(Register paramRegister, short paramShort) {
    ((I2cDeviceSynch)this.deviceClient).write(paramRegister.bVal | 0x80, TypeConversion.shortToByteArray(paramShort, ByteOrder.LITTLE_ENDIAN));
  }
  
  public enum CalibrationStatus {
    BARO_CAL_COMPLETE,
    IMU_CAL_ACCUMULATE,
    IMU_CAL_COMPLETE,
    IMU_CAL_INPROGRESS(0),
    IMU_CAL_MASK(0),
    MAG_CAL_COMPLETE(0);
    
    public byte bVal;
    
    static {
      CalibrationStatus calibrationStatus = new CalibrationStatus("BARO_CAL_COMPLETE", 5, 8);
      BARO_CAL_COMPLETE = calibrationStatus;
      $VALUES = new CalibrationStatus[] { IMU_CAL_INPROGRESS, IMU_CAL_ACCUMULATE, IMU_CAL_COMPLETE, IMU_CAL_MASK, MAG_CAL_COMPLETE, calibrationStatus };
    }
    
    CalibrationStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum IntegrationControl {
    RESET_ALL(0),
    RESET_DISP_X(0),
    RESET_DISP_Y(0),
    RESET_DISP_Z(0),
    RESET_VEL_X(1),
    RESET_VEL_Y(2),
    RESET_VEL_Z(4),
    RESET_YAW(4);
    
    public byte bVal;
    
    static {
      IntegrationControl integrationControl = new IntegrationControl("RESET_ALL", 7, RESET_VEL_X.bVal | RESET_VEL_Y.bVal | RESET_VEL_Z.bVal | RESET_DISP_X.bVal | RESET_DISP_Y.bVal | RESET_DISP_Z.bVal | RESET_YAW.bVal);
      RESET_ALL = integrationControl;
      $VALUES = new IntegrationControl[] { RESET_VEL_X, RESET_VEL_Y, RESET_VEL_Z, RESET_DISP_X, RESET_DISP_Y, RESET_DISP_Z, RESET_YAW, integrationControl };
    }
    
    IntegrationControl(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public byte bitor(byte param1Byte) {
      return (byte)(param1Byte | this.bVal);
    }
    
    public byte bitor(IntegrationControl param1IntegrationControl) {
      byte b = this.bVal;
      return (byte)(param1IntegrationControl.bVal | b);
    }
  }
  
  public enum OpStatus {
    INITIALIZING(0),
    NORMAL(0),
    SELFTEST_IN_PROGRESS(1),
    ERROR(4),
    IMU_AUTOCAL_IN_PROGRESS(4);
    
    public byte bVal;
    
    static {
      OpStatus opStatus = new OpStatus("NORMAL", 4, 4);
      NORMAL = opStatus;
      $VALUES = new OpStatus[] { INITIALIZING, SELFTEST_IN_PROGRESS, ERROR, IMU_AUTOCAL_IN_PROGRESS, opStatus };
    }
    
    OpStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public static class Parameters implements Cloneable {
    public int updateRate = 50;
    
    public Parameters clone() {
      try {
        return (Parameters)super.clone();
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new RuntimeException("internal error: Parameters can't be cloned");
      } 
    }
    
    public int realizedUpdateRate() {
      return 200 / 200 / this.updateRate;
    }
  }
  
  public enum Register {
    FIRST(0),
    FUSED_HEADING_H(0),
    FUSED_HEADING_L(0),
    FW_VER_MAJOR(0),
    FW_VER_MINOR(0),
    GYRO_FSR_DPS_H(0),
    GYRO_FSR_DPS_L(0),
    GYRO_X_H(0),
    GYRO_X_L(0),
    GYRO_Y_H(0),
    GYRO_Y_L(0),
    GYRO_Z_H(0),
    GYRO_Z_L(0),
    HEADING_H(0),
    HEADING_L(0),
    HW_REV(0),
    INTEGRATION_CTL(0),
    LAST(0),
    LINEAR_ACC_X_H(0),
    LINEAR_ACC_X_L(0),
    LINEAR_ACC_Y_H(0),
    LINEAR_ACC_Y_L(0),
    LINEAR_ACC_Z_H(0),
    LINEAR_ACC_Z_L(0),
    MAG_X_H(0),
    MAG_X_L(0),
    MAG_Y_H(0),
    MAG_Y_L(0),
    MAG_Z_H(0),
    MAG_Z_L(0),
    MPU_TEMP_C_H(0),
    MPU_TEMP_C_L(0),
    OP_STATUS(0),
    PAD_UNUSED(0),
    PITCH_H(0),
    PITCH_L(0),
    PRESSURE_DH(0),
    PRESSURE_DL(0),
    PRESSURE_IH(0),
    PRESSURE_IL(0),
    PRESSURE_TEMP_H(0),
    PRESSURE_TEMP_L(0),
    QUAT_OFFSET_W_H(0),
    QUAT_OFFSET_W_L(0),
    QUAT_OFFSET_X_H(0),
    QUAT_OFFSET_X_L(0),
    QUAT_OFFSET_Y_H(0),
    QUAT_OFFSET_Y_L(0),
    QUAT_OFFSET_Z_H(0),
    QUAT_OFFSET_Z_L(0),
    QUAT_W_H(0),
    QUAT_W_L(0),
    QUAT_X_H(0),
    QUAT_X_L(0),
    QUAT_Y_H(0),
    QUAT_Y_L(0),
    QUAT_Z_H(0),
    QUAT_Z_L(0),
    ROLL_H(0),
    ROLL_L(0),
    SELFTEST_STATUS(0),
    SENSOR_STATUS_H(0),
    SENSOR_STATUS_L(0),
    TIMESTAMP_H_H(0),
    TIMESTAMP_H_L(0),
    TIMESTAMP_L_H(0),
    TIMESTAMP_L_L(0),
    UNKNOWN(0),
    UPDATE_RATE_HZ(0),
    VEL_X_D_H(0),
    VEL_X_D_L(0),
    VEL_X_I_H(0),
    VEL_X_I_L(0),
    VEL_Y_D_H(0),
    VEL_Y_D_L(0),
    VEL_Y_I_H(0),
    VEL_Y_I_L(0),
    VEL_Z_D_H(0),
    VEL_Z_D_L(0),
    VEL_Z_I_H(0),
    VEL_Z_I_L(0),
    ACCEL_FSR_G(1),
    ACC_X_H(1),
    ACC_X_L(1),
    ACC_Y_H(1),
    ACC_Y_L(1),
    ACC_Z_H(1),
    ACC_Z_L(1),
    ALTITUDE_D_H(1),
    ALTITUDE_D_L(1),
    ALTITUDE_I_H(1),
    ALTITUDE_I_L(1),
    CAL_STATUS(1),
    CAPABILITY_FLAGS_H(1),
    CAPABILITY_FLAGS_L(1),
    DISP_X_D_H(1),
    DISP_X_D_L(1),
    DISP_X_I_H(1),
    DISP_X_I_L(1),
    DISP_Y_D_H(1),
    DISP_Y_D_L(1),
    DISP_Y_I_H(1),
    DISP_Y_I_L(1),
    DISP_Z_D_H(1),
    DISP_Z_D_L(1),
    DISP_Z_I_H(1),
    DISP_Z_I_L(1),
    WHOAMI(0),
    YAW_H(0),
    YAW_L(0),
    YAW_OFFSET_H(0),
    YAW_OFFSET_L(0);
    
    public byte bVal;
    
    static {
      FW_VER_MAJOR = new Register("FW_VER_MAJOR", 3, 2);
      FW_VER_MINOR = new Register("FW_VER_MINOR", 4, 3);
      UPDATE_RATE_HZ = new Register("UPDATE_RATE_HZ", 5, 4);
      ACCEL_FSR_G = new Register("ACCEL_FSR_G", 6, 5);
      GYRO_FSR_DPS_L = new Register("GYRO_FSR_DPS_L", 7, 6);
      GYRO_FSR_DPS_H = new Register("GYRO_FSR_DPS_H", 8, 7);
      OP_STATUS = new Register("OP_STATUS", 9, 8);
      CAL_STATUS = new Register("CAL_STATUS", 10, 9);
      SELFTEST_STATUS = new Register("SELFTEST_STATUS", 11, 10);
      CAPABILITY_FLAGS_L = new Register("CAPABILITY_FLAGS_L", 12, 11);
      CAPABILITY_FLAGS_H = new Register("CAPABILITY_FLAGS_H", 13, 12);
      SENSOR_STATUS_L = new Register("SENSOR_STATUS_L", 14, 16);
      SENSOR_STATUS_H = new Register("SENSOR_STATUS_H", 15, 17);
      TIMESTAMP_L_L = new Register("TIMESTAMP_L_L", 16, 18);
      TIMESTAMP_L_H = new Register("TIMESTAMP_L_H", 17, 19);
      TIMESTAMP_H_L = new Register("TIMESTAMP_H_L", 18, 20);
      TIMESTAMP_H_H = new Register("TIMESTAMP_H_H", 19, 21);
      YAW_L = new Register("YAW_L", 20, 22);
      YAW_H = new Register("YAW_H", 21, 23);
      ROLL_L = new Register("ROLL_L", 22, 24);
      ROLL_H = new Register("ROLL_H", 23, 25);
      PITCH_L = new Register("PITCH_L", 24, 26);
      PITCH_H = new Register("PITCH_H", 25, 27);
      HEADING_L = new Register("HEADING_L", 26, 28);
      HEADING_H = new Register("HEADING_H", 27, 29);
      FUSED_HEADING_L = new Register("FUSED_HEADING_L", 28, 30);
      FUSED_HEADING_H = new Register("FUSED_HEADING_H", 29, 31);
      ALTITUDE_I_L = new Register("ALTITUDE_I_L", 30, 32);
      ALTITUDE_I_H = new Register("ALTITUDE_I_H", 31, 33);
      ALTITUDE_D_L = new Register("ALTITUDE_D_L", 32, 34);
      ALTITUDE_D_H = new Register("ALTITUDE_D_H", 33, 35);
      LINEAR_ACC_X_L = new Register("LINEAR_ACC_X_L", 34, 36);
      LINEAR_ACC_X_H = new Register("LINEAR_ACC_X_H", 35, 37);
      LINEAR_ACC_Y_L = new Register("LINEAR_ACC_Y_L", 36, 38);
      LINEAR_ACC_Y_H = new Register("LINEAR_ACC_Y_H", 37, 39);
      LINEAR_ACC_Z_L = new Register("LINEAR_ACC_Z_L", 38, 40);
      LINEAR_ACC_Z_H = new Register("LINEAR_ACC_Z_H", 39, 41);
      QUAT_W_L = new Register("QUAT_W_L", 40, 42);
      QUAT_W_H = new Register("QUAT_W_H", 41, 43);
      QUAT_X_L = new Register("QUAT_X_L", 42, 44);
      QUAT_X_H = new Register("QUAT_X_H", 43, 45);
      QUAT_Y_L = new Register("QUAT_Y_L", 44, 46);
      QUAT_Y_H = new Register("QUAT_Y_H", 45, 47);
      QUAT_Z_L = new Register("QUAT_Z_L", 46, 48);
      QUAT_Z_H = new Register("QUAT_Z_H", 47, 49);
      MPU_TEMP_C_L = new Register("MPU_TEMP_C_L", 48, 50);
      MPU_TEMP_C_H = new Register("MPU_TEMP_C_H", 49, 51);
      GYRO_X_L = new Register("GYRO_X_L", 50, 52);
      GYRO_X_H = new Register("GYRO_X_H", 51, 53);
      GYRO_Y_L = new Register("GYRO_Y_L", 52, 54);
      GYRO_Y_H = new Register("GYRO_Y_H", 53, 55);
      GYRO_Z_L = new Register("GYRO_Z_L", 54, 56);
      GYRO_Z_H = new Register("GYRO_Z_H", 55, 57);
      ACC_X_L = new Register("ACC_X_L", 56, 58);
      ACC_X_H = new Register("ACC_X_H", 57, 59);
      ACC_Y_L = new Register("ACC_Y_L", 58, 60);
      ACC_Y_H = new Register("ACC_Y_H", 59, 61);
      ACC_Z_L = new Register("ACC_Z_L", 60, 62);
      ACC_Z_H = new Register("ACC_Z_H", 61, 63);
      MAG_X_L = new Register("MAG_X_L", 62, 64);
      MAG_X_H = new Register("MAG_X_H", 63, 65);
      MAG_Y_L = new Register("MAG_Y_L", 64, 66);
      MAG_Y_H = new Register("MAG_Y_H", 65, 67);
      MAG_Z_L = new Register("MAG_Z_L", 66, 68);
      MAG_Z_H = new Register("MAG_Z_H", 67, 69);
      PRESSURE_IL = new Register("PRESSURE_IL", 68, 70);
      PRESSURE_IH = new Register("PRESSURE_IH", 69, 71);
      PRESSURE_DL = new Register("PRESSURE_DL", 70, 72);
      PRESSURE_DH = new Register("PRESSURE_DH", 71, 73);
      PRESSURE_TEMP_L = new Register("PRESSURE_TEMP_L", 72, 74);
      PRESSURE_TEMP_H = new Register("PRESSURE_TEMP_H", 73, 75);
      YAW_OFFSET_L = new Register("YAW_OFFSET_L", 74, 76);
      YAW_OFFSET_H = new Register("YAW_OFFSET_H", 75, 77);
      QUAT_OFFSET_W_L = new Register("QUAT_OFFSET_W_L", 76, 78);
      QUAT_OFFSET_W_H = new Register("QUAT_OFFSET_W_H", 77, 79);
      QUAT_OFFSET_X_L = new Register("QUAT_OFFSET_X_L", 78, 80);
      QUAT_OFFSET_X_H = new Register("QUAT_OFFSET_X_H", 79, 81);
      QUAT_OFFSET_Y_L = new Register("QUAT_OFFSET_Y_L", 80, 82);
      QUAT_OFFSET_Y_H = new Register("QUAT_OFFSET_Y_H", 81, 83);
      QUAT_OFFSET_Z_L = new Register("QUAT_OFFSET_Z_L", 82, 84);
      QUAT_OFFSET_Z_H = new Register("QUAT_OFFSET_Z_H", 83, 85);
      INTEGRATION_CTL = new Register("INTEGRATION_CTL", 84, 86);
      PAD_UNUSED = new Register("PAD_UNUSED", 85, 87);
      VEL_X_I_L = new Register("VEL_X_I_L", 86, 88);
      VEL_X_I_H = new Register("VEL_X_I_H", 87, 89);
      VEL_X_D_L = new Register("VEL_X_D_L", 88, 90);
      VEL_X_D_H = new Register("VEL_X_D_H", 89, 91);
      VEL_Y_I_L = new Register("VEL_Y_I_L", 90, 92);
      VEL_Y_I_H = new Register("VEL_Y_I_H", 91, 93);
      VEL_Y_D_L = new Register("VEL_Y_D_L", 92, 94);
      VEL_Y_D_H = new Register("VEL_Y_D_H", 93, 95);
      VEL_Z_I_L = new Register("VEL_Z_I_L", 94, 96);
      VEL_Z_I_H = new Register("VEL_Z_I_H", 95, 97);
      VEL_Z_D_L = new Register("VEL_Z_D_L", 96, 98);
      VEL_Z_D_H = new Register("VEL_Z_D_H", 97, 99);
      DISP_X_I_L = new Register("DISP_X_I_L", 98, 100);
      DISP_X_I_H = new Register("DISP_X_I_H", 99, 101);
      DISP_X_D_L = new Register("DISP_X_D_L", 100, 102);
      DISP_X_D_H = new Register("DISP_X_D_H", 101, 103);
      DISP_Y_I_L = new Register("DISP_Y_I_L", 102, 104);
      DISP_Y_I_H = new Register("DISP_Y_I_H", 103, 105);
      DISP_Y_D_L = new Register("DISP_Y_D_L", 104, 106);
      DISP_Y_D_H = new Register("DISP_Y_D_H", 105, 107);
      DISP_Z_I_L = new Register("DISP_Z_I_L", 106, 108);
      DISP_Z_I_H = new Register("DISP_Z_I_H", 107, 109);
      DISP_Z_D_L = new Register("DISP_Z_D_L", 108, 110);
      DISP_Z_D_H = new Register("DISP_Z_D_H", 109, 111);
      LAST = new Register("LAST", 110, DISP_Z_D_H.bVal);
      Register register = new Register("UNKNOWN", 111, -1);
      UNKNOWN = register;
      $VALUES = new Register[] { 
          FIRST, WHOAMI, HW_REV, FW_VER_MAJOR, FW_VER_MINOR, UPDATE_RATE_HZ, ACCEL_FSR_G, GYRO_FSR_DPS_L, GYRO_FSR_DPS_H, OP_STATUS, 
          CAL_STATUS, SELFTEST_STATUS, CAPABILITY_FLAGS_L, CAPABILITY_FLAGS_H, SENSOR_STATUS_L, SENSOR_STATUS_H, TIMESTAMP_L_L, TIMESTAMP_L_H, TIMESTAMP_H_L, TIMESTAMP_H_H, 
          YAW_L, YAW_H, ROLL_L, ROLL_H, PITCH_L, PITCH_H, HEADING_L, HEADING_H, FUSED_HEADING_L, FUSED_HEADING_H, 
          ALTITUDE_I_L, ALTITUDE_I_H, ALTITUDE_D_L, ALTITUDE_D_H, LINEAR_ACC_X_L, LINEAR_ACC_X_H, LINEAR_ACC_Y_L, LINEAR_ACC_Y_H, LINEAR_ACC_Z_L, LINEAR_ACC_Z_H, 
          QUAT_W_L, QUAT_W_H, QUAT_X_L, QUAT_X_H, QUAT_Y_L, QUAT_Y_H, QUAT_Z_L, QUAT_Z_H, MPU_TEMP_C_L, MPU_TEMP_C_H, 
          GYRO_X_L, GYRO_X_H, GYRO_Y_L, GYRO_Y_H, GYRO_Z_L, GYRO_Z_H, ACC_X_L, ACC_X_H, ACC_Y_L, ACC_Y_H, 
          ACC_Z_L, ACC_Z_H, MAG_X_L, MAG_X_H, MAG_Y_L, MAG_Y_H, MAG_Z_L, MAG_Z_H, PRESSURE_IL, PRESSURE_IH, 
          PRESSURE_DL, PRESSURE_DH, PRESSURE_TEMP_L, PRESSURE_TEMP_H, YAW_OFFSET_L, YAW_OFFSET_H, QUAT_OFFSET_W_L, QUAT_OFFSET_W_H, QUAT_OFFSET_X_L, QUAT_OFFSET_X_H, 
          QUAT_OFFSET_Y_L, QUAT_OFFSET_Y_H, QUAT_OFFSET_Z_L, QUAT_OFFSET_Z_H, INTEGRATION_CTL, PAD_UNUSED, VEL_X_I_L, VEL_X_I_H, VEL_X_D_L, VEL_X_D_H, 
          VEL_Y_I_L, VEL_Y_I_H, VEL_Y_D_L, VEL_Y_D_H, VEL_Z_I_L, VEL_Z_I_H, VEL_Z_D_L, VEL_Z_D_H, DISP_X_I_L, DISP_X_I_H, 
          DISP_X_D_L, DISP_X_D_H, DISP_Y_I_L, DISP_Y_I_H, DISP_Y_D_L, DISP_Y_D_H, DISP_Z_I_L, DISP_Z_I_H, DISP_Z_D_L, DISP_Z_D_H, 
          LAST, register };
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
  
  public enum SelfTestStatus {
    COMPLETE(128),
    RESULT_ACCEL_PASSED(128),
    RESULT_BARO_PASSED(128),
    RESULT_GYRO_PASSED(1),
    RESULT_MAG_PASSED(1);
    
    public byte bVal;
    
    static {
      SelfTestStatus selfTestStatus = new SelfTestStatus("RESULT_BARO_PASSED", 4, 8);
      RESULT_BARO_PASSED = selfTestStatus;
      $VALUES = new SelfTestStatus[] { COMPLETE, RESULT_GYRO_PASSED, RESULT_ACCEL_PASSED, RESULT_MAG_PASSED, selfTestStatus };
    }
    
    SelfTestStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum SensorStatus {
    MOVING(1),
    SEALEVEL_PRESS_SET(1),
    ALTITUDE_VALID(1),
    FUSED_HEADING_VALID(1),
    MAG_DISTURBANCE(1),
    YAW_STABLE(2);
    
    public byte bVal;
    
    static {
      ALTITUDE_VALID = new SensorStatus("ALTITUDE_VALID", 3, 8);
      SEALEVEL_PRESS_SET = new SensorStatus("SEALEVEL_PRESS_SET", 4, 16);
      SensorStatus sensorStatus = new SensorStatus("FUSED_HEADING_VALID", 5, 32);
      FUSED_HEADING_VALID = sensorStatus;
      $VALUES = new SensorStatus[] { MOVING, YAW_STABLE, MAG_DISTURBANCE, ALTITUDE_VALID, SEALEVEL_PRESS_SET, sensorStatus };
    }
    
    SensorStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\kauailabs\NavxMicroNavigationSensor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */