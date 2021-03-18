package org.firstinspires.ftc.robotcore.internal.usb.exception;

import android.hardware.usb.UsbDeviceConnection;

public class RobotUsbStuckUsbWriteException extends RobotUsbException {
  public UsbDeviceConnection device = null;
  
  public RobotUsbStuckUsbWriteException(UsbDeviceConnection paramUsbDeviceConnection, String paramString, Object... paramVarArgs) {
    this(paramString, paramVarArgs);
    this.device = paramUsbDeviceConnection;
  }
  
  public RobotUsbStuckUsbWriteException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbStuckUsbWriteException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbStuckUsbWriteException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbStuckUsbWriteException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbStuckUsbWriteException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbStuckUsbWriteException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */