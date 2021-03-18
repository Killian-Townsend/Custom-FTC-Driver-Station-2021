package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Application;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public abstract class WifiStartStoppable {
  public boolean DEBUG = true;
  
  public boolean DEBUG_VERBOSE = false;
  
  private final ReentrantLock completionLock = new ReentrantLock();
  
  private Semaphore completionSemaphore = new Semaphore(0);
  
  private boolean completionSuccess = true;
  
  protected ActionListenerFailure failureReason = ActionListenerFailure.UNKNOWN;
  
  protected int startCount = 0;
  
  protected final Object startStopLock = new Object();
  
  protected final WifiDirectAgent wifiDirectAgent = (WifiDirectAgent)this;
  
  protected final StartResult wifiDirectAgentStarted = new StartResult();
  
  protected WifiStartStoppable(int paramInt) {}
  
  protected WifiStartStoppable(WifiDirectAgent paramWifiDirectAgent) {}
  
  protected boolean callDoStart() {
    return ((Boolean)trace("doStart", this.DEBUG, new Func<Boolean>() {
          public Boolean value() {
            return WifiStartStoppable.this.<Boolean>lockCompletion(Boolean.valueOf(false), new Func<Boolean>() {
                  public Boolean value() {
                    try {
                      boolean bool = WifiStartStoppable.this.doStart();
                      return Boolean.valueOf(bool);
                    } catch (InterruptedException interruptedException) {
                      Thread.currentThread().interrupt();
                      return Boolean.valueOf(false);
                    } 
                  }
                });
          }
        })).booleanValue();
  }
  
  protected void callDoStop() {
    trace("doStop", this.DEBUG, new Runnable() {
          public void run() {
            WifiStartStoppable.this.lockCompletion(null, new Func<Void>() {
                  public Void value() {
                    try {
                      WifiStartStoppable.this.doStop();
                    } catch (InterruptedException interruptedException) {
                      Thread.currentThread().interrupt();
                    } 
                    return null;
                  }
                });
          }
        });
  }
  
  protected abstract boolean doStart() throws InterruptedException;
  
  protected abstract void doStop() throws InterruptedException;
  
  public ActionListenerFailure getActionListenerFailureReason() {
    return this.failureReason;
  }
  
  public abstract String getTag();
  
  public WifiDirectAgent getWifiDirectAgent() {
    return this.wifiDirectAgent;
  }
  
  public void internalStop() {
    try {
    
    } finally {
      if (this.DEBUG_VERBOSE)
        RobotLog.vv(getTag(), "...stop()"); 
    } 
  }
  
  protected boolean isCompletionLockHeld() {
    return this.completionLock.isHeldByCurrentThread();
  }
  
  protected <T> T lockCompletion(T paramT, Func<T> paramFunc) {
    T t = paramT;
    try {
      this.completionLock.lockInterruptibly();
      try {
        return (T)object;
      } finally {
        t = paramT;
        this.completionLock.unlock();
        t = paramT;
      } 
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
      return t;
    } 
  }
  
  protected boolean receivedCompletionInterrupt(InterruptedException paramInterruptedException) {
    Assert.assertTrue(isCompletionLockHeld());
    return receivedInterrupt(paramInterruptedException);
  }
  
  protected boolean receivedInterrupt(InterruptedException paramInterruptedException) {
    Thread.currentThread().interrupt();
    return false;
  }
  
  protected void releaseCompletion(boolean paramBoolean) {
    Assert.assertNotNull(this.wifiDirectAgent);
    Assert.assertTrue(this.wifiDirectAgent.isLooperThread());
    Assert.assertFalse(isCompletionLockHeld());
    this.completionSuccess = paramBoolean;
    this.completionSemaphore.release();
  }
  
  protected boolean resetCompletion() {
    Assert.assertTrue(isCompletionLockHeld());
    this.completionSuccess = true;
    this.completionSemaphore = new Semaphore(0);
    return true;
  }
  
  public void restart() {
    if (this.DEBUG)
      RobotLog.vv(getTag(), "restart()..."); 
    try {
    
    } finally {
      if (this.DEBUG)
        RobotLog.vv(getTag(), "...restart()"); 
    } 
  }
  
  public StartResult start() {
    StartResult startResult = new StartResult();
    return start(startResult) ? startResult : null;
  }
  
  public boolean start(StartResult paramStartResult) {
    try {
    
    } finally {
      if (this.DEBUG_VERBOSE)
        RobotLog.vv(getTag(), "...start()"); 
    } 
  }
  
  protected boolean startIsIdempotent() {
    return false;
  }
  
  protected boolean startIsRefCounted() {
    return true;
  }
  
  public void stop(StartResult paramStartResult) {
    boolean bool1;
    WifiStartStoppable wifiStartStoppable = paramStartResult.getStartStoppable();
    boolean bool2 = false;
    if ((wifiStartStoppable == null && paramStartResult.getStartCount() == 0) || paramStartResult.getStartStoppable() == this) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    Assert.assertTrue(bool1);
    synchronized (this.startStopLock) {
      while (paramStartResult.getStartCount() > 0) {
        paramStartResult.decrementStartCount();
        internalStop();
      } 
      bool1 = bool2;
      if (paramStartResult.getStartCount() == 0)
        bool1 = true; 
      Assert.assertTrue(bool1);
      return;
    } 
  }
  
  protected void stopDueToFailure() {
    internalStop();
  }
  
  public void terminate() {
    synchronized (this.startStopLock) {
      if (this.startCount > 0) {
        this.startCount = 1;
        internalStop();
      } 
      return;
    } 
  }
  
  protected <T> T trace(String paramString, boolean paramBoolean, Func<T> paramFunc) {
    if (paramBoolean)
      RobotLog.vv(getTag(), "%s()...", new Object[] { paramString }); 
    try {
      return (T)paramFunc.value();
    } finally {
      if (paramBoolean)
        RobotLog.vv(getTag(), "...%s()", new Object[] { paramString }); 
    } 
  }
  
  protected void trace(String paramString, Runnable paramRunnable) {
    trace(paramString, true, paramRunnable);
  }
  
  protected void trace(String paramString, boolean paramBoolean, Runnable paramRunnable) {
    if (paramBoolean)
      RobotLog.vv(getTag(), "%s()...", new Object[] { paramString }); 
    try {
      paramRunnable.run();
      return;
    } finally {
      if (paramBoolean)
        RobotLog.vv(getTag(), "...%s()", new Object[] { paramString }); 
    } 
  }
  
  protected boolean waitForCompletion() throws InterruptedException {
    Assert.assertNotNull(this.wifiDirectAgent);
    Assert.assertFalse(this.wifiDirectAgent.isLooperThread());
    Assert.assertTrue(isCompletionLockHeld());
    this.completionSemaphore.acquire();
    return this.completionSuccess;
  }
  
  public enum ActionListenerFailure {
    BUSY, ERROR, NO_SERVICE_REQUESTS, P2P_UNSUPPORTED, UNKNOWN, WIFI_DISABLED;
    
    static {
      ERROR = new ActionListenerFailure("ERROR", 2);
      BUSY = new ActionListenerFailure("BUSY", 3);
      NO_SERVICE_REQUESTS = new ActionListenerFailure("NO_SERVICE_REQUESTS", 4);
      ActionListenerFailure actionListenerFailure = new ActionListenerFailure("WIFI_DISABLED", 5);
      WIFI_DISABLED = actionListenerFailure;
      $VALUES = new ActionListenerFailure[] { UNKNOWN, P2P_UNSUPPORTED, ERROR, BUSY, NO_SERVICE_REQUESTS, actionListenerFailure };
    }
    
    public static ActionListenerFailure from(int param1Int, WifiDirectAgent param1WifiDirectAgent) {
      if (param1Int != 0) {
        boolean bool = true;
        if (param1Int != 1) {
          WifiState wifiState;
          if (param1Int != 2)
            return (param1Int != 3) ? UNKNOWN : NO_SERVICE_REQUESTS; 
          if (param1WifiDirectAgent != null) {
            wifiState = param1WifiDirectAgent.getWifiState();
          } else {
            wifiState = WifiState.UNKNOWN;
          } 
          param1Int = bool;
          if (wifiState != WifiState.DISABLED)
            if (wifiState == WifiState.DISABLING) {
              param1Int = bool;
            } else {
              param1Int = 0;
            }  
          return (param1Int != 0) ? WIFI_DISABLED : BUSY;
        } 
        return P2P_UNSUPPORTED;
      } 
      return ERROR;
    }
    
    public String toString() {
      Application application = AppUtil.getInstance().getApplication();
      int i = WifiStartStoppable.null.$SwitchMap$org$firstinspires$ftc$robotcore$internal$network$WifiStartStoppable$ActionListenerFailure[ordinal()];
      return (i != 1) ? ((i != 2) ? ((i != 3) ? ((i != 4) ? ((i != 5) ? application.getString(R.string.actionlistenerfailure_unknown) : application.getString(R.string.actionlistenerfailure_nosevicerequests)) : application.getString(R.string.actionlistenerfailure_error)) : application.getString(R.string.actionlistenerfailure_busy)) : application.getString(R.string.actionlistenerfailure_nowifi)) : application.getString(R.string.actionlistenerfailure_nop2p);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiStartStoppable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */