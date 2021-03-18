package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbWriteLockException extends RobotUsbException {
  public RobotUsbWriteLockException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbWriteLockException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbWriteLockException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbWriteLockException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbWriteLockException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbWriteLockException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */