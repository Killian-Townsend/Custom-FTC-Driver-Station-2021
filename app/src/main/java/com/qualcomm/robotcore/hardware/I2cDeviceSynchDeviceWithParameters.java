package com.qualcomm.robotcore.hardware;

public abstract class I2cDeviceSynchDeviceWithParameters<DEVICE_CLIENT extends I2cDeviceSynchSimple, PARAMETERS> extends I2cDeviceSynchDevice<DEVICE_CLIENT> {
  protected PARAMETERS parameters;
  
  protected I2cDeviceSynchDeviceWithParameters(DEVICE_CLIENT paramDEVICE_CLIENT, boolean paramBoolean, PARAMETERS paramPARAMETERS) {
    super(paramDEVICE_CLIENT, paramBoolean);
    this.parameters = paramPARAMETERS;
  }
  
  protected boolean doInitialize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield parameters : Ljava/lang/Object;
    //   7: invokevirtual internalInitialize : (Ljava/lang/Object;)Z
    //   10: istore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: iload_1
    //   14: ireturn
    //   15: astore_2
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_2
    //   19: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	15	finally
  }
  
  public PARAMETERS getParameters() {
    return this.parameters;
  }
  
  public boolean initialize(PARAMETERS paramPARAMETERS) {
    if (internalInitialize(paramPARAMETERS)) {
      this.isInitialized = true;
      return true;
    } 
    return false;
  }
  
  protected abstract boolean internalInitialize(PARAMETERS paramPARAMETERS);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceSynchDeviceWithParameters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */