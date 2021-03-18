package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbDeviceClosedException extends RobotUsbException {
  public RobotUsbDeviceClosedException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbDeviceClosedException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbDeviceClosedException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbDeviceClosedException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbDeviceClosedException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbDeviceClosedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */