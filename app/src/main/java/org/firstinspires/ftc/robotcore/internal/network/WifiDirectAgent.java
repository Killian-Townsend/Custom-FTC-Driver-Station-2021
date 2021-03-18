package org.firstinspires.ftc.robotcore.internal.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.ClassUtil;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.firstinspires.ftc.robotcore.external.Consumer;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.Assert;
import org.firstinspires.ftc.robotcore.internal.system.CallbackRegistrar;

public class WifiDirectAgent extends WifiStartStoppable {
  public static final String TAG = "NetDiscover_wifiDirectAgent";
  
  protected static WifiDirectAgent theInstance;
  
  protected static StartResult theInstanceStartResult = new StartResult();
  
  protected final CallbackRegistrar<Callback> callbacks = new CallbackRegistrar();
  
  protected final ChannelListener channelListener = new ChannelListener();
  
  protected final Context context = (Context)AppUtil.getInstance().getApplication();
  
  protected IntentFilter intentFilter;
  
  protected boolean isWifiP2pEnabled = false;
  
  protected final CallbackLooper looper = CallbackLooper.getDefault();
  
  protected final WifiBroadcastReceiver wifiBroadcastReceiver;
  
  protected final WifiP2pManager.Channel wifiP2pChannel;
  
  protected final WifiP2pManager wifiP2pManager;
  
  protected NetworkInfo.State wifiP2pState = NetworkInfo.State.UNKNOWN;
  
  protected WifiState wifiState = WifiState.UNKNOWN;
  
  static {
    WifiDirectAgent wifiDirectAgent = new WifiDirectAgent();
    theInstance = wifiDirectAgent;
    wifiDirectAgent.start(theInstanceStartResult);
  }
  
  public WifiDirectAgent() {
    super(0);
    WifiP2pManager wifiP2pManager = (WifiP2pManager)this.context.getSystemService("wifip2p");
    this.wifiP2pManager = wifiP2pManager;
    this.wifiP2pChannel = wifiP2pManager.initialize(this.context, this.looper.getLooper(), this.channelListener);
    this.wifiBroadcastReceiver = new WifiBroadcastReceiver();
    IntentFilter intentFilter = new IntentFilter();
    this.intentFilter = intentFilter;
    intentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
    this.intentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
    this.intentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
    this.intentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
    this.intentFilter.addAction("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE");
    this.intentFilter.addAction("android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED");
    this.intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
  }
  
  protected static String format(WifiP2pDevice paramWifiP2pDevice) {
    return paramWifiP2pDevice.toString().replace(": ", "=").replace("\n ", " ");
  }
  
  public static WifiDirectAgent getInstance() {
    return theInstance;
  }
  
  public boolean disconnectFromWifiDirect() {
    return ((Boolean)lockCompletion(Boolean.valueOf(false), new Func<Boolean>() {
          public Boolean value() {
            boolean bool2 = WifiDirectAgent.this.resetCompletion();
            boolean bool1 = bool2;
            if (bool2)
              try {
                WifiDirectAgent.this.wifiP2pManager.requestGroupInfo(WifiDirectAgent.this.wifiP2pChannel, new WifiP2pManager.GroupInfoListener() {
                      public void onGroupInfoAvailable(WifiP2pGroup param2WifiP2pGroup) {
                        if (param2WifiP2pGroup != null && param2WifiP2pGroup.isGroupOwner()) {
                          WifiDirectAgent.this.wifiP2pManager.removeGroup(WifiDirectAgent.this.wifiP2pChannel, new WifiP2pManager.ActionListener() {
                                public void onFailure(int param3Int) {
                                  WifiDirectAgent.this.releaseCompletion(false);
                                }
                                
                                public void onSuccess() {
                                  WifiDirectAgent.this.releaseCompletion(true);
                                }
                              });
                          return;
                        } 
                        WifiDirectAgent.this.releaseCompletion(false);
                      }
                    });
                bool1 = WifiDirectAgent.this.waitForCompletion();
              } catch (InterruptedException interruptedException) {
                bool1 = WifiDirectAgent.this.receivedCompletionInterrupt(interruptedException);
              }  
            return Boolean.valueOf(bool1);
          }
        })).booleanValue();
  }
  
  public void doListen() {
    this.context.registerReceiver(this.wifiBroadcastReceiver, this.intentFilter, null, this.looper.getHandler());
  }
  
  public void doNotListen() {
    this.context.unregisterReceiver(this.wifiBroadcastReceiver);
  }
  
  protected boolean doStart() {
    doListen();
    (new WifiDirectPersistentGroupManager(this)).requestPersistentGroups(new WifiDirectPersistentGroupManager.PersistentGroupInfoListener() {
          public void onPersistentGroupInfoAvailable(Collection<WifiP2pGroup> param1Collection) {
            Iterator<WifiP2pGroup> iterator = param1Collection.iterator();
            while (iterator.hasNext()) {
              RobotLog.vv("NetDiscover_wifiDirectAgent", "found persistent group: %s", new Object[] { ((WifiP2pGroup)iterator.next()).getNetworkName() });
            } 
          }
        });
    return true;
  }
  
  protected void doStop() {
    doNotListen();
  }
  
  public CallbackLooper getLooper() {
    return this.looper;
  }
  
  public String getTag() {
    return "NetDiscover_wifiDirectAgent";
  }
  
  public NetworkInfo.State getWifiDirectState() {
    return this.wifiP2pState;
  }
  
  public WifiP2pManager.Channel getWifiP2pChannel() {
    return this.wifiP2pChannel;
  }
  
  public WifiP2pManager getWifiP2pManager() {
    return this.wifiP2pManager;
  }
  
  public WifiState getWifiState() {
    return this.wifiState;
  }
  
  @Deprecated
  public boolean isAirplaneModeOn() {
    return WifiUtil.isAirplaneModeOn();
  }
  
  @Deprecated
  public boolean isBluetoothOn() {
    return WifiUtil.isBluetoothOn();
  }
  
  public boolean isLooperThread() {
    Assert.assertNotNull(this.looper);
    return CallbackLooper.isLooperThread();
  }
  
  @Deprecated
  public boolean isWifiConnected() {
    return WifiUtil.isWifiConnected();
  }
  
  public boolean isWifiDirectConnected() {
    NetworkInfo.State state = getWifiDirectState();
    return (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING);
  }
  
  public boolean isWifiDirectEnabled() {
    return this.isWifiP2pEnabled;
  }
  
  @Deprecated
  public boolean isWifiEnabled() {
    return WifiUtil.isWifiEnabled();
  }
  
  public void registerCallback(Callback paramCallback) {
    this.callbacks.registerCallback(paramCallback);
  }
  
  public void setWifiP2pChannels(int paramInt1, int paramInt2, WifiP2pManager.ActionListener paramActionListener) {
    Method method = ClassUtil.getDeclaredMethod(getWifiP2pManager().getClass(), "setWifiP2pChannels", new Class[] { WifiP2pManager.Channel.class, int.class, int.class, WifiP2pManager.ActionListener.class });
    if (method != null) {
      ClassUtil.invoke(getWifiP2pManager(), method, new Object[] { getWifiP2pChannel(), Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), paramActionListener });
      return;
    } 
    throw new RuntimeException("setWifiP2pChannels() is not supported on this device");
  }
  
  public void unregisterCallback(Callback paramCallback) {
    this.callbacks.unregisterCallback(paramCallback);
  }
  
  public static interface Callback {
    void onReceive(Context param1Context, Intent param1Intent);
  }
  
  protected class ChannelListener implements WifiP2pManager.ChannelListener {
    public void onChannelDisconnected() {}
  }
  
  protected class WifiBroadcastReceiver extends BroadcastReceiver {
    protected static final String TAG = "NetDiscover_wifiDirectAgent_bcast";
    
    protected void dump(NetworkInfo param1NetworkInfo) {
      Assert.assertNotNull(param1NetworkInfo);
      RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "NetworkInfo: %s", new Object[] { param1NetworkInfo.toString() });
    }
    
    protected void dump(WifiP2pDevice param1WifiP2pDevice) {
      RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "this device changed: %s", new Object[] { WifiDirectAgent.format(param1WifiP2pDevice) });
    }
    
    protected void dump(WifiP2pDeviceList param1WifiP2pDeviceList) {
      ArrayList arrayList = new ArrayList(param1WifiP2pDeviceList.getDeviceList());
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("peers found: count=");
      stringBuilder.append(arrayList.size());
      RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", stringBuilder.toString());
      for (WifiP2pDevice wifiP2pDevice : arrayList) {
        StringBuilder stringBuilder1 = new StringBuilder();
        stringBuilder1.append("    peer: ");
        stringBuilder1.append(wifiP2pDevice.deviceAddress);
        stringBuilder1.append(" ");
        stringBuilder1.append(wifiP2pDevice.deviceName);
        RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", stringBuilder1.toString());
      } 
    }
    
    protected void dump(WifiP2pGroup param1WifiP2pGroup) {
      Assert.assertNotNull(param1WifiP2pGroup);
      RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "WifiP2pGroup: %s", new Object[] { param1WifiP2pGroup.toString().replace("\n ", ", ") });
    }
    
    protected void dump(WifiP2pInfo param1WifiP2pInfo) {
      Assert.assertNotNull(param1WifiP2pInfo);
      RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "WifiP2pInfo: %s", new Object[] { param1WifiP2pInfo.toString() });
    }
    
    public void onReceive(final Context context, final Intent intent) {
      int i;
      boolean bool;
      NetworkInfo networkInfo;
      WifiDirectAgent wifiDirectAgent;
      WifiP2pInfo wifiP2pInfo;
      WifiP2pGroup wifiP2pGroup;
      String str = intent.getAction();
      switch (str.hashCode()) {
        default:
          i = -1;
          break;
        case 1695662461:
          if (str.equals("android.net.wifi.p2p.STATE_CHANGED")) {
            i = 2;
            break;
          } 
        case 315025416:
          if (str.equals("android.net.wifi.p2p.PERSISTENT_GROUPS_CHANGED")) {
            i = 1;
            break;
          } 
        case -1331207498:
          if (str.equals("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE")) {
            i = 6;
            break;
          } 
        case -1394739139:
          if (str.equals("android.net.wifi.p2p.PEERS_CHANGED")) {
            i = 3;
            break;
          } 
        case -1566767901:
          if (str.equals("android.net.wifi.p2p.THIS_DEVICE_CHANGED")) {
            i = 5;
            break;
          } 
        case -1772632330:
          if (str.equals("android.net.wifi.p2p.CONNECTION_STATE_CHANGE")) {
            i = 4;
            break;
          } 
        case -1875733435:
          if (str.equals("android.net.wifi.WIFI_STATE_CHANGED")) {
            i = 0;
            break;
          } 
      } 
      switch (i) {
        case 6:
          if (intent.getIntExtra("discoveryState", 0) == 2) {
            bool = true;
          } else {
            bool = false;
          } 
          RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "p2p discoverPeers()=%s", new Object[] { Boolean.valueOf(bool) });
          break;
        case 5:
          dump((WifiP2pDevice)intent.getParcelableExtra("wifiP2pDevice"));
          break;
        case 4:
          networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
          wifiP2pInfo = (WifiP2pInfo)intent.getParcelableExtra("wifiP2pInfo");
          wifiP2pGroup = (WifiP2pGroup)intent.getParcelableExtra("p2pGroupInfo");
          WifiDirectAgent.this.wifiP2pState = networkInfo.getState();
          RobotLog.dd("NetDiscover_wifiDirectAgent_bcast", "connection changed: networkInfo.state=%s", new Object[] { networkInfo.getState() });
          dump(networkInfo);
          dump(wifiP2pInfo);
          dump(wifiP2pGroup);
          break;
        case 3:
          dump((WifiP2pDeviceList)intent.getParcelableExtra("wifiP2pDeviceList"));
          break;
        case 2:
          i = intent.getIntExtra("wifi_p2p_state", 0);
          wifiDirectAgent = WifiDirectAgent.this;
          if (i == 2) {
            bool = true;
          } else {
            bool = false;
          } 
          wifiDirectAgent.isWifiP2pEnabled = bool;
          RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "wifiP2pEnabled=%s", new Object[] { Boolean.valueOf(this.this$0.isWifiP2pEnabled) });
          break;
        case 1:
          RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "wifi direct remembered groups cleared");
          NetworkConnectionHandler.getInstance().sendCommand(new Command("CMD_NOTIFY_WIFI_DIRECT_REMEMBERED_GROUPS_CHANGED"));
          break;
        case 0:
          i = intent.getIntExtra("wifi_state", 0);
          WifiDirectAgent.this.wifiState = WifiState.from(i);
          RobotLog.vv("NetDiscover_wifiDirectAgent_bcast", "wifiState=%s", new Object[] { this.this$0.wifiState });
          break;
      } 
      WifiDirectAgent.this.callbacks.callbacksDo(new Consumer<WifiDirectAgent.Callback>() {
            public void accept(WifiDirectAgent.Callback param2Callback) {
              param2Callback.onReceive(context, intent);
            }
          });
    }
  }
  
  class null implements Consumer<Callback> {
    public void accept(WifiDirectAgent.Callback param1Callback) {
      param1Callback.onReceive(context, intent);
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectAgent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */