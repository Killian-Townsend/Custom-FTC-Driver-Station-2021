package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteReadMultipleBytesCommand;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.TimestampedData;

public class LynxI2cDeviceSynchV2 extends LynxI2cDeviceSynch {
  public LynxI2cDeviceSynchV2(Context paramContext, LynxModule paramLynxModule, int paramInt) {
    super(paramContext, paramLynxModule, paramInt);
  }
  
  public TimestampedData readTimeStamped(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: new com/qualcomm/hardware/lynx/LynxI2cDeviceSynchV2$1
    //   6: dup
    //   7: aload_0
    //   8: iload_1
    //   9: iload_2
    //   10: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxI2cDeviceSynchV2;II)V
    //   13: invokevirtual acquireI2cLockWhile : (Lcom/qualcomm/hardware/lynx/Supplier;)Ljava/lang/Object;
    //   16: checkcast com/qualcomm/robotcore/hardware/TimestampedData
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: areturn
    //   24: astore_3
    //   25: goto -> 73
    //   28: astore_3
    //   29: aload_0
    //   30: aload_3
    //   31: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   34: pop
    //   35: aload_0
    //   36: astore_3
    //   37: goto -> 48
    //   40: aload_0
    //   41: aload_3
    //   42: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   45: pop
    //   46: aconst_null
    //   47: astore_3
    //   48: aload_0
    //   49: getfield readTimeStampedPlaceholder : Lcom/qualcomm/hardware/lynx/LynxUsbUtil$Placeholder;
    //   52: aload_3
    //   53: aload_0
    //   54: invokevirtual getI2cAddress : ()Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   57: iload_1
    //   58: iload_2
    //   59: invokestatic makeFakeData : (Ljava/lang/Object;Lcom/qualcomm/robotcore/hardware/I2cAddr;II)Lcom/qualcomm/robotcore/hardware/TimestampedI2cData;
    //   62: invokevirtual log : (Ljava/lang/Object;)Ljava/lang/Object;
    //   65: checkcast com/qualcomm/robotcore/hardware/TimestampedData
    //   68: astore_3
    //   69: aload_0
    //   70: monitorexit
    //   71: aload_3
    //   72: areturn
    //   73: aload_0
    //   74: monitorexit
    //   75: aload_3
    //   76: athrow
    //   77: astore_3
    //   78: goto -> 40
    //   81: astore_3
    //   82: goto -> 40
    //   85: astore_3
    //   86: goto -> 40
    // Exception table:
    //   from	to	target	type
    //   2	20	85	java/lang/InterruptedException
    //   2	20	81	com/qualcomm/robotcore/exception/RobotCoreException
    //   2	20	77	java/lang/RuntimeException
    //   2	20	28	com/qualcomm/hardware/lynx/LynxNackException
    //   2	20	24	finally
    //   29	35	24	finally
    //   40	46	24	finally
    //   48	69	24	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxI2cDeviceSynchV2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */