package org.slf4j.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.event.SubstituteLoggingEvent;

public class SubstituteLoggerFactory implements ILoggerFactory {
  final LinkedBlockingQueue<SubstituteLoggingEvent> eventQueue = new LinkedBlockingQueue<SubstituteLoggingEvent>();
  
  final Map<String, SubstituteLogger> loggers = new HashMap<String, SubstituteLogger>();
  
  boolean postInitialization = false;
  
  public void clear() {
    this.loggers.clear();
    this.eventQueue.clear();
  }
  
  public LinkedBlockingQueue<SubstituteLoggingEvent> getEventQueue() {
    return this.eventQueue;
  }
  
  public Logger getLogger(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield loggers : Ljava/util/Map;
    //   6: aload_1
    //   7: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   12: checkcast org/slf4j/helpers/SubstituteLogger
    //   15: astore_3
    //   16: aload_3
    //   17: astore_2
    //   18: aload_3
    //   19: ifnonnull -> 51
    //   22: new org/slf4j/helpers/SubstituteLogger
    //   25: dup
    //   26: aload_1
    //   27: aload_0
    //   28: getfield eventQueue : Ljava/util/concurrent/LinkedBlockingQueue;
    //   31: aload_0
    //   32: getfield postInitialization : Z
    //   35: invokespecial <init> : (Ljava/lang/String;Ljava/util/Queue;Z)V
    //   38: astore_2
    //   39: aload_0
    //   40: getfield loggers : Ljava/util/Map;
    //   43: aload_1
    //   44: aload_2
    //   45: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   50: pop
    //   51: aload_0
    //   52: monitorexit
    //   53: aload_2
    //   54: areturn
    //   55: astore_1
    //   56: aload_0
    //   57: monitorexit
    //   58: aload_1
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	55	finally
    //   22	51	55	finally
  }
  
  public List<String> getLoggerNames() {
    return new ArrayList<String>(this.loggers.keySet());
  }
  
  public List<SubstituteLogger> getLoggers() {
    return new ArrayList<SubstituteLogger>(this.loggers.values());
  }
  
  public void postInitialization() {
    this.postInitialization = true;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\helpers\SubstituteLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */