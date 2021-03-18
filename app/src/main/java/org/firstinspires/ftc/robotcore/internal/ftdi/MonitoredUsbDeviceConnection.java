package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.system.DebuggableReentrantLock;
import org.firstinspires.ftc.robotcore.internal.system.WatchdogMonitor;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbFTDIException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbStuckUsbWriteException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbUnspecifiedException;
import org.firstinspires.ftc.robotcore.internal.usb.exception.RobotUsbWriteLockException;

public class MonitoredUsbDeviceConnection {
  public static final String TAG = "MonitoredUsbDeviceConnection";
  
  protected static final DebuggableReentrantLock usbWriteLock = new DebuggableReentrantLock();
  
  protected byte[] buffer;
  
  protected Callable<RobotUsbException> bulkTransferAction;
  
  protected int callResult;
  
  protected Callable<RobotUsbException> controlTransferAction;
  
  protected final UsbDeviceConnection delegate;
  
  protected UsbEndpoint endpoint;
  
  protected Callable<RobotUsbException> failureAction;
  
  protected FailureType failureType = FailureType.UNKNOWN;
  
  protected final FtDevice ftDevice;
  
  protected int index;
  
  protected int length;
  
  protected final WatchdogMonitor monitor = new WatchdogMonitor();
  
  protected int msUsbWriteDurationMax = 200;
  
  protected int msUsbWriteLockAcquire = 250;
  
  protected int offset;
  
  protected int request;
  
  protected int requestType;
  
  protected final String serialNumber;
  
  protected int timeout;
  
  protected int value;
  
  public MonitoredUsbDeviceConnection(FtDevice paramFtDevice, UsbDeviceConnection paramUsbDeviceConnection) {
    this.ftDevice = paramFtDevice;
    this.delegate = paramUsbDeviceConnection;
    this.serialNumber = paramUsbDeviceConnection.getSerial();
    initializeMonitoring();
  }
  
  private boolean acquireUsbWriteLock() throws InterruptedException {
    return (usbWriteLock.tryLock() || usbWriteLock.tryLock(this.msUsbWriteLockAcquire, TimeUnit.MILLISECONDS));
  }
  
  private void releaseUsbWriteLock() {
    usbWriteLock.unlock();
  }
  
  private RobotUsbException unableToAcquireWriteLockException() {
    String str;
    Thread thread = usbWriteLock.getOwner();
    if (thread == null) {
      str = "";
    } else {
      str = String.format(": owner: id=%d TID=%d name=%s", new Object[] { Long.valueOf(str.getId()), Integer.valueOf(ThreadPool.getTID((Thread)str)), str.getName() });
    } 
    return (RobotUsbException)new RobotUsbWriteLockException("unable to acquire usb write lock after %d ms%s", new Object[] { Integer.valueOf(this.msUsbWriteLockAcquire), str });
  }
  
  public int bulkTransfer(UsbEndpoint paramUsbEndpoint, byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3) throws InterruptedException, RobotUsbException {
    if (paramUsbEndpoint.getDirection() == 128)
      return this.delegate.bulkTransfer(paramUsbEndpoint, paramArrayOfbyte, paramInt1, paramInt2, paramInt3); 
    try {
      synchronized (this.monitor) {
        this.callResult = -1000;
        boolean bool = acquireUsbWriteLock();
        if (bool) {
          try {
            this.endpoint = paramUsbEndpoint;
            this.buffer = paramArrayOfbyte;
            this.offset = paramInt1;
            this.length = paramInt2;
            this.timeout = paramInt3;
            this.failureType = FailureType.WRITE;
            RobotUsbException robotUsbException = (RobotUsbException)this.monitor.monitor(this.bulkTransferAction, this.failureAction, this.msUsbWriteDurationMax, TimeUnit.MILLISECONDS);
            if (robotUsbException == null) {
              releaseUsbWriteLock();
              paramInt1 = this.callResult;
              return paramInt1;
            } 
            throw robotUsbException;
          } catch (ExecutionException executionException) {
          
          } catch (CancellationException cancellationException) {
          
          } finally {}
          throw RobotUsbUnspecifiedException.createChained(paramUsbEndpoint, "write: internal error: unexpected exception from future", new Object[0]);
        } 
        throw unableToAcquireWriteLockException();
      } 
    } catch (RuntimeException runtimeException) {
      throw RobotUsbFTDIException.createChained(runtimeException, "runtime exception %s during write() of %d bytes on %s", new Object[] { runtimeException.getClass().getSimpleName(), Integer.valueOf(paramInt2), this.serialNumber });
    } 
  }
  
  public boolean claimInterface(UsbInterface paramUsbInterface, boolean paramBoolean) {
    return this.delegate.claimInterface(paramUsbInterface, paramBoolean);
  }
  
  public void close() {
    RobotLog.vv("MonitoredUsbDeviceConnection", "closing UsbDeviceConnection(%s)", new Object[] { this.serialNumber });
    this.delegate.close();
    this.monitor.close(false);
  }
  
  public int controlTransfer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte, int paramInt5, int paramInt6) throws RobotUsbException {
    return controlTransfer(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfbyte, 0, paramInt5, paramInt6);
  }
  
  public int controlTransfer(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfbyte, int paramInt5, int paramInt6, int paramInt7) throws RobotUsbException {
    synchronized (this.monitor) {
      this.callResult = -1000;
      try {
        boolean bool = acquireUsbWriteLock();
        if (bool) {
          try {
            this.requestType = paramInt1;
            this.request = paramInt2;
            this.value = paramInt3;
            this.index = paramInt4;
            this.buffer = paramArrayOfbyte;
            this.offset = paramInt5;
            try {
              this.length = paramInt6;
              this.timeout = paramInt7;
              this.failureType = FailureType.CONTROL_TRANSFER;
              RobotUsbException robotUsbException = (RobotUsbException)this.monitor.monitor(this.controlTransferAction, this.failureAction, this.msUsbWriteDurationMax, TimeUnit.MILLISECONDS);
              if (robotUsbException == null) {
                try {
                  releaseUsbWriteLock();
                } catch (RuntimeException null) {
                  throw RobotUsbFTDIException.createChained(runtimeException, "runtime exception %s during controlTransfer() of %d bytes on %s", new Object[] { runtimeException.getClass().getSimpleName(), Integer.valueOf(paramInt6), this.serialNumber });
                } 
              } else {
                throw runtimeException;
              } 
            } catch (ExecutionException executionException) {
              throw RobotUsbUnspecifiedException.createChained(executionException, "control transfer: internal error: unexpected exception from future", new Object[0]);
            } catch (CancellationException null) {
              throw RobotUsbUnspecifiedException.createChained(runtimeException, "control transfer: internal error: unexpected exception from future", new Object[0]);
            } finally {}
          } catch (ExecutionException executionException) {
            throw RobotUsbUnspecifiedException.createChained(executionException, "control transfer: internal error: unexpected exception from future", new Object[0]);
          } catch (CancellationException null) {
          
          } finally {}
          releaseUsbWriteLock();
          throw paramArrayOfbyte;
        } 
        throw unableToAcquireWriteLockException();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } catch (RuntimeException runtimeException) {}
      throw RobotUsbFTDIException.createChained(runtimeException, "runtime exception %s during controlTransfer() of %d bytes on %s", new Object[] { runtimeException.getClass().getSimpleName(), Integer.valueOf(paramInt6), this.serialNumber });
    } 
  }
  
  public byte[] getRawDescriptors() {
    return this.delegate.getRawDescriptors();
  }
  
  protected void initializeMonitoring() {
    this.bulkTransferAction = new Callable<RobotUsbException>() {
        public RobotUsbException call() {
          MonitoredUsbDeviceConnection monitoredUsbDeviceConnection = MonitoredUsbDeviceConnection.this;
          monitoredUsbDeviceConnection.callResult = monitoredUsbDeviceConnection.delegate.bulkTransfer(MonitoredUsbDeviceConnection.this.endpoint, MonitoredUsbDeviceConnection.this.buffer, MonitoredUsbDeviceConnection.this.offset, MonitoredUsbDeviceConnection.this.length, MonitoredUsbDeviceConnection.this.timeout);
          return null;
        }
      };
    this.controlTransferAction = new Callable<RobotUsbException>() {
        public RobotUsbException call() {
          MonitoredUsbDeviceConnection monitoredUsbDeviceConnection = MonitoredUsbDeviceConnection.this;
          monitoredUsbDeviceConnection.callResult = monitoredUsbDeviceConnection.delegate.controlTransfer(MonitoredUsbDeviceConnection.this.requestType, MonitoredUsbDeviceConnection.this.request, MonitoredUsbDeviceConnection.this.value, MonitoredUsbDeviceConnection.this.index, MonitoredUsbDeviceConnection.this.buffer, MonitoredUsbDeviceConnection.this.offset, MonitoredUsbDeviceConnection.this.length, MonitoredUsbDeviceConnection.this.timeout);
          return null;
        }
      };
    this.failureAction = new Callable<RobotUsbException>() {
        public RobotUsbException call() {
          String str1;
          String str2;
          Thread thread = MonitoredUsbDeviceConnection.this.monitor.getMonitoredThread();
          if (thread == null) {
            str2 = "";
          } else {
            str2 = String.format(" threadId=%d TID=%d:", new Object[] { Long.valueOf(thread.getId()), Integer.valueOf(ThreadPool.getTID(thread)) });
          } 
          int i = MonitoredUsbDeviceConnection.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$ftdi$MonitoredUsbDeviceConnection$FailureType[MonitoredUsbDeviceConnection.this.failureType.ordinal()];
          if (i != 1) {
            if (i != 2) {
              str1 = "unknown failure";
            } else {
              str1 = String.format("control(%d bytes)", new Object[] { Integer.valueOf(this.this$0.length) });
            } 
          } else {
            str1 = String.format("write(%d bytes)", new Object[] { Integer.valueOf(this.this$0.length) });
          } 
          RobotLog.ee("MonitoredUsbDeviceConnection", "watchdog: stuck USB %s%s: serial=%s closing device", new Object[] { str1, str2, this.this$0.serialNumber, str2 });
          RobotUsbStuckUsbWriteException robotUsbStuckUsbWriteException = new RobotUsbStuckUsbWriteException(MonitoredUsbDeviceConnection.this.delegate, "watchdog: stuck USB %s: closed %s", new Object[] { str1, this.this$0.serialNumber });
          MonitoredUsbDeviceConnection.this.ftDevice.setDeviceClosedReason((RobotUsbException)robotUsbStuckUsbWriteException);
          MonitoredUsbDeviceConnection.this.ftDevice.close();
          return (RobotUsbException)robotUsbStuckUsbWriteException;
        }
      };
  }
  
  public boolean releaseInterface(UsbInterface paramUsbInterface) {
    return this.delegate.releaseInterface(paramUsbInterface);
  }
  
  protected enum FailureType {
    CONTROL_TRANSFER, UNKNOWN, WRITE;
    
    static {
      FailureType failureType = new FailureType("CONTROL_TRANSFER", 2);
      CONTROL_TRANSFER = failureType;
      $VALUES = new FailureType[] { UNKNOWN, WRITE, failureType };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\MonitoredUsbDeviceConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */