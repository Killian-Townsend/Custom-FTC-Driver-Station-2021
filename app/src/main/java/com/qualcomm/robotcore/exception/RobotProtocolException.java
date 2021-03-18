package com.qualcomm.robotcore.exception;

public class RobotProtocolException extends Exception {
  public RobotProtocolException(String paramString) {
    super(paramString);
  }
  
  public RobotProtocolException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\exception\RobotProtocolException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */