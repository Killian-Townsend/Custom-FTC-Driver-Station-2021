package org.firstinspires.ftc.robotcore.internal.system;

import com.qualcomm.robotcore.util.ThreadPool;
import java.util.concurrent.Executor;

public class ThreadBorrowingExecutor implements Executor, ThreadPool.ThreadBorrowable {
  protected final Executor delegate;
  
  public ThreadBorrowingExecutor(Executor paramExecutor) {
    this.delegate = paramExecutor;
  }
  
  public boolean canBorrowThread(Thread paramThread) {
    return true;
  }
  
  public void execute(Runnable paramRunnable) {
    this.delegate.execute(paramRunnable);
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\system\ThreadBorrowingExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */