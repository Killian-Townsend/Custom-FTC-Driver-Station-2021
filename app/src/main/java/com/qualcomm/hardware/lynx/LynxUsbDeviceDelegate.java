package com.qualcomm.hardware.lynx;

import com.qualcomm.hardware.lynx.commands.LynxMessage;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareDeviceCloseOnTearDown;
import com.qualcomm.robotcore.hardware.LynxModuleMetaList;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class LynxUsbDeviceDelegate implements LynxUsbDevice, HardwareDeviceCloseOnTearDown {
  public static String TAG = "LynxUsb";
  
  protected LynxUsbDeviceImpl delegate;
  
  protected boolean isOpen;
  
  protected boolean releaseOnClose;
  
  public LynxUsbDeviceDelegate(LynxUsbDeviceImpl paramLynxUsbDeviceImpl) {
    this.delegate = paramLynxUsbDeviceImpl;
    this.releaseOnClose = true;
    this.isOpen = true;
    RobotLog.vv(TAG, "0x%08x on 0x%08x: new delegate to [%s]", new Object[] { Integer.valueOf(hashCode()), Integer.valueOf(this.delegate.hashCode()), this.delegate.getSerialNumber() });
  }
  
  public void acquireNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    assertOpen();
    this.delegate.acquireNetworkTransmissionLock(paramLynxMessage);
  }
  
  public void addConfiguredModule(LynxModule paramLynxModule) throws RobotCoreException, InterruptedException, LynxNackException {
    assertOpen();
    this.delegate.addConfiguredModule(paramLynxModule);
  }
  
  public void arm() throws RobotCoreException, InterruptedException {
    assertOpen();
    this.delegate.arm();
  }
  
  public void armOrPretend() throws RobotCoreException, InterruptedException {
    assertOpen();
    this.delegate.armOrPretend();
  }
  
  protected void assertOpen() {
    if (!this.isOpen)
      Assert.assertTrue(false, "0x%08x on 0x%08x: closed", new Object[] { Integer.valueOf(hashCode()), Integer.valueOf(this.delegate.hashCode()) }); 
  }
  
  public void changeModuleAddress(LynxModule paramLynxModule, int paramInt, Runnable paramRunnable) {
    assertOpen();
    this.delegate.changeModuleAddress(paramLynxModule, paramInt, paramRunnable);
  }
  
  public void clearGlobalWarning() {
    assertOpen();
    this.delegate.clearGlobalWarning();
  }
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield releaseOnClose : Z
    //   6: ifeq -> 74
    //   9: getstatic com/qualcomm/hardware/lynx/LynxUsbDeviceDelegate.TAG : Ljava/lang/String;
    //   12: ldc '0x%08x on 0x%08x: releasing delegate to [%s]'
    //   14: iconst_3
    //   15: anewarray java/lang/Object
    //   18: dup
    //   19: iconst_0
    //   20: aload_0
    //   21: invokevirtual hashCode : ()I
    //   24: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   27: aastore
    //   28: dup
    //   29: iconst_1
    //   30: aload_0
    //   31: getfield delegate : Lcom/qualcomm/hardware/lynx/LynxUsbDeviceImpl;
    //   34: invokevirtual hashCode : ()I
    //   37: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   40: aastore
    //   41: dup
    //   42: iconst_2
    //   43: aload_0
    //   44: getfield delegate : Lcom/qualcomm/hardware/lynx/LynxUsbDeviceImpl;
    //   47: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   50: aastore
    //   51: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   54: aload_0
    //   55: iconst_0
    //   56: putfield releaseOnClose : Z
    //   59: aload_0
    //   60: getfield delegate : Lcom/qualcomm/hardware/lynx/LynxUsbDeviceImpl;
    //   63: invokevirtual releaseRef : ()V
    //   66: aload_0
    //   67: iconst_0
    //   68: putfield isOpen : Z
    //   71: goto -> 119
    //   74: getstatic com/qualcomm/hardware/lynx/LynxUsbDeviceDelegate.TAG : Ljava/lang/String;
    //   77: ldc '0x%08x on 0x%08x: closing closed[%s]; ignored'
    //   79: iconst_3
    //   80: anewarray java/lang/Object
    //   83: dup
    //   84: iconst_0
    //   85: aload_0
    //   86: invokevirtual hashCode : ()I
    //   89: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   92: aastore
    //   93: dup
    //   94: iconst_1
    //   95: aload_0
    //   96: getfield delegate : Lcom/qualcomm/hardware/lynx/LynxUsbDeviceImpl;
    //   99: invokevirtual hashCode : ()I
    //   102: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   105: aastore
    //   106: dup
    //   107: iconst_2
    //   108: aload_0
    //   109: getfield delegate : Lcom/qualcomm/hardware/lynx/LynxUsbDeviceImpl;
    //   112: invokevirtual getSerialNumber : ()Lcom/qualcomm/robotcore/util/SerialNumber;
    //   115: aastore
    //   116: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)V
    //   119: aload_0
    //   120: monitorexit
    //   121: return
    //   122: astore_1
    //   123: aload_0
    //   124: monitorexit
    //   125: aload_1
    //   126: athrow
    // Exception table:
    //   from	to	target	type
    //   2	71	122	finally
    //   74	119	122	finally
  }
  
  public void disarm() throws RobotCoreException, InterruptedException {
    assertOpen();
    this.delegate.disarm();
  }
  
  public LynxModuleMetaList discoverModules() throws RobotCoreException, InterruptedException {
    assertOpen();
    return this.delegate.discoverModules();
  }
  
  public void disengage() {
    assertOpen();
    this.delegate.disengage();
  }
  
  public void engage() {
    assertOpen();
    this.delegate.engage();
  }
  
  public void failSafe() {
    assertOpen();
    this.delegate.failSafe();
  }
  
  public RobotArmingStateNotifier.ARMINGSTATE getArmingState() {
    assertOpen();
    return this.delegate.getArmingState();
  }
  
  public LynxModule getConfiguredModule(int paramInt) {
    assertOpen();
    return this.delegate.getConfiguredModule(paramInt);
  }
  
  public String getConnectionInfo() {
    assertOpen();
    return this.delegate.getConnectionInfo();
  }
  
  public LynxUsbDeviceImpl getDelegationTarget() {
    return this.delegate;
  }
  
  public String getDeviceName() {
    assertOpen();
    return this.delegate.getDeviceName();
  }
  
  public String getGlobalWarning() {
    assertOpen();
    return this.delegate.getGlobalWarning();
  }
  
  public HardwareDevice.Manufacturer getManufacturer() {
    assertOpen();
    return this.delegate.getManufacturer();
  }
  
  public RobotUsbModule getOwner() {
    assertOpen();
    return this.delegate.getOwner();
  }
  
  public RobotUsbDevice getRobotUsbDevice() {
    assertOpen();
    return this.delegate.getRobotUsbDevice();
  }
  
  public SerialNumber getSerialNumber() {
    assertOpen();
    return this.delegate.getSerialNumber();
  }
  
  public SyncdDevice.ShutdownReason getShutdownReason() {
    assertOpen();
    return this.delegate.getShutdownReason();
  }
  
  public int getVersion() {
    assertOpen();
    return this.delegate.getVersion();
  }
  
  public boolean isEngaged() {
    assertOpen();
    return this.delegate.isEngaged();
  }
  
  public boolean isSystemSynthetic() {
    assertOpen();
    return this.delegate.isSystemSynthetic();
  }
  
  public void lockNetworkLockAcquisitions() {
    this.delegate.lockNetworkLockAcquisitions();
  }
  
  public void noteMissingModule(LynxModule paramLynxModule, String paramString) {
    assertOpen();
    this.delegate.noteMissingModule(paramLynxModule, paramString);
  }
  
  public void pretend() throws RobotCoreException, InterruptedException {
    assertOpen();
    this.delegate.pretend();
  }
  
  public void registerCallback(RobotArmingStateNotifier.Callback paramCallback, boolean paramBoolean) {
    assertOpen();
    this.delegate.registerCallback(paramCallback, paramBoolean);
  }
  
  public void releaseNetworkTransmissionLock(LynxMessage paramLynxMessage) throws InterruptedException {
    assertOpen();
    this.delegate.releaseNetworkTransmissionLock(paramLynxMessage);
  }
  
  public void removeConfiguredModule(LynxModule paramLynxModule) {
    assertOpen();
    this.delegate.removeConfiguredModule(paramLynxModule);
  }
  
  public void resetDeviceConfigurationForOpMode() {
    assertOpen();
    this.delegate.resetDeviceConfigurationForOpMode();
  }
  
  public boolean setControlHubModuleAddressIfNecessary() throws RobotCoreException, InterruptedException {
    assertOpen();
    return this.delegate.setControlHubModuleAddressIfNecessary();
  }
  
  public void setGlobalWarning(String paramString) {
    assertOpen();
    this.delegate.setGlobalWarning(paramString);
  }
  
  public void setOwner(RobotUsbModule paramRobotUsbModule) {
    assertOpen();
    this.delegate.setOwner(paramRobotUsbModule);
  }
  
  public void setSystemSynthetic(boolean paramBoolean) {
    assertOpen();
    this.delegate.setSystemSynthetic(paramBoolean);
  }
  
  public void setThrowOnNetworkLockAcquisition(boolean paramBoolean) {
    this.delegate.setThrowOnNetworkLockAcquisition(paramBoolean);
  }
  
  public void suppressGlobalWarning(boolean paramBoolean) {
    assertOpen();
    this.delegate.suppressGlobalWarning(paramBoolean);
  }
  
  public void transmit(LynxMessage paramLynxMessage) throws InterruptedException {
    assertOpen();
    this.delegate.transmit(paramLynxMessage);
  }
  
  public void unregisterCallback(RobotArmingStateNotifier.Callback paramCallback) {
    assertOpen();
    this.delegate.unregisterCallback(paramCallback);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\lynx\LynxUsbDeviceDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */