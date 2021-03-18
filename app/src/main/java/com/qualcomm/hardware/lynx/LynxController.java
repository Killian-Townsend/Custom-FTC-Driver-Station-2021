package com.qualcomm.hardware.lynx;

import android.content.Context;
import com.qualcomm.hardware.lynx.commands.LynxCommand;
import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.Engagable;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDeviceHealth;
import com.qualcomm.robotcore.hardware.HardwareDeviceHealthImpl;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.util.concurrent.Callable;

public abstract class LynxController extends LynxCommExceptionHandler implements Engagable, HardwareDevice, HardwareDeviceHealth, RobotArmingStateNotifier.Callback, RobotArmingStateNotifier {
  protected Context context;
  
  protected final HardwareDeviceHealthImpl hardwareDeviceHealth;
  
  protected boolean isEngaged;
  
  protected boolean isHardwareInitialized;
  
  protected boolean isHooked;
  
  private LynxModule module;
  
  private LynxModuleIntf pretendModule;
  
  protected final WeakReferenceSet<RobotArmingStateNotifier.Callback> registeredCallbacks = new WeakReferenceSet();
  
  public LynxController(Context paramContext, LynxModule paramLynxModule) {
    this.context = paramContext;
    this.module = paramLynxModule;
    this.isEngaged = true;
    this.isHooked = false;
    this.isHardwareInitialized = false;
    this.pretendModule = new PretendLynxModule();
    this.hardwareDeviceHealth = new HardwareDeviceHealthImpl(getTag(), getHealthStatusOverride());
    this.module.noteController(this);
  }
  
  protected void adjustHookingToMatchEngagement() {
    if (!this.isHooked && this.isEngaged) {
      hook();
      return;
    } 
    if (this.isHooked && !this.isEngaged)
      unhook(); 
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isEngaged : ()Z
    //   6: ifeq -> 17
    //   9: aload_0
    //   10: invokevirtual floatHardware : ()V
    //   13: aload_0
    //   14: invokevirtual disengage : ()V
    //   17: aload_0
    //   18: getstatic com/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus.CLOSED : Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;
    //   21: invokevirtual setHealthStatus : (Lcom/qualcomm/robotcore/hardware/HardwareDeviceHealth$HealthStatus;)V
    //   24: aload_0
    //   25: monitorexit
    //   26: return
    //   27: astore_1
    //   28: aload_0
    //   29: monitorexit
    //   30: aload_1
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	27	finally
    //   17	24	27	finally
  }
  
  public void disengage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isEngaged : Z
    //   6: ifne -> 46
    //   9: aload_0
    //   10: invokevirtual getTag : ()Ljava/lang/String;
    //   13: ldc 'disengage mod#=%d'
    //   15: iconst_1
    //   16: anewarray java/lang/Object
    //   19: dup
    //   20: iconst_0
    //   21: aload_0
    //   22: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   25: invokeinterface getModuleAddress : ()I
    //   30: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   33: aastore
    //   34: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   37: aload_0
    //   38: iconst_0
    //   39: putfield isEngaged : Z
    //   42: aload_0
    //   43: invokevirtual adjustHookingToMatchEngagement : ()V
    //   46: aload_0
    //   47: monitorexit
    //   48: return
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	46	49	finally
    //   46	48	49	finally
    //   50	52	49	finally
  }
  
  protected void doHook() {}
  
  protected void doUnhook() {}
  
  public void engage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isEngaged : Z
    //   6: ifne -> 46
    //   9: aload_0
    //   10: invokevirtual getTag : ()Ljava/lang/String;
    //   13: ldc 'engaging mod#=%d'
    //   15: iconst_1
    //   16: anewarray java/lang/Object
    //   19: dup
    //   20: iconst_0
    //   21: aload_0
    //   22: invokevirtual getModule : ()Lcom/qualcomm/hardware/lynx/LynxModuleIntf;
    //   25: invokeinterface getModuleAddress : ()I
    //   30: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   33: aastore
    //   34: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   37: aload_0
    //   38: iconst_1
    //   39: putfield isEngaged : Z
    //   42: aload_0
    //   43: invokevirtual adjustHookingToMatchEngagement : ()V
    //   46: aload_0
    //   47: monitorexit
    //   48: return
    //   49: astore_1
    //   50: aload_0
    //   51: monitorexit
    //   52: aload_1
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   2	46	49	finally
    //   46	48	49	finally
    //   50	52	49	finally
  }
  
  protected void finishConstruction() {
    moduleNowArmedOrPretending();
    this.module.registerCallback(this, false);
  }
  
  protected void floatHardware() {}
  
  public void forgetLastKnown() {}
  
  public RobotArmingStateNotifier.ARMINGSTATE getArmingState() {
    return this.module.getArmingState();
  }
  
  public String getConnectionInfo() {
    return getModule().getConnectionInfo();
  }
  
  public abstract String getDeviceName();
  
  public HardwareDeviceHealth.HealthStatus getHealthStatus() {
    return this.hardwareDeviceHealth.getHealthStatus();
  }
  
  protected Callable<HardwareDeviceHealth.HealthStatus> getHealthStatusOverride() {
    return new Callable<HardwareDeviceHealth.HealthStatus>() {
        public HardwareDeviceHealth.HealthStatus call() throws Exception {
          return (LynxController.this.module.getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.PRETENDING) ? HardwareDeviceHealth.HealthStatus.UNHEALTHY : HardwareDeviceHealth.HealthStatus.UNKNOWN;
        }
      };
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.Lynx;
  }
  
  protected LynxModuleIntf getModule() {
    return this.isHooked ? this.module : this.pretendModule;
  }
  
  public SerialNumber getSerialNumber() {
    return this.module.getSerialNumber();
  }
  
  protected abstract String getTag();
  
  public int getVersion() {
    return 1;
  }
  
  protected void hook() {
    doHook();
    this.isHooked = true;
    try {
      initializeHardwareIfNecessary();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.ee(getTag(), (Throwable)robotCoreException, "exception thrown in LynxController.hook()");
      return;
    } 
  }
  
  protected void initializeHardware() throws RobotCoreException, InterruptedException {}
  
  void initializeHardwareIfNecessary() throws RobotCoreException, InterruptedException {
    if (!this.isHardwareInitialized) {
      RobotLog.vv(getTag(), "initializeHardware() mod#=%d", new Object[] { Integer.valueOf(getModule().getModuleAddress()) });
      initializeHardware();
      this.isHardwareInitialized = isArmed();
    } 
  }
  
  protected boolean isArmed() {
    return (this.module.getArmingState() == RobotArmingStateNotifier.ARMINGSTATE.ARMED);
  }
  
  public boolean isEngaged() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isEngaged : Z
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
    //   2	9	11	finally
    //   12	14	11	finally
  }
  
  protected void moduleNowArmedOrPretending() {
    adjustHookingToMatchEngagement();
  }
  
  protected void moduleNowDisarmed() {
    if (this.isHooked)
      unhook(); 
  }
  
  public void onModuleStateChange(RobotArmingStateNotifier paramRobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/qualcomm/hardware/lynx/LynxController$2.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE : [I
    //   5: aload_2
    //   6: invokevirtual ordinal : ()I
    //   9: iaload
    //   10: istore_3
    //   11: iload_3
    //   12: iconst_1
    //   13: if_icmpeq -> 36
    //   16: iload_3
    //   17: iconst_2
    //   18: if_icmpeq -> 36
    //   21: iload_3
    //   22: iconst_3
    //   23: if_icmpeq -> 29
    //   26: goto -> 40
    //   29: aload_0
    //   30: invokevirtual moduleNowDisarmed : ()V
    //   33: goto -> 40
    //   36: aload_0
    //   37: invokevirtual moduleNowArmedOrPretending : ()V
    //   40: aload_0
    //   41: getfield registeredCallbacks : Lcom/qualcomm/robotcore/util/WeakReferenceSet;
    //   44: invokevirtual iterator : ()Ljava/util/Iterator;
    //   47: astore_1
    //   48: aload_1
    //   49: invokeinterface hasNext : ()Z
    //   54: ifeq -> 76
    //   57: aload_1
    //   58: invokeinterface next : ()Ljava/lang/Object;
    //   63: checkcast com/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier$Callback
    //   66: aload_0
    //   67: aload_2
    //   68: invokeinterface onModuleStateChange : (Lcom/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier;Lcom/qualcomm/robotcore/hardware/usb/RobotArmingStateNotifier$ARMINGSTATE;)V
    //   73: goto -> 48
    //   76: aload_0
    //   77: monitorexit
    //   78: return
    //   79: astore_1
    //   80: aload_0
    //   81: monitorexit
    //   82: aload_1
    //   83: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	79	finally
    //   29	33	79	finally
    //   36	40	79	finally
    //   40	48	79	finally
    //   48	73	79	finally
  }
  
  public void registerCallback(RobotArmingStateNotifier.Callback paramCallback, boolean paramBoolean) {
    this.registeredCallbacks.add(paramCallback);
    if (paramBoolean)
      paramCallback.onModuleStateChange(this, getArmingState()); 
  }
  
  public void resetDeviceConfigurationForOpMode() {
    try {
      initializeHardware();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } catch (RobotCoreException robotCoreException) {
      RobotLog.vv(getTag(), (Throwable)robotCoreException, "exception initializing hardware; ignored");
      return;
    } 
  }
  
  public void setHealthStatus(HardwareDeviceHealth.HealthStatus paramHealthStatus) {
    this.hardwareDeviceHealth.setHealthStatus(paramHealthStatus);
  }
  
  protected void setHealthyIfArmed() {
    if (isArmed())
      setHealthStatus(HardwareDeviceHealth.HealthStatus.HEALTHY); 
  }
  
  protected void unhook() {
    doUnhook();
    this.isHooked = false;
  }
  
  public void unregisterCallback(RobotArmingStateNotifier.Callback paramCallback) {
    this.registeredCallbacks.remove(paramCallback);
  }
  
  public class PretendLynxModule implements LynxModuleIntf {
    boolean isEngaged = true;
    
    public <T> T acquireI2cLockWhile(Supplier<T> param1Supplier) throws InterruptedException, RobotCoreException, LynxNackException {
      return param1Supplier.get();
    }
    
    public void acquireNetworkTransmissionLock(LynxMessage param1LynxMessage) throws InterruptedException {}
    
    public void close() {}
    
    public void disengage() {
      this.isEngaged = false;
    }
    
    public void engage() {
      this.isEngaged = true;
    }
    
    public void finishedWithMessage(LynxMessage param1LynxMessage) throws InterruptedException {}
    
    public String getConnectionInfo() {
      return LynxController.this.module.getConnectionInfo();
    }
    
    public String getDeviceName() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(LynxController.this.module.getDeviceName());
      stringBuilder.append(" (pretend)");
      return stringBuilder.toString();
    }
    
    public String getFirmwareVersionString() {
      return getDeviceName();
    }
    
    public int getInterfaceBaseCommandNumber(String param1String) {
      return LynxController.this.module.getInterfaceBaseCommandNumber(param1String);
    }
    
    public HardwareDevice.Manufacturer getManufacturer() {
      return HardwareDevice.Manufacturer.Lynx;
    }
    
    public int getModuleAddress() {
      return LynxController.this.module.getModuleAddress();
    }
    
    public String getNullableFirmwareVersionString() {
      return null;
    }
    
    public SerialNumber getSerialNumber() {
      return LynxController.this.module.getSerialNumber();
    }
    
    public int getVersion() {
      return 1;
    }
    
    public boolean isCommandSupported(Class<? extends LynxCommand> param1Class) {
      return false;
    }
    
    public boolean isEngaged() {
      return this.isEngaged;
    }
    
    public boolean isNotResponding() {
      return false;
    }
    
    public boolean isParent() {
      return true;
    }
    
    public void noteAttentionRequired() {}
    
    public void noteDatagramReceived() {}
    
    public void noteNotResponding() {}
    
    public void releaseNetworkTransmissionLock(LynxMessage param1LynxMessage) throws InterruptedException {}
    
    public void resetDeviceConfigurationForOpMode() {}
    
    public void resetPingTimer(LynxMessage param1LynxMessage) {}
    
    public void retransmit(LynxMessage param1LynxMessage) throws InterruptedException {}
    
    public void sendCommand(LynxMessage param1LynxMessage) throws InterruptedException, LynxUnsupportedCommandException {}
    
    public void validateCommand(LynxMessage param1LynxMessage) throws LynxUnsupportedCommandException {}
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */