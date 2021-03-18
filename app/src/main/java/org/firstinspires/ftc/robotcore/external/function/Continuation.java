package org.firstinspires.ftc.robotcore.external.function;

import android.os.Handler;
import android.os.Looper;
import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.Executor;
import org.firstinspires.ftc.robotcore.internal.system.MemberwiseCloneable;

public class Continuation<T> {
  public static final String TAG = Continuation.class.getSimpleName();
  
  protected Dispatcher<T> dispatcher;
  
  protected final T target;
  
  protected Continuation(T paramT) {
    this.target = paramT;
  }
  
  public static <T> Continuation<T> create(Handler paramHandler, T paramT) {
    return (new Continuation<T>(paramT)).createHandlerDispatcher(paramHandler);
  }
  
  public static <T> Continuation<T> create(Executor paramExecutor, T paramT) {
    return (new Continuation<T>(paramT)).createExecutorDispatcher(paramExecutor);
  }
  
  public static <T> Continuation<T> createTrivial(T paramT) {
    return (new Continuation<T>(paramT)).createTrivialDispatcher();
  }
  
  public boolean canBorrowThread(Thread paramThread) {
    if (this.dispatcher.isTrivial())
      return true; 
    if (this.dispatcher.isExecutor()) {
      ExecutorDispatcher executorDispatcher = (ExecutorDispatcher)this.dispatcher;
      if (executorDispatcher.getExecutor() instanceof ThreadPool.ThreadBorrowable)
        return ((ThreadPool.ThreadBorrowable)executorDispatcher.getExecutor()).canBorrowThread(paramThread); 
    } 
    return false;
  }
  
  protected Continuation<T> createExecutorDispatcher(Executor paramExecutor) {
    this.dispatcher = new ExecutorDispatcher<T>(this, paramExecutor);
    return this;
  }
  
  public <U> Continuation<U> createForNewTarget(U paramU) {
    return (new Continuation((T)paramU)).setDispatcher(this.dispatcher.copyAndCast());
  }
  
  protected Continuation<T> createHandlerDispatcher(Handler paramHandler) {
    Handler handler = paramHandler;
    if (paramHandler == null)
      if (Looper.myLooper() != null) {
        handler = new Handler();
      } else {
        handler = paramHandler;
        if (Looper.getMainLooper() != null)
          handler = new Handler(Looper.getMainLooper()); 
      }  
    if (handler != null) {
      this.dispatcher = new HandlerDispatcher<T>(this, handler);
      return this;
    } 
    throw new IllegalArgumentException("handler is null, but no looper on this thread or main thread");
  }
  
  protected Continuation<T> createTrivialDispatcher() {
    this.dispatcher = new TrivialDispatcher<T>(this);
    return this;
  }
  
  public void dispatch(ContinuationResult<? super T> paramContinuationResult) {
    this.dispatcher.dispatch(paramContinuationResult);
  }
  
  public void dispatchHere(ContinuationResult<? super T> paramContinuationResult) {
    paramContinuationResult.handle(this.target);
  }
  
  public Dispatcher<T> getDispatcher() {
    return this.dispatcher;
  }
  
  public Handler getHandler() {
    return ((HandlerDispatcher)this.dispatcher).getHandler();
  }
  
  public T getTarget() {
    return this.target;
  }
  
  public boolean isDispatchSynchronous() {
    return isTrivial();
  }
  
  public boolean isHandler() {
    return this.dispatcher.isHandler();
  }
  
  public boolean isTrivial() {
    return this.dispatcher.isTrivial();
  }
  
  protected Continuation<T> setDispatcher(Dispatcher<T> paramDispatcher) {
    this.dispatcher = paramDispatcher;
    paramDispatcher.setContinuation(this);
    return this;
  }
  
  protected static abstract class Dispatcher<S> extends MemberwiseCloneable<Dispatcher<S>> {
    protected Continuation<S> continuation;
    
    public Dispatcher(Continuation<S> param1Continuation) {
      this.continuation = param1Continuation;
    }
    
    public <U> Dispatcher<U> copyAndCast() {
      Dispatcher<U> dispatcher = (Dispatcher)memberwiseClone();
      dispatcher.continuation = null;
      return dispatcher;
    }
    
    public abstract void dispatch(ContinuationResult<? super S> param1ContinuationResult);
    
    public boolean isExecutor() {
      return false;
    }
    
    public boolean isHandler() {
      return false;
    }
    
    public boolean isTrivial() {
      return false;
    }
    
    public void setContinuation(Continuation<S> param1Continuation) {
      this.continuation = param1Continuation;
    }
  }
  
  protected static class ExecutorDispatcher<S> extends Dispatcher<S> {
    private final Executor executor;
    
    public ExecutorDispatcher(Continuation<S> param1Continuation, Executor param1Executor) {
      super(param1Continuation);
      this.executor = param1Executor;
    }
    
    public void dispatch(final ContinuationResult<? super S> consumer) {
      final T capturedTarget = this.continuation.target;
      this.executor.execute(new Runnable() {
            public void run() {
              consumer.handle(capturedTarget);
            }
          });
    }
    
    public Executor getExecutor() {
      return this.executor;
    }
    
    public boolean isExecutor() {
      return true;
    }
  }
  
  class null implements Runnable {
    public void run() {
      consumer.handle(capturedTarget);
    }
  }
  
  protected static class HandlerDispatcher<S> extends Dispatcher<S> {
    private final Handler handler;
    
    public HandlerDispatcher(Continuation<S> param1Continuation, Handler param1Handler) {
      super(param1Continuation);
      this.handler = param1Handler;
    }
    
    public void dispatch(final ContinuationResult<? super S> consumer) {
      final T capturedTarget = this.continuation.target;
      this.handler.post(new Runnable() {
            public void run() {
              consumer.handle(capturedTarget);
            }
          });
    }
    
    public Handler getHandler() {
      return this.handler;
    }
    
    public boolean isHandler() {
      return true;
    }
  }
  
  class null implements Runnable {
    public void run() {
      consumer.handle(capturedTarget);
    }
  }
  
  protected static class TrivialDispatcher<S> extends Dispatcher<S> {
    public TrivialDispatcher(Continuation<S> param1Continuation) {
      super(param1Continuation);
    }
    
    public void dispatch(ContinuationResult<? super S> param1ContinuationResult) {
      param1ContinuationResult.handle((S)this.continuation.target);
    }
    
    public boolean isTrivial() {
      return true;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\external\function\Continuation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */