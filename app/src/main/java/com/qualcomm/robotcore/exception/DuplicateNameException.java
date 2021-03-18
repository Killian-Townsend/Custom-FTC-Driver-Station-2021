package com.qualcomm.robotcore.exception;

public class DuplicateNameException extends RuntimeException {
  public DuplicateNameException(String paramString) {
    super(paramString);
  }
  
  public DuplicateNameException(String paramString, Object... paramVarArgs) {
    super(String.format(paramString, paramVarArgs));
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\exception\DuplicateNameException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */