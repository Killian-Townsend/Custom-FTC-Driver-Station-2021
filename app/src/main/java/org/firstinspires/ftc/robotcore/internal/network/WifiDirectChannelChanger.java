package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import com.qualcomm.robotcore.R;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.PreferencesHelper;
import org.firstinspires.ftc.robotcore.internal.ui.UILocation;

public class WifiDirectChannelChanger {
  public static final String TAG = "WifiDirectChannelChanger";
  
  private int channel = 0;
  
  private Context context = AppUtil.getDefContext();
  
  private volatile boolean isChangingChannels = false;
  
  private int listenChannel = 0;
  
  private PreferencesHelper preferencesHelper = new PreferencesHelper("WifiDirectChannelChanger", this.context);
  
  private WifiDirectAgent wifiDirectAgent = WifiDirectAgent.getInstance();
  
  private void finishChannelChange(boolean paramBoolean) {
    RobotLog.vv("WifiDirectChannelChanger", "finishChannelChange() channel=%d success=%s", new Object[] { Integer.valueOf(this.channel), Boolean.valueOf(paramBoolean) });
    if (paramBoolean) {
      issueSuccessToast();
    } else {
      issueFailureToast();
    } 
    this.isChangingChannels = false;
  }
  
  private void issueFailureToast() {
    AppUtil.getInstance().showToast(UILocation.BOTH, this.context.getString(R.string.setWifiChannelFailure, new Object[] { WifiDirectChannelAndDescription.getDescription(this.channel) }), 1);
  }
  
  private void issueSuccessToast() {
    AppUtil.getInstance().showToast(UILocation.BOTH, this.context.getString(R.string.setWifiChannelSuccess, new Object[] { WifiDirectChannelAndDescription.getDescription(this.channel) }), 1);
  }
  
  private void startChannelChange(int paramInt) {
    RobotLog.vv("WifiDirectChannelChanger", "startChannelChange() channel=%d", new Object[] { Integer.valueOf(paramInt) });
    this.isChangingChannels = true;
    this.channel = paramInt;
  }
  
  public void changeToChannel(int paramInt) {
    RobotLog.vv("WifiDirectChannelChanger", "changeToChannel() channel=%d", new Object[] { Integer.valueOf(paramInt) });
    startChannelChange(paramInt);
    AppUtil.getInstance().runOnUiThread(new Runnable() {
          public void run() {
            if (WifiDirectChannelChanger.this.channel > 11) {
              WifiDirectChannelChanger.access$102(WifiDirectChannelChanger.this, 0);
            } else {
              WifiDirectChannelChanger wifiDirectChannelChanger = WifiDirectChannelChanger.this;
              WifiDirectChannelChanger.access$102(wifiDirectChannelChanger, wifiDirectChannelChanger.channel);
            } 
            WifiDirectChannelChanger.this.wifiDirectAgent.setWifiP2pChannels(WifiDirectChannelChanger.this.listenChannel, WifiDirectChannelChanger.this.channel, new WifiP2pManager.ActionListener() {
                  public void onFailure(int param2Int) {
                    if (param2Int != 0) {
                      if (param2Int != 1) {
                        if (param2Int != 2) {
                          RobotLog.vv("WifiDirectChannelChanger", "callSetWifiP2pChannels() failure (unknown reason)");
                        } else {
                          RobotLog.vv("WifiDirectChannelChanger", "callSetWifiP2pChannels() failure (BUSY)");
                        } 
                      } else {
                        RobotLog.vv("WifiDirectChannelChanger", "callSetWifiP2pChannels() failure (P2P_UNSUPPORTED)");
                      } 
                    } else {
                      RobotLog.vv("WifiDirectChannelChanger", "callSetWifiP2pChannels() failure (ERROR)");
                    } 
                    WifiDirectChannelChanger.this.finishChannelChange(false);
                  }
                  
                  public void onSuccess() {
                    RobotLog.vv("WifiDirectChannelChanger", "callSetWifiP2pChannels() success");
                    WifiDirectChannelChanger.this.preferencesHelper.writePrefIfDifferent(WifiDirectChannelChanger.this.context.getString(R.string.pref_wifip2p_channel), Integer.valueOf(WifiDirectChannelChanger.this.channel));
                    RobotLog.vv("WifiDirectChannelChanger", "Channel %d saved as preference \"pref_wifip2p_channel\".", new Object[] { Integer.valueOf(WifiDirectChannelChanger.access$000(this.this$1.this$0)) });
                    WifiDirectChannelChanger.this.finishChannelChange(true);
                  }
                });
          }
        });
  }
  
  public boolean isBusy() {
    return this.isChangingChannels;
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\WifiDirectChannelChanger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */