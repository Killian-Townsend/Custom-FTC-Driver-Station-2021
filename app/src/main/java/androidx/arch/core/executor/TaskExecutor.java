package androidx.arch.core.executor;

public abstract class TaskExecutor {
  public abstract void executeOnDiskIO(Runnable paramRunnable);
  
  public void executeOnMainThread(Runnable paramRunnable) {
    if (isMainThread()) {
      paramRunnable.run();
      return;
    } 
    postToMainThread(paramRunnable);
  }
  
  public abstract boolean isMainThread();
  
  public abstract void postToMainThread(Runnable paramRunnable);
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\androidx\arch\core\executor\TaskExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */