package com.qualcomm.hardware.hitechnic;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.TypeConversion;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class HiTechnicNxtDcMotorController extends HiTechnicNxtController implements DcMotorController, VoltageSensor {
  protected static final byte[] ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP;
  
  protected static final byte[] ADDRESS_MOTOR_MODE_MAP;
  
  protected static final byte[] ADDRESS_MOTOR_POWER_MAP;
  
  protected static final byte[] ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP;
  
  public static final int BUSY_THRESHOLD = 5;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 3;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_CONSTANT_SPEED_NXT = 1;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_POWER_CONTROL_ONLY_NXT = 0;
  
  protected static final byte CHANNEL_MODE_FLAG_SELECT_RUN_TO_POSITION = 2;
  
  protected static final int CHANNEL_MODE_MASK_BUSY = 128;
  
  protected static final int CHANNEL_MODE_MASK_EMPTY_D5 = 32;
  
  protected static final int CHANNEL_MODE_MASK_ERROR = 64;
  
  protected static final int CHANNEL_MODE_MASK_LOCK = 4;
  
  protected static final int CHANNEL_MODE_MASK_NO_TIMEOUT = 16;
  
  protected static final int CHANNEL_MODE_MASK_REVERSE = 8;
  
  protected static final int CHANNEL_MODE_MASK_SELECTION = 3;
  
  protected static final I2cAddr I2C_ADDRESS = I2cAddr.create8bit(2);
  
  protected static final int MOTOR_FIRST = 1;
  
  protected static final int MOTOR_LAST = 2;
  
  protected static final int MOTOR_MAX = 3;
  
  protected static final int OFFSET_UNUSED = -1;
  
  protected static final double apiPowerMax = 1.0D;
  
  protected static final double apiPowerMin = -1.0D;
  
  protected static final byte bPowerBrake = 0;
  
  protected static final byte bPowerFloat = -128;
  
  protected static final byte bPowerMax = 100;
  
  protected static final byte bPowerMin = -100;
  
  protected static final byte cbEncoder = 4;
  
  protected static final int iRegWindowFirst = 64;
  
  protected static final int iRegWindowMax = 86;
  
  final MotorProperties[] motors = new MotorProperties[3];
  
  static {
    ADDRESS_MOTOR_POWER_MAP = new byte[] { -1, 69, 70 };
    ADDRESS_MOTOR_MODE_MAP = new byte[] { -1, 68, 71 };
    ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP = new byte[] { -1, 64, 72 };
    ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP = new byte[] { -1, 76, 80 };
  }
  
  public HiTechnicNxtDcMotorController(Context paramContext, LegacyModule paramLegacyModule, int paramInt) {
    super(paramContext, (I2cController)paramLegacyModule, paramInt, I2C_ADDRESS);
    paramInt = 0;
    while (true) {
      MotorProperties[] arrayOfMotorProperties = this.motors;
      if (paramInt < arrayOfMotorProperties.length) {
        arrayOfMotorProperties[paramInt] = new MotorProperties();
        paramInt++;
        continue;
      } 
      I2cDeviceSynch.HeartbeatAction heartbeatAction = new I2cDeviceSynch.HeartbeatAction(true, true, new I2cDeviceSynch.ReadWindow(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[1], 1, I2cDeviceSynch.ReadMode.ONLY_ONCE));
      this.i2cDeviceSynch.setHeartbeatAction(heartbeatAction);
      this.i2cDeviceSynch.setHeartbeatInterval(2000);
      this.i2cDeviceSynch.enableWriteCoalescing(true);
      this.i2cDeviceSynch.setReadWindow(new I2cDeviceSynch.ReadWindow(64, 22, I2cDeviceSynch.ReadMode.BALANCED));
      finishConstruction();
      return;
    } 
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
  
  private void validateApiMotorPower(double paramDouble) {
    if (-1.0D <= paramDouble && paramDouble <= 1.0D)
      return; 
    throw new IllegalArgumentException(String.format("illegal motor power %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(paramDouble), Double.valueOf(-1.0D), Double.valueOf(1.0D) }));
  }
  
  void brakeAllAtZero() {
    for (int i = 1; i <= 2; i++)
      (this.motors[i]).zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE; 
  }
  
  protected void controllerNowArmedOrPretending() {
    adjustHookingToMatchEngagement();
  }
  
  protected void doHook() {
    forgetLastKnown();
    this.i2cDeviceSynch.engage();
  }
  
  protected void doUnhook() {
    this.i2cDeviceSynch.disengage();
    forgetLastKnown();
  }
  
  void finishModeSwitchIfNecessary(int paramInt) {
    if (!(this.motors[paramInt]).modeSwitchCompletionNeeded)
      return; 
    DcMotor.RunMode runMode2 = internalGetCachedOrQueriedRunMode(paramInt);
    DcMotor.RunMode runMode1 = (this.motors[paramInt]).prevRunMode;
    byte b1 = modeToByte(runMode2);
    byte b2 = modeToByte(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    while (true) {
      if (isArmed()) {
        byte b = (byte)(this.i2cDeviceSynch.read8(ADDRESS_MOTOR_MODE_MAP[paramInt]) & 0x3);
        if (b != b1 && (runMode2 != DcMotor.RunMode.STOP_AND_RESET_ENCODER || b != b2)) {
          Thread.yield();
          continue;
        } 
      } 
      if (runMode2 == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
        while (internalQueryMotorCurrentPosition(paramInt) != 0 && isArmed())
          Thread.yield(); 
      } else {
        if (runMode2.isPIDMode() && (runMode1 == null || !runMode1.isPIDMode())) {
          double d2 = (this.motors[paramInt]).prevPower;
          double d1 = d2;
          if (runMode2 == DcMotor.RunMode.RUN_TO_POSITION)
            d1 = Math.abs(d2); 
          internalSetMotorPower(paramInt, d1);
        } else if (runMode2 == DcMotor.RunMode.RUN_TO_POSITION) {
          double d = internalGetCachedOrQueriedMotorPower(paramInt);
          if (d < 0.0D)
            internalSetMotorPower(paramInt, Math.abs(d)); 
        } 
        if (DcMotor.RunMode.STOP_AND_RESET_ENCODER.equals(runMode1)) {
          Byte byte_ = (Byte)(this.motors[paramInt]).lastKnownPowerByte.getRawValue();
          if (byte_ != null)
            internalSetMotorPower(paramInt, internalMotorPowerFromByte(paramInt, byte_.byteValue(), runMode1)); 
        } 
      } 
      forgetLastKnownPowers();
      (this.motors[paramInt]).modeSwitchCompletionNeeded = false;
      return;
    } 
  }
  
  protected void floatHardware() {
    for (int i = 1; i <= 2; i++)
      setMotorPowerFloat(i); 
    this.i2cDeviceSynch.waitForWriteCompletions(I2cWaitControl.ATOMIC);
  }
  
  void forgetLastKnown() {
    for (int i = 1; i <= 2; i++) {
      (this.motors[i]).lastKnownMode.invalidate();
      (this.motors[i]).lastKnownPowerByte.invalidate();
      (this.motors[i]).lastKnownTargetPosition.invalidate();
    } 
  }
  
  void forgetLastKnownPowers() {
    for (int i = 1; i <= 2; i++)
      (this.motors[i]).lastKnownPowerByte.invalidate(); 
  }
  
  public String getConnectionInfo() {
    return String.format(this.context.getString(R.string.controllerPortConnectionInfoFormat), new Object[] { this.controller.getConnectionInfo(), Integer.valueOf(this.physicalPort) });
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.nxtDcMotorControllerName);
  }
  
  public int getMotorCurrentPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
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
    //   4: invokevirtual validateMotor : (I)V
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual internalGetCachedOrQueriedMotorPower : (I)D
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getfield i2cDeviceSynch : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch;
    //   16: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController.ADDRESS_MOTOR_POWER_MAP : [B
    //   19: iload_1
    //   20: baload
    //   21: invokeinterface read8 : (I)B
    //   26: istore_1
    //   27: iload_1
    //   28: bipush #-128
    //   30: if_icmpne -> 38
    //   33: iconst_1
    //   34: istore_2
    //   35: goto -> 40
    //   38: iconst_0
    //   39: istore_2
    //   40: aload_0
    //   41: monitorexit
    //   42: iload_2
    //   43: ireturn
    //   44: astore_3
    //   45: aload_0
    //   46: monitorexit
    //   47: aload_3
    //   48: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	44	finally
  }
  
  public int getMotorTargetPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
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
  
  public int getVersion() {
    return 2;
  }
  
  public double getVoltage() {
    try {
      byte[] arrayOfByte = this.i2cDeviceSynch.read(84, 2);
      arrayOfByte[1] = (byte)(arrayOfByte[1] << 6);
      short s = ByteBuffer.wrap(arrayOfByte).order(ByteOrder.BIG_ENDIAN).getShort();
      return (s >> 6 & 0x3FF) * 0.02D;
    } catch (RuntimeException runtimeException) {
      return 0.0D;
    } 
  }
  
  protected void initPID() {}
  
  public void initializeHardware() {
    initPID();
    floatHardware();
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
    return (paramByte == Byte.MIN_VALUE) ? 0.0D : internalMotorPowerFromByte(paramInt, paramByte, internalGetCachedOrQueriedRunMode(paramInt));
  }
  
  double internalMotorPowerFromByte(int paramInt, byte paramByte, DcMotor.RunMode paramRunMode) {
    return (paramByte == Byte.MIN_VALUE) ? 0.0D : Range.clip(Range.scale(paramByte, -100.0D, 100.0D, -1.0D, 1.0D), -1.0D, 1.0D);
  }
  
  int internalQueryMotorCurrentPosition(int paramInt) {
    return TypeConversion.byteArrayToInt(this.i2cDeviceSynch.read(ADDRESS_MOTOR_CURRENT_ENCODER_VALUE_MAP[paramInt], 4), ByteOrder.BIG_ENDIAN);
  }
  
  double internalQueryMotorPower(int paramInt) {
    byte b = this.i2cDeviceSynch.read8(ADDRESS_MOTOR_POWER_MAP[paramInt]);
    (this.motors[paramInt]).lastKnownPowerByte.setValue(Byte.valueOf(b));
    return internalMotorPowerFromByte(paramInt, b);
  }
  
  int internalQueryMotorTargetPosition(int paramInt) {
    int i = TypeConversion.byteArrayToInt(this.i2cDeviceSynch.read(ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP[paramInt], 4), ByteOrder.BIG_ENDIAN);
    (this.motors[paramInt]).lastKnownTargetPosition.setValue(Integer.valueOf(i));
    return i;
  }
  
  DcMotor.RunMode internalQueryRunMode(int paramInt) {
    DcMotor.RunMode runMode = modeFromByte(this.i2cDeviceSynch.read8(ADDRESS_MOTOR_MODE_MAP[paramInt]));
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
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual getMotorTargetPosition : (I)I
    //   17: aload_0
    //   18: iload_1
    //   19: invokevirtual getMotorCurrentPosition : (I)I
    //   22: isub
    //   23: invokestatic abs : (I)I
    //   26: istore_1
    //   27: iload_1
    //   28: iconst_5
    //   29: if_icmple -> 37
    //   32: iconst_1
    //   33: istore_2
    //   34: goto -> 39
    //   37: iconst_0
    //   38: istore_2
    //   39: aload_0
    //   40: monitorexit
    //   41: iload_2
    //   42: ireturn
    //   43: astore_3
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_3
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	27	43	finally
  }
  
  public void resetDeviceConfigurationForOpMode() {
    floatHardware();
    runWithoutEncoders();
    brakeAllAtZero();
    forgetLastKnown();
  }
  
  public void resetDeviceConfigurationForOpMode(int paramInt) {
    validateMotor(paramInt);
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
    //   9: invokevirtual validateMotor : (I)V
    //   12: aload_0
    //   13: iload_1
    //   14: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   17: aload_0
    //   18: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   21: iload_1
    //   22: aaload
    //   23: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   26: invokevirtual getNonTimedValue : ()Ljava/lang/Object;
    //   29: checkcast com/qualcomm/robotcore/hardware/DcMotor$RunMode
    //   32: astore #4
    //   34: aload_0
    //   35: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   38: iload_1
    //   39: aaload
    //   40: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   43: aload_2
    //   44: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   47: ifeq -> 100
    //   50: aload_2
    //   51: invokestatic modeToByte : (Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)B
    //   54: istore_3
    //   55: aload_0
    //   56: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   59: iload_1
    //   60: aaload
    //   61: iconst_1
    //   62: putfield modeSwitchCompletionNeeded : Z
    //   65: aload_0
    //   66: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   69: iload_1
    //   70: aaload
    //   71: aload #4
    //   73: putfield prevRunMode : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   76: aload_0
    //   77: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   80: iload_1
    //   81: aaload
    //   82: aload_0
    //   83: iload_1
    //   84: invokevirtual internalGetCachedOrQueriedMotorPower : (I)D
    //   87: putfield prevPower : D
    //   90: aload_0
    //   91: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController.ADDRESS_MOTOR_MODE_MAP : [B
    //   94: iload_1
    //   95: baload
    //   96: iload_3
    //   97: invokevirtual write8 : (IB)V
    //   100: aload_0
    //   101: monitorexit
    //   102: return
    //   103: astore_2
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_2
    //   107: athrow
    // Exception table:
    //   from	to	target	type
    //   2	100	103	finally
  }
  
  public void setMotorPower(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController.ADDRESS_MOTOR_POWER_MAP : [B
    //   16: iload_1
    //   17: baload
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
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   12: aload_0
    //   13: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   16: iload_1
    //   17: aaload
    //   18: getfield lastKnownTargetPosition : Lcom/qualcomm/robotcore/util/LastKnown;
    //   21: iload_2
    //   22: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   25: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   28: ifeq -> 49
    //   31: iload_2
    //   32: getstatic java/nio/ByteOrder.BIG_ENDIAN : Ljava/nio/ByteOrder;
    //   35: invokestatic intToByteArray : (ILjava/nio/ByteOrder;)[B
    //   38: astore_3
    //   39: aload_0
    //   40: getstatic com/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController.ADDRESS_MOTOR_TARGET_ENCODER_VALUE_MAP : [B
    //   43: iload_1
    //   44: baload
    //   45: aload_3
    //   46: invokevirtual write : (I[B)V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore_3
    //   53: aload_0
    //   54: monitorexit
    //   55: aload_3
    //   56: athrow
    // Exception table:
    //   from	to	target	type
    //   2	49	52	finally
  }
  
  public void setMotorType(int paramInt, MotorConfigurationType paramMotorConfigurationType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_0
    //   8: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   11: iload_1
    //   12: aaload
    //   13: aload_2
    //   14: putfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   17: aload_0
    //   18: monitorexit
    //   19: return
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	20	finally
  }
  
  public void setMotorZeroPowerBehavior(int paramInt, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual validateMotor : (I)V
    //   7: aload_2
    //   8: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.UNKNOWN : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   11: if_acmpeq -> 73
    //   14: aload_0
    //   15: iload_1
    //   16: invokevirtual finishModeSwitchIfNecessary : (I)V
    //   19: aload_0
    //   20: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
    //   23: iload_1
    //   24: aaload
    //   25: getfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   28: aload_2
    //   29: if_acmpeq -> 70
    //   32: aload_0
    //   33: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
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
    //   53: getfield motors : [Lcom/qualcomm/hardware/hitechnic/HiTechnicNxtDcMotorController$MotorProperties;
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
  
  protected void validateMotor(int paramInt) {
    if (paramInt >= 1 && paramInt <= 2)
      return; 
    throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(1), Integer.valueOf(2) }));
  }
  
  static class MotorProperties {
    LastKnown<DcMotor.RunMode> lastKnownMode = new LastKnown();
    
    LastKnown<Byte> lastKnownPowerByte = new LastKnown();
    
    LastKnown<Integer> lastKnownTargetPosition = new LastKnown();
    
    boolean modeSwitchCompletionNeeded = false;
    
    MotorConfigurationType motorType = MotorConfigurationType.getUnspecifiedMotorType();
    
    double prevPower;
    
    DcMotor.RunMode prevRunMode = null;
    
    DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtDcMotorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */