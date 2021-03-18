package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxGetServoEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetServoEnableResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxSetServoEnableCommand;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.ServoConfigurationType;
import com.qualcomm.robotcore.util.LastKnown;

public class LynxServoController extends LynxController implements ServoController, ServoControllerEx {
  public static final String TAG = "LynxServoController";
  
  public static final double apiPositionFirst = 0.0D;
  
  public static final double apiPositionLast = 1.0D;
  
  public static final int apiServoFirst = 0;
  
  public static final int apiServoLast = 5;
  
  protected PwmControl.PwmRange[] defaultPwmRanges = new PwmControl.PwmRange[6];
  
  protected final LastKnown<Double>[] lastKnownCommandedPosition = (LastKnown<Double>[])LastKnown.createArray(6);
  
  protected final LastKnown<Boolean>[] lastKnownEnabled = (LastKnown<Boolean>[])LastKnown.createArray(6);
  
  protected PwmControl.PwmRange[] pwmRanges = new PwmControl.PwmRange[6];
  
  public LynxServoController(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    int i = 0;
    while (true) {
      PwmControl.PwmRange[] arrayOfPwmRange = this.pwmRanges;
      if (i < arrayOfPwmRange.length) {
        arrayOfPwmRange[i] = PwmControl.PwmRange.defaultRange;
        this.defaultPwmRanges[i] = PwmControl.PwmRange.defaultRange;
        i++;
        continue;
      } 
      finishConstruction();
      return;
    } 
  }
  
  private boolean internalGetPwmEnable(int paramInt) {
    Boolean bool = (Boolean)this.lastKnownEnabled[paramInt].getValue();
    if (bool != null)
      return bool.booleanValue(); 
    LynxGetServoEnableCommand lynxGetServoEnableCommand = new LynxGetServoEnableCommand(getModule(), paramInt);
    try {
      Boolean bool1 = Boolean.valueOf(((LynxGetServoEnableResponse)lynxGetServoEnableCommand.sendReceive()).isEnabled());
      this.lastKnownEnabled[paramInt].setValue(bool1);
      return bool1.booleanValue();
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Boolean)LynxUsbUtil.<Boolean>makePlaceholderValue(Boolean.valueOf(true))).booleanValue();
  }
  
  private void internalSetPwmEnable(int paramInt, boolean paramBoolean) {
    if (this.lastKnownEnabled[paramInt].updateValue(Boolean.valueOf(paramBoolean))) {
      if (!paramBoolean)
        this.lastKnownCommandedPosition[paramInt].invalidate(); 
      LynxSetServoEnableCommand lynxSetServoEnableCommand = new LynxSetServoEnableCommand(getModule(), paramInt, paramBoolean);
      try {
        lynxSetServoEnableCommand.send();
        return;
      } catch (InterruptedException interruptedException) {
      
      } catch (RuntimeException runtimeException) {
      
      } catch (LynxNackException lynxNackException) {}
      handleException(lynxNackException);
    } 
  }
  
  private void validateApiServoPosition(double paramDouble) {
    if (0.0D <= paramDouble && paramDouble <= 1.0D)
      return; 
    throw new IllegalArgumentException(String.format("illegal servo position %f; must be in interval [%f,%f]", new Object[] { Double.valueOf(paramDouble), Double.valueOf(0.0D), Double.valueOf(1.0D) }));
  }
  
  private void validateServo(int paramInt) {
    if (paramInt >= 0 && paramInt <= 5)
      return; 
    throw new IllegalArgumentException(String.format("Servo %d is invalid; valid servos are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(5) }));
  }
  
  public void floatHardware() {
    pwmDisable();
  }
  
  public void forgetLastKnown() {
    LastKnown.invalidateArray((LastKnown[])this.lastKnownCommandedPosition);
    LastKnown.invalidateArray((LastKnown[])this.lastKnownEnabled);
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxServoControllerDisplayName);
  }
  
  public ServoController.PwmStatus getPwmStatus() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aconst_null
    //   3: astore_3
    //   4: iconst_0
    //   5: istore_1
    //   6: iload_1
    //   7: bipush #6
    //   9: if_icmpge -> 60
    //   12: aload_0
    //   13: iload_1
    //   14: invokespecial internalGetPwmEnable : (I)Z
    //   17: istore_2
    //   18: aload_3
    //   19: ifnonnull -> 31
    //   22: iload_2
    //   23: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   26: astore #4
    //   28: goto -> 50
    //   31: aload_3
    //   32: astore #4
    //   34: aload_3
    //   35: invokevirtual booleanValue : ()Z
    //   38: iload_2
    //   39: if_icmpeq -> 50
    //   42: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.MIXED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   45: astore_3
    //   46: aload_0
    //   47: monitorexit
    //   48: aload_3
    //   49: areturn
    //   50: iload_1
    //   51: iconst_1
    //   52: iadd
    //   53: istore_1
    //   54: aload #4
    //   56: astore_3
    //   57: goto -> 6
    //   60: aload_3
    //   61: invokevirtual booleanValue : ()Z
    //   64: ifeq -> 74
    //   67: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.ENABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   70: astore_3
    //   71: goto -> 78
    //   74: getstatic com/qualcomm/robotcore/hardware/ServoController$PwmStatus.DISABLED : Lcom/qualcomm/robotcore/hardware/ServoController$PwmStatus;
    //   77: astore_3
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_3
    //   81: areturn
    //   82: astore_3
    //   83: aload_0
    //   84: monitorexit
    //   85: aload_3
    //   86: athrow
    // Exception table:
    //   from	to	target	type
    //   12	18	82	finally
    //   22	28	82	finally
    //   34	46	82	finally
    //   60	71	82	finally
    //   74	78	82	finally
  }
  
  public double getServoPosition(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield lastKnownCommandedPosition : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   15: iload_1
    //   16: aaload
    //   17: invokevirtual getValue : ()Ljava/lang/Object;
    //   20: checkcast java/lang/Double
    //   23: astore #4
    //   25: aload #4
    //   27: ifnull -> 40
    //   30: aload #4
    //   32: invokevirtual doubleValue : ()D
    //   35: dstore_2
    //   36: aload_0
    //   37: monitorexit
    //   38: dload_2
    //   39: dreturn
    //   40: new com/qualcomm/hardware/lynx/commands/core/LynxGetServoPulseWidthCommand
    //   43: dup
    //   44: aload_0
    //   45: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   48: iload_1
    //   49: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   52: astore #4
    //   54: aload #4
    //   56: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   59: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetServoPulseWidthResponse
    //   62: invokevirtual getPulseWidth : ()I
    //   65: i2d
    //   66: aload_0
    //   67: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   70: iload_1
    //   71: aaload
    //   72: getfield usPulseLower : D
    //   75: aload_0
    //   76: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   79: iload_1
    //   80: aaload
    //   81: getfield usPulseUpper : D
    //   84: dconst_0
    //   85: dconst_1
    //   86: invokestatic scale : (DDDDD)D
    //   89: invokestatic valueOf : (D)Ljava/lang/Double;
    //   92: invokevirtual doubleValue : ()D
    //   95: dconst_0
    //   96: dconst_1
    //   97: invokestatic clip : (DDD)D
    //   100: invokestatic valueOf : (D)Ljava/lang/Double;
    //   103: astore #4
    //   105: aload_0
    //   106: getfield lastKnownCommandedPosition : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   109: iload_1
    //   110: aaload
    //   111: aload #4
    //   113: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   116: pop
    //   117: aload #4
    //   119: invokevirtual doubleValue : ()D
    //   122: dstore_2
    //   123: aload_0
    //   124: monitorexit
    //   125: dload_2
    //   126: dreturn
    //   127: astore #4
    //   129: goto -> 139
    //   132: astore #4
    //   134: goto -> 139
    //   137: astore #4
    //   139: aload_0
    //   140: aload #4
    //   142: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   145: pop
    //   146: dconst_0
    //   147: invokestatic valueOf : (D)Ljava/lang/Double;
    //   150: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   153: checkcast java/lang/Double
    //   156: invokevirtual doubleValue : ()D
    //   159: dstore_2
    //   160: aload_0
    //   161: monitorexit
    //   162: dload_2
    //   163: dreturn
    //   164: astore #4
    //   166: aload_0
    //   167: monitorexit
    //   168: aload #4
    //   170: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	164	finally
    //   11	25	164	finally
    //   30	36	164	finally
    //   40	54	164	finally
    //   54	123	137	java/lang/InterruptedException
    //   54	123	132	java/lang/RuntimeException
    //   54	123	127	com/qualcomm/hardware/lynx/LynxNackException
    //   54	123	164	finally
    //   139	160	164	finally
  }
  
  public PwmControl.PwmRange getServoPwmRange(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: aload_0
    //   8: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   11: iload_1
    //   12: iconst_0
    //   13: iadd
    //   14: aaload
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: areturn
    //   20: astore_2
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_2
    //   24: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	20	finally
  }
  
  protected String getTag() {
    return "LynxServoController";
  }
  
  public void initializeHardware() {
    for (int i = 0; i <= 5; i++) {
      PwmControl.PwmRange[] arrayOfPwmRange = this.pwmRanges;
      int j = i + 0;
      arrayOfPwmRange[j] = null;
      setServoPwmRange(i, this.defaultPwmRanges[j]);
    } 
    floatHardware();
    forgetLastKnown();
  }
  
  public boolean isServoPwmEnabled(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: invokespecial internalGetPwmEnable : (I)Z
    //   14: istore_2
    //   15: aload_0
    //   16: monitorexit
    //   17: iload_2
    //   18: ireturn
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	15	19	finally
  }
  
  public void pwmDisable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: iload_1
    //   5: bipush #6
    //   7: if_icmpge -> 28
    //   10: aload_0
    //   11: iload_1
    //   12: iconst_0
    //   13: invokespecial internalSetPwmEnable : (IZ)V
    //   16: iload_1
    //   17: iconst_1
    //   18: iadd
    //   19: istore_1
    //   20: goto -> 4
    //   23: astore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_2
    //   27: athrow
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    // Exception table:
    //   from	to	target	type
    //   10	16	23	finally
  }
  
  public void pwmEnable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: iload_1
    //   5: bipush #6
    //   7: if_icmpge -> 28
    //   10: aload_0
    //   11: iload_1
    //   12: iconst_1
    //   13: invokespecial internalSetPwmEnable : (IZ)V
    //   16: iload_1
    //   17: iconst_1
    //   18: iadd
    //   19: istore_1
    //   20: goto -> 4
    //   23: astore_2
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_2
    //   27: athrow
    //   28: aload_0
    //   29: monitorexit
    //   30: return
    // Exception table:
    //   from	to	target	type
    //   10	16	23	finally
  }
  
  public void setServoPosition(int paramInt, double paramDouble) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: dload_2
    //   13: invokespecial validateApiServoPosition : (D)V
    //   16: aload_0
    //   17: getfield lastKnownCommandedPosition : [Lcom/qualcomm/robotcore/util/LastKnown;
    //   20: iload_1
    //   21: aaload
    //   22: dload_2
    //   23: invokestatic valueOf : (D)Ljava/lang/Double;
    //   26: invokevirtual updateValue : (Ljava/lang/Object;)Z
    //   29: ifeq -> 113
    //   32: dload_2
    //   33: dconst_0
    //   34: dconst_1
    //   35: aload_0
    //   36: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   39: iload_1
    //   40: aaload
    //   41: getfield usPulseLower : D
    //   44: aload_0
    //   45: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   48: iload_1
    //   49: aaload
    //   50: getfield usPulseUpper : D
    //   53: invokestatic scale : (DDDDD)D
    //   56: dconst_0
    //   57: ldc2_w 65535.0
    //   60: invokestatic clip : (DDD)D
    //   63: dstore_2
    //   64: new com/qualcomm/hardware/lynx/commands/core/LynxSetServoPulseWidthCommand
    //   67: dup
    //   68: aload_0
    //   69: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   72: iload_1
    //   73: dload_2
    //   74: d2i
    //   75: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;II)V
    //   78: astore #4
    //   80: aload #4
    //   82: invokevirtual send : ()V
    //   85: goto -> 107
    //   88: astore #4
    //   90: goto -> 100
    //   93: astore #4
    //   95: goto -> 100
    //   98: astore #4
    //   100: aload_0
    //   101: aload #4
    //   103: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   106: pop
    //   107: aload_0
    //   108: iload_1
    //   109: iconst_1
    //   110: invokespecial internalSetPwmEnable : (IZ)V
    //   113: aload_0
    //   114: monitorexit
    //   115: return
    //   116: astore #4
    //   118: aload_0
    //   119: monitorexit
    //   120: aload #4
    //   122: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	116	finally
    //   11	80	116	finally
    //   80	85	98	java/lang/InterruptedException
    //   80	85	93	java/lang/RuntimeException
    //   80	85	88	com/qualcomm/hardware/lynx/LynxNackException
    //   80	85	116	finally
    //   100	107	116	finally
    //   107	113	116	finally
  }
  
  public void setServoPwmDisable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: iconst_0
    //   12: invokespecial internalSetPwmEnable : (IZ)V
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
  
  public void setServoPwmEnable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: aload_0
    //   8: iload_1
    //   9: iconst_0
    //   10: iadd
    //   11: iconst_1
    //   12: invokespecial internalSetPwmEnable : (IZ)V
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
  
  public void setServoPwmRange(int paramInt, PwmControl.PwmRange paramPwmRange) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validateServo : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_2
    //   12: aload_0
    //   13: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   16: iload_1
    //   17: aaload
    //   18: invokevirtual equals : (Ljava/lang/Object;)Z
    //   21: ifne -> 71
    //   24: aload_0
    //   25: getfield pwmRanges : [Lcom/qualcomm/robotcore/hardware/PwmControl$PwmRange;
    //   28: iload_1
    //   29: aload_2
    //   30: aastore
    //   31: new com/qualcomm/hardware/lynx/commands/core/LynxSetServoConfigurationCommand
    //   34: dup
    //   35: aload_0
    //   36: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   39: iload_1
    //   40: aload_2
    //   41: getfield usFrame : D
    //   44: d2i
    //   45: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;II)V
    //   48: astore_2
    //   49: aload_2
    //   50: invokevirtual send : ()V
    //   53: goto -> 71
    //   56: astore_2
    //   57: goto -> 65
    //   60: astore_2
    //   61: goto -> 65
    //   64: astore_2
    //   65: aload_0
    //   66: aload_2
    //   67: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   70: pop
    //   71: aload_0
    //   72: monitorexit
    //   73: return
    //   74: astore_2
    //   75: aload_0
    //   76: monitorexit
    //   77: aload_2
    //   78: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	74	finally
    //   11	49	74	finally
    //   49	53	64	java/lang/InterruptedException
    //   49	53	60	java/lang/RuntimeException
    //   49	53	56	com/qualcomm/hardware/lynx/LynxNackException
    //   49	53	74	finally
    //   65	71	74	finally
  }
  
  public void setServoType(int paramInt, ServoConfigurationType paramServoConfigurationType) {
    validateServo(paramInt);
    paramInt += 0;
    PwmControl.PwmRange pwmRange = new PwmControl.PwmRange(paramServoConfigurationType.getUsPulseLower(), paramServoConfigurationType.getUsPulseUpper(), paramServoConfigurationType.getUsFrame());
    this.defaultPwmRanges[paramInt] = pwmRange;
    setServoPwmRange(paramInt, pwmRange);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxServoController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */