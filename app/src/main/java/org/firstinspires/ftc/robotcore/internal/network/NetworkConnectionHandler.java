package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.preference.PreferenceManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.exception.RobotProtocolException;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.robocol.PeerDiscovery;
import com.qualcomm.robotcore.robocol.RobocolDatagram;
import com.qualcomm.robotcore.robocol.RobocolDatagramSocket;
import com.qualcomm.robotcore.robocol.RobocolParsable;
import com.qualcomm.robotcore.util.Device;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;
import com.qualcomm.robotcore.wifi.NetworkConnection;
import com.qualcomm.robotcore.wifi.NetworkConnectionFactory;
import com.qualcomm.robotcore.wifi.NetworkType;
import com.qualcomm.robotcore.wifi.SoftApAssistant;
import com.qualcomm.robotcore.wifi.WifiDirectAssistant;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.RobotCoreGamepadManager;

public class NetworkConnectionHandler {
  private static final int IP_ADDRESS_TIMEOUT_SECONDS = 3;
  
  public static final String TAG = "NetworkConnectionHandler";
  
  private static final NetworkConnectionHandler theInstance = new NetworkConnectionHandler();
  
  protected static WifiManager wifiManager = null;
  
  protected final Object callbackLock = new Object();
  
  protected String connectionOwner;
  
  protected String connectionOwnerPassword;
  
  protected Context context;
  
  private final SendOnceRunnable.DisconnectionCallback disconnectionCallback = new SendOnceRunnable.DisconnectionCallback() {
      public void disconnected() {
        NetworkConnectionHandler.this.updatePeerStatus(false, false);
      }
    };
  
  private boolean isPeerConnected = false;
  
  protected final ElapsedTime lastRecvPacket = new ElapsedTime();
  
  protected NetworkConnection networkConnection = null;
  
  private final List<PeerStatusCallback> peerStatusCallbacks = new ArrayList<PeerStatusCallback>();
  
  private final Object peerStatusLock = new Object();
  
  protected RecvLoopRunnable recvLoopRunnable;
  
  protected volatile InetAddress remoteAddr;
  
  protected ScheduledFuture<?> sendLoopFuture;
  
  protected ScheduledExecutorService sendLoopService = null;
  
  protected final SendOnceRunnable sendOnceRunnable = new SendOnceRunnable(this.disconnectionCallback, this.lastRecvPacket);
  
  protected boolean setupNeeded = true;
  
  protected volatile NetworkSetupRunnable setupRunnable;
  
  protected volatile RobocolDatagramSocket socket;
  
  protected final NetworkConnectionCallbackChainer theNetworkConnectionCallback = new NetworkConnectionCallbackChainer();
  
  protected final RecvLoopCallbackChainer theRecvLoopCallback = new RecvLoopCallbackChainer();
  
  protected WifiManager.WifiLock wifiLock;
  
  public static NetworkType getDefaultNetworkType(Context paramContext) {
    return (Device.isRevControlHub() == true) ? NetworkType.RCWIRELESSAP : NetworkType.fromString(PreferenceManager.getDefaultSharedPreferences(paramContext).getString(paramContext.getString(R.string.pref_pairing_kind), NetworkType.globalDefaultAsString()));
  }
  
  public static NetworkConnectionHandler getInstance() {
    return theInstance;
  }
  
  protected static WifiManager getWifiManager() {
    if (wifiManager == null)
      wifiManager = (WifiManager)AppUtil.getDefContext().getSystemService("wifi"); 
    return wifiManager;
  }
  
  private CallbackResult handleSoftAPPeersAvailable() {
    CallbackResult callbackResult1;
    CallbackResult callbackResult2 = CallbackResult.NOT_HANDLED;
    Iterator<ScanResult> iterator = ((SoftApAssistant)this.networkConnection).getScanResults().iterator();
    while (true) {
      callbackResult1 = callbackResult2;
      if (iterator.hasNext()) {
        ScanResult scanResult = iterator.next();
        RobotLog.v(scanResult.SSID);
        if (scanResult.SSID.equalsIgnoreCase(this.connectionOwner)) {
          this.networkConnection.connect(this.connectionOwner, this.connectionOwnerPassword);
          callbackResult1 = CallbackResult.HANDLED;
          break;
        } 
        continue;
      } 
      break;
    } 
    return callbackResult1;
  }
  
  private CallbackResult handleWifiDirectPeersAvailable() {
    CallbackResult callbackResult1;
    CallbackResult callbackResult2 = CallbackResult.NOT_HANDLED;
    Iterator<WifiP2pDevice> iterator = ((WifiDirectAssistant)this.networkConnection).getPeers().iterator();
    while (true) {
      callbackResult1 = callbackResult2;
      if (iterator.hasNext()) {
        WifiP2pDevice wifiP2pDevice = iterator.next();
        if (wifiP2pDevice.deviceAddress.equalsIgnoreCase(this.connectionOwner)) {
          this.networkConnection.connect(wifiP2pDevice.deviceAddress);
          callbackResult1 = CallbackResult.HANDLED;
          break;
        } 
        continue;
      } 
      break;
    } 
    return callbackResult1;
  }
  
  private void initNetworkConnection(NetworkType paramNetworkType) {
    NetworkConnection networkConnection = this.networkConnection;
    if (networkConnection != null && networkConnection.getNetworkType() != paramNetworkType) {
      shutdown();
      this.networkConnection = null;
    } 
    if (this.networkConnection == null) {
      this.networkConnection = NetworkConnectionFactory.getNetworkConnection(paramNetworkType, this.context);
      synchronized (this.callbackLock) {
        this.networkConnection.setCallback(this.theNetworkConnectionCallback);
        return;
      } 
    } 
  }
  
  public static WifiManager.WifiLock newWifiLock() {
    return getWifiManager().createWifiLock(3, "");
  }
  
  private void updatePeerStatus(boolean paramBoolean1, boolean paramBoolean2) {
    synchronized (this.peerStatusLock) {
      boolean bool;
      if (paramBoolean1 != this.isPeerConnected) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isPeerConnected = paramBoolean1;
      if (bool || paramBoolean2)
        for (PeerStatusCallback peerStatusCallback : this.peerStatusCallbacks) {
          if (this.isPeerConnected) {
            peerStatusCallback.onPeerConnected();
            continue;
          } 
          peerStatusCallback.onPeerDisconnected();
        }  
      if (bool)
        if (this.isPeerConnected) {
          RobotLog.vv("NetworkConnectionHandler", "Peer connection established");
        } else {
          RobotLog.vv("NetworkConnectionHandler", "Peer connection lost");
        }  
      return;
    } 
  }
  
  public void acquireWifiLock() {
    WifiManager.WifiLock wifiLock = this.wifiLock;
    if (wifiLock != null)
      wifiLock.acquire(); 
  }
  
  public void cancelConnectionSearch() {
    this.networkConnection.cancelPotentialConnections();
  }
  
  public void clientDisconnect() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield sendOnceRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/SendOnceRunnable;
    //   6: ifnull -> 16
    //   9: aload_0
    //   10: getfield sendOnceRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/SendOnceRunnable;
    //   13: invokevirtual clearCommands : ()V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield remoteAddr : Ljava/net/InetAddress;
    //   21: aload_0
    //   22: monitorexit
    //   23: return
    //   24: astore_1
    //   25: aload_0
    //   26: monitorexit
    //   27: aload_1
    //   28: athrow
    // Exception table:
    //   from	to	target	type
    //   2	16	24	finally
    //   16	21	24	finally
  }
  
  public boolean connectedWithUnexpectedDevice() {
    if (getNetworkType() != NetworkType.WIRELESSAP) {
      String str = this.connectionOwner;
      if (str != null && !str.equals(this.networkConnection.getConnectionOwnerMacAddress())) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Network Connection - connected to ");
        stringBuilder.append(this.networkConnection.getConnectionOwnerMacAddress());
        stringBuilder.append(", expected ");
        stringBuilder.append(this.connectionOwner);
        RobotLog.ee("NetworkConnectionHandler", stringBuilder.toString());
        return true;
      } 
    } 
    return false;
  }
  
  public boolean connectingOrConnected() {
    NetworkConnection.ConnectStatus connectStatus = this.networkConnection.getConnectStatus();
    return (connectStatus == NetworkConnection.ConnectStatus.CONNECTED || connectStatus == NetworkConnection.ConnectStatus.CONNECTING);
  }
  
  public boolean connectionMatches(String paramString) {
    String str = this.connectionOwner;
    return (str != null && str.equals(paramString));
  }
  
  public void discoverPotentialConnections() {
    this.networkConnection.discoverPotentialConnections();
  }
  
  public long getBytesPerSecond() {
    NetworkSetupRunnable networkSetupRunnable = this.setupRunnable;
    return (networkSetupRunnable != null) ? networkSetupRunnable.getBytesPerSecond() : 0L;
  }
  
  public String getConnectionOwnerName() {
    return this.networkConnection.getConnectionOwnerName();
  }
  
  public InetAddress getCurrentPeerAddr() {
    return this.remoteAddr;
  }
  
  public String getDeviceName() {
    return this.networkConnection.getDeviceName();
  }
  
  public String getFailureReason() {
    return this.networkConnection.getFailureReason();
  }
  
  public NetworkConnection getNetworkConnection() {
    return this.networkConnection;
  }
  
  public NetworkType getNetworkType() {
    NetworkConnection networkConnection = this.networkConnection;
    return (networkConnection == null) ? NetworkType.UNKNOWN_NETWORK_TYPE : networkConnection.getNetworkType();
  }
  
  public long getRxDataCount() {
    NetworkSetupRunnable networkSetupRunnable = this.setupRunnable;
    return (networkSetupRunnable != null) ? networkSetupRunnable.getRxDataCount() : 0L;
  }
  
  public long getTxDataCount() {
    NetworkSetupRunnable networkSetupRunnable = this.setupRunnable;
    return (networkSetupRunnable != null) ? networkSetupRunnable.getTxDataCount() : 0L;
  }
  
  public int getWifiChannel() {
    return this.networkConnection.getWifiChannel();
  }
  
  public CallbackResult handleConnectionInfoAvailable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   5: astore_2
    //   6: new java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial <init> : ()V
    //   13: astore_3
    //   14: aload_3
    //   15: ldc_w 'Handling new network connection infomation, connected: '
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: pop
    //   22: aload_3
    //   23: aload_0
    //   24: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   27: invokevirtual isConnected : ()Z
    //   30: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   33: pop
    //   34: aload_3
    //   35: ldc_w ' setup needed: '
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: pop
    //   42: aload_3
    //   43: aload_0
    //   44: getfield setupNeeded : Z
    //   47: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   50: pop
    //   51: ldc 'NetworkConnectionHandler'
    //   53: aload_3
    //   54: invokevirtual toString : ()Ljava/lang/String;
    //   57: invokestatic ii : (Ljava/lang/String;Ljava/lang/String;)V
    //   60: aload_0
    //   61: getfield lastRecvPacket : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   64: invokevirtual reset : ()V
    //   67: aload_0
    //   68: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   71: invokevirtual isConnected : ()Z
    //   74: ifeq -> 238
    //   77: aload_0
    //   78: getfield setupNeeded : Z
    //   81: ifeq -> 238
    //   84: aload_0
    //   85: iconst_0
    //   86: putfield setupNeeded : Z
    //   89: aload_0
    //   90: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   93: invokevirtual getNetworkType : ()Lcom/qualcomm/robotcore/wifi/NetworkType;
    //   96: getstatic com/qualcomm/robotcore/wifi/NetworkType.WIFIDIRECT : Lcom/qualcomm/robotcore/wifi/NetworkType;
    //   99: if_acmpeq -> 184
    //   102: new com/qualcomm/robotcore/util/ElapsedTime
    //   105: dup
    //   106: invokespecial <init> : ()V
    //   109: astore_3
    //   110: invokestatic getWifiManager : ()Landroid/net/wifi/WifiManager;
    //   113: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   116: invokevirtual getIpAddress : ()I
    //   119: istore_1
    //   120: iload_1
    //   121: ifne -> 184
    //   124: aload_3
    //   125: invokevirtual seconds : ()D
    //   128: ldc2_w 3.0
    //   131: dcmpg
    //   132: ifge -> 184
    //   135: invokestatic currentThread : ()Ljava/lang/Thread;
    //   138: invokevirtual isInterrupted : ()Z
    //   141: ifne -> 184
    //   144: invokestatic getWifiManager : ()Landroid/net/wifi/WifiManager;
    //   147: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   150: invokevirtual getIpAddress : ()I
    //   153: istore_1
    //   154: ldc2_w 50
    //   157: invokestatic sleep : (J)V
    //   160: goto -> 120
    //   163: astore #4
    //   165: ldc 'NetworkConnectionHandler'
    //   167: aload #4
    //   169: ldc_w 'Thread interrupted while waiting for IP address'
    //   172: invokestatic ee : (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;)V
    //   175: invokestatic currentThread : ()Ljava/lang/Thread;
    //   178: invokevirtual interrupt : ()V
    //   181: goto -> 120
    //   184: aload_0
    //   185: getfield callbackLock : Ljava/lang/Object;
    //   188: astore_3
    //   189: aload_3
    //   190: monitorenter
    //   191: aload_0
    //   192: new org/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable
    //   195: dup
    //   196: aload_0
    //   197: getfield theRecvLoopCallback : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler$RecvLoopCallbackChainer;
    //   200: aload_0
    //   201: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   204: aload_0
    //   205: getfield lastRecvPacket : Lcom/qualcomm/robotcore/util/ElapsedTime;
    //   208: invokespecial <init> : (Lorg/firstinspires/ftc/robotcore/internal/network/RecvLoopRunnable$RecvLoopCallback;Lcom/qualcomm/robotcore/wifi/NetworkConnection;Lcom/qualcomm/robotcore/util/ElapsedTime;)V
    //   211: putfield setupRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable;
    //   214: aload_3
    //   215: monitorexit
    //   216: new java/lang/Thread
    //   219: dup
    //   220: aload_0
    //   221: getfield setupRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable;
    //   224: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   227: invokevirtual start : ()V
    //   230: goto -> 238
    //   233: astore_2
    //   234: aload_3
    //   235: monitorexit
    //   236: aload_2
    //   237: athrow
    //   238: aload_0
    //   239: monitorexit
    //   240: aload_2
    //   241: areturn
    //   242: astore_2
    //   243: aload_0
    //   244: monitorexit
    //   245: aload_2
    //   246: athrow
    // Exception table:
    //   from	to	target	type
    //   2	120	242	finally
    //   124	154	242	finally
    //   154	160	163	java/lang/InterruptedException
    //   154	160	242	finally
    //   165	181	242	finally
    //   184	191	242	finally
    //   191	216	233	finally
    //   216	230	242	finally
    //   234	236	233	finally
    //   236	238	242	finally
  }
  
  public CallbackResult handlePeersAvailable() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: getstatic org/firstinspires/ftc/robotcore/internal/network/CallbackResult.NOT_HANDLED : Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   5: astore_2
    //   6: aload_0
    //   7: getfield networkConnection : Lcom/qualcomm/robotcore/wifi/NetworkConnection;
    //   10: invokevirtual getNetworkType : ()Lcom/qualcomm/robotcore/wifi/NetworkType;
    //   13: astore_3
    //   14: getstatic org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler$2.$SwitchMap$com$qualcomm$robotcore$wifi$NetworkType : [I
    //   17: aload_3
    //   18: invokevirtual ordinal : ()I
    //   21: iaload
    //   22: istore_1
    //   23: iload_1
    //   24: iconst_1
    //   25: if_icmpeq -> 93
    //   28: iload_1
    //   29: iconst_2
    //   30: if_icmpeq -> 85
    //   33: iload_1
    //   34: iconst_3
    //   35: if_icmpeq -> 46
    //   38: iload_1
    //   39: iconst_4
    //   40: if_icmpeq -> 46
    //   43: goto -> 98
    //   46: new java/lang/StringBuilder
    //   49: dup
    //   50: invokespecial <init> : ()V
    //   53: astore #4
    //   55: aload #4
    //   57: ldc_w 'Unhandled peers available event: '
    //   60: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   63: pop
    //   64: aload #4
    //   66: aload_3
    //   67: invokevirtual toString : ()Ljava/lang/String;
    //   70: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   73: pop
    //   74: aload #4
    //   76: invokevirtual toString : ()Ljava/lang/String;
    //   79: invokestatic e : (Ljava/lang/String;)V
    //   82: goto -> 98
    //   85: aload_0
    //   86: invokespecial handleSoftAPPeersAvailable : ()Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   89: astore_2
    //   90: goto -> 98
    //   93: aload_0
    //   94: invokespecial handleWifiDirectPeersAvailable : ()Lorg/firstinspires/ftc/robotcore/internal/network/CallbackResult;
    //   97: astore_2
    //   98: aload_0
    //   99: monitorexit
    //   100: aload_2
    //   101: areturn
    //   102: astore_2
    //   103: aload_0
    //   104: monitorexit
    //   105: aload_2
    //   106: athrow
    // Exception table:
    //   from	to	target	type
    //   2	23	102	finally
    //   46	82	102	finally
    //   85	90	102	finally
    //   93	98	102	finally
  }
  
  public void init(WifiManager.WifiLock paramWifiLock, NetworkType paramNetworkType, String paramString1, String paramString2, Context paramContext, RobotCoreGamepadManager paramRobotCoreGamepadManager) {
    this.wifiLock = paramWifiLock;
    this.connectionOwner = paramString1;
    this.connectionOwnerPassword = paramString2;
    this.context = paramContext;
    this.sendOnceRunnable.parameters.gamepadManager = paramRobotCoreGamepadManager;
    shutdown();
    this.networkConnection = null;
    initNetworkConnection(paramNetworkType);
    startWifiAndDiscoverConnections();
  }
  
  public void init(NetworkType paramNetworkType, Context paramContext) {
    this.context = paramContext;
    initNetworkConnection(paramNetworkType);
  }
  
  public void injectReceivedCommand(Command paramCommand) {
    NetworkSetupRunnable networkSetupRunnable = this.setupRunnable;
    if (networkSetupRunnable != null) {
      paramCommand.setIsInjected(true);
      networkSetupRunnable.injectReceivedCommand(paramCommand);
      RobotLog.vv("Robocol", "locally injecting %s", new Object[] { paramCommand.getName() });
      return;
    } 
    RobotLog.vv("NetworkConnectionHandler", "injectReceivedCommand(): setupRunnable==null; command ignored");
  }
  
  public boolean isNetworkConnected() {
    return this.networkConnection.isConnected();
  }
  
  public boolean isPeerConnected() {
    synchronized (this.peerStatusLock) {
      return this.isPeerConnected;
    } 
  }
  
  public boolean isWifiDirect() {
    return this.networkConnection.getNetworkType().equals(NetworkType.WIFIDIRECT);
  }
  
  public CallbackResult processAcknowledgments(Command paramCommand) throws RobotCoreException {
    if (paramCommand.isAcknowledged()) {
      if (SendOnceRunnable.DEBUG)
        RobotLog.vv("Robocol", "received ack: %s(%d)", new Object[] { paramCommand.getName(), Integer.valueOf(paramCommand.getSequenceNumber()) }); 
      removeCommand(paramCommand);
      return CallbackResult.HANDLED;
    } 
    paramCommand.acknowledge();
    sendCommand(paramCommand);
    return CallbackResult.NOT_HANDLED;
  }
  
  public void pushNetworkConnectionCallback(NetworkConnection.NetworkConnectionCallback paramNetworkConnectionCallback) {
    synchronized (this.callbackLock) {
      this.theNetworkConnectionCallback.push(paramNetworkConnectionCallback);
      return;
    } 
  }
  
  public void pushReceiveLoopCallback(RecvLoopRunnable.RecvLoopCallback paramRecvLoopCallback) {
    synchronized (this.callbackLock) {
      this.theRecvLoopCallback.push(paramRecvLoopCallback);
      return;
    } 
  }
  
  public boolean readyForCommandProcessing() {
    synchronized (this.callbackLock) {
      if (this.recvLoopRunnable == null)
        return false; 
      return true;
    } 
  }
  
  public boolean registerPeerStatusCallback(PeerStatusCallback paramPeerStatusCallback) {
    synchronized (this.peerStatusLock) {
      this.peerStatusCallbacks.add(paramPeerStatusCallback);
      return this.isPeerConnected;
    } 
  }
  
  public boolean removeCommand(Command paramCommand) {
    SendOnceRunnable sendOnceRunnable = this.sendOnceRunnable;
    return (sendOnceRunnable != null && sendOnceRunnable.removeCommand(paramCommand));
  }
  
  public void removeNetworkConnectionCallback(NetworkConnection.NetworkConnectionCallback paramNetworkConnectionCallback) {
    synchronized (this.callbackLock) {
      this.theNetworkConnectionCallback.remove(paramNetworkConnectionCallback);
      return;
    } 
  }
  
  public void removeReceiveLoopCallback(RecvLoopRunnable.RecvLoopCallback paramRecvLoopCallback) {
    synchronized (this.callbackLock) {
      this.theRecvLoopCallback.remove(paramRecvLoopCallback);
      return;
    } 
  }
  
  public void sendCommand(Command paramCommand) {
    SendOnceRunnable sendOnceRunnable = this.sendOnceRunnable;
    if (sendOnceRunnable != null)
      sendOnceRunnable.sendCommand(paramCommand); 
  }
  
  public void sendDataToPeer(RobocolParsable paramRobocolParsable) throws RobotCoreException {
    InetAddress inetAddress = this.remoteAddr;
    if (inetAddress != null)
      sendDatagram(new RobocolDatagram(paramRobocolParsable, inetAddress)); 
  }
  
  public void sendDatagram(RobocolDatagram paramRobocolDatagram) {
    RobocolDatagramSocket robocolDatagramSocket = this.socket;
    if (robocolDatagramSocket != null)
      robocolDatagramSocket.send(paramRobocolDatagram); 
  }
  
  public void sendReply(Command paramCommand1, Command paramCommand2) {
    if (wasTransmittedRemotely(paramCommand1)) {
      sendCommand(paramCommand2);
      return;
    } 
    injectReceivedCommand(paramCommand2);
  }
  
  public void setRecvLoopRunnable(RecvLoopRunnable paramRecvLoopRunnable) {
    synchronized (this.callbackLock) {
      this.recvLoopRunnable = paramRecvLoopRunnable;
      paramRecvLoopRunnable.setCallback(this.theRecvLoopCallback);
      return;
    } 
  }
  
  public void shutdown() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield setupRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable;
    //   6: ifnull -> 21
    //   9: aload_0
    //   10: getfield setupRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable;
    //   13: invokevirtual shutdown : ()V
    //   16: aload_0
    //   17: aconst_null
    //   18: putfield setupRunnable : Lorg/firstinspires/ftc/robotcore/internal/network/NetworkSetupRunnable;
    //   21: aload_0
    //   22: getfield sendLoopFuture : Ljava/util/concurrent/ScheduledFuture;
    //   25: ifnull -> 44
    //   28: aload_0
    //   29: getfield sendLoopFuture : Ljava/util/concurrent/ScheduledFuture;
    //   32: iconst_1
    //   33: invokeinterface cancel : (Z)Z
    //   38: pop
    //   39: aload_0
    //   40: aconst_null
    //   41: putfield sendLoopFuture : Ljava/util/concurrent/ScheduledFuture;
    //   44: aload_0
    //   45: getfield sendLoopService : Ljava/util/concurrent/ScheduledExecutorService;
    //   48: ifnull -> 65
    //   51: aload_0
    //   52: getfield sendLoopService : Ljava/util/concurrent/ScheduledExecutorService;
    //   55: invokeinterface shutdown : ()V
    //   60: aload_0
    //   61: aconst_null
    //   62: putfield sendLoopService : Ljava/util/concurrent/ScheduledExecutorService;
    //   65: aload_0
    //   66: aconst_null
    //   67: putfield remoteAddr : Ljava/net/InetAddress;
    //   70: aload_0
    //   71: iconst_1
    //   72: putfield setupNeeded : Z
    //   75: aload_0
    //   76: monitorexit
    //   77: return
    //   78: astore_1
    //   79: aload_0
    //   80: monitorexit
    //   81: aload_1
    //   82: athrow
    // Exception table:
    //   from	to	target	type
    //   2	21	78	finally
    //   21	44	78	finally
    //   44	65	78	finally
    //   65	75	78	finally
  }
  
  public void startConnection(String paramString1, String paramString2) {
    this.connectionOwner = paramString1;
    this.connectionOwnerPassword = paramString2;
    this.networkConnection.connect(paramString1, paramString2);
  }
  
  public void startKeepAlives() {
    SendOnceRunnable sendOnceRunnable = this.sendOnceRunnable;
    if (sendOnceRunnable != null) {
      boolean bool;
      SendOnceRunnable.Parameters parameters = sendOnceRunnable.parameters;
      if (AppUtil.getInstance().isDriverStation() && Device.phoneImplementsAggressiveWifiScanning()) {
        bool = true;
      } else {
        bool = false;
      } 
      parameters.originateKeepAlives = bool;
    } 
  }
  
  public void startWifiAndDiscoverConnections() {
    acquireWifiLock();
    this.networkConnection.enable();
    if (!this.networkConnection.isConnected())
      this.networkConnection.discoverPotentialConnections(); 
  }
  
  public void stop() {
    this.networkConnection.disable();
    WifiManager.WifiLock wifiLock = this.wifiLock;
    if (wifiLock != null && wifiLock.isHeld())
      this.wifiLock.release(); 
  }
  
  public void stopKeepAlives() {
    SendOnceRunnable sendOnceRunnable = this.sendOnceRunnable;
    if (sendOnceRunnable != null)
      sendOnceRunnable.parameters.originateKeepAlives = false; 
  }
  
  public void stopPeerDiscovery() {
    if (this.setupRunnable != null)
      this.setupRunnable.stopPeerDiscovery(); 
  }
  
  public void updateConnection(RobocolDatagram paramRobocolDatagram) throws RobotCoreException, RobotProtocolException {
    /* monitor enter ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler}} */
    try {
      PeerDiscovery.forReceive().fromByteArray(paramRobocolDatagram.getData());
      this.lastRecvPacket.reset();
      if (paramRobocolDatagram.getAddress().equals(this.remoteAddr)) {
        updatePeerStatus(true, false);
        /* monitor exit ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler}} */
        return;
      } 
      this.remoteAddr = paramRobocolDatagram.getAddress();
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("new remote peer discovered: ");
      stringBuilder.append(this.remoteAddr.getHostAddress());
      RobotLog.vv("PeerDiscovery", stringBuilder.toString());
      if (this.setupRunnable != null)
        this.socket = this.setupRunnable.getSocket(); 
      if (this.socket != null) {
        if (this.sendLoopFuture == null || this.sendLoopFuture.isDone()) {
          RobotLog.vv("NetworkConnectionHandler", "starting sending loop");
          ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
          this.sendLoopService = scheduledExecutorService;
          this.sendLoopFuture = scheduledExecutorService.scheduleAtFixedRate(this.sendOnceRunnable, 0L, 40L, TimeUnit.MILLISECONDS);
        } 
        updatePeerStatus(true, true);
      } 
      /* monitor exit ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler}} */
      return;
    } catch (RobotProtocolException robotProtocolException) {
      RobotLog.ee("NetworkConnectionHandler", robotProtocolException.getMessage());
      throw robotProtocolException;
    } finally {}
    /* monitor exit ThisExpression{ObjectType{org/firstinspires/ftc/robotcore/internal/network/NetworkConnectionHandler}} */
    throw paramRobocolDatagram;
  }
  
  protected boolean wasTransmittedRemotely(Command paramCommand) {
    return paramCommand.isInjected() ^ true;
  }
  
  protected class NetworkConnectionCallbackChainer implements NetworkConnection.NetworkConnectionCallback {
    protected final CopyOnWriteArrayList<NetworkConnection.NetworkConnectionCallback> callbacks = new CopyOnWriteArrayList<NetworkConnection.NetworkConnectionCallback>();
    
    public CallbackResult onNetworkConnectionEvent(NetworkConnection.NetworkEvent param1NetworkEvent) {
      Iterator<NetworkConnection.NetworkConnectionCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((NetworkConnection.NetworkConnectionCallback)iterator.next()).onNetworkConnectionEvent(param1NetworkEvent).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    void push(NetworkConnection.NetworkConnectionCallback param1NetworkConnectionCallback) {
      synchronized (this.callbacks) {
        remove(param1NetworkConnectionCallback);
        if (param1NetworkConnectionCallback != null && !this.callbacks.contains(param1NetworkConnectionCallback))
          this.callbacks.add(0, param1NetworkConnectionCallback); 
        return;
      } 
    }
    
    void remove(NetworkConnection.NetworkConnectionCallback param1NetworkConnectionCallback) {
      // Byte code:
      //   0: aload_0
      //   1: getfield callbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
      //   4: astore_2
      //   5: aload_2
      //   6: monitorenter
      //   7: aload_1
      //   8: ifnull -> 20
      //   11: aload_0
      //   12: getfield callbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
      //   15: aload_1
      //   16: invokevirtual remove : (Ljava/lang/Object;)Z
      //   19: pop
      //   20: aload_2
      //   21: monitorexit
      //   22: return
      //   23: astore_1
      //   24: aload_2
      //   25: monitorexit
      //   26: aload_1
      //   27: athrow
      // Exception table:
      //   from	to	target	type
      //   11	20	23	finally
      //   20	22	23	finally
      //   24	26	23	finally
    }
  }
  
  protected class RecvLoopCallbackChainer implements RecvLoopRunnable.RecvLoopCallback {
    protected final CopyOnWriteArrayList<RecvLoopRunnable.RecvLoopCallback> callbacks = new CopyOnWriteArrayList<RecvLoopRunnable.RecvLoopCallback>();
    
    public CallbackResult commandEvent(Command param1Command) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      boolean bool = false;
      while (iterator.hasNext()) {
        CallbackResult callbackResult = ((RecvLoopRunnable.RecvLoopCallback)iterator.next()).commandEvent(param1Command);
        if (bool || callbackResult.isHandled()) {
          bool = true;
        } else {
          bool = false;
        } 
        if (callbackResult.stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      if (!bool) {
        StringBuilder stringBuilder = new StringBuilder();
        for (RecvLoopRunnable.RecvLoopCallback recvLoopCallback : this.callbacks) {
          if (stringBuilder.length() > 0)
            stringBuilder.append(","); 
          stringBuilder.append(recvLoopCallback.getClass().getSimpleName());
        } 
        RobotLog.vv("Robocol", "unable to process command %s callbacks=%s", new Object[] { param1Command.getName(), stringBuilder.toString() });
      } 
      return bool ? CallbackResult.HANDLED : CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult emptyEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).emptyEvent(param1RobocolDatagram).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult gamepadEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).gamepadEvent(param1RobocolDatagram).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult heartbeatEvent(RobocolDatagram param1RobocolDatagram, long param1Long) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).heartbeatEvent(param1RobocolDatagram, param1Long).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult packetReceived(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).packetReceived(param1RobocolDatagram).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult peerDiscoveryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).peerDiscoveryEvent(param1RobocolDatagram).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    void push(RecvLoopRunnable.RecvLoopCallback param1RecvLoopCallback) {
      synchronized (this.callbacks) {
        remove(param1RecvLoopCallback);
        if (param1RecvLoopCallback != null && !this.callbacks.contains(param1RecvLoopCallback))
          this.callbacks.add(0, param1RecvLoopCallback); 
        return;
      } 
    }
    
    void remove(RecvLoopRunnable.RecvLoopCallback param1RecvLoopCallback) {
      // Byte code:
      //   0: aload_0
      //   1: getfield callbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
      //   4: astore_2
      //   5: aload_2
      //   6: monitorenter
      //   7: aload_1
      //   8: ifnull -> 20
      //   11: aload_0
      //   12: getfield callbacks : Ljava/util/concurrent/CopyOnWriteArrayList;
      //   15: aload_1
      //   16: invokevirtual remove : (Ljava/lang/Object;)Z
      //   19: pop
      //   20: aload_2
      //   21: monitorexit
      //   22: return
      //   23: astore_1
      //   24: aload_2
      //   25: monitorexit
      //   26: aload_1
      //   27: athrow
      // Exception table:
      //   from	to	target	type
      //   11	20	23	finally
      //   20	22	23	finally
      //   24	26	23	finally
    }
    
    public CallbackResult reportGlobalError(String param1String, boolean param1Boolean) {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).reportGlobalError(param1String, param1Boolean).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
    
    public CallbackResult telemetryEvent(RobocolDatagram param1RobocolDatagram) throws RobotCoreException {
      Iterator<RecvLoopRunnable.RecvLoopCallback> iterator = this.callbacks.iterator();
      while (iterator.hasNext()) {
        if (((RecvLoopRunnable.RecvLoopCallback)iterator.next()).telemetryEvent(param1RobocolDatagram).stopDispatch())
          return CallbackResult.HANDLED; 
      } 
      return CallbackResult.NOT_HANDLED;
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\NetworkConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */