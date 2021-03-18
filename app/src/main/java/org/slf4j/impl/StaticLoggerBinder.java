package org.slf4j.impl;

import org.slf4j.ILoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

public class StaticLoggerBinder implements LoggerFactoryBinder {
  public static String REQUESTED_API_VERSION;
  
  private static final StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
  
  private static final String loggerFactoryClassStr;
  
  private final ILoggerFactory loggerFactory = new AndroidLoggerFactory();
  
  static {
    REQUESTED_API_VERSION = "1.7.x";
    loggerFactoryClassStr = AndroidLoggerFactory.class.getName();
  }
  
  public static StaticLoggerBinder getSingleton() {
    return SINGLETON;
  }
  
  public ILoggerFactory getLoggerFactory() {
    return this.loggerFactory;
  }
  
  public String getLoggerFactoryClassStr() {
    return loggerFactoryClassStr;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\impl\StaticLoggerBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */