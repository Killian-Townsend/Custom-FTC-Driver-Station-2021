package org.firstinspires.ftc.robotcore.internal.ftdi;

import com.qualcomm.robotcore.util.RobotLog;

public class ReadBufferWorker implements Runnable {
  public static final String TAG = "ReadBufferWorker";
  
  private final ReadBufferManager readBufferManager;
  
  ReadBufferWorker(ReadBufferManager paramReadBufferManager) {
    this.readBufferManager = paramReadBufferManager;
  }
  
  public void run() {
    try {
      while (true) {
        BulkPacketBufferIn bulkPacketBufferIn = this.readBufferManager.acquireReadableInputBuffer();
        this.readBufferManager.processBulkInData(bulkPacketBufferIn);
        this.readBufferManager.releaseWritableInputBuffer(bulkPacketBufferIn);
        if (!Thread.interrupted())
          continue; 
        throw new InterruptedException();
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } catch (FtDeviceIOException ftDeviceIOException) {
      RobotLog.ee("ReadBufferWorker", ftDeviceIOException, "exception in read buffer worker: fatal");
      return;
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("ReadBufferWorker", runtimeException, "exception in read buffer worker: fatal");
      return;
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\ftdi\ReadBufferWorker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */