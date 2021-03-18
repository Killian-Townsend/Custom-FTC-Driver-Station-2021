package com.qualcomm.hardware.matrix;

import com.qualcomm.hardware.motors.MatrixLegacyMotor;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.TypeConversion;
import java.util.Arrays;
import java.util.Set;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class MatrixDcMotorController implements DcMotorController {
  private static final int BATTERY_UNITS = 40;
  
  private static final byte CHANNEL_MODE_FLAG_SELECT_FLOAT = 0;
  
  private static final byte CHANNEL_MODE_FLAG_SELECT_POWER_CONTROL = 1;
  
  private static final byte CHANNEL_MODE_FLAG_SELECT_RESET = 4;
  
  private static final byte CHANNEL_MODE_FLAG_SELECT_RTP_CONTROL = 3;
  
  private static final byte CHANNEL_MODE_FLAG_SELECT_SPEED_CONTROL = 2;
  
  private static final byte I2C_DATA_OFFSET = 4;
  
  private static final int MAX_NUM_MOTORS = 4;
  
  private static final byte MODE_PENDING_BIT = 8;
  
  private static final int NO_TARGET = 0;
  
  private static final int POSITION_DATA_SIZE = 4;
  
  public static final byte POWER_MAX = 100;
  
  public static final byte POWER_MIN = -100;
  
  private static final byte SPEED_STOPPED = 0;
  
  private static final int TARGET_DATA_SIZE = 4;
  
  protected static final double apiPowerMax = 1.0D;
  
  protected static final double apiPowerMin = -1.0D;
  
  private int batteryVal;
  
  protected MatrixMasterController master;
  
  private MotorProperties[] motorCache = new MotorProperties[] { new MotorProperties(1), new MotorProperties(1), new MotorProperties(2), new MotorProperties(3), new MotorProperties(4) };
  
  private boolean pendMotorPowerChanges = false;
  
  public MatrixDcMotorController(MatrixMasterController paramMatrixMasterController) {
    this.master = paramMatrixMasterController;
    this.batteryVal = 0;
    paramMatrixMasterController.registerMotorController(this);
    for (byte b = 0; b < 4; b = (byte)(b + 1)) {
      paramMatrixMasterController.queueTransaction(new MatrixI2cTransaction(b, (byte)0, 0, (byte)0));
      (this.motorCache[b]).runMode = DcMotor.RunMode.RUN_WITHOUT_ENCODER;
      (this.motorCache[b]).floating = true;
      (this.motorCache[b]).power = 0.0D;
    } 
    this.pendMotorPowerChanges = false;
  }
  
  private void throwIfMotorIsInvalid(int paramInt) {
    if (paramInt >= 1 && paramInt <= 4)
      return; 
    throw new IllegalArgumentException(String.format("Motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(1), Integer.valueOf(4) }));
  }
  
  void brakeAllAtZero() {
    for (int i = 0; i < 4; i++)
      (this.motorCache[i]).zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE; 
  }
  
  public void close() {
    setMotorPowerFloat(1);
    setMotorPowerFloat(2);
    setMotorPowerFloat(3);
    setMotorPowerFloat(4);
  }
  
  protected DcMotor.RunMode flagMatrixToRunMode(byte paramByte) {
    if (paramByte != 1) {
      if (paramByte != 2) {
        if (paramByte != 3) {
          if (paramByte != 4) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid run mode flag ");
            stringBuilder.append(paramByte);
            RobotLog.e(stringBuilder.toString());
            return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
          } 
          return DcMotor.RunMode.STOP_AND_RESET_ENCODER;
        } 
        return DcMotor.RunMode.RUN_TO_POSITION;
      } 
      return DcMotor.RunMode.RUN_USING_ENCODER;
    } 
    return DcMotor.RunMode.RUN_WITHOUT_ENCODER;
  }
  
  public int getBattery() {
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)0, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_BATTERY);
    if (this.master.queueTransaction(matrixI2cTransaction))
      this.master.waitOnRead(); 
    return this.batteryVal;
  }
  
  public String getConnectionInfo() {
    return this.master.getConnectionInfo();
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.displayNameMatrixMotorController);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Matrix;
  }
  
  public int getMotorCurrentPosition(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_POSITION);
    if (this.master.queueTransaction(matrixI2cTransaction))
      this.master.waitOnRead(); 
    return (this.motorCache[paramInt]).position;
  }
  
  public DcMotor.RunMode getMotorMode(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    return (this.motorCache[paramInt]).runMode;
  }
  
  public double getMotorPower(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    return (this.motorCache[paramInt]).power;
  }
  
  public boolean getMotorPowerFloat(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    return (this.motorCache[paramInt]).floating;
  }
  
  public int getMotorTargetPosition(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TARGET);
    if (this.master.queueTransaction(matrixI2cTransaction))
      this.master.waitOnRead(); 
    return (this.motorCache[paramInt]).target;
  }
  
  public MotorConfigurationType getMotorType(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial throwIfMotorIsInvalid : (I)V
    //   7: aload_0
    //   8: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
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
    //   4: invokespecial throwIfMotorIsInvalid : (I)V
    //   7: aload_0
    //   8: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   11: iload_1
    //   12: aaload
    //   13: getfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
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
  
  public int getVersion() {
    return 1;
  }
  
  public void handleReadBattery(byte[] paramArrayOfbyte) {
    this.batteryVal = TypeConversion.unsignedByteToInt(paramArrayOfbyte[4]) * 40;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Battery voltage: ");
    stringBuilder.append(this.batteryVal);
    stringBuilder.append("mV");
    RobotLog.v(stringBuilder.toString());
  }
  
  public void handleReadMode(MatrixI2cTransaction paramMatrixI2cTransaction, byte[] paramArrayOfbyte) {
    (this.motorCache[paramMatrixI2cTransaction.motor]).mode = paramArrayOfbyte[4];
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Mode: ");
    stringBuilder.append((this.motorCache[paramMatrixI2cTransaction.motor]).mode);
    RobotLog.v(stringBuilder.toString());
  }
  
  public void handleReadPosition(MatrixI2cTransaction paramMatrixI2cTransaction, byte[] paramArrayOfbyte) {
    (this.motorCache[paramMatrixI2cTransaction.motor]).position = TypeConversion.byteArrayToInt(Arrays.copyOfRange(paramArrayOfbyte, 4, 8));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Position motor: ");
    stringBuilder.append(paramMatrixI2cTransaction.motor);
    stringBuilder.append(" ");
    stringBuilder.append((this.motorCache[paramMatrixI2cTransaction.motor]).position);
    RobotLog.v(stringBuilder.toString());
  }
  
  public void handleReadTargetPosition(MatrixI2cTransaction paramMatrixI2cTransaction, byte[] paramArrayOfbyte) {
    (this.motorCache[paramMatrixI2cTransaction.motor]).target = TypeConversion.byteArrayToInt(Arrays.copyOfRange(paramArrayOfbyte, 4, 8));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Target motor: ");
    stringBuilder.append(paramMatrixI2cTransaction.motor);
    stringBuilder.append(" ");
    stringBuilder.append((this.motorCache[paramMatrixI2cTransaction.motor]).target);
    RobotLog.v(stringBuilder.toString());
  }
  
  public boolean isBusy(int paramInt) {
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE);
    this.master.queueTransaction(matrixI2cTransaction);
    this.master.waitOnRead();
    return (((this.motorCache[matrixI2cTransaction.motor]).mode & 0x80) != 0);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    brakeAllAtZero();
  }
  
  public void resetDeviceConfigurationForOpMode(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
  }
  
  protected byte runModeToFlagMatrix(DcMotor.RunMode paramRunMode) {
    int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[paramRunMode.migrate().ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? 4 : 3) : 1) : 2;
  }
  
  void setFloatingFromMode(int paramInt) {
    if ((this.motorCache[paramInt]).runMode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
      (this.motorCache[paramInt]).floating = true;
      return;
    } 
    (this.motorCache[paramInt]).floating = false;
  }
  
  public void setMotorMode(int paramInt, DcMotor.RunMode paramRunMode) {
    paramRunMode = paramRunMode.migrate();
    throwIfMotorIsInvalid(paramInt);
    if (!(this.motorCache[paramInt]).floating && (this.motorCache[paramInt]).runMode == paramRunMode)
      return; 
    byte b = runModeToFlagMatrix(paramRunMode);
    DcMotor.RunMode runMode = (this.motorCache[paramInt]).runMode;
    double d = getMotorPower(paramInt);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE, b);
    this.master.queueTransaction(matrixI2cTransaction);
    (this.motorCache[paramInt]).runMode = paramRunMode;
    if (paramRunMode.isPIDMode() && !runMode.isPIDMode())
      setMotorPower(paramInt, d); 
    setFloatingFromMode(paramInt);
  }
  
  public void setMotorPower(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial throwIfMotorIsInvalid : (I)V
    //   7: dload_2
    //   8: ldc2_w -1.0
    //   11: dconst_1
    //   12: invokestatic clip : (DDD)D
    //   15: dstore_2
    //   16: aload_0
    //   17: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   20: iload_1
    //   21: aaload
    //   22: getfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   25: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.FLOAT : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   28: if_acmpne -> 45
    //   31: dload_2
    //   32: dconst_0
    //   33: dcmpl
    //   34: ifne -> 45
    //   37: aload_0
    //   38: iload_1
    //   39: invokevirtual setMotorPowerFloat : (I)V
    //   42: goto -> 132
    //   45: ldc2_w 100.0
    //   48: dload_2
    //   49: dmul
    //   50: d2i
    //   51: i2b
    //   52: istore #4
    //   54: aload_0
    //   55: getfield pendMotorPowerChanges : Z
    //   58: ifeq -> 142
    //   61: bipush #8
    //   63: istore #5
    //   65: goto -> 68
    //   68: new com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   71: dup
    //   72: iload_1
    //   73: i2b
    //   74: iload #4
    //   76: aload_0
    //   77: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   80: iload_1
    //   81: aaload
    //   82: getfield target : I
    //   85: iload #5
    //   87: aload_0
    //   88: aload_0
    //   89: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   92: iload_1
    //   93: aaload
    //   94: getfield runMode : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   97: invokevirtual runModeToFlagMatrix : (Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)B
    //   100: ior
    //   101: i2b
    //   102: invokespecial <init> : (BBIB)V
    //   105: astore #6
    //   107: aload_0
    //   108: getfield master : Lcom/qualcomm/hardware/matrix/MatrixMasterController;
    //   111: aload #6
    //   113: invokevirtual queueTransaction : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;)Z
    //   116: pop
    //   117: aload_0
    //   118: iload_1
    //   119: invokevirtual setFloatingFromMode : (I)V
    //   122: aload_0
    //   123: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   126: iload_1
    //   127: aaload
    //   128: dload_2
    //   129: putfield power : D
    //   132: aload_0
    //   133: monitorexit
    //   134: return
    //   135: astore #6
    //   137: aload_0
    //   138: monitorexit
    //   139: aload #6
    //   141: athrow
    //   142: iconst_0
    //   143: istore #5
    //   145: goto -> 68
    // Exception table:
    //   from	to	target	type
    //   2	31	135	finally
    //   37	42	135	finally
    //   54	61	135	finally
    //   68	132	135	finally
  }
  
  public void setMotorPower(Set<DcMotor> paramSet, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield pendMotorPowerChanges : Z
    //   7: aload_1
    //   8: invokeinterface iterator : ()Ljava/util/Iterator;
    //   13: astore_1
    //   14: aload_1
    //   15: invokeinterface hasNext : ()Z
    //   20: ifeq -> 41
    //   23: aload_1
    //   24: invokeinterface next : ()Ljava/lang/Object;
    //   29: checkcast com/qualcomm/robotcore/hardware/DcMotor
    //   32: dload_2
    //   33: invokeinterface setPower : (D)V
    //   38: goto -> 14
    //   41: new com/qualcomm/hardware/matrix/MatrixI2cTransaction
    //   44: dup
    //   45: iconst_0
    //   46: getstatic com/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty.PROPERTY_START : Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty;
    //   49: iconst_1
    //   50: invokespecial <init> : (BLcom/qualcomm/hardware/matrix/MatrixI2cTransaction$I2cTransactionProperty;I)V
    //   53: astore_1
    //   54: aload_0
    //   55: getfield master : Lcom/qualcomm/hardware/matrix/MatrixMasterController;
    //   58: aload_1
    //   59: invokevirtual queueTransaction : (Lcom/qualcomm/hardware/matrix/MatrixI2cTransaction;)Z
    //   62: pop
    //   63: aload_0
    //   64: iconst_0
    //   65: putfield pendMotorPowerChanges : Z
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: astore_1
    //   72: aload_0
    //   73: iconst_0
    //   74: putfield pendMotorPowerChanges : Z
    //   77: aload_1
    //   78: athrow
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	79	finally
    //   7	14	71	finally
    //   14	38	71	finally
    //   41	63	71	finally
    //   63	68	79	finally
    //   72	79	79	finally
  }
  
  protected void setMotorPowerFloat(int paramInt) {
    throwIfMotorIsInvalid(paramInt);
    if (!(this.motorCache[paramInt]).floating) {
      MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_MODE, 4);
      this.master.queueTransaction(matrixI2cTransaction);
    } 
    (this.motorCache[paramInt]).floating = true;
    (this.motorCache[paramInt]).power = 0.0D;
  }
  
  public void setMotorTargetPosition(int paramInt1, int paramInt2) {
    throwIfMotorIsInvalid(paramInt1);
    MatrixI2cTransaction matrixI2cTransaction = new MatrixI2cTransaction((byte)paramInt1, MatrixI2cTransaction.I2cTransactionProperty.PROPERTY_TARGET, paramInt2);
    this.master.queueTransaction(matrixI2cTransaction);
    (this.motorCache[paramInt1]).target = paramInt2;
  }
  
  public void setMotorType(int paramInt, MotorConfigurationType paramMotorConfigurationType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial throwIfMotorIsInvalid : (I)V
    //   7: aload_0
    //   8: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
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
    //   4: invokespecial throwIfMotorIsInvalid : (I)V
    //   7: aload_2
    //   8: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.UNKNOWN : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   11: if_acmpeq -> 55
    //   14: aload_0
    //   15: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   18: iload_1
    //   19: aaload
    //   20: aload_2
    //   21: putfield zeroPowerBehavior : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   24: aload_0
    //   25: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   28: iload_1
    //   29: aaload
    //   30: getfield power : D
    //   33: dconst_0
    //   34: dcmpl
    //   35: ifne -> 52
    //   38: aload_0
    //   39: iload_1
    //   40: aload_0
    //   41: getfield motorCache : [Lcom/qualcomm/hardware/matrix/MatrixDcMotorController$MotorProperties;
    //   44: iload_1
    //   45: aaload
    //   46: getfield power : D
    //   49: invokevirtual setMotorPower : (ID)V
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: new java/lang/IllegalArgumentException
    //   58: dup
    //   59: ldc_w 'zeroPowerBehavior may not be UNKNOWN'
    //   62: invokespecial <init> : (Ljava/lang/String;)V
    //   65: athrow
    //   66: astore_2
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_2
    //   70: athrow
    // Exception table:
    //   from	to	target	type
    //   2	52	66	finally
    //   55	66	66	finally
  }
  
  private class MotorProperties {
    public boolean floating = true;
    
    public byte mode = 0;
    
    public MotorConfigurationType motorType = MotorConfigurationType.getMotorType(MatrixLegacyMotor.class);
    
    public int position = 0;
    
    public double power = 0.0D;
    
    public DcMotor.RunMode runMode = DcMotor.RunMode.STOP_AND_RESET_ENCODER;
    
    public int target = 0;
    
    public DcMotor.ZeroPowerBehavior zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE;
    
    public MotorProperties(int param1Int) {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\matrix\MatrixDcMotorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */