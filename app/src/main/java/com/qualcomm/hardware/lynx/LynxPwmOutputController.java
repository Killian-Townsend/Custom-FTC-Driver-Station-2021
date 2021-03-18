package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxGetPWMEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxGetPWMEnableResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxSetPWMConfigurationCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetPWMEnableCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxSetPWMPulseWidthCommand;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.PWMOutputController;
import com.qualcomm.robotcore.hardware.PWMOutputControllerEx;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.SerialNumber;

@Deprecated
public class LynxPwmOutputController extends LynxController implements PWMOutputController, PWMOutputControllerEx {
  public static final String TAG = "LynxPwmOutputController";
  
  public static final int apiPortFirst = 0;
  
  public static final int apiPortLast = 3;
  
  protected LastKnown<Integer>[] lastKnownOutputTimes = (LastKnown<Integer>[])LastKnown.createArray(4);
  
  protected LastKnown<Integer>[] lastKnownPulseWidthPeriods = (LastKnown<Integer>[])LastKnown.createArray(4);
  
  public LynxPwmOutputController(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    finishConstruction();
  }
  
  private boolean internalGetPwmEnable(int paramInt) {
    LynxGetPWMEnableCommand lynxGetPWMEnableCommand = new LynxGetPWMEnableCommand(getModule(), paramInt);
    try {
      return ((LynxGetPWMEnableResponse)lynxGetPWMEnableCommand.sendReceive()).isEnabled();
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    return ((Boolean)LynxUsbUtil.<Boolean>makePlaceholderValue(Boolean.valueOf(true))).booleanValue();
  }
  
  private void internalSetPwmEnable(int paramInt, boolean paramBoolean) {
    LynxSetPWMEnableCommand lynxSetPWMEnableCommand = new LynxSetPWMEnableCommand(getModule(), paramInt, paramBoolean);
    try {
      lynxSetPWMEnableCommand.send();
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
  }
  
  private void validatePort(int paramInt) {
    if (paramInt >= 0 && paramInt <= 3)
      return; 
    throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(3) }));
  }
  
  public void floatHardware() {
    for (int i = 0; i <= 3; i++)
      setPwmDisable(i); 
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxPwmOutputControllerDisplayName);
  }
  
  public int getPulseWidthOutputTime(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
    //   7: new com/qualcomm/hardware/lynx/commands/core/LynxGetPWMPulseWidthCommand
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
    //   26: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetPWMPulseWidthResponse
    //   29: invokevirtual getPulseWidth : ()I
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
  
  public int getPulseWidthPeriod(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
    //   7: new com/qualcomm/hardware/lynx/commands/core/LynxGetPWMConfigurationCommand
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
    //   26: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetPWMConfigurationResponse
    //   29: invokevirtual getFramePeriod : ()I
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
  
  public SerialNumber getSerialNumber() {
    return getModule().getSerialNumber();
  }
  
  protected String getTag() {
    return "LynxPwmOutputController";
  }
  
  public void initializeHardware() {
    for (int i = 0; i <= 3; i++) {
      setPwmDisable(i);
      internalSetPulseWidthPeriod(i + 0, 20000);
    } 
  }
  
  void internalSetPulseWidthOutputTime(int paramInt1, int paramInt2) {
    if (this.lastKnownOutputTimes[paramInt1].updateValue(Integer.valueOf(paramInt2))) {
      LynxSetPWMPulseWidthCommand lynxSetPWMPulseWidthCommand = new LynxSetPWMPulseWidthCommand(getModule(), paramInt1, paramInt2);
      try {
        lynxSetPWMPulseWidthCommand.send();
        return;
      } catch (InterruptedException interruptedException) {
      
      } catch (RuntimeException runtimeException) {
      
      } catch (LynxNackException lynxNackException) {}
      handleException(lynxNackException);
    } 
  }
  
  void internalSetPulseWidthPeriod(int paramInt1, int paramInt2) {
    if (this.lastKnownOutputTimes[paramInt1].updateValue(Integer.valueOf(paramInt2))) {
      LynxSetPWMConfigurationCommand lynxSetPWMConfigurationCommand = new LynxSetPWMConfigurationCommand(getModule(), paramInt1, paramInt2);
      try {
        lynxSetPWMConfigurationCommand.send();
        return;
      } catch (InterruptedException interruptedException) {
      
      } catch (RuntimeException runtimeException) {
      
      } catch (LynxNackException lynxNackException) {}
      handleException(lynxNackException);
    } 
  }
  
  public boolean isPwmEnabled(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
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
  
  public void setPulseWidthOutputTime(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: iload_1
    //   13: iload_2
    //   14: invokevirtual internalSetPulseWidthOutputTime : (II)V
    //   17: aload_0
    //   18: iload_1
    //   19: iconst_0
    //   20: iadd
    //   21: invokevirtual setPwmEnable : (I)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_3
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	27	finally
    //   11	24	27	finally
  }
  
  public void setPulseWidthPeriod(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: iload_1
    //   13: iload_2
    //   14: invokevirtual internalSetPulseWidthPeriod : (II)V
    //   17: aload_0
    //   18: iload_1
    //   19: iconst_0
    //   20: iadd
    //   21: invokevirtual setPwmEnable : (I)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_3
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	27	finally
    //   11	24	27	finally
  }
  
  public void setPwmDisable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
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
  
  public void setPwmEnable(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePort : (I)V
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
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxPwmOutputController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */