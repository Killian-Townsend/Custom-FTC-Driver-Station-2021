package com.qualcomm.robotcore.wifi;

import android.content.Context;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.util.ThreadPool;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.network.WifiDirectAgent;

public abstract class AccessPointAssistant extends NetworkConnection {
  private static final String DEFAULT_TETHERING_IP_ADDR = "192.168.43.1";
  
  private static final String TAG = "AccessPointAssistant";
  
  protected boolean doContinuousScans;
  
  protected final WifiManager wifiManager;
  
  public AccessPointAssistant(Context paramContext) {
    super(paramContext);
    this.wifiManager = (WifiManager)paramContext.getApplicationContext().getSystemService("wifi");
    this.doContinuousScans = false;
    WifiDirectAgent.getInstance().doNotListen();
  }
  
  public void cancelPotentialConnections() {
    this.doContinuousScans = false;
  }
  
  public void connect(String paramString) {}
  
  public void connect(String paramString1, String paramString2) {}
  
  public void createConnection() {}
  
  public void detectWifiReset() {}
  
  public void discoverPotentialConnections() {
    ThreadPool.getDefaultScheduler().schedule(new Runnable() {
          public void run() {
            RobotLog.ii("AccessPointAssistant", "Future scan now...");
            AccessPointAssistant.this.wifiManager.startScan();
            AccessPointAssistant.this.doContinuousScans = true;
          }
        }1000L, TimeUnit.MILLISECONDS);
  }
  
  public NetworkConnection.ConnectStatus getConnectStatus() {
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    int i = null.$SwitchMap$android$net$wifi$SupplicantState[wifiInfo.getSupplicantState().ordinal()];
    return (i != 1) ? ((i != 2) ? ((i != 3) ? NetworkConnection.ConnectStatus.NOT_CONNECTED : NetworkConnection.ConnectStatus.NOT_CONNECTED) : NetworkConnection.ConnectStatus.CONNECTED) : NetworkConnection.ConnectStatus.CONNECTING;
  }
  
  public InetAddress getConnectionOwnerAddress() {
    try {
      return InetAddress.getByName("192.168.43.1");
    } catch (UnknownHostException unknownHostException) {
      unknownHostException.printStackTrace();
      return null;
    } 
  }
  
  public String getConnectionOwnerMacAddress() {
    return getConnectionOwnerName();
  }
  
  public String getDeviceName() {
    return getConnectionOwnerName();
  }
  
  public String getFailureReason() {
    return null;
  }
  
  public String getInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Name: ");
    stringBuilder.append(getDeviceName());
    stringBuilder.append("\nIP Address: ");
    stringBuilder.append(getIpAddress());
    stringBuilder.append("\nAccess Point SSID: ");
    stringBuilder.append(getConnectionOwnerName());
    if (getPassphrase() != null) {
      stringBuilder.append("\nPassphrase: ");
      stringBuilder.append(getPassphrase());
    } 
    return stringBuilder.toString();
  }
  
  protected abstract String getIpAddress();
  
  public NetworkType getNetworkType() {
    return NetworkType.WIRELESSAP;
  }
  
  public String getPassphrase() {
    return null;
  }
  
  public boolean isConnected() {
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (wifiInfo != null) {
      bool1 = bool2;
      if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
        bool1 = true; 
    } 
    return bool1;
  }
  
  public void onWaitForConnection() {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\AccessPointAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */