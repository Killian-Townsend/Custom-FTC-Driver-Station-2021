package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.util.SerialNumber;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public abstract class ModernRoboticsUsbController extends ModernRoboticsUsbDevice {
  protected final Object callbackLock = new Object();
  
  protected final AtomicInteger callbackWaiterCount = new AtomicInteger();
  
  protected final Object concurrentClientLock = new Object();
  
  protected final AtomicLong readCompletionCount = new AtomicLong();
  
  protected boolean readWriteRunnableIsRunning = false;
  
  protected WRITE_STATUS writeStatus = WRITE_STATUS.IDLE;
  
  public ModernRoboticsUsbController(Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, ModernRoboticsUsbDevice.CreateReadWriteRunnable paramCreateReadWriteRunnable) throws RobotCoreException, InterruptedException {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice, paramCreateReadWriteRunnable);
  }
  
  protected boolean isOkToReadOrWrite() {
    return (isArmed() && this.readWriteRunnableIsRunning);
  }
  
  public byte[] read(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield concurrentClientLock : Ljava/lang/Object;
    //   4: astore #5
    //   6: aload #5
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield callbackLock : Ljava/lang/Object;
    //   13: astore #6
    //   15: aload #6
    //   17: monitorenter
    //   18: iconst_0
    //   19: istore #4
    //   21: iload #4
    //   23: istore_3
    //   24: aload_0
    //   25: getfield writeStatus : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   28: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS.IDLE : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   31: if_acmpeq -> 51
    //   34: aload_0
    //   35: invokevirtual isArmed : ()Z
    //   38: ifeq -> 109
    //   41: aload_0
    //   42: invokevirtual waitForCallback : ()Z
    //   45: ifne -> 21
    //   48: goto -> 109
    //   51: iload_3
    //   52: ifne -> 79
    //   55: aload_0
    //   56: invokevirtual isOkToReadOrWrite : ()Z
    //   59: ifeq -> 79
    //   62: aload_0
    //   63: iload_1
    //   64: iload_2
    //   65: invokespecial read : (II)[B
    //   68: astore #7
    //   70: aload #6
    //   72: monitorexit
    //   73: aload #5
    //   75: monitorexit
    //   76: aload #7
    //   78: areturn
    //   79: iload_2
    //   80: newarray byte
    //   82: astore #7
    //   84: aload #6
    //   86: monitorexit
    //   87: aload #5
    //   89: monitorexit
    //   90: aload #7
    //   92: areturn
    //   93: astore #7
    //   95: aload #6
    //   97: monitorexit
    //   98: aload #7
    //   100: athrow
    //   101: astore #6
    //   103: aload #5
    //   105: monitorexit
    //   106: aload #6
    //   108: athrow
    //   109: iconst_1
    //   110: istore_3
    //   111: goto -> 51
    // Exception table:
    //   from	to	target	type
    //   9	18	101	finally
    //   24	48	93	finally
    //   55	73	93	finally
    //   73	76	101	finally
    //   79	87	93	finally
    //   87	90	101	finally
    //   95	98	93	finally
    //   98	101	101	finally
    //   103	106	101	finally
  }
  
  public void readComplete() throws InterruptedException {
    synchronized (this.callbackLock) {
      super.readComplete();
      if (this.writeStatus == WRITE_STATUS.READ)
        this.writeStatus = WRITE_STATUS.IDLE; 
      this.readCompletionCount.incrementAndGet();
      this.callbackLock.notifyAll();
      return;
    } 
  }
  
  public void shutdownComplete() {
    this.readWriteRunnableIsRunning = false;
    synchronized (this.callbackLock) {
      this.writeStatus = WRITE_STATUS.IDLE;
      this.callbackLock.notifyAll();
      while (this.callbackWaiterCount.get() > 0)
        Thread.yield(); 
      return;
    } 
  }
  
  public void startupComplete() {
    this.readWriteRunnableIsRunning = true;
  }
  
  boolean waitForCallback() {
    this.callbackWaiterCount.incrementAndGet();
    boolean bool = this.readWriteRunnableIsRunning;
    boolean bool2 = true;
    if (bool) {
      boolean bool3;
      try {
        this.callbackLock.wait();
        bool3 = false;
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
        bool3 = true;
      } 
      if (bool3 || !this.readWriteRunnableIsRunning)
        bool2 = false; 
      this.callbackWaiterCount.decrementAndGet();
      return bool2;
    } 
    boolean bool1 = false;
  }
  
  boolean waitForNextReadComplete() {
    synchronized (this.concurrentClientLock) {
      synchronized (this.callbackLock) {
        long l = this.readCompletionCount.get();
        while (this.readCompletionCount.get() < l + 1L) {
          if (!isArmed())
            return false; 
          if (!waitForCallback())
            return false; 
        } 
        return true;
      } 
    } 
  }
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    // Byte code:
    //   0: aload_0
    //   1: getfield concurrentClientLock : Ljava/lang/Object;
    //   4: astore #5
    //   6: aload #5
    //   8: monitorenter
    //   9: aload_0
    //   10: getfield callbackLock : Ljava/lang/Object;
    //   13: astore #6
    //   15: aload #6
    //   17: monitorenter
    //   18: iconst_0
    //   19: istore #4
    //   21: iload #4
    //   23: istore_3
    //   24: aload_0
    //   25: getfield writeStatus : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   28: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS.DIRTY : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   31: if_acmpne -> 51
    //   34: aload_0
    //   35: invokevirtual isArmed : ()Z
    //   38: ifeq -> 94
    //   41: aload_0
    //   42: invokevirtual waitForCallback : ()Z
    //   45: ifne -> 21
    //   48: goto -> 94
    //   51: iload_3
    //   52: ifne -> 75
    //   55: aload_0
    //   56: invokevirtual isOkToReadOrWrite : ()Z
    //   59: ifeq -> 75
    //   62: aload_0
    //   63: getstatic com/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS.DIRTY : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   66: putfield writeStatus : Lcom/qualcomm/hardware/modernrobotics/ModernRoboticsUsbController$WRITE_STATUS;
    //   69: aload_0
    //   70: iload_1
    //   71: aload_2
    //   72: invokespecial write : (I[B)V
    //   75: aload #6
    //   77: monitorexit
    //   78: aload #5
    //   80: monitorexit
    //   81: return
    //   82: astore_2
    //   83: aload #6
    //   85: monitorexit
    //   86: aload_2
    //   87: athrow
    //   88: astore_2
    //   89: aload #5
    //   91: monitorexit
    //   92: aload_2
    //   93: athrow
    //   94: iconst_1
    //   95: istore_3
    //   96: goto -> 51
    // Exception table:
    //   from	to	target	type
    //   9	18	88	finally
    //   24	48	82	finally
    //   55	75	82	finally
    //   75	78	82	finally
    //   78	81	88	finally
    //   83	86	82	finally
    //   86	88	88	finally
    //   89	92	88	finally
  }
  
  public void writeComplete() throws InterruptedException {
    synchronized (this.callbackLock) {
      super.writeComplete();
      if (this.writeStatus == WRITE_STATUS.DIRTY)
        this.writeStatus = WRITE_STATUS.READ; 
      this.callbackLock.notifyAll();
      return;
    } 
  }
  
  protected enum WRITE_STATUS {
    DIRTY, IDLE, READ;
    
    static {
      WRITE_STATUS wRITE_STATUS = new WRITE_STATUS("READ", 2);
      READ = wRITE_STATUS;
      $VALUES = new WRITE_STATUS[] { IDLE, DIRTY, wRITE_STATUS };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */