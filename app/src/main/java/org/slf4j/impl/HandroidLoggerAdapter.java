package org.slf4j.impl;

import android.util.Log;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

public class HandroidLoggerAdapter extends AndroidLoggerAdapter {
  @Deprecated
  public static int ANDROID_API_LEVEL = 1;
  
  public static String APP_NAME;
  
  public static boolean DEBUG = false;
  
  private static Method crashlyticsLog;
  
  private static Method crashlyticsLogException;
  
  HandroidLoggerAdapter(String paramString) {
    super(paramString);
  }
  
  public static void enableLoggingToCrashlytics() {
    try {
      Class<?> clazz = Class.forName("com.crashlytics.android.Crashlytics");
      crashlyticsLog = clazz.getDeclaredMethod("log", new Class[] { int.class, String.class, String.class });
      crashlyticsLogException = clazz.getDeclaredMethod("logException", new Class[] { Throwable.class });
      System.out.println("slf4j-handroid: enabling integration with Crashlytics");
      return;
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    } 
  }
  
  public static String getStackTraceString(Throwable paramThrowable) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter printWriter = new PrintWriter(stringWriter);
    paramThrowable.printStackTrace(printWriter);
    printWriter.flush();
    return stringWriter.toString();
  }
  
  private static String postprocessMessage(String paramString) {
    StringBuilder stringBuilder = new StringBuilder(paramString.length());
    int j = 0;
    int i = j;
    while (j < paramString.length()) {
      char c = paramString.charAt(j);
      if (c != '\n' || i == 0)
        if (c >= ' ' || c == '\n') {
          stringBuilder.append(c);
        } else {
          stringBuilder.append(' ');
        }  
      if (c == '\n') {
        i = 1;
      } else {
        i = 0;
      } 
      j++;
    } 
    return stringBuilder.toString();
  }
  
  protected boolean isLoggable(int paramInt) {
    return DEBUG ? true : super.isLoggable(paramInt);
  }
  
  protected void logInternal(int paramInt, String paramString, Throwable paramThrowable) {
    String str = paramString;
    if (paramThrowable != null) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(paramString);
      stringBuilder.append('\n');
      stringBuilder.append(getStackTraceString(paramThrowable));
      str = stringBuilder.toString();
    } 
    paramString = postprocessMessage(str).trim();
    Method method = crashlyticsLog;
    if (method != null) {
      try {
        method.invoke(null, new Object[] { Integer.valueOf(paramInt), this.name, paramString });
        if (paramInt >= 5 && paramThrowable != null) {
          crashlyticsLogException.invoke(null, new Object[] { paramThrowable });
          return;
        } 
      } catch (Exception exception) {
        throw new RuntimeException(exception);
      } 
    } else {
      Log.println(paramInt, this.name, (String)exception);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\HandroidLoggerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */