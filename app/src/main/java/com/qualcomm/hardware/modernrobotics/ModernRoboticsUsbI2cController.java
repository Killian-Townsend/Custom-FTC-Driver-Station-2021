package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public abstract class ModernRoboticsUsbI2cController extends ModernRoboticsUsbDevice implements I2cController {
  protected final I2cController.I2cPortReadyBeginEndNotifications[] i2cPortReadyBeginEndCallbacks;
  
  protected boolean notificationsActive;
  
  protected final int numberOfI2cPorts;
  
  public ModernRoboticsUsbI2cController(int paramInt, Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, ModernRoboticsUsbDevice.CreateReadWriteRunnable paramCreateReadWriteRunnable) throws RobotCoreException, InterruptedException {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, paramCreateReadWriteRunnable);
    this.numberOfI2cPorts = paramInt;
    this.i2cPortReadyBeginEndCallbacks = new I2cController.I2cPortReadyBeginEndNotifications[paramInt];
    this.notificationsActive = false;
  }
  
  public void deregisterForPortReadyBeginEndCallback(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual throwIfI2cPortIsInvalid : (I)V
    //   7: aload_0
    //   8: getfield i2cPortReadyBeginEndCallbacks : [Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyBeginEndNotifications;
    //   11: iload_1
    //   12: aaload
    //   13: astore_2
    //   14: aload_2
    //   15: ifnull -> 39
    //   18: aload_0
    //   19: getfield i2cPortReadyBeginEndCallbacks : [Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyBeginEndNotifications;
    //   22: iload_1
    //   23: aaload
    //   24: iload_1
    //   25: invokeinterface onPortIsReadyCallbacksEnd : (I)V
    //   30: goto -> 39
    //   33: invokestatic currentThread : ()Ljava/lang/Thread;
    //   36: invokevirtual interrupt : ()V
    //   39: aload_0
    //   40: getfield i2cPortReadyBeginEndCallbacks : [Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyBeginEndNotifications;
    //   43: iload_1
    //   44: aconst_null
    //   45: aastore
    //   46: aload_0
    //   47: monitorexit
    //   48: return
    //   49: astore_2
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_2
    //   53: athrow
    //   54: astore_2
    //   55: goto -> 33
    // Exception table:
    //   from	to	target	type
    //   2	14	49	finally
    //   18	30	54	java/lang/InterruptedException
    //   18	30	49	finally
    //   33	39	49	finally
    //   39	46	49	finally
  }
  
  public I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual throwIfI2cPortIsInvalid : (I)V
    //   7: aload_0
    //   8: getfield i2cPortReadyBeginEndCallbacks : [Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyBeginEndNotifications;
    //   11: iload_1
    //   12: aaload
    //   13: astore_2
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_2
    //   17: areturn
    //   18: astore_2
    //   19: aload_0
    //   20: monitorexit
    //   21: aload_2
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	18	finally
  }
  
  public boolean isArmed() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokespecial isArmed : ()Z
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_2
    //   4: invokevirtual throwIfI2cPortIsInvalid : (I)V
    //   7: aload_1
    //   8: ifnull -> 51
    //   11: aload_0
    //   12: iload_2
    //   13: invokevirtual deregisterForPortReadyBeginEndCallback : (I)V
    //   16: aload_0
    //   17: getfield i2cPortReadyBeginEndCallbacks : [Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyBeginEndNotifications;
    //   20: iload_2
    //   21: aload_1
    //   22: aastore
    //   23: aload_0
    //   24: getfield notificationsActive : Z
    //   27: istore_3
    //   28: iload_3
    //   29: ifeq -> 48
    //   32: aload_1
    //   33: iload_2
    //   34: invokeinterface onPortIsReadyCallbacksBegin : (I)V
    //   39: goto -> 48
    //   42: invokestatic currentThread : ()Ljava/lang/Thread;
    //   45: invokevirtual interrupt : ()V
    //   48: aload_0
    //   49: monitorexit
    //   50: return
    //   51: new java/lang/IllegalArgumentException
    //   54: dup
    //   55: ldc 'illegal null: registerForI2cNotificationsCallback(null,%d)'
    //   57: iconst_1
    //   58: anewarray java/lang/Object
    //   61: dup
    //   62: iconst_0
    //   63: iload_2
    //   64: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   67: aastore
    //   68: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   71: invokespecial <init> : (Ljava/lang/String;)V
    //   74: athrow
    //   75: astore_1
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_1
    //   79: athrow
    //   80: astore_1
    //   81: goto -> 42
    // Exception table:
    //   from	to	target	type
    //   2	7	75	finally
    //   11	28	75	finally
    //   32	39	80	java/lang/InterruptedException
    //   32	39	75	finally
    //   42	48	75	finally
    //   51	75	75	finally
  }
  
  public void shutdownComplete() throws InterruptedException {
    if (this.i2cPortReadyBeginEndCallbacks != null)
      for (int i = 0; i < this.numberOfI2cPorts; i++) {
        I2cController.I2cPortReadyBeginEndNotifications i2cPortReadyBeginEndNotifications = this.i2cPortReadyBeginEndCallbacks[i];
        if (i2cPortReadyBeginEndNotifications != null)
          i2cPortReadyBeginEndNotifications.onPortIsReadyCallbacksEnd(i); 
      }  
    this.notificationsActive = false;
  }
  
  public void startupComplete() throws InterruptedException {
    this.notificationsActive = true;
    if (this.i2cPortReadyBeginEndCallbacks != null)
      for (int i = 0; i < this.numberOfI2cPorts; i++) {
        I2cController.I2cPortReadyBeginEndNotifications i2cPortReadyBeginEndNotifications = this.i2cPortReadyBeginEndCallbacks[i];
        if (i2cPortReadyBeginEndNotifications != null)
          i2cPortReadyBeginEndNotifications.onPortIsReadyCallbacksBegin(i); 
      }  
  }
  
  protected void throwIfI2cPortIsInvalid(int paramInt) {
    if (paramInt >= 0 && paramInt < this.numberOfI2cPorts)
      return; 
    throw new IllegalArgumentException(String.format("port %d is invalid; valid ports are %d..%d", new Object[] { Integer.valueOf(paramInt), Integer.valueOf(0), Integer.valueOf(this.numberOfI2cPorts - 1) }));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbI2cController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */