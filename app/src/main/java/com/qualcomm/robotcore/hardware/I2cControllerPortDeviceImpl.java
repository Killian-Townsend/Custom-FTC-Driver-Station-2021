package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;

public abstract class I2cControllerPortDeviceImpl implements RobotArmingStateNotifier.Callback, I2cControllerPortDevice {
  protected final I2cController controller;
  
  protected final int physicalPort;
  
  protected I2cControllerPortDeviceImpl(I2cController paramI2cController, int paramInt) {
    this.controller = paramI2cController;
    this.physicalPort = paramInt;
  }
  
  protected void controllerNowArmedOrPretending() {}
  
  protected void controllerNowDisarmed() {}
  
  protected void finishConstruction() {
    controllerNowArmedOrPretending();
    I2cController i2cController = this.controller;
    if (i2cController instanceof RobotArmingStateNotifier)
      ((RobotArmingStateNotifier)i2cController).registerCallback(this, false); 
  }
  
  public I2cController getI2cController() {
    return this.controller;
  }
  
  public int getPort() {
    return this.physicalPort;
  }
  
  public void onModuleStateChange(RobotArmingStateNotifier paramRobotArmingStateNotifier, RobotArmingStateNotifier.ARMINGSTATE paramARMINGSTATE) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic com/qualcomm/robotcore/hardware/I2cControllerPortDeviceImpl$1.$SwitchMap$com$qualcomm$robotcore$hardware$usb$RobotArmingStateNotifier$ARMINGSTATE : [I
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
    //   30: invokevirtual controllerNowDisarmed : ()V
    //   33: goto -> 40
    //   36: aload_0
    //   37: invokevirtual controllerNowArmedOrPretending : ()V
    //   40: aload_0
    //   41: monitorexit
    //   42: return
    //   43: astore_1
    //   44: aload_0
    //   45: monitorexit
    //   46: aload_1
    //   47: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	43	finally
    //   29	33	43	finally
    //   36	40	43	finally
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cControllerPortDeviceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */