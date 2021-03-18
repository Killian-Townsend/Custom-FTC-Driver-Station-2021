package com.qualcomm.robotcore.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.CallbackResult;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public abstract class NetworkConnection {
  private static final int GHZ_24_BASE_FREQ = 2407;
  
  private static final int GHZ_50_BASE_FREQ = 5000;
  
  protected NetworkConnectionCallback callback = null;
  
  protected final Object callbackLock = new Object();
  
  protected Context context;
  
  protected NetworkEvent lastEvent = null;
  
  public NetworkConnection(Context paramContext) {
    Context context = paramContext;
    if (paramContext == null)
      context = AppUtil.getDefContext(); 
    this.context = context;
  }
  
  public static boolean isDeviceNameValid(String paramString) {
    return paramString.matches("^\\p{Print}+$");
  }
  
  public abstract void cancelPotentialConnections();
  
  public abstract void connect(String paramString);
  
  public abstract void connect(String paramString1, String paramString2);
  
  public abstract void createConnection();
  
  public abstract void detectWifiReset();
  
  public abstract void disable();
  
  public abstract void discoverPotentialConnections();
  
  public abstract void enable();
  
  public NetworkConnectionCallback getCallback() {
    synchronized (this.callbackLock) {
      return this.callback;
    } 
  }
  
  public abstract ConnectStatus getConnectStatus();
  
  public abstract InetAddress getConnectionOwnerAddress();
  
  public abstract String getConnectionOwnerMacAddress();
  
  public abstract String getConnectionOwnerName();
  
  public abstract String getDeviceName();
  
  public abstract String getFailureReason();
  
  public abstract String getInfo();
  
  public abstract NetworkType getNetworkType();
  
  public abstract String getPassphrase();
  
  public int getWifiChannel() {
    int i = ((WifiManager)this.context.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getFrequency();
    return (i >= 5000) ? ((i - 5000) / 5) : ((i >= 2407) ? ((i - 2407) / 5) : 0);
  }
  
  public abstract boolean isConnected();
  
  public abstract void onWaitForConnection();
  
  protected void sendEvent(NetworkEvent paramNetworkEvent) {
    if (this.lastEvent == paramNetworkEvent) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Dropping duplicate network event ");
      stringBuilder.append(paramNetworkEvent.toString());
      RobotLog.i(stringBuilder.toString());
      return;
    } 
    this.lastEvent = paramNetworkEvent;
    synchronized (this.callbackLock) {
      if (this.callback != null)
        this.callback.onNetworkConnectionEvent(paramNetworkEvent); 
      return;
    } 
  }
  
  public void setCallback(NetworkConnectionCallback paramNetworkConnectionCallback) {
    synchronized (this.callbackLock) {
      this.callback = paramNetworkConnectionCallback;
      return;
    } 
  }
  
  public abstract void setNetworkSettings(String paramString1, String paramString2, ApChannel paramApChannel) throws InvalidNetworkSettingException;
  
  public enum ConnectStatus {
    CONNECTED, CONNECTING, ERROR, GROUP_OWNER, NOT_CONNECTED;
    
    static {
      CONNECTED = new ConnectStatus("CONNECTED", 2);
      GROUP_OWNER = new ConnectStatus("GROUP_OWNER", 3);
      ConnectStatus connectStatus = new ConnectStatus("ERROR", 4);
      ERROR = connectStatus;
      $VALUES = new ConnectStatus[] { NOT_CONNECTED, CONNECTING, CONNECTED, GROUP_OWNER, connectStatus };
    }
  }
  
  public static interface NetworkConnectionCallback {
    CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent param1NetworkEvent);
  }
  
  public enum NetworkEvent {
    AP_CREATED, CONNECTED_AS_GROUP_OWNER, CONNECTED_AS_PEER, CONNECTING, CONNECTION_INFO_AVAILABLE, DISCONNECTED, DISCOVERING_PEERS, ERROR, GROUP_CREATED, PEERS_AVAILABLE, UNKNOWN;
    
    static {
      CONNECTING = new NetworkEvent("CONNECTING", 3);
      CONNECTED_AS_PEER = new NetworkEvent("CONNECTED_AS_PEER", 4);
      CONNECTED_AS_GROUP_OWNER = new NetworkEvent("CONNECTED_AS_GROUP_OWNER", 5);
      DISCONNECTED = new NetworkEvent("DISCONNECTED", 6);
      CONNECTION_INFO_AVAILABLE = new NetworkEvent("CONNECTION_INFO_AVAILABLE", 7);
      AP_CREATED = new NetworkEvent("AP_CREATED", 8);
      ERROR = new NetworkEvent("ERROR", 9);
      NetworkEvent networkEvent = new NetworkEvent("UNKNOWN", 10);
      UNKNOWN = networkEvent;
      $VALUES = new NetworkEvent[] { 
          DISCOVERING_PEERS, PEERS_AVAILABLE, GROUP_CREATED, CONNECTING, CONNECTED_AS_PEER, CONNECTED_AS_GROUP_OWNER, DISCONNECTED, CONNECTION_INFO_AVAILABLE, AP_CREATED, ERROR, 
          networkEvent };
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\NetworkConnection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */