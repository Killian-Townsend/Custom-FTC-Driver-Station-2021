package com.qualcomm.robotcore.util;

import android.os.Debug;
import android.os.Process;
import android.util.LongSparseArray;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.Deadline;

public class ThreadPool {
  public static final String TAG = "ThreadPool";
  
  private static ScheduledExecutorService defaultScheduler;
  
  private static ExecutorService defaultSerialThreadPool;
  
  private static ExecutorService defaultThreadPool;
  
  private static Map<ExecutorService, Integer> extantExecutors;
  
  private static final Object extantExecutorsLock;
  
  private static LongSparseArray<Integer> threadIdMap = new LongSparseArray();
  
  static {
    extantExecutors = new WeakHashMap<ExecutorService, Integer>();
    extantExecutorsLock = new Object();
    defaultThreadPool = null;
    defaultSerialThreadPool = null;
    defaultScheduler = null;
  }
  
  public static boolean awaitFuture(Future paramFuture, long paramLong, TimeUnit paramTimeUnit) {
    try {
      paramFuture.get(paramLong, paramTimeUnit);
      return true;
    } catch (CancellationException|ExecutionException cancellationException) {
      return true;
    } catch (TimeoutException timeoutException) {
      return false;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return false;
    } 
  }
  
  public static boolean awaitTermination(ExecutorService paramExecutorService, long paramLong, TimeUnit paramTimeUnit, String paramString) throws InterruptedException {
    verifyNotOnExecutorThread(paramExecutorService);
    Deadline deadline = new Deadline(paramLong, paramTimeUnit);
    int i = 0;
    while (true) {
      boolean bool = paramExecutorService.isTerminated();
      if (!bool) {
        RobotLog.vv("ThreadPool", "waiting for service %s", new Object[] { paramString });
        if (paramExecutorService.awaitTermination(Math.min(2500L, deadline.timeRemaining(TimeUnit.MILLISECONDS)), TimeUnit.MILLISECONDS)) {
          Assert.assertTrue(paramExecutorService.isTerminated());
          RobotLog.vv("ThreadPool", "service %s terminated in awaitTermination()", new Object[] { paramString });
          bool = true;
        } else if (deadline.hasExpired()) {
          RobotLog.ee("ThreadPool", "deadline expired waiting for service termination: %s", new Object[] { paramString });
        } else {
          i++;
          RobotLog.vv("ThreadPool", "awaiting shutdown: thread pool=\"%s\" attempt=%d", new Object[] { paramString, Integer.valueOf(i) });
          logThreadStacks(paramExecutorService);
          interruptThreads(paramExecutorService);
          continue;
        } 
      } 
      if (bool) {
        RobotLog.vv("ThreadPool", "executive service %s(0x%08x) is terminated", new Object[] { paramString, Integer.valueOf(paramExecutorService.hashCode()) });
        return bool;
      } 
      RobotLog.vv("ThreadPool", "executive service %s(0x%08x) is NOT terminated", new Object[] { paramString, Integer.valueOf(paramExecutorService.hashCode()) });
      synchronized (extantExecutorsLock) {
        System.gc();
        Iterator<ExecutorService> iterator = extantExecutors.keySet().iterator();
        while (iterator.hasNext())
          logThreadStacks(iterator.next()); 
        return bool;
      } 
    } 
  }
  
  public static void awaitTerminationOrExitApplication(ExecutorService paramExecutorService, long paramLong, TimeUnit paramTimeUnit, String paramString1, String paramString2) {
    try {
      if (!awaitTermination(paramExecutorService, paramLong, paramTimeUnit, paramString1)) {
        exitApplication(paramString1, paramString2);
        return;
      } 
    } catch (InterruptedException interruptedException) {
      RobotLog.vv("ThreadPool", "awaitTerminationOrExitApplication %s; interrupt thrown", new Object[] { paramString1 });
      try {
        Thread.sleep(100L);
      } catch (InterruptedException interruptedException1) {
        Thread.currentThread().interrupt();
      } 
      if (!paramExecutorService.isTerminated()) {
        RobotLog.vv("ThreadPool", "awaitTerminationOrExitApplication %s; exiting application after interrupt", new Object[] { paramString1 });
        exitApplication(paramString1, paramString2);
      } 
    } 
  }
  
  public static void cancelFutureOrExitApplication(Future paramFuture, long paramLong, TimeUnit paramTimeUnit, String paramString1, String paramString2) {
    try {
      paramFuture.cancel(true);
      paramFuture.get(paramLong, paramTimeUnit);
      return;
    } catch (CancellationException cancellationException) {
      return;
    } catch (ExecutionException executionException) {
      RobotLog.logExceptionHeader(executionException, "exception thrown in future; ignoring", new Object[0]);
      return;
    } catch (TimeoutException timeoutException) {
      exitApplication(paramString1, paramString2);
      return;
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return;
    } 
  }
  
  public static void exitApplication(String paramString1, String paramString2) {
    RobotLog.ee("ThreadPool", "*****************************************************************");
    RobotLog.ee("ThreadPool", "%s took too long to exit; emergency killing app.", new Object[] { paramString1 });
    RobotLog.ee("ThreadPool", "%s", new Object[] { paramString2 });
    RobotLog.ee("ThreadPool", "*****************************************************************");
    while (Debug.isDebuggerConnected())
      Thread.yield(); 
    System.exit(-1);
  }
  
  public static ExecutorService getDefault() {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultThreadPool : Ljava/util/concurrent/ExecutorService;
    //   6: ifnonnull -> 18
    //   9: ldc_w 'default threadpool'
    //   12: invokestatic newCachedThreadPool : (Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;
    //   15: putstatic com/qualcomm/robotcore/util/ThreadPool.defaultThreadPool : Ljava/util/concurrent/ExecutorService;
    //   18: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultThreadPool : Ljava/util/concurrent/ExecutorService;
    //   21: astore_0
    //   22: ldc com/qualcomm/robotcore/util/ThreadPool
    //   24: monitorexit
    //   25: aload_0
    //   26: areturn
    //   27: astore_0
    //   28: ldc com/qualcomm/robotcore/util/ThreadPool
    //   30: monitorexit
    //   31: aload_0
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	27	finally
    //   18	25	27	finally
    //   28	31	27	finally
  }
  
  public static ScheduledExecutorService getDefaultScheduler() {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultScheduler : Ljava/util/concurrent/ScheduledExecutorService;
    //   6: ifnonnull -> 20
    //   9: bipush #24
    //   11: ldc_w 'default scheduler'
    //   14: invokestatic newScheduledExecutor : (ILjava/lang/String;)Lcom/qualcomm/robotcore/util/ThreadPool$RecordingScheduledExecutor;
    //   17: putstatic com/qualcomm/robotcore/util/ThreadPool.defaultScheduler : Ljava/util/concurrent/ScheduledExecutorService;
    //   20: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultScheduler : Ljava/util/concurrent/ScheduledExecutorService;
    //   23: astore_0
    //   24: ldc com/qualcomm/robotcore/util/ThreadPool
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/qualcomm/robotcore/util/ThreadPool
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	27	29	finally
    //   30	33	29	finally
  }
  
  public static ExecutorService getDefaultSerial() {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultSerialThreadPool : Ljava/util/concurrent/ExecutorService;
    //   6: ifnonnull -> 18
    //   9: ldc_w 'default serial threadpool'
    //   12: invokestatic newSingleThreadExecutor : (Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;
    //   15: putstatic com/qualcomm/robotcore/util/ThreadPool.defaultSerialThreadPool : Ljava/util/concurrent/ExecutorService;
    //   18: getstatic com/qualcomm/robotcore/util/ThreadPool.defaultSerialThreadPool : Ljava/util/concurrent/ExecutorService;
    //   21: astore_0
    //   22: ldc com/qualcomm/robotcore/util/ThreadPool
    //   24: monitorexit
    //   25: aload_0
    //   26: areturn
    //   27: astore_0
    //   28: ldc com/qualcomm/robotcore/util/ThreadPool
    //   30: monitorexit
    //   31: aload_0
    //   32: athrow
    // Exception table:
    //   from	to	target	type
    //   3	18	27	finally
    //   18	25	27	finally
    //   28	31	27	finally
  }
  
  public static int getTID(long paramLong) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.threadIdMap : Landroid/util/LongSparseArray;
    //   6: lload_0
    //   7: iconst_0
    //   8: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   11: invokevirtual get : (JLjava/lang/Object;)Ljava/lang/Object;
    //   14: checkcast java/lang/Integer
    //   17: invokevirtual intValue : ()I
    //   20: istore_2
    //   21: ldc com/qualcomm/robotcore/util/ThreadPool
    //   23: monitorexit
    //   24: iload_2
    //   25: ireturn
    //   26: astore_3
    //   27: ldc com/qualcomm/robotcore/util/ThreadPool
    //   29: monitorexit
    //   30: aload_3
    //   31: athrow
    // Exception table:
    //   from	to	target	type
    //   3	24	26	finally
    //   27	30	26	finally
  }
  
  public static int getTID(Thread paramThread) {
    return getTID(paramThread.getId());
  }
  
  private static void interruptThreads(ExecutorService paramExecutorService) {
    if (paramExecutorService instanceof ContainerOfThreads)
      for (Thread thread : paramExecutorService) {
        if (thread.isAlive()) {
          if (thread.getId() == Thread.currentThread().getId())
            RobotLog.vv("ThreadPool", "interrupting current thread"); 
          thread.interrupt();
        } 
      }  
  }
  
  public static void logThreadLifeCycle(String paramString, Runnable paramRunnable) {
    try {
      Thread.currentThread().setName(paramString);
      RobotLog.v(String.format("thread: '%s' starting...", new Object[] { paramString }));
      paramRunnable.run();
      return;
    } finally {
      RobotLog.v(String.format("thread: ...terminating '%s'", new Object[] { paramString }));
    } 
  }
  
  private static void logThreadStacks(ExecutorService paramExecutorService) {
    if (paramExecutorService instanceof ContainerOfThreads)
      for (Thread thread : paramExecutorService) {
        if (thread.isAlive())
          RobotLog.logStackTrace(thread, "", new Object[0]); 
      }  
  }
  
  public static ExecutorService newCachedThreadPool(String paramString) {
    RecordingThreadPool recordingThreadPool = new RecordingThreadPool(0, 2147483647, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
    if (paramString != null)
      recordingThreadPool.setNameRootForThreads(paramString); 
    noteNewExecutor(recordingThreadPool);
    return recordingThreadPool;
  }
  
  public static ExecutorService newFixedThreadPool(int paramInt, String paramString) {
    RecordingThreadPool recordingThreadPool = new RecordingThreadPool(paramInt, paramInt, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    if (paramString != null)
      recordingThreadPool.setNameRootForThreads(paramString); 
    noteNewExecutor(recordingThreadPool);
    return recordingThreadPool;
  }
  
  public static RecordingScheduledExecutor newScheduledExecutor(int paramInt, String paramString) {
    RecordingScheduledExecutor recordingScheduledExecutor = new RecordingScheduledExecutor(paramInt);
    if (paramString != null)
      recordingScheduledExecutor.setNameRootForThreads(paramString); 
    noteNewExecutor(recordingScheduledExecutor);
    return recordingScheduledExecutor;
  }
  
  public static ExecutorService newSingleThreadExecutor(String paramString) {
    RecordingThreadPool recordingThreadPool = new RecordingThreadPool(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
    if (paramString != null)
      recordingThreadPool.setNameRootForThreads(paramString); 
    noteNewExecutor(recordingThreadPool);
    return recordingThreadPool;
  }
  
  private static void noteNewExecutor(ExecutorService paramExecutorService) {
    synchronized (extantExecutorsLock) {
      extantExecutors.put(paramExecutorService, Integer.valueOf(1));
      return;
    } 
  }
  
  private static void noteTID(Thread paramThread, int paramInt) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.threadIdMap : Landroid/util/LongSparseArray;
    //   6: aload_0
    //   7: invokevirtual getId : ()J
    //   10: iload_1
    //   11: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   14: invokevirtual put : (JLjava/lang/Object;)V
    //   17: ldc com/qualcomm/robotcore/util/ThreadPool
    //   19: monitorexit
    //   20: return
    //   21: astore_0
    //   22: ldc com/qualcomm/robotcore/util/ThreadPool
    //   24: monitorexit
    //   25: aload_0
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	21	finally
    //   22	25	21	finally
  }
  
  private static void removeTID(Thread paramThread) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/util/ThreadPool
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/util/ThreadPool.threadIdMap : Landroid/util/LongSparseArray;
    //   6: aload_0
    //   7: invokevirtual getId : ()J
    //   10: invokevirtual remove : (J)V
    //   13: ldc com/qualcomm/robotcore/util/ThreadPool
    //   15: monitorexit
    //   16: return
    //   17: astore_0
    //   18: ldc com/qualcomm/robotcore/util/ThreadPool
    //   20: monitorexit
    //   21: aload_0
    //   22: athrow
    // Exception table:
    //   from	to	target	type
    //   3	16	17	finally
    //   18	21	17	finally
  }
  
  protected static Throwable retrieveUserException(Runnable paramRunnable, Throwable paramThrowable) {
    Throwable throwable = paramThrowable;
    if (paramThrowable == null) {
      throwable = paramThrowable;
      if (paramRunnable instanceof Future) {
        throwable = paramThrowable;
        try {
          if (((Future)paramRunnable).isDone()) {
            ((Future)paramRunnable).get();
            return paramThrowable;
          } 
          return throwable;
        } catch (CancellationException cancellationException) {
          return null;
        } catch (ExecutionException executionException) {
          return executionException.getCause();
        } catch (InterruptedException interruptedException) {
          Thread.currentThread().interrupt();
          return paramThrowable;
        } 
      } 
    } 
    return throwable;
  }
  
  private static void verifyNotOnExecutorThread(ExecutorService paramExecutorService) {
    if (paramExecutorService instanceof ContainerOfThreads) {
      Iterator<Thread> iterator = ((ContainerOfThreads)paramExecutorService).iterator();
      while (iterator.hasNext()) {
        if ((Thread)iterator.next() == Thread.currentThread())
          Assert.assertFailed(); 
      } 
    } 
  }
  
  public static interface ContainerOfThreads extends Iterable<Thread> {
    void noteFinishedThread(Thread param1Thread);
    
    void noteNewThread(Thread param1Thread);
    
    void setNameRootForThreads(String param1String);
    
    void setPriorityForThreads(Integer param1Integer);
  }
  
  static class ContainerOfThreadsRecorder implements ContainerOfThreads {
    String nameRootForThreads = null;
    
    Integer priorityForThreads = null;
    
    AtomicInteger threadCount = new AtomicInteger(0);
    
    Queue<Thread> threads = new ConcurrentLinkedQueue<Thread>();
    
    public Iterator<Thread> iterator() {
      return this.threads.iterator();
    }
    
    protected void logThread(Thread param1Thread, String param1String) {
      String str;
      int i = hashCode();
      if (this.nameRootForThreads == null) {
        str = "";
      } else {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(": ");
        stringBuilder.append(this.nameRootForThreads);
        str = stringBuilder.toString();
      } 
      RobotLog.vv("ThreadPool", "container(0x%08x%s) %s id=%d TID=%d count=%d", new Object[] { Integer.valueOf(i), str, param1String, Long.valueOf(param1Thread.getId()), Integer.valueOf(ThreadPool.getTID(param1Thread)), Integer.valueOf(this.threads.size()) });
    }
    
    public void noteFinishedThread(Thread param1Thread) {
      logThread(param1Thread, "removed");
      this.threads.remove(param1Thread);
    }
    
    public void noteNewThread(Thread param1Thread) {
      this.threads.add(param1Thread);
      String str = this.nameRootForThreads;
      if (str != null)
        param1Thread.setName(String.format("%s-#%d", new Object[] { str, Integer.valueOf(this.threadCount.getAndIncrement()) })); 
      Integer integer = this.priorityForThreads;
      if (integer != null)
        param1Thread.setPriority(integer.intValue()); 
      logThread(param1Thread, "added");
    }
    
    public void setNameRootForThreads(String param1String) {
      this.nameRootForThreads = param1String;
    }
    
    public void setPriorityForThreads(Integer param1Integer) {
      this.priorityForThreads = param1Integer;
    }
  }
  
  public static class RecordingScheduledExecutor extends ContainerOfThreadsRecorder implements ScheduledExecutorService {
    protected ScheduledThreadPoolExecutor executor;
    
    RecordingScheduledExecutor(int param1Int) {
      ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(param1Int, new ThreadPool.ThreadFactoryImpl(this)) {
          protected void afterExecute(Runnable param2Runnable, Throwable param2Throwable) {
            super.afterExecute(param2Runnable, param2Throwable);
            Throwable throwable = ThreadPool.retrieveUserException(param2Runnable, param2Throwable);
            if (throwable != null)
              RobotLog.ee("ThreadPool", throwable, "exception thrown in thread pool; ignored"); 
          }
        };
      this.executor = scheduledThreadPoolExecutor;
      scheduledThreadPoolExecutor.setRemoveOnCancelPolicy(true);
    }
    
    public void allowCoreThreadTimeOut(boolean param1Boolean) {
      this.executor.allowCoreThreadTimeOut(param1Boolean);
    }
    
    public boolean awaitTermination(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException {
      return this.executor.awaitTermination(param1Long, param1TimeUnit);
    }
    
    public void execute(Runnable param1Runnable) {
      this.executor.execute(param1Runnable);
    }
    
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> param1Collection) throws InterruptedException {
      return this.executor.invokeAll(param1Collection);
    }
    
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> param1Collection, long param1Long, TimeUnit param1TimeUnit) throws InterruptedException {
      return this.executor.invokeAll(param1Collection, param1Long, param1TimeUnit);
    }
    
    public <T> T invokeAny(Collection<? extends Callable<T>> param1Collection) throws InterruptedException, ExecutionException {
      return this.executor.invokeAny(param1Collection);
    }
    
    public <T> T invokeAny(Collection<? extends Callable<T>> param1Collection, long param1Long, TimeUnit param1TimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.executor.invokeAny(param1Collection, param1Long, param1TimeUnit);
    }
    
    public boolean isShutdown() {
      return this.executor.isShutdown();
    }
    
    public boolean isTerminated() {
      return this.executor.isTerminated();
    }
    
    public ScheduledFuture<?> schedule(Runnable param1Runnable, long param1Long, TimeUnit param1TimeUnit) {
      return this.executor.schedule(param1Runnable, param1Long, param1TimeUnit);
    }
    
    public <V> ScheduledFuture<V> schedule(Callable<V> param1Callable, long param1Long, TimeUnit param1TimeUnit) {
      return this.executor.schedule(param1Callable, param1Long, param1TimeUnit);
    }
    
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable param1Runnable, long param1Long1, long param1Long2, TimeUnit param1TimeUnit) {
      return this.executor.scheduleAtFixedRate(param1Runnable, param1Long1, param1Long2, param1TimeUnit);
    }
    
    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable param1Runnable, long param1Long1, long param1Long2, TimeUnit param1TimeUnit) {
      return this.executor.scheduleWithFixedDelay(param1Runnable, param1Long1, param1Long2, param1TimeUnit);
    }
    
    public void setKeepAliveTime(long param1Long, TimeUnit param1TimeUnit) {
      this.executor.setKeepAliveTime(param1Long, param1TimeUnit);
    }
    
    public void shutdown() {
      this.executor.shutdown();
    }
    
    public List<Runnable> shutdownNow() {
      return this.executor.shutdownNow();
    }
    
    public Future<?> submit(Runnable param1Runnable) {
      return this.executor.submit(param1Runnable);
    }
    
    public <T> Future<T> submit(Runnable param1Runnable, T param1T) {
      return this.executor.submit(param1Runnable, param1T);
    }
    
    public <T> Future<T> submit(Callable<T> param1Callable) {
      return this.executor.submit(param1Callable);
    }
  }
  
  class null extends ScheduledThreadPoolExecutor {
    null(int param1Int, ThreadFactory param1ThreadFactory) {
      super(param1Int, param1ThreadFactory);
    }
    
    protected void afterExecute(Runnable param1Runnable, Throwable param1Throwable) {
      super.afterExecute(param1Runnable, param1Throwable);
      Throwable throwable = ThreadPool.retrieveUserException(param1Runnable, param1Throwable);
      if (throwable != null)
        RobotLog.ee("ThreadPool", throwable, "exception thrown in thread pool; ignored"); 
    }
  }
  
  public static class RecordingThreadPool extends ContainerOfThreadsRecorder implements ExecutorService {
    ThreadPoolExecutor executor;
    
    RecordingThreadPool(int param1Int1, int param1Int2, long param1Long, TimeUnit param1TimeUnit, BlockingQueue<Runnable> param1BlockingQueue) {
      this.executor = new ThreadPoolExecutor(param1Int1, param1Int2, param1Long, param1TimeUnit, param1BlockingQueue, new ThreadPool.ThreadFactoryImpl(this)) {
          protected void afterExecute(Runnable param2Runnable, Throwable param2Throwable) {
            super.afterExecute(param2Runnable, param2Throwable);
            Throwable throwable = ThreadPool.retrieveUserException(param2Runnable, param2Throwable);
            if (throwable != null)
              RobotLog.ee("ThreadPool", throwable, "exception thrown in thread pool; ignored"); 
          }
        };
    }
    
    public boolean awaitTermination(long param1Long, TimeUnit param1TimeUnit) throws InterruptedException {
      return this.executor.awaitTermination(param1Long, param1TimeUnit);
    }
    
    public void execute(Runnable param1Runnable) {
      this.executor.execute(param1Runnable);
    }
    
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> param1Collection) throws InterruptedException {
      return this.executor.invokeAll(param1Collection);
    }
    
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> param1Collection, long param1Long, TimeUnit param1TimeUnit) throws InterruptedException {
      return this.executor.invokeAll(param1Collection, param1Long, param1TimeUnit);
    }
    
    public <T> T invokeAny(Collection<? extends Callable<T>> param1Collection) throws InterruptedException, ExecutionException {
      return this.executor.invokeAny(param1Collection);
    }
    
    public <T> T invokeAny(Collection<? extends Callable<T>> param1Collection, long param1Long, TimeUnit param1TimeUnit) throws InterruptedException, ExecutionException, TimeoutException {
      return this.executor.invokeAny(param1Collection, param1Long, param1TimeUnit);
    }
    
    public boolean isShutdown() {
      return this.executor.isShutdown();
    }
    
    public boolean isTerminated() {
      return this.executor.isTerminated();
    }
    
    public void shutdown() {
      this.executor.shutdown();
    }
    
    public List<Runnable> shutdownNow() {
      return this.executor.shutdownNow();
    }
    
    public Future<?> submit(Runnable param1Runnable) {
      return this.executor.submit(param1Runnable);
    }
    
    public <T> Future<T> submit(Runnable param1Runnable, T param1T) {
      return this.executor.submit(param1Runnable, param1T);
    }
    
    public <T> Future<T> submit(Callable<T> param1Callable) {
      return this.executor.submit(param1Callable);
    }
  }
  
  class null extends ThreadPoolExecutor {
    null(int param1Int1, int param1Int2, long param1Long, TimeUnit param1TimeUnit, BlockingQueue<Runnable> param1BlockingQueue, ThreadFactory param1ThreadFactory) {
      super(param1Int1, param1Int2, param1Long, param1TimeUnit, param1BlockingQueue, param1ThreadFactory);
    }
    
    protected void afterExecute(Runnable param1Runnable, Throwable param1Throwable) {
      super.afterExecute(param1Runnable, param1Throwable);
      Throwable throwable = ThreadPool.retrieveUserException(param1Runnable, param1Throwable);
      if (throwable != null)
        RobotLog.ee("ThreadPool", throwable, "exception thrown in thread pool; ignored"); 
    }
  }
  
  public static class Singleton<T> {
    public static int INFINITE_TIMEOUT = -1;
    
    private boolean inFlight = false;
    
    private final Object lock = new Object();
    
    private ThreadPool.SingletonResult<T> result = null;
    
    private ExecutorService service = null;
    
    public T await() throws InterruptedException {
      ThreadPool.SingletonResult<T> singletonResult = getResult();
      return (singletonResult != null) ? singletonResult.await() : null;
    }
    
    public T await(long param1Long) throws InterruptedException {
      ThreadPool.SingletonResult<T> singletonResult = getResult();
      return (singletonResult != null) ? singletonResult.await(param1Long) : null;
    }
    
    public ThreadPool.SingletonResult<T> getResult() {
      synchronized (this.lock) {
        return this.result;
      } 
    }
    
    public void reset() {
      synchronized (this.lock) {
        this.result = null;
        this.inFlight = false;
        return;
      } 
    }
    
    public void setService(ExecutorService param1ExecutorService) {
      this.service = param1ExecutorService;
    }
    
    public ThreadPool.SingletonResult<T> submit(int param1Int, final Runnable runnable) {
      return submit(param1Int, new Callable<T>() {
            public T call() throws Exception {
              runnable.run();
              return null;
            }
          });
    }
    
    public ThreadPool.SingletonResult<T> submit(int param1Int, final Callable<T> callable) {
      synchronized (this.lock) {
        if (!this.inFlight)
          if (this.service != null) {
            this.inFlight = true;
            this.result = new ThreadPool.SingletonResult<T>(param1Int, this, this.service.submit(new Callable<T>() {
                    public T call() throws Exception {
                      try {
                        null = (T)callable.call();
                      } catch (InterruptedException interruptedException) {
                        Thread.currentThread().interrupt();
                      } catch (Exception exception) {
                        RobotLog.ee("ThreadPool", exception, "exception thrown during Singleton.submit()");
                      } finally {
                        null = null;
                      } 
                    }
                  }));
          } else {
            throw new IllegalArgumentException("Singleton service must be set before work is submitted");
          }  
        return this.result;
      } 
    }
    
    public ThreadPool.SingletonResult<T> submit(Runnable param1Runnable) {
      return submit(INFINITE_TIMEOUT, param1Runnable);
    }
    
    public ThreadPool.SingletonResult<T> submit(Callable<T> param1Callable) {
      return submit(INFINITE_TIMEOUT, param1Callable);
    }
  }
  
  class null implements Callable<T> {
    public T call() throws Exception {
      runnable.run();
      return null;
    }
  }
  
  class null implements Callable<T> {
    public T call() throws Exception {
      try {
        null = (T)callable.call();
      } catch (InterruptedException interruptedException) {
        Thread.currentThread().interrupt();
      } catch (Exception exception) {
        RobotLog.ee("ThreadPool", exception, "exception thrown during Singleton.submit()");
      } finally {
        null = null;
      } 
    }
  }
  
  public static class SingletonResult<T> {
    private Future<T> future;
    
    private long nsDeadline;
    
    private ThreadPool.Singleton<T> singleton;
    
    public SingletonResult(int param1Int, ThreadPool.Singleton<T> param1Singleton, Future<T> param1Future) {
      long l;
      this.future = null;
      this.singleton = param1Singleton;
      this.future = param1Future;
      if (param1Int == ThreadPool.Singleton.INFINITE_TIMEOUT) {
        l = -1L;
      } else {
        l = System.nanoTime() + param1Int * 1000000L;
      } 
      this.nsDeadline = l;
    }
    
    public T await() throws InterruptedException {
      if (this.nsDeadline >= 0L) {
        long l = System.nanoTime();
        return await(Math.max(0L, this.nsDeadline - l) / 1000000L);
      } 
      try {
        if (this.future != null)
          return this.future.get(); 
      } catch (ExecutionException executionException) {
        RobotLog.ee("ThreadPool", executionException, "singleton threw ExecutionException");
      } 
      return null;
    }
    
    public T await(long param1Long) throws InterruptedException {
      try {
        if (this.future != null)
          return this.future.get(param1Long, TimeUnit.MILLISECONDS); 
      } catch (ExecutionException executionException) {
        RobotLog.ee("ThreadPool", executionException, "singleton threw ExecutionException");
      } catch (TimeoutException timeoutException) {
        param1Long = this.nsDeadline;
        if (param1Long > 0L && param1Long < System.nanoTime())
          ThreadPool.Singleton.access$102(this.singleton, false); 
      } 
      return null;
    }
    
    public void setFuture(Future<T> param1Future) {
      this.future = param1Future;
    }
  }
  
  public static interface ThreadBorrowable {
    boolean canBorrowThread(Thread param1Thread);
  }
  
  static class ThreadFactoryImpl implements ThreadFactory {
    final ThreadPool.ContainerOfThreads container;
    
    final ThreadFactory threadFactory = Executors.defaultThreadFactory();
    
    ThreadFactoryImpl(ThreadPool.ContainerOfThreads param1ContainerOfThreads) {
      this.container = param1ContainerOfThreads;
    }
    
    public Thread newThread(final Runnable runUserCode) {
      runUserCode = this.threadFactory.newThread(new Runnable() {
            public void run() {
              ThreadPool.noteTID(Thread.currentThread(), Process.myTid());
              try {
                runUserCode.run();
                return;
              } finally {
                ThreadPool.ThreadFactoryImpl.this.container.noteFinishedThread(Thread.currentThread());
                ThreadPool.removeTID(Thread.currentThread());
              } 
            }
          });
      this.container.noteNewThread((Thread)runUserCode);
      return (Thread)runUserCode;
    }
  }
  
  class null implements Runnable {
    public void run() {
      ThreadPool.noteTID(Thread.currentThread(), Process.myTid());
      try {
        runUserCode.run();
        return;
      } finally {
        this.this$0.container.noteFinishedThread(Thread.currentThread());
        ThreadPool.removeTID(Thread.currentThread());
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\ThreadPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */