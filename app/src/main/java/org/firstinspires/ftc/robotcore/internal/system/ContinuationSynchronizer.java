package org.firstinspires.ftc.robotcore.internal.system;

import android.text.TextUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.ThrowingRunnable;

public class ContinuationSynchronizer<T> {
  public static String TAG = "ContinuationSynchronizer";
  
  protected final Deadline deadline;
  
  protected boolean enableTrace = false;
  
  protected boolean isFinished;
  
  protected final CountDownLatch latch;
  
  protected final Object lock = new Object();
  
  protected Tracer tracer;
  
  protected T value;
  
  public ContinuationSynchronizer() {
    this(new Deadline(2147483647L, TimeUnit.SECONDS));
  }
  
  public ContinuationSynchronizer(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean) {
    this(new Deadline(paramLong, paramTimeUnit), paramBoolean);
  }
  
  public ContinuationSynchronizer(long paramLong, TimeUnit paramTimeUnit, boolean paramBoolean, T paramT) {
    this(new Deadline(paramLong, paramTimeUnit), paramBoolean, paramT);
  }
  
  public ContinuationSynchronizer(Deadline paramDeadline) {
    this(paramDeadline, true, (T)null);
  }
  
  public ContinuationSynchronizer(Deadline paramDeadline, boolean paramBoolean) {
    this(paramDeadline, paramBoolean, (T)null);
  }
  
  public ContinuationSynchronizer(Deadline paramDeadline, boolean paramBoolean, T paramT) {
    this.tracer = Tracer.create(TAG, paramBoolean);
    this.deadline = paramDeadline;
    this.latch = new CountDownLatch(1);
    this.isFinished = false;
    this.value = paramT;
    this.enableTrace = paramBoolean;
  }
  
  public void await() throws InterruptedException {
    if (!this.deadline.await(this.latch))
      this.tracer.traceError("deadline expired during await()", new Object[0]); 
  }
  
  public void await(String paramString) throws InterruptedException {
    Tracer tracer = this.tracer;
    tracer.trace(tracer.format("awaiting(%s)", new Object[] { paramString }), new ThrowingRunnable<InterruptedException>() {
          public void run() throws InterruptedException {
            if (!ContinuationSynchronizer.this.deadline.await(ContinuationSynchronizer.this.latch))
              ContinuationSynchronizer.this.tracer.traceError("deadline expired during await()", new Object[0]); 
          }
        });
  }
  
  public void finish(T paramT) {
    finish("", paramT);
  }
  
  public void finish(String paramString, final T value) {
    if (TextUtils.isEmpty(paramString)) {
      paramString = "";
    } else {
      StringBuilder stringBuilder1 = new StringBuilder();
      stringBuilder1.append("\"");
      stringBuilder1.append(paramString);
      stringBuilder1.append("\": ");
      paramString = stringBuilder1.toString();
    } 
    Tracer tracer = this.tracer;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("finish(");
    stringBuilder.append(paramString);
    stringBuilder.append(value);
    stringBuilder.append(")");
    tracer.trace(stringBuilder.toString(), new Runnable() {
          public void run() {
            synchronized (ContinuationSynchronizer.this.lock) {
              if (ContinuationSynchronizer.this.isFinished) {
                if (ContinuationSynchronizer.this.value == null)
                  ContinuationSynchronizer.this.value = (T)value; 
              } else {
                ContinuationSynchronizer.this.value = (T)value;
              } 
              ContinuationSynchronizer.this.isFinished = true;
              ContinuationSynchronizer.this.deadline.expire();
              ContinuationSynchronizer.this.latch.countDown();
              return;
            } 
          }
        });
  }
  
  public Deadline getDeadline() {
    return this.deadline;
  }
  
  public T getValue() {
    return this.value;
  }
  
  public boolean isFinished() {
    return this.isFinished;
  }
  
  public boolean isSuccessful() {
    return (this.value != null);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\ContinuationSynchronizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */