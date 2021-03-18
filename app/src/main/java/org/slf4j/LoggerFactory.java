package org.slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import org.slf4j.event.LoggingEvent;
import org.slf4j.event.SubstituteLoggingEvent;
import org.slf4j.helpers.NOPLoggerFactory;
import org.slf4j.helpers.SubstituteLogger;
import org.slf4j.helpers.SubstituteLoggerFactory;
import org.slf4j.helpers.Util;
import org.slf4j.impl.StaticLoggerBinder;

public final class LoggerFactory {
  private static final String[] API_COMPATIBILITY_LIST;
  
  static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
  
  static boolean DETECT_LOGGER_NAME_MISMATCH = false;
  
  static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
  
  static final int FAILED_INITIALIZATION = 2;
  
  static volatile int INITIALIZATION_STATE = 0;
  
  static final String JAVA_VENDOR_PROPERTY = "java.vendor.url";
  
  static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
  
  static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
  
  static final NOPLoggerFactory NOP_FALLBACK_FACTORY;
  
  static final int NOP_FALLBACK_INITIALIZATION = 4;
  
  static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
  
  static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
  
  static final int ONGOING_INITIALIZATION = 1;
  
  static final String REPLAY_URL = "http://www.slf4j.org/codes.html#replay";
  
  private static String STATIC_LOGGER_BINDER_PATH;
  
  static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
  
  static final SubstituteLoggerFactory SUBST_FACTORY = new SubstituteLoggerFactory();
  
  static final int SUCCESSFUL_INITIALIZATION = 3;
  
  static final int UNINITIALIZED = 0;
  
  static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
  
  static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
  
  static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
  
  static {
    NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
    DETECT_LOGGER_NAME_MISMATCH = Util.safeGetBooleanSystemProperty("slf4j.detectLoggerNameMismatch");
    API_COMPATIBILITY_LIST = new String[] { "1.6", "1.7" };
    STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
  }
  
  private static final void bind() {
    Set<URL> set = null;
    try {
      if (!isAndroid()) {
        set = findPossibleStaticLoggerBinderPathSet();
        reportMultipleBindingAmbiguity(set);
      } 
      StaticLoggerBinder.getSingleton();
      INITIALIZATION_STATE = 3;
      reportActualBinding(set);
      fixSubstituteLoggers();
      replayEvents();
      SUBST_FACTORY.clear();
      return;
    } catch (NoClassDefFoundError noClassDefFoundError) {
      if (messageContainsOrgSlf4jImplStaticLoggerBinder(noClassDefFoundError.getMessage())) {
        INITIALIZATION_STATE = 4;
        Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
        Util.report("Defaulting to no-operation (NOP) logger implementation");
        Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
        return;
      } 
      failedBinding(noClassDefFoundError);
      throw noClassDefFoundError;
    } catch (NoSuchMethodError noSuchMethodError) {
      String str = noSuchMethodError.getMessage();
      if (str != null && str.contains("org.slf4j.impl.StaticLoggerBinder.getSingleton()")) {
        INITIALIZATION_STATE = 2;
        Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
        Util.report("Your binding is version 1.5.5 or earlier.");
        Util.report("Upgrade your binding to version 1.6.x.");
      } 
      throw noSuchMethodError;
    } catch (Exception exception) {
      failedBinding(exception);
      throw new IllegalStateException("Unexpected initialization failure", exception);
    } 
  }
  
  private static void emitReplayOrSubstituionWarning(SubstituteLoggingEvent paramSubstituteLoggingEvent, int paramInt) {
    if (paramSubstituteLoggingEvent.getLogger().isDelegateEventAware()) {
      emitReplayWarning(paramInt);
      return;
    } 
    if (paramSubstituteLoggingEvent.getLogger().isDelegateNOP())
      return; 
    emitSubstitutionWarning();
  }
  
  private static void emitReplayWarning(int paramInt) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("A number (");
    stringBuilder.append(paramInt);
    stringBuilder.append(") of logging calls during the initialization phase have been intercepted and are");
    Util.report(stringBuilder.toString());
    Util.report("now being replayed. These are subject to the filtering rules of the underlying logging system.");
    Util.report("See also http://www.slf4j.org/codes.html#replay");
  }
  
  private static void emitSubstitutionWarning() {
    Util.report("The following set of substitute loggers may have been accessed");
    Util.report("during the initialization phase. Logging calls during this");
    Util.report("phase were not honored. However, subsequent logging calls to these");
    Util.report("loggers will work as normally expected.");
    Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
  }
  
  static void failedBinding(Throwable paramThrowable) {
    INITIALIZATION_STATE = 2;
    Util.report("Failed to instantiate SLF4J LoggerFactory", paramThrowable);
  }
  
  static Set<URL> findPossibleStaticLoggerBinderPathSet() {
    LinkedHashSet<URL> linkedHashSet = new LinkedHashSet();
    try {
      Enumeration<URL> enumeration;
      ClassLoader classLoader = LoggerFactory.class.getClassLoader();
      if (classLoader == null) {
        enumeration = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
      } else {
        enumeration = enumeration.getResources(STATIC_LOGGER_BINDER_PATH);
      } 
      while (enumeration.hasMoreElements())
        linkedHashSet.add(enumeration.nextElement()); 
    } catch (IOException iOException) {
      Util.report("Error getting resources from path", iOException);
    } 
    return linkedHashSet;
  }
  
  private static void fixSubstituteLoggers() {
    synchronized (SUBST_FACTORY) {
      SUBST_FACTORY.postInitialization();
      for (SubstituteLogger substituteLogger : SUBST_FACTORY.getLoggers())
        substituteLogger.setDelegate(getLogger(substituteLogger.getName())); 
      return;
    } 
  }
  
  public static ILoggerFactory getILoggerFactory() {
    // Byte code:
    //   0: getstatic org/slf4j/LoggerFactory.INITIALIZATION_STATE : I
    //   3: ifne -> 34
    //   6: ldc org/slf4j/LoggerFactory
    //   8: monitorenter
    //   9: getstatic org/slf4j/LoggerFactory.INITIALIZATION_STATE : I
    //   12: ifne -> 22
    //   15: iconst_1
    //   16: putstatic org/slf4j/LoggerFactory.INITIALIZATION_STATE : I
    //   19: invokestatic performInitialization : ()V
    //   22: ldc org/slf4j/LoggerFactory
    //   24: monitorexit
    //   25: goto -> 34
    //   28: astore_1
    //   29: ldc org/slf4j/LoggerFactory
    //   31: monitorexit
    //   32: aload_1
    //   33: athrow
    //   34: getstatic org/slf4j/LoggerFactory.INITIALIZATION_STATE : I
    //   37: istore_0
    //   38: iload_0
    //   39: iconst_1
    //   40: if_icmpeq -> 90
    //   43: iload_0
    //   44: iconst_2
    //   45: if_icmpeq -> 80
    //   48: iload_0
    //   49: iconst_3
    //   50: if_icmpeq -> 73
    //   53: iload_0
    //   54: iconst_4
    //   55: if_icmpne -> 62
    //   58: getstatic org/slf4j/LoggerFactory.NOP_FALLBACK_FACTORY : Lorg/slf4j/helpers/NOPLoggerFactory;
    //   61: areturn
    //   62: new java/lang/IllegalStateException
    //   65: dup
    //   66: ldc_w 'Unreachable code'
    //   69: invokespecial <init> : (Ljava/lang/String;)V
    //   72: athrow
    //   73: invokestatic getSingleton : ()Lorg/slf4j/impl/StaticLoggerBinder;
    //   76: invokevirtual getLoggerFactory : ()Lorg/slf4j/ILoggerFactory;
    //   79: areturn
    //   80: new java/lang/IllegalStateException
    //   83: dup
    //   84: ldc 'org.slf4j.LoggerFactory in failed state. Original exception was thrown EARLIER. See also http://www.slf4j.org/codes.html#unsuccessfulInit'
    //   86: invokespecial <init> : (Ljava/lang/String;)V
    //   89: athrow
    //   90: getstatic org/slf4j/LoggerFactory.SUBST_FACTORY : Lorg/slf4j/helpers/SubstituteLoggerFactory;
    //   93: areturn
    // Exception table:
    //   from	to	target	type
    //   9	22	28	finally
    //   22	25	28	finally
    //   29	32	28	finally
  }
  
  public static Logger getLogger(Class<?> paramClass) {
    Logger logger = getLogger(paramClass.getName());
    if (DETECT_LOGGER_NAME_MISMATCH) {
      Class<?> clazz = Util.getCallingClass();
      if (clazz != null && nonMatchingClasses(paramClass, clazz)) {
        Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), clazz.getName() }));
        Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
      } 
    } 
    return logger;
  }
  
  public static Logger getLogger(String paramString) {
    return getILoggerFactory().getLogger(paramString);
  }
  
  private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> paramSet) {
    return (paramSet.size() > 1);
  }
  
  private static boolean isAndroid() {
    String str = Util.safeGetSystemProperty("java.vendor.url");
    return (str == null) ? false : str.toLowerCase().contains("android");
  }
  
  private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String paramString) {
    return (paramString == null) ? false : (paramString.contains("org/slf4j/impl/StaticLoggerBinder") ? true : (paramString.contains("org.slf4j.impl.StaticLoggerBinder")));
  }
  
  private static boolean nonMatchingClasses(Class<?> paramClass1, Class<?> paramClass2) {
    return paramClass2.isAssignableFrom(paramClass1) ^ true;
  }
  
  private static final void performInitialization() {
    bind();
    if (INITIALIZATION_STATE == 3)
      versionSanityCheck(); 
  }
  
  private static void replayEvents() {
    LinkedBlockingQueue linkedBlockingQueue = SUBST_FACTORY.getEventQueue();
    int j = linkedBlockingQueue.size();
    ArrayList arrayList = new ArrayList(128);
    int i = 0;
    while (true) {
      if (linkedBlockingQueue.drainTo(arrayList, 128) == 0)
        return; 
      for (SubstituteLoggingEvent substituteLoggingEvent : arrayList) {
        replaySingleEvent(substituteLoggingEvent);
        if (!i)
          emitReplayOrSubstituionWarning(substituteLoggingEvent, j); 
        i++;
      } 
      arrayList.clear();
    } 
  }
  
  private static void replaySingleEvent(SubstituteLoggingEvent paramSubstituteLoggingEvent) {
    if (paramSubstituteLoggingEvent == null)
      return; 
    SubstituteLogger substituteLogger = paramSubstituteLoggingEvent.getLogger();
    String str = substituteLogger.getName();
    if (!substituteLogger.isDelegateNull()) {
      if (substituteLogger.isDelegateNOP())
        return; 
      if (substituteLogger.isDelegateEventAware()) {
        substituteLogger.log((LoggingEvent)paramSubstituteLoggingEvent);
        return;
      } 
      Util.report(str);
      return;
    } 
    throw new IllegalStateException("Delegate logger cannot be null at this state.");
  }
  
  private static void reportActualBinding(Set<URL> paramSet) {
    if (paramSet != null && isAmbiguousStaticLoggerBinderPathSet(paramSet)) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Actual binding is of type [");
      stringBuilder.append(StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr());
      stringBuilder.append("]");
      Util.report(stringBuilder.toString());
    } 
  }
  
  private static void reportMultipleBindingAmbiguity(Set<URL> paramSet) {
    if (isAmbiguousStaticLoggerBinderPathSet(paramSet)) {
      Util.report("Class path contains multiple SLF4J bindings.");
      for (URL uRL : paramSet) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Found binding in [");
        stringBuilder.append(uRL);
        stringBuilder.append("]");
        Util.report(stringBuilder.toString());
      } 
      Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
    } 
  }
  
  static void reset() {
    INITIALIZATION_STATE = 0;
  }
  
  private static final void versionSanityCheck() {
    try {
      String str = StaticLoggerBinder.REQUESTED_API_VERSION;
      String[] arrayOfString = API_COMPATIBILITY_LIST;
      int j = arrayOfString.length;
      int i = 0;
      boolean bool = false;
      while (true) {
        if (i < j) {
          if (str.startsWith(arrayOfString[i]))
            bool = true; 
        } else {
          return;
        } 
        i++;
      } 
    } catch (NoSuchFieldError noSuchFieldError) {
      return;
    } finally {
      Exception exception = null;
      Util.report("Unexpected problem occured during version sanity check", exception);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\slf4j\LoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */