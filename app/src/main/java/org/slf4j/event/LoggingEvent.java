package org.slf4j.event;

import org.slf4j.Marker;

public interface LoggingEvent {
  Object[] getArgumentArray();
  
  Level getLevel();
  
  String getLoggerName();
  
  Marker getMarker();
  
  String getMessage();
  
  String getThreadName();
  
  Throwable getThrowable();
  
  long getTimeStamp();
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\event\LoggingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */