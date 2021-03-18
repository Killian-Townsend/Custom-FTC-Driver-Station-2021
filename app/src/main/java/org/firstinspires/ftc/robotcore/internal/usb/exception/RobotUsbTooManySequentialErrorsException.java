package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbTooManySequentialErrorsException extends RobotUsbException {
  public RobotUsbTooManySequentialErrorsException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbTooManySequentialErrorsException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbTooManySequentialErrorsException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbTooManySequentialErrorsException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbTooManySequentialErrorsException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbTooManySequentialErrorsException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */