package org.slf4j.impl;

import android.util.Log;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

class AndroidLoggerAdapter extends MarkerIgnoringBase {
  private static final long serialVersionUID = -1227274521521287937L;
  
  AndroidLoggerAdapter(String paramString) {
    this.name = paramString;
  }
  
  private void formatAndLog(int paramInt, String paramString, Object... paramVarArgs) {
    if (isLoggable(paramInt)) {
      FormattingTuple formattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
      logInternal(paramInt, formattingTuple.getMessage(), formattingTuple.getThrowable());
    } 
  }
  
  private void log(int paramInt, String paramString, Throwable paramThrowable) {
    if (isLoggable(paramInt))
      logInternal(paramInt, paramString, paramThrowable); 
  }
  
  public void debug(String paramString) {
    log(3, paramString, null);
  }
  
  public void debug(String paramString, Object paramObject) {
    formatAndLog(3, paramString, new Object[] { paramObject });
  }
  
  public void debug(String paramString, Object paramObject1, Object paramObject2) {
    formatAndLog(3, paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public void debug(String paramString, Throwable paramThrowable) {
    log(2, paramString, paramThrowable);
  }
  
  public void debug(String paramString, Object... paramVarArgs) {
    formatAndLog(3, paramString, paramVarArgs);
  }
  
  public void error(String paramString) {
    log(6, paramString, null);
  }
  
  public void error(String paramString, Object paramObject) {
    formatAndLog(6, paramString, new Object[] { paramObject });
  }
  
  public void error(String paramString, Object paramObject1, Object paramObject2) {
    formatAndLog(6, paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public void error(String paramString, Throwable paramThrowable) {
    log(6, paramString, paramThrowable);
  }
  
  public void error(String paramString, Object... paramVarArgs) {
    formatAndLog(6, paramString, paramVarArgs);
  }
  
  public void info(String paramString) {
    log(4, paramString, null);
  }
  
  public void info(String paramString, Object paramObject) {
    formatAndLog(4, paramString, new Object[] { paramObject });
  }
  
  public void info(String paramString, Object paramObject1, Object paramObject2) {
    formatAndLog(4, paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public void info(String paramString, Throwable paramThrowable) {
    log(4, paramString, paramThrowable);
  }
  
  public void info(String paramString, Object... paramVarArgs) {
    formatAndLog(4, paramString, paramVarArgs);
  }
  
  public boolean isDebugEnabled() {
    return isLoggable(3);
  }
  
  public boolean isErrorEnabled() {
    return isLoggable(6);
  }
  
  public boolean isInfoEnabled() {
    return isLoggable(4);
  }
  
  protected boolean isLoggable(int paramInt) {
    return Log.isLoggable(this.name, paramInt);
  }
  
  public boolean isTraceEnabled() {
    return isLoggable(2);
  }
  
  public boolean isWarnEnabled() {
    return isLoggable(5);
  }
  
  protected void logInternal(int paramInt, String paramString, Throwable paramThrowable) {
    String str = paramString;
    if (paramThrowable != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append('\n');
      stringBuilder.append(Log.getStackTraceString(paramThrowable));
      str = stringBuilder.toString();
    } 
    Log.println(paramInt, this.name, str);
  }
  
  public void trace(String paramString) {
    log(2, paramString, null);
  }
  
  public void trace(String paramString, Object paramObject) {
    formatAndLog(2, paramString, new Object[] { paramObject });
  }
  
  public void trace(String paramString, Object paramObject1, Object paramObject2) {
    formatAndLog(2, paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public void trace(String paramString, Throwable paramThrowable) {
    log(2, paramString, paramThrowable);
  }
  
  public void trace(String paramString, Object... paramVarArgs) {
    formatAndLog(2, paramString, paramVarArgs);
  }
  
  public void warn(String paramString) {
    log(5, paramString, null);
  }
  
  public void warn(String paramString, Object paramObject) {
    formatAndLog(5, paramString, new Object[] { paramObject });
  }
  
  public void warn(String paramString, Object paramObject1, Object paramObject2) {
    formatAndLog(5, paramString, new Object[] { paramObject1, paramObject2 });
  }
  
  public void warn(String paramString, Throwable paramThrowable) {
    log(5, paramString, paramThrowable);
  }
  
  public void warn(String paramString, Object... paramVarArgs) {
    formatAndLog(5, paramString, paramVarArgs);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\AndroidLoggerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */