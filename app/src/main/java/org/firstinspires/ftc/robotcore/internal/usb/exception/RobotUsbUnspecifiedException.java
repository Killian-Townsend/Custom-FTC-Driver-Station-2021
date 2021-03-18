package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbUnspecifiedException extends RobotUsbException {
  public RobotUsbUnspecifiedException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbUnspecifiedException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbUnspecifiedException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbUnspecifiedException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbUnspecifiedException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbUnspecifiedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */