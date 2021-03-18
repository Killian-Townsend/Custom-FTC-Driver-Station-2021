package com.qualcomm.robotcore.exception;

public class RobotCoreException extends Exception {
  public RobotCoreException(String paramString) {
    super(paramString);
  }
  
  public RobotCoreException(String paramString, Throwable paramThrowable) {
    super(paramString, paramThrowable);
  }
  
  public RobotCoreException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
  
  public static RobotCoreException createChained(Exception paramException, String paramString, Object... paramVarArgs) {
    return new RobotCoreException(String.format(paramString, paramVarArgs), paramException);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\exception\RobotCoreException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */