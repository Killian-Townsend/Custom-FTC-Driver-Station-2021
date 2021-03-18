package com.qualcomm.robotcore.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.qualcomm.robotcore.exception.RobotCoreException;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;
import org.firstinspires.ftc.robotcore.internal.files.LogOutputStream;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Misc;

public class RobotLog {
  public static final String OPMODE_START_TAG = "******************** START - OPMODE %s ********************";
  
  public static final String OPMODE_STOP_TAG = "******************** STOP - OPMODE %s ********************";
  
  public static final String TAG = "RobotCore";
  
  private static final Object globalErrorLock = new Object();
  
  private static String globalErrorMessage = "";
  
  private static boolean globalErrorMsgSticky;
  
  private static final Object globalWarningLock = new Object();
  
  private static String globalWarningMessage = "";
  
  private static boolean globalWarningMsgSticky;
  
  private static WeakHashMap<GlobalWarningSource, Integer> globalWarningSources = new WeakHashMap<GlobalWarningSource, Integer>();
  
  private static int kbLogcatQuantum;
  
  private static String logcatCommand;
  
  private static String logcatCommandRaw;
  
  private static String logcatFilter;
  
  private static String logcatFormat;
  
  private static int logcatRotatedLogsMax;
  
  private static LoggingThread loggingThread;
  
  private static String matchLogFilename;
  
  private static Calendar matchStartTime;
  
  private static double msTimeOffset = 0.0D;
  
  static {
    globalErrorMsgSticky = false;
    globalWarningMsgSticky = false;
    loggingThread = null;
    matchLogFilename = null;
    logcatCommandRaw = "logcat";
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("exec ");
    stringBuilder.append(logcatCommandRaw);
    logcatCommand = stringBuilder.toString();
    kbLogcatQuantum = 4096;
    logcatRotatedLogsMax = 4;
    logcatFormat = "threadtime";
    logcatFilter = "UsbRequestJNI:S UsbRequest:S art:W ThreadPool:W System:W ExtendedExtractor:W OMXClient:W MediaPlayer:W dalvikvm:W  *:V";
    matchStartTime = null;
  }
  
  public static void a(String paramString) {
    internalLog(7, "RobotCore", paramString);
  }
  
  public static void a(String paramString, Object... paramVarArgs) {
    v(String.format(paramString, paramVarArgs));
  }
  
  public static void aa(String paramString1, String paramString2) {
    internalLog(7, paramString1, paramString2);
  }
  
  public static void aa(String paramString1, String paramString2, Object... paramVarArgs) {
    vv(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void aa(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(7, paramString1, paramThrowable, paramString2);
  }
  
  public static void aa(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    vv(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public static void addGlobalWarningMessage(String paramString) {
    synchronized (globalWarningLock) {
      if (!globalWarningMessage.isEmpty()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(globalWarningMessage);
        stringBuilder.append("; ");
        stringBuilder.append(paramString);
        globalWarningMessage = stringBuilder.toString();
      } else {
        globalWarningMessage = paramString;
      } 
      return;
    } 
  }
  
  public static void cancelWriteLogcatToDisk() {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/RobotLog
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   6: astore_0
    //   7: aload_0
    //   8: ifnonnull -> 15
    //   11: ldc com/qualcomm/robotcore/util/RobotLog
    //   13: monitorexit
    //   14: return
    //   15: invokestatic getDefContext : ()Landroid/content/Context;
    //   18: astore_0
    //   19: aload_0
    //   20: invokevirtual getPackageName : ()Ljava/lang/String;
    //   23: pop
    //   24: aload_0
    //   25: invokestatic getLogFile : (Landroid/content/Context;)Ljava/io/File;
    //   28: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   31: pop
    //   32: ldc2_w 500
    //   35: invokestatic sleep : (J)V
    //   38: getstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   41: invokevirtual kill : ()V
    //   44: ldc 'Waiting for the logcat process to die.'
    //   46: invokestatic v : (Ljava/lang/String;)V
    //   49: new com/qualcomm/robotcore/util/ElapsedTime
    //   52: dup
    //   53: invokespecial <init> : ()V
    //   56: astore_0
    //   57: getstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   60: ifnull -> 86
    //   63: aload_0
    //   64: invokevirtual milliseconds : ()D
    //   67: ldc2_w 1000.0
    //   70: dcmpl
    //   71: ifle -> 80
    //   74: getstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   77: invokevirtual interrupt : ()V
    //   80: invokestatic yield : ()V
    //   83: goto -> 57
    //   86: ldc com/qualcomm/robotcore/util/RobotLog
    //   88: monitorexit
    //   89: return
    //   90: astore_0
    //   91: ldc com/qualcomm/robotcore/util/RobotLog
    //   93: monitorexit
    //   94: aload_0
    //   95: athrow
    //   96: astore_0
    //   97: goto -> 38
    // Exception table:
    //   from	to	target	type
    //   3	7	90	finally
    //   15	32	90	finally
    //   32	38	96	java/lang/InterruptedException
    //   32	38	90	finally
    //   38	57	90	finally
    //   57	80	90	finally
    //   80	83	90	finally
  }
  
  public static void clearGlobalErrorMsg() {
    if (globalErrorMsgSticky)
      return; 
    synchronized (globalErrorLock) {
      globalErrorMessage = "";
      return;
    } 
  }
  
  public static void clearGlobalWarningMsg() {
    if (globalWarningMsgSticky)
      return; 
    synchronized (globalWarningLock) {
      globalWarningMessage = "";
      return;
    } 
  }
  
  public static String combineGlobalWarnings(List<String> paramList) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String str : paramList) {
      if (str != null && !str.isEmpty()) {
        if (stringBuilder.length() > 0)
          stringBuilder.append("; "); 
        stringBuilder.append(str);
      } 
    } 
    return stringBuilder.toString();
  }
  
  public static void d(String paramString) {
    internalLog(3, "RobotCore", paramString);
  }
  
  public static void d(String paramString, Object... paramVarArgs) {
    d(String.format(paramString, paramVarArgs));
  }
  
  public static void dd(String paramString1, String paramString2) {
    internalLog(3, paramString1, paramString2);
  }
  
  public static void dd(String paramString1, String paramString2, Object... paramVarArgs) {
    dd(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void dd(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(3, paramString1, paramThrowable, paramString2);
  }
  
  public static void dd(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    dd(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public static void e(String paramString) {
    internalLog(6, "RobotCore", paramString);
  }
  
  public static void e(String paramString, Object... paramVarArgs) {
    e(String.format(paramString, paramVarArgs));
  }
  
  public static void ee(String paramString1, String paramString2) {
    internalLog(6, paramString1, paramString2);
  }
  
  public static void ee(String paramString1, String paramString2, Object... paramVarArgs) {
    ee(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void ee(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(6, paramString1, paramThrowable, paramString2);
  }
  
  public static void ee(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    ee(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public static List<File> getExtantLogFiles(Context paramContext) {
    ArrayList<File> arrayList = new ArrayList();
    File file = getLogFile(paramContext);
    arrayList.add(file);
    int i = 1;
    while (true) {
      File file1 = file.getParentFile();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(file.getName());
      stringBuilder.append(".");
      stringBuilder.append(i);
      stringBuilder.append(".gz");
      file1 = new File(file1, stringBuilder.toString());
      if (file1.exists()) {
        arrayList.add(file1);
        i++;
        continue;
      } 
      return arrayList;
    } 
  }
  
  public static String getGlobalErrorMsg() {
    synchronized (globalErrorLock) {
      return globalErrorMessage;
    } 
  }
  
  public static String getGlobalWarningMessage() {
    null = new ArrayList();
    synchronized (globalWarningLock) {
      null.add(globalWarningMessage);
      Iterator<GlobalWarningSource> iterator = globalWarningSources.keySet().iterator();
      while (iterator.hasNext())
        null.add(((GlobalWarningSource)iterator.next()).getGlobalWarning()); 
      return combineGlobalWarnings(null);
    } 
  }
  
  protected static int getIntStatic(Class paramClass, String paramString) {
    try {
      return paramClass.getField(paramString).getInt(null);
    } catch (Exception exception) {
      return 0;
    } 
  }
  
  public static long getLocalTime(long paramLong) {
    return (long)(paramLong - msTimeOffset + 0.5D);
  }
  
  private static File getLogFile(Context paramContext) {
    return new File(getLogFilename(paramContext));
  }
  
  public static String getLogFilename() {
    return getLogFilename(AppUtil.getDefContext());
  }
  
  public static String getLogFilename(Context paramContext) {
    String str;
    File file = AppUtil.LOG_FOLDER;
    file.mkdirs();
    if (AppUtil.getInstance().isRobotController()) {
      str = "robotControllerLog.txt";
    } else if (AppUtil.getInstance().isDriverStation()) {
      str = "driverStationLog.txt";
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(str.getPackageName());
      stringBuilder.append("Log.txt");
      str = stringBuilder.toString();
    } 
    return (new File(file, str)).getAbsolutePath();
  }
  
  private static File getMatchLogFile(Context paramContext, String paramString, int paramInt) {
    return new File(getMatchLogFilename(paramContext, paramString, paramInt));
  }
  
  public static String getMatchLogFilename(Context paramContext, String paramString, int paramInt) {
    File file = AppUtil.MATCH_LOG_FOLDER;
    file.mkdirs();
    return (new File(file, String.format("Match-%d-%s.txt", new Object[] { Integer.valueOf(paramInt), paramString.replaceAll(" ", "_") }))).getAbsolutePath();
  }
  
  public static long getRemoteTime() {
    return getRemoteTime(AppUtil.getInstance().getWallClockTime());
  }
  
  public static long getRemoteTime(long paramLong) {
    return (long)(paramLong + msTimeOffset + 0.5D);
  }
  
  private static StackTraceElement getStackTop(Exception paramException) {
    StackTraceElement[] arrayOfStackTraceElement = paramException.getStackTrace();
    return (arrayOfStackTraceElement.length > 0) ? arrayOfStackTraceElement[0] : null;
  }
  
  protected static String getStringStatic(Class paramClass, String paramString) {
    try {
      return (String)paramClass.getField(paramString).get(null);
    } catch (Exception exception) {
      return "";
    } 
  }
  
  public static boolean hasGlobalErrorMsg() {
    return getGlobalErrorMsg().isEmpty() ^ true;
  }
  
  public static boolean hasGlobalWarningMsg() {
    return getGlobalWarningMessage().isEmpty() ^ true;
  }
  
  public static void i(String paramString) {
    internalLog(4, "RobotCore", paramString);
  }
  
  public static void i(String paramString, Object... paramVarArgs) {
    i(String.format(paramString, paramVarArgs));
  }
  
  public static void ii(String paramString1, String paramString2) {
    internalLog(4, paramString1, paramString2);
  }
  
  public static void ii(String paramString1, String paramString2, Object... paramVarArgs) {
    ii(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void ii(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(4, paramString1, paramThrowable, paramString2);
  }
  
  public static void ii(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    ii(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public static void internalLog(int paramInt, String paramString1, String paramString2) {
    if (msTimeOffset == 0.0D) {
      Log.println(paramInt, paramString1, paramString2);
      return;
    } 
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTimeInMillis(getRemoteTime());
    Log.println(paramInt, paramString1, Misc.formatInvariant("{%5d %2d.%03d} %s", new Object[] { Integer.valueOf((int)(msTimeOffset + 0.5D)), Integer.valueOf(gregorianCalendar.get(13)), Integer.valueOf(gregorianCalendar.get(14)), paramString2 }));
  }
  
  public static void internalLog(int paramInt, String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(paramInt, paramString1, paramString2);
    logStackTrace(paramString1, paramThrowable);
  }
  
  public static void logAndThrow(String paramString) throws RobotCoreException {
    w(paramString);
    throw new RobotCoreException(paramString);
  }
  
  public static void logBuildConfig(Class paramClass) {
    String str = getStringStatic(paramClass, "APPLICATION_ID");
    v("BuildConfig: versionCode=%d versionName=%s module=%s", new Object[] { Integer.valueOf(getIntStatic(paramClass, "VERSION_CODE")), getStringStatic(paramClass, "VERSION_NAME"), str });
  }
  
  public static void logBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte, int paramInt) {
    logBytes(paramString1, paramString2, paramArrayOfbyte, 0, paramInt);
  }
  
  public static void logBytes(String paramString1, String paramString2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    byte b = 58;
    while (paramInt1 < paramInt2) {
      StringBuilder stringBuilder = new StringBuilder();
      int i;
      for (i = 0; i < 16; i++) {
        int j = i + paramInt1;
        if (j >= paramInt2)
          break; 
        stringBuilder.append(String.format("%02x ", new Object[] { Byte.valueOf(paramArrayOfbyte[j]) }));
      } 
      vv(paramString1, "%s%c %s", new Object[] { paramString2, Character.valueOf(b), stringBuilder.toString() });
      b = 124;
      paramInt1 += 16;
    } 
  }
  
  public static void logDeviceInfo() {
    i("Android Device: maker=%s model=%s sdk=%d", new Object[] { Build.MANUFACTURER, Build.MODEL, Integer.valueOf(Build.VERSION.SDK_INT) });
  }
  
  public static void logExceptionHeader(Exception paramException, String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    e("exception %s(%s): %s [%s]", new Object[] { paramException.getClass().getSimpleName(), paramException.getMessage(), paramString, getStackTop(paramException) });
  }
  
  public static void logExceptionHeader(String paramString1, Exception paramException, String paramString2, Object... paramVarArgs) {
    paramString2 = String.format(paramString2, paramVarArgs);
    ee(paramString1, "exception %s(%s): %s [%s]", new Object[] { paramException.getClass().getSimpleName(), paramException.getMessage(), paramString2, getStackTop(paramException) });
  }
  
  private static void logMatch() {
    File file = new File(matchLogFilename);
    final String filename = file.getAbsolutePath();
    pruneMatchLogsIfNecessary();
    if (file.exists() && !file.delete()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Could not delete match log file: ");
      stringBuilder.append(str1);
      ee("RobotCore", stringBuilder.toString());
    } 
    String str2 = String.format("'%d-%d %d:%d:%d.000'", new Object[] { Integer.valueOf(matchStartTime.get(2) + 1), Integer.valueOf(matchStartTime.get(5)), Integer.valueOf(matchStartTime.get(11)), Integer.valueOf(matchStartTime.get(12)), Integer.valueOf(matchStartTime.get(13)) });
    (new LoggingThread("MatchLogging") {
        public void run() {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("saving match logcat to ");
          stringBuilder.append(filename);
          RobotLog.ii("RobotCore", stringBuilder.toString());
          stringBuilder = new StringBuilder();
          stringBuilder.append("logging command line: ");
          stringBuilder.append(commandLine);
          RobotLog.ii("RobotCore", stringBuilder.toString());
          run(commandLine);
          stringBuilder = new StringBuilder();
          stringBuilder.append("exiting match logcat for ");
          stringBuilder.append(filename);
          RobotLog.ii("RobotCore", stringBuilder.toString());
        }
      }).start();
  }
  
  private static void logStackFrames(StackTraceElement[] paramArrayOfStackTraceElement) {
    int j = paramArrayOfStackTraceElement.length;
    for (int i = 0; i < j; i++) {
      e("    at %s", new Object[] { paramArrayOfStackTraceElement[i].toString() });
    } 
  }
  
  public static void logStackTrace(String paramString, Throwable paramThrowable) {
    if (paramThrowable != null)
      paramThrowable.printStackTrace(LogOutputStream.printStream(paramString)); 
  }
  
  public static void logStackTrace(Thread paramThread, String paramString, Object... paramVarArgs) {
    paramString = String.format(paramString, paramVarArgs);
    e("thread id=%d tid=%d name=\"%s\" %s", new Object[] { Long.valueOf(paramThread.getId()), Integer.valueOf(ThreadPool.getTID(paramThread)), paramThread.getName(), paramString });
    logStackFrames(paramThread.getStackTrace());
  }
  
  public static void logStackTrace(Thread paramThread, StackTraceElement[] paramArrayOfStackTraceElement) {
    e("thread id=%d tid=%d name=\"%s\"", new Object[] { Long.valueOf(paramThread.getId()), Integer.valueOf(ThreadPool.getTID(paramThread)), paramThread.getName() });
    logStackFrames(paramArrayOfStackTraceElement);
  }
  
  public static void logStackTrace(Throwable paramThrowable) {
    logStackTrace("RobotCore", paramThrowable);
  }
  
  @Deprecated
  public static void logStacktrace(Throwable paramThrowable) {
    logStackTrace(paramThrowable);
  }
  
  public static void onApplicationStart() {
    writeLogcatToDisk(AppUtil.getDefContext(), kbLogcatQuantum);
  }
  
  public static void processTimeSynch(long paramLong1, long paramLong2, long paramLong3, long paramLong4) {
    if (paramLong1 != 0L && paramLong2 != 0L && paramLong3 != 0L) {
      if (paramLong4 == 0L)
        return; 
      setMsTimeOffset((paramLong2 - paramLong1 + paramLong3 - paramLong4) / 2.0D);
    } 
  }
  
  protected static void pruneMatchLogsIfNecessary() {
    File[] arrayOfFile = AppUtil.MATCH_LOG_FOLDER.listFiles();
    if (arrayOfFile.length >= 5) {
      Arrays.sort(arrayOfFile, new Comparator<File>() {
            public int compare(File param1File1, File param1File2) {
              return Long.compare(param1File2.lastModified(), param1File1.lastModified());
            }
          });
      for (int i = 0; i < arrayOfFile.length - 5; i++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pruning old logs deleting ");
        stringBuilder.append(arrayOfFile[i].getName());
        ii("RobotCore", stringBuilder.toString());
        arrayOfFile[i].delete();
      } 
    } 
  }
  
  public static void registerGlobalWarningSource(GlobalWarningSource paramGlobalWarningSource) {
    synchronized (globalWarningLock) {
      globalWarningSources.put(paramGlobalWarningSource, Integer.valueOf(1));
      return;
    } 
  }
  
  public static void setGlobalErrorMsg(RobotCoreException paramRobotCoreException, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(": ");
    stringBuilder.append(paramRobotCoreException.getMessage());
    setGlobalErrorMsg(stringBuilder.toString());
  }
  
  public static void setGlobalErrorMsg(RuntimeException paramRuntimeException, String paramString) {
    setGlobalErrorMsg(String.format("%s: %s: %s", new Object[] { paramString, paramRuntimeException.getClass().getSimpleName(), paramRuntimeException.getMessage() }));
  }
  
  public static void setGlobalErrorMsg(String paramString, Object... paramVarArgs) {
    setGlobalErrorMsg(String.format(paramString, paramVarArgs));
  }
  
  public static boolean setGlobalErrorMsg(String paramString) {
    synchronized (globalErrorLock) {
      if (globalErrorMessage.isEmpty()) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(globalErrorMessage);
        stringBuilder.append(paramString);
        globalErrorMessage = stringBuilder.toString();
        return true;
      } 
      return false;
    } 
  }
  
  public static void setGlobalErrorMsgAndThrow(RobotCoreException paramRobotCoreException, String paramString) throws RobotCoreException {
    setGlobalErrorMsg(paramRobotCoreException, paramString);
    throw paramRobotCoreException;
  }
  
  public static void setGlobalErrorMsgAndThrow(RuntimeException paramRuntimeException, String paramString) throws RobotCoreException {
    setGlobalErrorMsg(paramRuntimeException, paramString);
    throw paramRuntimeException;
  }
  
  public static void setGlobalErrorMsgSticky(boolean paramBoolean) {
    globalErrorMsgSticky = paramBoolean;
  }
  
  @Deprecated
  public static void setGlobalWarningMessage(String paramString) {
    addGlobalWarningMessage(paramString);
  }
  
  public static void setGlobalWarningMessage(String paramString, Object... paramVarArgs) {
    setGlobalWarningMessage(String.format(paramString, paramVarArgs));
  }
  
  public static void setGlobalWarningMsg(RobotCoreException paramRobotCoreException, String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(paramString);
    stringBuilder.append(": ");
    stringBuilder.append(paramRobotCoreException.getMessage());
    setGlobalWarningMessage(stringBuilder.toString());
  }
  
  public static void setGlobalWarningMsgSticky(boolean paramBoolean) {
    globalWarningMsgSticky = paramBoolean;
  }
  
  public static void setMsTimeOffset(double paramDouble) {
    msTimeOffset = paramDouble;
  }
  
  public static void startMatchLogging(Context paramContext, String paramString, int paramInt) throws RobotCoreException {
    matchStartTime = Calendar.getInstance();
    matchLogFilename = getMatchLogFilename(paramContext, paramString, paramInt);
    ii("RobotCore", String.format("******************** START - OPMODE %s ********************", new Object[] { paramString }));
  }
  
  public static void stopMatchLogging() {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/RobotLog
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/RobotLog.matchStartTime : Ljava/util/Calendar;
    //   6: ifnull -> 32
    //   9: ldc 'RobotCore'
    //   11: ldc '******************** STOP - OPMODE %s ********************'
    //   13: iconst_1
    //   14: anewarray java/lang/Object
    //   17: dup
    //   18: iconst_0
    //   19: getstatic com/qualcomm/robotcore/util/RobotLog.matchLogFilename : Ljava/lang/String;
    //   22: aastore
    //   23: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
    //   26: invokestatic ii : (Ljava/lang/String;Ljava/lang/String;)V
    //   29: invokestatic logMatch : ()V
    //   32: aconst_null
    //   33: putstatic com/qualcomm/robotcore/util/RobotLog.matchStartTime : Ljava/util/Calendar;
    //   36: ldc com/qualcomm/robotcore/util/RobotLog
    //   38: monitorexit
    //   39: return
    //   40: astore_0
    //   41: ldc com/qualcomm/robotcore/util/RobotLog
    //   43: monitorexit
    //   44: aload_0
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   3	32	40	finally
    //   32	36	40	finally
  }
  
  public static void unregisterGlobalWarningSource(GlobalWarningSource paramGlobalWarningSource) {
    synchronized (globalWarningLock) {
      globalWarningSources.remove(paramGlobalWarningSource);
      return;
    } 
  }
  
  public static void v(String paramString) {
    internalLog(2, "RobotCore", paramString);
  }
  
  public static void v(String paramString, Object... paramVarArgs) {
    v(String.format(paramString, paramVarArgs));
  }
  
  public static void vv(String paramString1, String paramString2) {
    internalLog(2, paramString1, paramString2);
  }
  
  public static void vv(String paramString1, String paramString2, Object... paramVarArgs) {
    vv(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void vv(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(2, paramString1, paramThrowable, paramString2);
  }
  
  public static void vv(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    vv(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public static void w(String paramString) {
    internalLog(5, "RobotCore", paramString);
  }
  
  public static void w(String paramString, Object... paramVarArgs) {
    w(String.format(paramString, paramVarArgs));
  }
  
  protected static void writeLogcatToDisk(Context paramContext, int paramInt) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/RobotLog
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 15
    //   11: ldc com/qualcomm/robotcore/util/RobotLog
    //   13: monitorexit
    //   14: return
    //   15: new com/qualcomm/robotcore/util/RobotLog$1
    //   18: dup
    //   19: ldc_w 'Logging Thread'
    //   22: aload_0
    //   23: iload_1
    //   24: invokespecial <init> : (Ljava/lang/String;Landroid/content/Context;I)V
    //   27: astore_0
    //   28: aload_0
    //   29: putstatic com/qualcomm/robotcore/util/RobotLog.loggingThread : Lcom/qualcomm/robotcore/util/RobotLog$LoggingThread;
    //   32: aload_0
    //   33: invokevirtual start : ()V
    //   36: ldc com/qualcomm/robotcore/util/RobotLog
    //   38: monitorexit
    //   39: return
    //   40: astore_0
    //   41: ldc com/qualcomm/robotcore/util/RobotLog
    //   43: monitorexit
    //   44: aload_0
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	40	finally
    //   15	36	40	finally
  }
  
  public static void ww(String paramString1, String paramString2) {
    internalLog(5, paramString1, paramString2);
  }
  
  public static void ww(String paramString1, String paramString2, Object... paramVarArgs) {
    ww(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public static void ww(String paramString1, Throwable paramThrowable, String paramString2) {
    internalLog(5, paramString1, paramThrowable, paramString2);
  }
  
  public static void ww(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    ww(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  protected static class LoggingThread extends Thread {
    private RunShellCommand shell = new RunShellCommand();
    
    LoggingThread(String param1String) {
      super(param1String);
    }
    
    public void kill() {
      this.shell.commitSeppuku();
    }
    
    public void run(String param1String) {
      this.shell.run(param1String);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\RobotLog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */