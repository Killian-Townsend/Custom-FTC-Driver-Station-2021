package com.qualcomm.robotcore.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.qualcomm.robotcore.hardware.configuration.LynxConstants;
import com.qualcomm.robotcore.robocol.Command;
import com.qualcomm.robotcore.util.RobotLog;
import java.lang.reflect.InvocationTargetException;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.network.ApChannel;
import org.firstinspires.ftc.robotcore.internal.network.ApChannelManager;
import org.firstinspires.ftc.robotcore.internal.network.ApChannelManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManager;
import org.firstinspires.ftc.robotcore.internal.network.DeviceNameManagerFactory;
import org.firstinspires.ftc.robotcore.internal.network.InvalidNetworkSettingException;
import org.firstinspires.ftc.robotcore.internal.network.NetworkConnectionHandler;
import org.firstinspires.ftc.robotcore.internal.network.PasswordManager;
import org.firstinspires.ftc.robotcore.internal.network.PasswordManagerFactory;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class RobotControllerAccessPointAssistant extends AccessPointAssistant {
  private static final String TAG = "RobotControllerAccessPointAssistant";
  
  public static final int WIFI_AP_STATE_DISABLED = 11;
  
  public static final int WIFI_AP_STATE_ENABLED = 13;
  
  private static RobotControllerAccessPointAssistant robotControllerAccessPointAssistant;
  
  private ApChannelManager apChannelManager = ApChannelManagerFactory.getInstance();
  
  private NetworkConnection.ConnectStatus connectStatus;
  
  private final Object enableDisableLock = new Object();
  
  private IntentFilter intentFilter;
  
  private DeviceNameManager nameManager = DeviceNameManagerFactory.getInstance();
  
  private PasswordManager passwordManager = PasswordManagerFactory.getInstance();
  
  private BroadcastReceiver receiver;
  
  private RobotControllerAccessPointAssistant(Context paramContext) {
    super(paramContext);
    IntentFilter intentFilter = new IntentFilter();
    this.intentFilter = intentFilter;
    intentFilter.addAction("android.net.wifi.WIFI_AP_STATE_CHANGED");
    this.intentFilter.addAction("org.firstinspires.ftc.intent.action.FTC_FACTORY_RESET");
    this.intentFilter.addAction("org.firstinspires.ftc.intent.action.FTC_AP_NOTIFY_BAND_CHANGE");
  }
  
  public static RobotControllerAccessPointAssistant getRobotControllerAccessPointAssistant(Context paramContext) {
    // Byte code:
    //   0: ldc com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant
    //   2: monitorenter
    //   3: getstatic com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant.robotControllerAccessPointAssistant : Lcom/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant;
    //   6: ifnonnull -> 20
    //   9: new com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Landroid/content/Context;)V
    //   17: putstatic com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant.robotControllerAccessPointAssistant : Lcom/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant;
    //   20: getstatic com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant.robotControllerAccessPointAssistant : Lcom/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant;
    //   23: astore_0
    //   24: ldc com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/qualcomm/robotcore/wifi/RobotControllerAccessPointAssistant
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  private void handleBandChangeViaButton(Intent paramIntent) {
    int i = paramIntent.getIntExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_BAND", -1);
    if (i == 0) {
      RobotLog.ii("RobotControllerAccessPointAssistant", "Received notification that the band has been switched to 2.4 GHz");
    } else if (i == 1) {
      RobotLog.ii("RobotControllerAccessPointAssistant", "Received notification that the band has been switched to 5 GHz");
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Received band switch notification with invalid band ");
      stringBuilder.append(i);
      RobotLog.ww("RobotControllerAccessPointAssistant", stringBuilder.toString());
    } 
    Command command = new Command("CMD_VISUALLY_CONFIRM_WIFI_BAND_SWITCH", Integer.toString(i));
    NetworkConnectionHandler.getInstance().injectReceivedCommand(command);
  }
  
  private void handleFactoryReset() {
    RobotLog.ww("RobotControllerAccessPointAssistant", "Received request to do access point factory reset");
    AppUtil.getInstance().showToast(UILocation.BOTH, "Resetting access point to default name and password", 1);
    NetworkConnectionHandler.getInstance().injectReceivedCommand(new Command("CMD_VISUALLY_CONFIRM_WIFI_RESET"));
    try {
      Thread.sleep(400L);
    } catch (InterruptedException interruptedException) {
      Thread.currentThread().interrupt();
    } 
    String str1 = this.nameManager.resetDeviceName(false);
    String str2 = this.passwordManager.resetPassword(false);
    ApChannel apChannel = this.apChannelManager.resetChannel(false);
    try {
      setNetworkSettings(str1, str2, apChannel);
      return;
    } catch (InvalidNetworkSettingException invalidNetworkSettingException) {
      RobotLog.ee("RobotControllerAccessPointAssistant", (Throwable)invalidNetworkSettingException, "Default name, password, or channel rejected during reset attempt");
      return;
    } 
  }
  
  private void handleWifiStateChange(Intent paramIntent) {
    int i = paramIntent.getIntExtra("wifi_state", 0);
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Wifi state change:, wifiApState: ");
    stringBuilder.append(i);
    RobotLog.ii("RobotControllerAccessPointAssistant", stringBuilder.toString());
    if (i == 11) {
      this.connectStatus = NetworkConnection.ConnectStatus.NOT_CONNECTED;
      sendEvent(NetworkConnection.NetworkEvent.DISCONNECTED);
      return;
    } 
    if (i == 13) {
      this.connectStatus = NetworkConnection.ConnectStatus.CONNECTED;
      sendEvent(NetworkConnection.NetworkEvent.CONNECTION_INFO_AVAILABLE);
    } 
  }
  
  public void createConnection() {
    RobotLog.ii("RobotControllerAccessPointAssistant", "Sending SSID and password to AP service");
    try {
      setNetworkSettings(this.nameManager.getDeviceName(), this.passwordManager.getPassword(), (ApChannel)null);
      return;
    } catch (InvalidNetworkSettingException invalidNetworkSettingException) {
      RobotLog.ee("RobotControllerAccessPointAssistant", (Throwable)invalidNetworkSettingException, "Currently stored name or password is now being rejected");
      return;
    } 
  }
  
  public void detectWifiReset() {
    RobotLog.dd("RobotControllerAccessPointAssistant", "detectWifiReset button=%b", new Object[] { Boolean.valueOf(AndroidBoard.getInstance().getUserButtonPin().getState()) });
    if (LynxConstants.isRevControlHub() && AndroidBoard.getInstance().getUserButtonPin().getState()) {
      RobotLog.ii("RobotControllerAccessPointAssistant", "Wifi settings reset requested through the Control Hub button");
      Intent intent = new Intent("org.firstinspires.ftc.intent.action.FTC_FACTORY_RESET");
      this.context.sendBroadcast(intent);
    } 
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
        RobotLog.ii("RobotControllerAccessPointAssistant", "Enabling network services");
        this.receiver = new BroadcastReceiver() {
            public void onReceive(Context param1Context, Intent param1Intent) {
              if (param1Intent.getAction().equals("android.net.wifi.WIFI_AP_STATE_CHANGED")) {
                RobotControllerAccessPointAssistant.this.handleWifiStateChange(param1Intent);
                return;
              } 
              if (param1Intent.getAction().equals("org.firstinspires.ftc.intent.action.FTC_FACTORY_RESET")) {
                RobotControllerAccessPointAssistant.this.handleFactoryReset();
                return;
              } 
              if (param1Intent.getAction().equals("org.firstinspires.ftc.intent.action.FTC_AP_NOTIFY_BAND_CHANGE"))
                RobotControllerAccessPointAssistant.this.handleBandChangeViaButton(param1Intent); 
            }
          };
        this.context.registerReceiver(this.receiver, this.intentFilter);
      } 
      return;
    } 
  }
  
  public NetworkConnection.ConnectStatus getConnectStatus() {
    if (isWifiApEnabled() == true) {
      RobotLog.ii("RobotControllerAccessPointAssistant", "Wifi AP is enabled");
      return NetworkConnection.ConnectStatus.CONNECTED;
    } 
    RobotLog.ii("RobotControllerAccessPointAssistant", "Wifi AP is not enabled");
    return NetworkConnection.ConnectStatus.NOT_CONNECTED;
  }
  
  public String getConnectionOwnerName() {
    return this.nameManager.getDeviceName();
  }
  
  protected String getIpAddress() {
    return getConnectionOwnerAddress().getHostAddress();
  }
  
  public NetworkType getNetworkType() {
    return NetworkType.RCWIRELESSAP;
  }
  
  public String getPassphrase() {
    return this.passwordManager.getPassword();
  }
  
  public boolean isConnected() {
    return (getConnectStatus() == NetworkConnection.ConnectStatus.CONNECTED);
  }
  
  protected boolean isWifiApEnabled() {
    try {
      return ((Boolean)this.wifiManager.getClass().getMethod("isWifiApEnabled", new Class[0]).invoke(this.wifiManager, new Object[0])).booleanValue();
    } catch (NoSuchMethodException noSuchMethodException) {
    
    } catch (IllegalAccessException illegalAccessException) {
    
    } catch (InvocationTargetException invocationTargetException) {}
    invocationTargetException.printStackTrace();
    return false;
  }
  
  public void onWaitForConnection() {
    createConnection();
  }
  
  public void setNetworkSettings(String paramString1, String paramString2, ApChannel paramApChannel) throws InvalidNetworkSettingException {
    int i = AndroidBoard.getInstance().supportsBulkNetworkSettings() ^ true;
    RobotLog.dd("RobotControllerAccessPointAssistant", "setNetworkProperties(deviceName=%s, password=%s, ApChannel=%s) sendSettingsIndividually=%b", new Object[] { paramString1, paramString2, paramApChannel, Boolean.valueOf(i) });
    if (paramString1 != null)
      this.nameManager.setDeviceName(paramString1, i); 
    if (paramString2 != null)
      this.passwordManager.setPassword(paramString2, i); 
    if (paramApChannel != null)
      this.apChannelManager.setChannel(paramApChannel, i); 
    if (i == 0) {
      Intent intent = new Intent("org.firstinspires.ftc.intent.action.FTC_AP_SETTINGS_CHANGE");
      intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_NAME", paramString1);
      intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_PASSWORD", paramString2);
      if (paramApChannel != null && paramApChannel != ApChannel.UNKNOWN) {
        intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_BAND", paramApChannel.band.androidInternalValue);
        intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_CHANNEL", paramApChannel.channelNum);
      } 
      RobotLog.dd("RobotControllerAccessPointAssistant", "Sending bulk settings broadcast intent");
      AppUtil.getDefContext().sendBroadcast(intent);
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcore\wifi\RobotControllerAccessPointAssistant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */