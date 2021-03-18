package org.firstinspires.ftc.robotcore.internal.usb.exception;

public class RobotUsbTimeoutException extends RobotUsbException {
  public long nsTimerExpire;
  
  public long nsTimerStart;
  
  public RobotUsbTimeoutException(long paramLong, String paramString) {
    super(paramString);
    initialize(paramLong);
  }
  
  protected RobotUsbTimeoutException(long paramLong, String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
    initialize(paramLong);
  }
  
  public RobotUsbTimeoutException(long paramLong, String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
    initialize(paramLong);
  }
  
  public static RobotUsbTimeoutException createChained(long paramLong, Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotUsbTimeoutException(paramLong, String.format(paramString, paramVarArgs), paramException);
  }
  
  protected void initialize(long paramLong) {
    this.nsTimerStart = paramLong;
    this.nsTimerExpire = System.nanoTime();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\interna\\usb\exception\RobotUsbTimeoutException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */