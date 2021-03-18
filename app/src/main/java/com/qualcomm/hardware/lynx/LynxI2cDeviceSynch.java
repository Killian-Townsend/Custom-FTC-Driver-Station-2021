package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.R;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cConfigureChannelCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cReadSingleByteCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cReadStatusQueryCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cReadStatusQueryResponse;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteMultipleBytesCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteSingleByteCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteStatusQueryCommand;
import com.qualcomm.hardware.lynx.commands.core.LynxI2cWriteStatusQueryResponse;
import com.qualcomm.hardware.lynx.commands.standard.LynxNack;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDeviceHealth;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchReadHistory;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchReadHistoryImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchSimple;
import com.qualcomm.robotcore.hardware.I2cWaitControl;
import com.qualcomm.robotcore.hardware.TimestampedData;
import com.qualcomm.robotcore.hardware.TimestampedI2cData;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.Util;
import java.util.concurrent.BlockingQueue;

public abstract class LynxI2cDeviceSynch extends LynxController implements I2cDeviceSynchSimple, I2cDeviceSynchReadHistory {
  public static final String TAG = "LynxI2cDeviceSynch";
  
  protected int bus;
  
  protected I2cAddr i2cAddr;
  
  private boolean loggingEnabled;
  
  private String loggingTag;
  
  private int msBusyWait = 3;
  
  private String name;
  
  private final I2cDeviceSynchReadHistoryImpl readHistory = new I2cDeviceSynchReadHistoryImpl();
  
  private LynxUsbUtil.Placeholder<TimestampedData> readStatusQueryPlaceholder = new LynxUsbUtil.Placeholder<TimestampedData>("LynxI2cDeviceSynch", "readStatusQuery", new Object[0]);
  
  protected LynxUsbUtil.Placeholder<TimestampedData> readTimeStampedPlaceholder = new LynxUsbUtil.Placeholder<TimestampedData>("LynxI2cDeviceSynch", "readTimestamped", new Object[0]);
  
  protected LynxI2cDeviceSynch(Context paramContext, LynxModule paramLynxModule, int paramInt) {
    super(paramContext, paramLynxModule);
    this.bus = paramInt;
    this.i2cAddr = I2cAddr.zero();
    this.loggingEnabled = false;
    this.loggingTag = "LynxI2cDeviceSynch";
    finishConstruction();
  }
  
  private void internalWrite(int paramInt, byte[] paramArrayOfbyte, final I2cWaitControl waitControl) {
    if (paramArrayOfbyte.length > 0) {
      LynxI2cWriteSingleByteCommand lynxI2cWriteSingleByteCommand;
      final LynxI2cWriteMultipleBytesCommand writeTx;
      paramArrayOfbyte = Util.concatenateByteArrays(new byte[] { (byte)paramInt }, paramArrayOfbyte);
      if (paramArrayOfbyte.length == 1) {
        lynxI2cWriteSingleByteCommand = new LynxI2cWriteSingleByteCommand(getModule(), this.bus, this.i2cAddr, paramArrayOfbyte[0]);
      } else {
        lynxI2cWriteMultipleBytesCommand = new LynxI2cWriteMultipleBytesCommand(getModule(), this.bus, this.i2cAddr, (byte[])lynxI2cWriteSingleByteCommand);
      } 
      try {
        acquireI2cLockWhile(new Supplier() {
              public Object get() throws InterruptedException, RobotCoreException, LynxNackException {
                LynxI2cDeviceSynch.this.sendI2cWriteTx(writeTx);
                LynxI2cDeviceSynch.this.internalWaitForWriteCompletions(waitControl);
                return null;
              }
            });
        return;
      } catch (InterruptedException interruptedException) {
      
      } catch (LynxNackException lynxNackException) {
      
      } catch (RobotCoreException robotCoreException) {
      
      } catch (RuntimeException runtimeException) {}
      handleException(runtimeException);
    } 
  }
  
  protected <T> T acquireI2cLockWhile(Supplier<T> paramSupplier) throws InterruptedException, RobotCoreException, LynxNackException {
    return getModule().acquireI2cLockWhile(paramSupplier);
  }
  
  public void close() {
    setHealthStatus(HardwareDeviceHealth.HealthStatus.CLOSED);
    super.close();
  }
  
  public void enableWriteCoalescing(boolean paramBoolean) {}
  
  public String getConnectionInfo() {
    return String.format("%s; bus %d; addr7=0x%02x", new Object[] { getModule().getConnectionInfo(), Integer.valueOf(this.bus), Integer.valueOf(this.i2cAddr.get7Bit()) });
  }
  
  public String getDeviceName() {
    return this.context.getString(R.string.lynxI2cDeviceSynchDisplayName);
  }
  
  public BlockingQueue<TimestampedI2cData> getHistoryQueue() {
    return this.readHistory.getHistoryQueue();
  }
  
  public int getHistoryQueueCapacity() {
    return this.readHistory.getHistoryQueueCapacity();
  }
  
  public I2cAddr getI2cAddr() {
    return this.i2cAddr;
  }
  
  public I2cAddr getI2cAddress() {
    return this.i2cAddr;
  }
  
  public boolean getLogging() {
    return this.loggingEnabled;
  }
  
  public String getLoggingTag() {
    return this.loggingTag;
  }
  
  protected String getTag() {
    return "LynxI2cDeviceSynch";
  }
  
  public String getUserConfiguredName() {
    return this.name;
  }
  
  protected void internalWaitForWriteCompletions(I2cWaitControl paramI2cWaitControl) {
    if (paramI2cWaitControl == I2cWaitControl.WRITTEN)
      for (boolean bool = true;; bool = false) {
        if (bool) {
          LynxI2cWriteStatusQueryCommand lynxI2cWriteStatusQueryCommand = new LynxI2cWriteStatusQueryCommand(getModule(), this.bus);
          try {
            if (((LynxI2cWriteStatusQueryResponse)lynxI2cWriteStatusQueryCommand.sendReceive()).isStatusOk())
              setHealthyIfArmed(); 
            continue;
          } catch (LynxNackException lynxNackException) {
            int i = null.$SwitchMap$com$qualcomm$hardware$lynx$commands$standard$LynxNack$StandardReasonCode[lynxNackException.getNack().getNackReasonCodeAsEnum().ordinal()];
          } catch (InterruptedException interruptedException) {
            handleException(interruptedException);
          } catch (RuntimeException runtimeException) {
            handleException(runtimeException);
          } 
        } else {
          return;
        } 
      }  
  }
  
  public boolean isArmed() {
    return super.isArmed();
  }
  
  public boolean isWriteCoalescingEnabled() {
    return false;
  }
  
  protected TimestampedData pollForReadResult(I2cAddr paramI2cAddr, int paramInt1, int paramInt2) {
    LynxI2cDeviceSynch lynxI2cDeviceSynch = null;
    boolean bool;
    for (bool = true; bool; bool = false) {
      LynxI2cReadStatusQueryCommand lynxI2cReadStatusQueryCommand = new LynxI2cReadStatusQueryCommand(getModule(), this.bus, paramInt2);
      try {
        LynxI2cReadStatusQueryResponse lynxI2cReadStatusQueryResponse = (LynxI2cReadStatusQueryResponse)lynxI2cReadStatusQueryCommand.sendReceive();
        long l = System.nanoTime();
        lynxI2cReadStatusQueryResponse.logResponse();
        TimestampedI2cData timestampedI2cData = new TimestampedI2cData();
        timestampedI2cData.data = lynxI2cReadStatusQueryResponse.getBytes();
        if (!lynxI2cReadStatusQueryResponse.getPayloadTimeWindow().isCleared())
          l = lynxI2cReadStatusQueryResponse.getPayloadTimeWindow().getNanosecondsLast(); 
        timestampedI2cData.nanoTime = l;
        timestampedI2cData.i2cAddr = paramI2cAddr;
        timestampedI2cData.register = paramInt1;
        if (timestampedI2cData.data.length == paramInt2) {
          this.readStatusQueryPlaceholder.reset();
          this.readHistory.addToHistoryQueue(timestampedI2cData);
          setHealthyIfArmed();
          return (TimestampedData)timestampedI2cData;
        } 
        RobotLog.ee(this.loggingTag, "readStatusQuery: cbExpected=%d cbRead=%d", new Object[] { Integer.valueOf(paramInt2), Integer.valueOf(timestampedI2cData.data.length) });
      } catch (LynxNackException lynxNackException) {
        int i = null.$SwitchMap$com$qualcomm$hardware$lynx$commands$standard$LynxNack$StandardReasonCode[lynxNackException.getNack().getNackReasonCodeAsEnum().ordinal()];
        if (i != 1 && i != 2) {
          if (i != 3) {
            handleException(lynxNackException);
          } else {
            handleException(lynxNackException);
          } 
        } else {
          continue;
        } 
      } catch (InterruptedException interruptedException) {
        handleException(interruptedException);
      } catch (RuntimeException runtimeException) {
        handleException(runtimeException);
      } 
      lynxI2cDeviceSynch = this;
    } 
    return (TimestampedData)this.readStatusQueryPlaceholder.log(TimestampedI2cData.makeFakeData(lynxI2cDeviceSynch, paramI2cAddr, paramInt1, paramInt2));
  }
  
  public byte[] read(int paramInt1, int paramInt2) {
    return (readTimeStamped(paramInt1, paramInt2)).data;
  }
  
  public byte read8(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/qualcomm/hardware/lynx/commands/core/LynxI2cWriteSingleByteCommand
    //   5: dup
    //   6: aload_0
    //   7: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   10: aload_0
    //   11: getfield bus : I
    //   14: aload_0
    //   15: getfield i2cAddr : Lcom/qualcomm/robotcore/hardware/I2cAddr;
    //   18: iload_1
    //   19: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxModuleIntf;ILcom/qualcomm/robotcore/hardware/I2cAddr;I)V
    //   22: astore_3
    //   23: aload_0
    //   24: new com/qualcomm/hardware/lynx/LynxI2cDeviceSynch$1
    //   27: dup
    //   28: aload_0
    //   29: aload_3
    //   30: iload_1
    //   31: invokespecial <init> : (Lcom/qualcomm/hardware/lynx/LynxI2cDeviceSynch;Lcom/qualcomm/hardware/lynx/commands/core/LynxI2cWriteSingleByteCommand;I)V
    //   34: invokevirtual acquireI2cLockWhile : (Lcom/qualcomm/hardware/lynx/Supplier;)Ljava/lang/Object;
    //   37: checkcast java/lang/Byte
    //   40: invokevirtual byteValue : ()B
    //   43: istore_2
    //   44: aload_0
    //   45: monitorexit
    //   46: iload_2
    //   47: ireturn
    //   48: astore_3
    //   49: goto -> 61
    //   52: astore_3
    //   53: goto -> 61
    //   56: astore_3
    //   57: goto -> 61
    //   60: astore_3
    //   61: aload_0
    //   62: aload_3
    //   63: invokevirtual handleException : (Ljava/lang/Exception;)Z
    //   66: pop
    //   67: iconst_0
    //   68: invokestatic valueOf : (B)Ljava/lang/Byte;
    //   71: invokestatic makePlaceholderValue : (Ljava/lang/Object;)Ljava/lang/Object;
    //   74: checkcast java/lang/Byte
    //   77: invokevirtual byteValue : ()B
    //   80: istore_2
    //   81: aload_0
    //   82: monitorexit
    //   83: iload_2
    //   84: ireturn
    //   85: astore_3
    //   86: aload_0
    //   87: monitorexit
    //   88: aload_3
    //   89: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	85	finally
    //   23	44	60	java/lang/InterruptedException
    //   23	44	56	com/qualcomm/hardware/lynx/LynxNackException
    //   23	44	52	com/qualcomm/robotcore/exception/RobotCoreException
    //   23	44	48	java/lang/RuntimeException
    //   23	44	85	finally
    //   61	81	85	finally
  }
  
  public abstract TimestampedData readTimeStamped(int paramInt1, int paramInt2);
  
  public void resetDeviceConfigurationForOpMode() {
    super.resetDeviceConfigurationForOpMode();
    this.readTimeStampedPlaceholder.reset();
    this.readStatusQueryPlaceholder.reset();
  }
  
  protected void sendI2cWriteTx(LynxCommand paramLynxCommand) throws LynxNackException, InterruptedException {
    while (true) {
      try {
        paramLynxCommand.send();
        return;
      } catch (LynxNackException lynxNackException) {
        int i = null.$SwitchMap$com$qualcomm$hardware$lynx$commands$standard$LynxNack$StandardReasonCode[lynxNackException.getNack().getNackReasonCodeAsEnum().ordinal()];
        if (i == 1 || i == 2) {
          Thread.sleep(this.msBusyWait);
          continue;
        } 
        throw lynxNackException;
      } 
    } 
  }
  
  public void setBusSpeed(BusSpeed paramBusSpeed) {
    LynxI2cConfigureChannelCommand lynxI2cConfigureChannelCommand = new LynxI2cConfigureChannelCommand(getModule(), this.bus, paramBusSpeed.toSpeedCode());
    try {
      lynxI2cConfigureChannelCommand.send();
      return;
    } catch (InterruptedException interruptedException) {
    
    } catch (RuntimeException runtimeException) {
    
    } catch (LynxNackException lynxNackException) {}
    handleException(lynxNackException);
  }
  
  public void setHistoryQueueCapacity(int paramInt) {
    this.readHistory.setHistoryQueueCapacity(paramInt);
  }
  
  public void setI2cAddr(I2cAddr paramI2cAddr) {
    this.i2cAddr = paramI2cAddr;
  }
  
  public void setI2cAddress(I2cAddr paramI2cAddr) {
    this.i2cAddr = paramI2cAddr;
  }
  
  public void setLogging(boolean paramBoolean) {
    this.loggingEnabled = paramBoolean;
  }
  
  public void setLoggingTag(String paramString) {
    this.loggingTag = paramString;
  }
  
  public void setUserConfiguredName(String paramString) {
    this.name = paramString;
  }
  
  public void waitForWriteCompletions(final I2cWaitControl waitControl) {
    /* monitor enter ThisExpression{ObjectType{com/qualcomm/hardware/lynx/LynxI2cDeviceSynch}} */
    try {
      acquireI2cLockWhile(new Supplier() {
            public Object get() throws InterruptedException, RobotCoreException, LynxNackException {
              LynxI2cDeviceSynch.this.internalWaitForWriteCompletions(waitControl);
              return null;
            }
          });
    } catch (InterruptedException interruptedException) {
      handleException(interruptedException);
    } catch (LynxNackException lynxNackException) {
    
    } catch (RobotCoreException robotCoreException) {
    
    } catch (RuntimeException runtimeException) {
    
    } finally {}
    /* monitor exit ThisExpression{ObjectType{com/qualcomm/hardware/lynx/LynxI2cDeviceSynch}} */
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    internalWrite(paramInt, paramArrayOfbyte, I2cWaitControl.ATOMIC);
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte, I2cWaitControl paramI2cWaitControl) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: aload_2
    //   5: aload_3
    //   6: invokespecial internalWrite : (I[BLcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
    //   9: aload_0
    //   10: monitorexit
    //   11: return
    //   12: astore_2
    //   13: aload_0
    //   14: monitorexit
    //   15: aload_2
    //   16: athrow
    // Exception table:
    //   from	to	target	type
    //   2	9	12	finally
  }
  
  public void write8(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_2
    //   3: i2b
    //   4: istore_3
    //   5: getstatic com/qualcomm/robotcore/hardware/I2cWaitControl.ATOMIC : Lcom/qualcomm/robotcore/hardware/I2cWaitControl;
    //   8: astore #4
    //   10: aload_0
    //   11: iload_1
    //   12: iconst_1
    //   13: newarray byte
    //   15: dup
    //   16: iconst_0
    //   17: iload_3
    //   18: bastore
    //   19: aload #4
    //   21: invokespecial internalWrite : (I[BLcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore #4
    //   29: aload_0
    //   30: monitorexit
    //   31: aload #4
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   5	24	27	finally
  }
  
  public void write8(int paramInt1, int paramInt2, I2cWaitControl paramI2cWaitControl) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: iconst_1
    //   5: newarray byte
    //   7: dup
    //   8: iconst_0
    //   9: iload_2
    //   10: i2b
    //   11: bastore
    //   12: aload_3
    //   13: invokespecial internalWrite : (I[BLcom/qualcomm/robotcore/hardware/I2cWaitControl;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: astore_3
    //   20: aload_0
    //   21: monitorexit
    //   22: aload_3
    //   23: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	19	finally
  }
  
  public enum BusSpeed {
    FAST_400K,
    STANDARD_100K {
      protected LynxI2cConfigureChannelCommand.SpeedCode toSpeedCode() {
        return LynxI2cConfigureChannelCommand.SpeedCode.STANDARD_100K;
      }
    };
    
    static {
      null  = new null("FAST_400K", 1);
      FAST_400K = ;
      $VALUES = new BusSpeed[] { STANDARD_100K,  };
    }
    
    protected LynxI2cConfigureChannelCommand.SpeedCode toSpeedCode() {
      throw new AbstractMethodError();
    }
  }
  
  enum null {
    protected LynxI2cConfigureChannelCommand.SpeedCode toSpeedCode() {
      return LynxI2cConfigureChannelCommand.SpeedCode.STANDARD_100K;
    }
  }
  
  enum null {
    protected LynxI2cConfigureChannelCommand.SpeedCode toSpeedCode() {
      return LynxI2cConfigureChannelCommand.SpeedCode.FAST_400K;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxI2cDeviceSynch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */