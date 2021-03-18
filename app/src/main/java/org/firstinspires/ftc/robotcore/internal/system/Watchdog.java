package org.firstinspires.ftc.robotcore.internal.system;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Watchdog {
  private static final String TAG = "Watchdog";
  
  private boolean alreadyGrowled;
  
  private Runnable bark;
  
  private Deadline deadline;
  
  private ScheduledExecutorService executorService;
  
  private ScheduledFuture<?> futureTask;
  
  private Runnable growl;
  
  private int growlTime;
  
  private int period;
  
  private long timeout;
  
  private TimeUnit unit;
  
  public Watchdog(Runnable paramRunnable, int paramInt, long paramLong, TimeUnit paramTimeUnit) {
    this.period = paramInt;
    this.timeout = paramLong;
    this.growlTime = 0;
    this.unit = paramTimeUnit;
    this.bark = paramRunnable;
    this.growl = null;
    this.deadline = null;
    this.executorService = null;
    this.futureTask = null;
  }
  
  public Watchdog(Runnable paramRunnable1, Runnable paramRunnable2, int paramInt1, int paramInt2, long paramLong, TimeUnit paramTimeUnit) {
    this.period = paramInt2;
    this.timeout = paramLong;
    this.unit = paramTimeUnit;
    this.bark = paramRunnable1;
    this.growl = paramRunnable2;
    this.growlTime = paramInt1;
    this.deadline = null;
    this.executorService = null;
    this.futureTask = null;
  }
  
  protected void checkDog() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   6: ifnonnull -> 19
    //   9: ldc 'Watchdog'
    //   11: ldc 'Checking a dog that is not alive.'
    //   13: invokestatic ww : (Ljava/lang/String;Ljava/lang/String;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: aload_0
    //   20: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   23: invokevirtual hasExpired : ()Z
    //   26: ifeq -> 45
    //   29: aload_0
    //   30: getfield bark : Ljava/lang/Runnable;
    //   33: invokeinterface run : ()V
    //   38: aload_0
    //   39: invokevirtual euthanize : ()V
    //   42: goto -> 93
    //   45: aload_0
    //   46: getfield growl : Ljava/lang/Runnable;
    //   49: ifnull -> 93
    //   52: aload_0
    //   53: getfield alreadyGrowled : Z
    //   56: ifne -> 93
    //   59: aload_0
    //   60: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   63: aload_0
    //   64: getfield unit : Ljava/util/concurrent/TimeUnit;
    //   67: invokevirtual timeRemaining : (Ljava/util/concurrent/TimeUnit;)J
    //   70: aload_0
    //   71: getfield growlTime : I
    //   74: i2l
    //   75: lcmp
    //   76: ifgt -> 93
    //   79: aload_0
    //   80: getfield growl : Ljava/lang/Runnable;
    //   83: invokeinterface run : ()V
    //   88: aload_0
    //   89: iconst_1
    //   90: putfield alreadyGrowled : Z
    //   93: aload_0
    //   94: monitorexit
    //   95: return
    //   96: astore_1
    //   97: aload_0
    //   98: monitorexit
    //   99: aload_1
    //   100: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	96	finally
    //   19	42	96	finally
    //   45	93	96	finally
  }
  
  public void euthanize() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aconst_null
    //   4: putfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   7: aload_0
    //   8: getfield executorService : Ljava/util/concurrent/ScheduledExecutorService;
    //   11: ifnull -> 29
    //   14: aload_0
    //   15: getfield executorService : Ljava/util/concurrent/ScheduledExecutorService;
    //   18: invokeinterface shutdownNow : ()Ljava/util/List;
    //   23: pop
    //   24: aload_0
    //   25: aconst_null
    //   26: putfield executorService : Ljava/util/concurrent/ScheduledExecutorService;
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	32	finally
  }
  
  public boolean isRunning() {
    return (this.executorService != null);
  }
  
  public void start() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   6: ifnull -> 19
    //   9: ldc 'Watchdog'
    //   11: ldc 'Don't start the same watchdog twice'
    //   13: invokestatic ee : (Ljava/lang/String;Ljava/lang/String;)V
    //   16: aload_0
    //   17: monitorexit
    //   18: return
    //   19: aload_0
    //   20: new org/firstinspires/ftc/robotcore/internal/system/Deadline
    //   23: dup
    //   24: aload_0
    //   25: getfield timeout : J
    //   28: aload_0
    //   29: getfield unit : Ljava/util/concurrent/TimeUnit;
    //   32: invokespecial <init> : (JLjava/util/concurrent/TimeUnit;)V
    //   35: putfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   38: iconst_1
    //   39: ldc 'Watchdog'
    //   41: invokestatic newScheduledExecutor : (ILjava/lang/String;)Lcom/qualcomm/robotcore/util/ThreadPool$RecordingScheduledExecutor;
    //   44: astore_1
    //   45: aload_0
    //   46: aload_1
    //   47: putfield executorService : Ljava/util/concurrent/ScheduledExecutorService;
    //   50: aload_0
    //   51: aload_1
    //   52: new org/firstinspires/ftc/robotcore/internal/system/Watchdog$WatchdogPeriodic
    //   55: dup
    //   56: aload_0
    //   57: aconst_null
    //   58: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/system/Watchdog;Lorg/firstinspires/ftc/robotcore/internal/system/Watchdog$1;)V
    //   61: aload_0
    //   62: getfield period : I
    //   65: i2l
    //   66: aload_0
    //   67: getfield period : I
    //   70: i2l
    //   71: aload_0
    //   72: getfield unit : Ljava/util/concurrent/TimeUnit;
    //   75: invokeinterface scheduleAtFixedRate : (Ljava/lang/Runnable;JJLjava/util/concurrent/TimeUnit;)Ljava/util/concurrent/ScheduledFuture;
    //   80: putfield futureTask : Ljava/util/concurrent/ScheduledFuture;
    //   83: aload_0
    //   84: iconst_0
    //   85: putfield alreadyGrowled : Z
    //   88: aload_0
    //   89: monitorexit
    //   90: return
    //   91: astore_1
    //   92: aload_0
    //   93: monitorexit
    //   94: aload_1
    //   95: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	91	finally
    //   19	88	91	finally
  }
  
  public void stroke() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   6: ifnull -> 19
    //   9: aload_0
    //   10: getfield deadline : Lorg/firstinspires/ftc/robotcore/internal/system/Deadline;
    //   13: invokevirtual reset : ()V
    //   16: goto -> 30
    //   19: ldc 'Watchdog'
    //   21: ldc 'The dog was stroked after it was euthanized.'
    //   23: invokestatic ii : (Ljava/lang/String;Ljava/lang/String;)V
    //   26: aload_0
    //   27: invokevirtual start : ()V
    //   30: aload_0
    //   31: iconst_0
    //   32: putfield alreadyGrowled : Z
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: astore_1
    //   39: aload_0
    //   40: monitorexit
    //   41: aload_1
    //   42: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	38	finally
    //   19	30	38	finally
    //   30	35	38	finally
  }
  
  private class WatchdogPeriodic implements Runnable {
    private WatchdogPeriodic() {}
    
    public void run() {
      Watchdog.this.checkDog();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\Watchdog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */