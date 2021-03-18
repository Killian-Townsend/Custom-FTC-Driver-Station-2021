package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;

public abstract class I2cDeviceSynchDevice<DEVICE_CLIENT extends I2cDeviceSynchSimple> implements RobotArmingStateNotifier.Callback, HardwareDevice {
  protected DEVICE_CLIENT deviceClient;
  
  protected boolean deviceClientIsOwned;
  
  protected boolean isInitialized;
  
  protected I2cDeviceSynchDevice(DEVICE_CLIENT paramDEVICE_CLIENT, boolean paramBoolean) {
    this.deviceClient = paramDEVICE_CLIENT;
    this.deviceClientIsOwned = paramBoolean;
    this.isInitialized = false;
    paramDEVICE_CLIENT.enableWriteCoalescing(false);
  }
  
  public void close() {
    if (this.deviceClientIsOwned)
      this.deviceClient.close(); 
  }
  
  protected void disengage() {
    DEVICE_CLIENT dEVICE_CLIENT = this.deviceClient;
    if (dEVICE_CLIENT instanceof Engagable)
      ((Engagable)dEVICE_CLIENT).disengage(); 
  }
  
  protected abstract boolean doInitialize();
  
  protected void engage() {
    DEVICE_CLIENT dEVICE_CLIENT = this.deviceClient;
    if (dEVICE_CLIENT instanceof Engagable)
      ((Engagable)dEVICE_CLIENT).engage(); 
  }
  
  public String getConnectionInfo() {
    return this.deviceClient.getConnectionInfo();
  }
  
  public DEVICE_CLIENT getDeviceClient() {
    return this.deviceClient;
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean initialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual doInitialize : ()Z
    //   6: istore_1
    //   7: aload_0
    //   8: iload_1
    //   9: putfield isInitialized : Z
    //   12: aload_0
    //   13: monitorexit
    //   14: iload_1
    //   15: ireturn
    //   16: astore_2
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_2
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	12	16	finally
  }
  
  protected void initializeIfNecessary() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield isInitialized : Z
    //   6: ifne -> 14
    //   9: aload_0
    //   10: invokevirtual initialize : ()Z
    //   13: pop
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: astore_1
    //   18: aload_0
    //   19: monitorexit
    //   20: aload_1
    //   21: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	17	finally
  }
  
  public void onModuleStateChange(RobotArmingStateNotifier paramRobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE) {
    if (paramARMINGSTATE == RobotArmingStateNotifier.ARMINGSTATE.ARMED) {
      initializeIfNecessary();
      return;
    } 
    if (paramARMINGSTATE == RobotArmingStateNotifier.ARMINGSTATE.PRETENDING) {
      initializeIfNecessary();
      this.isInitialized = false;
    } 
  }
  
  protected void registerArmingStateCallback(boolean paramBoolean) {
    DEVICE_CLIENT dEVICE_CLIENT = this.deviceClient;
    if (dEVICE_CLIENT instanceof RobotArmingStateNotifier)
      ((RobotArmingStateNotifier)dEVICE_CLIENT).registerCallback(this, paramBoolean); 
  }
  
  public void resetDeviceConfigurationForOpMode() {
    this.deviceClient.resetDeviceConfigurationForOpMode();
    initialize();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */