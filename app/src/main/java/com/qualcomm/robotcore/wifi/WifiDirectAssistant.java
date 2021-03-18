package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Looper;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ApChannelManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;
import org.firstinspires.ftc.robotcore.internal.network.WifiUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;

public class WifiDirectAssistant extends NetworkConnection {
  public static final String TAG = "WifiDirect";
  
  private static WifiDirectAssistant wifiDirectAssistant;
  
  private int clients = 0;
  
  private NetworkConnection.ConnectStatus connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
  
  private final Object connectStatusLock = new Object();
  
  private final WifiDirectConnectionInfoListener connectionListener;
  
  private String deviceMacAddress = "";
  
  private String deviceName = "";
  
  private int failureReason = 0;
  
  private boolean groupFormed = false;
  
  private final WifiDirectGroupInfoListener groupInfoListener;
  
  private String groupInterface = "";
  
  private String groupNetworkName = "";
  
  private InetAddress groupOwnerAddress = null;
  
  private final Object groupOwnerLock = new Object();
  
  private String groupOwnerMacAddress = "";
  
  private String groupOwnerName = "";
  
  private final IntentFilter intentFilter;
  
  private boolean isWifiP2pEnabled = false;
  
  private NetworkConnection.NetworkEvent lastEvent = null;
  
  private String passphrase = "";
  
  private final WifiDirectPeerListListener peerListListener;
  
  private final List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
  
  private PreferencesHelper preferencesHelper;
  
  private WifiP2pBroadcastReceiver receiver;
  
  private final WifiP2pManager.Channel wifiP2pChannel;
  
  private final WifiP2pManager wifiP2pManager;
  
  private WifiDirectAssistant(Context paramContext) {
    super(paramContext);
    IntentFilter intentFilter = new IntentFilter();
    this.intentFilter = intentFilter;
    intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
    this.intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
    this.intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
    this.intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
    WifiP2pManager wifiP2pManager = (WifiP2pManager)paramContext.getSystemService("wifip2p");
    this.wifiP2pManager = wifiP2pManager;
    this.wifiP2pChannel = wifiP2pManager.initialize(paramContext, Looper.getMainLooper(), null);
    this.receiver = new WifiP2pBroadcastReceiver();
    this.connectionListener = new WifiDirectConnectionInfoListener();
    this.peerListListener = new WifiDirectPeerListListener();
    this.groupInfoListener = new WifiDirectGroupInfoListener();
    PreferencesHelper preferencesHelper = new PreferencesHelper("WifiDirect", this.context);
    this.preferencesHelper = preferencesHelper;
    preferencesHelper.remove(paramContext.getString(R.string.pref_wifip2p_groupowner_connectedto));
  }
  
  public static String failureReasonToString(int paramInt) {
    if (paramInt != 0) {
      if (paramInt != 1) {
        if (paramInt != 2) {
          StringBuilder stringBuilder = new StringBuilder();
          stringBuilder.append("UNKNOWN (reason ");
          stringBuilder.append(paramInt);
          stringBuilder.append(")");
          return stringBuilder.toString();
        } 
        return "BUSY";
      } 
      return "P2P_UNSUPPORTED";
    } 
    return "ERROR";
  }
  
  private String getGroupOwnerMacAddress() {
    return this.groupOwnerMacAddress;
  }
  
  public static WifiDirectAssistant getWifiDirectAssistant(Context paramContext) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/wifi/WifiDirectAssistant
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/wifi/WifiDirectAssistant.wifiDirectAssistant : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant;
    //   6: ifnonnull -> 20
    //   9: new com/qualcomm/robotcore/wifi/WifiDirectAssistant
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Landroid/content/Context;)V
    //   17: putstatic com/qualcomm/robotcore/wifi/WifiDirectAssistant.wifiDirectAssistant : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant;
    //   20: getstatic com/qualcomm/robotcore/wifi/WifiDirectAssistant.wifiDirectAssistant : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant;
    //   23: astore_0
    //   24: ldc com/qualcomm/robotcore/wifi/WifiDirectAssistant
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/qualcomm/robotcore/wifi/WifiDirectAssistant
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  private void onWifiP2pThisDeviceChanged(WifiP2pDevice paramWifiP2pDevice) {
    this.deviceName = paramWifiP2pDevice.deviceName;
    this.deviceMacAddress = paramWifiP2pDevice.deviceAddress;
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("device information: ");
    stringBuilder.append(this.deviceName);
    stringBuilder.append(" ");
    stringBuilder.append(this.deviceMacAddress);
    RobotLog.vv("WifiDirect", stringBuilder.toString());
  }
  
  public void cancelDiscoverPeers() {
    RobotLog.dd("WifiDirect", "stop discovering peers");
    this.wifiP2pManager.stopPeerDiscovery(this.wifiP2pChannel, null);
  }
  
  public void cancelPotentialConnections() {
    cancelDiscoverPeers();
  }
  
  public void connect(String paramString) {
    synchronized (this.connectStatusLock) {
      if (this.connectStatus == NetworkConnection.ConnectStatus.CONNECTING || this.connectStatus == NetworkConnection.ConnectStatus.CONNECTED) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("connection request to ");
        stringBuilder1.append(paramString);
        stringBuilder1.append(" ignored, already connected");
        RobotLog.dd("WifiDirect", stringBuilder1.toString());
        return;
      } 
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("connecting to ");
      stringBuilder.append(paramString);
      RobotLog.dd("WifiDirect", stringBuilder.toString());
      this.connectStatus = NetworkConnection.ConnectStatus.CONNECTING;
      null = new WifiP2pConfig();
      ((WifiP2pConfig)null).deviceAddress = paramString;
      ((WifiP2pConfig)null).wps.setup = 0;
      ((WifiP2pConfig)null).groupOwnerIntent = 1;
      this.wifiP2pManager.connect(this.wifiP2pChannel, (WifiP2pConfig)null, new WifiP2pManager.ActionListener() {
            public void onFailure(int param1Int) {
              String str = WifiDirectAssistant.failureReasonToString(param1Int);
              WifiDirectAssistant.access$802(WifiDirectAssistant.this, param1Int);
              StringBuilder stringBuilder = new StringBuilder();
              stringBuilder.append("connect cannot start - reason: ");
              stringBuilder.append(str);
              RobotLog.dd("WifiDirect", stringBuilder.toString());
              WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.ERROR);
            }
            
            public void onSuccess() {
              RobotLog.dd("WifiDirect", "connect started");
              WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTING);
            }
          });
      return;
    } 
  }
  
  public void connect(String paramString1, String paramString2) {
    throw new UnsupportedOperationException("This method is not supported for this class");
  }
  
  public void createConnection() {
    createGroup();
  }
  
  public void createGroup() {
    this.wifiP2pManager.createGroup(this.wifiP2pChannel, new WifiP2pManager.ActionListener() {
          public void onFailure(int param1Int) {
            if (param1Int == 2) {
              RobotLog.dd("WifiDirect", "cannot create group, does group already exist?");
              return;
            } 
            String str = WifiDirectAssistant.failureReasonToString(param1Int);
            WifiDirectAssistant.access$802(WifiDirectAssistant.this, param1Int);
            null = new StringBuilder();
            null.append("Wifi Direct failure while trying to create group - reason: ");
            null.append(str);
            RobotLog.w(null.toString());
            synchronized (WifiDirectAssistant.this.connectStatusLock) {
              WifiDirectAssistant.access$702(WifiDirectAssistant.this, NetworkConnection.ConnectStatus.ERROR);
              WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.ERROR);
              return;
            } 
          }
          
          public void onSuccess() {
            WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.GROUP_CREATED);
            RobotLog.dd("WifiDirect", "created group");
          }
        });
  }
  
  public void detectWifiReset() {}
  
  public void disable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield clients : I
    //   7: iconst_1
    //   8: isub
    //   9: putfield clients : I
    //   12: new java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial <init> : ()V
    //   19: astore_1
    //   20: aload_1
    //   21: ldc_w 'There are '
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_1
    //   29: aload_0
    //   30: getfield clients : I
    //   33: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload_1
    //   38: ldc_w ' Wifi Direct Assistant Clients (-)'
    //   41: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: pop
    //   45: ldc 'WifiDirect'
    //   47: aload_1
    //   48: invokevirtual toString : ()Ljava/lang/String;
    //   51: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   54: aload_0
    //   55: getfield clients : I
    //   58: ifne -> 133
    //   61: ldc 'WifiDirect'
    //   63: ldc_w 'Disabling Wifi Direct Assistant'
    //   66: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   69: aload_0
    //   70: getfield wifiP2pManager : Landroid/net/wifi/p2p/WifiP2pManager;
    //   73: aload_0
    //   74: getfield wifiP2pChannel : Landroid/net/wifi/p2p/WifiP2pManager$Channel;
    //   77: aconst_null
    //   78: invokevirtual stopPeerDiscovery : (Landroid/net/wifi/p2p/WifiP2pManager$Channel;Landroid/net/wifi/p2p/WifiP2pManager$ActionListener;)V
    //   81: aload_0
    //   82: getfield wifiP2pManager : Landroid/net/wifi/p2p/WifiP2pManager;
    //   85: aload_0
    //   86: getfield wifiP2pChannel : Landroid/net/wifi/p2p/WifiP2pManager$Channel;
    //   89: aconst_null
    //   90: invokevirtual cancelConnect : (Landroid/net/wifi/p2p/WifiP2pManager$Channel;Landroid/net/wifi/p2p/WifiP2pManager$ActionListener;)V
    //   93: aload_0
    //   94: getfield context : Landroid/content/Context;
    //   97: aload_0
    //   98: getfield receiver : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$WifiP2pBroadcastReceiver;
    //   101: invokevirtual unregisterReceiver : (Landroid/content/BroadcastReceiver;)V
    //   104: aload_0
    //   105: aconst_null
    //   106: putfield lastEvent : Lcom/qualcomm/robotcore/wifi/NetworkConnection$NetworkEvent;
    //   109: aload_0
    //   110: getfield connectStatusLock : Ljava/lang/Object;
    //   113: astore_1
    //   114: aload_1
    //   115: monitorenter
    //   116: aload_0
    //   117: getstatic com/qualcomm/robotcore/wifi/NetworkConnection$ConnectStatus.NOT_CONNECTED : Lcom/qualcomm/robotcore/wifi/NetworkConnection$ConnectStatus;
    //   120: putfield connectStatus : Lcom/qualcomm/robotcore/wifi/NetworkConnection$ConnectStatus;
    //   123: aload_1
    //   124: monitorexit
    //   125: goto -> 133
    //   128: astore_2
    //   129: aload_1
    //   130: monitorexit
    //   131: aload_2
    //   132: athrow
    //   133: aload_0
    //   134: monitorexit
    //   135: return
    //   136: astore_1
    //   137: aload_0
    //   138: monitorexit
    //   139: aload_1
    //   140: athrow
    //   141: astore_1
    //   142: goto -> 104
    // Exception table:
    //   from	to	target	type
    //   2	93	136	finally
    //   93	104	141	java/lang/IllegalArgumentException
    //   93	104	136	finally
    //   104	116	136	finally
    //   116	125	128	finally
    //   129	131	128	finally
    //   131	133	136	finally
  }
  
  public void discoverPeers() {
    this.wifiP2pManager.discoverPeers(this.wifiP2pChannel, new WifiP2pManager.ActionListener() {
          public void onFailure(int param1Int) {
            String str = WifiDirectAssistant.failureReasonToString(param1Int);
            WifiDirectAssistant.access$802(WifiDirectAssistant.this, param1Int);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Wifi Direct failure while trying to discover peers - reason: ");
            stringBuilder.append(str);
            RobotLog.w(stringBuilder.toString());
            WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.ERROR);
          }
          
          public void onSuccess() {
            WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.DISCOVERING_PEERS);
            RobotLog.dd("WifiDirect", "discovering peers");
          }
        });
  }
  
  public void discoverPotentialConnections() {
    discoverPeers();
  }
  
  public void enable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield clients : I
    //   7: iconst_1
    //   8: iadd
    //   9: putfield clients : I
    //   12: new java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial <init> : ()V
    //   19: astore_1
    //   20: aload_1
    //   21: ldc_w 'There are '
    //   24: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   27: pop
    //   28: aload_1
    //   29: aload_0
    //   30: getfield clients : I
    //   33: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   36: pop
    //   37: aload_1
    //   38: ldc_w ' Wifi Direct Assistant Clients (+)'
    //   41: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   44: pop
    //   45: ldc 'WifiDirect'
    //   47: aload_1
    //   48: invokevirtual toString : ()Ljava/lang/String;
    //   51: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   54: aload_0
    //   55: getfield clients : I
    //   58: iconst_1
    //   59: if_icmpne -> 106
    //   62: ldc 'WifiDirect'
    //   64: ldc_w 'Enabling Wifi Direct Assistant'
    //   67: invokestatic vv : (Ljava/lang/String;Ljava/lang/String;)V
    //   70: aload_0
    //   71: getfield receiver : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$WifiP2pBroadcastReceiver;
    //   74: ifnonnull -> 90
    //   77: aload_0
    //   78: new com/qualcomm/robotcore/wifi/WifiDirectAssistant$WifiP2pBroadcastReceiver
    //   81: dup
    //   82: aload_0
    //   83: aconst_null
    //   84: invokespecial <init> : (Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant;Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$1;)V
    //   87: putfield receiver : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$WifiP2pBroadcastReceiver;
    //   90: aload_0
    //   91: getfield context : Landroid/content/Context;
    //   94: aload_0
    //   95: getfield receiver : Lcom/qualcomm/robotcore/wifi/WifiDirectAssistant$WifiP2pBroadcastReceiver;
    //   98: aload_0
    //   99: getfield intentFilter : Landroid/content/IntentFilter;
    //   102: invokevirtual registerReceiver : (Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
    //   105: pop
    //   106: invokestatic getInstance : ()Lorg/firstinspires/ftc/robotcore/internal/network/WifiDirectAgent;
    //   109: invokevirtual doListen : ()V
    //   112: aload_0
    //   113: monitorexit
    //   114: return
    //   115: astore_1
    //   116: aload_0
    //   117: monitorexit
    //   118: aload_1
    //   119: athrow
    // Exception table:
    //   from	to	target	type
    //   2	90	115	finally
    //   90	106	115	finally
    //   106	112	115	finally
  }
  
  public NetworkConnection.ConnectStatus getConnectStatus() {
    synchronized (this.connectStatusLock) {
      return this.connectStatus;
    } 
  }
  
  public InetAddress getConnectionOwnerAddress() {
    return getGroupOwnerAddress();
  }
  
  public String getConnectionOwnerMacAddress() {
    return getGroupOwnerMacAddress();
  }
  
  public String getConnectionOwnerName() {
    return getGroupOwnerName();
  }
  
  public String getDeviceMacAddress() {
    return this.deviceMacAddress;
  }
  
  public String getDeviceName() {
    return this.deviceName;
  }
  
  public String getFailureReason() {
    return failureReasonToString(this.failureReason);
  }
  
  public String getGroupInterface() {
    return this.groupInterface;
  }
  
  public String getGroupNetworkName() {
    return this.groupNetworkName;
  }
  
  public InetAddress getGroupOwnerAddress() {
    synchronized (this.groupOwnerLock) {
      return this.groupOwnerAddress;
    } 
  }
  
  public String getGroupOwnerName() {
    return this.groupOwnerName;
  }
  
  public String getInfo() {
    StringBuilder stringBuilder = new StringBuilder();
    if (isEnabled()) {
      stringBuilder.append("Name: ");
      stringBuilder.append(getDeviceName());
      if (isGroupOwner()) {
        stringBuilder.append("\nIP Address: ");
        stringBuilder.append(getGroupOwnerAddress().getHostAddress());
        stringBuilder.append("\nPassphrase: ");
        stringBuilder.append(getPassphrase());
        stringBuilder.append("\nGroup Owner");
      } else if (isConnected()) {
        stringBuilder.append("\nGroup Owner: ");
        stringBuilder.append(getGroupOwnerName());
        stringBuilder.append("\nConnected");
      } else {
        stringBuilder.append("\nNo connection information");
      } 
    } 
    return stringBuilder.toString();
  }
  
  public NetworkType getNetworkType() {
    return NetworkType.WIFIDIRECT;
  }
  
  public String getPassphrase() {
    return this.passphrase;
  }
  
  public List<WifiP2pDevice> getPeers() {
    return new ArrayList<WifiP2pDevice>(this.peers);
  }
  
  public boolean isConnected() {
    synchronized (this.connectStatusLock) {
      if (this.connectStatus == NetworkConnection.ConnectStatus.CONNECTED || this.connectStatus == NetworkConnection.ConnectStatus.GROUP_OWNER)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return bool;
  }
  
  public boolean isEnabled() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield clients : I
    //   6: istore_1
    //   7: iload_1
    //   8: ifle -> 16
    //   11: iconst_1
    //   12: istore_2
    //   13: goto -> 18
    //   16: iconst_0
    //   17: istore_2
    //   18: aload_0
    //   19: monitorexit
    //   20: iload_2
    //   21: ireturn
    //   22: astore_3
    //   23: aload_0
    //   24: monitorexit
    //   25: aload_3
    //   26: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	22	finally
  }
  
  public boolean isGroupOwner() {
    synchronized (this.connectStatusLock) {
      if (this.connectStatus == NetworkConnection.ConnectStatus.GROUP_OWNER)
        return true; 
    } 
    boolean bool = false;
    /* monitor exit ClassFileLocalVariableReferenceExpression{type=ObjectType{java/lang/Object}, name=SYNTHETIC_LOCAL_VARIABLE_2} */
    return bool;
  }
  
  public boolean isWifiP2pEnabled() {
    return this.isWifiP2pEnabled;
  }
  
  public void onWaitForConnection() {}
  
  public void removeGroup() {
    this.wifiP2pManager.removeGroup(this.wifiP2pChannel, null);
  }
  
  protected void sendEvent(NetworkConnection.NetworkEvent paramNetworkEvent) {
    NetworkConnection.NetworkEvent networkEvent = this.lastEvent;
    if (networkEvent == paramNetworkEvent && networkEvent != NetworkConnection.NetworkEvent.PEERS_AVAILABLE)
      return; 
    this.lastEvent = paramNetworkEvent;
    synchronized (this.callbackLock) {
      if (this.callback != null)
        this.callback.onNetworkConnectionEvent(paramNetworkEvent); 
      return;
    } 
  }
  
  public void setNetworkSettings(String paramString1, String paramString2, ApChannel paramApChannel) throws InvalidNetworkSettingException {
    if (paramString1 != null)
      DeviceNameManagerFactory.getInstance().setDeviceName(paramString1, true); 
    if (paramApChannel != null)
      ApChannelManagerFactory.getInstance().setChannel(paramApChannel, true); 
  }
  
  private class WifiDirectConnectionInfoListener implements WifiP2pManager.ConnectionInfoListener {
    private WifiDirectConnectionInfoListener() {}
    
    public void onConnectionInfoAvailable(WifiP2pInfo param1WifiP2pInfo) {
      WifiDirectAssistant.this.wifiP2pManager.requestGroupInfo(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.groupInfoListener);
      synchronized (WifiDirectAssistant.this.groupOwnerLock) {
        WifiDirectAssistant.access$502(WifiDirectAssistant.this, param1WifiP2pInfo.groupOwnerAddress);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("group owners address: ");
        stringBuilder.append(WifiDirectAssistant.this.groupOwnerAddress.toString());
        RobotLog.dd("WifiDirect", stringBuilder.toString());
        if (param1WifiP2pInfo.groupFormed && param1WifiP2pInfo.isGroupOwner) {
          RobotLog.dd("WifiDirect", "group formed, this device is the group owner (GO)");
          synchronized (WifiDirectAssistant.this.connectStatusLock) {
            WifiDirectAssistant.access$702(WifiDirectAssistant.this, NetworkConnection.ConnectStatus.GROUP_OWNER);
            WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTED_AS_GROUP_OWNER);
            return;
          } 
        } 
        if (param1WifiP2pInfo.groupFormed) {
          RobotLog.dd("WifiDirect", "group formed, this device is a client");
          synchronized (WifiDirectAssistant.this.connectStatusLock) {
            WifiDirectAssistant.access$702(WifiDirectAssistant.this, NetworkConnection.ConnectStatus.CONNECTED);
            WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTED_AS_PEER);
            return;
          } 
        } 
        null = new StringBuilder();
        null.append("group NOT formed, ERROR: ");
        null.append(param1WifiP2pInfo.toString());
        RobotLog.dd("WifiDirect", null.toString());
        WifiDirectAssistant.access$802(WifiDirectAssistant.this, 0);
        synchronized (WifiDirectAssistant.this.connectStatusLock) {
          WifiDirectAssistant.access$702(WifiDirectAssistant.this, NetworkConnection.ConnectStatus.ERROR);
          WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.ERROR);
          return;
        } 
      } 
    }
  }
  
  private class WifiDirectGroupInfoListener implements WifiP2pManager.GroupInfoListener {
    private WifiDirectGroupInfoListener() {}
    
    public void onGroupInfoAvailable(WifiP2pGroup param1WifiP2pGroup) {
      String str;
      if (param1WifiP2pGroup == null)
        return; 
      if (param1WifiP2pGroup.isGroupOwner()) {
        WifiDirectAssistant wifiDirectAssistant1 = WifiDirectAssistant.this;
        WifiDirectAssistant.access$902(wifiDirectAssistant1, wifiDirectAssistant1.deviceMacAddress);
        wifiDirectAssistant1 = WifiDirectAssistant.this;
        WifiDirectAssistant.access$1102(wifiDirectAssistant1, wifiDirectAssistant1.deviceName);
      } else {
        WifiP2pDevice wifiP2pDevice = param1WifiP2pGroup.getOwner();
        WifiDirectAssistant.access$902(WifiDirectAssistant.this, wifiP2pDevice.deviceAddress);
        WifiDirectAssistant.access$1102(WifiDirectAssistant.this, wifiP2pDevice.deviceName);
      } 
      WifiDirectAssistant.access$1302(WifiDirectAssistant.this, param1WifiP2pGroup.getInterface());
      WifiDirectAssistant.access$1402(WifiDirectAssistant.this, param1WifiP2pGroup.getNetworkName());
      WifiDirectAssistant.access$1502(WifiDirectAssistant.this, param1WifiP2pGroup.getPassphrase());
      WifiDirectAssistant wifiDirectAssistant = WifiDirectAssistant.this;
      if (wifiDirectAssistant.passphrase != null) {
        str = WifiDirectAssistant.this.passphrase;
      } else {
        str = "";
      } 
      WifiDirectAssistant.access$1502(wifiDirectAssistant, str);
      RobotLog.vv("WifiDirect", "connection information available");
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("connection information - groupOwnerName = ");
      stringBuilder.append(WifiDirectAssistant.this.groupOwnerName);
      RobotLog.vv("WifiDirect", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("connection information - groupOwnerMacAddress = ");
      stringBuilder.append(WifiDirectAssistant.this.groupOwnerMacAddress);
      RobotLog.vv("WifiDirect", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("connection information - groupInterface = ");
      stringBuilder.append(WifiDirectAssistant.this.groupInterface);
      RobotLog.vv("WifiDirect", stringBuilder.toString());
      stringBuilder = new StringBuilder();
      stringBuilder.append("connection information - groupNetworkName = ");
      stringBuilder.append(WifiDirectAssistant.this.groupNetworkName);
      RobotLog.vv("WifiDirect", stringBuilder.toString());
      WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE);
    }
  }
  
  private class WifiDirectPeerListListener implements WifiP2pManager.PeerListListener {
    private WifiDirectPeerListListener() {}
    
    public void onPeersAvailable(WifiP2pDeviceList param1WifiP2pDeviceList) {
      WifiDirectAssistant.this.peers.clear();
      WifiDirectAssistant.this.peers.addAll(param1WifiP2pDeviceList.getDeviceList());
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("peers found: ");
      stringBuilder.append(WifiDirectAssistant.this.peers.size());
      RobotLog.vv("WifiDirect", stringBuilder.toString());
      if (WifiDirectAssistant.this.peers.size() == 0)
        WifiUtil.doLocationServicesCheck(); 
      for (WifiP2pDevice wifiP2pDevice : WifiDirectAssistant.this.peers) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("    peer: ");
        stringBuilder1.append(wifiP2pDevice.deviceAddress);
        stringBuilder1.append(" ");
        stringBuilder1.append(wifiP2pDevice.deviceName);
        RobotLog.vv("WifiDirect", stringBuilder1.toString());
      } 
      WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.PEERS_AVAILABLE);
    }
  }
  
  private class WifiP2pBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pBroadcastReceiver() {}
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      StringBuilder stringBuilder;
      WifiP2pInfo wifiP2pInfo;
      String str = param1Intent.getAction();
      boolean bool1 = "android.net.wifi.p2p.STATE_CHANGED".equals(str);
      boolean bool = true;
      if (bool1) {
        int i = param1Intent.getIntExtra("wifi_p2p_state", -1);
        WifiDirectAssistant wifiDirectAssistant = WifiDirectAssistant.this;
        if (i != 2)
          bool = false; 
        WifiDirectAssistant.access$1602(wifiDirectAssistant, bool);
        stringBuilder = new StringBuilder();
        stringBuilder.append("broadcast: state - enabled: ");
        stringBuilder.append(WifiDirectAssistant.this.isWifiP2pEnabled);
        RobotLog.dd("WifiDirect", stringBuilder.toString());
        return;
      } 
      if ("android.net.wifi.p2p.PEERS_CHANGED".equals(str)) {
        RobotLog.dd("WifiDirect", "broadcast: peers changed");
        WifiDirectAssistant.this.wifiP2pManager.requestPeers(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.peerListListener);
        return;
      } 
      if ("android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(str)) {
        NetworkInfo networkInfo = (NetworkInfo)param1Intent.getParcelableExtra("networkInfo");
        wifiP2pInfo = (WifiP2pInfo)param1Intent.getParcelableExtra("wifiP2pInfo");
        WifiP2pGroup wifiP2pGroup = (WifiP2pGroup)param1Intent.getParcelableExtra("p2pGroupInfo");
        RobotLog.dd("WifiDirect", "broadcast: connection changed: connectStatus=%s networkInfo.state=%s", new Object[] { WifiDirectAssistant.access$700(this.this$0), networkInfo.getState() });
        if (networkInfo.isConnected()) {
          if (!WifiDirectAssistant.this.isConnected()) {
            WifiDirectAssistant.this.preferencesHelper.writeStringPrefIfDifferent(stringBuilder.getString(R.string.pref_wifip2p_groupowner_connectedto), (wifiP2pGroup.getOwner()).deviceName);
            WifiDirectAssistant.this.wifiP2pManager.requestConnectionInfo(WifiDirectAssistant.this.wifiP2pChannel, WifiDirectAssistant.this.connectionListener);
            WifiDirectAssistant.this.wifiP2pManager.stopPeerDiscovery(WifiDirectAssistant.this.wifiP2pChannel, null);
            return;
          } 
        } else {
          WifiDirectAssistant.this.preferencesHelper.remove(stringBuilder.getString(R.string.pref_wifip2p_groupowner_connectedto));
          synchronized (WifiDirectAssistant.this.connectStatusLock) {
            WifiDirectAssistant.access$702(WifiDirectAssistant.this, NetworkConnection.ConnectStatus.NOT_CONNECTED);
            if (!WifiDirectAssistant.this.groupFormed)
              WifiDirectAssistant.this.discoverPeers(); 
            if (WifiDirectAssistant.this.isConnected()) {
              RobotLog.vv("WifiDirect", "disconnecting");
              WifiDirectAssistant.this.sendEvent(NetworkConnection.NetworkEvent.DISCONNECTED);
            } 
            WifiDirectAssistant.access$2002(WifiDirectAssistant.this, wifiP2pInfo.groupFormed);
            return;
          } 
        } 
      } else {
        if ("android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(wifiP2pInfo)) {
          RobotLog.dd("WifiDirect", "broadcast: this device changed");
          WifiDirectAssistant.this.onWifiP2pThisDeviceChanged((WifiP2pDevice)param1Intent.getParcelableExtra("wifiP2pDevice"));
          return;
        } 
        RobotLog.dd("WifiDirect", "broadcast: %s", new Object[] { wifiP2pInfo });
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\WifiDirectAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */