package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.core.LynxSetDIODirectionCommand;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.util.LastKnown;
import com.qualcomm.robotcore.util.SerialNumber;

public class LynxDigitalChannelController extends LynxController implements DigitalChannelController {
  public static final String TAG = "LynxDigitalChannelController";
  
  public static final int apiPinFirst = 0;
  
  public static final int apiPinLast = 7;
  
  protected final PinProperties[] pins = new PinProperties[8];
  
  public LynxDigitalChannelController(Context paramContext, LynxModule paramLynxModule) throws RobotCoreException, InterruptedException {
    super(paramContext, paramLynxModule);
    for (int i = 0; i <= 7; i++)
      this.pins[i + 0] = new PinProperties(); 
    finishConstruction();
  }
  
  private void validatePin(int paramInt) {
    if (paramInt >= 0 && paramInt <= 7)
      return; 
    throw new IllegalArgumentException(String.format("pin %d is invalid; valid pins are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(7) }));
  }
  
  public void forgetLastKnown() {
    for (PinProperties pinProperties : this.pins) {
      pinProperties.lastKnownMode.invalidate();
      pinProperties.lastKnownState.invalidate();
    } 
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxDigitalChannelControllerDisplayName);
  }
  
  public DigitalChannel.Mode getDigitalChannelMode(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokespecial validatePin : (I)V
    //   7: iload_1
    //   8: iconst_0
    //   9: iadd
    //   10: istore_1
    //   11: aload_0
    //   12: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   15: iload_1
    //   16: aaload
    //   17: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   20: invokevirtual getValue : ()Ljava/lang/Object;
    //   23: checkcast com/qualcomm/robotcore/hardware/DigitalChannel$Mode
    //   26: astore_2
    //   27: aload_2
    //   28: ifnull -> 35
    //   31: aload_0
    //   32: monitorexit
    //   33: aload_2
    //   34: areturn
    //   35: new com/qualcomm/hardware/lynx/commands/core/LynxGetDIODirectionCommand
    //   38: dup
    //   39: aload_0
    //   40: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   43: iload_1
    //   44: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   47: astore_2
    //   48: aload_2
    //   49: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   52: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetDIODirectionResponse
    //   55: invokevirtual getMode : ()Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   58: astore_2
    //   59: aload_0
    //   60: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   63: iload_1
    //   64: aaload
    //   65: getfield lastKnownMode : Lcom/qualcomm/robotcore/util/LastKnown;
    //   68: aload_2
    //   69: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   72: pop
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_2
    //   76: areturn
    //   77: astore_2
    //   78: goto -> 86
    //   81: astore_2
    //   82: goto -> 86
    //   85: astore_2
    //   86: aload_0
    //   87: aload_2
    //   88: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   91: pop
    //   92: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.INPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   95: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   98: checkcast com/qualcomm/robotcore/hardware/DigitalChannel$Mode
    //   101: astore_2
    //   102: aload_0
    //   103: monitorexit
    //   104: aload_2
    //   105: areturn
    //   106: astore_2
    //   107: aload_0
    //   108: monitorexit
    //   109: aload_2
    //   110: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	106	finally
    //   11	27	106	finally
    //   35	48	106	finally
    //   48	73	85	java/lang/InterruptedException
    //   48	73	81	java/lang/RuntimeException
    //   48	73	77	com/qualcomm/hardware/lynx/LynxNackException
    //   48	73	106	finally
    //   86	102	106	finally
  }
  
  public boolean getDigitalChannelState(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual getDigitalChannelMode : (I)Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   7: astore_3
    //   8: aload_0
    //   9: iload_1
    //   10: invokespecial validatePin : (I)V
    //   13: iload_1
    //   14: iconst_0
    //   15: iadd
    //   16: istore_1
    //   17: aload_3
    //   18: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.OUTPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   21: if_acmpne -> 47
    //   24: aload_0
    //   25: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   28: iload_1
    //   29: aaload
    //   30: getfield lastKnownState : Lcom/qualcomm/robotcore/util/LastKnown;
    //   33: invokevirtual getNonTimedValue : ()Ljava/lang/Object;
    //   36: checkcast java/lang/Boolean
    //   39: invokevirtual booleanValue : ()Z
    //   42: istore_2
    //   43: aload_0
    //   44: monitorexit
    //   45: iload_2
    //   46: ireturn
    //   47: new com/qualcomm/hardware/lynx/commands/core/LynxGetSingleDIOInputCommand
    //   50: dup
    //   51: aload_0
    //   52: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   55: iload_1
    //   56: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;I)V
    //   59: astore_3
    //   60: aload_0
    //   61: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   64: instanceof com/qualcomm/hardware/lynx/LynxModule
    //   67: ifeq -> 105
    //   70: aload_0
    //   71: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   74: checkcast com/qualcomm/hardware/lynx/LynxModule
    //   77: astore #4
    //   79: aload #4
    //   81: invokevirtual getBulkCachingMode : ()Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   84: getstatic com/qualcomm/hardware/lynx/LynxModule$BulkCachingMode.OFF : Lcom/qualcomm/hardware/lynx/LynxModule$BulkCachingMode;
    //   87: if_acmpeq -> 105
    //   90: aload #4
    //   92: aload_3
    //   93: invokevirtual recordBulkCachingCommandIntent : (Lcom/qualcomm/hardware/lynx/commands/core/LynxDekaInterfaceCommand;)Lcom/qualcomm/hardware/lynx/LynxModule$BulkData;
    //   96: iload_1
    //   97: invokevirtual getDigitalChannelState : (I)Z
    //   100: istore_2
    //   101: aload_0
    //   102: monitorexit
    //   103: iload_2
    //   104: ireturn
    //   105: aload_3
    //   106: invokevirtual sendReceive : ()Lcom/qualcomm/hardware/lynx/commands/LynxMessage;
    //   109: checkcast com/qualcomm/hardware/lynx/commands/core/LynxGetSingleDIOInputResponse
    //   112: invokevirtual getValue : ()Z
    //   115: istore_2
    //   116: aload_0
    //   117: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   120: iload_1
    //   121: aaload
    //   122: getfield lastKnownState : Lcom/qualcomm/robotcore/util/LastKnown;
    //   125: iload_2
    //   126: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   129: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   132: pop
    //   133: aload_0
    //   134: monitorexit
    //   135: iload_2
    //   136: ireturn
    //   137: astore_3
    //   138: goto -> 146
    //   141: astore_3
    //   142: goto -> 146
    //   145: astore_3
    //   146: aload_0
    //   147: aload_3
    //   148: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   151: pop
    //   152: iconst_0
    //   153: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   156: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   159: checkcast java/lang/Boolean
    //   162: invokevirtual booleanValue : ()Z
    //   165: istore_2
    //   166: aload_0
    //   167: monitorexit
    //   168: iload_2
    //   169: ireturn
    //   170: astore_3
    //   171: aload_0
    //   172: monitorexit
    //   173: aload_3
    //   174: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	170	finally
    //   17	43	170	finally
    //   47	101	170	finally
    //   105	133	145	java/lang/InterruptedException
    //   105	133	141	java/lang/RuntimeException
    //   105	133	137	com/qualcomm/hardware/lynx/LynxNackException
    //   105	133	170	finally
    //   146	166	170	finally
  }
  
  public SerialNumber getSerialNumber() {
    return getModule().getSerialNumber();
  }
  
  protected String getTag() {
    return "LynxDigitalChannelController";
  }
  
  public void initializeHardware() {
    forgetLastKnown();
    for (int i = 0; i <= 7; i++)
      internalSetDigitalChannelMode(i + 0, DigitalChannel.Mode.INPUT); 
  }
  
  void internalSetDigitalChannelMode(int paramInt, DigitalChannel.Mode paramMode) {
    LynxSetDIODirectionCommand lynxSetDIODirectionCommand = new LynxSetDIODirectionCommand(getModule(), paramInt, paramMode);
    try {
      lynxSetDIODirectionCommand.send();
      (this.pins[paramInt]).lastKnownMode.setValue(paramMode);
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
    (this.pins[paramInt]).lastKnownMode.invalidate();
  }
  
  public void setDigitalChannelMode(int paramInt, DigitalChannel.Mode paramMode) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual getDigitalChannelMode : (I)Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   7: astore_3
    //   8: aload_0
    //   9: iload_1
    //   10: invokespecial validatePin : (I)V
    //   13: iload_1
    //   14: iconst_0
    //   15: iadd
    //   16: istore_1
    //   17: aload_0
    //   18: iload_1
    //   19: aload_2
    //   20: invokevirtual internalSetDigitalChannelMode : (ILcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;)V
    //   23: aload_3
    //   24: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.INPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   27: if_acmpne -> 54
    //   30: aload_2
    //   31: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.OUTPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   34: if_acmpne -> 54
    //   37: aload_0
    //   38: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   41: iload_1
    //   42: aaload
    //   43: getfield lastKnownState : Lcom/qualcomm/robotcore/util/LastKnown;
    //   46: iconst_0
    //   47: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   50: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   53: pop
    //   54: aload_0
    //   55: monitorexit
    //   56: return
    //   57: astore_2
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_2
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	57	finally
    //   17	54	57	finally
  }
  
  @Deprecated
  public void setDigitalChannelMode(int paramInt, DigitalChannelController.Mode paramMode) {
    setDigitalChannelMode(paramInt, paramMode.migrate());
  }
  
  public void setDigitalChannelState(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual getDigitalChannelMode : (I)Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   7: astore_3
    //   8: aload_0
    //   9: iload_1
    //   10: invokespecial validatePin : (I)V
    //   13: iload_1
    //   14: iconst_0
    //   15: iadd
    //   16: istore_1
    //   17: aload_3
    //   18: getstatic com/qualcomm/robotcore/hardware/DigitalChannel$Mode.OUTPUT : Lcom/qualcomm/robotcore/hardware/DigitalChannel$Mode;
    //   21: if_acmpne -> 83
    //   24: new com/qualcomm/hardware/lynx/commands/core/LynxSetSingleDIOOutputCommand
    //   27: dup
    //   28: aload_0
    //   29: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   32: iload_1
    //   33: iload_2
    //   34: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;IZ)V
    //   37: astore_3
    //   38: aload_3
    //   39: invokevirtual send : ()V
    //   42: aload_0
    //   43: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   46: iload_1
    //   47: aaload
    //   48: getfield lastKnownState : Lcom/qualcomm/robotcore/util/LastKnown;
    //   51: iload_2
    //   52: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   55: invokevirtual setValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   58: pop
    //   59: goto -> 83
    //   62: aload_0
    //   63: aload_3
    //   64: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   67: pop
    //   68: aload_0
    //   69: getfield pins : [Lcom/qualcomm/hardware/lynx/LynxDigitalChannelController$PinProperties;
    //   72: iload_1
    //   73: aaload
    //   74: getfield lastKnownState : Lcom/qualcomm/robotcore/util/LastKnown;
    //   77: invokevirtual invalidate : ()V
    //   80: aload_0
    //   81: monitorexit
    //   82: return
    //   83: aload_0
    //   84: monitorexit
    //   85: return
    //   86: astore_3
    //   87: aload_0
    //   88: monitorexit
    //   89: aload_3
    //   90: athrow
    //   91: astore_3
    //   92: goto -> 62
    //   95: astore_3
    //   96: goto -> 62
    //   99: astore_3
    //   100: goto -> 62
    // Exception table:
    //   from	to	target	type
    //   2	13	86	finally
    //   17	38	86	finally
    //   38	42	99	java/lang/InterruptedException
    //   38	42	95	java/lang/RuntimeException
    //   38	42	91	com/qualcomm/hardware/lynx/LynxNackException
    //   38	42	86	finally
    //   42	59	86	finally
    //   62	80	86	finally
  }
  
  protected class PinProperties {
    LastKnown<DigitalChannel.Mode> lastKnownMode = new LastKnown();
    
    LastKnown<Boolean> lastKnownState = new LastKnown();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxDigitalChannelController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */