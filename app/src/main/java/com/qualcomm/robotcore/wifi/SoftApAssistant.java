package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.qualcomm.robotcore.util.ReadWriteFile;
import com.qualcomm.robotcore.util.RobotLog;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

public class SoftApAssistant extends NetworkConnection {
  private static String DEFAULT_PASSWORD = "password";
  
  private static String DEFAULT_SSID = "FTC-1234";
  
  private static final String NETWORK_PASSWORD_FILE = "FTC_RobotController_password.txt";
  
  private static final String NETWORK_SSID_FILE = "FTC_RobotController_SSID.txt";
  
  public static final String TAG = "SoftApAssistant";
  
  private static IntentFilter intentFilter;
  
  private static SoftApAssistant softApAssistant;
  
  private Context context = null;
  
  String password = DEFAULT_PASSWORD;
  
  private BroadcastReceiver receiver;
  
  private final List<ScanResult> scanResults = new ArrayList<ScanResult>();
  
  String ssid = DEFAULT_SSID;
  
  private final WifiManager wifiManager;
  
  private SoftApAssistant(Context paramContext) {
    super(paramContext);
    this.wifiManager = (WifiManager)paramContext.getApplicationContext().getSystemService("wifi");
  }
  
  private WifiConfiguration buildConfig(String paramString1, String paramString2) {
    WifiConfiguration wifiConfiguration = new WifiConfiguration();
    wifiConfiguration.SSID = paramString1;
    wifiConfiguration.preSharedKey = paramString2;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Setting up network, myConfig.SSID: ");
    stringBuilder.append(wifiConfiguration.SSID);
    stringBuilder.append(", password: ");
    stringBuilder.append(wifiConfiguration.preSharedKey);
    RobotLog.v(stringBuilder.toString());
    wifiConfiguration.status = 2;
    wifiConfiguration.allowedAuthAlgorithms.set(0);
    wifiConfiguration.allowedKeyManagement.set(1);
    wifiConfiguration.allowedProtocols.set(1);
    wifiConfiguration.allowedProtocols.set(0);
    wifiConfiguration.allowedGroupCiphers.set(2);
    wifiConfiguration.allowedGroupCiphers.set(3);
    wifiConfiguration.allowedPairwiseCiphers.set(1);
    wifiConfiguration.allowedPairwiseCiphers.set(2);
    return wifiConfiguration;
  }
  
  private String getIpAddressAsString(int paramInt) {
    return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(paramInt & 0xFF), Integer.valueOf(paramInt >> 8 & 0xFF), Integer.valueOf(paramInt >> 16 & 0xFF), Integer.valueOf(paramInt >> 24 & 0xFF) });
  }
  
  public static SoftApAssistant getSoftApAssistant(Context paramContext) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/wifi/SoftApAssistant
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.softApAssistant : Lcom/qualcomm/robotcore/wifi/SoftApAssistant;
    //   6: ifnonnull -> 20
    //   9: new com/qualcomm/robotcore/wifi/SoftApAssistant
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Landroid/content/Context;)V
    //   17: putstatic com/qualcomm/robotcore/wifi/SoftApAssistant.softApAssistant : Lcom/qualcomm/robotcore/wifi/SoftApAssistant;
    //   20: new android/content/IntentFilter
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: astore_0
    //   28: aload_0
    //   29: putstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   32: aload_0
    //   33: ldc 'android.net.wifi.SCAN_RESULTS'
    //   35: invokevirtual addAction : (Ljava/lang/String;)V
    //   38: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   41: ldc 'android.net.wifi.NETWORK_IDS_CHANGED'
    //   43: invokevirtual addAction : (Ljava/lang/String;)V
    //   46: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   49: ldc 'android.net.wifi.STATE_CHANGE'
    //   51: invokevirtual addAction : (Ljava/lang/String;)V
    //   54: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   57: ldc 'android.net.wifi.supplicant.STATE_CHANGE'
    //   59: invokevirtual addAction : (Ljava/lang/String;)V
    //   62: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   65: ldc 'android.net.wifi.supplicant.CONNECTION_CHANGE'
    //   67: invokevirtual addAction : (Ljava/lang/String;)V
    //   70: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.intentFilter : Landroid/content/IntentFilter;
    //   73: ldc 'android.net.wifi.WIFI_STATE_CHANGED'
    //   75: invokevirtual addAction : (Ljava/lang/String;)V
    //   78: getstatic com/qualcomm/robotcore/wifi/SoftApAssistant.softApAssistant : Lcom/qualcomm/robotcore/wifi/SoftApAssistant;
    //   81: astore_0
    //   82: ldc com/qualcomm/robotcore/wifi/SoftApAssistant
    //   84: monitorexit
    //   85: aload_0
    //   86: areturn
    //   87: astore_0
    //   88: ldc com/qualcomm/robotcore/wifi/SoftApAssistant
    //   90: monitorexit
    //   91: aload_0
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	87	finally
    //   20	82	87	finally
  }
  
  private boolean isSoftAccessPoint() {
    try {
      return ((Boolean)this.wifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]).invoke(this.wifiManager, new Object[0])).booleanValue();
    } catch (NoSuchMethodException noSuchMethodException) {
      noSuchMethodException.printStackTrace();
      return false;
    } catch (InvocationTargetException invocationTargetException) {
      invocationTargetException.printStackTrace();
      return false;
    } catch (IllegalAccessException illegalAccessException) {
      illegalAccessException.printStackTrace();
      return false;
    } 
  }
  
  public void cancelPotentialConnections() {}
  
  public void connect(String paramString) {
    connect(paramString, DEFAULT_PASSWORD);
  }
  
  public void connect(String paramString1, String paramString2) {
    this.ssid = paramString1;
    this.password = paramString2;
    WifiConfiguration wifiConfiguration = buildConfig(String.format("\"%s\"", new Object[] { paramString1 }), String.format("\"%s\"", new Object[] { paramString2 }));
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Connecting to SoftAP, SSID: ");
    stringBuilder.append(wifiConfiguration.SSID);
    stringBuilder.append(", supplicant state: ");
    stringBuilder.append(wifiInfo.getSupplicantState());
    RobotLog.v(stringBuilder.toString());
    if (wifiInfo.getSSID().equals(wifiConfiguration.SSID) && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
      sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE); 
    if (!wifiInfo.getSSID().equals(wifiConfiguration.SSID) || wifiInfo.getSupplicantState() != SupplicantState.COMPLETED) {
      int i = this.wifiManager.addNetwork(wifiConfiguration);
      this.wifiManager.saveConfiguration();
      if (i != -1)
        for (WifiConfiguration wifiConfiguration1 : this.wifiManager.getConfiguredNetworks()) {
          if (wifiConfiguration1.SSID != null) {
            String str = wifiConfiguration1.SSID;
            StringBuilder stringBuilder1 = new StringBuilder();
            stringBuilder1.append("\"");
            stringBuilder1.append(paramString1);
            stringBuilder1.append("\"");
            if (str.equals(stringBuilder1.toString())) {
              this.wifiManager.disconnect();
              this.wifiManager.enableNetwork(wifiConfiguration1.networkId, true);
              this.wifiManager.reconnect();
              break;
            } 
          } 
        }  
    } 
  }
  
  public void createConnection() {
    if (this.wifiManager.isWifiEnabled())
      this.wifiManager.setWifiEnabled(false); 
    File file1 = AppUtil.FIRST_FOLDER;
    File file2 = new File(file1, "FTC_RobotController_SSID.txt");
    if (!file2.exists())
      ReadWriteFile.writeFile(file1, "FTC_RobotController_SSID.txt", DEFAULT_SSID); 
    File file3 = new File(file1, "FTC_RobotController_password.txt");
    if (!file3.exists())
      ReadWriteFile.writeFile(file1, "FTC_RobotController_password.txt", DEFAULT_PASSWORD); 
    String str2 = ReadWriteFile.readFile(file2);
    String str3 = ReadWriteFile.readFile(file3);
    if (str2.isEmpty() || str2.length() >= 15)
      ReadWriteFile.writeFile(file1, "FTC_RobotController_SSID.txt", DEFAULT_SSID); 
    if (str3.isEmpty())
      ReadWriteFile.writeFile(file1, "FTC_RobotController_password.txt", DEFAULT_PASSWORD); 
    this.ssid = ReadWriteFile.readFile(file2);
    String str1 = ReadWriteFile.readFile(file3);
    this.password = str1;
    WifiConfiguration wifiConfiguration = buildConfig(this.ssid, str1);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Advertising SSID: ");
    stringBuilder.append(this.ssid);
    stringBuilder.append(", password: ");
    stringBuilder.append(this.password);
    RobotLog.v(stringBuilder.toString());
    try {
      Boolean bool;
      if (isSoftAccessPoint()) {
        bool = Boolean.valueOf(true);
      } else {
        this.wifiManager.getClass().getMethod("setWifiApConfiguration", new Class[] { WifiConfiguration.class }).invoke(this.wifiManager, new Object[] { bool });
        Method method = this.wifiManager.getClass().getMethod("setWifiApEnabled", new Class[] { WifiConfiguration.class, boolean.class });
        method.invoke(this.wifiManager, new Object[] { null, Boolean.valueOf(false) });
        bool = (Boolean)method.invoke(this.wifiManager, new Object[] { bool, Boolean.valueOf(true) });
      } 
      if (bool.booleanValue()) {
        sendEvent(NetworkConnection.NetworkEvent.AP_CREATED);
        return;
      } 
    } catch (NoSuchMethodException noSuchMethodException) {
      RobotLog.e(noSuchMethodException.getMessage());
      noSuchMethodException.printStackTrace();
    } catch (InvocationTargetException invocationTargetException) {
    
    } catch (IllegalAccessException illegalAccessException) {}
  }
  
  public void detectWifiReset() {}
  
  public void disable() {
    try {
      this.context.unregisterReceiver(this.receiver);
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      return;
    } 
  }
  
  public void discoverPotentialConnections() {
    this.wifiManager.startScan();
  }
  
  public void enable() {
    if (this.receiver == null)
      this.receiver = new BroadcastReceiver() {
          public void onReceive(Context param1Context, Intent param1Intent) {
            String str = param1Intent.getAction();
            WifiInfo wifiInfo = SoftApAssistant.this.wifiManager.getConnectionInfo();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onReceive(), action: ");
            stringBuilder.append(str);
            stringBuilder.append(", wifiInfo: ");
            stringBuilder.append(wifiInfo);
            RobotLog.v(stringBuilder.toString());
            if (wifiInfo.getSSID().equals(SoftApAssistant.this.ssid) && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
              SoftApAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE); 
            if ("android.net.wifi.SCAN_RESULTS".equals(str)) {
              SoftApAssistant.this.scanResults.clear();
              SoftApAssistant.this.scanResults.addAll(SoftApAssistant.this.wifiManager.getScanResults());
              stringBuilder = new StringBuilder();
              stringBuilder.append("Soft AP scanResults found: ");
              stringBuilder.append(SoftApAssistant.this.scanResults.size());
              RobotLog.v(stringBuilder.toString());
              for (ScanResult scanResult : SoftApAssistant.this.scanResults) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append("    scanResult: ");
                stringBuilder1.append(scanResult.SSID);
                RobotLog.v(stringBuilder1.toString());
              } 
              SoftApAssistant.this.sendEvent(NetworkConnection.NetworkEvent.PEERS_AVAILABLE);
            } 
            if ("android.net.wifi.supplicant.STATE_CHANGE".equals(str) && wifiInfo.getSupplicantState() == SupplicantState.COMPLETED)
              SoftApAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE); 
          }
        }; 
    this.context.registerReceiver(this.receiver, intentFilter);
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
    return this.ssid.replace("\"", "");
  }
  
  public String getConnectionOwnerName() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ssid in softap assistant: ");
    stringBuilder.append(this.ssid);
    RobotLog.v(stringBuilder.toString());
    return this.ssid.replace("\"", "");
  }
  
  public String getDeviceName() {
    return this.ssid;
  }
  
  public String getFailureReason() {
    return null;
  }
  
  public String getInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    stringBuilder.append("Name: ");
    stringBuilder.append(getDeviceName());
    if (isSoftAccessPoint()) {
      stringBuilder.append("\nAccess Point SSID: ");
      stringBuilder.append(getConnectionOwnerName());
      stringBuilder.append("\nPassphrase: ");
      stringBuilder.append(getPassphrase());
      stringBuilder.append("\nAdvertising");
    } else if (isConnected()) {
      stringBuilder.append("\nIP Address: ");
      stringBuilder.append(getIpAddressAsString(wifiInfo.getIpAddress()));
      stringBuilder.append("\nAccess Point SSID: ");
      stringBuilder.append(getConnectionOwnerName());
      stringBuilder.append("\nPassphrase: ");
      stringBuilder.append(getPassphrase());
    } else {
      stringBuilder.append("\nNo connection information");
    } 
    return stringBuilder.toString();
  }
  
  public NetworkType getNetworkType() {
    return NetworkType.SOFTAP;
  }
  
  public String getPassphrase() {
    return this.password;
  }
  
  public List<ScanResult> getScanResults() {
    return this.scanResults;
  }
  
  public boolean isConnected() {
    if (isSoftAccessPoint())
      return true; 
    WifiInfo wifiInfo = this.wifiManager.getConnectionInfo();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("isConnected(), current supplicant state: ");
    stringBuilder.append(wifiInfo.getSupplicantState().toString());
    RobotLog.v(stringBuilder.toString());
    return (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED);
  }
  
  public void onWaitForConnection() {}
  
  public void setNetworkSettings(String paramString1, String paramString2, ApChannel paramApChannel) {}
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\SoftApAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */