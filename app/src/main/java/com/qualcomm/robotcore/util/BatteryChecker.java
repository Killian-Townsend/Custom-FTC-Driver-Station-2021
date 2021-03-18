package com.qualcomm.robotcore.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class BatteryChecker {
  protected static final int BATTERY_WARN_THRESHOLD = 30;
  
  private static final int LOG_THRESHOLD = 70;
  
  public static final String TAG = "BatteryChecker";
  
  protected static final boolean debugBattery = false;
  
  protected final Handler batteryHandler;
  
  Runnable batteryLevelChecker = new Runnable() {
      public void run() {
        BatteryChecker batteryChecker = BatteryChecker.this;
        batteryChecker.pollBatteryLevel(batteryChecker.watcher);
        synchronized (BatteryChecker.this.batteryHandler) {
          if (!BatteryChecker.this.closed)
            BatteryChecker.this.batteryHandler.postDelayed(BatteryChecker.this.batteryLevelChecker, BatteryChecker.this.repeatDelay); 
          return;
        } 
      }
    };
  
  protected boolean closed;
  
  private Context context = AppUtil.getDefContext();
  
  private long initialDelay = 5000L;
  
  protected final Monitor monitor = new Monitor();
  
  private long repeatDelay;
  
  private BatteryWatcher watcher;
  
  public BatteryChecker(BatteryWatcher paramBatteryWatcher, long paramLong) {
    this.watcher = paramBatteryWatcher;
    this.repeatDelay = paramLong;
    this.batteryHandler = new Handler();
    this.closed = true;
  }
  
  public void close() {
    if (!this.closed) {
      try {
        this.context.unregisterReceiver(this.monitor);
      } catch (Exception exception) {
        RobotLog.ee("BatteryChecker", exception, "Failed to unregister battery monitor receiver; ignored");
      } 
      try {
        synchronized (this.batteryHandler) {
          this.closed = true;
          this.batteryHandler.removeCallbacks(this.batteryLevelChecker);
          return;
        } 
      } catch (Exception exception) {
        RobotLog.ee("BatteryChecker", exception, "Failed to remove battery monitor callbacks; ignored");
      } 
    } 
  }
  
  protected void logBatteryInfo(int paramInt, boolean paramBoolean) {
    if (paramInt < 30) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("percent remaining: ");
      stringBuilder.append(paramInt);
      stringBuilder.append(" is charging: ");
      stringBuilder.append(paramBoolean);
      RobotLog.ii("BatteryChecker", stringBuilder.toString());
    } 
  }
  
  public void pollBatteryLevel(BatteryWatcher paramBatteryWatcher) {
    processBatteryChanged(registerReceiver(null));
  }
  
  protected void processBatteryChanged(Intent paramIntent) {
    if (paramIntent == null)
      return; 
    int i = paramIntent.getIntExtra("level", -1);
    int j = paramIntent.getIntExtra("scale", -1);
    if (i >= 0 && j > 0) {
      boolean bool = true;
      if ((paramIntent.getIntExtra("plugged", 1) & 0x7) == 0)
        bool = false; 
      i = i * 100 / j;
      logBatteryInfo(i, bool);
      this.watcher.updateBatteryStatus(new BatteryStatus(i, bool));
    } 
  }
  
  protected Intent registerReceiver(BroadcastReceiver paramBroadcastReceiver) {
    IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    return this.context.registerReceiver(paramBroadcastReceiver, intentFilter);
  }
  
  public void startBatteryMonitoring() {
    synchronized (this.batteryHandler) {
      this.closed = false;
      this.batteryHandler.postDelayed(this.batteryLevelChecker, this.initialDelay);
      registerReceiver(this.monitor);
      return;
    } 
  }
  
  public static class BatteryStatus {
    public boolean isCharging;
    
    public double percent;
    
    protected BatteryStatus() {}
    
    public BatteryStatus(double param1Double, boolean param1Boolean) {
      this.percent = param1Double;
      this.isCharging = param1Boolean;
    }
    
    public static BatteryStatus deserialize(String param1String) {
      String[] arrayOfString = param1String.split("\\|");
      BatteryStatus batteryStatus = new BatteryStatus();
      batteryStatus.percent = Double.parseDouble(arrayOfString[0]);
      batteryStatus.isCharging = Boolean.parseBoolean(arrayOfString[1]);
      return batteryStatus;
    }
    
    public String serialize() {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(this.percent);
      stringBuilder.append('|');
      stringBuilder.append(this.isCharging);
      return stringBuilder.toString();
    }
  }
  
  public static interface BatteryWatcher {
    void updateBatteryStatus(BatteryChecker.BatteryStatus param1BatteryStatus);
  }
  
  protected class Monitor extends BroadcastReceiver {
    public void onReceive(Context param1Context, Intent param1Intent) {
      byte b;
      String str = param1Intent.getAction();
      if (str.hashCode() == -1538406691 && str.equals("android.intent.action.BATTERY_CHANGED")) {
        b = 0;
      } else {
        b = -1;
      } 
      if (b != 0)
        return; 
      BatteryChecker.this.processBatteryChanged(param1Intent);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\BatteryChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */