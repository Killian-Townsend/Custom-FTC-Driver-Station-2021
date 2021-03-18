package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WatchdogMonitor {
  public static final String TAG = "WatchdogMonitor";
  
  protected ExecutorService executorService = ThreadPool.newSingleThreadExecutor("WatchdogMonitor");
  
  protected Thread monitoredThread = null;
  
  protected Runner runner = new Runner();
  
  protected final Object startStopLock = new Object();
  
  public void close(boolean paramBoolean) {
    synchronized (this.startStopLock) {
      if (this.executorService != null) {
        if (paramBoolean) {
          this.executorService.shutdownNow();
          ThreadPool.awaitTerminationOrExitApplication(this.executorService, 1L, TimeUnit.SECONDS, "WatchdogMonitor", "internal error");
        } else {
          this.executorService.shutdown();
        } 
        this.executorService = null;
      } 
      return;
    } 
  }
  
  public Thread getMonitoredThread() {
    return this.monitoredThread;
  }
  
  public <V> V monitor(Callable<V> paramCallable1, Callable<V> paramCallable2, long paramLong, TimeUnit paramTimeUnit) throws ExecutionException, InterruptedException {
    this.monitoredThread = Thread.currentThread();
    Future<V> future = schedule(paramCallable2, paramLong, paramTimeUnit);
    try {
      paramCallable1 = (Callable<V>)paramCallable1.call();
      if (!future.cancel(false))
        paramCallable1 = (Callable<V>)future.get(); 
      this.monitoredThread = null;
      return (V)paramCallable1;
    } catch (Exception exception) {
      throw new ExecutionException("exception while monitoring", exception);
    } finally {}
    if (!future.cancel(false))
      future.get(); 
    this.monitoredThread = null;
    throw paramCallable1;
  }
  
  protected <V> Future<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit) {
    try {
      this.runner.await();
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    this.runner.initialize(paramCallable, paramTimeUnit.toMillis(paramLong));
    try {
      this.executorService.submit(this.runner);
    } catch (RuntimeException runtimeException) {
      RobotLog.ee("WatchdogMonitor", runtimeException, "executorService.submit() failed");
      this.runner.noteRunComplete();
    } 
    return this.runner;
  }
  
  protected class Runner<V> implements Runnable, Future<V> {
    Callable<V> callable;
    
    V callableResult;
    
    final ReusableCountDownLatch cancelInterlock = new ReusableCountDownLatch(0);
    
    boolean done;
    
    ExecutionException executionException;
    
    boolean isCancelled;
    
    final ReusableCountDownLatch isCancelledAvailable = new ReusableCountDownLatch(0);
    
    long msTimeout;
    
    final ReusableCountDownLatch runComplete = new ReusableCountDownLatch(0);
    
    public void await() throws InterruptedException {
      this.runComplete.await();
    }
    
    public void await(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException, TimeoutException {
      if (this.runComplete.await(param1Long, param1TimeUnit))
        return; 
      throw new TimeoutException("timeout awaiting watchdog timer");
    }
    
    public boolean cancel(boolean param1Boolean) {
      this.cancelInterlock.countDown();
      try {
        this.isCancelledAvailable.await();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } 
      return this.isCancelled;
    }
    
    public V get() throws InterruptedException, ExecutionException {
      WatchdogMonitor.this.runner.await();
      if (WatchdogMonitor.this.runner.executionException == null)
        return this.callableResult; 
      throw WatchdogMonitor.this.runner.executionException;
    }
    
    public V get(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
      WatchdogMonitor.this.runner.await(param1Long, param1TimeUnit);
      if (WatchdogMonitor.this.runner.executionException == null)
        return this.callableResult; 
      throw WatchdogMonitor.this.runner.executionException;
    }
    
    public void initialize(Callable<V> param1Callable, long param1Long) {
      this.callable = param1Callable;
      this.msTimeout = param1Long;
      this.runComplete.reset(1);
      this.cancelInterlock.reset(1);
      this.isCancelledAvailable.reset(1);
      this.callableResult = null;
      this.executionException = null;
      this.isCancelled = false;
      this.done = false;
    }
    
    public boolean isCancelled() {
      return this.isCancelled;
    }
    
    public boolean isDone() {
      return this.done;
    }
    
    protected void noteRunComplete() {
      this.isCancelledAvailable.countDown();
      this.done = true;
      this.runComplete.countDown();
    }
    
    public void run() {
      try {
      
      } catch (InterruptedException interruptedException) {
      
      } finally {
        noteRunComplete();
      } 
      noteRunComplete();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\WatchdogMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */