package org.firstinspires.ftc.robotcore.internal.files;

import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.ThrowingCallable;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@Deprecated
public class FileBasedLock {
  public static final String TAG = "FileBasedLock";
  
  protected File lockFile;
  
  protected final int msClockSlop = 2000;
  
  protected final int msDeadlineInterval = 4000;
  
  protected final int msRefreshInterval = 1000;
  
  protected final Random random = new Random();
  
  protected File rootDir;
  
  public FileBasedLock(File paramFile) {
    RobotLog.ee("FileBasedLock", "FileBasedLock is currently broken. Use LockingRunner instead");
    AppUtil.getInstance().exitApplication();
    this.rootDir = paramFile.getAbsoluteFile();
    this.lockFile = new File(this.rootDir, "lock.dat");
    AppUtil.getInstance().ensureDirectoryExists(paramFile);
  }
  
  protected long getDeadline(File paramFile) {
    try {
      while (true) {
        String str = ReadWriteFile.readFileOrThrow(paramFile);
        String[] arrayOfString = str.split("\\|");
        if (arrayOfString.length == 3 && Integer.valueOf(arrayOfString[0]).intValue() == Integer.valueOf(arrayOfString[2]).intValue())
          return Long.valueOf(arrayOfString[1]).longValue(); 
      } 
    } catch (IOException iOException) {
      return 0L;
    } 
  }
  
  protected void lock(long paramLong, TimeUnit paramTimeUnit) throws TimeoutException, InterruptedException {
    if (paramLong >= 0L) {
      File file = newTempFile();
      try {
        (new FileOutputStream(file)).close();
        paramLong = paramTimeUnit.toMillis(paramLong);
        long l = msNow();
        paramLong += l;
        if (paramLong < l)
          paramLong = Long.MAX_VALUE; 
        while (!Thread.interrupted()) {
          if (msNow() <= paramLong) {
            refreshDeadline(file);
            if (file.renameTo(this.lockFile)) {
              RobotLog.vv("FileBasedLock", "locked %s", new Object[] { this.lockFile.getPath() });
              return;
            } 
            l = getDeadline(this.lockFile);
            if (l != 0L && l + 2000L < msNow()) {
              RobotLog.vv("FileBasedLock", "breaking lock %s", new Object[] { this.lockFile.getPath() });
              releaseLock();
            } 
            Thread.yield();
            continue;
          } 
          file.delete();
          throw new TimeoutException(String.format("unable to acquire lock %s", new Object[] { this.rootDir.getPath() }));
        } 
        throw new InterruptedException(String.format("interrupt while acquiring lock %s", new Object[] { this.rootDir.getPath() }));
      } catch (IOException iOException) {
        throw new RuntimeException(String.format("unable to create %s", new Object[] { file.getPath() }), iOException);
      } 
    } 
    throw new IllegalArgumentException(String.format("timeout must be >= 0: %d", new Object[] { Long.valueOf(paramLong) }));
  }
  
  public <T, E extends Throwable> T lockWhile(long paramLong, TimeUnit paramTimeUnit, ThrowingCallable<T, E> paramThrowingCallable) throws TimeoutException, InterruptedException, E {
    lock(paramLong, paramTimeUnit);
    try {
      Future<?> future = ThreadPool.getDefault().submit(new Runnable() {
            public void run() {
              while (true) {
                if (!Thread.currentThread().isInterrupted())
                  try {
                    Thread.sleep(1000L, 0);
                    RobotLog.vv("FileBasedLock", "refreshing lock %s", new Object[] { this.this$0.lockFile.getPath() });
                    FileBasedLock fileBasedLock = FileBasedLock.this;
                    fileBasedLock.refreshDeadline(fileBasedLock.lockFile);
                    continue;
                  } catch (InterruptedException interruptedException) {
                    break;
                  }  
                return;
              } 
            }
          });
    } finally {
      unlock();
    } 
  }
  
  public <T> T lockWhile(final Supplier<T> supplier) throws InterruptedException {
    try {
      return (T)lockWhile((ThrowingCallable)new ThrowingCallable<T, NeverThrown>() {
            public T call() {
              return (T)supplier.get();
            }
          });
    } catch (NeverThrown neverThrown) {
      throw AppUtil.getInstance().unreachable("FileBasedLock", neverThrown);
    } 
  }
  
  public <T, E extends Throwable> T lockWhile(ThrowingCallable<T, E> paramThrowingCallable) throws InterruptedException, E {
    try {
      return (T)lockWhile(Long.MAX_VALUE, TimeUnit.MILLISECONDS, (ThrowingCallable)paramThrowingCallable);
    } catch (TimeoutException timeoutException) {
      throw AppUtil.getInstance().unreachable("FileBasedLock", timeoutException);
    } 
  }
  
  public void lockWhile(final Runnable runnable) throws InterruptedException {
    lockWhile(new Supplier<Void>() {
          public Void get() {
            runnable.run();
            return null;
          }
        });
  }
  
  protected long msNow() {
    return System.currentTimeMillis();
  }
  
  protected File newTempFile() {
    File file = this.rootDir;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(UUID.randomUUID().toString());
    stringBuilder.append(".tmp");
    return new File(file, stringBuilder.toString());
  }
  
  protected void refreshDeadline(File paramFile) {
    long l = msNow();
    int i = this.random.nextInt();
    ReadWriteFile.writeFile(paramFile, String.format(Locale.getDefault(), "%d|%d|%d", new Object[] { Integer.valueOf(i), Long.valueOf(l + 4000L), Integer.valueOf(i) }));
  }
  
  protected void releaseLock() {
    File file = newTempFile();
    if (this.lockFile.renameTo(file)) {
      if (!file.delete()) {
        RobotLog.ee("FileBasedLock", "unable to delete %s", new Object[] { file.getPath() });
        return;
      } 
    } else if (this.lockFile.exists()) {
      RobotLog.ee("FileBasedLock", "unable to rename %s to %s for deletion", new Object[] { this.lockFile.getPath(), file.getPath() });
    } 
  }
  
  protected void unlock() {
    RobotLog.vv("FileBasedLock", "unlocking %s", new Object[] { this.lockFile.getPath() });
    releaseLock();
  }
  
  protected class NeverThrown extends Exception {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\files\FileBasedLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */