package org.slf4j.event;

import org.slf4j.Marker;
import org.slf4j.helpers.SubstituteLogger;

public class SubstituteLoggingEvent implements LoggingEvent {
  Object[] argArray;
  
  Level level;
  
  SubstituteLogger logger;
  
  String loggerName;
  
  Marker marker;
  
  String message;
  
  String threadName;
  
  Throwable throwable;
  
  long timeStamp;
  
  public Object[] getArgumentArray() {
    return this.argArray;
  }
  
  public Level getLevel() {
    return this.level;
  }
  
  public SubstituteLogger getLogger() {
    return this.logger;
  }
  
  public String getLoggerName() {
    return this.loggerName;
  }
  
  public Marker getMarker() {
    return this.marker;
  }
  
  public String getMessage() {
    return this.message;
  }
  
  public String getThreadName() {
    return this.threadName;
  }
  
  public Throwable getThrowable() {
    return this.throwable;
  }
  
  public long getTimeStamp() {
    return this.timeStamp;
  }
  
  public void setArgumentArray(Object[] paramArrayOfObject) {
    this.argArray = paramArrayOfObject;
  }
  
  public void setLevel(Level paramLevel) {
    this.level = paramLevel;
  }
  
  public void setLogger(SubstituteLogger paramSubstituteLogger) {
    this.logger = paramSubstituteLogger;
  }
  
  public void setLoggerName(String paramString) {
    this.loggerName = paramString;
  }
  
  public void setMarker(Marker paramMarker) {
    this.marker = paramMarker;
  }
  
  public void setMessage(String paramString) {
    this.message = paramString;
  }
  
  public void setThreadName(String paramString) {
    this.threadName = paramString;
  }
  
  public void setThrowable(Throwable paramThrowable) {
    this.throwable = paramThrowable;
  }
  
  public void setTimeStamp(long paramLong) {
    this.timeStamp = paramLong;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\event\SubstituteLoggingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */