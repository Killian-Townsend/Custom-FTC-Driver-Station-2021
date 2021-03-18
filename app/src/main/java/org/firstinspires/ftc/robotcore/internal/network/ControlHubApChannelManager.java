package org.firstinspires.ftc.robotcore.internal.network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.ResultReceiver;
import com.qualcomm.robotcore.util.RobotLog;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.firstinspires.ftc.robotcore.internal.hardware.android.AndroidBoard;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.robotcore.internal.system.SynchronousResultReceiver;

public final class ControlHubApChannelManager implements ApChannelManager {
  private static final boolean DEBUG = false;
  
  private static final ApChannel FACTORY_DEFAULT_AP_CHANNEL = ApChannel.AUTO_5_GHZ;
  
  private static final String TAG = "ControlHubApChannelManager";
  
  private final ChannelResultReceiver channelResultReceiver = new ChannelResultReceiver();
  
  private final Context context = AppUtil.getDefContext();
  
  private EnumSet<ApChannel> supportedChannels = null;
  
  private void setChannelViaBulkSettingsApi(ApChannel paramApChannel) throws InvalidNetworkSettingException {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Setting channel via bulk WiFi settings API: ");
    stringBuilder.append(paramApChannel.getDisplayName());
    RobotLog.vv("ControlHubApChannelManager", stringBuilder.toString());
    NetworkConnectionHandler.getInstance().getNetworkConnection().setNetworkSettings(null, null, paramApChannel);
  }
  
  private void setChannelViaLegacyApi(int paramInt) {
    RobotLog.vv("ControlHubApChannelManager", "Sending ap channel change intent");
    Intent intent = new Intent("org.firstinspires.ftc.intent.action.FTC_AP_CHANNEL_CHANGE");
    intent.putExtra("org.firstinspires.ftc.intent.extra.EXTRA_AP_PREF", paramInt);
    this.context.sendBroadcast(intent);
  }
  
  public ApChannel getCurrentChannel() {
    if (AndroidBoard.getInstance().supportsGetChannelInfoIntent()) {
      Intent intent = new Intent("org.firstinspires.ftc.intent.action.FTC_AP_GET_CURRENT_CHANNEL_INFO");
      intent.putExtra("org.firstinspires.ftc.intent.extra.RESULT_RECEIVER", (Parcelable)AppUtil.wrapResultReceiverForIpc((ResultReceiver)this.channelResultReceiver));
      AppUtil.getDefContext().sendBroadcast(intent);
      try {
        return (ApChannel)this.channelResultReceiver.awaitResult(1L, TimeUnit.SECONDS);
      } catch (InterruptedException interruptedException) {
        RobotLog.ee("ControlHubApChannelManager", "Thread interrupted while getting current channel from AP service");
        Thread.currentThread().interrupt();
      } catch (TimeoutException timeoutException) {
        RobotLog.ee("ControlHubApChannelManager", "Timeout while getting current channel from AP service");
      } 
    } 
    return ApChannel.UNKNOWN;
  }
  
  public Set<ApChannel> getSupportedChannels() {
    if (this.supportedChannels == null) {
      EnumSet<ApChannel> enumSet = EnumSet.copyOf(ApChannel.ALL_2_4_GHZ_CHANNELS);
      enumSet.add(ApChannel.AUTO_2_4_GHZ);
      if (AndroidBoard.getInstance().supports5GhzAp()) {
        enumSet.addAll(ApChannel.NON_DFS_5_GHZ_CHANNELS);
        if (AndroidBoard.getInstance().supports5GhzAutoSelection())
          enumSet.add(ApChannel.AUTO_5_GHZ); 
      } 
      this.supportedChannels = enumSet;
    } 
    return this.supportedChannels;
  }
  
  public ApChannel resetChannel(boolean paramBoolean) {
    ApChannel apChannel = FACTORY_DEFAULT_AP_CHANNEL;
    try {
      setChannel(apChannel, paramBoolean);
      return apChannel;
    } catch (InvalidNetworkSettingException invalidNetworkSettingException) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append("Unable to reset channel to ");
      stringBuilder.append(apChannel);
      RobotLog.ee("ControlHubApChannelManager", invalidNetworkSettingException, stringBuilder.toString());
      return apChannel;
    } 
  }
  
  public void setChannel(ApChannel paramApChannel, boolean paramBoolean) throws InvalidNetworkSettingException {
    if (getSupportedChannels().contains(paramApChannel)) {
      if (paramBoolean) {
        if (AndroidBoard.getInstance().supportsBulkNetworkSettings()) {
          setChannelViaBulkSettingsApi(paramApChannel);
          return;
        } 
        setChannelViaLegacyApi(paramApChannel.channelNum);
      } 
      return;
    } 
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("This device does not support channel ");
    stringBuilder.append(paramApChannel);
    throw new InvalidNetworkSettingException(stringBuilder.toString());
  }
  
  private static class ChannelResultReceiver extends SynchronousResultReceiver<ApChannel> {
    public ChannelResultReceiver() {
      super(3, "ControlHubApChannelManager", new Handler(Looper.getMainLooper()));
    }
    
    protected ApChannel provideResult(int param1Int, Bundle param1Bundle) {
      return ApChannel.fromBandAndChannel(param1Bundle.getInt("current_band"), param1Bundle.getInt("current_channel"));
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\network\ControlHubApChannelManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */