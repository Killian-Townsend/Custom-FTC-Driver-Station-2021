package com.qualcomm.robotcore.hardware.usb.serial;

import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
  protected static final String TAG = "SerialPort";
  
  protected int baudRate;
  
  protected File file;
  
  protected FileDescriptor fileDescriptor;
  
  protected FileInputStream fileInputStream;
  
  protected FileOutputStream fileOutputStream;
  
  static {
    System.loadLibrary("RobotCore");
  }
  
  public SerialPort(File paramFile, int paramInt) throws IOException {
    this.file = paramFile;
    ensureReadWriteable(paramFile);
    this.baudRate = paramInt;
    FileDescriptor fileDescriptor = open(paramFile.getAbsolutePath(), paramInt, isDragonboard());
    this.fileDescriptor = fileDescriptor;
    if (fileDescriptor != null) {
      this.fileInputStream = new FileInputStream(this.fileDescriptor);
      this.fileOutputStream = new FileOutputStream(this.fileDescriptor);
      return;
    } 
    throw new IOException(String.format("SerialPort.SerialPort: failed: path=%s", new Object[] { paramFile.getAbsolutePath() }));
  }
  
  public static native void close(FileDescriptor paramFileDescriptor);
  
  protected static void ensureReadWriteable(File paramFile) throws SecurityException {
    if (paramFile.canRead() && paramFile.canWrite())
      return; 
    RobotLog.vv("SerialPort", "making RW: %s", new Object[] { paramFile.getAbsolutePath() });
    try {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("incorrect perms on ");
      stringBuilder.append(paramFile.getAbsolutePath());
      throw new RuntimeException(stringBuilder.toString());
    } catch (Exception exception) {
      RobotLog.logStacktrace(exception);
      throw new SecurityException(String.format("SerialPort.ensureReadWriteFile: exception: path=%s", new Object[] { paramFile.getAbsolutePath() }), exception);
    } 
  }
  
  private boolean isDragonboard() {
    return (LynxConstants.getControlHubVersion() == 0);
  }
  
  private static native FileDescriptor open(String paramString, int paramInt, boolean paramBoolean);
  
  public void close() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield fileDescriptor : Ljava/io/FileDescriptor;
    //   6: ifnull -> 21
    //   9: aload_0
    //   10: getfield fileDescriptor : Ljava/io/FileDescriptor;
    //   13: invokestatic close : (Ljava/io/FileDescriptor;)V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield fileDescriptor : Ljava/io/FileDescriptor;
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
  
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }
  
  public int getBaudRate() {
    return this.baudRate;
  }
  
  public InputStream getInputStream() {
    return this.fileInputStream;
  }
  
  public String getName() {
    return this.file.getAbsolutePath();
  }
  
  public OutputStream getOutputStream() {
    return this.fileOutputStream;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\hardwar\\usb\serial\SerialPort.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */