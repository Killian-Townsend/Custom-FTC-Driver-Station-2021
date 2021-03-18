package org.firstinspires.ftc.robotcore.internal.system;

import java.lang.ref.WeakReference;
import java.util.concurrent.Semaphore;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.ThrowingCallable;

public final class LockingRunner {
  private static final int MAX_CONCURRENT_EXECUTIONS = 1;
  
  private WeakReference<Thread> lockingThreadReference = null;
  
  private final Semaphore semaphore = new Semaphore(1, true);
  
  private void lock() throws InterruptedException {
    WeakReference<Thread> weakReference = this.lockingThreadReference;
    if (weakReference == null || !((Thread)weakReference.get()).equals(Thread.currentThread())) {
      this.semaphore.acquire();
      this.lockingThreadReference = new WeakReference<Thread>(Thread.currentThread());
      return;
    } 
    throw new RuntimeException("The thread currently holding the lock tried to obtain the lock. This is invalid behavior, as LockingRunner does not (currently) support re-entrant locking, to preserve full compatibility with file-based locking.");
  }
  
  private void unlock() {
    this.lockingThreadReference = null;
    this.semaphore.release();
  }
  
  public <T> T lockWhile(final Supplier<T> supplier) throws InterruptedException {
    try {
      return (T)lockWhile((ThrowingCallable)new ThrowingCallable<T, NeverThrown>() {
            public T call() {
              return (T)supplier.get();
            }
          });
    } catch (NeverThrown neverThrown) {
      throw AppUtil.getInstance().unreachable(neverThrown);
    } 
  }
  
  public <T, E extends Throwable> T lockWhile(ThrowingCallable<T, E> paramThrowingCallable) throws InterruptedException, E {
    lock();
    try {
      return (T)paramThrowingCallable.call();
    } finally {
      unlock();
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
  
  private static class NeverThrown extends Exception {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\LockingRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */