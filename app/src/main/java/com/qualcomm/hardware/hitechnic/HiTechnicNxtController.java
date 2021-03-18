package com.qualcomm.hardware.hitechnic;

import android.content.Context;
import com.qualcomm.robotcore.hardware.Engagable;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cController;
import com.qualcomm.robotcore.hardware.I2cControllerPortDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceImpl;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchImpl;
import com.qualcomm.robotcore.hardware.I2cWaitControl;

public abstract class HiTechnicNxtController extends I2cControllerPortDeviceImpl implements Engagable, HardwareDevice {
  protected Context context;
  
  protected I2cDevice i2cDevice;
  
  protected I2cDeviceSynch i2cDeviceSynch;
  
  protected boolean isEngaged;
  
  protected boolean isHardwareInitialized;
  
  protected boolean isHooked;
  
  public HiTechnicNxtController(Context paramContext, I2cController paramI2cController, int paramInt, I2cAddr paramI2cAddr) {
    super(paramI2cController, paramInt);
    this.context = paramContext;
    this.isEngaged = true;
    this.isHooked = false;
    this.isHardwareInitialized = false;
    this.i2cDevice = (I2cDevice)new I2cDeviceImpl(paramI2cController, paramInt);
    this.i2cDeviceSynch = (I2cDeviceSynch)new I2cDeviceSynchImpl(this.i2cDevice, paramI2cAddr, true);
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
    //   18: getfield i2cDeviceSynch : Lcom/qualcomm/robotcore/hardware/I2cDeviceSynch;
    //   21: invokeinterface close : ()V
    //   26: aload_0
    //   27: monitorexit
    //   28: return
    //   29: astore_1
    //   30: aload_0
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   2	17	29	finally
    //   17	26	29	finally
  }
  
  protected void controllerNowDisarmed() {
    if (this.isHooked)
      unhook(); 
  }
  
  public void disengage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield isEngaged : Z
    //   7: aload_0
    //   8: invokevirtual adjustHookingToMatchEngagement : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  protected void doHook() {}
  
  protected void doUnhook() {}
  
  public void engage() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_1
    //   4: putfield isEngaged : Z
    //   7: aload_0
    //   8: invokevirtual adjustHookingToMatchEngagement : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  protected abstract void floatHardware();
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return HardwareDevice.Manufacturer.HiTechnic;
  }
  
  protected void hook() {
    doHook();
    this.isHooked = true;
    initializeHardwareIfNecessary();
  }
  
  protected abstract void initializeHardware();
  
  void initializeHardwareIfNecessary() {
    if (!this.isHardwareInitialized && isArmed()) {
      this.isHardwareInitialized = true;
      initializeHardware();
    } 
  }
  
  protected boolean isArmed() {
    return this.i2cDeviceSynch.isArmed();
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
    //   2	7	11	finally
  }
  
  protected byte read8(int paramInt) {
    return isEngaged() ? this.i2cDeviceSynch.read8(paramInt) : 0;
  }
  
  protected void unhook() {
    doUnhook();
    this.isHooked = false;
  }
  
  protected void write(int paramInt, byte[] paramArrayOfbyte) {
    if (isEngaged())
      this.i2cDeviceSynch.write(paramInt, paramArrayOfbyte, I2cWaitControl.NONE); 
  }
  
  protected void write8(int paramInt, byte paramByte) {
    if (isEngaged())
      this.i2cDeviceSynch.write8(paramInt, paramByte, I2cWaitControl.NONE); 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\hitechnic\HiTechnicNxtController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */