package org.slf4j.helpers;

public class FormattingTuple {
  public static FormattingTuple NULL = new FormattingTuple(null);
  
  private Object[] argArray;
  
  private String message;
  
  private Throwable throwable;
  
  public FormattingTuple(String paramString) {
    this(paramString, null, null);
  }
  
  public FormattingTuple(String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
    this.message = paramString;
    this.throwable = paramThrowable;
    this.argArray = paramArrayOfObject;
  }
  
  public Object[] getArgArray() {
    return this.argArray;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\FormattingTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */