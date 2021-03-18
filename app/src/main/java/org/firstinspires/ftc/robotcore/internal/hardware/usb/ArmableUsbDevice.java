package org.firstinspires.ftc.robotcore.internal.hardware.usb;

import android.content.Context;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDeviceCloseOnTearDown;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.GlobalWarningSource;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.external.function.InterruptableThrowingRunnable;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Misc;
import org.firstinspires.ftc.robotcore.internal.system.Tracer;

public abstract class ArmableUsbDevice implements RobotUsbModule, GlobalWarningSource, HardwareDeviceCloseOnTearDown {
  public static boolean TRACE = false;
  
  protected final Object armingLock = new Object();
  
  protected RobotArmingStateNotifier.ARMINGSTATE armingState;
  
  protected final Context context;
  
  protected OpenRobotUsbDevice openRobotUsbDevice;
  
  protected final AtomicInteger referenceCount;
  
  protected final WeakReferenceSet<RobotArmingStateNotifier.Callback> registeredCallbacks = new WeakReferenceSet();
  
  protected RobotUsbDevice robotUsbDevice;
  
  protected final SerialNumber serialNumber;
  
  protected SyncdDevice.Manager syncdDeviceManager;
  
  protected Tracer tracer = Tracer.create(getTag(), TRACE);
  
  protected String warningMessage;
  
  protected final Object warningMessageLock = new Object();
  
  protected int warningMessageSuppressionCount;
  
  public ArmableUsbDevice(Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, OpenRobotUsbDevice paramOpenRobotUsbDevice) {
    this.context = paramContext;
    this.serialNumber = paramSerialNumber;
    this.syncdDeviceManager = paramManager;
    this.openRobotUsbDevice = paramOpenRobotUsbDevice;
    this.referenceCount = new AtomicInteger(1);
    this.robotUsbDevice = null;
    this.armingState = RobotArmingStateNotifier.ARMINGSTATE.DISARMED;
    this.warningMessageSuppressionCount = 0;
    this.warningMessage = "";
  }
  
  public void addRef() {
    while (true) {
      boolean bool;
      int i = this.referenceCount.get();
      if (i > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      if (i <= 0)
        return; 
      int j = i + 1;
      if (this.referenceCount.compareAndSet(i, j)) {
        this.tracer.trace("0x%08x: addRef [%s]=%d", new Object[] { Integer.valueOf(hashCode()), getSerialNumber(), Integer.valueOf(j) });
        return;
      } 
    } 
  }
  
  public void arm() throws RobotCoreException, InterruptedException {
    this.tracer.trace("arm()", new InterruptableThrowingRunnable<RobotCoreException>() {
          public void run() throws RobotCoreException, InterruptedException {
            synchronized (ArmableUsbDevice.this.armingLock) {
              int i = ArmableUsbDevice.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[ArmableUsbDevice.this.armingState.ordinal()];
              if (i != 1) {
                if (i == 2) {
                  RobotArmingStateNotifier.ARMINGSTATE aRMINGSTATE = ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_ARMED);
                  try {
                    ArmableUsbDevice.this.doArm();
                    ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.ARMED);
                    return;
                  } catch (Exception exception) {
                    ArmableUsbDevice.this.setArmingState(aRMINGSTATE);
                    throw exception;
                  } 
                } 
                throw new RobotCoreException("illegal state: can't arm() from state %s", new Object[] { this.this$0.armingState.toString() });
              } 
              return;
            } 
            ArmableUsbDevice.this.disarm();
            throw SYNTHETIC_LOCAL_VARIABLE_2;
          }
        });
  }
  
  protected void armDevice() throws RobotCoreException, InterruptedException {
    synchronized (this.armingLock) {
      boolean bool;
      internalClearGlobalWarning();
      if (this.robotUsbDevice == null) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      RobotUsbDevice robotUsbDevice5 = null;
      RobotUsbDevice robotUsbDevice6 = null;
      RobotUsbDevice robotUsbDevice3 = null;
      RobotUsbDevice robotUsbDevice4 = robotUsbDevice3;
      RobotUsbDevice robotUsbDevice1 = robotUsbDevice5;
      RobotUsbDevice robotUsbDevice2 = robotUsbDevice6;
      try {
        this.tracer.trace("opening %s...", new Object[] { this.serialNumber });
        robotUsbDevice4 = robotUsbDevice3;
        robotUsbDevice1 = robotUsbDevice5;
        robotUsbDevice2 = robotUsbDevice6;
        robotUsbDevice3 = this.openRobotUsbDevice.open();
        robotUsbDevice4 = robotUsbDevice3;
        robotUsbDevice1 = robotUsbDevice3;
        robotUsbDevice2 = robotUsbDevice3;
        this.tracer.trace("...opening, now arming %s...", new Object[] { this.serialNumber });
        robotUsbDevice4 = robotUsbDevice3;
        robotUsbDevice1 = robotUsbDevice3;
        robotUsbDevice2 = robotUsbDevice3;
        armDevice(robotUsbDevice3);
        robotUsbDevice4 = robotUsbDevice3;
        robotUsbDevice1 = robotUsbDevice3;
        robotUsbDevice2 = robotUsbDevice3;
        this.tracer.trace("...arming %s...", new Object[] { this.serialNumber });
        return;
      } catch (RobotCoreException robotCoreException2) {
        robotUsbDevice1 = robotUsbDevice2;
        RobotCoreException robotCoreException1 = robotCoreException2;
      } catch (RuntimeException runtimeException) {
      
      } catch (InterruptedException interruptedException) {
        if (robotUsbDevice4 != null)
          robotUsbDevice4.close(); 
        throw interruptedException;
      } 
      RobotLog.logExceptionHeader(getTag(), runtimeException, "exception arming %s", new Object[] { this.serialNumber });
      if (interruptedException != null)
        interruptedException.close(); 
      setGlobalWarning(getUnableToOpenMessage());
      throw runtimeException;
    } 
  }
  
  protected abstract void armDevice(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException, InterruptedException;
  
  public void armOrPretend() throws RobotCoreException, InterruptedException {
    synchronized (this.armingLock) {
      arm();
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_1} */
  }
  
  public void clearGlobalWarning() {
    synchronized (this.warningMessageLock) {
      internalClearGlobalWarning();
      this.warningMessageSuppressionCount = 0;
      return;
    } 
  }
  
  public void close() {
    while (true) {
      int i = this.referenceCount.get();
      if (i <= 0)
        return; 
      if (this.referenceCount.compareAndSet(i, 0)) {
        doClose();
        return;
      } 
    } 
  }
  
  protected String composeGlobalWarning() {
    return this.warningMessage;
  }
  
  public void disarm() throws RobotCoreException, InterruptedException {
    this.tracer.trace("disarm()", new InterruptableThrowingRunnable<RobotCoreException>() {
          public void run() throws RobotCoreException, InterruptedException {
            synchronized (ArmableUsbDevice.this.armingLock) {
              int i = ArmableUsbDevice.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[ArmableUsbDevice.this.armingState.ordinal()];
              if (i != 1)
                if (i != 2) {
                  if (i != 3 && i != 4 && i != 5)
                    throw new RobotCoreException("illegal state: can't disarm() from state %s", new Object[] { this.this$0.armingState.toString() }); 
                } else {
                  return;
                }  
              RobotArmingStateNotifier.ARMINGSTATE aRMINGSTATE = ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_DISARMED);
              try {
                ArmableUsbDevice.this.doDisarm();
                ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.DISARMED);
              } catch (InterruptedException interruptedException) {
                ArmableUsbDevice.this.setArmingState(aRMINGSTATE);
                Thread.currentThread().interrupt();
              } catch (Exception exception) {
                RobotLog.ee(ArmableUsbDevice.this.getTag(), exception, "exception thrown during disarm()");
                ArmableUsbDevice.this.setArmingState(aRMINGSTATE);
                throw exception;
              } 
              return;
            } 
          }
        });
  }
  
  protected abstract void disarmDevice() throws InterruptedException;
  
  protected void doArm() throws RobotCoreException, InterruptedException {
    armDevice();
  }
  
  protected void doClose() {
    this.tracer.trace("doClose()", new Runnable() {
          public void run() {
            synchronized (ArmableUsbDevice.this.armingLock) {
              Exception exception;
              ArmableUsbDevice.this.tracer.trace("doClose([%s])...", new Object[] { this.this$0.getSerialNumber() });
              try {
                int i = ArmableUsbDevice.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[ArmableUsbDevice.this.armingState.ordinal()];
                if (i != 1) {
                  if (i != 6) {
                    ArmableUsbDevice.this.doCloseFromOther();
                  } else {
                    if (ArmableUsbDevice.this.robotUsbDevice != null) {
                      RobotLog.v("safety-closing USB device for %s", new Object[] { this.this$0.serialNumber });
                      ArmableUsbDevice.this.robotUsbDevice.close();
                      ArmableUsbDevice.this.robotUsbDevice = null;
                    } 
                    ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
                    ArmableUsbDevice.this.unregisterGlobalWarningSource();
                    return;
                  } 
                } else {
                  ArmableUsbDevice.this.doCloseFromArmed();
                } 
                if (ArmableUsbDevice.this.robotUsbDevice != null) {
                  RobotLog.v("safety-closing USB device for %s", new Object[] { this.this$0.serialNumber });
                  ArmableUsbDevice.this.robotUsbDevice.close();
                  ArmableUsbDevice.this.robotUsbDevice = null;
                } 
                ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
                ArmableUsbDevice.this.unregisterGlobalWarningSource();
                return;
              } catch (InterruptedException null) {
                Thread.currentThread().interrupt();
                if (ArmableUsbDevice.this.robotUsbDevice != null) {
                  RobotLog.v("safety-closing USB device for %s", new Object[] { this.this$0.serialNumber });
                  ArmableUsbDevice.this.robotUsbDevice.close();
                  ArmableUsbDevice.this.robotUsbDevice = null;
                } 
                ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
                ArmableUsbDevice.this.unregisterGlobalWarningSource();
                return;
              } catch (RobotCoreException|RuntimeException robotCoreException) {
                if (ArmableUsbDevice.this.robotUsbDevice != null) {
                  RobotLog.v("safety-closing USB device for %s", new Object[] { this.this$0.serialNumber });
                  ArmableUsbDevice.this.robotUsbDevice.close();
                  ArmableUsbDevice.this.robotUsbDevice = null;
                } 
                ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
                ArmableUsbDevice.this.unregisterGlobalWarningSource();
                return;
              } finally {
                exception = null;
              } 
              ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.CLOSED);
              ArmableUsbDevice.this.unregisterGlobalWarningSource();
              ArmableUsbDevice.this.tracer.trace("...doClose([%s])", new Object[] { this.this$0.getSerialNumber() });
              throw exception;
            } 
          }
        });
  }
  
  protected void doCloseFromArmed() throws RobotCoreException, InterruptedException {
    disarm();
  }
  
  protected void doCloseFromOther() throws RobotCoreException, InterruptedException {
    disarm();
  }
  
  protected void doDisarm() throws RobotCoreException, InterruptedException {
    disarmDevice();
  }
  
  protected void doPretend() throws RobotCoreException, InterruptedException {
    pretendDevice();
  }
  
  protected void finishConstruction() {
    registerGlobalWarningSource();
  }
  
  public RobotArmingStateNotifier.ARMINGSTATE getArmingState() {
    return this.armingState;
  }
  
  public Context getContext() {
    return this.context;
  }
  
  public String getGlobalWarning() {
    synchronized (this.warningMessageLock) {
      String str;
      if (this.warningMessageSuppressionCount > 0) {
        str = "";
      } else {
        str = composeGlobalWarning();
      } 
      return str;
    } 
  }
  
  protected RobotUsbDevice getPretendDevice(SerialNumber paramSerialNumber) {
    return null;
  }
  
  public RobotUsbDevice getRobotUsbDevice() {
    return this.robotUsbDevice;
  }
  
  public SerialNumber getSerialNumber() {
    return this.serialNumber;
  }
  
  protected abstract String getTag();
  
  protected String getUnableToOpenMessage() {
    return Misc.formatForUser(R.string.warningUnableToOpen, new Object[] { SerialNumber.getDeviceDisplayName(this.serialNumber) });
  }
  
  protected void internalClearGlobalWarning() {
    synchronized (this.warningMessageLock) {
      if (!this.warningMessage.isEmpty())
        RobotLog.vv(getTag(), "clearing extant global warning: \"%s\"", new Object[] { this.warningMessage }); 
      this.warningMessage = "";
      return;
    } 
  }
  
  protected boolean isArmed() {
    return (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.ARMED);
  }
  
  protected boolean isArmedOrArming() {
    return (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.ARMED || this.armingState == RobotArmingStateNotifier.ARMINGSTATE.TO_ARMED);
  }
  
  protected boolean isPretending() {
    return (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.PRETENDING);
  }
  
  public void pretend() throws RobotCoreException, InterruptedException {
    this.tracer.trace("pretend()", new InterruptableThrowingRunnable<RobotCoreException>() {
          public void run() throws RobotCoreException, InterruptedException {
            Exception exception;
            Object object = ArmableUsbDevice.this.armingLock;
            /* monitor enter ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
            try {
              int i = ArmableUsbDevice.null.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE[ArmableUsbDevice.this.armingState.ordinal()];
              if (i != 2) {
                if (i == 3) {
                  /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
                  return;
                } 
                throw new RobotCoreException("illegal state: can't pretend() from state %s", new Object[] { this.this$0.armingState.toString() });
              } 
              RobotArmingStateNotifier.ARMINGSTATE aRMINGSTATE = ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.TO_PRETENDING);
              try {
                ArmableUsbDevice.this.doPretend();
                ArmableUsbDevice.this.setArmingState(RobotArmingStateNotifier.ARMINGSTATE.PRETENDING);
                /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=null} */
                return;
              } catch (Exception exception1) {
                RobotLog.logExceptionHeader(ArmableUsbDevice.this.getTag(), exception1, "exception while pretending; reverting to %s", new Object[] { aRMINGSTATE });
                ArmableUsbDevice.this.setArmingState(aRMINGSTATE);
                throw exception1;
              } 
            } catch (RobotCoreException robotCoreException) {
            
            } catch (RuntimeException null) {
            
            } catch (InterruptedException null) {
            
            } finally {}
            ArmableUsbDevice.this.disarm();
            throw exception;
          }
        });
  }
  
  protected void pretendDevice() throws RobotCoreException, InterruptedException {
    synchronized (this.armingLock) {
      pretendDevice(getPretendDevice(this.serialNumber));
      return;
    } 
  }
  
  protected void pretendDevice(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException, InterruptedException {
    armDevice(paramRobotUsbDevice);
  }
  
  public void registerCallback(RobotArmingStateNotifier.Callback paramCallback, boolean paramBoolean) {
    this.registeredCallbacks.add(paramCallback);
    if (paramBoolean)
      paramCallback.onModuleStateChange((RobotArmingStateNotifier)this, this.armingState); 
  }
  
  protected void registerGlobalWarningSource() {
    RobotLog.registerGlobalWarningSource(this);
  }
  
  public void releaseRef() {
    while (true) {
      boolean bool;
      int i = this.referenceCount.get();
      if (i > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      Assert.assertTrue(bool);
      if (i <= 0)
        return; 
      int j = i - 1;
      if (this.referenceCount.compareAndSet(i, j)) {
        this.tracer.trace("0x%08x: releaseRef [%s]=%d", new Object[] { Integer.valueOf(hashCode()), getSerialNumber(), Integer.valueOf(j) });
        if (j == 0)
          doClose(); 
        return;
      } 
    } 
  }
  
  protected RobotArmingStateNotifier.ARMINGSTATE setArmingState(RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE) {
    RobotArmingStateNotifier.ARMINGSTATE aRMINGSTATE = this.armingState;
    this.armingState = paramARMINGSTATE;
    Iterator<RobotArmingStateNotifier.Callback> iterator = this.registeredCallbacks.iterator();
    while (iterator.hasNext())
      ((RobotArmingStateNotifier.Callback)iterator.next()).onModuleStateChange((RobotArmingStateNotifier)this, paramARMINGSTATE); 
    return aRMINGSTATE;
  }
  
  public void setGlobalWarning(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield warningMessageLock : Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: aload_1
    //   8: ifnull -> 26
    //   11: aload_0
    //   12: getfield warningMessage : Ljava/lang/String;
    //   15: invokevirtual isEmpty : ()Z
    //   18: ifeq -> 26
    //   21: aload_0
    //   22: aload_1
    //   23: putfield warningMessage : Ljava/lang/String;
    //   26: aload_2
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_2
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   11	26	29	finally
    //   26	28	29	finally
    //   30	32	29	finally
  }
  
  public void suppressGlobalWarning(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield warningMessageLock : Ljava/lang/Object;
    //   4: astore_2
    //   5: aload_2
    //   6: monitorenter
    //   7: iload_1
    //   8: ifeq -> 24
    //   11: aload_0
    //   12: aload_0
    //   13: getfield warningMessageSuppressionCount : I
    //   16: iconst_1
    //   17: iadd
    //   18: putfield warningMessageSuppressionCount : I
    //   21: goto -> 34
    //   24: aload_0
    //   25: aload_0
    //   26: getfield warningMessageSuppressionCount : I
    //   29: iconst_1
    //   30: isub
    //   31: putfield warningMessageSuppressionCount : I
    //   34: aload_2
    //   35: monitorexit
    //   36: return
    //   37: astore_3
    //   38: aload_2
    //   39: monitorexit
    //   40: aload_3
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   11	21	37	finally
    //   24	34	37	finally
    //   34	36	37	finally
    //   38	40	37	finally
  }
  
  public void unregisterCallback(RobotArmingStateNotifier.Callback paramCallback) {
    this.registeredCallbacks.remove(paramCallback);
  }
  
  protected void unregisterGlobalWarningSource() {
    RobotLog.unregisterGlobalWarningSource(this);
  }
  
  public static interface OpenRobotUsbDevice {
    RobotUsbDevice open() throws RobotCoreException, InterruptedException;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\hardwar\\usb\ArmableUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */