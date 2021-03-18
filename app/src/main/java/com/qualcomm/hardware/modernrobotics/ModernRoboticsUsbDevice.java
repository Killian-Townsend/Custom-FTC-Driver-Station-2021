package com.qualcomm.hardware.modernrobotics;

import android.content.Context;
import com.qualcomm.hardware.modernrobotics.comm.ReadWriteRunnable;
import com.qualcomm.hardware.modernrobotics.comm.RobotUsbDevicePretendModernRobotics;
import com.qualcomm.robotcore.eventloop.SyncdDevice;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.usb.RobotArmingStateNotifier;
import com.qualcomm.robotcore.hardware.usb.RobotUsbDevice;
import com.qualcomm.robotcore.hardware.usb.RobotUsbModule;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.SerialNumber;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.hardware.usb.ArmableUsbDevice;

public abstract class ModernRoboticsUsbDevice extends ArmableUsbDevice implements ReadWriteRunnable.Callback {
  protected final CreateReadWriteRunnable createReadWriteRunnable;
  
  protected volatile ReadWriteRunnable readWriteRunnable;
  
  protected ExecutorService readWriteService;
  
  public ModernRoboticsUsbDevice(Context paramContext, SerialNumber paramSerialNumber, SyncdDevice.Manager paramManager, ArmableUsbDevice.OpenRobotUsbDevice paramOpenRobotUsbDevice, CreateReadWriteRunnable paramCreateReadWriteRunnable) throws RobotCoreException, InterruptedException {
    super(paramContext, paramSerialNumber, paramManager, paramOpenRobotUsbDevice);
    this.createReadWriteRunnable = paramCreateReadWriteRunnable;
    this.readWriteService = null;
    finishConstruction();
  }
  
  private static void logAndThrow(String paramString) throws RobotCoreException {
    System.err.println(paramString);
    throw new RobotCoreException(paramString);
  }
  
  protected void armDevice(RobotUsbDevice paramRobotUsbDevice) throws RobotCoreException, InterruptedException {
    synchronized (this.armingLock) {
      this.robotUsbDevice = paramRobotUsbDevice;
      this.readWriteRunnable = this.createReadWriteRunnable.create(paramRobotUsbDevice);
      if (this.readWriteRunnable != null) {
        String str;
        if (this.armingState == RobotArmingStateNotifier.ARMINGSTATE.TO_PRETENDING) {
          str = "pretend ";
        } else {
          str = "";
        } 
        RobotLog.v("Starting up %sdevice %s", new Object[] { str, this.serialNumber });
        this.readWriteRunnable.setOwner((RobotUsbModule)this);
        this.readWriteRunnable.setCallback(this);
        this.readWriteService = ThreadPool.newSingleThreadExecutor("readWriteService");
        this.readWriteRunnable.executeUsing(this.readWriteService);
        this.syncdDeviceManager.registerSyncdDevice((SyncdDevice)this.readWriteRunnable);
        this.readWriteRunnable.setAcceptingWrites(true);
      } 
      return;
    } 
  }
  
  protected void disarmDevice() throws InterruptedException {
    synchronized (this.armingLock) {
      if (this.readWriteService != null)
        this.readWriteService.shutdown(); 
      if (this.readWriteRunnable != null) {
        this.readWriteRunnable.setAcceptingWrites(false);
        this.readWriteRunnable.drainPendingWrites();
        this.syncdDeviceManager.unregisterSyncdDevice((SyncdDevice)this.readWriteRunnable);
        this.readWriteRunnable.close();
        this.readWriteRunnable = null;
      } 
      if (this.readWriteService != null) {
        ThreadPool.awaitTerminationOrExitApplication(this.readWriteService, 30L, TimeUnit.SECONDS, "ReadWriteRunnable for Modern Robotics USB Device", "internal error");
        this.readWriteService = null;
      } 
      if (this.robotUsbDevice != null) {
        this.robotUsbDevice.close();
        this.robotUsbDevice = null;
      } 
      return;
    } 
  }
  
  public CreateReadWriteRunnable getCreateReadWriteRunnable() {
    return this.createReadWriteRunnable;
  }
  
  public abstract String getDeviceName();
  
  public ArmableUsbDevice.OpenRobotUsbDevice getOpenRobotUsbDevice() {
    return this.openRobotUsbDevice;
  }
  
  protected RobotUsbDevice getPretendDevice(SerialNumber paramSerialNumber) {
    return (RobotUsbDevice)new RobotUsbDevicePretendModernRobotics(paramSerialNumber);
  }
  
  public ReadWriteRunnable getReadWriteRunnable() {
    return this.readWriteRunnable;
  }
  
  public int getVersion() {
    return read8(0);
  }
  
  public void initializeHardware() {}
  
  public byte[] read(int paramInt1, int paramInt2) {
    ReadWriteRunnable readWriteRunnable = this.readWriteRunnable;
    return (readWriteRunnable != null) ? readWriteRunnable.read(paramInt1, paramInt2) : new byte[paramInt2];
  }
  
  public byte read8(int paramInt) {
    return read(paramInt, 1)[0];
  }
  
  public void readComplete() throws InterruptedException {}
  
  public byte readFromWriteCache(int paramInt) {
    return readFromWriteCache(paramInt, 1)[0];
  }
  
  public byte[] readFromWriteCache(int paramInt1, int paramInt2) {
    ReadWriteRunnable readWriteRunnable = this.readWriteRunnable;
    return (readWriteRunnable != null) ? readWriteRunnable.readFromWriteCache(paramInt1, paramInt2) : new byte[paramInt2];
  }
  
  public void shutdownComplete() throws InterruptedException {}
  
  public void startupComplete() throws InterruptedException {}
  
  public void write(int paramInt, byte[] paramArrayOfbyte) {
    ReadWriteRunnable readWriteRunnable = this.readWriteRunnable;
    if (readWriteRunnable != null)
      readWriteRunnable.write(paramInt, paramArrayOfbyte); 
  }
  
  public void write8(int paramInt, byte paramByte) {
    write(paramInt, new byte[] { paramByte });
  }
  
  public void write8(int paramInt, double paramDouble) {
    write(paramInt, new byte[] { (byte)(int)paramDouble });
  }
  
  public void write8(int paramInt1, int paramInt2) {
    write(paramInt1, new byte[] { (byte)paramInt2 });
  }
  
  public void writeComplete() throws InterruptedException {}
  
  public static interface CreateReadWriteRunnable {
    ReadWriteRunnable create(RobotUsbDevice param1RobotUsbDevice) throws RobotCoreException, InterruptedException;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\hardware\modernrobotics\ModernRoboticsUsbDevice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */