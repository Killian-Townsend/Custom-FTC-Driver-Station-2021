package org.firstinspires.ftc.robotcore.internal.files;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.Closeable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Predicate;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class MediaTransferProtocolMonitor implements Closeable {
  public static final String TAG = "MTPMonitor";
  
  public static final String tmpMTPExtension = ".mtpmgr.tmp";
  
  protected final Object concurrentClientLock = new Object();
  
  protected final Context context = AppUtil.getDefContext();
  
  protected RecursiveFileObserver dirObserver = null;
  
  protected ExecutorService executorService = null;
  
  protected int msMinimumScanInterval = 5000;
  
  protected Set<String> pendingPaths = new HashSet<String>();
  
  public static MediaTransferProtocolMonitor getInstance() {
    return InstanceHolder.theInstance;
  }
  
  public static boolean isIndicatorFile(File paramFile) {
    return paramFile.getName().endsWith(".mtpmgr.tmp");
  }
  
  public static void makeIndicatorFile(File paramFile) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(UUID.randomUUID().toString());
    stringBuilder.append(".mtpmgr.tmp");
    ReadWriteFile.writeFile(new File(paramFile, stringBuilder.toString()), "internal system utility file - you can delete this file");
  }
  
  public static void renoticeIndicatorFiles(File paramFile) {
    getInstance().noteFiles(AppUtil.getInstance().filesIn(paramFile, new Predicate<File>() {
            public boolean test(File param1File) {
              return MediaTransferProtocolMonitor.isIndicatorFile(param1File);
            }
          }));
  }
  
  public void close() {
    stop();
  }
  
  public void noteFile(File paramFile) {
    noteFiles(Collections.singletonList(paramFile));
  }
  
  public void noteFiles(List<File> paramList) {
    null = new ArrayList();
    for (File file : paramList) {
      if (file.exists()) {
        if (!file.isDirectory())
          null.add(file.getAbsolutePath()); 
        continue;
      } 
      if (file.getParentFile().exists())
        null.add(file.getAbsolutePath()); 
    } 
    synchronized (this.concurrentClientLock) {
      this.pendingPaths.addAll(null);
      return;
    } 
  }
  
  protected void notifyMTP() {
    null = new ArrayList();
    synchronized (this.concurrentClientLock) {
      null.addAll(this.pendingPaths);
      this.pendingPaths = new HashSet<String>();
      if (!null.isEmpty()) {
        null = null.<String>toArray(new String[null.size()]);
        MediaScannerConnection.scanFile(this.context, (String[])null, null, new MediaScannerConnection.OnScanCompletedListener() {
              public void onScanCompleted(String param1String, Uri param1Uri) {
                File file = new File(param1String);
                if (param1Uri == null) {
                  if (file.exists()) {
                    RobotLog.ww("MTPMonitor", "scanning failed; retrying later: %s", new Object[] { param1String });
                    return;
                  } 
                } else if (MediaTransferProtocolMonitor.isIndicatorFile(file)) {
                  file.delete();
                } 
              }
            });
      } 
      return;
    } 
  }
  
  protected void start() {
    synchronized (this.concurrentClientLock) {
      stop();
      startNotifications();
      startObserver();
      return;
    } 
  }
  
  protected void startNotifications() {
    ExecutorService executorService = ThreadPool.newSingleThreadExecutor("MTPMonitor");
    this.executorService = executorService;
    executorService.submit(new Runnable() {
          public void run() {
            while (true) {
              if (!Thread.currentThread().isInterrupted()) {
                try {
                  Thread.sleep(MediaTransferProtocolMonitor.this.msMinimumScanInterval);
                } catch (InterruptedException interruptedException) {
                  Thread.currentThread().interrupt();
                } 
                if (Thread.currentThread().isInterrupted())
                  return; 
                MediaTransferProtocolMonitor.this.notifyMTP();
                continue;
              } 
              return;
            } 
          }
        });
  }
  
  protected void startObserver() {
    final File directory = AppUtil.ROOT_FOLDER;
    RobotLog.vv("MTPMonitor", "observing: %s", new Object[] { file.getAbsolutePath() });
    RecursiveFileObserver recursiveFileObserver = new RecursiveFileObserver(file.getAbsolutePath(), 17352, RecursiveFileObserver.Mode.RECURSIVE, new RecursiveFileObserver.Listener() {
          public void onEvent(int param1Int, File param1File) {
            if ((param1Int & 0xFFF) != 0)
              MediaTransferProtocolMonitor.this.noteFile(param1File); 
            if ((param1Int & 0x4000) != 0) {
              RobotLog.vv("MTPMonitor", "observed OVERFLOW: event=0x%02x %s", new Object[] { Integer.valueOf(param1Int), param1File });
              List<File> list = AppUtil.getInstance().filesUnder(directory, new Predicate<File>() {
                    public boolean test(File param2File) {
                      return true;
                    }
                  });
              MediaTransferProtocolMonitor.this.noteFiles(list);
            } 
          }
        });
    this.dirObserver = recursiveFileObserver;
    recursiveFileObserver.startWatching();
  }
  
  protected void stop() {
    synchronized (this.concurrentClientLock) {
      stopObserver();
      stopNotifications();
      return;
    } 
  }
  
  protected void stopNotifications() {
    ExecutorService executorService = this.executorService;
    if (executorService != null) {
      executorService.shutdownNow();
      ThreadPool.awaitTerminationOrExitApplication(this.executorService, 2L, TimeUnit.SECONDS, "MTPMonitor", "internal error");
      this.executorService = null;
    } 
  }
  
  protected void stopObserver() {
    RecursiveFileObserver recursiveFileObserver = this.dirObserver;
    if (recursiveFileObserver != null) {
      recursiveFileObserver.stopWatching();
      this.dirObserver = null;
    } 
  }
  
  protected static class InstanceHolder {
    public static MediaTransferProtocolMonitor theInstance = new MediaTransferProtocolMonitor();
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\MediaTransferProtocolMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */