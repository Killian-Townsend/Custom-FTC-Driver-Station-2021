package org.firstinspires.ftc.robotcore.internal.system;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class SynchronousResultReceiver<T> extends ResultReceiver {
  private final BlockingQueue<T> resultQueue;
  
  private final String tag;
  
  public SynchronousResultReceiver(int paramInt, String paramString, Handler paramHandler) {
    super(paramHandler);
    this.resultQueue = new ArrayBlockingQueue<T>(paramInt);
    this.tag = paramString;
  }
  
  public final T awaitResult(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException, TimeoutException {
    paramTimeUnit = (TimeUnit)this.resultQueue.poll(paramLong, paramTimeUnit);
    if (paramTimeUnit != null)
      return (T)paramTimeUnit; 
    throw new TimeoutException();
  }
  
  protected final void onReceiveResult(int paramInt, Bundle paramBundle) {
    if ((this.resultQueue.offer(provideResult(paramInt, paramBundle)) ^ true) != 0)
      RobotLog.ww(this.tag, "The queue is full! Ignoring the result we just received."); 
  }
  
  protected abstract T provideResult(int paramInt, Bundle paramBundle);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\SynchronousResultReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */