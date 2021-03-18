package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class DriverStationAccessPointAssistant extends AccessPointAssistant {
  private static final boolean DEBUG = false;
  
  private static final String TAG = "DriverStationAccessPointAssistant";
  
  private static final Object listenersLock = new Object();
  
  private static DriverStationAccessPointAssistant wirelessAPAssistant;
  
  private NetworkConnection.ConnectStatus connectStatus;
  
  private final Object enableDisableLock = new Object();
  
  private ArrayList<ConnectedNetworkHealthListener> healthListeners = new ArrayList<ConnectedNetworkHealthListener>();
  
  private IntentFilter intentFilter;
  
  private NetworkHealthPollerThread networkHealthPollerThread;
  
  private BroadcastReceiver receiver;
  
  private final List<ScanResult> scanResults = new ArrayList<ScanResult>();
  
  private DriverStationAccessPointAssistant(Context paramContext) {
    super(paramContext);
    IntentFilter intentFilter = new IntentFilter();
    this.intentFilter = intentFilter;
    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
    this.intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
    if (this.wifiManager.getConnectionInfo().getSupplicantState() == SupplicantState.COMPLETED) {
      this.connectStatus = NetworkConnection.ConnectStatus.CONNECTED;
      saveConnectionInfo(this.wifiManager.getConnectionInfo());
      startHealthPoller();
      return;
    } 
    this.connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
  }
  
  public static DriverStationAccessPointAssistant getDriverStationAccessPointAssistant(Context paramContext) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant.wirelessAPAssistant : Lcom/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant;
    //   6: ifnonnull -> 20
    //   9: new com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Landroid/content/Context;)V
    //   17: putstatic com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant.wirelessAPAssistant : Lcom/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant;
    //   20: getstatic com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant.wirelessAPAssistant : Lcom/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant;
    //   23: astore_0
    //   24: ldc com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/qualcomm/robotcore/wifi/DriverStationAccessPointAssistant
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  private static String getIpAddressAsString(int paramInt) {
    return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(paramInt & 0xFF), Integer.valueOf(paramInt >> 8 & 0xFF), Integer.valueOf(paramInt >> 16 & 0xFF), Integer.valueOf(paramInt >> 24 & 0xFF) });
  }
  
  private void handleWifiDisconnect() {
    RobotLog.vv("DriverStationAccessPointAssistant", "Handling wifi disconnect");
    this.connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
    sendEvent(NetworkConnection.NetworkEvent.DISCONNECTED);
    NetworkConnectionHandler.getInstance().shutdown();
  }
  
  private void killHealthPoller() {
    NetworkHealthPollerThread networkHealthPollerThread = this.networkHealthPollerThread;
    if (networkHealthPollerThread != null) {
      networkHealthPollerThread.interrupt();
      this.networkHealthPollerThread = null;
    } 
  }
  
  private void saveConnectionInfo(WifiInfo paramWifiInfo) {
    String str2 = paramWifiInfo.getSSID().replace("\"", "");
    String str1 = paramWifiInfo.getBSSID();
    PreferencesHelper preferencesHelper = new PreferencesHelper("DriverStationAccessPointAssistant", this.context);
    preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_last_known_ssid), str2);
    preferencesHelper.writePrefIfDifferent(this.context.getString(R.string.pref_last_known_macaddr), str1);
  }
  
  private void startHealthPoller() {
    NetworkHealthPollerThread networkHealthPollerThread = this.networkHealthPollerThread;
    if (networkHealthPollerThread != null && !networkHealthPollerThread.isInterrupted())
      return; 
    networkHealthPollerThread = new NetworkHealthPollerThread();
    this.networkHealthPollerThread = networkHealthPollerThread;
    networkHealthPollerThread.start();
  }
  
  protected boolean connectToAccessPoint(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Attempting to auto-connect to ");
    stringBuilder.append(paramString);
    RobotLog.vv("DriverStationAccessPointAssistant", stringBuilder.toString());
    List list = this.wifiManager.getConfiguredNetworks();
    if (list == null) {
      RobotLog.ee("DriverStationAccessPointAssistant", "Wifi is likely off");
      return false;
    } 
    Iterator<WifiConfiguration> iterator = list.iterator();
    while (iterator.hasNext()) {
      WifiConfiguration wifiConfiguration = iterator.next();
      if (wifiConfiguration.SSID != null) {
        String str = wifiConfiguration.SSID;
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("\"");
        stringBuilder1.append(paramString);
        stringBuilder1.append("\"");
        if (str.equals(stringBuilder1.toString())) {
          if (!this.wifiManager.enableNetwork(wifiConfiguration.networkId, true)) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Could not enable ");
            stringBuilder2.append(paramString);
            RobotLog.ww("DriverStationAccessPointAssistant", stringBuilder2.toString());
            return false;
          } 
          if (!this.wifiManager.reconnect()) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Could not reconnect to ");
            stringBuilder2.append(paramString);
            RobotLog.ww("DriverStationAccessPointAssistant", stringBuilder2.toString());
            return false;
          } 
          break;
        } 
      } 
    } 
    return true;
  }
  
  public void disable() {
    synchronized (this.enableDisableLock) {
      if (this.receiver != null) {
        this.context.unregisterReceiver(this.receiver);
        this.receiver = null;
      } 
      return;
    } 
  }
  
  public void enable() {
    synchronized (this.enableDisableLock) {
      if (this.receiver == null) {
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context param1Context, Intent param1Intent) {
              String str = param1Intent.getAction();
              if ("android.net.wifi.SCAN_RESULTS".equals(str)) {
                DriverStationAccessPointAssistant.this.handleScanResultsAvailable(param1Intent);
                return;
              } 
              if ("android.net.wifi.STATE_CHANGE".equals(str))
                DriverStationAccessPointAssistant.this.handleNetworkStateChanged(param1Intent); 
            }
          };
        this.context.registerReceiver(this.receiver, this.intentFilter);
      } 
      return;
    } 
  }
  
  public String getConnectionOwnerName() {
    return WifiUtil.getConnectedSsid();
  }
  
  protected String getIpAddress() {
    return getIpAddressAsString(this.wifiManager.getConnectionInfo().getIpAddress());
  }
  
  protected void handleNetworkStateChanged(Intent paramIntent) {
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    NetworkInfo networkInfo = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Wifi state change:, state: ");
    stringBuilder.append(networkInfo);
    stringBuilder.append(", wifiInfo: ");
    stringBuilder.append(wifiInfo);
    RobotLog.v(stringBuilder.toString());
    if (this.connectStatus == NetworkConnection.ConnectStatus.CONNECTED && !networkInfo.isConnected()) {
      handleWifiDisconnect();
      killHealthPoller();
      return;
    } 
    if (this.connectStatus == NetworkConnection.ConnectStatus.NOT_CONNECTED && networkInfo.isConnected() == true) {
      startHealthPoller();
      this.connectStatus = NetworkConnection.ConnectStatus.CONNECTED;
      saveConnectionInfo(wifiInfo);
      sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE);
    } 
  }
  
  protected void handleScanResultsAvailable(Intent paramIntent) {
    PreferencesHelper preferencesHelper = new PreferencesHelper("DriverStationAccessPointAssistant", this.context);
    String str1 = (String)preferencesHelper.readPref(this.context.getString(R.string.pref_last_known_ssid));
    String str2 = (String)preferencesHelper.readPref(this.context.getString(R.string.pref_last_known_macaddr));
    this.scanResults.clear();
    this.scanResults.addAll(this.wifiManager.getScanResults());
    if (this.doContinuousScans == true) {
      if (!lookForKnownAccessPoint(str1, str2, this.scanResults)) {
        this.wifiManager.startScan();
        return;
      } 
      this.doContinuousScans = false;
    } 
  }
  
  protected boolean lookForKnownAccessPoint(String paramString1, String paramString2, List<ScanResult> paramList) {
    if (paramString1 != null) {
      if (paramString2 == null)
        return false; 
      for (ScanResult scanResult : paramList) {
        if (scanResult.SSID.equals(paramString1) && scanResult.BSSID.equals(paramString2)) {
          if (connectToAccessPoint(scanResult.SSID) == true)
            return true; 
          break;
        } 
      } 
    } 
    return false;
  }
  
  public void registerNetworkHealthListener(ConnectedNetworkHealthListener paramConnectedNetworkHealthListener) {
    synchronized (listenersLock) {
      this.healthListeners.add(paramConnectedNetworkHealthListener);
      return;
    } 
  }
  
  public void setNetworkSettings(String paramString1, String paramString2, ApChannel paramApChannel) {
    RobotLog.ee("DriverStationAccessPointAssistant", "setNetworkProperties not supported on Driver Station");
  }
  
  public void unregisterNetworkHealthListener(ConnectedNetworkHealthListener paramConnectedNetworkHealthListener) {
    synchronized (listenersLock) {
      this.healthListeners.remove(paramConnectedNetworkHealthListener);
      return;
    } 
  }
  
  public static interface ConnectedNetworkHealthListener {
    void onNetworkHealthUpdate(int param1Int1, int param1Int2);
  }
  
  public class NetworkHealthPollerThread extends Thread {
    public void run() {
      while (true) {
        if (!Thread.currentThread().isInterrupted()) {
          WifiInfo wifiInfo = DriverStationAccessPointAssistant.this.wifiManager.getConnectionInfo();
          int i = wifiInfo.getRssi();
          int j = wifiInfo.getLinkSpeed();
          synchronized (DriverStationAccessPointAssistant.listenersLock) {
            Iterator<DriverStationAccessPointAssistant.ConnectedNetworkHealthListener> iterator = DriverStationAccessPointAssistant.this.healthListeners.iterator();
            while (iterator.hasNext())
              ((DriverStationAccessPointAssistant.ConnectedNetworkHealthListener)iterator.next()).onNetworkHealthUpdate(i, j); 
            try {
              Thread.sleep(1000L);
            } catch (InterruptedException null) {
              Thread.currentThread().interrupt();
            } 
          } 
        } 
        return;
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\DriverStationAccessPointAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */