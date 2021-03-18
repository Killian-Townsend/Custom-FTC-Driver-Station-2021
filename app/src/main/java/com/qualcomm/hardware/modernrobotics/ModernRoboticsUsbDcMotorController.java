package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.hardware.HardwareFactory;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnable;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableSegment;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnableStandard;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.ModernRoboticsMotorControllerParamsState;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.util.DifferentialControlLoopCoefficients;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteOrder;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public final class ModernRoboticsUsbDcMotorController extends ModernRoboticsUsbController implements DcMotorController, VoltageSensor {
  protected static final int ADDRESS_BATTERY_VOLTAGE = 84;
  
  protected static final int[] ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP;
  
  protected static final int ADDRESS_MOTOR1_CURRENT_ENCODER_VALUE = 76;
  
  protected static final int ADDRESS_MOTOR1_D_COEFFICIENT = 89;
  
  protected static final int ADDRESS_MOTOR1_GEAR_RATIO = 86;
  
  protected static final int ADDRESS_MOTOR1_I_COEFFICIENT = 88;
  
  protected static final int ADDRESS_MOTOR1_MODE = 68;
  
  protected static final int ADDRESS_MOTOR1_POWER = 69;
  
  protected static final int ADDRESS_MOTOR1_P_COEFFICIENT = 87;
  
  protected static final int ADDRESS_MOTOR1_TARGET_ENCODER_VALUE = 64;
  
  protected static final int ADDRESS_MOTOR2_CURRENT_ENCODER_VALUE = 80;
  
  protected static final int ADDRESS_MOTOR2_D_COEFFICIENT = 93;
  
  protected static final int ADDRESS_MOTOR2_GEAR_RATIO = 90;
  
  protected static final int ADDRESS_MOTOR2_I_COEFFICIENT = 92;
  
  protected static final int ADDRESS_MOTOR2_MODE = 71;
  
  protected static final int ADDRESS_MOTOR2_POWER = 70;
  
  protected static final int ADDRESS_MOTOR2_P_COEFFICIENT = 91;
  
  protected static final int ADDRESS_MOTOR2_TARGET_ENCODER_VALUE = 72;
  
  protected static final int[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP;
  
  protected static final int[] ADDRESS_MOTOR_GEAR_RATIO_MAP;
  
  protected static final int[] ADDRESS_MOTOR_MODE_MAP;
  
  protected static final int[] ADDRESS_MOTOR_POWER_MAP = new int[] { 255, 69, 70 };
  
  protected static final int[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP;
  
  protected static final int ADDRESS_PID_PARAMS_LOCK = 3;
  
  protected static final int ADDRESS_UNUSED = 255;
  
  protected static final double BATTERY_MAX_MEASURABLE_VOLTAGE = 20.4D;
  
  protected static final int BATTERY_MAX_MEASURABLE_VOLTAGE_INT = 1023;
  
  public static final int BUSY_THRESHOLD = 5;
  
  protected static final byte CHANNEL_MODE_FLAG_BUSY = -128;
  
  protected static final byte CHANNEL_MODE_FLAG_ERROR = 64;
  
  protected static final byte CHANNEL_MODE_FLAG_LOCK = 4;
  
  protected static final byte CHANNEL_MODE_FLAG_NO_TIMEOUT = 16;
  
  protected static final byte CHANNEL_MODE_FLAG_REVERSE = 8;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED = 1;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY = 0;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
  
  protected static final byte CHANNEL_MODE_FLAG_UNUSED = 32;
  
  protected static final int CHANNEL_MODE_MASK_BUSY = 128;
  
  protected static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
  
  protected static final int CHANNEL_MODE_MASK_ERROR = 64;
  
  protected static final int CHANNEL_MODE_MASK_LOCK = 4;
  
  protected static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
  
  protected static final int CHANNEL_MODE_MASK_REVERSE = 8;
  
  protected static final int CHANNEL_MODE_MASK_SELECTION = 3;
  
  protected static final byte CHANNEL_MODE_UNKNOWN = -1;
  
  protected static final boolean DEBUG_LOGGING = false;
  
  protected static final byte DEFAULT_D_COEFFICIENT = -72;
  
  protected static final byte DEFAULT_I_COEFFICIENT = 64;
  
  protected static final byte DEFAULT_P_COEFFICIENT = -128;
  
  protected static final int DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAX = 255;
  
  protected static final int MONITOR_LENGTH = 30;
  
  protected static final int MOTOR_FIRST = 1;
  
  protected static final int MOTOR_LAST = 2;
  
  protected static final int MOTOR_MAX = 3;
  
  protected static final byte PID_PARAMS_LOCK_DISABLE = -69;
  
  protected static final byte PID_PARAMS_LOCK_ENABLE = 0;
  
  protected static final byte RATIO_MAX = 127;
  
  protected static final byte RATIO_MIN = -128;
  
  protected static final byte START_ADDRESS = 64;
  
  public static final String TAG = "MRMotorController";
  
  protected static final double apiPowerMax = 1.0D;
  
  protected static final double apiPowerMin = -1.0D;
  
  protected static final byte bPowerBrake = 0;
  
  protected static final byte bPowerFloat = -128;
  
  protected static final byte bPowerMax = 100;
  
  protected static final byte bPowerMin = -100;
  
  protected static final byte cbEncoder = 4;
  
  protected static final int cbRatioPIDParams = 4;
  
  final MotorProperties[] motors = new MotorProperties[3];
  
  protected ReadWriteRunnableSegment pidParamsLockSegment;
  
  protected ReadWriteRunnableSegment[] rgPidParamsSegment;
  
  static {
    ADDRESS_MOTOR_MODE_MAP = new int[] { 255, 68, 71 };
    ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new int[] { 255, 64, 72 };
    ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new int[] { 255, 76, 80 };
    ADDRESS_MOTOR_GEAR_RATIO_MAP = new int[] { 255, 86, 90 };
    ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP = new int[] { 255, 87, 91 };
  }
  
  public ModernRoboticsUsbDcMotorController(Context paramContext, SerialNumber paramSerialNumber, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, SyncdDevice.Manager paramManager) throws RobotCoreException, InterruptedException {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, new ModernRoboticsUsbDevice.CreateReadWriteRunnable(paramContext, paramSerialNumber) {
          public ReadWriteRunnable create(RobotUsbDevice param1RobotUsbDevice) {
            return (ReadWriteRunnable)new ReadWriteRunnableStandard(context, serialNumber, param1RobotUsbDevice, 30, 64, false);
          }
        });
    int i = 0;
    while (true) {
      MotorProperties[] arrayOfMotorProperties = this.motors;
      if (i < arrayOfMotorProperties.length) {
        arrayOfMotorProperties[i] = new MotorProperties();
        i++;
        continue;
      } 
      break;
    } 
  }
  
  private void doArmOrPretend(boolean paramBoolean) throws RobotCoreException, InterruptedException {
    String str1;
    String str2 = HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber);
    if (paramBoolean) {
      str1 = "";
    } else {
      str1 = " (pretend)";
    } 
    RobotLog.d("arming modern motor controller %s%s...", new Object[] { str2, str1 });
    forgetLastKnown();
    if (paramBoolean) {
      armDevice();
    } else {
      pretendDevice();
    } 
    createSegments();
    RobotLog.d("...arming modern motor controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
  }
  
  private void floatHardware() {
    for (int i = 1; i <= 2; i++)
      setMotorPowerFloat(i); 
  }
  
  public static DcMotor.RunMode modeFromByte(byte paramByte) {
    int i = paramByte & 0x3;
    return (i != 0) ? ((i != 1) ? ((i != 2) ? ((i != 3) ? DcMotor.RunMode.RUN_WITHOUT_ENCODER : DcMotor.RunMode.STOP_AND_RESET_ENCODER) : DcMotor.RunMode.RUN_TO_POSITION) : DcMotor.RunMode.RUN_USING_ENCODER) : DcMotor.RunMode.RUN_WITHOUT_ENCODER;
  }
  
  public static byte modeToByte(DcMotor.RunMode paramRunMode) {
    int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.migrate().ordinal()];
    return (i != 2) ? ((i != 3) ? ((i != 4) ? 1 : 3) : 2) : 0;
  }
  
  private void runWithoutEncoders() {
    for (int i = 1; i <= 2; i++)
      setMotorMode(i, DcMotor.RunMode.RUN_WITHOUT_ENCODER); 
  }
  
  private void setDifferentialControlLoopCoefficientsToDefault() {
    for (int i = 1; i <= 2; i++) {
      write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[i], new byte[] { Byte.MIN_VALUE, 64, -72 });
    } 
  }
  
  private void validateApiMotorPower(double paramDouble) {
    if (-1.0D <= paramDouble && paramDouble <= 1.0D)
      return; 
    throw new IllegalArgumentException(String.format("illegal motor power %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(paramDouble), Double.valueOf(-1.0D), Double.valueOf(1.0D) }));
  }
  
  private void validateMotor(int paramInt) {
    if (paramInt >= 1 && paramInt <= 2)
      return; 
    throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(1), Integer.valueOf(2) }));
  }
  
  void brakeAllAtZero() {
    for (int i = 1; i <= 2; i++)
      (this.motors[i]).zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE; 
  }
  
  protected void createSegments() {
    ReadWriteRunnable readWriteRunnable = this.readWriteRunnable;
    int i = 1;
    this.pidParamsLockSegment = readWriteRunnable.createSegment(0, 3, 1);
    this.rgPidParamsSegment = new ReadWriteRunnableSegment[3];
    while (i <= 2) {
      this.rgPidParamsSegment[i] = this.readWriteRunnable.createSegment(i, ADDRESS_MOTOR_GEAR_RATIO_MAP[i], 4);
      i++;
    } 
  }
  
  protected void doArm() throws RobotCoreException, InterruptedException {
    int i = 1;
    doArmOrPretend(true);
    while (i <= 2) {
      updateMotorParamsIfNecessary(i);
      i++;
    } 
  }
  
  public void doClose() {
    floatHardware();
    super.doClose();
  }
  
  protected void doCloseFromArmed() {
    floatHardware();
    doCloseFromOther();
  }
  
  protected void doCloseFromOther() {
    try {
      doDisarm();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } catch (RobotCoreException robotCoreException) {
      return;
    } 
  }
  
  protected void doDisarm() throws RobotCoreException, InterruptedException {
    RobotLog.d("disarming modern motor controller %s...", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
    disarmDevice();
    forgetLastKnown();
    RobotLog.d("...disarming modern motor controller %s complete", new Object[] { HardwareFactory.getDeviceDisplayName(this.context, this.serialNumber) });
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    doArmOrPretend(false);
  }
  
  void finishModeSwitchIfNecessary(int paramInt) {
    if (!(this.motors[paramInt]).modeSwitchCompletionNeeded)
      return; 
    try {
      DcMotor.RunMode runMode1 = internalGetCachedOrQueriedRunMode(paramInt);
      DcMotor.RunMode runMode2 = (this.motors[paramInt]).prevRunMode;
      byte b1 = modeToByte(runMode1);
      byte b2 = modeToByte(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      do {
        byte b3;
        boolean bool = isArmed();
        if (!bool)
          break; 
        byte b4 = (byte)(read8(ADDRESS_MOTOR_MODE_MAP[paramInt]) & 0x3);
        if (b4 == b1 || (runMode1 == DcMotor.RunMode.STOP_AND_RESET_ENCODER && b4 == b2))
          break; 
        MotorProperties motorProperties = this.motors[paramInt];
        int i = motorProperties.modeSwitchWaitCount;
        motorProperties.modeSwitchWaitCount = i + 1;
        if (i < (this.motors[paramInt]).modeSwitchWaitCountMax)
          continue; 
        SerialNumber serialNumber = getSerialNumber();
        i = (this.motors[paramInt]).modeSwitchWaitCount;
        if (runMode2 == null) {
          b3 = -1;
        } else {
          b3 = modeToByte(runMode2);
        } 
        RobotLog.dd("MRMotorController", "mode resend: motor=[%s,%d] wait=%d from=%d to=%d cur=%d", new Object[] { serialNumber, Integer.valueOf(paramInt), Integer.valueOf(i - 1), Byte.valueOf(b3), Byte.valueOf(b1), Byte.valueOf(b4) });
        write8(ADDRESS_MOTOR_MODE_MAP[paramInt], b1);
        (this.motors[paramInt]).modeSwitchWaitCount = 0;
      } while (waitForNextReadComplete());
      if (runMode1.isPIDMode() && (runMode2 == null || !runMode2.isPIDMode())) {
        double d2 = (this.motors[paramInt]).prevPower;
        double d1 = d2;
        if (runMode1 == DcMotor.RunMode.RUN_TO_POSITION)
          d1 = Math.abs(d2); 
        internalSetMotorPower(paramInt, d1);
      } else if (runMode1 == DcMotor.RunMode.RUN_TO_POSITION) {
        double d = internalQueryMotorPower(paramInt);
        if (d < 0.0D)
          internalSetMotorPower(paramInt, Math.abs(d)); 
      } 
      if (runMode1 == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
        long l = System.nanoTime() + 100000000L;
        while (internalQueryMotorCurrentPosition(paramInt) != 0) {
          long l2 = System.nanoTime();
          long l1 = l;
          if (l2 > l) {
            RobotLog.dd("MRMotorController", "mode resend: motor=[%s,%d] mode=%s", new Object[] { getSerialNumber(), Integer.valueOf(paramInt), runMode1 });
            write8(ADDRESS_MOTOR_MODE_MAP[paramInt], b1);
            l1 = l2 + 100000000L;
          } 
          if (!isArmed())
            break; 
          boolean bool = waitForNextReadComplete();
          l = l1;
          if (!bool)
            break; 
        } 
      } 
      return;
    } finally {
      forgetLastKnown();
      (this.motors[paramInt]).modeSwitchCompletionNeeded = false;
    } 
  }
  
  void forgetLastKnown() {
    for (int i = 1; i <= 2; i++) {
      (this.motors[i]).lastKnownPowerByte.invalidate();
      (this.motors[i]).lastKnownMode.invalidate();
      (this.motors[i]).lastKnownTargetPosition.invalidate();
    } 
  }
  
  void forgetLastKnownPowers() {
    for (int i = 1; i <= 2; i++)
      (this.motors[i]).lastKnownPowerByte.invalidate(); 
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("USB ");
    stringBuilder.append(getSerialNumber());
    return stringBuilder.toString();
  }
  
  public String getDeviceName() {
    return String.format("%s %s", new Object[] { this.context.getString(R.string.moduleDisplayNameMotorController), this.robotUsbDevice.getFirmwareVersion() });
  }
  
  public DifferentialControlLoopCoefficients getDifferentialControlLoopCoefficients(int paramInt) {
    validateMotor(paramInt);
    DifferentialControlLoopCoefficients differentialControlLoopCoefficients = new DifferentialControlLoopCoefficients();
    byte[] arrayOfByte = read(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[paramInt], 3);
    differentialControlLoopCoefficients.p = arrayOfByte[0];
    differentialControlLoopCoefficients.i = arrayOfByte[1];
    differentialControlLoopCoefficients.d = arrayOfByte[2];
    return differentialControlLoopCoefficients;
  }
  
  public double getGearRatio(int paramInt) {
    validateMotor(paramInt);
    return read(ADDRESS_MOTOR_GEAR_RATIO_MAP[paramInt], 1)[0] / 127.0D;
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.ModernRobotics;
  }
  
  public int getMotorCurrentPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual internalQueryMotorCurrentPosition : (I)I
    //   17: istore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: iload_1
    //   21: ireturn
    //   22: astore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	22	finally
  }
  
  public DcMotor.RunMode getMotorMode(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual internalGetCachedOrQueriedRunMode : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   17: astore_2
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_2
    //   21: areturn
    //   22: astore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	22	finally
  }
  
  public double getMotorPower(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual internalQueryMotorPower : (I)D
    //   17: dstore_2
    //   18: aload_0
    //   19: monitorexit
    //   20: dload_2
    //   21: dreturn
    //   22: astore #4
    //   24: aload_0
    //   25: monitorexit
    //   26: aload #4
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	22	finally
  }
  
  public boolean getMotorPowerFloat(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP : [I
    //   16: iload_1
    //   17: iaload
    //   18: invokevirtual read8 : (I)B
    //   21: istore_1
    //   22: iload_1
    //   23: bipush #-128
    //   25: if_icmpne -> 33
    //   28: iconst_1
    //   29: istore_2
    //   30: goto -> 35
    //   33: iconst_0
    //   34: istore_2
    //   35: aload_0
    //   36: monitorexit
    //   37: iload_2
    //   38: ireturn
    //   39: astore_3
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_3
    //   43: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	39	finally
  }
  
  public int getMotorTargetPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual internalQueryMotorTargetPosition : (I)I
    //   17: istore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: iload_1
    //   21: ireturn
    //   22: astore_2
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_2
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	22	finally
  }
  
  public MotorConfigurationType getMotorType(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   11: iload_1
    //   12: aaload
    //   13: getfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: areturn
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	21	finally
  }
  
  public DcMotor.ZeroPowerBehavior getMotorZeroPowerBehavior(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   16: iload_1
    //   17: aaload
    //   18: getfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   21: astore_2
    //   22: aload_0
    //   23: monitorexit
    //   24: aload_2
    //   25: areturn
    //   26: astore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_2
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	26	finally
  }
  
  protected String getTag() {
    return "MRMotorController";
  }
  
  public double getVoltage() {
    byte[] arrayOfByte = read(84, 2);
    return ((TypeConversion.unsignedByteToInt(arrayOfByte[0]) << 2) + (TypeConversion.unsignedByteToInt(arrayOfByte[1]) & 0x3) & 0x3FF) / 1023.0D * 20.4D;
  }
  
  public void initializeHardware() {
    floatHardware();
    setDifferentialControlLoopCoefficientsToDefault();
  }
  
  double internalGetCachedOrQueriedMotorPower(int paramInt) {
    Byte byte_ = (Byte)(this.motors[paramInt]).lastKnownPowerByte.getNonTimedValue();
    return (byte_ != null) ? internalMotorPowerFromByte(paramInt, byte_.byteValue()) : internalQueryMotorPower(paramInt);
  }
  
  DcMotor.RunMode internalGetCachedOrQueriedRunMode(int paramInt) {
    DcMotor.RunMode runMode2 = (DcMotor.RunMode)(this.motors[paramInt]).lastKnownMode.getNonTimedValue();
    DcMotor.RunMode runMode1 = runMode2;
    if (runMode2 == null)
      runMode1 = internalQueryRunMode(paramInt); 
    return runMode1;
  }
  
  double internalMotorPowerFromByte(int paramInt, byte paramByte) {
    return (paramByte == Byte.MIN_VALUE) ? 0.0D : Range.scale(paramByte, -100.0D, 100.0D, -1.0D, 1.0D);
  }
  
  int internalQueryMotorCurrentPosition(int paramInt) {
    return TypeConversion.byteArrayToInt(read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[paramInt], 4), ByteOrder.BIG_ENDIAN);
  }
  
  double internalQueryMotorPower(int paramInt) {
    byte b = read8(ADDRESS_MOTOR_POWER_MAP[paramInt]);
    (this.motors[paramInt]).lastKnownPowerByte.setValue(Byte.valueOf(b));
    return internalMotorPowerFromByte(paramInt, b);
  }
  
  int internalQueryMotorTargetPosition(int paramInt) {
    int i = TypeConversion.byteArrayToInt(read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[paramInt], 4), ByteOrder.BIG_ENDIAN);
    (this.motors[paramInt]).lastKnownTargetPosition.setValue(Integer.valueOf(i));
    return i;
  }
  
  DcMotor.RunMode internalQueryRunMode(int paramInt) {
    DcMotor.RunMode runMode = modeFromByte(read8(ADDRESS_MOTOR_MODE_MAP[paramInt]));
    (this.motors[paramInt]).lastKnownMode.setValue(runMode);
    return runMode;
  }
  
  void internalSetMotorPower(int paramInt, byte paramByte) {
    if ((this.motors[paramInt]).lastKnownPowerByte.updateValue(Byte.valueOf(paramByte)))
      write8(ADDRESS_MOTOR_POWER_MAP[paramInt], paramByte); 
  }
  
  void internalSetMotorPower(int paramInt, double paramDouble) {
    byte b;
    paramDouble = Range.clip(paramDouble, -1.0D, 1.0D);
    validateApiMotorPower(paramDouble);
    if (paramDouble == 0.0D && (this.motors[paramInt]).zeroPowerBehavior == DcMotor.ZeroPowerBehavior.FLOAT) {
      b = Byte.MIN_VALUE;
    } else {
      b = (byte)(int)Range.scale(paramDouble, -1.0D, 1.0D, -100.0D, 100.0D);
    } 
    internalSetMotorPower(paramInt, b);
  }
  
  public boolean isBusy(int paramInt) {
    validateMotor(paramInt);
    finishModeSwitchIfNecessary(paramInt);
    return (Math.abs(getMotorTargetPosition(paramInt) - getMotorCurrentPosition(paramInt)) > 5);
  }
  
  protected void paranoidSleep(int paramInt) {
    if (paramInt > 0) {
      long l = paramInt;
      try {
        Thread.sleep(l);
        return;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
    } 
  }
  
  ModernRoboticsMotorControllerParamsState readMotorParams(int paramInt) {
    return ModernRoboticsMotorControllerParamsState.fromByteArray(read(ADDRESS_MOTOR_GEAR_RATIO_MAP[paramInt], 4));
  }
  
  public void resetDeviceConfigurationForOpMode() {
    floatHardware();
    runWithoutEncoders();
    brakeAllAtZero();
    forgetLastKnown();
  }
  
  public void resetDeviceConfigurationForOpMode(int paramInt) {
    validateMotor(paramInt);
    if ((this.motors[paramInt]).internalMotorType != null && (this.motors[paramInt]).motorType != (this.motors[paramInt]).internalMotorType)
      setMotorType(paramInt, (this.motors[paramInt]).internalMotorType); 
  }
  
  public void setDifferentialControlLoopCoefficients(int paramInt, DifferentialControlLoopCoefficients paramDifferentialControlLoopCoefficients) {
    validateMotor(paramInt);
    if (paramDifferentialControlLoopCoefficients.p > 255.0D)
      paramDifferentialControlLoopCoefficients.p = 255.0D; 
    if (paramDifferentialControlLoopCoefficients.i > 255.0D)
      paramDifferentialControlLoopCoefficients.i = 255.0D; 
    if (paramDifferentialControlLoopCoefficients.d > 255.0D)
      paramDifferentialControlLoopCoefficients.d = 255.0D; 
    write(ADDRESS_MAX_DIFFERENTIAL_CONTROL_LOOP_COEFFICIENT_MAP[paramInt], new byte[] { (byte)(int)paramDifferentialControlLoopCoefficients.p, (byte)(int)paramDifferentialControlLoopCoefficients.i, (byte)(int)paramDifferentialControlLoopCoefficients.d });
  }
  
  protected void setEEPromLock(boolean paramBoolean) {
    byte b;
    if (paramBoolean) {
      b = 0;
    } else {
      b = -69;
    } 
    writeSegment(this.pidParamsLockSegment, new byte[] { b });
  }
  
  public void setGearRatio(int paramInt, double paramDouble) {
    validateMotor(paramInt);
    Range.throwIfRangeIsInvalid(paramDouble, -1.0D, 1.0D);
    write(ADDRESS_MOTOR_GEAR_RATIO_MAP[paramInt], new byte[] { (byte)(int)(paramDouble * 127.0D) });
  }
  
  public void setMotorMode(int paramInt, DcMotor.RunMode paramRunMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_2
    //   3: invokevirtual migrate : ()Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   6: astore_2
    //   7: aload_0
    //   8: iload_1
    //   9: invokespecial validateMotor : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   17: aload_0
    //   18: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   21: iload_1
    //   22: aaload
    //   23: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   26: invokevirtual getNonTimedValue : ()Ljava/lang/Object;
    //   29: checkcast com/qualcomm/robotcore/hardware/DcMotor$RunMode
    //   32: astore #4
    //   34: aload_0
    //   35: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   38: iload_1
    //   39: aaload
    //   40: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   43: aload_2
    //   44: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   47: ifeq -> 110
    //   50: aload_0
    //   51: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   54: iload_1
    //   55: aaload
    //   56: iconst_1
    //   57: putfield modeSwitchCompletionNeeded : Z
    //   60: aload_0
    //   61: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   64: iload_1
    //   65: aaload
    //   66: iconst_0
    //   67: putfield modeSwitchWaitCount : I
    //   70: aload_0
    //   71: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   74: iload_1
    //   75: aaload
    //   76: aload #4
    //   78: putfield prevRunMode : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   81: aload_0
    //   82: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   85: iload_1
    //   86: aaload
    //   87: aload_0
    //   88: iload_1
    //   89: invokevirtual internalGetCachedOrQueriedMotorPower : (I)D
    //   92: putfield prevPower : D
    //   95: aload_2
    //   96: invokestatic modeToByte : (Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)B
    //   99: istore_3
    //   100: aload_0
    //   101: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_MODE_MAP : [I
    //   104: iload_1
    //   105: iaload
    //   106: iload_3
    //   107: invokevirtual write8 : (IB)V
    //   110: aload_0
    //   111: monitorexit
    //   112: return
    //   113: astore_2
    //   114: aload_0
    //   115: monitorexit
    //   116: aload_2
    //   117: athrow
    // Exception table:
    //   from	to	target	type
    //   2	110	113	finally
  }
  
  public void setMotorPower(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: dload_2
    //   15: invokevirtual internalSetMotorPower : (ID)V
    //   18: aload_0
    //   19: monitorexit
    //   20: return
    //   21: astore #4
    //   23: aload_0
    //   24: monitorexit
    //   25: aload #4
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	18	21	finally
  }
  
  protected void setMotorPowerFloat(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_POWER_MAP : [I
    //   16: iload_1
    //   17: iaload
    //   18: bipush #-128
    //   20: invokevirtual write8 : (IB)V
    //   23: aload_0
    //   24: monitorexit
    //   25: return
    //   26: astore_2
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_2
    //   30: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	26	finally
  }
  
  public void setMotorTargetPosition(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   16: iload_1
    //   17: aaload
    //   18: getfield lastKnownTargetPosition : Lcom/qualcomm/robotcore/util/LastKnown;
    //   21: iload_2
    //   22: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   25: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   28: ifeq -> 47
    //   31: aload_0
    //   32: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP : [I
    //   35: iload_1
    //   36: iaload
    //   37: iload_2
    //   38: getstatic java/nio/ByteOrder.BIG_ENDIAN : Ljava/nio/ByteOrder;
    //   41: invokestatic intToByteArray : (ILjava/nio/ByteOrder;)[B
    //   44: invokevirtual write : (I[B)V
    //   47: aload_0
    //   48: monitorexit
    //   49: return
    //   50: astore_3
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_3
    //   54: athrow
    // Exception table:
    //   from	to	target	type
    //   2	47	50	finally
  }
  
  public void setMotorType(int paramInt, MotorConfigurationType paramMotorConfigurationType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   11: iload_1
    //   12: aaload
    //   13: aload_2
    //   14: putfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   17: aload_0
    //   18: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   21: iload_1
    //   22: aaload
    //   23: getfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   26: ifnonnull -> 39
    //   29: aload_0
    //   30: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   33: iload_1
    //   34: aaload
    //   35: aload_2
    //   36: putfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   39: aload_0
    //   40: iload_1
    //   41: invokevirtual updateMotorParamsIfNecessary : (I)V
    //   44: aload_0
    //   45: monitorexit
    //   46: return
    //   47: astore_2
    //   48: aload_0
    //   49: monitorexit
    //   50: aload_2
    //   51: athrow
    // Exception table:
    //   from	to	target	type
    //   2	39	47	finally
    //   39	44	47	finally
  }
  
  public void setMotorZeroPowerBehavior(int paramInt, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_2
    //   8: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.UNKNOWN : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   11: if_acmpeq -> 73
    //   14: aload_0
    //   15: iload_1
    //   16: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   19: aload_0
    //   20: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   23: iload_1
    //   24: aaload
    //   25: getfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   28: aload_2
    //   29: if_acmpeq -> 70
    //   32: aload_0
    //   33: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   36: iload_1
    //   37: aaload
    //   38: aload_2
    //   39: putfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   42: aload_0
    //   43: iload_1
    //   44: invokevirtual internalGetCachedOrQueriedMotorPower : (I)D
    //   47: dconst_0
    //   48: dcmpl
    //   49: ifne -> 70
    //   52: aload_0
    //   53: getfield motors : [Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbDcMotorController$MotorProperties;
    //   56: iload_1
    //   57: aaload
    //   58: getfield lastKnownPowerByte : Lcom/qualcomm/robotcore/util/LastKnown;
    //   61: invokevirtual invalidate : ()V
    //   64: aload_0
    //   65: iload_1
    //   66: dconst_0
    //   67: invokevirtual internalSetMotorPower : (ID)V
    //   70: aload_0
    //   71: monitorexit
    //   72: return
    //   73: new java/lang/IllegalArgumentException
    //   76: dup
    //   77: ldc_w 'zeroPowerBehavior may not be UNKNOWN'
    //   80: invokespecial <init> : (Ljava/lang/String;)V
    //   83: athrow
    //   84: astore_2
    //   85: aload_0
    //   86: monitorexit
    //   87: aload_2
    //   88: athrow
    // Exception table:
    //   from	to	target	type
    //   2	70	84	finally
    //   73	84	84	finally
  }
  
  protected void updateMotorParamsIfNecessary(int paramInt) {
    if (this.robotUsbDevice != null && this.robotUsbDevice.getFirmwareVersion() != null && (this.robotUsbDevice.getFirmwareVersion()).majorVersion >= 2 && (this.motors[paramInt]).motorType.hasModernRoboticsParams()) {
      null = (this.motors[paramInt]).motorType.getModernRoboticsParams();
      ModernRoboticsMotorControllerParamsState modernRoboticsMotorControllerParamsState = readMotorParams(paramInt);
      if (modernRoboticsMotorControllerParamsState.equals(null)) {
        RobotLog.vv("MRMotorController", "motor params already correct: #=%d params=%s", new Object[] { Integer.valueOf(paramInt), modernRoboticsMotorControllerParamsState });
        return;
      } 
      RobotLog.vv("MRMotorController", "updating motor params: #=%d old=%s new=%s", new Object[] { Integer.valueOf(paramInt), modernRoboticsMotorControllerParamsState, null });
      this.readWriteRunnable.suppressReads(true);
      try {
      
      } finally {
        this.readWriteRunnable.suppressReads(false);
        waitForNextReadComplete();
      } 
    } 
  }
  
  protected void writeSegment(ReadWriteRunnableSegment paramReadWriteRunnableSegment, byte[] paramArrayOfbyte) {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        paramReadWriteRunnableSegment.getWriteLock().lock();
        try {
          this.writeStatus = ModernRoboticsUsbController.WRITE_STATUS.DIRTY;
          byte[] arrayOfByte = paramReadWriteRunnableSegment.getWriteBuffer();
          System.arraycopy(paramArrayOfbyte, 0, arrayOfByte, 0, Math.min(paramArrayOfbyte.length, arrayOfByte.length));
          paramReadWriteRunnableSegment.getWriteLock().unlock();
          this.readWriteRunnable.queueSegmentWrite(paramReadWriteRunnableSegment.getKey());
          return;
        } finally {
          paramReadWriteRunnableSegment.getWriteLock().unlock();
        } 
      } 
    } 
  }
  
  static class MotorProperties {
    MotorConfigurationType internalMotorType = null;
    
    LastKnown<DcMotor.RunMode> lastKnownMode = new LastKnown();
    
    LastKnown<Byte> lastKnownPowerByte = new LastKnown();
    
    LastKnown<Integer> lastKnownTargetPosition = new LastKnown();
    
    boolean modeSwitchCompletionNeeded = false;
    
    int modeSwitchWaitCount = 0;
    
    int modeSwitchWaitCountMax = 4;
    
    MotorConfigurationType motorType = MotorConfigurationType.getUnspecifiedMotorType();
    
    double prevPower;
    
    DcMotor.RunMode prevRunMode = null;
    
    DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDcMotorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */