package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbProtocolException extends RobotUsbException {
  public RobotUsbProtocolException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbProtocolException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbProtocolException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbProtocolException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbProtocolException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */