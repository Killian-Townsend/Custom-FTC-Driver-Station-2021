package org.firstinspires.ftc.robotcore.internal.system;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import com.qualcomm.robotcore.util.WeakReferenceSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.external.function.Continuation;
import org.firstinspires.ftc.robotcore.external.function.ContinuationResult;
import org.firstinspires.ftc.robotcore.internal.collections.MutableReference;
import org.firstinspires.ftc.robotcore.internal.files.MediaTransferProtocolMonitor;
import org.firstinspires.ftc.robotcore.internal.network.CallbackLooper;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.RobotCoreCommandList;
import org.firstinspires.ftc.robotcore.internal.ui.ProgressParameters;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.FtcWebSocketMessage;
import org.firstinspires.ftc.robotcore.internal.webserver.websockets.WebSocketManager;

public class AppUtil {
  public static final String BLOCKS_BLK_EXT = ".blk";
  
  public static final String BLOCKS_JS_EXT = ".js";
  
  public static final File BLOCKS_SOUNDS_DIR;
  
  public static final File BLOCK_OPMODES_DIR;
  
  public static final File CONFIG_FILES_DIR;
  
  public static final String DISMISS_PROGRESS_MSG = "dismissProgress";
  
  public static final File FIRST_FOLDER;
  
  public static final File LOG_FOLDER;
  
  public static final File LYNX_FIRMWARE_UPDATE_DIR;
  
  public static final File MATCH_LOG_FOLDER;
  
  public static final int MAX_MATCH_LOGS_TO_KEEP = 5;
  
  public static final File OTA_UPDATE_DIR;
  
  public static final String PROGRESS_NAMESPACE = "progress";
  
  public static final File RC_APP_UPDATE_DIR;
  
  public static final File ROBOT_DATA_DIR;
  
  public static final File ROBOT_SETTINGS;
  
  public static final File ROOT_FOLDER = Environment.getExternalStorageDirectory();
  
  public static final String SHOW_PROGRESS_MSG = "showProgress";
  
  public static final File SOUNDS_CACHE;
  
  public static final File SOUNDS_DIR;
  
  public static final String TAG = "AppUtil";
  
  public static final File TFLITE_MODELS_DIR;
  
  public static final File UPDATES_DIR;
  
  public static final File WEBCAM_CALIBRATIONS_DIR;
  
  private Application application;
  
  private Activity currentActivity;
  
  private ProgressDialog currentProgressDialog;
  
  private Map<String, DialogContext> dialogContextMap = new ConcurrentHashMap<String, DialogContext>();
  
  private final Object dialogLock = new Object();
  
  private LifeCycleMonitor lifeCycleMonitor;
  
  private Random random;
  
  private final Lock requestPermissionLock = new ReentrantLock();
  
  private Activity rootActivity;
  
  private final Object timeLock = new Object();
  
  private String usbFileSystemRoot;
  
  private final WeakReferenceSet<UsbFileSystemRootListener> usbfsListeners = new WeakReferenceSet();
  
  private final Object usbfsRootLock = new Object();
  
  private WebSocketManager webSocketManager;
  
  static {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(ROOT_FOLDER);
    stringBuilder.append("/FIRST/");
    FIRST_FOLDER = new File(stringBuilder.toString());
    LOG_FOLDER = ROOT_FOLDER;
    stringBuilder = new StringBuilder();
    stringBuilder.append(FIRST_FOLDER);
    stringBuilder.append("/matchlogs/");
    MATCH_LOG_FOLDER = new File(stringBuilder.toString());
    CONFIG_FILES_DIR = FIRST_FOLDER;
    BLOCK_OPMODES_DIR = new File(FIRST_FOLDER, "/blocks/");
    BLOCKS_SOUNDS_DIR = new File(BLOCK_OPMODES_DIR, "/sounds/");
    ROBOT_SETTINGS = new File(FIRST_FOLDER, "/settings/");
    ROBOT_DATA_DIR = new File(FIRST_FOLDER, "/data/");
    UPDATES_DIR = new File(FIRST_FOLDER, "/updates/");
    RC_APP_UPDATE_DIR = new File(UPDATES_DIR, "/Robot Controller Application/");
    LYNX_FIRMWARE_UPDATE_DIR = new File(UPDATES_DIR, "/Expansion Hub Firmware/");
    OTA_UPDATE_DIR = new File(Environment.getExternalStorageDirectory(), "/OTA-Updates");
    SOUNDS_DIR = new File(FIRST_FOLDER, "sounds");
    SOUNDS_CACHE = new File(SOUNDS_DIR, "cache");
    stringBuilder = new StringBuilder();
    stringBuilder.append(FIRST_FOLDER);
    stringBuilder.append("/webcamcalibrations/");
    WEBCAM_CALIBRATIONS_DIR = new File(stringBuilder.toString());
    stringBuilder = new StringBuilder();
    stringBuilder.append(FIRST_FOLDER);
    stringBuilder.append("/tflitemodels/");
    TFLITE_MODELS_DIR = new File(stringBuilder.toString());
    System.loadLibrary("RobotCore");
  }
  
  public static int getColor(int paramInt) {
    return getDefContext().getColor(paramInt);
  }
  
  public static Context getDefContext() {
    return (Context)getInstance().getApplication();
  }
  
  public static AppUtil getInstance() {
    return InstanceHolder.theInstance;
  }
  
  private void initializeRootActivityIfNecessary() {
    if (this.rootActivity == null) {
      Activity activity = this.currentActivity;
      this.rootActivity = activity;
      RobotLog.vv("AppUtil", "rootActivity=%s", new Object[] { activity.getClass().getSimpleName() });
    } 
  }
  
  private native boolean nativeSetCurrentTimeMillis(long paramLong);
  
  public static void onApplicationStart(Application paramApplication) {
    getInstance().initialize(paramApplication);
  }
  
  protected static String usbFileSystemRootFromDeviceName(String paramString) {
    String[] arrayOfString;
    if (TextUtils.isEmpty(paramString)) {
      paramString = null;
    } else {
      arrayOfString = paramString.split("/");
    } 
    if (arrayOfString != null && arrayOfString.length > 2) {
      StringBuilder stringBuilder = new StringBuilder(arrayOfString[0]);
      for (int i = 1; i < arrayOfString.length - 2; i++) {
        stringBuilder.append("/");
        stringBuilder.append(arrayOfString[i]);
      } 
      return stringBuilder.toString();
    } 
    return null;
  }
  
  public static ResultReceiver wrapResultReceiverForIpc(ResultReceiver paramResultReceiver) {
    Parcel parcel = Parcel.obtain();
    paramResultReceiver.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    paramResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
    parcel.recycle();
    return paramResultReceiver;
  }
  
  public void addUsbfsListener(UsbFileSystemRootListener paramUsbFileSystemRootListener) {
    synchronized (this.usbfsListeners) {
      this.usbfsListeners.add(paramUsbFileSystemRootListener);
      return;
    } 
  }
  
  public void asyncRequestUsbPermission(String paramString, Context paramContext, UsbDevice paramUsbDevice, Deadline paramDeadline, Handler paramHandler, Consumer<Boolean> paramConsumer) {
    asyncRequestUsbPermission(paramString, paramContext, paramUsbDevice, paramDeadline, Continuation.create(paramHandler, paramConsumer));
  }
  
  public void asyncRequestUsbPermission(String paramString, Context paramContext, UsbDevice paramUsbDevice, Deadline paramDeadline, ExecutorService paramExecutorService, Consumer<Boolean> paramConsumer) {
    asyncRequestUsbPermission(paramString, paramContext, paramUsbDevice, paramDeadline, Continuation.create(paramExecutorService, paramConsumer));
  }
  
  public void asyncRequestUsbPermission(final String tag, final Context modalContext, final UsbDevice usbDevice, final Deadline deadline, final Continuation<? extends Consumer<Boolean>> continuation) {
    RobotLog.vv(tag, "asyncRequestUsbPermission()...");
    try {
      Assert.assertFalse(CallbackLooper.isLooperThread());
      final UsbManager usbManager = (UsbManager)modalContext.getSystemService("usb");
      if (usbManager.hasPermission(usbDevice)) {
        RobotLog.dd(tag, "permission already available for %s", new Object[] { usbDevice.getDeviceName() });
        continuation.dispatch(new ContinuationResult<Consumer<Boolean>>() {
              public void handle(Consumer<Boolean> param1Consumer) {
                param1Consumer.accept(Boolean.valueOf(true));
              }
            });
      } else {
        final MutableReference result = new MutableReference(Boolean.valueOf(false));
        final Runnable runnable = new Runnable() {
            public void run() {
              try {
                boolean bool = deadline.tryLock(AppUtil.this.requestPermissionLock);
                if (bool) {
                  try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("org.firstinspires.ftc.USB_PERMISSION_REQUEST:");
                    stringBuilder.append(usbDevice.getDeviceName());
                    final String actionUsbPermissionRequest = stringBuilder.toString();
                    null = new CountDownLatch(1);
                    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
                        public void onReceive(Context param2Context, Intent param2Intent) {
                          if (param2Intent.getAction().equals(actionUsbPermissionRequest)) {
                            UsbDevice usbDevice = (UsbDevice)param2Intent.getParcelableExtra("device");
                            if (usbDevice != null && usbDevice.getDeviceName().equals(usbDevice.getDeviceName())) {
                              result.setValue(Boolean.valueOf(param2Intent.getBooleanExtra("permission", false)));
                              if (!((Boolean)result.getValue()).booleanValue())
                                RobotLog.vv(tag, "requestPermission(%s): user declined permission", new Object[] { usbDevice.getDeviceName() }); 
                              RobotLog.vv(tag, "releasing permissionResultAvailable latch");
                              permissionResultAvailable.countDown();
                              return;
                            } 
                            RobotLog.ee(tag, "unexpected permission request response");
                          } 
                        }
                      };
                    IntentFilter intentFilter = new IntentFilter(str);
                    modalContext.registerReceiver(broadcastReceiver, intentFilter, null, CallbackLooper.getDefault().getHandler());
                    try {
                      PendingIntent pendingIntent = PendingIntent.getBroadcast(modalContext, 0, new Intent(str), 1073741824);
                      usbManager.requestPermission(usbDevice, pendingIntent);
                      if (deadline.await(null)) {
                        RobotLog.vv(tag, "permissionResultAvailable latch awaited");
                      } else {
                        RobotLog.vv(tag, "requestPermission(): cancelled or timed out waiting for user response");
                        pendingIntent.cancel();
                      } 
                    } catch (InterruptedException interruptedException) {
                    
                    } finally {
                      modalContext.unregisterReceiver(broadcastReceiver);
                    } 
                    stringBuilder.unregisterReceiver(broadcastReceiver);
                  } finally {
                    AppUtil.this.requestPermissionLock.unlock();
                  } 
                } else {
                  RobotLog.vv(tag, "requestPermission(): requestPermissionLock.tryLock() returned false");
                } 
                return;
              } catch (InterruptedException interruptedException) {
                Thread.currentThread().interrupt();
                return;
              } finally {
                RobotLog.vv(tag, "USB permission request for %s: result=%s", new Object[] { this.val$usbDevice.getDeviceName(), this.val$result.getValue() });
              } 
            }
          };
        if (continuation.isHandler()) {
          ThreadPool.getDefault().submit(new Runnable() {
                public void run() {
                  runnable.run();
                  continuation.dispatch(new ContinuationResult<Consumer<Boolean>>() {
                        public void handle(Consumer<Boolean> param2Consumer) {
                          param2Consumer.accept(result.getValue());
                        }
                      });
                }
              });
        } else {
          continuation.createForNewTarget(null).dispatch(new ContinuationResult<Void>() {
                public void handle(Void param1Void) {
                  runnable.run();
                  continuation.dispatchHere(new ContinuationResult<Consumer<Boolean>>() {
                        public void handle(Consumer<Boolean> param2Consumer) {
                          param2Consumer.accept(result.getValue());
                        }
                      });
                }
              });
        } 
      } 
      return;
    } finally {
      RobotLog.vv(tag, "...asyncRequestUsbPermission()");
    } 
  }
  
  public void copyFile(File paramFile1, File paramFile2) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(paramFile1);
    try {
      copyStream(fileInputStream, paramFile2);
      return;
    } finally {
      fileInputStream.close();
    } 
  }
  
  public void copyStream(File paramFile, OutputStream paramOutputStream) throws IOException {
    FileInputStream fileInputStream = new FileInputStream(paramFile);
    try {
      copyStream(fileInputStream, paramOutputStream);
      return;
    } finally {
      fileInputStream.close();
    } 
  }
  
  public void copyStream(InputStream paramInputStream, File paramFile) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(paramFile);
    try {
      copyStream(paramInputStream, fileOutputStream);
      return;
    } finally {
      fileOutputStream.close();
    } 
  }
  
  public void copyStream(InputStream paramInputStream, OutputStream paramOutputStream) throws IOException {
    byte[] arrayOfByte = new byte[Math.min(4096, paramInputStream.available())];
    while (true) {
      int i = paramInputStream.read(arrayOfByte);
      if (i <= 0)
        return; 
      paramOutputStream.write(arrayOfByte, 0, i);
    } 
  }
  
  public File createTempDirectory(String paramString1, String paramString2, File paramFile) throws IOException {
    if (paramString1.length() >= 3) {
      String str = paramString2;
      if (paramString2 == null)
        str = ".tmp"; 
      File file = paramFile;
      if (paramFile == null)
        file = new File(System.getProperty("java.io.tmpdir", ".")); 
      while (true) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(paramString1);
        stringBuilder.append(this.random.nextInt());
        stringBuilder.append(str);
        File file1 = new File(file, stringBuilder.toString());
        if (file1.mkdir())
          return file1; 
      } 
    } 
    throw new IllegalArgumentException("prefix must be at least 3 characters");
  }
  
  public File createTempFile(String paramString1, String paramString2, File paramFile) throws IOException {
    return File.createTempFile(paramString1, paramString2, paramFile);
  }
  
  public void delete(File paramFile) {
    deleteChildren(paramFile);
    if (!paramFile.delete())
      RobotLog.ee("AppUtil", "failed to delete '%s'", new Object[] { paramFile.getAbsolutePath() }); 
  }
  
  public void deleteChildren(File paramFile) {
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null) {
      int j = arrayOfFile.length;
      for (int i = 0; i < j; i++)
        delete(arrayOfFile[i]); 
    } 
  }
  
  public void dismissAllDialogs(UILocation paramUILocation) {
    Iterator<?> iterator = (new ArrayList(this.dialogContextMap.keySet())).iterator();
    while (iterator.hasNext())
      dismissDialog((String)iterator.next()); 
    if (paramUILocation == UILocation.BOTH)
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_DISMISS_ALL_DIALOGS")); 
  }
  
  protected void dismissDialog(final String uuidString) {
    runOnUiThread(new Runnable() {
          public void run() {
            AppUtil.DialogContext dialogContext = (AppUtil.DialogContext)AppUtil.this.dialogContextMap.remove(uuidString);
            if (dialogContext != null) {
              dialogContext.isArmed = false;
              dialogContext.dialog.dismiss();
            } 
          }
        });
  }
  
  public void dismissDialog(UILocation paramUILocation, RobotCoreCommandList.DismissDialog paramDismissDialog) {
    dismissDialog(paramDismissDialog.uuidString);
    if (paramUILocation == UILocation.BOTH)
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_DISMISS_DIALOG", paramDismissDialog.serialize())); 
  }
  
  public void dismissProgress(UILocation paramUILocation) {
    runOnUiThread(new Runnable() {
          public void run() {
            if (AppUtil.this.currentProgressDialog != null) {
              AppUtil.this.currentProgressDialog.dismiss();
              AppUtil.access$102(AppUtil.this, null);
            } 
          }
        });
    WebSocketManager webSocketManager = this.webSocketManager;
    if (webSocketManager != null)
      webSocketManager.broadcastToNamespace("progress", new FtcWebSocketMessage("progress", "dismissProgress")); 
    if (paramUILocation == UILocation.BOTH)
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_DISMISS_PROGRESS")); 
  }
  
  public void ensureDirectoryExists(File paramFile) {
    ensureDirectoryExists(paramFile, true);
  }
  
  public void ensureDirectoryExists(File paramFile, boolean paramBoolean) {
    if (!paramFile.isDirectory()) {
      paramFile.delete();
      File file = paramFile.getParentFile();
      if (file != null)
        ensureDirectoryExists(file, paramBoolean); 
      if (paramFile.mkdir()) {
        if (paramBoolean) {
          MediaTransferProtocolMonitor.makeIndicatorFile(paramFile);
          return;
        } 
      } else {
        if (!paramFile.isDirectory())
          RobotLog.ee("AppUtil", "failed to create directory %s", new Object[] { paramFile }); 
        if (paramBoolean)
          MediaTransferProtocolMonitor.renoticeIndicatorFiles(paramFile); 
      } 
    } 
  }
  
  public void exitApplication() {
    exitApplication(0);
  }
  
  public void exitApplication(int paramInt) {
    RobotLog.vv("AppUtil", "exitApplication(%d)", new Object[] { Integer.valueOf(paramInt) });
    System.exit(paramInt);
  }
  
  public RuntimeException failFast(String paramString1, String paramString2) {
    RobotLog.ee(paramString1, paramString2);
    exitApplication(-1);
    return new RuntimeException("keep compiler happy");
  }
  
  public RuntimeException failFast(String paramString1, String paramString2, Object... paramVarArgs) {
    return failFast(paramString1, String.format(paramString2, paramVarArgs));
  }
  
  public RuntimeException failFast(String paramString1, Throwable paramThrowable, String paramString2) {
    RobotLog.ee(paramString1, paramThrowable, paramString2);
    exitApplication(-1);
    return new RuntimeException("keep compiler happy", paramThrowable);
  }
  
  public RuntimeException failFast(String paramString1, Throwable paramThrowable, String paramString2, Object... paramVarArgs) {
    return failFast(paramString1, paramThrowable, String.format(paramString2, paramVarArgs));
  }
  
  public List<File> filesIn(File paramFile) {
    return filesIn(paramFile, (Predicate<File>)null);
  }
  
  public List<File> filesIn(File paramFile, final String extension) {
    return filesIn(paramFile, new Predicate<File>() {
          public boolean test(File param1File) {
            return param1File.getName().endsWith(extension);
          }
        });
  }
  
  public List<File> filesIn(File paramFile, Predicate<File> paramPredicate) {
    ArrayList<File> arrayList = new ArrayList();
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile != null) {
      int j = arrayOfFile.length;
      for (int i = 0; i < j; i++) {
        File file = arrayOfFile[i];
        if (paramPredicate == null || paramPredicate.test(file))
          arrayList.add(file.getAbsoluteFile()); 
      } 
    } 
    return arrayList;
  }
  
  public List<File> filesUnder(File paramFile) {
    return filesUnder(paramFile, (Predicate<File>)null);
  }
  
  public List<File> filesUnder(File paramFile, final String extension) {
    return filesUnder(paramFile, new Predicate<File>() {
          public boolean test(File param1File) {
            return param1File.getName().endsWith(extension);
          }
        });
  }
  
  public List<File> filesUnder(File paramFile, Predicate<File> paramPredicate) {
    File[] arrayOfFile;
    ArrayList<File> arrayList = new ArrayList();
    if (paramFile.isDirectory()) {
      arrayOfFile = paramFile.listFiles();
      int j = arrayOfFile.length;
      for (int i = 0; i < j; i++)
        arrayList.addAll(filesUnder(arrayOfFile[i], paramPredicate)); 
    } else if (arrayOfFile.exists() && (paramPredicate == null || paramPredicate.test(arrayOfFile))) {
      arrayList.add(arrayOfFile.getAbsoluteFile());
    } 
    return arrayList;
  }
  
  public String findCaller(String paramString, int paramInt) {
    StackTraceElement stackTraceElement = (new RuntimeException()).getStackTrace()[paramInt + 2];
    String str = stackTraceElement.getClassName();
    str = str.substring(str.lastIndexOf('.') + 1);
    return Misc.formatInvariant("%s caller=[%s:%d] %s", new Object[] { paramString, (new File(stackTraceElement.getFileName())).getName(), Integer.valueOf(stackTraceElement.getLineNumber()), str });
  }
  
  public Activity getActivity() {
    return this.currentActivity;
  }
  
  public String getAppName() {
    return isRobotController() ? getDefContext().getString(R.string.appNameRobotController) : (isDriverStation() ? getDefContext().getString(R.string.appNameDriverStation) : getDefContext().getString(R.string.appNameUnknown));
  }
  
  public Application getApplication() {
    return this.application;
  }
  
  public String getApplicationId() {
    return getApplication().getPackageName();
  }
  
  public SimpleDateFormat getIso8601DateFormat() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    return simpleDateFormat;
  }
  
  public Context getModalContext() {
    Activity activity = this.currentActivity;
    return (Context)((activity != null) ? activity : getApplication());
  }
  
  public String getNonNullUsbFileSystemRoot() {
    String str2 = getUsbFileSystemRoot();
    String str1 = str2;
    if (str2 == null)
      str1 = "/dev/bus/usb"; 
    return str1;
  }
  
  public File getRelativePath(File paramFile1, File paramFile2) {
    File file2 = new File("");
    File file1 = paramFile2;
    paramFile2 = file2;
    while (!paramFile1.equals(file1)) {
      file2 = file1.getParentFile();
      paramFile2 = new File(new File(file1.getName()), paramFile2.getPath());
      if (file2 == null)
        return paramFile2; 
      file1 = file2;
    } 
    return paramFile2;
  }
  
  public String getRemoteAppName() {
    return isRobotController() ? getDefContext().getString(R.string.appNameDriverStation) : (isDriverStation() ? getDefContext().getString(R.string.appNameRobotController) : getDefContext().getString(R.string.appNameUnknown));
  }
  
  public Activity getRootActivity() {
    return this.rootActivity;
  }
  
  public File getSettingsFile(String paramString) {
    File file2 = new File(paramString);
    File file1 = file2;
    if (!file2.isAbsolute()) {
      ensureDirectoryExists(ROBOT_SETTINGS);
      file1 = new File(ROBOT_SETTINGS, paramString);
    } 
    return file1;
  }
  
  public String getUsbFileSystemRoot() {
    if (this.usbFileSystemRoot == null)
      synchronized (this.usbfsRootLock) {
        Iterator<String> iterator = ((UsbManager)getDefContext().getSystemService("usb")).getDeviceList().keySet().iterator();
        while (iterator.hasNext()) {
          String str = usbFileSystemRootFromDeviceName(iterator.next());
          if (str != null) {
            setUsbFileSystemRoot(str);
            break;
          } 
        } 
      }  
    return this.usbFileSystemRoot;
  }
  
  public long getWallClockTime() {
    return System.currentTimeMillis();
  }
  
  protected void initialize(Application paramApplication) {
    this.lifeCycleMonitor = new LifeCycleMonitor();
    this.rootActivity = null;
    this.currentActivity = null;
    this.currentProgressDialog = null;
    this.random = new Random();
    paramApplication.registerActivityLifecycleCallbacks(this.lifeCycleMonitor);
    this.application = paramApplication;
    RobotLog.vv("AppUtil", "initializing: getExternalStorageDirectory()=%s", new Object[] { Environment.getExternalStorageDirectory() });
    this.usbFileSystemRoot = null;
    getUsbFileSystemRoot();
  }
  
  public boolean isDriverStation() {
    return getApplicationId().equals(getDefContext().getString(R.string.packageNameDriverStation));
  }
  
  public boolean isRobotController() {
    return getApplicationId().equals(getDefContext().getString(R.string.packageNameRobotController));
  }
  
  public boolean isSaneWalkClockTime(long paramLong) {
    GregorianCalendar gregorianCalendar = new GregorianCalendar();
    gregorianCalendar.setTimeInMillis(paramLong);
    return (gregorianCalendar.get(1) > 1975);
  }
  
  protected void notifyUsbListeners(String paramString) {
    WeakReferenceSet<UsbFileSystemRootListener> weakReferenceSet;
    Iterator<UsbFileSystemRootListener> iterator;
    synchronized (this.usbfsListeners) {
      ArrayList<UsbFileSystemRootListener> arrayList = new ArrayList<UsbFileSystemRootListener>((Collection<? extends UsbFileSystemRootListener>)this.usbfsListeners);
      iterator = arrayList.iterator();
      while (iterator.hasNext())
        ((UsbFileSystemRootListener)iterator.next()).onUsbFileSystemRootChanged(paramString); 
      return;
    } 
  }
  
  public void removeUsbfsListener(UsbFileSystemRootListener paramUsbFileSystemRootListener) {
    synchronized (this.usbfsListeners) {
      this.usbfsListeners.remove(paramUsbFileSystemRootListener);
      return;
    } 
  }
  
  public void restartApp(int paramInt) {
    RobotLog.vv("AppUtil", "restarting app");
    PendingIntent pendingIntent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(this.rootActivity.getIntent()), this.rootActivity.getIntent().getFlags());
    ((AlarmManager)this.rootActivity.getSystemService("alarm")).set(1, System.currentTimeMillis() + 1500L, pendingIntent);
    System.exit(paramInt);
  }
  
  public void runOnUiThread(Activity paramActivity, Runnable paramRunnable) {
    paramActivity.runOnUiThread(paramRunnable);
  }
  
  public void runOnUiThread(Runnable paramRunnable) {
    runOnUiThread(getActivity(), paramRunnable);
  }
  
  public void setTimeZone(String paramString) {
    if (!LynxConstants.isRevControlHub())
      return; 
    TimeZone timeZone = TimeZone.getDefault();
    AlarmManager alarmManager = (AlarmManager)getDefContext().getSystemService("alarm");
    try {
      alarmManager.setTimeZone(paramString);
      TimeZone.setDefault(null);
      TimeZone timeZone1 = TimeZone.getDefault();
      RobotLog.vv("AppUtil", "attempted to set timezone: before=%s after=%s", new Object[] { timeZone.getID(), timeZone1.getID() });
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      RobotLog.ee("AppUtil", "Attempted to set invalid timezone: %s", new Object[] { timeZone.getID() });
      return;
    } 
  }
  
  public void setUsbFileSystemRoot(UsbDevice paramUsbDevice) {
    setUsbFileSystemRoot(usbFileSystemRootFromDeviceName(paramUsbDevice.getDeviceName()));
  }
  
  protected void setUsbFileSystemRoot(String paramString) {
    if (paramString != null && this.usbFileSystemRoot == null)
      synchronized (this.usbfsRootLock) {
        if (this.usbFileSystemRoot == null) {
          RobotLog.ii("AppUtil", "found usbFileSystemRoot: %s", new Object[] { paramString });
          this.usbFileSystemRoot = paramString;
          notifyUsbListeners(paramString);
        } 
        return;
      }  
  }
  
  public void setWallClockIfCurrentlyInsane(long paramLong, String paramString) {
    synchronized (this.timeLock) {
      boolean bool;
      boolean bool1 = isSaneWalkClockTime(getWallClockTime());
      if (paramString != null && !paramString.isEmpty()) {
        bool = true;
      } else {
        bool = false;
      } 
      if (!bool1 && isSaneWalkClockTime(paramLong) && bool) {
        setWallClockTime(paramLong);
        setTimeZone(paramString);
      } 
      return;
    } 
  }
  
  public void setWallClockTime(long paramLong) {
    nativeSetCurrentTimeMillis(paramLong);
  }
  
  public void setWebSocketManager(WebSocketManager paramWebSocketManager) {
    this.webSocketManager = paramWebSocketManager;
    paramWebSocketManager.registerNamespaceAsBroadcastOnly("progress");
  }
  
  public DialogContext showAlertDialog(UILocation paramUILocation, String paramString1, String paramString2) {
    return showDialog(new DialogParams(paramUILocation, paramString1, paramString2));
  }
  
  public DialogContext showDialog(DialogParams paramDialogParams) {
    return showDialog(paramDialogParams, (Continuation<? extends Consumer<DialogContext>>)null);
  }
  
  public DialogContext showDialog(DialogParams paramDialogParams, Handler paramHandler, Consumer<DialogContext> paramConsumer) {
    if (paramConsumer != null) {
      Continuation continuation = Continuation.create(paramHandler, paramConsumer);
    } else {
      paramHandler = null;
    } 
    return showDialog(paramDialogParams, (Continuation<? extends Consumer<DialogContext>>)paramHandler);
  }
  
  public DialogContext showDialog(DialogParams paramDialogParams, Executor paramExecutor, Consumer<DialogContext> paramConsumer) {
    if (paramConsumer != null) {
      Continuation continuation = Continuation.create(paramExecutor, paramConsumer);
    } else {
      paramExecutor = null;
    } 
    return showDialog(paramDialogParams, (Continuation<? extends Consumer<DialogContext>>)paramExecutor);
  }
  
  public DialogContext showDialog(DialogParams paramDialogParams, Consumer<DialogContext> paramConsumer) {
    return showDialog(paramDialogParams, (Handler)null, paramConsumer);
  }
  
  public DialogContext showDialog(DialogParams paramDialogParams, final Continuation<? extends Consumer<DialogContext>> runOnDismiss) {
    synchronized (this.dialogLock) {
      String str;
      final DialogParams paramsCopy = paramDialogParams.copy();
      final RobotCoreCommandList.ShowDialog showDialog = new RobotCoreCommandList.ShowDialog();
      showDialog.title = dialogParams.title;
      showDialog.message = dialogParams.message;
      if (dialogParams.uuidString != null) {
        str = dialogParams.uuidString;
      } else {
        str = UUID.randomUUID().toString();
      } 
      showDialog.uuidString = str;
      final MutableReference result = new MutableReference();
      synchronousRunOnUiThread(new Runnable() {
            public void run() {
              final AppUtil.DialogContext dialogContext = new AppUtil.DialogContext(showDialog.uuidString);
              AlertDialog.Builder builder = new AlertDialog.Builder((Context)paramsCopy.activity);
              builder.setTitle(paramsCopy.title);
              builder.setMessage(paramsCopy.message);
              int i = AppUtil.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$system$AppUtil$DialogFlavor[paramsCopy.flavor.ordinal()];
              if (i != 1) {
                if (i != 2) {
                  if (i != 3) {
                    dialogContext.dialog = builder.create();
                    dialogContext.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                          public void onShow(DialogInterface param2DialogInterface) {
                            RobotLog.vv("AppUtil", "dialog shown: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                          }
                        });
                    dialogContext.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                          public void onCancel(DialogInterface param2DialogInterface) {
                            RobotLog.vv("AppUtil", "dialog cancelled: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                            dialogContext.outcome = AppUtil.DialogContext.Outcome.CANCELLED;
                          }
                        });
                    dialogContext.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                          public void onDismiss(DialogInterface param2DialogInterface) {
                            RobotLog.vv("AppUtil", "dialog dismissed: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                            dialogContext.dismissed.countDown();
                            if (runOnDismiss != null)
                              runOnDismiss.dispatch(new ContinuationResult<Consumer<AppUtil.DialogContext>>() {
                                    public void handle(Consumer<AppUtil.DialogContext> param3Consumer) {
                                      param3Consumer.accept(dialogContext);
                                    }
                                  }); 
                            if (dialogContext.isArmed) {
                              RobotCoreCommandList.DismissDialog dismissDialog = new RobotCoreCommandList.DismissDialog(showDialog.uuidString);
                              AppUtil.this.dismissDialog(UILocation.BOTH, dismissDialog);
                            } 
                          }
                        });
                    AppUtil.this.dialogContextMap.put(dialogContext.uuidString, dialogContext);
                    result.setValue(dialogContext);
                    dialogContext.dialog.show();
                    return;
                  } 
                } else {
                  dialogContext.input = new EditText((Context)paramsCopy.activity);
                  dialogContext.input.setInputType(1);
                  if (paramsCopy.defaultValue != null)
                    dialogContext.input.setText(paramsCopy.defaultValue); 
                  builder.setView((View)dialogContext.input);
                } 
                if (paramsCopy.uiLocation == UILocation.ONLY_LOCAL) {
                  builder.setPositiveButton(R.string.buttonNameOK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                          RobotLog.vv("AppUtil", "dialog OK clicked: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                          dialogContext.outcome = AppUtil.DialogContext.Outcome.CONFIRMED;
                          if (dialogContext.input != null) {
                            AppUtil.DialogContext dialogContext = dialogContext;
                            dialogContext.textResult = (CharSequence)dialogContext.input.getText();
                          } 
                          param2DialogInterface.dismiss();
                        }
                      });
                  builder.setNegativeButton(R.string.buttonNameCancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                          RobotLog.vv("AppUtil", "dialog cancel clicked: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                          param2DialogInterface.cancel();
                        }
                      });
                } else {
                  throw new IllegalArgumentException("remote confirmation dialogs not yet supported");
                } 
              } else {
                builder.setNeutralButton(R.string.buttonNameOK, new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        dialogContext.outcome = AppUtil.DialogContext.Outcome.CONFIRMED;
                      }
                    });
              } 
              dialogContext.dialog = builder.create();
              dialogContext.dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface param2DialogInterface) {
                      RobotLog.vv("AppUtil", "dialog shown: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                    }
                  });
              dialogContext.dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface param2DialogInterface) {
                      RobotLog.vv("AppUtil", "dialog cancelled: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                      dialogContext.outcome = AppUtil.DialogContext.Outcome.CANCELLED;
                    }
                  });
              dialogContext.dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    public void onDismiss(DialogInterface param2DialogInterface) {
                      RobotLog.vv("AppUtil", "dialog dismissed: uuid=%s", new Object[] { this.val$dialogContext.uuidString });
                      dialogContext.dismissed.countDown();
                      if (runOnDismiss != null)
                        runOnDismiss.dispatch(new ContinuationResult<Consumer<AppUtil.DialogContext>>() {
                              public void handle(Consumer<AppUtil.DialogContext> param3Consumer) {
                                param3Consumer.accept(dialogContext);
                              }
                            }); 
                      if (dialogContext.isArmed) {
                        RobotCoreCommandList.DismissDialog dismissDialog = new RobotCoreCommandList.DismissDialog(showDialog.uuidString);
                        AppUtil.this.dismissDialog(UILocation.BOTH, dismissDialog);
                      } 
                    }
                  });
              AppUtil.this.dialogContextMap.put(dialogContext.uuidString, dialogContext);
              result.setValue(dialogContext);
              dialogContext.dialog.show();
            }
          });
      Assert.assertNotNull(mutableReference.getValue());
      if (dialogParams.uiLocation == UILocation.BOTH)
        NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_SHOW_DIALOG", showDialog.serialize())); 
      return (DialogContext)mutableReference.getValue();
    } 
  }
  
  public void showProgress(UILocation paramUILocation, final Activity activity, final String message, final ProgressParameters progressParameters) {
    runOnUiThread(new Runnable() {
          public void run() {
            if (AppUtil.this.currentProgressDialog == null) {
              AppUtil.access$102(AppUtil.this, new ProgressDialog((Context)activity));
              AppUtil.this.currentProgressDialog.setMessage(message);
              AppUtil.this.currentProgressDialog.setProgressStyle(1);
              AppUtil.this.currentProgressDialog.setMax(cappedMax);
              AppUtil.this.currentProgressDialog.setProgress(0);
              AppUtil.this.currentProgressDialog.setCanceledOnTouchOutside(false);
              AppUtil.this.currentProgressDialog.show();
            } 
            if (progressParameters.cur == 0) {
              AppUtil.this.currentProgressDialog.setIndeterminate(true);
              return;
            } 
            AppUtil.this.currentProgressDialog.setIndeterminate(false);
            AppUtil.this.currentProgressDialog.setProgress(progressParameters.cur);
          }
        });
    RobotCoreCommandList.ShowProgress showProgress = new RobotCoreCommandList.ShowProgress();
    showProgress.message = message;
    showProgress.cur = progressParameters.cur;
    showProgress.max = progressParameters.max;
    WebSocketManager webSocketManager = this.webSocketManager;
    if (webSocketManager != null)
      webSocketManager.broadcastToNamespace("progress", new FtcWebSocketMessage("progress", "showProgress", showProgress.serialize())); 
    if (paramUILocation == UILocation.BOTH)
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_SHOW_PROGRESS", showProgress.serialize())); 
  }
  
  public void showProgress(UILocation paramUILocation, String paramString, double paramDouble) {
    showProgress(paramUILocation, paramString, ProgressParameters.fromFraction(paramDouble));
  }
  
  public void showProgress(UILocation paramUILocation, String paramString, double paramDouble, int paramInt) {
    showProgress(paramUILocation, paramString, ProgressParameters.fromFraction(paramDouble, paramInt));
  }
  
  public void showProgress(UILocation paramUILocation, String paramString, ProgressParameters paramProgressParameters) {
    showProgress(paramUILocation, getActivity(), paramString, paramProgressParameters);
  }
  
  @Deprecated
  public void showToast(UILocation paramUILocation, Activity paramActivity, Context paramContext, String paramString) {
    showToast(paramUILocation, paramString);
  }
  
  @Deprecated
  public void showToast(UILocation paramUILocation, Activity paramActivity, Context paramContext, String paramString, int paramInt) {
    showToast(paramUILocation, paramString, paramInt);
  }
  
  @Deprecated
  public void showToast(UILocation paramUILocation, Context paramContext, String paramString) {
    showToast(paramUILocation, paramString);
  }
  
  public void showToast(UILocation paramUILocation, String paramString) {
    showToast(paramUILocation, paramString, 0);
  }
  
  public void showToast(UILocation paramUILocation, final String msg, final int duration) {
    (new Handler(Looper.getMainLooper())).post(new Runnable() {
          public void run() {
            Toast toast = Toast.makeText(AppUtil.getDefContext(), msg, duration);
            ((TextView)toast.getView().findViewById(16908299)).setTextSize(18.0F);
            toast.show();
          }
        });
    if (paramUILocation == UILocation.BOTH) {
      RobotCoreCommandList.ShowToast showToast = new RobotCoreCommandList.ShowToast();
      showToast.message = msg;
      showToast.duration = duration;
      NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_SHOW_TOAST", showToast.serialize()));
    } 
  }
  
  public void showWaitCursor(String paramString, Runnable paramRunnable) {
    showWaitCursor(paramString, paramRunnable, null);
  }
  
  public void showWaitCursor(final String message, final Runnable backgroundWorker, final Runnable runPostOnUIThread) {
    runOnUiThread(new Runnable() {
          public void run() {
            (new AsyncTask<Object, Void, Void>() {
                ProgressDialog dialog;
                
                protected Void doInBackground(Object... param2VarArgs) {
                  backgroundWorker.run();
                  return null;
                }
                
                protected void onPostExecute(Void param2Void) {
                  this.dialog.dismiss();
                  if (runPostOnUIThread != null)
                    runPostOnUIThread.run(); 
                }
                
                protected void onPreExecute() {
                  ProgressDialog progressDialog = new ProgressDialog((Context)AppUtil.this.getActivity());
                  this.dialog = progressDialog;
                  progressDialog.setMessage(message);
                  this.dialog.setIndeterminate(true);
                  this.dialog.setCancelable(false);
                  this.dialog.show();
                }
              }).execute(new Object[0]);
          }
        });
  }
  
  public void synchronousRunOnUiThread(Activity paramActivity, final Runnable action) {
    try {
      final CountDownLatch uiDone = new CountDownLatch(1);
      paramActivity.runOnUiThread(new Runnable() {
            public void run() {
              action.run();
              uiDone.countDown();
            }
          });
      countDownLatch.await();
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public void synchronousRunOnUiThread(Runnable paramRunnable) {
    synchronousRunOnUiThread(getActivity(), paramRunnable);
  }
  
  public RuntimeException unreachable() {
    return unreachable("AppUtil");
  }
  
  public RuntimeException unreachable(String paramString) {
    return failFast(paramString, "internal error: this code is unreachable");
  }
  
  public RuntimeException unreachable(String paramString, Throwable paramThrowable) {
    return failFast(paramString, paramThrowable, "internal error: this code is unreachable");
  }
  
  public RuntimeException unreachable(Throwable paramThrowable) {
    return unreachable("AppUtil", paramThrowable);
  }
  
  public static class DialogContext {
    protected AlertDialog dialog;
    
    public final CountDownLatch dismissed = new CountDownLatch(1);
    
    protected EditText input = null;
    
    protected boolean isArmed = true;
    
    protected Outcome outcome = Outcome.UNKNOWN;
    
    protected CharSequence textResult = null;
    
    protected final String uuidString;
    
    public DialogContext(String param1String) {
      this.uuidString = param1String;
    }
    
    public void dismissDialog() {
      AppUtil.getInstance().runOnUiThread(new Runnable() {
            public void run() {
              AlertDialog alertDialog = AppUtil.DialogContext.this.dialog;
              if (alertDialog != null)
                alertDialog.dismiss(); 
            }
          });
    }
    
    public Outcome getOutcome() {
      return this.outcome;
    }
    
    public CharSequence getText() {
      return this.textResult;
    }
    
    public enum Outcome {
      CANCELLED, CONFIRMED, UNKNOWN;
      
      static {
        Outcome outcome = new Outcome("CONFIRMED", 2);
        CONFIRMED = outcome;
        $VALUES = new Outcome[] { UNKNOWN, CANCELLED, outcome };
      }
    }
  }
  
  class null implements Runnable {
    public void run() {
      AlertDialog alertDialog = this.this$0.dialog;
      if (alertDialog != null)
        alertDialog.dismiss(); 
    }
  }
  
  public enum Outcome {
    CANCELLED, CONFIRMED, UNKNOWN;
    
    static {
      Outcome outcome = new Outcome("CONFIRMED", 2);
      CONFIRMED = outcome;
      $VALUES = new Outcome[] { UNKNOWN, CANCELLED, outcome };
    }
  }
  
  public enum DialogFlavor {
    ALERT, CONFIRM, PROMPT;
    
    static {
      DialogFlavor dialogFlavor = new DialogFlavor("PROMPT", 2);
      PROMPT = dialogFlavor;
      $VALUES = new DialogFlavor[] { ALERT, CONFIRM, dialogFlavor };
    }
  }
  
  public static class DialogParams extends MemberwiseCloneable<DialogParams> {
    public Activity activity = AppUtil.getInstance().getActivity();
    
    public String defaultValue = null;
    
    public AppUtil.DialogFlavor flavor = AppUtil.DialogFlavor.ALERT;
    
    public String message;
    
    public String title;
    
    public UILocation uiLocation;
    
    public String uuidString = null;
    
    public DialogParams(UILocation param1UILocation, String param1String1, String param1String2) {
      this.uiLocation = param1UILocation;
      this.title = param1String1;
      this.message = param1String2;
    }
    
    public DialogParams copy() {
      return (DialogParams)memberwiseClone();
    }
  }
  
  private static class InstanceHolder {
    public static AppUtil theInstance = new AppUtil();
  }
  
  private class LifeCycleMonitor implements Application.ActivityLifecycleCallbacks {
    private LifeCycleMonitor() {}
    
    public void onActivityCreated(Activity param1Activity, Bundle param1Bundle) {
      AppUtil.access$402(AppUtil.this, param1Activity);
      AppUtil.this.initializeRootActivityIfNecessary();
    }
    
    public void onActivityDestroyed(Activity param1Activity) {
      if (param1Activity == AppUtil.this.rootActivity && AppUtil.this.rootActivity != null) {
        RobotLog.vv("AppUtil", "rootActivity=%s destroyed", new Object[] { AppUtil.access$600(this.this$0).getClass().getSimpleName() });
        AppUtil.access$602(AppUtil.this, null);
        AppUtil.this.initializeRootActivityIfNecessary();
      } 
    }
    
    public void onActivityPaused(Activity param1Activity) {}
    
    public void onActivityResumed(Activity param1Activity) {
      AppUtil.access$402(AppUtil.this, param1Activity);
      AppUtil.this.initializeRootActivityIfNecessary();
    }
    
    public void onActivitySaveInstanceState(Activity param1Activity, Bundle param1Bundle) {}
    
    public void onActivityStarted(Activity param1Activity) {
      AppUtil.access$402(AppUtil.this, param1Activity);
      AppUtil.this.initializeRootActivityIfNecessary();
    }
    
    public void onActivityStopped(Activity param1Activity) {}
  }
  
  public static interface UsbFileSystemRootListener {
    void onUsbFileSystemRootChanged(String param1String);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\AppUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */