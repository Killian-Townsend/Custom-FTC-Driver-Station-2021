package com.qualcomm.hardware.bosch;

import com.qualcomm.robotcore.hardware.I2cAddr;
import java.util.Locale;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngularVelocity;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.MagneticFlux;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;
import org.firstinspires.ftc.robotcore.external.navigation.Temperature;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.firstinspires.ftc.robotcore.internal.collections.SimpleGson;

public interface BNO055IMU {
  public static final I2cAddr I2CADDR_ALTERNATE;
  
  public static final I2cAddr I2CADDR_DEFAULT;
  
  public static final I2cAddr I2CADDR_UNSPECIFIED = I2cAddr.zero();
  
  static {
    I2CADDR_DEFAULT = I2cAddr.create7bit(40);
    I2CADDR_ALTERNATE = I2cAddr.create7bit(41);
  }
  
  void close();
  
  Acceleration getAcceleration();
  
  Orientation getAngularOrientation();
  
  Orientation getAngularOrientation(AxesReference paramAxesReference, AxesOrder paramAxesOrder, org.firstinspires.ftc.robotcore.external.navigation.AngleUnit paramAngleUnit);
  
  AngularVelocity getAngularVelocity();
  
  CalibrationStatus getCalibrationStatus();
  
  Acceleration getGravity();
  
  Acceleration getLinearAcceleration();
  
  MagneticFlux getMagneticFieldStrength();
  
  Acceleration getOverallAcceleration();
  
  Parameters getParameters();
  
  Position getPosition();
  
  Quaternion getQuaternionOrientation();
  
  SystemError getSystemError();
  
  SystemStatus getSystemStatus();
  
  Temperature getTemperature();
  
  Velocity getVelocity();
  
  boolean initialize(Parameters paramParameters);
  
  boolean isAccelerometerCalibrated();
  
  boolean isGyroCalibrated();
  
  boolean isMagnetometerCalibrated();
  
  boolean isSystemCalibrated();
  
  byte[] read(Register paramRegister, int paramInt);
  
  byte read8(Register paramRegister);
  
  CalibrationData readCalibrationData();
  
  void startAccelerationIntegration(Position paramPosition, Velocity paramVelocity, int paramInt);
  
  void stopAccelerationIntegration();
  
  void write(Register paramRegister, byte[] paramArrayOfbyte);
  
  void write8(Register paramRegister, int paramInt);
  
  void writeCalibrationData(CalibrationData paramCalibrationData);
  
  public enum AccelBandwidth {
    HZ1000,
    HZ125,
    HZ15_63,
    HZ250,
    HZ31_25,
    HZ500,
    HZ62_5,
    HZ7_81(0);
    
    public final byte bVal;
    
    static {
      HZ125 = new AccelBandwidth("HZ125", 4, 4);
      HZ250 = new AccelBandwidth("HZ250", 5, 5);
      HZ500 = new AccelBandwidth("HZ500", 6, 6);
      AccelBandwidth accelBandwidth = new AccelBandwidth("HZ1000", 7, 7);
      HZ1000 = accelBandwidth;
      $VALUES = new AccelBandwidth[] { HZ7_81, HZ15_63, HZ31_25, HZ62_5, HZ125, HZ250, HZ500, accelBandwidth };
    }
    
    AccelBandwidth(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 2);
    }
  }
  
  public enum AccelPowerMode {
    DEEP(0),
    LOW1(0),
    LOW2(0),
    NORMAL(0),
    STANDBY(0),
    SUSPEND(1);
    
    public final byte bVal;
    
    static {
      LOW2 = new AccelPowerMode("LOW2", 4, 4);
      AccelPowerMode accelPowerMode = new AccelPowerMode("DEEP", 5, 5);
      DEEP = accelPowerMode;
      $VALUES = new AccelPowerMode[] { NORMAL, SUSPEND, LOW1, STANDBY, LOW2, accelPowerMode };
    }
    
    AccelPowerMode(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 5);
    }
  }
  
  public enum AccelRange {
    G2(0),
    G16(1),
    G4(1),
    G8(2);
    
    public final byte bVal;
    
    static {
      AccelRange accelRange = new AccelRange("G16", 3, 3);
      G16 = accelRange;
      $VALUES = new AccelRange[] { G2, G4, G8, accelRange };
    }
    
    AccelRange(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 0);
    }
  }
  
  public enum AccelUnit {
    METERS_PERSEC_PERSEC(0),
    MILLI_EARTH_GRAVITY(0);
    
    public final byte bVal;
    
    static {
      AccelUnit accelUnit = new AccelUnit("MILLI_EARTH_GRAVITY", 1, 1);
      MILLI_EARTH_GRAVITY = accelUnit;
      $VALUES = new AccelUnit[] { METERS_PERSEC_PERSEC, accelUnit };
    }
    
    AccelUnit(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public static interface AccelerationIntegrator {
    Acceleration getAcceleration();
    
    Position getPosition();
    
    Velocity getVelocity();
    
    void initialize(BNO055IMU.Parameters param1Parameters, Position param1Position, Velocity param1Velocity);
    
    void update(Acceleration param1Acceleration);
  }
  
  public enum AngleUnit {
    DEGREES(0),
    RADIANS(0);
    
    public final byte bVal;
    
    static {
      AngleUnit angleUnit = new AngleUnit("RADIANS", 1, 1);
      RADIANS = angleUnit;
      $VALUES = new AngleUnit[] { DEGREES, angleUnit };
    }
    
    AngleUnit(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static AngleUnit fromAngleUnit(org.firstinspires.ftc.robotcore.external.navigation.AngleUnit param1AngleUnit) {
      return (param1AngleUnit == org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES) ? DEGREES : RADIANS;
    }
    
    public org.firstinspires.ftc.robotcore.external.navigation.AngleUnit toAngleUnit() {
      return (this == DEGREES) ? org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES : org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.RADIANS;
    }
  }
  
  public static class CalibrationData implements Cloneable {
    public short dxAccel;
    
    public short dxGyro;
    
    public short dxMag;
    
    public short dyAccel;
    
    public short dyGyro;
    
    public short dyMag;
    
    public short dzAccel;
    
    public short dzGyro;
    
    public short dzMag;
    
    public short radiusAccel;
    
    public short radiusMag;
    
    public static CalibrationData deserialize(String param1String) {
      return (CalibrationData)SimpleGson.getInstance().fromJson(param1String, CalibrationData.class);
    }
    
    public CalibrationData clone() {
      try {
        return (CalibrationData)super.clone();
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new RuntimeException("internal error: CalibrationData can't be cloned");
      } 
    }
    
    public String serialize() {
      return SimpleGson.getInstance().toJson(this);
    }
  }
  
  public static class CalibrationStatus {
    public final byte calibrationStatus;
    
    public CalibrationStatus(int param1Int) {
      this.calibrationStatus = (byte)param1Int;
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format(Locale.getDefault(), "s%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 6 & 0x3) }));
      stringBuilder.append(" ");
      stringBuilder.append(String.format(Locale.getDefault(), "g%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 4 & 0x3) }));
      stringBuilder.append(" ");
      stringBuilder.append(String.format(Locale.getDefault(), "a%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 2 & 0x3) }));
      stringBuilder.append(" ");
      stringBuilder.append(String.format(Locale.getDefault(), "m%d", new Object[] { Integer.valueOf(this.calibrationStatus >> 0 & 0x3) }));
      return stringBuilder.toString();
    }
  }
  
  public enum GyroBandwidth {
    HZ116(0),
    HZ12(0),
    HZ23(0),
    HZ230(0),
    HZ32(0),
    HZ47(0),
    HZ523(0),
    HZ64(0);
    
    public final byte bVal;
    
    static {
      HZ116 = new GyroBandwidth("HZ116", 2, 2);
      HZ47 = new GyroBandwidth("HZ47", 3, 3);
      HZ23 = new GyroBandwidth("HZ23", 4, 4);
      HZ12 = new GyroBandwidth("HZ12", 5, 5);
      HZ64 = new GyroBandwidth("HZ64", 6, 6);
      GyroBandwidth gyroBandwidth = new GyroBandwidth("HZ32", 7, 7);
      HZ32 = gyroBandwidth;
      $VALUES = new GyroBandwidth[] { HZ523, HZ230, HZ116, HZ47, HZ23, HZ12, HZ64, gyroBandwidth };
    }
    
    GyroBandwidth(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 3);
    }
  }
  
  public enum GyroPowerMode {
    ADVANCED(0),
    DEEP(0),
    FAST(0),
    NORMAL(0),
    SUSPEND(0);
    
    public final byte bVal;
    
    static {
      DEEP = new GyroPowerMode("DEEP", 2, 2);
      SUSPEND = new GyroPowerMode("SUSPEND", 3, 3);
      GyroPowerMode gyroPowerMode = new GyroPowerMode("ADVANCED", 4, 4);
      ADVANCED = gyroPowerMode;
      $VALUES = new GyroPowerMode[] { NORMAL, FAST, DEEP, SUSPEND, gyroPowerMode };
    }
    
    GyroPowerMode(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 0);
    }
  }
  
  public enum GyroRange {
    DPS1000(0),
    DPS125(0),
    DPS2000(0),
    DPS250(0),
    DPS500(0);
    
    public final byte bVal;
    
    static {
      DPS250 = new GyroRange("DPS250", 3, 3);
      GyroRange gyroRange = new GyroRange("DPS125", 4, 4);
      DPS125 = gyroRange;
      $VALUES = new GyroRange[] { DPS2000, DPS1000, DPS500, DPS250, gyroRange };
    }
    
    GyroRange(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 0);
    }
  }
  
  public enum MagOpMode {
    ENHANCED(0),
    HIGH(0),
    LOW(0),
    REGULAR(1);
    
    public final byte bVal;
    
    static {
      MagOpMode magOpMode = new MagOpMode("HIGH", 3, 3);
      HIGH = magOpMode;
      $VALUES = new MagOpMode[] { LOW, REGULAR, ENHANCED, magOpMode };
    }
    
    MagOpMode(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 3);
    }
  }
  
  public enum MagPowerMode {
    NORMAL(0),
    FORCE(1),
    SLEEP(1),
    SUSPEND(2);
    
    public final byte bVal;
    
    static {
      MagPowerMode magPowerMode = new MagPowerMode("FORCE", 3, 3);
      FORCE = magPowerMode;
      $VALUES = new MagPowerMode[] { NORMAL, SLEEP, SUSPEND, magPowerMode };
    }
    
    MagPowerMode(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 5);
    }
  }
  
  public enum MagRate {
    HZ2(0),
    HZ20(0),
    HZ25(0),
    HZ30(0),
    HZ6(1),
    HZ10(2),
    HZ15(2),
    HZ8(2);
    
    public final byte bVal;
    
    static {
      MagRate magRate = new MagRate("HZ30", 7, 7);
      HZ30 = magRate;
      $VALUES = new MagRate[] { HZ2, HZ6, HZ8, HZ10, HZ15, HZ20, HZ25, magRate };
    }
    
    MagRate(int param1Int1) {
      this.bVal = (byte)(param1Int1 << 0);
    }
  }
  
  public static class Parameters implements Cloneable {
    public BNO055IMU.AccelBandwidth accelBandwidth = BNO055IMU.AccelBandwidth.HZ62_5;
    
    public BNO055IMU.AccelPowerMode accelPowerMode = BNO055IMU.AccelPowerMode.NORMAL;
    
    public BNO055IMU.AccelRange accelRange = BNO055IMU.AccelRange.G4;
    
    public BNO055IMU.AccelUnit accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    
    public BNO055IMU.AccelerationIntegrator accelerationIntegrationAlgorithm = null;
    
    public BNO055IMU.AngleUnit angleUnit = BNO055IMU.AngleUnit.RADIANS;
    
    public BNO055IMU.CalibrationData calibrationData = null;
    
    public String calibrationDataFile = null;
    
    public BNO055IMU.GyroBandwidth gyroBandwidth = BNO055IMU.GyroBandwidth.HZ32;
    
    public BNO055IMU.GyroPowerMode gyroPowerMode = BNO055IMU.GyroPowerMode.NORMAL;
    
    public BNO055IMU.GyroRange gyroRange = BNO055IMU.GyroRange.DPS2000;
    
    public I2cAddr i2cAddr = BNO055IMU.I2CADDR_DEFAULT;
    
    public boolean loggingEnabled = false;
    
    public String loggingTag = "AdaFruitIMU";
    
    public BNO055IMU.MagOpMode magOpMode = BNO055IMU.MagOpMode.REGULAR;
    
    public BNO055IMU.MagPowerMode magPowerMode = BNO055IMU.MagPowerMode.NORMAL;
    
    public BNO055IMU.MagRate magRate = BNO055IMU.MagRate.HZ10;
    
    public BNO055IMU.SensorMode mode = BNO055IMU.SensorMode.IMU;
    
    public BNO055IMU.PitchMode pitchMode = BNO055IMU.PitchMode.ANDROID;
    
    public BNO055IMU.TempUnit temperatureUnit = BNO055IMU.TempUnit.CELSIUS;
    
    public boolean useExternalCrystal = true;
    
    public Parameters clone() {
      try {
        BNO055IMU.CalibrationData calibrationData;
        Parameters parameters = (Parameters)super.clone();
        if (parameters.calibrationData == null) {
          calibrationData = null;
        } else {
          calibrationData = parameters.calibrationData.clone();
        } 
        parameters.calibrationData = calibrationData;
        return parameters;
      } catch (CloneNotSupportedException cloneNotSupportedException) {
        throw new RuntimeException("internal error: Parameters can't be cloned");
      } 
    }
  }
  
  public enum PitchMode {
    WINDOWS(0),
    ANDROID(2);
    
    public final byte bVal;
    
    static {
      PitchMode pitchMode = new PitchMode("ANDROID", 1, 1);
      ANDROID = pitchMode;
      $VALUES = new PitchMode[] { WINDOWS, pitchMode };
    }
    
    PitchMode(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum Register {
    ACC_AM_THRES(0),
    ACC_CONFIG(0),
    ACC_DATA_X_LSB(0),
    ACC_DATA_X_MSB(0),
    ACC_DATA_Y_LSB(0),
    ACC_DATA_Y_MSB(0),
    ACC_DATA_Z_LSB(0),
    ACC_DATA_Z_MSB(0),
    ACC_HG_DURATION(0),
    ACC_HG_THRES(0),
    ACC_ID(0),
    ACC_INT_SETTINGS(0),
    ACC_NM_SET(0),
    ACC_NM_THRES(0),
    ACC_OFFSET_X_LSB(0),
    ACC_OFFSET_X_MSB(0),
    ACC_OFFSET_Y_LSB(0),
    ACC_OFFSET_Y_MSB(0),
    ACC_OFFSET_Z_LSB(0),
    ACC_OFFSET_Z_MSB(0),
    ACC_RADIUS_LSB(0),
    ACC_RADIUS_MSB(0),
    ACC_SLEEP_CONFIG(0),
    AXIS_MAP_CONFIG(0),
    AXIS_MAP_SIGN(0),
    BL_REV_ID(0),
    CALIB_STAT(0),
    CHIP_ID(0),
    DATA_SELECT(0),
    EUL_H_LSB(0),
    EUL_H_MSB(0),
    EUL_P_LSB(0),
    EUL_P_MSB(0),
    EUL_R_LSB(0),
    EUL_R_MSB(0),
    GRV_DATA_X_LSB(0),
    GRV_DATA_X_MSB(0),
    GRV_DATA_Y_LSB(0),
    GRV_DATA_Y_MSB(0),
    GRV_DATA_Z_LSB(0),
    GRV_DATA_Z_MSB(0),
    GRYO_AM_SET(0),
    GRYO_AM_THRES(0),
    GRYO_DUR_X(0),
    GRYO_DUR_Y(0),
    GRYO_DUR_Z(0),
    GRYO_HR_X_SET(0),
    GRYO_HR_Y_SET(0),
    GRYO_HR_Z_SET(0),
    GRYO_INT_SETTING(0),
    GYR_CONFIG_0(0),
    GYR_CONFIG_1(0),
    GYR_DATA_X_LSB(0),
    GYR_DATA_X_MSB(0),
    GYR_DATA_Y_LSB(0),
    GYR_DATA_Y_MSB(0),
    GYR_DATA_Z_LSB(0),
    GYR_DATA_Z_MSB(0),
    GYR_ID(0),
    GYR_OFFSET_X_LSB(0),
    GYR_OFFSET_X_MSB(0),
    GYR_OFFSET_Y_LSB(0),
    GYR_OFFSET_Y_MSB(0),
    GYR_OFFSET_Z_LSB(0),
    GYR_OFFSET_Z_MSB(0),
    GYR_SLEEP_CONFIG(0),
    INTR_STAT(0),
    INT_EN(0),
    INT_MSK(0),
    LIA_DATA_X_LSB(0),
    LIA_DATA_X_MSB(0),
    LIA_DATA_Y_LSB(0),
    LIA_DATA_Y_MSB(0),
    LIA_DATA_Z_LSB(0),
    LIA_DATA_Z_MSB(0),
    MAG_CONFIG(0),
    MAG_DATA_X_LSB(0),
    MAG_DATA_X_MSB(0),
    MAG_DATA_Y_LSB(0),
    MAG_DATA_Y_MSB(0),
    MAG_DATA_Z_LSB(0),
    MAG_DATA_Z_MSB(0),
    MAG_ID(0),
    MAG_OFFSET_X_LSB(0),
    MAG_OFFSET_X_MSB(0),
    MAG_OFFSET_Y_LSB(0),
    MAG_OFFSET_Y_MSB(0),
    MAG_OFFSET_Z_LSB(0),
    MAG_OFFSET_Z_MSB(0),
    MAG_RADIUS_LSB(0),
    MAG_RADIUS_MSB(0),
    OPR_MODE(0),
    PAGE_ID(7),
    PWR_MODE(7),
    QUA_DATA_W_LSB(7),
    QUA_DATA_W_MSB(7),
    QUA_DATA_X_LSB(7),
    QUA_DATA_X_MSB(7),
    QUA_DATA_Y_LSB(7),
    QUA_DATA_Y_MSB(7),
    QUA_DATA_Z_LSB(7),
    QUA_DATA_Z_MSB(7),
    SELFTEST_RESULT(7),
    SIC_MATRIX_0_LSB(7),
    SIC_MATRIX_0_MSB(7),
    SIC_MATRIX_1_LSB(7),
    SIC_MATRIX_1_MSB(7),
    SIC_MATRIX_2_LSB(7),
    SIC_MATRIX_2_MSB(7),
    SIC_MATRIX_3_LSB(7),
    SIC_MATRIX_3_MSB(7),
    SIC_MATRIX_4_LSB(7),
    SIC_MATRIX_4_MSB(7),
    SIC_MATRIX_5_LSB(7),
    SIC_MATRIX_5_MSB(7),
    SIC_MATRIX_6_LSB(7),
    SIC_MATRIX_6_MSB(7),
    SIC_MATRIX_7_LSB(7),
    SIC_MATRIX_7_MSB(7),
    SIC_MATRIX_8_LSB(7),
    SIC_MATRIX_8_MSB(7),
    SW_REV_ID_LSB(7),
    SW_REV_ID_MSB(7),
    SYS_CLK_STAT(7),
    SYS_ERR(7),
    SYS_STAT(7),
    SYS_TRIGGER(7),
    TEMP(7),
    TEMP_SOURCE(7),
    UNIQUE_ID_FIRST(7),
    UNIQUE_ID_LAST(7),
    UNIT_SEL(7);
    
    public final byte bVal;
    
    static {
      ACC_ID = new Register("ACC_ID", 2, 1);
      MAG_ID = new Register("MAG_ID", 3, 2);
      GYR_ID = new Register("GYR_ID", 4, 3);
      SW_REV_ID_LSB = new Register("SW_REV_ID_LSB", 5, 4);
      SW_REV_ID_MSB = new Register("SW_REV_ID_MSB", 6, 5);
      BL_REV_ID = new Register("BL_REV_ID", 7, 6);
      ACC_DATA_X_LSB = new Register("ACC_DATA_X_LSB", 8, 8);
      ACC_DATA_X_MSB = new Register("ACC_DATA_X_MSB", 9, 9);
      ACC_DATA_Y_LSB = new Register("ACC_DATA_Y_LSB", 10, 10);
      ACC_DATA_Y_MSB = new Register("ACC_DATA_Y_MSB", 11, 11);
      ACC_DATA_Z_LSB = new Register("ACC_DATA_Z_LSB", 12, 12);
      ACC_DATA_Z_MSB = new Register("ACC_DATA_Z_MSB", 13, 13);
      MAG_DATA_X_LSB = new Register("MAG_DATA_X_LSB", 14, 14);
      MAG_DATA_X_MSB = new Register("MAG_DATA_X_MSB", 15, 15);
      MAG_DATA_Y_LSB = new Register("MAG_DATA_Y_LSB", 16, 16);
      MAG_DATA_Y_MSB = new Register("MAG_DATA_Y_MSB", 17, 17);
      MAG_DATA_Z_LSB = new Register("MAG_DATA_Z_LSB", 18, 18);
      MAG_DATA_Z_MSB = new Register("MAG_DATA_Z_MSB", 19, 19);
      GYR_DATA_X_LSB = new Register("GYR_DATA_X_LSB", 20, 20);
      GYR_DATA_X_MSB = new Register("GYR_DATA_X_MSB", 21, 21);
      GYR_DATA_Y_LSB = new Register("GYR_DATA_Y_LSB", 22, 22);
      GYR_DATA_Y_MSB = new Register("GYR_DATA_Y_MSB", 23, 23);
      GYR_DATA_Z_LSB = new Register("GYR_DATA_Z_LSB", 24, 24);
      GYR_DATA_Z_MSB = new Register("GYR_DATA_Z_MSB", 25, 25);
      EUL_H_LSB = new Register("EUL_H_LSB", 26, 26);
      EUL_H_MSB = new Register("EUL_H_MSB", 27, 27);
      EUL_R_LSB = new Register("EUL_R_LSB", 28, 28);
      EUL_R_MSB = new Register("EUL_R_MSB", 29, 29);
      EUL_P_LSB = new Register("EUL_P_LSB", 30, 30);
      EUL_P_MSB = new Register("EUL_P_MSB", 31, 31);
      QUA_DATA_W_LSB = new Register("QUA_DATA_W_LSB", 32, 32);
      QUA_DATA_W_MSB = new Register("QUA_DATA_W_MSB", 33, 33);
      QUA_DATA_X_LSB = new Register("QUA_DATA_X_LSB", 34, 34);
      QUA_DATA_X_MSB = new Register("QUA_DATA_X_MSB", 35, 35);
      QUA_DATA_Y_LSB = new Register("QUA_DATA_Y_LSB", 36, 36);
      QUA_DATA_Y_MSB = new Register("QUA_DATA_Y_MSB", 37, 37);
      QUA_DATA_Z_LSB = new Register("QUA_DATA_Z_LSB", 38, 38);
      QUA_DATA_Z_MSB = new Register("QUA_DATA_Z_MSB", 39, 39);
      LIA_DATA_X_LSB = new Register("LIA_DATA_X_LSB", 40, 40);
      LIA_DATA_X_MSB = new Register("LIA_DATA_X_MSB", 41, 41);
      LIA_DATA_Y_LSB = new Register("LIA_DATA_Y_LSB", 42, 42);
      LIA_DATA_Y_MSB = new Register("LIA_DATA_Y_MSB", 43, 43);
      LIA_DATA_Z_LSB = new Register("LIA_DATA_Z_LSB", 44, 44);
      LIA_DATA_Z_MSB = new Register("LIA_DATA_Z_MSB", 45, 45);
      GRV_DATA_X_LSB = new Register("GRV_DATA_X_LSB", 46, 46);
      GRV_DATA_X_MSB = new Register("GRV_DATA_X_MSB", 47, 47);
      GRV_DATA_Y_LSB = new Register("GRV_DATA_Y_LSB", 48, 48);
      GRV_DATA_Y_MSB = new Register("GRV_DATA_Y_MSB", 49, 49);
      GRV_DATA_Z_LSB = new Register("GRV_DATA_Z_LSB", 50, 50);
      GRV_DATA_Z_MSB = new Register("GRV_DATA_Z_MSB", 51, 51);
      TEMP = new Register("TEMP", 52, 52);
      CALIB_STAT = new Register("CALIB_STAT", 53, 53);
      SELFTEST_RESULT = new Register("SELFTEST_RESULT", 54, 54);
      INTR_STAT = new Register("INTR_STAT", 55, 55);
      SYS_CLK_STAT = new Register("SYS_CLK_STAT", 56, 56);
      SYS_STAT = new Register("SYS_STAT", 57, 57);
      SYS_ERR = new Register("SYS_ERR", 58, 58);
      UNIT_SEL = new Register("UNIT_SEL", 59, 59);
      DATA_SELECT = new Register("DATA_SELECT", 60, 60);
      OPR_MODE = new Register("OPR_MODE", 61, 61);
      PWR_MODE = new Register("PWR_MODE", 62, 62);
      SYS_TRIGGER = new Register("SYS_TRIGGER", 63, 63);
      TEMP_SOURCE = new Register("TEMP_SOURCE", 64, 64);
      AXIS_MAP_CONFIG = new Register("AXIS_MAP_CONFIG", 65, 65);
      AXIS_MAP_SIGN = new Register("AXIS_MAP_SIGN", 66, 66);
      SIC_MATRIX_0_LSB = new Register("SIC_MATRIX_0_LSB", 67, 67);
      SIC_MATRIX_0_MSB = new Register("SIC_MATRIX_0_MSB", 68, 68);
      SIC_MATRIX_1_LSB = new Register("SIC_MATRIX_1_LSB", 69, 69);
      SIC_MATRIX_1_MSB = new Register("SIC_MATRIX_1_MSB", 70, 70);
      SIC_MATRIX_2_LSB = new Register("SIC_MATRIX_2_LSB", 71, 71);
      SIC_MATRIX_2_MSB = new Register("SIC_MATRIX_2_MSB", 72, 72);
      SIC_MATRIX_3_LSB = new Register("SIC_MATRIX_3_LSB", 73, 73);
      SIC_MATRIX_3_MSB = new Register("SIC_MATRIX_3_MSB", 74, 74);
      SIC_MATRIX_4_LSB = new Register("SIC_MATRIX_4_LSB", 75, 75);
      SIC_MATRIX_4_MSB = new Register("SIC_MATRIX_4_MSB", 76, 76);
      SIC_MATRIX_5_LSB = new Register("SIC_MATRIX_5_LSB", 77, 77);
      SIC_MATRIX_5_MSB = new Register("SIC_MATRIX_5_MSB", 78, 78);
      SIC_MATRIX_6_LSB = new Register("SIC_MATRIX_6_LSB", 79, 79);
      SIC_MATRIX_6_MSB = new Register("SIC_MATRIX_6_MSB", 80, 80);
      SIC_MATRIX_7_LSB = new Register("SIC_MATRIX_7_LSB", 81, 81);
      SIC_MATRIX_7_MSB = new Register("SIC_MATRIX_7_MSB", 82, 82);
      SIC_MATRIX_8_LSB = new Register("SIC_MATRIX_8_LSB", 83, 83);
      SIC_MATRIX_8_MSB = new Register("SIC_MATRIX_8_MSB", 84, 84);
      ACC_OFFSET_X_LSB = new Register("ACC_OFFSET_X_LSB", 85, 85);
      ACC_OFFSET_X_MSB = new Register("ACC_OFFSET_X_MSB", 86, 86);
      ACC_OFFSET_Y_LSB = new Register("ACC_OFFSET_Y_LSB", 87, 87);
      ACC_OFFSET_Y_MSB = new Register("ACC_OFFSET_Y_MSB", 88, 88);
      ACC_OFFSET_Z_LSB = new Register("ACC_OFFSET_Z_LSB", 89, 89);
      ACC_OFFSET_Z_MSB = new Register("ACC_OFFSET_Z_MSB", 90, 90);
      MAG_OFFSET_X_LSB = new Register("MAG_OFFSET_X_LSB", 91, 91);
      MAG_OFFSET_X_MSB = new Register("MAG_OFFSET_X_MSB", 92, 92);
      MAG_OFFSET_Y_LSB = new Register("MAG_OFFSET_Y_LSB", 93, 93);
      MAG_OFFSET_Y_MSB = new Register("MAG_OFFSET_Y_MSB", 94, 94);
      MAG_OFFSET_Z_LSB = new Register("MAG_OFFSET_Z_LSB", 95, 95);
      MAG_OFFSET_Z_MSB = new Register("MAG_OFFSET_Z_MSB", 96, 96);
      GYR_OFFSET_X_LSB = new Register("GYR_OFFSET_X_LSB", 97, 97);
      GYR_OFFSET_X_MSB = new Register("GYR_OFFSET_X_MSB", 98, 98);
      GYR_OFFSET_Y_LSB = new Register("GYR_OFFSET_Y_LSB", 99, 99);
      GYR_OFFSET_Y_MSB = new Register("GYR_OFFSET_Y_MSB", 100, 100);
      GYR_OFFSET_Z_LSB = new Register("GYR_OFFSET_Z_LSB", 101, 101);
      GYR_OFFSET_Z_MSB = new Register("GYR_OFFSET_Z_MSB", 102, 102);
      ACC_RADIUS_LSB = new Register("ACC_RADIUS_LSB", 103, 103);
      ACC_RADIUS_MSB = new Register("ACC_RADIUS_MSB", 104, 104);
      MAG_RADIUS_LSB = new Register("MAG_RADIUS_LSB", 105, 105);
      MAG_RADIUS_MSB = new Register("MAG_RADIUS_MSB", 106, 106);
      ACC_CONFIG = new Register("ACC_CONFIG", 107, 8);
      MAG_CONFIG = new Register("MAG_CONFIG", 108, 9);
      GYR_CONFIG_0 = new Register("GYR_CONFIG_0", 109, 10);
      GYR_CONFIG_1 = new Register("GYR_CONFIG_1", 110, 11);
      ACC_SLEEP_CONFIG = new Register("ACC_SLEEP_CONFIG", 111, 12);
      GYR_SLEEP_CONFIG = new Register("GYR_SLEEP_CONFIG", 112, 13);
      INT_MSK = new Register("INT_MSK", 113, 15);
      INT_EN = new Register("INT_EN", 114, 16);
      ACC_AM_THRES = new Register("ACC_AM_THRES", 115, 17);
      ACC_INT_SETTINGS = new Register("ACC_INT_SETTINGS", 116, 18);
      ACC_HG_DURATION = new Register("ACC_HG_DURATION", 117, 19);
      ACC_HG_THRES = new Register("ACC_HG_THRES", 118, 20);
      ACC_NM_THRES = new Register("ACC_NM_THRES", 119, 21);
      ACC_NM_SET = new Register("ACC_NM_SET", 120, 22);
      GRYO_INT_SETTING = new Register("GRYO_INT_SETTING", 121, 23);
      GRYO_HR_X_SET = new Register("GRYO_HR_X_SET", 122, 24);
      GRYO_DUR_X = new Register("GRYO_DUR_X", 123, 25);
      GRYO_HR_Y_SET = new Register("GRYO_HR_Y_SET", 124, 26);
      GRYO_DUR_Y = new Register("GRYO_DUR_Y", 125, 27);
      GRYO_HR_Z_SET = new Register("GRYO_HR_Z_SET", 126, 28);
      GRYO_DUR_Z = new Register("GRYO_DUR_Z", 127, 29);
      GRYO_AM_THRES = new Register("GRYO_AM_THRES", 128, 30);
      GRYO_AM_SET = new Register("GRYO_AM_SET", 129, 31);
      UNIQUE_ID_FIRST = new Register("UNIQUE_ID_FIRST", 130, 80);
      Register register = new Register("UNIQUE_ID_LAST", 131, 95);
      UNIQUE_ID_LAST = register;
      $VALUES = new Register[] { 
          PAGE_ID, CHIP_ID, ACC_ID, MAG_ID, GYR_ID, SW_REV_ID_LSB, SW_REV_ID_MSB, BL_REV_ID, ACC_DATA_X_LSB, ACC_DATA_X_MSB, 
          ACC_DATA_Y_LSB, ACC_DATA_Y_MSB, ACC_DATA_Z_LSB, ACC_DATA_Z_MSB, MAG_DATA_X_LSB, MAG_DATA_X_MSB, MAG_DATA_Y_LSB, MAG_DATA_Y_MSB, MAG_DATA_Z_LSB, MAG_DATA_Z_MSB, 
          GYR_DATA_X_LSB, GYR_DATA_X_MSB, GYR_DATA_Y_LSB, GYR_DATA_Y_MSB, GYR_DATA_Z_LSB, GYR_DATA_Z_MSB, EUL_H_LSB, EUL_H_MSB, EUL_R_LSB, EUL_R_MSB, 
          EUL_P_LSB, EUL_P_MSB, QUA_DATA_W_LSB, QUA_DATA_W_MSB, QUA_DATA_X_LSB, QUA_DATA_X_MSB, QUA_DATA_Y_LSB, QUA_DATA_Y_MSB, QUA_DATA_Z_LSB, QUA_DATA_Z_MSB, 
          LIA_DATA_X_LSB, LIA_DATA_X_MSB, LIA_DATA_Y_LSB, LIA_DATA_Y_MSB, LIA_DATA_Z_LSB, LIA_DATA_Z_MSB, GRV_DATA_X_LSB, GRV_DATA_X_MSB, GRV_DATA_Y_LSB, GRV_DATA_Y_MSB, 
          GRV_DATA_Z_LSB, GRV_DATA_Z_MSB, TEMP, CALIB_STAT, SELFTEST_RESULT, INTR_STAT, SYS_CLK_STAT, SYS_STAT, SYS_ERR, UNIT_SEL, 
          DATA_SELECT, OPR_MODE, PWR_MODE, SYS_TRIGGER, TEMP_SOURCE, AXIS_MAP_CONFIG, AXIS_MAP_SIGN, SIC_MATRIX_0_LSB, SIC_MATRIX_0_MSB, SIC_MATRIX_1_LSB, 
          SIC_MATRIX_1_MSB, SIC_MATRIX_2_LSB, SIC_MATRIX_2_MSB, SIC_MATRIX_3_LSB, SIC_MATRIX_3_MSB, SIC_MATRIX_4_LSB, SIC_MATRIX_4_MSB, SIC_MATRIX_5_LSB, SIC_MATRIX_5_MSB, SIC_MATRIX_6_LSB, 
          SIC_MATRIX_6_MSB, SIC_MATRIX_7_LSB, SIC_MATRIX_7_MSB, SIC_MATRIX_8_LSB, SIC_MATRIX_8_MSB, ACC_OFFSET_X_LSB, ACC_OFFSET_X_MSB, ACC_OFFSET_Y_LSB, ACC_OFFSET_Y_MSB, ACC_OFFSET_Z_LSB, 
          ACC_OFFSET_Z_MSB, MAG_OFFSET_X_LSB, MAG_OFFSET_X_MSB, MAG_OFFSET_Y_LSB, MAG_OFFSET_Y_MSB, MAG_OFFSET_Z_LSB, MAG_OFFSET_Z_MSB, GYR_OFFSET_X_LSB, GYR_OFFSET_X_MSB, GYR_OFFSET_Y_LSB, 
          GYR_OFFSET_Y_MSB, GYR_OFFSET_Z_LSB, GYR_OFFSET_Z_MSB, ACC_RADIUS_LSB, ACC_RADIUS_MSB, MAG_RADIUS_LSB, MAG_RADIUS_MSB, ACC_CONFIG, MAG_CONFIG, GYR_CONFIG_0, 
          GYR_CONFIG_1, ACC_SLEEP_CONFIG, GYR_SLEEP_CONFIG, INT_MSK, INT_EN, ACC_AM_THRES, ACC_INT_SETTINGS, ACC_HG_DURATION, ACC_HG_THRES, ACC_NM_THRES, 
          ACC_NM_SET, GRYO_INT_SETTING, GRYO_HR_X_SET, GRYO_DUR_X, GRYO_HR_Y_SET, GRYO_DUR_Y, GRYO_HR_Z_SET, GRYO_DUR_Z, GRYO_AM_THRES, GRYO_AM_SET, 
          UNIQUE_ID_FIRST, register };
    }
    
    Register(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
  }
  
  public enum SensorMode {
    ACCGYRO(7),
    ACCMAG(7),
    ACCONLY(7),
    AMG(7),
    COMPASS(7),
    CONFIG(0),
    DISABLED(0),
    GYRONLY(0),
    IMU(0),
    M4G(0),
    MAGGYRO(0),
    MAGONLY(0),
    NDOF(0),
    NDOF_FMC_OFF(0);
    
    public final byte bVal;
    
    static {
      GYRONLY = new SensorMode("GYRONLY", 3, 3);
      ACCMAG = new SensorMode("ACCMAG", 4, 4);
      ACCGYRO = new SensorMode("ACCGYRO", 5, 5);
      MAGGYRO = new SensorMode("MAGGYRO", 6, 6);
      AMG = new SensorMode("AMG", 7, 7);
      IMU = new SensorMode("IMU", 8, 8);
      COMPASS = new SensorMode("COMPASS", 9, 9);
      M4G = new SensorMode("M4G", 10, 10);
      NDOF_FMC_OFF = new SensorMode("NDOF_FMC_OFF", 11, 11);
      NDOF = new SensorMode("NDOF", 12, 12);
      SensorMode sensorMode = new SensorMode("DISABLED", 13, -1);
      DISABLED = sensorMode;
      $VALUES = new SensorMode[] { 
          CONFIG, ACCONLY, MAGONLY, GYRONLY, ACCMAG, ACCGYRO, MAGGYRO, AMG, IMU, COMPASS, 
          M4G, NDOF_FMC_OFF, NDOF, sensorMode };
    }
    
    SensorMode(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public boolean isFusionMode() {
      int i = BNO055IMU.null.$SwitchMap$com$qualcomm$hardware$bosch$BNO055IMU$SensorMode[ordinal()];
      return !(i != 1 && i != 2 && i != 3 && i != 4 && i != 5);
    }
  }
  
  public enum SystemError {
    ACCELEROMETER_POWER_MODE_NOT_AVAILABLE(0),
    FUSION_CONFIGURATION_ERROR(0),
    LOW_POWER_MODE_NOT_AVAILABLE(0),
    NO_ERROR(0),
    PERIPHERAL_INITIALIZATION_ERROR(0),
    REGISTER_MAP_ADDRESS_OUT_OF_RANGE(0),
    REGISTER_MAP_OUT_OF_RANGE(0),
    REGISTER_MAP_WRITE_ERROR(0),
    SELF_TEST_FAILED(0),
    SENSOR_CONFIGURATION_ERROR(0),
    SYSTEM_INITIALIZATION_ERROR(0),
    UNKNOWN(-1);
    
    public final byte bVal;
    
    static {
      SELF_TEST_FAILED = new SystemError("SELF_TEST_FAILED", 4, 3);
      REGISTER_MAP_OUT_OF_RANGE = new SystemError("REGISTER_MAP_OUT_OF_RANGE", 5, 4);
      REGISTER_MAP_ADDRESS_OUT_OF_RANGE = new SystemError("REGISTER_MAP_ADDRESS_OUT_OF_RANGE", 6, 5);
      REGISTER_MAP_WRITE_ERROR = new SystemError("REGISTER_MAP_WRITE_ERROR", 7, 6);
      LOW_POWER_MODE_NOT_AVAILABLE = new SystemError("LOW_POWER_MODE_NOT_AVAILABLE", 8, 7);
      ACCELEROMETER_POWER_MODE_NOT_AVAILABLE = new SystemError("ACCELEROMETER_POWER_MODE_NOT_AVAILABLE", 9, 8);
      FUSION_CONFIGURATION_ERROR = new SystemError("FUSION_CONFIGURATION_ERROR", 10, 9);
      SystemError systemError = new SystemError("SENSOR_CONFIGURATION_ERROR", 11, 10);
      SENSOR_CONFIGURATION_ERROR = systemError;
      $VALUES = new SystemError[] { 
          UNKNOWN, NO_ERROR, PERIPHERAL_INITIALIZATION_ERROR, SYSTEM_INITIALIZATION_ERROR, SELF_TEST_FAILED, REGISTER_MAP_OUT_OF_RANGE, REGISTER_MAP_ADDRESS_OUT_OF_RANGE, REGISTER_MAP_WRITE_ERROR, LOW_POWER_MODE_NOT_AVAILABLE, ACCELEROMETER_POWER_MODE_NOT_AVAILABLE, 
          FUSION_CONFIGURATION_ERROR, systemError };
    }
    
    SystemError(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static SystemError from(int param1Int) {
      for (SystemError systemError : values()) {
        if (systemError.bVal == param1Int)
          return systemError; 
      } 
      return UNKNOWN;
    }
  }
  
  public enum SystemStatus {
    IDLE(-1),
    INITIALIZING_PERIPHERALS(-1),
    RUNNING_FUSION(-1),
    RUNNING_NO_FUSION(-1),
    SELF_TEST(-1),
    SYSTEM_ERROR(-1),
    SYSTEM_INITIALIZATION(-1),
    UNKNOWN(-1);
    
    public final byte bVal;
    
    static {
      INITIALIZING_PERIPHERALS = new SystemStatus("INITIALIZING_PERIPHERALS", 3, 2);
      SYSTEM_INITIALIZATION = new SystemStatus("SYSTEM_INITIALIZATION", 4, 3);
      SELF_TEST = new SystemStatus("SELF_TEST", 5, 4);
      RUNNING_FUSION = new SystemStatus("RUNNING_FUSION", 6, 5);
      SystemStatus systemStatus = new SystemStatus("RUNNING_NO_FUSION", 7, 6);
      RUNNING_NO_FUSION = systemStatus;
      $VALUES = new SystemStatus[] { UNKNOWN, IDLE, SYSTEM_ERROR, INITIALIZING_PERIPHERALS, SYSTEM_INITIALIZATION, SELF_TEST, RUNNING_FUSION, systemStatus };
    }
    
    SystemStatus(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static SystemStatus from(int param1Int) {
      for (SystemStatus systemStatus : values()) {
        if (systemStatus.bVal == param1Int)
          return systemStatus; 
      } 
      return UNKNOWN;
    }
    
    public String toShortString() {
      switch (this) {
        default:
          return "unk";
        case null:
          return "running";
        case null:
          return "fusion";
        case null:
          return "selftest";
        case null:
          return "sysinit";
        case null:
          return "periph";
        case null:
          return "syserr";
        case null:
          break;
      } 
      return "idle";
    }
  }
  
  public enum TempUnit {
    CELSIUS(0),
    FARENHEIT(0);
    
    public final byte bVal;
    
    static {
      TempUnit tempUnit = new TempUnit("FARENHEIT", 1, 1);
      FARENHEIT = tempUnit;
      $VALUES = new TempUnit[] { CELSIUS, tempUnit };
    }
    
    TempUnit(int param1Int1) {
      this.bVal = (byte)param1Int1;
    }
    
    public static TempUnit fromTempUnit(org.firstinspires.ftc.robotcore.external.navigation.TempUnit param1TempUnit) {
      if (param1TempUnit == org.firstinspires.ftc.robotcore.external.navigation.TempUnit.CELSIUS)
        return CELSIUS; 
      if (param1TempUnit == org.firstinspires.ftc.robotcore.external.navigation.TempUnit.FARENHEIT)
        return FARENHEIT; 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("TempUnit.");
      stringBuilder.append(param1TempUnit);
      stringBuilder.append(" is not supported by BNO055IMU");
      throw new UnsupportedOperationException(stringBuilder.toString());
    }
    
    public org.firstinspires.ftc.robotcore.external.navigation.TempUnit toTempUnit() {
      return (this == CELSIUS) ? org.firstinspires.ftc.robotcore.external.navigation.TempUnit.CELSIUS : org.firstinspires.ftc.robotcore.external.navigation.TempUnit.FARENHEIT;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\bosch\BNO055IMU.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */