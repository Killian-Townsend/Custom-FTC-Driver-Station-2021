package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cReadMultipleBytesCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cReadSingleByteCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteSingleByteCommand;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;

public class LynxI2cDeviceSynchV1 extends LynxI2cDeviceSynch {
  public LynxI2cDeviceSynchV1(Context paramContext, LynxModule paramLynxModule, int paramInt) {
    super(paramContext, paramLynxModule, paramInt);
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: new com/qualcomm/hardware/lynx/LynxI2cDeviceSynchV1$1
    //   6: dup
    //   7: aload_0
    //   8: new com/qualcomm/hardware/lynx/commands/core/LynxI2cWriteSingleByteCommand
    //   11: dup
    //   12: aload_0
    //   13: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   16: aload_0
    //   17: getfield bus : I
    //   20: aload_0
    //   21: getfield i2cAddr : Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   24: iload_1
    //   25: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;ILcom/qualcomm/robotcore/hardware/I2cAddr;I)V
    //   28: iload_2
    //   29: iload_1
    //   30: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxI2cDeviceSynchV1;Lcom/qualcomm/hardware/lynx/commands/core/LynxI2cWriteSingleByteCommand;II)V
    //   33: invokevirtual acquireI2cLockWhile : (Lcom/qualcomm/hardware/lynx/Supplier;)Ljava/lang/Object;
    //   36: checkcast com/qualcomm/robotcore/hardware/TimestampedData
    //   39: astore_3
    //   40: aload_0
    //   41: monitorexit
    //   42: aload_3
    //   43: areturn
    //   44: astore_3
    //   45: goto -> 93
    //   48: astore_3
    //   49: aload_0
    //   50: aload_3
    //   51: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   54: pop
    //   55: aload_0
    //   56: astore_3
    //   57: goto -> 68
    //   60: aload_0
    //   61: aload_3
    //   62: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   65: pop
    //   66: aconst_null
    //   67: astore_3
    //   68: aload_0
    //   69: getfield readTimeStampedPlaceholder : Lcom/qualcomm/hardware/lynx/LynxUsbUtil$Placeholder;
    //   72: aload_3
    //   73: aload_0
    //   74: invokevirtual getI2cAddress : ()Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   77: iload_1
    //   78: iload_2
    //   79: invokestatic makeFakeData : (Ljava/lang/Object;Lcom/qualcomm/robotcore/hardware/I2cAddr;II)Lcom/qualcomm/robotcore/hardware/TimestampedI2cData;
    //   82: invokevirtual log : (Ljava/lang/Object;)Ljava/lang/Object;
    //   85: checkcast com/qualcomm/robotcore/hardware/TimestampedData
    //   88: astore_3
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_3
    //   92: areturn
    //   93: aload_0
    //   94: monitorexit
    //   95: aload_3
    //   96: athrow
    //   97: astore_3
    //   98: goto -> 60
    //   101: astore_3
    //   102: goto -> 60
    //   105: astore_3
    //   106: goto -> 60
    // Exception table:
    //   from	to	target	type
    //   2	40	105	java/lang/InterruptedException
    //   2	40	101	com/qualcomm/robotcore/exception/RobotCoreException
    //   2	40	97	java/lang/RuntimeException
    //   2	40	48	com/qualcomm/hardware/lynx/LynxNackException
    //   2	40	44	finally
    //   49	55	44	finally
    //   60	66	44	finally
    //   68	89	44	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxI2cDeviceSynchV1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */