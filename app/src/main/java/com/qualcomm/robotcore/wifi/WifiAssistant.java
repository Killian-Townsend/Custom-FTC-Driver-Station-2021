package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import com.qualcomm.robotcore.util.RobotLog;

public class WifiAssistant {
  private final Context context;
  
  private final IntentFilter intentFilter;
  
  private final WifiStateBroadcastReceiver receiver;
  
  public WifiAssistant(Context paramContext, WifiAssistantCallback paramWifiAssistantCallback) {
    this.context = paramContext;
    if (paramWifiAssistantCallback == null)
      RobotLog.v("WifiAssistantCallback is null"); 
    this.receiver = new WifiStateBroadcastReceiver(paramWifiAssistantCallback);
    IntentFilter intentFilter = new IntentFilter();
    this.intentFilter = intentFilter;
    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
  }
  
  public void disable() {
    this.context.unregisterReceiver(this.receiver);
  }
  
  public void enable() {
    this.context.registerReceiver(this.receiver, this.intentFilter);
  }
  
  public static interface WifiAssistantCallback {
    void wifiEventCallback(WifiAssistant.WifiState param1WifiState);
  }
  
  public enum WifiState {
    CONNECTED, NOT_CONNECTED;
    
    static {
      WifiState wifiState = new WifiState("NOT_CONNECTED", 1);
      NOT_CONNECTED = wifiState;
      $VALUES = new WifiState[] { CONNECTED, wifiState };
    }
  }
  
  private static class WifiStateBroadcastReceiver extends BroadcastReceiver {
    private final WifiAssistant.WifiAssistantCallback callback;
    
    private WifiAssistant.WifiState state = null;
    
    public WifiStateBroadcastReceiver(WifiAssistant.WifiAssistantCallback param1WifiAssistantCallback) {
      this.callback = param1WifiAssistantCallback;
    }
    
    private void notify(WifiAssistant.WifiState param1WifiState) {
      if (this.state == param1WifiState)
        return; 
      this.state = param1WifiState;
      WifiAssistant.WifiAssistantCallback wifiAssistantCallback = this.callback;
      if (wifiAssistantCallback != null)
        wifiAssistantCallback.wifiEventCallback(param1WifiState); 
    }
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      if (param1Intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
        if (((NetworkInfo)param1Intent.getParcelableExtra("networkInfo")).isConnected()) {
          notify(WifiAssistant.WifiState.CONNECTED);
          return;
        } 
        notify(WifiAssistant.WifiState.NOT_CONNECTED);
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\WifiAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */