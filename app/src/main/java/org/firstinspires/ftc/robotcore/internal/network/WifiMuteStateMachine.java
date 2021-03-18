package org.firstinspires.ftc.robotcore.internal.network;

import android.app.Activity;
import android.net.wifi.WifiManager;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.Event;
import org.firstinspires.ftc.robotcore.external.State;
import org.firstinspires.ftc.robotcore.external.StateMachine;
import org.firstinspires.ftc.robotcore.external.StateTransition;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.CallbackRegistrar;
import org.firstinspires.ftc.robotcore.internal.system.Watchdog;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class WifiMuteStateMachine extends StateMachine {
  private static final String TAG = "WifiMuteStateMachine";
  
  private static final int WIFI_MUTE_PERIOD = 1;
  
  private static final int WIFI_MUTE_TIMEOUT = 600;
  
  private static final int WIFI_MUTE_WARN = 10;
  
  private Activity activity;
  
  private final WifiState blackhole = new WifiState();
  
  protected final CallbackRegistrar<Callback> callbacks = new CallbackRegistrar();
  
  private final TimeoutSuspended timeoutSuspended = new TimeoutSuspended();
  
  private WifiManager wifiManager;
  
  private final WifiMuteFragment wifiMuteFragment = new WifiMuteFragment();
  
  protected Watchdog wifiMuzzleWatchdog = new Watchdog(new WifiMuteRunnable(), new WifiGrowlRunnable(), 10, 1, 600L, TimeUnit.SECONDS);
  
  private final WifiOff wifiOff = new WifiOff();
  
  private final WifiOn wifiOn = new WifiOn();
  
  private final WifiPendingOff wifiPendingOff = new WifiPendingOff();
  
  public WifiMuteStateMachine() {
    this.wifiMuteFragment.setStateMachine(this);
    this.wifiManager = (WifiManager)AppUtil.getDefContext().getApplicationContext().getSystemService("wifi");
    Activity activity = AppUtil.getInstance().getActivity();
    this.activity = activity;
    activity.getFragmentManager().beginTransaction().add(16908290, this.wifiMuteFragment).hide(this.wifiMuteFragment).commit();
  }
  
  protected void enableWifi(boolean paramBoolean) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Set wifi enable ");
    stringBuilder.append(paramBoolean);
    RobotLog.ii("WifiMuteStateMachine", stringBuilder.toString());
    if (paramBoolean) {
      AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, AppUtil.getDefContext().getString(R.string.toastEnableWifi));
    } else {
      AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, AppUtil.getDefContext().getString(R.string.toastDisableWifi));
    } 
    this.wifiManager.setWifiEnabled(paramBoolean);
  }
  
  public void initialize() {
    addTransition(new StateTransition(this.wifiOn, WifiMuteEvent.USER_ACTIVITY, this.wifiOn));
    addTransition(new StateTransition(this.wifiOn, WifiMuteEvent.WATCHDOG_WARNING, this.wifiPendingOff));
    addTransition(new StateTransition(this.wifiOn, WifiMuteEvent.RUNNING_OPMODE, this.timeoutSuspended));
    addTransition(new StateTransition(this.wifiOn, WifiMuteEvent.ACTIVITY_STOP, this.wifiOff));
    addTransition(new StateTransition(this.wifiOn, WifiMuteEvent.ACTIVITY_OTHER, this.timeoutSuspended));
    addTransition(new StateTransition(this.wifiPendingOff, WifiMuteEvent.USER_ACTIVITY, this.wifiOn));
    addTransition(new StateTransition(this.wifiPendingOff, WifiMuteEvent.WATCHDOG_TIMEOUT, this.wifiOff));
    addTransition(new StateTransition(this.timeoutSuspended, WifiMuteEvent.STOPPED_OPMODE, this.wifiOn));
    addTransition(new StateTransition(this.timeoutSuspended, WifiMuteEvent.ACTIVITY_START, this.wifiOn));
    addTransition(new StateTransition(this.wifiOff, WifiMuteEvent.USER_ACTIVITY, this.wifiOn));
    addTransition(new StateTransition(this.wifiOff, WifiMuteEvent.ACTIVITY_START, this.wifiOn));
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("State Machine ");
    stringBuilder.append(toString());
    RobotLog.ii("WifiMuteStateMachine", stringBuilder.toString());
  }
  
  protected boolean isWifiEnabled() {
    return this.wifiManager.isWifiEnabled();
  }
  
  protected Toast makeToast(Activity paramActivity, String paramString) {
    Toast toast = Toast.makeText(paramActivity.getApplicationContext(), paramString, 0);
    ((TextView)toast.getView().findViewById(16908299)).setTextSize(18.0F);
    toast.show();
    return toast;
  }
  
  protected void notifyPendingCancel() {
    this.callbacks.callbacksDo(new Consumer<Callback>() {
          public void accept(WifiMuteStateMachine.Callback param1Callback) {
            param1Callback.onPendingCancel();
          }
        });
  }
  
  protected void notifyPendingOn() {
    this.callbacks.callbacksDo(new Consumer<Callback>() {
          public void accept(WifiMuteStateMachine.Callback param1Callback) {
            param1Callback.onPendingOn();
          }
        });
  }
  
  protected void notifyWifiOff() {
    this.callbacks.callbacksDo(new Consumer<Callback>() {
          public void accept(WifiMuteStateMachine.Callback param1Callback) {
            param1Callback.onWifiOff();
          }
        });
  }
  
  protected void notifyWifiOn() {
    this.callbacks.callbacksDo(new Consumer<Callback>() {
          public void accept(WifiMuteStateMachine.Callback param1Callback) {
            param1Callback.onWifiOn();
          }
        });
  }
  
  public void registerCallback(Callback paramCallback) {
    this.callbacks.registerCallback(paramCallback);
  }
  
  public void start() {
    start(this.blackhole);
    this.wifiMuzzleWatchdog.start();
  }
  
  public void stop() {
    if (this.wifiMuzzleWatchdog.isRunning())
      this.wifiMuzzleWatchdog.euthanize(); 
  }
  
  public void unregisterCallback(Callback paramCallback) {
    this.callbacks.unregisterCallback(paramCallback);
  }
  
  public static interface Callback {
    void onPendingCancel();
    
    void onPendingOn();
    
    void onWifiOff();
    
    void onWifiOn();
  }
  
  private class TimeoutSuspended extends WifiState {
    private TimeoutSuspended() {}
    
    public void onEnter(Event param1Event) {
      super.onEnter(param1Event);
      if (WifiMuteStateMachine.this.wifiMuzzleWatchdog.isRunning())
        WifiMuteStateMachine.this.wifiMuzzleWatchdog.euthanize(); 
    }
    
    public void onExit(Event param1Event) {
      super.onExit(param1Event);
      if (!WifiMuteStateMachine.this.wifiMuzzleWatchdog.isRunning())
        WifiMuteStateMachine.this.wifiMuzzleWatchdog.start(); 
    }
  }
  
  private class WifiGrowlRunnable implements Runnable {
    private WifiGrowlRunnable() {}
    
    public void run() {
      RobotLog.ii("WifiMuteStateMachine", "Watchdog growled");
      WifiMuteStateMachine.this.consumeEvent(WifiMuteEvent.WATCHDOG_WARNING);
    }
  }
  
  private class WifiMuteRunnable implements Runnable {
    private WifiMuteRunnable() {}
    
    public void run() {
      RobotLog.ii("WifiMuteStateMachine", "Watchdog barked");
      WifiMuteStateMachine.this.consumeEvent(WifiMuteEvent.WATCHDOG_TIMEOUT);
    }
  }
  
  private class WifiOff extends WifiState {
    private WifiOff() {}
    
    public void onEnter(Event param1Event) {
      super.onEnter(param1Event);
      WifiMuteStateMachine.this.wifiMuteFragment.displayDisabledMessage();
      if (WifiMuteStateMachine.this.wifiMuzzleWatchdog.isRunning())
        WifiMuteStateMachine.this.wifiMuzzleWatchdog.euthanize(); 
      if (WifiMuteStateMachine.this.isWifiEnabled()) {
        AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, AppUtil.getDefContext().getString(R.string.toastDisableWifi));
        WifiMuteStateMachine.this.enableWifi(false);
        WifiMuteStateMachine.this.notifyWifiOff();
      } 
    }
    
    public void onExit(Event param1Event) {}
  }
  
  private class WifiOn extends WifiState {
    boolean isEnabled = true;
    
    private WifiOn() {}
    
    public void onEnter(Event param1Event) {
      super.onEnter(param1Event);
      if (WifiMuteStateMachine.this.wifiMuzzleWatchdog.isRunning()) {
        WifiMuteStateMachine.this.wifiMuzzleWatchdog.stroke();
      } else {
        WifiMuteStateMachine.this.wifiMuzzleWatchdog.start();
      } 
      if (!WifiMuteStateMachine.this.isWifiEnabled()) {
        AppUtil.getInstance().showToast(UILocation.ONLY_LOCAL, AppUtil.getDefContext().getString(R.string.toastEnableWifi));
        WifiMuteStateMachine.this.enableWifi(true);
        WifiMuteStateMachine.this.notifyWifiOn();
      } 
      WifiMuteStateMachine.this.activity.getFragmentManager().beginTransaction().hide(WifiMuteStateMachine.this.wifiMuteFragment).commit();
    }
  }
  
  private class WifiPendingOff extends WifiState {
    private final String msg = AppUtil.getDefContext().getString(R.string.toastDisableWifiWarn);
    
    CountDownTimer wifiOffNotificationTimer = new CountDownTimer(TimeUnit.SECONDS.toMillis(10L), TimeUnit.SECONDS.toMillis(1L)) {
        public void onFinish() {}
        
        public void onTick(long param2Long) {
          WifiMuteStateMachine.this.wifiMuteFragment.setCountdownNumber(param2Long / TimeUnit.SECONDS.toMillis(1L));
        }
      };
    
    private WifiPendingOff() {}
    
    public void onEnter(Event param1Event) {
      super.onEnter(param1Event);
      WifiMuteStateMachine.this.wifiMuteFragment.reset();
      WifiMuteStateMachine.this.activity.getFragmentManager().beginTransaction().show(WifiMuteStateMachine.this.wifiMuteFragment).commit();
      WifiMuteStateMachine.this.notifyPendingOn();
      this.wifiOffNotificationTimer.start();
    }
    
    public void onExit(Event param1Event) {
      super.onExit(param1Event);
      WifiMuteStateMachine.this.notifyPendingCancel();
      this.wifiOffNotificationTimer.cancel();
    }
  }
  
  class null extends CountDownTimer {
    null(long param1Long1, long param1Long2) {
      super(param1Long1, param1Long2);
    }
    
    public void onFinish() {}
    
    public void onTick(long param1Long) {
      WifiMuteStateMachine.this.wifiMuteFragment.setCountdownNumber(param1Long / TimeUnit.SECONDS.toMillis(1L));
    }
  }
  
  private class WifiState implements State {
    private WifiState() {}
    
    public void onEnter(Event param1Event) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Enter State: ");
      stringBuilder.append(getClass().getSimpleName());
      RobotLog.ii("WifiMuteStateMachine", stringBuilder.toString());
    }
    
    public void onExit(Event param1Event) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Exit State: ");
      stringBuilder.append(getClass().getSimpleName());
      RobotLog.ii("WifiMuteStateMachine", stringBuilder.toString());
    }
    
    public String toString() {
      return getClass().getSimpleName();
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiMuteStateMachine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */