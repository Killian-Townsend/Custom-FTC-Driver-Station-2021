package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbFTDIException extends RobotUsbException {
  public RobotUsbFTDIException(String paramString) {
    super(paramString);
  }
  
  protected RobotUsbFTDIException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotUsbFTDIException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotUsbFTDIException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbFTDIException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbFTDIException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */