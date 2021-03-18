package org.firstinspires.ftc.robotcore.internal.network;

import android.os.Handler;
import android.os.Looper;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class CallbackLooper {
  public static final String TAG = "CallbackLooper";
  
  protected static final ThreadLocal<CallbackLooper> tls = new ThreadLocal<CallbackLooper>();
  
  protected ExecutorService executorService = null;
  
  protected Handler handler = null;
  
  protected Looper looper = null;
  
  protected Thread thread = null;
  
  public static CallbackLooper getDefault() {
    return InstanceHolder.theInstance;
  }
  
  public static boolean isLooperThread() {
    return (tls.get() != null);
  }
  
  public Handler getHandler() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public Looper getLooper() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield looper : Landroid/os/Looper;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void post(Runnable paramRunnable) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield handler : Landroid/os/Handler;
    //   6: aload_1
    //   7: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   10: pop
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
  
  public void start() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   6: ifnonnull -> 59
    //   9: aload_0
    //   10: ldc 'CallbackLooper'
    //   12: invokestatic newSingleThreadExecutor : (Ljava/lang/String;)Ljava/util/concurrent/ExecutorService;
    //   15: putfield executorService : Ljava/util/concurrent/ExecutorService;
    //   18: new java/util/concurrent/CountDownLatch
    //   21: dup
    //   22: iconst_1
    //   23: invokespecial <init> : (I)V
    //   26: astore_1
    //   27: aload_0
    //   28: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   31: new org/firstinspires/ftc/robotcore/internal/network/CallbackLooper$1
    //   34: dup
    //   35: aload_0
    //   36: aload_1
    //   37: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/network/CallbackLooper;Ljava/util/concurrent/CountDownLatch;)V
    //   40: invokeinterface submit : (Ljava/lang/Runnable;)Ljava/util/concurrent/Future;
    //   45: pop
    //   46: aload_1
    //   47: invokevirtual await : ()V
    //   50: goto -> 59
    //   53: invokestatic currentThread : ()Ljava/lang/Thread;
    //   56: invokevirtual interrupt : ()V
    //   59: aload_0
    //   60: monitorexit
    //   61: return
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    //   67: astore_1
    //   68: goto -> 53
    // Exception table:
    //   from	to	target	type
    //   2	46	62	finally
    //   46	50	67	java/lang/InterruptedException
    //   46	50	62	finally
    //   53	59	62	finally
  }
  
  public void stop() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   6: ifnull -> 64
    //   9: aload_0
    //   10: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   13: invokeinterface shutdownNow : ()Ljava/util/List;
    //   18: pop
    //   19: aload_0
    //   20: getfield executorService : Ljava/util/concurrent/ExecutorService;
    //   23: ldc2_w 3
    //   26: getstatic java/util/concurrent/TimeUnit.SECONDS : Ljava/util/concurrent/TimeUnit;
    //   29: ldc 'CallbackLooper'
    //   31: invokestatic awaitTermination : (Ljava/util/concurrent/ExecutorService;JLjava/util/concurrent/TimeUnit;Ljava/lang/String;)Z
    //   34: pop
    //   35: goto -> 44
    //   38: invokestatic currentThread : ()Ljava/lang/Thread;
    //   41: invokevirtual interrupt : ()V
    //   44: aload_0
    //   45: aconst_null
    //   46: putfield executorService : Ljava/util/concurrent/ExecutorService;
    //   49: aload_0
    //   50: aconst_null
    //   51: putfield looper : Landroid/os/Looper;
    //   54: aload_0
    //   55: aconst_null
    //   56: putfield handler : Landroid/os/Handler;
    //   59: aload_0
    //   60: aconst_null
    //   61: putfield thread : Ljava/lang/Thread;
    //   64: aload_0
    //   65: monitorexit
    //   66: return
    //   67: astore_1
    //   68: aload_0
    //   69: monitorexit
    //   70: aload_1
    //   71: athrow
    //   72: astore_1
    //   73: goto -> 38
    // Exception table:
    //   from	to	target	type
    //   2	19	67	finally
    //   19	35	72	java/lang/InterruptedException
    //   19	35	67	finally
    //   38	44	67	finally
    //   44	64	67	finally
  }
  
  protected static class InstanceHolder {
    public static final CallbackLooper theInstance;
    
    static {
      CallbackLooper callbackLooper = new CallbackLooper();
      theInstance = callbackLooper;
      callbackLooper.start();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\CallbackLooper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */