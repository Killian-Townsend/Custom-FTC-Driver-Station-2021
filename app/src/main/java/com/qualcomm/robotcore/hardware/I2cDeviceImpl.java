package com.qualcomm.robotcore.hardware;

import com.qualcomm.robotcore.R;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import org.firstinspires.ftc.robotcore.internal.hardware.TimeWindow;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class I2cDeviceImpl extends I2cControllerPortDeviceImpl implements I2cDevice, HardwareDevice, I2cController.I2cPortReadyCallback {
  protected I2cController.I2cPortReadyCallback callback = null;
  
  protected AtomicInteger callbackCount = new AtomicInteger(0);
  
  public I2cDeviceImpl(I2cController paramI2cController, int paramInt) {
    super(paramI2cController, paramInt);
  }
  
  public void clearI2cPortActionFlag() {
    this.controller.clearI2cPortActionFlag(this.physicalPort);
  }
  
  public void close() {}
  
  protected void controllerNowArmedOrPretending() {}
  
  public void copyBufferIntoWriteBuffer(byte[] paramArrayOfbyte) {
    this.controller.copyBufferIntoWriteBuffer(this.physicalPort, paramArrayOfbyte);
  }
  
  public void deregisterForPortReadyBeginEndCallback() {
    this.controller.deregisterForPortReadyBeginEndCallback(this.physicalPort);
  }
  
  public void deregisterForPortReadyCallback() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   6: aload_0
    //   7: getfield physicalPort : I
    //   10: invokeinterface deregisterForPortReadyCallback : (I)V
    //   15: aload_0
    //   16: aconst_null
    //   17: putfield callback : Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyCallback;
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_1
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_1
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  @Deprecated
  public void enableI2cReadMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2) {
    this.controller.enableI2cReadMode(this.physicalPort, paramI2cAddr, paramInt1, paramInt2);
  }
  
  @Deprecated
  public void enableI2cWriteMode(I2cAddr paramI2cAddr, int paramInt1, int paramInt2) {
    this.controller.enableI2cWriteMode(this.physicalPort, paramI2cAddr, paramInt1, paramInt2);
  }
  
  public int getCallbackCount() {
    return this.callbackCount.get();
  }
  
  public String getConnectionInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.controller.getConnectionInfo());
    stringBuilder.append("; port ");
    stringBuilder.append(this.physicalPort);
    return stringBuilder.toString();
  }
  
  public I2cController getController() {
    return this.controller;
  }
  
  public byte[] getCopyOfReadBuffer() {
    return this.controller.getCopyOfReadBuffer(this.physicalPort);
  }
  
  public byte[] getCopyOfWriteBuffer() {
    return this.controller.getCopyOfWriteBuffer(this.physicalPort);
  }
  
  public String getDeviceName() {
    return AppUtil.getDefContext().getString(R.string.configTypeI2cDevice);
  }
  
  public I2cController.I2cPortReadyCallback getI2cPortReadyCallback() {
    return this.callback;
  }
  
  public byte[] getI2cReadCache() {
    return this.controller.getI2cReadCache(this.physicalPort);
  }
  
  public Lock getI2cReadCacheLock() {
    return this.controller.getI2cReadCacheLock(this.physicalPort);
  }
  
  public TimeWindow getI2cReadCacheTimeWindow() {
    return this.controller.getI2cReadCacheTimeWindow(this.physicalPort);
  }
  
  public byte[] getI2cWriteCache() {
    return this.controller.getI2cWriteCache(this.physicalPort);
  }
  
  public Lock getI2cWriteCacheLock() {
    return this.controller.getI2cWriteCacheLock(this.physicalPort);
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    return this.controller.getManufacturer();
  }
  
  public int getMaxI2cWriteLatency() {
    return this.controller.getMaxI2cWriteLatency(this.physicalPort);
  }
  
  public int getPort() {
    return this.physicalPort;
  }
  
  public I2cController.I2cPortReadyBeginEndNotifications getPortReadyBeginEndCallback() {
    return this.controller.getPortReadyBeginEndCallback(this.physicalPort);
  }
  
  public int getVersion() {
    return 1;
  }
  
  public boolean isArmed() {
    return this.controller.isArmed();
  }
  
  public boolean isI2cPortActionFlagSet() {
    return this.controller.isI2cPortActionFlagSet(this.physicalPort);
  }
  
  public boolean isI2cPortInReadMode() {
    return this.controller.isI2cPortInReadMode(this.physicalPort);
  }
  
  public boolean isI2cPortInWriteMode() {
    return this.controller.isI2cPortInWriteMode(this.physicalPort);
  }
  
  public boolean isI2cPortReady() {
    return this.controller.isI2cPortReady(this.physicalPort);
  }
  
  public void portIsReady(int paramInt) {
    this.callbackCount.incrementAndGet();
    I2cController.I2cPortReadyCallback i2cPortReadyCallback = this.callback;
    if (i2cPortReadyCallback != null)
      i2cPortReadyCallback.portIsReady(paramInt); 
  }
  
  public void readI2cCacheFromController() {
    this.controller.readI2cCacheFromController(this.physicalPort);
  }
  
  @Deprecated
  public void readI2cCacheFromModule() {
    readI2cCacheFromController();
  }
  
  public void registerForI2cPortReadyCallback(I2cController.I2cPortReadyCallback paramI2cPortReadyCallback) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield callback : Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyCallback;
    //   7: aload_0
    //   8: getfield controller : Lcom/qualcomm/robotcore/hardware/I2cController;
    //   11: aload_0
    //   12: aload_0
    //   13: getfield physicalPort : I
    //   16: invokeinterface registerForI2cPortReadyCallback : (Lcom/qualcomm/robotcore/hardware/I2cController$I2cPortReadyCallback;I)V
    //   21: aload_0
    //   22: monitorexit
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	24	finally
  }
  
  public void registerForPortReadyBeginEndCallback(I2cController.I2cPortReadyBeginEndNotifications paramI2cPortReadyBeginEndNotifications) {
    this.controller.registerForPortReadyBeginEndCallback(paramI2cPortReadyBeginEndNotifications, this.physicalPort);
  }
  
  public void resetDeviceConfigurationForOpMode() {}
  
  public void setI2cPortActionFlag() {
    this.controller.setI2cPortActionFlag(this.physicalPort);
  }
  
  public void writeI2cCacheToController() {
    this.controller.writeI2cCacheToController(this.physicalPort);
  }
  
  @Deprecated
  public void writeI2cCacheToModule() {
    writeI2cCacheToController();
  }
  
  public void writeI2cPortFlagOnlyToController() {
    this.controller.writeI2cPortFlagOnlyToController(this.physicalPort);
  }
  
  @Deprecated
  public void writeI2cPortFlagOnlyToModule() {
    writeI2cPortFlagOnlyToController();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardware\I2cDeviceImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */