package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxDekaInterfaceCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetADCResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetBulkInputDataResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorChannelCurrentAlertLevelCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorChannelCurrentAlertLevelResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorChannelModeCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorChannelModeResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorConstantPowerCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorConstantPowerResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorTargetVelocityCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetMotorTargetVelocityResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelCurrentAlertLevelCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorChannelModeCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorConstantPowerCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorPIDControlLoopCoefficientsCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorPIDFControlLoopCoefficientsCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetMotorTargetVelocityCommand;
import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.TargetPositionNotSetException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorControllerEx;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.ExpansionHubMotorControllerParamsState;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class LynxDcMotorController extends LynxController implements DcMotorController, DcMotorControllerEx {
  protected static boolean DEBUG = false;
  
  public static final String TAG = "LynxMotor";
  
  public static final int apiMotorFirst = 0;
  
  public static final int apiMotorLast = 3;
  
  public static final double apiPowerFirst = -1.0D;
  
  public static final double apiPowerLast = 1.0D;
  
  protected final MotorProperties[] motors = new MotorProperties[4];
  
  public LynxDcMotorController(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    int i = 0;
    while (true) {
      MotorProperties[] arrayOfMotorProperties = this.motors;
      if (i < arrayOfMotorProperties.length) {
        arrayOfMotorProperties[i] = new MotorProperties();
        i++;
        continue;
      } 
      finishConstruction();
      return;
    } 
  }
  
  private void reportPIDFControlLoopCoefficients() throws RobotCoreException, InterruptedException {
    reportPIDFControlLoopCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
    reportPIDFControlLoopCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
  }
  
  private void reportPIDFControlLoopCoefficients(DcMotor.RunMode paramRunMode) throws RobotCoreException, InterruptedException {
    if (DEBUG)
      for (int i = 0; i <= 3; i++) {
        PIDFCoefficients pIDFCoefficients = getPIDFCoefficients(i, paramRunMode);
        RobotLog.vv("LynxMotor", "mod=%d motor=%d mode=%s p=%f i=%f d=%f f=%f alg=%s", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(i), paramRunMode.toString(), Double.valueOf(pIDFCoefficients.p), Double.valueOf(pIDFCoefficients.i), Double.valueOf(pIDFCoefficients.d), Double.valueOf(pIDFCoefficients.f), pIDFCoefficients.algorithm });
      }  
  }
  
  private void runWithoutEncoders() {
    for (int i = 0; i <= 3; i++)
      setMotorMode(i, DcMotor.RunMode.RUN_WITHOUT_ENCODER); 
  }
  
  private void validateMotor(int paramInt) {
    if (paramInt >= 0 && paramInt <= 3)
      return; 
    throw new IllegalArgumentException(String.format("motor %d is invalid; valid motors are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(3) }));
  }
  
  private void validatePIDMode(int paramInt, DcMotor.RunMode paramRunMode) {
    if (paramRunMode.isPIDMode())
      return; 
    throw new IllegalArgumentException(String.format("motor %d: mode %s is invalid as PID Mode", new Object[] { Integer.valueOf(paramInt), paramRunMode }));
  }
  
  protected void doHook() {
    forgetLastKnown();
  }
  
  protected void doUnhook() {
    forgetLastKnown();
  }
  
  public void floatHardware() {
    for (int i = 0; i <= 3; i++)
      setMotorPowerFloat(i); 
  }
  
  public void forgetLastKnown() {
    for (MotorProperties motorProperties : this.motors) {
      motorProperties.lastKnownMode.invalidate();
      motorProperties.lastKnownPower.invalidate();
      motorProperties.lastKnownTargetPosition.invalidate();
      motorProperties.lastKnownZeroPowerBehavior.invalidate();
      motorProperties.lastKnownEnable.invalidate();
    } 
  }
  
  protected int getDefaultMaxMotorSpeed(int paramInt) {
    return (this.motors[paramInt]).motorType.getAchieveableMaxTicksPerSecondRounded();
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxDcMotorControllerDisplayName);
  }
  
  protected int getModuleAddress() {
    return getModule().getModuleAddress();
  }
  
  public double getMotorCurrent(int paramInt, CurrentUnit paramCurrentUnit) {
    LynxGetADCCommand lynxGetADCCommand = new LynxGetADCCommand(getModule(), LynxGetADCCommand.Channel.motorCurrent(paramInt), LynxGetADCCommand.Mode.ENGINEERING);
    try {
      return paramCurrentUnit.convert(((LynxGetADCResponse)lynxGetADCCommand.sendReceive()).getValue(), CurrentUnit.MILLIAMPS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public double getMotorCurrentAlert(int paramInt, CurrentUnit paramCurrentUnit) {
    Double double_ = (Double)(this.motors[paramInt]).lastKnownCurrentAlert.getValue();
    if (double_ != null)
      return paramCurrentUnit.convert(double_.doubleValue(), CurrentUnit.MILLIAMPS); 
    LynxGetMotorChannelCurrentAlertLevelCommand lynxGetMotorChannelCurrentAlertLevelCommand = new LynxGetMotorChannelCurrentAlertLevelCommand(getModule(), paramInt);
    try {
      null = ((LynxGetMotorChannelCurrentAlertLevelResponse)lynxGetMotorChannelCurrentAlertLevelCommand.sendReceive()).getCurrentLimit();
      (this.motors[paramInt]).lastKnownCurrentAlert.setValue(Double.valueOf(null));
      return paramCurrentUnit.convert(null, CurrentUnit.MILLIAMPS);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Double)LynxUsbUtil.<Double>makePlaceholderValue(Double.valueOf(0.0D))).doubleValue();
  }
  
  public int getMotorCurrentPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: new com/qualcomm/hardware/lynx/commands/core/LynxGetMotorEncoderPositionCommand
    //   14: dup
    //   15: aload_0
    //   16: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   19: iload_1
    //   20: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   23: astore_2
    //   24: aload_0
    //   25: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   28: instanceof com/qualcomm/hardware/lynx/LynxModule
    //   31: ifeq -> 66
    //   34: aload_0
    //   35: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   38: checkcast com/qualcomm/hardware/lynx/LynxModule
    //   41: astore_3
    //   42: aload_3
    //   43: invokevirtual getBulkCachingMode : ()Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   46: getstatic com/qualcomm/hardware/lynx/LynxModule$BulkCachingMode.OFF : Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   49: if_acmpeq -> 66
    //   52: aload_3
    //   53: aload_2
    //   54: invokevirtual recordBulkCachingCommandIntent : (Lcom/qualcomm/hardware/lynx/commands/core/LynxDekaInterfaceCommand;)Lcom/qualcomm/hardware/lynx/LynxModule$BulkData;
    //   57: iload_1
    //   58: invokevirtual getMotorCurrentPosition : (I)I
    //   61: istore_1
    //   62: aload_0
    //   63: monitorexit
    //   64: iload_1
    //   65: ireturn
    //   66: aload_2
    //   67: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   70: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetMotorEncoderPositionResponse
    //   73: invokevirtual getPosition : ()I
    //   76: istore_1
    //   77: aload_0
    //   78: monitorexit
    //   79: iload_1
    //   80: ireturn
    //   81: astore_2
    //   82: goto -> 90
    //   85: astore_2
    //   86: goto -> 90
    //   89: astore_2
    //   90: aload_0
    //   91: aload_2
    //   92: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   95: pop
    //   96: iconst_0
    //   97: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   100: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   103: checkcast java/lang/Integer
    //   106: invokevirtual intValue : ()I
    //   109: istore_1
    //   110: aload_0
    //   111: monitorexit
    //   112: iload_1
    //   113: ireturn
    //   114: astore_2
    //   115: aload_0
    //   116: monitorexit
    //   117: aload_2
    //   118: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	114	finally
    //   11	62	114	finally
    //   66	77	89	java/lang/InterruptedException
    //   66	77	85	java/lang/RuntimeException
    //   66	77	81	com/qualcomm/hardware/lynx/LynxNackException
    //   66	77	114	finally
    //   90	110	114	finally
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
    //   9: iconst_0
    //   10: iadd
    //   11: invokevirtual internalGetPublicMotorMode : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: areturn
    //   19: astore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
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
    //   9: iconst_0
    //   10: iadd
    //   11: invokevirtual internalGetMotorPower : (I)D
    //   14: dstore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: dload_2
    //   18: dreturn
    //   19: astore #4
    //   21: aload_0
    //   22: monitorexit
    //   23: aload #4
    //   25: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public boolean getMotorPowerFloat(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iconst_0
    //   8: istore #5
    //   10: iload_1
    //   11: iconst_0
    //   12: iadd
    //   13: istore_1
    //   14: iload #5
    //   16: istore #4
    //   18: aload_0
    //   19: iload_1
    //   20: invokevirtual internalGetZeroPowerBehavior : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   23: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.FLOAT : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   26: if_acmpne -> 48
    //   29: aload_0
    //   30: iload_1
    //   31: invokevirtual internalGetMotorPower : (I)D
    //   34: dstore_2
    //   35: iload #5
    //   37: istore #4
    //   39: dload_2
    //   40: dconst_0
    //   41: dcmpl
    //   42: ifne -> 48
    //   45: iconst_1
    //   46: istore #4
    //   48: aload_0
    //   49: monitorexit
    //   50: iload #4
    //   52: ireturn
    //   53: astore #6
    //   55: aload_0
    //   56: monitorexit
    //   57: aload #6
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	53	finally
    //   18	35	53	finally
  }
  
  public int getMotorTargetPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: new com/qualcomm/hardware/lynx/commands/core/LynxGetMotorTargetPositionCommand
    //   10: dup
    //   11: aload_0
    //   12: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   15: iload_1
    //   16: iconst_0
    //   17: iadd
    //   18: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   21: astore_2
    //   22: aload_2
    //   23: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   26: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetMotorTargetPositionResponse
    //   29: invokevirtual getTarget : ()I
    //   32: istore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: iload_1
    //   36: ireturn
    //   37: astore_2
    //   38: goto -> 46
    //   41: astore_2
    //   42: goto -> 46
    //   45: astore_2
    //   46: aload_0
    //   47: aload_2
    //   48: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   51: pop
    //   52: iconst_0
    //   53: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   56: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   59: checkcast java/lang/Integer
    //   62: invokevirtual intValue : ()I
    //   65: istore_1
    //   66: aload_0
    //   67: monitorexit
    //   68: iload_1
    //   69: ireturn
    //   70: astore_2
    //   71: aload_0
    //   72: monitorexit
    //   73: aload_2
    //   74: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	70	finally
    //   22	33	45	java/lang/InterruptedException
    //   22	33	41	java/lang/RuntimeException
    //   22	33	37	com/qualcomm/hardware/lynx/LynxNackException
    //   22	33	70	finally
    //   46	66	70	finally
  }
  
  public MotorConfigurationType getMotorType(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   11: iload_1
    //   12: iconst_0
    //   13: iadd
    //   14: aaload
    //   15: getfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: areturn
    //   23: astore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_2
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	23	finally
  }
  
  public double getMotorVelocity(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: invokevirtual internalGetMotorTicksPerSecond : (I)I
    //   14: istore_1
    //   15: iload_1
    //   16: i2d
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
    //   2	15	22	finally
  }
  
  public double getMotorVelocity(int paramInt, AngleUnit paramAngleUnit) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: iload_1
    //   13: invokevirtual internalGetMotorTicksPerSecond : (I)I
    //   16: i2d
    //   17: aload_0
    //   18: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   21: iload_1
    //   22: aaload
    //   23: getfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   26: invokevirtual getTicksPerRev : ()D
    //   29: ddiv
    //   30: dstore_3
    //   31: aload_2
    //   32: invokevirtual getUnnormalized : ()Lorg/firstinspires/ftc/robotcore/external/navigation/UnnormalizedAngleUnit;
    //   35: dload_3
    //   36: ldc2_w 360.0
    //   39: dmul
    //   40: invokevirtual fromDegrees : (D)D
    //   43: dstore_3
    //   44: aload_0
    //   45: monitorexit
    //   46: dload_3
    //   47: dreturn
    //   48: astore_2
    //   49: aload_0
    //   50: monitorexit
    //   51: aload_2
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	48	finally
    //   11	44	48	finally
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
    //   9: iconst_0
    //   10: iadd
    //   11: invokevirtual internalGetZeroPowerBehavior : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   14: astore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_2
    //   18: areturn
    //   19: astore_2
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_2
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public PIDCoefficients getPIDCoefficients(int paramInt, DcMotor.RunMode paramRunMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: new com/qualcomm/hardware/lynx/commands/core/LynxGetMotorPIDControlLoopCoefficientsCommand
    //   14: dup
    //   15: aload_0
    //   16: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   19: iload_1
    //   20: aload_2
    //   21: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)V
    //   24: astore_3
    //   25: aload_3
    //   26: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   29: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetMotorPIDControlLoopCoefficientsResponse
    //   32: astore_3
    //   33: new com/qualcomm/robotcore/hardware/PIDCoefficients
    //   36: dup
    //   37: aload_3
    //   38: invokevirtual getP : ()I
    //   41: invokestatic externalCoefficientFromInternal : (I)D
    //   44: aload_3
    //   45: invokevirtual getI : ()I
    //   48: invokestatic externalCoefficientFromInternal : (I)D
    //   51: aload_3
    //   52: invokevirtual getD : ()I
    //   55: invokestatic externalCoefficientFromInternal : (I)D
    //   58: invokespecial <init> : (DDD)V
    //   61: astore_3
    //   62: aload_0
    //   63: monitorexit
    //   64: aload_3
    //   65: areturn
    //   66: astore_2
    //   67: goto -> 71
    //   70: astore_2
    //   71: aload_0
    //   72: aload_2
    //   73: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   76: pop
    //   77: goto -> 133
    //   80: astore_3
    //   81: aload_3
    //   82: invokevirtual getNack : ()Lcom/qualcomm/hardware/lynx/commands/standard/LynxNack;
    //   85: invokevirtual getNackReasonCode : ()Lcom/qualcomm/hardware/lynx/commands/standard/LynxNack$ReasonCode;
    //   88: getstatic com/qualcomm/hardware/lynx/commands/standard/LynxNack$StandardReasonCode.PARAM2 : Lcom/qualcomm/hardware/lynx/commands/standard/LynxNack$StandardReasonCode;
    //   91: if_acmpne -> 127
    //   94: aload_0
    //   95: iload_1
    //   96: iconst_0
    //   97: iadd
    //   98: aload_2
    //   99: invokevirtual getPIDFCoefficients : (ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)Lcom/qualcomm/robotcore/hardware/PIDFCoefficients;
    //   102: astore_2
    //   103: new com/qualcomm/robotcore/hardware/PIDCoefficients
    //   106: dup
    //   107: aload_2
    //   108: getfield p : D
    //   111: aload_2
    //   112: getfield i : D
    //   115: aload_2
    //   116: getfield d : D
    //   119: invokespecial <init> : (DDD)V
    //   122: astore_2
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_2
    //   126: areturn
    //   127: aload_0
    //   128: aload_3
    //   129: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   132: pop
    //   133: new com/qualcomm/robotcore/hardware/PIDCoefficients
    //   136: dup
    //   137: invokespecial <init> : ()V
    //   140: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   143: checkcast com/qualcomm/robotcore/hardware/PIDCoefficients
    //   146: astore_2
    //   147: aload_0
    //   148: monitorexit
    //   149: aload_2
    //   150: areturn
    //   151: astore_2
    //   152: aload_0
    //   153: monitorexit
    //   154: aload_2
    //   155: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	151	finally
    //   11	25	151	finally
    //   25	62	80	com/qualcomm/hardware/lynx/LynxNackException
    //   25	62	70	java/lang/InterruptedException
    //   25	62	66	java/lang/RuntimeException
    //   25	62	151	finally
    //   71	77	151	finally
    //   81	123	151	finally
    //   127	133	151	finally
    //   133	147	151	finally
  }
  
  public PIDFCoefficients getPIDFCoefficients(int paramInt, DcMotor.RunMode paramRunMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   6: ldc_w com/qualcomm/hardware/lynx/commands/core/LynxGetMotorPIDFControlLoopCoefficientsCommand
    //   9: invokeinterface isCommandSupported : (Ljava/lang/Class;)Z
    //   14: ifeq -> 126
    //   17: aload_0
    //   18: iload_1
    //   19: invokespecial validateMotor : (I)V
    //   22: new com/qualcomm/hardware/lynx/commands/core/LynxGetMotorPIDFControlLoopCoefficientsCommand
    //   25: dup
    //   26: aload_0
    //   27: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   30: iload_1
    //   31: iconst_0
    //   32: iadd
    //   33: aload_2
    //   34: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)V
    //   37: astore_2
    //   38: aload_2
    //   39: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   42: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetMotorPIDFControlLoopCoefficientsResponse
    //   45: astore_2
    //   46: new com/qualcomm/robotcore/hardware/PIDFCoefficients
    //   49: dup
    //   50: aload_2
    //   51: invokevirtual getP : ()I
    //   54: invokestatic externalCoefficientFromInternal : (I)D
    //   57: aload_2
    //   58: invokevirtual getI : ()I
    //   61: invokestatic externalCoefficientFromInternal : (I)D
    //   64: aload_2
    //   65: invokevirtual getD : ()I
    //   68: invokestatic externalCoefficientFromInternal : (I)D
    //   71: aload_2
    //   72: invokevirtual getF : ()I
    //   75: invokestatic externalCoefficientFromInternal : (I)D
    //   78: aload_2
    //   79: invokevirtual getInternalMotorControlAlgorithm : ()Lcom/qualcomm/hardware/lynx/commands/core/LynxSetMotorPIDFControlLoopCoefficientsCommand$InternalMotorControlAlgorithm;
    //   82: invokevirtual toExternal : ()Lcom/qualcomm/robotcore/hardware/MotorControlAlgorithm;
    //   85: invokespecial <init> : (DDDDLcom/qualcomm/robotcore/hardware/MotorControlAlgorithm;)V
    //   88: astore_2
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_2
    //   92: areturn
    //   93: astore_2
    //   94: goto -> 102
    //   97: astore_2
    //   98: goto -> 102
    //   101: astore_2
    //   102: aload_0
    //   103: aload_2
    //   104: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   107: pop
    //   108: new com/qualcomm/robotcore/hardware/PIDFCoefficients
    //   111: dup
    //   112: invokespecial <init> : ()V
    //   115: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   118: checkcast com/qualcomm/robotcore/hardware/PIDFCoefficients
    //   121: astore_2
    //   122: aload_0
    //   123: monitorexit
    //   124: aload_2
    //   125: areturn
    //   126: new com/qualcomm/robotcore/hardware/PIDFCoefficients
    //   129: dup
    //   130: aload_0
    //   131: iload_1
    //   132: aload_2
    //   133: invokevirtual getPIDCoefficients : (ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)Lcom/qualcomm/robotcore/hardware/PIDCoefficients;
    //   136: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/PIDCoefficients;)V
    //   139: astore_2
    //   140: aload_0
    //   141: monitorexit
    //   142: aload_2
    //   143: areturn
    //   144: astore_2
    //   145: aload_0
    //   146: monitorexit
    //   147: aload_2
    //   148: athrow
    // Exception table:
    //   from	to	target	type
    //   2	38	144	finally
    //   38	89	101	java/lang/InterruptedException
    //   38	89	97	java/lang/RuntimeException
    //   38	89	93	com/qualcomm/hardware/lynx/LynxNackException
    //   38	89	144	finally
    //   102	122	144	finally
    //   126	140	144	finally
  }
  
  protected String getTag() {
    return "LynxMotor";
  }
  
  public void initializeHardware() throws RobotCoreException, InterruptedException {
    floatHardware();
    runWithoutEncoders();
    forgetLastKnown();
    for (int i = 0; i <= 3; i++)
      updateMotorParams(i); 
    reportPIDFControlLoopCoefficients();
  }
  
  protected DcMotor.RunMode internalGetMotorChannelMode(int paramInt) {
    DcMotor.RunMode runMode = (DcMotor.RunMode)(this.motors[paramInt]).lastKnownMode.getValue();
    if (runMode != null && runMode != DcMotor.RunMode.STOP_AND_RESET_ENCODER)
      return runMode; 
    LynxGetMotorChannelModeCommand lynxGetMotorChannelModeCommand = new LynxGetMotorChannelModeCommand(getModule(), paramInt);
    try {
      return ((LynxGetMotorChannelModeResponse)lynxGetMotorChannelModeCommand.sendReceive()).getMode();
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return LynxUsbUtil.<DcMotor.RunMode>makePlaceholderValue(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
  }
  
  double internalGetMotorPower(int paramInt) {
    Double double_ = (Double)(this.motors[paramInt]).lastKnownPower.getValue();
    if (double_ != null) {
      if (DEBUG)
        RobotLog.vv("LynxMotor", "getMotorPower(cached): mod=%d motor=%d power=%f", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), double_ }); 
      return double_.doubleValue();
    } 
    DcMotor.RunMode runMode = internalGetPublicMotorMode(paramInt);
    try {
      int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[runMode.ordinal()];
      if (i != 1 && i != 2) {
        i = ((LynxGetMotorConstantPowerResponse)(new LynxGetMotorConstantPowerCommand(getModule(), paramInt)).sendReceive()).getPower();
        Double double_2 = Double.valueOf(Range.scale(i, -32767.0D, 32767.0D, -1.0D, 1.0D));
        double_1 = double_2;
        if (DEBUG) {
          RobotLog.vv("LynxMotor", "getMotorPower: mod=%d motor=%d iPower=%d power=%f", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), Integer.valueOf(i), double_2 });
          double_1 = double_2;
        } 
      } else {
        i = ((LynxGetMotorTargetVelocityResponse)(new LynxGetMotorTargetVelocityCommand(getModule(), paramInt)).sendReceive()).getVelocity();
        Double double_2 = Double.valueOf(Math.signum(i) * Range.scale(Math.abs(i), 0.0D, getDefaultMaxMotorSpeed(paramInt), 0.0D, 1.0D));
        double_1 = double_2;
        if (DEBUG) {
          RobotLog.vv("LynxMotor", "getMotorPower: mod=%d motor=%d velocity=%d power=%f", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), Integer.valueOf(i), double_2 });
          double_1 = double_2;
        } 
      } 
      Double double_1 = Double.valueOf(Range.clip(double_1.doubleValue(), -1.0D, 1.0D));
      (this.motors[paramInt]).lastKnownPower.setValue(double_1);
      return double_1.doubleValue();
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Integer)LynxUsbUtil.<Integer>makePlaceholderValue(Integer.valueOf(0))).intValue();
  }
  
  int internalGetMotorTicksPerSecond(int paramInt) {
    LynxGetBulkInputDataCommand lynxGetBulkInputDataCommand = new LynxGetBulkInputDataCommand(getModule());
    if (getModule() instanceof LynxModule) {
      LynxModule lynxModule = (LynxModule)getModule();
      if (lynxModule.getBulkCachingMode() != LynxModule.BulkCachingMode.OFF) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("motorVelocity");
        stringBuilder.append(paramInt);
        return lynxModule.recordBulkCachingCommandIntent((LynxDekaInterfaceCommand<?>)lynxGetBulkInputDataCommand, stringBuilder.toString()).getMotorVelocity(paramInt);
      } 
    } 
    try {
      return ((LynxGetBulkInputDataResponse)lynxGetBulkInputDataCommand.sendReceive()).getVelocity(paramInt);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Integer)LynxUsbUtil.<Integer>makePlaceholderValue(Integer.valueOf(0))).intValue();
  }
  
  protected DcMotor.RunMode internalGetPublicMotorMode(int paramInt) {
    DcMotor.RunMode runMode = (DcMotor.RunMode)(this.motors[paramInt]).lastKnownMode.getValue();
    if (runMode != null)
      return runMode; 
    if ((this.motors[paramInt]).lastKnownMode.getNonTimedValue() == DcMotor.RunMode.STOP_AND_RESET_ENCODER)
      return DcMotor.RunMode.STOP_AND_RESET_ENCODER; 
    LynxGetMotorChannelModeCommand lynxGetMotorChannelModeCommand = new LynxGetMotorChannelModeCommand(getModule(), paramInt);
    try {
      DcMotor.RunMode runMode1 = ((LynxGetMotorChannelModeResponse)lynxGetMotorChannelModeCommand.sendReceive()).getMode();
      (this.motors[paramInt]).lastKnownMode.setValue(runMode1);
      return runMode1;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return LynxUsbUtil.<DcMotor.RunMode>makePlaceholderValue(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
  }
  
  DcMotor.ZeroPowerBehavior internalGetZeroPowerBehavior(int paramInt) {
    DcMotor.ZeroPowerBehavior zeroPowerBehavior = (DcMotor.ZeroPowerBehavior)(this.motors[paramInt]).lastKnownZeroPowerBehavior.getValue();
    if (zeroPowerBehavior != null)
      return zeroPowerBehavior; 
    LynxGetMotorChannelModeCommand lynxGetMotorChannelModeCommand = new LynxGetMotorChannelModeCommand(getModule(), paramInt);
    try {
      DcMotor.ZeroPowerBehavior zeroPowerBehavior1 = ((LynxGetMotorChannelModeResponse)lynxGetMotorChannelModeCommand.sendReceive()).getZeroPowerBehavior();
      (this.motors[paramInt]).lastKnownZeroPowerBehavior.setValue(zeroPowerBehavior1);
      return zeroPowerBehavior1;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return LynxUsbUtil.<DcMotor.ZeroPowerBehavior>makePlaceholderValue(DcMotor.ZeroPowerBehavior.BRAKE);
  }
  
  void internalSetMotorEnable(int paramInt, boolean paramBoolean) {
    if ((this.motors[paramInt]).lastKnownEnable.updateValue(Boolean.valueOf(paramBoolean))) {
      LynxSetMotorChannelEnableCommand lynxSetMotorChannelEnableCommand = new LynxSetMotorChannelEnableCommand(getModule(), paramInt, paramBoolean);
      try {
        if (DEBUG)
          RobotLog.vv("LynxMotor", "setMotorEnable mod=%d motor=%d enable=%s", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean).toString() }); 
        lynxSetMotorChannelEnableCommand.send();
        return;
      } catch (LynxNackException lynxNackException) {
        if (lynxNackException.getNack().getNackReasonCode() != LynxNack.StandardReasonCode.MOTOR_NOT_CONFIG_BEFORE_ENABLED) {
          handleException(lynxNackException);
          return;
        } 
        throw new TargetPositionNotSetException();
      } catch (InterruptedException interruptedException) {
        handleException(interruptedException);
        return;
      } catch (RuntimeException runtimeException) {
        handleException(runtimeException);
        return;
      } 
    } 
  }
  
  void internalSetMotorPower(int paramInt, double paramDouble) {
    internalSetMotorPower(paramInt, paramDouble, false);
  }
  
  void internalSetMotorPower(int paramInt, double paramDouble, boolean paramBoolean) {
    paramDouble = Range.clip(paramDouble, -1.0D, 1.0D);
    if ((this.motors[paramInt]).lastKnownPower.updateValue(Double.valueOf(paramDouble)) || paramBoolean) {
      LynxSetMotorTargetVelocityCommand lynxSetMotorTargetVelocityCommand;
      DcMotor.RunMode runMode = internalGetPublicMotorMode(paramInt);
      int i = null.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode[runMode.ordinal()];
      runMode = null;
      if (i != 1 && i != 2) {
        if (i != 3) {
          i = 0;
        } else {
          i = (int)Range.scale(paramDouble, -1.0D, 1.0D, -32767.0D, 32767.0D);
          LynxSetMotorConstantPowerCommand lynxSetMotorConstantPowerCommand = new LynxSetMotorConstantPowerCommand(getModule(), paramInt, i);
        } 
      } else {
        i = (int)(Math.signum(paramDouble) * Range.scale(Math.abs(paramDouble), 0.0D, 1.0D, 0.0D, getDefaultMaxMotorSpeed(paramInt)));
        lynxSetMotorTargetVelocityCommand = new LynxSetMotorTargetVelocityCommand(getModule(), paramInt, i);
      } 
      if (lynxSetMotorTargetVelocityCommand != null) {
        try {
          if (DEBUG)
            RobotLog.vv("LynxMotor", "setMotorPower: mod=%d motor=%d iPower=%d", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), Integer.valueOf(i) }); 
          lynxSetMotorTargetVelocityCommand.send();
          internalSetMotorEnable(paramInt, true);
          return;
        } catch (InterruptedException interruptedException) {
        
        } catch (RuntimeException runtimeException) {
        
        } catch (LynxNackException lynxNackException) {}
        handleException(lynxNackException);
      } 
    } 
  }
  
  protected boolean internalSetPIDFCoefficients(int paramInt, DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) {
    boolean bool;
    if (!(this.motors[paramInt]).originalPIDParams.containsKey(paramRunMode)) {
      PIDFCoefficients pIDFCoefficients = getPIDFCoefficients(paramInt + 0, paramRunMode);
      (this.motors[paramInt]).originalPIDParams.put(paramRunMode, new ExpansionHubMotorControllerParamsState(paramRunMode, pIDFCoefficients));
    } 
    int i = LynxSetMotorPIDControlLoopCoefficientsCommand.internalCoefficientFromExternal(paramPIDFCoefficients.p);
    int j = LynxSetMotorPIDControlLoopCoefficientsCommand.internalCoefficientFromExternal(paramPIDFCoefficients.i);
    int k = LynxSetMotorPIDControlLoopCoefficientsCommand.internalCoefficientFromExternal(paramPIDFCoefficients.d);
    int m = LynxSetMotorPIDControlLoopCoefficientsCommand.internalCoefficientFromExternal(paramPIDFCoefficients.f);
    if (paramRunMode == DcMotor.RunMode.RUN_TO_POSITION && paramPIDFCoefficients.algorithm != MotorControlAlgorithm.LegacyPID && (paramPIDFCoefficients.i != 0.0D || paramPIDFCoefficients.d != 0.0D || paramPIDFCoefficients.f != 0.0D)) {
      RobotLog.ww("LynxMotor", "using unreasonable coefficients for RUN_TO_POSITION: setPIDFCoefficients(%d, %s, %s)", new Object[] { Integer.valueOf(paramInt + 0), paramRunMode, paramPIDFCoefficients });
      bool = false;
    } else {
      bool = true;
    } 
    if (bool) {
      LynxSetMotorPIDFControlLoopCoefficientsCommand.InternalMotorControlAlgorithm internalMotorControlAlgorithm;
      if (getModule().isCommandSupported((Class)LynxSetMotorPIDFControlLoopCoefficientsCommand.class)) {
        internalMotorControlAlgorithm = LynxSetMotorPIDFControlLoopCoefficientsCommand.InternalMotorControlAlgorithm.fromExternal(paramPIDFCoefficients.algorithm);
        LynxSetMotorPIDFControlLoopCoefficientsCommand lynxSetMotorPIDFControlLoopCoefficientsCommand = new LynxSetMotorPIDFControlLoopCoefficientsCommand(getModule(), paramInt, paramRunMode, i, j, k, m, internalMotorControlAlgorithm);
        try {
          lynxSetMotorPIDFControlLoopCoefficientsCommand.send();
        } catch (InterruptedException interruptedException) {
          return handleException(interruptedException);
        } catch (RuntimeException runtimeException) {
        
        } catch (LynxNackException lynxNackException) {}
      } else if (m == 0 && ((PIDFCoefficients)internalMotorControlAlgorithm).algorithm == MotorControlAlgorithm.LegacyPID) {
        LynxSetMotorPIDControlLoopCoefficientsCommand lynxSetMotorPIDControlLoopCoefficientsCommand = new LynxSetMotorPIDControlLoopCoefficientsCommand(getModule(), paramInt, (DcMotor.RunMode)lynxNackException, i, j, k);
        try {
          lynxSetMotorPIDControlLoopCoefficientsCommand.send();
        } catch (InterruptedException interruptedException) {
          return handleException(interruptedException);
        } catch (RuntimeException runtimeException) {
        
        } catch (LynxNackException lynxNackException1) {}
      } else {
        RobotLog.ww("LynxMotor", "not supported: setPIDFCoefficients(%d, %s, %s)", new Object[] { Integer.valueOf(paramInt + 0), lynxNackException1, internalMotorControlAlgorithm });
        return false;
      } 
    } 
    return bool;
  }
  
  void internalSetZeroPowerBehavior(int paramInt, DcMotor.ZeroPowerBehavior paramZeroPowerBehavior) {
    if ((this.motors[paramInt]).lastKnownZeroPowerBehavior.updateValue(paramZeroPowerBehavior)) {
      DcMotor.RunMode runMode = internalGetMotorChannelMode(paramInt);
      LynxSetMotorChannelModeCommand lynxSetMotorChannelModeCommand = new LynxSetMotorChannelModeCommand(getModule(), paramInt, runMode, paramZeroPowerBehavior);
      try {
        if (DEBUG)
          RobotLog.vv("LynxMotor", "setZeroBehavior mod=%d motor=%d zero=%s", new Object[] { Integer.valueOf(getModuleAddress()), Integer.valueOf(paramInt), paramZeroPowerBehavior.toString() }); 
        lynxSetMotorChannelModeCommand.send();
        return;
      } catch (InterruptedException interruptedException) {
      
      } catch (RuntimeException runtimeException) {
      
      } catch (LynxNackException lynxNackException) {}
      handleException(lynxNackException);
    } 
  }
  
  public boolean isBusy(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: new com/qualcomm/hardware/lynx/commands/core/LynxIsMotorAtTargetCommand
    //   14: dup
    //   15: aload_0
    //   16: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   19: iload_1
    //   20: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   23: astore_3
    //   24: aload_0
    //   25: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   28: instanceof com/qualcomm/hardware/lynx/LynxModule
    //   31: ifeq -> 69
    //   34: aload_0
    //   35: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   38: checkcast com/qualcomm/hardware/lynx/LynxModule
    //   41: astore #4
    //   43: aload #4
    //   45: invokevirtual getBulkCachingMode : ()Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   48: getstatic com/qualcomm/hardware/lynx/LynxModule$BulkCachingMode.OFF : Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   51: if_acmpeq -> 69
    //   54: aload #4
    //   56: aload_3
    //   57: invokevirtual recordBulkCachingCommandIntent : (Lcom/qualcomm/hardware/lynx/commands/core/LynxDekaInterfaceCommand;)Lcom/qualcomm/hardware/lynx/LynxModule$BulkData;
    //   60: iload_1
    //   61: invokevirtual isMotorBusy : (I)Z
    //   64: istore_2
    //   65: aload_0
    //   66: monitorexit
    //   67: iload_2
    //   68: ireturn
    //   69: aload_3
    //   70: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   73: checkcast com/qualcomm/hardware/lynx/commands/core/LynxIsMotorAtTargetResponse
    //   76: invokevirtual isAtTarget : ()Z
    //   79: istore_2
    //   80: aload_0
    //   81: monitorexit
    //   82: iload_2
    //   83: iconst_1
    //   84: ixor
    //   85: ireturn
    //   86: astore_3
    //   87: goto -> 95
    //   90: astore_3
    //   91: goto -> 95
    //   94: astore_3
    //   95: aload_0
    //   96: aload_3
    //   97: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   100: pop
    //   101: iconst_0
    //   102: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   105: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   108: checkcast java/lang/Boolean
    //   111: invokevirtual booleanValue : ()Z
    //   114: istore_2
    //   115: aload_0
    //   116: monitorexit
    //   117: iload_2
    //   118: ireturn
    //   119: astore_3
    //   120: aload_0
    //   121: monitorexit
    //   122: aload_3
    //   123: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	119	finally
    //   11	65	119	finally
    //   69	80	94	java/lang/InterruptedException
    //   69	80	90	java/lang/RuntimeException
    //   69	80	86	com/qualcomm/hardware/lynx/LynxNackException
    //   69	80	119	finally
    //   95	115	119	finally
  }
  
  public boolean isMotorEnabled(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   15: iload_1
    //   16: aaload
    //   17: getfield lastKnownEnable : Lcom/qualcomm/robotcore/util/LastKnown;
    //   20: invokevirtual getValue : ()Ljava/lang/Object;
    //   23: checkcast java/lang/Boolean
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 40
    //   31: aload_3
    //   32: invokevirtual booleanValue : ()Z
    //   35: istore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: iload_2
    //   39: ireturn
    //   40: new com/qualcomm/hardware/lynx/commands/core/LynxGetMotorChannelEnableCommand
    //   43: dup
    //   44: aload_0
    //   45: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   48: iload_1
    //   49: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   52: astore_3
    //   53: aload_3
    //   54: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   57: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetMotorChannelEnableResponse
    //   60: invokevirtual isEnabled : ()Z
    //   63: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   66: astore_3
    //   67: aload_0
    //   68: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   71: iload_1
    //   72: aaload
    //   73: getfield lastKnownEnable : Lcom/qualcomm/robotcore/util/LastKnown;
    //   76: aload_3
    //   77: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   80: pop
    //   81: aload_3
    //   82: invokevirtual booleanValue : ()Z
    //   85: istore_2
    //   86: aload_0
    //   87: monitorexit
    //   88: iload_2
    //   89: ireturn
    //   90: astore_3
    //   91: goto -> 99
    //   94: astore_3
    //   95: goto -> 99
    //   98: astore_3
    //   99: aload_0
    //   100: aload_3
    //   101: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   104: pop
    //   105: iconst_1
    //   106: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   109: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   112: checkcast java/lang/Boolean
    //   115: invokevirtual booleanValue : ()Z
    //   118: istore_2
    //   119: aload_0
    //   120: monitorexit
    //   121: iload_2
    //   122: ireturn
    //   123: astore_3
    //   124: aload_0
    //   125: monitorexit
    //   126: aload_3
    //   127: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	123	finally
    //   11	27	123	finally
    //   31	36	123	finally
    //   40	53	123	finally
    //   53	86	98	java/lang/InterruptedException
    //   53	86	94	java/lang/RuntimeException
    //   53	86	90	com/qualcomm/hardware/lynx/LynxNackException
    //   53	86	123	finally
    //   99	119	123	finally
  }
  
  public boolean isMotorOverCurrent(int paramInt) {
    LynxGetBulkInputDataCommand lynxGetBulkInputDataCommand = new LynxGetBulkInputDataCommand(getModule());
    if (getModule() instanceof LynxModule) {
      LynxModule lynxModule = (LynxModule)getModule();
      if (lynxModule.getBulkCachingMode() != LynxModule.BulkCachingMode.OFF) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("motorOverCurrent");
        stringBuilder.append(paramInt);
        return lynxModule.recordBulkCachingCommandIntent((LynxDekaInterfaceCommand<?>)lynxGetBulkInputDataCommand, stringBuilder.toString()).isMotorOverCurrent(paramInt);
      } 
    } 
    try {
      return ((LynxGetBulkInputDataResponse)lynxGetBulkInputDataCommand.sendReceive()).isOverCurrent(paramInt);
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Boolean)LynxUsbUtil.<Boolean>makePlaceholderValue(Boolean.valueOf(false))).booleanValue();
  }
  
  protected void rememberPIDParams(int paramInt, ExpansionHubMotorControllerParamsState paramExpansionHubMotorControllerParamsState) {
    (this.motors[paramInt]).desiredPIDParams.put(paramExpansionHubMotorControllerParamsState.mode, paramExpansionHubMotorControllerParamsState);
  }
  
  public void resetDeviceConfigurationForOpMode(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   15: iload_1
    //   16: aaload
    //   17: getfield desiredPIDParams : Ljava/util/Map;
    //   20: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   23: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   28: pop
    //   29: aload_0
    //   30: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   33: iload_1
    //   34: aaload
    //   35: getfield desiredPIDParams : Ljava/util/Map;
    //   38: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_USING_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   41: invokeinterface remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   46: pop
    //   47: aload_0
    //   48: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   51: iload_1
    //   52: aaload
    //   53: getfield originalPIDParams : Ljava/util/Map;
    //   56: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   59: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   64: ifeq -> 102
    //   67: aload_0
    //   68: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   71: iload_1
    //   72: aaload
    //   73: getfield desiredPIDParams : Ljava/util/Map;
    //   76: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   79: aload_0
    //   80: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   83: iload_1
    //   84: aaload
    //   85: getfield originalPIDParams : Ljava/util/Map;
    //   88: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_TO_POSITION : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   91: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   96: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   101: pop
    //   102: aload_0
    //   103: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   106: iload_1
    //   107: aaload
    //   108: getfield originalPIDParams : Ljava/util/Map;
    //   111: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_USING_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   114: invokeinterface containsKey : (Ljava/lang/Object;)Z
    //   119: ifeq -> 157
    //   122: aload_0
    //   123: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   126: iload_1
    //   127: aaload
    //   128: getfield desiredPIDParams : Ljava/util/Map;
    //   131: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_USING_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   134: aload_0
    //   135: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   138: iload_1
    //   139: aaload
    //   140: getfield originalPIDParams : Ljava/util/Map;
    //   143: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_USING_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   146: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   151: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   156: pop
    //   157: aload_0
    //   158: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   161: iload_1
    //   162: aaload
    //   163: getfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   166: ifnull -> 188
    //   169: aload_0
    //   170: iload_1
    //   171: iconst_0
    //   172: iadd
    //   173: aload_0
    //   174: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   177: iload_1
    //   178: aaload
    //   179: getfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   182: invokevirtual setMotorType : (ILcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;)V
    //   185: goto -> 193
    //   188: aload_0
    //   189: iload_1
    //   190: invokevirtual updateMotorParams : (I)V
    //   193: aload_0
    //   194: monitorexit
    //   195: return
    //   196: astore_2
    //   197: aload_0
    //   198: monitorexit
    //   199: aload_2
    //   200: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	196	finally
    //   11	102	196	finally
    //   102	157	196	finally
    //   157	185	196	finally
    //   188	193	196	finally
  }
  
  public void setMotorCurrentAlert(int paramInt, double paramDouble, CurrentUnit paramCurrentUnit) {
    LynxSetMotorChannelCurrentAlertLevelCommand lynxSetMotorChannelCurrentAlertLevelCommand = new LynxSetMotorChannelCurrentAlertLevelCommand(getModule(), paramInt, (int)Math.round(paramCurrentUnit.toMilliAmps(paramDouble)));
    try {
      lynxSetMotorChannelCurrentAlertLevelCommand.send();
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
  }
  
  public void setMotorDisable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: iconst_0
    //   12: invokevirtual internalSetMotorEnable : (IZ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  public void setMotorEnable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: iconst_1
    //   12: invokevirtual internalSetMotorEnable : (IZ)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  public void setMotorMode(int paramInt, DcMotor.RunMode paramRunMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   15: iload_1
    //   16: aaload
    //   17: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   20: aload_2
    //   21: invokevirtual isValue : (Ljava/lang/Object;)Z
    //   24: ifne -> 221
    //   27: aload_0
    //   28: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   31: iload_1
    //   32: aaload
    //   33: getfield lastKnownPower : Lcom/qualcomm/robotcore/util/LastKnown;
    //   36: invokevirtual getNonTimedValue : ()Ljava/lang/Object;
    //   39: checkcast java/lang/Double
    //   42: astore #4
    //   44: aload #4
    //   46: astore_3
    //   47: aload #4
    //   49: ifnonnull -> 61
    //   52: aload_0
    //   53: iload_1
    //   54: invokevirtual internalGetMotorPower : (I)D
    //   57: invokestatic valueOf : (D)Ljava/lang/Double;
    //   60: astore_3
    //   61: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.UNKNOWN : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   64: astore #4
    //   66: aload_2
    //   67: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.STOP_AND_RESET_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   70: if_acmpne -> 96
    //   73: aload_0
    //   74: iload_1
    //   75: dconst_0
    //   76: invokevirtual internalSetMotorPower : (ID)V
    //   79: new com/qualcomm/hardware/lynx/commands/core/LynxResetMotorEncoderCommand
    //   82: dup
    //   83: aload_0
    //   84: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   87: iload_1
    //   88: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   91: astore #5
    //   93: goto -> 120
    //   96: aload_0
    //   97: iload_1
    //   98: invokevirtual internalGetZeroPowerBehavior : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   101: astore #4
    //   103: new com/qualcomm/hardware/lynx/commands/core/LynxSetMotorChannelModeCommand
    //   106: dup
    //   107: aload_0
    //   108: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   111: iload_1
    //   112: aload_2
    //   113: aload #4
    //   115: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;)V
    //   118: astore #5
    //   120: getstatic com/qualcomm/hardware/lynx/LynxDcMotorController.DEBUG : Z
    //   123: ifeq -> 174
    //   126: ldc 'LynxMotor'
    //   128: ldc_w 'setMotorChannelMode: mod=%d motor=%d mode=%s power=%f zero=%s'
    //   131: iconst_5
    //   132: anewarray java/lang/Object
    //   135: dup
    //   136: iconst_0
    //   137: aload_0
    //   138: invokevirtual getModuleAddress : ()I
    //   141: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   144: aastore
    //   145: dup
    //   146: iconst_1
    //   147: iload_1
    //   148: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   151: aastore
    //   152: dup
    //   153: iconst_2
    //   154: aload_2
    //   155: invokevirtual toString : ()Ljava/lang/String;
    //   158: aastore
    //   159: dup
    //   160: iconst_3
    //   161: aload_3
    //   162: aastore
    //   163: dup
    //   164: iconst_4
    //   165: aload #4
    //   167: invokevirtual toString : ()Ljava/lang/String;
    //   170: aastore
    //   171: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   174: aload #5
    //   176: invokevirtual send : ()V
    //   179: aload_0
    //   180: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   183: iload_1
    //   184: aaload
    //   185: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   188: aload_2
    //   189: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   192: pop
    //   193: aload_0
    //   194: iload_1
    //   195: aload_3
    //   196: invokevirtual doubleValue : ()D
    //   199: iconst_1
    //   200: invokevirtual internalSetMotorPower : (IDZ)V
    //   203: goto -> 221
    //   206: astore_2
    //   207: goto -> 215
    //   210: astore_2
    //   211: goto -> 215
    //   214: astore_2
    //   215: aload_0
    //   216: aload_2
    //   217: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   220: pop
    //   221: aload_0
    //   222: monitorexit
    //   223: return
    //   224: astore_2
    //   225: aload_0
    //   226: monitorexit
    //   227: aload_2
    //   228: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	224	finally
    //   11	44	224	finally
    //   52	61	224	finally
    //   61	93	224	finally
    //   96	120	224	finally
    //   120	174	214	java/lang/InterruptedException
    //   120	174	210	java/lang/RuntimeException
    //   120	174	206	com/qualcomm/hardware/lynx/LynxNackException
    //   120	174	224	finally
    //   174	203	214	java/lang/InterruptedException
    //   174	203	210	java/lang/RuntimeException
    //   174	203	206	com/qualcomm/hardware/lynx/LynxNackException
    //   174	203	224	finally
    //   215	221	224	finally
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
    //   9: iconst_0
    //   10: iadd
    //   11: dload_2
    //   12: invokevirtual internalSetMotorPower : (ID)V
    //   15: aload_0
    //   16: monitorexit
    //   17: return
    //   18: astore #4
    //   20: aload_0
    //   21: monitorexit
    //   22: aload #4
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	18	finally
  }
  
  protected void setMotorPowerFloat(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: iload_1
    //   13: getstatic com/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior.FLOAT : Lcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;
    //   16: invokevirtual internalSetZeroPowerBehavior : (ILcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;)V
    //   19: aload_0
    //   20: iload_1
    //   21: dconst_0
    //   22: invokevirtual internalSetMotorPower : (ID)V
    //   25: aload_0
    //   26: monitorexit
    //   27: return
    //   28: astore_2
    //   29: aload_0
    //   30: monitorexit
    //   31: aload_2
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	28	finally
    //   11	25	28	finally
  }
  
  public void setMotorTargetPosition(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: iload_2
    //   5: iconst_5
    //   6: invokevirtual setMotorTargetPosition : (III)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_3
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_3
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void setMotorTargetPosition(int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: new com/qualcomm/hardware/lynx/commands/core/LynxSetMotorTargetPositionCommand
    //   10: dup
    //   11: aload_0
    //   12: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   15: iload_1
    //   16: iconst_0
    //   17: iadd
    //   18: iload_2
    //   19: iload_3
    //   20: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;III)V
    //   23: astore #4
    //   25: aload #4
    //   27: invokevirtual send : ()V
    //   30: goto -> 52
    //   33: astore #4
    //   35: goto -> 45
    //   38: astore #4
    //   40: goto -> 45
    //   43: astore #4
    //   45: aload_0
    //   46: aload #4
    //   48: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   51: pop
    //   52: aload_0
    //   53: monitorexit
    //   54: return
    //   55: astore #4
    //   57: aload_0
    //   58: monitorexit
    //   59: aload #4
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	25	55	finally
    //   25	30	43	java/lang/InterruptedException
    //   25	30	38	java/lang/RuntimeException
    //   25	30	33	com/qualcomm/hardware/lynx/LynxNackException
    //   25	30	55	finally
    //   45	52	55	finally
  }
  
  public void setMotorType(int paramInt, MotorConfigurationType paramMotorConfigurationType) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   15: iload_1
    //   16: aaload
    //   17: aload_2
    //   18: putfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   21: aload_0
    //   22: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   25: iload_1
    //   26: aaload
    //   27: getfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   30: ifnonnull -> 43
    //   33: aload_0
    //   34: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   37: iload_1
    //   38: aaload
    //   39: aload_2
    //   40: putfield internalMotorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   43: aload_2
    //   44: invokevirtual hasExpansionHubVelocityParams : ()Z
    //   47: ifeq -> 59
    //   50: aload_0
    //   51: iload_1
    //   52: aload_2
    //   53: invokevirtual getHubVelocityParams : ()Lcom/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState;
    //   56: invokevirtual rememberPIDParams : (ILcom/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState;)V
    //   59: aload_2
    //   60: invokevirtual hasExpansionHubPositionParams : ()Z
    //   63: ifeq -> 75
    //   66: aload_0
    //   67: iload_1
    //   68: aload_2
    //   69: invokevirtual getHubPositionParams : ()Lcom/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState;
    //   72: invokevirtual rememberPIDParams : (ILcom/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState;)V
    //   75: aload_0
    //   76: iload_1
    //   77: invokevirtual updateMotorParams : (I)V
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: astore_2
    //   84: aload_0
    //   85: monitorexit
    //   86: aload_2
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	83	finally
    //   11	43	83	finally
    //   43	59	83	finally
    //   59	75	83	finally
    //   75	80	83	finally
  }
  
  public void setMotorVelocity(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/qualcomm/hardware/lynx/LynxDcMotorController$1.$SwitchMap$com$qualcomm$robotcore$hardware$DcMotor$RunMode : [I
    //   5: aload_0
    //   6: iload_1
    //   7: invokevirtual getMotorMode : (I)Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   10: invokevirtual ordinal : ()I
    //   13: iaload
    //   14: istore #4
    //   16: iload #4
    //   18: iconst_1
    //   19: if_icmpeq -> 36
    //   22: iload #4
    //   24: iconst_2
    //   25: if_icmpeq -> 36
    //   28: aload_0
    //   29: iload_1
    //   30: getstatic com/qualcomm/robotcore/hardware/DcMotor$RunMode.RUN_USING_ENCODER : Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   33: invokevirtual setMotorMode : (ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)V
    //   36: aload_0
    //   37: iload_1
    //   38: invokespecial validateMotor : (I)V
    //   41: iload_1
    //   42: iconst_0
    //   43: iadd
    //   44: istore_1
    //   45: dload_2
    //   46: invokestatic round : (D)J
    //   49: l2i
    //   50: sipush #-32767
    //   53: sipush #32767
    //   56: invokestatic clip : (III)I
    //   59: istore #4
    //   61: new com/qualcomm/hardware/lynx/commands/core/LynxSetMotorTargetVelocityCommand
    //   64: dup
    //   65: aload_0
    //   66: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   69: iload_1
    //   70: iload #4
    //   72: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;II)V
    //   75: astore #5
    //   77: getstatic com/qualcomm/hardware/lynx/LynxDcMotorController.DEBUG : Z
    //   80: ifeq -> 120
    //   83: ldc 'LynxMotor'
    //   85: ldc_w 'setMotorVelocity: mod=%d motor=%d iPower=%d'
    //   88: iconst_3
    //   89: anewarray java/lang/Object
    //   92: dup
    //   93: iconst_0
    //   94: aload_0
    //   95: invokevirtual getModuleAddress : ()I
    //   98: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   101: aastore
    //   102: dup
    //   103: iconst_1
    //   104: iload_1
    //   105: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   108: aastore
    //   109: dup
    //   110: iconst_2
    //   111: iload #4
    //   113: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   116: aastore
    //   117: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   120: aload #5
    //   122: invokevirtual send : ()V
    //   125: aload_0
    //   126: iload_1
    //   127: iconst_1
    //   128: invokevirtual internalSetMotorEnable : (IZ)V
    //   131: goto -> 153
    //   134: astore #5
    //   136: goto -> 146
    //   139: astore #5
    //   141: goto -> 146
    //   144: astore #5
    //   146: aload_0
    //   147: aload #5
    //   149: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   152: pop
    //   153: aload_0
    //   154: monitorexit
    //   155: return
    //   156: astore #5
    //   158: aload_0
    //   159: monitorexit
    //   160: aload #5
    //   162: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	156	finally
    //   28	36	156	finally
    //   36	41	156	finally
    //   45	61	156	finally
    //   61	120	144	java/lang/InterruptedException
    //   61	120	139	java/lang/RuntimeException
    //   61	120	134	com/qualcomm/hardware/lynx/LynxNackException
    //   61	120	156	finally
    //   120	131	144	java/lang/InterruptedException
    //   120	131	139	java/lang/RuntimeException
    //   120	131	134	com/qualcomm/hardware/lynx/LynxNackException
    //   120	131	156	finally
    //   146	153	156	finally
  }
  
  public void setMotorVelocity(int paramInt, double paramDouble, AngleUnit paramAngleUnit) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateMotor : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: getstatic org/firstinspires/ftc/robotcore/external/navigation/UnnormalizedAngleUnit.DEGREES : Lorg/firstinspires/ftc/robotcore/external/navigation/UnnormalizedAngleUnit;
    //   14: aload #4
    //   16: invokevirtual getUnnormalized : ()Lorg/firstinspires/ftc/robotcore/external/navigation/UnnormalizedAngleUnit;
    //   19: dload_2
    //   20: invokevirtual fromUnit : (Lorg/firstinspires/ftc/robotcore/external/navigation/UnnormalizedAngleUnit;D)D
    //   23: ldc2_w 360.0
    //   26: ddiv
    //   27: dstore_2
    //   28: aload_0
    //   29: iload_1
    //   30: iconst_0
    //   31: iadd
    //   32: aload_0
    //   33: getfield motors : [Lcom/qualcomm/hardware/lynx/LynxDcMotorController$MotorProperties;
    //   36: iload_1
    //   37: aaload
    //   38: getfield motorType : Lcom/qualcomm/robotcore/hardware/configuration/typecontainers/MotorConfigurationType;
    //   41: invokevirtual getTicksPerRev : ()D
    //   44: dload_2
    //   45: dmul
    //   46: invokevirtual setMotorVelocity : (ID)V
    //   49: aload_0
    //   50: monitorexit
    //   51: return
    //   52: astore #4
    //   54: aload_0
    //   55: monitorexit
    //   56: aload #4
    //   58: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	52	finally
    //   11	49	52	finally
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
    //   11: if_acmpeq -> 25
    //   14: aload_0
    //   15: iload_1
    //   16: iconst_0
    //   17: iadd
    //   18: aload_2
    //   19: invokevirtual internalSetZeroPowerBehavior : (ILcom/qualcomm/robotcore/hardware/DcMotor$ZeroPowerBehavior;)V
    //   22: aload_0
    //   23: monitorexit
    //   24: return
    //   25: new java/lang/IllegalArgumentException
    //   28: dup
    //   29: ldc_w 'zeroPowerBehavior may not be UNKNOWN'
    //   32: invokespecial <init> : (Ljava/lang/String;)V
    //   35: athrow
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	36	finally
    //   25	36	36	finally
  }
  
  public void setPIDCoefficients(int paramInt, DcMotor.RunMode paramRunMode, PIDCoefficients paramPIDCoefficients) {
    setPIDFCoefficients(paramInt, paramRunMode, new PIDFCoefficients(paramPIDCoefficients));
  }
  
  public void setPIDFCoefficients(int paramInt, DcMotor.RunMode paramRunMode, PIDFCoefficients paramPIDFCoefficients) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: aload_2
    //   5: invokespecial validatePIDMode : (ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;)V
    //   8: aload_0
    //   9: iload_1
    //   10: invokespecial validateMotor : (I)V
    //   13: iload_1
    //   14: iconst_0
    //   15: iadd
    //   16: istore_1
    //   17: aload_2
    //   18: invokevirtual migrate : ()Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;
    //   21: astore_2
    //   22: aload_0
    //   23: iload_1
    //   24: new com/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState
    //   27: dup
    //   28: aload_2
    //   29: aload_3
    //   30: invokespecial <init> : (Lcom/qualcomm/robotcore/hardware/DcMotor$RunMode;Lcom/qualcomm/robotcore/hardware/PIDFCoefficients;)V
    //   33: invokevirtual rememberPIDParams : (ILcom/qualcomm/robotcore/hardware/configuration/ExpansionHubMotorControllerParamsState;)V
    //   36: aload_0
    //   37: iload_1
    //   38: aload_2
    //   39: aload_3
    //   40: invokevirtual internalSetPIDFCoefficients : (ILcom/qualcomm/robotcore/hardware/DcMotor$RunMode;Lcom/qualcomm/robotcore/hardware/PIDFCoefficients;)Z
    //   43: istore #4
    //   45: iload #4
    //   47: ifeq -> 53
    //   50: aload_0
    //   51: monitorexit
    //   52: return
    //   53: new java/lang/UnsupportedOperationException
    //   56: dup
    //   57: ldc_w 'setting of pidf coefficents not supported: motor=%d mode=%s pidf=%s'
    //   60: iconst_3
    //   61: anewarray java/lang/Object
    //   64: dup
    //   65: iconst_0
    //   66: iload_1
    //   67: iconst_0
    //   68: iadd
    //   69: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   72: aastore
    //   73: dup
    //   74: iconst_1
    //   75: aload_2
    //   76: aastore
    //   77: dup
    //   78: iconst_2
    //   79: aload_3
    //   80: aastore
    //   81: invokestatic formatForUser : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   84: invokespecial <init> : (Ljava/lang/String;)V
    //   87: athrow
    //   88: astore_2
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_2
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	88	finally
    //   17	45	88	finally
    //   53	88	88	finally
  }
  
  protected void updateMotorParams(int paramInt) {
    for (ExpansionHubMotorControllerParamsState expansionHubMotorControllerParamsState : (this.motors[paramInt]).desiredPIDParams.values()) {
      if (!expansionHubMotorControllerParamsState.isDefault())
        internalSetPIDFCoefficients(paramInt, expansionHubMotorControllerParamsState.mode, expansionHubMotorControllerParamsState.getPidfCoefficients()); 
    } 
  }
  
  protected class MotorProperties {
    Map<DcMotor.RunMode, ExpansionHubMotorControllerParamsState> desiredPIDParams = new ConcurrentHashMap<DcMotor.RunMode, ExpansionHubMotorControllerParamsState>();
    
    MotorConfigurationType internalMotorType = null;
    
    LastKnown<Double> lastKnownCurrentAlert = new LastKnown();
    
    LastKnown<Boolean> lastKnownEnable = new LastKnown();
    
    LastKnown<DcMotor.RunMode> lastKnownMode = new LastKnown();
    
    LastKnown<Double> lastKnownPower = new LastKnown();
    
    LastKnown<Integer> lastKnownTargetPosition = new LastKnown();
    
    LastKnown<DcMotor.ZeroPowerBehavior> lastKnownZeroPowerBehavior = new LastKnown();
    
    MotorConfigurationType motorType = MotorConfigurationType.getUnspecifiedMotorType();
    
    Map<DcMotor.RunMode, ExpansionHubMotorControllerParamsState> originalPIDParams = new ConcurrentHashMap<DcMotor.RunMode, ExpansionHubMotorControllerParamsState>();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxDcMotorController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */