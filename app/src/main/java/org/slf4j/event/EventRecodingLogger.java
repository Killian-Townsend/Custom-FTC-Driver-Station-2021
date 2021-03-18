package org.slf4j.event;

import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.helpers.SubstituteLogger;

public class EventRecodingLogger implements Logger {
  Queue<SubstituteLoggingEvent> eventQueue;
  
  SubstituteLogger logger;
  
  String name;
  
  public EventRecodingLogger(SubstituteLogger paramSubstituteLogger, Queue<SubstituteLoggingEvent> paramQueue) {
    this.logger = paramSubstituteLogger;
    this.name = paramSubstituteLogger.getName();
    this.eventQueue = paramQueue;
  }
  
  private void recordEvent(Level paramLevel, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
    recordEvent(paramLevel, null, paramString, paramArrayOfObject, paramThrowable);
  }
  
  private void recordEvent(Level paramLevel, Marker paramMarker, String paramString, Object[] paramArrayOfObject, Throwable paramThrowable) {
    SubstituteLoggingEvent substituteLoggingEvent = new SubstituteLoggingEvent();
    substituteLoggingEvent.setTimeStamp(System.currentTimeMillis());
    substituteLoggingEvent.setLevel(paramLevel);
    substituteLoggingEvent.setLogger(this.logger);
    substituteLoggingEvent.setLoggerName(this.name);
    substituteLoggingEvent.setMarker(paramMarker);
    substituteLoggingEvent.setMessage(paramString);
    substituteLoggingEvent.setArgumentArray(paramArrayOfObject);
    substituteLoggingEvent.setThrowable(paramThrowable);
    substituteLoggingEvent.setThreadName(Thread.currentThread().getName());
    this.eventQueue.add(substituteLoggingEvent);
  }
  
  public void debug(String paramString) {
    recordEvent(Level.TRACE, paramString, null, null);
  }
  
  public void debug(String paramString, Object paramObject) {
    recordEvent(Level.DEBUG, paramString, new Object[] { paramObject }, null);
  }
  
  public void debug(String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.DEBUG, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void debug(String paramString, Throwable paramThrowable) {
    recordEvent(Level.DEBUG, paramString, null, paramThrowable);
  }
  
  public void debug(String paramString, Object... paramVarArgs) {
    recordEvent(Level.DEBUG, paramString, paramVarArgs, null);
  }
  
  public void debug(Marker paramMarker, String paramString) {
    recordEvent(Level.DEBUG, paramMarker, paramString, null, null);
  }
  
  public void debug(Marker paramMarker, String paramString, Object paramObject) {
    recordEvent(Level.DEBUG, paramMarker, paramString, new Object[] { paramObject }, null);
  }
  
  public void debug(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.DEBUG, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void debug(Marker paramMarker, String paramString, Throwable paramThrowable) {
    recordEvent(Level.DEBUG, paramMarker, paramString, null, paramThrowable);
  }
  
  public void debug(Marker paramMarker, String paramString, Object... paramVarArgs) {
    recordEvent(Level.DEBUG, paramMarker, paramString, paramVarArgs, null);
  }
  
  public void error(String paramString) {
    recordEvent(Level.ERROR, paramString, null, null);
  }
  
  public void error(String paramString, Object paramObject) {
    recordEvent(Level.ERROR, paramString, new Object[] { paramObject }, null);
  }
  
  public void error(String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.ERROR, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void error(String paramString, Throwable paramThrowable) {
    recordEvent(Level.ERROR, paramString, null, paramThrowable);
  }
  
  public void error(String paramString, Object... paramVarArgs) {
    recordEvent(Level.ERROR, paramString, paramVarArgs, null);
  }
  
  public void error(Marker paramMarker, String paramString) {
    recordEvent(Level.ERROR, paramMarker, paramString, null, null);
  }
  
  public void error(Marker paramMarker, String paramString, Object paramObject) {
    recordEvent(Level.ERROR, paramMarker, paramString, new Object[] { paramObject }, null);
  }
  
  public void error(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.ERROR, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void error(Marker paramMarker, String paramString, Throwable paramThrowable) {
    recordEvent(Level.ERROR, paramMarker, paramString, null, paramThrowable);
  }
  
  public void error(Marker paramMarker, String paramString, Object... paramVarArgs) {
    recordEvent(Level.ERROR, paramMarker, paramString, paramVarArgs, null);
  }
  
  public String getName() {
    return this.name;
  }
  
  public void info(String paramString) {
    recordEvent(Level.INFO, paramString, null, null);
  }
  
  public void info(String paramString, Object paramObject) {
    recordEvent(Level.INFO, paramString, new Object[] { paramObject }, null);
  }
  
  public void info(String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.INFO, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void info(String paramString, Throwable paramThrowable) {
    recordEvent(Level.INFO, paramString, null, paramThrowable);
  }
  
  public void info(String paramString, Object... paramVarArgs) {
    recordEvent(Level.INFO, paramString, paramVarArgs, null);
  }
  
  public void info(Marker paramMarker, String paramString) {
    recordEvent(Level.INFO, paramMarker, paramString, null, null);
  }
  
  public void info(Marker paramMarker, String paramString, Object paramObject) {
    recordEvent(Level.INFO, paramMarker, paramString, new Object[] { paramObject }, null);
  }
  
  public void info(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.INFO, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void info(Marker paramMarker, String paramString, Throwable paramThrowable) {
    recordEvent(Level.INFO, paramMarker, paramString, null, paramThrowable);
  }
  
  public void info(Marker paramMarker, String paramString, Object... paramVarArgs) {
    recordEvent(Level.INFO, paramMarker, paramString, paramVarArgs, null);
  }
  
  public boolean isDebugEnabled() {
    return true;
  }
  
  public boolean isDebugEnabled(Marker paramMarker) {
    return true;
  }
  
  public boolean isErrorEnabled() {
    return true;
  }
  
  public boolean isErrorEnabled(Marker paramMarker) {
    return true;
  }
  
  public boolean isInfoEnabled() {
    return true;
  }
  
  public boolean isInfoEnabled(Marker paramMarker) {
    return true;
  }
  
  public boolean isTraceEnabled() {
    return true;
  }
  
  public boolean isTraceEnabled(Marker paramMarker) {
    return true;
  }
  
  public boolean isWarnEnabled() {
    return true;
  }
  
  public boolean isWarnEnabled(Marker paramMarker) {
    return true;
  }
  
  public void trace(String paramString) {
    recordEvent(Level.TRACE, paramString, null, null);
  }
  
  public void trace(String paramString, Object paramObject) {
    recordEvent(Level.TRACE, paramString, new Object[] { paramObject }, null);
  }
  
  public void trace(String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.TRACE, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void trace(String paramString, Throwable paramThrowable) {
    recordEvent(Level.TRACE, paramString, null, paramThrowable);
  }
  
  public void trace(String paramString, Object... paramVarArgs) {
    recordEvent(Level.TRACE, paramString, paramVarArgs, null);
  }
  
  public void trace(Marker paramMarker, String paramString) {
    recordEvent(Level.TRACE, paramMarker, paramString, null, null);
  }
  
  public void trace(Marker paramMarker, String paramString, Object paramObject) {
    recordEvent(Level.TRACE, paramMarker, paramString, new Object[] { paramObject }, null);
  }
  
  public void trace(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.TRACE, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void trace(Marker paramMarker, String paramString, Throwable paramThrowable) {
    recordEvent(Level.TRACE, paramMarker, paramString, null, paramThrowable);
  }
  
  public void trace(Marker paramMarker, String paramString, Object... paramVarArgs) {
    recordEvent(Level.TRACE, paramMarker, paramString, paramVarArgs, null);
  }
  
  public void warn(String paramString) {
    recordEvent(Level.WARN, paramString, null, null);
  }
  
  public void warn(String paramString, Object paramObject) {
    recordEvent(Level.WARN, paramString, new Object[] { paramObject }, null);
  }
  
  public void warn(String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.WARN, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void warn(String paramString, Throwable paramThrowable) {
    recordEvent(Level.WARN, paramString, null, paramThrowable);
  }
  
  public void warn(String paramString, Object... paramVarArgs) {
    recordEvent(Level.WARN, paramString, paramVarArgs, null);
  }
  
  public void warn(Marker paramMarker, String paramString) {
    recordEvent(Level.WARN, paramString, null, null);
  }
  
  public void warn(Marker paramMarker, String paramString, Object paramObject) {
    recordEvent(Level.WARN, paramString, new Object[] { paramObject }, null);
  }
  
  public void warn(Marker paramMarker, String paramString, Object paramObject1, Object paramObject2) {
    recordEvent(Level.WARN, paramMarker, paramString, new Object[] { paramObject1, paramObject2 }, null);
  }
  
  public void warn(Marker paramMarker, String paramString, Throwable paramThrowable) {
    recordEvent(Level.WARN, paramMarker, paramString, null, paramThrowable);
  }
  
  public void warn(Marker paramMarker, String paramString, Object... paramVarArgs) {
    recordEvent(Level.WARN, paramMarker, paramString, paramVarArgs, null);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\event\EventRecodingLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */