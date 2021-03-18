package com.qualcomm.hardware.stmicroelectronics;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDeviceHealth;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class VL53L0X extends I2cDeviceSynchDevice<I2cDeviceSynch> implements DistanceSensor {
  public static final I2cAddr ADDRESS_I2C_DEFAULT = I2cAddr.create8bit(82);
  
  protected static final int FAKE_DISTANCE_MM = 65535;
  
  protected String MYTAG = "STMicroVL53L0X: ";
  
  boolean assume_uninitialized = true;
  
  boolean did_timeout = false;
  
  protected ElapsedTime ioElapsedTime;
  
  protected int io_timeout = 0;
  
  long measurement_timing_budget_us;
  
  private byte spad_count;
  
  private boolean spad_type_is_aperture;
  
  private byte stop_variable = 0;
  
  public VL53L0X(I2cDeviceSynch paramI2cDeviceSynch) {
    super((I2cDeviceSynchSimple)paramI2cDeviceSynch, true);
    ((I2cDeviceSynch)this.deviceClient).setI2cAddress(ADDRESS_I2C_DEFAULT);
    registerArmingStateCallback(false);
    ((I2cDeviceSynch)this.deviceClient).engage();
    this.ioElapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
    this.did_timeout = false;
  }
  
  private float getSignalRateLimit() {
    return readShort(Register.FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT) / 128.0F;
  }
  
  private boolean getSpadInfo() {
    boolean bool;
    writeReg(128, 1);
    writeReg(255, 1);
    writeReg(0, 0);
    writeReg(255, 6);
    writeReg(131, (byte)(((I2cDeviceSynch)this.deviceClient).read8(131) | 0x4));
    writeReg(255, 7);
    writeReg(129, 1);
    writeReg(128, 1);
    writeReg(148, 107);
    writeReg(131, 0);
    writeReg(131, 1);
    byte b = readReg(146);
    this.spad_count = (byte)(b & Byte.MAX_VALUE);
    if ((b >> 7 & 0x1) == 0) {
      bool = false;
    } else {
      bool = true;
    } 
    this.spad_type_is_aperture = bool;
    writeReg(129, 0);
    writeReg(255, 6);
    writeReg(131, readReg(131) & 0xFFFFFFFB);
    writeReg(255, 1);
    writeReg(0, 1);
    writeReg(255, 0);
    writeReg(128, 0);
    return true;
  }
  
  private boolean initVL53L0X(boolean paramBoolean) {
    if (paramBoolean)
      writeReg(Register.VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV, (byte)(readReg(Register.VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV) | 0x1)); 
    writeReg(136, 0);
    writeReg(128, 1);
    writeReg(255, 1);
    writeReg(0, 0);
    this.stop_variable = ((I2cDeviceSynch)this.deviceClient).read8(145);
    writeReg(0, 1);
    writeReg(255, 0);
    writeReg(128, 0);
    writeReg(Register.MSRC_CONFIG_CONTROL, (byte)(readReg(Register.MSRC_CONFIG_CONTROL) | 0x12));
    RobotLog.dd(this.MYTAG, "initial sig rate lim (MCPS) %.06f", new Object[] { Float.valueOf(getSignalRateLimit()) });
    setSignalRateLimit(0.25F);
    RobotLog.dd(this.MYTAG, "adjusted sig rate lim (MCPS) %.06f", new Object[] { Float.valueOf(getSignalRateLimit()) });
    writeReg(Register.SYSTEM_SEQUENCE_CONFIG, (byte)-1);
    if (!getSpadInfo())
      return false; 
    byte[] arrayOfByte = ((I2cDeviceSynch)this.deviceClient).read(Register.GLOBAL_CONFIG_SPAD_ENABLES_REF_0.bVal, 6);
    writeReg(255, 1);
    writeReg(Register.DYNAMIC_SPAD_REF_EN_START_OFFSET.bVal, 0);
    writeReg(Register.DYNAMIC_SPAD_NUM_REQUESTED_REF_SPAD.bVal, 44);
    writeReg(255, 0);
    writeReg(Register.GLOBAL_CONFIG_REF_EN_START_SELECT.bVal, 180);
    if (this.spad_type_is_aperture) {
      b1 = 12;
    } else {
      b1 = 0;
    } 
    byte b = (byte)b1;
    byte b1 = 0;
    for (byte b2 = b1; b1 < 48; b2 = b3) {
      byte b3;
      if (b1 < b || b2 == this.spad_count) {
        b3 = b1 / 8;
        arrayOfByte[b3] = (byte)(arrayOfByte[b3] & 1 << b1 % 8);
        b3 = b2;
      } else {
        b3 = b2;
        if ((arrayOfByte[b1 / 8] >> b1 % 8 & 0x1) != 0)
          b3 = (byte)(b2 + 1); 
      } 
      b1 = (byte)(b1 + 1);
    } 
    ((I2cDeviceSynch)this.deviceClient).write(Register.GLOBAL_CONFIG_SPAD_ENABLES_REF_0.bVal, arrayOfByte);
    writeReg(255, 1);
    writeReg(0, 0);
    writeReg(255, 0);
    writeReg(9, 0);
    writeReg(16, 0);
    writeReg(17, 0);
    writeReg(36, 1);
    writeReg(37, 255);
    writeReg(117, 0);
    writeReg(255, 1);
    writeReg(78, 44);
    writeReg(72, 0);
    writeReg(48, 32);
    writeReg(255, 0);
    writeReg(48, 9);
    writeReg(84, 0);
    writeReg(49, 4);
    writeReg(50, 3);
    writeReg(64, 131);
    writeReg(70, 37);
    writeReg(96, 0);
    writeReg(39, 0);
    writeReg(80, 6);
    writeReg(81, 0);
    writeReg(82, 150);
    writeReg(86, 8);
    writeReg(87, 48);
    writeReg(97, 0);
    writeReg(98, 0);
    writeReg(100, 0);
    writeReg(101, 0);
    writeReg(102, 160);
    writeReg(255, 1);
    writeReg(34, 50);
    writeReg(71, 20);
    writeReg(73, 255);
    writeReg(74, 0);
    writeReg(255, 0);
    writeReg(122, 10);
    writeReg(123, 0);
    writeReg(120, 33);
    writeReg(255, 1);
    writeReg(35, 52);
    writeReg(66, 0);
    writeReg(68, 255);
    writeReg(69, 38);
    writeReg(70, 5);
    writeReg(64, 64);
    writeReg(14, 6);
    writeReg(32, 26);
    writeReg(67, 64);
    writeReg(255, 0);
    writeReg(52, 3);
    writeReg(53, 68);
    writeReg(255, 1);
    writeReg(49, 4);
    writeReg(75, 9);
    writeReg(76, 5);
    writeReg(77, 4);
    writeReg(255, 0);
    writeReg(68, 0);
    writeReg(69, 32);
    writeReg(71, 8);
    writeReg(72, 40);
    writeReg(103, 0);
    writeReg(112, 4);
    writeReg(113, 1);
    writeReg(114, 254);
    writeReg(118, 0);
    writeReg(119, 0);
    writeReg(255, 1);
    writeReg(13, 1);
    writeReg(255, 0);
    writeReg(128, 1);
    writeReg(1, 248);
    writeReg(255, 1);
    writeReg(142, 1);
    writeReg(0, 1);
    writeReg(255, 0);
    writeReg(128, 0);
    writeReg(Register.SYSTEM_INTERRUPT_CONFIG_GPIO.bVal, 4);
    writeReg(Register.GPIO_HV_MUX_ACTIVE_HIGH.bVal, readReg(Register.GPIO_HV_MUX_ACTIVE_HIGH) & 0xFFFFFFEF);
    writeReg(Register.SYSTEM_INTERRUPT_CLEAR.bVal, 1);
    this.measurement_timing_budget_us = getMeasurementTimingBudget();
    writeReg(Register.SYSTEM_SEQUENCE_CONFIG.bVal, 232);
    setMeasurementTimingBudget(this.measurement_timing_budget_us);
    writeReg(Register.SYSTEM_SEQUENCE_CONFIG.bVal, 1);
    if (!performSingleRefCalibration(64))
      return false; 
    writeReg(Register.SYSTEM_SEQUENCE_CONFIG.bVal, 2);
    if (!performSingleRefCalibration(0))
      return false; 
    writeReg(Register.SYSTEM_SEQUENCE_CONFIG.bVal, 232);
    this.assume_uninitialized = false;
    setTimeout(200);
    startContinuous();
    return true;
  }
  
  private boolean setSignalRateLimit(float paramFloat) {
    if (paramFloat < 0.0F || paramFloat > 511.99D)
      return false; 
    writeShort(Register.FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT, (short)(int)(paramFloat * 128.0F));
    return true;
  }
  
  protected long calcMacroPeriod(int paramInt) {
    return (paramInt * 2304L * 1655L + 500L) / 1000L;
  }
  
  int decodeTimeout(int paramInt) {
    return ((paramInt & 0xFF) << (paramInt & 0xFF00) >> 8) + 1;
  }
  
  protected int decodeVcselPeriod(int paramInt) {
    return paramInt + 1 << 1;
  }
  
  public boolean didTimeoutOccur() {
    return this.did_timeout;
  }
  
  protected boolean doInitialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield MYTAG : Ljava/lang/String;
    //   6: ldc 'Checking to see if it's really a VL53L0X sensor...'
    //   8: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;)V
    //   11: aload_0
    //   12: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   15: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   18: sipush #192
    //   21: invokeinterface read8 : (I)B
    //   26: istore_1
    //   27: aload_0
    //   28: getfield MYTAG : Ljava/lang/String;
    //   31: ldc 'Reg 0xC0 = %x (should be 0xEE)'
    //   33: iconst_1
    //   34: anewarray java/lang/Object
    //   37: dup
    //   38: iconst_0
    //   39: iload_1
    //   40: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   43: aastore
    //   44: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   47: aload_0
    //   48: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   51: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   54: sipush #193
    //   57: invokeinterface read8 : (I)B
    //   62: istore_1
    //   63: aload_0
    //   64: getfield MYTAG : Ljava/lang/String;
    //   67: ldc_w 'Reg 0xC1 = %x (should be 0xAA)'
    //   70: iconst_1
    //   71: anewarray java/lang/Object
    //   74: dup
    //   75: iconst_0
    //   76: iload_1
    //   77: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   80: aastore
    //   81: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   84: aload_0
    //   85: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   88: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   91: sipush #194
    //   94: invokeinterface read8 : (I)B
    //   99: istore_1
    //   100: aload_0
    //   101: getfield MYTAG : Ljava/lang/String;
    //   104: ldc_w 'Reg 0xC2 = %x (should be 0x10)'
    //   107: iconst_1
    //   108: anewarray java/lang/Object
    //   111: dup
    //   112: iconst_0
    //   113: iload_1
    //   114: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   117: aastore
    //   118: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   121: aload_0
    //   122: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   125: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   128: bipush #81
    //   130: invokeinterface read8 : (I)B
    //   135: istore_1
    //   136: aload_0
    //   137: getfield MYTAG : Ljava/lang/String;
    //   140: ldc_w 'Reg 0x51 = %x (should be 0x0099)'
    //   143: iconst_1
    //   144: anewarray java/lang/Object
    //   147: dup
    //   148: iconst_0
    //   149: iload_1
    //   150: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   153: aastore
    //   154: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   157: aload_0
    //   158: getfield deviceClient : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynchSimple;
    //   161: checkcast com/qualcomm/robotcore/hardware/I2cDeviceSynch
    //   164: bipush #97
    //   166: invokeinterface read8 : (I)B
    //   171: istore_1
    //   172: aload_0
    //   173: getfield MYTAG : Ljava/lang/String;
    //   176: ldc_w 'Reg 0x61 = %x (should be 0x0000)'
    //   179: iconst_1
    //   180: anewarray java/lang/Object
    //   183: dup
    //   184: iconst_0
    //   185: iload_1
    //   186: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   189: aastore
    //   190: invokestatic dd : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   193: aload_0
    //   194: iconst_0
    //   195: invokespecial initVL53L0X : (Z)Z
    //   198: istore_2
    //   199: aload_0
    //   200: monitorexit
    //   201: iload_2
    //   202: ireturn
    //   203: astore_3
    //   204: aload_0
    //   205: monitorexit
    //   206: aload_3
    //   207: athrow
    // Exception table:
    //   from	to	target	type
    //   2	199	203	finally
  }
  
  protected long encodeTimeout(int paramInt) {
    long l = 0L;
    if (paramInt > 0) {
      l = (paramInt - 1);
      for (paramInt = 0; (0xFFFFFFFFFFFFFF00L & l) > 0L; paramInt++)
        l >>= 1L; 
      l = (paramInt << 8) | l & 0xFFL;
    } 
    return l;
  }
  
  public String getDeviceName() {
    return "STMicroelectronics_VL53L0X_Range_Sensor";
  }
  
  public double getDistance(DistanceUnit paramDistanceUnit) {
    double d = readRangeContinuousMillimeters();
    if (paramDistanceUnit == DistanceUnit.CM) {
      double d1 = 10.0D;
      return d / d1;
    } 
    if (paramDistanceUnit == DistanceUnit.METER) {
      double d1 = 1000.0D;
      return d / d1;
    } 
    if (paramDistanceUnit == DistanceUnit.INCH) {
      double d1 = 25.4D;
      return d / d1;
    } 
    return d;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Other;
  }
  
  long getMeasurementTimingBudget() {
    SequenceStepEnables sequenceStepEnables = new SequenceStepEnables();
    SequenceStepTimeouts sequenceStepTimeouts = new SequenceStepTimeouts();
    getSequenceStepEnables(sequenceStepEnables);
    getSequenceStepTimeouts(sequenceStepEnables, sequenceStepTimeouts);
    boolean bool = sequenceStepEnables.tcc;
    long l2 = 2870L;
    if (bool)
      l2 = 2870L + sequenceStepTimeouts.msrc_dss_tcc_us + 590L; 
    if (sequenceStepEnables.dss) {
      l1 = l2 + (sequenceStepTimeouts.msrc_dss_tcc_us + 690L) * 2L;
    } else {
      l1 = l2;
      if (sequenceStepEnables.msrc)
        l1 = l2 + sequenceStepTimeouts.msrc_dss_tcc_us + 660L; 
    } 
    l2 = l1;
    if (sequenceStepEnables.pre_range)
      l2 = l1 + sequenceStepTimeouts.pre_range_us + 660L; 
    long l1 = l2;
    if (sequenceStepEnables.final_range)
      l1 = l2 + sequenceStepTimeouts.final_range_us + 550L; 
    this.measurement_timing_budget_us = l1;
    return l1;
  }
  
  public byte getModelID() {
    return readReg(Register.IDENTIFICATION_MODEL_ID);
  }
  
  protected void getSequenceStepEnables(SequenceStepEnables paramSequenceStepEnables) {
    boolean bool1;
    byte b = readReg(Register.SYSTEM_SEQUENCE_CONFIG);
    boolean bool2 = true;
    if ((b >> 4 & 0x1) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    paramSequenceStepEnables.tcc = bool1;
    if ((b >> 3 & 0x1) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    paramSequenceStepEnables.dss = bool1;
    if ((b >> 2 & 0x1) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    paramSequenceStepEnables.msrc = bool1;
    if ((b >> 6 & 0x1) != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    paramSequenceStepEnables.pre_range = bool1;
    if ((b >> 7 & 0x1) != 0) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    paramSequenceStepEnables.final_range = bool1;
  }
  
  protected void getSequenceStepTimeouts(SequenceStepEnables paramSequenceStepEnables, SequenceStepTimeouts paramSequenceStepTimeouts) {
    paramSequenceStepTimeouts.pre_range_vcsel_period_pclks = getVcselPulsePeriod(vcselPeriodType.VcselPeriodPreRange);
    paramSequenceStepTimeouts.msrc_dss_tcc_mclks = readReg(Register.MSRC_CONFIG_TIMEOUT_MACROP) + 1;
    paramSequenceStepTimeouts.msrc_dss_tcc_us = timeoutMclksToMicroseconds(paramSequenceStepTimeouts.msrc_dss_tcc_mclks, paramSequenceStepTimeouts.pre_range_vcsel_period_pclks);
    paramSequenceStepTimeouts.pre_range_mclks = decodeTimeout(readShort(Register.PRE_RANGE_CONFIG_TIMEOUT_MACROP_HI));
    paramSequenceStepTimeouts.pre_range_us = timeoutMclksToMicroseconds(paramSequenceStepTimeouts.pre_range_mclks, paramSequenceStepTimeouts.pre_range_vcsel_period_pclks);
    paramSequenceStepTimeouts.final_range_vcsel_period_pclks = getVcselPulsePeriod(vcselPeriodType.VcselPeriodFinalRange);
    paramSequenceStepTimeouts.final_range_mclks = decodeTimeout(readShort(Register.FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI));
    if (paramSequenceStepEnables.pre_range)
      paramSequenceStepTimeouts.final_range_mclks -= paramSequenceStepTimeouts.pre_range_mclks; 
    paramSequenceStepTimeouts.final_range_us = timeoutMclksToMicroseconds(paramSequenceStepTimeouts.final_range_mclks, paramSequenceStepTimeouts.final_range_vcsel_period_pclks);
  }
  
  protected int getTimeout() {
    return this.io_timeout;
  }
  
  protected int getVcselPulsePeriod(vcselPeriodType paramvcselPeriodType) {
    return (paramvcselPeriodType == vcselPeriodType.VcselPeriodPreRange) ? decodeVcselPeriod(readReg(Register.PRE_RANGE_CONFIG_VCSEL_PERIOD)) : ((paramvcselPeriodType == vcselPeriodType.VcselPeriodFinalRange) ? decodeVcselPeriod(readReg(Register.FINAL_RANGE_CONFIG_VCSEL_PERIOD)) : 255);
  }
  
  protected boolean performSingleRefCalibration(int paramInt) {
    writeReg(Register.SYSRANGE_START.bVal, paramInt | 0x1);
    writeReg(Register.SYSTEM_INTERRUPT_CLEAR.bVal, 1);
    writeReg(Register.SYSRANGE_START.bVal, 0);
    return true;
  }
  
  protected int readRangeContinuousMillimeters() {
    if (this.io_timeout > 0)
      this.ioElapsedTime.reset(); 
    if (this.assume_uninitialized)
      return 65535; 
    while ((readReg(Register.RESULT_INTERRUPT_STATUS) & 0x7) == 0) {
      if ((this.did_timeout && ((I2cDeviceSynch)this.deviceClient).getHealthStatus() == HardwareDeviceHealth.HealthStatus.UNHEALTHY) || Thread.currentThread().isInterrupted())
        return 65535; 
      if (this.ioElapsedTime.milliseconds() > this.io_timeout) {
        this.did_timeout = true;
        if (((I2cDeviceSynch)this.deviceClient).getHealthStatus() == HardwareDeviceHealth.HealthStatus.HEALTHY)
          this.assume_uninitialized = true; 
        return 65535;
      } 
    } 
    this.did_timeout = false;
    short s = TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(Register.RESULT_RANGE_STATUS.bVal + 10, 2));
    writeReg(Register.SYSTEM_INTERRUPT_CLEAR.bVal, 1);
    return s;
  }
  
  protected byte readReg(byte paramByte) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramByte);
  }
  
  protected byte readReg(int paramInt) {
    return ((I2cDeviceSynch)this.deviceClient).read8((byte)paramInt);
  }
  
  protected byte readReg(Register paramRegister) {
    return ((I2cDeviceSynch)this.deviceClient).read8(paramRegister.bVal);
  }
  
  protected short readShort(Register paramRegister) {
    return TypeConversion.byteArrayToShort(((I2cDeviceSynch)this.deviceClient).read(paramRegister.bVal, 2));
  }
  
  protected int readUnsignedByte(Register paramRegister) {
    return TypeConversion.unsignedByteToInt(readReg(paramRegister));
  }
  
  protected boolean setMeasurementTimingBudget(long paramLong) {
    long l1;
    SequenceStepEnables sequenceStepEnables = new SequenceStepEnables();
    SequenceStepTimeouts sequenceStepTimeouts = new SequenceStepTimeouts();
    if (paramLong < 20000L)
      return false; 
    long l2 = 2280L;
    getSequenceStepEnables(sequenceStepEnables);
    getSequenceStepTimeouts(sequenceStepEnables, sequenceStepTimeouts);
    if (sequenceStepEnables.tcc)
      l2 = 2280L + sequenceStepTimeouts.msrc_dss_tcc_us + 590L; 
    if (sequenceStepEnables.dss) {
      l1 = l2 + (sequenceStepTimeouts.msrc_dss_tcc_us + 690L) * 2L;
    } else {
      l1 = l2;
      if (sequenceStepEnables.msrc)
        l1 = l2 + sequenceStepTimeouts.msrc_dss_tcc_us + 660L; 
    } 
    l2 = l1;
    if (sequenceStepEnables.pre_range)
      l2 = l1 + sequenceStepTimeouts.pre_range_us + 660L; 
    if (sequenceStepEnables.final_range) {
      l1 = l2 + 550L;
      if (l1 > paramLong)
        return false; 
      l2 = timeoutMicrosecondsToMclks(paramLong - l1, sequenceStepTimeouts.final_range_vcsel_period_pclks);
      l1 = l2;
      if (sequenceStepEnables.pre_range)
        l1 = l2 + sequenceStepTimeouts.pre_range_mclks; 
      writeShort(Register.FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI, (short)(int)encodeTimeout((int)l1));
      this.measurement_timing_budget_us = paramLong;
    } 
    return true;
  }
  
  protected void setTimeout(int paramInt) {
    this.io_timeout = paramInt;
  }
  
  protected void startContinuous() {
    startContinuous(0);
  }
  
  protected void startContinuous(int paramInt) {
    writeReg(128, 1);
    writeReg(255, 1);
    writeReg(0, 0);
    writeReg(145, this.stop_variable);
    writeReg(0, 1);
    writeReg(255, 0);
    writeReg(128, 0);
    if (paramInt != 0) {
      short s = readShort(Register.OSC_CALIBRATE_VAL);
      int i = paramInt;
      if (s != 0)
        i = paramInt * s; 
      ((I2cDeviceSynch)this.deviceClient).write(Register.SYSTEM_INTERMEASUREMENT_PERIOD.bVal, TypeConversion.intToByteArray(i));
      writeReg(Register.SYSRANGE_START.bVal, 4);
      return;
    } 
    writeReg(Register.SYSRANGE_START.bVal, 2);
  }
  
  protected void stopContinuous() {
    writeReg(Register.SYSRANGE_START.bVal, 1);
    writeReg(255, 1);
    writeReg(0, 0);
    writeReg(145, 0);
    writeReg(0, 1);
    writeReg(255, 0);
  }
  
  protected long timeoutMclksToMicroseconds(int paramInt1, int paramInt2) {
    long l = calcMacroPeriod(paramInt2);
    return (paramInt1 * l + l / 2L) / 1000L;
  }
  
  protected long timeoutMicrosecondsToMclks(long paramLong, int paramInt) {
    long l = calcMacroPeriod(paramInt);
    return (paramLong * 1000L + l / 2L) / l;
  }
  
  protected void writeReg(byte paramByte1, byte paramByte2) {
    writeReg(paramByte1, paramByte2, I2cWaitControl.NONE);
  }
  
  protected void writeReg(byte paramByte1, byte paramByte2, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramByte1, paramByte2, paramI2cWaitControl);
  }
  
  protected void writeReg(int paramInt1, int paramInt2) {
    writeReg((byte)paramInt1, (byte)paramInt2, I2cWaitControl.NONE);
  }
  
  protected void writeReg(int paramInt1, int paramInt2, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write8((byte)paramInt1, (byte)paramInt2, paramI2cWaitControl);
  }
  
  protected void writeReg(Register paramRegister, byte paramByte) {
    writeReg(paramRegister, paramByte, I2cWaitControl.NONE);
  }
  
  protected void writeReg(Register paramRegister, byte paramByte, I2cWaitControl paramI2cWaitControl) {
    ((I2cDeviceSynch)this.deviceClient).write8(paramRegister.bVal, paramByte, paramI2cWaitControl);
  }
  
  protected void writeShort(Register paramRegister, short paramShort) {
    ((I2cDeviceSynch)this.deviceClient).write(paramRegister.bVal, TypeConversion.shortToByteArray(paramShort));
  }
  
  public enum Register {
    ALGO_PART_TO_PART_RANGE_OFFSET_MM,
    ALGO_PHASECAL_CONFIG_TIMEOUT,
    ALGO_PHASECAL_LIM,
    CROSSTALK_COMPENSATION_PEAK_RATE_MCPS,
    DYNAMIC_SPAD_NUM_REQUESTED_REF_SPAD,
    DYNAMIC_SPAD_REF_EN_START_OFFSET,
    FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT,
    FINAL_RANGE_CONFIG_MIN_SNR,
    FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI,
    FINAL_RANGE_CONFIG_TIMEOUT_MACROP_LO,
    FINAL_RANGE_CONFIG_VALID_PHASE_HIGH,
    FINAL_RANGE_CONFIG_VALID_PHASE_LOW,
    FINAL_RANGE_CONFIG_VCSEL_PERIOD,
    GLOBAL_CONFIG_REF_EN_START_SELECT,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_0,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_1,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_2,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_3,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_4,
    GLOBAL_CONFIG_SPAD_ENABLES_REF_5,
    GLOBAL_CONFIG_VCSEL_WIDTH,
    GPIO_HV_MUX_ACTIVE_HIGH,
    HISTOGRAM_CONFIG_INITIAL_PHASE_SELECT,
    HISTOGRAM_CONFIG_READOUT_CTRL,
    I2C_SLAVE_DEVICE_ADDRESS,
    IDENTIFICATION_MODEL_ID,
    IDENTIFICATION_REVISION_ID,
    MSRC_CONFIG_CONTROL,
    MSRC_CONFIG_TIMEOUT_MACROP,
    OSC_CALIBRATE_VAL,
    POWER_MANAGEMENT_GO1_POWER_FORCE,
    PRE_RANGE_CONFIG_MIN_SNR,
    PRE_RANGE_CONFIG_SIGMA_THRESH_HI,
    PRE_RANGE_CONFIG_SIGMA_THRESH_LO,
    PRE_RANGE_CONFIG_TIMEOUT_MACROP_HI,
    PRE_RANGE_CONFIG_TIMEOUT_MACROP_LO,
    PRE_RANGE_CONFIG_VALID_PHASE_HIGH,
    PRE_RANGE_CONFIG_VALID_PHASE_LOW,
    PRE_RANGE_CONFIG_VCSEL_PERIOD,
    PRE_RANGE_MIN_COUNT_RATE_RTN_LIMIT,
    RESULT_CORE_AMBIENT_WINDOW_EVENTS_REF,
    RESULT_CORE_AMBIENT_WINDOW_EVENTS_RTN,
    RESULT_CORE_RANGING_TOTAL_EVENTS_REF,
    RESULT_CORE_RANGING_TOTAL_EVENTS_RTN,
    RESULT_INTERRUPT_STATUS,
    RESULT_PEAK_SIGNAL_RATE_REF,
    RESULT_RANGE_STATUS,
    SOFT_RESET_GO2_SOFT_RESET_N,
    SYSRANGE_START(0),
    SYSTEM_HISTOGRAM_BIN(0),
    SYSTEM_INTERMEASUREMENT_PERIOD(0),
    SYSTEM_INTERRUPT_CLEAR(0),
    SYSTEM_INTERRUPT_CONFIG_GPIO(0),
    SYSTEM_RANGE_CONFIG(0),
    SYSTEM_SEQUENCE_CONFIG(0),
    SYSTEM_THRESH_HIGH(12),
    SYSTEM_THRESH_LOW(14),
    VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV(14);
    
    public int bVal;
    
    static {
      SYSTEM_RANGE_CONFIG = new Register("SYSTEM_RANGE_CONFIG", 4, 9);
      SYSTEM_INTERMEASUREMENT_PERIOD = new Register("SYSTEM_INTERMEASUREMENT_PERIOD", 5, 4);
      SYSTEM_INTERRUPT_CONFIG_GPIO = new Register("SYSTEM_INTERRUPT_CONFIG_GPIO", 6, 10);
      GPIO_HV_MUX_ACTIVE_HIGH = new Register("GPIO_HV_MUX_ACTIVE_HIGH", 7, 132);
      SYSTEM_INTERRUPT_CLEAR = new Register("SYSTEM_INTERRUPT_CLEAR", 8, 11);
      RESULT_INTERRUPT_STATUS = new Register("RESULT_INTERRUPT_STATUS", 9, 19);
      RESULT_RANGE_STATUS = new Register("RESULT_RANGE_STATUS", 10, 20);
      RESULT_CORE_AMBIENT_WINDOW_EVENTS_RTN = new Register("RESULT_CORE_AMBIENT_WINDOW_EVENTS_RTN", 11, 188);
      RESULT_CORE_RANGING_TOTAL_EVENTS_RTN = new Register("RESULT_CORE_RANGING_TOTAL_EVENTS_RTN", 12, 192);
      RESULT_CORE_AMBIENT_WINDOW_EVENTS_REF = new Register("RESULT_CORE_AMBIENT_WINDOW_EVENTS_REF", 13, 208);
      RESULT_CORE_RANGING_TOTAL_EVENTS_REF = new Register("RESULT_CORE_RANGING_TOTAL_EVENTS_REF", 14, 212);
      RESULT_PEAK_SIGNAL_RATE_REF = new Register("RESULT_PEAK_SIGNAL_RATE_REF", 15, 182);
      ALGO_PART_TO_PART_RANGE_OFFSET_MM = new Register("ALGO_PART_TO_PART_RANGE_OFFSET_MM", 16, 40);
      I2C_SLAVE_DEVICE_ADDRESS = new Register("I2C_SLAVE_DEVICE_ADDRESS", 17, 138);
      MSRC_CONFIG_CONTROL = new Register("MSRC_CONFIG_CONTROL", 18, 96);
      PRE_RANGE_CONFIG_MIN_SNR = new Register("PRE_RANGE_CONFIG_MIN_SNR", 19, 39);
      PRE_RANGE_CONFIG_VALID_PHASE_LOW = new Register("PRE_RANGE_CONFIG_VALID_PHASE_LOW", 20, 86);
      PRE_RANGE_CONFIG_VALID_PHASE_HIGH = new Register("PRE_RANGE_CONFIG_VALID_PHASE_HIGH", 21, 87);
      PRE_RANGE_MIN_COUNT_RATE_RTN_LIMIT = new Register("PRE_RANGE_MIN_COUNT_RATE_RTN_LIMIT", 22, 100);
      FINAL_RANGE_CONFIG_MIN_SNR = new Register("FINAL_RANGE_CONFIG_MIN_SNR", 23, 103);
      FINAL_RANGE_CONFIG_VALID_PHASE_LOW = new Register("FINAL_RANGE_CONFIG_VALID_PHASE_LOW", 24, 71);
      FINAL_RANGE_CONFIG_VALID_PHASE_HIGH = new Register("FINAL_RANGE_CONFIG_VALID_PHASE_HIGH", 25, 72);
      FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT = new Register("FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT", 26, 68);
      PRE_RANGE_CONFIG_SIGMA_THRESH_HI = new Register("PRE_RANGE_CONFIG_SIGMA_THRESH_HI", 27, 97);
      PRE_RANGE_CONFIG_SIGMA_THRESH_LO = new Register("PRE_RANGE_CONFIG_SIGMA_THRESH_LO", 28, 98);
      PRE_RANGE_CONFIG_VCSEL_PERIOD = new Register("PRE_RANGE_CONFIG_VCSEL_PERIOD", 29, 80);
      PRE_RANGE_CONFIG_TIMEOUT_MACROP_HI = new Register("PRE_RANGE_CONFIG_TIMEOUT_MACROP_HI", 30, 81);
      PRE_RANGE_CONFIG_TIMEOUT_MACROP_LO = new Register("PRE_RANGE_CONFIG_TIMEOUT_MACROP_LO", 31, 82);
      SYSTEM_HISTOGRAM_BIN = new Register("SYSTEM_HISTOGRAM_BIN", 32, 129);
      HISTOGRAM_CONFIG_INITIAL_PHASE_SELECT = new Register("HISTOGRAM_CONFIG_INITIAL_PHASE_SELECT", 33, 51);
      HISTOGRAM_CONFIG_READOUT_CTRL = new Register("HISTOGRAM_CONFIG_READOUT_CTRL", 34, 85);
      FINAL_RANGE_CONFIG_VCSEL_PERIOD = new Register("FINAL_RANGE_CONFIG_VCSEL_PERIOD", 35, 112);
      FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI = new Register("FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI", 36, 113);
      FINAL_RANGE_CONFIG_TIMEOUT_MACROP_LO = new Register("FINAL_RANGE_CONFIG_TIMEOUT_MACROP_LO", 37, 114);
      CROSSTALK_COMPENSATION_PEAK_RATE_MCPS = new Register("CROSSTALK_COMPENSATION_PEAK_RATE_MCPS", 38, 32);
      MSRC_CONFIG_TIMEOUT_MACROP = new Register("MSRC_CONFIG_TIMEOUT_MACROP", 39, 70);
      SOFT_RESET_GO2_SOFT_RESET_N = new Register("SOFT_RESET_GO2_SOFT_RESET_N", 40, 191);
      IDENTIFICATION_MODEL_ID = new Register("IDENTIFICATION_MODEL_ID", 41, 192);
      IDENTIFICATION_REVISION_ID = new Register("IDENTIFICATION_REVISION_ID", 42, 194);
      OSC_CALIBRATE_VAL = new Register("OSC_CALIBRATE_VAL", 43, 248);
      GLOBAL_CONFIG_VCSEL_WIDTH = new Register("GLOBAL_CONFIG_VCSEL_WIDTH", 44, 50);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_0 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_0", 45, 176);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_1 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_1", 46, 177);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_2 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_2", 47, 178);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_3 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_3", 48, 179);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_4 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_4", 49, 180);
      GLOBAL_CONFIG_SPAD_ENABLES_REF_5 = new Register("GLOBAL_CONFIG_SPAD_ENABLES_REF_5", 50, 181);
      GLOBAL_CONFIG_REF_EN_START_SELECT = new Register("GLOBAL_CONFIG_REF_EN_START_SELECT", 51, 182);
      DYNAMIC_SPAD_NUM_REQUESTED_REF_SPAD = new Register("DYNAMIC_SPAD_NUM_REQUESTED_REF_SPAD", 52, 78);
      DYNAMIC_SPAD_REF_EN_START_OFFSET = new Register("DYNAMIC_SPAD_REF_EN_START_OFFSET", 53, 79);
      POWER_MANAGEMENT_GO1_POWER_FORCE = new Register("POWER_MANAGEMENT_GO1_POWER_FORCE", 54, 128);
      VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV = new Register("VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV", 55, 137);
      ALGO_PHASECAL_LIM = new Register("ALGO_PHASECAL_LIM", 56, 48);
      Register register = new Register("ALGO_PHASECAL_CONFIG_TIMEOUT", 57, 48);
      ALGO_PHASECAL_CONFIG_TIMEOUT = register;
      $VALUES = new Register[] { 
          SYSRANGE_START, SYSTEM_THRESH_HIGH, SYSTEM_THRESH_LOW, SYSTEM_SEQUENCE_CONFIG, SYSTEM_RANGE_CONFIG, SYSTEM_INTERMEASUREMENT_PERIOD, SYSTEM_INTERRUPT_CONFIG_GPIO, GPIO_HV_MUX_ACTIVE_HIGH, SYSTEM_INTERRUPT_CLEAR, RESULT_INTERRUPT_STATUS, 
          RESULT_RANGE_STATUS, RESULT_CORE_AMBIENT_WINDOW_EVENTS_RTN, RESULT_CORE_RANGING_TOTAL_EVENTS_RTN, RESULT_CORE_AMBIENT_WINDOW_EVENTS_REF, RESULT_CORE_RANGING_TOTAL_EVENTS_REF, RESULT_PEAK_SIGNAL_RATE_REF, ALGO_PART_TO_PART_RANGE_OFFSET_MM, I2C_SLAVE_DEVICE_ADDRESS, MSRC_CONFIG_CONTROL, PRE_RANGE_CONFIG_MIN_SNR, 
          PRE_RANGE_CONFIG_VALID_PHASE_LOW, PRE_RANGE_CONFIG_VALID_PHASE_HIGH, PRE_RANGE_MIN_COUNT_RATE_RTN_LIMIT, FINAL_RANGE_CONFIG_MIN_SNR, FINAL_RANGE_CONFIG_VALID_PHASE_LOW, FINAL_RANGE_CONFIG_VALID_PHASE_HIGH, FINAL_RANGE_CONFIG_MIN_COUNT_RATE_RTN_LIMIT, PRE_RANGE_CONFIG_SIGMA_THRESH_HI, PRE_RANGE_CONFIG_SIGMA_THRESH_LO, PRE_RANGE_CONFIG_VCSEL_PERIOD, 
          PRE_RANGE_CONFIG_TIMEOUT_MACROP_HI, PRE_RANGE_CONFIG_TIMEOUT_MACROP_LO, SYSTEM_HISTOGRAM_BIN, HISTOGRAM_CONFIG_INITIAL_PHASE_SELECT, HISTOGRAM_CONFIG_READOUT_CTRL, FINAL_RANGE_CONFIG_VCSEL_PERIOD, FINAL_RANGE_CONFIG_TIMEOUT_MACROP_HI, FINAL_RANGE_CONFIG_TIMEOUT_MACROP_LO, CROSSTALK_COMPENSATION_PEAK_RATE_MCPS, MSRC_CONFIG_TIMEOUT_MACROP, 
          SOFT_RESET_GO2_SOFT_RESET_N, IDENTIFICATION_MODEL_ID, IDENTIFICATION_REVISION_ID, OSC_CALIBRATE_VAL, GLOBAL_CONFIG_VCSEL_WIDTH, GLOBAL_CONFIG_SPAD_ENABLES_REF_0, GLOBAL_CONFIG_SPAD_ENABLES_REF_1, GLOBAL_CONFIG_SPAD_ENABLES_REF_2, GLOBAL_CONFIG_SPAD_ENABLES_REF_3, GLOBAL_CONFIG_SPAD_ENABLES_REF_4, 
          GLOBAL_CONFIG_SPAD_ENABLES_REF_5, GLOBAL_CONFIG_REF_EN_START_SELECT, DYNAMIC_SPAD_NUM_REQUESTED_REF_SPAD, DYNAMIC_SPAD_REF_EN_START_OFFSET, POWER_MANAGEMENT_GO1_POWER_FORCE, VHV_CONFIG_PAD_SCL_SDA__EXTSUP_HV, ALGO_PHASECAL_LIM, register };
    }
    
    Register(int param1Int1) {
      this.bVal = param1Int1;
    }
  }
  
  protected class SequenceStepEnables {
    boolean dss;
    
    boolean final_range;
    
    boolean msrc;
    
    boolean pre_range;
    
    boolean tcc;
  }
  
  protected class SequenceStepTimeouts {
    int final_range_mclks;
    
    long final_range_us;
    
    int final_range_vcsel_period_pclks;
    
    int msrc_dss_tcc_mclks;
    
    long msrc_dss_tcc_us;
    
    int pre_range_mclks;
    
    long pre_range_us;
    
    int pre_range_vcsel_period_pclks;
  }
  
  enum vcselPeriodType {
    VcselPeriodPreRange,
    VcselPeriodFinalRange(14);
    
    static {
      vcselPeriodType vcselPeriodType1 = new vcselPeriodType("VcselPeriodFinalRange", 1);
      VcselPeriodFinalRange = vcselPeriodType1;
      $VALUES = new vcselPeriodType[] { VcselPeriodPreRange, vcselPeriodType1 };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\stmicroelectronics\VL53L0X.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */