package org.slf4j.impl;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

class AndroidLoggerFactory implements ILoggerFactory {
  static final String ANONYMOUS_TAG = "null";
  
  static final int TAG_MAX_LENGTH = 23;
  
  private final ConcurrentMap<String, Logger> loggerMap = new ConcurrentHashMap<String, Logger>();
  
  private static String getSimpleName(String paramString) {
    int i = paramString.length();
    int j = paramString.lastIndexOf('.');
    if (j != -1)
      if (i - ++j <= 23)
        return paramString.substring(j);  
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('*');
    stringBuilder.append(paramString.substring(i - 23 + 1));
    return stringBuilder.toString();
  }
  
  static String loggerNameToTag(String paramString) {
    String str1;
    if (paramString == null)
      return "null"; 
    int k = paramString.length();
    if (k <= 23 && HandroidLoggerAdapter.APP_NAME == null)
      return paramString; 
    String str2 = HandroidLoggerAdapter.APP_NAME;
    int j = 0;
    if (str2 != null) {
      int m = paramString.lastIndexOf('.');
      if (m >= 0)
        paramString = paramString.substring(m + 1, k); 
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append(HandroidLoggerAdapter.APP_NAME);
      stringBuilder1.append(":");
      stringBuilder1.append(paramString);
      String str = stringBuilder1.toString();
      paramString = str;
      if (str.length() > 23) {
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str.substring(0, 22));
        stringBuilder2.append('*');
        str1 = stringBuilder2.toString();
      } 
      return str1;
    } 
    StringBuilder stringBuilder = new StringBuilder(26);
    int i = 0;
    while (true) {
      int m = str1.indexOf('.', j);
      if (m != -1) {
        stringBuilder.append(str1.charAt(j));
        if (m - j > 1)
          stringBuilder.append('*'); 
        stringBuilder.append('.');
        j = m + 1;
        m = stringBuilder.length();
        i = m;
        if (m > 23)
          return getSimpleName(str1); 
        continue;
      } 
      if (i == 0 || i + k - j > 23)
        return getSimpleName(str1); 
      stringBuilder.append(str1, j, k);
      return stringBuilder.toString();
    } 
  }
  
  public Logger getLogger(String paramString) {
    String str = loggerNameToTag(paramString);
    Logger logger2 = this.loggerMap.get(str);
    Logger logger1 = logger2;
    if (logger2 == null) {
      HandroidLoggerAdapter handroidLoggerAdapter = new HandroidLoggerAdapter(str);
      logger1 = (Logger)this.loggerMap.putIfAbsent(str, handroidLoggerAdapter);
      if (logger1 == null)
        return (Logger)handroidLoggerAdapter; 
    } 
    return logger1;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\AndroidLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */