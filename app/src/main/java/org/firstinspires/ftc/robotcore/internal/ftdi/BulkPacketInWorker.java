package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.hardware.usb.UsbEndpoint;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.system.FrequentErrorReporter;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;

public class BulkPacketInWorker extends FtConstants implements Runnable {
  public static final String TAG = "BulkPacketInWorker";
  
  final UsbEndpoint endpoint;
  
  final FrequentErrorReporter<Integer> errorReporter;
  
  final FtDevice ftDevice;
  
  final int msReadTimeout;
  
  final ReadBufferManager readBufferManager;
  
  final Object trivialInput;
  
  final MonitoredUsbDeviceConnection usbDeviceConnection;
  
  BulkPacketInWorker(FtDevice paramFtDevice, ReadBufferManager paramReadBufferManager, MonitoredUsbDeviceConnection paramMonitoredUsbDeviceConnection, UsbEndpoint paramUsbEndpoint) {
    this.ftDevice = paramFtDevice;
    this.endpoint = paramUsbEndpoint;
    this.usbDeviceConnection = paramMonitoredUsbDeviceConnection;
    this.readBufferManager = paramReadBufferManager;
    this.msReadTimeout = paramFtDevice.getDriverParameters().getBulkInReadTimeout();
    this.errorReporter = new FrequentErrorReporter();
    this.trivialInput = new Object();
  }
  
  public void awaitTrivialInput(long paramLong, TimeUnit paramTimeUnit) {
    paramLong = paramTimeUnit.toMillis(paramLong);
    synchronized (this.trivialInput) {
      this.trivialInput.wait(paramLong);
    } 
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/util/concurrent/TimeUnit}, name=paramTimeUnit} */
  }
  
  protected void noteTrivialInput() {
    synchronized (this.trivialInput) {
      this.trivialInput.notifyAll();
      return;
    } 
  }
  
  public void run() {
    try {
      while (true) {
        BulkPacketBufferIn bulkPacketBufferIn = this.readBufferManager.acquireWritableInputBuffer();
        int i = this.usbDeviceConnection.bulkTransfer(this.endpoint, bulkPacketBufferIn.array(), 0, bulkPacketBufferIn.capacity(), this.msReadTimeout);
        if (i > 0) {
          bulkPacketBufferIn.setCurrentLength(i);
          this.readBufferManager.releaseReadableBuffer(bulkPacketBufferIn);
          if (i <= 2)
            noteTrivialInput(); 
        } else {
          bulkPacketBufferIn.setCurrentLength(0);
          this.readBufferManager.releaseWritableInputBuffer(bulkPacketBufferIn);
          if (i < 0) {
            this.errorReporter.ee(Integer.valueOf(i), "BulkPacketInWorker", "%s: bulkTransfer() error: %d", new Object[] { this.ftDevice.getSerialNumber(), Integer.valueOf(i) });
          } else {
            this.errorReporter.reset();
          } 
        } 
        if (!Thread.interrupted())
          continue; 
        throw new InterruptedException();
      } 
    } catch (InterruptedException interruptedException) {
      this.readBufferManager.purgeInputData();
      Thread.currentThread().interrupt();
      return;
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("BulkPacketInWorker", runtimeException, "unexpected exception");
      return;
    } catch (RobotUsbException robotUsbException) {
      RobotLog.ee("BulkPacketInWorker", (Throwable)robotUsbException, "unexpected exception");
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\BulkPacketInWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */